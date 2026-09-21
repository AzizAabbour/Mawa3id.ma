package ma.mawa3id.service;

import lombok.RequiredArgsConstructor;
import ma.mawa3id.domain.entity.Customer;
import ma.mawa3id.dto.customer.CustomerRequest;
import ma.mawa3id.dto.customer.CustomerResponse;
import ma.mawa3id.exception.DuplicateResourceException;
import ma.mawa3id.exception.ResourceNotFoundException;
import ma.mawa3id.mapper.CustomerMapper;
import ma.mawa3id.repository.CustomerRepository;
import ma.mawa3id.tenant.TenantContext;
import ma.mawa3id.validation.MoroccanPhoneValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Transactional(readOnly = true)
    public Page<CustomerResponse> searchCustomers(String query, Pageable pageable) {
        Long businessId = TenantContext.getCurrentBusinessId();
        return customerRepository.searchCustomers(businessId, query, pageable)
                .map(customerMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id) {
        Long businessId = TenantContext.getCurrentBusinessId();
        Customer customer = customerRepository.findById(id)
                .filter(c -> c.getBusinessId().equals(businessId))
                .orElseThrow(() -> new ResourceNotFoundException("Client", "id", id));
        return customerMapper.toResponse(customer);
    }

    @Transactional
    public CustomerResponse createCustomer(CustomerRequest request) {
        Long businessId = TenantContext.getCurrentBusinessId();
        String normalizedPhone = MoroccanPhoneValidator.normalize(request.getPhone());

        if (customerRepository.findByBusinessIdAndPhone(businessId, normalizedPhone).isPresent()) {
            throw new DuplicateResourceException("Un client existe déjà avec ce numéro de téléphone dans votre établissement");
        }

        Customer customer = customerMapper.toEntity(request);
        customer.setBusinessId(businessId);
        customer.setPhone(normalizedPhone);
        if (request.getWhatsappNumber() != null) {
            customer.setWhatsappNumber(MoroccanPhoneValidator.normalize(request.getWhatsappNumber()));
        } else {
            customer.setWhatsappNumber(normalizedPhone);
        }

        return customerMapper.toResponse(customerRepository.save(customer));
    }

    @Transactional
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        Long businessId = TenantContext.getCurrentBusinessId();
        Customer customer = customerRepository.findById(id)
                .filter(c -> c.getBusinessId().equals(businessId))
                .orElseThrow(() -> new ResourceNotFoundException("Client", "id", id));

        customerMapper.updateEntityFromDto(request, customer);
        customer.setPhone(MoroccanPhoneValidator.normalize(request.getPhone()));
        if (request.getWhatsappNumber() != null) {
            customer.setWhatsappNumber(MoroccanPhoneValidator.normalize(request.getWhatsappNumber()));
        }

        return customerMapper.toResponse(customerRepository.save(customer));
    }
}
