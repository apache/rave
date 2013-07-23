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

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.rave.model.RegionWidget;
import org.apache.rave.model.Widget;
import org.apache.rave.portal.web.renderer.RenderScope;
import org.apache.rave.portal.web.renderer.RenderService;
import org.apache.rave.portal.web.renderer.ScriptLocation;
import org.apache.rave.portal.web.renderer.ScriptManager;
import org.apache.rave.portal.web.renderer.model.RegionWidgetWrapper;

import javax.servlet.jsp.JspException;

/**
 * JSP tag that renders a RegionWidget
 */
public class RegionWidgetTag extends AbstractContextAwareSingletonBeanDependentTag<RenderService> {

    private RegionWidget regionWidget;
    private Widget widget;
    
    private static final String REGISTER_DISABLED_WIDGET_KEY = "disabledRegisterWidget";

    // Script block for disabled gadget
    private static final String DISABLED_SCRIPT_BLOCK =
            "<script>require(['rave'], function(rave){rave.registerWidget('%1$s', {type: 'DISABLED'," +
            " regionWidgetId: '%2$s'," +
            " disabledMessage: '%3$s'," +
            " collapsed: %4$s," +
            " widgetId: '%5$s'})});</script>";

    public RegionWidgetTag() {
        super(RenderService.class);
    }

    public RegionWidget getRegionWidget() {
        return regionWidget;
    }

    public void setRegionWidget(RegionWidget regionWidget) {
        this.regionWidget = regionWidget;
    }

    public Widget getWidget() {
        return widget;
    }

    public void setWidget(Widget widget) {
        this.widget = widget;
    }

    /**
     * Delegates rendering of the RegionWidget to the RenderService
     *
     * @return EVAL_BODY_INCLUDE if no exception is thrown
     * @throws JspException if the regionWidget is not set or is not supported by the renderService.
     */
    @Override
    public int doStartTag() throws JspException {

        if (regionWidget == null) {
            throw new JspException("RegionWidget not set: " + regionWidget);
        }

        if (widget != null && getBean().getSupportedWidgetTypes().contains(widget.getType()) ) {
            if ( widget.isDisableRendering() ) {
                ScriptManager scriptManager = getBeanFromContext(ScriptManager.class);
                String widgetScript = String.format(DISABLED_SCRIPT_BLOCK, regionWidget.getRegion().getId(),
                        regionWidget.getId(),
                        StringEscapeUtils.escapeEcmaScript(widget.getDisableRenderingMessage()),
                        regionWidget.isCollapsed(),
                        widget.getId());
                String key = REGISTER_DISABLED_WIDGET_KEY + "-" + widget.getId();
                scriptManager.registerScriptBlock(key, widgetScript, ScriptLocation.AFTER_RAVE, RenderScope.CURRENT_REQUEST, getContext());
            } else {
                writeString(getBean().render(new RegionWidgetWrapper(widget, regionWidget), getContext()));
            }
        }
        else {
                throw new JspException("Unsupported regionWidget type: " + regionWidget);
        }
        //Certain JSP implementations use tag pools.  Setting the regionWidget to null ensures that there is no chance a given tag
        //will accidentally re-use a region widget if the attribute in the JSP is empty
        regionWidget = null;
        widget = null;
        return EVAL_BODY_INCLUDE;
    }
}
