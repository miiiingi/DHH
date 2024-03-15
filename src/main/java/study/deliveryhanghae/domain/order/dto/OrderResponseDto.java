package study.deliveryhanghae.domain.order.dto;

public class OrderResponseDto {

    public record OrderDto(
            Long id,
            String menuName,
            String menuUrl,
            Long menuPrice,
            Long point,
            String remainPoint,
            Long userId
    ) {

    }
    public record PayDto(
            Long price,
            Long id,
            Long point
    ) {
    }

}
