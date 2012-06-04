package org.apache.rave.portal.model;

import java.util.List;

/**
 */
public interface PageTemplateRegion {

    Long getId();

    long getRenderSequence();

    void setRenderSequence(long renderSequence);

    PageTemplate getPageTemplate();

    void setPageTemplate(PageTemplate pageTemplate);

    List<PageTemplateWidget> getPageTemplateWidgets();

    void setPageTemplateWidgets(List<PageTemplateWidget> pageTemplateWidgets);

    boolean isLocked();

    void setLocked(boolean locked);

}
