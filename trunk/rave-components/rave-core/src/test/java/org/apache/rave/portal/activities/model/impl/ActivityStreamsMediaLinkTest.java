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

package org.apache.rave.portal.activities.model.impl;

import org.apache.rave.portal.model.impl.ActivityStreamsMediaLinkImpl;

import java.util.HashMap;

public class ActivityStreamsMediaLinkTest {
    public static ActivityStreamsMediaLinkImpl testEntity() {
      ActivityStreamsMediaLinkImpl tst = new ActivityStreamsMediaLinkImpl();
      HashMap<String,String> hm = new HashMap<String,String>(); 
      hm.put("OPEN", "SOCIAL"); 
      hm.put("FOO", "BAR"); 
      hm.put("FAZ", "BAZ"); 

      tst.setDuration(2);
      tst.setHeight(4);
      tst.setUrl("http://eight");
      tst.setWidth(16);


      return tst;
    }


}
