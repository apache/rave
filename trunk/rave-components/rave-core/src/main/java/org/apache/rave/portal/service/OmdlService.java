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

import org.apache.rave.model.Page;
import org.apache.rave.portal.model.util.omdl.OmdlOutputAdapter;
import org.springframework.web.multipart.MultipartFile;

public interface OmdlService {
    /**
     * Export a page object (together with all its regionwidgets) to a jaxb OMDL xml format
     * @param pageId - pageId to export
     * @param omdlUrl - page url - used in construction of OMDL file
     * @return an OmdlOutputAdapter - a JaxB xml OMDL format
     */
    OmdlOutputAdapter exportOmdl(String pageId, String omdlUrl);
    
    /**
     * Import an OMDL xml format file to generate into a page object (with all of its reqionwidgets)
     * @param multipartFile - uploaded xml file
     * @param pageName - user defined page name
     * @return - rave page object
     */
    Page importOmdl(MultipartFile multipartFile, String pageName);

}
