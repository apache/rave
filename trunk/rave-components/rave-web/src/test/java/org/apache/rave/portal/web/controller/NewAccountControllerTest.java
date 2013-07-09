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

package org.apache.rave.portal.web.controller;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.apache.rave.model.User;
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.service.CaptchaService;
import org.apache.rave.portal.service.NewAccountService;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.service.impl.ReCaptchaService;
import org.apache.rave.portal.web.model.UserForm;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.apache.rave.portal.web.validator.NewAccountValidator;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * This is a test class for NewAccountController, which is used to make new user accounts through
 * a web form.
 */
public class NewAccountControllerTest {
	 //Tests need to be implemented.
	private NewAccountController newAccountController;
  private MockHttpServletRequest request;
    private RedirectAttributes redirectAttributes;
	private UserService userService;

	@Before
	public void setup() {
    NewAccountService newAccountService = createNiceMock(NewAccountService.class);
		userService = createNiceMock(UserService.class);
    request = new MockHttpServletRequest();
    redirectAttributes = createNiceMock(RedirectAttributes.class);
    NewAccountValidator newAccountValidator = new NewAccountValidator(userService);
    CaptchaService captchaService = new ReCaptchaService(false, null, null, false, "error message");
		newAccountController = new NewAccountController(newAccountService, newAccountValidator, captchaService);
	}

	@Test
	public void setUpForm_ShouldAddAttributeForUser() {
		final ModelMap model = new ModelMap();
		String User = ModelKeys.NEW_USER;
		newAccountController.setUpForm(model, request);

		//assert that the model is not null
		assertThat(model, CoreMatchers.notNullValue());

		//assert that the model size is two
		assertThat(model.size(), CoreMatchers.equalTo(2));

		//assert that the model does contain an attribute associated with User after setUpForm() is called
		assertThat(model.containsAttribute(User), CoreMatchers.equalTo(true));

		//assert that the model does not contain new user as null
		assertThat(model.get(User), CoreMatchers.notNullValue());
	}

	@Test
	public void create_UsernameNotSpecified() {
		final Model model = createNiceMock(Model.class);
		final UserForm User = new UserForm();
		final BindingResult errors = createNiceMock(BindingResult.class);
		final String username = ""; //no username specified
		final String password = "password";
		final String confirmPassword = password;
		List<ObjectError> errorList = new ArrayList<ObjectError>();
		final ObjectError error = new ObjectError("username.required", "Username required");

		User.setUsername(username);
		User.setPassword(password);
		User.setConfirmPassword(confirmPassword);

		errorList.add(error);

		expect(errors.hasErrors()).andReturn(true).anyTimes();
		expect(errors.getAllErrors()).andReturn(errorList).anyTimes();
		replay(errors);

        replay(model);
		String result = newAccountController.create(User, errors, model, request, redirectAttributes);
		errorList = errors.getAllErrors();

		assertThat(errorList.size(), CoreMatchers.equalTo(1));
		assertThat(errorList.get(0).getDefaultMessage(), CoreMatchers.equalTo("Username required"));
		assertThat(result, CoreMatchers.equalTo(ViewNames.NEW_ACCOUNT));
	}

	@Test
	public void create_UsernameAlreadyExists() {
		final Model model = createNiceMock(Model.class);
		final UserForm User = new UserForm();
		final BindingResult errors = createNiceMock(BindingResult.class);
		final String username = "canonical"; //specified username already exists in database
		final String password = "password";
		final String confirmPassword = password;
		final org.apache.rave.model.User existingUser = new UserImpl();
		List<ObjectError> errorList = new ArrayList<ObjectError>();
		final ObjectError error = new ObjectError("username.exists", "Username already exists");

		User.setUsername(username);
		User.setPassword(password);
		User.setConfirmPassword(confirmPassword);

		existingUser.setUsername(username);
		existingUser.setPassword(password);

		errorList.add(error);


		expect(errors.hasErrors()).andReturn(true).anyTimes();
		expect(errors.getAllErrors()).andReturn(errorList).anyTimes();
		replay(errors);

		expect(userService.getUserByUsername(username)).andReturn(existingUser).anyTimes();
		replay(userService);
		replay(model);
		String result = newAccountController.create(User, errors, model, request, redirectAttributes);
		errorList = errors.getAllErrors();

		assertThat(errorList.size(), CoreMatchers.equalTo(1));
		assertThat(errorList.get(0).getDefaultMessage(), CoreMatchers.equalTo("Username already exists"));
		assertThat(result, CoreMatchers.equalTo(ViewNames.NEW_ACCOUNT));
	}

	@Test
	public void create_InvalidUsernameLength() {
		final Model model = createNiceMock(Model.class);
		final UserForm User = new UserForm();
		final BindingResult errors = createNiceMock(BindingResult.class);
		final String username = "u"; //username length less than 2 characters
		final String password = "password";
		final String confirmPassword = password;
		List<ObjectError> errorList = new ArrayList<ObjectError>();
		final ObjectError error = new ObjectError("username.invalid.length", "Username must be atleast 2 characters long");

		User.setUsername(username);
		User.setPassword(password);
		User.setConfirmPassword(confirmPassword);

		errorList.add(error);

		expect(errors.hasErrors()).andReturn(true).anyTimes();
		expect(errors.getAllErrors()).andReturn(errorList).anyTimes();
		replay(errors);
		replay(model);

		String result = newAccountController.create(User, errors, model, request, redirectAttributes);
		errorList = errors.getAllErrors();

		assertThat(errorList.size(), CoreMatchers.equalTo(1));
		assertThat(errorList.get(0).getDefaultMessage(), CoreMatchers.equalTo("Username must be atleast 2 characters long"));
		assertThat(result, CoreMatchers.equalTo(ViewNames.NEW_ACCOUNT));
	}

	@Test
	public void create_PasswordNotSpecified() {
		final Model model = createNiceMock(Model.class);
		final UserForm User = new UserForm();
		final BindingResult errors = createNiceMock(BindingResult.class);
		final String username = "username";
		final String password = ""; //password not specified
		final String confirmPassword = password;
		List<ObjectError> errorList = new ArrayList<ObjectError>();

		User.setUsername(username);
		User.setPassword(password);
		User.setConfirmPassword(confirmPassword);

		errorList.add(new ObjectError("password.required", "Password required"));
		errorList.add(new ObjectError("confirmPassword.required", "Confirm password required"));

		expect(errors.hasErrors()).andReturn(true).anyTimes();
		expect(errors.getAllErrors()).andReturn(errorList).anyTimes();
		replay(errors);
		replay(model);
		String result = newAccountController.create(User, errors, model, request, redirectAttributes);
		errorList = errors.getAllErrors();

		assertThat(errorList.size(), CoreMatchers.equalTo(2));
		assertThat(errorList.get(0).getDefaultMessage(), CoreMatchers.equalTo("Password required"));
		assertThat(errorList.get(1).getDefaultMessage(), CoreMatchers.equalTo("Confirm password required"));
		assertThat(result, CoreMatchers.equalTo(ViewNames.NEW_ACCOUNT));
	}

	@Test
	public void create_ConfirmPasswordNotSpecified() {
		final Model model = createNiceMock(Model.class);
		final UserForm User = new UserForm();
		final BindingResult errors = createNiceMock(BindingResult.class);
		final String username = "usename";
		final String password = "pasword";
		final String confirmPassword = ""; //confirm password not specified
		List<ObjectError> errorList = new ArrayList<ObjectError>();

		User.setUsername(username);
		User.setPassword(password);
		User.setConfirmPassword(confirmPassword);

		errorList.add(new ObjectError("confirmPassword.required", "Confirm password required"));

		expect(errors.hasErrors()).andReturn(true).anyTimes();
		expect(errors.getAllErrors()).andReturn(errorList).anyTimes();
		replay(errors);

        replay(model);
		String result = newAccountController.create(User, errors, model, request, redirectAttributes);
		errorList = errors.getAllErrors();

		assertThat(errorList.size(), CoreMatchers.equalTo(1));
		assertThat(errorList.get(0).getDefaultMessage(), CoreMatchers.equalTo("Confirm password required"));
		assertThat(result, CoreMatchers.equalTo(ViewNames.NEW_ACCOUNT));
	}

	@Test
	public void create_InvalidPasswordLength() {
		final Model model = createNiceMock(Model.class);
		final UserForm User = new UserForm();
		final BindingResult errors = createNiceMock(BindingResult.class);
		final String username = "usename";
		final String password = "pas"; //invalid length password
		final String confirmPassword = password;
		List<ObjectError> errorList = new ArrayList<ObjectError>();

		User.setUsername(username);
		User.setPassword(password);
		User.setConfirmPassword(confirmPassword);

		errorList.add(new ObjectError("password.invalid.length", "Password must be atleast 4 characters long"));

		expect(errors.hasErrors()).andReturn(true).anyTimes();
		expect(errors.getAllErrors()).andReturn(errorList).anyTimes();
		replay(errors);
		replay(model);
		String result = newAccountController.create(User, errors, model, request, redirectAttributes);
		errorList = errors.getAllErrors();

		assertThat(errorList.size(), CoreMatchers.equalTo(1));
		assertThat(errorList.get(0).getDefaultMessage(), CoreMatchers.equalTo("Password must be atleast 4 characters long"));
		assertThat(result, CoreMatchers.equalTo(ViewNames.NEW_ACCOUNT));
	}

	@Test
	public void create_PasswordMismatchCaseOne() {
		final Model model = createNiceMock(Model.class);
		final UserForm User = new UserForm();
		final BindingResult errors = createNiceMock(BindingResult.class);
		final String username = "username";
		final String password = "password";
		final String confirmPassword = "passwor"; //confirm password not of same length as password
		List<ObjectError> errorList = new ArrayList<ObjectError>();

		User.setUsername(username);
		User.setPassword(password);
		User.setConfirmPassword(confirmPassword);

		errorList.add(new ObjectError("confirmPassword.mismatch", "Password mismatch"));

		expect(errors.hasErrors()).andReturn(true).anyTimes();
		expect(errors.getAllErrors()).andReturn(errorList).anyTimes();
		replay(errors);
		replay(model);
		String result = new String(newAccountController.create(User, errors, model, request, redirectAttributes));
		errorList = errors.getAllErrors();

		assertThat(errorList.size(), CoreMatchers.equalTo(1));
		assertThat(errorList.get(0).getDefaultMessage(), CoreMatchers.equalTo("Password mismatch"));
		assertThat(result, CoreMatchers.equalTo(ViewNames.NEW_ACCOUNT));
	}

	@Test
	public void create_PasswordMismatchCaseTwo() {
		final Model model = createNiceMock(Model.class);
		final UserForm User = new UserForm();
		final BindingResult errors = createNiceMock(BindingResult.class);
		final String username = "username";
		final String password = "password";
		final String confirmPassword = "passwodr"; //confirm password mistyped
		List<ObjectError> errorList = new ArrayList<ObjectError>();

		User.setUsername(username);
		User.setPassword(password);
		User.setConfirmPassword(confirmPassword);

		errorList.add(new ObjectError("confirmPassword.mismatch", "Password mismatch"));

		expect(errors.hasErrors()).andReturn(true).anyTimes();
		expect(errors.getAllErrors()).andReturn(errorList).anyTimes();
		replay(errors);
        replay(model);
		String result = newAccountController.create(User, errors, model, request, redirectAttributes);
		errorList = errors.getAllErrors();

		assertThat(errorList.size(), CoreMatchers.equalTo(1));
		assertThat(errorList.get(0).getDefaultMessage(), CoreMatchers.equalTo("Password mismatch"));
		assertThat(result, CoreMatchers.equalTo(ViewNames.NEW_ACCOUNT));
	}

	@Test
	public void create_BlankFormSubmitted() {
		final Model model = createNiceMock(Model.class);
		final UserForm User = new UserForm();
		final BindingResult errors = createNiceMock(BindingResult.class);
		final String username = ""; //Username not specified
		final String password = ""; //Password not specified
		final String confirmPassword = ""; //Confirm password not specified
		List<ObjectError> errorList = new ArrayList<ObjectError>();

		User.setUsername(username);
		User.setPassword(password);
		User.setConfirmPassword(confirmPassword);

		errorList.add(new ObjectError("username.required", "Username required"));
		errorList.add(new ObjectError("password.required", "Password required"));
		errorList.add(new ObjectError("confirmPassword.required", "Confirm password required"));

		expect(errors.hasErrors()).andReturn(true).anyTimes();
		expect(errors.getAllErrors()).andReturn(errorList).anyTimes();
		replay(errors);

        replay(model);
		String result = newAccountController.create(User, errors, model, request, redirectAttributes);
		errorList = errors.getAllErrors();

		assertThat(errorList.size(), CoreMatchers.equalTo(3));
		assertThat(errorList.get(0).getDefaultMessage(), CoreMatchers.equalTo("Username required"));
		assertThat(errorList.get(1).getDefaultMessage(), CoreMatchers.equalTo("Password required"));
		assertThat(errorList.get(2).getDefaultMessage(), CoreMatchers.equalTo("Confirm password required"));
		assertThat(result, CoreMatchers.equalTo(ViewNames.NEW_ACCOUNT));
	}

	@Test
	public void create_ValidFormSubmitted() {
		final Model model = createNiceMock(Model.class);
		final UserForm User = new UserForm();
		final BindingResult errors = createNiceMock(BindingResult.class);
		final String username = "username"; //Username not specified
		final String password = "password"; //Password not specified
		final String confirmPassword = password; //Confirm password not specified
		List<ObjectError> errorList = new ArrayList<ObjectError>();

		User.setUsername(username);
		User.setPassword(password);
		User.setConfirmPassword(confirmPassword);

		expect(errors.hasErrors()).andReturn(false).anyTimes();
		expect(errors.getAllErrors()).andReturn(errorList).anyTimes();
		replay(errors);

		String result = newAccountController.create(User, errors, model, request, redirectAttributes);
		errorList = errors.getAllErrors();

		assertThat(errorList.size(), CoreMatchers.equalTo(0));
		assertThat(result, CoreMatchers.equalTo(ViewNames.REDIRECT_LOGIN));
	}
}