package com.onclass.report.model.exceptions;


import com.onclass.report.model.enums.ExceptionStatusCode;

public class TechnologyMicroserviceException extends BusinessException {
    public TechnologyMicroserviceException(String message) {
        super(ExceptionStatusCode.INTERNAL_SERVER_ERROR, message, 500);
    }
}

