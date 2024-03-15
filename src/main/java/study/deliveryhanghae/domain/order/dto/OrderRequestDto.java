package study.deliveryhanghae.domain.order.dto;

import study.deliveryhanghae.domain.order.entity.Order;
import study.deliveryhanghae.domain.payment.entity.Menu;
import study.deliveryhanghae.domain.user.entity.User;

public record OrderRequestDto(Menu menu) {

    public Order toEntity(Menu menu, User user){
        return Order.builder()
                .menu(menu)
                .user(user)
                .build();
    }

}
