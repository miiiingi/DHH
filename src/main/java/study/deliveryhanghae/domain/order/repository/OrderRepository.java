package study.deliveryhanghae.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import study.deliveryhanghae.domain.order.entity.Order;
import study.deliveryhanghae.domain.owner.entity.Owner;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.menu JOIN FETCH o.owner WHERE o.owner = :owner")
    List<Order> findAllWithMenuByOwner(Owner owner);
}
