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
package fr.utbm.info.da53.lw5.syntaxtree;

import fr.utbm.info.da53.lw5.error.CompilationErrorType;
import fr.utbm.info.da53.lw5.error.CompilerException;
import fr.utbm.info.da53.lw5.error.IntermediateCodeGenerationException;
import fr.utbm.info.da53.lw5.threeaddresscode.ThreeAddressCode;
import fr.utbm.info.da53.lw5.type.Value;
import fr.utbm.info.da53.lw5.util.Util;

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
		super(line);
		String s = literal;
		while (s!=null && Util.isString(s)) {
			s = Util.unstringify(s);
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
	public String toString() {
		return Util.stringify(this.literal.getValue(String.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generate(ThreeAddressCode code)
			throws IntermediateCodeGenerationException {
		return code.createConstant(this.literal.getValue(String.class));
	}
	
}
