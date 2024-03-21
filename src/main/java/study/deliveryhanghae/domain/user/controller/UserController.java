package study.deliveryhanghae.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import study.deliveryhanghae.domain.user.dto.UserRequestDto.SignupRequestRecord;
import study.deliveryhanghae.domain.user.service.UserService;
import study.deliveryhanghae.global.handler.exception.BusinessException;

import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j(topic = "회원가입, 로그인")
@Tag(name = "User API", description = "회원가입, 로그인")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/v1/signup")
    public String signupPage(Model model) {
        model.addAttribute("ErrorCode", null);
        model.addAttribute("ErrorMessage", null);
        return "signup";
    }

    @GetMapping("/v1/login")
    public String loginPage() {
        return "login";
    }

    @Operation(summary = "회원가입", description = "회원 가입시 필요한 정보를 입력합니다.")
    @PostMapping("/signup")
    public String signup(SignupRequestRecord requestDto, Model model) {
        try {
            userService.signUp(requestDto);
        } catch (BusinessException ex) {
            model.addAttribute("ErrorCode", ex.getErrorCode().getStatus());
            model.addAttribute("ErrorMessage", ex.getErrorCode().getMessage());
            return "signup";
        }
        return "redirect:/v1/login";
    }


    // 인증 이메일 전송
    @PostMapping("/mailSend")
    @ResponseBody
    public HashMap<String, Object> mailSend(@RequestBody Map<String, String> body) {
        String mail = body.get("mail");
        HashMap<String, Object> map = new HashMap<>();
        int number;
        try {
            number = userService.sendMail(mail);
            String num = String.valueOf(number);
            map.put("success", Boolean.TRUE);
            map.put("number", num);
        } catch (Exception e) {
            map.put("success", Boolean.FALSE);
            map.put("error", e.getMessage());
        }
        return map;
    }

}
