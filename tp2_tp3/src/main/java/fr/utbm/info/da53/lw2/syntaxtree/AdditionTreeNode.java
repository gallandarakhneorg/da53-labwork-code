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
import fr.utbm.info.da53.lw2.type.NumberUtil;
import fr.utbm.info.da53.lw2.type.Value;
import fr.utbm.info.da53.lw2.type.VariableType;

/**
 * Node for the addition operator.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class AdditionTreeNode extends AbstractBinaryOperatorTreeNode {
	
	/**
	 * @param leftOperand
	 * @param rightOperand
	 */
	public AdditionTreeNode(AbstractValueTreeNode leftOperand, AbstractValueTreeNode rightOperand) {
		super(leftOperand, rightOperand);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Value compute(ExecutionContext executionContext, Value left, Value right) throws InterpreterException {
		if (left.getType()==VariableType.STRING || right.getType()==VariableType.STRING) {
			return new Value(left.getValue().toString() + right.getValue().toString());
		}
		
		if (left.getType()!=VariableType.NUMBER) {
			fail(executionContext, InterpreterErrorType.EXPECTING_NUMBER, "left operand of "+getOperatorString()); //$NON-NLS-1$
		}
		if (right.getType()!=VariableType.NUMBER) {
			fail(executionContext, InterpreterErrorType.EXPECTING_NUMBER, "right operand of "+getOperatorString()); //$NON-NLS-1$
		}
		Number l = left.getValue(Number.class);
		Number r = right.getValue(Number.class);
		return new Value(NumberUtil.toNumber(l.doubleValue() + r.doubleValue()));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getOperatorString() {
		return "+"; //$NON-NLS-1$
	}

}
