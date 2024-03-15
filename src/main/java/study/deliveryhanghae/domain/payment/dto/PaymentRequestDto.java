package study.deliveryhanghae.domain.payment.dto;

import jakarta.validation.constraints.PositiveOrZero;

public class PaymentRequestDto {
    public record OrderRequestDto(
            Long price,
            Long id
    ) {
    }
    public record PayRequestDto(
            Long price,
            Long id
    ){

    }

}
