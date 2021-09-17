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

import java.util.ArrayList;
import java.util.List;

import fr.utbm.info.da53.lw2.type.Value;
import fr.utbm.info.da53.lw2.type.VariableType;

/**
 * This is an entry in the symbol table.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class SymbolTableEntry implements Comparable<SymbolTableEntry> {
		
	private final String identifier;
	private final int firstOccurrenceLine;
	private final Value value = new Value();
	
	/**
	 * @param identifier is the lexeme represented by this entry.
	 * @param line is the line of the token.
	 */
	SymbolTableEntry(String identifier, int line) {
		assert(identifier!=null);
		assert(line>=1);
		this.identifier = identifier;
		this.firstOccurrenceLine = line;
	}
	
	/** Return the identifier.
	 * @return the identifier.
	 */
	public String id() {
		return this.identifier;
	}
	
	/** Return the line of the first occurence of the symbol.
	 * 
	 * @return the line of the first occurence of the symbol.
	 */
	public int line() {
		return this.firstOccurrenceLine;
	}
	
	/** Replies the value.
	 * 
	 * @return the value, never <code>null</code>.
	 */
	public Value getValue() {
		return this.value;
	}
	
	/** Set the value.
	 * 
	 * @param value
	 */
	public void setValue(Number value) {
		this.value.set(value);
	}

	/** Set the value at the given index in an array.
	 * 
	 * @param value
	 * @param index
	 */
	public void setValueAt(Value value, int index) {
		List<Value> vals;
		if (this.value.getType()==VariableType.ARRAY) {
			vals = this.value.getValueArray();
		}
		else {
			vals = new ArrayList<Value>(index+1);
		}
		if (index>=vals.size()) {
			while (vals.size()<index) {
				vals.add(Value.UNDEF);
			}
			vals.add(value==null ? Value.UNDEF : value);
		}
		else if (index>=0) {
			vals.set(index, value==null ? Value.UNDEF : value);
		}
		this.value.set(vals);
	}

	/** Set the value.
	 * 
	 * @param value
	 */
	public void setValue(Value value) {
		this.value.set(value);
	}

	/** Set the value.
	 * 
	 * @param value
	 */
	public void setValue(List<Value> value) {
		this.value.set(value);
	}

	/** Set the value.
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value.set(value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.identifier
				+"=" //$NON-NLS-1$
				+this.value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(SymbolTableEntry o) {
		if (o==null) return Integer.MAX_VALUE;
		if (this.identifier==o.identifier) return 0;
		if (this.identifier==null) return Integer.MIN_VALUE;
		if (o.identifier==null) return Integer.MAX_VALUE;
		return this.identifier.compareTo(o.identifier);
	}
	
}
