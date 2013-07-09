/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.opensocial.util;



import org.apache.rave.model.ActivityStreamsObject;
import org.apache.rave.model.ActivityStreamsMediaLink;
import org.apache.rave.portal.model.impl.ActivityStreamsEntryImpl;
import org.apache.rave.portal.model.impl.ActivityStreamsMediaLinkImpl;
import org.apache.rave.portal.model.impl.ActivityStreamsObjectImpl;
import org.apache.rave.util.ActivityConversionUtil;
import org.apache.shindig.social.core.model.ActivityEntryImpl;
import org.apache.shindig.social.core.model.ActivityObjectImpl;
import org.apache.shindig.social.opensocial.model.ActivityEntry;
import org.apache.shindig.social.opensocial.model.ActivityObject;
import org.apache.shindig.social.opensocial.model.MediaLink;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ActivityConversionUtilTest {

    private ActivityStreamsEntryImpl activityStreamsEntry;
    private List<ActivityObject> activityObjects;
    private List<ActivityStreamsObjectImpl> activityStreamsObjects;
    private ActivityEntryImpl activityEntry;
    private ActivityObject activityObject;
    private ActivityStreamsObjectImpl activityStreamsObject;
    private ActivityStreamsMediaLinkImpl activityStreamsMediaLink;

    private ActivityStreamsObjectImpl authorObject;
    private  ActivityConversionUtil conversionUtilTest ;

    private static final String ID_1 = "1234";
    private static final String GROUP_ID = "self";
    private static final String ACTIVITY_TITLE = "Activity Title";
    private static final String ACTIVITY_ID = "Activity ID";

    @Before
    public void setup() {
        conversionUtilTest=new ActivityConversionUtil();

        activityStreamsEntry = new ActivityStreamsEntryImpl();
        activityStreamsObject = new ActivityStreamsObjectImpl();
        authorObject = new ActivityStreamsObjectImpl();
        authorObject.setDisplayName("Test Author");
        activityStreamsObject.setAuthor(authorObject);
        activityStreamsObject.setDisplayName("Test Streams Object");
        activityStreamsObject.setPublished(new Date());
        activityStreamsObject.setId("Test ID");
        activityStreamsEntry.setUserId(ID_1);
        activityStreamsEntry.setActor(activityStreamsObject);
        activityStreamsEntry.setContent("Activity Content");
        activityStreamsEntry.setGenerator(new ActivityStreamsObjectImpl());
        activityStreamsEntry.setIcon(new ActivityStreamsMediaLinkImpl());
        activityStreamsEntry.setId(ACTIVITY_ID);
        activityStreamsEntry.setObject(activityStreamsObject);
        activityStreamsEntry.setPublished(new Date());
        activityStreamsEntry.setProvider(new ActivityStreamsObjectImpl());
        activityStreamsEntry.setTarget(new ActivityStreamsObjectImpl());
        activityStreamsEntry.setTitle(ACTIVITY_TITLE);
        activityStreamsEntry.setUpdated(new Date());
        activityStreamsEntry.setUrl("Activity URL");
        activityStreamsEntry.setVerb("Activity Verb");
        activityStreamsEntry.setOpenSocial(new ActivityObjectImpl());
        activityObjects = new ArrayList<ActivityObject>();
        activityObjects.add(new ActivityObjectImpl());

        activityStreamsObjects = new ArrayList<ActivityStreamsObjectImpl>();
        activityStreamsObjects.add(new ActivityStreamsObjectImpl());

        activityEntry = new ActivityEntryImpl();
        activityEntry.setId("entry id");
        activityEntry.setTitle("activity title");
        activityEntry.setVerb("activity verb");

        activityObject = new ActivityObjectImpl();
        activityObject.setId("object id");
        activityObject.setSummary("summary");
        activityObject.setUpdated(new Date().toString());

        activityStreamsMediaLink = new ActivityStreamsMediaLinkImpl();
        activityStreamsMediaLink.setHeight(10);
        activityStreamsMediaLink.setWidth(10);
        activityStreamsMediaLink.setDuration(10);
        activityStreamsMediaLink.setUrl("test");




    }

    @Test
    public void testConvertActivityEntry()  {
        ActivityStreamsEntryImpl activityStreamsEntry1 = conversionUtilTest.convert((ActivityEntry)activityEntry);

        assertThat(activityStreamsEntry1.getId(), is(equalTo(activityEntry.getId())));

        ActivityEntry reconverted = conversionUtilTest.convert(activityStreamsEntry1);

        assertThat(reconverted.getId(), is(equalTo(activityStreamsEntry1.getId())));
    }

    @Test
    public void testConvertAll()  {
       List<ActivityObject> convertedObjects = conversionUtilTest.convert(activityStreamsObjects);

        assertThat(convertedObjects.size(), is(equalTo(activityStreamsObjects.size())));


    }

    @Test
    public void testConvertAllActivityObject()  {
        List<ActivityStreamsObject> convertedObjects = conversionUtilTest.convertObjectEntries(activityObjects);

        assertThat(convertedObjects.size(), is(equalTo(activityObjects.size())));


    }



    @Test
    public void testConvertActivityStreamsObjectImpl()  {
        ActivityObject entry = conversionUtilTest.convert(activityStreamsObject);

        assertThat(entry.getDisplayName(), is(equalTo(activityStreamsObject.getDisplayName())));


        ActivityStreamsObject reconverted = conversionUtilTest.convert(entry)    ;

        assertThat(entry.getDisplayName(), is(equalTo(reconverted.getDisplayName())));


    }

    @Test
    public void testConvertMediaLink()  {
        MediaLink entry = conversionUtilTest.convert(activityStreamsMediaLink);

        assertThat(entry.getHeight(), is(equalTo(activityStreamsMediaLink.getHeight())));
        assertThat(entry.getDuration(), is(equalTo(activityStreamsMediaLink.getDuration())));
        assertThat(entry.getWidth(), is(equalTo(activityStreamsMediaLink.getWidth())));
        assertThat(entry.getUrl(), is(equalTo(activityStreamsMediaLink.getUrl())));

        ActivityStreamsMediaLink reconverted = conversionUtilTest.convert(entry)    ;

        assertThat(entry.getHeight(), is(equalTo(reconverted.getHeight())));
        assertThat(entry.getDuration(), is(equalTo(reconverted.getDuration())));
        assertThat(entry.getWidth(), is(equalTo(reconverted.getWidth())));
        assertThat(entry.getUrl(), is(equalTo(reconverted.getUrl())));


    }

}
