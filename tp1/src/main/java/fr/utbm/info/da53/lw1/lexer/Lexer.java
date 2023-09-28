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
package fr.utbm.info.da53.lw1.lexer;

import java.io.IOException;

import fr.utbm.info.da53.lw1.error.SyntaxError;
import fr.utbm.info.da53.lw1.token.Token;

/** This is the lexical analyzer.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public interface Lexer {
	
	/** Return the number of the column where the next character is located 
	 * in the source program.
	 * 
	 * @return the number of the column.
	 */
	int getCurrentColumn();
	
	/** Return the number of the line where the next character is located 
	 * in the source program.
	 * 
	 * @return the number of the line.
	 */
	int getCurrentLine();

	/** Dispose all the resources used by the lexer (including the input stream).
	 * 
	 * @throws IOException
	 */
	void dispose() throws IOException;
	
	/**
	 * Read the next token from the input stream.
	 * 
	 * @return the next token, or <code>null</code> if no more token.
	 * @throws SyntaxError
	 */
	Token getNextToken() throws SyntaxError;
	
}
