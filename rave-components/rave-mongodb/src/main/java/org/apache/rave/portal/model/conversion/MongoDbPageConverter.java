package org.apache.rave.portal.model.conversion;

import com.google.common.collect.Lists;
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.repository.PageLayoutRepository;
import org.apache.rave.portal.repository.UserRepository;
import org.apache.rave.portal.repository.WidgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.apache.rave.portal.model.util.MongoDbModelUtil.generateId;

@Component
public class MongoDbPageConverter implements HydratingModelConverter<Page, MongoDbPage> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WidgetRepository widgetRepository;

    @Autowired
    private PageLayoutRepository pageLayoutRepository;

    @Override
    public Class<Page> getSourceType() {
        return Page.class;
    }

    @Override
    public MongoDbPage convert(Page sourcePage) {
        MongoDbPage page = sourcePage instanceof MongoDbPage ? (MongoDbPage) sourcePage : new MongoDbPage();
        page.setId(page.getId() == null ? generateId() : sourcePage.getId());
        page.setOwnerId(sourcePage.getOwner().getId());
        page.setPageLayoutCode(sourcePage.getPageLayout().getCode());
        page.setName(sourcePage.getName());
        page.setRegions(sourcePage.getRegions());

        page.setOwner(null);
        page.setPageLayout(null);
        page.setParentPage(null);
        page.setPageLayoutRepository(null);
        page.setUserRepository(null);

        for (PageUser user : sourcePage.getMembers()) {
            List<PageUser> convertedMembers = Lists.newLinkedList();
            convertedMembers.add(convert(user));
            page.setMembers(convertedMembers);
        }
        //No need to convert regions to anything special at this time
        for (Region region : page.getRegions()) {
            if (region.getId() == null) {
                region.setId(generateId());
            }
            region.setPage(null);
            manipulateRegion(region, false);
        }
        if (page.getSubPages() != null) {
            for (Page subPage : sourcePage.getSubPages()) {
                List<Page> convertedPages = Lists.newArrayList();
                convertedPages.add(convert(subPage));
                page.setSubPages(convertedPages);
            }
        }
        return page;
    }

    public MongoDbPageUser convert(PageUser sourceUser) {
        MongoDbPageUser user = sourceUser instanceof MongoDbPageUser ? (MongoDbPageUser) sourceUser : new MongoDbPageUser();
        user.setId(sourceUser.getId() == null ? generateId() : sourceUser.getId());
        user.setUserId(sourceUser.getUser().getId());
        user.setPage(null);
        user.setUser(null);
        user.setUserRepository(null);
        return user;
    }

    @Override
    public void hydrate(MongoDbPage page) {
        page.setPageLayoutRepository(pageLayoutRepository);
        page.setUserRepository(userRepository);

        for (PageUser user : page.getMembers()) {
            user.setPage(page);
            if (user instanceof MongoDbPageUser) {
                hydrate((MongoDbPageUser) user);
            }
        }
        for (Region region : page.getRegions()) {
            region.setPage(page);
            manipulateRegion(region, true);
        }
        if (page.getSubPages() != null) {
            for (Page subPage : page.getSubPages()) {
                subPage.setParentPage(page);
                if (subPage instanceof MongoDbPage) {
                    hydrate((MongoDbPage) subPage);
                }
            }
        }
    }


    public void hydrate(MongoDbPageUser user) {
        user.setUserRepository(userRepository);
    }

    public void hydrate(MongoDbRegionWidget widget, Region region) {
        widget.setRegion(region);
        widget.setWidgetRepository(widgetRepository);
    }

    public MongoDbRegionWidget convert(RegionWidget sourceRegionWidget) {
        MongoDbRegionWidget regionWidget = sourceRegionWidget instanceof MongoDbRegionWidget ? (MongoDbRegionWidget) sourceRegionWidget : new MongoDbRegionWidget();
        regionWidget.setId(sourceRegionWidget.getId() == null ? generateId() : sourceRegionWidget.getId());
        regionWidget.setWidgetId(sourceRegionWidget.getWidget().getId());
        regionWidget.setWidget(null);
        regionWidget.setWidgetRepository(null);
        regionWidget.setPreferences(sourceRegionWidget.getPreferences());
        for (RegionWidgetPreference preference : regionWidget.getPreferences()) {
            preference.setRegionWidgetId(regionWidget.getId());
        }
        return regionWidget;
    }

    private void manipulateRegion(Region region, Boolean hydrate) {
        for (RegionWidget regionWidget : region.getRegionWidgets()) {
            if (hydrate) {
                hydrate((MongoDbRegionWidget) regionWidget, region);
            } else {
                region.getRegionWidgets().remove(regionWidget);
                region.getRegionWidgets().add(convert(regionWidget));
            }
        }
    }


}
