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
public class RoundControllerTest {
    private MockMvc mockMvc;
    private static final MediaType INVALID_MEDIA_TYPE = new MediaType("application", "invalid+json", UTF_8);
    private static final ClassLoader CLASS_LOADER = RoundControllerTest.class.getClassLoader();
    private static InputStream ROUND_SCHEMA;
    private static InputStream ROUND_COLLECTION_SCHEMA;
    private static InputStream TRACK_SCHEMA;
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

    private static final Track FIRST_TRACK = new TrackBuilder()
            .setId(766599953)
            .setName("Glencairn East")
            .setLocation("Glencairn")
            .setVariation("East")
            .setLength(510.1722f)
            .setPitEntry(Arrays.asList(null, null))
            .setPitExit(Arrays.asList(null, null))
            .setGridSize(16)
            .build();

    private static final Track SECOND_TRACK = new TrackBuilder()
            .setId(-1408779593)
            .setName("Glencairn West")
            .setLocation("Glencairn")
            .setVariation("West")
            .setLength(451.7371f)
            .setPitEntry(Arrays.asList(null, null))
            .setPitExit(Arrays.asList(null, null))
            .setGridSize(16)
            .build();

    private static final Round FIRST_ROUND = new RoundBuilder()
            .setId(1)
            .setTrack(FIRST_TRACK)
            .setRaces(new HashSet<>(Arrays.asList(FIRST_RACE, SECOND_RACE)))
            .build();

    private static final Round SECOND_ROUND = new RoundBuilder()
            .setId(2)
            .setTrack(SECOND_TRACK)
            .setRaces(new HashSet<>(Arrays.asList(FIRST_RACE, SECOND_RACE)))
            .build();

    private static final Event FIRST_EVENT = new EventBuilder()
            .setId(1)
            .setName("Kart One UK Nationals")
            .setTier(8)
            .setVerified(true)
            .setRounds(new HashSet<>(Arrays.asList(FIRST_ROUND, SECOND_ROUND)))
            .build();

    @InjectMocks
    RoundController roundController;

    @Mock
    private APIService apiService;

    @Before
    public void setUp() throws Exception {
        ROUND_COLLECTION_SCHEMA = CLASS_LOADER.getResourceAsStream("rounds.schema.json");
        ROUND_SCHEMA = CLASS_LOADER.getResourceAsStream("round.schema.json");
        TRACK_SCHEMA = CLASS_LOADER.getResourceAsStream("track.schema.json");
        ERROR_SCHEMA = CLASS_LOADER.getResourceAsStream("error.schema.json");
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new RoundController(apiService, Collections.singletonList(FIRST_EVENT)))
                .setMessageConverters(HALMessageConverter.getConverter(Collections.singletonList(ALL)))
                .setControllerAdvice(new APIExceptionHandler())
                .build();
    }

    @Test
    public void GetAllRounds_ValidEventId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT);

        mockMvc.perform(get(String.format("/events/%d/rounds/", FIRST_EVENT.getId())).accept(PROJECT_CARS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(PROJECT_CARS))
                .andExpect(content().string(matchesJsonSchema(ROUND_COLLECTION_SCHEMA)))
                .andExpect(jsonPath("$._embedded.pcars:round", hasItem(
                        allOf(
                                hasEntry("id", (Object) FIRST_ROUND.getId()),
                                hasEntry("track", (Object) FIRST_ROUND.getTrack().getName()),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasEntry("href", String.format("http://localhost/events/%d/rounds/%d", FIRST_EVENT.getId(), FIRST_ROUND.getId()))))))))
                .andExpect(jsonPath("$._embedded.pcars:round", hasItem(
                        allOf(
                                hasEntry("id", (Object) SECOND_ROUND.getId()),
                                hasEntry("track", (Object) SECOND_ROUND.getTrack().getName()),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasEntry("href", String.format("http://localhost/events/%d/rounds/%d", FIRST_EVENT.getId(), SECOND_ROUND.getId()))))))))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", String.format("http://localhost/events/%d/rounds", FIRST_EVENT.getId()))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))));

        verify(apiService, times(1)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void GetAllRounds_ValidEventId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT);

        mockMvc.perform(get(String.format("/events/%d/rounds/", FIRST_EVENT.getId())).accept(FALLBACK))
                .andExpect(status().isOk())
                .andExpect(content().contentType(FALLBACK))
                .andExpect(content().string(matchesJsonSchema(ROUND_COLLECTION_SCHEMA)))
                .andExpect(jsonPath("$._embedded.pcars:round", hasItem(
                        allOf(
                                hasEntry("id", (Object) FIRST_ROUND.getId()),
                                hasEntry("track", (Object) FIRST_ROUND.getTrack().getName()),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasEntry("href", String.format("http://localhost/events/%d/rounds/%d", FIRST_EVENT.getId(), FIRST_ROUND.getId()))))))))
                .andExpect(jsonPath("$._embedded.pcars:round", hasItem(
                        allOf(
                                hasEntry("id", (Object) SECOND_ROUND.getId()),
                                hasEntry("track", (Object) SECOND_ROUND.getTrack().getName()),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasEntry("href", String.format("http://localhost/events/%d/rounds/%d", FIRST_EVENT.getId(), SECOND_ROUND.getId()))))))))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", String.format("http://localhost/events/%d/rounds", FIRST_EVENT.getId()))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))));

        verify(apiService, times(1)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void GetAllRounds_ValidEventId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT);

        mockMvc.perform(get(String.format("/events/%d/rounds/", FIRST_EVENT.getId())).accept(INVALID_MEDIA_TYPE))
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
    public void GetAllRounds_ValidEventId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT);

        mockMvc.perform(put(String.format("/events/%d/rounds/", FIRST_EVENT.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }
    
    @Test
    public void GetAllRounds_InvalidEventId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(get("/events/8675309/rounds/").accept(PROJECT_CARS))
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
    public void GetAllRounds_InvalidEventId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(get("/events/8675309/rounds/").accept(FALLBACK))
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
    public void GetAllRounds_InvalidEventId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(get("/events/8675309/rounds/").accept(INVALID_MEDIA_TYPE))
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
    public void GetAllRounds_InvalidEventId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(put("/events/8675309/rounds/").accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void GetSingleRound_ValidEventId_ValidRoundId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT, FIRST_ROUND);

        mockMvc.perform(get(String.format("/events/%d/rounds/%d", FIRST_EVENT.getId(), FIRST_ROUND.getId())).accept(PROJECT_CARS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(PROJECT_CARS))
                .andExpect(content().string(matchesJsonSchema(ROUND_SCHEMA)))
                .andExpect(jsonPath("$.id", is(FIRST_ROUND.getId())))
                .andExpect(jsonPath("$.track", is(FIRST_ROUND.getTrack().getName())))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", String.format("http://localhost/events/%d/rounds/%d", FIRST_EVENT.getId(), FIRST_ROUND.getId()))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))
                .andExpect(jsonPath("$._links.pcars:races", hasEntry("href", String.format("http://localhost/events/%d/rounds/%d/races", FIRST_EVENT.getId(), FIRST_ROUND.getId()))))
                .andExpect(jsonPath("$._links.pcars:rounds", hasEntry("href", String.format("http://localhost/events/%d/rounds", FIRST_EVENT.getId()))))
                .andExpect(jsonPath("$._links.pcars:track",
                        hasItems(
                                hasEntry("href", String.format("http://localhost/tracks/%d", FIRST_ROUND.getTrack().getId())),
                                hasEntry("href", String.format("http://localhost/events/%d/rounds/%d/track", FIRST_EVENT.getId(), FIRST_ROUND.getId())))));
                
        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }
    
    @Test
    public void GetSingleRound_ValidEventId_ValidRoundId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT, FIRST_ROUND);

        mockMvc.perform(get(String.format("/events/%d/rounds/%d", FIRST_EVENT.getId(), FIRST_ROUND.getId())).accept(FALLBACK))
                .andExpect(status().isOk())
                .andExpect(content().contentType(FALLBACK))
                .andExpect(content().string(matchesJsonSchema(ROUND_SCHEMA)))
                .andExpect(jsonPath("$.id", is(FIRST_ROUND.getId())))
                .andExpect(jsonPath("$.track", is(FIRST_ROUND.getTrack().getName())))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", String.format("http://localhost/events/%d/rounds/%d", FIRST_EVENT.getId(), FIRST_ROUND.getId()))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))
                .andExpect(jsonPath("$._links.pcars:races", hasEntry("href", String.format("http://localhost/events/%d/rounds/%d/races", FIRST_EVENT.getId(), FIRST_ROUND.getId()))))
                .andExpect(jsonPath("$._links.pcars:rounds", hasEntry("href", String.format("http://localhost/events/%d/rounds", FIRST_EVENT.getId()))))
                .andExpect(jsonPath("$._links.pcars:track",
                        hasItems(
                                hasEntry("href", String.format("http://localhost/tracks/%d", FIRST_ROUND.getTrack().getId())),
                                hasEntry("href", String.format("http://localhost/events/%d/rounds/%d/track", FIRST_EVENT.getId(), FIRST_ROUND.getId())))));
                
        verify(apiService, times(2)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void GetSingleRound_ValidEventId_ValidRoundId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT, FIRST_ROUND);

        mockMvc.perform(get(String.format("/events/%d/rounds/%d", FIRST_EVENT.getId(), FIRST_ROUND.getId())).accept(INVALID_MEDIA_TYPE))
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
    public void GetSingleRound_ValidEventId_ValidRoundId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT, FIRST_ROUND);

        mockMvc.perform(put(String.format("/events/%d/rounds/%d", FIRST_EVENT.getId(), FIRST_ROUND.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void GetSingleRound_ValidEventId_InvalidRoundId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new RoundNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/%d/rounds/8675309", FIRST_EVENT.getId())).accept(PROJECT_CARS))
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
    public void GetSingleRound_ValidEventId_InvalidRoundId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new RoundNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/%d/rounds/8675309", FIRST_EVENT.getId())).accept(FALLBACK))
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
    public void GetSingleRound_ValidEventId_InvalidRoundId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new RoundNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/%d/rounds/8675309", FIRST_EVENT.getId())).accept(INVALID_MEDIA_TYPE))
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
    public void GetSingleRound_ValidEventId_InvalidRoundId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_EVENT).thenThrow(new RoundNotFoundException(8675309));

        mockMvc.perform(put(String.format("/events/%d/rounds/8675309", FIRST_EVENT.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void GetSingleRound_InvalidEventId_XXXRoundId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/8675309/rounds/%d", FIRST_ROUND.getId())).accept(PROJECT_CARS))
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
    public void GetSingleRound_InvalidEventId_XXXRoundId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/8675309/rounds/%d", FIRST_ROUND.getId())).accept(FALLBACK))
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
    public void GetSingleRound_InvalidEventId_XXXRoundId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(get(String.format("/events/8675309/rounds/%d", FIRST_ROUND.getId())).accept(INVALID_MEDIA_TYPE))
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
    public void GetSingleRound_InvalidEventId_XXXRoundId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new EventNotFoundException(8675309));

        mockMvc.perform(put(String.format("/events/8675309/rounds/%d", FIRST_ROUND.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void GetSingleRoundTrack_ValidEventId_ValidRoundId_ValidAcceptHeader() throws Exception {
        mockMvc.perform(get(String.format("/events/%d/rounds/%d/track", FIRST_EVENT.getId(), FIRST_ROUND.getId())).accept(PROJECT_CARS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(PROJECT_CARS))
                .andExpect(content().string(matchesJsonSchema(TRACK_SCHEMA)))
                .andExpect(jsonPath("$.id", is(FIRST_TRACK.getId())))
                .andExpect(jsonPath("$.name", is(FIRST_TRACK.getName())))
                .andExpect(jsonPath("$.location", is(FIRST_TRACK.getLocation())))
                .andExpect(jsonPath("$.variation", is(FIRST_TRACK.getVariation())))
                .andExpect(jsonPath("$.length", closeTo((double) FIRST_TRACK.getLength(), 0.001)))
                .andExpect(jsonPath("$.pitEntry", is(FIRST_TRACK.getPitEntry())))
                .andExpect(jsonPath("$.pitExit", is(FIRST_TRACK.getPitExit())))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost/")))
                .andExpect(jsonPath("$._links.self",
                        hasItems(
                                hasEntry("href", String.format("http://localhost/events/%d/rounds/%d/track", FIRST_EVENT.getId(), FIRST_ROUND.getId())),
                                hasEntry("href", String.format("http://localhost/tracks/%d", FIRST_TRACK.getId())))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))
                .andExpect(jsonPath("$._links.pcars:tracks", hasEntry("href", "http://localhost/tracks")))
                .andExpect(jsonPath("$._links.pcars:round", hasEntry("href", String.format("http://localhost/events/%d/rounds/%d", FIRST_EVENT.getId(), FIRST_ROUND.getId()))));
    }

    @Test
    public void GetSingleRoundTrack_ValidEventId_ValidRoundId_FallbackAcceptHeader() throws Exception {
        mockMvc.perform(get(String.format("/events/%d/rounds/%d/track", FIRST_EVENT.getId(), FIRST_ROUND.getId())).accept(FALLBACK))
                .andExpect(status().isOk())
                .andExpect(content().contentType(FALLBACK))
                .andExpect(content().string(matchesJsonSchema(TRACK_SCHEMA)))
                .andExpect(jsonPath("$.id", is(FIRST_TRACK.getId())))
                .andExpect(jsonPath("$.name", is(FIRST_TRACK.getName())))
                .andExpect(jsonPath("$.location", is(FIRST_TRACK.getLocation())))
                .andExpect(jsonPath("$.variation", is(FIRST_TRACK.getVariation())))
                .andExpect(jsonPath("$.length", closeTo((double) FIRST_TRACK.getLength(), 0.001)))
                .andExpect(jsonPath("$.pitEntry", is(FIRST_TRACK.getPitEntry())))
                .andExpect(jsonPath("$.pitExit", is(FIRST_TRACK.getPitExit())))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost/")))
                .andExpect(jsonPath("$._links.self",
                        hasItems(
                                hasEntry("href", String.format("http://localhost/events/%d/rounds/%d/track", FIRST_EVENT.getId(), FIRST_ROUND.getId())),
                                hasEntry("href", String.format("http://localhost/tracks/%d", FIRST_TRACK.getId())))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))
                .andExpect(jsonPath("$._links.pcars:tracks", hasEntry("href", "http://localhost/tracks")))
                .andExpect(jsonPath("$._links.pcars:round", hasEntry("href", String.format("http://localhost/events/%d/rounds/%d", FIRST_EVENT.getId(), FIRST_ROUND.getId()))));
    }
    
    @Test
    public void GetSingleRoundTrack_ValidEventId_ValidRoundId_InvalidAcceptHeader() throws Exception {
        mockMvc.perform(get(String.format("/events/%d/rounds/%d/track", FIRST_EVENT.getId(), FIRST_ROUND.getId())).accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_ACCEPTABLE.value())))
                .andExpect(jsonPath("$.message", is(NOT_ACCEPTABLE.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Accept header must be \"vnd.senorpez.pcars.v1+json\" for Project CARS " +
                        "or \"vnd.senorpez.pcars2.v1+json\" for Project CARS 2")));
    }
    
    @Test
    public void GetSingleRoundTrack_ValidEventId_ValidRoundId_InvalidMethod() throws Exception {
        mockMvc.perform(put(String.format("/events/%d/rounds/%d/track", FIRST_EVENT.getId(), FIRST_ROUND.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));
    }

    @Test
    public void GetSingleRoundTrack_ValidEventId_InvalidRoundId_ValidAcceptHeader() throws Exception {
        mockMvc.perform(get(String.format("/events/%d/rounds/8675309/track", FIRST_EVENT.getId())).accept(PROJECT_CARS))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Round with ID of %d not found", 8675309))));
    }

    @Test
    public void GetSingleRoundTrack_ValidEventId_InvalidRoundId_FallbackAcceptHeader() throws Exception {
        mockMvc.perform(get(String.format("/events/%d/rounds/8675309/track", FIRST_EVENT.getId())).accept(FALLBACK))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Round with ID of %d not found", 8675309))));
    }
    
    @Test
    public void GetSingleRoundTrack_ValidEventId_InvalidRoundId_InvalidAcceptHeader() throws Exception {
        mockMvc.perform(get(String.format("/events/%d/rounds/8675309/track", FIRST_EVENT.getId())).accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_ACCEPTABLE.value())))
                .andExpect(jsonPath("$.message", is(NOT_ACCEPTABLE.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Accept header must be \"vnd.senorpez.pcars.v1+json\" for Project CARS " +
                        "or \"vnd.senorpez.pcars2.v1+json\" for Project CARS 2")));
    }
    
    @Test
    public void GetSingleRoundTrack_ValidEventId_InvalidRoundId_InvalidMethod() throws Exception {
        mockMvc.perform(put(String.format("/events/%d/rounds/8675309/track", FIRST_EVENT.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));
    }

    @Test
    public void GetSingleRoundTrack_InvalidEventId_XXXRoundId_ValidAcceptHeader() throws Exception {
        mockMvc.perform(get(String.format("/events/8675309/rounds/%d/track", FIRST_ROUND.getId())).accept(PROJECT_CARS))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Event with ID of %d not found", 8675309))));
    }

    @Test
    public void GetSingleRoundTrack_InvalidEventId_XXXRoundId_FallbackAcceptHeader() throws Exception {
        mockMvc.perform(get(String.format("/events/8675309/rounds/%d/track", FIRST_ROUND.getId())).accept(FALLBACK))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Event with ID of %d not found", 8675309))));
    }
    
    @Test
    public void GetSingleRoundTrack_InvalidEventId_XXXRoundId_InvalidAcceptHeader() throws Exception {
        mockMvc.perform(get(String.format("/events/8675309/rounds/%d/track", FIRST_ROUND.getId())).accept(INVALID_MEDIA_TYPE))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_ACCEPTABLE.value())))
                .andExpect(jsonPath("$.message", is(NOT_ACCEPTABLE.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Accept header must be \"vnd.senorpez.pcars.v1+json\" for Project CARS " +
                        "or \"vnd.senorpez.pcars2.v1+json\" for Project CARS 2")));
    }
    
    @Test
    public void GetSingleRoundTrack_InvalidEventId_XXXRoundId_InvalidMethod() throws Exception {
        mockMvc.perform(put(String.format("/events/8675309/rounds/%d/track", FIRST_ROUND.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));
    }
}
