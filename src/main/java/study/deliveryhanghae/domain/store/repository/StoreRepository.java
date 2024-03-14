package study.deliveryhanghae.domain.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.deliveryhanghae.domain.store.entity.Store;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store,Long> {

    List<Store> findByMenuListNameContaining(String menuName);
}
