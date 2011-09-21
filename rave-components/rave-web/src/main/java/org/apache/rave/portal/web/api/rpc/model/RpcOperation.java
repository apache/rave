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

/**
 * Defines an RPC operation that can be executed to return a given result
 */
public abstract class RpcOperation<T> {

    /**
     * Wrapped execution of a given execute method defined by the subclass.  Provides generic mapping of exceptions to
     * RPC results
     *
     * @return valid RpcResult object for the type
     */
    public RpcResult<T> getResult(){
        RpcResult<T> result;
        try {
            T subject = execute();
            result = new RpcResult<T>(false, subject);
        } catch (IllegalArgumentException e) {
            result = createRpcResultError(e, RpcResult.ErrorCode.INVALID_PARAMS);
        } catch (DuplicateItemException e) {
            result = createRpcResultError(e, RpcResult.ErrorCode.DUPLICATE_ITEM);
        } catch (Exception e) {
            result = createRpcResultError(e, RpcResult.ErrorCode.INTERNAL_ERROR);            
        }
        return result;
    }

    /**
     * The method to be overridden that defines the RPC action being performed.
     *
     * @return the result of the RPC operation
     */
    public abstract T execute();
        
    private RpcResult<T> createRpcResultError(Exception e, RpcResult.ErrorCode errorCode) {      
        return new RpcResult<T>(true, e.getMessage(), errorCode);
    }
}