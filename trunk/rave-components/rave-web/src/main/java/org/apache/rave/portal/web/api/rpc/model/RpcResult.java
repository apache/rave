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
 * Generic RPC result context for RPC operations of the given type
 */
public class RpcResult<T> {

    /**
     * Error codes returned to the client for any errors encountered while performing the operation.
     */
    public static enum ErrorCode {
        NO_ERROR,
        INVALID_PARAMS,
        INTERNAL_ERROR,
        DUPLICATE_ITEM
    }

    private boolean error;
    private String errorMessage;
    private ErrorCode errorCode;
    private T result;

    /**
     * Constructs a new  instance and sets the default error code to the following:
     * <p/>
     * if error is true:
     *    INTERNAL_ERROR
     * else
     *    NO_ERROR
     *
     * @param error boolean representing whether an error occurred
     */
    public RpcResult(boolean error) {
        this.error = error;
        setDefaultCode(error);
    }

    /**
     * Constructs a new instance, sets a result and sets the default error code to the following:
     * <p/>
     * if error is true:
     *    INTERNAL_ERROR
     * else
     *    NO_ERROR
     *
     * @param error boolean representing whether an error occurred
     * @param result the result of the RPC operation
     */
    public RpcResult(boolean error, T result) {
        this.error = error;
        this.result = result;
        setDefaultCode(error);
    }

    /**
     * Constructs a new instance with the specified values
     * @param error  boolean representing whether an error occurred
     * @param errorMessage the error message to return to the caller
     * @param errorCode the error code to return to the caller
     */
    public RpcResult(boolean error, String errorMessage, ErrorCode errorCode) {
        this.error = error;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    /**
     * Constructs a new instance with the specified values
     * @param error  boolean representing whether an error occurred
     * @param errorMessage the error message to return to the caller
     * @param errorCode the error code to return to the caller
     * @param result the result of the RPC operation
     */
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
