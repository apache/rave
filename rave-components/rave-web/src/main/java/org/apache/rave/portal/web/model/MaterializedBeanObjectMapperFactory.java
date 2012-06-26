package org.apache.rave.portal.web.model;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.mrbean.MrBeanModule;
import org.springframework.beans.factory.FactoryBean;

/**
 * Configures the Jackson ObjectMapper to materialize interfaces
 */
public class MaterializedBeanObjectMapperFactory implements FactoryBean<ObjectMapper> {
    @Override
    public ObjectMapper getObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new MrBeanModule());
        return mapper;
    }

    @Override
    public Class<?> getObjectType() {
        return ObjectMapper.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
