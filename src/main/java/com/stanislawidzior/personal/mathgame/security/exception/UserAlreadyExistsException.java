package com.stanislawidzior.personal.mathgame.security.exception;

import com.stanislawidzior.personal.mathgame.security.exception.base.SecurityException;

public class UserAlreadyExistsException extends SecurityException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
