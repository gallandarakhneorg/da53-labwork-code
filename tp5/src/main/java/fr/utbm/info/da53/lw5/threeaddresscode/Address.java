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
package fr.utbm.info.da53.lw5.threeaddresscode;

import java.io.Serializable;

/**
 * Address in a three-address code.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class Address implements Cloneable, Serializable {

	private static final long serialVersionUID = -6897565406398320325L;
	
	private String name;
	private long offset;
	private Number value;
	private AddressBase base;
	private boolean isLabel;
	
	/** Force this address to be an program address.
	 * 
	 * @param adr
	 */
	void set(long adr) {
		this.offset = adr;
		this.base = AddressBase.PROGRAM_START;
		this.value = null;
		this.isLabel = false;
	}
	
	/**
	 * @param name is the textual representation of the address.
	 * @param offset is the offset to add to the base to obtain the address value.
	 * @param base is the base of the computation of the address.
	 */
	public Address(String name, long offset, AddressBase base) {
		this.name = name;
		this.offset = offset;
		this.base = base;
		this.value = null;
		this.isLabel = false;
	}
	
	/**
	 * @param label is the label to backpatch.
	 */
	public Address(String label) {
		this.name = label;
		this.offset = 0;
		this.base = AddressBase.PROGRAM_START;
		this.value = null;
		this.isLabel = true;
	}

	/**
	 * @param cons is the constant value to put in the address.
	 */
	public Address(long cons) {
		this.name = Long.toString(cons);
		this.offset = 0;
		this.base = null;
		this.value = cons;
		this.isLabel = false;
	}

	/**
	 * @param cons is the constant value to put in the address.
	 */
	public Address(double cons) {
		this.name = Double.toString(cons);
		this.offset = 0;
		this.base = null;
		this.value = cons;
		this.isLabel = false;
	}

	/**
	 * @param cons is the constant value to put in the address.
	 */
	public Address(Number cons) {
		this.name = cons.toString();
		this.offset = 0;
		this.base = null;
		this.value = cons;
		this.isLabel = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Address clone() {
		try {
			return (Address)super.clone();
		}
		catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		if (isConstant() || isLabel())
			return this.name;
		assert(isAddress());
		return this.name+"["+this.base.toString()+"+"+this.offset+"]";  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
	}
	
	/** Replies if the address contains a label to backpatch, not an address.
	 * 
	 * @return <code>true</code> if label, otherwise <code>false</code>.
	 */
	public boolean isLabel() {
		return this.isLabel;
	}

	/** Replies if the address contains a constant, not an address.
	 * 
	 * @return <code>true</code> if constant, otherwise <code>false</code>.
	 */
	public boolean isConstant() {
		return !this.isLabel && this.value!=null;
	}
	
	/** Replies if the address contains an address, not a constant.
	 * 
	 * @return <code>true</code> if address, otherwise <code>false</code>.
	 */
	public boolean isAddress() {
		return !this.isLabel && this.value==null;
	}

	/**
	 * Replies the offset.
	 * @return the offset.
	 */
	public long offset() {
		return this.offset;
	}
	
	/**
	 * Replies the value.
	 * @return the value.
	 */
	public Number value() {
		return this.value;
	}

	/**
	 * Replies the base.
	 * @return the base.
	 */
	public AddressBase base() {
		return this.base;
	}
	
}
