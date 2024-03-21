package study.deliveryhanghae.domain.owner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import study.deliveryhanghae.domain.owner.dto.OwnerRequestDto;
import study.deliveryhanghae.domain.owner.entity.Owner;
import study.deliveryhanghae.domain.owner.repository.OwnerRepository;
import study.deliveryhanghae.domain.user.dto.SignupRequestRecord;
import study.deliveryhanghae.global.handler.exception.BusinessException;
import study.deliveryhanghae.global.handler.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(OwnerRequestDto.SignupRequestDto requestDto) {
        if (ownerRepository.findByEmail(requestDto.email()).isPresent()) {
            throw new BusinessException(ErrorCode.ALREADY_EXIST_EMAIL);
        }

        String password = passwordEncoder.encode(requestDto.password());

        Owner owner = Owner.builder()
                .email(requestDto.email())
                .password(password)
                .name(requestDto.name())
                .build();
        ownerRepository.save(owner);
    }


    /***
     * 사장님 메인 페이지에 보내 줄 포인트 얻기
     * @param ownerID
     * @return
     */
    public int getOwnerPoint(Long ownerID){
        return ownerRepository.findPointById(ownerID);
    }
}
