package com.senorpez.projectcars.staticdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

@RestControllerAdvice
class APIExceptionHandler {
    @ExceptionHandler({
            CarNotFoundException.class,
            CarClassNotFoundException.class,
            EventNotFoundException.class,
            RoundNotFoundException.class,
            RaceNotFoundException.class,
            LiveryNotFoundException.class})
    ResponseEntity<ErrorResponse> handleAPIObjectNotFound(final Exception e) {
        return ResponseEntity
                .status(NOT_FOUND)
                .contentType(APPLICATION_JSON_UTF8)
                .body(
                        new ErrorResponse(NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    ResponseEntity<ErrorResponse> handle405MethodNotAllowed() {
        return ResponseEntity
                .status(METHOD_NOT_ALLOWED)
                .contentType(APPLICATION_JSON_UTF8)
                .body(
                        new ErrorResponse(METHOD_NOT_ALLOWED, "Only GET methods allowed."));
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)

    ResponseEntity<ErrorResponse> handle406NotAcceptable() {
        return ResponseEntity
                .status(NOT_ACCEPTABLE)
                .contentType(APPLICATION_JSON_UTF8)
                .body(
                        new ErrorResponse(NOT_ACCEPTABLE, "Accept header must be \"vnd.senorpez.pcars.v1+json\" for Project CARS " +
                                "or \"vnd.senorpez.pcars2.v1+json\" for Project CARS 2"));
    }

    private class ErrorResponse {
        @JsonProperty("code")
        private final int code;
        @JsonProperty("message")
        private final String message;
        @JsonProperty("detail")
        private final String detail;

        private ErrorResponse(final HttpStatus httpStatus, final String detail) {
            this.code = httpStatus.value();
            this.message = httpStatus.getReasonPhrase();
            this.detail = detail;
        }
    }
}
