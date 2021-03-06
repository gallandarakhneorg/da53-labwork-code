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
package fr.utbm.info.da53.lw1.error;

import fr.utbm.info.da53.lw1.util.LocaleUtil;

/**
 * Exception thrown when a program is invalid. 
 * 
 * @author Jonathan DEMANGE &lt;jonathan.demange@utbm.fr&gt;
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class SyntaxError extends Exception {
	
	private static final long serialVersionUID = 1014236666498219773L;

	/**
	 * @param line is the line of the error.
	 * @param column is the column of the error.
	 */
	public SyntaxError(int line, int column) {
		super(LocaleUtil.getString(SyntaxError.class, "ERROR_MESSAGE", //$NON-NLS-1$
				line,
				column));
	}

	/**
	 * @param line is the line of the error.
	 * @param column is the column of the error.
	 * @param explanation is the explanation of the error.
	 */
	public SyntaxError(int line, int column, String explanation) {
		super(LocaleUtil.getString(SyntaxError.class, "ERROR_MESSAGE_x", //$NON-NLS-1$
				line, column, explanation));
	}
	
	/**
	 * @param line is the line of the error.
	 * @param column is the column of the error.
	 * @param cause is the cause of the error.
	 */
	public SyntaxError(int line, int column, Throwable cause) {
		super(LocaleUtil.getString(SyntaxError.class, "ERROR_MESSAGE_x", //$NON-NLS-1$
				line, column, cause), cause);
	}

	/**
	 * @param line is the line of the error.
	 * @param column is the column of the error.
	 * @param explanation is the explanation of the error.
	 * @param cause is the cause of the error.
	 */
	public SyntaxError(int line, int column, String explanation, Throwable cause) {
		super(LocaleUtil.getString(SyntaxError.class, "ERROR_MESSAGE_x", //$NON-NLS-1$
				line, column, explanation), cause);
	}

}
