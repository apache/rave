package org.apache.rave.portal.model.conversion;

import org.apache.rave.model.ModelConverter;
import org.apache.rave.portal.model.JpaWidgetRating;
import org.apache.rave.portal.model.WidgetRating;
import org.springframework.stereotype.Component;

/**
 * Converts a WidgetRating to a JpaWidgetRating
 */
@Component
public class JpaWidgetRatingConverter implements ModelConverter<WidgetRating, JpaWidgetRating> {

    @Override
    public Class<WidgetRating> getSourceType() {
        return WidgetRating.class;
    }

    @Override
    public JpaWidgetRating convert(WidgetRating source) {
        return source instanceof JpaWidgetRating ? (JpaWidgetRating) source : createEntity(source);
    }

    private JpaWidgetRating createEntity(WidgetRating source) {
        JpaWidgetRating converted = null;
        if(source != null) {
            converted = new JpaWidgetRating();
            updateProperties(source, converted);
        }
        return converted;
    }

    private void updateProperties(WidgetRating source, JpaWidgetRating converted) {
        converted.setId(source.getId());
        converted.setScore(source.getScore());
        converted.setUserId(source.getUserId());
        converted.setWidgetId(source.getWidgetId());
    }
}
