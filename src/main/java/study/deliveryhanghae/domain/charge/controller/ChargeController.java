package study.deliveryhanghae.domain.charge.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import study.deliveryhanghae.domain.charge.dto.ChargeRequestDto.ChargeCallBackDto;
import study.deliveryhanghae.domain.charge.dto.ChargeResponseDto.ChargeDto;
import study.deliveryhanghae.domain.charge.service.ChargeService;
import study.deliveryhanghae.global.config.security.user.UserDetailsImpl;

@Slf4j
@Controller
@RequiredArgsConstructor
@Tag(name = "Charge API", description = "결제 관련 API")
public class ChargeController {
    private final ChargeService chargeService;

    @Operation(summary = "결제 페이지", description = "유저의 결제 항목을 조회합니다.")
    @GetMapping("/v2/charge")
    public String chargePage(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model){
        ChargeDto chargeDto = chargeService.preChargeProcess(userDetails.getUser().getId());
        model.addAttribute("chargeDto", chargeDto);
        return "charge";
    }

    @Operation(summary = "결제", description = "유저가 선택한 메뉴를 결제합니다.")
    @PostMapping("/v2/charge")
    public String validationPayment(@AuthenticationPrincipal UserDetailsImpl userDetails,@RequestBody ChargeCallBackDto request) {
        chargeService.chargeCallback(request,userDetails.getUser().getId());
        return "redirect:/v1";
    }
}
