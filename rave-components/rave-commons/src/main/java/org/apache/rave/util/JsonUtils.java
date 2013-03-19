package org.apache.rave.util;


import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.mrbean.MrBeanModule;
import org.springframework.core.io.Resource;

import java.io.IOException;

public class JsonUtils {
    private JsonUtils(){}

    public static <T> String stringify(T object) {
        try {
            return getMapper().writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parse(String serialized, Class<T> klass) {
        try {
            return serialized == null ? null : getMapper().readValue(serialized, klass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static <T> T parse(Resource resource, Class<T> klass) {
        try {
            return getMapper().readValue(resource.getFile(), klass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ObjectMapper getMapper() {
        ObjectMapper jacksonMapper = new ObjectMapper();
        AnnotationIntrospector primary = new JacksonAnnotationIntrospector();
        jacksonMapper.setAnnotationIntrospector(primary);
        jacksonMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jacksonMapper.registerModule(new MrBeanModule());
        return jacksonMapper;
    }
}
