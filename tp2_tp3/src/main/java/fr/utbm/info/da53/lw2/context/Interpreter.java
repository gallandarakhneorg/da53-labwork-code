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
 * Collection of statements.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public interface Interpreter {

	/** Replies the code.
	 * 
	 * @return the code.
	 */
	public SortedMap<Integer,Statement> getCode();
	
	/** Run the entire code.
	 * 
	 * @throws InterpreterException
	 */
	public void run() throws InterpreterException;

	/** Run the statement inside a new execution context which
	 * is inherit from the given one.
	 * 
	 * @param executionContext
	 * @param statement
	 * @throws InterpreterException
	 */
	public void reentrantRun(ExecutionContext executionContext, Statement statement) throws InterpreterException;

	/**
	 * Run one statement.
	 * 
	 * @param context
	 * @return the new top context.
	 * @throws InterpreterException
	 */
	public ExecutionContext runStatement(ExecutionContext context) throws InterpreterException;

	/** Create a debug context.
	 * 
	 * @return the execution context
	 * @throws InterpreterException
	 */
	public DebugInterpreter debug() throws InterpreterException;

	/** Set the standard input to use.
	 * 
	 * @param stdin
	 */
	public void setStandardInput(StandardInput stdin);

	/** Replies the standard input.
	 * 
	 * @return the stdin
	 */
	public StandardInput getStandardInput();

	/** Set the standard output to use.
	 * 
	 * @param stdout
	 */
	public void setStandardOutput(StandardOutput stdout);

	/** Replies the standard output.
	 * 
	 * @return the stdout
	 */
	public StandardOutput getStandardOutput();
	
	/** Stop the interpreter.
	 */
	public void exit();

	/** Add a listener on the interpreter events.
	 * 
	 * @param listener
	 */
	public void addInterpreterListener(InterpreterListener listener);

	/** Remove a listener on the interpreter events.
	 * 
	 * @param listener
	 */
	public void removeInterpreterListener(InterpreterListener listener);

}
