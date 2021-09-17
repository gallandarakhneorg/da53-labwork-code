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
 * Exception thrown when a number is invalid. 
 * 
 * @author Jonathan DEMANGE &lt;jonathan.demange@utbm.fr&gt;
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class InvalidNumberFormatError extends SyntaxError {
	
	private static final long serialVersionUID = 243781630012608864L;

	/**
	 * @param line is the line of the error.
	 * @param column is the column of the error.
	 */
	public InvalidNumberFormatError(int line, int column) {
		super(line, column, LocaleUtil.getString(InvalidNumberFormatError.class, "ERROR_MESSAGE")); //$NON-NLS-1$
	}

}
