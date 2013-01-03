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

package org.apache.rave.portal.model;


import org.apache.rave.portal.model.impl.PageTemplateImpl;
import org.apache.rave.portal.repository.PageLayoutRepository;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonMethod;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonAutoDetect(value = JsonMethod.FIELD, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MongoDbPageTemplate extends PageTemplateImpl {

    @XmlTransient @JsonIgnore
    private PageLayoutRepository pageLayoutRepository;
    private String pageLayoutCode;

    public void setPageLayoutRepository(PageLayoutRepository pageLayoutRepository) {
        this.pageLayoutRepository = pageLayoutRepository;
    }

    public String getPageLayoutCode() {
        return pageLayoutCode;
    }

    public void setPageLayoutCode(String pageLayoutCode) {
        this.pageLayoutCode = pageLayoutCode;

    }

    public PageLayoutRepository getPageLayoutRepository() {
        return pageLayoutRepository;
    }

    @Override
    public PageLayout getPageLayout() {
        PageLayout layout = super.getPageLayout();
        if(layout == null) {
            layout = pageLayoutRepository.getByPageLayoutCode(pageLayoutCode);
            super.setPageLayout(layout);
        }
        return layout;
    }
}
