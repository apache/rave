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

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaFactory;
import net.tanesha.recaptcha.ReCaptchaResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.rave.portal.service.CaptchaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Implementation of ReCaptcha captcha service
 */
@Service
public class ReCaptchaService implements CaptchaService {

    @SuppressWarnings("unused")
    private static Logger log = LoggerFactory.getLogger(ReCaptchaService.class);

    public static final String PARAM_CAPTCHA_RESPONSE = "recaptcha_response_field";
    public static final String PARAM_CAPTCHA_CHALLENGE = "recaptcha_challenge_field";


    private String invalidConfigurationMessage;
    private boolean captchaEnabled;
    private String publicKey;
    private boolean createNoScript;
    private String privateKey;


    @Autowired
    public ReCaptchaService(@Value("${portal.captcha.enabled}") boolean captchaEnabled,
                            @Value("${portal.captcha.key.public}") String publicKey,
                            @Value("${portal.captcha.key.private}") String privateKey,
                            @Value("${portal.captcha.usenoscript}") boolean createNoScript,
                            @Value("${portal.captcha.invalid.configuration}") String invalidConfigurationMessage
    ) {
        this.captchaEnabled = captchaEnabled;
        this.publicKey = publicKey;
        this.createNoScript = createNoScript;
        this.privateKey = privateKey;
        this.invalidConfigurationMessage = invalidConfigurationMessage;
    }

    @Override
    public boolean isValid(HttpServletRequest request) {
        log.debug("ReCaptcha enabled:  {}", captchaEnabled);
        if (!captchaEnabled) {
            return true;
        }
        if (StringUtils.isBlank(privateKey) || StringUtils.isBlank(publicKey)) {
            log.error("ReCaptcha service is enabled, however, private or public keys are not defined.");
            return true;
        }

        boolean secure = request.isSecure();
        ReCaptcha captcha;
        if (secure) {
            captcha = ReCaptchaFactory.newSecureReCaptcha(publicKey, privateKey, createNoScript);
        } else {
            captcha = ReCaptchaFactory.newReCaptcha(publicKey, privateKey, createNoScript);
        }
        String response = request.getParameter(PARAM_CAPTCHA_RESPONSE);
        String challenge = request.getParameter(PARAM_CAPTCHA_CHALLENGE);
        String remoteAddress = request.getRemoteAddr();
        // validate:
        ReCaptchaResponse captchaResponse = captcha.checkAnswer(remoteAddress, challenge, response);
        boolean valid = captchaResponse.isValid();
        if (valid) {
            return true;
        }
        log.warn("Invalid captcha response:  {}", captchaResponse.getErrorMessage());
        return false;

    }

    @Override
    public String createHtml(HttpServletRequest request) {
        if (captchaEnabled) {
            if (StringUtils.isBlank(privateKey) || StringUtils.isBlank(publicKey)) {
                return invalidConfigurationMessage;
            }
            boolean secure = request.isSecure();

            ReCaptcha captcha;
            if (secure) {
                captcha = ReCaptchaFactory.newSecureReCaptcha(publicKey, privateKey, createNoScript);
            } else {
                captcha = ReCaptchaFactory.newReCaptcha(publicKey, privateKey, createNoScript);
            }

            return captcha.createRecaptchaHtml(null, null);
        }
        return "";
    }
}
