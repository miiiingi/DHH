package study.deliveryhanghae.domain.store.dto;

import java.util.List;

public class StoreResponseDto{
    public record StoreListDto(Long id, String name, String imageUrl){

    }

    public record GetStore(
            List<GetMenuList> menuLists,
            String storeName
    ) {
    }

    public record GetMenuList (Long menuId,
                               String menuName,
                               int menuPrice,
                               String menuUrl,
                               String menuDescription) {

    }

}
