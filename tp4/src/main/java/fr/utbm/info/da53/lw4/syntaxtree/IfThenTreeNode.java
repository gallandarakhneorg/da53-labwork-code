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
 * Node for the "IF-THEN-ELSE" statement.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class IfThenTreeNode extends AbstractStatementTreeNode {
	
	/**
	 * @param line is the first line in the source program where this node is located.
	 * @param condition
	 * @param thenStatement
	 * @param elseStatement
	 */
	public IfThenTreeNode(int line, AbstractComparisonOperatorTreeNode condition, AbstractStatementTreeNode thenStatement, AbstractStatementTreeNode elseStatement) {
		super(line);
		setChildren(condition, thenStatement, elseStatement);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("IF "); //$NON-NLS-1$
		b.append(getChildAt(0).toString());
		b.append(" THEN "); //$NON-NLS-1$
		b.append(getChildAt(1).toString());
		Object o = getChildAt(2);
		if (o!=null) {
			b.append(" ELSE "); //$NON-NLS-1$
			b.append(o.toString());
		}
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
			fail(code, "Expecting a condition for IF statement"); //$NON-NLS-1$
		}
		assert(condition!=null);
		
		AbstractStatementTreeNode thenStatement = (AbstractStatementTreeNode)getChildAt(1);
		if (thenStatement==null) {
			fail(code, "Expecting a statement for the THEN clause"); //$NON-NLS-1$
		}
		assert(thenStatement!=null);

		AbstractStatementTreeNode elseStatement = (AbstractStatementTreeNode)getChildAt(2);

		String conditionValue = condition.generate(code);
		
		String afterLabel = code.createLabel();
		String elseLabel = (elseStatement!=null) ? code.createLabel() : afterLabel;
		
		code.addRecord(ThreeAddressInstruction.jumpIfFalse(conditionValue, elseLabel));
		
		thenStatement.generate(code);
		
		if (elseStatement!=null) {
			code.addRecord(ThreeAddressInstruction.jump(afterLabel));
			code.setNextLabel(elseLabel);
			elseStatement.generate(code);
		}
		
		code.setNextLabel(afterLabel);
	}

}
