package com.senorpez.projectcars.staticdata;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;

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

public class Track2ControllerTest {
    private MockMvc mockMvc;
    private static final MediaType INVALID_MEDIA_TYPE = new MediaType("application", "invalid", UTF_8);
    private static final ClassLoader CLASS_LOADER = Track2ControllerTest.class.getClassLoader();
    private static InputStream TRACK_SCHEMA;
    private static InputStream TRACK_COLLECTION_SCHEMA;
    private static InputStream ERROR_SCHEMA;

    private static final Track2 FIRST_TRACK = new TrackBuilder()
            .setId(766599953)
            .setName("Glencairn East")
            .setGridSize(16)
            .build2();

    private static final Track2 SECOND_TRACK = new TrackBuilder()
            .setId(-1408779593)
            .setName("Glencairn West")
            .setGridSize(16)
            .build2();

    @InjectMocks
    TrackController trackController;

    @Mock
    private APIService apiService;

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Before
    public void setUp() throws Exception {
        TRACK_SCHEMA = CLASS_LOADER.getResourceAsStream("track2.schema.json");
        TRACK_COLLECTION_SCHEMA = CLASS_LOADER.getResourceAsStream("tracks2.schema.json");
        ERROR_SCHEMA = CLASS_LOADER.getResourceAsStream("error.schema.json");
        MockitoAnnotations.initMocks(this);
        
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new Track2Controller(apiService, Arrays.asList(FIRST_TRACK, SECOND_TRACK)))
                .setMessageConverters(HALMessageConverter.getConverter(Collections.singletonList(ALL)))
                .setControllerAdvice(new APIExceptionHandler())
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Test
    public void GetAllTracks_ValidAcceptHeader() throws Exception {
        when(apiService.findAll(any())).thenReturn(Arrays.asList(FIRST_TRACK, SECOND_TRACK));

        mockMvc.perform(get("/tracks").accept(PROJECT_CARS_2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(PROJECT_CARS_2))
                .andExpect(content().string(matchesJsonSchema(TRACK_COLLECTION_SCHEMA)))
                .andExpect(jsonPath("$._embedded.pcars:track", hasItem(
                        allOf(
                                hasEntry("id", (Object) FIRST_TRACK.getId()),
                                hasEntry("name", (Object) FIRST_TRACK.getName()),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasEntry("href", String.format("http://localhost:8080/tracks/%d", FIRST_TRACK.getId()))))))))
                .andExpect(jsonPath("$._embedded.pcars:track", hasItem(
                        allOf(
                                hasEntry("id", (Object) SECOND_TRACK.getId()),
                                hasEntry("name", (Object) SECOND_TRACK.getName()),
                                hasEntry(equalTo("_links"),
                                        hasEntry(equalTo("self"),
                                                hasEntry("href", String.format("http://localhost:8080/tracks/%d", SECOND_TRACK.getId()))))))))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost:8080/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", "http://localhost:8080/tracks")))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost:8080/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))
                .andDo(document("tracks2",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Accept")
                                        .description("Accept header")
                                        .attributes(key("acceptvalue").value(SupportedMediaTypes.PROJECT_CARS_2_VALUE))),
                        responseFields(
                                fieldWithPath("_embedded.pcars:track").description("Event resources"),
                                fieldWithPath("_embedded.pcars:track[].id").description("ID number"),
                                fieldWithPath("_embedded.pcars:track[].name").description("Name"),
                                fieldWithPath("_embedded.pcars:track[].gridSize").description("Grid Size"),
                                subsectionWithPath("_links").ignored(),
                                subsectionWithPath("_embedded.pcars:track[]._links").ignored()),
                        commonLinks));


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
                        "or \"vnd.senorpez.pcars2.v0+json\" for Project CARS 2")));

        verifyZeroInteractions(apiService);
    }

    @Test
    public void GetAllTracks_InvalidMethod() throws Exception {
        when(apiService.findAll(any())).thenReturn(Arrays.asList(FIRST_TRACK, SECOND_TRACK));

        mockMvc.perform(put("/tracks").accept(PROJECT_CARS_2))
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

        mockMvc.perform(get(String.format("/tracks/%d", FIRST_TRACK.getId())).accept(PROJECT_CARS_2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(PROJECT_CARS_2))
                .andExpect(content().string(matchesJsonSchema(TRACK_SCHEMA)))
                .andExpect(jsonPath("$.id", is(FIRST_TRACK.getId())))
                .andExpect(jsonPath("$.name", is(FIRST_TRACK.getName())))
                .andExpect(jsonPath("$.gridSize", is(FIRST_TRACK.getGridSize())))
                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost:8080/")))
                .andExpect(jsonPath("$._links.self", hasEntry("href", String.format("http://localhost:8080/tracks/%d", FIRST_TRACK.getId()))))
                .andExpect(jsonPath("$._links.curies", everyItem(
                        allOf(
                                hasEntry("href", (Object) "http://localhost:8080/docs/{rel}"),
                                hasEntry("name", (Object) "pcars"),
                                hasEntry("templated", (Object) true)))))
                .andExpect(jsonPath("$._links.pcars:tracks", hasEntry("href", "http://localhost:8080/tracks")))
                .andDo(document("track2",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Accept")
                                        .description("Accept header")
                                        .attributes(key("acceptvalue").value(SupportedMediaTypes.PROJECT_CARS_2_VALUE))),
                        responseFields(
                                fieldWithPath("id").description("ID number"),
                                fieldWithPath("name").description("Name"),
                                fieldWithPath("gridSize").description("Number of grid spots"),
                                subsectionWithPath("_links").ignored()),
                        commonLinks.and(
                                linkWithRel("pcars:tracks").description("List of track resources"))));

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
                        "or \"vnd.senorpez.pcars2.v0+json\" for Project CARS 2")));

        verifyZeroInteractions(apiService);
    }
    
    @Test
    public void GetSingleTrack_ValidTrackId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenReturn(FIRST_TRACK);

        mockMvc.perform(put(String.format("/tracks/%d", FIRST_TRACK.getId())).accept(PROJECT_CARS_2))
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

        mockMvc.perform(get("/tracks/8675309").accept(PROJECT_CARS_2))
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
                        "or \"vnd.senorpez.pcars2.v0+json\" for Project CARS 2")));

        verifyZeroInteractions(apiService);
    }
    
    @Test
    public void GetSingleTrack_InvalidTrackId_InvalidMethod() throws Exception {
        when(apiService.findOne(any(), any(), any())).thenThrow(new TrackNotFoundException(8675309));

        mockMvc.perform(put("/tracks/8675309").accept(PROJECT_CARS_2))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
                .andExpect(jsonPath("$.code", is(METHOD_NOT_ALLOWED.value())))
                .andExpect(jsonPath("$.message", is(METHOD_NOT_ALLOWED.getReasonPhrase())))
                .andExpect(jsonPath("$.detail", is("Only GET methods allowed.")));

        verifyZeroInteractions(apiService);
    }
}
