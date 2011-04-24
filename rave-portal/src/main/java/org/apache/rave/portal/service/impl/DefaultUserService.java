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

package org.apache.rave.portal.service.impl;

import java.util.Random;

import org.apache.rave.portal.model.Person;
import org.apache.rave.portal.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class DefaultUserService implements UserService {
    private String userId;

    @Override
    public Person getAuthenticatedUser() {
        //TODO: Returning random mock data until we hook in real authentication
        String requestUserId;
        Person person = new Person();
        if (this.userId == null) {
          Random random = new Random();
          switch (random.nextInt(3)) {
            case 1: 
              requestUserId = "john.doe";
              break;
            case 2: 
              requestUserId = "jane.doe";
              break;
            default:
              requestUserId = "canonical";
              break;
          }
        } else {
          requestUserId = this.userId;
        }
        person.setUserId(requestUserId);
        return person;
    }
    
    @Override
    public void setAuthenticatedUser(String userId) {
      this.userId = userId;
    }
}