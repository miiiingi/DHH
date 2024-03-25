package study.deliveryhanghae.domain.owner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import study.deliveryhanghae.domain.owner.dto.OwnerRequestDto.*;
import study.deliveryhanghae.domain.owner.entity.Owner;
import study.deliveryhanghae.domain.owner.repository.OwnerRepository;
import study.deliveryhanghae.domain.user.repository.UserRepository;
import study.deliveryhanghae.global.handler.exception.BusinessException;
import study.deliveryhanghae.global.handler.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    public void signUp(SignupRequestDto requestDto) {

        if (userRepository.existsByEmail(requestDto.email())){
            throw new BusinessException(ErrorCode.ALREADY_EXIST_EMAIL_USER);
        }

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
}
