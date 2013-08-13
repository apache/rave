/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.rave.portal.model.conversion.impl;

import org.apache.rave.model.*;
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.impl.*;
import org.apache.rave.portal.repository.PageLayoutRepository;
import org.apache.rave.portal.repository.WidgetRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: dsullivan
 * Date: 11/26/12
 * Time: 10:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class MongoDbPageTemplateConverterTest {
    private MongoDbPageTemplateConverter pageTemplateConverter;
    private PageLayoutRepository pageLayoutRepository;
    private WidgetRepository widgetRepository;

    @Before
    public void setup() {
        pageLayoutRepository = createMock(PageLayoutRepository.class);
        widgetRepository = createMock(WidgetRepository.class);
        pageTemplateConverter = new MongoDbPageTemplateConverter();
        pageTemplateConverter.setPageLayoutRepository(pageLayoutRepository);
    }

    @Test
    public void getSourceType_Valid() {
        assertThat(pageTemplateConverter.getSourceType(), equalTo(PageTemplate.class));
    }

    @Test
    public void hydrate_Valid() {
        MongoDbPageTemplate mongoDbPageTemplate = new MongoDbPageTemplate();
        PageTemplateRegion ptRegion1 = new PageTemplateRegionImpl();
        PageTemplateRegion ptRegion2 = new PageTemplateRegionImpl();
        PageTemplateRegion ptRegion3 = new PageTemplateRegionImpl();
        PageTemplateRegion ptRegion4 = new PageTemplateRegionImpl();
        List<PageTemplateRegion> pageTemplateRegions1 = Arrays.asList(
                ptRegion1,
                ptRegion2
        );
        List<PageTemplateRegion> pageTemplateRegions2 = Arrays.asList(
                ptRegion3
        );
        List<PageTemplateRegion> pageTemplateRegions3 = Arrays.asList(
                ptRegion4
        );
        mongoDbPageTemplate.setPageTemplateRegions(pageTemplateRegions1);
        PageTemplateWidget widget1 = new PageTemplateWidgetImpl();
        PageTemplateWidget widget2 = new PageTemplateWidgetImpl();
        PageTemplateWidget widget3 = new PageTemplateWidgetImpl();
        PageTemplateWidget widget4 = new PageTemplateWidgetImpl();
        PageTemplateWidget widget5 = new PageTemplateWidgetImpl();

        PageTemplate template1 = new MongoDbPageTemplate();
        PageTemplate template2 = new MongoDbPageTemplate();

        List<PageTemplateWidget> pageTemplateWidgets1 = Arrays.asList(
                widget1,
                widget2
        );
        List<PageTemplateWidget> pageTemplateWidgets2 = Arrays.asList(
                widget3
        );
        List<PageTemplateWidget> pageTemplateWidgets3 = Arrays.asList(
                widget4
        );
        List<PageTemplateWidget> pageTemplateWidgets4 = Arrays.asList(
                widget5
        );
        List<PageTemplate> pageTemplates = Arrays.asList(
                template1,
                template2
        );
        ptRegion1.setPageTemplateWidgets(pageTemplateWidgets1);
        ptRegion2.setPageTemplateWidgets(pageTemplateWidgets2);
        ptRegion3.setPageTemplateWidgets(pageTemplateWidgets3);
        ptRegion4.setPageTemplateWidgets(pageTemplateWidgets4);
        template1.setPageTemplateRegions(pageTemplateRegions2);
        template2.setPageTemplateRegions(pageTemplateRegions3);
        mongoDbPageTemplate.setSubPageTemplates(pageTemplates);

        pageTemplateConverter.hydrate(mongoDbPageTemplate);

        assertThat(mongoDbPageTemplate.getPageLayoutRepository(), is(sameInstance(pageLayoutRepository)));
        assertThat((MongoDbPageTemplate) ptRegion1.getPageTemplate(), is(sameInstance(mongoDbPageTemplate)));
        assertThat((MongoDbPageTemplate) ptRegion2.getPageTemplate(), is(sameInstance(mongoDbPageTemplate)));
        assertThat(widget1.getPageTemplateRegion(), is(sameInstance(ptRegion1)));
        assertThat(widget2.getPageTemplateRegion(), is(sameInstance(ptRegion1)));
        assertThat(widget3.getPageTemplateRegion(), is(sameInstance(ptRegion2)));
        assertThat(widget4.getPageTemplateRegion(), is(sameInstance(ptRegion3)));
        assertThat(widget5.getPageTemplateRegion(), is(sameInstance(ptRegion4)));

        assertThat((MongoDbPageTemplate) template1.getParentPageTemplate(), is(sameInstance(mongoDbPageTemplate)));
        assertThat((MongoDbPageTemplate) template2.getParentPageTemplate(), is(sameInstance(mongoDbPageTemplate)));
    }

    @Test
    public void hydrate_Null(){
        pageTemplateConverter.hydrate(null);
        assertThat(true, is(true));
    }

    @Test
    public void convert_Not_InstanceOf(){
        PageTemplate pageTemplate = new PageTemplateImpl();
        pageTemplate.setPageLayout(new PageLayoutImpl());
        MongoDbPageTemplate subPage = new MongoDbPageTemplate();
        subPage.setPageLayout(new PageLayoutImpl());
        ArrayList<PageTemplate> subPages = new ArrayList<PageTemplate>();
        subPages.add(subPage);
        pageTemplate.setSubPageTemplates(subPages);
        PageTemplateRegion region = createNewPageTemplateRegion();
        ArrayList<PageTemplateRegion> regions = new ArrayList<PageTemplateRegion>();
        regions.add(region);
        pageTemplate.setPageTemplateRegions(regions);

        MongoDbPageTemplate converted = pageTemplateConverter.convert(pageTemplate);

        assertNotNull(converted.getId());
        assertNotNull(converted.getPageTemplateRegions());
        assertNotNull(converted.getSubPageTemplates());
    }

    private PageTemplateRegion createNewPageTemplateRegion() {
        return new PageTemplateRegion() {
                @Override
                public String getId() {
                    return null;  //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public long getRenderSequence() {
                    return 0;  //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void setRenderSequence(long renderSequence) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public PageTemplate getPageTemplate() {
                    return null;  //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void setPageTemplate(PageTemplate pageTemplate) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public List<PageTemplateWidget> getPageTemplateWidgets() {
                    return null;  //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void setPageTemplateWidgets(List<PageTemplateWidget> pageTemplateWidgets) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public boolean isLocked() {
                    return false;  //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void setLocked(boolean locked) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }
            };
    }

    @Test
    public void convert_PageTemplateWidgets_Null(){
        PageTemplate pageTemplate = new PageTemplateImpl();
        pageTemplate.setPageLayout(new PageLayoutImpl());
        PageTemplateRegion pageTemplateRegion = new PageTemplateRegionImpl();
        pageTemplate.setPageTemplateRegions(Arrays.asList(pageTemplateRegion));

        MongoDbPageTemplate converted = pageTemplateConverter.convert(pageTemplate);

        assertNotNull(converted.getId());
    }

    @Test
    public void convert_Valid(){

        PageTemplate mongoPageTemplate1 = new MongoDbPageTemplate();
        PageTemplate mongoPageTemplate2 = new MongoDbPageTemplate();
        PageLayoutRepository tempPageLayoutRepository = createMock(PageLayoutRepository.class);
        WidgetRepository tempWidgetRepository = createMock(WidgetRepository.class);
        ((MongoDbPageTemplate)mongoPageTemplate1).setPageLayoutRepository(tempPageLayoutRepository);
        ((MongoDbPageTemplate)mongoPageTemplate2).setPageLayoutRepository(tempPageLayoutRepository);
        PageTemplateRegion pageTemplateRegion = new PageTemplateRegionImpl();
        PageTemplateWidget pageTemplateWidget = new PageTemplateWidgetImpl();

        //the source PageTemplate tested in the first recursion
        ((MongoDbPageTemplate)mongoPageTemplate1).setId("1234");
        mongoPageTemplate1.setName("Blah");
        mongoPageTemplate1.setDescription("Blahty Blahty Blah");
        mongoPageTemplate1.setPageType("user");
        MongoDbPageLayout mongoDbPageLayout1 = new MongoDbPageLayout();
        mongoDbPageLayout1.setId("7777");
        mongoDbPageLayout1.setCode("4321");
        mongoPageTemplate1.setPageLayout(mongoDbPageLayout1);
        mongoPageTemplate1.setRenderSequence(9999);
        mongoPageTemplate1.setDefaultTemplate(true);

        //the PageTemplate to be inserted into the subPageTemplates array, tested in the second recursion loop
        ((MongoDbPageTemplate)mongoPageTemplate2).setId("3232");
        mongoPageTemplate2.setName("Yeah");
        mongoPageTemplate2.setDescription("Yeah Yeah Ya");
        mongoPageTemplate2.setPageType("user");
        MongoDbPageLayout mongoDbPageLayout2 = new MongoDbPageLayout();
        mongoDbPageLayout2.setId("8888");
        mongoDbPageLayout2.setCode("2345");
        mongoPageTemplate2.setPageLayout(mongoDbPageLayout2);
        mongoPageTemplate2.setRenderSequence(8787);
        mongoPageTemplate2.setDefaultTemplate(true);

        //the PageTemplateWidget to be add as a field to the pageTemplateWidgets array of the PageTemplateRegion
        ((PageTemplateWidgetImpl)pageTemplateWidget).setId("3333");
        pageTemplateWidget.setHideChrome(true);
        pageTemplateWidget.setRenderSeq(3456);
        Widget widget = new WidgetImpl();
        ((WidgetImpl)widget).setId("87623876");
        ((PageTemplateWidgetImpl)pageTemplateWidget).setWidgetId("4444");
        pageTemplateWidget.setLocked(true);

        //the PageTemplateRegion to be converted
        ((PageTemplateRegionImpl)pageTemplateRegion).setId("2929");
        pageTemplateRegion.setRenderSequence(56376);
        pageTemplateRegion.setLocked(true);

        //create and add subPageTemplate array to the first PageTemplate
        List<PageTemplate> subPageTemplates = Arrays.asList(
                (PageTemplate)mongoPageTemplate2
        );
        mongoPageTemplate1.setSubPageTemplates(subPageTemplates);

        //create and add pageTemplateWidgets to the PageTemplateRegion
        List<PageTemplateWidget> pageTemplateWidgets = Arrays.asList(
              pageTemplateWidget
        );
         pageTemplateRegion.setPageTemplateWidgets(pageTemplateWidgets);

        //create and add pageTemplateRegions to the first PageTemplate
        List<PageTemplateRegion> pageTemplateRegions = Arrays.asList(
            pageTemplateRegion
        );
        mongoPageTemplate1.setPageTemplateRegions(pageTemplateRegions);

        expect(tempPageLayoutRepository.getByPageLayoutCode("4321")).andReturn(mongoDbPageLayout1);
        expect(tempPageLayoutRepository.getByPageLayoutCode("2345")).andReturn(mongoDbPageLayout2);
        expect(tempWidgetRepository.get("4444")).andReturn(widget);
        replay(tempPageLayoutRepository, tempWidgetRepository);

        MongoDbPageTemplate convertedTemplate = pageTemplateConverter.convert(mongoPageTemplate1);

        //test convert made in first convert call
        assertNotNull(convertedTemplate.getId());
        assertThat(convertedTemplate.getName(), is(mongoPageTemplate1.getName()));
        assertThat(convertedTemplate.getDescription(), is(mongoPageTemplate1.getDescription()));
        assertThat(convertedTemplate.getPageType(), is(mongoPageTemplate1.getPageType()));
        assertNull(convertedTemplate.getParentPageTemplate());
        assertThat(convertedTemplate.getPageLayoutCode(), is(mongoPageTemplate1.getPageLayout().getCode()));
        //assertNotNull(mongoDbPageTemplate2.getPageLayout());//This should be testing to see that the PageLayout is being properly set
        assertThat(convertedTemplate.getRenderSequence(), is(mongoPageTemplate1.getRenderSequence()));
        assertThat(convertedTemplate.isDefaultTemplate(), is(mongoPageTemplate1.isDefaultTemplate()));

        //test converts made in second subPageTemplate loop
        for(int i = 0; i< convertedTemplate.getSubPageTemplates().size(); i++){
            PageTemplate source = mongoPageTemplate1.getSubPageTemplates().get(i);
            PageTemplate converted = convertedTemplate.getSubPageTemplates().get(i);

            assertNotNull(converted.getId());
            assertThat(converted.getName(), is(source.getName()));
            assertThat(converted.getDescription(), is(source.getDescription()));
            assertThat(converted.getPageType(), is(source.getPageType()));
            assertNull(converted.getParentPageTemplate());
            assertThat(((MongoDbPageTemplate)converted).getPageLayoutCode(), is(source.getPageLayout().getCode()));
            //assertNotNull(converted.getPageLayout());//This should be testing to see that the PageLayout is being properly set
            assertThat(converted.getRenderSequence(), is(source.getRenderSequence()));
            assertThat(converted.isDefaultTemplate(), is(source.isDefaultTemplate()));
        }

        //test converts made in the PageTemplateRegions loop
        for(int i = 0; i< convertedTemplate.getPageTemplateRegions().size(); i++){
            PageTemplateRegion source = mongoPageTemplate1.getPageTemplateRegions().get(i);
            PageTemplateRegion converted = convertedTemplate.getPageTemplateRegions().get(i);

            assertThat(converted.getId(), is(source.getId()));
            assertThat(converted.getRenderSequence(), is(source.getRenderSequence()));
            //assertNotNull(converted.getPageTemplate()); // this should test to make sure the PageTemplate was set
            assertThat(converted.isLocked(), is(source.isLocked()));

            //test converts made in the PageTemplateWidgets loop
            for(int j = 0; j<converted.getPageTemplateWidgets().size(); j++){
                PageTemplateWidget widgetSource = source.getPageTemplateWidgets().get(j);
                PageTemplateWidget widgetConverted = converted.getPageTemplateWidgets().get(j);

                assertThat(widgetConverted.getId(), is(widgetSource.getId()));
                assertThat(widgetConverted.isHideChrome(), is(widgetSource.isHideChrome()));
                assertThat(widgetConverted.getRenderSeq(), is(widgetSource.getRenderSeq()));
                assertThat(((PageTemplateWidgetImpl)widgetConverted).getWidgetId(), is(((PageTemplateWidgetImpl)widgetSource).getWidgetId()));
                //assertNotNull(widgetConverted.getWidget());//This should test to make sure the WidgetId is set
                assertThat(widgetConverted.isLocked(), is(widgetSource.isLocked()));
            }
        }
    }

    @Test
    public void convert_Valid_Null_WidgetID(){
        PageTemplate source = new PageTemplateImpl();
        PageTemplateRegion pageTemplateRegion = new PageTemplateRegionImpl();
        source.setPageTemplateRegions(Arrays.asList(pageTemplateRegion));
        PageTemplateWidget pageTemplateWidget = new PageTemplateWidgetImpl();
        pageTemplateRegion.setPageTemplateWidgets(Arrays.asList(pageTemplateWidget));
        source.setPageLayout(new PageLayoutImpl());

        MongoDbPageTemplate converted = pageTemplateConverter.convert(source);
        assertTrue(converted instanceof MongoDbPageTemplate);
    }

}
