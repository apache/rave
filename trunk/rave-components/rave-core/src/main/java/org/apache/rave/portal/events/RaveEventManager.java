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
package org.apache.rave.portal.events;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RaveEventManager  {

    private final Map<Class<? extends RaveEvent>, Set<RaveEventListener>> eventListenerMap;

    public RaveEventManager() {
        this.eventListenerMap = new Hashtable<Class<? extends RaveEvent>, Set<RaveEventListener>> ();
    }

    public void addListener(Class<? extends RaveEvent> event, RaveEventListener listener) {
        synchronized (eventListenerMap){
            if (eventListenerMap.containsKey(event)){
                Set<RaveEventListener> eventListeners = eventListenerMap.get(event);
                if(eventListeners == null){
                    eventListeners = new HashSet<RaveEventListener>();
                }
                eventListeners.add(listener);
            } else {
                Set<RaveEventListener> eventListeners = new HashSet<RaveEventListener>();
                eventListeners.add(listener);
                eventListenerMap.put(event, eventListeners);
            }
        }
    }

    public void removeListener(Class<? extends RaveEvent> event, RaveEventListener listener) {
        synchronized (eventListenerMap){
            if(eventListenerMap.containsKey(event))
                eventListenerMap.get(event).remove(listener);
        }
    }

    public synchronized void fireEvent(RaveEvent event){
        Set<RaveEventListener> listeners = eventListenerMap.get(event.getClass());
        if (listeners != null && !listeners.isEmpty()){
            for (RaveEventListener listener : eventListenerMap.get(event.getClass())) {
                listener.handleEvent(event);
            }
        }
    }
}
