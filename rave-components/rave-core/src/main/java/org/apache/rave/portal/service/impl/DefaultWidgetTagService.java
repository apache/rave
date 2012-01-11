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

import org.apache.rave.portal.model.WidgetTag;
import org.apache.rave.portal.repository.WidgetTagRepository;
import org.apache.rave.portal.service.WidgetTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class DefaultWidgetTagService implements WidgetTagService {

    private final WidgetTagRepository widgetTagRepository;

    @Autowired

    public DefaultWidgetTagService(WidgetTagRepository widgetTagRepository) {
        this.widgetTagRepository = widgetTagRepository;
    }

    @Override
    public WidgetTag getWidgetTag(Long id) {
        return widgetTagRepository.get(id);
    }


    @Override
    @Transactional
    public void saveWidgetTag(WidgetTag widgetTag) {
        widgetTagRepository.save(widgetTag);
    }

    @Override
    public WidgetTag getWidgetTagByWidgetIdAndKeyword(Long widgetId, String keyword) {
        return widgetTagRepository.getByWidgetIdAndTag(widgetId, keyword);
    }


}
