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

/**
 * Address in virtual machine.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class Address extends Number {

	private static final long serialVersionUID = -6020249455012390644L;
	
	private final long address;
	
	/**
	 * @param adr
	 */
	public Address(long adr) {
		this.address = adr;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int intValue() {
		return (int)this.address;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long longValue() {
		return this.address;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float floatValue() {
		return this.address;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double doubleValue() {
		return this.address;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Long.toString(this.address);
	}

}