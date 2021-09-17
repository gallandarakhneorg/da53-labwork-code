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
 * Node for the "IF-THEN-ELSE" statement.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class IfThenTreeNode extends AbstractStatementTreeNode {
	
	/**
	 * @param condition
	 * @param thenStatement
	 * @param elseStatement
	 */
	public IfThenTreeNode(AbstractComparisonOperatorTreeNode condition, AbstractStatementTreeNode thenStatement, AbstractStatementTreeNode elseStatement) {
		setChildren(condition, thenStatement, elseStatement);
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
		else {
			Value r = condition.evaluate(executionContext);
			if (r.getType()==VariableType.BOOLEAN) {
				if (r.getValue(Boolean.class)) {
					AbstractStatementTreeNode statement = (AbstractStatementTreeNode)getChildAt(1);
					if (statement==null) {
						warn(executionContext, InterpreterErrorType.NOTHING_TO_RUN, toString());
					}
					else {
						return statement.run(executionContext);
					}
				}
				else {
					AbstractStatementTreeNode statement = (AbstractStatementTreeNode)getChildAt(2);
					if (statement!=null) {
						return statement.run(executionContext);
					}
				}
			}
			else {
				warn(executionContext, InterpreterErrorType.EXPECTING_BOOLEAN, condition.toString());
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
		b.append("IF "); //$NON-NLS-1$
		b.append(getChildAt(0).toString());
		b.append(" THEN "); //$NON-NLS-1$
		b.append(getChildAt(1).toString());
		Object o = getChildAt(2);
		if (o!=null) {
			b.append(" ELSE "); //$NON-NLS-1$
			b.append(o.toString());
		}
		return b.toString();
	}

}
