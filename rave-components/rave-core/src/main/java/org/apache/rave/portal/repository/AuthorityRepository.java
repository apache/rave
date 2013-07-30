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

package org.apache.rave.portal.repository;

import org.apache.rave.repository.Repository;
import org.apache.rave.model.Authority;

import java.util.List;

/**
 * Repository interface for {@link org.apache.rave.model.Authority}
 */
public interface AuthorityRepository extends Repository<Authority> {

    /**
     * Finds the {@link org.apache.rave.model.Authority} by its name
     *
     * @param authorityName (unique) name of the Authority
     * @return Authority if it can be found, otherwise {@literal null}
     */
    Authority getByAuthority(String authorityName);

    /**
     * @return a List of all default {@link org.apache.rave.model.Authority}'s.
     */
    List<Authority> getAllDefault();
}
