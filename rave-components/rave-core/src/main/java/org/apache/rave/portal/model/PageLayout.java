package org.apache.rave.portal.model;

public interface PageLayout {
    /**
     * Gets the code used by the rendering engine to identify the page layout
     *
     * @return Valid code known by rendering engine
     */
    String getCode();

    void setCode(String code);

    /**
     * Gets the number of regions supported by this page layout
     *
     * @return Valid number of regions > 0
     */
    Long getNumberOfRegions();

    void setNumberOfRegions(Long numberOfRegions);

    Long getRenderSequence();

    void setRenderSequence(Long renderSequence);

    boolean isUserSelectable();

    void setUserSelectable(boolean userSelectable);
}
