package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Util {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(Object o) throws IOException {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new IOException("Error converting object " + o.getClass() + " to json", e);
        }
    }

    public static <T> T toObject(byte[] bytes, Class<T> clazz) throws IOException {
        try {
            return objectMapper.readValue(bytes, clazz);
        } catch (IOException e) {
            throw new IOException("Unable to parse object to byte array", e);
        }
    }

}
