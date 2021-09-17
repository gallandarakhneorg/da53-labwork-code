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
import fr.utbm.info.da53.lw2.symbol.SymbolTable;
import fr.utbm.info.da53.lw2.symbol.SymbolTableEntry;
import fr.utbm.info.da53.lw2.type.NumberUtil;
import fr.utbm.info.da53.lw2.type.Value;
import fr.utbm.info.da53.lw2.type.VariableType;

/**
 * Node for any element of array
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class ArrayElementTreeNode extends AbstractValueTreeNode {
	
	private final String identifier;
	
	/**
	 * @param identifier
	 * @param index
	 */
	public ArrayElementTreeNode(String identifier, AbstractValueTreeNode index) {
		assert(identifier!=null && !identifier.isEmpty());
		this.identifier = SymbolTable.formatIdentifier(identifier);
		setChildren(index);
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
			return Value.UNDEF;
		}
		assert(value!=null);
		if (value.getType()!=VariableType.ARRAY) {
			fail(executionContext, InterpreterErrorType.EXPECTING_ARRAY, this.identifier);
			return Value.UNDEF;
		}
		
		AbstractValueTreeNode indexExpression = (AbstractValueTreeNode)getChildAt(0);
		if (indexExpression==null) {
			fail(executionContext, InterpreterErrorType.INVALID_ARRAY_INDEX, toString());
			return Value.UNDEF;
		}
		
		Value index = indexExpression.evaluate(executionContext);
		if (index.isUnset()) {
			fail(executionContext, InterpreterErrorType.INVALID_ARRAY_INDEX, toString());
			return Value.UNDEF;
		}
		if (index.getType()!=VariableType.NUMBER) {
			fail(executionContext, InterpreterErrorType.INVALID_ARRAY_INDEX, toString());
			return Value.UNDEF;
		}
		
		Number position = index.getValue(Number.class);
		if (!NumberUtil.isInteger(position)) {
			warn(executionContext, InterpreterErrorType.INVALID_ARRAY_INDEX, toString());
		}
		
		List<Value> array = value.getValueArray();
		assert(array!=null);
		
		int javaPosition = position.intValue() - 1;
		
		if (javaPosition<0 || javaPosition>=array.size()) {
			String range;
			if (array.isEmpty()) range = ""; //$NON-NLS-1$
			else range = "1;"+array.size(); //$NON-NLS-1$
			fail(executionContext, InterpreterErrorType.INDEX_OUT_OF_BOUNDS,
					this.identifier
					+"(" //$NON-NLS-1$
					+position.intValue()
					+ ") not in [" //$NON-NLS-1$
					+range
					+"]"); //$NON-NLS-1$
			return Value.UNDEF;
		}
		
		Value element = array.get(javaPosition);
		if (element==null) return Value.UNDEF;
		return element;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(this.identifier);
		b.append("("); //$NON-NLS-1$
		Object index = getChildAt(0);
		if (index!=null) b.append(index.toString());
		b.append(")"); //$NON-NLS-1$
		return b.toString();
	}
	
}
