/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.portal.util;

/**
 * Environment variables for OpenSocial calls from the portal (to Shindig)
 */
public class OpenSocialEnvironment {
    /**
     * Protocol for the opensocial engine (http, https)
     */
    private String engineProtocol;
    /**
     * Root location of the opensocial engine (localhost:8080, www.example.com)
     */
    private String engineRoot;
    /**
     * Path after the root for gadgets (gadgets)
     */
    private String engineGadgetPath;


    public void setEngineProtocol(String engineProtocol) {
        this.engineProtocol = engineProtocol;
    }

    public String getEngineProtocol() {
        return engineProtocol;
    }

    public void setEngineRoot(String engineRoot) {
        this.engineRoot = engineRoot;
    }

    public String getEngineRoot() {
        return engineRoot;
    }

    public void setEngineGadgetPath(String engineGadgetPath) {
        this.engineGadgetPath = engineGadgetPath;
    }

    public String getEngineGadgetPath() {
        return engineGadgetPath;
    }
}
