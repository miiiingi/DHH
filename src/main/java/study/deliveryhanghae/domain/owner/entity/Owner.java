package study.deliveryhanghae.domain.owner.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(indexes = @Index(name = "idx_owner_email", columnList = "email", unique = true))
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private int point;

    // 사장님이 가게 가지고 있는지 없는지 확인하는 상태값
    // security 적용 전까지 false default로 넣어두도록 하겠습니다.
    private boolean storeStatus=false;
    @Builder
    public Owner(Long id, String name, String email, String password, int point) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.point = point;
    }
    public void updatePoint(int point) {
        this.point += point;
    }

    public void hasStore() {
        this.storeStatus = true;
    }
    public void deleteStore() {
        this.storeStatus = false;
    }
}
