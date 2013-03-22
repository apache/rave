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
package org.apache.rave.portal.model.util.omdl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import org.apache.rave.model.Page;
import org.apache.rave.model.User;
import org.apache.rave.model.Widget;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.service.WidgetService;

/**
 * This is a jaxb page export format for the OMDL specification
 * 
 * See: http://omdl.org 
 * 
 * for more information on the specification.
 * 
 */
//@Component
@XmlRootElement(name = "workspace")
@XmlAccessorType(XmlAccessType.NONE)
public class OmdlOutputAdapter implements OmdlConstants {

    @SuppressWarnings("unused")
    private Page page;

    private String wookieUrl;

    @SuppressWarnings("unused")
    @XmlElement
    private Status status;
    
    @SuppressWarnings("unused")
    @XmlElement
    private String identifier;
    
    @XmlElement
    private String title;
    
    @SuppressWarnings("unused")
    @XmlElement
    private String description;
    
    @SuppressWarnings("unused")
    @XmlElement
    private String creator;
    
    @SuppressWarnings("unused")
    @XmlElement
    private String date;
    
    @SuppressWarnings("unused")
    @XmlElement
    private String layout;

    private List<OmdlWidget> widgetsList;

    private WidgetService widgetService;
    private UserService userService;

    // needed for the JAXBContext when serializing to xml
    public OmdlOutputAdapter(){}

    //@Autowired
    public OmdlOutputAdapter(WidgetService widgetService, UserService userService){
        this.widgetService = widgetService;
        this.userService = userService;
    }

    public void buildModel(Page page, String omdlUrl, String wookieUrl) {
        this.wookieUrl = wookieUrl;
        OmdlWidget omdlWidget = null;
        this.status = new Status(OmdlModelUtils.getDate(), DEFAULT_STATUS);
        this.page = page;
        this.identifier = omdlUrl;
        this.title = page.getName();
        this.description = DEFAULT_DESCRIPTION;
        this.creator = getCreator(page);
        this.date = OmdlModelUtils.getDate();
        this.layout = OmdlModelUtils.getOmdlLayoutForExport(page.getPageLayout().getCode());
        widgetsList = new ArrayList<OmdlWidget>();
        for(int i=0;i<page.getRegions().size(); i++){
            for(int j=0;j<page.getRegions().get(i).getRegionWidgets().size();j++){
                 Widget widget = widgetService.getWidget(page.getRegions().get(i).getRegionWidgets().get(j).getWidgetId());
                 omdlWidget = new OmdlWidget();
                 omdlWidget.setUrl(widget.getUrl());
                 omdlWidget.setLink(createLinkElement(widget.getType(), widget.getUrl()));
                 // figure out what positioning to use for this widget
                 String position = OmdlModelUtils.getPositionString(i+1, page.getRegions().size(), j+1, 
                         page.getRegions().get(i).getRegionWidgets().size());
                 if(position != null && position !=""){
                     omdlWidget.setPosition(position);
                 }
                 omdlWidget.setWidgetType(UNKNOWN_VALUE);
                 widgetsList.add(omdlWidget);
            }
        }
    }

    public void setWookieUrl(String wookieUrl) {
        this.wookieUrl = wookieUrl;
    }

    private Link createLinkElement(String appType, String url){
        Link link = new Link();
        if(appType.equals("OpenSocial")){
            appType = APP_TYPE_OPENSOCIAL;
        }else if(appType.equals("W3C")){
            appType = APP_TYPE_W3C;
            url = wookieUrl + "/widgets/" + url;
        }else{
            appType = APP_TYPE_UNKNOWN;
        }
        link.setType(appType);
        link.setRel(REL_TYPE);
        link.setHref(url);
        return link;
    }

    private String getCreator(Page page){
        String result="";
        User user = userService.getUserById(page.getOwnerId());
        result = user.getDisplayName();
        if(result == null || result.equals("")){
            result = user.getPreferredName();
            if(result == null || result.equals("")){
                result = user.getUsername();
            }
        }
        return result;
    }
    
    @XmlElement(name="app")
    public List<OmdlWidget> getWidgetsList() {
        return widgetsList;
    }

    public void setWidgetsList(List<OmdlWidget> widgetsList) {
        this.widgetsList = widgetsList;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Private class used to model OMDL element <status>
     */
    @XmlAccessorType(XmlAccessType.NONE)
    private static class Status {
        @XmlAttribute
        private String date;
        @SuppressWarnings("unused")
        @XmlValue
        private String text;

        @SuppressWarnings("unused")
        public Status(){}
        
        public Status(String date, String text){
            this.date = date;
            this.text = text;
        }

        @SuppressWarnings("unused")
        public String getDate() {
            return date;
        }

        @SuppressWarnings("unused")
        public void setDate(String date) {
            this.date = date;
        }
    }

    @XmlAccessorType(XmlAccessType.NONE)
    private static class Link {
        @SuppressWarnings("unused")
        @XmlAttribute
        private String rel;
        @SuppressWarnings("unused")
        @XmlAttribute
        private String type;
        @SuppressWarnings("unused")
        @XmlAttribute
        private String href;

        public Link(){}

        public void setHref(String href) {
            this.href = href;
        }

        public void setRel(String rel) {
            this.rel = rel;
        }
        public void setType(String type) {
            this.type = type;
        }
    }
    
    /**
     * Private class used to model the OMDL child element of <app>
     */
    @XmlAccessorType(XmlAccessType.NONE)
    private static class OmdlWidget {

        @SuppressWarnings("unused")
        @XmlAttribute(name = "id")
        private String url;
        @SuppressWarnings("unused")
        @XmlElement(name = "type")
        private String widgetType;
        @SuppressWarnings("unused")
        @XmlElement
        private Link link;
        @SuppressWarnings("unused")
        @XmlElement
        private String position;

        public OmdlWidget(){}

        public void setPosition(String position) {
            this.position = position;
        }

        public void setWidgetType(String widgetType) {
            this.widgetType = widgetType;
        }

        public void setUrl(String url) {
            this.url = url;
        }
        
        public void setLink(Link link) {
            this.link = link;
        }

    }
}