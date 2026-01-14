package com.onclass.report.model.exceptions;

import com.onclass.report.model.enums.ExceptionStatusCode;

public class PersonMicroserviceException extends BusinessException {
    public PersonMicroserviceException(String message) {
        super(ExceptionStatusCode.INTERNAL_SERVER_ERROR, message, 500);
    }
}

