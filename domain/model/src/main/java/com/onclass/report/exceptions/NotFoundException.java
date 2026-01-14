package com.onclass.report.exceptions;


import com.onclass.report.enums.ExceptionStatusCode;

public class NotFoundException extends BusinessException {
    public NotFoundException(String message) {
        super(ExceptionStatusCode.NOT_FOUND, message, 404);
    }
}
