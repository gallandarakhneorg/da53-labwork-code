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

import java.lang.ref.WeakReference;
import java.util.Set;
import java.util.TreeSet;

import fr.utbm.info.da53.lw2.symbol.SymbolTable;
import fr.utbm.info.da53.lw2.symbol.SymbolTableEntry;
import fr.utbm.info.da53.lw2.type.Value;

/**
 * Context of execution.
 * The execution context is used to return from a subfunction.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class ExecutionContext {
		
	private final WeakReference<Interpreter> interpreter;
	private SymbolTable topTable;
	private ExecutionContext parent;
	private int currentLine = -1;
	private int nextLine = -1;
	
	/** Create a root execution context.
	 * 
	 * @param interpreter
	 * @param table
	 */
	public ExecutionContext(Interpreter interpreter, SymbolTable table) {
		this(interpreter, null, table);
	}

	/** Create an execution context.
	 * @param parent
	 */
	public ExecutionContext(ExecutionContext parent) {
		this(null, parent, null);
	}
	
	/** Create an execution context.
	 * @param parent
	 * @param table
	 */
	private ExecutionContext(Interpreter interpreter, ExecutionContext parent, SymbolTable table) {
		this.parent = parent;
		if (this.parent==null) {
			if (interpreter==null) throw new IllegalStateException();
			this.interpreter = new WeakReference<Interpreter>(interpreter);
			if (table==null)
				this.topTable = new SymbolTable();
			else
				this.topTable = table;
		}
		else {
			this.interpreter = null;
			this.topTable = null;
			this.nextLine = parent.nextLine;
			this.currentLine = parent.currentLine;
		}
	}
	
	/** Replies the interpreter.
	 * 
	 * @return the interpreter.
	 */
	public Interpreter getInterpreter() {
		ExecutionContext context = this;
		Interpreter i;
		do {
			i = context.interpreter==null ? null : context.interpreter.get();
			if (i!=null) return i;
			context = context.parent;
		}
		while (context!=null);
		throw new IllegalStateException();
	}

	/** Replies the parent context.
	 * 
	 * @return the parent context.
	 */
	public ExecutionContext getParent() {
		return this.parent;
	}
	
	/** Close the execution context.
	 */
	public void close() {
		if (this.topTable!=null)
			this.topTable.clear();
		this.topTable = null;
		this.parent = null;
	}
	
	/** Set the current executed line.
	 * 
	 * @param lineNumber
	 */
	public void setCurrentLine(int lineNumber) {
		this.currentLine = lineNumber;
	}
	
	/** Replies the current line.
	 * 
	 * @return the current line.
	 */
	public int getCurrentLine() {
		return this.currentLine;
	}
	
	/** Set the next line to run.
	 * 
	 * @param lineNumber
	 */
	public void setNextLine(int lineNumber) {
		this.nextLine = lineNumber;
	}
	
	/** Replies the next line to run.
	 * 
	 * @return the next line to run.
	 */
	public int getNextLine() {
		return this.nextLine;
	}

	/** Replies a snapshot of the symbol table.
	 * 
	 * @return the variable entry
	 */
	public Set<SymbolTableEntry> snapshot() {
		Set<SymbolTableEntry> snap = new TreeSet<SymbolTableEntry>();
		ExecutionContext context = this;
		while (context!=null) {
			if (context.topTable!=null) {
				for(SymbolTableEntry entry : context.topTable) {
					// Assume that add() does not overwrite an already present value in the set.
					snap.add(entry);
				}
			}
			context = context.getParent();
		}
		return snap;
	}

	/** Replies the value of a variable.
	 * 
	 * @param variableName
	 * @return the variable entry
	 */
	public SymbolTableEntry getSymbolTableEntry(String variableName) {
		ExecutionContext context = this;
		while (context!=null) {
			if (context.topTable!=null) {
				SymbolTableEntry e = context.topTable.get(variableName);
				if (e!=null) return e;
			}
			context = context.getParent();
		}
		return null;
	}

	/** Declare a variable.
	 * 
	 * @param variableName
	 * @param value
	 * @return the variable entry, or <code>null</code> if the variable was not declared.
	 */
	public SymbolTableEntry setVariable(String variableName, Value value) {
		ExecutionContext context = this;
		while (context!=null) {
			if (context.topTable!=null) {
				SymbolTableEntry e = context.topTable.get(variableName);
				if (e!=null) {
					e.setValue(value);
					return e;
				}
			}
			context = context.getParent();
		}
		return null;
	}
	
}
