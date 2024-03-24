package study.deliveryhanghae.domain.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import study.deliveryhanghae.domain.order.dto.OrderRequestDto.PayDto;
import study.deliveryhanghae.domain.order.dto.OrderResponseDto.OrderDto;
import study.deliveryhanghae.domain.order.service.OrderService;
import study.deliveryhanghae.global.config.security.user.UserDetailsImpl;

@Controller
@RequiredArgsConstructor
@Tag(name = "Order API", description = "주문, 배달 관련 API")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "선택 메뉴 상세 페이지 이동", description = "선택한 메뉴의 상세페이지로 이동합니다.")
    @RequestMapping(value = "/v1/orders/{menuId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String order(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long menuId, Model model) {
        OrderDto responseDto = orderService.order(menuId, userDetails.getUser().getId());
        model.addAttribute("responseDto", responseDto);
        return "order";
    }

    @Operation(summary = "선택 메뉴 결제 페이지 이동", description = "선택한 메뉴의 결제페이지로 이동합니다.")
    @PostMapping("/v1/orders/payment")
    public String pay(@ModelAttribute PayDto requestDto, Model model) {
        int remainingPoints = orderService.pay(requestDto);
        model.addAttribute("remainingPoints", remainingPoints);
        return "pay";
    }

    @Operation(summary = "선택 주문 배달 완료", description = "선택한 주문의 배달 완료를 설정합니다.")
    @RequestMapping(value="/v2/deliverys/{orderId}", method = { RequestMethod.POST})
    public String delivery(@PathVariable Long orderId){
        orderService.updateOrderStatus(orderId);
        orderService.processUserPayment(orderId);
        return "redirect:/v2";
    }

}
