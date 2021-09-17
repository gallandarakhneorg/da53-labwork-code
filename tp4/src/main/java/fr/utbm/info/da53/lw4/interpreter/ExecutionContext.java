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
package fr.utbm.info.da53.lw4.interpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import fr.utbm.info.da53.lw4.threeaddresscode.ThreeAddressCode;
import fr.utbm.info.da53.lw4.threeaddresscode.ThreeAddressRecord;
import fr.utbm.info.da53.lw4.util.Util;

/**
 * Execution context for the nterpreter of three-address code.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
class ExecutionContext {

	private final Map<String,String> values = new TreeMap<String,String>();
	private final Stack<Context> functionCalls = new Stack<Context>();
	private final List<String> params = new ArrayList<String>();
	private int ordinalCounter = 0;
		
	/**
	 */
	public ExecutionContext() {
		//
	}
	
	/** Replies the next record to run from the given code.
	 * 
	 * @param code
	 * @return the record to run or <code>null</code>.
	 */
	public ThreeAddressRecord getRecord(ThreeAddressCode code) {
		return code.getRecord(this.ordinalCounter);
	}
	
	/** Move the ordinal counter from one address.
	 */
	public void moveOrdinalCounter() {
		this.ordinalCounter++;
	}
	
	/** Move the ordinal counter from one address.
	 * 
	 * @param address
	 */
	public void moveOrdinalCounter(int address) {
		this.ordinalCounter = address;
	}
	
	/** Add a parameter to a procedure or a function.
	 * 
	 * @param param
	 */
	public void addParameter(String param) {
		this.params.add(param);
	}
	
	/** Replies the instruction number that is corresponding
	 * to the given label.
	 * @param label
	 * @param code
	 * @return the instruction number.
	 */
	public int getInstructionPosition(String label, ThreeAddressCode code) {
		String l = label;
		if (Util.isString(label)) {
			l = Util.unstringify(label);
		}
		else if (Util.isNumber(label) || Util.isBoolean(label)) {
			l = label;
		}
		else {
			String val = this.values.get(label);
			if (val!=null) l = val;
		}
		if (l!=null) {
			if (Util.isString(l)) {
				l = Util.unstringify(l);
			}
			int i=0; 
			for(ThreeAddressRecord record : code.getRecords()) {
				if (l.equals(record.getLabel())) {
					return i;
				}
				++i;
			}
		}
		return -1;
	}

	/** Replies the value of the given variable.
	 * 
	 * @param variable is the name of the variable.
	 * @return the value
	 */
	public String getValue(String variable) {
		if (variable==null || Util.isString(variable)
			|| Util.isNumber(variable) || Util.isBoolean(variable))
			return variable;
		String val = this.values.get(variable);
		if (val!=null) return val;
		throw new RuntimeException("undeclared variable: "+variable); //$NON-NLS-1$
	}

	/** Set the value of the given variable.
	 * 
	 * @param id is the name of the variable
	 * @param value is the value of the variable.
	 */
	public void setValue(String id, String value) {
		this.values.put(id, value);
	}
	
	/** Set the i-th element of the variable as it is an array.
	 * 
	 * @param array is the identifier of the array.
	 * @param i is the index.
	 * @param value is the value to put inside.
	 */
	public void setArrayElement(String array, String i, String value) {
		String ii = getValue(i);
		if (!Util.isInteger(ii))
			throw new RuntimeException("Integer is expected for the array index"); //$NON-NLS-1$
		int index = Integer.parseInt(ii);
		if (index<0)
			throw new RuntimeException("Index out of bounds: "+index); //$NON-NLS-1$
		
		String v = this.values.get(array);
		if (v==null || !Util.isArray(v)) {
			String[] tab = new String[index+1];
			tab[index] = getValue(value);
			this.values.put(array, Util.toString(tab));
		}
		else {
			String[] tab = Util.parseArray(v);
			if (index>=tab.length) {
				String[] tab2 = new String[index+1];
				System.arraycopy(tab, 0, tab2, 0, tab.length);
				tab = tab2;
			}
			tab[index] = getValue(value);
			this.values.put(array, Util.toString(tab));
		}
	}

	/** Replies the i-th element of the variable as it is an array.
	 * 
	 * @param array is the identifier of the array.
	 * @param i is the index.
	 * @return the value from the array.
	 */
	public String getArrayElement(String array, String i) {
		String ii = getValue(i);
		if (!Util.isInteger(ii))
			throw new RuntimeException("Integer is expected for the array index"); //$NON-NLS-1$
		int index = Integer.parseInt(ii);
		if (index<0)
			throw new RuntimeException("Index out of bounds: "+index); //$NON-NLS-1$
		
		String v = this.values.get(array);
		if (v==null || !Util.isArray(v)) {
			throw new RuntimeException(array+" is not an array"); //$NON-NLS-1$
		}

		String[] tab = Util.parseArray(v);
		if (index>=tab.length) {
			throw new RuntimeException("Index out of bounds: "+index); //$NON-NLS-1$
		}
		return tab[index];
	}

	/** Open a function context.
	 * 
	 * @param position is the address of the instruction to run.
	 * @param nbParams is the numbers of parameters to pass to the function.
	 * @param result is the name of the variable in which the result should be saved.
	 */
	public void callFunction(int position, int nbParams, String result) {
		if (this.params.size()<nbParams) {
			throw new RuntimeException("Not enough parameters to a function call"); //$NON-NLS-1$
		}
		List<String> formalParams = new ArrayList<String>();
		for(int i=0; i<nbParams; ++i) {
			formalParams.add(this.params.remove(0));
		}
		Context context = new Context(formalParams, result, this.ordinalCounter+1);
		this.functionCalls.push(context);
		this.ordinalCounter = position;
	}
	
	/** Replies the n-th formal parameter.
	 * 
	 * @param n
	 * @return the n-th formal parameter.
	 */
	public String getFormalParameter(int n) {
		if (this.functionCalls.isEmpty()) {
			throw new RuntimeException("Cannot invoke formal parameter outside a function call"); //$NON-NLS-1$
		}
		Context c = this.functionCalls.peek();
		if (c==null) {
			throw new RuntimeException("Cannot invoke formal parameter outside a function call"); //$NON-NLS-1$
		}
		return c.getFormalParameter(n);
	}
	
	/** Close a function context.
	 * 
	 * @param value is the value to return, or <code>null</code>.
	 */
	public void returnFunction(String value) {
		if (this.functionCalls.isEmpty()) {
			throw new RuntimeException("Cannot invoke formal parameter outside a function call"); //$NON-NLS-1$
		}
		Context c = this.functionCalls.pop();
		if (c==null) {
			throw new RuntimeException("Cannot invoke formal parameter outside a function call"); //$NON-NLS-1$
		}

		String v = getValue(value);

		String var = c.getReturnVariable();
		if (var!=null) {
			if (v==null) {
				throw new RuntimeException("Excepting a return value"); //$NON-NLS-1$
			}
			setValue(var, v);
		}
		
		this.ordinalCounter = c.getReturnAddress();
	}

	/**
	 * Execution context for the nterpreter of three-address code.
	 * 
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $Name$ $Revision$ $Date$
	 */
	private class Context {

		private final List<String> formalParams;
		private final String returnVariable;
		private final int returnAddress;
		
		/**
		 * @param formalParams
		 * @param returnVariable
		 * @param returnAddress
		 */
		public Context(List<String> formalParams, String returnVariable, int returnAddress) {
			this.formalParams = formalParams;
			this.returnVariable = returnVariable;
			this.returnAddress = returnAddress;
		}
		
		/** Replies the n-th formal parameter.
		 * 
		 * @param n
		 * @return the n-th formal parameter.
		 */
		public String getFormalParameter(int n) {
			if (n<0 || n>= this.formalParams.size())
				throw new RuntimeException("not enough parameter to the function"); //$NON-NLS-1$
			return this.formalParams.get(n);
		}
		
		/** Replies the name of the variable that should contains
		 * the returned value.
		 * @return the name of the variable that should contains
		 * the returned value.
		 */
		public String getReturnVariable() {
			return this.returnVariable;
		}
		
		/** Replies the address where to return.
		 * 
		 * @return the address.
		 */
		public int getReturnAddress() {
			return this.returnAddress;
		}
		
	}
	
}