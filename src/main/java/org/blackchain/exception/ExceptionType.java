package org.blackchain.exception;

public enum ExceptionType {

    INVALID_ADDRESS("Invalid address");

    private String name;

    ExceptionType(String name) {
        this.name = name;
    }

    ;
}
