package com.onclass.report.enums;

import lombok.Getter;

@Getter
public enum ExceptionMessages {
    BOOTCAMP_NOT_FOUND("Bootcamp not found "),
    WEB_CLIENT_INTERNAL_SERVER_ERROR("Internal server error in the capability microservice.");

    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }
}
