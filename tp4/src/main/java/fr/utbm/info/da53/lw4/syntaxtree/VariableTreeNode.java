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
package fr.utbm.info.da53.lw4.syntaxtree;

import fr.utbm.info.da53.lw4.error.IntermediateCodeGenerationException;
import fr.utbm.info.da53.lw4.symbol.SymbolTable;
import fr.utbm.info.da53.lw4.threeaddresscode.ThreeAddressCode;

/**
 * Node for any variable
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class VariableTreeNode extends AbstractValueTreeNode {
	
	private final String identifier;
	
	/**
	 * @param line is the first line in the source program where this node is located.
	 * @param identifier
	 */
	public VariableTreeNode(int line, String identifier) {
		super(line);
		assert(identifier!=null && !identifier.isEmpty());
		this.identifier = SymbolTable.formatIdentifier(identifier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.identifier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generate(ThreeAddressCode code)
			throws IntermediateCodeGenerationException {
		return this.identifier;
	}
	
}
