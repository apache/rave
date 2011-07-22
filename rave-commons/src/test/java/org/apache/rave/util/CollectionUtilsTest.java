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

package org.apache.rave.util;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CollectionUtilsTest {
    private CollectionUtils.CollectionReconciliationHelper<TestObject, String> reconciliationHelper =
            new CollectionUtils.CollectionReconciliationHelper<TestObject, String>() {
                @Override
                public String extractKey(TestObject object) {
                    return object.getName();
                }

                @Override
                public void reconcileValues(TestObject existingObject, TestObject updatedObject) {
                    if (!StringUtils.equals(existingObject.getValue(), updatedObject.getValue())) {
                        existingObject.setValue(updatedObject.getValue());
                    }
                }
            };

    TestObject object1 = new TestObject("test1", "value1");
    TestObject object2 = new TestObject("test2", "value2");
    TestObject object3 = new TestObject("test3", "value3");
    TestObject object4 = new TestObject("test4", "value4");

    HashMap<TestObject, TestObject> existingObjectsMap;
    HashMap<TestObject, TestObject> updatedObjectsMap;

    @Before
    public void setup() {
        existingObjectsMap = getTestObjectsMap();
        updatedObjectsMap = getTestObjectsMap();
    }

    private HashMap<TestObject, TestObject> getTestObjectsMap() {
        TestObject testObject1 = new TestObject("test1", "value1");
        TestObject testObject2 = new TestObject("test2", "value2");
        TestObject testObject3 = new TestObject("test3", "value3");
        TestObject testObject4 = new TestObject("test4", "value4");

        HashMap<TestObject, TestObject> testObjectsMap = new HashMap<TestObject, TestObject>();
        testObjectsMap.put(testObject1, testObject1);
        testObjectsMap.put(testObject2, testObject2);
        testObjectsMap.put(testObject3, testObject3);
        testObjectsMap.put(testObject4, testObject4);
        return testObjectsMap;
    }

    @Test
    public void reconcileObjectCollections_addNewObject() {
        TestObject newObject = new TestObject("newName1", "newValue1");
        updatedObjectsMap.put(newObject, newObject);

        ArrayList<TestObject> existingObjects = new ArrayList<TestObject>(existingObjectsMap.values());
        ArrayList<TestObject> updatedObjects = new ArrayList<TestObject>(updatedObjectsMap.values());

        assertFalse(org.apache.commons.collections.CollectionUtils.isEqualCollection(existingObjects, updatedObjects));
        CollectionUtils.reconcileObjectCollections(existingObjects, updatedObjects, reconciliationHelper, true);
        assertTrue(org.apache.commons.collections.CollectionUtils.isEqualCollection(existingObjects, updatedObjects));
    }

    @Test
    public void reconcileObjectCollections_addSingleNewObject_removeFlagFalse() {
        TestObject newObject = new TestObject("newName1", "newValue1");

        ArrayList<TestObject> existingObjects = new ArrayList<TestObject>(existingObjectsMap.values());
        ArrayList<TestObject> updatedObjects = new ArrayList<TestObject>();
        updatedObjects.add(newObject);

        CollectionUtils.reconcileObjectCollections(existingObjects, updatedObjects, reconciliationHelper, false);
        assertTrue(existingObjects.containsAll(existingObjectsMap.values()));
        assertTrue(existingObjects.contains(newObject));
    }

    @Test
    public void reconcileObjectCollections_addSingleNewObject_removeFlagTrue() {
        TestObject newObject = new TestObject("newName1", "newValue1");

        ArrayList<TestObject> existingObjects = new ArrayList<TestObject>(existingObjectsMap.values());
        ArrayList<TestObject> updatedObjects = new ArrayList<TestObject>();
        updatedObjects.add(newObject);

        CollectionUtils.reconcileObjectCollections(existingObjects, updatedObjects, reconciliationHelper, true);
        assertFalse(org.apache.commons.collections.CollectionUtils.containsAny(existingObjects,  existingObjectsMap.values()));
        assertTrue(existingObjects.contains(newObject));
    }

    @Test
    public void reconcileObjectCollections_updateObject() {
        updatedObjectsMap.get(object2).setValue("New Value For Object 2");

        ArrayList<TestObject> existingObjects = new ArrayList<TestObject>(existingObjectsMap.values());
        ArrayList<TestObject> updatedObjects = new ArrayList<TestObject>(updatedObjectsMap.values());

        assertFalse(org.apache.commons.collections.CollectionUtils.isEqualCollection(existingObjects, updatedObjects));
        CollectionUtils.reconcileObjectCollections(existingObjects, updatedObjects, reconciliationHelper, true);
        assertTrue(org.apache.commons.collections.CollectionUtils.isEqualCollection(existingObjects, updatedObjects));
    }

    @Test
    public void reconcileObjectCollections_updateSingleObject_removeFlagFalse() {
        TestObject updatedObject = new TestObject(object2.getName(), "New Value For Object 2");

        ArrayList<TestObject> existingObjects = new ArrayList<TestObject>(existingObjectsMap.values());
        ArrayList<TestObject> updatedObjects = new ArrayList<TestObject>();
        updatedObjects.add(updatedObject);

        CollectionUtils.reconcileObjectCollections(existingObjects, updatedObjects, reconciliationHelper, false);
        assertFalse(existingObjects.contains(object2));
        assertTrue(existingObjects.contains(updatedObject));
    }

    @Test
    public void reconcileObjectCollections_removeObject_removeFlagTrue() {
        updatedObjectsMap.remove(object2);

        ArrayList<TestObject> existingObjects = new ArrayList<TestObject>(existingObjectsMap.values());
        ArrayList<TestObject> updatedObjects = new ArrayList<TestObject>(updatedObjectsMap.values());

        assertFalse(org.apache.commons.collections.CollectionUtils.isEqualCollection(existingObjects, updatedObjects));
        CollectionUtils.reconcileObjectCollections(existingObjects, updatedObjects, reconciliationHelper, true);
        assertTrue(org.apache.commons.collections.CollectionUtils.isEqualCollection(existingObjects, updatedObjects));
    }

    @Test
    public void reconcileObjectCollections_removeObject_removeFlagFalse() {
        updatedObjectsMap.remove(object2);

        ArrayList<TestObject> existingObjects = new ArrayList<TestObject>(existingObjectsMap.values());
        ArrayList<TestObject> updatedObjects = new ArrayList<TestObject>(updatedObjectsMap.values());

        assertFalse(org.apache.commons.collections.CollectionUtils.isEqualCollection(existingObjects, updatedObjects));
        CollectionUtils.reconcileObjectCollections(existingObjects, updatedObjects, reconciliationHelper, false);
        assertTrue(org.apache.commons.collections.CollectionUtils.isEqualCollection(existingObjects, existingObjectsMap.values()));
    }

    @Test
    public void reconcileObjectCollections_addObject_updateObject_removeObject_removeFlagTrue() {
        TestObject newObject = new TestObject("newName1", "newValue1");
        updatedObjectsMap.put(newObject, newObject);
        updatedObjectsMap.get(object2).setValue("New Value For Object 2");
        updatedObjectsMap.remove(object1);

        ArrayList<TestObject> existingObjects = new ArrayList<TestObject>(existingObjectsMap.values());
        ArrayList<TestObject> updatedObjects = new ArrayList<TestObject>(updatedObjectsMap.values());

        assertFalse(org.apache.commons.collections.CollectionUtils.isEqualCollection(existingObjects, updatedObjects));
        CollectionUtils.reconcileObjectCollections(existingObjects, updatedObjects, reconciliationHelper, true);
        assertTrue(org.apache.commons.collections.CollectionUtils.isEqualCollection(existingObjects, updatedObjects));
    }

    private class TestObject {
        private String name;
        private String value;

        private TestObject(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TestObject that = (TestObject) o;

            if (name != null ? !name.equals(that.name) : that.name != null) return false;
            if (value != null ? !value.equals(that.value) : that.value != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (value != null ? value.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "TestObject{" +
                    "name='" + name + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }
}
