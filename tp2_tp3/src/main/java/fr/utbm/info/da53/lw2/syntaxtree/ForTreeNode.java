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
import fr.utbm.info.da53.lw2.symbol.SymbolTable;
import fr.utbm.info.da53.lw2.symbol.SymbolTableEntry;
import fr.utbm.info.da53.lw2.type.NumberUtil;
import fr.utbm.info.da53.lw2.type.Value;
import fr.utbm.info.da53.lw2.type.VariableType;

/**
 * Node for the "FOR" statement.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class ForTreeNode extends AbstractStatementTreeNode {

	private final String identifier;

	/**
	 * @param identifier
	 * @param startValue
	 * @param endValue
	 * @param stepValue
	 * @param statement
	 */
	public ForTreeNode(String identifier, AbstractValueTreeNode startValue, AbstractValueTreeNode endValue, AbstractValueTreeNode stepValue, AbstractStatementTreeNode statement) {
		this.identifier = SymbolTable.formatIdentifier(identifier);
		setChildren(startValue, endValue, stepValue, statement);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExecutionContext run(ExecutionContext executionContext) throws InterpreterException {
		AbstractValueTreeNode startExpression = (AbstractValueTreeNode)getChildAt(0);
		if (startExpression==null) {
			fail(executionContext, InterpreterErrorType.EXPECTING_NUMBER, "from"); //$NON-NLS-1$
		}
		assert(startExpression!=null);
		Value startValue = startExpression.evaluate(executionContext);
		if (startValue.isUnset()) {
			fail(executionContext, InterpreterErrorType.UNSET_VALUE, "from"); //$NON-NLS-1$
		}
		if (startValue.getType()!=VariableType.NUMBER) {
			fail(executionContext, InterpreterErrorType.EXPECTING_NUMBER, "from"); //$NON-NLS-1$
		}
		
		AbstractValueTreeNode endExpression = (AbstractValueTreeNode)getChildAt(1);
		if (endExpression==null) {
			fail(executionContext, InterpreterErrorType.EXPECTING_NUMBER, "to"); //$NON-NLS-1$
		}
		assert(endExpression!=null);
		Value endValue = endExpression.evaluate(executionContext);
		if (endValue.isUnset()) {
			fail(executionContext, InterpreterErrorType.UNSET_VALUE, "to"); //$NON-NLS-1$
		}
		if (endValue.getType()!=VariableType.NUMBER) {
			fail(executionContext, InterpreterErrorType.EXPECTING_NUMBER, "to"); //$NON-NLS-1$
		}
		
		Number startNumber = startValue.getValue(Number.class);
		Number endNumber = endValue.getValue(Number.class);
		
		Number step = 1;
		AbstractValueTreeNode stepExpression = (AbstractValueTreeNode)getChildAt(2);
		if (stepExpression!=null) {
			Value stepValue = stepExpression.evaluate(executionContext);
			if (stepValue.isUnset()) {
				fail(executionContext, InterpreterErrorType.UNSET_VALUE, "step"); //$NON-NLS-1$
			}
			if (stepValue.getType()!=VariableType.NUMBER) {
				fail(executionContext, InterpreterErrorType.EXPECTING_NUMBER, "step"); //$NON-NLS-1$
			}
			
			step = stepValue.getValue(Number.class);
			
			if (step.intValue()==0) {
				warn(executionContext, InterpreterErrorType.ZERO_STEP, "step"); //$NON-NLS-1$
				step = 1;
			}
			
			if ((NumberUtil.isPositive(step) && startNumber.doubleValue()>endNumber.doubleValue())
				||
				(NumberUtil.isNegative(step) && startNumber.doubleValue()<endNumber.doubleValue())) {
				warn(executionContext, InterpreterErrorType.INVERTED_START_END_INDEXES,
						startNumber.toString() + " > " + endNumber.toString()); //$NON-NLS-1$
				step = NumberUtil.negate(step);
			}
		}
		else if (startNumber.doubleValue()>endNumber.doubleValue()) {
			warn(executionContext, InterpreterErrorType.INVERTED_START_END_INDEXES,
					startNumber.toString() + " > " + endNumber.toString()); //$NON-NLS-1$
			step = -1;
		}
		
		AbstractStatementTreeNode statement = (AbstractStatementTreeNode)getChildAt(3);
		if (statement==null) {
			warn(executionContext, InterpreterErrorType.NOTHING_TO_RUN);
		}
		else {
			SymbolTableEntry counter = executionContext.getSymbolTableEntry(this.identifier);
			if (counter==null) {
				fail(executionContext, InterpreterErrorType.UNDEFINED_VARIABLE);
			}
			else {
				counter.setValue(startNumber);
				while (NumberUtil.compare(counter.getValue().getValue(Number.class), endNumber) <= 0) {
					executionContext.getInterpreter().reentrantRun(executionContext, statement);
					counter.setValue(
							NumberUtil.add(
									counter.getValue().getValue(Number.class), 
									step,
									executionContext.getCurrentLine()));
				}
			}
		}

		return executionContext;
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

}
