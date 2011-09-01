/*
 * Copyright 2011 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.rave.persistence.impl;

import org.springframework.dao.DataAccessException;

/**
 * Exception class that represents an H2 database exception translated via Spring
 * 
 * @author CARLUCCI
 */
public class TranslatedH2Exception extends DataAccessException {
    private int errorCode;
    private String error;

    public static final int UNKNOWN_ERROR_CODE = -1;
    
    public TranslatedH2Exception(int errorCode, String error, String message) {
        super(message);
        this.errorCode = errorCode;
        this.error = error;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getError() {
        return error;
    }
}
