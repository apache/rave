package org.apache.rave.portal.model;

/**
 */
public interface PageTemplateWidget {
    PageTemplateRegion getPageTemplateRegion();

    void setPageTemplateRegion(PageTemplateRegion pageTemplateRegion);

    long getRenderSeq();

    void setRenderSeq(long renderSeq);

    Widget getWidget();

    void setWidget(Widget widget);

    boolean isLocked();

    void setLocked(boolean locked);

    boolean isHideChrome();

    void setHideChrome(boolean hideChrome);
}
