/* 
 * $Id$
 * 
 * Copyright (c) 2012-2021 Stephane GALLAND.
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
package fr.utbm.info.da53.lw4.util;

/**
 * Utilities.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class Util {

	/** Replies if the given string represents a string.
	 * 
	 * @param v
	 * @return <code>true</code> or <code>false</code>.
	 */
	public static boolean isString(String v) {
		return (v.startsWith("\"")); //$NON-NLS-1$
	}

	/** Replies if the given string represents a number.
	 * 
	 * @param v
	 * @return <code>true</code> or <code>false</code>.
	 */
	public static boolean isNumber(String v) {
		try {
			Double.parseDouble(v);
			return true;
		}
		catch(Throwable ex) {
			return false;
		}
	}

	/** Replies if the given string represents an integer.
	 * 
	 * @param v
	 * @return <code>true</code> or <code>false</code>.
	 */
	public static boolean isInteger(String v) {
		try {
			Integer.parseInt(v);
			return true;
		}
		catch(Throwable ex) {
			return false;
		}
	}

	/** Replies if the given string represents a boolean.
	 * 
	 * @param v
	 * @return <code>true</code> or <code>false</code>.
	 */
	public static boolean isBoolean(String v) {
		return Boolean.TRUE.toString().equals(v)
				|| Boolean.FALSE.toString().equals(v);
	}

	/**
	 * @param v
	 * @return <code>true</code> or <code>false</code>.
	 */
	public static String unstringify(String v) {
		if (v.startsWith("\"") && v.endsWith("\"")) { //$NON-NLS-1$//$NON-NLS-2$
			String r = new String(v.substring(1, v.length()-1));
			r = r.replaceAll("(\\\\n)|(\\\\r)", "\n"); //$NON-NLS-1$//$NON-NLS-2$
			r = r.replaceAll("(\\\\t)", "\t"); //$NON-NLS-1$//$NON-NLS-2$
			r = r.replaceAll("(\\\\f)", "\f"); //$NON-NLS-1$//$NON-NLS-2$
			r = r.replaceAll("(\\\\\")", "\""); //$NON-NLS-1$//$NON-NLS-2$
			return r;
		}
		return v;
	}
	
	/**
	 * @param s
	 * @return <code>true</code> or <code>false</code>.
	 */
	public static String stringify(String s) {
		String r = s.replaceAll("\"", "\\\"");  //$NON-NLS-1$//$NON-NLS-2$
		r = r.replaceAll("\n|\r", "\\\\n"); //$NON-NLS-1$//$NON-NLS-2$
		r = r.replaceAll("\t", "\\\\t"); //$NON-NLS-1$//$NON-NLS-2$
		r = r.replaceAll("\f", "\\\\f"); //$NON-NLS-1$//$NON-NLS-2$
		return "\""+r+"\""; //$NON-NLS-1$//$NON-NLS-2$
	}

	/** Replies the string representation of the given value.
	 * 
	 * @param v
	 * @return the string representation
	 */
	public static String toString(double v) {
		if ((v-(long)v)==0.) {
			return Long.toString((long)v);
		}
		return Double.toString(v);
	}
	
	/** Replies the given string is an array.
	 * 
	 * @param v
	 * @return <code>true</code> or <code>false</code>.
	 */
	public static boolean isArray(String v) {
		return (v.startsWith("[") && v.endsWith("]"));  //$NON-NLS-1$//$NON-NLS-2$
	}

	/** Parse the given string to detect an array.
	 * 
	 * @param v
	 * @return the array.
	 */
	public static String[] parseArray(String v) {
		if (v.startsWith("[") && v.endsWith("]")) {  //$NON-NLS-1$//$NON-NLS-2$
			String elements = new String(v.substring(1, v.length()-1));
			return elements.split("[ \t\n\r\f]+%ARRAYSEP%[ \t\n\r\f]+"); //$NON-NLS-1$
		}
		return new String[0];
	}
	
	/** Generate the string representation of an array.
	 * 
	 * @param tab
	 * @return the string representation of an array.
	 */
	public static String toString(String[] tab) {
		StringBuilder b = new StringBuilder();
		b.append("["); //$NON-NLS-1$
		boolean first = true;
		for(String t : tab) {
			if (t!=null && !t.isEmpty()) {
				if (!first) {
					b.append(" %ARRAYSEP% "); //$NON-NLS-1$
				}
				b.append(t);
				first = false;
			}
		}
		b.append("]"); //$NON-NLS-1$
		return b.toString();
	}

}