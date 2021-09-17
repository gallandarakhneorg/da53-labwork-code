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

import fr.utbm.info.da53.lw4.construct.Statement;
import fr.utbm.info.da53.lw4.error.IntermediateCodeGenerationException;
import fr.utbm.info.da53.lw4.threeaddresscode.ThreeAddressCode;
import fr.utbm.info.da53.lw4.threeaddresscode.ThreeAddressRecord;

/**
 * This abstract class represents any node in the syntax tree
 * that is for a statement.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public abstract class AbstractStatementTreeNode extends AbstractSyntaxTreeNode implements Statement {
	
	private int basicLine = 0;
	
	/**
	 * @param line is the first line in the source program where this node is located.
	 */
	public AbstractStatementTreeNode(int line) {
		super(line);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int basicLine() {
		return this.basicLine;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBasicLine(int line) {
		this.basicLine = line;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void generate(ThreeAddressCode code)
			throws IntermediateCodeGenerationException {
		int count = code.getRecordCount();
		code.startInstruction(basicLine());
		generateStatement(code);
		int after = code.getRecordCount();
		if (after>count) {
			ThreeAddressRecord record = code.getRecord(count);
			record.setComment(toString().replaceAll("[\n\r]+", " ")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/** Generate the three address code for this node.
	 * 
	 * @param code is the code inside which the generation must be done.
	 * @throws IntermediateCodeGenerationException
	 */
	protected abstract void generateStatement(ThreeAddressCode code) throws IntermediateCodeGenerationException;

}
