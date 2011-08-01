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
//TODO is this the right package name convention?
package org.apache.rave.portal.web.validator;

import java.util.ArrayList;
import java.util.List;

import org.apache.rave.portal.model.NewUser;
import org.apache.rave.portal.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

public class NewAccountValidator implements Validator {

	 protected final Logger logger=LoggerFactory.getLogger(getClass());
	 
	 private UserService userService;

	 @Autowired
	 public NewAccountValidator(UserService userService) {
		 this.userService = userService;
	 }
	 
	 public boolean supports(Class aClass){
		  return NewAccountValidator.class.equals(aClass);
	 }
	 
	 public void validate(Object obj, Errors errors){
		  logger.debug("Validator called");
		  NewUser newUser = (NewUser) obj;
		  List<ObjectError> errorList = new ArrayList<ObjectError>();
		  
		  //check if the username is null
		  if(newUser.getUsername() == "") {
			  errors.rejectValue("username", "username.required");
			  logger.error("Username required");
		  }
		  
		  //check if username length is less than 2
		  else if(newUser.getUsername().length() < 2) {
			  errors.rejectValue("username", "username.invalid.length");
			  logger.error("Username must be atleast 2 characters long");
		  }
		  
		  //check if username is already in use
		  
		  else if(userService.getUserByUsername(newUser.getUsername()) != null) {
			  errors.rejectValue("username", "username.exits");
			  logger.error("Username already exists");
		  }
		  
		  
		  //check if the password is null
		  if(newUser.getPassword() == "") {
			  errors.rejectValue("password", "password.required");
			  logger.error("Password required");
		  }
		  
		  //check if the password length is less than 4
		  else if(newUser.getPassword().length() < 4) {
			  errors.rejectValue("password", "password.invalid.lenght");
			  logger.error("Password must be atleast 4 characters long");
		  }
		  
		  //check if the confirm password is null
		  if(newUser.getConfirmPassword() == "") {
			  errors.rejectValue("confirmPassword", "confirmPassword.required");
			  logger.error("Confirm Password required");
		  }
		  
		  //check if the confirm password matches the previous entered password
		  if(newUser.getConfirmPassword().length() != newUser.getPassword().length() || newUser.getConfirmPassword().compareTo(newUser.getPassword()) != 0) {
			  errors.rejectValue("confirmPassword", "confirmPassword.mismatch");
			  logger.error("Password mismatch");
		  }

		  if(errors.hasErrors()){
			  errorList = errors.getAllErrors();
			  for (ObjectError error : errorList) {
				  logger.error("Validation error: " + error.toString());
			  }
		  }
		  else {
			  logger.debug("Validation successful");
		  }
	 }
}