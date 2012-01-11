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
package org.apache.rave.portal.repository;

import org.apache.rave.persistence.Repository;
import org.apache.rave.portal.model.WidgetTag;

/**
 *
  */
public interface WidgetTagRepository extends Repository<WidgetTag> {

    /**
         * Tries to find a {@link WidgetTag} by the id's of a Widget and Tag keyword
         *
         * @param widgetId unique identifier of a Widget
         * @param keyword   tag's keyword
         * @return {@link WidgetTag} if it exists, otherwise {@literal null}
         */
        WidgetTag getByWidgetIdAndTag(Long widgetId, String keyword);
}
