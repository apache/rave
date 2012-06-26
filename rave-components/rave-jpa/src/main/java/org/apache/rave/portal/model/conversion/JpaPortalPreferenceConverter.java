package org.apache.rave.portal.model.conversion;

import org.apache.rave.model.ModelConverter;
import org.apache.rave.portal.model.PortalPreference;
import org.apache.rave.portal.model.JpaPortalPreference;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;

/**
 * Converts an PortalPreference JpaPortalPreference
 */
@Component
public class JpaPortalPreferenceConverter implements ModelConverter<PortalPreference, JpaPortalPreference> {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Class<PortalPreference> getSourceType() {
        return PortalPreference.class;
    }

    @Override
    public JpaPortalPreference convert(PortalPreference source) {
        return source instanceof JpaPortalPreference ? (JpaPortalPreference) source : createEntity(source);
    }

    private JpaPortalPreference createEntity(PortalPreference source) {
        JpaPortalPreference converted = null;
        if (source != null) {
            TypedQuery<JpaPortalPreference> query = manager.createNamedQuery(JpaPortalPreference.GET_BY_KEY, JpaPortalPreference.class);
            query.setParameter(JpaPortalPreference.PARAM_KEY, source.getKey());
            converted = getSingleResult(query.getResultList());

            if (converted == null) {
                converted = new JpaPortalPreference();
            }
            updateProperties(source, converted);
        }
        return converted;
    }

    private void updateProperties(PortalPreference source, JpaPortalPreference converted) {
        converted.setKey(source.getKey());
        converted.setValues(source.getValues());
    }
}
