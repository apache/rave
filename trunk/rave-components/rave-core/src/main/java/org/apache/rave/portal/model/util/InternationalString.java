/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.rave.portal.model.util;

import org.apache.rave.portal.util.LocalizationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A String that has been internationalized into several Locales. This class is used as a replacement for the String
 * type whenever a String needs to be i18nn-capable. An instance of the InternationalString interface composes within it
 * an ArrayList of LocalizedString instances, where each String is specific to a particular Locale.
 * 
 * Note: See also the javax.xml.registry.infomodel.InternationalString interface, which uses a similar approach
 * 
 */
public class InternationalString {

    /**
     * Default constructor
     */
    public InternationalString() {
        this.localizedStrings = new ArrayList<LocalizedString>();
    }

    /**
     * The collection of localized strings representing the different versions of the string for particular
     * language-tags.
     */
    private ArrayList<LocalizedString> localizedStrings;

    public LocalizedString getDefaultLocalizedString() {
        return LocalizationUtils.getLocalizedElement(
                this.localizedStrings.toArray(new LocalizedString[this.localizedStrings.size()]), null);
    }

    /**
     * Return the localized string that is the best match for the given locale
     * 
     * @param locale
     * @return a string best matching the locale, or null if no string matches
     */
    public LocalizedString getLocalizedString(String locale) {
        return getLocalizedString(new String[] { locale });
    }

    /**
     * Return the localized string that is the best match for the given array of locales
     * 
     * @param locales
     * @return a string best matching the locales, or null if no string matches
     */
    public LocalizedString getLocalizedString(String[] locales) {
        return LocalizationUtils.getLocalizedElement(
                this.localizedStrings.toArray(new LocalizedString[this.localizedStrings.size()]), locales);
    }

    /**
     * Returns all the localized strings for this object
     * 
     * @return a List containing localized strings
     */
    public List<LocalizedString> getLocalizedStrings() {
        return this.localizedStrings;
    }

    /**
     * Set a single localized string
     * 
     * @param localizedString
     */
    public void setLocalizedString(LocalizedString localizedString) {
        this.localizedStrings.add(localizedString);
    }

    /**
     * Adds many localized strings
     * 
     * @param localizedStrings
     */
    public void setLocalizedStrings(List<LocalizedString> localizedStrings) {
        this.localizedStrings.addAll(localizedStrings);
    }

    /**
     * Remove a single localized string
     * 
     * @param localizedString
     */
    public void removeLocalizedString(LocalizedString localizedString) {
        this.localizedStrings.remove(localizedString);
    }

    /**
     * Remove many localized strings
     * 
     * @param localizedStrings
     */
    public void removeLocalizedStrings(List<LocalizedString> localizedStrings) {
        this.localizedStrings.removeAll(localizedStrings);
    }

}
