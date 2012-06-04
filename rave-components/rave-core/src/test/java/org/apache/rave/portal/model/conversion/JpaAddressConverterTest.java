package org.apache.rave.portal.model.conversion;

import org.apache.rave.portal.model.Address;
import org.apache.rave.portal.model.impl.AddressImpl;
import org.apache.rave.portal.model.JpaAddress;
import org.apache.rave.portal.model.impl.AddressImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaAddressConverterTest {

    @Autowired
    private JpaAddressConverter addressConverter;

    @Test
    public void noConversion() {
        Address address = new JpaAddress();
        assertThat(addressConverter.convert(address), is(sameInstance(address)));
    }

    @Test
    public void newAddress() {
        Address address = new AddressImpl();
        address.setStreetAddress("1600 Pennsylvania Avenue");
        address.setLocality("Washington DC");
        address.setCountry("USA");
        address.setRegion("Washington DC");
        address.setPrimary(true);

        JpaAddress converted = addressConverter.convert(address);
        assertThat(converted, is(not(sameInstance(address))));
        assertThat(converted, is(instanceOf(JpaAddress.class)));
        assertThat(converted.getStreetAddress(), is(equalTo(address.getStreetAddress())));
        assertThat(converted.getLocality(), is(equalTo(address.getLocality())));
        assertThat(converted.getCountry(), is(equalTo(address.getCountry())));
        assertThat(converted.getRegion(), is(equalTo(address.getRegion())));
        assertThat(converted.getPrimary(), is(true));
    }
}
