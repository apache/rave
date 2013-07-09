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
package org.apache.rave.portal.model.conversion;

import org.apache.rave.model.ApplicationData;
import org.apache.rave.portal.model.JpaApplicationData;
import org.apache.rave.portal.model.impl.ApplicationDataImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaApplicationDataConverterTest {

    @Autowired
    private JpaApplicationDataConverter applicationDataConverter;

    @Test
    public void noConversion() {
        ApplicationData applicationData = new JpaApplicationData();
        assertThat(applicationDataConverter.convert(applicationData), is(sameInstance(applicationData)));
    }

    @Test
    public void nullConversion() {
        ApplicationData applicationData = null;
        assertThat(applicationDataConverter.convert(applicationData), is(nullValue()));
    }

    @Test
    public void newApplicationData() {
        ApplicationData applicationData = new ApplicationDataImpl();
        applicationData.setId("1");
        applicationData.setAppUrl("url");
        applicationData.setData(new HashMap<String, String>());
        applicationData.setUserId("userid");

        JpaApplicationData converted = applicationDataConverter.convert(applicationData);
        assertThat(converted, is(not(sameInstance(applicationData))));
        assertThat(converted, is(instanceOf(JpaApplicationData.class)));
        assertThat(converted.getId(), is(equalTo(applicationData.getId())));
        assertThat(converted.getEntityId().toString(), is(equalTo(applicationData.getId())));
        assertThat(converted.getAppUrl(), is(equalTo(applicationData.getAppUrl())));
        assertThat(converted.getData(), is(equalTo(applicationData.getData())));
        assertThat(converted.getUserId(), is(equalTo(applicationData.getUserId())));
    }
}