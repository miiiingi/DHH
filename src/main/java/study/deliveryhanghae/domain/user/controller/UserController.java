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
import study.deliveryhanghae.domain.user.dto.LoginRequestRecord;
import study.deliveryhanghae.domain.user.dto.SignupRequestRecord;
import study.deliveryhanghae.domain.user.service.UserService;
import study.deliveryhanghae.global.handler.exception.BusinessException;

@Controller
@Slf4j(topic = "회원가입, 로그인")
@Tag(name = "User API", description = "회원가입, 로그인")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("favicon.ico")
    @ResponseBody
    public void returnNoFavicon() {
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("ErrorCode", null);
        model.addAttribute("ErrorMessage", null);
        return "signup";
    }

    @GetMapping("/login-page")
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
        return "redirect:/login-page";
    }

    @Operation(summary = "유저 로그인", description = "유저 로그인 사용할 정보를 입력합니다.")
    @PostMapping("/login")
    public void login(@RequestBody LoginRequestRecord requestDto) {

    }
}
