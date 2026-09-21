package ma.mawa3id.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.mawa3id.dto.subscription.SubscriptionPlanResponse;
import ma.mawa3id.dto.subscription.SubscriptionResponse;
import ma.mawa3id.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subscription")
@RequiredArgsConstructor
@Tag(name = "Abonnement & Facturation", description = "Gestion des forfaits SaaS et paiements en MAD")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/plans")
    @Operation(summary = "Lister tous les forfaits disponibles (Free, Starter, Business, Pro)")
    public ResponseEntity<List<SubscriptionPlanResponse>> getPlans() {
        return ResponseEntity.ok(subscriptionService.getPlans());
    }

    @GetMapping
    @Operation(summary = "Consulter l'état de l'abonnement et quotas de l'entreprise")
    public ResponseEntity<SubscriptionResponse> getCurrentSubscription() {
        return ResponseEntity.ok(subscriptionService.getCurrentSubscription());
    }

    @PostMapping("/upgrade")
    @Operation(summary = "Changer ou renouveler le forfait de l'entreprise (Paiement MAD)")
    public ResponseEntity<SubscriptionResponse> upgradeSubscription(
            @RequestBody Map<String, String> request) {
        String planSlug = request.get("planSlug");
        String paymentMethod = request.getOrDefault("paymentMethod", "CARD");
        return ResponseEntity.ok(subscriptionService.upgradeSubscription(planSlug, paymentMethod));
    }
}
