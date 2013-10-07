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

package org.apache.rave.portal.util.data;

import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.apache.rave.util.JsonUtils.parse;

public class DataImporter<T> {

    protected DataImporter.Executor<T> dataExecutor;
    protected List<Resource> scriptLocations;
    protected Class<T> modelClass;

    public List<Resource> getScriptLocations() {
        return scriptLocations;
    }

    public void setScriptLocations(List<Resource> scriptLocations) {
        this.scriptLocations = scriptLocations;
    }

    @PostConstruct
    public void importData() {
        if (scriptLocations != null && dataExecutor.needsLoading()) {
            for (Resource resource : scriptLocations) {
                T wrapper = parse(resource, modelClass);
                dataExecutor.loadData(wrapper);
            }
        }
    }

    public void setDataExecutor(Executor<T> dataExecutor) {
        this.dataExecutor = dataExecutor;
    }

    public void setModelClass(Class<T> modelClass) {
        this.modelClass = modelClass;
    }

    public static interface Executor<T> {
        boolean needsLoading();
        void loadData(T models);
    }
}
