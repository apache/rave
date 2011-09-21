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

import com.ibm.icu.util.GlobalizationPreferences;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InternationalStringTest {

    @Test
    public void twoLanguages() {
        InternationalString internationalString = new InternationalString();
        internationalString.setLocalizedString(new LocalizedString("Hello World", "en"));
        internationalString.setLocalizedString(new LocalizedString("你好世界", "cn"));

        assertEquals("你好世界", internationalString.getLocalizedString("cn").getValue());
        assertEquals("Hello World", internationalString.getLocalizedString("en").getValue());
    }

    @Test
    public void defaultLocale() {
        GlobalizationPreferences prefs = new GlobalizationPreferences();

        InternationalString internationalString = new InternationalString();
        internationalString.setLocalizedString(new LocalizedString("Hello World", "en"));
        internationalString.setLocalizedString(new LocalizedString("Hallo Wereld", "nl"));
        internationalString.setLocalizedString(new LocalizedString("你好世界", "cn"));

        // The actual test depends on the system default locale
        String defaultLocale = prefs.getLocale(0).toLanguageTag();
        if (defaultLocale.contains("cn"))
            assertEquals("你好世界", internationalString.getDefaultLocalizedString().getValue());
        if (defaultLocale.equals("nl"))
            assertEquals("Hallo Wereld", internationalString.getDefaultLocalizedString().getValue());
        if (defaultLocale.contains("en"))
            assertEquals("Hello World", internationalString.getDefaultLocalizedString().getValue());
    }

    @Test
    public void noLocalizedStrings() {
        InternationalString internationalString = new InternationalString();
        assertEquals(null, internationalString.getLocalizedString("cn"));
    }

    @Test
    public void noMatch() {
        InternationalString internationalString = new InternationalString();
        internationalString.setLocalizedString(new LocalizedString("Hello World", "en"));
        assertEquals(null, internationalString.getLocalizedString("cn"));
    }

    /**
     * This tests that we can use a "default" non-localized string as a last-resort fallback
     */
    @Test
    public void nonLocalizedFallback() {
        InternationalString internationalString = new InternationalString();
        internationalString.setLocalizedString(new LocalizedString("Hello World", null));
        assertEquals("Hello World", internationalString.getLocalizedString("cn").getValue());
    }

    @Test
    public void fallback() {
        InternationalString internationalString = new InternationalString();
        internationalString.setLocalizedString(new LocalizedString("Hello World", "en"));
        internationalString.setLocalizedString(new LocalizedString("Hi World", "en-us"));
        internationalString.setLocalizedString(new LocalizedString("How Do You Do World", "en-gb"));

        assertEquals("Hello World", internationalString.getLocalizedString("en").getValue());
        assertEquals("How Do You Do World", internationalString.getLocalizedString("en-gb").getValue());
        assertEquals("Hi World", internationalString.getLocalizedString("en-us").getValue());
        assertEquals("Hello World", internationalString.getLocalizedString("en-za").getValue());
        assertEquals("Hi World", internationalString.getLocalizedString("en-us-posix").getValue());
    }

}
