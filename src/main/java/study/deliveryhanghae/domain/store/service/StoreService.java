package study.deliveryhanghae.domain.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.deliveryhanghae.domain.store.dto.StoreResponseDto.StoreListDto;
import study.deliveryhanghae.domain.store.repository.StoreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

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
}
