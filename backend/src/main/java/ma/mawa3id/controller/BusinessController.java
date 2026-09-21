package ma.mawa3id.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.mawa3id.dto.business.BusinessResponse;
import ma.mawa3id.dto.business.BusinessUpdateRequest;
import ma.mawa3id.service.BusinessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/business")
@RequiredArgsConstructor
@Tag(name = "Entreprise (Workspace)", description = "Gestion du profil de l'établissement")
public class BusinessController {

    private final BusinessService businessService;

    @GetMapping
    @Operation(summary = "Obtenir le profil de l'entreprise connectée")
    public ResponseEntity<BusinessResponse> getCurrentBusiness() {
        return ResponseEntity.ok(businessService.getCurrentBusiness());
    }

    @PutMapping
    @Operation(summary = "Mettre à jour les informations de l'entreprise")
    public ResponseEntity<BusinessResponse> updateBusiness(@Valid @RequestBody BusinessUpdateRequest request) {
        return ResponseEntity.ok(businessService.updateBusiness(request));
    }
}
