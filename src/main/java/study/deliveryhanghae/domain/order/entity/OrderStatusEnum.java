package study.deliveryhanghae.domain.order.entity;

import lombok.Getter;

@Getter
public enum OrderStatusEnum {
    PREPARING("준비 중"),
    DELIVERED("배달 완료");

    private final String status;

    OrderStatusEnum(String status) {
        this.status = status;
    }

}
