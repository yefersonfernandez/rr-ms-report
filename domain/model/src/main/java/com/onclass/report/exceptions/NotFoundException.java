package com.onclass.report.model.exceptions;


import com.onclass.report.model.enums.ExceptionStatusCode;

public class NotFoundException extends BusinessException {
    public NotFoundException(String message) {
        super(ExceptionStatusCode.NOT_FOUND, message, 404);
    }
}
