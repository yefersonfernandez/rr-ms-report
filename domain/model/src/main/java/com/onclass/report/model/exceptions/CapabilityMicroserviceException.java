package com.onclass.report.model.exceptions;

import com.onclass.report.model.enums.ExceptionStatusCode;

public class CapabilityMicroserviceException extends BusinessException {
    public CapabilityMicroserviceException(String message) {
        super(ExceptionStatusCode.INTERNAL_SERVER_ERROR, message, 500);
    }
}

