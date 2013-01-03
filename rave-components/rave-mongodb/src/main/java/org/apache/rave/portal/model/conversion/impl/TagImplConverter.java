package org.apache.rave.portal.model.conversion.impl;

import org.apache.rave.portal.model.Tag;
import org.apache.rave.portal.model.conversion.HydratingModelConverter;
import org.apache.rave.portal.model.impl.TagImpl;
import org.springframework.stereotype.Component;

import static org.apache.rave.portal.model.util.MongoDbModelUtil.generateId;

@Component
public class TagImplConverter implements HydratingModelConverter<Tag, TagImpl> {
    @Override
    public void hydrate(TagImpl dehydrated) {
        //NOP
    }

    @Override
    public Class<Tag> getSourceType() {
        return Tag.class;
    }

    @Override
    public TagImpl convert(Tag tag) {
        String id = tag.getId() == null ? generateId() : tag.getId();
        return new TagImpl(id, tag.getKeyword());
    }
}
