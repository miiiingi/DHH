package study.deliveryhanghae.domain.order.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import study.deliveryhanghae.domain.order.service.OrderService;

@Controller
@RequestMapping
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(value = "/orders/{menuId}", method = {RequestMethod.POST, RequestMethod.GET})
    public String order(@PathVariable Long menuId, Model model) {
       Long userId = 1L;
//
//       if (user == null) {
//           return "redirect:/login-page";
//       }

       model.addAttribute("menuName", orderService.order(menuId, userId).menuName());
       model.addAttribute("menuUrl", orderService.order(menuId, userId).menuUrl());
       model.addAttribute("menuPrice", orderService.order(menuId, userId).menuPrice());

       return "payment";
    }

}
