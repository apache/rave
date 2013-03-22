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

import org.apache.rave.model.PortalPreference;
import org.apache.rave.portal.service.PortalPreferenceService;
import org.apache.rave.portal.web.util.PortalPreferenceKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.JspException;

/**
 * JSP tag that renders the value of the JavaScriptDebugMode portal preference
 */
public class JavaScriptDebugModeTag extends AbstractContextAwareSingletonBeanDependentTag<PortalPreferenceService> {
    private Logger log = LoggerFactory.getLogger(getClass());
    private final String DEBUG_MODE_OFF = "0";
    private final String DEBUG_MODE_ON = "1";

    public JavaScriptDebugModeTag() {
        super(PortalPreferenceService.class);
    }

    @Override
    public int doStartTag() throws JspException {
        writeString(getJavaScriptDebugModeValue());
        return EVAL_BODY_INCLUDE;
    }

    private String getJavaScriptDebugModeValue() {
        // default to off
        String debugMode = DEBUG_MODE_OFF;
        try {
            PortalPreference debugModePref = getBean().getPreference(PortalPreferenceKeys.JAVASCRIPT_DEBUG_MODE);
            if (debugModePref != null && DEBUG_MODE_ON.equals(debugModePref.getValue())) {
                debugMode = DEBUG_MODE_ON;
            }
        }
        catch(Exception e) {
            // if there are any errors we will revert to the default value
            log.warn("unable to determine the value of portal preference '" + PortalPreferenceKeys.JAVASCRIPT_DEBUG_MODE + "'.  Defaulting to '" + DEBUG_MODE_OFF + "'", e);
        }
        finally {
            return debugMode;
        }
    }
}