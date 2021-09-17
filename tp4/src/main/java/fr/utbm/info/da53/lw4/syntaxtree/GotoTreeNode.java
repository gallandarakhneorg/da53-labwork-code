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
import fr.utbm.info.da53.lw4.threeaddresscode.ThreeAddressInstruction;

/**
 * Node for the "GOTO" statement.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class GotoTreeNode extends AbstractJumpTreeNode {
	
	/**
	 * @param linenum is the first line in the source program where this node is located.
	 * @param line
	 */
	public GotoTreeNode(int linenum, AbstractValueTreeNode line) {
		super(linenum);
		setChildren(line);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "GOTO " + getChildAt(0).toString(); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void generateStatement(ThreeAddressCode code)
			throws IntermediateCodeGenerationException {
		AbstractValueTreeNode expression = (AbstractValueTreeNode)getChildAt(0);
		if (expression==null) {
			fail(code, "Line expression is required for GOSUB"); //$NON-NLS-1$
		}
		assert(expression!=null);
		
		String line = expression.generate(code);
		
		String target = generateJumpMapping(code, line);
		
		code.addRecord(ThreeAddressInstruction.jump(target));
	}

}
