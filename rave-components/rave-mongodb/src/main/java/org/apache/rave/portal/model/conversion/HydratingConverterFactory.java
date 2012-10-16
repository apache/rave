package org.apache.rave.portal.model.conversion;

import org.apache.rave.model.ModelConverter;

/**
 * Created with IntelliJ IDEA.
 * User: mfranklin
 * Date: 10/16/12
 * Time: 8:24 AM
 * To change this template use File | Settings | File Templates.
 */
public interface HydratingConverterFactory {
    @SuppressWarnings("unchecked")
    <S, T> T convert(S source, Class<S> clazz);

    @SuppressWarnings("unchecked")
    <S> void hydrate(S source, Class<S> clazz);

    @SuppressWarnings("unchecked")
    <S,T> ModelConverter<S, T> getConverter(Class<S> clazz);
}
