package org.apache.rave.portal.model.conversion;

import org.apache.rave.model.ModelConverter;
import org.apache.rave.portal.model.JpaOrganization;
import org.apache.rave.portal.model.Organization;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;

/**
 * Converts an Address to a JpaAddress
 */
@Component
public class JpaOrganizationConverter implements ModelConverter<Organization, JpaOrganization> {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Class<Organization> getSourceType() {
        return Organization.class;
    }

    @Override
    public JpaOrganization convert(Organization source) {
        return source instanceof JpaOrganization ? (JpaOrganization)source : createEntity(source);
    }

    private JpaOrganization createEntity(Organization source) {
        JpaOrganization converted;
        TypedQuery<JpaOrganization> query = manager.createNamedQuery(JpaOrganization.FIND_BY_NAME, JpaOrganization.class);
        query.setParameter(JpaOrganization.NAME_PARAM, source.getName());
        converted = getSingleResult(query.getResultList());

        if(converted == null) {
            converted = new JpaOrganization();
        }
        updateProperties(source, converted);
        return converted;
    }

    private void updateProperties(Organization source, JpaOrganization converted) {
        converted.setAddress(source.getAddress());
        converted.setDescription(source.getDescription());
        converted.setEndDate(source.getEndDate());
        converted.setField(source.getField());
        converted.setName(source.getName());
        converted.setStartDate(source.getStartDate());
        converted.setSubField(source.getSubField());
        converted.setTitle(source.getTitle());
        converted.setWebpage(source.getWebpage());
        converted.setQualifier(source.getQualifier());
        converted.setPrimary(source.getPrimary());
    }
}
