package org.apache.rave.portal.model.conversion;

import org.apache.rave.model.ModelConverter;
import org.apache.rave.portal.model.JpaUser;
import org.apache.rave.portal.model.User;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Converts from a {@link org.apache.rave.portal.model.User} to a {@link org.apache.rave.portal.model.JpaUser}
 */
@Component
public class JpaUserConverter implements ModelConverter<User, JpaUser> {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public JpaUser convert(User source) {
        return source instanceof JpaUser ? (JpaUser) source : createEntity(source);
    }

    @Override
    public Class<User> getSourceType() {
        return User.class;
    }

    private JpaUser createEntity(User source) {
        JpaUser converted = null;
        if (source != null) {
            converted = new JpaUser();
            updateProperties(source, converted);
        }
        return converted;
    }

    private void updateProperties(User source, JpaUser converted) {
        converted.setEntityId(source.getId());

        //TODO: Finish all properties
    }
}
