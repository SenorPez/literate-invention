package com.senorpez.projectcars.racereport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senorpez.projectcars.racedata.Race;
import com.senorpez.projectcars.racedata.Telemetry;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.hal.CurieProvider;
import org.springframework.hateoas.hal.DefaultCurieProvider;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class Application {
    private static final String HAL_OBJECT_MAPPER_BEAN_NAME = "_halObjectMapper";

    static final List<Race> RACES = new ArrayList<>();

    @Autowired
    private BeanFactory beanFactory;

    public static void main(final String[] args) {
        final Telemetry telemetry = new Telemetry(Paths.get(args[0]));
        while (telemetry.hasNext()) {
            RACES.add(new Race(telemetry));
        }
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public HttpMessageConverters customConverters() {
        return new HttpMessageConverters(new Application.HalMappingJackson2HttpMessageConverter());
    }

    private class HalMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
        private HalMappingJackson2HttpMessageConverter() {
            setSupportedMediaTypes(Collections.singletonList(
                    new MediaType("application", "doesntmatter") {
                        @Override
                        public boolean isCompatibleWith(final MediaType other) {
                            if (other == null) {
                                return false;
                            } else if (other.getSubtype().startsWith("vnd.senorpez") && other.getSubtype().endsWith("+json")) {
                                return true;
                            }
                            return super.isCompatibleWith(other);
                        }
                    }
            ));

            final ObjectMapper halObjectMapper = beanFactory.getBean(HAL_OBJECT_MAPPER_BEAN_NAME, ObjectMapper.class);
            setObjectMapper(halObjectMapper);
        }
    }

    @Bean
    public CurieProvider curieProvider() {
        return new DefaultCurieProvider("pcars", new UriTemplate("/{rel}"));
    }
}
