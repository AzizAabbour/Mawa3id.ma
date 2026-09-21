package ma.mawa3id.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.mawa3id.domain.enums.AppointmentStatus;
import ma.mawa3id.dto.appointment.AppointmentRequest;
import ma.mawa3id.dto.appointment.AppointmentResponse;
import ma.mawa3id.dto.appointment.AppointmentStatusUpdateRequest;
import ma.mawa3id.service.AppointmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "Rendez-vous", description = "Gestion des réservations, statuts et calendrier")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping
    @Operation(summary = "Lister les rendez-vous avec filtres de statut, employé et date")
    public ResponseEntity<Page<AppointmentResponse>> getAppointments(
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(appointmentService.getAppointments(status, employeeId, startDate, endDate, pageable));
    }

    @GetMapping("/calendar")
    @Operation(summary = "Obtenir les rendez-vous pour une plage de dates (Vue Calendrier)")
    public ResponseEntity<List<AppointmentResponse>> getCalendarAppointments(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(appointmentService.getAppointmentsForCalendar(startDate, endDate));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter le détail d'un rendez-vous")
    public ResponseEntity<AppointmentResponse> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau rendez-vous depuis le tableau de bord")
    public ResponseEntity<AppointmentResponse> createAppointment(@Valid @RequestBody AppointmentRequest request) {
        return new ResponseEntity<>(appointmentService.createAppointment(request), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Changer le statut d'un rendez-vous (CONFIRMED, CANCELLED, COMPLETED, NO_SHOW)")
    public ResponseEntity<AppointmentResponse> updateStatus(
            @PathVariable Long id, @Valid @RequestBody AppointmentStatusUpdateRequest request) {
        return ResponseEntity.ok(appointmentService.updateStatus(id, request));
    }
}
