/*
 * Copyright 2011 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.rave.portal.model;

import org.apache.rave.model.ModelConverter;
import org.apache.rave.portal.model.conversion.JpaConverter;
import org.apache.rave.portal.model.impl.AddressImpl;
import org.apache.rave.portal.model.impl.PersonImpl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 *
 */
@Ignore
public class WidgetTagTest {
    
    private JpaWidgetTag widgetTag;
    
    private static final Long VALID_ENTITY_ID = 1L;
    private static final Long VALID_USER_ID = 1L;
    private static final Long VALID_WIDGET_ID = 3L;
    private static final Date VALID_CREATED_DATE = new Date();
    
    @Before
    public void setUp() {
        ModelConverter personConverter = createMock(ModelConverter.class);
        expect(personConverter.getSourceType()).andReturn(Person.class).anyTimes();
        expect(personConverter.convert(isA(PersonImpl.class))).andReturn(new JpaPerson());
        replay(personConverter);

        ModelConverter addressConverter = createMock(ModelConverter.class);
        expect(addressConverter.getSourceType()).andReturn(Address.class).anyTimes();
        expect(addressConverter.convert(isA(AddressImpl.class))).andReturn(new JpaAddress());
        replay(addressConverter);

        ModelConverter pageLayoutConverter = createMock(ModelConverter.class);
        expect(pageLayoutConverter.getSourceType()).andReturn(Address.class).anyTimes();
        expect(pageLayoutConverter.convert(isA(PageLayout.class))).andReturn(new JpaPageLayout());
        replay(pageLayoutConverter);
        List<ModelConverter> converters = new ArrayList<ModelConverter>();
        converters.add(personConverter);
        converters.add(addressConverter);
        new JpaConverter(converters);
        widgetTag = new JpaWidgetTag();
        widgetTag.setEntityId(VALID_ENTITY_ID);
        widgetTag.setWidgetId(VALID_WIDGET_ID);
        widgetTag.setUser(new JpaUser(1L, "John.Doe"));
        widgetTag.setCreatedDate(VALID_CREATED_DATE);
        widgetTag.setTag(new JpaTag(1L, "test"));
    }
    
    @Test
    public void getters() {
        assertEquals(VALID_ENTITY_ID, widgetTag.getEntityId());
        assertEquals(VALID_WIDGET_ID, widgetTag.getWidgetId());
        assertEquals(VALID_USER_ID, widgetTag.getUser().getId());
        assertEquals(VALID_CREATED_DATE, widgetTag.getCreatedDate());
    }
    
}
