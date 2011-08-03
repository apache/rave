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

import org.apache.rave.portal.model.User;
import org.apache.rave.portal.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

public class UserProfileValidator implements Validator {

	 protected final Logger logger=LoggerFactory.getLogger(getClass());
	 
	 private UserService userService;

	 @Autowired
	 public UserProfileValidator(UserService userService) {
		 this.userService = userService;
	 }
	 
	 public boolean supports(Class aClass){
		  return UserProfileValidator.class.equals(aClass);
	 }
	 
	 public void validate(Object obj, Errors errors){
		  logger.debug("Validator called");
		  User user = (User) obj;
		  List<ObjectError> errorList = new ArrayList<ObjectError>();
		  
		  //check if the password is null
		  if(user.getPassword() == "") {
			  errors.rejectValue("password", "password.required");
			  logger.error("Password required");
		  }
		  
		  //check if the password length is less than 4
		  else if(user.getPassword().length() < 4) {
			  errors.rejectValue("password", "password.invalid.length");
			  logger.error("Password must be at least 4 characters long");
		  }
		  
		  //check if the confirm password is null
		  if(user.getConfirmPassword() == "") {
			  errors.rejectValue("confirmPassword", "confirmPassword.required");
			  logger.error("Confirm Password required");
		  }
		  
		  //check if the confirm password matches the previous entered password
		  if(user.getConfirmPassword().length() != user.getPassword().length() || user.getConfirmPassword().compareTo(user.getPassword()) != 0) {
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