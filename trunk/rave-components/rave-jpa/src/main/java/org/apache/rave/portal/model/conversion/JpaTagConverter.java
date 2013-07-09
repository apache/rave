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
package org.apache.rave.portal.model.conversion;

import org.apache.rave.model.ModelConverter;
import org.apache.rave.persistence.jpa.util.JpaUtil;
import org.apache.rave.portal.model.JpaTag;
import org.apache.rave.model.Tag;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Converts from a {@link org.apache.rave.model.Tag} to a {@link org.apache.rave.portal.model.JpaTag}
 */
@Component
public class JpaTagConverter implements ModelConverter<Tag, JpaTag> {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Class<Tag> getSourceType() {
        return Tag.class;
    }

    @Override
    public JpaTag convert(Tag source) {
        return source instanceof JpaTag ? (JpaTag)source : createEntity(source);
    }

    private JpaTag createEntity(Tag source) {
        JpaTag convertedTag;
        TypedQuery<JpaTag> query = manager.createNamedQuery(JpaTag.FIND_BY_KEYWORD, JpaTag.class);
        query.setParameter(JpaTag.KEYWORD_PARAM, source.getKeyword());
        convertedTag = JpaUtil.getSingleResult(query.getResultList());

        if (convertedTag == null){
            convertedTag = new JpaTag();
        }
        updateProperties(source, convertedTag);
        return convertedTag;
    }

    private void updateProperties(Tag source, JpaTag jpaTag) {
        jpaTag.setKeyword(source.getKeyword());
    }


}
