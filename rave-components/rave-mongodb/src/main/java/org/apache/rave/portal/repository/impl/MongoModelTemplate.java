/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.rave.portal.repository.impl;

import com.mongodb.WriteResult;
import org.apache.rave.portal.model.conversion.HydratingConverterFactory;
import org.apache.rave.portal.repository.MongoModelOperations;
import org.apache.rave.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 */
public class MongoModelTemplate<T, E extends T> implements MongoModelOperations<T> {

    private static final Logger logger = LoggerFactory.getLogger(MongoModelTemplate.class);

    @Autowired
    protected MongoOperations mongoTemplate;

    @Autowired
    protected HydratingConverterFactory converter;
    
    protected final Class<T> type;
    protected final Class<E> dbType;
    protected final String collection;
    
    public MongoModelTemplate(Class<T> type, Class<E> dbType, String collection) {
        this.type = type;
        this.dbType = dbType;
        this.collection = collection;
    } 

    @Override
    public long count(Query query) {
        return mongoTemplate.count(query, collection);
    }

    @Override
    public void remove(Query query) {
        mongoTemplate.remove(query, collection);
    }

    @Override
    public T get(String id) {
        E fromDb = mongoTemplate.findById(id, dbType, collection);
        if(fromDb == null) {
            logger.warn(String.format("Could not find requested %2$s instance: %1$s", id, dbType));
        }
        return hydrate(fromDb);
    }

    @Override
    public T save(T item) {
        E converted = converter.convert(item, type);
        mongoTemplate.save(converted, collection);
        converter.hydrate(converted, type);
        return converted;
    }

    @Override
    public T findOne(Query query) {
        return hydrate(mongoTemplate.findOne(query, dbType, collection));
    }

    @Override
    public List<T> find(Query query) {
        return hydrate(mongoTemplate.find(query, dbType, collection));
    }

    @Override
    public int update(Query query, Update update) {
        WriteResult result = mongoTemplate.updateMulti(query, update, collection);
        return result.getN();
    }

    @Override
    public <E> MapReduceResults<E> mapReduce(String mapFunction, String reduceFunction, Class<E> entityClass) {
        return mongoTemplate.mapReduce(collection, mapFunction, reduceFunction, entityClass);
    }

    @Override
    public <E> MapReduceResults<E> mapReduce(Query query, String mapFunction, String reduceFunction, Class<E> entityClass) {
        return mongoTemplate.mapReduce(query, collection, mapFunction, reduceFunction, entityClass);
    }

    private List<T> hydrate(List<E> mongoDbTs) {
        for(E p : mongoDbTs) {
            hydrate(p);
        }
        return CollectionUtils.<T>toBaseTypedList(mongoDbTs);
    }

    private T hydrate(E page) {
        converter.hydrate(page, type);
        return page;
    }

    public void setMongoTemplate(MongoOperations mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void setConverter(HydratingConverterFactory converter) {
        this.converter = converter;
    }
}
