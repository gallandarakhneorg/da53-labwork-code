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
 * List of the compiler errors.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public enum CompilationErrorType {

	/** The FOR identifier and NEXT identifier are not the same.
	 */
	NOT_SAME_FOR_NEXT_IDENTIFIERS,
	
	/** An expression is expected.
	 */
	EXPECTING_EXPRESSION,
	
	/** No statement of in "then" block.
	 */
	NO_STATEMENT_IN_THEN_BLOCK,

	/** No statement of in "while" block.
	 */
	NO_STATEMENT_IN_WHILE_BLOCK,

	/** No statement of in "for" block.
	 */
	NO_STATEMENT_IN_FOR_BLOCK,

	/** A comparison operator is expected.
	 */
	EXPECTING_COMPARISON_OPERATOR,

	/** A left operand is expected.
	 */
	EXPECTING_LEFT_OPERAND,

	/** A right operand is expected.
	 */
	EXPECTING_RIGHT_OPERAND,

	/** Invalid format for the line number.
	 */
	INVALID_LINE_NUMBER,
	
	/** A string literal is required.
	 */
	STRING_LITERAL_REQUIRED,

	/** A number literal is required.
	 */
	NUMBER_LITERAL_REQUIRED,

	/** An illegal character was found by the lexer.
	 */
	ILLEGAL_CHARACTER,
	
	/** General syntax error.
	 */
	SYNTAX_ERROR,
	
	/** Internal error in the lexer.
	 */
	INTERNAL_LEXER_ERROR,

	/** Internal error.
	 */
	INTERNAL_ERROR;

	/** Replies the error message.
	 * 
	 * @return the error message.
	 */
	public String getMessage() {
		ResourceBundle bundle = ResourceBundle.getBundle(CompilationErrorType.class.getCanonicalName());
		return bundle.getString(name());
	}

}
