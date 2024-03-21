package study.deliveryhanghae.domain.charge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.deliveryhanghae.domain.charge.entity.Charge;

public interface ChargeRepository extends JpaRepository<Charge, Long> {
}
