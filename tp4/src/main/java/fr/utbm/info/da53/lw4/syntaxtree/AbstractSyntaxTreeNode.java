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

import fr.utbm.info.da53.lw4.error.ErrorRepository;
import fr.utbm.info.da53.lw4.error.IntermediateCodeGenerationException;
import fr.utbm.info.da53.lw4.threeaddresscode.ThreeAddressCode;

/**
 * This abstract class represents any node in the syntax tree.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public abstract class AbstractSyntaxTreeNode {
	
	/** Children in the syntax tree.
	 */
	private AbstractSyntaxTreeNode[] children = new AbstractSyntaxTreeNode[0];
	
	/** First line in the source program where this node is located.
	 */
	private final int sourceLine;
	
	/**
	 * @param sourceLine is the first line in the source program where this node is located.
	 */
	public AbstractSyntaxTreeNode(int sourceLine) {
		this.sourceLine = sourceLine;
	}
	
	/** Replies the first line in the source program where this node is located.
	 * 
	 * @return the first line.
	 */
	public int sourceLine() {
		return this.sourceLine;
	}

	/** Set the children.
	 * 
	 * @param children
	 */
	protected void setChildren(AbstractSyntaxTreeNode... children) {
		this.children = children;
	}
	
	/** Replies the child node at the given index.
	 * 
	 * @param index
	 * @return the child or <code>null</code> if none.
	 */
	protected AbstractSyntaxTreeNode getChildAt(int index) {
		if (index>=0 && index<this.children.length) {
			return this.children[index];
		}
		return null;
	}
	
	/** Replies the number of children.
	 * 
	 * @return the number of children.
	 */
	protected int getChildCount() {
		return this.children.length;
	}
	
	/** Warn the user.
	 * 
	 * @param code is the three address code.
	 * @param message is the additional message.
	 */
	protected void warn(ThreeAddressCode code, String message) {
		ErrorRepository.add(new IntermediateCodeGenerationException(
				sourceLine(),
				message));
	}
	
	/** Warn the user.
	 * 
	 * @param code is the three address code.
	 * @param e
	 */
	protected void warn(ThreeAddressCode code, Throwable e) {
		ErrorRepository.add(new IntermediateCodeGenerationException(
				sourceLine(),
				e));
	}

	/** Fail and notify the user. This function never returns.
	 * 
	 * @param code is the three address code.
	 * @param message is the additional message.
	 * @throws IntermediateCodeGenerationException
	 */
	protected void fail(ThreeAddressCode code, String message)
	throws IntermediateCodeGenerationException {
		throw new IntermediateCodeGenerationException(
				sourceLine(),
				message);
	}
	
	/** Fail and notify the user.
	 * 
	 * @param code is the three address code.
	 * @param e
	 * @throws IntermediateCodeGenerationException
	 */
	protected void fail(ThreeAddressCode code, Throwable e)
			throws IntermediateCodeGenerationException {
		throw new IntermediateCodeGenerationException(
				sourceLine(), e);
	}

}
