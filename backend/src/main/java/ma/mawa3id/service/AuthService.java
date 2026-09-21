package ma.mawa3id.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.mawa3id.domain.entity.*;
import ma.mawa3id.domain.enums.Role;
import ma.mawa3id.domain.enums.SubscriptionStatus;
import ma.mawa3id.dto.auth.*;
import ma.mawa3id.exception.BusinessRuleException;
import ma.mawa3id.exception.DuplicateResourceException;
import ma.mawa3id.exception.ResourceNotFoundException;
import ma.mawa3id.repository.*;
import ma.mawa3id.security.JwtTokenProvider;
import ma.mawa3id.security.UserPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final SubscriptionPlanRepository planRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final NotificationConfigRepository notificationConfigRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new business owner: email={}, business={}", request.getEmail(), request.getBusinessName());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Un compte existe déjà avec cette adresse email.");
        }

        // 1. Create User
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.BUSINESS_OWNER)
                .active(true)
                .emailVerified(true) // Pre-verified for smooth onboarding
                .language("fr")
                .build();

        user = userRepository.save(user);

        // 2. Generate slug
        String baseSlug = toSlug(request.getBusinessName());
        String slug = baseSlug;
        int counter = 1;
        while (businessRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter++;
        }

        // 3. Create Business
        Business business = Business.builder()
                .name(request.getBusinessName())
                .slug(slug)
                .phone(request.getPhone())
                .whatsappNumber(request.getPhone())
                .email(request.getEmail())
                .city(request.getCity() != null ? request.getCity() : "Casablanca")
                .category(request.getBusinessCategory())
                .ownerId(user.getId())
                .active(true)
                .build();

        business = businessRepository.save(business);

        // Link user to business
        user.setBusinessId(business.getId());
        userRepository.save(user);

        // 4. Create default Free Trial Subscription
        SubscriptionPlan freePlan = planRepository.findBySlug("free")
                .orElseGet(() -> planRepository.findAll().stream().findFirst().orElse(null));

        if (freePlan != null) {
            Subscription subscription = Subscription.builder()
                    .businessId(business.getId())
                    .plan(freePlan)
                    .status(SubscriptionStatus.TRIAL)
                    .currentPeriodStart(LocalDateTime.now())
                    .currentPeriodEnd(LocalDateTime.now().plusDays(30))
                    .trialEndsAt(LocalDateTime.now().plusDays(14))
                    .build();
            subscriptionRepository.save(subscription);
        }

        // 5. Create default Notification Config
        NotificationConfig notificationConfig = NotificationConfig.builder()
                .businessId(business.getId())
                .whatsappEnabled(true)
                .smsEnabled(false)
                .emailEnabled(false)
                .reminder24hEnabled(true)
                .reminder2hEnabled(true)
                .defaultLanguage("fr")
                .build();
        notificationConfigRepository.save(notificationConfig);

        // 6. Generate Tokens
        UserPrincipal principal = UserPrincipal.create(user);
        String accessToken = tokenProvider.generateAccessToken(principal);
        String refreshToken = tokenProvider.generateRefreshToken(principal);

        // Save refresh token
        saveRefreshToken(user.getId(), refreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getPublicId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .businessId(business.getId())
                .businessName(business.getName())
                .businessSlug(business.getSlug())
                .build();
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        String accessToken = tokenProvider.generateAccessToken(principal);
        String refreshToken = tokenProvider.generateRefreshToken(principal);

        saveRefreshToken(principal.getId(), refreshToken);

        Business business = null;
        if (principal.getBusinessId() != null) {
            business = businessRepository.findById(principal.getBusinessId()).orElse(null);
        }

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(principal.getPublicId())
                .email(principal.getEmail())
                .firstName(principal.getFirstName())
                .lastName(principal.getLastName())
                .role(principal.getRole())
                .businessId(principal.getBusinessId())
                .businessName(business != null ? business.getName() : null)
                .businessSlug(business != null ? business.getSlug() : null)
                .build();
    }

    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String tokenStr = request.getRefreshToken();
        if (!tokenProvider.validateToken(tokenStr)) {
            throw new BusinessRuleException("Jeton de rafraîchissement invalide ou expiré");
        }

        RefreshToken refreshToken = refreshTokenRepository.findByToken(tokenStr)
                .orElseThrow(() -> new BusinessRuleException("Jeton de rafraîchissement révoqué ou introuvable"));

        if (!refreshToken.isValid()) {
            throw new BusinessRuleException("Jeton de rafraîchissement expiré");
        }

        User user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        UserPrincipal principal = UserPrincipal.create(user);
        String newAccessToken = tokenProvider.generateAccessToken(principal);
        String newRefreshToken = tokenProvider.generateRefreshToken(principal);

        // Rotate refresh token
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
        saveRefreshToken(user.getId(), newRefreshToken);

        Business business = null;
        if (user.getBusinessId() != null) {
            business = businessRepository.findById(user.getBusinessId()).orElse(null);
        }

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .userId(user.getPublicId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .businessId(user.getBusinessId())
                .businessName(business != null ? business.getName() : null)
                .businessSlug(business != null ? business.getSlug() : null)
                .build();
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            String token = UUID.randomUUID().toString();
            user.setPasswordResetToken(token);
            user.setPasswordResetExpiresAt(LocalDateTime.now().plusHours(2));
            userRepository.save(user);
            log.info("Password reset token generated for {}: {}", user.getEmail(), token);
        });
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByPasswordResetToken(request.getToken())
                .orElseThrow(() -> new BusinessRuleException("Jeton de réinitialisation invalide"));

        if (user.getPasswordResetExpiresAt() == null ||
                user.getPasswordResetExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BusinessRuleException("Le jeton de réinitialisation a expiré");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordResetToken(null);
        user.setPasswordResetExpiresAt(null);
        userRepository.save(user);
    }

    private void saveRefreshToken(Long userId, String token) {
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(userId)
                .token(token)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);
    }

    private String toSlug(String input) {
        String nowhitespace = WHITESPACE.matcher(input.trim()).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }
}
