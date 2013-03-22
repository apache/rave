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

package org.apache.rave.portal.model.conversion.impl;

import com.google.common.collect.Lists;
import org.apache.rave.model.*;
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.impl.*;
import org.apache.rave.portal.repository.PageLayoutRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: dsullivan
 * Date: 11/28/12
 * Time: 8:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class MongoDbUserConverterTest {
    private MongoDbUserConverter userConverter;

    @Before
    public void setup() {
        userConverter = new MongoDbUserConverter();
        PageLayoutRepository pageLayoutRepository = createMock(PageLayoutRepository.class);
        userConverter.setPageLayoutRepository(pageLayoutRepository);
    }

    @Test
    public void getType_Valid(){
        assertThat(userConverter.getSourceType(), is(equalTo(User.class)));
    }

    @Test
    public void hydrate_Valid() {
        MongoDbUser user = new MongoDbUser();
        userConverter.hydrate(user);

        assertNotNull(user.getFriends());
        assertNotNull(user.getAuthorityCodes());
        assertThat(user.getPageLayoutRepository(), is(sameInstance(userConverter.getPageLayoutRepository())));

        userConverter.hydrate(null);
        assertThat(true, is(true));
    }

    @Test
    public void hydrate_Null(){
        MongoDbUser user = new MongoDbUser();
        user.setFriends(Lists.<MongoDbPersonAssociation>newArrayList());
        user.setAuthorityCodes(Lists.<String>newArrayList());
        userConverter.hydrate(user);

        assertNotNull(user.getFriends());
        assertNotNull(user.getAuthorityCodes());
        assertThat(user.getPageLayoutRepository(), is(sameInstance(userConverter.getPageLayoutRepository())));
    }

    @Test
    public void convert_Valid() {
        User source = new MongoDbUser("1234");
        PageLayoutRepository pageLayoutRepository = createMock(PageLayoutRepository.class);
        ((MongoDbUser)source).setPageLayoutRepository(pageLayoutRepository);
        expect(pageLayoutRepository.getByPageLayoutCode("User")).andReturn(new PageLayoutImpl());
        //create dummy fields for source
        Authority authority1 = new AuthorityImpl("Role1");
        Authority authority2 = new AuthorityImpl("Role2");
        Collection<Authority> authorities = Arrays.asList(
                authority1,
                authority2
        );
        source.setAuthorities(authorities);
        source.setUsername("Stinky");
        source.setEmail("stinky@mcdingus.com");
        source.setDisplayName("Stinky McDingus");
        source.setAdditionalName("Stinky-Marie");
        source.setFamilyName("McDingus");
        source.setGivenName("Stinkapotamus");
        source.setHonorificPrefix("Sir");
        source.setHonorificSuffix("Esquire");
        source.setPreferredName("Stink");
        source.setAboutMe("I live in the Himalayas");
        source.setStatus("Cold up here");
        source.setAddresses(Arrays.asList((Address) new AddressImpl("123 Mount Everest")));
        source.setOrganizations(Arrays.asList(
                (Organization) new OrganizationImpl(),
                (Organization) new OrganizationImpl()
        ));
        source.setProperties(Arrays.asList(
                (PersonProperty) new PersonPropertyImpl(),
                (PersonProperty) new PersonPropertyImpl()
        ));
        source.setPassword("12345");
        source.setConfirmPassword("54321");
        source.setDefaultPageLayoutCode("PageLayout");
        source.setEnabled(true);
        source.setExpired(true);
        source.setLocked(true);
        source.setOpenId("9977");
        source.setForgotPasswordHash("Forgotten Password");
        source.setForgotPasswordTime(new Date());

        MongoDbUser converted = userConverter.convert(source);

        //Test for setting authorityCodes
        Iterator sourceIter = source.getAuthorities().iterator();
        Iterator convertedIter = converted.getAuthorities().iterator();
        while (sourceIter.hasNext()) {
            assertThat(
                    ((GrantedAuthority)sourceIter.next()).getAuthority(),
                    is(
                            ((GrantedAuthority) convertedIter.next()).getAuthority()
                    )
            );
        }

        //Test for updateProperties
        assertThat(converted.getId(), is(source.getId()));
        assertThat(converted.getUsername(), is(source.getUsername()));
        assertThat(converted.getEmail(), is(source.getEmail()));
        assertThat(converted.getDisplayName(), is(source.getDisplayName()));
        assertThat(converted.getAdditionalName(), is(source.getAdditionalName()));
        assertThat(converted.getFamilyName(), is(source.getFamilyName()));
        assertThat(converted.getGivenName(), is(source.getGivenName()));
        assertThat(converted.getHonorificPrefix(), is(source.getHonorificPrefix()));
        assertThat(converted.getHonorificSuffix(), is(source.getHonorificSuffix()));
        assertThat(converted.getPreferredName(), is(source.getPreferredName()));
        assertThat(converted.getAboutMe(), is(source.getAboutMe()));
        assertThat(converted.getStatus(), is(source.getStatus()));
        assertThat(converted.getStatus(), is(source.getStatus()));
        for(int i = 0; i<converted.getAddresses().size(); i++){
            assertThat(converted.getAddresses().get(i), is(sameInstance(source.getAddresses().get(i))));
        }
        for(int i = 0; i<converted.getOrganizations().size(); i++){
            assertThat(converted.getOrganizations().get(i), is(sameInstance(source.getOrganizations().get(i))));
        }
        for(int i = 0; i<converted.getProperties().size(); i++){
            assertThat(converted.getProperties().get(i), is(sameInstance(source.getProperties().get(i))));
        }
        assertThat(converted.getPassword(), is(source.getPassword()));
        assertThat(converted.getConfirmPassword(), is(source.getConfirmPassword()));
        //This method should test to ensure that the Default Page Layout has been set
        //assertThat(converted.getDefaultPageLayout(), is(source.getDefaultPageLayout()));
        assertThat(converted.isEnabled(), is(source.isEnabled()));
        assertThat(converted.isExpired(), is(source.isExpired()));
        assertThat(converted.isLocked(), is(source.isLocked()));
        assertThat(converted.getOpenId(), is(source.getOpenId()));
        assertThat(converted.getForgotPasswordHash(), is(source.getForgotPasswordHash()));
        assertThat(converted.getForgotPasswordTime(), is(source.getForgotPasswordTime()));

        //create UserImpl to test else block
        source = new UserImpl();
        PageLayout pageLayout = new PageLayoutImpl();
        pageLayout.setCode("code");
        source.setDefaultPageLayout(pageLayout);
        converted = userConverter.convert(source);

        //assertNotNull(converted.getId());
        assertThat(converted.getDefaultPageLayoutCode(), is(pageLayout.getCode()));
        assertNull(source.getDefaultPageLayout());

    }

    @Test
    public void convert_Null_DefaultPageLayoutCode(){
        User source = new UserImpl();

        User converted = userConverter.convert(source);

        assertNull(converted.getDefaultPageLayoutCode());
    }
}
