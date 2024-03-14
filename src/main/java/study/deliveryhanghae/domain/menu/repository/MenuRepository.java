package study.deliveryhanghae.domain.menu.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import study.deliveryhanghae.domain.menu.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {

}
