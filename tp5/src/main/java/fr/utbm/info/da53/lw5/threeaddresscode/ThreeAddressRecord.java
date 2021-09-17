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

import fr.utbm.info.da53.lw5.type.NumberUtil;

/**
 * Record in a the quadruple form of three address code.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class ThreeAddressRecord implements Cloneable, Serializable {

	private static final long serialVersionUID = 6433325404407603247L;

	/** Size of a record in bytes.
	 */
	public static final int RECORD_SIZE = 4 * NumberUtil.SIZEOF_LONG;

	private static final int LABEL_SIZE = 15;
	
	private final ThreeAddressInstruction instruction;
	private Address argument1;
	private Address argument2;
	private Address result;
	
	private String comment = null;
	
	private String label = null;
	private Address address = null;
	
	/**
	 * @param instruction
	 * @param argument1
	 * @param argument2
	 * @param result
	 */
	public ThreeAddressRecord(ThreeAddressInstruction instruction, Address argument1, Address argument2, Address result) {
		this.instruction = instruction;
		this.argument1 = argument1;
		this.argument2 = argument2;
		this.result = result;
	}
	
	/**
	 * @param instruction
	 */
	public ThreeAddressRecord(ThreeAddressInstruction instruction) {
		this.instruction = instruction;
		this.argument1 = this.argument2 = this.result = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ThreeAddressRecord clone() {
		try {
			ThreeAddressRecord r = (ThreeAddressRecord)super.clone();
			r.argument1 = this.argument1.clone();
			r.argument2 = this.argument2.clone();
			r.result = this.result.clone();
			return r;
		}
		catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}
	
	/** Replies the instruction of the record.
	 * 
	 * @return the operator or the instruction.
	 */
	public ThreeAddressInstruction instruction() {
		return this.instruction;
	}
	
	/** Replies the first argument of the instruction.
	 * 
	 * @return the first argument of the instruction.
	 */
	public Address getArgument1() {
		return this.argument1;
	}

	/** Replies the second argument of the instruction.
	 * 
	 * @return the second argument of the instruction.
	 */
	public Address getArgument2() {
		return this.argument2;
	}

	/** Replies the result of the instruction.
	 * 
	 * @return the result of the instruction.
	 */
	public Address getResult() {
		return this.result;
	}

	/** Replies the label of the instruction.
	 * 
	 * @return the label of the instruction; or <code>null</code>
	 * if none.
	 */
	public String getLabel() {
		return this.label;
	}

	/** Set the label of the instruction.
	 * 
	 * @param label is the label of the instruction; or <code>null</code>
	 * if none.
	 */
	void setLabel(String label) {
		this.label = label;
	}
	
	/** Replies the address of the instruction.
	 * 
	 * @return the address of the instruction; or <code>null</code>
	 * if none.
	 */
	public Address getAddress() {
		return this.address;
	}

	/** Set the address of the instruction.
	 * 
	 * @param addr is the address of the instruction; or <code>null</code>
	 * if none.
	 */
	void setAddress(int addr) {
		this.address = new Address(Integer.toString(addr), addr, AddressBase.PROGRAM_START);
	}

	/** Set the comment of the instruction.
	 *  
	 * @param comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		if (this.comment!=null && !this.comment.isEmpty()) {
			for(int i=0; i<LABEL_SIZE; ++i) {
				b.append(" "); //$NON-NLS-1$
			}
			b.append("#-- "); //$NON-NLS-1$
			b.append(this.comment);
			b.append("\n"); //$NON-NLS-1$
		}
		b.append(formatLabel(this.label));
		b.append(this.instruction.toString(this.argument1, this.argument2, this.result));
		return b.toString();
	}
	
	private static String formatLabel(String label) {
		StringBuilder b = new StringBuilder();
		if (label!=null && !label.isEmpty()) {
			b.append(label);
			b.append(":"); //$NON-NLS-1$
		}
		while (b.length()<LABEL_SIZE) {
			b.append(" "); //$NON-NLS-1$
		}
		return b.toString();
	}

}
