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

import org.apache.rave.portal.web.renderer.ScriptLocation;
import org.apache.rave.portal.web.util.ModelKeys;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class RegisterInitializationScriptTag extends BodyTagSupport {
    private ScriptLocation location;

    public void setLocation(ScriptLocation location) {
        this.location = location;
    }

    @Override
    public int doAfterBody() {
        addScriptToRequestVariable(getBodyContent().getString());
        return SKIP_BODY;
    }
    
    private void addScriptToRequestVariable(String script) {
        String requestAttributeName = null;
        if (location.equals(ScriptLocation.BEFORE_RAVE)) {
            requestAttributeName = ModelKeys.BEFORE_RAVE_INIT_SCRIPT;
        } else if (location.equals(ScriptLocation.AFTER_RAVE)) {
            requestAttributeName = ModelKeys.AFTER_RAVE_INIT_SCRIPT;
        } else {
            throw new IllegalArgumentException("invalid ScriptLocation " + location);
        }
        updateRequestAttribute(requestAttributeName, script);
    }
    
    private void updateRequestAttribute(String requestAttributeName, String script) {
        ServletRequest request = pageContext.getRequest();        
        StringBuilder attr = (StringBuilder)request.getAttribute(requestAttributeName);
        if (attr == null) {
            attr = new StringBuilder(script);
        } else {
            attr.append(script);
        }
        request.setAttribute(requestAttributeName, attr);
    }
}
