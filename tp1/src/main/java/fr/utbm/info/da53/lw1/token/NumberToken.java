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
package fr.utbm.info.da53.lw1.token;

/**
 * This is the token for all the numbers.
 * 
 * @author Jonathan DEMANGE &lt;jonathan.demange@utbm.fr&gt;
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class NumberToken extends Token {
	
	/** Factory method for this token.
	 *
	 * @param lexeme the lexeme.
	 * @param tokenType the token type, usually this type.
	 */
	public static NumberToken create(String lexeme, Class<? extends Token> tokenType) {
		return new NumberToken(lexeme);
	}

	/**
	 * This is the token for all the numbers.
	 * 
	 * @author Jonathan DEMANGE &lt;jonathan.demange@utbm.fr&gt;
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $Name$ $Revision$ $Date$
	 */
	public static enum NumberType {
		/** Integer.
		 */
		INTEGER_NUMBER,
		/** Floating-point.
		 */
		FLOATING_POINT_NUMBER;
	}

	private Number value;
	private NumberType type;
	
	/**
	 * @param lexeme
	 */
	public NumberToken(String lexeme) {
		super(lexeme);
		Number n;
		try {
			n = Long.valueOf(lexeme);
			this.type = NumberType.INTEGER_NUMBER;
		}
		catch(Throwable ex) {
			n = Double.valueOf(lexeme);
			this.type = NumberType.FLOATING_POINT_NUMBER;
		}
		this.value = n;
	}
	
	/** Replies the type of the number.
	 * 
	 * @return the type of the number.
	 */
	public NumberType getType() {
		return this.type;
	}
	
	/** Set the type of the number.
	 * Caution: when casting to integer, the fractional
	 * part of the number is lost.
	 * 
	 * @param type is the type of the number.
	 */
	public void setType(NumberType type) {
		if (type!=null && type!=this.type) {
			switch(type) {
			case FLOATING_POINT_NUMBER:
				this.value = this.value.doubleValue();
				break;
			case INTEGER_NUMBER:
				this.value = this.value.longValue();
				break;
			}
			this.type = type;
		}
	}

	/**
	 * Replies the attribute associated to this token as an integer value.
	 * 
	 * @return the attribute of this token.
	 */
	public int intValue() {
		return this.value.intValue();
	}
	
	/**
	 * Replies the attribute associated to this token as a long integer value.
	 * 
	 * @return the attribute of this token.
	 */
	public long longValue() {
		return this.value.longValue();
	}

	/**
	 * Replies the attribute associated to this token as a byte value.
	 * 
	 * @return the attribute of this token.
	 */
	public byte byteValue() {
		return this.value.byteValue();
	}

	/**
	 * Replies the attribute associated to this token as a short integer value.
	 * 
	 * @return the attribute of this token.
	 */
	public short shortValue() {
		return this.value.shortValue();
	}

	/**
	 * Replies the attribute associated to this token as a single-precision floating-point value.
	 * 
	 * @return the attribute of this token.
	 */
	public float floatValue() {
		return this.value.floatValue();
	}

	/**
	 * Replies the attribute associated to this token as a double-precision floating-point value.
	 * 
	 * @return the attribute of this token.
	 */
	public double doubleValue() {
		return this.value.doubleValue();
	}

	/** {@InheritDoc}
	 */
	@Override
	public String toString() {
		return "<NUM," //$NON-NLS-1$
				+this.value
				+">"; //$NON-NLS-1$
	}
	
}

