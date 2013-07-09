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
package org.apache.rave.portal.security.impl;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;

/**
 * Custom DefaultMethodSecurityExpressionHandler class which overrides the
 * filter method since some JPA implementations will return unmodifiable List
 * objects.
 * 
 * @author carlucci
 */
public class RaveMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    /**
     * Custom filter method that copies the filterTarget to a modifiable ArrayList
     * so the containing elements can be filtered out via the parent filter method.
     * 
     * @param filterTarget the Collection or array of objects to be filtered
     * @param filterExpression the filter expression
     * @param ctx the EvaluationContext
     * @return the filtered Collection or array
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object filter(Object filterTarget, Expression filterExpression, EvaluationContext ctx) {
        // certain implementations of JPA will create unmodifiable List objects, so clone the
        // object into an ArrayList before passing into super.filter                 
        if (filterTarget instanceof Collection) {
            return super.filter(new ArrayList((Collection)filterTarget), filterExpression, ctx);
        } else {
            return super.filter(filterTarget, filterExpression, ctx);
        }                    
    }    
}
