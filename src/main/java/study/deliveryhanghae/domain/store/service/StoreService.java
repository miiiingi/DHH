package study.deliveryhanghae.domain.store.service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import study.deliveryhanghae.domain.menu.entity.Menu;
import study.deliveryhanghae.domain.menu.repository.MenuRepository;
import study.deliveryhanghae.domain.owner.entity.Owner;
import study.deliveryhanghae.domain.owner.repository.OwnerRepository;
import study.deliveryhanghae.domain.store.dto.StoreRequestDto.CreateStoreDto;
import study.deliveryhanghae.domain.store.dto.StoreRequestDto.UpdateStoreDto;
import study.deliveryhanghae.domain.store.dto.StoreResponseDto.GetMenuListDto;
import study.deliveryhanghae.domain.store.dto.StoreResponseDto.GetStoreDto;
import study.deliveryhanghae.domain.store.dto.StoreResponseDto.StoreListDto;
import study.deliveryhanghae.domain.store.entity.Store;
import study.deliveryhanghae.domain.store.repository.StoreRepository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;

    private final String uploadDir = "C:/project/DHH/src/main/resources/static/images/";

    // 업장 전체 목록 조회
    public List<StoreListDto> getStoreList() {
        List<Store> store = storeRepository.findAll();
        return mapStoresToDto(store);
    }

    // 검색한 조건에 맞는 업장의 목록을 반환
    public List<StoreListDto> getSearchStroeList(String searchMenu) {
        List<Store> store = storeRepository.findByMenuListNameContaining(searchMenu);
        return mapStoresToDto(store);
    }

    // 선택한 업장, 해당 업장의 메뉴 목록 반환
    public GetStoreDto getStore(Long storeId) {

        Store store = storeRepository.getReferenceById(storeId);

        // 기존코드 사장님에 있던 메서드와 중복으로 해당 메서드 사용
        return getGetMenuList(store);
    }

    /***
     * 여기서부터 사장님 코드 입니다
     *
     */

    // 사장님 가게 등록
    @Transactional
    public void createOwnerStore(CreateStoreDto requestDto, Owner owner, MultipartFile file) throws IOException {

        String fullPath = uploadDir + file.getOriginalFilename();
        file.transferTo(new File(fullPath));

        Owner ownerDB = ownerRepository.getReferenceById(owner.getId());

        ownerDB.hasStore();

        storeRepository.save(requestDto.toEntity(ownerDB, fullPath, file.getOriginalFilename()));
    }

    // 사장님 가게,메뉴 리스트 얻기
    @Transactional(readOnly = true)
    public GetStoreDto getOwnerStore(Owner owner) {

        Store store = storeRepository.findByOwner(owner);

        return getGetMenuList(store);
    }

    /***
     * 
     * 
     * @param owner
     * @param requestDto
     * @param file
     * @throws IOException
     */
    // 사장님 가게 정보 수정
    @Transactional
    public void updateOwnerStore(Owner owner, UpdateStoreDto requestDto, MultipartFile file) throws IOException {

        Store store = storeRepository.findByOwner(owner);

        String fullPath = uploadDir + file.getOriginalFilename();
        file.transferTo(new File(fullPath));

        store.update(requestDto.name(),
                fullPath,
                requestDto.address(),
                requestDto.description(),
                file.getOriginalFilename());

    }


    /**
     *
     *
     * @param owner
     */

    // 사장님 가게 삭제
    @Transactional
    public void deleteOwnerStore(Owner owner) {

        Owner ownerDB = ownerRepository.getReferenceById(owner.getId());

        ownerDB.deleteStore();

        storeRepository.deleteByOwner(ownerDB);

    }

    /***
     * 공통 기능 메서드 분리하였습니다.
     * List<store> to  StoreListDto
     * @param stores
     * @return
     */

    // 스토어 storeListDto 형태로 변환 메서드 분리
    private List<StoreListDto> mapStoresToDto(List<Store> stores) {
        return stores.stream()
                .map(store -> new StoreListDto(
                                store.getId(),
                                store.getName(),
                                store.getImageUrl()))
                .toList();
    }


    @NotNull
    private GetStoreDto getGetMenuList(Store store) {
        List<Menu> menus = menuRepository.findByStore(store);

        List<GetMenuListDto> menuLists = new ArrayList<>();

        for (Menu menu : menus) {
            menuLists.add(
                    new GetMenuListDto(
                            menu.getId(),
                            menu.getName(),
                            menu.getPrice(),
                            menu.getImageUrl(),
                            menu.getDescription()
                    )
            );
        }
        return new GetStoreDto(menuLists, store.getName());
    }


    // 삭제시 비밀번호 확인 하는 메서드
    public boolean checkPassword(String password, String inputPassword) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String hashedPassword = encoder.encode(inputPassword);

        return passwordEncoder.matches(password, hashedPassword);
    }

}
