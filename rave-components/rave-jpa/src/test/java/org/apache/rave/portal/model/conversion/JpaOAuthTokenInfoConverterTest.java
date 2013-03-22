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

import org.apache.rave.portal.model.JpaOAuthTokenInfo;
import org.apache.rave.model.OAuthTokenInfo;
import org.apache.rave.portal.model.impl.OAuthTokenInfoImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaOAuthTokenInfoConverterTest {

    @Autowired
    private JpaOAuthTokenInfoConverter oAuthTokenInfoConverter;

    @Test
    public void noConversion() {
        OAuthTokenInfo oAuthTokenInfo = new JpaOAuthTokenInfo();
        assertThat(oAuthTokenInfoConverter.convert(oAuthTokenInfo), is(sameInstance(oAuthTokenInfo)));
    }

    @Test
    public void nullConversion() {
        OAuthTokenInfo oAuthTokenInfo = null;
        assertThat(oAuthTokenInfoConverter.convert(oAuthTokenInfo), is(nullValue()));
    }

    @Test
    public void newOAuthTokenInfo() {
        OAuthTokenInfo oAuthTokenInfo = new OAuthTokenInfoImpl();
        oAuthTokenInfo.setId("1");
        oAuthTokenInfo.setAccessToken("accesstoken");
        oAuthTokenInfo.setAppUrl("appurl");
        oAuthTokenInfo.setModuleId("moduleid");
        oAuthTokenInfo.setServiceName("servicename");
        oAuthTokenInfo.setSessionHandle("sessionhandle");
        oAuthTokenInfo.setTokenExpireMillis(99L);
        oAuthTokenInfo.setTokenName("tokenname");
        oAuthTokenInfo.setTokenSecret("tokensecret");
        oAuthTokenInfo.setUserId("userid");

        JpaOAuthTokenInfo converted = oAuthTokenInfoConverter.convert(oAuthTokenInfo);
        assertThat(converted, is(not(sameInstance(oAuthTokenInfo))));
        assertThat(converted, is(instanceOf(JpaOAuthTokenInfo.class)));
        assertThat(converted.getId(), is(equalTo(oAuthTokenInfo.getId())));
        assertThat(converted.getAccessToken(), is(equalTo(oAuthTokenInfo.getAccessToken())));
        assertThat(converted.getAppUrl(), is(equalTo(oAuthTokenInfo.getAppUrl())));
        assertThat(converted.getModuleId(), is(equalTo(oAuthTokenInfo.getModuleId())));
        assertThat(converted.getServiceName(), is(equalTo(oAuthTokenInfo.getServiceName())));
        assertThat(converted.getSessionHandle(), is(equalTo(oAuthTokenInfo.getSessionHandle())));
        assertThat(converted.getTokenExpireMillis(), is(equalTo(oAuthTokenInfo.getTokenExpireMillis())));
        assertThat(converted.getTokenName(), is(equalTo(oAuthTokenInfo.getTokenName())));
        assertThat(converted.getTokenSecret(), is(equalTo(oAuthTokenInfo.getTokenSecret())));
        assertThat(converted.getUserId(), is(equalTo(oAuthTokenInfo.getUserId())));
    }
}