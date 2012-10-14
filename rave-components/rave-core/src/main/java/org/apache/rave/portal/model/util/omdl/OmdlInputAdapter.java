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
    private List<String> topLeft;
    private List<String> middleLeft;
    private List<String> bottomLeft;
    // region 2
    private List<String> topCenter;
    private List<String> middleCenter;
    private List<String> bottomCenter;
    // region 3
    private List<String> topRight;
    private List<String> middleRight;
    private List<String> bottomRight;
    // unknown region (possibly a 4th region)
    private List<String> unknown;

    public OmdlInputAdapter() {
        topLeft = new ArrayList<String>();
        middleLeft = new ArrayList<String>();
        bottomLeft = new ArrayList<String>();
        topCenter = new ArrayList<String>();
        middleCenter = new ArrayList<String>();
        bottomCenter = new ArrayList<String>();
        topRight = new ArrayList<String>();
        middleRight = new ArrayList<String>();
        bottomRight = new ArrayList<String>();
        unknown = new ArrayList<String>();
    }

    public List<String> getAllUrls(){
        List<String> newList = new ArrayList<String>();
        newList.addAll(getAllLeftUrls());
        newList.addAll(getAllCenterUrls());
        newList.addAll(getAllRightUrls());
        newList.addAll(getAllUnknownUrls());
        return newList;
    }

    public List<String> getAllUnknownUrls(){
        Collections.reverse(unknown);
        return unknown;
    }

    public List<String> getAllRightUrls(){
        Collections.reverse(topRight);
        Collections.reverse(middleRight);
        Collections.reverse(bottomRight);
        List<String> newList = new ArrayList<String>();
        newList.addAll(topRight);
        newList.addAll(middleRight);
        newList.addAll(bottomRight);
        Collections.reverse(newList);
        return newList;
    }

    public List<String> getAllCenterUrls(){
        Collections.reverse(topCenter);
        Collections.reverse(middleCenter);
        Collections.reverse(bottomCenter);
        List<String> newList = new ArrayList<String>();
        newList.addAll(topCenter);
        newList.addAll(middleCenter);
        newList.addAll(bottomCenter);
        Collections.reverse(newList);
        return newList;
    }

    public List<String> getAllLeftUrls(){
        Collections.reverse(topLeft);
        Collections.reverse(middleLeft);
        Collections.reverse(bottomLeft);
        List<String> newList = new ArrayList<String>();
        newList.addAll(topLeft);
        newList.addAll(middleLeft);
        newList.addAll(bottomLeft);
        Collections.reverse(newList);
        return newList;
    }

    public void addToAppMap(String url, String position){
        if(position.contains(POSITION_LEFT)){
            LEFT_FOUND = true;
            if(position.contains(POSITION_TOP)){
                topLeft.add(url);
            }else if (position.contains(POSITION_MIDDLE)) {
                middleLeft.add(url);
            }else{// otherwise go to bottom
                bottomLeft.add(url);
            }
        }
        else if(position.contains(POSITION_CENTER)){
            CENTER_FOUND = true;
            if(position.contains(POSITION_TOP)){
                topCenter.add(url);
            }else if (position.contains(POSITION_MIDDLE)) {
                middleCenter.add(url);
            }else{// otherwise go to bottom
                bottomCenter.add(url);
            }
        }
        else if(position.contains(POSITION_RIGHT)){
            RIGHT_FOUND = true;
            if(position.contains(POSITION_TOP)){
                topRight.add(url);
            }else if (position.contains(POSITION_MIDDLE)) {
                middleRight.add(url);
            }else{// otherwise go to bottom
                bottomRight.add(url);
            }
        }
        else{
            unknown.add(url);
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
