package ma.mawa3id.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.mawa3id.dto.workinghour.WorkingHourDto;
import ma.mawa3id.service.WorkingHourService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/working-hours")
@RequiredArgsConstructor
@Tag(name = "Horaires d'ouverture", description = "Configuration des plages de travail et pauses")
public class WorkingHourController {

    private final WorkingHourService workingHourService;

    @GetMapping
    @Operation(summary = "Obtenir les horaires d'ouverture de l'entreprise ou d'un collaborateur")
    public ResponseEntity<List<WorkingHourDto>> getWorkingHours(@RequestParam(required = false) Long employeeId) {
        return ResponseEntity.ok(workingHourService.getWorkingHours(employeeId));
    }

    @PostMapping
    @Operation(summary = "Enregistrer les horaires d'ouverture")
    public ResponseEntity<List<WorkingHourDto>> saveWorkingHours(@Valid @RequestBody List<WorkingHourDto> dtoList) {
        return ResponseEntity.ok(workingHourService.saveWorkingHours(dtoList));
    }
}
