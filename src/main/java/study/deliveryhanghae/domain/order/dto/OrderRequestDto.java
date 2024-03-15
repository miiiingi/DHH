package study.deliveryhanghae.domain.order.dto;

public class OrderRequestDto {

    public record PayDto(
            Long price,
            Long id
    ){

    }

    public record OrderDto(
            Long menuId,
            Long price
    ) {
    }
}
