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

import org.apache.rave.portal.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaCategoryConverterTest {

    @Autowired
    private JpaCategoryConverter categoryConverter;

    @Test
    public void noConversion() {
        Category category = new JpaCategory();
        assertThat(categoryConverter.convert(category), is(sameInstance(category)));
    }

    @Test
    public void newCategory() {
        Category category = new CategoryImpl();
        category.setCreatedDate(new Date());
        category.setId(9L);
        category.setLastModifiedDate(new Date());
        category.setText("hello");
        category.setCreatedUser(new User(1L));
        category.setLastModifiedUser(new User(1L));
        category.setWidgets(new ArrayList<Widget>());

        JpaCategory converted = categoryConverter.convert(category);
        assertThat(converted, is(not(sameInstance(category))));
        assertThat(converted, is(instanceOf(JpaCategory.class)));
        assertThat(converted.getCreatedDate(), is(equalTo(category.getCreatedDate())));
        assertThat(converted.getCreatedUser(), is(equalTo(category.getCreatedUser())));
        assertThat(converted.getEntityId(), is(equalTo(category.getId())));
        assertThat(converted.getId(), is(equalTo(category.getId())));
        assertThat(converted.getLastModifiedDate(), is(equalTo(category.getLastModifiedDate())));
        assertThat(converted.getLastModifiedUser(), is(equalTo(category.getLastModifiedUser())));
        assertThat(converted.getText(), is(equalTo(category.getText())));
        assertThat(converted.getWidgets(), is(equalTo(category.getWidgets())));
    }
}
