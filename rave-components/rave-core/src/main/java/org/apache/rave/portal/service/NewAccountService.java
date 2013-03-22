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
package org.apache.rave.portal.service;


import org.apache.rave.model.User;

public interface NewAccountService {

    /**
     * Creates a new account using several other Rave services.
     *
     * @param newUser the {@link org.apache.rave.model.User} from which a new {@link org.apache.rave.model.User} can be created
     * @throws Exception in case something goes wrong
     */
    public void createNewAccount(User newUser) throws Exception;
}