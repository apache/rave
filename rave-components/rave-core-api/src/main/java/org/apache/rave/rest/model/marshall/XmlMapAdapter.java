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

package org.apache.rave.rest.model.marshall;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides marshalling utility for Map to XML and vice versa
 */
public class XmlMapAdapter<E, T> extends XmlAdapter<XmlMapAdapter.EntryList<E, T>, Map<E, T>> {

    @Override
    public Map<E, T> unmarshal(EntryList<E, T> entryList) throws Exception {
        Map<E, T> map = null;
        if (entryList != null) {
            map = new HashMap<E, T>();
            for (Entry<E, T> entry : entryList.getEntries()) {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        return map;
    }

    @Override
    public EntryList<E, T> marshal(Map<E, T> map) throws Exception {
        EntryList<E, T> list = null;
        if(map != null) {
            list = new EntryList<E, T>();
            List<Entry<E, T>> entryList = list.getEntries();
            for(Map.Entry<E, T> entry : map.entrySet()) {
                entryList.add(new Entry<E, T>(entry));
            }
        }
        return list;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "entry", propOrder = { "key", "value"  })
    public static class Entry<E, T> {

        @XmlElement(name = "key")
        private E key;
        @XmlElement(name = "value")
        private T value;

        public Entry() {
        }

        public Entry(Map.Entry<E, T> e) {
            key = e.getKey();
            value = e.getValue();
        }

        public E getKey() {
            return key;
        }

        public void setKey(E key) {
            this.key = key;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "map", propOrder = { "entries" })
    @XmlRootElement(name = "map")
    public static class EntryList<E, T> {

        @XmlElement(name = "entry")
        private List<Entry<E, T>> entries = new ArrayList<Entry<E, T>>();

        public EntryList() {
        }

        public EntryList(Map<E, T> map) {
            for (Map.Entry<E, T> e : map.entrySet()) {
                entries.add(new Entry<E, T>(e));
            }
        }

        public List<Entry<E, T>> getEntries() {
            return entries;
        }

        public void setEntries(List<Entry<E, T>> entries) {
            this.entries = entries;
        }
    }
}

