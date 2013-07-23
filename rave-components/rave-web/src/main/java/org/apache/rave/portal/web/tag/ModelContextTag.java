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

import org.apache.rave.repository.Repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * Adds the model of the specified type ot the page context
 */
public class ModelContextTag<T, E extends Repository<T>> extends AbstractContextAwareSingletonBeanDependentTag<E> {

    protected ModelContextTag(Class<E> clazz) {
        super(clazz);
        this.init();
    }

    protected String var;
    protected String id;
    protected int scope;
    protected boolean scopeSpecified;

    @Override
    public int doStartTag() throws JspException {
        return super.doStartTag();
    }

    @Override
    public int doEndTag() throws JspException {
        this.updateContext(this.getBean().get(id));
        this.init();
        return EVAL_PAGE;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getScope() {
        return scope;
    }

    public void setScope(int scope) {
        this.scope = scope;
        scopeSpecified = true;
    }

    protected void init() {
        this.var = null;
        this.id = null;
        this.scope = PageContext.PAGE_SCOPE;
    }

    protected void updateContext(T result) throws JspException {
        if (var != null) {
            /*
             * Store the result, letting an IllegalArgumentException
             * propagate back if the scope is invalid (e.g., if an attempt
             * is made to store something in the session without any
             * HttpSession existing).
             */
            if (result != null) {
                pageContext.setAttribute(var, result, scope);
            } else {
                if (scopeSpecified) {
                    pageContext.removeAttribute(var, scope);
                } else {
                    pageContext.removeAttribute(var);
                }
            }
        } else {
            handleNoVar(result);
        }
    }

    protected void handleNoVar(T result) throws JspException {
        writeString(result.toString());
    }
}
