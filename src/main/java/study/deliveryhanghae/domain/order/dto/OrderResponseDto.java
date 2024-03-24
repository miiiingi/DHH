package study.deliveryhanghae.domain.order.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    public record GetOrderDto(
            Long orderId,
            String orderStatus,
            String createdAt,
            String menuName
    ){
        public GetOrderDto(Long orderId, String orderStatus, LocalDateTime createdAt, String menuName) {
            this(orderId, orderStatus, formatDateTime(createdAt), menuName);
        }
        private static String formatDateTime(LocalDateTime dateTime) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return dateTime.format(formatter);
        }

    }
}
