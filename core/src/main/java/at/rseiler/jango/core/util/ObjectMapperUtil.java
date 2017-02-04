package at.rseiler.jango.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class ObjectMapperUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectMapperUtil.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> T read(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            LOGGER.error("Failed to serialize object", e);
            throw new RuntimeException(e);
        }
    }

    public static byte[] write(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to deserialize object");
            throw new RuntimeException(e);
        }
    }

    private ObjectMapperUtil() {
    }
}
