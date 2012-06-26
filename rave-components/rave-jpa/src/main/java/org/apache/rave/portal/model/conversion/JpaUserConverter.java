package org.apache.rave.portal.model.conversion;

import org.apache.rave.model.ModelConverter;
import org.apache.rave.portal.model.Authority;
import org.apache.rave.portal.model.JpaUser;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.impl.AuthorityImpl;
import org.springframework.security.core.GrantedAuthority;
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
        converted.setPassword(source.getPassword());
        converted.setConfirmPassword(source.getConfirmPassword());
        converted.setDefaultPageLayout(source.getDefaultPageLayout());
        converted.setDefaultPageLayoutCode(source.getDefaultPageLayoutCode());
        converted.setEnabled(source.isEnabled());
        converted.setExpired(source.isExpired());
        converted.setLocked(source.isLocked());
        converted.setOpenId(source.getOpenId());
        converted.setForgotPasswordHash(source.getForgotPasswordHash());
        converted.setForgotPasswordTime(source.getForgotPasswordTime());
        updateAuthorities(source, converted);
    }

    private void updateAuthorities(User source, JpaUser converted) {
        converted.getAuthorities().clear();
        for(GrantedAuthority grantedAuthority : source.getAuthorities()) {
            converted.addAuthority(grantedAuthority instanceof Authority ? (Authority)grantedAuthority : new AuthorityImpl(grantedAuthority));
        }
    }
}
