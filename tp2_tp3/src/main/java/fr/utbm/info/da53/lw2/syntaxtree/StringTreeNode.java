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
import fr.utbm.info.da53.lw2.error.CompilationErrorType;
import fr.utbm.info.da53.lw2.error.CompilerException;
import fr.utbm.info.da53.lw2.error.InterpreterException;
import fr.utbm.info.da53.lw2.type.Value;

/**
 * Node for the string literal.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class StringTreeNode extends AbstractValueTreeNode {
	
	private final Value literal;
	
	/**
	 * @param literal
	 * @param line
	 * @throws CompilerException
	 */
	public StringTreeNode(String literal, int line) throws CompilerException {
		String s;
		if (literal!=null
				&& literal.startsWith("\"") //$NON-NLS-1$
				&& literal.endsWith("\"") //$NON-NLS-1$
				&& literal.length()>=2) {
			s = literal.substring(1, literal.length()-1);
		}
		else {
			s = literal;
		}
		if (s==null) {
			throw new CompilerException(CompilationErrorType.STRING_LITERAL_REQUIRED, line, literal);
		}
		this.literal = new Value(s);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Value evaluate(ExecutionContext executionContext) throws InterpreterException {
		return this.literal;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "\"" //$NON-NLS-1$
				+ this.literal
				+ "\""; //$NON-NLS-1$
	}
	
}
