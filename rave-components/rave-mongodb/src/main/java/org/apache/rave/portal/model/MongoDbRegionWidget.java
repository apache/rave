package org.apache.rave.portal.model;


import org.apache.rave.portal.model.impl.RegionWidgetImpl;
import org.apache.rave.portal.repository.WidgetRepository;

public class MongoDbRegionWidget extends RegionWidgetImpl {
    private long widgetId;
    private WidgetRepository widgetRepository;

    public long getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(long widgetId) {
        this.widgetId = widgetId;
    }

    public WidgetRepository getWidgetRepository() {
        return widgetRepository;
    }

    public void setWidgetRepository(WidgetRepository widgetRepository) {
        this.widgetRepository = widgetRepository;
    }

    @Override
    public Widget getWidget() {
        Widget widget = super.getWidget();
        if(widget == null) {
            widget = widgetRepository.get(widgetId);
            super.setWidget(widget);
        }
        return widget;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegionWidget)) return false;
        RegionWidget that = (RegionWidget) o;
        return !(this.getId() != null ? !this.getId().equals(that.getId()) : that.getId() != null);

    }

    @Override
    public int hashCode() {
        return this.getId() != null ? this.getId().hashCode() : 0;
    }


}
