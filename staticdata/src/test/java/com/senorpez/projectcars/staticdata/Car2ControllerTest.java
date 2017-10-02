package com.senorpez.projectcars.staticdata;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static com.senorpez.projectcars.staticdata.DocumentationCommon.commonLinks;
import static com.senorpez.projectcars.staticdata.SupportedMediaTypes.PROJECT_CARS_2;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.ALL;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class Car2ControllerTest {
    private MockMvc mockMvc;
    private static final MediaType INVALID_MEDIA_TYPE = new MediaType("application", "invalid+json", UTF_8);
    private static final ClassLoader CLASS_LOADER = Car2ControllerTest.class.getClassLoader();
    private static InputStream CAR_SCHEMA;
    private static InputStream CAR_CLASS_SCHEMA;
    private static InputStream CAR_COLLECTION_SCHEMA;
    private static InputStream ERROR_SCHEMA;

    private static final Car2 FIRST_CAR = new CarBuilder()
            .setId(-2046825312)
            .setManufacturer("Ruf")
            .setModel("CTR3 SMS-R")
            .setCarClass(new CarClassBuilder()
                    .setId(1)
                    .setName("GT1X")
                    .build())
            .setLiveries(new HashSet<>(Arrays.asList(
                    new LiveryBuilder().setId(51).setName("Ruf CTR3 SMS-R #32").build(),
                    new LiveryBuilder().setId(52).setName("Ruf CTR3 SMS-R #33").build())))
            .build2();

    private static final Car2 SECOND_CAR = new CarBuilder()
            .setId(844159614)
            .setManufacturer("SMS")
            .setModel("125cc Shifter Kart")
            .setCarClass(new CarClassBuilder()
                    .setId(2)
                    .setName("Kart1")
                    .build())
            .setLiveries(new HashSet<>(Arrays.asList(
                    new LiveryBuilder().setId(51).setName("Mudino #4").build(),
                    new LiveryBuilder().setId(52).setName("Luquitas #11").build())))
            .build2();

    @InjectMocks
    Car2Controller carController;

    @Mock
    private APIService apiService;

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Before
    public void setUp() throws Exception {
        CAR_COLLECTION_SCHEMA = CLASS_LOADER.getResourceAsStream("cars2.schema.json");
        CAR_SCHEMA = CLASS_LOADER.getResourceAsStream("car2.schema.json");
        CAR_CLASS_SCHEMA = CLASS_LOADER.getResourceAsStream("class.schema.json");
        ERROR_SCHEMA = CLASS_LOADER.getResourceAsStream("error.schema.json");
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new Car2Controller(apiService, Arrays.asList(FIRST_CAR, SECOND_CAR)))
                .setMessageConverters(HALMessageConverter.getConverter(Collections.singletonList(ALL)))
                .setControllerAdvice(new APIExceptionHandler())
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Test
    public void getAllCars_ValidAcceptHeader() throws Exception {
        when(apiService.findAll(any())).thenReturn(Arrays.asList(FIRST_CAR, SECOND_CAR));

        mockMvc.perform(get("/cars").accept(PROJECT_CARS_2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(PROJECT_CARS_2))
                .andExpect(content().string(matchesJsonSchema(CAR_COLLECTION_SCHEMA)))
                .andExpect(jsonPath("$._embedded.pcars:car", hasItem(
                        allOf(
                                hasEntry("id", (Object) FIRST_CAR.getId()),
                                hasEntry("name", (Object) FIRST_CAR.getName()),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasEntry("href", String.format("http://localhost:8080/cars/%d", FIRST_CAR.getId()))))))))
                .andExpect(jsonPath("$._embedded.pcars:car", hasItem(
                        allOf(
                                hasEntry("id", (Object) SECOND_CAR.getId()),
                                hasEntry("name", (Object) SECOND_CAR.getName()),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasEntry("href", String.format("http://localhost:8080/cars/%d", SECOND_CAR.getId()))))))))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost:8080/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", "http://localhost:8080/cars")))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost:8080/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))
                .andDo(document("cars2",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Accept")
                                        .description("Accept header")
                                        .attributes(key("acceptvalue").value(SupportedMediaTypes.PROJECT_CARS_2_VALUE))),
                        responseFields(
                                fieldWithPath("_embedded.pcars:car").description("Car resources"),
                                fieldWithPath("_embedded.pcars:car[].id").description("ID number"),
                                fieldWithPath("_embedded.pcars:car[].name").description("Name"),
                                fieldWithPath("_embedded.pcars:car[].class").description("Class"),
                                subsectionWithPath("_links").ignored(),
                                subsectionWithPath("_embedded.pcars:car[]._links").ignored()),
                        commonLinks));

        verify(apiService, times(1)).findAll(any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getAllCars_InvalidAcceptHeader() throws Exception {
        when(apiService.findAll(any())).thenReturn(Arrays.asList(FIRST_CAR, SECOND_CAR));

        mockMvc.perform(get("/cars").accept(INVALID_MEDIA_TYPE))
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
    public void getAllCars_InvalidMethod() throws Exception {
        when(apiService.findAll(any())).thenReturn(Arrays.asList(FIRST_CAR, SECOND_CAR));

        mockMvc.perform(put("/cars").accept(PROJECT_CARS_2))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleCar_ValidId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_CAR);

        mockMvc.perform(get(String.format("/cars/%d", FIRST_CAR.getId())).accept(PROJECT_CARS_2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(PROJECT_CARS_2))
                .andExpect(content().string(matchesJsonSchema(CAR_SCHEMA)))
                .andExpect(jsonPath("$.id", is(FIRST_CAR.getId())))
                .andExpect(jsonPath("$.class", is(FIRST_CAR.getCarClass().getName())))
                .andExpect(jsonPath("$.name", is(FIRST_CAR.getName())))

                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost:8080/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", String.format("http://localhost:8080/cars/%d", FIRST_CAR.getId()))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost:8080/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))

                .andExpect(jsonPath("$._links.pcars:cars", hasEntry("href", "http://localhost:8080/cars")))
                .andExpect(jsonPath("$._links.pcars:class",
                        hasItems(
                                hasEntry("href", String.format("http://localhost:8080/classes/%d", FIRST_CAR.getCarClass().getId())),
                                hasEntry("href", String.format("http://localhost:8080/cars/%d/class", FIRST_CAR.getId()))
                        )))
                .andExpect(jsonPath("$._links.pcars:liveries", hasEntry("href", String.format("http://localhost:8080/cars/%d/liveries", FIRST_CAR.getId()))))
                .andDo(document("car2",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Accept")
                                        .description("Accept header")
                                        .attributes(key("acceptvalue").value(SupportedMediaTypes.PROJECT_CARS_2_VALUE))),
                        responseFields(
                                fieldWithPath("id").description("ID number"),
                                fieldWithPath("name").description("Name"),
                                fieldWithPath("class").description("Class name"),
                                subsectionWithPath("_links").ignored()),
                        commonLinks.and(
                                linkWithRel("pcars:cars").description("List of cars"),
                                linkWithRel("pcars:class").description("Class"),
                                linkWithRel("pcars:liveries").description("List of liveries"))));

        verify(apiService, times(1)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleCar_ValidId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_CAR);

        mockMvc.perform(get(String.format("/cars/%d", FIRST_CAR.getId())).accept(INVALID_MEDIA_TYPE))
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
    public void getSingleCar_ValidId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_CAR);

        mockMvc.perform(put(String.format("/cars/%d", FIRST_CAR.getId())).accept(PROJECT_CARS_2))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleCar_InvalidId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(get("/cars/8675309").accept(PROJECT_CARS_2))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Car with ID of %d not found", 8675309))));

        verify(apiService, times(1)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleCar_InvalidId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(get("/cars/8675309").accept(INVALID_MEDIA_TYPE))
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
    public void getSingleCar_InvalidId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(put("/cars/8675309").accept(PROJECT_CARS_2))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleCarClass_ValidId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_CAR);

        mockMvc.perform(get(String.format("/cars/%d/class", FIRST_CAR.getId())).accept(PROJECT_CARS_2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(PROJECT_CARS_2))
                .andExpect(content().string(matchesJsonSchema(CAR_CLASS_SCHEMA)))
                .andExpect(jsonPath("$.id", is(FIRST_CAR.getCarClass().getId())))
                .andExpect(jsonPath("$.name", is(FIRST_CAR.getCarClass().getName())))

                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost:8080/")))
                .andExpect(jsonPath("$._links.self",
                        hasItems(
                                hasEntry("href", String.format("http://localhost:8080/cars/%d/class", FIRST_CAR.getId())),
                                hasEntry("href", String.format("http://localhost:8080/classes/%d", FIRST_CAR.getCarClass().getId())))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost:8080/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))

                .andExpect(jsonPath("$._links.pcars:car", hasEntry("href", String.format("http://localhost:8080/cars/%d", FIRST_CAR.getId()))))
                .andExpect(jsonPath("$._links.pcars:classes", hasEntry("href", "http://localhost:8080/classes")));

        verify(apiService, times(1)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleCarClass_ValidId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_CAR);

        mockMvc.perform(get(String.format("/cars/%d/class", FIRST_CAR.getId())).accept(INVALID_MEDIA_TYPE))
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
    public void getSingleCarClass_ValidId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_CAR);

        mockMvc.perform(put(String.format("/cars/%d/class", FIRST_CAR.getId())).accept(PROJECT_CARS_2))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));


        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleCarClass_InvalidId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(get("/cars/8675309/class").accept(PROJECT_CARS_2))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Car with ID of %d not found", 8675309))));

        verify(apiService, times(1)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleCarClass_InvalidId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(get("/cars/8675309/class").accept(INVALID_MEDIA_TYPE))
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
    public void getSingleCarClass_InvalidId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(put("/cars/8675309/class").accept(PROJECT_CARS_2))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }
}
