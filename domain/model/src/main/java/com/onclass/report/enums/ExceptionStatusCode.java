package com.onclass.report.enums;

public enum ExceptionStatusCode {
    FIELDS_BAD_REQUEST("400-BD-FIELDS"),
    NOT_FOUND("404-NF"),
    INTERNAL_SERVER_ERROR("500-ISE"),
    OK("200-OK");

    private final String statusCode;

    ExceptionStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String status() {
        return statusCode;
    }
}
