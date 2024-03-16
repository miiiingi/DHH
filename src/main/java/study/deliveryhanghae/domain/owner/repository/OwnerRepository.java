package study.deliveryhanghae.domain.owner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import study.deliveryhanghae.domain.owner.entity.Owner;

public interface OwnerRepository extends JpaRepository<Owner,Long> {
    @Query("SELECT o.point FROM Owner o WHERE o.id = ?1")
    Integer findPointById(Long id);
}
