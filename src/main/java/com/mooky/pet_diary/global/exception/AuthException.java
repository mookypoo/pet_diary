package com.mooky.pet_diary.global.exception;

/**
 * <pre>
 * AUTH_001 invalid credentials = when user is not authorized to access certain content
 * AUTH_002 invalid login = incorrect email, password or google id token
 * AUTH_003 missing jwt token
 * AUTH_004 invalid jwt token (eg. issuer does not match or other JwtException)
 * AUTH_005 expired jwt token
 * </pre>
 */
public class AuthException extends ApiException {

    private AuthException(String error, String errorMessage, String errorCode, String errorValue, String errorTitle) {
        super(error, errorMessage, errorCode, errorValue, errorTitle, 404);
    }

    /**
     * @param errorValue
     * @param errorMessage can be null
     */
    public static AuthException invalidCredentials(String errorValue, String errorMessage) {
        String errorMsg = errorMessage != null ? errorMessage : "you are not authorized to access this content";
        return new AuthException(
                "invalid_credentials",
                errorMsg,
                "AUTH_001",
                errorValue,
                null);
    }
    
    public static AuthException invalidLogin(String errorMessage, String errorValue, String errorTitle) {
        return new AuthException("auth_exception", errorMessage, "AUTH_002", errorValue, errorTitle);
    }

    public static AuthException missingJwtToken() {
        return new AuthException("missing_access_token", "missing jwt token", "AUTH_003", null, "jwt_token_error");
    }

    public static AuthException invalidJwtToken(String errorMessage) {
        return new AuthException("invalid_access_token", errorMessage, "AUTH_004", null,
                "jwt_token_error");
    }

    public static AuthException expiredJwtToken() {
        return new AuthException("expired_jwt_token", "please send refresh token to server", "AUTH_005", null,
                "jwt_token_error");
    }
    
}

