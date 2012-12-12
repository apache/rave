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

package org.apache.rave.portal.repository.impl;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
* Created with IntelliJ IDEA.
* User: mfranklin
* Date: 10/14/12
* Time: 8:14 PM
*/
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-applicationContext.xml"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class MongoDbWidgetRepositoryTest {

//    @Autowired
//    WidgetRepository widgetRepository;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    CategoryRepository categoryRepository;
//
//    @Autowired
//    Mongo mongo;
//
//    @After
//    public void tearDown() {
//        mongo.dropDatabase("rave");
//    }
//
//    @Test
//    public void save_basic() {
//        List<Category> categoryList = Arrays.asList((Category)new CategoryImpl("GOO"), new CategoryImpl("FOO"));
//        categoryRepository.save(categoryList.get(0));
//        categoryRepository.save(categoryList.get(1));
//
//        User user1 = new MongoDbUser(12345L);
//        user1.setDisplayName("GEORGE DOE");
//        userRepository.save(user1);
//
//        User user2 = new MongoDbUser(12345L);
//        user2.setDisplayName("JANE DOE");
//        userRepository.save(user2);
//
//        Widget widget = new WidgetImpl();
//        widget.setUrl("http://localhost:8080/demogadgets/test.xml");
//        widget.setAuthor("mfranklin");
//        widget.setAuthorEmail("developer@apache.org");
//        widget.setDescription("DESCRIPTION");
//        widget.setCategories(categoryList);
//        widget.setOwner(user2);
//        widget.setTitle("TITLE");
//        widget.setTitleUrl("http://title.com");
//        widget.setType("OpenSocial");
//        widget.setWidgetStatus(WidgetStatus.PUBLISHED);
//
//        WidgetComment widgetComment = new WidgetCommentImpl();
//        widgetComment.setText("BOO HOO");
//        widgetComment.setCreatedDate(new Date());
//        widgetComment.setLastModifiedDate(new Date());
//        widgetComment.setUser(user1);
//        widget.setComments(Arrays.asList(widgetComment));
//
//        WidgetRating rating = new WidgetRatingImpl();
//        rating.setScore(10);
//        rating.setUserId(user2.getId());
//        widget.setRatings(Arrays.asList(rating));
//
//        WidgetTag tag = new WidgetTagImpl();
//        tag.setCreatedDate(new Date());
//        tag.setUser(user2);
//        tag.setTag(new TagImpl("TEST"));
//        widget.setTags(Arrays.asList(tag));
//
//        Widget saved = widgetRepository.save(widget);
//
//        Widget fromDb = widgetRepository.get(saved.getId());
//        assertThat(fromDb.getOwner().getId(), is(equalTo(widget.getOwner().getId())));
//        assertThat(fromDb.getRatings().get(0).getScore(), is(equalTo(widget.getRatings().get(0).getScore())));
//        assertThat(fromDb.getComments().get(0).getUser().getId(), is(equalTo(widget.getComments().get(0).getUser().getId())));
//        assertThat(fromDb.getComments().get(0).getText(), is(equalTo(widget.getComments().get(0).getText())));
//        assertThat(fromDb.getTags().get(0).getUser().getId(), is(equalTo(widget.getTags().get(0).getUser().getId())));
//        assertThat(fromDb.getTags().get(0).getTag().getKeyword(), is(equalTo(widget.getTags().get(0).getTag().getKeyword())));
//
//    }
}
