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
 * Node for the "PRINT" statement.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class PrintTreeNode extends AbstractStatementTreeNode {
	
	/**
	 * @param expression is the expression to display.
	 */
	public PrintTreeNode(AbstractValueTreeNode expression) {
		if (expression!=null) setChildren(expression);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExecutionContext run(ExecutionContext executionContext) throws InterpreterException {
		AbstractValueTreeNode expression = (AbstractValueTreeNode)getChildAt(0);
		if (expression!=null) {
			Value v = expression.evaluate(executionContext);
			assert(v!=null);
			if (v.isSet()) {
				executionContext.getInterpreter().getStandardOutput().println(v.getValue().toString());
			}
			else {
				warn(executionContext, InterpreterErrorType.UNSET_VALUE);
				executionContext.getInterpreter().getStandardOutput().println();
			}
		}
		else {
			executionContext.getInterpreter().getStandardOutput().println();
		}
		return executionContext;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "PRINT " + getChildAt(0).toString(); //$NON-NLS-1$
	}

}
