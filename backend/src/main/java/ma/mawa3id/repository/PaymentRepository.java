package ma.mawa3id.repository;

import ma.mawa3id.domain.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Page<Payment> findByBusinessId(Long businessId, Pageable pageable);
    Optional<Payment> findByProviderTransactionId(String transactionId);
}
