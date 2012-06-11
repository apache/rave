package org.apache.rave.portal.model.impl;


import org.apache.rave.portal.model.RegionWidgetPreference;

public class RegionWidgetPreferenceImpl implements RegionWidgetPreference {

    private Long regionWidgetId;
    private String name;
    private String value;

    public RegionWidgetPreferenceImpl() { }

    public RegionWidgetPreferenceImpl(Long regionWidgetId, String name, String value) {
        this.regionWidgetId = regionWidgetId;
        this.name = name;
        this.value = value;
    }

    public Long getRegionWidgetId() {
        return regionWidgetId;
    }

    public void setRegionWidgetId(Long regionWidgetId) {
        this.regionWidgetId = regionWidgetId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RegionWidgetPreferenceImpl)) return false;

        RegionWidgetPreferenceImpl that = (RegionWidgetPreferenceImpl) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (regionWidgetId != null ? !regionWidgetId.equals(that.regionWidgetId) : that.regionWidgetId != null)
            return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = regionWidgetId != null ? regionWidgetId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
