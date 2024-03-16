package study.deliveryhanghae.domain.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import study.deliveryhanghae.domain.order.dto.OrderRequestDto.PayDto;
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

        OrderResponseDto.OrderDto responseDto = orderService.order(menuId, userId);

        model.addAttribute("responseDto", responseDto);

        return "payment";
    }

    @PostMapping("/payment")
    public String pay(@ModelAttribute PayDto requestDto, Model model) {
        int remainingPoints = orderService.pay(requestDto);
        model.addAttribute("remainingPoints", remainingPoints);
        return "pay";
    }

}
