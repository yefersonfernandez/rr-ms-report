package com.onclass.report.exceptions;


import com.onclass.report.enums.ExceptionStatusCode;

public class TechnologyMicroserviceException extends BusinessException {
    public TechnologyMicroserviceException(String message) {
        super(ExceptionStatusCode.INTERNAL_SERVER_ERROR, message, 500);
    }
}

