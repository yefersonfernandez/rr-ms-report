package com.onclass.report.exceptions;

import com.onclass.report.enums.ExceptionStatusCode;

public class PersonMicroserviceException extends BusinessException {
    public PersonMicroserviceException(String message) {
        super(ExceptionStatusCode.INTERNAL_SERVER_ERROR, message, 500);
    }
}

