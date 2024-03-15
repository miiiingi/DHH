package study.deliveryhanghae.domain.payment.dto;

public class PaymentResponseDto {
    public record PayResponseDto(
            Long price,
            Long id,
            Long point
    ) {
    }

}
