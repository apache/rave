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

import org.apache.rave.portal.security.ModelPermissionEvaluator;
import org.apache.rave.portal.security.ModelPermissionEvaluator.Permission;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 *
 * @author carlucci
 */
public class RavePermissionEvaluatorTest {
    private RavePermissionEvaluator ravePermissionEvaluator;
    private Authentication authentication;
    private BasicEntityModel basicEntityModel;
    private NonBasicEntityModel nonBasicEntityModel;

    private String READ_PERMISSION = "read";
    private String CREATE_OR_UPDATE_PERMISSION = "create_or_update";
    private Long VALID_BASIC_ENTITY_MODEL_ID = 4L;

    @Before
    public void setUp() {
        List<ModelPermissionEvaluator<?>> modelPermissionEvaluatorList = new ArrayList<ModelPermissionEvaluator<?>>();
        modelPermissionEvaluatorList.add(new BasicEntityModelPermissionEvaluator());
        modelPermissionEvaluatorList.add(new NonBasicEntityModelPermissionEvaluator());
        modelPermissionEvaluatorList.add(new TestModelPermissionEvaluator());
        ravePermissionEvaluator = new RavePermissionEvaluator(modelPermissionEvaluatorList);

        authentication = createMock(Authentication.class);
        basicEntityModel = new BasicEntityModel(VALID_BASIC_ENTITY_MODEL_ID);
        nonBasicEntityModel = new NonBasicEntityModel();
    }

    @Test
    public void testLoadOrderOverride() {
        @SuppressWarnings("unchecked")
        ModelPermissionEvaluator<BasicEntityModel> mockedOverriddenPermissionEvaluator = createMock(ModelPermissionEvaluator.class);
        expect(mockedOverriddenPermissionEvaluator.getType()).andReturn(BasicEntityModel.class);
        expect(mockedOverriddenPermissionEvaluator.getLoadOrder()).andReturn(2);
        expect(mockedOverriddenPermissionEvaluator.hasPermission(authentication, basicEntityModel, Permission.fromString(READ_PERMISSION))).andReturn(true);
        replay(mockedOverriddenPermissionEvaluator);

         List<ModelPermissionEvaluator<?>> modelPermissionEvaluatorList = new ArrayList<ModelPermissionEvaluator<?>>();
        // note we are adding the override instance first to verify the Collections.sort works as expected
        modelPermissionEvaluatorList.add(mockedOverriddenPermissionEvaluator);
        modelPermissionEvaluatorList.add(new BasicEntityModelPermissionEvaluator());
        ravePermissionEvaluator = new RavePermissionEvaluator(modelPermissionEvaluatorList);

        assertThat(ravePermissionEvaluator.hasPermission(authentication, basicEntityModel, READ_PERMISSION), is(true));
        verify(mockedOverriddenPermissionEvaluator);
    }

    @Test
    public void testHasPermission_3args_read() {
        assertThat(ravePermissionEvaluator.hasPermission(authentication, basicEntityModel, READ_PERMISSION), is(true));
    }

    @Test
    public void testHasPermission_3args_createOrUpdate_nullEntityId() {
        assertThat(ravePermissionEvaluator.hasPermission(authentication, new BasicEntityModel(), CREATE_OR_UPDATE_PERMISSION), is(true));
    }

    @Test
    public void testHasPermission_3args_createOrUpdate_populatedEntityId() {
        assertThat(ravePermissionEvaluator.hasPermission(authentication, basicEntityModel, CREATE_OR_UPDATE_PERMISSION), is(true));
    }

    @Ignore
    @Test(expected=IllegalArgumentException.class)
    public void testHasPermission_3args_createOrUpdate_nonBasicEntityModel() {
        ravePermissionEvaluator.hasPermission(authentication, nonBasicEntityModel, CREATE_OR_UPDATE_PERMISSION);
    }

    @Test
    public void testHasPermission_3args_nullModel() {
        assertThat(ravePermissionEvaluator.hasPermission(authentication, null, READ_PERMISSION), is(false));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testHasPermission_3args_invalidEvaluator() {
        List<String> list = new ArrayList<String>();
        assertThat(ravePermissionEvaluator.hasPermission(authentication, list, READ_PERMISSION), is(true));
    }

    @Test
    public void testHasPermission_4args() {
        assertThat(ravePermissionEvaluator.hasPermission(authentication, VALID_BASIC_ENTITY_MODEL_ID, BasicEntityModel.class.getName(), READ_PERMISSION), is(true));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testHasPermission_4args_createOrUpdatePermission() {
        ravePermissionEvaluator.hasPermission(authentication, VALID_BASIC_ENTITY_MODEL_ID, BasicEntityModel.class.getName(), CREATE_OR_UPDATE_PERMISSION);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testHasPermission_4args_invalidClass() {
        assertThat(ravePermissionEvaluator.hasPermission(authentication, VALID_BASIC_ENTITY_MODEL_ID, "badclass", READ_PERMISSION), is(true));
    }

    @Test
    public void testFindAndRegisterCompatibleMPE() {
        assertThat(ravePermissionEvaluator.hasPermission(authentication, new TestModelImpl(), READ_PERMISSION), is(true));
    }

    interface TestModel {};

    class TestModelImpl implements TestModel {}

    class BasicEntityModel {
        private Long entityId;

        public BasicEntityModel() { }

        public BasicEntityModel(Long entityId) {
            this.entityId = entityId;
        }

        public Long getEntityId() {
            return entityId;
        }

        public void setEntityId(Long entityId) {
            this.entityId = entityId;
        }
    }

    class BasicEntityModelPermissionEvaluator extends AbstractModelPermissionEvaluator<BasicEntityModel> {
        @Override
        public Class<BasicEntityModel> getType() {
            return BasicEntityModel.class;
        }

        @Override
        public boolean hasPermission(Authentication authentication, BasicEntityModel basicEntityModel, Permission permission) {
            return true;
        }

        @Override
        public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Permission permission) {
            return true;
        }
    }

    class NonBasicEntityModel {
        public NonBasicEntityModel() { }
    }

    class NonBasicEntityModelPermissionEvaluator extends AbstractModelPermissionEvaluator<NonBasicEntityModel> {
        @Override
        public Class<NonBasicEntityModel> getType() {
            return NonBasicEntityModel.class;
        }

        @Override
        public boolean hasPermission(Authentication authentication, NonBasicEntityModel nonBasicEntityModel, Permission permission) {
            return true;
        }

        @Override
        public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Permission permission) {
            return true;
        }
    }

    class TestModelPermissionEvaluator extends AbstractModelPermissionEvaluator<TestModel> {
        @Override
        public Class<TestModel> getType() {
            return TestModel.class;
        }

        @Override
        public boolean hasPermission(Authentication authentication, TestModel testModel, Permission permission) {
            return true;
        }

        @Override
        public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Permission permission) {
            return true;
        }
    }

}
