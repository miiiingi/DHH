package study.deliveryhanghae.domain.payment.dto;

import jakarta.validation.constraints.PositiveOrZero;

public class PaymentRequestDto {
    public record PayRequestDto(
            @PositiveOrZero(message = "가격을 입력해주세요.")
            Long price,
            Long id
    ) {
    }

}
