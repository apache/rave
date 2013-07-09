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

import org.apache.rave.portal.model.impl.ActivityStreamsEntryImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ActivityStreamsEntryTest {
    public static ActivityStreamsEntryImpl testEntity() {
      ActivityStreamsEntryImpl tst = new ActivityStreamsEntryImpl();
      HashMap<String,String> hm = new HashMap<String,String>(); 
      hm.put("TEST", "TEST"); 
      hm.put("FOO", "BAR"); 
      List<String> ls=new ArrayList<String>();
      ls.add("faz");
      ls.add("baz");
      Date d = new Date(10000);

      tst.setUrl("http://apache.org");
      tst.setObjectType("COMMENT");
      tst.setOpenSocial(null);
      tst.setExtensions(hm);
      tst.setId("ID_TST");
      tst.setInReplyTo("REPLY_TO");
      tst.setStartTime(d);
      tst.setTo("TO_PERSON");
      tst.setSource("SOURCE");
     
      return tst;
    }


}
