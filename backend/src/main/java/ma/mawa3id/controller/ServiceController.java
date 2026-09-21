package ma.mawa3id.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.mawa3id.dto.service.ServiceRequest;
import ma.mawa3id.dto.service.ServiceResponse;
import ma.mawa3id.service.ServiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
@Tag(name = "Prestations & Services", description = "Gestion du catalogue de prestations et tarifs en MAD")
public class ServiceController {

    private final ServiceService serviceService;

    @GetMapping
    @Operation(summary = "Lister toutes les prestations de l'établissement")
    public ResponseEntity<List<ServiceResponse>> getServices() {
        return ResponseEntity.ok(serviceService.getServices());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter le détail d'une prestation")
    public ResponseEntity<ServiceResponse> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.getServiceById(id));
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle prestation")
    public ResponseEntity<ServiceResponse> createService(@Valid @RequestBody ServiceRequest request) {
        return new ResponseEntity<>(serviceService.createService(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une prestation existante")
    public ResponseEntity<ServiceResponse> updateService(
            @PathVariable Long id, @Valid @RequestBody ServiceRequest request) {
        return ResponseEntity.ok(serviceService.updateService(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Désactiver une prestation")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}
