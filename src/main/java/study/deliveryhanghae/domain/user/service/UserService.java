package study.deliveryhanghae.domain.user.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import study.deliveryhanghae.domain.user.dto.UserRequestDto.SignupRequestRecord;
import study.deliveryhanghae.domain.user.entity.User;
import study.deliveryhanghae.domain.user.repository.UserRepository;
import study.deliveryhanghae.global.handler.exception.BusinessException;
import study.deliveryhanghae.global.handler.exception.ErrorCode;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "kimmingi722@gmail.com";

    public String generateRandomNumber() {
        SecureRandom secureRandom = new SecureRandom();
        int number = secureRandom.nextInt(90000) + 100000;
        return String.valueOf(number);
    }

    public MimeMessage createMail(String mail, String number) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + number + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    public String sendMail(String mail) {
        String number = generateRandomNumber();
        MimeMessage message = createMail(mail, number);
        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.EMAIL_SEND_FAILURE);
        }

        return number;
    }

    public void signUp(SignupRequestRecord requestDto) {
        if (userRepository.findByEmail(requestDto.email()).isPresent()) {
            throw new BusinessException(ErrorCode.ALREADY_EXIST_EMAIL);
        }
        if (userRepository.findByNickname(requestDto.nickname()).isPresent()) {
            throw new BusinessException(ErrorCode.ALREADY_EXIST_NICKNAME);
        }
        String password = passwordEncoder.encode(requestDto.password());

        User user = User.builder()
                .email(requestDto.email())
                .password(password)
                .nickname(requestDto.nickname())
                .address(requestDto.address())
                .build();
        userRepository.save(user);
    }
}