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

import org.apache.rave.portal.model.impl.ActivityStreamsObjectImpl;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Date;

import static org.junit.Assert.*;

public class ActivityStreamsObjectTest {
    public static ActivityStreamsObjectImpl testEntity() {
      ActivityStreamsObjectImpl tst = new ActivityStreamsObjectImpl();
      HashMap<String,String> hm = new HashMap<String,String>(); 
      hm.put("TEST", "TEST"); 
      hm.put("FOO", "BAR"); 
      List<String> ls=new ArrayList<String>();
      ls.add("faz");
      ls.add("baz");
      Date d = new Date(10000);

      ActivityStreamsObjectImpl author = new ActivityStreamsObjectImpl();
      author.setDisplayName("apache");
      author.setContent("foobar");

      tst.setUrl("http://apache.org");
      tst.setObjectType("COMMENT");
      tst.setOpenSocial(null);
      tst.setExtensions(hm);
      tst.setId("ID_TST");
      tst.setAuthor(author);
      tst.setDisplayName("name");
      tst.setDownstreamDuplicates(ls);
      tst.setInvited("invited set");
      tst.setStartTime(d);
     
      return tst;
    }


    @Test
    public void convertAllTest() {  
      final int numToTest = 23;
      List<ActivityStreamsObjectImpl> ll=new LinkedList<ActivityStreamsObjectImpl>();
      ActivityStreamsObjectImpl tst = testEntity();
      for(int i = 0; i < numToTest; i ++) {
        ll.add(tst);
        tst = testEntity();
      }
      List tstList = tst.convertAll(ll);
      assertEquals(numToTest, tstList.size());
    }
}
