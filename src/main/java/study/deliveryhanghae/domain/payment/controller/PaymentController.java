package study.deliveryhanghae.domain.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import study.deliveryhanghae.domain.payment.dto.PaymentRequestDto.PayRequestDto;
import study.deliveryhanghae.domain.payment.dto.PaymentRequestDto.OrderRequestDto;
import study.deliveryhanghae.domain.payment.dto.PaymentResponseDto.PayResponseDto;
import study.deliveryhanghae.domain.payment.service.PaymentService;

import java.util.Collections;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    @GetMapping
    public String orderPage(){
        return "order";
    }
    @PostMapping
    public String order(@ModelAttribute OrderRequestDto requestDto, Model model) {
        PayResponseDto responseDto =  paymentService.order(requestDto);
        model.addAttribute("responseDto", responseDto);
        return "payment";
    }

//    @GetMapping("/payment")
//    public String paymentPage() {
//        return "payment";
//    }

    @PostMapping("/payment")
    public String pay(@ModelAttribute PayRequestDto requestDto, Model model) {
        Long remainingPoints = paymentService.pay(requestDto);
        model.addAttribute("remainingPoints", remainingPoints);
        return "success_pay";
    }

}
