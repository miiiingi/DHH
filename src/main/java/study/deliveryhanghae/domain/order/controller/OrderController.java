package study.deliveryhanghae.domain.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import study.deliveryhanghae.domain.order.dto.OrderRequestDto.PayDto;
import study.deliveryhanghae.domain.order.dto.OrderResponseDto.OrderDto;
import study.deliveryhanghae.domain.order.service.OrderService;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @RequestMapping(value = "/v1/orders/{menuId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String order(@PathVariable Long menuId, Model model) {
        Long userId = 1L;
        OrderDto responseDto = orderService.order(menuId, userId);
        model.addAttribute("responseDto", responseDto);
        return "order";
    }

    @PostMapping("/v1/orders/payment")
    public String pay(@ModelAttribute PayDto requestDto, Model model) {
        int remainingPoints = orderService.pay(requestDto);
        model.addAttribute("remainingPoints", remainingPoints);
        return "pay";
    }

    @RequestMapping(value="/v2/deliverys/{orderId}", method = { RequestMethod.POST})
    public String delivery(@PathVariable Long orderId){
        orderService.updateOrderStatus(orderId);
        orderService.processUserPayment(orderId);
        return "redirect:/v2";
    }

}
