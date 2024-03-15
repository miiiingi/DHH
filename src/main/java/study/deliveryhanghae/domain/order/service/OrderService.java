package study.deliveryhanghae.domain.order.service;

import org.springframework.stereotype.Service;
import study.deliveryhanghae.domain.order.dto.OrderRequestDto;
import study.deliveryhanghae.domain.order.dto.OrderResponseDto;
import study.deliveryhanghae.domain.order.repository.OrderRepository;
import study.deliveryhanghae.domain.payment.entity.Menu;
import study.deliveryhanghae.domain.payment.repository.MenuRepository;
import study.deliveryhanghae.domain.user.entity.User;
import study.deliveryhanghae.domain.user.repository.UserRepository;
import study.deliveryhanghae.global.handler.exception.BusinessException;
import study.deliveryhanghae.global.handler.exception.ErrorCode;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final MenuRepository menuRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, MenuRepository menuRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.userRepository = userRepository;
    }

    public OrderResponseDto order(Long menuId, Long userId) {

        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MENU));

        User user = userRepository.getReferenceById(userId);

        OrderRequestDto requestDto = new OrderRequestDto(menu);

        orderRepository.save(requestDto.toEntity(menu, user));

        return new OrderResponseDto(menu.getName(), menu.getImage_url(), menu.getPrice());
    }
}
