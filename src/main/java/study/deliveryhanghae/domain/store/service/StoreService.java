package study.deliveryhanghae.domain.store.service;

import com.amazonaws.services.s3.AmazonS3;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import study.deliveryhanghae.domain.menu.entity.Menu;
import study.deliveryhanghae.domain.owner.entity.Owner;
import study.deliveryhanghae.domain.owner.repository.OwnerRepository;
import study.deliveryhanghae.domain.store.dto.StoreRequestDto.CreateStoreDto;
import study.deliveryhanghae.domain.store.dto.StoreRequestDto.UpdateStoreDto;
import study.deliveryhanghae.domain.store.dto.StoreResponseDto.GetMenuListDto;
import study.deliveryhanghae.domain.store.dto.StoreResponseDto.GetStoreDto;
import study.deliveryhanghae.domain.store.dto.StoreResponseDto.StoreListDto;
import study.deliveryhanghae.domain.store.entity.Store;
import study.deliveryhanghae.domain.store.repository.StoreRepository;
import study.deliveryhanghae.global.config.security.s3.S3Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j(topic = "storeService")
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;
    private final AmazonS3 s3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

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

        Store store = storeRepository.findAllJoinFetch(storeId);

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

        String s3FileName = UUID.randomUUID() + file.getOriginalFilename();
        String s3UrlText = s3Client.getUrl(bucket, s3FileName).toString();
        s3Service.delete(s3FileName);
        s3Service.upload(file, s3FileName);
        Owner ownerDB = ownerRepository.getReferenceById(owner.getId());
        ownerDB.hasStore();
        storeRepository.save(requestDto.toEntity(ownerDB, s3UrlText, file.getOriginalFilename()));
    }

    // 사장님 가게,메뉴 리스트 얻기
    @Transactional(readOnly = true)
    public GetStoreDto getOwnerStore(Owner owner) {

        Store store = storeRepository.findAllJoinFetch(owner);
        if (store == null) {
            store = storeRepository.findByOwner(owner);
            return new GetStoreDto(null,
                    store.getName(),
                    store.getImageUrl(),
                    store.getDescription());
        }
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
//
//        //  이전 가게 이미지 삭제
        Store store = storeRepository.findByOwner(owner);
        String previousS3UrlText = store.getImageUrl();
        log.info(previousS3UrlText);
        String keyName = previousS3UrlText.substring(previousS3UrlText.lastIndexOf("/") + 1);
        s3Service.delete(keyName);

        //  새로운 가게 이미지 등록
        String s3FileName = UUID.randomUUID() + file.getOriginalFilename();
        s3Service.upload(file, s3FileName);
        String s3UrlText = s3Client.getUrl(bucket, s3FileName).toString();
        store.update(requestDto.name(),
                s3UrlText,
                requestDto.address(),
                requestDto.description(),
                file.getOriginalFilename());

    }


    /**
     * @param owner
     */

    // 사장님 가게 삭제
    @Transactional
    public void deleteOwnerStore(Owner owner) {

        Owner ownerDB = ownerRepository.getReferenceById(owner.getId());

        ownerDB.deleteStore();

        storeRepository.deleteAllInBatchByOwner(ownerDB);

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

        List<Menu> menus = store.getMenuList();

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
        return new GetStoreDto(menuLists, store.getName(), store.getImageUrl(), store.getDescription());
    }


    // 삭제시 비밀번호 확인 하는 메서드
    public boolean checkPassword(String password, String inputPassword) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String hashedPassword = encoder.encode(inputPassword);

        return passwordEncoder.matches(password, hashedPassword);
    }

}
