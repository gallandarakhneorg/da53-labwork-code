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
package fr.utbm.info.da53.lw4.threeaddresscode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import fr.utbm.info.da53.lw4.symbol.SymbolTable;
import fr.utbm.info.da53.lw4.symbol.SymbolTableEntry;

/**
 * Three address code.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class ThreeAddressCode implements Cloneable, Serializable {

	private static final long serialVersionUID = -5615410518220369534L;

	private final SymbolTable symbolTable;
	private List<ThreeAddressRecord> records = new ArrayList<ThreeAddressRecord>();

	/** Value -&gt; id
	 */
	private Map<String, String> stringLiterals = new TreeMap<String, String>();

	/** Basic line -&gt; Instruction address
	 */
	private Map<Integer, Integer> basicLines = new TreeMap<Integer, Integer>();

	private int nbTempNames = 0;
	private int labelNumber = 0;
	private String nextLabel = null;
	private String firstLabel = null;

	/**
	 * @param symbolTable
	 */
	public ThreeAddressCode(SymbolTable symbolTable) {
		this.symbolTable = symbolTable;
		this.records.add(ThreeAddressInstruction.jump("INIT")); //$NON-NLS-1$
	}

	/**
	 * Return the entry for the given id.
	 * 
	 * @param id
	 * @return the entry, or <code>null</code> if not found.
	 */
	public SymbolTableEntry getSymbolTableEntry(String id) {
		return this.symbolTable.get(id);
	}
	
	/** Replies the number of records in the code.
	 * 
	 * @return the number of records in the code.
	 */
	public int getRecordCount() {
		return this.records.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ThreeAddressCode clone() {
		try {
			ThreeAddressCode clone = (ThreeAddressCode) super.clone();
			clone.records = new ArrayList<ThreeAddressRecord>(this.records);
			clone.stringLiterals = new TreeMap<String, String>(
					this.stringLiterals);
			clone.basicLines = new TreeMap<Integer, Integer>(this.basicLines);
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	/**
	 * Add an instruction without label.
	 * 
	 * @param record
	 */
	public void addRecord(ThreeAddressRecord record) {
		if (record != null) {
			this.records.add(record);
			if (this.firstLabel == null) {
				if (this.nextLabel != null) {
					this.firstLabel = this.nextLabel;
				} else {
					this.firstLabel = "START"; //$NON-NLS-1$
					this.nextLabel = this.firstLabel;
				}
			}
			record.setLabel(this.nextLabel);
			this.nextLabel = null;
		}
	}

	/**
	 * Replies the address of the given record.
	 * 
	 * @param record
	 * @return the address, or <code>-1</code> if not found.
	 */
	public int getAddress(ThreeAddressRecord record) {
		return this.records.indexOf(record);
	}

	/**
	 * Replies the records.
	 * 
	 * @return the records.
	 */
	public List<ThreeAddressRecord> getRecords() {
		return Collections.unmodifiableList(this.records);
	}

	/**
	 * Replies the record at the given address.
	 * 
	 * @param address
	 * @return the record, or <code>null</code> if not found.
	 */
	public ThreeAddressRecord getRecord(int address) {
		if (address >= 0 && address < this.records.size()) {
			return this.records.get(address);
		}
		return null;
	}

	/**
	 * Replies the record at the given label.
	 * 
	 * @param label
	 * @return the record, or <code>null</code> if not found.
	 */
	public ThreeAddressRecord getRecord(String label) {
		return getRecord(getAddressFor(label));
	}

	/**
	 * Replies the address for the given label.
	 * 
	 * @param label
	 * @return the index or, {@code -1} if not found.
	 */
	public int getAddressFor(String label) {
		int i = 0;
		for (ThreeAddressRecord record : this.records) {
			if (label.equals(record.getLabel())) {
				return i;
			}
			++i;
		}
		return -1;
	}

	/**
	 * Add the given constant in the symbol table and returns the corresponding
	 * variable.
	 * 
	 * @param value
	 * @return the variable name.
	 */
	public String createConstant(String value) {
		String val = value;
		String var = this.stringLiterals.get(val);
		if (var == null) {
			var = "@s" + (this.stringLiterals.size()); //$NON-NLS-1$
			this.stringLiterals.put(val, var);
		}
		return var;
	}

	/**
	 * Create a temporary variable name.
	 * 
	 * @return the variable name.
	 */
	public String createTempVariable() {
		++this.nbTempNames;
		String var = "@t" + this.nbTempNames; //$NON-NLS-1$
		return var;
	}

	/**
	 * Create a label.
	 * 
	 * @return an unique label.
	 */
	public String createLabel() {
		++this.labelNumber;
		return "L" + this.labelNumber; //$NON-NLS-1$
	}

	/**
	 * Mark the next record (not already added) with the given label.
	 * 
	 * @param label
	 */
	public void setNextLabel(String label) {
		this.nextLabel = label;
	}

	/**
	 * Start to record a Basic instruction.
	 * 
	 * @param line
	 *            is the basic's line.
	 */
	public void startInstruction(int line) {
		if (line>0) {
			this.basicLines.put(line, this.records.size());
		}
	}

	/**
	 * Finalize the generation of the three-address code.
	 */
	public void finalizeGeneration() {
		if (this.records.get(this.records.size()-1).instruction()!=ThreeAddressInstruction.EXIT) {
			addRecord(ThreeAddressInstruction.exit());
		}

		//
		// Generate the JUMPING function
		//
		String returnLabel = createLabel();
		String param1 = createTempVariable();
		String returnVal = createTempVariable();
		String tmp = createTempVariable();
		String nextLabel = null;
				
		setNextLabel("JMPMAP"); //$NON-NLS-1$
		addRecord(ThreeAddressInstruction.formalParam(param1, 0));
		getRecord(getRecordCount()-1).setComment("Mapping table from a Basic line to address into the three-address-code"); //$NON-NLS-1$
		
		Iterator<Entry<Integer,Integer>> iterator = this.basicLines.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<Integer,Integer> entry = iterator.next();
			ThreeAddressRecord record = this.records.get(entry.getValue());

			setNextLabel(nextLabel);
			nextLabel = createLabel();
			
			if (record.getLabel()==null) {
				String nLabel = createLabel();
				record.setLabel(nLabel);
			}
			
			addRecord(ThreeAddressInstruction.eq(tmp, param1, Integer.toString(entry.getKey())));
			addRecord(ThreeAddressInstruction.jumpIfFalse(tmp, nextLabel));
			addRecord(ThreeAddressInstruction.set(returnVal, stringify(record.getLabel())));
			addRecord(ThreeAddressInstruction.jump(returnLabel));
		}
		
		setNextLabel(nextLabel);
		String errorMsg = createConstant("Illegal line number: "); //$NON-NLS-1$
		addRecord(ThreeAddressInstruction.addition(tmp, errorMsg, param1));
		addRecord(ThreeAddressInstruction.error(tmp));
		
		setNextLabel(returnLabel);
		addRecord(ThreeAddressInstruction.returnFunction(returnVal));
		
		//
		// Generate the string constants.
		//
		setNextLabel("INIT"); //$NON-NLS-1$
		boolean first = true;
		for(Entry<String,String> stringEntry : this.stringLiterals.entrySet()) {
			addRecord(ThreeAddressInstruction.set(stringEntry.getValue(),
					stringify(stringEntry.getKey())));
			if (first) {
				getRecord(getRecordCount()-1).setComment("Initialization of the string literal constants"); //$NON-NLS-1$
				first = false;
			}
		}
		
		addRecord(ThreeAddressInstruction.jump(this.firstLabel));
	}

	private static String stringify(String s) {
		String r = s.replaceAll("\"", "\\\""); //$NON-NLS-1$//$NON-NLS-2$
		r = r.replaceAll("[\n\r]", "\\\\n"); //$NON-NLS-1$//$NON-NLS-2$
		r = r.replaceAll("[\t]", "\\\\t"); //$NON-NLS-1$//$NON-NLS-2$
		r = r.replaceAll("[\f]", "\\\\f"); //$NON-NLS-1$//$NON-NLS-2$
		return "\"" + r + "\""; //$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		for (ThreeAddressRecord record : this.records) {
			b.append(record.toString());
			b.append("\n"); //$NON-NLS-1$
		}
		return b.toString();
	}

}
