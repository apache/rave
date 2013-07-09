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
package org.apache.rave.portal.activities.repository;

import org.apache.rave.model.ActivityStreamsEntry;
import org.apache.rave.portal.model.JpaActivityStreamsEntry;
import org.apache.rave.portal.repository.ActivityStreamsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(locations = {"classpath:test-dataContext.xml"
                                  , "classpath:test-applicationContext.xml"})
public class JpaActivityEntryRepositoryTest {
    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private ActivityStreamsRepository repository;

    JpaActivityStreamsEntry test1;
    JpaActivityStreamsEntry test2;
    JpaActivityStreamsEntry test3;

    @Before
    public void setup() {
        test1 = new JpaActivityStreamsEntry();
        test2 = new JpaActivityStreamsEntry();
        test3 = new JpaActivityStreamsEntry();

    }

    @Test
    @Transactional
    @Rollback(true)
    public void getAllTest() {
        repository.save(test1);
        repository.save(test2);
        repository.save(test3);
        List<ActivityStreamsEntry> collection =
          (List<ActivityStreamsEntry>)repository.getAll();
        assertEquals(3, collection.size());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void getByIdTest(){
        repository.save(test1);

        List<ActivityStreamsEntry> collection =
                (List<ActivityStreamsEntry>)repository.getAll();

        ActivityStreamsEntry t = repository.get(collection.get(0).getId());
        assertEquals(t.getId(), collection.get(0).getId());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void deleteTest() {
        repository.save(test1);
        repository.save(test2);
        repository.save(test3);
        String deletedId = test2.getId();
        repository.delete(test2);
        List<ActivityStreamsEntry> collection =
          (List<ActivityStreamsEntry>)repository.getAll();

        // Check that it was actually test2 that was deleted
        Iterator it = collection.iterator();
        while(it.hasNext()) {
          ActivityStreamsEntry ae = (ActivityStreamsEntry)it.next();
          assertNotEquals(ae.getId(), deletedId);
        }
        assertEquals(2, collection.size());
         

    }

    @Test
    @Transactional
    @Rollback(true)
    public void deleteByIdTest() {
        repository.save(test1);
        repository.save(test2);
        repository.save(test3);

        List<ActivityStreamsEntry> collection =
          (List<ActivityStreamsEntry>)repository.getAll();

        String deletedId =  collection.get(0).getId();
        repository.deleteById(deletedId);

        collection =
                (List<ActivityStreamsEntry>)repository.getAll();
        // Check that it was actually test1 that was deleted
        Iterator it = collection.iterator();
        while(it.hasNext()) {
          ActivityStreamsEntry ae = (ActivityStreamsEntry)it.next();
          assertNotEquals(ae.getId(), deletedId);
        }
        assertEquals(2, collection.size());
    }


}
