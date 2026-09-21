package ma.mawa3id.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.mawa3id.dto.appointment.AppointmentResponse;
import ma.mawa3id.dto.appointment.AvailableSlotDto;
import ma.mawa3id.dto.appointment.PublicBookingRequest;
import ma.mawa3id.dto.business.BusinessPublicResponse;
import ma.mawa3id.service.PublicBookingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
@Tag(name = "Réservation Publique (Clients)", description = "Prise de rendez-vous client directe sans inscription")
public class PublicBookingController {

    private final PublicBookingService publicBookingService;

    @GetMapping("/{slug}")
    @Operation(summary = "Consulter les informations publiques et prestations d'un établissement")
    public ResponseEntity<BusinessPublicResponse> getBusinessBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(publicBookingService.getBusinessBySlug(slug));
    }

    @GetMapping("/{slug}/slots")
    @Operation(summary = "Consulter les créneaux disponibles pour une prestation et date données")
    public ResponseEntity<List<AvailableSlotDto>> getAvailableSlots(
            @PathVariable String slug,
            @RequestParam Long serviceId,
            @RequestParam(required = false) Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(publicBookingService.getAvailableSlots(slug, serviceId, employeeId, date));
    }

    @PostMapping("/{slug}")
    @Operation(summary = "Confirmer une réservation directe sans compte client préalable")
    public ResponseEntity<AppointmentResponse> bookPublicAppointment(
            @PathVariable String slug,
            @Valid @RequestBody PublicBookingRequest request) {
        return new ResponseEntity<>(publicBookingService.bookPublicAppointment(slug, request), HttpStatus.CREATED);
    }
}
