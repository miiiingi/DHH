package study.deliveryhanghae.domain.store.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import study.deliveryhanghae.domain.store.entity.Menu;
import study.deliveryhanghae.domain.store.entity.Store;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findByStore(Store store);
}
