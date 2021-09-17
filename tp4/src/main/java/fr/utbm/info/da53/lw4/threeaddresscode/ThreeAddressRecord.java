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

/**
 * Record in a the quadruple form of three address code.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class ThreeAddressRecord implements Cloneable, Serializable {

	private static final long serialVersionUID = 6433325404407603247L;

	private static int LABEL_SIZE = 15;
	
	private final ThreeAddressInstruction instruction;
	private final String argument1;
	private final String argument2;
	private final String result;
	
	private String comment = null;
	
	private String label = null;
	
	/**
	 * @param instruction
	 * @param argument1
	 * @param argument2
	 * @param result
	 */
	public ThreeAddressRecord(ThreeAddressInstruction instruction, String argument1, String argument2, String result) {
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
			return (ThreeAddressRecord)super.clone();
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
	public String getArgument1() {
		return this.argument1;
	}

	/** Replies the second argument of the instruction.
	 * 
	 * @return the second argument of the instruction.
	 */
	public String getArgument2() {
		return this.argument2;
	}

	/** Replies the result of the instruction.
	 * 
	 * @return the result of the instruction.
	 */
	public String getResult() {
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
