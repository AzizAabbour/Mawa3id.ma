package ma.mawa3id.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.mawa3id.domain.enums.Role;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    @Builder.Default
    private String tokenType = "Bearer";
    private UUID userId;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private Long businessId;
    private String businessName;
    private String businessSlug;
}
