package study.deliveryhanghae.domain.owner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import study.deliveryhanghae.domain.order.dto.OrderResponseDto.getOrderDto;
import study.deliveryhanghae.domain.order.service.OrderService;
import study.deliveryhanghae.domain.owner.service.OwnerService;

import java.util.List;

@Controller
@RequestMapping("/v2")
public class OwnerController {
    private final OrderService orderService;
    private final OwnerService ownerService;

    public OwnerController(OrderService orderService, OwnerService ownerService) {
        this.orderService = orderService;
        this.ownerService = ownerService;
    }
    @GetMapping
    public String getOwnerMain(Model model){
        List<getOrderDto> orderList=orderService.getOrderList();

        model.addAttribute("orderList",orderList);
        model.addAttribute("ownersPoint", ownerService.getOwnerPoint(1L));

        return "owner";
    }
}
