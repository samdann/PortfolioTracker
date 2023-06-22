package org.blackchain.exception;

public class AccountException extends RuntimeException {

    private ExceptionType type;

    public AccountException(String message) {
        super(message);
    }

    public AccountException(String message, ExceptionType type) {
        super(type.name() + ": " + message);
    }

}
