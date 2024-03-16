package study.deliveryhanghae.domain.owner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.deliveryhanghae.domain.owner.entity.Owner;

public interface OwnerRepository extends JpaRepository<Owner,Long> {
}
