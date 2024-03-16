package study.deliveryhanghae.domain.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import study.deliveryhanghae.domain.order.entity.Order;
import study.deliveryhanghae.domain.order.entity.OrderStatusEnum;

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
    public record getOrderDto(
            Long orderId,
            String orderStatus,
            String createdAt,
            String menuName
    ){
        public static getOrderDto mapToOrderDto(Order order){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String createdAtString = order.getCreateAt().format(formatter);

            return new getOrderDto(
                    order.getId(),
                    order.getOrderStatus().getStatus(),
                    createdAtString,
                    order.getMenu().getName()
            );
        }

    }

    public record PayDto(
            int price,
            Long id,
            int point
    ) {
    }

}
