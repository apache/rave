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

package org.apache.rave.portal.web.renderer;


import org.apache.rave.portal.web.renderer.impl.DefaultScriptManager;
import org.apache.rave.portal.web.renderer.model.RenderContext;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ScriptManagerTest {
    
    public static final String SCRIPT_1 = "FOO";
    public static final String SCRIPT_2 = "BAR";
    public static final String SCRIPT_1_KEY = "f";
    public static final String SCRIPT_2_KEY = "b";
    
    private ScriptManager manager;
    private RenderContext context;
    private HashMap contextProperties;

    @Before
    public void setup() {
        manager = new DefaultScriptManager();
        context = new RenderContext();
        contextProperties = new HashMap();
        context.setProperties(contextProperties);
    }

    @Test
    public void registerBlockAndRetrieve_simple() {
        manager.registerScriptBlock(SCRIPT_1_KEY, SCRIPT_1, ScriptLocation.BEFORE_RAVE);
        List<String> scriptBlocks = manager.getScriptBlocks(ScriptLocation.BEFORE_RAVE, context);
        assertThat(scriptBlocks.size(), is(equalTo(1)));
        assertThat(scriptBlocks.get(0), is(equalTo(SCRIPT_1)));
    }

    @Test
    public void registerBlockAndRetrieve_simpleFullSignature() {
        manager.registerScriptBlock(SCRIPT_1_KEY, SCRIPT_1, ScriptLocation.BEFORE_RAVE, RenderScope.GLOBAL, context);
        List<String> scriptBlocks = manager.getScriptBlocks(ScriptLocation.BEFORE_RAVE, context);
        assertThat(scriptBlocks.size(), is(equalTo(1)));
        assertThat(scriptBlocks.get(0), is(equalTo(SCRIPT_1)));
    }

    @Test
    public void registerBlockAndRetrieve_list() {
        manager.registerScriptBlock(SCRIPT_1_KEY, SCRIPT_1, ScriptLocation.BEFORE_RAVE);
        manager.registerScriptBlock(SCRIPT_2_KEY, SCRIPT_2, ScriptLocation.BEFORE_RAVE);
        List<String> scriptBlocks = manager.getScriptBlocks(ScriptLocation.BEFORE_RAVE, context);
        assertThat(scriptBlocks.size(), is(equalTo(2)));
        assertThat(scriptBlocks.get(0), is(equalTo(SCRIPT_1)));
        assertThat(scriptBlocks.get(1), is(equalTo(SCRIPT_2)));
    }

    @Test
    public void registerBlockAndRetrieve_multi() {
        manager.registerScriptBlock(SCRIPT_1_KEY, SCRIPT_1, ScriptLocation.BEFORE_RAVE);
        manager.registerScriptBlock(SCRIPT_2_KEY, SCRIPT_2, ScriptLocation.AFTER_RAVE);
        List<String> scriptBlocks = manager.getScriptBlocks(ScriptLocation.BEFORE_RAVE, context);
        assertThat(scriptBlocks.size(), is(equalTo(1)));
        assertThat(scriptBlocks.get(0), is(equalTo(SCRIPT_1)));
        scriptBlocks = manager.getScriptBlocks(ScriptLocation.AFTER_RAVE, context);
        assertThat(scriptBlocks.size(), is(equalTo(1)));
        assertThat(scriptBlocks.get(0), is(equalTo(SCRIPT_2)));
    }

    @Test
    public void registerBlockAndRetrieve_simpleInContext() {
        manager.registerScriptBlock(SCRIPT_1_KEY, SCRIPT_1, ScriptLocation.BEFORE_RAVE, RenderScope.CURRENT_REQUEST, context);
        List<String> scriptBlocks = manager.getScriptBlocks(ScriptLocation.BEFORE_RAVE, context);
        assertThat(scriptBlocks.size(), is(equalTo(1)));
        assertThat(scriptBlocks.get(0), is(equalTo(SCRIPT_1)));
    }

    @Test
    public void registerBlockAndRetrieve_listInContext() {
        manager.registerScriptBlock(SCRIPT_1_KEY, SCRIPT_1, ScriptLocation.BEFORE_RAVE, RenderScope.CURRENT_REQUEST, context);
        manager.registerScriptBlock(SCRIPT_2_KEY, SCRIPT_2, ScriptLocation.BEFORE_RAVE, RenderScope.CURRENT_REQUEST, context);
        List<String> scriptBlocks = manager.getScriptBlocks(ScriptLocation.BEFORE_RAVE, context);
        assertThat(scriptBlocks.size(), is(equalTo(2)));
        assertThat(scriptBlocks.get(0), is(equalTo(SCRIPT_1)));
        assertThat(scriptBlocks.get(1), is(equalTo(SCRIPT_2)));
    }

    @Test
    public void registerBlockAndRetrieve_multiInContext() {
        manager.registerScriptBlock(SCRIPT_1_KEY, SCRIPT_1, ScriptLocation.BEFORE_RAVE, RenderScope.CURRENT_REQUEST, context);
        manager.registerScriptBlock(SCRIPT_2_KEY, SCRIPT_2, ScriptLocation.AFTER_RAVE, RenderScope.CURRENT_REQUEST, context);
        List<String> scriptBlocks = manager.getScriptBlocks(ScriptLocation.BEFORE_RAVE, context);
        assertThat(scriptBlocks.size(), is(equalTo(1)));
        assertThat(scriptBlocks.get(0), is(equalTo(SCRIPT_1)));
        scriptBlocks = manager.getScriptBlocks(ScriptLocation.AFTER_RAVE, context);
        assertThat(scriptBlocks.size(), is(equalTo(1)));
        assertThat(scriptBlocks.get(0), is(equalTo(SCRIPT_2)));
    }

    @Test
    public void registerBlockAndRetrieve_combined() {
        manager.registerScriptBlock(SCRIPT_1_KEY, SCRIPT_1, ScriptLocation.BEFORE_RAVE, RenderScope.GLOBAL, context);
        manager.registerScriptBlock(SCRIPT_2_KEY, SCRIPT_2, ScriptLocation.BEFORE_RAVE, RenderScope.CURRENT_REQUEST, context);
        List<String> scriptBlocks = manager.getScriptBlocks(ScriptLocation.BEFORE_RAVE, context);
        assertThat(scriptBlocks.size(), is(equalTo(2)));
        assertThat(scriptBlocks.get(0), is(equalTo(SCRIPT_1)));
        assertThat(scriptBlocks.get(1), is(equalTo(SCRIPT_2)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullContext() {
        manager.getScriptBlocks(ScriptLocation.AFTER_LIB, null);
    }
}
