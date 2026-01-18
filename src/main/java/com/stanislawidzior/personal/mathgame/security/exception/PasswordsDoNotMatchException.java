package com.stanislawidzior.personal.mathgame.security.exception;

import com.stanislawidzior.personal.mathgame.security.exception.base.SecurityException;

public class PasswordsDoNotMatchException extends SecurityException {
    public PasswordsDoNotMatchException() {
        super("Passwords do not match.");
    }
}
