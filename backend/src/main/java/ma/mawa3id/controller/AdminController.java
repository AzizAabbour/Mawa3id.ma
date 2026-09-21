package ma.mawa3id.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.mawa3id.dto.business.BusinessResponse;
import ma.mawa3id.dto.dashboard.AdminDashboardStatsDto;
import ma.mawa3id.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Super Administration", description = "Gestion globale de la plateforme Mawa3id.ma (MRR, modération)")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/statistics")
    @Operation(summary = "Statistiques globales de la plateforme (MRR, Total Entreprises, SMS & WhatsApp)")
    public ResponseEntity<AdminDashboardStatsDto> getPlatformStats() {
        return ResponseEntity.ok(adminService.getPlatformStats());
    }

    @GetMapping("/businesses")
    @Operation(summary = "Lister toutes les entreprises inscrites sur la plateforme")
    public ResponseEntity<Page<BusinessResponse>> getAllBusinesses(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(adminService.getAllBusinesses(pageable));
    }

    @PatchMapping("/businesses/{id}/status")
    @Operation(summary = "Suspendre ou activer une entreprise")
    public ResponseEntity<BusinessResponse> toggleBusinessStatus(
            @PathVariable Long id, @RequestBody Map<String, Boolean> request) {
        boolean active = request.getOrDefault("active", true);
        return ResponseEntity.ok(adminService.toggleBusinessStatus(id, active));
    }
}
