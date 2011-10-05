package org.apache.rave.portal.model;

import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

import static junit.framework.Assert.assertEquals;

/**
 * Test for {@link Authority}
 */
public class DefaultGrantedAuthorityTest {

    @Test
    public void testAuthority() throws Exception {
        GrantedAuthority grantedAuthority = new Authority();
        ((Authority) grantedAuthority).setAuthority("user");
        assertEquals("user", grantedAuthority.getAuthority());

        GrantedAuthority grantedAuthority2 = new Authority("admin");
        assertEquals("admin", grantedAuthority2.getAuthority());
    }
}
