package com.JPA.redis.Exception;

public enum ErrorCode {

    USER_NOT_FOUND(1001, "User not found"),
    TOKEN_CAN_NOT_BE_REFRESHED(1002, "Token can not be refreshed"),
    USERNAME_ALREADY_EXISTS(1002, "Username already exists"),
    TOKEN_REVOKED(1003, "Token revoked");

    private final int code;
    private final String message;

    ErrorCode(
            int code,
            String message
    ) {
        this.code = code;
        this.message = message;
    }
    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }


}
