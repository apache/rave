package org.apache.rave.portal.model;

import java.util.List;

public interface PageTemplate {
    PageType getPageType();

    void setPageType(PageType pageType);

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    PageTemplate getParentPageTemplate();

    void setParentPageTemplate(PageTemplate parentPageTemplate);

    PageLayout getPageLayout();

    void setPageLayout(PageLayout pageLayout);

    List<PageTemplateRegion> getPageTemplateRegions();

    void setPageTemplateRegions(List<PageTemplateRegion> pageTemplateRegions);

    long getRenderSequence();

    void setRenderSequence(long renderSequence);

    boolean isDefaultTemplate();

    void setDefaultTemplate(boolean defaultTemplate);

    List<PageTemplate> getSubPageTemplates();

    void setSubPageTemplates(List<PageTemplate> subPageTemplates);
}
