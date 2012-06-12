package org.apache.rave.portal.model.conversion;

import org.apache.rave.portal.model.JpaOAuthConsumerStore;
import org.apache.rave.portal.model.OAuthConsumerStore;
import org.apache.rave.portal.model.impl.OAuthConsumerStoreImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaOAuthConsumerStoreConverterTest {

    @Autowired
    private JpaOAuthConsumerStoreConverter oAuthConsumerStoreConverter;

    @Test
    public void noConversion() {
        OAuthConsumerStore oAuthConsumerStore = new JpaOAuthConsumerStore();
        assertThat(oAuthConsumerStoreConverter.convert(oAuthConsumerStore), is(sameInstance(oAuthConsumerStore)));
    }

    @Test
    public void nullConversion() {
        OAuthConsumerStore oAuthConsumerStore = null;
        assertThat(oAuthConsumerStoreConverter.convert(oAuthConsumerStore), is(nullValue()));
    }

    @Test
    public void newOAuthConsumerStore() {
        OAuthConsumerStore oAuthConsumerStore = new OAuthConsumerStoreImpl();
        oAuthConsumerStore.setId(1L);
        oAuthConsumerStore.setServiceName("servicename");
        oAuthConsumerStore.setCallbackUrl("callbackurl");
        oAuthConsumerStore.setConsumerKey("key");
        oAuthConsumerStore.setConsumerSecret("secret");
        oAuthConsumerStore.setGadgetUri("gadgeturi");
        oAuthConsumerStore.setKeyName("key");
        oAuthConsumerStore.setKeyType(OAuthConsumerStore.KeyType.HMAC_SYMMETRIC);

        JpaOAuthConsumerStore converted = oAuthConsumerStoreConverter.convert(oAuthConsumerStore);
        assertThat(converted, is(not(sameInstance(oAuthConsumerStore))));
        assertThat(converted, is(instanceOf(JpaOAuthConsumerStore.class)));
        assertThat(converted.getId(), is(equalTo(oAuthConsumerStore.getId())));
        assertThat(converted.getServiceName(), is(equalTo(oAuthConsumerStore.getServiceName())));
        assertThat(converted.getCallbackUrl(), is(equalTo(oAuthConsumerStore.getCallbackUrl())));
        assertThat(converted.getConsumerKey(), is(equalTo(oAuthConsumerStore.getConsumerKey())));
        assertThat(converted.getConsumerSecret(), is(equalTo(oAuthConsumerStore.getConsumerSecret())));
        assertThat(converted.getEntityId(), is(equalTo(oAuthConsumerStore.getId())));
        assertThat(converted.getGadgetUri(), is(equalTo(oAuthConsumerStore.getGadgetUri())));
        assertThat(converted.getKeyName(), is(equalTo(oAuthConsumerStore.getKeyName())));
        assertThat(converted.getKeyType(), is(equalTo(oAuthConsumerStore.getKeyType())));
    }
}