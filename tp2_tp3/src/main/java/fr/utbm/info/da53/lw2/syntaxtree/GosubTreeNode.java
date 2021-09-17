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
 * Node for the "GOSUB" statement.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class GosubTreeNode extends AbstractStatementTreeNode {
	
	/**
	 * @param line
	 */
	public GosubTreeNode(AbstractValueTreeNode line) {
		setChildren(line);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExecutionContext run(ExecutionContext executionContext) throws InterpreterException {
		ExecutionContext context = executionContext;
		AbstractValueTreeNode expression = (AbstractValueTreeNode)getChildAt(0);
		if (expression==null) {
			fail(executionContext, InterpreterErrorType.EXPECTING_INTEGER, toString());
		}
		else {
			Value v = expression.evaluate(executionContext);
			if (v.isSet() && v.getType()==VariableType.NUMBER) {
				Number n = v.getValue(Number.class);
				if (!NumberUtil.isInteger(n)) {
					warn(executionContext, InterpreterErrorType.EXPECTING_INTEGER, expression.toString());
				}
				context = new ExecutionContext(context);
				context.setNextLine(n.intValue());
			}
			else {
				fail(executionContext, InterpreterErrorType.EXPECTING_INTEGER, expression.toString());
			}
		}
		return context;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "GOSUB " + getChildAt(0).toString(); //$NON-NLS-1$
	}

}
