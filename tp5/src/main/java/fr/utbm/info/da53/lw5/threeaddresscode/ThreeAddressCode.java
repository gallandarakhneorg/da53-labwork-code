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
package fr.utbm.info.da53.lw5.threeaddresscode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import fr.utbm.info.da53.lw5.error.CompilerException;
import fr.utbm.info.da53.lw5.symbol.SymbolTable;
import fr.utbm.info.da53.lw5.symbol.SymbolTableEntry;
import fr.utbm.info.da53.lw5.type.NumberUtil;
import fr.utbm.info.da53.lw5.util.Util;
import fr.utbm.info.da53.lw5.vm.TinyByteCode;
import fr.utbm.info.da53.lw5.vm.TinyByteCode.ByteCodeParameterType;

/**
 * Three address code.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class ThreeAddressCode implements Cloneable, Serializable {

	private static final long serialVersionUID = -5615410518220369534L;

	/** Name of the function that is mapping a basic line number to an
	 * three-address code address.
	 */
	public static final String BASIC_GOTO_FUNCTION = "BASICLINES"; //$NON-NLS-1$
	
	private final SymbolTable symbolTable;
	private List<ThreeAddressRecord> records = new ArrayList<ThreeAddressRecord>();

	/** Value -&gt; id
	 */
	private Map<String, String> stringLiterals = new TreeMap<String, String>();
	
	/** Basic line -&gt; Instruction index
	 */
	private Map<Integer, Integer> basicLines = new TreeMap<Integer, Integer>();

	/** Label -&gt; address
	 */
	private Map<String,Integer> labelMapping = new TreeMap<String,Integer>();

	private int nbTempNames = 0;
	private int labelNumber = 0;
	private String nextLabel = null;
	private String firstLabel = null;

	private boolean isBasicGotoFunctionUsed = false;
	
	/**
	 * @param symbolTable
	 */
	public ThreeAddressCode(SymbolTable symbolTable) {
		this.symbolTable = symbolTable;
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
			clone.labelMapping = new TreeMap<String, Integer>(this.labelMapping);
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
			int adr = ThreeAddressRecord.RECORD_SIZE*this.records.size();
			record.setAddress(adr);
			this.records.add(record);
			if (this.firstLabel == null) {
				if (this.nextLabel != null) {
					this.firstLabel = this.nextLabel;
				} else {
					this.firstLabel = "START"; //$NON-NLS-1$
					this.nextLabel = this.firstLabel;
				}
			}
			if (this.nextLabel!=null && !this.nextLabel.isEmpty()) {
				record.setLabel(this.nextLabel);
				this.labelMapping.put(this.nextLabel, adr);
			}
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
			this.symbolTable.declareConstant(var, val);
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
	 * Replies the label for the next line or create a label if
	 * the next line has no label.
	 * 
	 * @return an unique label.
	 */
	public String getOrCreateLabel() {
		if (this.nextLabel!=null && !this.nextLabel.isEmpty())
			return this.nextLabel;
		return createLabel();
	}

	/**
	 * Mark the next record (not already added) with the given label.
	 * 
	 * @param label
	 */
	public void setNextLabel(String label) {
		this.nextLabel = label;
	}
	
	/** Replies the address of the variable with the given name.
	 * If the variable was not allocated before, this function
	 * allocates the variable.
	 * 
	 * @param str
	 * @return the address.
	 */
	public Address address(String str) {
		if (Util.isNumber(str)) {
			try {
				Number n = NumberUtil.parse(str, 0);
				if (NumberUtil.isInteger(n))
					return new Address(n.longValue());
				return new Address(n.doubleValue());
			}
			catch (CompilerException e1) {
				throw new RuntimeException(e1);
			}
		}
		
		SymbolTableEntry e = getSymbolTableEntry(str);
		if (e==null) {
			// The variable was never declared before
			e = this.symbolTable.declare(str, 0, NumberUtil.SIZEOF_LONG);
		}
		return e.getAddress();
	}
	
	/** Replies the address of the label with the given name.
	 * If the label was not allocated before, this function
	 * marks the label for backpatching.
	 * 
	 * @param str
	 * @return the address.
	 */
	public Address label(String str) {
		if (BASIC_GOTO_FUNCTION.equals(str)) {
			this.isBasicGotoFunctionUsed = true;
		}
		Integer adr = this.labelMapping.get(str);
		if (adr==null) {
			return new Address(str);
		}
		
		return new Address(str, adr.longValue(), AddressBase.PROGRAM_START);
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
		if (this.records.get(this.records.size()-1).instruction()!=ThreeAddressInstruction.EXIT
			|| (this.nextLabel!=null && !this.nextLabel.isEmpty())) {
			addRecord(ThreeAddressInstruction.exit());
		}
		
		if (this.isBasicGotoFunctionUsed) {
			//
			// Generate the JUMPING function
			//
			String param1 = createTempVariable();
			String returnVal = createTempVariable();
			String tmp = createTempVariable();
					
			setNextLabel(BASIC_GOTO_FUNCTION);
	
			addRecord(ThreeAddressInstruction.param(address(param1), new Address(0)));

			Iterator<Entry<Integer,Integer>> iterator = this.basicLines.entrySet().iterator();
			
			int adr = ThreeAddressRecord.RECORD_SIZE * this.records.size();
	
			while (iterator.hasNext()) {
				Entry<Integer,Integer> entry = iterator.next();
				ThreeAddressRecord record = this.records.get(entry.getValue());
	
				if (record.getLabel()==null) {
					String nLabel = createLabel();
					record.setLabel(nLabel);
				}
				
				adr += 4 * ThreeAddressRecord.RECORD_SIZE;
	
				addRecord(ThreeAddressInstruction.eq(address(tmp), address(param1), new Address((long)entry.getKey())));
				addRecord(ThreeAddressInstruction.jumpIfFalse(address(tmp), new Address(adr)));
				addRecord(ThreeAddressInstruction.set(address(returnVal), record.getAddress()));
				addRecord(ThreeAddressInstruction.returnFunction(address(returnVal)));
				
			}
			
			String errorMsg = createConstant("Line not found: "); //$NON-NLS-1$
			String cr = createConstant("\n"); //$NON-NLS-1$
			addRecord(ThreeAddressInstruction.print(address(errorMsg)));
			addRecord(ThreeAddressInstruction.print(address(tmp)));
			addRecord(ThreeAddressInstruction.print(address(cr)));
			addRecord(ThreeAddressInstruction.exit(new Address(1234)));
			addRecord(ThreeAddressInstruction.returnFunction(address(returnVal)));
		}
		
		// Backpatching
		for(ThreeAddressRecord record : this.records) {
			fixAddress(record.getArgument1());
			fixAddress(record.getArgument2());
			fixAddress(record.getResult());
		}
	}
	
	private void fixAddress(Address adr) {
		if (adr!=null && adr.isLabel()) {
			Integer l = this.labelMapping.get(adr.toString());
			if (l!=null) {
				adr.set(l.longValue());
			}
		}
	}
	
	/**
	 * Replies the byte representation of the three-address code.
	 * 
	 * @return the byte representation of the three-address code.
	 */
	public byte[] getByteCode() {
		Iterator<SymbolTableEntry> iterator;
		
		int codeSize = this.records.size() * ThreeAddressRecord.RECORD_SIZE;
		
		int strSize = 0;
		iterator = this.symbolTable.constantIterator();
		while (iterator.hasNext()) {
			SymbolTableEntry e = iterator.next();
			strSize += e.getSize();
		}
		
		int programSize = codeSize + strSize;
		
		byte[] quadruples = new byte[programSize];
		
		int adr = 0;
		
		for(ThreeAddressRecord record : this.records) {
			ByteCodeParameterType a1 = putAddress(quadruples, adr, 1, record.getArgument1(), programSize);
			ByteCodeParameterType a2 = putAddress(quadruples, adr, 2, record.getArgument2(), programSize);
			ByteCodeParameterType r  = putAddress(quadruples, adr, 3, record.getResult(), programSize);
			long inst = TinyByteCode.buildByteCodeInstruction(
					record.instruction(),
					a1,
					a2,
					r);
			Util.putLong(quadruples, adr, 0, inst);
			adr += ThreeAddressRecord.RECORD_SIZE;
		}
		
		iterator = this.symbolTable.constantIterator();
		while (iterator.hasNext()) {
			SymbolTableEntry e = iterator.next();
			byte[] b = e.getValue().getValue(String.class).getBytes();
			System.arraycopy(b, 0, quadruples, adr, b.length);
			adr += b.length;
			quadruples[adr] = '\0';
			++adr;
		}
		
		byte[] byteCode = new byte[9+2*NumberUtil.SIZEOF_LONG+quadruples.length];
		
		// Header
		byteCode[0] = 'L';
		byteCode[1] = 'O';
		byteCode[2] = '4';
		byteCode[3] = '6';
		byteCode[4] = 'B';
		byteCode[5] = 'C';
		byteCode[6] = '1';
		byteCode[7] = '0';
		
		// Options
		byteCode[8] = 0x01;

		// Size of the segment code
		Util.putLong(byteCode, 9, 0, programSize);

		// Size of the instruction part of the segment code.
		Util.putLong(byteCode, 9, 1, codeSize);
		
		// Copy the quadruples
		System.arraycopy(quadruples, 0, byteCode, 9+2*NumberUtil.SIZEOF_LONG, quadruples.length);
		
		return byteCode;
	}
	
	/** Put the given address value in the array at the index-th position
	 * from the base, each element has is size of long.
	 *  
	 * @param tab
	 * @param base
	 * @param index
	 * @param value
	 * @param programSize
	 * @return <code>true</code> if the added value is a double; <code>false</code>
	 * if the added value is a long.
	 */
	private ByteCodeParameterType putAddress(byte[] tab, int base, int index, Address value, int programSize) {
		if (value==null) {
			Util.putLong(tab, base, index, 0);
			return ByteCodeParameterType.ADDRESS;
		}
		ByteCodeParameterType r;
		Number n = null;
		if (value.isConstant()) {
			n = value.value();
			r = ByteCodeParameterType.LONG_CONSTANT;
		}
		else {
			r = ByteCodeParameterType.ADDRESS;
			switch(value.base()) {
			case PROGRAM_END:
				int codeSize = this.records.size() * ThreeAddressRecord.RECORD_SIZE;
				n = codeSize + value.offset();
				break;
			case PROGRAM_START:
				n = value.offset();
				break;
			case HEAP:
				n = programSize + value.offset();
				break;
			case MEMORY_CONTEXT:
				n = -(value.offset()+1);
				break;
			}
		}
		
		assert(n!=null);
		
		if (NumberUtil.isInteger(n)) {
			Util.putLong(tab, base, index, n.longValue());
			return r;
		}

		Util.putDouble(tab, base, index, n.doubleValue());
		return ByteCodeParameterType.DOUBLE_CONSTANT;
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
