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

import java.util.List;

import fr.utbm.info.da53.lw2.context.ExecutionContext;
import fr.utbm.info.da53.lw2.error.InterpreterErrorType;
import fr.utbm.info.da53.lw2.error.InterpreterException;
import fr.utbm.info.da53.lw2.type.NumberUtil;
import fr.utbm.info.da53.lw2.type.Value;
import fr.utbm.info.da53.lw2.type.VariableType;

/**
 * Node for any binary operator that returns a boolean result. 
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public abstract class AbstractComparisonOperatorTreeNode extends AbstractBinaryOperatorTreeNode {
	
	/**
	 */
	public AbstractComparisonOperatorTreeNode() {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Value compute(ExecutionContext executionContext, Value left, Value right) throws InterpreterException {
		int cr = 0;
		
		if (left.getType()==VariableType.STRING || right.getType()==VariableType.STRING) {
			String lv = left.getValue().toString();
			String rv = right.getValue().toString();
			cr = lv.compareTo(rv);
		}

		switch(left.getType()) {
		case BOOLEAN:
			if (right.getType()!=VariableType.BOOLEAN) {
				fail(executionContext, InterpreterErrorType.EXPECTING_BOOLEAN, "right operand of "+getOperatorString()); //$NON-NLS-1$
			}
			cr = left.getValue(Boolean.class).compareTo(right.getValue(Boolean.class));
			break;
		case NUMBER:
			if (right.getType()!=VariableType.NUMBER) {
				fail(executionContext, InterpreterErrorType.EXPECTING_NUMBER, "right operand of "+getOperatorString()); //$NON-NLS-1$
			}
			cr = NumberUtil.compare(left.getValue(Number.class), right.getValue(Number.class));
			break;
		case ARRAY:
			if (right.getType()!=VariableType.ARRAY) {
				fail(executionContext, InterpreterErrorType.EXPECTING_ARRAY, "right operand of "+getOperatorString()); //$NON-NLS-1$
			}
			List<Value> l = left.getValueArray();
			List<Value> r = right.getValueArray();
			cr = Value.compare(l, r);
			break;
		case STRING:
			//
		}
		
		return new Value(translate(cr));
	}
	
	/** Compare.
	 * 
	 * @param comparisonResult is the numerical representation of the comparison of the two operands.
	 * @return the comparison result.
	 */
	protected abstract boolean translate(int comparisonResult);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getOperatorString() {
		return "<>"; //$NON-NLS-1$
	}

}
