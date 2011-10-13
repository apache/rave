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
package org.apache.rave.portal.security.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.rave.portal.security.ModelPermissionEvaluator;
import org.apache.rave.portal.security.ModelPermissionEvaluator.Permission;
import org.springframework.security.core.Authentication;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author carlucci
 */
public class RavePermissionEvaluatorTest {
    private RavePermissionEvaluator ravePermissionEvaluator;
    private Authentication authentication;
    private FooModel fooModel;
    
    private String VALID_PERMISSION = "read";
    private Long VALID_FOO_ID = 4L;
    
    
    @Before
    public void setUp() {
        List<ModelPermissionEvaluator> modelPermissionEvaluatorList = new ArrayList<ModelPermissionEvaluator>();
        modelPermissionEvaluatorList.add(new FooModelPermissionEvaluator());                       
        ravePermissionEvaluator = new RavePermissionEvaluator(modelPermissionEvaluatorList);
        
        authentication = createMock(Authentication.class);
        fooModel = new FooModel();
    }
    
    @Test
    public void testLoadOrderOverride() {
        ModelPermissionEvaluator<FooModel> mockedOverriddenPermissionEvaluator = createMock(ModelPermissionEvaluator.class);                              
        expect(mockedOverriddenPermissionEvaluator.getType()).andReturn(FooModel.class);
        expect(mockedOverriddenPermissionEvaluator.getLoadOrder()).andReturn(2);
        expect(mockedOverriddenPermissionEvaluator.hasPermission(authentication, fooModel, Permission.fromString(VALID_PERMISSION))).andReturn(true);        
        replay(mockedOverriddenPermissionEvaluator);
        
         List<ModelPermissionEvaluator> modelPermissionEvaluatorList = new ArrayList<ModelPermissionEvaluator>();
        // note we are adding the overide instance first to verify the Collections.sort works as expected
        modelPermissionEvaluatorList.add(mockedOverriddenPermissionEvaluator);
        modelPermissionEvaluatorList.add(new FooModelPermissionEvaluator());                       
        ravePermissionEvaluator = new RavePermissionEvaluator(modelPermissionEvaluatorList);
        
        assertThat(ravePermissionEvaluator.hasPermission(authentication, fooModel, VALID_PERMISSION), is(true));        
        verify(mockedOverriddenPermissionEvaluator);    
    }
    
    @Test
    public void testHasPermission_3args() {        
        assertThat(ravePermissionEvaluator.hasPermission(authentication, fooModel, VALID_PERMISSION), is(true));        
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testHasPermission_3args_invalidEvaluator() {        
        List<String> list = new ArrayList<String>();
        assertThat(ravePermissionEvaluator.hasPermission(authentication, list, VALID_PERMISSION), is(true));        
    }    
    
    @Test
    public void testHasPermission_4args() {    
        assertThat(ravePermissionEvaluator.hasPermission(authentication, VALID_FOO_ID, FooModel.class.getName(), VALID_PERMISSION), is(true));        
    }
    
    class FooModel {
        public FooModel() {
            
        }
    }
    
    class FooModelPermissionEvaluator extends AbstractModelPermissionEvaluator<FooModel> {
        @Override
        public Class<FooModel> getType() {
            return FooModel.class;
        }

        @Override
        public boolean hasPermission(Authentication authentication, FooModel foo, Permission permission) {
            return true;
        }
        
        @Override
        public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Permission permission) {
            return true;
        }
    }       
}
