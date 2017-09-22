package com.senorpez.projectcars.staticdata;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.ALL;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TrackControllerTest {
    private MockMvc mockMvc;
    private static final MediaType INVALID_MEDIA_TYPE = new MediaType("application", "invalid", UTF_8);
    private static final ClassLoader CLASS_LOADER = TrackControllerTest.class.getClassLoader();
    private static InputStream TRACK_SCHEMA;
    private static InputStream TRACK_COLLECTION_SCHEMA;
    private static InputStream ERROR_SCHEMA;

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

    @InjectMocks
    TrackController trackController;

    @Mock
    private APIService apiService;

    @Before
    public void setUp() throws Exception {
        TRACK_SCHEMA = CLASS_LOADER.getResourceAsStream("track.schema.json");
        TRACK_COLLECTION_SCHEMA = CLASS_LOADER.getResourceAsStream("tracks.schema.json");
        ERROR_SCHEMA = CLASS_LOADER.getResourceAsStream("error.schema.json");
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new TrackController(apiService))
                .setMessageConverters(HALMessageConverter.getConverter(Collections.singletonList(ALL)))
                .setControllerAdvice(new APIExceptionHandler())
                .build();
    }

    @Test
    public void GetAllTracks_ValidAcceptHeader() throws Exception {
        when(apiService.findAll(any())).thenReturn(Arrays.asList(FIRST_TRACK, SECOND_TRACK));

        mockMvc.perform(get("/tracks").accept(PROJECT_CARS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(PROJECT_CARS))
                .andExpect(content().string(matchesJsonSchema(TRACK_COLLECTION_SCHEMA)))
                .andExpect(jsonPath("$._embedded.pcars:track", hasItem(
                        allOf(
                                hasEntry("id", (Object) FIRST_TRACK.getId()),
                                hasEntry("name", (Object) FIRST_TRACK.getName()),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasEntry("href", String.format("http://localhost/tracks/%d", FIRST_TRACK.getId()))))))))
                .andExpect(jsonPath("$._embedded.pcars:track", hasItem(
                        allOf(
                                hasEntry("id", (Object) SECOND_TRACK.getId()),
                                hasEntry("name", (Object) SECOND_TRACK.getName()),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasEntry("href", String.format("http://localhost/tracks/%d", SECOND_TRACK.getId()))))))))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", "http://localhost/tracks")))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))));

        verify(apiService, times(1)).findAll(any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void GetAllTracks_FallbackAcceptHeader() throws Exception {
        when(apiService.findAll(any())).thenReturn(Arrays.asList(FIRST_TRACK, SECOND_TRACK));

        mockMvc.perform(get("/tracks").accept(FALLBACK))
                .andExpect(status().isOk())
                .andExpect(content().contentType(FALLBACK))
                .andExpect(content().string(matchesJsonSchema(TRACK_COLLECTION_SCHEMA)))
                .andExpect(jsonPath("$._embedded.pcars:track", hasItem(
                        allOf(
                                hasEntry("id", (Object) FIRST_TRACK.getId()),
                                hasEntry("name", (Object) FIRST_TRACK.getName()),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasEntry("href", String.format("http://localhost/tracks/%d", FIRST_TRACK.getId()))))))))
                .andExpect(jsonPath("$._embedded.pcars:track", hasItem(
                        allOf(
                                hasEntry("id", (Object) SECOND_TRACK.getId()),
                                hasEntry("name", (Object) SECOND_TRACK.getName()),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasEntry("href", String.format("http://localhost/tracks/%d", SECOND_TRACK.getId()))))))))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", "http://localhost/tracks")))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))));

        verify(apiService, times(1)).findAll(any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void GetAllTracks_InvalidAcceptHeader() throws Exception {
        when(apiService.findAll(any())).thenReturn(Arrays.asList(FIRST_TRACK, SECOND_TRACK));

        mockMvc.perform(get("/tracks").accept(INVALID_MEDIA_TYPE))
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
    public void GetAllTracks_InvalidMethod() throws Exception {
        when(apiService.findAll(any())).thenReturn(Arrays.asList(FIRST_TRACK, SECOND_TRACK));

        mockMvc.perform(put("/tracks").accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void GetSingleTrack_ValidTrackId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_TRACK);

        mockMvc.perform(get(String.format("/tracks/%d", FIRST_TRACK.getId())).accept(PROJECT_CARS))
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
                .andExpect(jsonPath("$._links.self", hasEntry("href", String.format("http://localhost/tracks/%d", FIRST_TRACK.getId()))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))
                .andExpect(jsonPath("$._links.pcars:tracks", hasEntry("href", "http://localhost/tracks")));

        verify(apiService, times(1)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void GetSingleTrack_ValidTrackId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_TRACK);

        mockMvc.perform(get(String.format("/tracks/%d", FIRST_TRACK.getId())).accept(FALLBACK))
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
                .andExpect(jsonPath("$._links.self", hasEntry("href", String.format("http://localhost/tracks/%d", FIRST_TRACK.getId()))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))
                .andExpect(jsonPath("$._links.pcars:tracks", hasEntry("href", "http://localhost/tracks")));

        verify(apiService, times(1)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }
    
    @Test
    public void GetSingleTrack_ValidTrackId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_TRACK);

        mockMvc.perform(get(String.format("/tracks/%d", FIRST_TRACK.getId())).accept(INVALID_MEDIA_TYPE))
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
    public void GetSingleTrack_ValidTrackId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_TRACK);

        mockMvc.perform(put(String.format("/tracks/%d", FIRST_TRACK.getId())).accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void GetSingleTrack_InvalidTrackId_ValidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new TrackNotFoundException(8675309));

        mockMvc.perform(get("/tracks/8675309").accept(PROJECT_CARS))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Track with ID of %d not found", 8675309))));

        verify(apiService, times(1)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }

    @Test
    public void GetSingleTrack_InvalidTrackId_FallbackAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new TrackNotFoundException(8675309));

        mockMvc.perform(get("/tracks/8675309").accept(FALLBACK))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is(NOT_FOUND.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is(String.format("Track with ID of %d not found", 8675309))));

        verify(apiService, times(1)).findOne(any(), any(), any());
        verifyNoMoreInteractions(apiService);
    }
    
    @Test
    public void GetSingleTrack_InvalidTrackId_InvalidAcceptHeader() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new TrackNotFoundException(8675309));

        mockMvc.perform(get("/tracks/8675309").accept(INVALID_MEDIA_TYPE))
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
    public void GetSingleTrack_InvalidTrackId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new TrackNotFoundException(8675309));

        mockMvc.perform(put("/tracks/8675309").accept(PROJECT_CARS))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }
}
