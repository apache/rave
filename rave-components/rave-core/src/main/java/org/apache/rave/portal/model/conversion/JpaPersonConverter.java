package org.apache.rave.portal.model.conversion;

import org.apache.rave.model.ModelConverter;
import org.apache.rave.portal.model.JpaPerson;
import org.apache.rave.portal.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;

/**
 * Converts from a {@link org.apache.rave.portal.model.Person} to a {@link org.apache.rave.portal.model.JpaPerson}
 */
@Component
public class JpaPersonConverter implements ModelConverter<Person, JpaPerson> {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public JpaPerson convert(Person source) {
        return source instanceof JpaPerson ? (JpaPerson)source : createEntity(source);
    }

    @Override
    public Class<Person> getSourceType() {
        return Person.class;
    }

    private JpaPerson createEntity(Person source) {
        JpaPerson converted;
        TypedQuery<JpaPerson> query = manager.createNamedQuery(JpaPerson.FIND_BY_USERNAME, JpaPerson.class);
        query.setParameter(JpaPerson.USERNAME_PARAM, source.getUsername());
        converted = getSingleResult(query.getResultList());

        if(converted == null) {
            converted = new JpaPerson();
        }
        updateProperties(source, converted);
        return converted;
    }

    private void updateProperties(Person source, JpaPerson converted) {
        converted.setUsername(source.getUsername());
        converted.setEmail(source.getEmail());
        converted.setDisplayName(source.getDisplayName());
        converted.setAdditionalName(source.getUsername());
        converted.setFamilyName(source.getFamilyName());
        converted.setGivenName(source.getGivenName());
        converted.setHonorificPrefix(source.getHonorificPrefix());
        converted.setHonorificSuffix(source.getHonorificSuffix());
        converted.setPreferredName(source.getPreferredName());
        converted.setAboutMe(source.getAboutMe());
        converted.setStatus(source.getStatus());
        converted.setAddresses(source.getAddresses());
        converted.setOrganizations(source.getOrganizations());
        converted.setProperties(source.getProperties());
        converted.setFriends(source.getFriends());
    }
}
