package study.deliveryhanghae.domain.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import study.deliveryhanghae.domain.store.dto.StoreRequestDto;
import study.deliveryhanghae.domain.store.dto.StoreResponseDto;
import study.deliveryhanghae.domain.store.dto.StoreResponseDto.StoreListDto;
import study.deliveryhanghae.domain.store.service.StoreService;
import study.deliveryhanghae.global.config.security.UserDetailsImpl;
import study.deliveryhanghae.global.handler.exception.BusinessException;
import study.deliveryhanghae.global.handler.exception.ErrorCode;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j(topic = "가게 CRUD")
@Tag(name = "Store API", description = "가게 CRUD")
public class StoreController {

    private final StoreService storeService;
    private final ObjectMapper objectMapper;

    //유저 메인페이지
    @GetMapping("/v1")
    public String getMainPage(Model model) {
        List<StoreListDto> storeList = storeService.getStoreList();
        model.addAttribute("stores", storeList);
        return "index";
    }

    // 메인 페이지 검색 기능
    @GetMapping("/v1/search")
    public String getSearchStore(
            Model model,
            @RequestParam("searchMenu") String searchMenu) {
        List<StoreListDto> storeList = storeService.getSearchStroeList(searchMenu);
        model.addAttribute("stores", storeList);
        return "index";
    }

    // 선택한 상점 들어가기
    @GetMapping("/v1/{storeId}")
    public String getStore(@PathVariable Long storeId, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String storeName = storeService.getStore(storeId).storeName();
        List<StoreResponseDto.GetMenuList> menuLists = storeService.getStore(storeId).menuLists();
        model.addAttribute("storeName", storeService.getStore(storeId).storeName());
        model.addAttribute("menuList", storeService.getStore(storeId).menuLists());
        if (userDetails == null) {
            model.addAttribute("userDetails", "USER_NOT_LOGIN");

        } else {
            model.addAttribute("userDetails", "USER_LOGIN");
        }
        return "store";
    }



    /***
     * 여기서 부터 사장님 페이지 입니다
     */
    @Operation(summary = "가게 등록", description = "가게 등록시 필요한 정보를 입력한 뒤 메인페이지로 이동합니다.")
    @PostMapping("/v2/store")
    public String createOwnerStore(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                   @RequestPart("file") MultipartFile file,
                                   @RequestPart("request")String jsonData,
                                   Model model) {
        try {
            StoreRequestDto.Create requestDto = objectMapper.readValue(jsonData, StoreRequestDto.Create.class);
            storeService.createOwnerStore(requestDto, userDetails.getUser().getId(), file);
        } catch (BusinessException ex) {
            model.addAttribute("ErrorCode", ex.getErrorCode().getStatus());
            model.addAttribute("ErrorMessage", ex.getErrorCode().getMessage());
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return "owner";
    }

    @Operation(summary = "가게 조회", description = "가게 상세페이지에 들어갈 가게 이름과 메뉴 리스트를 조회합니다.")
    @GetMapping("/v2/store")
    public String getOwnerStore(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {

        String storeName = storeService.getOwnerStore(userDetails.getUser().getId()).storeName();
        List<StoreResponseDto.GetMenuList> menuLists = storeService.getOwnerStore(userDetails.getUser().getId()).menuLists();
        model.addAttribute("storeName", storeName);
        model.addAttribute("menuList", menuLists);

        return "ownerStore";
    }

    @Operation(summary = "가게 정보 수정", description = "가게의 정보들을 수정합니다.")
    @PutMapping("/v2/store")
    public String updateOwnerStore(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                   @RequestPart("file") MultipartFile file,
                                   @RequestPart("request")String jsonData,
                                   Model model) throws IOException {
        StoreRequestDto.Update requestDto = objectMapper.readValue(jsonData, StoreRequestDto.Update.class);
        // 임시로 id 뽑는 걸로 변경하여 ownerId 보내주고 있습니다.
        // security 적용 후 변경 칠요
        Long ownerId = 1L;
//        Owner owner = userDetails.getUser();
        storeService.updateOwnerStore(ownerId, requestDto, file);
//        storeService.updateOwnerStore(owner, requestDto, file);
        model.addAttribute("msg", "수정이 완료되었습니다.");

        return "ownerStore";
    }

    @Operation(summary = "가게 정보 삭제", description = "가게를 삭제한 뒤 가게 등록 페이지로 이동합니다.")
    @DeleteMapping("/v2/store")
    public String deleteOwnerStore(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
//        storeService.deleteOwnerStore(userDetails.getUser());
        Long ownerId = 1L;
        storeService.deleteOwnerStore(ownerId);
        model.addAttribute("msg", "삭제가 완료되었습니다.");

        return "owner";
    }

    @Operation(summary = "패스워드 확인", description = "가게 삭제시 패스워드 확인을 받습니다.")
    @PostMapping("/v2/store/password-check")
    public ResponseEntity<Boolean> checkPassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody String enteredPassword) {
        enteredPassword.replaceFirst("password=", "");

        // 서비스로부터 비밀번호 일치 여부 확인 후 반환
        boolean check = storeService.checkPassword(userDetails.getPassword(), enteredPassword);
        log.info(String.valueOf(check));
        return ResponseEntity.ok(check);
    }

}
