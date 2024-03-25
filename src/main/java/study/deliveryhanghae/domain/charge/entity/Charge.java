package study.deliveryhanghae.domain.charge.entity;

import com.siot.IamportRestClient.response.Payment;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.deliveryhanghae.domain.user.entity.User;

@Entity
@Getter
@NoArgsConstructor
public class Charge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int price;
    private String chargeUid; // 결제 고유 번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private User user;

    @Builder
    public Charge(String chargeUid, int price, User user) {
        this.chargeUid = chargeUid;
        this.price = price;
        this.user = user;
    }
}
