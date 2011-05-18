/**
 * 
 */
package org.apache.rave.os;

import org.apache.shindig.protocol.conversion.BeanConverter;
import org.apache.shindig.protocol.conversion.BeanJsonConverter;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

/**
 * 
 */
public class ShindigUtil {

    class SocialApiGuiceModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(BeanConverter.class).annotatedWith(Names.named("shindig.bean.converter.json")).to(
                    BeanJsonConverter.class);
        }
    }

    public ShindigUtil() {
        Injector injector = Guice.createInjector(new SocialApiGuiceModule());
        injector.injectMembers(this);
    }

    @Inject
    @Named("shindig.bean.converter.json")
    private BeanConverter beanConverter;

    public BeanConverter getBeanConverter() {
        return beanConverter;
    }

    public void setBeanConverter(BeanConverter beanConverter) {
        this.beanConverter = beanConverter;
    }
}
