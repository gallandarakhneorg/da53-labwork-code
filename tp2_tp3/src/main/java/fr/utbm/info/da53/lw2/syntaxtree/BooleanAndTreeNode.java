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
import fr.utbm.info.da53.lw2.type.VariableType;

/**
 * Node for the boolean and operator.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class BooleanAndTreeNode extends AbstractBinaryOperatorTreeNode {
	
	/**
	 * @param leftOperand
	 * @param rightOperand
	 */
	public BooleanAndTreeNode(AbstractValueTreeNode leftOperand, AbstractValueTreeNode rightOperand) {
		super(leftOperand, rightOperand);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Value compute(ExecutionContext executionContext, Value left, Value right) throws InterpreterException {
		if (left.getType()!=VariableType.BOOLEAN) {
			fail(executionContext, InterpreterErrorType.EXPECTING_BOOLEAN, "left operand of "+getOperatorString()); //$NON-NLS-1$
		}
		if (right.getType()!=VariableType.BOOLEAN) {
			fail(executionContext, InterpreterErrorType.EXPECTING_BOOLEAN, "right operand of "+getOperatorString()); //$NON-NLS-1$
		}
		Boolean l = left.getValue(Boolean.class);
		Boolean r = left.getValue(Boolean.class);
		return new Value(l.booleanValue() && r.booleanValue());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getOperatorString() {
		return "-"; //$NON-NLS-1$
	}
		
}
