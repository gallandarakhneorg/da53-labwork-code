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
package fr.utbm.info.da53.lw2.syntaxtree;

import fr.utbm.info.da53.lw2.context.ExecutionContext;
import fr.utbm.info.da53.lw2.error.InterpreterErrorType;
import fr.utbm.info.da53.lw2.error.InterpreterException;
import fr.utbm.info.da53.lw2.type.Value;

/**
 * Node for any binary operator.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public abstract class AbstractBinaryOperatorTreeNode extends AbstractValueTreeNode {
	
	/**
	 * @param leftOperand
	 * @param rightOperand
	 */
	public AbstractBinaryOperatorTreeNode(AbstractValueTreeNode leftOperand, AbstractValueTreeNode rightOperand) {
		setOperands(leftOperand, rightOperand);
	}
	
	/**
	 */
	public AbstractBinaryOperatorTreeNode() {
		//
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Value evaluate(ExecutionContext executionContext) throws InterpreterException {
		Value left, right;
		AbstractValueTreeNode child;
		
		child = getLeftOperand();
		if (child!=null) {
			left = child.evaluate(executionContext);
		}
		else {
			left = Value.UNDEF;
		}

		child = getRightOperand();
		if (child!=null) {
			right = child.evaluate(executionContext);
		}
		else {
			right = Value.UNDEF;
		}
		
		if (left.isUnset()) {
			warn(executionContext, InterpreterErrorType.UNSET_VALUE, "left operand of "+getOperatorString()); //$NON-NLS-1$
		}

		if (right.isUnset()) {
			warn(executionContext, InterpreterErrorType.UNSET_VALUE, "right operand of "+getOperatorString()); //$NON-NLS-1$
		}
		
		if (left.isSet() && right.isSet()) {
			return compute(executionContext, left, right);
		}

		return Value.UNDEF; 
	}
	
	/** Compute the result.
	 * 
	 * @param executionContext is the context of execution.
	 * @param left is the left operand, always set.
	 * @param right is the right operand, always set.
	 * @return the value, never <code>null</code>.
	 * @throws InterpreterException
	 */
	protected abstract Value compute(ExecutionContext executionContext, Value left, Value right) throws InterpreterException;

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

}
