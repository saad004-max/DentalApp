package ma.emsi.mvc.dto.authentificationDtos;


import java.util.Map;

public class AuthResult {
    private final boolean success;
    private final String message;
    private final UserPrincipal principal;
    private final Map<String, String> fieldErrors;

    private AuthResult(boolean success, String message, UserPrincipal principal, Map<String, String> fieldErrors) {
        this.success = success;
        this.message = message;
        this.principal = principal;
        this.fieldErrors = fieldErrors;
    }

    public static AuthResult success(UserPrincipal principal) {
        return new AuthResult(true, "OK", principal, Map.of());
    }

    public static AuthResult failure(String message) {
        return new AuthResult(false, message, null, Map.of());
    }

    public static AuthResult failure(String message, Map<String, String> fieldErrors) {
        return new AuthResult(false, message, null, fieldErrors);
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public UserPrincipal getPrincipal() { return principal; }
    public Map<String, String> getFieldErrors() { return fieldErrors; }
}
