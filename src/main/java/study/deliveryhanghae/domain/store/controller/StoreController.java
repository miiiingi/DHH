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
import study.deliveryhanghae.domain.store.dto.StoreRequestDto.CreateStoreDto;
import study.deliveryhanghae.domain.store.dto.StoreRequestDto.UpdateStoreDto;
import study.deliveryhanghae.domain.store.dto.StoreResponseDto.GetStoreDto;
import study.deliveryhanghae.domain.store.dto.StoreResponseDto.StoreListDto;
import study.deliveryhanghae.domain.store.service.StoreService;
import study.deliveryhanghae.global.config.security.owner.OwnerDetailsImpl;
import study.deliveryhanghae.global.config.security.user.UserDetailsImpl;
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

    //유저 메인페이지
    @Operation(summary = "메인페이지", description = "메인 페이지에 들어갈 가게 리스트를 조회합니다.")
    @GetMapping("/v1")
    public String getMainPage(Model model) {
        List<StoreListDto> storeList = storeService.getStoreList();
        model.addAttribute("stores", storeList);
        return "index";
    }

    // 메인 페이지 검색 기능
    @Operation(summary = "검색 기능", description = "메인 페이지에서 찾고 싶은 메뉴로 가게를 검색합니다.")
    @GetMapping("/v1/search")
    public String getSearchStore(
            Model model,
            @RequestParam("searchMenu") String searchMenu) {
        List<StoreListDto> storeList = storeService.getSearchStroeList(searchMenu);
        model.addAttribute("stores", storeList);
        return "index";
    }

    // 선택한 상점 들어가기
    @Operation(summary = "가게 조회", description = "메인 페이지에서 가게 선택시 선택한 가게의 상세페이지로 이동합니다.")
    @GetMapping("/v1/{storeId}")
    public String getStore(@PathVariable Long storeId, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        GetStoreDto storeMenuList = storeService.getStore(storeId);

        model.addAttribute("store", storeMenuList);

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
    public String createOwnerStore(@AuthenticationPrincipal OwnerDetailsImpl userDetails,
                                   @RequestPart("file") MultipartFile file,
                                   @RequestPart("request") CreateStoreDto requestDto,
                                   Model model) {
        try {
            storeService.createOwnerStore(requestDto, userDetails.getOwner(), file);
        } catch (BusinessException ex) {
            model.addAttribute("ErrorCode", ex.getErrorCode().getStatus());
            model.addAttribute("ErrorMessage", ex.getErrorCode().getMessage());
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return "redirect:/v2";
    }

    @Operation(summary = "가게 조회", description = "가게 상세페이지에 들어갈 가게 이름과 메뉴 리스트를 조회합니다.")
    @GetMapping("/v2/store")
    public String getOwnerStore(@AuthenticationPrincipal OwnerDetailsImpl userDetails, Model model) {

        GetStoreDto storeMenuList = storeService.getOwnerStore(userDetails.getOwner());
        model.addAttribute("store", storeMenuList);

        return "ownerStore";
    }

    @Operation(summary = "가게 정보 수정", description = "가게의 정보들을 수정합니다.")
    @PutMapping("/v2/store")
    @ResponseBody
    public String updateOwnerStore(@AuthenticationPrincipal OwnerDetailsImpl userDetails,
                                   @RequestPart("file") MultipartFile file,
                                   @RequestPart("request") UpdateStoreDto requestDto,
                                   Model model) throws IOException {
        storeService.updateOwnerStore(userDetails.getOwner(), requestDto, file);
        model.addAttribute("msg", "수정이 완료되었습니다.");
        return "ownerStore";
    }

    @Operation(summary = "가게 정보 삭제", description = "가게를 삭제한 뒤 가게 등록 페이지로 이동합니다.")
    @DeleteMapping("/v2/store")
    public String deleteOwnerStore(@AuthenticationPrincipal OwnerDetailsImpl userDetails, Model model) {
        storeService.deleteOwnerStore(userDetails.getOwner());
        model.addAttribute("msg", "삭제가 완료되었습니다.");

        return "owner";
    }

    @Operation(summary = "패스워드 확인", description = "가게 삭제시 패스워드 확인을 받습니다.")
    @PostMapping("/v2/store/password-check")
    public ResponseEntity<Boolean> checkPassword(@AuthenticationPrincipal OwnerDetailsImpl userDetails,
                                                 @RequestBody String enteredPassword) {
        enteredPassword.replaceFirst("password=", "");

        // 서비스로부터 비밀번호 일치 여부 확인 후 반환
        boolean check = storeService.checkPassword(userDetails.getPassword(), enteredPassword);
        return ResponseEntity.ok(check);
    }

}
