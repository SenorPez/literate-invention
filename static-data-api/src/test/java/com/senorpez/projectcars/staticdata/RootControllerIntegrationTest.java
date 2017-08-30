//package com.senorpez.projectcars.staticdata;
//
//import org.junit.Before;
//import org.junit.ClassRule;
//import org.junit.Rule;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.rules.SpringClassRule;
//import org.springframework.test.context.junit4.rules.SpringMethodRule;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//
//import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
//import static org.hamcrest.Matchers.hasEntry;
//import static org.hamcrest.Matchers.is;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
//
//@SpringBootTest
//public class RootControllerIntegrationTest {
//    private static final MediaType MEDIA_TYPE = new MediaType("application", "vnd.senorpez.pcars2.v1+json", StandardCharsets.UTF_8);
//    private static final ClassLoader CLASS_LOADER = RootControllerIntegrationTest.class.getClassLoader();
//    private static InputStream OBJECT_SCHEMA;
//    private static InputStream ERROR_SCHEMA;
//
//    private MockMvc mockMvc;
//
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//
//    @Before
//    public void setUp() throws Exception {
//        OBJECT_SCHEMA = CLASS_LOADER.getResourceAsStream("root.schema.json");
//        ERROR_SCHEMA = CLASS_LOADER.getResourceAsStream("error.schema.json");
//        mockMvc = webAppContextSetup(webApplicationContext).build();
//    }
//
//    @ClassRule
//    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();
//    @Rule
//    public final SpringMethodRule springMethodRule = new SpringMethodRule();
//
//    @Test
//    public void TestRoot() throws Exception {
//        mockMvc.perform(get("/").accept(MEDIA_TYPE))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MEDIA_TYPE))
//                .andExpect(content().string(matchesJsonSchema(OBJECT_SCHEMA)))
//                .andExpect(jsonPath("$._links.self", hasEntry("href", "http://localhost/")));
//    }
//
//    @Test
//    public void TestRoot_InvalidMethod() throws Exception {
//        mockMvc.perform(put("/").contentType(MEDIA_TYPE).content("Data"))
//                .andExpect(status().isMethodNotAllowed())
//                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
//                .andExpect(jsonPath("$.code", is(405)))
//                .andExpect(jsonPath("$.message", is("Method Not Allowed")));
//    }
//
//    @Test
//    public void TestRoot_InvalidAcceptHeader() throws Exception {
//        final MediaType mediaType = new MediaType("application", "vnd.senorpez.badmediatype+json");
//
//        mockMvc.perform(get("/").accept(mediaType))
//                .andExpect(status().isNotAcceptable())
//                .andExpect(content().string(matchesJsonSchema(ERROR_SCHEMA)))
//                .andExpect(jsonPath("$.code", is(406)))
//                .andExpect(jsonPath("$.message", is("Not Acceptable")));
//    }
//}
