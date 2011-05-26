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

package org.apache.rave.portal.repository.impl;


import org.apache.commons.lang.NotImplementedException;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.repository.WidgetRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class JpaWidgetRepository implements WidgetRepository{

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<Widget> getAll() {
        TypedQuery<Widget> query = manager.createNamedQuery("Widget.getAll", Widget.class);
        return query.getResultList();
    }

    @Override
    public Widget get(long id) {
        return manager.find(Widget.class, id);
    }

    @Override
    public Widget save(Widget item) {
        throw new NotImplementedException("Save is not implemented for this repository");
    }
}
