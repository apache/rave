package org.apache.rave.portal.repository.impl;

import com.google.common.collect.Lists;
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.impl.*;
import org.apache.rave.portal.repository.PageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: mfranklin
 * Date: 10/14/12
 * Time: 8:14 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-applicationContext.xml"})
public class MongoDbPageRepositoryTest {

    @Autowired
    PageRepository repository;

    @Test
    public void MongoTest() {
        Page page = new PageImpl();
        PageUser p = new PageUserImpl(new UserImpl(12345L), page);
        page.setName("PAGE NAME");
        page.setMembers(Lists.<PageUser>newLinkedList());
        page.getMembers().add(p);
        page.setOwner(new UserImpl(123456L));
        page.setRegions(Lists.<Region>newLinkedList());

        Region region = new RegionImpl();
        region.setPage(page);
        region.setRegionWidgets(Lists.<RegionWidget>newLinkedList());
        page.getRegions().add(region);

        RegionWidget regionWidget = new RegionWidgetImpl();
        regionWidget.setRegion(region);
        regionWidget.setPreferences(Lists.<RegionWidgetPreference>newLinkedList());
        region.getRegionWidgets().add(regionWidget);

        RegionWidgetPreference preference = new RegionWidgetPreferenceImpl();
        preference.setName("PREF NAME");
        preference.setValue("PREF_VALUE");
        regionWidget.getPreferences().add(preference);

        Widget widget = new WidgetImpl(13223L);
        regionWidget.setWidget(widget);

        page.setPageType(PageType.USER);
        page.setPageLayout(new PageLayoutImpl("LAYOUT"));

        Page saved = repository.save(page);
        assertThat(saved, instanceOf(MongoDbPage.class));

        Page fromDb = repository.get(saved.getId());
        assertThat(fromDb.getMembers().get(0), is(equalTo(saved.getMembers().get(0))));
        assertThat(fromDb.getRegions().get(0), is(equalTo(saved.getRegions().get(0))));
        assertThat(fromDb.getRegions().get(0).getRegionWidgets().get(0), is(equalTo(saved.getRegions().get(0).getRegionWidgets().get(0))));
        assertThat(fromDb.getRegions().get(0).getRegionWidgets().get(0).getPreferences().get(0), is(equalTo(saved.getRegions().get(0).getRegionWidgets().get(0).getPreferences().get(0))));
    }
}
