package study.deliveryhanghae.domain.charge.controller;

import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import study.deliveryhanghae.domain.charge.dto.ChargeRequestDto.ChargeCallBackDto;
import study.deliveryhanghae.domain.charge.dto.ChargeResponseDto.ChargeDto;
import study.deliveryhanghae.domain.charge.service.ChargeService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChargeController {
    private final ChargeService chargeService;

    @GetMapping("/charge")
    public String chargePage(Model model){
        Long userId = 1L;
        ChargeDto chargeDto = chargeService.preChargeProcess(userId);
        model.addAttribute("chargeDto",chargeDto);
        return "charge";
    }
    @ResponseBody
    @PostMapping("/charge")
    public String validationPayment(@RequestBody ChargeCallBackDto request) {
        chargeService.chargeCallback(request);
        return "/";
    }
}
