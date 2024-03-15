package study.deliveryhanghae.domain.store.dto;

import java.util.List;

public class StoreResponseDto{
    public static record StoreListDto(Long id,String name,String imageUrl){

    }

    public record GetStore(
            List<GetMenuList> menuLists,
            String storeName
    ) {

        public GetStore(List<GetMenuList> menuLists, String storeName) {
            this.menuLists = menuLists;
            this.storeName = storeName;
        }

    }

    public record GetMenuList (Long menuId, String menuName, int menuPrice, String menuUrl) {
        public GetMenuList(Long menuId, String menuName, int menuPrice, String menuUrl){
            this.menuId = menuId;
            this.menuName = menuName;
            this.menuPrice = menuPrice;
            this.menuUrl = menuUrl;
        }
    }

}
