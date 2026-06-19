package com.ferreira.taskify_api.exception;

import java.time.Instant;
import java.util.Map;

public record ValidationErrorResponse(
        Instant timestamp,
        Integer status,
        String error,
        Map<String, String> errors,
        String path
) {
}
