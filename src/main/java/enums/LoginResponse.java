package enums;

public enum LoginResponse {
    USERNAME_TAKEN(""),
    WEAK_PASS(""),
    SUCCESSFUL(""),
    MISMATCH(""), WRONG_PASSWORD("");

    private String message;

    LoginResponse(String message) {
        this.message = message;
    }
}
