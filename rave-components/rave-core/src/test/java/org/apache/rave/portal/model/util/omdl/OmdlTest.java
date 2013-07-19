/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.rave.portal.model.util.omdl;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OmdlTest {
    
    private OmdlInputAdapter omdlInputAdapter;
    
    @Before
    public void setUp() {
        omdlInputAdapter = new OmdlInputAdapter();
    }
    
    @Test
    public void testExportLayouts() {
        assertEquals("ONE COLUMNS", OmdlModelUtils.getOmdlLayoutForExport("columns_1"));
        assertEquals("TWO COLUMNS", OmdlModelUtils.getOmdlLayoutForExport("columns_2"));
        assertEquals("TWO COLUMNS WIDE NARROW", OmdlModelUtils.getOmdlLayoutForExport("columns_2wn"));
        assertEquals("THREE COLUMNS", OmdlModelUtils.getOmdlLayoutForExport("columns_3"));
        assertEquals("THREE COLUMNS NARROW WIDE NARROW", OmdlModelUtils.getOmdlLayoutForExport("columns_3nwn"));
        assertEquals("TWO COLUMNS TWO ROWS WIDE", OmdlModelUtils.getOmdlLayoutForExport("columns_3_newuser"));
        assertEquals("FOUR COLUMNS", OmdlModelUtils.getOmdlLayoutForExport("columns_4"));
        assertEquals("THREE COLUMNS NARROW WIDE NARROW TWO ROWS WIDE", OmdlModelUtils.getOmdlLayoutForExport("columns_3nwn_1_bottom"));
    }

    @Test
    public void testImportLayouts(){
        omdlInputAdapter.setLayoutCode("ONE COLUMNS");
        assertEquals("columns_1", OmdlModelUtils.getRaveLayoutForImport(omdlInputAdapter));
        
        omdlInputAdapter.setLayoutCode("TWO COLUMNS");
        assertEquals("columns_2", OmdlModelUtils.getRaveLayoutForImport(omdlInputAdapter));
        
        omdlInputAdapter.setLayoutCode("TWO COLUMNS WIDE NARROW");
        assertEquals("columns_2wn", OmdlModelUtils.getRaveLayoutForImport(omdlInputAdapter));
        
        omdlInputAdapter.setLayoutCode("THREE COLUMNS");
        assertEquals("columns_3", OmdlModelUtils.getRaveLayoutForImport(omdlInputAdapter));
        
        omdlInputAdapter.setLayoutCode("THREE COLUMNS NARROW WIDE NARROW");
        assertEquals("columns_3nwn", OmdlModelUtils.getRaveLayoutForImport(omdlInputAdapter));
        
        omdlInputAdapter.setLayoutCode("TWO COLUMNS TWO ROWS WIDE");
        assertEquals("columns_3_newuser", OmdlModelUtils.getRaveLayoutForImport(omdlInputAdapter));
        
        omdlInputAdapter.setLayoutCode("FOUR COLUMNS");
        assertEquals("columns_4", OmdlModelUtils.getRaveLayoutForImport(omdlInputAdapter));
        
        omdlInputAdapter.setLayoutCode("THREE COLUMNS NARROW WIDE NARROW TWO ROWS WIDE");
        assertEquals("columns_3nwn_1_bottom", OmdlModelUtils.getRaveLayoutForImport(omdlInputAdapter));
        // test for a default value, if no layout code given
        omdlInputAdapter.setLayoutCode(null);
        assertEquals("columns_1", OmdlModelUtils.getRaveLayoutForImport(omdlInputAdapter));
    }

}
