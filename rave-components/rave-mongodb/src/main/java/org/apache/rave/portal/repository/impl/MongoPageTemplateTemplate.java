package org.apache.rave.portal.repository.impl;

import org.apache.rave.model.PageTemplate;
import org.apache.rave.portal.model.MongoDbPageTemplate;
import org.apache.rave.portal.repository.MongoPageTemplateOperations;

import static org.apache.rave.portal.repository.util.CollectionNames.PAGE_TEMPLATE_COLLECTION;

/**
 * MongoModelTemplate for the PageTemplate class
 */
public class MongoPageTemplateTemplate extends MongoModelTemplate<PageTemplate, MongoDbPageTemplate> implements MongoPageTemplateOperations {

    public MongoPageTemplateTemplate() {
        super(PageTemplate.class, MongoDbPageTemplate.class, PAGE_TEMPLATE_COLLECTION);
    }
}
