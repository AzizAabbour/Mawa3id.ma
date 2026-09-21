package ma.mawa3id.repository;

import ma.mawa3id.domain.entity.User;
import ma.mawa3id.domain.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    Optional<User> findByPublicId(UUID publicId);
    Optional<User> findByEmailVerificationToken(String token);
    Optional<User> findByPasswordResetToken(String token);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    List<User> findByBusinessId(Long businessId);
    Page<User> findByRole(Role role, Pageable pageable);
    long countByBusinessId(Long businessId);
}
