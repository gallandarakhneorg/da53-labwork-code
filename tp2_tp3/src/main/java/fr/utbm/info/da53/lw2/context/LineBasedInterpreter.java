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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;

import fr.utbm.info.da53.lw2.error.ErrorRepository;
import fr.utbm.info.da53.lw2.error.InterpreterErrorType;
import fr.utbm.info.da53.lw2.error.InterpreterException;
import fr.utbm.info.da53.lw2.error.InterpreterWarning;
import fr.utbm.info.da53.lw2.symbol.SymbolTable;
import fr.utbm.info.da53.lw2.type.Value;

/**
 * Abstract implementation of an Interpreter.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class LineBasedInterpreter implements Interpreter {

	private final SortedMap<Integer,Statement> code;
	private final SymbolTable symbolTable;
	private Iterator<Entry<Integer,Statement>> stream;
	private Entry<Integer,Statement> current;
	private StandardInput stdin = new StdIn();
	private StandardOutput stdout = new StdOut();
	private final List<InterpreterListener> listeners = new ArrayList<InterpreterListener>();
	
	/**
	 * @param code
	 * @param symbolTable
	 */
	public LineBasedInterpreter(SortedMap<Integer,Statement> code, SymbolTable symbolTable) {
		this.code = code;
		this.symbolTable = symbolTable;
		this.stream = null;
		this.current = null;
		if (this.code!=null) {
			this.stream = code.entrySet().iterator();
			if (this.stream.hasNext()) {
				this.current = this.stream.next();
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addInterpreterListener(InterpreterListener listener) {
		synchronized(this.listeners) {
			this.listeners.add(listener);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeInterpreterListener(InterpreterListener listener) {
		synchronized(this.listeners) {
			this.listeners.remove(listener);
		}
	}

	/**
	 * Replies the listeners.
	 * @return the listeners.
	 */
	protected InterpreterListener[] getListeners() {
		synchronized(this.listeners) {
			InterpreterListener[] list = new InterpreterListener[this.listeners.size()];
			this.listeners.toArray(list);
			return list;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exit() {
		boolean notified = false;
		for(InterpreterListener listener : getListeners()) {
			notified = true;
			listener.interpreterKilled(this);
		}
		if (!notified) System.exit(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExecutionContext runStatement(ExecutionContext context) throws InterpreterException {
		assert(context!=null);
		ExecutionContext nextContext = null;

		if (this.current!=null) {
			Entry<Integer,Statement> followingEntry = null;
			if (this.stream!=null && this.stream.hasNext()) {
				followingEntry = this.stream.next();
			}
			
			int currentLine = this.current.getKey();
			Statement currentStatement = this.current.getValue();
			
			if (currentStatement!=null) {
				context.setCurrentLine(currentLine);
				if (followingEntry!=null)
					context.setNextLine(followingEntry.getKey());
				else
					context.setNextLine(-1);
				
				nextContext = currentStatement.run(context);
				
				int nextLine = nextContext.getNextLine();
				if (nextLine>0 && (followingEntry==null || nextLine!=followingEntry.getKey())) {
					followingEntry = detectNextCS(context.getCurrentLine(), nextLine);
				}
				
				nextContext.setCurrentLine(followingEntry==null ? -1 : nextLine);
				nextContext.setNextLine(-1);
			}
			else {
				ErrorRepository.add(new InterpreterWarning(InterpreterErrorType.NOTHING_TO_RUN, context.getCurrentLine()));
			}
			
			this.current = followingEntry;
			
		}
		else {
			ErrorRepository.add(new InterpreterWarning(InterpreterErrorType.NOTHING_TO_RUN, context.getCurrentLine()));
		}
		return nextContext;
	}
	
	private Entry<Integer,Statement> detectNextCS(int currentLine, int nextLine) {
		Entry<Integer,Statement> entry;
		this.stream = this.code.tailMap(nextLine).entrySet().iterator();
		if (this.stream.hasNext()) {
			entry = this.stream.next();
			if (!entry.getKey().equals(nextLine)) {
				ErrorRepository.add(
						new InterpreterWarning(InterpreterErrorType.LINE_NOT_FOUND,
						currentLine,
						Integer.toString(nextLine)));
			}
			return entry;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() throws InterpreterException {
		ExecutionContext context = new ExecutionContext(this, this.symbolTable);
		do {
			context = runStatement(context);
		}
		while (context!=null && context.getCurrentLine()>0);
		if (context!=null) {
			context.close();
		}
		this.symbolTable.resetValues();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reentrantRun(ExecutionContext executionContext, Statement statement)
			throws InterpreterException {
		if (statement!=null) {
			ExecutionContext origin = new ExecutionContext(executionContext);
			ExecutionContext context = origin;
			context = statement.run(context);
			if (context!=origin && context!=null && context.getCurrentLine()>0) {
				Entry<Integer,Statement> entry = detectNextCS(context.getCurrentLine(), context.getNextLine());
				context.setCurrentLine(entry==null ? -1 : context.getNextLine());
				context.setNextLine(-1);
				this.current = entry;
				do {
					context = runStatement(context);
				}
				while (context!=origin && context!=null && context.getCurrentLine()>0);
			}
			if (context!=null) {
				context.close();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DebugInterpreter debug() throws InterpreterException {
		ExecutionContext context = new ExecutionContext(this, this.symbolTable);
		if (this.current!=null) {
			context.setCurrentLine(this.current.getKey());
		}
		this.symbolTable.resetValues();
		return new DebugInterpreter(this,context);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SortedMap<Integer, Statement> getCode() {
		return Collections.unmodifiableSortedMap(this.code);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setStandardInput(StandardInput stdin) {
		if (stdin==null)
			this.stdin = new StdIn();
		else
			this.stdin = stdin;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StandardInput getStandardInput() {
		return this.stdin;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setStandardOutput(StandardOutput stdout) {
		if (stdout==null)
			this.stdout = new StdOut();
		else
			this.stdout = stdout;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StandardOutput getStandardOutput() {
		return this.stdout;
	}

	/**
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $Name$ $Revision$ $Date$
	 */
	private class StdIn implements StandardInput {

		/**
		 */
		public StdIn() {
			//
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Value readString(String message) {
			StandardOutput stdout = getStandardOutput();
			stdout.print(message);
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String line;
			try {
				line = reader.readLine();
			}
			catch (IOException ex) {
				return Value.UNDEF;
			}
			return Value.parseValue(line);
		}
		
	}

	/**
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $Name$ $Revision$ $Date$
	 */
	private static class StdOut implements StandardOutput {

		/**
		 */
		public StdOut() {
			//
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void println(String message) {
			System.out.println(message);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void print(String message) {
			System.out.print(message);
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void println() {
			System.out.println();
		}

	}

}