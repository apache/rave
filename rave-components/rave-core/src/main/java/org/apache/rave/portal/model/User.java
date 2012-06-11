package org.apache.rave.portal.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

public interface User extends Person, UserDetails {
    @Override
    Collection<GrantedAuthority> getAuthorities();

    void addAuthority(Authority authority);

    void removeAuthority(Authority authority);

    void setAuthorities(Collection<Authority> newAuthorities);

    /**
     * Gets the password stored in the database
     *
     * @return password as String
     */
    @Override
    String getPassword();

    void setPassword(String password);

    @Override
    String getUsername();

    void setUsername(String username);

    @Override
    boolean isAccountNonLocked();

    boolean isLocked();

    void setLocked(boolean locked);

    @Override
    boolean isCredentialsNonExpired();

    /**
     * Synchronized with password expiration {@see isCredentialsNonExpired}
     *
     * @return <code>true</code> if the user's is not expired valid (ie non-expired), <code>false</code> if no longer valid
     */
    //REVIEW NOTE: Conflating Account and Credential (non)expiration seems likely to cause confusion at some point.
    @Override
    boolean isAccountNonExpired();

    boolean isExpired();

    void setExpired(boolean expired);

    @Override
    boolean isEnabled();

    void setEnabled(boolean enabled);

    //The following properties are specific to the user profile.
    String getEmail();

    void setEmail(String email);

    String getOpenId();

    void setOpenId(String openId);

    String getForgotPasswordHash();

    void setForgotPasswordHash(String forgotPasswordHash);

    Date getForgotPasswordTime();

    void setForgotPasswordTime(Date forgotPasswordTime);

    PageLayout getDefaultPageLayout();

    void setDefaultPageLayout(PageLayout defaultPageLayout);

    /**
	 * @return the confirmPassword
	 */
    String getConfirmPassword();

    /**
     * @param confirmPassword the confirmPassword to set
     */
    void setConfirmPassword(String confirmPassword);

    /**
     * Conversion function to create a new Person object based off of this User class.  This is useful for when you
     * have a User object and want to pass on the Person data without the User class data (like password, etc)
     *
     * @return a Person object representing the data contained in this class
     */
    Person toPerson();

    Long getId();

    String getDefaultPageLayoutCode();

    void setDefaultPageLayoutCode(String pageLayoutCode);
}
