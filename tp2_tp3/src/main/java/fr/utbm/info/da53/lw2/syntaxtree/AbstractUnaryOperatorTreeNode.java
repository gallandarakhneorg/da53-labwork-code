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
 * Node for any unary operator.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public abstract class AbstractUnaryOperatorTreeNode extends AbstractValueTreeNode {
	
	/**
	 * @param operand
	 */
	public AbstractUnaryOperatorTreeNode(AbstractValueTreeNode operand) {
		setOperand(operand);
	}
	
	/**
	 */
	public AbstractUnaryOperatorTreeNode() {
		//
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Value evaluate(ExecutionContext executionContext) throws InterpreterException {
		Value left;
		AbstractValueTreeNode child;
		
		child = getOperand();
		if (child!=null) {
			left = child.evaluate(executionContext);
		}
		else {
			left = Value.UNDEF;
		}

		if (left.isUnset()) {
			warn(executionContext, InterpreterErrorType.UNSET_VALUE, "left operand of "+getOperatorString()); //$NON-NLS-1$
		}

		if (left.isSet()) {
			return compute(executionContext, left);
		}

		return Value.UNDEF; 
	}
	
	/** Compute the result.
	 * 
	 * @param executionContext is the context of execution.
	 * @param operandValue is the value of the operand, always set.
	 * @return the value, never <code>null</code>.
	 * @throws InterpreterException
	 */
	protected abstract Value compute(ExecutionContext executionContext, Value operandValue) throws InterpreterException;

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

}
