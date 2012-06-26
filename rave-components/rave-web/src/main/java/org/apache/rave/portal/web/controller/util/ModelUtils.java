package org.apache.rave.portal.web.controller.util;

import org.apache.rave.portal.model.Authority;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.model.impl.UserImpl;
import org.apache.rave.portal.web.model.UserForm;
import org.apache.rave.util.CollectionUtils;

/**
 */
public class ModelUtils {

    private ModelUtils() {}


    public static User convert(UserForm form) {
        User newUser = new UserImpl(form.getId(),  form.getUsername());
        newUser.setAuthorities(CollectionUtils.<Authority>toBaseTypedCollection(form.getAuthorities()));
        newUser.setPassword(form.getPassword());
        newUser.setConfirmPassword(form.getConfirmPassword());
        newUser.setForgotPasswordHash(form.getForgotPasswordHash());
        newUser.setDefaultPageLayoutCode(form.getDefaultPageLayoutCode());
        newUser.setStatus(form.getStatus());
        newUser.setAboutMe(form.getAboutMe());
        newUser.setGivenName(form.getGivenName());
        newUser.setFamilyName(form.getFamilyName());
        newUser.setDisplayName(form.getDisplayName());
        newUser.setEmail(form.getEmail());
        newUser.setOpenId(form.getOpenId());
        newUser.setEnabled(form.isEnabled());
        newUser.setExpired(form.isExpired());
        newUser.setLocked(form.isLocked());
        return newUser;
    }
}
