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
import static com.senorpez.projectcars.staticdata.Car.ShiftPattern.H;
import static com.senorpez.projectcars.staticdata.Car.ShiftPattern.SEQUENTIAL;
import static com.senorpez.projectcars.staticdata.Car.Shifter.PADDLES;
import static com.senorpez.projectcars.staticdata.Car.Shifter.SHIFTER;
import static com.senorpez.projectcars.staticdata.SupportedMediaTypes.FALLBACK;
import static com.senorpez.projectcars.staticdata.SupportedMediaTypes.PROJECT_CARS;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.ALL;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class CarControllerTest {
    private MockMvc mockMvc;
    private static final MediaType INVALID_MEDIA_TYPE = new MediaType("application", "invalid+json", UTF_8);
    private static final ClassLoader CLASS_LOADER = CarControllerTest.class.getClassLoader();
    private static InputStream CAR_SCHEMA;
    private static InputStream CAR_CLASS_SCHEMA;
    private static InputStream CAR_COLLECTION_SCHEMA;
    private static InputStream ERROR_SCHEMA;

    private static final Car FIRST_CAR = new CarBuilder()
            .setId(-2046825312)
            .setManufacturer("Ruf")
            .setModel("CTR3 SMS-R")
            .setCarClass(new CarClassBuilder()
                    .setId(1)
                    .setName("GT1X")
                    .build())
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
                    new LiveryBuilder().setId(51).setName("Ruf CTR3 SMS-R #32").build(),
                    new LiveryBuilder().setId(52).setName("Ruf CTR3 SMS-R #33").build())))
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

    @InjectMocks
    CarController carController;

    @Mock
    private APIService apiService;

    @Before
    public void setUp() throws Exception {
        CAR_COLLECTION_SCHEMA = CLASS_LOADER.getResourceAsStream("cars.schema.json");
        CAR_SCHEMA = CLASS_LOADER.getResourceAsStream("car.schema.json");
        CAR_CLASS_SCHEMA = CLASS_LOADER.getResourceAsStream("class.schema.json");
        ERROR_SCHEMA = CLASS_LOADER.getResourceAsStream("error.schema.json");
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new CarController(apiService))
                .setMessageConverters(HALMessageConverter.getConverter(Collections.singletonList(ALL)))
                .setControllerAdvice(new APIExceptionHandler())
                .build();
    }

    @Test
    public void getAllCars_ValidAcceptHeader() throws Exception {
        when(apiService.findAll(anyCollection())).thenReturn(Arrays.asList(FIRST_CAR, SECOND_CAR));

        mockMvc.perform(get("/cars").accept(PROJECT_CARS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(PROJECT_CARS))
                .andExpect(content().string(matchesJsonSchema(CAR_COLLECTION_SCHEMA)))
                .andExpect(jsonPath("$._embedded.pcars:car", hasItem(
                        allOf(
                                hasEntry("id", (Object) FIRST_CAR.getId()),
                                hasEntry("name", (Object) String.join(" ", FIRST_CAR.getManufacturer(), FIRST_CAR.getModel())),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasEntry("href", String.format("http://localhost/cars/%d", FIRST_CAR.getId()))))))))
                .andExpect(jsonPath("$._embedded.pcars:car", hasItem(
                        allOf(
                                hasEntry("id", (Object) SECOND_CAR.getId()),
                                hasEntry("name", (Object) String.join(" ", SECOND_CAR.getManufacturer(), SECOND_CAR.getModel())),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasEntry("href", String.format("http://localhost/cars/%d", SECOND_CAR.getId()))))))))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", "http://localhost/cars")))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))));

        verify(apiService, times(1)).findAll(anyCollection());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getAllCars_FallbackAcceptHeader() throws Exception {
        when(apiService.findAll(anyCollection())).thenReturn(Arrays.asList(FIRST_CAR, SECOND_CAR));

        mockMvc.perform(get("/cars").accept(FALLBACK))
                .andExpect(status().isOk())
                .andExpect(content().contentType(FALLBACK))
                .andExpect(content().string(matchesJsonSchema(CAR_COLLECTION_SCHEMA)))
                .andExpect(jsonPath("$._embedded.pcars:car", hasItem(
                        allOf(
                                hasEntry("id", (Object) FIRST_CAR.getId()),
                                hasEntry("name", (Object) String.join(" ", FIRST_CAR.getManufacturer(), FIRST_CAR.getModel())),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasEntry("href", String.format("http://localhost/cars/%d", FIRST_CAR.getId()))))))))
                .andExpect(jsonPath("$._embedded.pcars:car", hasItem(
                        allOf(
                                hasEntry("id", (Object) SECOND_CAR.getId()),
                                hasEntry("name", (Object) String.join(" ", SECOND_CAR.getManufacturer(), SECOND_CAR.getModel())),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasEntry("href", String.format("http://localhost/cars/%d", SECOND_CAR.getId()))))))))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", "http://localhost/cars")))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))));

        verify(apiService, times(1)).findAll(anyCollection());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getAllCars_InvalidAcceptHeader() throws Exception {
        when(apiService.findAll(anyCollection())).thenReturn(Arrays.asList(FIRST_CAR, SECOND_CAR));

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
        when(apiService.findAll(anyCollection())).thenReturn(Arrays.asList(FIRST_CAR, SECOND_CAR));

        mockMvc.perform(put("/cars").accept(PROJECT_CARS))
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
        when(apiService.findOne(anyCollection(), any(), any())).thenReturn(FIRST_CAR);

        mockMvc.perform(get(String.format("/cars/%d", FIRST_CAR.getId())).accept(PROJECT_CARS))
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

                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", String.format("http://localhost/cars/%d", FIRST_CAR.getId()))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))

                .andExpect(jsonPath("$._links.pcars:cars", hasEntry("href", "http://localhost/cars")))
                .andExpect(jsonPath("$._links.pcars:class",
                        hasItems(
                                hasEntry("href", String.format("http://localhost/classes/%d", FIRST_CAR.getCarClass().getId())),
                                hasEntry("href", String.format("http://localhost/cars/%d/class", FIRST_CAR.getId()))
                        )))
                .andExpect(jsonPath("$._links.pcars:liveries", hasEntry("href", String.format("http://localhost/cars/%d/liveries", FIRST_CAR.getId()))));

        verify(apiService, times(1)).findOne(anyCollection(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleCar_ValidId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(anyCollection(), any(), any())).thenReturn(FIRST_CAR);

        mockMvc.perform(get(String.format("/cars/%d", FIRST_CAR.getId())).accept(FALLBACK))
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

                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", String.format("http://localhost/cars/%d", FIRST_CAR.getId()))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))

                .andExpect(jsonPath("$._links.pcars:cars", hasEntry("href", "http://localhost/cars")))
                .andExpect(jsonPath("$._links.pcars:class",
                        hasItems(
                                hasEntry("href", String.format("http://localhost/classes/%d", FIRST_CAR.getCarClass().getId())),
                                hasEntry("href", String.format("http://localhost/cars/%d/class", FIRST_CAR.getId()))
                        )))
                .andExpect(jsonPath("$._links.pcars:liveries", hasEntry("href", String.format("http://localhost/cars/%d/liveries", FIRST_CAR.getId()))));

        verify(apiService, times(1)).findOne(anyCollection(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleCar_ValidId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(anyCollection(), any(), any())).thenReturn(FIRST_CAR);

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
        when(apiService.findOne(anyCollection(), any(), any())).thenReturn(FIRST_CAR);

        mockMvc.perform(put(String.format("/cars/%d", FIRST_CAR.getId())).accept(PROJECT_CARS))
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
        when(apiService.findOne(anyCollection(), any(), any())).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(get("/cars/8675309").accept(PROJECT_CARS))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Car with ID of %d not found", 8675309))));

        verify(apiService, times(1)).findOne(anyCollection(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleCar_InvalidId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(anyCollection(), any(), any())).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(get("/cars/8675309").accept(FALLBACK))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Car with ID of %d not found", 8675309))));

        verify(apiService, times(1)).findOne(anyCollection(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleCar_InvalidId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(anyCollection(), any(), any())).thenThrow(new CarNotFoundException(8675309));

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
        when(apiService.findOne(anyCollection(), any(), any())).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(put("/cars/8675309").accept(PROJECT_CARS))
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
        when(apiService.findOne(anyCollection(), any(), any())).thenReturn(FIRST_CAR);

        mockMvc.perform(get(String.format("/cars/%d/class", FIRST_CAR.getId())).accept(PROJECT_CARS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(PROJECT_CARS))
                .andExpect(content().string(matchesJsonSchema(CAR_CLASS_SCHEMA)))
                .andExpect(jsonPath("$.id", is(FIRST_CAR.getCarClass().getId())))
                .andExpect(jsonPath("$.name", is(FIRST_CAR.getCarClass().getName())))

                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost/")))
                .andExpect(jsonPath("$._links.self",
                        hasItems(
                                hasEntry("href", String.format("http://localhost/cars/%d/class", FIRST_CAR.getId())),
                                hasEntry("href", String.format("http://localhost/classes/%d", FIRST_CAR.getCarClass().getId())))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))

                .andExpect(jsonPath("$._links.pcars:car", hasEntry("href", String.format("http://localhost/cars/%d", FIRST_CAR.getId()))))
                .andExpect(jsonPath("$._links.pcars:classes", hasEntry("href", "http://localhost/classes")));

        verify(apiService, times(1)).findOne(anyCollection(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleCarClass_ValidId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(anyCollection(), any(), any())).thenReturn(FIRST_CAR);

        mockMvc.perform(get(String.format("/cars/%d/class", FIRST_CAR.getId())).accept(FALLBACK))
                .andExpect(status().isOk())
                .andExpect(content().contentType(FALLBACK))
                .andExpect(content().string(matchesJsonSchema(CAR_CLASS_SCHEMA)))
                .andExpect(jsonPath("$.id", is(FIRST_CAR.getCarClass().getId())))
                .andExpect(jsonPath("$.name", is(FIRST_CAR.getCarClass().getName())))

                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost/")))
                .andExpect(jsonPath("$._links.self",
                        hasItems(
                                hasEntry("href", String.format("http://localhost/cars/%d/class", FIRST_CAR.getId())),
                                hasEntry("href", String.format("http://localhost/classes/%d", FIRST_CAR.getCarClass().getId())))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))

                .andExpect(jsonPath("$._links.pcars:car", hasEntry("href", String.format("http://localhost/cars/%d", FIRST_CAR.getId()))))
                .andExpect(jsonPath("$._links.pcars:classes", hasEntry("href", "http://localhost/classes")));

        verify(apiService, times(1)).findOne(anyCollection(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleCarClass_ValidId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(anyCollection(), any(), any())).thenReturn(FIRST_CAR);

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
        when(apiService.findOne(anyCollection(), any(), any())).thenReturn(FIRST_CAR);

        mockMvc.perform(put(String.format("/cars/%d/class", FIRST_CAR.getId())).accept(PROJECT_CARS))
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
        when(apiService.findOne(anyCollection(), any(), any())).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(get("/cars/8675309/class").accept(PROJECT_CARS))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Car with ID of %d not found", 8675309))));

        verify(apiService, times(1)).findOne(anyCollection(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleCarClass_InvalidId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(anyCollection(), any(), any())).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(get("/cars/8675309/class").accept(FALLBACK))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Car with ID of %d not found", 8675309))));

        verify(apiService, times(1)).findOne(anyCollection(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void getSingleCarClass_InvalidId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(anyCollection(), any(), any())).thenThrow(new CarNotFoundException(8675309));

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
        when(apiService.findOne(anyCollection(), any(), any())).thenThrow(new CarNotFoundException(8675309));

        mockMvc.perform(put("/cars/8675309/class").accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }
}
