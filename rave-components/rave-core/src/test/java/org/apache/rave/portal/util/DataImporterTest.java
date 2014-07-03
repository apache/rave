/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.portal.util;

import org.apache.rave.model.*;
import org.apache.rave.portal.model.impl.*;
import org.apache.rave.portal.repository.*;
import org.apache.rave.portal.util.data.DataImporter;
import org.apache.rave.portal.util.data.ModelWrapper;
import org.apache.rave.portal.util.data.ModelWrapperDataExecutor;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Arrays;

import static org.easymock.EasyMock.*;

public class DataImporterTest {

    private PageLayoutRepository pageLayoutRepository;
    private UserRepository userRepository;
    private WidgetRepository widgetRepository;
    private PageRepository pageRepository;
    private AuthorityRepository authorityRepository;
    private PortalPreferenceRepository portalPreferenceRepository;
    private CategoryRepository categoryRepository;
    private PageTemplateRepository pageTemplateRepository;
    private ActivityStreamsRepository activityStreamsRepository;
    private DataImporter<ModelWrapper> importer;

    @Before
    public void setup() {
        pageLayoutRepository = createMock(PageLayoutRepository.class);
        userRepository = createMock(UserRepository.class);
        widgetRepository = createMock(WidgetRepository.class);
        pageRepository = createMock(PageRepository.class);
        authorityRepository = createMock(AuthorityRepository.class);
        portalPreferenceRepository = createMock(PortalPreferenceRepository.class);
        categoryRepository = createMock(CategoryRepository.class);
        pageTemplateRepository = createMock(PageTemplateRepository.class);
        activityStreamsRepository = createMock(ActivityStreamsRepository.class);

        ModelWrapperDataExecutor executor = new ModelWrapperDataExecutor();
        executor.setPageLayoutRepository(pageLayoutRepository);
        executor.setUserRepository(userRepository);
        executor.setWidgetRepository(widgetRepository);
        executor.setPageRepository(pageRepository);
        executor.setAuthorityRepository(authorityRepository);
        executor.setPortalPreferenceRepository(portalPreferenceRepository);
        executor.setCategoryRepository(categoryRepository);
        executor.setPageTemplateRepository(pageTemplateRepository);
        executor.setActivityStreamsRepository(activityStreamsRepository);

        importer = new DataImporter<ModelWrapper>();
        importer.setScriptLocations(Arrays.asList((Resource) new ClassPathResource("test-data.json")));
        importer.setDataExecutor(executor);
        importer.setModelClass(ModelWrapper.class);
    }

    @Test
    public void valid() {
        expect(pageLayoutRepository.save(isA(PageLayout.class))).andReturn(new PageLayoutImpl()).times(2);
        expect(userRepository.save(isA(User.class))).andReturn(new UserImpl()).times(2);
        expect(widgetRepository.getCountAll()).andReturn(0);
        expect(widgetRepository.save(isA(Widget.class))).andReturn(new WidgetImpl()).times(2);
        expect(authorityRepository.save(isA(Authority.class))).andReturn(new AuthorityImpl()).times(2);
        expect(portalPreferenceRepository.save(isA(PortalPreference.class))).andReturn(new PortalPreferenceImpl()).times(2);
        expect(categoryRepository.save(isA(Category.class))).andReturn(new CategoryImpl()).times(2);
        expect(pageTemplateRepository.save(isA(PageTemplate.class))).andReturn(new PageTemplateImpl()).times(2);
        expect(activityStreamsRepository.save(isA(ActivityStreamsEntry.class))).andReturn(new ActivityStreamsEntryImpl()).times(2);
        replay(pageTemplateRepository, pageLayoutRepository, userRepository, widgetRepository, pageRepository, authorityRepository, portalPreferenceRepository, categoryRepository, activityStreamsRepository);

        importer.importData();

        verify(pageTemplateRepository, pageLayoutRepository, userRepository, widgetRepository, pageRepository, authorityRepository, portalPreferenceRepository, categoryRepository, activityStreamsRepository);

    }

    @Test
    public void populated() {
        expect(widgetRepository.getCountAll()).andReturn(1);
        replay(pageTemplateRepository, pageLayoutRepository, userRepository, widgetRepository, pageRepository, authorityRepository, portalPreferenceRepository, categoryRepository, activityStreamsRepository);

        importer.importData();

        verify(pageTemplateRepository, pageLayoutRepository, userRepository, widgetRepository, pageRepository, authorityRepository, portalPreferenceRepository, categoryRepository, activityStreamsRepository);

    }

}
