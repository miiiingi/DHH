package study.deliveryhanghae.domain.user.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String password;
    @Column(unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum authority;
    private String address;
    private int point;

    @Builder
    public User(String email, String password, String nickname, String address) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.address = address;
        this.authority = UserRoleEnum.USER;
        this.point = 10000;
    }

    public void updatePoint(int point) {
        this.point = point;
    }
}
