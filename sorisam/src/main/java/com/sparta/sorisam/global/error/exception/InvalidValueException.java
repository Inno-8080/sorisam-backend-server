package com.sparta.sorisam.global.error.exception;

public class InvalidValueException extends BusinessException{
    public InvalidValueException(ErrorCode errorCode) {
        super(errorCode);
    }
}
