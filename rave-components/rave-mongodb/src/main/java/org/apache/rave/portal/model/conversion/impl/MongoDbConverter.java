/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.rave.portal.model.conversion.impl;

import org.apache.rave.model.ModelConverter;
import org.apache.rave.portal.model.conversion.HydratingConverterFactory;
import org.apache.rave.portal.model.conversion.HydratingModelConverter;
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
public class MongoDbConverter implements HydratingConverterFactory {

    private Map<Class<?>, HydratingModelConverter> converterMap;

    @Autowired
    public void setConverters(List<HydratingModelConverter> converters) {
        converterMap = new HashMap<Class<?>, HydratingModelConverter>();
        for(HydratingModelConverter converter : converters) {
            converterMap.put(converter.getSourceType(), converter);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S, T> T convert(S source, Class<S> clazz) {
        if(converterMap.containsKey(clazz)) {
            return (T)converterMap.get(clazz).convert(source);
        } else {
            throw new IllegalArgumentException("No ModelConverter found for type " + clazz);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S> void hydrate(S source, Class<S> clazz) {
        if(converterMap.containsKey(clazz)) {
            converterMap.get(clazz).hydrate(source);
        } else {
            throw new IllegalArgumentException("No ModelConverter found for type " + clazz);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S,T> ModelConverter<S, T> getConverter(Class<S> clazz) {
        return converterMap.get(clazz);
    }

}
