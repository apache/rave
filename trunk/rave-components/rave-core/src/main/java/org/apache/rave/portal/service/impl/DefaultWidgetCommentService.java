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

import org.apache.rave.portal.model.WidgetComment;
import org.apache.rave.portal.repository.WidgetCommentRepository;
import org.apache.rave.portal.service.WidgetCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class DefaultWidgetCommentService implements WidgetCommentService {
    
    private final WidgetCommentRepository widgetCommentRepository;
    
    @Autowired
    public DefaultWidgetCommentService(WidgetCommentRepository widgetCommentRepository) {
        this.widgetCommentRepository = widgetCommentRepository;
    }
    
    @Override
    public WidgetComment getWidgetComment(Long id) {
        return widgetCommentRepository.get(id);
    }

    @Override
    @Transactional
    public void saveWidgetComment(WidgetComment widgetComment) {
        widgetCommentRepository.save(widgetComment);
    }

    @Override
    @Transactional
    public void removeWidgetComment(Long id) {
        widgetCommentRepository.delete(getWidgetComment(id));
    }

    @Override
    @Transactional
    public int deleteAll(Long userId) {
        return widgetCommentRepository.deleteAll(userId);
    }
}
