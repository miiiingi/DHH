package study.deliveryhanghae.domain.menu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import study.deliveryhanghae.domain.menu.dto.MenuRequestDto.CreateMenuDto;
import study.deliveryhanghae.domain.menu.dto.MenuRequestDto.UpdateMenuDto;
import study.deliveryhanghae.domain.menu.service.MenuService;
import study.deliveryhanghae.domain.store.dto.StoreResponseDto.GetMenuListDto;
import study.deliveryhanghae.global.config.security.UserDetailsImpl;
import study.deliveryhanghae.global.handler.exception.BusinessException;
import study.deliveryhanghae.global.handler.exception.ErrorCode;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Slf4j(topic = "메뉴 CRUD")
@Tag(name = "Menu API", description = "메뉴 CRUD")
public class MenuController {

    private final MenuService menuService;

    // 메뉴 등록
    @Operation(summary = "가게 메뉴 등록", description = "가게 메뉴를 추가한 뒤 메인페이지로 이동합니다.")
    @PostMapping("/v2/menu")
    public String createMenu(@ModelAttribute CreateMenuDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        // 유저 정보에서 가게 정보 가져오기
        try {
            Long ownerId = userDetails.getUser().getId();
            menuService.createMenu(requestDto, ownerId);
            return "redirect:/v2/store";
        }catch (BusinessException e) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }catch (IOException e){
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "선택 메뉴 상세 페이지 이동", description = "선택한 메뉴의 상세페이지로 이동합니다.")
    @GetMapping("/v2/menu/{id}")
    public String getMenu(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails){

        Long ownerId = userDetails.getUser().getId();
        GetMenuListDto menu = menuService.getMenu(id, ownerId);
        model.addAttribute("menu",menu);
        return "menuDetail";
    }

    @Operation(summary = "메뉴의 내용 수정", description = "메뉴의 내용을 수정합니다.")
    @PutMapping("/v2/menu/{id}")
    @ResponseBody
    public ResponseEntity<String> updateMenu(@PathVariable Long id, @RequestPart(value="key") UpdateMenuDto requestDto,
                             @RequestPart(value = "menuImg", required = false) MultipartFile menuImg) throws IOException {

        try{
            menuService.updateMenu( id, requestDto,menuImg);
            String message = "수정이 완료되었습니다.";
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        }catch (IOException e){
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "메뉴를 삭제합니다.", description = "메뉴를 삭제합니다.")
    @DeleteMapping("/v2/menu/{id}")
    @ResponseBody
    public String deleteMenu( @PathVariable Long id){
        menuService.deleteMenu(id);
        String message = "정상적으로 삭제되었습니다.";
        return message;
    }

}
