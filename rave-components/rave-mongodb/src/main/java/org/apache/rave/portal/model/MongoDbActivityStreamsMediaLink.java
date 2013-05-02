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

package org.apache.rave.portal.model;


import com.google.common.collect.Maps;
import org.apache.rave.portal.model.impl.ActivityStreamsMediaLinkImpl;

import java.util.HashMap;
import java.util.Map;

public class MongoDbActivityStreamsMediaLink extends ActivityStreamsMediaLinkImpl {

    private HashMap openSocial;

    @Override
    public HashMap getOpenSocial() {
        return openSocial;
    }

    @Override
    public void setOpenSocial(Map openSocial) {
        this.openSocial = convertToHashMap(openSocial);
    }


    private HashMap convertToHashMap(Map extensions) {
        HashMap converted = null;
        if(extensions != null) {
            converted= Maps.newHashMap();
            converted.putAll(extensions);
        }
        return converted;
    }
}
