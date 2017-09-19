package com.senorpez.projectcars.staticdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.core.DefaultRelProvider;
import org.springframework.hateoas.hal.DefaultCurieProvider;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.List;

class HALMessageConverter {
    static HttpMessageConverter<Object> getConverter(final List<MediaType> mediaType) {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jackson2HalModule());

        final DefaultCurieProvider curieProvider = new DefaultCurieProvider("pcars", new UriTemplate("/docs/{rel}"));
        final DefaultRelProvider relProvider = new DefaultRelProvider();

        objectMapper.setHandlerInstantiator(new Jackson2HalModule.HalHandlerInstantiator(relProvider, curieProvider, null));

        final MappingJackson2HttpMessageConverter halConverter = new MappingJackson2HttpMessageConverter(objectMapper);
        halConverter.setObjectMapper(objectMapper);
        halConverter.setSupportedMediaTypes(mediaType);

        return halConverter;
    }
}
