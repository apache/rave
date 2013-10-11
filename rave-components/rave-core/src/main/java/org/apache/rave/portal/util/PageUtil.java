package org.apache.rave.portal.util;


import org.apache.rave.model.*;
import org.apache.rave.portal.model.impl.PageImpl;
import org.apache.rave.portal.model.impl.PageUserImpl;
import org.apache.rave.portal.model.impl.RegionImpl;
import org.apache.rave.portal.model.impl.RegionWidgetImpl;

import java.util.ArrayList;
import java.util.List;

public class PageUtil {
    private PageUtil() {}

    /**
     * Converts a PageTemplate to an instance of a Page
     *
     * @param template template
     * @param owner owner of the new page
     * @param createIds optionally set a negative ID as a relative reference
     * @return
     */
    public static Page convert(PageTemplate template, User owner, boolean createIds) {
        Page p = new PageImpl();
        if(createIds) {
            p.setId(String.valueOf(-1L));
        }
        p.setName(template.getName());
        p.setPageType(template.getPageType());
        p.setOwnerId(owner.getId());
        PageUser pageUser = new PageUserImpl(owner.getId(), p, template.getRenderSequence());
        pageUser.setPageStatus(PageInvitationStatus.OWNER);
        pageUser.setEditor(true);
        List<PageUser> members = new ArrayList<PageUser>();
        members.add(pageUser);
        p.setMembers(members);

        p.setPageLayout(template.getPageLayout());
        p.setRegions(convertRegions(template.getPageTemplateRegions(), p, createIds));
        p.setSubPages(convertPages(template.getSubPageTemplates(), p, createIds));
        return p;

    }

    /**
     * convertRegions: List of PageTemplateRegion, Page -> List of Regions
     * Converts the JpaRegion Templates of the Page Template to Regions for the page
     *
     * @param pageTemplateRegions List of PageTemplateRegion
     * @param page Page
     * @param createIds
     * @return list of JpaRegion
     */
    private static  List<Region> convertRegions(List<PageTemplateRegion> pageTemplateRegions, Page page, boolean createIds){
        List<Region> regions = new ArrayList<Region>();
        int idx = 0;
        for (PageTemplateRegion ptr : pageTemplateRegions){
            RegionImpl region = new RegionImpl();
            if(createIds) {
                region.setId(String.valueOf(--idx));
            }
            region.setRenderOrder((int) ptr.getRenderSequence());
            region.setPage(page);
            region.setLocked(ptr.isLocked());
            region.setRegionWidgets(convertWidgets(ptr.getPageTemplateWidgets(), region, createIds));
            regions.add(region);
        }
        return regions;
    }

    /**
     * convertWidgets: List of PageTemplateWidget, JpaRegion -> List of RegionWidget
     * Converts the Page Template Widgets to RegionWidgets for the given JpaRegion
     *
     * @param pageTemplateWidgets List of PageTemplateWidget
     * @param region JpaRegion
     * @param createIds
     * @return List of RegionWidget
     */
    private static  List<RegionWidget> convertWidgets(List<PageTemplateWidget> pageTemplateWidgets, Region region, boolean createIds){
        List<RegionWidget> widgets = new ArrayList<RegionWidget>();
        int idx=createIds ? 100 * Integer.parseInt(region.getId()) : 0;
        for (PageTemplateWidget ptw : pageTemplateWidgets){
            RegionWidgetImpl regionWidget = new RegionWidgetImpl();
            if(createIds) {
                regionWidget.setId(String.valueOf(--idx));
            }
            regionWidget.setRegion(region);
            regionWidget.setCollapsed(false);
            regionWidget.setLocked(ptw.isLocked());
            regionWidget.setHideChrome(ptw.isHideChrome());
            regionWidget.setRenderOrder((int) ptw.getRenderSeq());
            regionWidget.setWidgetId(ptw.getWidgetId());
            widgets.add(regionWidget);
        }
        return widgets;
    }

    /**
     * convertPages: List of PageTemplate, Page -> List of Page
     * Converts the template subpages in to a list of Pages for the given page object
     * This is a recursive function. A sub page could have a list of sub pages.
     *
     * @param pageTemplates List of PageTemplate
     * @param page Page
     * @param createIds
     * @return list of Page
     */
    private static  List<Page> convertPages(List<PageTemplate> pageTemplates, Page page, boolean createIds){
        List<Page> pages = new ArrayList<Page>();
        //Start at -1 since the first page is already set
        int idx=-1;
        for(PageTemplate pt : pageTemplates){
            PageImpl lPage = new PageImpl();
            if(createIds) {
                lPage.setId(String.valueOf(--idx));
            }
            lPage.setName(pt.getName());
            lPage.setPageType(pt.getPageType());
            lPage.setOwnerId(page.getOwnerId());
            lPage.setPageLayout(pt.getPageLayout());
            lPage.setParentPage(page);
            lPage.setRegions(convertRegions(pt.getPageTemplateRegions(), lPage, createIds));

            // create new pageUser tuple
            PageUser pageUser = new PageUserImpl(lPage.getOwnerId(), lPage, pt.getRenderSequence());
            pageUser.setPageStatus(PageInvitationStatus.OWNER);
            pageUser.setEditor(true);
            List<PageUser> members = new ArrayList<PageUser>();
            members.add(pageUser);
            lPage.setMembers(members);
            // recursive call
            lPage.setSubPages((pt.getSubPageTemplates() == null || pt.getSubPageTemplates().isEmpty()) ? null : convertPages(pt.getSubPageTemplates(), lPage, createIds));
            pages.add(lPage);
        }
        return pages;
    }
}
