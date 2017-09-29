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
public class RaceControllerTest {
    private MockMvc mockMvc;
    private static final MediaType INVALID_MEDIA_TYPE = new MediaType("application", "invalid+json", UTF_8);
    private static final ClassLoader CLASS_LOADER = RaceControllerTest.class.getClassLoader();
    private static InputStream RACE_SCHEMA;
    private static InputStream RACE_COLLECTION_SCHEMA;
    private static InputStream ERROR_SCHEMA;

    private static final Race FIRST_RACE = new RaceBuilder()
            .setId(1)
            .setLaps(10)
            .setTime(null)
            .setType("Sprint Race")
            .build();

    private static final Race SECOND_RACE = new RaceBuilder()
            .setId(2)
            .setLaps(15)
            .setTime(null)
            .setType("Main Race")
            .build();

    private static final Round FIRST_ROUND = new RoundBuilder()
            .setId(1)
            .setTrack(new TrackBuilder()
                    .setId(766599953)
                    .setName("Glencairn East")
                    .setLocation("Glencairn")
                    .setVariation("East")
                    .setLength(510.1722f)
                    .setPitEntry(Arrays.asList(null, null))
                    .setPitExit(Arrays.asList(null, null))
                    .setGridSize(16)
                    .build())
            .setRaces(new HashSet<>(Arrays.asList(FIRST_RACE, SECOND_RACE)))
            .build();

    private static final Event FIRST_EVENT = new EventBuilder()
            .setId(1)
            .setName("Kart One UK Nationals")
            .setTier(8)
            .setVerified(true)
            .setRounds(new HashSet<>(Collections.singletonList(FIRST_ROUND)))
            .build();
    
    @InjectMocks
    RaceController raceController;
    
    @Mock
    private APIService apiService;

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Before
    public void setUp() throws Exception {
        RACE_COLLECTION_SCHEMA = CLASS_LOADER.getResourceAsStream("races.schema.json");
        RACE_SCHEMA = CLASS_LOADER.getResourceAsStream("race.schema.json");
        ERROR_SCHEMA = CLASS_LOADER.getResourceAsStream("error.schema.json");
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new RaceController(apiService, Collections.singletonList(FIRST_EVENT)))
                .setMessageConverters(HALMessageConverter.getConverter(Collections.singletonList(ALL)))
                .setControllerAdvice(new APIExceptionHandler())
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Test
    public void GetAllRaces_ValidEventId_ValidRoundId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT, FIRST_ROUND);

        mockMvc.perform(get(String.format("/events/%d/rounds/%d/races", FIRST_EVENT.getId(), FIRST_ROUND.getId())).accept(PROJECT_CARS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(PROJECT_CARS))
                .andExpect(content().string(matchesJsonSchema(RACE_COLLECTION_SCHEMA)))
                .andExpect(jsonPath("$._embedded.pcars:race", hasItem(
                        allOf(
                                hasEntry("id", (Object) FIRST_RACE.getId()),
                                hasEntry("laps", (Object) FIRST_RACE.getLaps()),
                                hasEntry("time", (Object) FIRST_RACE.getTime()),
                                hasEntry("type", (Object) FIRST_RACE.getType()),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasEntry("href", String.format("http://localhost:8080/events/%d/rounds/%d/races/%d", FIRST_EVENT.getId(), FIRST_ROUND.getId(), FIRST_RACE.getId()))))))))
                .andExpect(jsonPath("$._embedded.pcars:race", hasItem(
                        allOf(
                                hasEntry("id", (Object) SECOND_RACE.getId()),
                                hasEntry("laps", (Object) SECOND_RACE.getLaps()),
                                hasEntry("time", (Object) SECOND_RACE.getTime()),
                                hasEntry("type", (Object) SECOND_RACE.getType()),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasEntry("href", String.format("http://localhost:8080/events/%d/rounds/%d/races/%d", FIRST_EVENT.getId(), FIRST_ROUND.getId(), SECOND_RACE.getId()))))))))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost:8080/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", String.format("http://localhost:8080/events/%d/rounds/%d/races", FIRST_EVENT.getId(), FIRST_ROUND.getId()))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost:8080/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))
                .andDo(document("races",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Accept")
                                        .description("Accept header")
                                        .attributes(key("acceptvalue").value(SupportedMediaTypes.PROJECT_CARS_VALUE))),
                        responseFields(
                                fieldWithPath("_embedded.pcars:race").description("Race resources"),
                                fieldWithPath("_embedded.pcars:race[].id").description("ID number"),
                                fieldWithPath("_embedded.pcars:race[].laps").description("Number of laps; null if timed race"),
                                fieldWithPath("_embedded.pcars:race[].time").description("Duration of race; null if laps race"),
                                fieldWithPath("_embedded.pcars:race[].type").description("Type"),
                                subsectionWithPath("_links").ignored(),
                                subsectionWithPath("_embedded.pcars:race[]._links").ignored()),
                        commonLinks.and(
                                linkWithRel("pcars:round").description("Round resource"))));


        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void GetAllRaces_ValidEventId_ValidRoundId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT, FIRST_ROUND);

        mockMvc.perform(get(String.format("/events/%d/rounds/%d/races", FIRST_EVENT.getId(), FIRST_ROUND.getId())).accept(FALLBACK))
                .andExpect(status().isOk())
                .andExpect(content().contentType(FALLBACK))
                .andExpect(content().string(matchesJsonSchema(RACE_COLLECTION_SCHEMA)))
                .andExpect(jsonPath("$._embedded.pcars:race", hasItem(
                        allOf(
                                hasEntry("id", (Object) FIRST_RACE.getId()),
                                hasEntry("laps", (Object) FIRST_RACE.getLaps()),
                                hasEntry("time", (Object) FIRST_RACE.getTime()),
                                hasEntry("type", (Object) FIRST_RACE.getType()),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasEntry("href", String.format("http://localhost:8080/events/%d/rounds/%d/races/%d", FIRST_EVENT.getId(), FIRST_ROUND.getId(), FIRST_RACE.getId()))))))))
                .andExpect(jsonPath("$._embedded.pcars:race", hasItem(
                        allOf(
                                hasEntry("id", (Object) SECOND_RACE.getId()),
                                hasEntry("laps", (Object) SECOND_RACE.getLaps()),
                                hasEntry("time", (Object) SECOND_RACE.getTime()),
                                hasEntry("type", (Object) SECOND_RACE.getType()),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasEntry("href", String.format("http://localhost:8080/events/%d/rounds/%d/races/%d", FIRST_EVENT.getId(), FIRST_ROUND.getId(), SECOND_RACE.getId()))))))))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost:8080/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", String.format("http://localhost:8080/events/%d/rounds/%d/races", FIRST_EVENT.getId(), FIRST_ROUND.getId()))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost:8080/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void GetAllRaces_ValidEventId_ValidRoundId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT, FIRST_ROUND);

        mockMvc.perform(get(String.format("/events/%d/rounds/%d/races", FIRST_EVENT.getId(), FIRST_ROUND.getId())).accept(INVALID_MEDIA_TYPE))
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
    public void GetAllRaces_ValidEventId_ValidRoundId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT, FIRST_ROUND);

        mockMvc.perform(put(String.format("/events/%d/rounds/%d/races", FIRST_EVENT.getId(), FIRST_ROUND.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void GetAllRaces_ValidEventId_InvalidRoundId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new RoundNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/%d/rounds/8675309/races", FIRST_EVENT.getId())).accept(PROJECT_CARS))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Round with ID of %d not found", 8675309))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void GetAllRaces_ValidEventId_InvalidRoundId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new RoundNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/%d/rounds/8675309/races", FIRST_EVENT.getId())).accept(FALLBACK))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Round with ID of %d not found", 8675309))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void GetAllRaces_ValidEventId_InvalidRoundId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new RoundNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/%d/rounds/8675309/races", FIRST_EVENT.getId())).accept(INVALID_MEDIA_TYPE))
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
    public void GetAllRaces_ValidEventId_InvalidRoundId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new RoundNotFoundException(8675309));

        mockMvc.perform(put(String.format("/events/%d/rounds/8675309/races", FIRST_EVENT.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void GetAllRaces_InvalidEventId_XXXRoundId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/8675309/rounds/%d/races", FIRST_ROUND.getId())).accept(PROJECT_CARS))
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
    public void GetAllRaces_InvalidEventId_XXXRoundId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/8675309/rounds/%d/races", FIRST_ROUND.getId())).accept(FALLBACK))
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
    public void GetAllRaces_InvalidEventId_XXXRoundId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/8675309/rounds/%d/races", FIRST_ROUND.getId())).accept(INVALID_MEDIA_TYPE))
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
    public void GetAllRaces_InvalidEventId_XXXRoundId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(put(String.format("/events/8675309/rounds/%d/races", FIRST_ROUND.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void GetSingleRace_ValidEventId_ValidRoundId_ValidRaceId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT, FIRST_ROUND, FIRST_RACE);

        mockMvc.perform(get(String.format("/events/%d/rounds/%d/races/%d", FIRST_EVENT.getId(), FIRST_ROUND.getId(), FIRST_RACE.getId())).accept(PROJECT_CARS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(PROJECT_CARS))
                .andExpect(content().string(matchesJsonSchema(RACE_SCHEMA)))
                .andExpect(jsonPath("$.id", is(FIRST_RACE.getId())))
                .andExpect(jsonPath("$.laps", is(FIRST_RACE.getLaps())))
                .andExpect(jsonPath("$.time", is(FIRST_RACE.getTime())))
                .andExpect(jsonPath("$.type", is(FIRST_RACE.getType())))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost:8080/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", String.format("http://localhost:8080/events/%d/rounds/%d/races/%d", FIRST_EVENT.getId(), FIRST_ROUND.getId(), FIRST_RACE.getId()))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost:8080/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))
                .andExpect(jsonPath("$._links.pcars:races", hasEntry("href", String.format("http://localhost:8080/events/%d/rounds/%d/races", FIRST_EVENT.getId(), FIRST_ROUND.getId()))));

        verify(apiService, times(3)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void GetSingleRace_ValidEventId_ValidRoundId_ValidRaceId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT, FIRST_ROUND, FIRST_RACE);

        mockMvc.perform(get(String.format("/events/%d/rounds/%d/races/%d", FIRST_EVENT.getId(), FIRST_ROUND.getId(), FIRST_RACE.getId())).accept(FALLBACK))
                .andExpect(status().isOk())
                .andExpect(content().contentType(FALLBACK))
                .andExpect(content().string(matchesJsonSchema(RACE_SCHEMA)))
                .andExpect(jsonPath("$.id", is(FIRST_RACE.getId())))
                .andExpect(jsonPath("$.laps", is(FIRST_RACE.getLaps())))
                .andExpect(jsonPath("$.time", is(FIRST_RACE.getTime())))
                .andExpect(jsonPath("$.type", is(FIRST_RACE.getType())))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost:8080/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", String.format("http://localhost:8080/events/%d/rounds/%d/races/%d", FIRST_EVENT.getId(), FIRST_ROUND.getId(), FIRST_RACE.getId()))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost:8080/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))
                .andExpect(jsonPath("$._links.pcars:races", hasEntry("href", String.format("http://localhost:8080/events/%d/rounds/%d/races", FIRST_EVENT.getId(), FIRST_ROUND.getId()))))
                .andDo(document("race",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Accept")
                                        .description("Accept header")
                                        .attributes(key("acceptvalue").value(SupportedMediaTypes.PROJECT_CARS_VALUE))),
                        responseFields(
                                fieldWithPath("id").description("ID number"),
                                fieldWithPath("laps").description("Number of laps; null if timed race"),
                                fieldWithPath("time").description("Duration of race; null if laps race"),
                                fieldWithPath("type").description("Type"),
                                subsectionWithPath("_links").ignored()),
                        commonLinks.and(
                                linkWithRel("pcars:races").description("List of race resources."))));

        verify(apiService, times(3)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void GetSingleRace_ValidEventId_ValidRoundId_ValidRaceId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT, FIRST_ROUND, FIRST_RACE);

        mockMvc.perform(get(String.format("/events/%d/rounds/%d/races/%d", FIRST_EVENT.getId(), FIRST_ROUND.getId(), FIRST_RACE.getId())).accept(INVALID_MEDIA_TYPE))
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
    public void GetSingleRace_ValidEventId_ValidRoundId_ValidRaceId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT, FIRST_ROUND, FIRST_RACE);

        mockMvc.perform(put(String.format("/events/%d/rounds/%d/races/%d", FIRST_EVENT.getId(), FIRST_ROUND.getId(), FIRST_RACE.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void GetSingleRace_ValidEventId_ValidRoundId_InvalidRaceId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT, FIRST_ROUND).thenThrow(new RaceNotFoundException(8675309));
        
        mockMvc.perform(get(String.format("/events/%d/rounds/%d/races/8675309", FIRST_EVENT.getId(), FIRST_ROUND.getId())).accept(PROJECT_CARS))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Race with ID of %d not found", 8675309))));

        verify(apiService, times(3)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void GetSingleRace_ValidEventId_ValidRoundId_InvalidRaceId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT, FIRST_ROUND).thenThrow(new RaceNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/%d/rounds/%d/races/8675309", FIRST_EVENT.getId(), FIRST_ROUND.getId())).accept(FALLBACK))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Race with ID of %d not found", 8675309))));

        verify(apiService, times(3)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void GetSingleRace_ValidEventId_ValidRoundId_InvalidRaceId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT, FIRST_ROUND).thenThrow(new RaceNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/%d/rounds/%d/races/8675309", FIRST_EVENT.getId(), FIRST_ROUND.getId())).accept(INVALID_MEDIA_TYPE))
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
    public void GetSingleRace_ValidEventId_ValidRoundId_InvalidRaceId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT, FIRST_ROUND).thenThrow(new RaceNotFoundException(8675309));

        mockMvc.perform(put(String.format("/events/%d/rounds/%d/races/8675309", FIRST_EVENT.getId(), FIRST_ROUND.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void GetSingleRace_ValidEventId_InvalidRoundId_XXXRaceId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new RoundNotFoundException(8675309));
        
        mockMvc.perform(get(String.format("/events/%d/rounds/8675309/races/%d", FIRST_EVENT.getId(), FIRST_RACE.getId())).accept(PROJECT_CARS))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Round with ID of %d not found", 8675309))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void GetSingleRace_ValidEventId_InvalidRoundId_XXXRaceId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new RoundNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/%d/rounds/8675309/races/%d", FIRST_EVENT.getId(), FIRST_RACE.getId())).accept(FALLBACK))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Round with ID of %d not found", 8675309))));

        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void GetSingleRace_ValidEventId_InvalidRoundId_XXXRaceId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new RoundNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/%d/rounds/8675309/races/%d", FIRST_EVENT.getId(), FIRST_RACE.getId())).accept(INVALID_MEDIA_TYPE))
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
    public void GetSingleRace_ValidEventId_InvalidRoundId_XXXRaceId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new RoundNotFoundException(8675309));

        mockMvc.perform(put(String.format("/events/%d/rounds/8675309/races/%d", FIRST_EVENT.getId(), FIRST_RACE.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void GetSingleRace_InvalidEventId_XXXRoundId_XXXRaceId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));
        
        mockMvc.perform(get(String.format("/events/8675309/rounds/%d/races/%d", FIRST_ROUND.getId(), FIRST_RACE.getId())).accept(PROJECT_CARS))
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
    public void GetSingleRace_InvalidEventId_XXXRoundId_XXXRaceId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/8675309/rounds/%d/races/%d", FIRST_ROUND.getId(), FIRST_RACE.getId())).accept(FALLBACK))
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
    public void GetSingleRace_InvalidEventId_XXXRoundId_XXXRaceId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/8675309/rounds/%d/races/%d", FIRST_ROUND.getId(), FIRST_RACE.getId())).accept(INVALID_MEDIA_TYPE))
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
    public void GetSingleRace_InvalidEventId_XXXRoundId_XXXRaceId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(put(String.format("/events/8675309/rounds/%d/races/%d", FIRST_ROUND.getId(), FIRST_RACE.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }
}
