package study.deliveryhanghae.domain.owner.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import study.deliveryhanghae.domain.order.service.OrderService;
import study.deliveryhanghae.domain.owner.dto.OwnerRequestDto;
import study.deliveryhanghae.domain.owner.dto.OwnerResponseDto.GetMainDto;
import study.deliveryhanghae.domain.owner.service.OwnerService;
import study.deliveryhanghae.global.aop.Timer;
import study.deliveryhanghae.global.config.security.owner.OwnerDetailsImpl;
import study.deliveryhanghae.global.handler.exception.BusinessException;

@Controller
@RequiredArgsConstructor
@Tag(name = "Owner API", description = "사장님 회원가입, 로그인")
public class OwnerController {
    private final OrderService orderService;
    private final OwnerService ownerService;

    @GetMapping("favicon.ico")
    @ResponseBody
    public void returnNoFavicon() {
    }

    @GetMapping("/v2/signup")
    public String signupPage(Model model) {
        model.addAttribute("ErrorCode", null);
        model.addAttribute("ErrorMessage", null);
        return "v2Signup";
    }

    @GetMapping("/v2/login-page")
    public String loginPage() {
        return "v2Login";
    }

    @Operation(summary = "회원가입", description = "회원 가입시 필요한 정보를 입력합니다.")
    @PostMapping("/v2/signup")
    public String signup(OwnerRequestDto.SignupRequestDto requestDto, Model model) {
        try {
            ownerService.signUp(requestDto);
        } catch (BusinessException ex) {
            model.addAttribute("ErrorCode", ex.getErrorCode().getStatus());
            model.addAttribute("ErrorMessage", ex.getErrorCode().getMessage());
            return "v2Signup";
        }
        return "redirect:/v2/login-page";
    }

    @Operation(summary = "로그인", description = "로그인 시 필요한 정보를 입력합니다.")
    @PostMapping("/v2/login")
    public void login(OwnerRequestDto.LoginRequestRecord requestDto) {
    }

    /***
     *  사장님 메인 페이지 이동
     *
     * @param model
     * @return
     */
    @Timer
    @Operation(summary = "메인페이지", description = "사장님 메인 페이지를 반환합니다.")
    @GetMapping("/v2")
    public String getOwnerMain(@AuthenticationPrincipal OwnerDetailsImpl userDetails, Model model) {
        // 사장님 가게 가지고 있는 상태 확인하고 없으면 생성하도록 반환
        if (!userDetails.getOwner().isStoreStatus()) {
            return "storeRegister";
        }
        GetMainDto mainResponseDto = orderService.getOrderList(userDetails.getOwner());
        model.addAttribute("mainResponseDto", mainResponseDto);
        return "owner";
    }

}
