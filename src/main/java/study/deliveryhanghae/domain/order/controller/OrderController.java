package study.deliveryhanghae.domain.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import study.deliveryhanghae.domain.order.dto.OrderRequestDto;
import study.deliveryhanghae.domain.order.dto.OrderResponseDto;
import study.deliveryhanghae.domain.order.service.OrderService;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @RequestMapping(value = "/{menuId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String order(@PathVariable Long menuId, Model model) {
        Long userId = 1L;

        OrderResponseDto.OrderDto order = orderService.order(menuId, userId);

        model.addAttribute("menuName", order.menuName());
        model.addAttribute("menuUrl", order.menuUrl());
        model.addAttribute("menuPrice", order.menuPrice());
        model.addAttribute("point", order.point());
        model.addAttribute("remainPoint", order.remainPoint());

        return "payment";
    }

    @PostMapping("/payment")
    public String pay(@ModelAttribute OrderRequestDto.PayDto requestDto, Model model) {
        Long remainingPoints = orderService.pay(requestDto);
        model.addAttribute("remainingPoints", remainingPoints);
        return "success_pay";
    }

}
