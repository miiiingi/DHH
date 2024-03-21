package study.deliveryhanghae.domain.charge.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.deliveryhanghae.domain.charge.dto.ChargeRequestDto.ChargeCallBackDto;
import study.deliveryhanghae.domain.charge.dto.ChargeResponseDto.ChargeDto;
import study.deliveryhanghae.domain.charge.entity.Charge;
import study.deliveryhanghae.domain.charge.repository.ChargeRepository;
import study.deliveryhanghae.domain.user.entity.User;
import study.deliveryhanghae.domain.user.repository.UserRepository;
import study.deliveryhanghae.global.handler.exception.BusinessException;

import java.util.UUID;

import static study.deliveryhanghae.global.handler.exception.ErrorCode.ENTITY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ChargeService {
    private final ChargeRepository chargeRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public ChargeDto preChargeProcess(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() ->
                new BusinessException(ENTITY_NOT_FOUND));
        String chargeUid = UUID.randomUUID().toString();
        String chargeName = user.getId()+chargeUid;
        return new ChargeDto(user,chargeUid,chargeName);
    }

    @Transactional
    public void chargeCallback(ChargeCallBackDto request){
        Long userId = 1L;
        User user = userRepository.findById(userId).orElseThrow(() ->
                new BusinessException(ENTITY_NOT_FOUND));
        int point = Integer.parseInt(request.amount());
        chargeRepository.save(new Charge(request.charge_uid(),point,user));
        user.updatePoint(point);
    }
}
