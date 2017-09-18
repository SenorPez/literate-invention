//package com.senorpez.projectcars.staticdata;
//
//import org.junit.Before;
//import org.junit.ClassRule;
//import org.junit.Rule;
//import org.junit.Test;
//import org.mockito.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.SpyBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.rules.SpringClassRule;
//import org.springframework.test.context.junit4.rules.SpringMethodRule;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.io.InputStream;
//import java.util.Arrays;
//
//import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
//import static java.nio.charset.StandardCharsets.UTF_8;
//import static org.hamcrest.Matchers.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//public class Car2ControllerIntegrationTest {
//    private static final MediaType MEDIA_TYPE = new MediaType("application", "vnd.senorpez.pcars2.v1+json", UTF_8);
//    private static final ClassLoader CLASS_LOADER = Car2ControllerIntegrationTest.class.getClassLoader();
//    private static InputStream COLLECTION_SCHEMA;
//    private static InputStream ERROR_SCHEMA;
//
//    private MockMvc mockMvc;
//
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//
//    @SpyBean
//
//    @InjectMocks
//    private Car2Controller car2Controller;
//
//    @Before
//    public void setUp() throws Exception {
//        COLLECTION_SCHEMA = CLASS_LOADER.getResourceAsStream("cars2.schema.json");
//        ERROR_SCHEMA = CLASS_LOADER.getResourceAsStream("error.schema.json");
//
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//    }
//
//    @ClassRule
//    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();
//    @Rule
//    public final SpringMethodRule springMethodRule = new SpringMethodRule();
//
//    @Test
//    public void GetAllCars_ValidAcceptHeader() throws Exception {
//        final EmbeddedCar2Model firstCar = new EmbeddedCar2Model(
//                new Car2Model(
//                        1, new Car2Builder()
//                        .setName("First Car")
//                        .build()));
//
//        final EmbeddedCar2Model secondCar = new EmbeddedCar2Model(
//                new Car2Model(
//                        2, new Car2Builder()
//                        .setName("Second Car")
//                        .build()));
//
//        when(car2ServiceMock.findAll(anyCollection())).thenReturn(Arrays.asList(firstCar, secondCar));
//
//        mockMvc.perform(get("/cars").accept(MEDIA_TYPE))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MEDIA_TYPE))
//                .andExpect(content().string(matchesJsonSchema(COLLECTION_SCHEMA)))
//
//                .andExpect(jsonPath("$._embedded.pcars:car", hasItem(hasEntry("id", 1))))
//                .andExpect(jsonPath("$._embedded.pcars:car", hasItem(hasEntry("name", "First Car"))))
//                .andExpect(jsonPath("$._embedded.pcars:car", hasItem(
//                        hasEntry(is("_links"),
//                                hasEntry(is("self"),
//                                        hasEntry("href", "http://localhost/cars/1"))))))
//
//                .andExpect(jsonPath("$._embedded.pcars:car", hasItem(hasEntry("id", 2))))
//                .andExpect(jsonPath("$._embedded.pcars:car", hasItem(hasEntry("name", "Second Car"))))
//                .andExpect(jsonPath("$._embedded.pcars:car", hasItem(
//                        hasEntry(is("_links"),
//                                hasEntry(is("self"),
//                                        hasEntry("href", "http://localhost/cars/2"))))))
//
//                .andExpect(jsonPath("$._links.self", hasEntry("href", "http://localhost/cars")))
//                .andExpect(jsonPath("$._links.index", hasEntry("href", "http://localhost/")))
//                .andExpect(jsonPath("$._links.curies", everyItem(
//                        allOf(
//                                hasEntry("href", (Object) "http://localhost/{rel}"),
//                                hasEntry("name", (Object) "pcars"),
//                                hasEntry("templated", (Object) true)))));
//    }
//
//    @Test
//    public void GetAllCars_InvalidMethod() throws Exception {
//        mockMvc.perform(put("/cars").contentType(MEDIA_TYPE).content("Data"))
//                .andExpect(status().isMethodNotAllowed())
//                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
//                .andExpect(jsonPath("$.code", is(405)))
//                .andExpect(jsonPath("$.message", is("Method Not Allowed")));
//    }
//
//    @Test
//    public void GetAllCars_InvalidAcceptHeader() throws Exception {
//        final MediaType mediaType = new MediaType("application", "vnd.senorpez.badmediatype+json");
//
//        mockMvc.perform(get("/cars").accept(mediaType))
//                .andExpect(status().isNotAcceptable())
//                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
//                .andExpect(jsonPath("$.code", is(406)))
//                .andExpect(jsonPath("$.message", is("Not Acceptable")));
//    }
//}
