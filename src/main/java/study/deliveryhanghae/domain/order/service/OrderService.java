package study.deliveryhanghae.domain.order.service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.deliveryhanghae.domain.menu.entity.Menu;
import study.deliveryhanghae.domain.menu.repository.MenuRepository;
import study.deliveryhanghae.domain.order.dto.OrderRequestDto.PayDto;
import study.deliveryhanghae.domain.order.dto.OrderResponseDto.GetOrderDto;
import study.deliveryhanghae.domain.order.dto.OrderResponseDto.OrderDto;
import study.deliveryhanghae.domain.order.entity.Order;
import study.deliveryhanghae.domain.order.entity.OrderStatusEnum;
import study.deliveryhanghae.domain.order.repository.OrderRepository;
import study.deliveryhanghae.domain.owner.dto.OwnerResponseDto.GetMainDto;
import study.deliveryhanghae.domain.owner.entity.Owner;
import study.deliveryhanghae.domain.owner.repository.OwnerRepository;
import study.deliveryhanghae.domain.store.entity.Store;
import study.deliveryhanghae.domain.user.entity.User;
import study.deliveryhanghae.domain.user.repository.UserRepository;
import study.deliveryhanghae.global.handler.exception.BusinessException;
import study.deliveryhanghae.global.handler.exception.ErrorCode;

import java.util.ArrayList;
import java.util.List;

import static study.deliveryhanghae.global.handler.exception.ErrorCode.ENTITY_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;

    @Transactional(readOnly = true)
    public OrderDto order(Long menuId, Long userId) {
        return getOrderDto(menuId, userId);
    }

    @NotNull
    private OrderDto getOrderDto(Long menuId, Long userId) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MENU));
        User user = userRepository.getReferenceById(userId);

        int remainPoint = calculateRemain(user.getPoint(), menu.getPrice());
        String remainPontStr = (remainPoint == -1) ? "잔고가 부족합니다." : String.valueOf(remainPoint);

        return new OrderDto(menu.getId(), menu.getName(), menu.getImageUrl(), menu.getPrice(), user.getPoint(), remainPontStr, userId);
    }

    private int calculateRemain(int userPoint, int menuPrice) {
        if (userPoint < menuPrice) {
            //한도 초과시 동작
            return -1;
        }
        return userPoint - menuPrice;
    }

    @Transactional
    public int pay(PayDto requestDto) {
        User user = userRepository.findById(requestDto.id()).orElseThrow(() ->
                new BusinessException(ENTITY_NOT_FOUND));
        Menu menu = menuRepository.findById(requestDto.menuId()).orElseThrow(() ->
                new BusinessException(ENTITY_NOT_FOUND));
        int remainPont = calculateRemain(user.getPoint(), menu.getPrice());
        if (remainPont == -1) {
            return -1;
        } else {
            user.updatePoint(calculateRemain(user.getPoint(), menu.getPrice()));
            createOrder(menu, user);
        }
        return user.getPoint();
    }

    @Transactional
    public void createOrder(Menu menu, User user) {
        Store store = menu.getStore();
        Owner owner = store.getOwner();

        Order order = Order.builder()
                .user(user)
                .menu(menu)
                .store(store)
                .owner(owner)
                .orderStatus(OrderStatusEnum.PREPARING)
                .build();

        orderRepository.save(order);
    }

    @Transactional
    public void processUserPayment(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new BusinessException(ENTITY_NOT_FOUND));
        Menu menu = order.getMenu();
        Owner owner = menu.getStore().getOwner();
        owner.updatePoint(menu.getPrice());
    }


    @Transactional(readOnly = true)
    public GetMainDto getOrderList(Owner owner) {

        List<Order> orders = orderRepository.findAllWithMenuByOwner(owner);

        List<GetOrderDto> orderList = new ArrayList<>();
        for (Order order : orders) {
            orderList.add(
                    new GetOrderDto(
                            order.getId(),
                            order.getOrderStatus().getStatus(),
                            order.getCreateAt(),
                            order.getMenu().getName())
            );
        }
        // 사장님 포인트 가졍오기
        int ownersPoint =0;

        if(orders.isEmpty()){
            ownersPoint = ownerRepository.findPointById(owner.getId());
            return new GetMainDto(orderList, ownersPoint);
        }
        ownersPoint =  orders.get(0).getOwner().getPoint();
        return new GetMainDto(orderList, ownersPoint);
    }

    @Transactional
    public void updateOrderStatus(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new BusinessException(ENTITY_NOT_FOUND));
        order.updateOrderStatus();
    }

}
