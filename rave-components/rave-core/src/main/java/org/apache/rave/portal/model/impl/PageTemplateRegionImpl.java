package org.apache.rave.portal.model.impl;

import org.apache.rave.portal.model.*;

import java.util.List;

public class PageTemplateRegionImpl implements PageTemplateRegion {
    private Long id;
    private long renderSequence;
    private PageTemplate pageTemplate;
    private List<PageTemplateWidget> pageTemplateWidgets;
    private boolean locked;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getRenderSequence() {
        return renderSequence;
    }

    public void setRenderSequence(long renderSequence) {
        this.renderSequence = renderSequence;
    }

    public PageTemplate getPageTemplate() {
        return pageTemplate;
    }

    public void setPageTemplate(PageTemplate pageTemplate) {
        this.pageTemplate = pageTemplate;
    }

    public List<PageTemplateWidget> getPageTemplateWidgets() {
        return pageTemplateWidgets;
    }

    public void setPageTemplateWidgets(List<PageTemplateWidget> pageTemplateWidgets) {
        this.pageTemplateWidgets = pageTemplateWidgets;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PageTemplateRegionImpl)) return false;

        PageTemplateRegionImpl that = (PageTemplateRegionImpl) o;

        if (locked != that.locked) return false;
        if (renderSequence != that.renderSequence) return false;
        if (pageTemplate != null ? !pageTemplate.equals(that.pageTemplate) : that.pageTemplate != null) return false;
        if (pageTemplateWidgets != null ? !pageTemplateWidgets.equals(that.pageTemplateWidgets) : that.pageTemplateWidgets != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (renderSequence ^ (renderSequence >>> 32));
        result = 31 * result + (pageTemplate != null ? pageTemplate.hashCode() : 0);
        result = 31 * result + (pageTemplateWidgets != null ? pageTemplateWidgets.hashCode() : 0);
        result = 31 * result + (locked ? 1 : 0);
        return result;
    }
}
