package study.deliveryhanghae.domain.menu.dto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import study.deliveryhanghae.domain.menu.entity.Menu;
import study.deliveryhanghae.domain.store.entity.Store;

@Slf4j
public class MenuRequestDto {
    public record CreateMenuDto(String menuName,
                                int menuPrice,
                                MultipartFile menuImg,
                                String menuDesc) {

        public Menu toEntity(Store store, String fullPath, String originFileName){

            return Menu.builder()
                    .name(menuName)
                    .price(menuPrice)
                    .imageUrl(fullPath)
                    .originFileName(originFileName)
                    .description(menuDesc)
                    .store(store)
                    .build();
        }
    }

    public record UpdateMenuDto(String menuName,
                                String menuDesc,
                                int menuPrice
    ) {

    }

}
