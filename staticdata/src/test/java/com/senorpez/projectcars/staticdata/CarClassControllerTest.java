package com.senorpez.projectcars.staticdata;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;

import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static com.senorpez.projectcars.staticdata.SupportedMediaTypes.FALLBACK;
import static com.senorpez.projectcars.staticdata.SupportedMediaTypes.PROJECT_CARS;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.ALL;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class CarClassControllerTest {
    private MockMvc mockMvc;
    private static final MediaType INVALID_MEDIA_TYPE = new MediaType("application", "invalid", UTF_8);
    private static final ClassLoader CLASS_LOADER = CarClassControllerTest.class.getClassLoader();
    private static InputStream CAR_CLASS_SCHEMA;
    private static InputStream CAR_CLASS_COLLECTION_SCHEMA;
    private static InputStream ERROR_SCHEMA;

    private static final CarClass FIRST_CLASS = new CarClassBuilder()
            .setId(-1289517523)
            .setName("LMP1")
            .build();

    private static final CarClass SECOND_CLASS = new CarClassBuilder()
            .setId(298754909)
            .setName("Road D")
            .build();

    @InjectMocks
    CarClassController carClassController;

    @Mock
    private APIService apiService;

    @Before
    public void setUp() throws Exception {
        CAR_CLASS_SCHEMA = CLASS_LOADER.getResourceAsStream("class.schema.json");
        CAR_CLASS_COLLECTION_SCHEMA = CLASS_LOADER.getResourceAsStream("classes.schema.json");
        ERROR_SCHEMA = CLASS_LOADER.getResourceAsStream("error.schema.json");
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new CarClassController(apiService))
                .setMessageConverters(HALMessageConverter.getConverter(Collections.singletonList(ALL)))
                .setControllerAdvice(new APIExceptionHandler())
                .build();
    }

    @Test
    public void getAllClasses_ValidAcceptHeader() throws Exception {
        when(apiService.findAll(anyCollection())).thenReturn(Arrays.asList(FIRST_CLASS, SECOND_CLASS));

        mockMvc.perform(get("/classes").accept(PROJECT_CARS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(PROJECT_CARS))
                .andExpect(content().string(matchesJsonSchema(CAR_CLASS_COLLECTION_SCHEMA)))
                .andExpect(jsonPath("$._embedded.pcars:class", hasItem(
                        allOf(
                                hasEntry("id", (Object) FIRST_CLASS.getId()),
                                hasEntry("name", (Object) FIRST_CLASS.getName()),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasEntry("href", String.format("http://localhost/classes/%d", FIRST_CLASS.getId()))))))))
                .andExpect(jsonPath("$._embedded.pcars:class", hasItem(
                        allOf(
                                hasEntry("id", (Object) SECOND_CLASS.getId()),
                                hasEntry("name", (Object) SECOND_CLASS.getName()),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasEntry("href", String.format("http://localhost/classes/%d", SECOND_CLASS.getId()))))))))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", "http://localhost/classes")))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))));

        verify(apiService, times(1)).findAll(anyCollection());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getAllClasses_FallbackAcceptHeader() throws Exception {
        when(apiService.findAll(anyCollection())).thenReturn(Arrays.asList(FIRST_CLASS, SECOND_CLASS));

        mockMvc.perform(get("/classes").accept(FALLBACK))
                .andExpect(status().isOk())
                .andExpect(content().contentType(FALLBACK))
                .andExpect(content().string(matchesJsonSchema(CAR_CLASS_COLLECTION_SCHEMA)))
                .andExpect(jsonPath("$._embedded.pcars:class", hasItem(
                        allOf(
                                hasEntry("id", (Object) FIRST_CLASS.getId()),
                                hasEntry("name", (Object) FIRST_CLASS.getName()),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasEntry("href", String.format("http://localhost/classes/%d", FIRST_CLASS.getId()))))))))
                .andExpect(jsonPath("$._embedded.pcars:class", hasItem(
                        allOf(
                                hasEntry("id", (Object) SECOND_CLASS.getId()),
                                hasEntry("name", (Object) SECOND_CLASS.getName()),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasEntry("href", String.format("http://localhost/classes/%d", SECOND_CLASS.getId()))))))))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", "http://localhost/classes")))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))));

        verify(apiService, times(1)).findAll(anyCollection());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getAllClasses_InvalidAcceptHeader() throws Exception {
        when(apiService.findAll(anyCollection())).thenReturn(Arrays.asList(FIRST_CLASS, SECOND_CLASS));

        mockMvc.perform(get("/classes").accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_ACCEPTABLE.value())))
                .andExpect(jsonPath("$.message", is(NOT_ACCEPTABLE.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Accept header must be \"vnd.senorpez.pcars.v1+json\" for Project CARS " +
                        "or \"vnd.senorpez.pcars2.v1+json\" for Project CARS 2")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getAllClasses_InvalidMethod() throws Exception {
        when(apiService.findAll(anyCollection())).thenReturn(Arrays.asList(FIRST_CLASS, SECOND_CLASS));

        mockMvc.perform(put("/classes").accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleClass_ValidId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(anyCollection(), any(), any())).thenReturn(FIRST_CLASS);

        mockMvc.perform(get(String.format("/classes/%d", FIRST_CLASS.getId())).accept(PROJECT_CARS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(PROJECT_CARS))
                .andExpect(content().string(matchesJsonSchema(CAR_CLASS_SCHEMA)))
                .andExpect(jsonPath("$.id", is(FIRST_CLASS.getId())))
                .andExpect(jsonPath("$.name", is(FIRST_CLASS.getName())))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", String.format("http://localhost/classes/%d", FIRST_CLASS.getId()))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))
                .andExpect(jsonPath("$._links.pcars:classes", hasEntry("href", "http://localhost/classes")));

        verify(apiService, times(1)).findOne(anyCollection(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleClass_ValidId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(anyCollection(), any(), any())).thenReturn(FIRST_CLASS);

        mockMvc.perform(get(String.format("/classes/%d", FIRST_CLASS.getId())).accept(FALLBACK))
                .andExpect(status().isOk())
                .andExpect(content().contentType(FALLBACK))
                .andExpect(content().string(matchesJsonSchema(CAR_CLASS_SCHEMA)))
                .andExpect(jsonPath("$.id", is(FIRST_CLASS.getId())))
                .andExpect(jsonPath("$.name", is(FIRST_CLASS.getName())))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", String.format("http://localhost/classes/%d", FIRST_CLASS.getId()))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))
                .andExpect(jsonPath("$._links.pcars:classes", hasEntry("href", "http://localhost/classes")));

        verify(apiService, times(1)).findOne(anyCollection(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleClass_ValidId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(anyCollection(), any(), any())).thenReturn(FIRST_CLASS);

        mockMvc.perform(get(String.format("/classes/%d", FIRST_CLASS.getId())).accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_ACCEPTABLE.value())))
                .andExpect(jsonPath("$.message", is(NOT_ACCEPTABLE.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Accept header must be \"vnd.senorpez.pcars.v1+json\" for Project CARS " +
                        "or \"vnd.senorpez.pcars2.v1+json\" for Project CARS 2")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleClass_ValidId_InvalidMethod() throws Exception {
        when(apiService.findOne(anyCollection(), any(), any())).thenReturn(FIRST_CLASS);

        mockMvc.perform(put(String.format("/classes/%d", FIRST_CLASS.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleClass_InvalidId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(anyCollection(), any(), any())).thenThrow(new CarClassNotFoundException(8675309));

        mockMvc.perform(get("/classes/8675309").accept(PROJECT_CARS))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Class with ID of %d not found", 8675309))));

        verify(apiService, times(1)).findOne(anyCollection(), any(), any());
    }
    
    @Test
    public void getSingleClass_InvalidId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(anyCollection(), any(), any())).thenThrow(new CarClassNotFoundException(8675309));

        mockMvc.perform(get("/classes/8675309").accept(FALLBACK))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Class with ID of %d not found", 8675309))));

        verify(apiService, times(1)).findOne(anyCollection(), any(), any());
    }

    @Test
    public void getSingleClass_InvalidId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(anyCollection(), any(), any())).thenThrow(new CarClassNotFoundException(8675309));

        mockMvc.perform(get("/classes/8675309").accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_ACCEPTABLE.value())))
                .andExpect(jsonPath("$.message", is(NOT_ACCEPTABLE.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Accept header must be \"vnd.senorpez.pcars.v1+json\" for Project CARS " +
                        "or \"vnd.senorpez.pcars2.v1+json\" for Project CARS 2")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleClass_InvalidId_InvalidMethod() throws Exception {
        when(apiService.findOne(anyCollection(), any(), any())).thenThrow(new CarClassNotFoundException(8675309));

        mockMvc.perform(put("/classes/8675309").accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }
}
