package study.deliveryhanghae.domain.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import study.deliveryhanghae.domain.order.entity.Order;

import java.time.LocalDate;
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
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
            LocalDateTime createdAt,
            String menuName
    ){
    }
}
