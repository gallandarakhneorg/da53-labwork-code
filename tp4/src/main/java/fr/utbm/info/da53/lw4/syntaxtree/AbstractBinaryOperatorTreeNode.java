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
import fr.utbm.info.da53.lw4.threeaddresscode.ThreeAddressCode;
import fr.utbm.info.da53.lw4.threeaddresscode.ThreeAddressInstruction;
import fr.utbm.info.da53.lw4.threeaddresscode.ThreeAddressRecord;

/**
 * Node for any binary operator.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public abstract class AbstractBinaryOperatorTreeNode extends AbstractValueTreeNode {
	
	/**
	 * @param line is the first line in the source program where this node is located.
	 * @param leftOperand
	 * @param rightOperand
	 */
	public AbstractBinaryOperatorTreeNode(int line, AbstractValueTreeNode leftOperand, AbstractValueTreeNode rightOperand) {
		super(line);
		setOperands(leftOperand, rightOperand);
	}
	
	/**
	 * @param line is the first line in the source program where this node is located.
	 */
	public AbstractBinaryOperatorTreeNode(int line) {
		super(line);
	}
	

	/** Set the operands.
	 * 
	 * @param leftOperand
	 * @param rightOperand
	 */
	public void setOperands(AbstractValueTreeNode leftOperand, AbstractValueTreeNode rightOperand) {
		setChildren(leftOperand, rightOperand);
	}
	
	/** Replies the left operand.
	 * 
	 * @return the left operand.
	 */
	protected AbstractValueTreeNode getLeftOperand() {
		return (AbstractValueTreeNode)getChildAt(0);
	}
	
	/** Replies the right operand.
	 * 
	 * @return the right operand.
	 */
	protected AbstractValueTreeNode getRightOperand() {
		return (AbstractValueTreeNode)getChildAt(1);
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
				+getLeftOperand().toString()
				+getOperatorString()
				+getRightOperand().toString()
				+")"; //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String generate(ThreeAddressCode code)
			throws IntermediateCodeGenerationException {
		AbstractValueTreeNode child;
		String left, right;
		
		child = getLeftOperand();
		if (child==null) {
			fail(code, "Left operand required for operator "+getOperatorString()); //$NON-NLS-1$
		}
		assert(child!=null);
		left = child.generate(code);
		if (left==null) {
			fail(code, "Left operand required for operator "+getOperatorString()); //$NON-NLS-1$
		}
		assert(left!=null);

		child = getRightOperand();
		if (child==null) {
			fail(code, "Right operand required for operator "+getOperatorString()); //$NON-NLS-1$
		}
		assert(child!=null);
		right = child.generate(code);
		if (right==null) {
			fail(code, "Right operand required for operator "+getOperatorString()); //$NON-NLS-1$
		}
		assert(right!=null);
		
		String tmp = code.createTempVariable();
		
		code.addRecord(new ThreeAddressRecord(
				getThreeAddressOperator(),
				left,
				right,
				tmp));
		
		return tmp;
	}
	
	/** Replies the three-address instruction for this operator.
	 * 
	 * @return the three-address instruction for this operator.
	 */
	protected abstract ThreeAddressInstruction getThreeAddressOperator();

}
