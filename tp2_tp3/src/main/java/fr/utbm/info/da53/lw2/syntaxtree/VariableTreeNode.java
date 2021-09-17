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
import fr.utbm.info.da53.lw2.type.Value;

/**
 * Node for any variable
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class VariableTreeNode extends AbstractValueTreeNode {
	
	private final String identifier;
	
	/**
	 * @param identifier
	 */
	public VariableTreeNode(String identifier) {
		assert(identifier!=null && !identifier.isEmpty());
		this.identifier = SymbolTable.formatIdentifier(identifier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Value evaluate(ExecutionContext executionContext) throws InterpreterException {
		SymbolTableEntry entry = executionContext.getSymbolTableEntry(this.identifier);
		if (entry==null) {
			warn(executionContext, InterpreterErrorType.UNDEFINED_VARIABLE, this.identifier);
			return Value.UNDEF;
		}
		Value value = entry.getValue();
		if (value.isUnset()) {
			warn(executionContext, InterpreterErrorType.UNSET_VALUE, this.identifier);
		}
		assert(value!=null);
		return value;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.identifier;
	}
	
}
