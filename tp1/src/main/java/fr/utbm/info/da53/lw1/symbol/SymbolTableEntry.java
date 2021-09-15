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
package fr.utbm.info.da53.lw1.symbol;

import fr.utbm.info.da53.lw1.token.ReservedWordToken;
import fr.utbm.info.da53.lw1.token.Token;

/**
 * This is an entry in the symbol table.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class SymbolTableEntry {
		
	private final Token token;
	private final int firstOccurrenceLine;
	private final int firstOccurrenceColumn;
	private final boolean isReservedWord;
	
	// type of the symbol may be put here: int, float, string, ...
	
	
	/**
	 * @param token is the token represented by this
	 * @param line is the line of the token.
	 * @param column is the column of the token.
	 */
	SymbolTableEntry(Token token, int line, int column) {
		assert(token!=null);
		assert(line>=1);
		assert(column>=1);
		this.isReservedWord = false;
		this.token = token;
		this.firstOccurrenceLine = line;
		this.firstOccurrenceColumn = column;
	}
	
	/**
	 * @param token is the token represented by this
	 */
	SymbolTableEntry(ReservedWordToken token) {
		assert(token!=null);
		this.isReservedWord = true;
		this.token = token;
		this.firstOccurrenceLine = -1;
		this.firstOccurrenceColumn = -1;
	}

	/** Return if this entry is for a reserved word or not.
	 * 
	 * @return <code>true</code> if this entry is for a reserved word;
	 * otherwise <code>false</code>.
	 */
	public boolean isReservedWord() {
		return this.isReservedWord;
	}
	
	/** Return the token.
	 * @return the token.
	 */
	public Token token() {
		return this.token;
	}
	
	/** Return the line of the first occurence of the symbol.
	 * 
	 * @return the line of the first occurence of the symbol.
	 */
	public int line() {
		return this.firstOccurrenceLine;
	}
	
	/** Return the column of the first occurence of the symbol.
	 * 
	 * @return the column of the first occurence of the symbol.
	 */
	public int column() {
		return this.firstOccurrenceColumn;
	}

}
