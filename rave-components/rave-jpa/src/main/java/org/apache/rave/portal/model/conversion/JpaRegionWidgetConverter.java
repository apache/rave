package org.apache.rave.portal.model.conversion;

import org.apache.rave.model.ModelConverter;
import org.apache.rave.portal.model.JpaRegionWidget;
import org.apache.rave.portal.model.RegionWidget;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;

@Component
public class JpaRegionWidgetConverter implements ModelConverter<RegionWidget, JpaRegionWidget> {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public Class<RegionWidget> getSourceType() {
        return RegionWidget.class;
    }

    @Override
    public JpaRegionWidget convert(RegionWidget source) {
        return source instanceof JpaRegionWidget ? (JpaRegionWidget) source : createEntity(source);
    }

    private JpaRegionWidget createEntity(RegionWidget source) {
        JpaRegionWidget converted = null;
        if (source != null) {
            TypedQuery<JpaRegionWidget> query = manager.createNamedQuery(JpaRegionWidget.FIND_BY_ID, JpaRegionWidget.class);
            query.setParameter(JpaRegionWidget.PARAM_WIDGET_ID, source.getId());
            converted = getSingleResult(query.getResultList());

            if (converted == null) {
                converted = new JpaRegionWidget();
            }
            updateProperties(source, converted);
        }
        return converted;
    }

    private void updateProperties(RegionWidget source, JpaRegionWidget converted) {
        converted.setId(source.getId());
        converted.setLocked(source.isLocked());
        converted.setCollapsed(source.isCollapsed());
        converted.setHideChrome(source.isHideChrome());
        converted.setPreferences(source.getPreferences());
        converted.setRegion(source.getRegion());
        converted.setRenderPosition(source.getRenderPosition());
        converted.setWidget(source.getWidget());
        converted.setRenderOrder(source.getRenderOrder());
    }
}
