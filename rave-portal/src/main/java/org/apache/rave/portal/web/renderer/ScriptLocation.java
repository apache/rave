package org.apache.rave.portal.web.renderer;

/**
 * Enumeration of potential render locations for the script block
 */
public enum ScriptLocation {
    /**
     * Renders before any third party Rave dependencies
     */
    BEFORE_LIB,
    /**
     * Renders immediately after third party Rave dependencies
     */
    AFTER_LIB,
    /**
     * Renders immediately before the Rave script blocks
     */
    BEFORE_RAVE,
    /**
     * Renders immediately after the Rave script blocks
     */
    AFTER_RAVE
}
