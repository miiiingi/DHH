package study.deliveryhanghae.domain.order.dto;

public class OrderResponseDto {

    public record OrderDto(
            Long id,
            String menuName,
            String menuUrl,
            int menuPrice,
            int point,
            String remainPoint,
            Long userId
    ) {

    }
    public record PayDto(
            int price,
            Long id,
            int point
    ) {
    }

}
