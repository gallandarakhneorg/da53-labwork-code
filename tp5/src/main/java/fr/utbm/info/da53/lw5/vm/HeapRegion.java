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

import java.util.Arrays;

/**
 * Allocated region in the heap.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
class HeapRegion {

	private final long address;
	
	private MemoryUnit[] data = new MemoryUnit[0];
	
	/**
	 * @param adr
	 */
	HeapRegion(long adr) {
		this.address = adr;
	}
	
	/** Replies the address of the heap region.
	 * 
	 * @return the address of the heap region.
	 */
	public long address() {
		return this.address;
	}
	
	/** Set the value at the given position from the start of the region.
	 * 
	 * @param index
	 * @param value
	 */
	public synchronized void setValueAt(int index, Object value) {
		if (index<0) throw new RuntimeException("Illegal memory access"); //$NON-NLS-1$
		if (index>=this.data.length) {
			// Make the array bigger
			MemoryUnit[] dt = new MemoryUnit[index+1];
			System.arraycopy(this.data, 0, dt, 0, this.data.length);
			this.data = dt;
		}
		if (this.data[index]==null)
			this.data[index] = new MemoryUnit(value);
		else
			this.data[index].setValue(value);
	}

	/** Replies the value at the given position from the start of the region.
	 * 
	 * @param index
	 * @return the value
	 */
	public synchronized Object getValueAt(int index) {
		if (index<0 || index>=this.data.length)
			throw new RuntimeException("Illegal memory access"); //$NON-NLS-1$
		MemoryUnit unit = this.data[index];
		if (unit==null) return 0l;
		return unit.getValue();
	}

	/** Replies the value that is corresponding to the entire region.
	 * 
	 * @return the value
	 */
	public synchronized Object getValue() {
		if (this.data.length==0) return 0l;
		if (this.data.length==1) return this.data[0].getValue();
		return Arrays.toString(this.data);
	}

	/** Type of the data inside the unit at the given position.
	 * 
	 * @param index
	 * @return the type of the data inside the unit; or <code>null</code>
	 * if the unit was not initialized.
	 */
	public synchronized Class<?> typeAt(int index) {
		if (this.data.length>0) {
			MemoryUnit u = this.data[index];
			if (u!=null) {
				return u.type();
			}
		}
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Arrays.toString(this.data);
	}

}