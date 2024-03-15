package study.deliveryhanghae.domain.order.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.deliveryhanghae.domain.order.dto.OrderRequestDto;
import study.deliveryhanghae.domain.order.dto.OrderResponseDto;
import study.deliveryhanghae.domain.order.entity.Order;
import study.deliveryhanghae.domain.order.repository.OrderRepository;
import study.deliveryhanghae.domain.store.entity.Menu;
import study.deliveryhanghae.domain.store.repository.MenuRepository;
import study.deliveryhanghae.domain.user.entity.User;
import study.deliveryhanghae.domain.user.repository.UserRepository;
import study.deliveryhanghae.global.handler.exception.BusinessException;
import study.deliveryhanghae.global.handler.exception.ErrorCode;

import static study.deliveryhanghae.global.handler.exception.ErrorCode.ENTITY_NOT_FOUND;
import static study.deliveryhanghae.global.handler.exception.ErrorCode.PAYMENT_REQUIRED;

;

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

    @Transactional
    public OrderResponseDto.OrderDto order(Long menuId, Long userId) {
        return getOrderDto(menuId, userId, menuRepository, userRepository, orderRepository);
    }

    @NotNull
    static OrderResponseDto.OrderDto getOrderDto(Long menuId, Long userId, MenuRepository menuRepository, UserRepository userRepository, OrderRepository orderRepository) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MENU));

        User user = userRepository.getReferenceById(userId);

        Order order = Order.builder()
                .user(user)
                .menu(menu)
                .build();

        orderRepository.save(order);

        String remainPoint = "";

        remainPoint = String.valueOf(user.getPoint() - menu.getPrice());

        if (menu.getPrice() > user.getPoint()) {
            remainPoint = "잔고가 부족합니다.";
        }

        return new OrderResponseDto.OrderDto(menu.getId(), menu.getName(), menu.getImageUrl(), menu.getPrice(), user.getPoint(), remainPoint, userId);
    }

    @Transactional
    public int pay(OrderRequestDto.PayDto requestDto) {
        User user = userRepository.findById(requestDto.id()).orElseThrow(() ->
                new BusinessException(ENTITY_NOT_FOUND));
        //유저 보유 포인트
        int availablePoints = user.getPoint();
        //구매할 물건의 가격 포인트
        int PurchasePoints = requestDto.price();
        //한도초과 확인
        if (availablePoints < PurchasePoints) {
            //한도 초과시 동작
            throw new BusinessException(PAYMENT_REQUIRED);
        }
        user.updatePoint(availablePoints - PurchasePoints);
        return user.getPoint();
    }
}
