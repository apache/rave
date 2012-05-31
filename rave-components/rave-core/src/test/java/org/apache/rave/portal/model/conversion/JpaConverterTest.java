package org.apache.rave.portal.model.conversion;

import org.apache.rave.model.ModelConverter;
import org.apache.rave.portal.model.*;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class JpaConverterTest {

    private List<ModelConverter> converters;

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        ModelConverter personConverter = createMock(ModelConverter.class);
        expect(personConverter.getSourceType()).andReturn(Person.class).anyTimes();
        expect(personConverter.convert(isA(PersonImpl.class))).andReturn(new JpaPerson());
        replay(personConverter);

        ModelConverter addressConverter = createMock(ModelConverter.class);
        expect(addressConverter.getSourceType()).andReturn(Address.class).anyTimes();
        expect(addressConverter.convert(isA(AddressImpl.class))).andReturn(new JpaAddress());
        replay(addressConverter);

        List<ModelConverter> converters = new ArrayList<ModelConverter>();
        converters.add(personConverter);
        converters.add(addressConverter);
        this.converters = converters;
        Field instance = JpaConverter.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test(expected=IllegalStateException.class)
    public void noInstance() {
        JpaConverter.getInstance();
    }

    @Test
    public void converts() {
        new JpaConverter(converters);
        assertThat(JpaConverter.getInstance().convert(new AddressImpl(), Address.class), is(instanceOf(JpaAddress.class)));
        assertThat(JpaConverter.getInstance().convert(new PersonImpl(), Person.class), is(instanceOf(JpaPerson.class)));

    }

    @Test
    public void getMap() {
        new JpaConverter(converters);
        ModelConverter<Address, JpaAddress> converter = JpaConverter.getInstance().getConverter(Address.class);
        assertThat(converter, is(not(nullValue())));
        assertThat(converter.getSourceType(), is(equalTo(Address.class)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void notFound() {
        new JpaConverter(converters);
        JpaConverter.getInstance().convert(new GroupImpl(), Group.class);
    }
}
