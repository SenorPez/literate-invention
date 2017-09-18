package com.senorpez.projectcars.staticdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
class APIExceptionHandler {
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(CarNotFoundException.class)
    ErrorResponse handleCarNotFound(final Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    ErrorResponse handle405MethodNotAllowed() {
        return new ErrorResponse(METHOD_NOT_ALLOWED);
    }

    @ResponseStatus(NOT_ACCEPTABLE)
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    ErrorResponse handle406NotAcceptable() {
        return new ErrorResponse(NOT_ACCEPTABLE);
    }

    private class ErrorResponse {
        @JsonProperty("code")
        private final int code;
        @JsonProperty("message")
        private final String message;
        @JsonProperty("detail")
        private final String detail;

        private ErrorResponse(final HttpStatus httpStatus) {
            this.code = httpStatus.value();
            this.message = httpStatus.getReasonPhrase();
            this.detail = null;
        }

        private ErrorResponse(final String detail) {
            this.code = HttpStatus.NOT_FOUND.value();
            this.message = HttpStatus.NOT_FOUND.getReasonPhrase();
            this.detail = detail;
        }
    }
}
