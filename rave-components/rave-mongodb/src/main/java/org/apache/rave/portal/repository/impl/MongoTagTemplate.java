package org.apache.rave.portal.repository.impl;

import org.apache.rave.portal.model.Tag;
import org.apache.rave.portal.model.impl.TagImpl;
import org.apache.rave.portal.repository.MongoTagOperations;
import org.springframework.stereotype.Component;

import static org.apache.rave.portal.repository.util.CollectionNames.TAG_COLLECTION;

/**
 *
 */
@Component
public class MongoTagTemplate extends MongoModelTemplate<Tag, TagImpl> implements MongoTagOperations {
    public MongoTagTemplate() {
        super(Tag.class, TagImpl.class, TAG_COLLECTION);
    }
}
