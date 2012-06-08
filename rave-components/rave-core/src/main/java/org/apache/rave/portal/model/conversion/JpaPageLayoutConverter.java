package org.apache.rave.portal.model.conversion;

import org.apache.rave.model.ModelConverter;
import org.apache.rave.portal.model.JpaPageLayout;
import org.apache.rave.portal.model.PageLayout;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;

@Component
public class JpaPageLayoutConverter implements ModelConverter<PageLayout, JpaPageLayout> {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public Class<PageLayout> getSourceType() {
        return PageLayout.class;
    }

    @Override
    public JpaPageLayout convert(PageLayout source) {
        return source instanceof JpaPageLayout ? (JpaPageLayout) source : createEntity(source);
    }

    private JpaPageLayout createEntity(PageLayout source) {
        JpaPageLayout converted = null;
        if (source != null) {
            TypedQuery<JpaPageLayout> query = manager.createNamedQuery(JpaPageLayout.PAGELAYOUT_GET_BY_LAYOUT_CODE, JpaPageLayout.class);
            query.setParameter(JpaPageLayout.CODE_PARAM, source.getCode());
            converted = getSingleResult(query.getResultList());

            if (converted == null) {
                converted = new JpaPageLayout();
            }
            updateProperties(source, converted);
        }
        return converted;
    }

    private void updateProperties(PageLayout source, JpaPageLayout converted) {
        converted.setCode(source.getCode());
        converted.setNumberOfRegions(source.getNumberOfRegions());
        converted.setRenderSequence(source.getRenderSequence());
        converted.setUserSelectable(source.isUserSelectable());
    }
}
