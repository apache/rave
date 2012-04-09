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
package org.apache.rave.portal.service;

import org.apache.rave.portal.model.WidgetComment;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

public interface WidgetCommentService {
    
    @PostAuthorize("hasPermission(returnObject, 'read')")
    WidgetComment getWidgetComment(Long id);
    
    @PreAuthorize("hasPermission(#widgetComment, 'create_or_update')")
    void saveWidgetComment(WidgetComment widgetComment);
    
    @PreAuthorize("hasPermission(#id, 'org.apache.rave.portal.model.WidgetComment', 'delete')") 
    void removeWidgetComment(Long id);

    /**
     * Deletes all Widget Comments for a userId
     *
     * @param userId
     * @return number of comments deleted
     */
    @PreAuthorize("hasPermission(new org.apache.rave.portal.security.impl.RaveSecurityContext(#userId, 'org.apache.rave.portal.model.User'), 'org.apache.rave.portal.model.WidgetComment', 'delete')")
    int deleteAll(Long userId);
}
