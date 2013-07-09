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
package org.apache.rave.portal.util;

import com.ibm.icu.util.GlobalizationPreferences;
import com.ibm.icu.util.ULocale;
import org.apache.rave.portal.model.util.LocalizedString;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class LocalizationUtilsTest {
	@Test
	public void testIsValidLanguageTag() {
		boolean nullValue = LocalizationUtils.isValidLanguageTag(null);
		assertFalse("Null value handled properly",nullValue);
	}

	@Test
	public void testGetLocalizedElement() {
		// This should be independent of the actual build locale.
		LocalizedString[] elements = new LocalizedString[3];
		elements[0] = new LocalizedString("Hello World", "en");
		elements[1] = new LocalizedString("你好世界", "cn");
		elements[2] = new LocalizedString("Nom de plume!", "fr");
		LocalizedString testMatch = new LocalizedString("Nom de plume!", "fr");
		String[] locales = { "es", "fr", "en" };
		LocalizedString bestMatch = LocalizationUtils.getLocalizedElement(
				elements, locales);
		assertTrue("Localized element best match returned correctly",bestMatch.getValue().equals(testMatch.getValue()));
	}

	@Test
	public void testProcessElementsByDefaultLocales() {
		// This will hopefully be independent of the real locale where the test
		// is run
		// Determine the "real" locales.
		GlobalizationPreferences prefs = new GlobalizationPreferences();
		ArrayList<ULocale> locales = (ArrayList<ULocale>)prefs.getLocales();
		assertNotNull("Locales array is not null", locales);

		// Set up a dummy list of strings. Last one will be the
		// correct one.
		LocalizedString[] elements = new LocalizedString[4];
		elements[0] = new LocalizedString("Hollow World", locales.get(0)
				.toLanguageTag());
		elements[1] = new LocalizedString("Hello World", "xx-xx");
		elements[2] = new LocalizedString("你好世界", "xy-xx");
		elements[3] = new LocalizedString("Nom de plume!", "xz-xx");

		// Sort and test
		LocalizedString[] sortedElements = LocalizationUtils
				.processElementsByDefaultLocales(elements);
		assertNotNull("Sorted elements list is not null", sortedElements);
		assertTrue(
				"First element of processed list has expected value",
				sortedElements[0].getLang().equals(
						locales.get(0).toLanguageTag()));
	}
}
