package ma.mawa3id.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.mawa3id.dto.dashboard.ChartDataPointDto;
import ma.mawa3id.dto.dashboard.DashboardStatisticsDto;
import ma.mawa3id.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Tableau de bord & Statistiques", description = "Indicateurs clés, rendez-vous du jour, taux de no-show et revenus")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/statistics")
    @Operation(summary = "Obtenir les statistiques synthétiques du tableau de bord")
    public ResponseEntity<DashboardStatisticsDto> getStatistics() {
        return ResponseEntity.ok(dashboardService.getStatistics());
    }

    @GetMapping("/charts/appointments")
    @Operation(summary = "Données du graphique des rendez-vous par jour")
    public ResponseEntity<List<ChartDataPointDto>> getAppointmentsChart() {
        return ResponseEntity.ok(dashboardService.getWeeklyAppointmentsChart());
    }

    @GetMapping("/charts/revenue")
    @Operation(summary = "Données du graphique de chiffre d'affaires mensuel en MAD")
    public ResponseEntity<List<ChartDataPointDto>> getRevenueChart() {
        return ResponseEntity.ok(dashboardService.getMonthlyRevenueChart());
    }
}
