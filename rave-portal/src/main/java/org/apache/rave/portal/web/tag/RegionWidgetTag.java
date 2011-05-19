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

import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.web.renderer.RenderService;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * JSP tag that renders a given Widget
 */
public class RegionWidgetTag extends TagSupport {

    private RenderService renderService;
    private RegionWidget regionWidget;

    public RegionWidget getRegionWidget() {
        return regionWidget;
    }

    public void setRegionWidget(RegionWidget regionWidget) {
        this.regionWidget = regionWidget;
    }

    @Override
    public int doStartTag() throws JspException {
        if (regionWidget != null && getRenderService().getSupportedWidgetTypes().contains(regionWidget.getWidget().getType())) {
            writeString(getRenderService().render(regionWidget));
        } else {
            throw new JspException("Unsupported regionWidget type or regionWidget not set: " + regionWidget);
        }
        //Certain JSP implementations use tag pools.  Setting the regionWidget to null ensures that there is no chance a given tag
        //will accidentally re-use a region widget if the attribute in the JSP is empty
        regionWidget = null;
        return EVAL_BODY_INCLUDE;
    }

    private void writeString(String output) throws JspException {
        try {
            this.pageContext.getOut().print(output);
        } catch (IOException e) {
            throw new JspException("Failed to render regionWidget", e);
        }
    }

    private RenderService getRenderService() throws JspException {
        if(renderService == null) {
            renderService = getRenderServiceFromContext();
        }
        return renderService;
    }

    private RenderService getRenderServiceFromContext() throws JspException {
        ServletContext currentServletContext = pageContext.getServletContext();
        ApplicationContext springContext = WebApplicationContextUtils.getRequiredWebApplicationContext(currentServletContext);
        return springContext.getBean(RenderService.class);
    }
}
