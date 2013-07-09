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

package org.apache.rave.rest.model;


import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Pages", propOrder = {
        "pages"
})
@XmlRootElement(name = "Pages")
public class PageList {

    @XmlElement(name = "Page")
    private List<Page> pages;

    public PageList() { }

    public PageList(List<Page> pages) {
        this.pages = pages;
    }

    public List<Page> getPages() {
        if(pages == null) pages = new ArrayList<Page>();
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }
}
