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

import fr.utbm.info.da53.lw4.threeaddresscode.ThreeAddressCode;
import fr.utbm.info.da53.lw4.threeaddresscode.ThreeAddressInstruction;

/**
 * This abstract class represents any statement that causes
 * to jump in the code.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public abstract class AbstractJumpTreeNode extends AbstractStatementTreeNode {
	
	/**
	 * @param line is the first line in the source program where this node is located.
	 */
	public AbstractJumpTreeNode(int line) {
		super(line);
	}
	
	/** Generate the instructions that map a line number from basic
	 * to a label in three-address code.
	 * 
	 * @param code is the code.
	 * @param line is the basic line.
	 * @return the variable that contains the line in three-address code.
	 */
	protected String generateJumpMapping(ThreeAddressCode code, String line) {
		code.addRecord(ThreeAddressInstruction.param(line));
		code.addRecord(ThreeAddressInstruction.function("JMPMAP", 1, "@ret")); //$NON-NLS-1$ //$NON-NLS-2$
		return "@ret"; //$NON-NLS-1$
	}
	
}
