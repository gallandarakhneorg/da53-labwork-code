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

import fr.utbm.info.da53.lw5.type.NumberUtil;
import fr.utbm.info.da53.lw5.util.Util;

/**
 * Region of memory that is containing a String, a Number or an Address.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
class MemoryUnit {

	private static final byte LONG = 0x00;
	private static final byte DOUBLE = 0x01;
	private static final byte ADDRESS = 0x02;
	private static final byte STRING = 0x04;
	
	private byte[] data = new byte[0];
	
	/**
	 * @param value
	 */
	public MemoryUnit(Object value) {
		setValue(value);
	}
	
	/** Replies the value stored in the region.
	 * 
	 * @return the value
	 */
	public synchronized Object getValue() {
		if (this.data.length>0) {
			switch(this.data[0]) {
			case LONG:
				return Util.getLong(this.data, 1, 0);
			case DOUBLE:
				return Util.getDouble(this.data, 1, 0);
			case ADDRESS:
				return new Address(Util.getLong(this.data, 1, 0));
			case STRING:
				return new String(this.data, 1, this.data.length-1);
			default:
				throw new RuntimeException("Unsupported type flag for memory storage: "+this.data[0]); //$NON-NLS-1$
			}
		}
		return null;
	}

	/** Set the value stored in the region.
	 * 
	 * @param value
	 */
	public synchronized void setValue(Object value) {
		if (value instanceof Address) {
			this.data = new byte[NumberUtil.SIZEOF_LONG+1];
			this.data[0] = ADDRESS;
			Util.putLong(this.data, 1, 0, ((Address)value).longValue());
		}
		else if (value instanceof Number) {
			Number n = (Number)value;
			if (NumberUtil.isInteger(n)) {
				this.data = new byte[NumberUtil.SIZEOF_LONG+1];
				this.data[0] = LONG;
				Util.putLong(this.data, 1, 0, n.longValue());
			}
			else {
				this.data = new byte[NumberUtil.SIZEOF_DOUBLE+1];
				this.data[0] = DOUBLE;
				Util.putDouble(this.data, 1, 0, n.doubleValue());
			}
		}
		else if (value instanceof CharSequence) {
			CharSequence cs = (CharSequence)value;
			byte[] b = cs.toString().getBytes();
			this.data = new byte[b.length+1];
			this.data[0] = STRING;
			System.arraycopy(b, 0, this.data, 1, b.length);
		}
		else if (value instanceof Boolean) {
			this.data = new byte[NumberUtil.SIZEOF_LONG+1];
			this.data[0] = LONG;
			Util.putLong(this.data, 1, 0, ((Boolean)value).booleanValue() ? 1 : 0);
		}
		else if (value==null) {
			throw new RuntimeException("Unsupported type flag for memory storage: "+value); //$NON-NLS-1$
		}
		else {
			throw new RuntimeException("Unsupported type flag for memory storage: "+value.getClass()); //$NON-NLS-1$
		}
	}
	
	/** Replies the size in bytes of the segment.
	 * 
	 * @return the size in bytes of the segment; or {@code -1} if
	 * not allocated.
	 */
	public synchronized int size() {
		return this.data.length-1;
	}
	
	/** Type of the data inside the unit.
	 * 
	 * @return the type of the data inside the unit; or <code>null</code>
	 * if the unit was not initialized.
	 */
	public synchronized Class<?> type() {
		if (this.data.length>0) {
			switch(this.data[0]) {
			case ADDRESS:
				return Address.class;
			case DOUBLE:
				return Double.class;
			case LONG:
				return Long.class;
			case STRING:
				return String.class;
			}
		}
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		Object o = getValue();
		if (o==null) return null;
		return o.toString();
	}
	
}