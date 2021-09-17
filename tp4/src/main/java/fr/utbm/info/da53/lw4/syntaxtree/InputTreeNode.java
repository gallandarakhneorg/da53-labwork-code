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

import java.util.Collections;
import java.util.List;

import fr.utbm.info.da53.lw4.error.IntermediateCodeGenerationException;
import fr.utbm.info.da53.lw4.threeaddresscode.ThreeAddressCode;
import fr.utbm.info.da53.lw4.threeaddresscode.ThreeAddressInstruction;

/**
 * Node for the "INPUT" statement.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class InputTreeNode extends AbstractStatementTreeNode {

	private final List<VariableName> identifiers;

	/**
	 * @param line is the first line in the source program where this node is located.
	 * @param identifiers
	 */
	public InputTreeNode(int line, List<VariableName> identifiers) {
		super(line);
		if (identifiers==null) {
			this.identifiers = Collections.emptyList();
		}
		else {
			this.identifiers = identifiers;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("INPUT "); //$NON-NLS-1$
		for(int i=0; i<this.identifiers.size(); ++i) {
			if (i>0) b.append(","); //$NON-NLS-1$
			b.append(this.identifiers.get(i));
		}
		return b.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void generateStatement(ThreeAddressCode code)
			throws IntermediateCodeGenerationException {
		for(VariableName var : this.identifiers) {
			String message = code.createConstant(var.id()+"="); //$NON-NLS-1$
			code.addRecord(ThreeAddressInstruction.print(message));
			code.addRecord(ThreeAddressInstruction.read(var.id()));
		}
	}

}
