package ma.mawa3id.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.mawa3id.dto.notification.*;
import ma.mawa3id.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Gestion des alertes WhatsApp & SMS, journaux et modèles personnalisés")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "Historique et journaux des notifications envoyées")
    public ResponseEntity<Page<NotificationResponse>> getNotifications(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(notificationService.getNotifications(pageable));
    }

    @GetMapping("/templates")
    @Operation(summary = "Lister les modèles de messages configurés")
    public ResponseEntity<List<NotificationTemplateResponse>> getTemplates() {
        return ResponseEntity.ok(notificationService.getTemplates());
    }

    @PostMapping("/templates")
    @Operation(summary = "Enregistrer un modèle de message WhatsApp / SMS personnalisé")
    public ResponseEntity<NotificationTemplateResponse> saveTemplate(
            @Valid @RequestBody NotificationTemplateRequest request) {
        return ResponseEntity.ok(notificationService.saveTemplate(request));
    }

    @PostMapping("/templates/preview")
    @Operation(summary = "Prévisualiser le rendu d'un modèle avec variables dynamiques")
    public ResponseEntity<Map<String, String>> previewTemplate(
            @Valid @RequestBody TemplatePreviewRequest request) {
        String rendered = notificationService.previewTemplate(request);
        return ResponseEntity.ok(Map.of("renderedContent", rendered));
    }

    @GetMapping("/config")
    @Operation(summary = "Consulter les canaux de notifications activés (WhatsApp, SMS, Email)")
    public ResponseEntity<NotificationConfigDto> getConfig() {
        return ResponseEntity.ok(notificationService.getConfig());
    }

    @PutMapping("/config")
    @Operation(summary = "Mettre à jour les paramètres de notifications")
    public ResponseEntity<NotificationConfigDto> updateConfig(@Valid @RequestBody NotificationConfigDto dto) {
        return ResponseEntity.ok(notificationService.updateConfig(dto));
    }
}
