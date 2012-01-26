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
package org.apache.rave.portal.web.controller.util;

import org.apache.rave.portal.model.Category;
import org.apache.rave.portal.service.CategoryService;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.easymock.EasyMock.*;

public class CategoryEditorTest {
    private CategoryEditor categoryEditor;
    private CategoryService categoryService;
    private final Long CATEGORY_ID = 333L;    
    private final String CATEGORY_TEXT = "category1";
    private Category validCategory;
         
    @Before
    public void setUp() {
        categoryService = createMock(CategoryService.class);
        categoryEditor = new CategoryEditor(categoryService);

        validCategory = new Category();
        validCategory.setEntityId(CATEGORY_ID);
        validCategory.setText(CATEGORY_TEXT);
    }
    
    @Test
    public void setAsText() {
        expect(categoryService.get(CATEGORY_ID)).andReturn(validCategory);
        replay(categoryService);
        categoryEditor.setAsText(String.valueOf(CATEGORY_ID));
        verify(categoryService);
    }
    
    @Test
    public void getAsText_validCategory() {
        categoryEditor.setValue(validCategory);        
        assertThat(categoryEditor.getAsText(), is(validCategory.getEntityId().toString()));  
    }

    @Test
    public void getAsText_nullCategory() {
        categoryEditor.setValue(null);
        assertThat(categoryEditor.getAsText(), is(nullValue(String.class)));
    }    
}