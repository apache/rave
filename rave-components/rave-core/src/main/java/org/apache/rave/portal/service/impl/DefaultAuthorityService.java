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

import org.apache.rave.model.Authority;
import org.apache.rave.rest.model.SearchResult;
import org.apache.rave.portal.repository.AuthorityRepository;
import org.apache.rave.portal.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultAuthorityService implements AuthorityService {


    private final AuthorityRepository repository;

    @Autowired
    public DefaultAuthorityService(AuthorityRepository repository) {
        this.repository = repository;
    }

    @Override
    public Authority getAuthorityById(String entityId) {
        return repository.get(entityId);
    }

    @Override
    public Authority getAuthorityByName(String authorityName) {
        return repository.getByAuthority(authorityName);
    }

    @Override
    public SearchResult<Authority> getAllAuthorities() {
        final int count = repository.getCountAll();
        final List<Authority> authorities = repository.getAll();
        return new SearchResult<Authority>(authorities, count);
    }
    
    @Override
    public SearchResult<Authority> getDefaultAuthorities() {       
        final List<Authority> authorities = repository.getAllDefault();
        return new SearchResult<Authority>(authorities, authorities.size());
    }    
}
