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
package fr.utbm.info.da53.lw2.context;

import java.util.SortedMap;

import fr.utbm.info.da53.lw2.error.InterpreterException;

/**
 * Context of for debugging.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class DebugInterpreter {

	private final Interpreter interpreter;
	private final ExecutionContext rootContext;
	private ExecutionContext currentContext;
	
	/**
	 * @param interpreter
	 * @param rootContext
	 */
	public DebugInterpreter(Interpreter interpreter, ExecutionContext rootContext) {
		this.interpreter = interpreter;
		this.rootContext = rootContext;
		this.currentContext = rootContext;
	}

	/** Replies the code.
	 * 
	 * @return the code.
	 */
	public SortedMap<Integer,Statement> getCode() {
		return this.interpreter.getCode();
	}
	
	/** Replies the root context.
	 * 
	 * @return the root context.
	 */
	public ExecutionContext getRootContext() {
		return this.rootContext;
	}

	/** Replies the current context.
	 * 
	 * @return the current context.
	 */
	public ExecutionContext getCurrentContext() {
		return this.currentContext;
	}
	
	/** Run the program until it stop.
	 * @throws InterpreterException
	 */
	public void runToEnd() throws InterpreterException {
		do {
			this.currentContext = this.interpreter.runStatement(this.currentContext);
		}
		while (this.currentContext!=null && this.currentContext.getCurrentLine()>0);
		if (this.currentContext!=null) {
			this.currentContext.close();
		}
	}

	/** Run one step.
	 * @throws InterpreterException
	 */
	public void step() throws InterpreterException {
		this.currentContext = this.interpreter.runStatement(this.currentContext);
	}

	/** Run the program until the given line reached.
	 * 
	 * @param line
	 * @throws InterpreterException
	 */
	public void runUntilLine(int line) throws InterpreterException {
		do {
			this.currentContext = this.interpreter.runStatement(this.currentContext);
		}
		while (this.currentContext!=null && this.currentContext.getCurrentLine()>0
				&& this.currentContext.getCurrentLine()!=line);
	}

}
