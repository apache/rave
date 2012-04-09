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

package org.apache.rave.portal.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.rave.exception.EmailException;
import org.apache.rave.portal.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * Default implementation of EmailService
 *
 * @version "$Id$"
 */
@Service
public class DefaultEmailService implements EmailService {

    @Autowired
    private SimpleMailMessage emailServiceMailMessage;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private FreeMarkerConfigurationFactoryBean freemarkerMailConfiguration;


    private static Logger log = LoggerFactory.getLogger(DefaultEmailService.class);

    @Override
    public void sendEmail(String to, String subject, String templateName, Map<String, Object> templateData) {
        // create a copy of mail message:
        SimpleMailMessage message = new SimpleMailMessage(emailServiceMailMessage);
        message.setSubject(subject);
        message.setTo(to);
        // set body text:
        Configuration configuration = freemarkerMailConfiguration.getObject();
        String text = parseTemplate(configuration, templateData, templateName);
        message.setText(text);
        // send email:
        mailSender.send(message);
    }

    private String parseTemplate(Configuration configuration, Map<String, Object> templateData, String templateName) {

        try {
            return FreeMarkerTemplateUtils.processTemplateIntoString(configuration.getTemplate(templateName), templateData);
        } catch (IOException e) {
            if (log.isDebugEnabled()) {
                log.error("Error parsing email template" + templateName, e);
            }
            throw new EmailException("Username reminder: error parsing email template" + templateName);
        } catch (TemplateException e) {
            if (log.isDebugEnabled()) {
                log.error("failed to render email template " + templateName, e);
            }
            throw new EmailException("Username reminder: error rendering email template: " + templateName);
        }
    }
    
    
    
}
