package study.deliveryhanghae.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.deliveryhanghae.domain.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
