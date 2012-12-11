package org.apache.rave.portal.model;

import org.apache.rave.portal.model.impl.PageLayoutImpl;
import org.apache.rave.portal.repository.PageLayoutRepository;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

/**
 * User: DSULLIVAN
 * Date: 12/10/12
 * Time: 2:46 PM
 */
public class MongoDbPageTemplateTest {
    private MongoDbPageTemplate template;
    private PageLayoutRepository pageLayoutRepository;

    @Before
    public void setup(){
        template = new MongoDbPageTemplate();
        pageLayoutRepository = createMock(PageLayoutRepository.class);
        template.setPageLayoutRepository(pageLayoutRepository);
    }

    @Test
    public void getPageLayout(){
        PageLayout p = new PageLayoutImpl();
         template.setPageLayoutCode("stinky");
        expect(pageLayoutRepository.getByPageLayoutCode("stinky")).andReturn(p);
        replay(pageLayoutRepository);

        assertThat(template.getPageLayout(), is(sameInstance(p)));
    }
}
