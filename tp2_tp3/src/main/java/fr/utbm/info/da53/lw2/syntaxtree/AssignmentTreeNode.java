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
import fr.utbm.info.da53.lw2.symbol.SymbolTableEntry;
import fr.utbm.info.da53.lw2.type.NumberUtil;
import fr.utbm.info.da53.lw2.type.Value;
import fr.utbm.info.da53.lw2.type.VariableType;

/**
 * Node for the "LET" statement.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class AssignmentTreeNode extends AbstractStatementTreeNode {
	
	private final VariableName identifier;
	
	/**
	 * @param identifier
	 * @param expression
	 */
	public AssignmentTreeNode(VariableName identifier, AbstractValueTreeNode expression) {
		this.identifier = identifier;
		if (expression!=null) setChildren(expression);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExecutionContext run(ExecutionContext executionContext) throws InterpreterException {
		Value value = Value.UNDEF;
		AbstractValueTreeNode expression = (AbstractValueTreeNode)getChildAt(0);
		if (expression!=null) {
			value = expression.evaluate(executionContext);
		}
		if (value.isUnset()) {
			warn(executionContext, InterpreterErrorType.UNSET_VALUE);
		}

		SymbolTableEntry entry = executionContext.getSymbolTableEntry(this.identifier.id());
		if (entry==null) {
			fail(executionContext, InterpreterErrorType.UNDEFINED_VARIABLE, this.identifier.id());
			return executionContext;
		}
		
		AbstractValueTreeNode indexExpression = this.identifier.getArrayIndex();
		if (indexExpression!=null) {
			Value indexValue = indexExpression.evaluate(executionContext);
			if (indexValue.getType()!=VariableType.NUMBER) {
				fail(executionContext, InterpreterErrorType.INVALID_ARRAY_INDEX, this.identifier.id());
				return executionContext;
			}
			if (indexValue.isUnset()) {
				fail(executionContext, InterpreterErrorType.INVALID_ARRAY_INDEX, this.identifier.id());
				return executionContext;
			}
			Number index = indexValue.getValue(Number.class);
			if (!NumberUtil.isInteger(index)) {
				warn(executionContext, InterpreterErrorType.INVALID_ARRAY_INDEX, this.identifier.id());
			}
			if (index.intValue()<1) {
				fail(executionContext, InterpreterErrorType.INDEX_OUT_OF_BOUNDS,
						this.identifier.id()+"("+index+")"); //$NON-NLS-1$ //$NON-NLS-2$
				return executionContext;
			}
			entry.setValueAt(value, index.intValue()-1);
		}
		else {
			entry.setValue(value);
		}
		
		return executionContext;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "LET " + this.identifier.toString() + " = " + getChildAt(0).toString(); //$NON-NLS-1$ //$NON-NLS-2$
	}

}
