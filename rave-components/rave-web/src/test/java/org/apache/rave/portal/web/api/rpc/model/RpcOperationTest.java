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

package org.apache.rave.portal.web.api.rpc.model;

import org.apache.rave.exception.DuplicateItemException;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class RpcOperationTest {

    private static final String BOO = "BOO";

    @Test
    public void execute_valid() {
        RpcOperation<String> valid = new RpcOperation<String>() {
            @Override
            public String execute() {
                return BOO;
            }
        };

        RpcResult<String> result = valid.getResult();
        assertThat(result.getResult(), is(equalTo(BOO)));
        assertThat(result.getErrorCode(), is(equalTo(RpcResult.ErrorCode.NO_ERROR)));
    }
    @Test
    public void execute_invalidArgs() {
        RpcOperation<String> valid = new RpcOperation<String>() {
            @Override
            public String execute() {
                throw new IllegalArgumentException();
            }
        };

        RpcResult<String> result = valid.getResult();
        assertThat(result.getResult(), is(nullValue()));
        assertThat(result.getErrorCode(), is(equalTo(RpcResult.ErrorCode.INVALID_PARAMS)));
    }
    @Test
    public void execute_internalError() {
        RpcOperation<String> valid = new RpcOperation<String>() {
            @Override
            public String execute() {
                throw new RuntimeException("Illegal Error");
            }
        };

        RpcResult<String> result = valid.getResult();
        assertThat(result.getResult(), is(nullValue()));
        assertThat(result.getErrorCode(), is(equalTo(RpcResult.ErrorCode.INTERNAL_ERROR)));
    }
    @Test
    public void execute_duplicateItemException() {
        RpcOperation<String> valid = new RpcOperation<String>() {
            @Override
            public String execute() {
                throw new DuplicateItemException("Duplicate Item Error");
            }
        };

        RpcResult<String> result = valid.getResult();
        assertThat(result.getResult(), is(nullValue()));
        assertThat(result.getErrorCode(), is(equalTo(RpcResult.ErrorCode.DUPLICATE_ITEM)));
    }    
}
