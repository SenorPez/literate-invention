package com.senorpez.projectcars.staticdata;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.InputStream;
import java.util.Collections;

import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static com.senorpez.projectcars.staticdata.SupportedMediaTypes.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.ALL;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class RootControllerTest {
    private MockMvc mockMvc;
    private final RootController rootController = new RootController();
    private static final MediaType INVALID_MEDIA_TYPE = new MediaType("application", "invalid+json", UTF_8);
    private static final ClassLoader CLASS_LOADER = RootControllerTest.class.getClassLoader();
    private static InputStream OBJECT_SCHEMA;
    private static InputStream OBJECT2_SCHEMA;
    private static InputStream ERROR_SCHEMA;

    @Before
    public void setUp() throws Exception {
        OBJECT_SCHEMA = CLASS_LOADER.getResourceAsStream("root.schema.json");
        OBJECT2_SCHEMA = CLASS_LOADER.getResourceAsStream("root2.schema.json");
        ERROR_SCHEMA = CLASS_LOADER.getResourceAsStream("error.schema.json");
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new RootController())
                .setMessageConverters(HALMessageConverter.getConverter(Collections.singletonList(ALL)))
                .setControllerAdvice(new APIExceptionHandler())
                .build();
    }

    @Test
    public void getRoot_ValidAcceptHeader() throws Exception {
        mockMvc.perform(get("/").accept(PROJECT_CARS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(PROJECT_CARS))
                .andExpect(content().string(matchesJsonSchema(OBJECT_SCHEMA)))
                .andExpect(jsonPath("$._links.self", hasEntry("href", "http://localhost/")))
                .andExpect(jsonPath("$._links.pcars:cars", hasEntry("href", "http://localhost/cars")))
                .andExpect(jsonPath("$._links.pcars:classes", hasEntry("href", "http://localhost/classes")))
                .andExpect(jsonPath("$._links.pcars:events", hasEntry("href", "http://localhost/events")))
                .andExpect(jsonPath("$._links.pcars:tracks", hasEntry("href", "http://localhost/tracks")))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))));
    }

    @Test
    public void getRoot_FallbackAcceptHeader() throws Exception {
        mockMvc.perform(get("/").accept(FALLBACK))
                .andExpect(status().isOk())
                .andExpect(content().contentType(FALLBACK))
                .andExpect(content().string(matchesJsonSchema(OBJECT_SCHEMA)))
                .andExpect(jsonPath("$._links.self", hasEntry("href", "http://localhost/")))
                .andExpect(jsonPath("$._links.pcars:cars", hasEntry("href", "http://localhost/cars")))
                .andExpect(jsonPath("$._links.pcars:classes", hasEntry("href", "http://localhost/classes")))
                .andExpect(jsonPath("$._links.pcars:events", hasEntry("href", "http://localhost/events")))
                .andExpect(jsonPath("$._links.pcars:tracks", hasEntry("href", "http://localhost/tracks")))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))));
    }

    @Test
    public void getRoot_InvalidAcceptHeader() throws Exception {
        mockMvc.perform(get("/").accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)));
    }

    @Test
    public void getRoot_InvalidMethod() throws Exception {
        mockMvc.perform(put("/").accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)));
    }

    @Test
    public void getRoot2_ValidAcceptHeader() throws Exception {
        mockMvc.perform(get("/").accept(PROJECT_CARS_2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(PROJECT_CARS_2))
                .andExpect(content().string(matchesJsonSchema(OBJECT2_SCHEMA)))
                .andExpect(jsonPath("$._links.self", hasEntry("href", "http://localhost/")))
                .andExpect(jsonPath("$._links.pcars:cars", hasEntry("href", "http://localhost/cars")))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))));
    }

    @Test
    public void getRoot2_InvalidAcceptHeader() throws Exception {
        mockMvc.perform(get("/").accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)));
    }

    @Test
    public void getRoot2_InvalidMethod() throws Exception {
        mockMvc.perform(put("/").accept(PROJECT_CARS_2))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)));
    }
}