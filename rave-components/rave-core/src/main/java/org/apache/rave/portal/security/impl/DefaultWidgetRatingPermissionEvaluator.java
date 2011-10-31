package org.apache.rave.portal.security.impl;

import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.WidgetRating;
import org.apache.rave.portal.repository.WidgetRatingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
public class DefaultWidgetRatingPermissionEvaluator extends AbstractModelPermissionEvaluator<WidgetRating> {
    private Logger log = LoggerFactory.getLogger(getClass());
    private WidgetRatingRepository widgetRatingRepository;

    @Autowired
    public DefaultWidgetRatingPermissionEvaluator(WidgetRatingRepository widgetRatingRepository) {
        this.widgetRatingRepository = widgetRatingRepository;
    }

    @Override
    public Class<WidgetRating> getType() {
        return WidgetRating.class;
    }

    /**
     * Checks to see if the Authentication object has the supplied Permission
     * on the supplied WidgetRating object.  This method invokes the private hasPermission
     * function with the trustedDomainObject parameter set to false since we don't
     * know if the model being passed in was modified in any way from the
     * actual entity in the database.
     *
     * @param authentication the current Authentication object
     * @param widgetRating   the WidgetRating model object
     * @param permission     the Permission to check
     * @return true if the Authentication has the proper permission, false otherwise
     */
    @Override
    public boolean hasPermission(Authentication authentication, WidgetRating widgetRating, Permission permission) {
        return hasPermission(authentication, widgetRating, permission, false);
    }

    /**
     * Checks to see if the Authentication object has the supplied Permission
     * for the Entity represented by the targetId(entityId) and targetType(model class name).
     * This method invokes the private hasPermission function with the
     * trustedDomainObject parameter set to true since we must pull the entity
     * from the database and are guaranteed a trusted domain object,
     * before performing our permission checks.
     *
     * @param authentication the current Authentication object
     * @param targetId       the entityId of the model to check, or a RaveSecurityContext object
     * @param targetType     the class of the model to check
     * @param permission     the Permission to check
     * @return true if the Authentication has the proper permission, false otherwise
     */
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Permission permission) {
        boolean hasPermission = false;
        if (targetId instanceof RaveSecurityContext) {
            hasPermission = verifyRaveSecurityContext(authentication, (RaveSecurityContext) targetId);
        } else {
            hasPermission = hasPermission(authentication, widgetRatingRepository.get((Long) targetId), permission, true);
        }
        return hasPermission;
    }

    private boolean hasPermission(Authentication authentication, WidgetRating widgetRating, Permission permission, boolean trustedDomainObject) {
        // this is our container of trusted widgetRating objects that can be re-used
        // in this method so that the same trusted widgetRating object doesn't have to
        // be looked up in the repository multiple times
        List<WidgetRating> trustedWidgetRatingContainer = new ArrayList<WidgetRating>();

        // first execute the AbstractModelPermissionEvaluator's hasPermission function
        // to see if it allows permission via it's "higher authority" logic
        if (super.hasPermission(authentication, widgetRating, permission)) {
            return true;
        }

        // perform the security logic depending on the Permission type
        boolean hasPermission = false;

        switch (permission) {
            case ADMINISTER:
                // if you are here, you are not an administrator, so you can't administer WidgetRating
                break;
            case CREATE:
            case DELETE:
            case READ:
            case UPDATE:
                // anyone can create, delete, read, or update a widgetRating that they own
                hasPermission = isWidgetRatingOwner(authentication, widgetRating, trustedWidgetRatingContainer, trustedDomainObject);
                break;
            default:
                log.warn("unknown permission: " + permission);
                break;
        }

        return hasPermission;
    }

    // returns a trusted WidgetRating object, either from the WidgetRatingRepository, or the
    // cached container list
    private WidgetRating getTrustedWidgetRating(long widgetRatingId, List<WidgetRating> trustedWidgetRatingContainer) {
        WidgetRating widgetRating = null;
        if (trustedWidgetRatingContainer.isEmpty()) {
            widgetRating = widgetRatingRepository.get(widgetRatingId);
            trustedWidgetRatingContainer.add(widgetRating);
        } else {
            widgetRating = trustedWidgetRatingContainer.get(0);
        }
        return widgetRating;
    }

    // checks to see if the Authentication object principal is the owner of the supplied widgetRating object
    // if trustedDomainObject is false, pull the entity from the database first to ensure
    // the model object is trusted and hasn't been modified
    private boolean isWidgetRatingOwner(Authentication authentication, WidgetRating widgetRating, List<WidgetRating> trustedWidgetRatingContainer, boolean trustedDomainObject) {
        WidgetRating trustedWidgetRating = null;
        if (trustedDomainObject) {
            trustedWidgetRating = widgetRating;
        } else {
            trustedWidgetRating = getTrustedWidgetRating(widgetRating.getEntityId(), trustedWidgetRatingContainer);
        }
        return isWidgetRatingOwnerById(authentication, trustedWidgetRating.getUserId());
    }

    private boolean isWidgetRatingOwnerByUsername(Authentication authentication, String username) {
        return ((User)authentication.getPrincipal()).getUsername().equals(username);
    }

    private boolean isWidgetRatingOwnerById(Authentication authentication, Long userId) {
        return ((User)authentication.getPrincipal()).getEntityId().equals(userId);
    }

    private boolean verifyRaveSecurityContext(Authentication authentication, RaveSecurityContext raveSecurityContext) {
        Class<?> clazz = null;
        try {
           clazz = Class.forName(raveSecurityContext.getType());
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException("unknown class specified in RaveSecurityContext: ", ex);
        }

        // perform the permissions check based on the class supplied to the RaveSecurityContext object
        if (User.class == clazz) {
            return isWidgetRatingOwnerById(authentication, (Long) raveSecurityContext.getId());
        } else {
            throw new IllegalArgumentException("unknown RaveSecurityContext type: " + raveSecurityContext.getType());
        }
    }
}
