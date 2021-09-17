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

import fr.utbm.info.da53.lw5.error.CompilerException;
import fr.utbm.info.da53.lw5.error.IntermediateCodeGenerationException;
import fr.utbm.info.da53.lw5.symbol.SymbolTable;
import fr.utbm.info.da53.lw5.threeaddresscode.ThreeAddressCode;
import fr.utbm.info.da53.lw5.threeaddresscode.ThreeAddressInstruction;

/**
 * Node for the "FOR" statement.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class ForTreeNode extends AbstractStatementTreeNode {

	private final String identifier;

	/**
	 * @param line is the first line in the source program where this node is located.
	 * @param identifier
	 * @param startValue
	 * @param endValue
	 * @param stepValue
	 * @param statement
	 */
	public ForTreeNode(int line, String identifier, AbstractValueTreeNode startValue, AbstractValueTreeNode endValue, AbstractValueTreeNode stepValue, AbstractStatementTreeNode statement) {
		super(line);
		this.identifier = SymbolTable.formatIdentifier(identifier);
		setChildren(startValue, endValue, stepValue, statement);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("FOR "); //$NON-NLS-1$
		b.append(this.identifier);
		b.append("="); //$NON-NLS-1$
		b.append(getChildAt(0).toString());
		b.append(" TO "); //$NON-NLS-1$
		b.append(getChildAt(1).toString());
		Object o = getChildAt(2);
		if (o!=null) {
			b.append(" STEP "); //$NON-NLS-1$
			b.append(o.toString());
		}
		o = getChildAt(3);
		if (o!=null) {
			b.append(" "); //$NON-NLS-1$
			b.append(o.toString());
		}
		b.append(" NEXT "); //$NON-NLS-1$
		b.append(this.identifier);
		return b.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void generateStatement(ThreeAddressCode code)
			throws IntermediateCodeGenerationException {
		AbstractValueTreeNode startValue = (AbstractValueTreeNode)getChildAt(0);
		if (startValue==null) {
			fail(code, "Not expression to compute the starting index in FOR statement"); //$NON-NLS-1$
		}
		assert(startValue!=null);
		
		AbstractValueTreeNode endValue = (AbstractValueTreeNode)getChildAt(1);
		if (endValue==null) {
			fail(code, "Not expression to compute the ending index in FOR statement"); //$NON-NLS-1$
		}
		assert(endValue!=null);

		AbstractValueTreeNode stepValue = (AbstractValueTreeNode)getChildAt(2);
		if (stepValue==null) {
			try {
				stepValue = new NumberTreeNode("1", sourceLine()); //$NON-NLS-1$
			}
			catch (CompilerException e) {
				throw new IntermediateCodeGenerationException(sourceLine(), e);
			}
		}
		assert(stepValue!=null);
		
		AbstractStatementTreeNode statement = (AbstractStatementTreeNode)getChildAt(3);
		if (statement==null) {
			warn(code, "Nothing to run in FOR loop"); //$NON-NLS-1$
		}

		String conditionLabel = code.createLabel();
		String afterLabel = code.createLabel();
		
		// Initialization
		String startVal = startValue.generate(code);
		code.addRecord(ThreeAddressInstruction.set(
				code.address(this.identifier),
				code.address(startVal)));
		
		// Compute end value
		code.setNextLabel(conditionLabel);
		String endVal = endValue.generate(code);

		// Test the end of the loop
		String tmp = code.createTempVariable();
		code.addRecord(ThreeAddressInstruction.le(
				code.address(tmp),
				code.address(this.identifier),
				code.address(endVal)));
		code.addRecord(ThreeAddressInstruction.jumpIfFalse(
				code.address(tmp),
				code.label(afterLabel)));
		
		// Loop statement
		if (statement!=null) {
			statement.generate(code);
		}
		
		// Incrementation
		String increment = stepValue.generate(code);
		code.addRecord(ThreeAddressInstruction.addition(
				code.address(this.identifier),
				code.address(this.identifier),
				code.address(increment)));

		// Return to condition
		code.addRecord(ThreeAddressInstruction.jump(code.label(conditionLabel)));
		
		code.setNextLabel(afterLabel);
	}

}
