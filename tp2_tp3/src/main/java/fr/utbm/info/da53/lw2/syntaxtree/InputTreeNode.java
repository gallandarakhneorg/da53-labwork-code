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

import java.util.Collections;
import java.util.List;

import fr.utbm.info.da53.lw2.context.ExecutionContext;
import fr.utbm.info.da53.lw2.error.InterpreterErrorType;
import fr.utbm.info.da53.lw2.error.InterpreterException;
import fr.utbm.info.da53.lw2.symbol.SymbolTableEntry;
import fr.utbm.info.da53.lw2.type.NumberUtil;
import fr.utbm.info.da53.lw2.type.Value;
import fr.utbm.info.da53.lw2.type.VariableType;

/**
 * Node for the "INPUT" statement.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class InputTreeNode extends AbstractStatementTreeNode {

	private final List<VariableName> identifiers;

	/**
	 * @param identifiers
	 */
	public InputTreeNode(List<VariableName> identifiers) {
		if (identifiers==null) {
			this.identifiers = Collections.emptyList();
		}
		else {
			this.identifiers = identifiers;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExecutionContext run(ExecutionContext executionContext) throws InterpreterException {
		if (this.identifiers.isEmpty()) {
			executionContext.getInterpreter().getStandardInput().readString("<Press Enter>"); //$NON-NLS-1$
		}
		else {
			for(VariableName variable : this.identifiers) {
				StringBuilder message = new StringBuilder();
				message.append(variable.id());
				message.append("="); //$NON-NLS-1$
				Value v = executionContext.getInterpreter().getStandardInput().readString(message.toString());
				SymbolTableEntry entry = executionContext.getSymbolTableEntry(variable.id());
				if (entry==null) {
					fail(executionContext, InterpreterErrorType.UNDEFINED_VARIABLE,variable.id());
				}
				else {
					AbstractValueTreeNode indexExpression = variable.getArrayIndex();
					if (indexExpression!=null) {
						Value indexValue = indexExpression.evaluate(executionContext);
						if (indexValue.getType()!=VariableType.NUMBER) {
							fail(executionContext, InterpreterErrorType.INVALID_ARRAY_INDEX, variable.id());
						}
						if (indexValue.isUnset()) {
							fail(executionContext, InterpreterErrorType.INVALID_ARRAY_INDEX, variable.id());
						}
						Number index = indexValue.getValue(Number.class);
						if (!NumberUtil.isInteger(index)) {
							warn(executionContext, InterpreterErrorType.INVALID_ARRAY_INDEX, variable.id());
						}
						if (index.intValue()<1) {
							fail(executionContext, InterpreterErrorType.INDEX_OUT_OF_BOUNDS,
									variable.id()+"("+index+")"); //$NON-NLS-1$ //$NON-NLS-2$
						}
						entry.setValueAt(v, index.intValue()-1);
					}
					else {
						entry.setValue(v);
					}
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
		b.append("INPUT "); //$NON-NLS-1$
		for(int i=0; i<this.identifiers.size(); ++i) {
			if (i>0) b.append(","); //$NON-NLS-1$
			b.append(this.identifiers.get(i));
		}
		return b.toString();
	}

}
