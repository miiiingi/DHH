package study.deliveryhanghae.domain.store.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.deliveryhanghae.domain.payment.entity.Menu;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "store")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @OneToMany(mappedBy = "store", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Menu> menuList = new ArrayList<>();

    @Builder
    public Store(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

}
