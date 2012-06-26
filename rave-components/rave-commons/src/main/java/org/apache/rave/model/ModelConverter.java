package org.apache.rave.model;

import org.springframework.core.convert.converter.Converter;

/**
 * Converts a source type to a target
 */
public interface ModelConverter<S,T> extends Converter<S,T> {
    Class<S> getSourceType();
}
