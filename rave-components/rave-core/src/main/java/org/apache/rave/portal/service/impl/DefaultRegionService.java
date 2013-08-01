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

import org.apache.rave.model.Region;
import org.apache.rave.rest.model.SearchResult;
import org.apache.rave.portal.repository.RegionRepository;
import org.apache.rave.portal.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DefaultRegionService implements RegionService {
	 
	 private final RegionRepository regionRepository;
	 
	 @Autowired
	 public DefaultRegionService(RegionRepository regionRepository) {
		  this.regionRepository = regionRepository;
	 }
	 
	 @Override
	 @Transactional
	 public void registerNewRegion(Region region) {
		  regionRepository.save(region);
	 }

    @Override
    public SearchResult<Region> getAll() {
        final int count = regionRepository.getCountAll();
        final List<Region> regions = regionRepository.getAll();
        return new SearchResult<Region>(regions, count);
    }

    @Override
    public SearchResult<Region> getLimitedList(int offset, int pageSize) {
        final int count = regionRepository.getCountAll();
        final List<Region> regions = regionRepository.getLimitedList(offset, pageSize);
        final SearchResult<Region> searchResult = new SearchResult<Region>(regions, count);
        searchResult.setOffset(offset);
        searchResult.setPageSize(pageSize);
        return searchResult;
    }

}