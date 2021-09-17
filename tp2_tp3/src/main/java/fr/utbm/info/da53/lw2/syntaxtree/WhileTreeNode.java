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
 * Node for the "WHILE" statement.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class WhileTreeNode extends AbstractStatementTreeNode {
	
	/**
	 * @param condition
	 * @param statement
	 */
	public WhileTreeNode(AbstractComparisonOperatorTreeNode condition, AbstractStatementTreeNode statement) {
		setChildren(condition, statement);
	}
	
	private boolean isTrue(ExecutionContext executionContext, AbstractComparisonOperatorTreeNode condition)  throws InterpreterException {
		Value r = condition.evaluate(executionContext);
		if (r.getType()==VariableType.BOOLEAN) {
			return r.getValue(Boolean.class).booleanValue();
		}
		warn(executionContext, InterpreterErrorType.EXPECTING_BOOLEAN, condition.toString());
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExecutionContext run(ExecutionContext executionContext) throws InterpreterException {
		AbstractComparisonOperatorTreeNode condition = (AbstractComparisonOperatorTreeNode)getChildAt(0);
		if (condition==null) {
			warn(executionContext, InterpreterErrorType.EXPECTING_BOOLEAN);
		}
		else if (isTrue(executionContext, condition)) {
			AbstractStatementTreeNode statement = (AbstractStatementTreeNode)getChildAt(1);
			if (statement==null) {
				warn(executionContext, InterpreterErrorType.NOTHING_TO_RUN, toString());
			}
			else {
				do {
					executionContext.getInterpreter().reentrantRun(executionContext, statement);
				}
				while (isTrue(executionContext, condition));
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
		b.append("WHILE "); //$NON-NLS-1$
		b.append(getChildAt(0).toString());
		b.append(" DO"); //$NON-NLS-1$
		Object o = getChildAt(1);
		if (o!=null) {
			b.append(" "); //$NON-NLS-1$
			b.append(o.toString());
		}
		b.append(" WEND"); //$NON-NLS-1$
		return b.toString();
	}

}
