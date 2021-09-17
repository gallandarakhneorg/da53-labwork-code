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
import fr.utbm.info.da53.lw5.threeaddresscode.ThreeAddressCode;
import fr.utbm.info.da53.lw5.threeaddresscode.ThreeAddressInstruction;
import fr.utbm.info.da53.lw5.threeaddresscode.ThreeAddressRecord;

/**
 * Node for any unary operator.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public abstract class AbstractUnaryOperatorTreeNode extends AbstractValueTreeNode {
	
	/**
	 * @param line is the first line in the source program where this node is located.
	 * @param operand
	 */
	public AbstractUnaryOperatorTreeNode(int line, AbstractValueTreeNode operand) {
		super(line);
		setOperand(operand);
	}
	
	/**
	 * @param line is the first line in the source program where this node is located.
	 */
	public AbstractUnaryOperatorTreeNode(int line) {
		super(line);
	}
	

	/** Set the operand.
	 * 
	 * @param operand
	 */
	public void setOperand(AbstractValueTreeNode operand) {
		setChildren(operand);
	}
	
	/** Replies the operand.
	 * 
	 * @return the operand.
	 */
	protected AbstractValueTreeNode getOperand() {
		return (AbstractValueTreeNode)getChildAt(0);
	}
	
	/** Replies the operator as a string.
	 * 
	 * @return the operator.
	 */
	public abstract String getOperatorString();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String toString() {
		return "(" //$NON-NLS-1$
				+getOperatorString()
				+getOperand().toString()
				+")"; //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String generate(ThreeAddressCode code)
			throws IntermediateCodeGenerationException {
		AbstractValueTreeNode child;

		child = getOperand();
		if (child==null) {
			fail(code, "Operand required for operator "+getOperatorString()); //$NON-NLS-1$
		}
		assert(child!=null);

		String operand = child.generate(code);
		if (operand==null) {
			fail(code, "Operand required for operator "+getOperatorString()); //$NON-NLS-1$
			return null;
		}
		
		String tmp = code.createTempVariable();
		
		code.addRecord(new ThreeAddressRecord(
				getThreeAddressOperator(),
				code.address(operand),
				null,
				code.address(tmp)));
		
		return tmp;
	}
	
	/** Replies the three-address instruction for this operator.
	 * 
	 * @return the three-address instruction for this operator.
	 */
	protected abstract ThreeAddressInstruction getThreeAddressOperator();

}
