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
package fr.utbm.info.da53.lw2.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Value in TinyBasic.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class Value implements Serializable, Cloneable, Comparable<Value> {
	
	private static final long serialVersionUID = -5561717466435045800L;
	
	/** Compare the two list are equal.
	 * 
	 * @param a
	 * @param b
	 * @return see {@link Comparable}
	 */
	public static int compare(List<Value> a, List<Value> b) {
		assert(a!=null && b!=null);
		int c = a.size() - b.size();
		if (c!=0) return c;
		Value v1, v2;
		for(int i=0; i<a.size(); ++i) {
			v1 = a.get(i);
			v2 = b.get(i);
			c = v1.compareTo(v2);
			if (c!=0) return c;
		}
		return 0;
	}

	
	/** Undefined value.
	 */
	public static final Value UNDEF = new Undef();
	
	/** Parse the given string and replies the best value
	 * that is corresponding to the given string.
	 * 
	 * @param v
	 * @return the value, never <code>null</code>.
	 */
	public static Value parseValue(String v) {
		if (v==null) return null;
		String sv = v.trim();
		if ("true".equalsIgnoreCase(sv)) //$NON-NLS-1$
			return new Value(Boolean.TRUE);
		if ("false".equalsIgnoreCase(sv)) //$NON-NLS-1$
			return new Value(Boolean.FALSE);
		if (sv.startsWith("[") && sv.endsWith("]")) { //$NON-NLS-1$ //$NON-NLS-2$
			sv = sv.substring(1, sv.length()-1);
			String[] elements = sv.split("[ \t\n\r\f]*,[ \t\n\r\f]*"); //$NON-NLS-1$
			List<Value> tab = new ArrayList<Value>();
			for(String e : elements) {
				tab.add(parseValue(e));
			}
			return new Value(tab);
		}
		try {
			return new Value(NumberUtil.parse(sv, -1));
		}
		catch(Throwable ex) {
			return new Value(sv);
		}
	}

	private VariableType type = null;
	private Object value = null;
	
	/**
	 */
	public Value() {
		//
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Value clone() {
		try {
			Value clone = (Value)super.clone();
			if (clone.type==VariableType.ARRAY && this.value!=null) {
				clone.value = new ArrayList<Value>((List<Value>)this.value);
			}
			return clone;
		}
		catch(CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	/**
	 * @param v
	 */
	public Value(String v) {
		this.value = v;
		this.type = VariableType.STRING;
	}

	/**
	 * @param v
	 */
	public Value(Boolean v) {
		this.value = v;
		this.type = VariableType.BOOLEAN;
	}

	/**
	 * @param v
	 */
	public Value(Number v) {
		this.value = v;
		this.type = VariableType.NUMBER;
	}
	
	/**
	 * @param v
	 */
	public Value(List<Value> v) {
		this.value = v;
		this.type = VariableType.ARRAY;
	}

	/** Replies the type of the value.
	 * 
	 * @return the type, or <code>null</code> if unknown.
	 */
	public VariableType getType() {
		return this.type;
	}
	
	/** Replies if the value is set.
	 * 
	 * @return <code>true</code> if the value is set; otherwise <code>false</code>.
	 * @see #isUnset()
	 */
	public boolean isSet() {
		return this.value!=null;
	}
	
	/** Replies if the value is unset.
	 * 
	 * @return <code>true</code> if the value is unset; otherwise <code>false</code>.
	 * @see #isSet()
	 */
	public boolean isUnset() {
		return this.value==null;
	}

	/** Replies the value.
	 * @return the value, or <code>null</code> if unset.
	 */
	public Object getValue() {
		return this.value;
	}

	/** Replies the value.
	 * @param type
	 * @return the value, or <code>null</code> if unset.
	 */
	public <T> T getValue(Class<T> type) {
		return this.value==null ? null : type.cast(this.value);
	}

	/** Replies the array of value.
	 * @return the value, or <code>null</code> if unset.
	 */
	@SuppressWarnings("unchecked")
	public <T> List<Value> getValueArray() {
		return (List<Value>)this.value;
	}

	/** Set the value.
	 * @param v
	 */
	public void set(Value v) {
		if (v==null) {
			this.value = null;
			this.type = null;
		}
		else {
			this.value = v.value;
			this.type = v.type;
		}
	}

	/**
	 * Set the value.
	 * @param v
	 */
	public void set(String v) {
		this.value = v;
		this.type = VariableType.STRING;
	}

	/**
	 * Set the value.
	 * @param v
	 */
	public void set(Boolean v) {
		this.value = v;
		this.type = VariableType.BOOLEAN;
	}

	/**
	 * Set the value.
	 * @param v
	 */
	public void set(List<Value> v) {
		this.value = v;
		this.type = VariableType.ARRAY;
	}

	/** Set the value.
	 * @param v
	 */
	public void set(Number v) {
		this.value = v;
		this.type = VariableType.NUMBER;
	}

	/** Unset the value.
	 */
	public void unset() {
		this.value = null;
		this.type = null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		if (this.value==null) {
			return "undef"; //$NON-NLS-1$
		}
		return this.value.toString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (obj==this.value) return true;
		if (obj instanceof Value) {
			Value v = (Value)obj;
			return this.type==v.type && 
					(this.value==v.value ||
						(this.value!=null && this.value.equals(v.value)));
		}
		else if (this.type==VariableType.NUMBER && obj instanceof Number) {
			return (this.value!=null && this.value.equals(obj));
		}
		else if (this.type==VariableType.BOOLEAN && obj instanceof Boolean) {
			return (this.value!=null && this.value.equals(obj));
		}
		else if (this.type==VariableType.STRING && obj!=null) {
			return (this.value!=null && this.value.equals(obj));
		}
		else if (this.type==VariableType.ARRAY && obj instanceof List) {
			return (this.value!=null && compare((List<Value>)this.value, (List<Value>)obj)==0);
		}
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int h = 1;
		h = h * 37 + ((this.value!=null) ? this.value.hashCode() : 0);
		h = h * 37 + ((this.type!=null) ? this.type.hashCode() : 0);
		return h;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(Value o) {
		if (o==null) return Integer.MAX_VALUE;
		if (getType()==VariableType.STRING || o.getType()==VariableType.STRING) {
			String lv = getValue().toString();
			String rv = o.getValue().toString();
			return lv.compareTo(rv);
		}

		if ((getType()==null||isUnset()) && (o.getType()==null||o.isUnset())) return 0;
		if (getType()==null||isUnset()) return Integer.MIN_VALUE;
		if (o.getType()==null||isUnset()) return Integer.MAX_VALUE;
		
		assert(getType()!=null && o.getType()!=null);
		assert(isSet() && o.isSet());
		
		VariableType t1 = getType();
		VariableType t2 = o.getType();

		int c = t1.compareTo(t2);
		if (c!=0) return c;
		
		assert(t1==t2);
		
		switch(t1) {
		case BOOLEAN:
			c = getValue(Boolean.class).compareTo(o.getValue(Boolean.class));
			break;
		case NUMBER:
			c = NumberUtil.compare(getValue(Number.class), getValue(Number.class));
			break;
		case ARRAY:
			c = compare(getValueArray(), o.getValueArray());
			break;
		case STRING:
			throw new IllegalStateException();
		}
		
		return c;
	}

	/**
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $Name$ $Revision$ $Date$
	 */
	private static class Undef extends Value {

		private static final long serialVersionUID = -8361517677895707274L;

		/**
		 */
		public Undef() {
			//
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void set(Boolean v) {
			throw new UnsupportedOperationException();
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void set(List<Value> v) {
			throw new UnsupportedOperationException();
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void set(Number v) {
			throw new UnsupportedOperationException();
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void set(String v) {
			throw new UnsupportedOperationException();
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void set(Value v) {
			throw new UnsupportedOperationException();
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void unset() {
			throw new UnsupportedOperationException();
		}
		
	}
	
}
