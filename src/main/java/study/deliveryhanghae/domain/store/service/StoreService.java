package study.deliveryhanghae.domain.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.deliveryhanghae.domain.menu.entity.Menu;
import study.deliveryhanghae.domain.store.dto.StoreResponseDto;
import study.deliveryhanghae.domain.store.dto.StoreResponseDto.StoreListDto;
import study.deliveryhanghae.domain.store.entity.Store;
import study.deliveryhanghae.domain.menu.repository.MenuRepository;
import study.deliveryhanghae.domain.store.repository.StoreRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    // 업장 전체 목록 조회
    public List<StoreListDto> getStoreList() {
        List<StoreListDto> storeList = storeRepository.findAll()
                .stream()
                .map(store -> new StoreListDto(store.getId(),
                        store.getName(),
                        store.getImageUrl()))
                .toList();
        return storeList;
    }

    public List<StoreListDto> getSearchStroeList(String searchMenu) {
        List<StoreListDto> storeList = storeRepository.findByMenuListNameContaining(searchMenu)
                .stream()
                .map(store -> new StoreListDto(store.getId(),
                        store.getName(),
                        store.getImageUrl()))
                .toList();
        return storeList;
    }

    public StoreResponseDto.GetStore getStore(Long storeId) {

        Store store = storeRepository.getReferenceById(storeId);

        List<Menu> menus = menuRepository.findByStore(store);

        List<StoreResponseDto.GetMenuList> menuLists = new ArrayList<>();

        for (Menu menu : menus) {
            menuLists.add(
                    new StoreResponseDto.GetMenuList(
                            menu.getId(),
                            menu.getName(),
                            menu.getPrice(),
                            menu.getImageUrl()
                    )
            );
        }

        return new StoreResponseDto.GetStore(menuLists, store.getName());
    }

}
