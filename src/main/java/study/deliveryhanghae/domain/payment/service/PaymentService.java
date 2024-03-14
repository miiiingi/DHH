package study.deliveryhanghae.domain.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.deliveryhanghae.domain.order.repository.OrderRepository;
import study.deliveryhanghae.domain.payment.dto.PaymentRequestDto.PayRequestDto;
import study.deliveryhanghae.domain.user.entity.User;
import study.deliveryhanghae.domain.user.repository.UserRepository;
import study.deliveryhanghae.global.config.security.jwt.JwtUtil;
import study.deliveryhanghae.global.handler.exception.BusinessException;

import static study.deliveryhanghae.global.handler.exception.ErrorCode.ENTITY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public Long pay(PayRequestDto requestDto) {
        User user = userRepository.findById(requestDto.id()).orElseThrow(() ->
                new BusinessException(ENTITY_NOT_FOUND));
        //유저 보유 포인트
        Long availablePoints = user.getPoint();
        //구매할 물건의 가격 포인트
        Long PurchasePoints = requestDto.price();
        //한도초과 확인
        if (availablePoints < PurchasePoints) {
            //한도 초과시 동작
            return 0L;
        }
        user.updatePoint(availablePoints - PurchasePoints);
        return user.getPoint();
    }
}
