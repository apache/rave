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
package org.apache.rave.portal.service.impl;

import org.apache.rave.portal.model.Tag;
import org.apache.rave.portal.model.WidgetTag;
import org.apache.rave.portal.repository.WidgetTagRepository;
import org.apache.rave.portal.service.WidgetTagService;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class DefaultWidgetTagServiceTest {

    private WidgetTagRepository widgetTagRepository;
    private WidgetTagService widgetTagService;


    @Before
    public void setup() {
        widgetTagRepository = createMock(WidgetTagRepository.class);
        widgetTagService = new DefaultWidgetTagService(widgetTagRepository);
    }

    @Test
    public void getWidgetTag() {
        WidgetTag tag = new WidgetTag();
        tag.setEntityId(1L);
        expect(widgetTagRepository.get(1L)).andReturn(tag);
        replay(widgetTagRepository);

        assertEquals(tag, widgetTagService.getWidgetTag(1L));
        verify(widgetTagRepository);
    }

    @Test
    public void saveWidgetTag() {
        try {

            WidgetTag wtag = new WidgetTag();
            wtag.setEntityId(3L);
            Tag tag = new Tag(4L, "test");
            wtag.setTag(tag);
            expect(widgetTagRepository.save(wtag)).andReturn(wtag);
            replay(widgetTagRepository);

            widgetTagService.saveWidgetTag(wtag);
            verify(widgetTagRepository);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
