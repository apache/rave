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
import java.util.Collections;
import java.util.List;

/**
 * This class is used to store the results found from parsing the contents
 * of an OMDL xml file.  We try to map the position instructions
 * found in the OMDL <app> elements to regions in Rave. This can then
 * be used to rebuild the page with region widgets in various regions
 */
public class OmdlInputAdapter implements OmdlConstants {

    // Has a LEFT Column been detected
    public boolean LEFT_FOUND = false;
    // Has a CENTER Column been detected
    public boolean CENTER_FOUND = false;
    // Has a RIGHT Column been detected
    public boolean RIGHT_FOUND = false;
    // Default page name given in xml file 
    private String name;
    // stores the layout code
    private String layoutCode;

    // region 1
    private List<OmdlWidgetReference> topLeft;
    private List<OmdlWidgetReference> middleLeft;
    private List<OmdlWidgetReference> bottomLeft;
    // region 2
    private List<OmdlWidgetReference> topCenter;
    private List<OmdlWidgetReference> middleCenter;
    private List<OmdlWidgetReference> bottomCenter;
    // region 3
    private List<OmdlWidgetReference> topRight;
    private List<OmdlWidgetReference> middleRight;
    private List<OmdlWidgetReference> bottomRight;
    // unknown region (possibly a 4th region)
    private List<OmdlWidgetReference> unknown;

    public OmdlInputAdapter() {
        topLeft = new ArrayList<OmdlWidgetReference>();
        middleLeft = new ArrayList<OmdlWidgetReference>();
        bottomLeft = new ArrayList<OmdlWidgetReference>();
        topCenter = new ArrayList<OmdlWidgetReference>();
        middleCenter = new ArrayList<OmdlWidgetReference>();
        bottomCenter = new ArrayList<OmdlWidgetReference>();
        topRight = new ArrayList<OmdlWidgetReference>();
        middleRight = new ArrayList<OmdlWidgetReference>();
        bottomRight = new ArrayList<OmdlWidgetReference>();
        unknown = new ArrayList<OmdlWidgetReference>();
    }

    public List<OmdlWidgetReference> getAllUrls(){
        List<OmdlWidgetReference> newList = new ArrayList<OmdlWidgetReference>();
        newList.addAll(getAllLeftUrls());
        newList.addAll(getAllCenterUrls());
        newList.addAll(getAllRightUrls());
        newList.addAll(getAllUnknownUrls());
        return newList;
    }

    public List<OmdlWidgetReference> getAllUnknownUrls(){
        Collections.reverse(unknown);
        return unknown;
    }

    public List<OmdlWidgetReference> getAllRightUrls(){
        Collections.reverse(topRight);
        Collections.reverse(middleRight);
        Collections.reverse(bottomRight);
        List<OmdlWidgetReference> newList = new ArrayList<OmdlWidgetReference>();
        newList.addAll(topRight);
        newList.addAll(middleRight);
        newList.addAll(bottomRight);
        Collections.reverse(newList);
        return newList;
    }

    public List<OmdlWidgetReference> getAllCenterUrls(){
        Collections.reverse(topCenter);
        Collections.reverse(middleCenter);
        Collections.reverse(bottomCenter);
        List<OmdlWidgetReference> newList = new ArrayList<OmdlWidgetReference>();
        newList.addAll(topCenter);
        newList.addAll(middleCenter);
        newList.addAll(bottomCenter);
        Collections.reverse(newList);
        return newList;
    }

    public List<OmdlWidgetReference> getAllLeftUrls(){
        Collections.reverse(topLeft);
        Collections.reverse(middleLeft);
        Collections.reverse(bottomLeft);
        List<OmdlWidgetReference> newList = new ArrayList<OmdlWidgetReference>();
        newList.addAll(topLeft);
        newList.addAll(middleLeft);
        newList.addAll(bottomLeft);
        Collections.reverse(newList);
        return newList;
    }

    public void addToAppMap(OmdlWidgetReference widgetReference, String position){
        if(position.contains(POSITION_LEFT)){
            LEFT_FOUND = true;
            if(position.contains(POSITION_TOP)){
                topLeft.add(widgetReference);
            }else if (position.contains(POSITION_MIDDLE)) {
                middleLeft.add(widgetReference);
            }else{// otherwise go to bottom
                bottomLeft.add(widgetReference);
            }
        }
        else if(position.contains(POSITION_CENTER)){
            CENTER_FOUND = true;
            if(position.contains(POSITION_TOP)){
                topCenter.add(widgetReference);
            }else if (position.contains(POSITION_MIDDLE)) {
                middleCenter.add(widgetReference);
            }else{// otherwise go to bottom
                bottomCenter.add(widgetReference);
            }
        }
        else if(position.contains(POSITION_RIGHT)){
            RIGHT_FOUND = true;
            if(position.contains(POSITION_TOP)){
                topRight.add(widgetReference);
            }else if (position.contains(POSITION_MIDDLE)) {
                middleRight.add(widgetReference);
            }else{// otherwise go to bottom
                bottomRight.add(widgetReference);
            }
        }
        else{
            unknown.add(widgetReference);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLayoutCode() {
        return layoutCode;
    }

    public void setLayoutCode(String layoutCode) {
        this.layoutCode = layoutCode;
    }

}
