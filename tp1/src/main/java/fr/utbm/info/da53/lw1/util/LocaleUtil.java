/* 
 * $Id$
 * 
 * Copyright (c) 2012-2021 Stephane GALLAND, Jonathan DEMANGE.
 * 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.utbm.info.da53.lw1.util;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This utility class permits to ease the use of localized strings.
 * It is strongly inspired by the Locale class from
 * the <a href="http://www.arakhne.org/arakhneVmutils/">Arakhn&ecirc;
 * Virtual Machine Utilities</a>. 
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class LocaleUtil {

	/** Read a localized string from a property file.
	 * 
	 * @param callerType is the type of the caller. This type is used to retreive the filename of the property file.
	 * @param key is the string key in the property file.
	 * @param parameters are the parameter values to put in the localized string.
	 * @return the localized string.
	 */
	public static String getString(Class<?> callerType, String key, Object... parameters) {
		if (callerType==null) return key;
		return getString(callerType.getCanonicalName(), key, parameters);
	}

	/** Read a localized string from a property file.
	 * 
	 * @param resourcePath is the filename of the resource file to read.
	 * @param key is the string key in the property file.
	 * @param parameters are the parameter values to put in the localized string.
	 * @return the localized string.
	 */
	public static String getString(String resourcePath, String key, Object... parameters) {
		// Get the resource file
		ResourceBundle resource = null;
		try {
			resource = ResourceBundle.getBundle(resourcePath,
					java.util.Locale.getDefault(),
					LocaleUtil.class.getClassLoader());
		}
		catch (MissingResourceException ex) {
			return key;
		}

		// get the resource string
		String result;

		try {
			result = resource.getString(key);
		}
		catch (Exception ex) {
			return key;
		}

		// replace the \n and \r by a real new line character
		result = result.replaceAll("[\\n\\r]","\n"); //$NON-NLS-1$ //$NON-NLS-2$
		result = result.replaceAll("\\t","\t"); //$NON-NLS-1$ //$NON-NLS-2$

		// replace the parameter values
		assert(parameters!=null);
		return MessageFormat.format(result, parameters);
	}
	
}
