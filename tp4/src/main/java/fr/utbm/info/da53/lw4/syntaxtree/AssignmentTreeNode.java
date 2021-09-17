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
package fr.utbm.info.da53.lw4.syntaxtree;

import fr.utbm.info.da53.lw4.error.IntermediateCodeGenerationException;
import fr.utbm.info.da53.lw4.symbol.SymbolTableEntry;
import fr.utbm.info.da53.lw4.threeaddresscode.ThreeAddressCode;
import fr.utbm.info.da53.lw4.threeaddresscode.ThreeAddressInstruction;

/**
 * Node for the "LET" statement.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class AssignmentTreeNode extends AbstractStatementTreeNode {
	
	private final VariableName identifier;
	
	/**
	 * @param line is the first line in the source program where this node is located.
	 * @param identifier
	 * @param expression
	 */
	public AssignmentTreeNode(int line, VariableName identifier, AbstractValueTreeNode expression) {
		super(line);
		this.identifier = identifier;
		if (expression!=null) setChildren(expression);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "LET " + this.identifier.toString() + " = " + getChildAt(0).toString(); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void generateStatement(ThreeAddressCode code)
			throws IntermediateCodeGenerationException {
		SymbolTableEntry entry = code.getSymbolTableEntry(this.identifier.id());
		if (entry==null) {
			fail(code, "Undefined variable: "+this.identifier.id()); //$NON-NLS-1$
		}
		assert(entry!=null);

		AbstractValueTreeNode expression = (AbstractValueTreeNode)getChildAt(0);
		if (expression==null) {
			fail(code, "Expression is required for LET statement"); //$NON-NLS-1$
		}
		assert(expression!=null);
		
		String value = expression.generate(code);
		if (value==null) {
			fail(code, "Expression is required for LET statement"); //$NON-NLS-1$
		}
		assert(value!=null);
		
		String variableName = this.identifier.id();
		
		AbstractValueTreeNode indexExpression = this.identifier.getArrayIndex();
		if (indexExpression!=null) {
			String index = indexExpression.generate(code);
			String idx = code.createTempVariable();
			code.addRecord(ThreeAddressInstruction.substraction(idx, index, "1")); //$NON-NLS-1$
			code.addRecord(ThreeAddressInstruction.setArray(variableName, idx, value));
		}
		else {
			code.addRecord(ThreeAddressInstruction.set(variableName, value));
		}
	}

}
