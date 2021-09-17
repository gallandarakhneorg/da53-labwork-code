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
 * Node for the "WHILE" statement.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class WhileTreeNode extends AbstractStatementTreeNode {
	
	/**
	 * @param line is the first line in the source program where this node is located.
	 * @param condition
	 * @param statement
	 */
	public WhileTreeNode(int line, AbstractComparisonOperatorTreeNode condition, AbstractStatementTreeNode statement) {
		super(line);
		setChildren(condition, statement);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("WHILE "); //$NON-NLS-1$
		b.append(getChildAt(0).toString());
		b.append(" DO"); //$NON-NLS-1$
		Object o = getChildAt(1);
		if (o!=null) {
			b.append(" "); //$NON-NLS-1$
			b.append(o.toString());
		}
		b.append(" WEND"); //$NON-NLS-1$
		return b.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void generateStatement(ThreeAddressCode code)
			throws IntermediateCodeGenerationException {
		AbstractComparisonOperatorTreeNode condition = (AbstractComparisonOperatorTreeNode)getChildAt(0);
		if (condition==null) {
			fail(code, "Condition required for WHILE statement"); //$NON-NLS-1$
		}
		assert(condition!=null);
		
		String conditionLabel = code.createLabel();
		String afterLabel = code.createLabel();
		
		code.setNextLabel(conditionLabel);
		String conditionResult = condition.generate(code);
		
		code.addRecord(ThreeAddressInstruction.jumpIfFalse(conditionResult, afterLabel));
		
		AbstractStatementTreeNode statement = (AbstractStatementTreeNode)getChildAt(1);
		if (statement==null) {
			warn(code, "No statement for WHILE loop"); //$NON-NLS-1$
		}
		else {
			statement.generate(code);
		}
		
		code.addRecord(ThreeAddressInstruction.jump(conditionLabel));
		
		code.setNextLabel(afterLabel);
	}

}
