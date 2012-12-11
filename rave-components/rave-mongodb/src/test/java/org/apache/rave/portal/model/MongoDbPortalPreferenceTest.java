package org.apache.rave.portal.model;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * User: DSULLIVAN
 * Date: 12/10/12
 * Time: 3:37 PM
 */
public class MongoDbPortalPreferenceTest {
    @Test
    public void testPortalPreference(){
        MongoDbPortalPreference pp = new MongoDbPortalPreference();
        pp.setId((long)123);

        assertThat(pp.getId(), is(equalTo((long)123)));
    }
}
