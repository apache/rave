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

import org.apache.rave.portal.model.JpaTag;
import org.apache.rave.portal.model.impl.TagImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaTagConverterTest {
    
    @Autowired
    JpaTagConverter jpaTagConverter;

    @Test
    public void convert_valid_tagImpl(){
        TagImpl tag = new TagImpl("blazer");
        JpaTag jpaTag = jpaTagConverter.convert(tag);
        assertNotNull(jpaTag);
        assertEquals(tag.getKeyword(), jpaTag.getKeyword());
    }


    @Test
    public void convert_valid_jpaTag(){
        JpaTag tag = new JpaTag();
        tag.setKeyword("blazer");
        tag.setEntityId(387L);
        JpaTag jpaTag = jpaTagConverter.convert(tag);
        assertNotNull(jpaTag);
        assertSame(tag, jpaTag);
    }
    
}
