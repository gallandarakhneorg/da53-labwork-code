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
import fr.utbm.info.da53.lw4.threeaddresscode.ThreeAddressCode;
import fr.utbm.info.da53.lw4.type.Value;

/**
 * Node for any boolean constant
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class BooleanTreeNode extends AbstractValueTreeNode {
	
	private final Value literal;
	
	/**
	 * @param line is the first line in the source program where this node is located.
	 * @param v
	 */
	public BooleanTreeNode(int line, boolean v) {
		super(line);
		this.literal = new Value(Boolean.valueOf(v));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.literal.getValue().toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generate(ThreeAddressCode code)
			throws IntermediateCodeGenerationException {
		return this.literal.getValue(Boolean.class).toString();
	}
	
}
