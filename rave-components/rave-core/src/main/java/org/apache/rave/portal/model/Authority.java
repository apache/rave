package org.apache.rave.portal.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: mfranklin
 * Date: 6/11/12
 * Time: 5:40 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Authority extends GrantedAuthority {
    @Override
    String getAuthority();

    void setAuthority(String authority);

    boolean isDefaultForNewUser();

    void setDefaultForNewUser(boolean defaultForNewUser);

    Collection<User> getUsers();

    void addUser(User user);

    void removeUser(User user);
}
