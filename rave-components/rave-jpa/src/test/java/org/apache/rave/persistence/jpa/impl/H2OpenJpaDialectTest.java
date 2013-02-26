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
package org.apache.rave.persistence.jpa.impl;

import org.apache.rave.exception.DuplicateItemException;
import org.apache.rave.persistence.impl.TranslatedH2Exception;
import org.h2.constant.ErrorCode;
import org.h2.jdbc.JdbcSQLException;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author CARLUCCI
 */
public class H2OpenJpaDialectTest {
    private H2OpenJpaDialect dialect;
    
    @Before
    public void setUp() {
        dialect = new H2OpenJpaDialect();
    }
  
    /**
     * Test of translateExceptionIfPossible method, of class H2OpenJpaDialect.
     */
    @Test
    public void testTranslateExceptionIfPossible_uniqueContstraintViolation() {                 
        JdbcSQLException jdbcEx = new JdbcSQLException("message", "sql statement", "state", ErrorCode.DUPLICATE_KEY_1, null, "stacktrace");
        RuntimeException re = new RuntimeException("dummy runtime exception", jdbcEx);

        assertTrue(dialect.translateExceptionIfPossible(re) instanceof DuplicateItemException);
    }
    
    @Test
    public void testTranslateExceptionIfPossible_unknownJdbcSQLExceptionError() {                 
        JdbcSQLException jdbcEx = new JdbcSQLException("message", "sql statement", "state", ErrorCode.CANNOT_DROP_CURRENT_USER, null, "stacktrace");
        RuntimeException re = new RuntimeException("dummy runtime exception", jdbcEx);     
        
        TranslatedH2Exception translatedException = (TranslatedH2Exception) dialect.translateExceptionIfPossible(re);             
        assertThat(translatedException.getErrorCode(), is(ErrorCode.CANNOT_DROP_CURRENT_USER));        
    }
    
    @Test
    public void testTranslateExceptionIfPossible_unknownRuntimeError() {                         
        RuntimeException re = new RuntimeException("dummy runtime exception", new NullPointerException("bad"));     
        
        TranslatedH2Exception translatedException = (TranslatedH2Exception) dialect.translateExceptionIfPossible(re);             
        assertThat(translatedException.getErrorCode(), is(TranslatedH2Exception.UNKNOWN_ERROR_CODE));        
    }    
}
