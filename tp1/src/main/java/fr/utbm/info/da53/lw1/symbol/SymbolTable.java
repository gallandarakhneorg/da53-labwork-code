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
package fr.utbm.info.da53.lw1.symbol;

import java.util.Map;
import java.util.TreeMap;

import fr.utbm.info.da53.lw1.token.ReservedWordToken;
import fr.utbm.info.da53.lw1.token.Token;

/**
 * Symbol table store tokens which define a part of Tiny Basic dialect of the BASIC language.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class SymbolTable {
		
	private final Map<Token,SymbolTableEntry> symbols = new TreeMap<Token,SymbolTableEntry>();
	
	/**
	 */
	public SymbolTable() {
		//
	}
	
	/** Add a symbol in the table.
	 * 
	 * @param token is the new token.
	 * @param line is the line of the token.
	 * @param column is the column of the token.
	 * @return the symbol table entry, never <code>null</code>
	 * @throws IllegalArgumentException when the given token is a reserved word.
	 */
	public SymbolTableEntry add(Token token, int line, int column) {
		if (token instanceof ReservedWordToken)
			throw new IllegalArgumentException();
		SymbolTableEntry entry = this.symbols.get(token);
		if (entry!=null) return entry;
		entry = new SymbolTableEntry(token, line, column);
		this.symbols.put(token, entry);
		if (token instanceof SymbolTablePointer) {
			((SymbolTablePointer)token).setSymbolTableEntry(entry);
		}
		return entry;
	}
	
	/** Add a reserved word in the table.
	 * 
	 * @param token is the new token.
	 * @return the symbol table entry, never <code>null</code>.
	 */
	public SymbolTableEntry addReservedWord(ReservedWordToken token) {
		SymbolTableEntry entry = this.symbols.get(token);
		if (entry!=null) return entry;
		entry = new SymbolTableEntry(token, -1, -1);
		this.symbols.put(token, entry);
		return entry;
	}

	/** Return the entry for the given lexeme.
	 * 
	 * @param lexeme
	 * @return the entry, or <code>null</code> if not found.
	 */
	public SymbolTableEntry get(String lexeme) {
		if (lexeme==null || lexeme.isEmpty()) return null;
		return get(new ComparisonToken(lexeme));
	}
	
	/** Return the entry for the given token.
	 * 
	 * @param token
	 * @return the entry, or <code>null</code> if not found.
	 */
	public SymbolTableEntry get(Token token) {
		if (token==null) return null;
		return this.symbols.get(token);
	}

	/** Return if the given lexeme is defined in the symbol table.
	 * 
	 * @param lexeme
	 * @return the entry, or <code>null</code> if not found.
	 */
	public boolean contains(String lexeme) {
		if (lexeme==null || lexeme.isEmpty()) return false;
		return contains(new ComparisonToken(lexeme));
	}

	/** Return if the given lexeme is defined in the symbol table.
	 * 
	 * @param token
	 * @return the entry, or <code>null</code> if not found.
	 */
	public boolean contains(Token token) {
		if (token==null) return false;
		return this.symbols.containsKey(token);
	}

	/**
	 * @author Jonathan DEMANGE &lt;jonathan.demange@utbm.fr&gt;
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $Name$ $Revision$ $Date$
	 */
	private static class ComparisonToken extends Token {

		/**
		 * @param lexeme
		 */
		public ComparisonToken(String lexeme) {
			super(lexeme);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return null;
		}
		
	}

}