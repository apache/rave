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
/**
 * Simple bean to model the identifier and url link for a given omdl widget
 */
public class OmdlWidgetReference {

    private String widgetIdentifier;
    private String widgetLink;
    private String widgetType;

    public OmdlWidgetReference(String widgetIdentifier, String widgetLink, String widgetType) {
        this.widgetIdentifier = widgetIdentifier;
        this.widgetLink = widgetLink;
        this.widgetType = widgetType;
    }
    
    public String getRaveWidgetTypeFromFormatType(){
        if(widgetType.equals(OmdlConstants.APP_TYPE_OPENSOCIAL)){
            return OmdlConstants.RAVE_APP_TYPE_OPENSOCIAL;
        }else if(widgetType.equals(OmdlConstants.APP_TYPE_W3C)){
            return OmdlConstants.RAVE_APP_TYPE_W3C;
        }
        return null;
    }

    public String getWidgetIdentifier() {
        return widgetIdentifier;
    }

    public void setWidgetIdentifier(String widgetIdentifier) {
        this.widgetIdentifier = widgetIdentifier;
    }

    public String getWidgetLink() {
        if(!widgetLink.contains("?format=") && widgetType != null){
            return widgetLink + "?format=" + widgetType; 
        }
        return widgetLink;
    }

    public void setWidgetLink(String widgetLink) {
        this.widgetLink = widgetLink;
    }

    public String getWidgetType() {
        return widgetType;
    }

    public void setWidgetType(String widgetType) {
        this.widgetType = widgetType;
    }

}
