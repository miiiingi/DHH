package study.deliveryhanghae.domain.owner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import study.deliveryhanghae.domain.owner.entity.Owner;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner,Long> {

    Optional<Owner> findByEmail(String username);
    @Query("SELECT o.point FROM Owner o WHERE o.id = ?1")
    Integer findPointById(Long id);

    boolean existsByEmail(String username);
}
