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

import fr.utbm.info.da53.lw4.threeaddresscode.ThreeAddressInstruction;

/**
 * Node for the "<=" relational operator.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class LowerEqualTreeNode extends AbstractComparisonOperatorTreeNode {
	
	/**
	 * @param line is the first line in the source program where this node is located.
	 */
	public LowerEqualTreeNode(int line) {
		super(line);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean translate(int comparisonResult) {
		return comparisonResult <= 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getOperatorString() {
		return "<="; //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ThreeAddressInstruction getThreeAddressOperator() {
		return ThreeAddressInstruction.BOOLEAN_LESS_EQUAL;
	}
}
