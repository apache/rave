package org.apache.rave.portal.model.conversion;

import org.apache.rave.model.ModelConverter;
import org.apache.rave.portal.model.JpaOrganization;
import org.apache.rave.portal.model.JpaPersonProperty;
import org.apache.rave.portal.model.Organization;
import org.apache.rave.portal.model.PersonProperty;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;

/**
 * Converts an Address to a JpaAddress
 */
@Component
public class JpaPersonPropertyConverter implements ModelConverter<PersonProperty, JpaPersonProperty> {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Class<PersonProperty> getSourceType() {
        return PersonProperty.class;
    }

    @Override
    public JpaPersonProperty convert(PersonProperty source) {
        return source instanceof JpaPersonProperty ? (JpaPersonProperty)source : createEntity(source);
    }

    private JpaPersonProperty createEntity(PersonProperty source) {
        JpaPersonProperty converted;
        TypedQuery<JpaPersonProperty> query = manager.createNamedQuery(JpaPersonProperty.FIND_BY_TYPE_AND_VALUE, JpaPersonProperty.class);
        query.setParameter(JpaPersonProperty.TYPE_PARAM, source.getType());
        query.setParameter(JpaPersonProperty.VALUE_PARAM, source.getValue());
        converted = getSingleResult(query.getResultList());

        if(converted == null) {
            converted = new JpaPersonProperty();
        }
        updateProperties(source, converted);
        return converted;
    }

    private void updateProperties(PersonProperty source, JpaPersonProperty converted) {
        converted.setQualifier(source.getQualifier());
        converted.setPrimary(source.getPrimary());
    }
}
