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

import org.apache.rave.portal.model.RegionWidget;

/**
 * Defines an RPC operation that can be executed to return a given result
 */
public abstract class RpcOperation<T> {
    public static enum Type {
        MOVE,
        DELETE
    }

    /**
     *
     * @return
     */
    public RpcResult<T> getResult(){
        RpcResult<T> result;
        try {
            T subject = execute();
            result = new RpcResult<T>(false, subject);
        } catch (IllegalArgumentException e) {
            result = new RpcResult<T>(true, e.getMessage(), RpcResult.ErrorCode.INVALID_PARAMS);
        } catch (Exception e) {
            result = new RpcResult<T>(true, e.getMessage(), RpcResult.ErrorCode.INTERNAL_ERROR);
        }
        return result;
    }

    /**
     *
     * @return
     */
    public abstract T execute();
}
