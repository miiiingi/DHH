package study.deliveryhanghae.domain.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.deliveryhanghae.domain.store.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
