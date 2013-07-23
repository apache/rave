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
package org.apache.rave.persistence.jpa.impl;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.rave.exception.DuplicateItemException;
import org.apache.rave.persistence.impl.TranslatedH2Exception;
import org.h2.constant.ErrorCode;
import org.h2.jdbc.JdbcSQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.jpa.vendor.OpenJpaDialect;

/**
 * This class is an extension of the OpenJpaDialect class used in configuring 
 * the EntityManagerFactory for our JPA layer.  We needed to override the 
 * translateExceptionIfPossible method to map the H2 specific database errors
 * to Rave's application exceptions.  If/when a new Database provider is used instead
 * of H2 a new class will need to be created for that database implementation.
 * 
 * 
 * @author CARLUCCI
 */
public class H2OpenJpaDialect extends OpenJpaDialect {
    private static final long serialVersionUID = 1L;
    /**
     * Translates an H2 database error into a Rave application Exception
     * 
     * @param re the RuntimeException to translate to a Rave application exception
     * @return a Rave application exception representing the database error
     */
    @Override
    public DataAccessException translateExceptionIfPossible(RuntimeException re) {        
        DataAccessException e = null;
        // first make sure the root exception is actually an org.h2.jdbc.JdbcSQLException
        if (ExceptionUtils.getRootCause(re) instanceof JdbcSQLException) {
            JdbcSQLException rootException = (JdbcSQLException)ExceptionUtils.getRootCause(re);
                        
            // now translate the H2 specific error codes into Rave's common application Exceptions
            // add more error codes to the switch statement that should be specifically trapped                         
            switch(rootException.getErrorCode()) {
                case ErrorCode.DUPLICATE_KEY_1: {
                    e = new DuplicateItemException("DUPLICATE_ITEM", rootException);
                    break;
                }

                default: {
                    e = new TranslatedH2Exception(rootException.getErrorCode(), "ERROR", "Unknown Database Error");
                    break;
                }
            }
        } else {            
            // we got a RuntimeException that wasn't an org.h2.jdbc.JdbcSQLException
            e = new TranslatedH2Exception(TranslatedH2Exception.UNKNOWN_ERROR_CODE, "ERROR", "Unknown Runtime Exception");
        }
        
        return e;                
    }    
}