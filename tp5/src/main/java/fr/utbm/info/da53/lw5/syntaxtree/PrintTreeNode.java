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
import fr.utbm.info.da53.lw5.threeaddresscode.ThreeAddressInstruction;

/**
 * Node for the "PRINT" statement.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class PrintTreeNode extends AbstractStatementTreeNode {
	
	/**
	 * @param line is the first line in the source program where this node is located.
	 * @param expression is the expression to display.
	 */
	public PrintTreeNode(int line, AbstractValueTreeNode expression) {
		super(line);
		if (expression!=null) setChildren(expression);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "PRINT " + getChildAt(0).toString(); //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void generateStatement(ThreeAddressCode code) throws IntermediateCodeGenerationException {
		AbstractValueTreeNode expression = (AbstractValueTreeNode)getChildAt(0);
		String cr = code.createConstant("\n"); //$NON-NLS-1$
		if (expression!=null) {
			String value = expression.generate(code);
			if (value!=null) {
				code.addRecord(ThreeAddressInstruction.print(
						code.address(value)));
			}
		}
		code.addRecord(ThreeAddressInstruction.print(code.address(cr)));
	}

}
