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
package fr.utbm.info.da53.lw5.vm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Context in the stack.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class StackContext {

	private final StackContext parent;
	
	private final boolean isAllGlobal;
	
	private int ordinalCounter = 0;
			
	private final List<Object> callParameters = new ArrayList<Object>();
	
	private Object[] formalParameters = new Object[0];
	
	private Address returnAddress = null;
	
	private final Map<Integer,Number> localVariables = new TreeMap<Integer,Number>();
	
	/**
	 * Create root stack element.
	 * 
	 * @param isAllGlobal indicates if all the variables should be global or not.
	 */
	public StackContext(boolean isAllGlobal) {
		this.parent = null;
		this.isAllGlobal = isAllGlobal;
	}

	/**
	 * Create child stack element.
	 * 
	 * @param parent
	 */
	public StackContext(StackContext parent) {
		this.parent = parent;
		this.isAllGlobal = parent.isAllGlobal;
	}
	
	/** Replies the parent context.
	 * 
	 * @return the parent context.
	 */
	public StackContext getParent() {
		return this.parent;
	}
	
	/** Replies the ordinal counter.
	 * 
	 * @return the ordinal counter.
	 */
	public int getOrdinalCounter() {
		return this.ordinalCounter;
	}

	/** Set the ordinal counter.
	 * 
	 * @param oc is the ordinal counter.
	 */
	public void setOrdinalCounter(int oc) {
		this.ordinalCounter = oc;
	}
	
	/** Add a parameter to pass to a function.
	 * 
	 * @param v
	 */
	public void addCallParameter(Object v) {
		this.callParameters.add(v);
	}

	/** Consume parameters to pass to a function.
	 * 
	 * @param n
	 * @return the parameters
	 */
	public Object[] consumeParameters(int n) {
		Object[] params = new Object[n];
		if (this.callParameters.size()<n)
			throw new RuntimeException("no enough parameters"); //$NON-NLS-1$
		for(int i=0; i<n; ++i) {
			params[i] = this.callParameters.remove(0);
		}
		return params;
	}
	
	/**
	 * Set the formal parameters of the stack.
	 * 
	 * @param params
	 */
	public void setFormalParameters(Object[] params) {
		this.formalParameters = params;
	}
	
	/**
	 * Replies the formal parameter at the given position.
	 * 
	 * @param index
	 * @return the formal parameter value.
	 */
	public Object getFormalParameter(int index) {
		return this.formalParameters[index];
	}
	
	/** Set the address where to put the returned value.
	 * 
	 * @param adr
	 */
	public void setReturnAddress(Address adr) {
		this.returnAddress = adr;
	}

	/** Replies the address where to put the returned value.
	 * 
	 * @return the address or <code>null</code> if there is no need to return something.
	 */
	public Address getReturnAddress() {
		return this.returnAddress;
	}

	/** Replies the value at the given index.
	 * 
	 * @param adr
	 * @return the value
	 */
	public Number getValueAt(int adr) {
		if (adr<0) {
			throw new RuntimeException("Illegal memory access. Invalid address: "+adr); //$NON-NLS-1$
		}
		StackContext cursor = this;
		do {
			if (cursor.localVariables.containsKey(adr)) {
				Number value = cursor.localVariables.get(adr);
				if (value!=null) return value;
			}
			cursor = cursor.getParent();
		}
		while (cursor!=null);
		return new Address(0l);
	}
	
	/** Set the value at the given position in the stack.
	 * 
	 * @param adr
	 * @param value
	 */
	public void setValueAt(int adr, Number value) {
		if (value==null) {
			throw new RuntimeException("Cannot set null value"); //$NON-NLS-1$
		}
		if (adr<0) {
			throw new RuntimeException("Illegal memory access. Invalid address: "+adr); //$NON-NLS-1$
		}
		StackContext cursor = this;
		do {
			if (cursor.parent==null || !cursor.isAllGlobal) {
				cursor.localVariables.put(adr, value);
				return;
			}
			cursor = cursor.parent;
		}
		while (cursor!=null);
		throw new RuntimeException("Illegal memory access. Invalid address: "+adr); //$NON-NLS-1$
	}

}