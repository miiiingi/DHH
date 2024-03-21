package study.deliveryhanghae.domain.charge.dto;

public class ChargeRequestDto {
    public record ChargeCallBackDto(
        String amount,
        String charge_uid
    ){}
}
