package study.deliveryhanghae.domain.owner.dto;

import study.deliveryhanghae.domain.order.dto.OrderResponseDto.getOrderDto;

import java.util.List;

public class OwnerResponseDto {
    public record GetMainDto(
            List<getOrderDto> orderList,
            int ownersPoint
    ){}
}
