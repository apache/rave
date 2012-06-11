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

import java.util.List;

public class TagImpl implements Tag {

    private String keyword;
    private List<WidgetTag> widgets;

    public TagImpl(String keyword, List<WidgetTag> widgets) {
        this.keyword = keyword;
        this.widgets = widgets;
    }

    public TagImpl(String keyword) {
        this.keyword = keyword;
    }

    public TagImpl() {
    }

    @Override
    public String getKeyword() {
        return this.keyword;
    }

    @Override
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public List<WidgetTag> getWidgets() {
        return this.widgets;
    }

    @Override
    public void setWidgets(List<WidgetTag> widgets) {
        this.widgets = widgets;
    }
}
