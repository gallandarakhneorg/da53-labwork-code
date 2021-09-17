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
package fr.utbm.info.da53.lw2.symbol;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import fr.utbm.info.da53.lw2.type.Value;

/**
 * Symbol table store tokens which define a part of Tiny Basic dialect of the BASIC language.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class SymbolTable implements Iterable<SymbolTableEntry> {
		
	/** Format the gien identifier to fit the language standards.
	 * 
	 * @param id
	 * @return the formated id.
	 */
	public static final String formatIdentifier(String id) {
		return (id==null) ? null : id.toUpperCase();
	}
	
	private final Map<String,SymbolTableEntry> symbols = new TreeMap<String,SymbolTableEntry>();
	
	/**
	 */
	public SymbolTable() {
		//
	}
	
	/** Add a symbol in the table.
	 * 
	 * @param identifier is the identifier.
	 * @param line is the line of the token.
	 * @return the symbol table entry, never <code>null</code>
	 * @throws IllegalArgumentException when the given token is a reserved word.
	 */
	public SymbolTableEntry declare(String identifier, int line) {
		String id = formatIdentifier(identifier);
		SymbolTableEntry entry = this.symbols.get(id);
		if (entry!=null) return entry;
		entry = new SymbolTableEntry(id, line);
		this.symbols.put(id, entry);
		return entry;
	}
	
	/** Return the entry for the given lexeme.
	 * 
	 * @param lexeme
	 * @return the entry, or <code>null</code> if not found.
	 */
	public SymbolTableEntry get(String lexeme) {
		if (lexeme==null || lexeme.isEmpty()) return null;
		return this.symbols.get(formatIdentifier(lexeme));
	}
	
	/** Return if the given lexeme is defined in the symbol table.
	 * 
	 * @param lexeme
	 * @return the entry, or <code>null</code> if not found.
	 */
	public boolean contains(String lexeme) {
		if (lexeme==null || lexeme.isEmpty()) return false;
		return this.symbols.containsKey(formatIdentifier(lexeme));
	}

	/** Clear the symbol table.
	 */
	public void clear() {
		this.symbols.clear();
	}
	
	/** Reset all the values of the symbol table.
	 */
	public void resetValues() {
		for(SymbolTableEntry entry : this.symbols.values()) {
			entry.setValue((Value)null);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		for(SymbolTableEntry entry : this.symbols.values()) {
			if (b.length()>0) {
				b.append("\n"); //$NON-NLS-1$
			}
			b.append(entry.toString());
		}
		return b.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<SymbolTableEntry> iterator() {
		return Collections.unmodifiableCollection(this.symbols.values()).iterator();
	}

}