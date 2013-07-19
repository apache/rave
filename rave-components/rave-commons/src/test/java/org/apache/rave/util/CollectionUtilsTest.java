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

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
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
    public void privateConstructor() throws Exception {
        Constructor<?>[] cons = CollectionUtils.class.getDeclaredConstructors();
        cons[0].setAccessible(true);
        cons[0].newInstance((Object[])null);
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

    @Test
    public void toBaseTypedList() {
        List<SubTestObject> list = new ArrayList<SubTestObject>();
        list.add(new SubTestObject("a", "b"));
        list.add(new SubTestObject("a", "b"));
        list.add(new SubTestObject("a", "b"));

        List<TestObject> down = CollectionUtils.toBaseTypedList((List<? extends TestObject>)list);
        assertThat(down.get(0), is(sameInstance((TestObject)list.get(0))));
        assertThat(down.get(0), is(sameInstance((TestObject)list.get(0))));
        assertThat(down.get(0), is(sameInstance((TestObject)list.get(0))));
    }

    @Test
    public void toBaseTypedList_null() {
        List<SubTestObject> list = null;

        List<TestObject> down = CollectionUtils.toBaseTypedList((List<? extends TestObject>)list);
        assertThat(down, is(nullValue()));
    }

    @Test
    public void toBaseTypedCollection() {
        Collection<SubTestObject> collection = new ArrayList<SubTestObject>();
        collection.add(new SubTestObject("a", "b"));
        collection.add(new SubTestObject("a", "b"));
        collection.add(new SubTestObject("a", "b"));

        Collection<TestObject> down = CollectionUtils.toBaseTypedCollection((Collection<? extends TestObject>) collection);
        assertThat(down.contains(collection.toArray()[0]), is(true));
        assertThat(down.contains(collection.toArray()[1]), is(true));
        assertThat(down.contains(collection.toArray()[2]), is(true));
    }

    @Test
    public void toBaseTypedCollection_null() {
        Collection<SubTestObject> collection = null;

        Collection<TestObject> down = CollectionUtils.toBaseTypedCollection((Collection<? extends TestObject>) collection);
        assertThat(down, is(nullValue()));
    }

    @Test
    public void getSingleValue_empty() {
        List<String> list = new ArrayList<String>();
        assertThat(CollectionUtils.getSingleValue(list), is(nullValue(String.class)));
    }

    @Test
    public void getSingleValue_single() {
        List<String> list = new ArrayList<String>();
        list.add("hello");
        assertThat(CollectionUtils.getSingleValue(list), is("hello"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getSingleValue_multiple() {
        List<String> list = new ArrayList<String>();
        list.add("foo");
        list.add("bar");
        CollectionUtils.getSingleValue(list);
    }

    @Test
    public void addUniqueValues() {
        List<TestObject> source = new ArrayList<TestObject>();
        TestObject testObject1 = new TestObject("a", "b");
        TestObject testObject2 = new TestObject("b", "c");
        TestObject testObject3 = new TestObject("k", "l");
        source.add(testObject1);
        source.add(testObject2);

        List<TestObject> target = new ArrayList<TestObject>();
        target.add(testObject1);
        target.add(testObject3);

        CollectionUtils.addUniqueValues(source, target);
        assertThat(target.size(), is(equalTo(3)));
        assertThat(target.contains(testObject1), is(true));
        assertThat(target.contains(testObject2), is(true));
        assertThat(target.contains(testObject3), is(true));
    }

    private class SubTestObject extends TestObject {
        private SubTestObject(String name, String value) {
            super(name, value);
        }
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
