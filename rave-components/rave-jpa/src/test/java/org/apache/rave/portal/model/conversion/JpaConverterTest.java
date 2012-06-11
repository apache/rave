package org.apache.rave.portal.model.conversion;

import org.apache.rave.model.ModelConverter;
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.impl.AddressImpl;
import org.apache.rave.portal.model.impl.GroupImpl;
import org.apache.rave.portal.model.impl.PersonImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-dataContext.xml", "classpath:test-applicationContext.xml"})
public class JpaConverterTest {

    private List<ModelConverter> converters;

    //Inject the list of converters from the context to work around any issues that arise during testing
    @Autowired
    private List<ModelConverter> originalConverters;

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

        ModelConverter pageLayoutConverter = createMock(ModelConverter.class);
        expect(pageLayoutConverter.getSourceType()).andReturn(Address.class).anyTimes();
        expect(pageLayoutConverter.convert(isA(PageLayout.class))).andReturn(new JpaPageLayout());
        replay(pageLayoutConverter);

        List<ModelConverter> converters = new ArrayList<ModelConverter>();
        converters.add(personConverter);
        converters.add(addressConverter);
        this.converters = converters;
        Field instance = JpaConverter.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @After
    public void tearDown() {
        //Replace the instance of converters with the one from the context
        new JpaConverter(originalConverters);
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
