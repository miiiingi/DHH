package study.deliveryhanghae.domain.order.dto;

public class OrderRequestDto {

    public record PayDto(
            int price,
            Long id
    ){

    }

    public record OrderDto(
            Long menuId,
            int price
    ) {
    }
}
