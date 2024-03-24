package study.deliveryhanghae.domain.store.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.deliveryhanghae.domain.menu.entity.Menu;
import study.deliveryhanghae.domain.owner.entity.Owner;

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

    @Column(name = "business_number", nullable = false)
    private String businessNumber;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "store", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Menu> menuList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="owner_id")
    private Owner owner;

    @Column(nullable = false)
    private String originFileName;

    @Column(nullable = false)
    private String imageUrl;

    @Builder
    public Store(String name, String businessNumber, String address, String description, Owner owner, String imageUrl, String originFileName) {
        this.name = name;
        this.businessNumber = businessNumber;
        this.address = address;
        this.description = description;
        this.imageUrl = imageUrl;
        this.originFileName = originFileName;
        this.owner = owner;
    }

    public void update(String name, String imageUrl, String address, String description, String originFileName) {
        this.name = name;
        this.originFileName = originFileName;
        this.imageUrl = imageUrl;
        this.address = address;
        this.description = description;
    }

}