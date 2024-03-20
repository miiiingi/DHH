package study.deliveryhanghae.domain.menu.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import study.deliveryhanghae.domain.store.entity.Store;

@Entity
@Getter
@Table(name = "menu")
@NoArgsConstructor
@SQLDelete(sql = "UPDATE menu SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name ="store_id")
    private Store store;

    private String originFileName;

    private Boolean deleted = Boolean.FALSE; // soft delete 구현

    @Builder
    public Menu(String name, int price, String imageUrl, String description, Store store, String originFileName) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
        this.store = store;
        this.originFileName = originFileName;
    }

    public void update(String name, int price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public void imgUpdate(String imageUrl, String originFileName) {
        this.originFileName = originFileName;
        this.imageUrl = imageUrl;
    }
}