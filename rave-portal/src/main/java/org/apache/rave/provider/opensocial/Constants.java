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

package org.apache.rave.provider.opensocial;

/**
 * Constants used while managing OpenSocial widgets
 */
public class Constants {
    private Constants() {}
    public static final String WIDGET_TYPE = "OpenSocial";
    
    // opensocial string constants    
    public static final String USER_PREFS = "userPrefs";
    public static final String DATA_TYPE = "dataType";
    // rave / opensocial related constants
    public static final String HAS_PREFS_TO_EDIT = "hasPrefsToEdit";
    
    /*
     * enum representing all of the valid OpenSocial preference data types
     */
    public static enum PrefDataTypes {
        STRING("STRING"),
        BOOLEAN("BOOL"),
        ENUM("ENUM"),
        LIST("LIST"),
        HIDDEN("HIDDEN");

        private final String dataType;
        private PrefDataTypes(String dataType) {
            this.dataType = dataType;
        }

        @Override
        public String toString() {
            return dataType;
        }
    }
}