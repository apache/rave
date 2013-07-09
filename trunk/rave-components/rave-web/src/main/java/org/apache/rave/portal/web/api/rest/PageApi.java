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
package org.apache.rave.portal.web.api.rest;

import org.apache.rave.model.Page;
import org.apache.rave.model.Region;
import org.apache.rave.model.RegionWidget;
import org.apache.rave.portal.model.util.omdl.OmdlOutputAdapter;
import org.apache.rave.portal.service.OmdlService;
import org.apache.rave.portal.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handler for all services exposed under the /api/rest/page path.
 * 
 * @author CARLUCCI
 */
@Controller(value="restPageApi")
@RequestMapping("/api/rest/page/*")
public class PageApi extends AbstractRestApi {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private PageService pageService;
    private OmdlService omdlService;
    
    @Autowired
    public PageApi(PageService pageService, OmdlService omdlService) {
        this.pageService = pageService;
        this.omdlService = omdlService;
    }       
          
    @RequestMapping(value = "{pageId}", method = RequestMethod.DELETE)
    public void deletePage(@PathVariable String pageId, HttpServletResponse response) {
        logger.debug("DELETE received for /api/rest/page/" + pageId);
        pageService.deletePage(pageId);
        
        // send a 204 back for success since there is no content being returned
        response.setStatus(HttpStatus.NO_CONTENT.value());
    }

    @ResponseBody
    @RequestMapping(value = "{pageId}/omdl", method = RequestMethod.GET)
    public OmdlOutputAdapter getOmdl(@PathVariable String pageId, HttpServletRequest request, HttpServletResponse response) {
        logger.debug("GET received for /api/rest/page/" + pageId + "/omdl");
        response.setHeader("Content-Disposition", "attachment; filename=\"rave-omdl-page-" + pageId + ".xml\"");
        return omdlService.exportOmdl(pageId, request.getRequestURL().toString());
    }

    @ResponseBody
    @RequestMapping(value = "{pageId}", method = RequestMethod.GET)
    public Page getPage(@PathVariable String pageId, @RequestParam(required=false) boolean export) {
        logger.debug("GET received for /api/rest/page/" + pageId);        
        Page page = pageService.getPage(pageId);
        if(export) {
            modifyForExport(page);
        }
        return page;
    }

    private static void modifyForExport(Page page) {
        page.setOwnerId(null);
        for(Region r : page.getRegions()){
            modifyForExport(r);
        }
    }

    private static void modifyForExport(Region r) {
        for(RegionWidget w : r.getRegionWidgets()) {
            w.setPreferences(null);
        }
    }
}