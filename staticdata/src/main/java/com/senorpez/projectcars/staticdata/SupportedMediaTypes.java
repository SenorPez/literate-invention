package com.senorpez.projectcars.staticdata;

import org.springframework.http.MediaType;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

class SupportedMediaTypes {
    static final MediaType PROJECT_CARS = new MediaType("application", "vnd.senorpez.pcars.v1+json", UTF_8);
    static final String PROJECT_CARS_VALUE = "application/vnd.senorpez.pcars.v1+json; charset=UTF-8";

    static final MediaType PROJECT_CARS_2 = new MediaType("application", "vnd.senorpez.pcars2.v1+json", UTF_8);
    static final String PROJECT_CARS_2_VALUE = "application/vnd.senorpez.pcars2.v1+json; charset=UTF-8";

    static final MediaType FALLBACK = APPLICATION_JSON_UTF8;
    static final String FALLBACK_VALUE = APPLICATION_JSON_UTF8_VALUE;
}
