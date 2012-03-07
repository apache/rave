/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.rave.portal.web.tag;

import org.apache.rave.service.StaticContentFetcherService;
import javax.servlet.jsp.JspException;

/**
 * JSP tag that renders a block of static content from the StaticContentFetcherService cache
 */
public class StaticContentTag extends AbstractContextAwareSingletonBeanDependentTag<StaticContentFetcherService> {
    private String contentKey;

    public StaticContentTag() {
        super(StaticContentFetcherService.class);
    }

    public String getContentKey() {
        return contentKey;
    }

    public void setContentKey(String contentKey) {
        this.contentKey = contentKey;
    }

    @Override
    public int doStartTag() throws JspException {
        if (contentKey != null) {
            writeString(getBean().getContent(contentKey));
        }
        else {
            throw new JspException("contentKey can't be null");
        }
        //Certain JSP implementations use tag pools.  Setting the contentKey to null ensures that there is no chance a given tag
        //will accidentally re-use a contentKey if the attribute in the JSP is empty
        contentKey = null;
        return EVAL_BODY_INCLUDE;
    }
}