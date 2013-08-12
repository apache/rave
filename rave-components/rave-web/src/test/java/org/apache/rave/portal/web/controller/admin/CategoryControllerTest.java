/*
 * Copyright 2012 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.rave.portal.web.controller.admin;

import org.apache.rave.model.Category;
import org.apache.rave.model.User;
import org.apache.rave.portal.model.impl.CategoryImpl;
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.service.CategoryService;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.web.util.ModelKeys;
import org.apache.rave.portal.web.util.ViewNames;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.support.SessionStatus;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link CategoryController}
 */
public class CategoryControllerTest {

    private CategoryController controller;
    private UserService userService;
    private CategoryService categoryService;
    private String validToken;

    private static final String UPDATE = "update";
    private static final String DELETE = "delete";
    private static final String CREATE = "create";
    private static final String REFERRER_ID = "35";

    @Before
    public void setup() {
        userService = createMock(UserService.class);
        categoryService = createMock(CategoryService.class);
        controller = new CategoryController();
        validToken = AdminControllerUtil.generateSessionToken();
        controller.setUserService(userService);
        controller.setCategoryService(categoryService);
    }

    @Test
    public void getCategories_valid(){
        List<Category> categories = new ArrayList<Category>();
        expect(categoryService.getAllList()).andReturn(categories);
        replay(categoryService);

        Model model = new ExtendedModelMap();

        String viewName = controller.getCategories("",REFERRER_ID, model);
        verify(categoryService);

        assertEquals(ViewNames.ADMIN_CATEGORIES, viewName);
        assertEquals(categories, model.asMap().get("categories"));
        assertFalse(model.containsAttribute("actionresult"));
        assertTrue(model.containsAttribute("category"));
        assertEquals("Check that the category object available", new CategoryImpl(), model.asMap().get("category"));
        assertTrue(model.containsAttribute("topnav"));
        assertTrue(model.containsAttribute("tabs"));
        assertTrue("verify tokencheck", model.asMap().containsKey(ModelKeys.TOKENCHECK));
        assertThat((String) model.asMap().get(ModelKeys.REFERRING_PAGE_ID), is(equalTo(REFERRER_ID)));
    }

    @Test
    public void getCategory_valid_update() {
        List<Category> categories = new ArrayList<Category>();
        expect(categoryService.getAllList()).andReturn(categories);
        replay(categoryService);

        Model model = new ExtendedModelMap();

        String viewName = controller.getCategories(UPDATE,REFERRER_ID, model);
        verify(categoryService);

        assertEquals(ViewNames.ADMIN_CATEGORIES, viewName);
        assertEquals(categories, model.asMap().get("categories"));
        assertTrue(model.containsAttribute("actionresult"));
        assertEquals(UPDATE, model.asMap().get("actionresult"));
        assertTrue(model.containsAttribute("category"));
        assertEquals("Check that the category object available", new CategoryImpl(), model.asMap().get("category"));
        assertTrue(model.containsAttribute("topnav"));
        assertTrue(model.containsAttribute("tabs"));
        assertThat((String) model.asMap().get(ModelKeys.REFERRING_PAGE_ID), is(equalTo(REFERRER_ID)));
    }

    @Test
    public void getCategory_valid_delete(){
        List<Category> categories = new ArrayList<Category>();
        expect(categoryService.getAllList()).andReturn(categories);
        replay(categoryService);

        Model model = new ExtendedModelMap();

        String viewName = controller.getCategories(DELETE,REFERRER_ID, model);
        verify(categoryService);

        assertEquals(ViewNames.ADMIN_CATEGORIES, viewName);
        assertEquals(categories, model.asMap().get("categories"));
        assertTrue(model.containsAttribute("actionresult"));
        assertEquals(DELETE, model.asMap().get("actionresult"));
        assertTrue(model.containsAttribute("category"));
        assertEquals("Check that the category object available", new CategoryImpl(), model.asMap().get("category"));
        assertTrue(model.containsAttribute("topnav"));
        assertTrue(model.containsAttribute("tabs"));
        assertThat((String) model.asMap().get(ModelKeys.REFERRING_PAGE_ID), is(equalTo(REFERRER_ID)));
    }

    @Test
    public void getCategory_valid_create(){
        List<Category> categories = new ArrayList<Category>();
        expect(categoryService.getAllList()).andReturn(categories);
        replay(categoryService);

        Model model = new ExtendedModelMap();

        String viewName = controller.getCategories(CREATE,REFERRER_ID, model);
        verify(categoryService);

        assertEquals(ViewNames.ADMIN_CATEGORIES, viewName);
        assertEquals(categories, model.asMap().get("categories"));
        assertTrue(model.containsAttribute("actionresult"));
        assertEquals(CREATE, model.asMap().get("actionresult"));
        assertTrue(model.containsAttribute("category"));
        assertEquals("Check category object available", new CategoryImpl(), model.asMap().get("category"));
        assertTrue(model.containsAttribute("topnav"));
        assertTrue(model.containsAttribute("tabs"));
        assertThat((String) model.asMap().get(ModelKeys.REFERRING_PAGE_ID), is(equalTo(REFERRER_ID)));
    }

    @Test
    public void createCategory_valid(){
        Model model = new ExtendedModelMap();
        User user = new UserImpl();
        String categoryText = "Social";
        CategoryImpl category = new CategoryImpl();
        category.setText(categoryText);
        SessionStatus sessionStatus = createMock(SessionStatus.class);
        expect(userService.getAuthenticatedUser()).andReturn(user).once();
        expect(categoryService.create(categoryText, user)).andReturn(new CategoryImpl());
        sessionStatus.setComplete();
        expectLastCall();
        replay(userService, categoryService,sessionStatus);
        String view = controller.createCategory(category, validToken, validToken,REFERRER_ID, model, sessionStatus);
        assertEquals("ViewName match", "redirect:/app/admin/categories?action=create&referringPageId=" +REFERRER_ID, view);
        assertTrue("empty model", model.asMap().isEmpty());
        verify(userService, categoryService, sessionStatus);
    }

    @Test(expected = SecurityException.class)
    public void createCategory_invalidToken(){
        Model model = new ExtendedModelMap();
        String invalidToken =  AdminControllerUtil.generateSessionToken();
        User user = new UserImpl();
        String categoryText = "Social";
        CategoryImpl category = new CategoryImpl();
        category.setText(categoryText);
        SessionStatus sessionStatus = createMock(SessionStatus.class);
        expect(userService.getAuthenticatedUser()).andReturn(user).once();
        expect(categoryService.create(categoryText, user)).andReturn(new CategoryImpl());
        sessionStatus.setComplete();
        expectLastCall();
        replay(userService, categoryService,sessionStatus);
        String view = controller.createCategory(category, validToken, invalidToken,REFERRER_ID, model, sessionStatus);
        assertTrue("Test should catch exception and never hit this test", false);
    }

    @Test
    public void createCategory_invalidValidRequest_emptyText(){
        Model model = new ExtendedModelMap();
        User user = new UserImpl();
        String categoryText = "";
        CategoryImpl category = new CategoryImpl();
        category.setText(categoryText);
        SessionStatus sessionStatus = createMock(SessionStatus.class);
        expect(userService.getAuthenticatedUser()).andReturn(user).once();
        replay(userService);
        String view = controller.createCategory(category, validToken, validToken,REFERRER_ID, model, sessionStatus);
        assertEquals("ViewName match", ViewNames.ADMIN_CATEGORIES, view);
        assertFalse("empty model", model.asMap().isEmpty());
        verify(userService);
    }

    @Test
    public void updateCategory_valid(){
        Model model = new ExtendedModelMap();
        User user = new UserImpl();
        String id = "1";
        String categoryText = "Social";
        CategoryImpl category = new CategoryImpl(id);
        category.setCreatedUserId(user.getId());
        category.setText(categoryText);
        SessionStatus sessionStatus = createMock(SessionStatus.class);
        expect(userService.getAuthenticatedUser()).andReturn(user).once();
        expect(categoryService.get(id)).andReturn(category);
        expect(categoryService.update(id, categoryText, user)).andReturn(category);
        sessionStatus.setComplete();
        expectLastCall();
        replay(userService, categoryService,sessionStatus);
        String view = controller.updateCategory(category, validToken, validToken,REFERRER_ID, model, sessionStatus);
        assertEquals("ViewName match", "redirect:/app/admin/categories?action=update&referringPageId=" + REFERRER_ID, view);
        assertTrue("empty model", model.asMap().isEmpty());
        verify(userService, categoryService, sessionStatus);
    }

    @Test(expected = SecurityException.class)
    public void updateCategory_invalidToken(){
        Model model = new ExtendedModelMap();
        User user = new UserImpl();
        String id = "1";
        String categoryText = "Social";
        String invalidToken = AdminControllerUtil.generateSessionToken();
        CategoryImpl category = new CategoryImpl();
        category.setCreatedUserId(user.getId());
        category.setText(categoryText);
        category.setId(id);
        SessionStatus sessionStatus = createMock(SessionStatus.class);
        expect(userService.getAuthenticatedUser()).andReturn(user).once();
        expect(categoryService.get(id)).andReturn(category);
        expect(categoryService.update(id, categoryText, user)).andReturn(category);
        sessionStatus.setComplete();
        expectLastCall();
        replay(userService, categoryService,sessionStatus);
        String view = controller.updateCategory(category, validToken, invalidToken,REFERRER_ID, model, sessionStatus);assertTrue("Test should catch exception and never hit this test", false);
        assertTrue("Test should catch exception and never hit this test", false);
    }

    @Test
    public void updateCategory_invalidValidRequest_emptyText(){
        Model model = new ExtendedModelMap();
        User user = new UserImpl();

        String id = "1";
        String categoryText = "";
        CategoryImpl category = new CategoryImpl();
        category.setCreatedUserId(user.getId());
        category.setText(categoryText);
        category.setId(id);
        SessionStatus sessionStatus = createMock(SessionStatus.class);
        expect(userService.getAuthenticatedUser()).andReturn(user).once();
        replay(userService);
        String view = controller.updateCategory(category, validToken, validToken,REFERRER_ID, model, sessionStatus);
        assertEquals("ViewName match", ViewNames.ADMIN_CATEGORY_DETAIL, view);
        assertFalse("empty model", model.asMap().isEmpty());
        verify(userService);
    }

    @Test
    public void updateCategory_invalidValidRequest_nullUser(){
        Model model = new ExtendedModelMap();
        String id = "1";
        User user = new UserImpl();
        String categoryText = "Social";
        CategoryImpl category = new CategoryImpl();
        category.setCreatedUserId(user.getId());
        category.setText(categoryText);
        category.setId(id);
        SessionStatus sessionStatus = createMock(SessionStatus.class);
        expect(userService.getAuthenticatedUser()).andReturn(user).once();
        replay(userService);
        String view = controller.updateCategory(category, validToken, validToken,REFERRER_ID, model, sessionStatus);
        assertEquals("ViewName match", ViewNames.ADMIN_CATEGORY_DETAIL, view);
        assertFalse("empty model", model.asMap().isEmpty());
        verify(userService);
    }

    @Test
    public void updateCategory_invalidValidRequest_nullWidgetToUpdate(){
        Model model = new ExtendedModelMap();
        User user = new UserImpl();
        String id = "1";
        String categoryText = "Social";
        CategoryImpl category = new CategoryImpl();
        category.setCreatedUserId(user.getId());
        category.setText(categoryText);
        category.setId(id);
        SessionStatus sessionStatus = createMock(SessionStatus.class);
        expect(userService.getAuthenticatedUser()).andReturn(user).once();
        expect(categoryService.get(id)).andReturn(null).once();
        replay(userService, categoryService);
        String view = controller.updateCategory(category, validToken, validToken, REFERRER_ID, model, sessionStatus);
        assertEquals("ViewName match", ViewNames.ADMIN_CATEGORY_DETAIL, view);
        assertFalse("empty model", model.asMap().isEmpty());
        verify(userService, categoryService);
    }

    @Test
    public void deleteCategory_valid(){
        Model model = new ExtendedModelMap();
        User user = new UserImpl();
        String id = "1";
        String categoryText = "Social";
        CategoryImpl category = new CategoryImpl();
        category.setCreatedUserId(user.getId());
        category.setText(categoryText);
        category.setId(id);
        SessionStatus sessionStatus = createMock(SessionStatus.class);
        expect(userService.getAuthenticatedUser()).andReturn(user).once();
        expect(categoryService.get(id)).andReturn(category).anyTimes();
        categoryService.delete(category);
        expectLastCall();
        sessionStatus.setComplete();
        expectLastCall();
        replay(userService, categoryService,sessionStatus);
        String view = controller.deleteCategory(category, validToken, validToken, "true",REFERRER_ID, model,sessionStatus);
        assertEquals("ViewName match", "redirect:/app/admin/categories?action=delete&referringPageId=" + REFERRER_ID, view);
        assertTrue("empty model", model.asMap().isEmpty());
        verify(userService, categoryService, sessionStatus);
    }

    @Test(expected = SecurityException.class)
    public void deleteCategory_invalidToken(){
        Model model = new ExtendedModelMap();
        User user = new UserImpl();
        String id = "1";
        String categoryText = "Social";
        String invalidToken = AdminControllerUtil.generateSessionToken();
        CategoryImpl category = new CategoryImpl();
        category.setCreatedUserId(user.getId());
        category.setText(categoryText);
        category.setId(id);
        SessionStatus sessionStatus = createMock(SessionStatus.class);
        expect(userService.getAuthenticatedUser()).andReturn(user).once();
        expect(categoryService.get(id)).andReturn(category);
        categoryService.delete(category);
        expectLastCall();
        sessionStatus.setComplete();
        expectLastCall();
        replay(userService, categoryService,sessionStatus);
        String view = controller.deleteCategory(category, validToken, invalidToken, "true",REFERRER_ID, model, sessionStatus);assertTrue("Test should catch exception and never hit this test", false);
        assertTrue("Test should catch exception and never hit this test", false);
    }

    @Test
    public void deleteCategory_invalidValidRequest_emptyText(){
        Model model = new ExtendedModelMap();
        User user = new UserImpl();

        String id = "1";
        String categoryText = "";
        CategoryImpl category = new CategoryImpl();
        category.setCreatedUserId(user.getId());
        category.setText(categoryText);
        category.setId(id);
        SessionStatus sessionStatus = createMock(SessionStatus.class);
        expect(userService.getAuthenticatedUser()).andReturn(user).once();
        replay(userService);
        String view = controller.deleteCategory(category, validToken, validToken, "true",REFERRER_ID, model, sessionStatus);
        assertEquals("ViewName match", ViewNames.ADMIN_CATEGORY_DETAIL, view);
        assertFalse("empty model", model.asMap().isEmpty());
        verify(userService);
    }

    @Test
    public void deleteCategory_invalidValidRequest_nullUser(){
        Model model = new ExtendedModelMap();
        String id = "1";
        User user = new UserImpl();
        String categoryText = "Social";
        CategoryImpl category = new CategoryImpl();
        category.setCreatedUserId(user.getId());
        category.setText(categoryText);
        category.setId(id);
        SessionStatus sessionStatus = createMock(SessionStatus.class);
        expect(userService.getAuthenticatedUser()).andReturn(user).once();
        replay(userService);
        String view = controller.deleteCategory(category, validToken, validToken, "true",REFERRER_ID, model, sessionStatus);
        assertEquals("ViewName match", ViewNames.ADMIN_CATEGORY_DETAIL, view);
        assertFalse("empty model", model.asMap().isEmpty());
        verify(userService);
    }

    @Test
    public void deleteCategory_invalidValidRequest_nullWidgetToDelete(){
        Model model = new ExtendedModelMap();
        User user = new UserImpl();
        String id = "1";
        String categoryText = "Social";
        CategoryImpl category = new CategoryImpl();
        category.setCreatedUserId(user.getId());
        category.setText(categoryText);
        category.setId(id);
        SessionStatus sessionStatus = createMock(SessionStatus.class);
        expect(userService.getAuthenticatedUser()).andReturn(user).once();
        expect(categoryService.get(id)).andReturn(null).once();
        replay(userService, categoryService);
        String view = controller.deleteCategory(category, validToken, validToken,"true",REFERRER_ID, model, sessionStatus);
        assertEquals("ViewName match", ViewNames.ADMIN_CATEGORY_DETAIL, view);
        assertFalse("empty model", model.asMap().isEmpty());
        verify(userService, categoryService);
    }

    @Test
    public void deleteCategory_invalidValidRequest_falseConfirmation(){
        Model model = new ExtendedModelMap();
        User user = new UserImpl();
        String id = "1";
        String categoryText = "Social";
        CategoryImpl category = new CategoryImpl();
        category.setCreatedUserId(user.getId());
        category.setText(categoryText);
        category.setId(id);
        SessionStatus sessionStatus = createMock(SessionStatus.class);
        expect(userService.getAuthenticatedUser()).andReturn(user).once();
        replay(userService);
        String view = controller.deleteCategory(category, validToken, validToken,"false",REFERRER_ID, model, sessionStatus);
        assertEquals("ViewName match", ViewNames.ADMIN_CATEGORY_DETAIL, view);
        assertEquals("missing confirmation", true, model.asMap().get("missingConfirm"));
        verify(userService);
    }

    @Test
    public void editCategory_valid () {
        User user = new UserImpl();
        String id = "1";
        String categoryText = "Social";
        Model model = new ExtendedModelMap();
        CategoryImpl category = new CategoryImpl();
        category.setCreatedUserId(user.getId());
        category.setText(categoryText);
        category.setId(id);
        expect(categoryService.get(id)).andReturn(category).once();
        replay(categoryService);
        String view = controller.editCategory(id,REFERRER_ID, model);
        assertEquals("ViewName match", ViewNames.ADMIN_CATEGORY_DETAIL, view);
        assertFalse("model is not empty", model.asMap().isEmpty());
        assertSame("check model object", category, model.asMap().get("category"));
        assertTrue("verify tokencheck", model.asMap().containsKey(ModelKeys.TOKENCHECK));
        verify(categoryService);
    }
}
