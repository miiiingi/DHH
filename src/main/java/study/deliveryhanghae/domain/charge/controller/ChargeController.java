package study.deliveryhanghae.domain.charge.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import study.deliveryhanghae.domain.charge.dto.ChargeRequestDto.ChargeCallBackDto;
import study.deliveryhanghae.domain.charge.dto.ChargeResponseDto.ChargeDto;
import study.deliveryhanghae.domain.charge.service.ChargeService;
import study.deliveryhanghae.global.config.security.user.UserDetailsImpl;

@Slf4j
@Controller
@RequiredArgsConstructor
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
    @ResponseBody
    @PostMapping("/v2/charge")
    public String validationPayment(@AuthenticationPrincipal UserDetailsImpl userDetails,@RequestBody ChargeCallBackDto request) {
        chargeService.chargeCallback(request,userDetails.getUser().getId());
        return "/v1";
    }
}
