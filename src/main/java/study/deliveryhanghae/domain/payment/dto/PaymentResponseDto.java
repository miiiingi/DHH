package study.deliveryhanghae.domain.payment.dto;

import jakarta.validation.constraints.PositiveOrZero;

public class PaymentResponseDto {
    public record PayResponseDto(
            @PositiveOrZero(message = "가격을 입력해주세요.")
            Long price
    ) {
    }

}
