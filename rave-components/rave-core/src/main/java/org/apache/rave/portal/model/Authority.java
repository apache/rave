package org.apache.rave.portal.model;

import org.springframework.security.core.GrantedAuthority;

import javax.xml.bind.annotation.XmlTransient;
import java.util.Collection;

@XmlTransient
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
