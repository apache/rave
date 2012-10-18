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

package org.apache.rave.portal.util.data;

import org.apache.rave.portal.model.*;
import org.apache.rave.portal.repository.*;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.mrbean.MrBeanModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

public class DataImporter {

    //TODO GROUP REPOSITORY

    @Autowired
    private PageLayoutRepository pageLayoutRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WidgetRepository widgetRepository;

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PortalPreferenceRepository portalPreferenceRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PageTemplateRepository pageTemplateRepository;

    private List<Resource> scriptLocations;

    public List<Resource> getScriptLocations() {
        return scriptLocations;
    }

    public void setScriptLocations(List<Resource> scriptLocations) {
        this.scriptLocations = scriptLocations;
    }

    @PostConstruct
    public void importData() {
        if (scriptLocations != null) {
            for (Resource resource : scriptLocations) {
                ModelWrapper wrapper = mapObject(resource);
                if (wrapper.getPageLayouts() != null) {
                    for (PageLayout layout : wrapper.getPageLayouts()) {
                        pageLayoutRepository.save(layout);
                    }
                }
                if (wrapper.getUsers() != null) {
                    for (User user : wrapper.getUsers()) {
                        userRepository.save(user);
                    }
                }
                if (wrapper.getPageLayouts() != null) {
                    for (PageLayout layout : wrapper.getPageLayouts()) {
                        pageLayoutRepository.save(layout);
                    }
                }
                if (wrapper.getWidgets() != null) {
                    for (Widget widget : wrapper.getWidgets()) {
                        widgetRepository.save(widget);
                    }
                }
                if (wrapper.getPages() != null) {
                    for (Page page : wrapper.getPages()) {
                        pageRepository.save(page);
                    }
                }
                if (wrapper.getAuthorities() != null) {
                    for (Authority authority : wrapper.getAuthorities()) {
                        authorityRepository.save(authority);
                    }
                }
                if (wrapper.getPortalPreferences() != null) {
                    for (PortalPreference preference : wrapper.getPortalPreferences()) {
                        portalPreferenceRepository.save(preference);
                    }
                }
                if (wrapper.getCategories() != null) {
                    for (Category category : wrapper.getCategories()) {
                        categoryRepository.save(category);
                    }
                }
                for(PageTemplate template : wrapper.getPageTemplates()) {
                    pageTemplateRepository.save(template);
                }
            }
        }

    }

    private ModelWrapper mapObject(Resource resource) {
        try {
            return getMapper().readValue(resource.getFile(), ModelWrapper.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ObjectMapper getMapper() {
        ObjectMapper jacksonMapper = new ObjectMapper();
        AnnotationIntrospector primary = new JacksonAnnotationIntrospector();
        jacksonMapper.setAnnotationIntrospector(primary);
        jacksonMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jacksonMapper.registerModule(new MrBeanModule());
        return jacksonMapper;
    }


}
