package study.deliveryhanghae.domain.owner.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import study.deliveryhanghae.domain.order.dto.OrderResponseDto.getOrderDto;
import study.deliveryhanghae.domain.order.service.OrderService;
import study.deliveryhanghae.domain.owner.dto.OwnerRequestDto;
import study.deliveryhanghae.domain.owner.dto.OwnerResponseDto.GetMainDto;
import study.deliveryhanghae.domain.owner.service.OwnerService;
import study.deliveryhanghae.global.config.security.UserDetailsImpl;
import study.deliveryhanghae.global.handler.exception.BusinessException;

import java.util.List;

@Controller
@RequiredArgsConstructor
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

    /***
     *  사장님 메인 페이지 이동
     * 
     * @param model
     * @return
     */
    @GetMapping("/v2")
    public String getOwnerMain(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails){
        // 사장님 가게 가지고 있는 상태 확인하고 없으면 생성하도록 반환
//        if (userDetails.getUser().isStoreStatus() == false) {
//            return "storeRegister";
//        }
        List<getOrderDto> orderList=orderService.getOrderList();
        GetMainDto mainResponseDto = new GetMainDto(orderList,ownerService.getOwnerPoint(1L));
        model.addAttribute("mainResponseDto",mainResponseDto);
        return "owner";
    }
}
