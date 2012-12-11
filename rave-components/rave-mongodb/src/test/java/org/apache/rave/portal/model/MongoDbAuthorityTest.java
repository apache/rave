package org.apache.rave.portal.model;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

/**
 * User: DSULLIVAN
 * Date: 12/10/12
 * Time: 10:42 AM
 */
public class MongoDbAuthorityTest {
    @Test
    public void testAuthority(){
        Authority auth = new MongoDbAuthority();
        long id = 123;
        ((MongoDbAuthority)auth).setId(id);
        assertThat(((MongoDbAuthority) auth).getId(), is(sameInstance((long)123)));
    }
}
