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
package fr.utbm.info.da53.lw2.syntaxtree;

import fr.utbm.info.da53.lw2.context.ExecutionContext;
import fr.utbm.info.da53.lw2.error.ErrorRepository;
import fr.utbm.info.da53.lw2.error.InterpreterErrorType;
import fr.utbm.info.da53.lw2.error.InterpreterException;
import fr.utbm.info.da53.lw2.error.InterpreterWarning;

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
	
	/**
	 */
	public AbstractSyntaxTreeNode() {
		//
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
	 * @param context is the execution context.
	 * @param type is the type of the warning.
	 * @param message is the additional message.
	 */
	protected void warn(ExecutionContext context, InterpreterErrorType type, String message) {
		ErrorRepository.add(new InterpreterWarning(
				type,
				context.getCurrentLine(),
				message));
	}
	
	/** Warn the user.
	 * 
	 * @param context is the execution context.
	 * @param type is the type of the warning.
	 */
	protected void warn(ExecutionContext context, InterpreterErrorType type) {
		ErrorRepository.add(new InterpreterWarning(
				type,
				context.getCurrentLine()));
	}

	/** Fail and notify the user. This function never returns.
	 * 
	 * @param context is the execution context.
	 * @param type is the type of the warning.
	 * @param message is the additional message.
	 * @throws InterpreterException
	 */
	protected void fail(ExecutionContext context, InterpreterErrorType type, String message)
	throws InterpreterException {
		throw new InterpreterException(
				type,
				context.getCurrentLine(),
				message);
	}
	
	/** Fail and notify the user.
	 * 
	 * @param context is the execution context.
	 * @param type is the type of the warning.
	 * @throws InterpreterException
	 */
	protected void fail(ExecutionContext context, InterpreterErrorType type)
			throws InterpreterException {
		throw new InterpreterException(
				type,
				context.getCurrentLine());
	}

}
