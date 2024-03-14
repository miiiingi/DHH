package study.deliveryhanghae.domain.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import study.deliveryhanghae.domain.payment.dto.PaymentRequestDto.PayRequestDto;
import study.deliveryhanghae.domain.payment.service.PaymentService;

@Controller
@RequestMapping("/orders/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping
    public String orderPage() {
        return "order";
    }

    @PostMapping
    public String pay(@ModelAttribute PayRequestDto requestDto, Model model) {
        Long remainingPoints = paymentService.pay(requestDto);
        model.addAttribute("remainingPoints", remainingPoints);
        return "success_pay";
    }

}
