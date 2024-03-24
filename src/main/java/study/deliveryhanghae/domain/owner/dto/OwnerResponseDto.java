package study.deliveryhanghae.domain.owner.dto;

import study.deliveryhanghae.domain.order.dto.OrderResponseDto;

import java.util.List;

public class OwnerResponseDto {
    public record GetMainDto(
            List<OrderResponseDto.GetOrderDto> orderList,
            int ownersPoint
    ){}
}
