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
import org.apache.rave.portal.web.renderer.ScriptManager;
import javax.servlet.jsp.JspException;
import java.util.List;

/**
 *  Renders a list of registered script blocks according to key
 */
public class ScriptTag extends AbstractContextAwareSingletonBeanDependentTag<ScriptManager> {

    private ScriptLocation location;

    public ScriptTag() {
        super(ScriptManager.class);
    }

    public ScriptLocation getLocation() {
        return location;
    }

    public void setLocation(ScriptLocation location) {
        this.location = location;
    }

    @Override
    public int doStartTag() throws JspException {
        int returnValue = SKIP_BODY;
        ScriptManager scriptManager = getBean();
        validateParams(scriptManager);
        List<String> scripts = scriptManager.getScriptBlocks(location, getContext());
        if(scripts != null) {
            renderScripts(scripts);
            returnValue = EVAL_BODY_INCLUDE;
        }
        location = null;
        return returnValue;
    }

    private void validateParams(ScriptManager scriptManager) throws JspException {
        if(location == null || scriptManager == null) {
            throw new JspException("Invalid configuration.  Ensure that you have correctly configured the application and provided a location to the tag");
        }
    }

    private void renderScripts(List<String> scripts) throws JspException {
        for(String script : scripts) {
            writeString(script);
        }
    }
}
