/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.portal.service;

import java.util.Map;

/**
 * @version "$Id$"
 */
public interface EmailService {

    /**
     * Sends an email to specified recipient
     *
     * @param to           recipient e.g. {@code John Doe<joe.doe@example.com>}
     * @param subject      email subject
     * @param templateName name of the Freemarker template
     * @param templateData data provided to Freemarker template
     */
    void sendEmail(String to, String subject, String templateName, Map<String, Object> templateData);
}
