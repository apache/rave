package org.apache.rave.rest.model;

import org.apache.rave.model.RegionWidgetPreference;
import org.apache.rave.rest.model.marshall.XmlMapAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegionWidget", propOrder = {
        "id", "type", "widgetId", "widgetUrl", "regionId", "collapsed", "locked", "hideChrome", "userPrefs"
})
@XmlRootElement(name = "RegionWidget")
public class RegionWidget {

    @XmlAttribute(name="id")
    private String id;
    @XmlAttribute(name="type")
    private String type;
    @XmlElement(name = "widgetId")
    private String widgetId;
    @XmlElement(name = "widgetUrl")
    private String widgetUrl;
    @XmlElement(name = "regionId")
    private String regionId;
    @XmlElement(name = "collapsed")
    private boolean collapsed;
    @XmlElement(name = "locked")
    private boolean locked;
    @XmlElement(name = "hideChrome")
    private boolean hideChrome;
    @XmlElement(name = "userPrefs")
    @XmlJavaTypeAdapter(value = XmlMapAdapter.class, type=Map.class)
    private Map<String, String> userPrefs;

    public RegionWidget() {  }

    public RegionWidget(org.apache.rave.model.RegionWidget widget) {
        this.id = widget.getId();
        this.widgetId = widget.getWidgetId();
        this.regionId = widget.getRegion().getId();
        this.collapsed = widget.isCollapsed();
        this.locked = widget.isLocked();
        this.hideChrome = widget.isHideChrome();
        this.userPrefs = createPrefs(widget);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(String widgetId) {
        this.widgetId = widgetId;
    }

    public String getWidgetUrl() {
        return widgetUrl;
    }

    public void setWidgetUrl(String widgetUrl) {
        this.widgetUrl = widgetUrl;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
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

    public Map getUserPrefs() {
        return userPrefs;
    }

    public void setUserPrefs(Map userPrefs) {
        this.userPrefs = userPrefs;
    }

    private Map<String, String> createPrefs(org.apache.rave.model.RegionWidget widget) {
        Map<String, String> created = null;
        List<RegionWidgetPreference> preferences = widget.getPreferences();
        if(preferences != null) {
            created = new HashMap<String, String>();
            for(RegionWidgetPreference pref : preferences) {
                created.put(pref.getName(), pref.getValue());
            }
        }
        return created;
    }
}
