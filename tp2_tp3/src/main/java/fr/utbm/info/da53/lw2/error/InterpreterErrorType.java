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
package fr.utbm.info.da53.lw2.error;

import java.util.ResourceBundle;

/**
 * List of the interpreter errors.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public enum InterpreterErrorType {

	/** Index ouf bounds.
	 */
	INDEX_OUT_OF_BOUNDS,
	
	/** The index of the array is invalid.
	 */
	INVALID_ARRAY_INDEX,
	
	/** Division by zero.
	 */
	DIVISION_BY_ZERO,

	/** Start index is greater than end index.
	 */
	INVERTED_START_END_INDEXES,

	/** The step of a for loop is zero.
	 */
	ZERO_STEP,

	/** Expecting integer number.
	 */
	EXPECTING_INTEGER,

	/** Expecting array variable.
	 */
	EXPECTING_ARRAY,

	/** Expecting number.
	 */
	EXPECTING_NUMBER,
	
	/** Expecting string.
	 */
	EXPECTING_STRING,

	/** Expecting boolean.
	 */
	EXPECTING_BOOLEAN,

	/** Input output error.
	 */
	IO,
	
	/** Cannot return outside a sub.
	 */
	RETURN_OUTSIDE_SUB,

	/** Line not found.
	 */
	LINE_NOT_FOUND,

	/** No more statement to run.
	 */
	NOTHING_TO_RUN,
	
	/** A variable was not defined.
	 */
	UNDEFINED_VARIABLE,
	
	/** A variable was not set.
	 */
	UNSET_VALUE;

	/** Replies the error message.
	 * 
	 * @return the error message.
	 */
	public String getMessage() {
		ResourceBundle bundle = ResourceBundle.getBundle(InterpreterErrorType.class.getCanonicalName());
		return bundle.getString(name());
	}

}
