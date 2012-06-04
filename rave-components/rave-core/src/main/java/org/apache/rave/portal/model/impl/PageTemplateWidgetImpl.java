package org.apache.rave.portal.model.impl;


import org.apache.rave.portal.model.*;

public class PageTemplateWidgetImpl implements PageTemplateWidget {
    private PageTemplateRegion pageTemplateRegion;
    private long renderSequence;
    private Widget widget;
    private boolean locked;
    private boolean hideChrome;

    public PageTemplateRegion getPageTemplateRegion() {
        return pageTemplateRegion;
    }

    public void setPageTemplateRegion(PageTemplateRegion pageTemplateRegion) {
        this.pageTemplateRegion = pageTemplateRegion;
    }

    public long getRenderSeq() {
        return renderSequence;
    }

    public void setRenderSeq(long renderSequence) {
        this.renderSequence = renderSequence;
    }

    public Widget getWidget() {
        return widget;
    }

    public void setWidget(Widget widget) {
        this.widget = widget;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isHideChrome() {
        return hideChrome;
    }

    public void setHideChrome(boolean hideChrome) {
        this.hideChrome = hideChrome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PageTemplateWidgetImpl)) return false;

        PageTemplateWidgetImpl that = (PageTemplateWidgetImpl) o;

        if (hideChrome != that.hideChrome) return false;
        if (locked != that.locked) return false;
        if (renderSequence != that.renderSequence) return false;
        if (pageTemplateRegion != null ? !pageTemplateRegion.equals(that.pageTemplateRegion) : that.pageTemplateRegion != null)
            return false;
        if (widget != null ? !widget.equals(that.widget) : that.widget != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pageTemplateRegion != null ? pageTemplateRegion.hashCode() : 0;
        result = 31 * result + (int) (renderSequence ^ (renderSequence >>> 32));
        result = 31 * result + (widget != null ? widget.hashCode() : 0);
        result = 31 * result + (locked ? 1 : 0);
        result = 31 * result + (hideChrome ? 1 : 0);
        return result;
    }
}
