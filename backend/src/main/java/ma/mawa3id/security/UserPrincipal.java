package ma.mawa3id.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ma.mawa3id.domain.entity.User;
import ma.mawa3id.domain.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

/**
 * Spring Security UserDetails implementation.
 */
@Getter
@AllArgsConstructor
@Builder
public class UserPrincipal implements UserDetails {

    private final Long id;
    private final UUID publicId;
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final Role role;
    private final Long businessId;
    private final boolean active;
    private final Collection<? extends GrantedAuthority> authorities;

    public static UserPrincipal create(User user) {
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());
        return UserPrincipal.builder()
                .id(user.getId())
                .publicId(user.getPublicId())
                .email(user.getEmail())
                .password(user.getPasswordHash())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .businessId(user.getBusinessId())
                .active(user.isActive())
                .authorities(Collections.singletonList(authority))
                .build();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
