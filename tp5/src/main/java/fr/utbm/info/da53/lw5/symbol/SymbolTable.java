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
package fr.utbm.info.da53.lw5.symbol;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import fr.utbm.info.da53.lw5.threeaddresscode.AddressBase;
import fr.utbm.info.da53.lw5.type.Value;

/**
 * Symbol table store tokens which define a part of Tiny Basic dialect of the BASIC language.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class SymbolTable implements Iterable<SymbolTableEntry> {
		
	/** Format the given identifier to fit the language standards.
	 * 
	 * @param id
	 * @return the formated id.
	 */
	public static final String formatIdentifier(String id) {
		return (id==null) ? null : id.toUpperCase();
	}
	
	private final SymbolTable parent;
	
	private final Map<String,SymbolTableEntry> symbols = new TreeMap<String,SymbolTableEntry>();
	
	private int nextAvailableVariableAddress = 0;
	private int nextAvailableConstantAddress = 0;
	
	/**
	 * Create a root symbol table.
	 */
	public SymbolTable() {
		this(null);
	}
	
	/**
	 * Create a child symbol table.
	 * 
	 * @param parent
	 */
	public SymbolTable(SymbolTable parent) {
		this.parent = parent;
	}

	/** Add a symbol in the table.
	 * 
	 * @param identifier is the identifier.
	 * @param line is the line of the token.
	 * @param sizeof is the size of the variable value, in bytes.
	 * @return the symbol table entry, never <code>null</code>
	 * @throws IllegalArgumentException when the given token is a reserved word.
	 */
	public SymbolTableEntry declare(String identifier, int line, int sizeof) {
		String id = formatIdentifier(identifier);
		SymbolTableEntry entry = this.symbols.get(id);
		if (entry!=null) return entry;
		entry = new SymbolTableEntry(id, line, this.nextAvailableVariableAddress, sizeof,
				AddressBase.MEMORY_CONTEXT);
		this.symbols.put(id, entry);
		this.nextAvailableVariableAddress += sizeof;
		return entry;
	}
	
	/** Add a symbol in the table.
	 * 
	 * @param identifier is the identifier.
	 * @param value is the value of the constant.
	 * @return the symbol table entry, never <code>null</code>
	 * @throws IllegalArgumentException when the given token is a reserved word.
	 */
	public SymbolTableEntry declareConstant(String identifier, String value) {
		SymbolTable table = this;
		while (table.parent!=null) {
			table = table.parent;
		}
		
		String id = formatIdentifier(identifier);
		SymbolTableEntry entry = table.symbols.get(id);
		if (entry!=null) return entry;
		byte[] bytes = value.getBytes();
		entry = new SymbolTableEntry(id, 0, table.nextAvailableConstantAddress, bytes.length+1,
				AddressBase.PROGRAM_END);
		entry.setValue(value);
		table.symbols.put(id, entry);
		table.nextAvailableConstantAddress += bytes.length+1;
		
		return entry;
	}

	/** Return the entry for the given lexeme.
	 * 
	 * @param lexeme
	 * @return the entry, or <code>null</code> if not found.
	 */
	public SymbolTableEntry get(String lexeme) {
		if (lexeme==null || lexeme.isEmpty()) return null;
		String id = formatIdentifier(lexeme);
		SymbolTable table = this;
		while (table!=null) {
			SymbolTableEntry e = this.symbols.get(id);
			if (e!=null) return e;
			table = table.parent;
		}
		return null;
	}
	
	/** Return if the given lexeme is defined in the symbol table.
	 * 
	 * @param lexeme
	 * @return the entry, or <code>null</code> if not found.
	 */
	public boolean contains(String lexeme) {
		if (lexeme==null || lexeme.isEmpty()) return false;
		String id = formatIdentifier(lexeme);
		SymbolTable table = this;
		while (table!=null) {
			if (this.symbols.containsKey(id)) return true;
			table = table.parent;
		}
		return false;
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
		Iterator<SymbolTableEntry> iterator = iterator();
		SymbolTableEntry e;
		while (iterator.hasNext()) {
			e = iterator.next();
			if (b.length()>0) {
				b.append("\n"); //$NON-NLS-1$
			}
			b.append(e.toString());
		}
		return b.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<SymbolTableEntry> iterator() {
		return new RecursiveIterator(this);
	}
	
	/**
	 * Replies an iterator on the constants.
	 * 
	 * @return the iterator on the constants.
	 */
	public Iterator<SymbolTableEntry> constantIterator() {
		return new ConstantIterator(this);
	}

	/**
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $Name$ $Revision$ $Date$
	 */
	public static class RecursiveIterator implements Iterator<SymbolTableEntry> {
	
		private SymbolTable table;
		private Iterator<SymbolTableEntry> iterator;
		private SymbolTableEntry next = null;
		
		/**
		 * @param table
		 */
		@SuppressWarnings("synthetic-access")
		public RecursiveIterator(SymbolTable table) {
			this.table = table;
			this.iterator = this.table.symbols.values().iterator();
			searchNext();
		}
		
		@SuppressWarnings("synthetic-access")
		private void searchNext() {
			this.next = null;
			while (this.table.parent!=null && (this.iterator==null || !this.iterator.hasNext())) {
				this.table = this.table.parent;
				this.iterator = this.table.symbols.values().iterator();
			}
			if (this.iterator!=null && this.iterator.hasNext()) {
				this.next = this.iterator.next();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return this.next!=null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SymbolTableEntry next() {
			SymbolTableEntry e = this.next;
			if (e==null) throw new NoSuchElementException();
			searchNext();
			return e;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	} // class RecursiveIterator

	/**
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $Name$ $Revision$ $Date$
	 */
	public static class ConstantIterator implements Iterator<SymbolTableEntry> {
	
		private final Iterator<SymbolTableEntry> iterator;
		private SymbolTableEntry next = null;
		
		/**
		 * @param table
		 */
		public ConstantIterator(SymbolTable table) {
			this.iterator = new RecursiveIterator(table);
			searchNext();
		}
		
		private void searchNext() {
			this.next = null;
			while (this.next==null && this.iterator.hasNext()) {
				SymbolTableEntry e = this.iterator.next();
				if (e.isConstant()) {
					this.next = e;
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return this.next!=null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SymbolTableEntry next() {
			SymbolTableEntry e = this.next;
			if (e==null) throw new NoSuchElementException();
			searchNext();
			return e;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	} // class ConstantIterator

}