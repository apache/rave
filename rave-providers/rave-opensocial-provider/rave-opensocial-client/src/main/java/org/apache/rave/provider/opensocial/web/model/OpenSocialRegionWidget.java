package org.apache.rave.provider.opensocial.web.model;

import org.apache.rave.rest.model.RegionWidget;

public class OpenSocialRegionWidget extends RegionWidget {
    protected String securityToken;
    protected String metadata;

    public OpenSocialRegionWidget() {  }

    public OpenSocialRegionWidget(RegionWidget base, String securityToken, String metadata) {
        this(base);
        this.securityToken = securityToken;
        this.metadata = metadata;
    }

    public OpenSocialRegionWidget(RegionWidget base) {
        this.id = base.getId();
        this.type = base.getType();
        this.widgetId = base.getWidgetId();
        this.widgetUrl = base.getWidgetUrl();
        this.regionId = base.getRegionId();
        this.collapsed = base.isCollapsed();
        this.locked = base.isLocked();
        this.hideChrome = base.isHideChrome();
        this.ownerId = base.getOwnerId();
        this.userPrefs = base.getUserPrefs();
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}
