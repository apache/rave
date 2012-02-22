/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.portal.service.impl;


import org.apache.commons.lang.StringUtils;
import org.apache.rave.portal.model.NewUser;

import org.apache.rave.portal.model.PageType;

import org.apache.rave.portal.model.Person;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.util.SearchResult;
import org.apache.rave.portal.repository.*;
import org.apache.rave.portal.service.EmailService;
import org.apache.rave.portal.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 *
 */
@Service(value = "userService")
public class DefaultUserService implements UserService {
    private static final Logger log = LoggerFactory.getLogger(DefaultUserService.class);

    private final UserRepository userRepository;
    private final PageRepository pageRepository;
    private final WidgetRatingRepository widgetRatingRepository;
    private final WidgetCommentRepository widgetCommentRepository;
    private final WidgetRepository widgetRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Value("${portal.mail.passwordservice.subject}")
    private String passwordReminderSubject;

    @Value("${portal.mail.passwordservice.template}")
    private String passwordReminderTemplate;

    @Value("${portal.mail.username.subject}")
    private String userNameReminderSubject;

    @Value("${portal.mail.username.template}")
    private String userNameReminderTemplate;

    @Value("${portal.mail.service.baseurl}")
    private String baseUrl;

    @Autowired
    public DefaultUserService(UserRepository userRepository,
                              PageRepository pageRepository,
                              WidgetRatingRepository widgetRatingRepository,
                              WidgetCommentRepository widgetCommentRepository,
                              WidgetRepository widgetRepository) {
        this.userRepository = userRepository;
        this.pageRepository = pageRepository;
        this.widgetRatingRepository = widgetRatingRepository;
        this.widgetCommentRepository = widgetCommentRepository;
        this.widgetRepository = widgetRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        log.debug("loadUserByUsername called with: {}", username);
        final User user = userRepository.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User with username '" + username + "' was not found!");
        }
        return user;
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        } else {
            throw new SecurityException("Could not get the authenticated user!");
        }
    }

    @Override
    public void setAuthenticatedUser(long userId) {
        final User user = userRepository.get(userId);
        if (user == null) {
            throw new UsernameNotFoundException("User with id '" + userId + "' was not found!");
        }
        SecurityContext securityContext = createContext(user);
        SecurityContextHolder.setContext(securityContext);
    }

    @Override
    public void clearAuthenticatedUser() {
        SecurityContextHolder.clearContext();
    }

    private SecurityContext createContext(final User user) {
        SecurityContext securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(new AbstractAuthenticationToken(user.getAuthorities()) {
            private static final long serialVersionUID = 1L;

            @Override
            public Object getCredentials() {
                return "N/A";
            }

            @Override
            public Object getPrincipal() {
                return user;
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }
        });
        return securityContext;
    }

    @Override
    @Transactional
    public void registerNewUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.get(id);
    }

    @Override
    public User getUserByUsername(String userName) {
        return userRepository.getByUsername(userName);
    }

    @Override
    public User getUserByEmail(String userEmail) {
        return userRepository.getByUserEmail(userEmail);
    }

    @Override
    public void updateUserProfile(User user) {
        userRepository.save(user);
    }

    @Override
    public SearchResult<User> getLimitedListOfUsers(int offset, int pageSize) {
        final int count = userRepository.getCountAll();
        final List<User> users = userRepository.getLimitedList(offset, pageSize);
        final SearchResult<User> searchResult = new SearchResult<User>(users, count);
        searchResult.setOffset(offset);
        searchResult.setPageSize(pageSize);
        return searchResult;
    }

    @Override
    public SearchResult<User> getUsersByFreeTextSearch(String searchTerm, int offset, int pageSize) {
        final int count = userRepository.getCountByUsernameOrEmail(searchTerm);
        final List<User> users = userRepository.findByUsernameOrEmail(searchTerm, offset, pageSize);
        final SearchResult<User> searchResult = new SearchResult<User>(users, count);
        searchResult.setOffset(offset);
        searchResult.setPageSize(pageSize);
        return searchResult;
    }

    @Override
    @Transactional
    // TODO RAVE-300: add security check that is is called by admin or the user itself
    public void deleteUser(Long userId) {
        log.info("about to delete userId: " + userId);
        User user = userRepository.get(userId);
        if (user == null) {
            log.warn("unable to find userId " + userId + " to delete");
            return;
        }

        final String username = user.getUsername();

        // delete all User type pages
        int numDeletedPages = pageRepository.deletePages(userId, PageType.USER);
        // delete all the widget comments
        int numWidgetComments = widgetCommentRepository.deleteAll(userId);
        // delete all the widget ratings
        int numWidgetRatings = widgetRatingRepository.deleteAll(userId);
        // unassign the user from any widgets where they were the owner
        int numWidgetsOwned = widgetRepository.unassignWidgetOwner(userId);
        // finally delete the user
        userRepository.delete(user);
        log.info("Deleted user [" + userId + ',' + username + "] - numPages: " + numDeletedPages +
                 ", numWidgetComments: " + numWidgetComments + ", numWidgetRatings: " + numWidgetRatings +
                 ", numWidgetsOwned: " + numWidgetsOwned);
    }

    @Override
    public List<Person> getAllByAddedWidget(long widgetId) {
        List<Person> persons = new ArrayList<Person>();
        List<User> users = userRepository.getAllByAddedWidget(widgetId);
        for (User u : users) {
            persons.add(u.toPerson());
        }
        return persons;
    }

    @Override
    public void updatePassword(NewUser newUser) {
        log.debug("Changing password  for user {}", newUser);
        User user = userRepository.getByForgotPasswordHash(newUser.getForgotPasswordHash());
        if (user == null) {
            throw new IllegalArgumentException("Could not find user for forgotPasswordHash " + newUser.getForgotPasswordHash());
        }
        String saltedHashedPassword = passwordEncoder.encode(newUser.getPassword());
        user.setPassword(saltedHashedPassword);
        // reset password hash and time
        user.setForgotPasswordHash(null);
        user.setForgotPasswordTime(null);
        userRepository.save(user);

    }


    @Override
    public void sendUserNameReminder(NewUser newUser) {
        log.debug("Calling send username  {}", newUser);
        User user = userRepository.getByUserEmail(newUser.getEmail());
        if (user == null) {
            throw new IllegalArgumentException("Could not find user for email " + newUser.getEmail());
        }
        String to = user.getUsername() + " <" + user.getEmail() + '>';
        Map<String, Object> templateData = new HashMap<String, Object>();
        templateData.put("user", user);
        emailService.sendEmail(to, userNameReminderSubject, userNameReminderTemplate, templateData);

    }


    @Override
    public void sendPasswordReminder(NewUser newUser) {
        log.debug("Calling send password change link for user {}", newUser);
        User user = userRepository.getByUserEmail(newUser.getEmail());
        if (user == null) {
            throw new IllegalArgumentException("Could not find user for email " + newUser.getEmail());
        }
        // create user hash:
        String input = user.getEmail() + user.getUsername() + String.valueOf(user.getEntityId()) + System.nanoTime();
        // hash needs to be URL friendly:
        String safeString = new String(Base64.encode(passwordEncoder.encode(input).getBytes()));
        String  hashedInput = safeString.replaceAll("[/=]", "A");
        user.setForgotPasswordHash(hashedInput);
        user.setForgotPasswordTime(Calendar.getInstance().getTime());
        userRepository.save(user);
        String to = user.getUsername() + " <" + user.getEmail() + '>';
        Map<String, Object> templateData = new HashMap<String, Object>();
        templateData.put("user", user);
        templateData.put("reminderUrl", baseUrl + hashedInput);
        emailService.sendEmail(to, passwordReminderSubject, passwordReminderTemplate, templateData);
    }


    @Override
    public boolean isValidReminderRequest(String forgotPasswordHash, int nrOfMinutesValid) {
        if (StringUtils.isBlank(forgotPasswordHash)) {
            return false;
        }

        User userForHash = userRepository.getByForgotPasswordHash(forgotPasswordHash);
        if (userForHash == null) {
            return false;
        }
        Date requestTime = userForHash.getForgotPasswordTime();
        Calendar expiredDate = Calendar.getInstance();
        expiredDate.add(Calendar.MINUTE, nrOfMinutesValid);

        if (requestTime == null || requestTime.after(expiredDate.getTime())) {
            // reset,  this is invalid state
            userForHash.setForgotPasswordHash(null);
            userForHash.setForgotPasswordTime(null);
            userRepository.save(userForHash);
            return false;
        }
        return true;
    }

}