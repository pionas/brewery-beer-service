package pl.excellentapp.brewery.beer.infrastructure.rest.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
abstract class AbstractMvcTest {

    protected String mapToJson(Object object) throws JsonProcessingException {
        return getObjectMapper().writeValueAsString(object);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz) throws IOException {
        return getObjectMapper().readValue(json, clazz);
    }

    protected ObjectMapper getObjectMapper() {
        final var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }
}
