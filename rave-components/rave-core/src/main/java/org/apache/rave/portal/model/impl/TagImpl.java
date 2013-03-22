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
package org.apache.rave.portal.model.impl;

import org.apache.rave.model.Tag;
import org.apache.rave.model.WidgetTag;

import java.util.List;

public class TagImpl implements Tag {

    private String id;
    private String keyword;

    public TagImpl(String id, String keyword) {
        this.id = id;
        this.keyword = keyword;
    }

    public TagImpl(String keyword) {
        this.keyword = keyword;
    }

    public TagImpl() {
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getKeyword() {
        return this.keyword;
    }
}
