package study.deliveryhanghae.global.handler.exception;


public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", " Invalid Input Value"),
    ENTITY_NOT_FOUND(400, "C003", " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    INVALID_TYPE_VALUE(400, "C005", " Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "C006", "Forbidden Access"),
    UNAUTHORIZED(401, "C007", "Unauthorized"),

    // Member
    ALREADY_EXIST_EMAIL(400, "M001", "이미 가입된 이메일입니다."),
    ACCESS_DENIED_ADMIN(400, "M002", "관리자 암호가 틀려 가입할 수 없습니다."),
    ALREADY_EXIST_NICKNAME(400, "M003", "이미 가입된 닉네임입니다."),
    NOT_MATCH_EMAIL_PASSWORD(400, "M004", "이메일 혹은 비밀번호가 틀렸습니다. 이메일 혹은 비밀번호를 확인하세요."),
    EMAIL_SEND_FAILURE(400, "M005", "이메일이 발송되지 않았습니다. 이메일을 다시 확인하세요."),

    // Pay
    PAYMENT_REQUIRED(402, "P001","잔액 부족입니다."),

    // Order
    NOT_FOUND_MENU(400, "O001", "해당하는 메뉴가 없습니다."),

    // Cart
    NOT_FOUND_CART(400, "C001", "장바구니가 비어있습니다."),
    ACCESS_DENIED_MEMBER(400, "C002", "접근할 수 없는 장바구니 입니다."),

    // STORE
    NOT_FOUND_STORE(400, "S001", "가게 정보가 없습니다."),
    NOT_FOUND_STORE_MENU(400, "S002", "해당하는 메뉴가 없습니다."),
    REDIS_ERROR(500, "R001", "Redis Error"),
    INVALID_JWT(502, "R002", "Jwt Invalid Error");
    private final String code;
    private final String message;
    private final int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }


}