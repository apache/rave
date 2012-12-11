package org.apache.rave.portal.model.conversion.impl;

import org.apache.rave.portal.model.Authority;
import org.apache.rave.portal.model.MongoDbAuthority;
import org.apache.rave.portal.model.impl.AuthorityImpl;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * A test for the MongoDbAuthorityCoverter.java
 * To change this template use File | Settings | File Templates.
 */
public class MongoDbAuthorityConverterTest {
    private MongoDbAuthorityConverter authorityConverter;

    @Before
    public void setup(){
        authorityConverter = new MongoDbAuthorityConverter();
    }

    @Test
    public void hydrate_Valid(){
        MongoDbAuthority authority = new MongoDbAuthority();
        authorityConverter.hydrate(authority);
        assertNotNull(authority);
    }

    @Test
    public void convert_Valid(){
        Authority authority = new AuthorityImpl();
        authority.setAuthority("asd;lkfjlkj");
        authority.setDefaultForNewUser(true);

        MongoDbAuthority converted = authorityConverter.convert(authority);
        assertNotNull(converted.getAuthority());
        assertNotNull(converted.getId());
        assertThat(converted.getAuthority(), is(sameInstance(authority.getAuthority())));
        assertThat(converted.isDefaultForNewUser(), is(sameInstance(authority.isDefaultForNewUser())));

        Authority authorityMongo = new MongoDbAuthority();
        authorityMongo.setAuthority("authority");
        authorityMongo.setDefaultForNewUser(true);
        MongoDbAuthority mongoConverted = authorityConverter.convert(authorityMongo);
        assertThat(mongoConverted, is(sameInstance(authorityMongo)));
        assertThat(mongoConverted.getAuthority(), is(sameInstance(authorityMongo.getAuthority())));
        assertThat(mongoConverted.isDefaultForNewUser(), is(sameInstance(authorityMongo.isDefaultForNewUser())));
    }

    @Test
    public void getSourceType_Valid(){
        assertNotNull(authorityConverter.getSourceType());
    }

}
