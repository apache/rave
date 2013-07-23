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

package org.apache.rave.util;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.module.mrbean.MrBeanModule;
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
        jacksonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jacksonMapper.registerModule(new MrBeanModule());
        return jacksonMapper;
    }
}
