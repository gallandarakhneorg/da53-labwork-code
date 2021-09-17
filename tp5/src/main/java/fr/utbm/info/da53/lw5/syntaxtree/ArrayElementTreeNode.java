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
package fr.utbm.info.da53.lw5.syntaxtree;

import fr.utbm.info.da53.lw5.error.IntermediateCodeGenerationException;
import fr.utbm.info.da53.lw5.symbol.SymbolTable;
import fr.utbm.info.da53.lw5.symbol.SymbolTableEntry;
import fr.utbm.info.da53.lw5.threeaddresscode.Address;
import fr.utbm.info.da53.lw5.threeaddresscode.ThreeAddressCode;
import fr.utbm.info.da53.lw5.threeaddresscode.ThreeAddressInstruction;
import fr.utbm.info.da53.lw5.type.NumberUtil;

/**
 * Node for any element of array
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class ArrayElementTreeNode extends AbstractValueTreeNode {
	
	private final String identifier;
	
	/**
	 * @param line is the first line in the source program where this node is located.
	 * @param identifier
	 * @param index
	 */
	public ArrayElementTreeNode(int line, String identifier, AbstractValueTreeNode index) {
		super(line);
		assert(identifier!=null && !identifier.isEmpty());
		this.identifier = SymbolTable.formatIdentifier(identifier);
		setChildren(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(this.identifier);
		b.append("("); //$NON-NLS-1$
		Object index = getChildAt(0);
		if (index!=null) b.append(index.toString());
		b.append(")"); //$NON-NLS-1$
		return b.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generate(ThreeAddressCode code)
			throws IntermediateCodeGenerationException {
		SymbolTableEntry entry = code.getSymbolTableEntry(this.identifier);
		if (entry==null) {
			fail(code, "Undefined variable: "+this.identifier); //$NON-NLS-1$
		}
		assert(entry!=null);
		
		AbstractValueTreeNode indexExpression = (AbstractValueTreeNode)getChildAt(0);
		if (indexExpression==null) {
			fail(code, "Invalid index expression"); //$NON-NLS-1$
		}
		assert(indexExpression!=null);
		
		String index = indexExpression.generate(code);

		String idx = code.createTempVariable();
		String value = code.createTempVariable();
		
		code.addRecord(ThreeAddressInstruction.substraction(
				code.address(idx),
				code.address(index),
				new Address(1)));
		code.addRecord(ThreeAddressInstruction.multiplication(
				code.address(idx),
				code.address(idx),
				new Address(NumberUtil.SIZEOF_LONG)));
		code.addRecord(ThreeAddressInstruction.getArray(
				code.address(value),
				code.address(this.identifier),
				code.address(idx)));
		
		return value;
	}
	
}
