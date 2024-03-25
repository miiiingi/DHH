package study.deliveryhanghae.domain.charge.dto;

import study.deliveryhanghae.domain.user.entity.User;

public class ChargeResponseDto {
    public record ChargeDto(
            User user,
            String chargeUid,
            String chargeName
    ){}
}
