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
import java.util.HashSet;

import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static com.senorpez.projectcars.staticdata.Car.Drivetrain.RWD;
import static com.senorpez.projectcars.staticdata.Car.EnginePosition.MID;
import static com.senorpez.projectcars.staticdata.Car.ShiftPattern.SEQUENTIAL;
import static com.senorpez.projectcars.staticdata.Car.Shifter.PADDLES;
import static com.senorpez.projectcars.staticdata.SupportedMediaTypes.FALLBACK;
import static com.senorpez.projectcars.staticdata.SupportedMediaTypes.PROJECT_CARS;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.ALL;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class LiveryControllerTest {
    private MockMvc mockMvc;
    private static final MediaType INVALID_MEDIA_TYPE = new MediaType("application", "invalid", UTF_8);
    private static final ClassLoader CLASS_LOADER = LiveryControllerTest.class.getClassLoader();
    private static InputStream LIVERY_SCHEMA;
    private static InputStream LIVERY_COLLECTION_SCHEMA;
    private static InputStream ERROR_SCHEMA;

    private static final Livery FIRST_LIVERY = new LiveryBuilder()
            .setId(51)
            .setName("Ruf CTR3 SMS-R #32")
            .build();

    private static final Livery SECOND_LIVERY = new LiveryBuilder()
            .setId(52)
            .setName("Ruf CTR3 SMS-R #33")
            .build();

    private static final CarClass FIRST_CLASS = new CarClassBuilder()
            .setId(1)
            .setName("GT1X")
            .build();

    private static final Car FIRST_CAR = new CarBuilder()
            .setId(-2046825312)
            .setManufacturer("Ruf")
            .setModel("CTR3 SMS-R")
            .setCarClass(FIRST_CLASS)
            .setYear(2014)
            .setCountry("Germany")
            .setDrivetrain(RWD)
            .setEnginePosition(MID)
            .setEngineType("Flat 6")
            .setTopSpeed(355)
            .setHorsepower(760)
            .setAcceleration(2.90f)
            .setBraking(2.18f)
            .setWeight(1155)
            .setTorque(944)
            .setWeightBalance(60)
            .setWheelbase(2.62f)
            .setShiftPattern(SEQUENTIAL)
            .setShifter(PADDLES)
            .setGears(6)
            .setDlc("Modified Car Pack")
            .setLiveries(new HashSet<>(Arrays.asList(
                    FIRST_LIVERY,
                    SECOND_LIVERY)))
            .build();

    @InjectMocks
    LiveryController liveryController;
    
    @Mock
    private APIService apiService;

    @Before
    public void setUp() throws Exception {
        LIVERY_SCHEMA = CLASS_LOADER.getResourceAsStream("livery.schema.json");
        LIVERY_COLLECTION_SCHEMA = CLASS_LOADER.getResourceAsStream("liveries.schema.json");
        ERROR_SCHEMA = CLASS_LOADER.getResourceAsStream("error.schema.json");
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new LiveryController(apiService, Collections.singletonList(FIRST_CAR)))
                .setMessageConverters(HALMessageConverter.getConverter(Collections.singletonList(ALL)))
                .setControllerAdvice(new APIExceptionHandler())
                .build();
    }

    @Test
    public void getAllLiveries_ValidCarId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_CAR);

        mockMvc.perform(get(String.format("/cars/%d/liveries", FIRST_CAR.getId())).accept(PROJECT_CARS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(PROJECT_CARS))
                .andExpect(content().string(matchesJsonSchema(LIVERY_COLLECTION_SCHEMA)))
                .andExpect(jsonPath("$._embedded.pcars:livery", hasItem(
                        allOf(
                                hasEntry("id", (Object) FIRST_LIVERY.getId()),
                                hasEntry("name", (Object) FIRST_LIVERY.getName()),
                                        hasEntry(equalTo("_links"),
                                                hasEntry(equalTo("self"),
                                                        hasEntry("href", String.format("http://localhost/cars/%d/liveries/%d", FIRST_CAR.getId(), FIRST_LIVERY.getId()))))))))
                .andExpect(jsonPath("$._embedded.pcars:livery", hasItem(
                        allOf(
                                hasEntry("id", (Object) SECOND_LIVERY.getId()),
                                hasEntry("name", (Object) SECOND_LIVERY.getName()),
                                        hasEntry(equalTo("_links"),
                                                hasEntry(equalTo("self"),
                                                        hasEntry("href", String.format("http://localhost/cars/%d/liveries/%d", FIRST_CAR.getId(), SECOND_LIVERY.getId()))))))))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", String.format("http://localhost/cars/%d/liveries", FIRST_CAR.getId()))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))));

        verify(apiService, times(1)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getAllLiveries_ValidCarId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_CAR);

        mockMvc.perform(get(String.format("/cars/%d/liveries", FIRST_CAR.getId())).accept(FALLBACK))
                .andExpect(status().isOk())
                .andExpect(content().contentType(FALLBACK))
                .andExpect(content().string(matchesJsonSchema(LIVERY_COLLECTION_SCHEMA)))
                .andExpect(jsonPath("$._embedded.pcars:livery", hasItem(
                        allOf(
                                hasEntry("id", (Object) FIRST_LIVERY.getId()),
                                hasEntry("name", (Object) FIRST_LIVERY.getName()),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasEntry("href", String.format("http://localhost/cars/%d/liveries/%d", FIRST_CAR.getId(), FIRST_LIVERY.getId()))))))))
                .andExpect(jsonPath("$._embedded.pcars:livery", hasItem(
                        allOf(
                                hasEntry("id", (Object) SECOND_LIVERY.getId()),
                                hasEntry("name", (Object) SECOND_LIVERY.getName()),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasEntry("href", String.format("http://localhost/cars/%d/liveries/%d", FIRST_CAR.getId(), SECOND_LIVERY.getId()))))))))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", String.format("http://localhost/cars/%d/liveries", FIRST_CAR.getId()))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))));

        verify(apiService, times(1)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getAllLiveries_ValidId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_CAR);

        mockMvc.perform(get(String.format("/cars/%d/liveries", FIRST_CAR.getId())).accept(INVALID_MEDIA_TYPE))
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
    public void getAllLiveries_ValidId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_CAR);

        mockMvc.perform(put(String.format("/cars/%d/liveries", FIRST_CAR.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getAllLiveries_InvalidCarId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(get("/cars/8675309/liveries").accept(PROJECT_CARS))
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
    public void getAllLiveries_InvalidCarId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(get("/cars/8675309/liveries").accept(FALLBACK))
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
    public void getAllLiveries_InvalidCarId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(get("/cars/8675309/liveries").accept(INVALID_MEDIA_TYPE))
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
    public void getAllLiveries_InvalidCarId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(put("/cars/8675309/liveries").accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleLivery_ValidCarId_ValidLiveryId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_CAR, FIRST_LIVERY);

        mockMvc.perform(get(String.format("/cars/%d/liveries/%d", FIRST_CAR.getId(), FIRST_LIVERY.getId())).accept(PROJECT_CARS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(PROJECT_CARS))
                .andExpect(content().string(matchesJsonSchema(LIVERY_SCHEMA)))
                .andExpect(jsonPath("$.id", is(FIRST_LIVERY.getId())))
                .andExpect(jsonPath("$.name", is(FIRST_LIVERY.getName())))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", String.format("http://localhost/cars/%d/liveries/%d", FIRST_CAR.getId(), FIRST_LIVERY.getId()))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))
                .andExpect(jsonPath("$._links.pcars:liveries", hasEntry("href", String.format("http://localhost/cars/%d/liveries", FIRST_CAR.getId()))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleLivery_ValidCarId_ValidLiveryId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_CAR, FIRST_LIVERY);

        mockMvc.perform(get(String.format("/cars/%d/liveries/%d", FIRST_CAR.getId(), FIRST_LIVERY.getId())).accept(FALLBACK))
                .andExpect(status().isOk())
                .andExpect(content().contentType(FALLBACK))
                .andExpect(content().string(matchesJsonSchema(LIVERY_SCHEMA)))
                .andExpect(jsonPath("$.id", is(FIRST_LIVERY.getId())))
                .andExpect(jsonPath("$.name", is(FIRST_LIVERY.getName())))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", String.format("http://localhost/cars/%d/liveries/%d", FIRST_CAR.getId(), FIRST_LIVERY.getId()))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))
                .andExpect(jsonPath("$._links.pcars:liveries", hasEntry("href", String.format("http://localhost/cars/%d/liveries", FIRST_CAR.getId()))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleLivery_ValidCarId_ValidLiveryId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_CAR, FIRST_LIVERY);

        mockMvc.perform(get(String.format("/cars/%d/liveries/%d", FIRST_CAR.getId(), FIRST_LIVERY.getId())).accept(INVALID_MEDIA_TYPE))
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
    public void getSingleLivery_ValidCarId_ValidLiveryId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_CAR, FIRST_LIVERY);

        mockMvc.perform(put(String.format("/cars/%d/liveries/%d", FIRST_CAR.getId(), FIRST_LIVERY.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleLivery_ValidCarId_InvalidLiveryId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_CAR).thenThrow(new LiveryNotFoundException(8675309));

        mockMvc.perform(get(String.format("/cars/%d/liveries/86753029", FIRST_CAR.getId())).accept(PROJECT_CARS))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Livery with ID of %d not found", 8675309))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleLivery_ValidCarId_InvalidLiveryId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_CAR).thenThrow(new LiveryNotFoundException(8675309));

        mockMvc.perform(get(String.format("/cars/%d/liveries/86753029", FIRST_CAR.getId())).accept(FALLBACK))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Livery with ID of %d not found", 8675309))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleLivery_ValidCarId_InvalidLiveryId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_CAR).thenThrow(new LiveryNotFoundException(8675309));

        mockMvc.perform(get(String.format("/cars/%d/liveries/86753029", FIRST_CAR.getId())).accept(INVALID_MEDIA_TYPE))
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
    public void getSingleLivery_ValidCarId_InvalidLiveryId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_CAR).thenThrow(new LiveryNotFoundException(8675309));

        mockMvc.perform(put(String.format("/cars/%d/liveries/8675309", FIRST_CAR.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleLivery_InvalidCarId_XXXLiveryId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(get(String.format("/cars/8675309/liveries/%d", FIRST_LIVERY.getId())).accept(PROJECT_CARS))
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
    public void getSingleLivery_InvalidCarId_XXXLiveryId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(get(String.format("/cars/8675309/liveries/%d", FIRST_LIVERY.getId())).accept(FALLBACK))
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
    public void getSingleLivery_InvalidCarId_XXXLiveryId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(get(String.format("/cars/8675309/liveries/%d", FIRST_LIVERY.getId())).accept(INVALID_MEDIA_TYPE))
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
    public void getSingleLivery_InvalidCarId_XXXLiveryId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(put(String.format("/cars/8675309/liveries/%d", FIRST_LIVERY.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }
}
