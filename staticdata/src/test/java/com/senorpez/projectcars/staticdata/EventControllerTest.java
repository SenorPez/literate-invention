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
import static com.senorpez.projectcars.staticdata.Car.Drivetrain.RWD;
import static com.senorpez.projectcars.staticdata.Car.EnginePosition.MID;
import static com.senorpez.projectcars.staticdata.Car.ShiftPattern.H;
import static com.senorpez.projectcars.staticdata.Car.ShiftPattern.SEQUENTIAL;
import static com.senorpez.projectcars.staticdata.Car.Shifter.PADDLES;
import static com.senorpez.projectcars.staticdata.Car.Shifter.SHIFTER;
import static com.senorpez.projectcars.staticdata.DocumentationCommon.commonLinks;
import static com.senorpez.projectcars.staticdata.SupportedMediaTypes.FALLBACK;
import static com.senorpez.projectcars.staticdata.SupportedMediaTypes.PROJECT_CARS;
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
public class EventControllerTest {
    private MockMvc mockMvc;
    private static final MediaType INVALID_MEDIA_TYPE = new MediaType("application", "invalid", UTF_8);
    private static final ClassLoader CLASS_LOADER = EventControllerTest.class.getClassLoader();
    private static InputStream EVENT_SCHEMA;
    private static InputStream EVENT_COLLECTION_SCHEMA;
    private static InputStream CAR_SCHEMA;
    private static InputStream CAR_COLLECTION_SCHEMA;
    private static InputStream CAR_CLASS_SCHEMA;
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

    private static final Car SECOND_CAR = new CarBuilder()
            .setId(844159614)
            .setManufacturer("SMS")
            .setModel("125cc Shifter Kart")
            .setCarClass(new CarClassBuilder()
                    .setId(2)
                    .setName("Kart1")
                    .build())
            .setYear(2011)
            .setCountry("England")
            .setDrivetrain(RWD)
            .setEnginePosition(MID)
            .setEngineType("Single Cylinder")
            .setTopSpeed(150)
            .setHorsepower(30)
            .setAcceleration(3.00f)
            .setBraking(1.85f)
            .setWeight(160)
            .setTorque(17)
            .setWeightBalance(59)
            .setWheelbase(1.03f)
            .setShiftPattern(H)
            .setShifter(SHIFTER)
            .setGears(6)
            .setDlc(null)
            .setLiveries(new HashSet<>(Arrays.asList(
                    new LiveryBuilder().setId(51).setName("Mudino #4").build(),
                    new LiveryBuilder().setId(52).setName("Luquitas #11").build())))
            .build();

    private static final Event FIRST_EVENT = new EventBuilder()
            .setId(103)
            .setName("RUF CTR3-GT Velocity")
            .setTier(null)
            .setVerified(false)
            .setCars(new HashSet<>(Collections.singletonList(FIRST_CAR)))
            .build();

    private static final Event SECOND_EVENT = new EventBuilder()
            .setId(1)
            .setName("Kart One UK Nationals")
            .setTier(1)
            .setVerified(true)
            .setCars(new HashSet<>(Collections.singletonList(SECOND_CAR)))
            .build();

    @InjectMocks
    EventController eventController;

    @Mock
    private APIService apiService;

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Before
    public void setUp() throws Exception {
        EVENT_SCHEMA = CLASS_LOADER.getResourceAsStream("event.schema.json");
        EVENT_COLLECTION_SCHEMA = CLASS_LOADER.getResourceAsStream("events.schema.json");
        CAR_SCHEMA = CLASS_LOADER.getResourceAsStream("car.schema.json");
        CAR_COLLECTION_SCHEMA = CLASS_LOADER.getResourceAsStream("cars.schema.json");
        CAR_CLASS_SCHEMA = CLASS_LOADER.getResourceAsStream("class.schema.json");
        LIVERY_SCHEMA = CLASS_LOADER.getResourceAsStream("livery.schema.json");
        LIVERY_COLLECTION_SCHEMA = CLASS_LOADER.getResourceAsStream("liveries.schema.json");
        ERROR_SCHEMA = CLASS_LOADER.getResourceAsStream("error.schema.json");
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new EventController(apiService, Arrays.asList(FIRST_EVENT, SECOND_EVENT)))
                .setMessageConverters(HALMessageConverter.getConverter(Collections.singletonList(ALL)))
                .setControllerAdvice(new APIExceptionHandler())
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Test
    public void getAllEvents_ValidAcceptHeader() throws Exception {
        when(apiService.findAll(any())).thenReturn(Arrays.asList(FIRST_EVENT, SECOND_EVENT));

        mockMvc.perform(get("/events").accept(PROJECT_CARS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(PROJECT_CARS))
                .andExpect(content().string(matchesJsonSchema(EVENT_COLLECTION_SCHEMA)))
                .andExpect(jsonPath("$._embedded.pcars:event", hasItem(
                        allOf(
                                hasEntry("id", (Object) FIRST_EVENT.getId()),
                                hasEntry("name", (Object) FIRST_EVENT.getName()),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasEntry("href", String.format("http://localhost:8080/events/%d", FIRST_EVENT.getId()))))))))
                .andExpect(jsonPath("$._embedded.pcars:event", hasItem(
                        allOf(
                                hasEntry("id", (Object) SECOND_EVENT.getId()),
                                hasEntry("name", (Object) SECOND_EVENT.getName()),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasEntry("href", String.format("http://localhost:8080/events/%d", SECOND_EVENT.getId()))))))))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost:8080/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", "http://localhost:8080/events")))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost:8080/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))
                .andDo(document("events",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Accept")
                                        .description("Accept header")
                                        .attributes(key("acceptvalue").value(SupportedMediaTypes.PROJECT_CARS_VALUE))),
                        responseFields(
                                fieldWithPath("_embedded.pcars:event").description("Event resources"),
                                fieldWithPath("_embedded.pcars:event[].id").description("ID number"),
                                fieldWithPath("_embedded.pcars:event[].name").description("Name"),
                                subsectionWithPath("_links").ignored(),
                                subsectionWithPath("_embedded.pcars:event[]._links").ignored()),
                        commonLinks));

        verify(apiService, times(1)).findAll(any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getAllEvents_FallbackAcceptHeader() throws Exception {
        when(apiService.findAll(any())).thenReturn(Arrays.asList(FIRST_EVENT, SECOND_EVENT));

        mockMvc.perform(get("/events").accept(FALLBACK))
                .andExpect(status().isOk())
                .andExpect(content().contentType(FALLBACK))
                .andExpect(content().string(matchesJsonSchema(EVENT_COLLECTION_SCHEMA)))
                .andExpect(jsonPath("$._embedded.pcars:event", hasItem(
                        allOf(
                                hasEntry("id", (Object) FIRST_EVENT.getId()),
                                hasEntry("name", (Object) FIRST_EVENT.getName()),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasEntry("href", String.format("http://localhost:8080/events/%d", FIRST_EVENT.getId()))))))))
                .andExpect(jsonPath("$._embedded.pcars:event", hasItem(
                        allOf(
                                hasEntry("id", (Object) SECOND_EVENT.getId()),
                                hasEntry("name", (Object) SECOND_EVENT.getName()),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasEntry("href", String.format("http://localhost:8080/events/%d", SECOND_EVENT.getId()))))))))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost:8080/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", "http://localhost:8080/events")))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost:8080/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))));

        verify(apiService, times(1)).findAll(any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getAllEvents_InvalidAcceptHeader() throws Exception {
        when(apiService.findAll(any())).thenReturn(Arrays.asList(FIRST_EVENT, SECOND_EVENT));

        mockMvc.perform(get("/events").accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_ACCEPTABLE.value())))
                .andExpect(jsonPath("$.message", is(NOT_ACCEPTABLE.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Accept header must be \"vnd.senorpez.pcars.v1+json\" for Project CARS " +
                        "or \"vnd.senorpez.pcars2.v0+json\" for Project CARS 2")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getAllEvents_InvalidMethod() throws Exception {
        when(apiService.findAll(any())).thenReturn(Arrays.asList(FIRST_EVENT, SECOND_EVENT));

        mockMvc.perform(put("/events").accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEvent_ValidId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT);

        mockMvc.perform(get(String.format("/events/%d", FIRST_EVENT.getId())).accept(PROJECT_CARS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(PROJECT_CARS))
                .andExpect(content().string(matchesJsonSchema(EVENT_SCHEMA)))
                .andExpect(jsonPath("$.id", is(FIRST_EVENT.getId())))
                .andExpect(jsonPath("$.name", is(FIRST_EVENT.getName())))
                .andExpect(jsonPath("$.tier", is(FIRST_EVENT.getTier())))
                .andExpect(jsonPath("$.verified", is(FIRST_EVENT.getVerified())))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost:8080/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", String.format("http://localhost:8080/events/%d", FIRST_EVENT.getId()))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost:8080/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))
                .andExpect(jsonPath("$._links.pcars:events", hasEntry("href", "http://localhost:8080/events")))
                .andExpect(jsonPath("$._links.pcars:cars", hasEntry("href", String.format("http://localhost:8080/events/%d/cars", FIRST_EVENT.getId()))))
                .andExpect(jsonPath("$._links.pcars:rounds", hasEntry("href", String.format("http://localhost:8080/events/%d/rounds", FIRST_EVENT.getId()))))
                .andDo(document("event",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Accept")
                                        .description("Accept header")
                                        .attributes(key("acceptvalue").value(SupportedMediaTypes.PROJECT_CARS_VALUE))),
                        responseFields(
                                fieldWithPath("id").description("ID number"),
                                fieldWithPath("name").description("Name"),
                                fieldWithPath("tier").description("Career Mode Tier"),
                                fieldWithPath("verified").ignored(),
                                subsectionWithPath("_links").ignored()),
                        commonLinks.and(
                                linkWithRel("pcars:events").description("List of event resources."),
                                linkWithRel("pcars:cars").description("List of car resources eligible for event"),
                                linkWithRel("pcars:rounds").description("List of round resources for event"))));

        verify(apiService, times(1)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEvent_ValidId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT);

        mockMvc.perform(get(String.format("/events/%d", FIRST_EVENT.getId())).accept(FALLBACK))
                .andExpect(status().isOk())
                .andExpect(content().contentType(FALLBACK))
                .andExpect(content().string(matchesJsonSchema(EVENT_SCHEMA)))
                .andExpect(jsonPath("$.id", is(FIRST_EVENT.getId())))
                .andExpect(jsonPath("$.name", is(FIRST_EVENT.getName())))
                .andExpect(jsonPath("$.tier", is(FIRST_EVENT.getTier())))
                .andExpect(jsonPath("$.verified", is(FIRST_EVENT.getVerified())))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost:8080/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", String.format("http://localhost:8080/events/%d", FIRST_EVENT.getId()))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost:8080/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))
                .andExpect(jsonPath("$._links.pcars:events", hasEntry("href", "http://localhost:8080/events")))
                .andExpect(jsonPath("$._links.pcars:cars", hasEntry("href", String.format("http://localhost:8080/events/%d/cars", FIRST_EVENT.getId()))))
                .andExpect(jsonPath("$._links.pcars:rounds", hasEntry("href", String.format("http://localhost:8080/events/%d/rounds", FIRST_EVENT.getId()))));

        verify(apiService, times(1)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEvent_ValidId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT);

        mockMvc.perform(get(String.format("/events/%d", FIRST_EVENT.getId())).accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_ACCEPTABLE.value())))
                .andExpect(jsonPath("$.message", is(NOT_ACCEPTABLE.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Accept header must be \"vnd.senorpez.pcars.v1+json\" for Project CARS " +
                        "or \"vnd.senorpez.pcars2.v0+json\" for Project CARS 2")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEvent_ValidId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT);

        mockMvc.perform(put(String.format("/events/%d", FIRST_EVENT.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEvent_InvalidId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(get("/events/8675309").accept(PROJECT_CARS))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Event with ID of %d not found", 8675309))));

        verify(apiService, times(1)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEvent_InvalidId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(get("/events/8675309").accept(FALLBACK))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Event with ID of %d not found", 8675309))));

        verify(apiService, times(1)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEvent_InvalidId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(get("/events/8675309").accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_ACCEPTABLE.value())))
                .andExpect(jsonPath("$.message", is(NOT_ACCEPTABLE.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Accept header must be \"vnd.senorpez.pcars.v1+json\" for Project CARS " +
                        "or \"vnd.senorpez.pcars2.v0+json\" for Project CARS 2")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEvent_InvalidId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(put("/events/8675309").accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCars_ValidId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT);

        mockMvc.perform(get(String.format("/events/%d/cars", FIRST_EVENT.getId())).accept(PROJECT_CARS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(PROJECT_CARS))
                .andExpect(content().string(matchesJsonSchema(CAR_COLLECTION_SCHEMA)))
                .andExpect(jsonPath("$._embedded.pcars:car", hasItem(
                        allOf(
                                hasEntry("id", (Object) FIRST_CAR.getId()),
                                hasEntry("name", (Object) String.join(" ", FIRST_CAR.getManufacturer(), FIRST_CAR.getModel())),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasItems(
                                                        hasEntry("href", String.format("http://localhost:8080/cars/%d", FIRST_CAR.getId())),
                                                        hasEntry("href", String.format("http://localhost:8080/events/%d/cars/%d", FIRST_EVENT.getId(), FIRST_CAR.getId())))))))))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost:8080/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", String.format("http://localhost:8080/events/%d/cars", FIRST_EVENT.getId()))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost:8080/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))));

        verify(apiService, times(1)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCars_ValidId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT);

        mockMvc.perform(get(String.format("/events/%d/cars", FIRST_EVENT.getId())).accept(FALLBACK))
                .andExpect(status().isOk())
                .andExpect(content().contentType(FALLBACK))
                .andExpect(content().string(matchesJsonSchema(CAR_COLLECTION_SCHEMA)))
                .andExpect(jsonPath("$._embedded.pcars:car", hasItem(
                        allOf(
                                hasEntry("id", (Object) FIRST_CAR.getId()),
                                hasEntry("name", (Object) String.join(" ", FIRST_CAR.getManufacturer(), FIRST_CAR.getModel())),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasItems(
                                                        hasEntry("href", String.format("http://localhost:8080/cars/%d", FIRST_CAR.getId())),
                                                        hasEntry("href", String.format("http://localhost:8080/events/%d/cars/%d", FIRST_EVENT.getId(), FIRST_CAR.getId())))))))))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost:8080/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", String.format("http://localhost:8080/events/%d/cars", FIRST_EVENT.getId()))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost:8080/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))));

        verify(apiService, times(1)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCars_ValidId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT);

        mockMvc.perform(get(String.format("/events/%d/cars", FIRST_EVENT.getId())).accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_ACCEPTABLE.value())))
                .andExpect(jsonPath("$.message", is(NOT_ACCEPTABLE.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Accept header must be \"vnd.senorpez.pcars.v1+json\" for Project CARS " +
                        "or \"vnd.senorpez.pcars2.v0+json\" for Project CARS 2")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCars_ValidId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT);

        mockMvc.perform(put(String.format("/events/%d/cars", FIRST_EVENT.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCars_InvalidId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/%d/cars", FIRST_EVENT.getId())).accept(PROJECT_CARS))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Event with ID of %d not found", 8675309))));

        verify(apiService, times(1)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCars_InvalidId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/%d/cars", FIRST_EVENT.getId())).accept(FALLBACK))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Event with ID of %d not found", 8675309))));

        verify(apiService, times(1)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCars_InvalidId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/%d/cars", FIRST_EVENT.getId())).accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_ACCEPTABLE.value())))
                .andExpect(jsonPath("$.message", is(NOT_ACCEPTABLE.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Accept header must be \"vnd.senorpez.pcars.v1+json\" for Project CARS " +
                        "or \"vnd.senorpez.pcars2.v0+json\" for Project CARS 2")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCars_InvalidId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(put(String.format("/events/%d/cars", FIRST_EVENT.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));


        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCar_ValidEventId_ValidCarId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT)
                .thenReturn(FIRST_CAR);
        mockMvc.perform(get(String.format("/events/%d/cars/%d", FIRST_EVENT.getId(), FIRST_CAR.getId())).accept(PROJECT_CARS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(PROJECT_CARS))
                .andExpect(content().string(matchesJsonSchema(CAR_SCHEMA)))
                .andExpect(jsonPath("$.id", is(FIRST_CAR.getId())))
                .andExpect(jsonPath("$.manufacturer", is(FIRST_CAR.getManufacturer())))
                .andExpect(jsonPath("$.model", is(FIRST_CAR.getModel())))
                .andExpect(jsonPath("$.country", is(FIRST_CAR.getCountry())))
                .andExpect(jsonPath("$.class", is(FIRST_CAR.getCarClass().getName())))
                .andExpect(jsonPath("$.year", is(FIRST_CAR.getYear())))
                .andExpect(jsonPath("$.drivetrain", is(FIRST_CAR.getDrivetrain().toString())))
                .andExpect(jsonPath("$.enginePosition", is(FIRST_CAR.getEnginePosition().getDisplayString())))
                .andExpect(jsonPath("$.engineType", is(FIRST_CAR.getEngineType())))
                .andExpect(jsonPath("$.topSpeed", is(FIRST_CAR.getTopSpeed())))
                .andExpect(jsonPath("$.horsepower", is(FIRST_CAR.getHorsepower())))
                .andExpect(jsonPath("$.acceleration", closeTo((double) FIRST_CAR.getAcceleration(), 0.001)))
                .andExpect(jsonPath("$.braking", closeTo((double) FIRST_CAR.getBraking(), 0.001)))
                .andExpect(jsonPath("$.weight", is(FIRST_CAR.getWeight())))
                .andExpect(jsonPath("$.torque", is(FIRST_CAR.getTorque())))
                .andExpect(jsonPath("$.weightBalance", is(FIRST_CAR.getWeightBalance())))
                .andExpect(jsonPath("$.wheelbase", closeTo((double) FIRST_CAR.getWheelbase(), 0.001)))
                .andExpect(jsonPath("$.shiftPattern", is(FIRST_CAR.getShiftPattern().getDisplayString())))
                .andExpect(jsonPath("$.shifter", is(FIRST_CAR.getShifter().getDisplayString())))
                .andExpect(jsonPath("$.gears", is(FIRST_CAR.getGears())))
                .andExpect(jsonPath("$.dlc", is(FIRST_CAR.getDlc())))

                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost:8080/")))
                .andExpect(jsonPath("$._links.self",
                        hasItems(
                                hasEntry("href", String.format("http://localhost:8080/events/%d/cars/%d", FIRST_EVENT.getId(), FIRST_CAR.getId())),
                                hasEntry("href", String.format("http://localhost:8080/cars/%d", FIRST_CAR.getId())))))

                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost:8080/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))

                .andExpect(jsonPath("$._links.pcars:cars", hasEntry("href", String.format("http://localhost:8080/events/%d/cars", FIRST_EVENT.getId()))))
                .andExpect(jsonPath("$._links.pcars:class",
                        hasItems(
                                hasEntry("href", String.format("http://localhost:8080/classes/%d", FIRST_CAR.getCarClass().getId())),
                                hasEntry("href", String.format("http://localhost:8080/cars/%d/class", FIRST_CAR.getId())),
                                hasEntry("href", String.format("http://localhost:8080/events/%d/cars/%d/class", FIRST_EVENT.getId(), FIRST_CAR.getId()))
                        )))
                .andExpect(jsonPath("$._links.pcars:liveries",
                        hasItems(
                                hasEntry("href", String.format("http://localhost:8080/cars/%d/liveries", FIRST_CAR.getId())),
                                hasEntry("href", String.format("http://localhost:8080/events/%d/cars/%d/liveries", FIRST_EVENT.getId(), FIRST_CAR.getId())))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCar_ValidEventId_ValidCarId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT)
                .thenReturn(FIRST_CAR);
        mockMvc.perform(get(String.format("/events/%d/cars/%d", FIRST_EVENT.getId(), FIRST_CAR.getId())).accept(FALLBACK))
                .andExpect(status().isOk())
                .andExpect(content().contentType(FALLBACK))
                .andExpect(content().string(matchesJsonSchema(CAR_SCHEMA)))
                .andExpect(jsonPath("$.id", is(FIRST_CAR.getId())))
                .andExpect(jsonPath("$.manufacturer", is(FIRST_CAR.getManufacturer())))
                .andExpect(jsonPath("$.model", is(FIRST_CAR.getModel())))
                .andExpect(jsonPath("$.country", is(FIRST_CAR.getCountry())))
                .andExpect(jsonPath("$.class", is(FIRST_CAR.getCarClass().getName())))
                .andExpect(jsonPath("$.year", is(FIRST_CAR.getYear())))
                .andExpect(jsonPath("$.drivetrain", is(FIRST_CAR.getDrivetrain().toString())))
                .andExpect(jsonPath("$.enginePosition", is(FIRST_CAR.getEnginePosition().getDisplayString())))
                .andExpect(jsonPath("$.engineType", is(FIRST_CAR.getEngineType())))
                .andExpect(jsonPath("$.topSpeed", is(FIRST_CAR.getTopSpeed())))
                .andExpect(jsonPath("$.horsepower", is(FIRST_CAR.getHorsepower())))
                .andExpect(jsonPath("$.acceleration", closeTo((double) FIRST_CAR.getAcceleration(), 0.001)))
                .andExpect(jsonPath("$.braking", closeTo((double) FIRST_CAR.getBraking(), 0.001)))
                .andExpect(jsonPath("$.weight", is(FIRST_CAR.getWeight())))
                .andExpect(jsonPath("$.torque", is(FIRST_CAR.getTorque())))
                .andExpect(jsonPath("$.weightBalance", is(FIRST_CAR.getWeightBalance())))
                .andExpect(jsonPath("$.wheelbase", closeTo((double) FIRST_CAR.getWheelbase(), 0.001)))
                .andExpect(jsonPath("$.shiftPattern", is(FIRST_CAR.getShiftPattern().getDisplayString())))
                .andExpect(jsonPath("$.shifter", is(FIRST_CAR.getShifter().getDisplayString())))
                .andExpect(jsonPath("$.gears", is(FIRST_CAR.getGears())))
                .andExpect(jsonPath("$.dlc", is(FIRST_CAR.getDlc())))

                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost:8080/")))
                .andExpect(jsonPath("$._links.self",
                        hasItems(
                                hasEntry("href", String.format("http://localhost:8080/events/%d/cars/%d", FIRST_EVENT.getId(), FIRST_CAR.getId())),
                                hasEntry("href", String.format("http://localhost:8080/cars/%d", FIRST_CAR.getId())))))

                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost:8080/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))

                .andExpect(jsonPath("$._links.pcars:cars", hasEntry("href", String.format("http://localhost:8080/events/%d/cars", FIRST_EVENT.getId()))))
                .andExpect(jsonPath("$._links.pcars:class",
                        hasItems(
                                hasEntry("href", String.format("http://localhost:8080/classes/%d", FIRST_CAR.getCarClass().getId())),
                                hasEntry("href", String.format("http://localhost:8080/cars/%d/class", FIRST_CAR.getId())),
                                hasEntry("href", String.format("http://localhost:8080/events/%d/cars/%d/class", FIRST_EVENT.getId(), FIRST_CAR.getId()))
                        )))
                .andExpect(jsonPath("$._links.pcars:liveries",
                        hasItems(
                                hasEntry("href", String.format("http://localhost:8080/cars/%d/liveries", FIRST_CAR.getId())),
                                hasEntry("href", String.format("http://localhost:8080/events/%d/cars/%d/liveries", FIRST_EVENT.getId(), FIRST_CAR.getId())))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCar_ValidEventId_ValidCarId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT)
                .thenReturn(FIRST_CAR);
        mockMvc.perform(get(String.format("/events/%d/cars/%d", FIRST_EVENT.getId(), FIRST_CAR.getId())).accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_ACCEPTABLE.value())))
                .andExpect(jsonPath("$.message", is(NOT_ACCEPTABLE.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Accept header must be \"vnd.senorpez.pcars.v1+json\" for Project CARS " +
                        "or \"vnd.senorpez.pcars2.v0+json\" for Project CARS 2")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCar_ValidEventId_ValidCarId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT)
                .thenReturn(FIRST_CAR);
        mockMvc.perform(put(String.format("/events/%d/cars/%d", FIRST_EVENT.getId(), FIRST_CAR.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCar_ValidEventId_InvalidCarId_ValidAcceptHeader() throws Exception {
        when((Event) apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/%d/cars/8675309", FIRST_EVENT.getId())).accept(PROJECT_CARS))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Car with ID of %d not found", 8675309))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCar_ValidEventId_InvalidCarId_FallbackAcceptHeader() throws Exception {
        when((Event) apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/%d/cars/8675309", FIRST_EVENT.getId())).accept(FALLBACK))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Car with ID of %d not found", 8675309))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCar_ValidEventId_InvalidCarId_InvalidAcceptHeader() throws Exception {
        when((Event) apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/%d/cars/8675309", FIRST_EVENT.getId())).accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_ACCEPTABLE.value())))
                .andExpect(jsonPath("$.message", is(NOT_ACCEPTABLE.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Accept header must be \"vnd.senorpez.pcars.v1+json\" for Project CARS " +
                        "or \"vnd.senorpez.pcars2.v0+json\" for Project CARS 2")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCar_ValidEventId_InvalidCarId_InvalidMethod() throws Exception {
        when((Event) apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(put(String.format("/events/%d/cars/8675309", FIRST_EVENT.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCar_InvalidEventId_XXXCarId_ValidAcceptHeader() throws Exception {
        when((Event) apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/8675309/cars/%d", FIRST_CAR.getId())).accept(PROJECT_CARS))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Event with ID of %d not found", 8675309))));

        verify(apiService, times(1)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCar_InvalidEventId_XXXCarId_FallbackAcceptHeader() throws Exception {
        when((Event) apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/8675309/cars/%d", FIRST_CAR.getId())).accept(FALLBACK))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Event with ID of %d not found", 8675309))));

        verify(apiService, times(1)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCar_InvalidEventId_XXXCarId_InvalidAcceptHeader() throws Exception {
        when((Event) apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/8675309/cars/%d", FIRST_CAR.getId())).accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_ACCEPTABLE.value())))
                .andExpect(jsonPath("$.message", is(NOT_ACCEPTABLE.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Accept header must be \"vnd.senorpez.pcars.v1+json\" for Project CARS " +
                        "or \"vnd.senorpez.pcars2.v0+json\" for Project CARS 2")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCar_InvalidEventId_XXXCarId_InvalidMethod() throws Exception {
        when((Event) apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(put(String.format("/events/8675309/cars/%d", FIRST_CAR.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCar_ValidEventId_ForeignCarId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT)
                .thenThrow(new CarNotFoundException(SECOND_CAR.getId()));

        mockMvc.perform(get(String.format("/events/%d/cars/%d", FIRST_EVENT.getId(), SECOND_CAR.getId())).accept(PROJECT_CARS))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Car with ID of %d not found", SECOND_CAR.getId()))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCar_ValidEventId_ForeignCarId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT)
                .thenThrow(new CarNotFoundException(SECOND_CAR.getId()));

        mockMvc.perform(get(String.format("/events/%d/cars/%d", FIRST_EVENT.getId(), SECOND_CAR.getId())).accept(FALLBACK))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Car with ID of %d not found", SECOND_CAR.getId()))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCar_ValidEventId_ForeignCarId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT)
                .thenThrow(new CarNotFoundException(SECOND_CAR.getId()));

        mockMvc.perform(get(String.format("/events/%d/cars/%d", FIRST_EVENT.getId(), SECOND_CAR.getId())).accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_ACCEPTABLE.value())))
                .andExpect(jsonPath("$.message", is(NOT_ACCEPTABLE.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Accept header must be \"vnd.senorpez.pcars.v1+json\" for Project CARS " +
                        "or \"vnd.senorpez.pcars2.v0+json\" for Project CARS 2")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCar_ValidEventId_ForeignCarId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT)
                .thenThrow(new CarNotFoundException(SECOND_CAR.getId()));

        mockMvc.perform(put(String.format("/events/%d/cars/%d", FIRST_EVENT.getId(), SECOND_CAR.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCarClass_ValidEventId_ValidCarId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenReturn(FIRST_CAR);
        mockMvc.perform(get(String.format("/events/%d/cars/%d/class", FIRST_EVENT.getId(), FIRST_CAR.getId())).accept(PROJECT_CARS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(PROJECT_CARS))
                .andExpect(content().string(matchesJsonSchema(CAR_CLASS_SCHEMA)))
                .andExpect(jsonPath("$.id", is(FIRST_CLASS.getId())))
                .andExpect(jsonPath("$.name", is(FIRST_CLASS.getName())))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost:8080/")))
                .andExpect(jsonPath("$._links.self",
                        hasItems(
                                hasEntry("href", String.format("http://localhost:8080/classes/%d", FIRST_CLASS.getId())),
                                hasEntry("href", String.format("http://localhost:8080/cars/%d/class", FIRST_CAR.getId())),
                                hasEntry("href", String.format("http://localhost:8080/events/%d/cars/%d/class", FIRST_EVENT.getId(), FIRST_CAR.getId())))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost:8080/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))
                .andExpect(jsonPath("$._links.pcars:classes", hasEntry("href", "http://localhost:8080/classes")))
                .andExpect(jsonPath("$._links.pcars:car",
                        hasItems(
                                hasEntry("href", String.format("http://localhost:8080/cars/%d", FIRST_CAR.getId())),
                                hasEntry("href", String.format("http://localhost:8080/events/%d/cars/%d", FIRST_EVENT.getId(), FIRST_CAR.getId())))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCarClass_ValidEventId_ValidCarId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenReturn(FIRST_CAR);
        mockMvc.perform(get(String.format("/events/%d/cars/%d/class", FIRST_EVENT.getId(), FIRST_CAR.getId())).accept(FALLBACK))
                .andExpect(status().isOk())
                .andExpect(content().contentType(FALLBACK))
                .andExpect(content().string(matchesJsonSchema(CAR_CLASS_SCHEMA)))
                .andExpect(jsonPath("$.id", is(FIRST_CLASS.getId())))
                .andExpect(jsonPath("$.name", is(FIRST_CLASS.getName())))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost:8080/")))
                .andExpect(jsonPath("$._links.self",
                        hasItems(
                                hasEntry("href", String.format("http://localhost:8080/classes/%d", FIRST_CLASS.getId())),
                                hasEntry("href", String.format("http://localhost:8080/cars/%d/class", FIRST_CAR.getId())),
                                hasEntry("href", String.format("http://localhost:8080/events/%d/cars/%d/class", FIRST_EVENT.getId(), FIRST_CAR.getId())))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost:8080/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))
                .andExpect(jsonPath("$._links.pcars:classes", hasEntry("href", "http://localhost:8080/classes")))
                .andExpect(jsonPath("$._links.pcars:car",
                        hasItems(
                                hasEntry("href", String.format("http://localhost:8080/cars/%d", FIRST_CAR.getId())),
                                hasEntry("href", String.format("http://localhost:8080/events/%d/cars/%d", FIRST_EVENT.getId(), FIRST_CAR.getId())))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCarClass_ValidEventId_ValidCarId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenReturn(FIRST_CAR);
        mockMvc.perform(get(String.format("/events/%d/cars/%d/class", FIRST_EVENT.getId(), FIRST_CAR.getId())).accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_ACCEPTABLE.value())))
                .andExpect(jsonPath("$.message", is(NOT_ACCEPTABLE.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Accept header must be \"vnd.senorpez.pcars.v1+json\" for Project CARS " +
                        "or \"vnd.senorpez.pcars2.v0+json\" for Project CARS 2")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCarClass_ValidEventId_ValidCarId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenReturn(FIRST_CAR);
        mockMvc.perform(put(String.format("/events/%d/cars/%d/class", FIRST_EVENT.getId(), FIRST_CAR.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCarClass_ValidEventId_InvalidCarId_ValidAcceptHeader() throws Exception {
        when((Event) apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/%d/cars/8675309/class", FIRST_EVENT.getId())).accept(PROJECT_CARS))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Car with ID of %d not found", 8675309))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCarClass_ValidEventId_InvalidCarId_FallbackAcceptHeader() throws Exception {
        when((Event) apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/%d/cars/8675309/class", FIRST_EVENT.getId())).accept(FALLBACK))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Car with ID of %d not found", 8675309))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCarClass_ValidEventId_InvalidCarId_InvalidAcceptHeader() throws Exception {
        when((Event) apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/%d/cars/8675309/class", FIRST_EVENT.getId())).accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_ACCEPTABLE.value())))
                .andExpect(jsonPath("$.message", is(NOT_ACCEPTABLE.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Accept header must be \"vnd.senorpez.pcars.v1+json\" for Project CARS " +
                        "or \"vnd.senorpez.pcars2.v0+json\" for Project CARS 2")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCarClass_ValidEventId_InvalidCarId_InvalidMethod() throws Exception {
        when((Event) apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(put(String.format("/events/%d/cars/8675309/class", FIRST_EVENT.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCarClass_InvalidEventId_XXXCarId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/8675309/cars/%d/class", FIRST_CAR.getId())).accept(PROJECT_CARS))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Event with ID of %d not found", 8675309))));

        verify(apiService, times(1)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCarClass_InvalidEventId_XXXCarId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/8675309/cars/%d/class", FIRST_CAR.getId())).accept(FALLBACK))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Event with ID of %d not found", 8675309))));

        verify(apiService, times(1)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCarClass_InvalidEventId_XXXCarId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/8675309/cars/%d/class", FIRST_CAR.getId())).accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_ACCEPTABLE.value())))
                .andExpect(jsonPath("$.message", is(NOT_ACCEPTABLE.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Accept header must be \"vnd.senorpez.pcars.v1+json\" for Project CARS " +
                        "or \"vnd.senorpez.pcars2.v0+json\" for Project CARS 2")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCarClass_InvalidEventId_XXXCarId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(put(String.format("/events/8675309/cars/%d/class", FIRST_CAR.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCarClass_ValidEventId_ForeignCarId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new CarNotFoundException(SECOND_CAR.getId()));

        mockMvc.perform(get(String.format("/events/%d/cars/%d/class", FIRST_EVENT.getId(), SECOND_CAR.getId())).accept(PROJECT_CARS))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Car with ID of %d not found", SECOND_CAR.getId()))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCarClass_ValidEventId_ForeignCarId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new CarNotFoundException(SECOND_CAR.getId()));

        mockMvc.perform(get(String.format("/events/%d/cars/%d/class", FIRST_EVENT.getId(), SECOND_CAR.getId())).accept(FALLBACK))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Car with ID of %d not found", SECOND_CAR.getId()))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCarClass_ValidEventId_ForeignCarId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenReturn(SECOND_CAR);

        mockMvc.perform(get(String.format("/events/%d/cars/%d/class", FIRST_EVENT.getId(), SECOND_CAR.getId())).accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_ACCEPTABLE.value())))
                .andExpect(jsonPath("$.message", is(NOT_ACCEPTABLE.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Accept header must be \"vnd.senorpez.pcars.v1+json\" for Project CARS " +
                        "or \"vnd.senorpez.pcars2.v0+json\" for Project CARS 2")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCarClass_ValidEventId_ForeignCarId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenReturn(SECOND_CAR);

        mockMvc.perform(put(String.format("/events/%d/cars/%d/class", FIRST_EVENT.getId(), SECOND_CAR.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLiveries_ValidEventId_ValidCarId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenReturn(FIRST_CAR);
        mockMvc.perform(get(String.format("/events/%d/cars/%d/liveries", FIRST_EVENT.getId(), FIRST_CAR.getId())).accept(PROJECT_CARS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(PROJECT_CARS))
                .andExpect(content().string(matchesJsonSchema(LIVERY_COLLECTION_SCHEMA)))
                .andExpect(jsonPath("$._embedded.pcars:livery", hasItem(
                        allOf(
                                hasEntry("id", (Object) FIRST_LIVERY.getId()),
                                hasEntry("name", (Object) FIRST_LIVERY.getName()),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasItems(
                                                        hasEntry("href", String.format("http://localhost:8080/events/%d/cars/%d/liveries/%d", FIRST_EVENT.getId(), FIRST_CAR.getId(), FIRST_LIVERY.getId())),
                                                        hasEntry("href", String.format("http://localhost:8080/cars/%d/liveries/%d", FIRST_CAR.getId(), FIRST_LIVERY.getId())))))))))
                .andExpect(jsonPath("$._embedded.pcars:livery", hasItem(
                        allOf(
                                hasEntry("id", (Object) SECOND_LIVERY.getId()),
                                hasEntry("name", (Object) SECOND_LIVERY.getName()),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasItems(
                                                        hasEntry("href", String.format("http://localhost:8080/events/%d/cars/%d/liveries/%d", FIRST_EVENT.getId(), FIRST_CAR.getId(), SECOND_LIVERY.getId())),
                                                        hasEntry("href", String.format("http://localhost:8080/cars/%d/liveries/%d", FIRST_CAR.getId(), SECOND_LIVERY.getId())))))))))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost:8080/")))
                .andExpect(jsonPath("$._links.self",
                        hasItems(
                                hasEntry("href", String.format("http://localhost:8080/cars/%d/liveries", FIRST_CAR.getId())),
                                hasEntry("href", String.format("http://localhost:8080/events/%d/cars/%d/liveries", FIRST_EVENT.getId(), FIRST_CAR.getId())))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost:8080/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))
                .andExpect(jsonPath("$._links.pcars:car",
                        hasItems(
                                hasEntry("href", String.format("http://localhost:8080/cars/%d", FIRST_CAR.getId())),
                                hasEntry("href", String.format("http://localhost:8080/events/%d/cars/%d", FIRST_EVENT.getId(), FIRST_CAR.getId())))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLiveries_ValidEventId_ValidCarId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenReturn(FIRST_CAR);
        mockMvc.perform(get(String.format("/events/%d/cars/%d/liveries", FIRST_EVENT.getId(), FIRST_CAR.getId())).accept(FALLBACK))
                .andExpect(status().isOk())
                .andExpect(content().contentType(FALLBACK))
                .andExpect(content().string(matchesJsonSchema(LIVERY_COLLECTION_SCHEMA)))
                .andExpect(jsonPath("$._embedded.pcars:livery", hasItem(
                        allOf(
                                hasEntry("id", (Object) FIRST_LIVERY.getId()),
                                hasEntry("name", (Object) FIRST_LIVERY.getName()),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasItems(
                                                        hasEntry("href", String.format("http://localhost:8080/events/%d/cars/%d/liveries/%d", FIRST_EVENT.getId(), FIRST_CAR.getId(), FIRST_LIVERY.getId())),
                                                        hasEntry("href", String.format("http://localhost:8080/cars/%d/liveries/%d", FIRST_CAR.getId(), FIRST_LIVERY.getId())))))))))
                .andExpect(jsonPath("$._embedded.pcars:livery", hasItem(
                        allOf(
                                hasEntry("id", (Object) SECOND_LIVERY.getId()),
                                hasEntry("name", (Object) SECOND_LIVERY.getName()),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasItems(
                                                        hasEntry("href", String.format("http://localhost:8080/events/%d/cars/%d/liveries/%d", FIRST_EVENT.getId(), FIRST_CAR.getId(), SECOND_LIVERY.getId())),
                                                        hasEntry("href", String.format("http://localhost:8080/cars/%d/liveries/%d", FIRST_CAR.getId(), SECOND_LIVERY.getId())))))))))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost:8080/")))
                .andExpect(jsonPath("$._links.self",
                        hasItems(
                                hasEntry("href", String.format("http://localhost:8080/cars/%d/liveries", FIRST_CAR.getId())),
                                hasEntry("href", String.format("http://localhost:8080/events/%d/cars/%d/liveries", FIRST_EVENT.getId(), FIRST_CAR.getId())))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost:8080/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))
                .andExpect(jsonPath("$._links.pcars:car",
                        hasItems(
                                hasEntry("href", String.format("http://localhost:8080/cars/%d", FIRST_CAR.getId())),
                                hasEntry("href", String.format("http://localhost:8080/events/%d/cars/%d", FIRST_EVENT.getId(), FIRST_CAR.getId())))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLiveries_ValidEventId_ValidCarId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenReturn(FIRST_CAR);
        mockMvc.perform(get(String.format("/events/%d/cars/%d/liveries", FIRST_EVENT.getId(), FIRST_CAR.getId())).accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_ACCEPTABLE.value())))
                .andExpect(jsonPath("$.message", is(NOT_ACCEPTABLE.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Accept header must be \"vnd.senorpez.pcars.v1+json\" for Project CARS " +
                        "or \"vnd.senorpez.pcars2.v0+json\" for Project CARS 2")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLiveries_ValidEventId_ValidCarId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenReturn(FIRST_CAR);
        mockMvc.perform(put(String.format("/events/%d/cars/%d/liveries", FIRST_EVENT.getId(), FIRST_CAR.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLiveries_ValidEventId_InvalidCarId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/%d/cars/8675309/liveries", FIRST_EVENT.getId())).accept(PROJECT_CARS))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Car with ID of %d not found", 8675309))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLiveries_ValidEventId_InvalidCarId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/%d/cars/8675309/liveries", FIRST_EVENT.getId())).accept(FALLBACK))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Car with ID of %d not found", 8675309))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLiveries_ValidEventId_InvalidCarId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/%d/cars/8675309/liveries", FIRST_EVENT.getId())).accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_ACCEPTABLE.value())))
                .andExpect(jsonPath("$.message", is(NOT_ACCEPTABLE.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Accept header must be \"vnd.senorpez.pcars.v1+json\" for Project CARS " +
                        "or \"vnd.senorpez.pcars2.v0+json\" for Project CARS 2")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLiveries_ValidEventId_InvalidCarId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(put(String.format("/events/%d/cars/8675309/liveries", FIRST_EVENT.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLiveries_InvalidEventId_XXXCarId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/8675309/cars/%d/liveries", FIRST_CAR.getId())).accept(PROJECT_CARS))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Event with ID of %d not found", 8675309))));

        verify(apiService, times(1)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLiveries_InvalidEventId_XXXCarId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/8675309/cars/%d/liveries", FIRST_CAR.getId())).accept(FALLBACK))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Event with ID of %d not found", 8675309))));

        verify(apiService, times(1)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLiveries_InvalidEventId_XXXCarId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/8675309/cars/%d/liveries", FIRST_CAR.getId())).accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_ACCEPTABLE.value())))
                .andExpect(jsonPath("$.message", is(NOT_ACCEPTABLE.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Accept header must be \"vnd.senorpez.pcars.v1+json\" for Project CARS " +
                        "or \"vnd.senorpez.pcars2.v0+json\" for Project CARS 2")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLiveries_InvalidEventId_XXXCarId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(put(String.format("/events/8675309/cars/%d/liveries", FIRST_CAR.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLiveries_ValidEventId_ForeignCarId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new CarNotFoundException(SECOND_CAR.getId()));

        mockMvc.perform(get(String.format("/events/%d/cars/%d/liveries", FIRST_EVENT.getId(), SECOND_CAR.getId())).accept(PROJECT_CARS))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Car with ID of %d not found", SECOND_CAR.getId()))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLiveries_ValidEventId_ForeignCarId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new CarNotFoundException(SECOND_CAR.getId()));

        mockMvc.perform(get(String.format("/events/%d/cars/%d/liveries", FIRST_EVENT.getId(), SECOND_CAR.getId())).accept(FALLBACK))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Car with ID of %d not found", SECOND_CAR.getId()))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLiveries_ValidEventId_ForeignCarId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenReturn(SECOND_CAR);

        mockMvc.perform(get(String.format("/events/%d/cars/%d/liveries", FIRST_EVENT.getId(), SECOND_CAR.getId())).accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_ACCEPTABLE.value())))
                .andExpect(jsonPath("$.message", is(NOT_ACCEPTABLE.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Accept header must be \"vnd.senorpez.pcars.v1+json\" for Project CARS " +
                        "or \"vnd.senorpez.pcars2.v0+json\" for Project CARS 2")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLiveries_ValidEventId_ForeignCarId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenReturn(SECOND_CAR);

        mockMvc.perform(put(String.format("/events/%d/cars/%d/liveries", FIRST_EVENT.getId(), SECOND_CAR.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLivery_ValidEventId_ValidCarId_ValidLiveryId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenReturn(FIRST_CAR).thenReturn(FIRST_LIVERY);

        mockMvc.perform(get(String.format("/events/%d/cars/%d/liveries/%d", FIRST_EVENT.getId(), FIRST_CAR.getId(), FIRST_LIVERY.getId())).accept(PROJECT_CARS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(PROJECT_CARS))
                .andExpect(content().string(matchesJsonSchema(LIVERY_SCHEMA)))
                .andExpect(jsonPath("$.id", is(FIRST_LIVERY.getId())))
                .andExpect(jsonPath("$.name", is(FIRST_LIVERY.getName())))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost:8080/")))
                .andExpect(jsonPath("$._links.self",
                        hasItems(
                                hasEntry("href", String.format("http://localhost:8080/cars/%d/liveries/%d", FIRST_CAR.getId(), FIRST_LIVERY.getId())),
                                hasEntry("href", String.format("http://localhost:8080/events/%d/cars/%d/liveries/%d", FIRST_EVENT.getId(), FIRST_CAR.getId(), FIRST_LIVERY.getId())))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost:8080/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))
                .andExpect(jsonPath("$._links.pcars:liveries",
                        hasItems(
                                hasEntry("href", String.format("http://localhost:8080/cars/%d/liveries", FIRST_CAR.getId())),
                                hasEntry("href", String.format("http://localhost:8080/events/%d/cars/%d/liveries", FIRST_EVENT.getId(), FIRST_CAR.getId())))));

        verify(apiService, times(3)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLivery_ValidEventId_ValidCarId_ValidLiveryId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenReturn(FIRST_CAR).thenReturn(FIRST_LIVERY);

        mockMvc.perform(get(String.format("/events/%d/cars/%d/liveries/%d", FIRST_EVENT.getId(), FIRST_CAR.getId(), FIRST_LIVERY.getId())).accept(FALLBACK))
                .andExpect(status().isOk())
                .andExpect(content().contentType(FALLBACK))
                .andExpect(content().string(matchesJsonSchema(LIVERY_SCHEMA)))
                .andExpect(jsonPath("$.id", is(FIRST_LIVERY.getId())))
                .andExpect(jsonPath("$.name", is(FIRST_LIVERY.getName())))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost:8080/")))
                .andExpect(jsonPath("$._links.self",
                        hasItems(
                                hasEntry("href", String.format("http://localhost:8080/cars/%d/liveries/%d", FIRST_CAR.getId(), FIRST_LIVERY.getId())),
                                hasEntry("href", String.format("http://localhost:8080/events/%d/cars/%d/liveries/%d", FIRST_EVENT.getId(), FIRST_CAR.getId(), FIRST_LIVERY.getId())))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost:8080/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))
                .andExpect(jsonPath("$._links.pcars:liveries",
                        hasItems(
                                hasEntry("href", String.format("http://localhost:8080/cars/%d/liveries", FIRST_CAR.getId())),
                                hasEntry("href", String.format("http://localhost:8080/events/%d/cars/%d/liveries", FIRST_EVENT.getId(), FIRST_CAR.getId())))));

        verify(apiService, times(3)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLivery_ValidEventId_ValidCarId_ValidLiveryId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenReturn(FIRST_CAR).thenReturn(FIRST_LIVERY);

        mockMvc.perform(get(String.format("/events/%d/cars/%d/liveries/%d", FIRST_EVENT.getId(), FIRST_CAR.getId(), FIRST_LIVERY.getId())).accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_ACCEPTABLE.value())))
                .andExpect(jsonPath("$.message", is(NOT_ACCEPTABLE.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Accept header must be \"vnd.senorpez.pcars.v1+json\" for Project CARS " +
                        "or \"vnd.senorpez.pcars2.v0+json\" for Project CARS 2")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLivery_ValidEventId_ValidCarId_ValidLiveryId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenReturn(FIRST_CAR).thenReturn(FIRST_LIVERY);

        mockMvc.perform(put(String.format("/events/%d/cars/%d/liveries/%d", FIRST_EVENT.getId(), FIRST_CAR.getId(), FIRST_LIVERY.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLivery_ValidEventId_ValidCarId_InvalidLiveryId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenReturn(FIRST_CAR).thenThrow(new LiveryNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/%d/cars/%d/liveries/8675309", FIRST_EVENT.getId(), FIRST_CAR.getId())).accept(PROJECT_CARS))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Livery with ID of %d not found", 8675309))));

        verify(apiService, times(3)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLivery_ValidEventId_ValidCarId_InvalidLiveryId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenReturn(FIRST_CAR).thenThrow(new LiveryNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/%d/cars/%d/liveries/8675309", FIRST_EVENT.getId(), FIRST_CAR.getId())).accept(FALLBACK))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Livery with ID of %d not found", 8675309))));

        verify(apiService, times(3)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLivery_ValidEventId_ValidCarId_InvalidLiveryId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenReturn(FIRST_CAR).thenThrow(new LiveryNotFoundException(8675309));
        mockMvc.perform(get(String.format("/events/%d/cars/%d/liveries/8675309", FIRST_EVENT.getId(), FIRST_CAR.getId())).accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_ACCEPTABLE.value())))
                .andExpect(jsonPath("$.message", is(NOT_ACCEPTABLE.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Accept header must be \"vnd.senorpez.pcars.v1+json\" for Project CARS " +
                        "or \"vnd.senorpez.pcars2.v0+json\" for Project CARS 2")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLivery_ValidEventId_ValidCarId_InvalidLiveryId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenReturn(FIRST_CAR).thenThrow(new LiveryNotFoundException(8675309));

        mockMvc.perform(put(String.format("/events/%d/cars/%d/liveries/8675309", FIRST_EVENT.getId(), FIRST_CAR.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLivery_ValidEventId_InvalidCarId_XXXLiveryId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/%d/cars/8675309/liveries/%d", FIRST_EVENT.getId(), FIRST_LIVERY.getId())).accept(PROJECT_CARS))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Car with ID of %d not found", 8675309))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLivery_ValidEventId_InvalidCarId_XXXLiveryId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/%d/cars/8675309/liveries/%d", FIRST_EVENT.getId(), FIRST_LIVERY.getId())).accept(FALLBACK))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Car with ID of %d not found", 8675309))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLivery_ValidEventId_InvalidCarId_XXXLiveryId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/%d/cars/8675309/liveries/%d", FIRST_EVENT.getId(), FIRST_LIVERY.getId())).accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_ACCEPTABLE.value())))
                .andExpect(jsonPath("$.message", is(NOT_ACCEPTABLE.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Accept header must be \"vnd.senorpez.pcars.v1+json\" for Project CARS " +
                        "or \"vnd.senorpez.pcars2.v0+json\" for Project CARS 2")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLivery_ValidEventId_InvalidCarId_XXXLiveryId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(put(String.format("/events/%d/cars/8675309/liveries/%d", FIRST_EVENT.getId(), FIRST_LIVERY.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLivery_InvalidEventId_XXXCarId_XXXLiveryId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/8675309/cars/%d/liveries/%d", FIRST_CAR.getId(), FIRST_LIVERY.getId())).accept(PROJECT_CARS))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Event with ID of %d not found", 8675309))));

        verify(apiService, times(1)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLivery_InvalidEventId_XXXCarId_XXXLiveryId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/8675309/cars/%d/liveries/%d", FIRST_CAR.getId(), FIRST_LIVERY.getId())).accept(FALLBACK))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Event with ID of %d not found", 8675309))));

        verify(apiService, times(1)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLivery_InvalidEventId_XXXCarId_XXXLiveryId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/8675309/cars/%d/liveries/%d", FIRST_CAR.getId(), FIRST_LIVERY.getId())).accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_ACCEPTABLE.value())))
                .andExpect(jsonPath("$.message", is(NOT_ACCEPTABLE.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Accept header must be \"vnd.senorpez.pcars.v1+json\" for Project CARS " +
                        "or \"vnd.senorpez.pcars2.v0+json\" for Project CARS 2")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLivery_InvalidEventId_XXXCarId_XXXLiveryId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(put(String.format("/events/8675309/cars/%d/liveries/%d", FIRST_CAR.getId(), FIRST_LIVERY.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLivery_ValidEventId_ForeignCarId_ValidLiveryId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new CarNotFoundException(SECOND_CAR.getId()));
        mockMvc.perform(get(String.format("/events/%d/cars/%d/liveries/%d", FIRST_EVENT.getId(), SECOND_CAR.getId(), FIRST_LIVERY.getId())).accept(PROJECT_CARS))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Car with ID of %d not found", SECOND_CAR.getId()))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLivery_ValidEventId_ForeignCarId_ValidLiveryId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new CarNotFoundException(SECOND_CAR.getId()));
        mockMvc.perform(get(String.format("/events/%d/cars/%d/liveries/%d", FIRST_EVENT.getId(), SECOND_CAR.getId(), FIRST_LIVERY.getId())).accept(FALLBACK))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Car with ID of %d not found", SECOND_CAR.getId()))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLivery_ValidEventId_ForeignCarId_ValidLiveryId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new CarNotFoundException(SECOND_CAR.getId()));
        mockMvc.perform(get(String.format("/events/%d/cars/%d/liveries/%d", FIRST_EVENT.getId(), SECOND_CAR.getId(), FIRST_LIVERY.getId())).accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_ACCEPTABLE.value())))
                .andExpect(jsonPath("$.message", is(NOT_ACCEPTABLE.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Accept header must be \"vnd.senorpez.pcars.v1+json\" for Project CARS " +
                        "or \"vnd.senorpez.pcars2.v0+json\" for Project CARS 2")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLivery_ValidEventId_ForeignCarId_ValidLiveryId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new CarNotFoundException(SECOND_CAR.getId()));
        mockMvc.perform(put(String.format("/events/%d/cars/%d/liveries/%d", FIRST_EVENT.getId(), SECOND_CAR.getId(), FIRST_LIVERY.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLivery_ValidEventId_ForeignCarId_InvalidLiveryId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new CarNotFoundException(SECOND_CAR.getId()));
        mockMvc.perform(get(String.format("/events/%d/cars/%d/liveries/8675309", FIRST_EVENT.getId(), SECOND_CAR.getId())).accept(PROJECT_CARS))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Car with ID of %d not found", SECOND_CAR.getId()))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLivery_ValidEventId_ForeignCarId_InvalidLiveryId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new CarNotFoundException(SECOND_CAR.getId()));
        mockMvc.perform(get(String.format("/events/%d/cars/%d/liveries/8675309", FIRST_EVENT.getId(), SECOND_CAR.getId())).accept(FALLBACK))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Car with ID of %d not found", SECOND_CAR.getId()))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLivery_ValidEventId_ForeignCarId_InvalidLiveryId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new CarNotFoundException(SECOND_CAR.getId()));
        mockMvc.perform(get(String.format("/events/%d/cars/%d/liveries/8675309", FIRST_EVENT.getId(), SECOND_CAR.getId())).accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_ACCEPTABLE.value())))
                .andExpect(jsonPath("$.message", is(NOT_ACCEPTABLE.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Accept header must be \"vnd.senorpez.pcars.v1+json\" for Project CARS " +
                        "or \"vnd.senorpez.pcars2.v0+json\" for Project CARS 2")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void getSingleEventCarLivery_ValidEventId_ForeignCarId_InvalidLiveryId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new CarNotFoundException(SECOND_CAR.getId()));
        mockMvc.perform(put(String.format("/events/%d/cars/%d/liveries/8675309", FIRST_EVENT.getId(), SECOND_CAR.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }
}