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

/**
 * Result context for RPC operations for a given type
 */
public class RpcResult<T> {

    public static enum ErrorCode {
        NO_ERROR,
        INVALID_PARAMS,
        INTERNAL_ERROR
    }

    private boolean error;
    private String errorMessage;
    private ErrorCode errorCode;
    private T result;

    public RpcResult(boolean error) {
        this.error = error;
        setDefaultCode(error);
    }

    public RpcResult(boolean error, T result) {
        this.error = error;
        this.result = result;
        setDefaultCode(error);
    }

    public RpcResult(boolean error, String errorMessage, ErrorCode errorCode) {
        this.error = error;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public RpcResult(boolean error, String errorMessage, ErrorCode errorCode, T result) {
        this.error = error;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.result = result;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    private void setDefaultCode(boolean error) {
        errorCode = error ? ErrorCode.INTERNAL_ERROR : ErrorCode.NO_ERROR;
    }

}
