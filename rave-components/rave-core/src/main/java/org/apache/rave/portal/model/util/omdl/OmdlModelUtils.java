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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.rave.portal.model.util.ModelUtils;

/**
 * Utility methods for OMDL import and export
 */
public class OmdlModelUtils implements OmdlConstants{
    
    /**
     * Enumeration to mapping rave internal layout codes to OMDL layout strings
     */
    public enum InternalLayoutCodes {
        columns_1{
            @Override
            public String toString() {
                return NUMBER_ONE + SPACE + COLUMNS;
            }
        },
        columns_2{
            @Override
            public String toString() {
                return NUMBER_TWO + SPACE + COLUMNS;
            }
        }, 
        columns_2wn{
            @Override
            public String toString() {
                return NUMBER_TWO + SPACE + COLUMNS + SPACE + WIDE + SPACE + NARROW;
            }
        }, 
        columns_3{
            @Override
            public String toString() {
                return NUMBER_THREE + SPACE + COLUMNS;
            }
        },  
        columns_3nwn{
            @Override
            public String toString() {
                return NUMBER_THREE + SPACE + COLUMNS + SPACE + NARROW + SPACE + WIDE + SPACE + NARROW;
            }
        }, 
        columns_3_newuser{
            @Override
            public String toString() {
                return NUMBER_TWO + SPACE + COLUMNS + SPACE + NUMBER_TWO + SPACE + ROWS + SPACE + WIDE;
            }
        },
        columns_4{
            @Override
            public String toString() {
                return NUMBER_FOUR + SPACE + COLUMNS;
            }
        }, 
        columns_3nwn_1_bottom{
            @Override
            public String toString() {
                return NUMBER_THREE + SPACE + COLUMNS + SPACE + NARROW + SPACE + WIDE + SPACE + NARROW + SPACE + NUMBER_TWO
                + SPACE + ROWS + SPACE + WIDE;
            }
        };

        public static InternalLayoutCodes convertString(String code){
            try {
                return valueOf(code);
            } catch (Exception e) {
                return columns_1;
            }
        }
    };// Note: person_profile not in the list

    /**
     * Method to convert one of Raves internal page layout codes into
     * an OMDL format layout string using enumeration defined above
     * @param raveLayoutCode
     * @return - converted string
     */
    public static String getOmdlLayoutForExport(String raveLayoutCode) {
        switch (InternalLayoutCodes.convertString(raveLayoutCode)) {
        case columns_1:
            return InternalLayoutCodes.columns_1.toString();
        case columns_2:
            return InternalLayoutCodes.columns_2.toString();
        case columns_2wn:
            return InternalLayoutCodes.columns_2wn.toString();
        case columns_3:
            return InternalLayoutCodes.columns_3.toString();
        case columns_3nwn:
            return InternalLayoutCodes.columns_3nwn.toString();
        case columns_3_newuser:
            return InternalLayoutCodes.columns_3_newuser.toString();
        case columns_4:
            return InternalLayoutCodes.columns_4.toString();
        case columns_3nwn_1_bottom:
            return InternalLayoutCodes.columns_3nwn_1_bottom.toString();
        default:
            return InternalLayoutCodes.columns_1.toString();
        }
    }

    /**
     * Used in a page export to OMDL, maps where a widget is found within
     * a particular region, to a String value in OMDL, giving a hint to positioning
     * @param region - current region
     * @param totalRegions - total regions
     * @param widget - current widget within a region
     * @param totalWidget - total number of widgets in this region
     * @return - a string containing an <app> (regionWidget) position in the UI.
     */
    public static String getPositionString(int region, int totalRegions, int widget, int totalWidget){
        String vs = getVerticalStringForWidget(widget, totalWidget);
        String hs = getColumnStringForWidget(region, totalRegions);
        String both = vs + " " + hs;
        return both.trim();
    }

    /**
     * Find the Vertical position of this widget in OMDL format
     * @param thisWidgetIndex - index of this widget
     * @param totalWidgets - total widgets in region
     * @return - omdl string for vertical alignment
     */
    public static String getVerticalStringForWidget(int thisWidgetIndex, int totalWidgets){
        int count = (totalWidgets + 3 - 1) / 3;
        if(totalWidgets<2){
            return "";
        }
        else if(thisWidgetIndex <= count){
            return POSITION_TOP;
        }
        else if(thisWidgetIndex <= count*2){
            return POSITION_MIDDLE;
        }
        else{
            return POSITION_BOTTOM;
        }
    }

    /**
     * Find the horizontal position of this widget in OMDL format
     * @param currentRegion -  this region
     * @param numberOfRegions - total page regions
     * @return - omdl string for horizontal alignment
     */
    public static String getColumnStringForWidget(int currentRegion, int numberOfRegions){
        String columnStr = "";
        switch (numberOfRegions){
            case 1: // one region - always return blank string ""
                break;
            case 2:
                // two regions
                switch (currentRegion) {
                    case 1: // Two regions current region one
                        columnStr = POSITION_LEFT;
                        break;
                    case 2: // Two regions current region two
                        columnStr = POSITION_RIGHT;
                        break;
                    default: 
                       // nothing
                }
                break;
            default:
                // three regions
                switch (currentRegion) {
                    case 1: // Three regions current region one
                        columnStr = POSITION_LEFT;
                        break;
                    case 2: // Three regions current region two
                        columnStr = POSITION_CENTER;
                        break;
                    case 3: // Three regions current region three
                        columnStr = POSITION_RIGHT;
                        break;
                    default: 
                       // nothing
                }
                break;
        }
        return columnStr;
    }

    /**
     * Return the date to be used in <date> element of OMDL - uses Raves 'standard_date_format'
     * @return date string
     */
    public static String getDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(ModelUtils.STANDARD_DATE_FORMAT);
        return dateFormat.format(new Date());
    }

    /*
     * ############################### IMPORT RELATED ROUTINES ###############################
     */

    /**
     * Given an OMDL string describing the page layout, try to convert this into one
     * of Raves internal layout codes.
     */
    public static String getRaveLayoutForImport(OmdlInputAdapter omdlInputAdapter){
        String omdlLayoutString = omdlInputAdapter.getLayoutCode();
        if(omdlLayoutString == null) {
            return getLayoutFromWidgetsPosition(omdlInputAdapter);
        }
        if(omdlLayoutString.equals(InternalLayoutCodes.columns_1.toString())){
            return "columns_1";
        }else if(omdlLayoutString.equals(InternalLayoutCodes.columns_2.toString())){
            return "columns_2";
        }else if(omdlLayoutString.equals(InternalLayoutCodes.columns_2wn.toString())){
            return "columns_2wn";
        }else if(omdlLayoutString.equals(InternalLayoutCodes.columns_3.toString())){
            return "columns_3";
        }else if(omdlLayoutString.equals(InternalLayoutCodes.columns_3nwn.toString())){
            return "columns_3nwn";
        }else if(omdlLayoutString.equals(InternalLayoutCodes.columns_3_newuser.toString())){
            return "columns_3_newuser";
        }else if(omdlLayoutString.equals(InternalLayoutCodes.columns_4.toString())){
            return "columns_4";
        }else if(omdlLayoutString.equals(InternalLayoutCodes.columns_3nwn_1_bottom.toString())){
            return "columns_3nwn_1_bottom";
        }else{
            return getLayoutFromWidgetsPosition(omdlInputAdapter);
        }
    }

    /**
     * Try to ascertain the page layout from hints given by each <app> widget
     * @param omdlInputAdapter
     * @return - internal rave layout code
     */
    public static String getLayoutFromWidgetsPosition(OmdlInputAdapter omdlInputAdapter){
        if (omdlInputAdapter.LEFT_FOUND && omdlInputAdapter.CENTER_FOUND && omdlInputAdapter.RIGHT_FOUND){
            return "columns_3";
        }
        if (omdlInputAdapter.LEFT_FOUND && !omdlInputAdapter.CENTER_FOUND && omdlInputAdapter.RIGHT_FOUND){
            return "columns_2";
        }
        // give up - make it one column
        return "columns_1";
    }
}
