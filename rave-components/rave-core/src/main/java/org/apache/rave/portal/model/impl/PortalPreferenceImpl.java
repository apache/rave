package org.apache.rave.portal.model.impl;

import org.apache.rave.exception.NotSupportedException;
import org.apache.rave.portal.model.PortalPreference;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PortalPreferenceImpl implements PortalPreference {
    private String key;
    private List<String> values = new LinkedList<String>();

    public PortalPreferenceImpl() {}

    public PortalPreferenceImpl(String key, List<String> values) {
        this.key = key;
        this.values = values;
    }

    public PortalPreferenceImpl(String key, String values) {
        this.key = key;
        this.values.add(values);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public String getValue() {
        if (values.isEmpty()) {
            return null;
        } else if (values.size() == 1) {
            return values.get(0);
        }
        throw new NotSupportedException("Cannot return single value for a List of size " + values.size());
    }

    public void setValue(String value) {
        List<String> values = new ArrayList<String>();
        values.add(value);
        this.values = values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PortalPreferenceImpl)) return false;

        PortalPreferenceImpl that = (PortalPreferenceImpl) o;

        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        if (values != null ? !values.equals(that.values) : that.values != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (values != null ? values.hashCode() : 0);
        return result;
    }
}
