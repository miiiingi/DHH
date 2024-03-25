package study.deliveryhanghae.domain.store.dto;

import java.util.List;

public class StoreResponseDto{
    public record StoreListDto(Long id, String name, String imageUrl){

    }

    public record GetStoreDto(
            List<GetMenuListDto> menuLists,
            String storeName,
            String storeUrl,
            String storeDesc

    ) {
    }

    public record GetMenuListDto (Long menuId,
                               String menuName,
                               int menuPrice,
                               String menuUrl,
                               String menuDescription) {

    }

}
