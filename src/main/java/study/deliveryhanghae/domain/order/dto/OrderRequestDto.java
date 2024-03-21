package study.deliveryhanghae.domain.order.dto;

public class OrderRequestDto {

    public record PayDto(
            Long id, //유저 아이디
            Long menuId
    ){

    }

}
