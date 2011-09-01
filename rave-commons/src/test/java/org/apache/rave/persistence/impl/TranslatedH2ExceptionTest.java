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

package org.apache.rave.persistence.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author CARLUCCI
 */
public class TranslatedH2ExceptionTest {       
    private final int ERROR_CODE = 123;
    private final String ERROR = "Dummy Error";
    private final String MESSAGE = "H2 specific error message";
    
    private TranslatedH2Exception e;
    
    @Before
    public void setup() {
        e = new TranslatedH2Exception(ERROR_CODE, ERROR, MESSAGE);
    }
        
    /**
     * Test of getErrorCode method, of class TranslatedH2Exception.
     */
    @Test
    public void testGetErrorCode() {
        assertThat(e.getErrorCode(), is(ERROR_CODE));
    }

    /**
     * Test of getError method, of class TranslatedH2Exception.
     */
    @Test
    public void testGetError() {
        assertThat(e.getError(), is(ERROR));
    }
    
    @Test
    public void testGetMessage() {
        assertThat(e.getMessage(), is(MESSAGE));
    }    
}
