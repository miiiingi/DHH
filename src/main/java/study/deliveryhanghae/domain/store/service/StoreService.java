package study.deliveryhanghae.domain.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.deliveryhanghae.domain.payment.entity.Menu;
import study.deliveryhanghae.domain.payment.repository.MenuRepository;
import study.deliveryhanghae.domain.store.dto.StoreResponseDto.GetMenuList;
import study.deliveryhanghae.domain.store.dto.StoreResponseDto.GetStore;
import study.deliveryhanghae.domain.store.dto.StoreResponseDto.StoreListDto;
import study.deliveryhanghae.domain.store.entity.Store;
import study.deliveryhanghae.domain.store.repository.StoreRepository;
import study.deliveryhanghae.domain.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;


    // 업장 전체 목록 조회
    public List<StoreListDto> getStoreList() {
        return storeRepository.findAll()
                .stream()
                .map(store -> new StoreListDto(store.getId(),
                        store.getName(),
                        store.getImageUrl()))
                .toList();
    }

    public List<StoreListDto> getSearchStroeList(String searchMenu) {
        return storeRepository.findByMenuListNameContaining(searchMenu)
                .stream()
                .map(store -> new StoreListDto(store.getId(),
                        store.getName(),
                        store.getImageUrl()))
                .toList();
    }

    public GetStore getStore(Long storeId) {

        Store store = storeRepository.getReferenceById(storeId);

        List<Menu> menus = menuRepository.findByStore(store);

        List<GetMenuList> menuLists = new ArrayList<>();

        for (Menu menu : menus) {
            menuLists.add(
                    new GetMenuList(
                            menu.getId(),
                            menu.getName(),
                            menu.getPrice(),
                            menu.getImage_url()
                    )
            );
        }

        return new GetStore(menuLists, store.getName());
    }

}
