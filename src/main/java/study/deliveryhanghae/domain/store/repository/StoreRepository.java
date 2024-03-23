package study.deliveryhanghae.domain.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import study.deliveryhanghae.domain.owner.entity.Owner;
import study.deliveryhanghae.domain.store.entity.Store;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store,Long> {

    @Query("SELECT DISTINCT s FROM Store s JOIN FETCH s.menuList WHERE s.id = :storeId")
    Store findAllJoinFetch(Long storeId);

    @Query("SELECT DISTINCT s FROM Store s JOIN FETCH s.menuList WHERE s.owner = :owner")
    Store findAllJoinFetch(Owner owner);


    List<Store> findByMenuListNameContaining(String menuName);

    Store findByOwner(Owner owner);

    void deleteAllInBatchByOwner(Owner owner);
}
