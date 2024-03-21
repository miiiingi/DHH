package study.deliveryhanghae.domain.owner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class OwnerRequestDto {

    public record SignupRequestDto(
            @NotNull(message = "이메일 필수")
            @Email(message = "올바른 이메일 형식을 입력해주세요.")
            @Schema(description = "이메일", example = "test@test.com")
            String email,
            @Size(min = 8, max = 15, message = "비밀번호 8자 이상, 15자 이하로 입력해야 합니다.")
            @Schema(description = "비밀번호", example = "12345678a1!")
            String password,

            @Schema(description = "이름", example = "김르탄")
            String name

    ) {
    }

    public record LoginRequestDto(
            @NotNull(message = "이메일 필수")
            @Email(message = "올바른 이메일 형식을 입력해주세요.")
            @Schema(description = "이메일", example = "test@test.com")
            String email,
            @Size(min = 8, max = 15, message = "비밀번호 8자 이상, 15자 이하로 입력해야 합니다.")
            @Schema(description = "비밀번호", example = "12345678a1!")
            String password

    ) {
    }

}
