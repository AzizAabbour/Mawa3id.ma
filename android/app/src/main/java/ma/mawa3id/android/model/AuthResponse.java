package ma.mawa3id.android.model;

public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private Long businessId;
    private String businessName;
    private String businessSlug;

    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getRole() { return role; }
    public Long getBusinessId() { return businessId; }
    public String getBusinessName() { return businessName; }
    public String getBusinessSlug() { return businessSlug; }
}
