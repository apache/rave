/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.apache.rave.os;

import org.apache.rave.os.model.RaveNameImpl;
import org.apache.shindig.social.opensocial.jpa.EnumDb;
import org.apache.shindig.social.opensocial.jpa.PersonDb;
import org.apache.shindig.social.opensocial.model.Drinker;
import org.apache.shindig.social.opensocial.model.Name;
import org.apache.shindig.social.opensocial.model.NetworkPresence;
import org.apache.shindig.social.opensocial.model.Person.Gender;
import org.apache.shindig.social.opensocial.model.Smoker;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class DatabasePopulateContextListener implements ServletContextListener {

    private final static String PersistenceUnitName = "raveShindigPersistenceUnit";
    private static EntityManager em;
    private static EntityManagerFactory emf;

    public static EntityManager getEntityManager() {
        if (em == null) {
            if (emf == null) {
                emf = Persistence.createEntityManagerFactory(PersistenceUnitName);
            }
            em = emf.createEntityManager();
        }
        return em;
    }


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String sqlScripts[] = {"initial_data.sql"};

        List<File> scriptLocations = new ArrayList<File>();
        for (String sqlScript : sqlScripts) {
            scriptLocations.add(new File(sqlScript));
        }

        populateDatabase();
    }

    private void populateDatabase() {
        PersonDb person = new PersonDb();
        person.setId("canonical");
        Name name = new RaveNameImpl();
        name.setFamilyName("canonical");
        name.setGivenName("canonical");
        person.setName(name);
        person.setDrinker(new EnumDb<Drinker>(Drinker.NO));
        person.setGender(Gender.male);
        person.setNetworkPresence(new EnumDb<NetworkPresence>(NetworkPresence.ONLINE));
        person.setAboutMe("About me");
        person.setAge(25);
        person.setBirthday(new Date());
        person.setNickname("hoosier");
        person.setSmoker(new EnumDb<Smoker>(Smoker.NO));

        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(person);
        em.flush();
        em.getTransaction().commit();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
