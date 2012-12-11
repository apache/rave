package org.apache.rave.portal.model;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * User: DSULLIVAN
 * Date: 12/10/12
 * Time: 1:37 PM
 */
public class MongoDbPersonAssociationTest {

    @Test
    public void testPersonAssociation(){

        MongoDbPersonAssociation personAssociation = new MongoDbPersonAssociation();
        personAssociation.setRequestStatus(FriendRequestStatus.ACCEPTED);
        personAssociation.setPersonId((long)46765);

        assertThat(personAssociation.getRequestStatus(), is(equalTo(FriendRequestStatus.ACCEPTED)));
        assertThat(personAssociation.getPersonId(), is(equalTo((long)46765)));

    }
}
