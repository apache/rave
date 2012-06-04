package org.apache.rave.portal.model.impl;

import org.apache.rave.portal.model.WidgetRating;

public class WidgetRatingImpl implements WidgetRating {

    private Long id;
    private Long widgetId;
    private Long userId;
    private Integer score;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(Long widgetId) {
        this.widgetId = widgetId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WidgetRatingImpl)) return false;

        WidgetRatingImpl that = (WidgetRatingImpl) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (score != null ? !score.equals(that.score) : that.score != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (widgetId != null ? !widgetId.equals(that.widgetId) : that.widgetId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (widgetId != null ? widgetId.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (score != null ? score.hashCode() : 0);
        return result;
    }
}
