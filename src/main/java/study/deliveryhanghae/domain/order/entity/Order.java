package study.deliveryhanghae.domain.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import study.deliveryhanghae.domain.menu.entity.Menu;
import study.deliveryhanghae.domain.owner.entity.Owner;
import study.deliveryhanghae.domain.store.entity.Store;
import study.deliveryhanghae.domain.user.entity.User;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Order{

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    @Enumerated(EnumType.ORDINAL)
    private OrderStatusEnum orderStatus;

    @CreationTimestamp
    @Column(name = "create_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(name = "finished_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime finishedAt;

    @Builder
    public Order(Long id, User user, Menu menu, Store store, Owner owner, OrderStatusEnum orderStatus, LocalDateTime createAt, LocalDateTime finishedAt) {
        this.id = id;
        this.user = user;
        this.menu = menu;
        this.store = store;
        this.owner = owner;
        this.orderStatus = orderStatus;
        this.createAt = createAt;
        this.finishedAt = finishedAt;
    }
}
