package ma.mawa3id.service;

import lombok.RequiredArgsConstructor;
import ma.mawa3id.domain.entity.Employee;
import ma.mawa3id.domain.entity.Service;
import ma.mawa3id.dto.service.ServiceRequest;
import ma.mawa3id.dto.service.ServiceResponse;
import ma.mawa3id.exception.ResourceNotFoundException;
import ma.mawa3id.mapper.ServiceMapper;
import ma.mawa3id.repository.EmployeeRepository;
import ma.mawa3id.repository.ServiceRepository;
import ma.mawa3id.tenant.TenantContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final EmployeeRepository employeeRepository;
    private final ServiceMapper serviceMapper;

    @Transactional(readOnly = true)
    public List<ServiceResponse> getServices() {
        Long businessId = TenantContext.getCurrentBusinessId();
        List<Service> services = serviceRepository.findByBusinessId(businessId);
        return serviceMapper.toResponseList(services);
    }

    @Transactional(readOnly = true)
    public ServiceResponse getServiceById(Long id) {
        Long businessId = TenantContext.getCurrentBusinessId();
        Service service = serviceRepository.findById(id)
                .filter(s -> s.getBusinessId().equals(businessId))
                .orElseThrow(() -> new ResourceNotFoundException("Prestation", "id", id));
        return serviceMapper.toResponse(service);
    }

    @Transactional
    public ServiceResponse createService(ServiceRequest request) {
        Long businessId = TenantContext.getCurrentBusinessId();
        Service service = serviceMapper.toEntity(request);
        service.setBusinessId(businessId);

        if (request.getEmployeeIds() != null && !request.getEmployeeIds().isEmpty()) {
            List<Employee> employees = employeeRepository.findAllById(request.getEmployeeIds());
            service.setAssignedEmployees(new HashSet<>(employees));
        }

        return serviceMapper.toResponse(serviceRepository.save(service));
    }

    @Transactional
    public ServiceResponse updateService(Long id, ServiceRequest request) {
        Long businessId = TenantContext.getCurrentBusinessId();
        Service service = serviceRepository.findById(id)
                .filter(s -> s.getBusinessId().equals(businessId))
                .orElseThrow(() -> new ResourceNotFoundException("Prestation", "id", id));

        serviceMapper.updateEntityFromDto(request, service);

        if (request.getEmployeeIds() != null) {
            List<Employee> employees = employeeRepository.findAllById(request.getEmployeeIds());
            service.setAssignedEmployees(new HashSet<>(employees));
        }

        return serviceMapper.toResponse(serviceRepository.save(service));
    }

    @Transactional
    public void deleteService(Long id) {
        Long businessId = TenantContext.getCurrentBusinessId();
        Service service = serviceRepository.findById(id)
                .filter(s -> s.getBusinessId().equals(businessId))
                .orElseThrow(() -> new ResourceNotFoundException("Prestation", "id", id));

        service.setActive(false);
        serviceRepository.save(service);
    }
}
