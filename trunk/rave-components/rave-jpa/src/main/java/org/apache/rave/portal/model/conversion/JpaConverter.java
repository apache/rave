/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.rave.portal.model.conversion;

import org.apache.rave.model.ModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Converts objects into their JPA representations by delegating to wired in components
 */
@Component
public class JpaConverter {
    //Workaround for inability to access spring context without a lot of machinery
    //Will allow for a getInstance method to be called.  this is needed because the
    //Converters are all Spring beans with their own dependencies.
    private static JpaConverter instance;

    Map<Class<?>, ModelConverter> converterMap;

    @Autowired
    JpaConverter(List<ModelConverter> converters) {
        converterMap = new HashMap<Class<?>, ModelConverter>();
        for(ModelConverter converter : converters) {
            converterMap.put(converter.getSourceType(), converter);
        }
        instance = this;
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
    public <S,T> ModelConverter<S, T> getConverter(Class<S> clazz) {
        return converterMap.get(clazz);
    }
    
    protected static boolean isInstanceSet() {
        return instance != null;
    }

    public static JpaConverter getInstance() {
        if(instance == null) {
            throw new IllegalStateException("Conversion factory not set by the Spring context");
        }
        return instance;
    }

}
