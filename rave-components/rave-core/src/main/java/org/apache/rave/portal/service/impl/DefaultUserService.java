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

import org.apache.rave.portal.model.Person;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.util.SearchResult;
import org.apache.rave.portal.repository.UserRepository;
import org.apache.rave.portal.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Service(value = "userService")
public class DefaultUserService implements UserService {
    private static final Logger log = LoggerFactory.getLogger(DefaultUserService.class);

    private final UserRepository userRepository;

    @Autowired
    public DefaultUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        User user = userRepository.get(userId);
        if (user == null) {
            return;
        }
        userRepository.removeUser(user);
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
}