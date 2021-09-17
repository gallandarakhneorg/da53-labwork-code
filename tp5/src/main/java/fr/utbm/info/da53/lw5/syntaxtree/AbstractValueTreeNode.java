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

import fr.utbm.info.da53.lw5.error.IntermediateCodeGenerationException;
import fr.utbm.info.da53.lw5.threeaddresscode.ThreeAddressCode;

/**
 * This abstract class represents any node that is representing a value
 * (number of string literal).
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public abstract class AbstractValueTreeNode extends AbstractSyntaxTreeNode {
	
	/**
	 * @param line is the first line in the source program where this node is located.
	 */
	public AbstractValueTreeNode(int line) {
		super(line);
	}
	
	/** Generate the three address code for this node.
	 * 
	 * @param code is the code inside which the generation must be done.
	 * @throws IntermediateCodeGenerationException
	 * @return the name of the variable initialized/return by the generated block;
	 * or <code>null</code> if none. 
	 */
	public abstract String generate(ThreeAddressCode code) throws IntermediateCodeGenerationException;

}
