package study.deliveryhanghae.domain.menu.service;

import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import study.deliveryhanghae.domain.menu.dto.MenuRequestDto.CreateMenuDto;
import study.deliveryhanghae.domain.menu.dto.MenuRequestDto.UpdateMenuDto;
import study.deliveryhanghae.domain.menu.entity.Menu;
import study.deliveryhanghae.domain.menu.repository.MenuRepository;
import study.deliveryhanghae.domain.owner.entity.Owner;
import study.deliveryhanghae.domain.store.dto.StoreResponseDto.GetMenuListDto;
import study.deliveryhanghae.domain.store.entity.Store;
import study.deliveryhanghae.domain.store.repository.StoreRepository;
import study.deliveryhanghae.global.config.s3.S3Service;
import study.deliveryhanghae.global.handler.exception.BusinessException;
import study.deliveryhanghae.global.handler.exception.ErrorCode;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import static study.deliveryhanghae.global.handler.exception.ErrorCode.NOT_FOUND_STORE_MENU;


@Slf4j(topic = "menuService")
@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    private final S3Service s3Service;
    private final AmazonS3 s3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 메뉴 추가
    @Transactional
    public void createMenu(CreateMenuDto requestDto, Owner owner) throws IOException {
        // owner 정보에서 가게 정보 뽑기
        Store store = getStoreByOwner(owner);

        MultipartFile file = requestDto.menuImg();
        String originFileName = file.getOriginalFilename(); // img 원본 이름
        String s3FileName = UUID.randomUUID() + originFileName;
        String s3UrlText = s3Client.getUrl(bucket, s3FileName).toString();
        s3Service.delete(s3FileName);
        s3Service.upload(file, s3FileName);
        Menu menu = requestDto.toEntity(store, s3UrlText, originFileName);

        menuRepository.save(menu);
    }

    public GetMenuListDto getMenu(Long id, Owner owner) {
        // 유저의 가게 가져오기
        Store store = getStoreByOwner(owner);

        // user store id 와 menu store id 비교
        Long storeId = store.getId();

        // 메뉴 존재하는지 확인
        Menu menu = hasMenu(id);

        // 유저 가게 정보와 메뉴 가게 정보가 일치여부 확인
        if (!(Objects.equals(storeId, menu.getStore().getId()))) {
            throw new BusinessException(NOT_FOUND_STORE_MENU);
        }

        return new GetMenuListDto(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getImageUrl(),
                menu.getDescription());
    }

    @Transactional
    public void updateMenu(Long id, UpdateMenuDto requestDto, MultipartFile menuImg) throws IOException {
        // 메뉴 존재하는지 확인
        Menu menu = hasMenu(id);
        String previousS3UrlText = menu.getImageUrl();
        String keyName = previousS3UrlText.substring(previousS3UrlText.lastIndexOf("/") + 1);
        s3Service.delete(keyName);

        if(menuImg != null){
            String originFileName = menuImg.getOriginalFilename(); // img 원본 이름
            String s3FileName = UUID.randomUUID() + originFileName;
            s3Service.upload(menuImg, s3FileName);
            String s3UrlText = s3Client.getUrl(bucket, s3FileName).toString();
            menu.imgUpdate(s3UrlText, originFileName);
        }
        // 메뉴 업데이트
        menu.update(
                requestDto.menuName(),
                requestDto.menuPrice(),
                requestDto.menuDesc()
        );

    }
    
    // 메뉴 삭제
    public void deleteMenu(Long id) {
        Menu menu = hasMenu(id);
        menuRepository.delete(menu);
    }

    // 유저 가게 정보 확인
    private Store getStoreByOwner(Owner owner) {
        return storeRepository.findByOwner(owner);
    }

    // 존재하는 메뉴인지 확인
    private Menu hasMenu(Long menuId) {
        return menuRepository.findById(menuId).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND_STORE_MENU)
        );
    }

}
