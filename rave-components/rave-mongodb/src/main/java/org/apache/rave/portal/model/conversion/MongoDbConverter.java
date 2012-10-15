package org.apache.rave.portal.model.conversion;

import org.apache.rave.model.ModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Converts reference properties to hydrated objects
 * <p/>
 * TODO: REMOVE REPOSITORY INJECTION WHEN MODEL-SPLIT BRANCH IS MERGED
 */
@Component
public class MongoDbConverter {

    Map<Class<?>, HydratingModelConverter> converterMap;

    @Autowired
    MongoDbConverter(List<HydratingModelConverter> converters) {
        converterMap = new HashMap<Class<?>, HydratingModelConverter>();
        for(HydratingModelConverter converter : converters) {
            converterMap.put(converter.getSourceType(), converter);
        }
    }

    @SuppressWarnings("unchecked")
    public <S, T> T convert(S source, Class<S> clazz) {
        if(converterMap.containsKey(clazz)) {
            return (T)converterMap.get(clazz).convert(source);
        } else {
            throw new IllegalArgumentException("No ModelConverter found for type " + clazz);
        }
    }

    @SuppressWarnings("unchecked")
    public <S> void hydrate(S source, Class<S> clazz) {
        if(converterMap.containsKey(clazz)) {
            converterMap.get(clazz).hydrate(source);
        } else {
            throw new IllegalArgumentException("No ModelConverter found for type " + clazz);
        }
    }

    @SuppressWarnings("unchecked")
    public <S,T> ModelConverter<S, T> getConverter(Class<S> clazz) {
        return converterMap.get(clazz);
    }

}
