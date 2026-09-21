package ma.mawa3id.service;

import lombok.RequiredArgsConstructor;
import ma.mawa3id.domain.entity.Business;
import ma.mawa3id.dto.business.BusinessResponse;
import ma.mawa3id.dto.business.BusinessUpdateRequest;
import ma.mawa3id.exception.ResourceNotFoundException;
import ma.mawa3id.mapper.BusinessMapper;
import ma.mawa3id.repository.BusinessRepository;
import ma.mawa3id.tenant.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final BusinessMapper businessMapper;

    @Transactional(readOnly = true)
    public BusinessResponse getCurrentBusiness() {
        Long businessId = TenantContext.getCurrentBusinessId();
        if (businessId == null) {
            throw new ResourceNotFoundException("Aucune entreprise associée à la session actuelle");
        }
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Entreprise", "id", businessId));
        return businessMapper.toResponse(business);
    }

    @Transactional
    public BusinessResponse updateBusiness(BusinessUpdateRequest request) {
        Long businessId = TenantContext.getCurrentBusinessId();
        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Entreprise", "id", businessId));

        businessMapper.updateEntityFromDto(request, business);
        return businessMapper.toResponse(businessRepository.save(business));
    }
}
