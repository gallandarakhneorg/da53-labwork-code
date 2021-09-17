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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import fr.utbm.info.da53.lw2.error.CompilationErrorType;
import fr.utbm.info.da53.lw2.error.CompilerException;
import fr.utbm.info.da53.lw2.error.InterpreterErrorType;
import fr.utbm.info.da53.lw2.error.InterpreterException;

/**
 * Utilities for numbers.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class NumberUtil {
	
	private static Number parseInteger(String number, int line) throws CompilerException {
		Number n = null;
		try {
			n = Byte.parseByte(number);
		}
		catch(Throwable ex) {
			//
		}
		if (n==null) {
			try {
				n = Short.parseShort(number);
			}
			catch(Throwable ex) {
				//
			}
		}
		if (n==null) {
			try {
				n = Integer.parseInt(number);
			}
			catch(Throwable ex) {
				//
			}
		}
		if (n==null) {
			try {
				n = Long.parseLong(number);
			}
			catch(Throwable ex) {
				throw new CompilerException(
						CompilationErrorType.NUMBER_LITERAL_REQUIRED,
						line,
						number);
			}
		}
		assert(n!=null);
		return n;
	}
	
	/**
	 * Transforms the given string into a number.
	 *  
	 * @param number
	 * @param line
	 * @return the number
	 * @throws CompilerException
	 */
	public static Number parse(String number, int line) throws CompilerException {
		Number n = null;
		try {
			n = Float.parseFloat(number);
			if ((n.floatValue() - n.longValue())==0l) {
				return parseInteger(Long.toString(n.longValue()), line);
			}
		}
		catch(Throwable ex) {
			//
		}
		if (n==null) {
			try {
				n = Double.parseDouble(number);
				if ((n.doubleValue() - n.longValue())==0l) {
					return parseInteger(Long.toString(n.longValue()), line);
				}
			}
			catch(Throwable e) {
				//
			}
		}
		
		if (n!=null) return n;
		return parseInteger(number, line);
	}
	
	/**
	 * Transforms the given string into an integer.
	 *  
	 * @param number
	 * @return the integer or <code>null</code> if not an integer.
	 */
	public static Integer parseInt(String number) {
		try {
			return Integer.parseInt(number);
		}
		catch(Throwable ex) {
			return null;
		}
	}
	
	/** Convert the given double value into the smallest Number class.
	 * 
	 * @param v
	 * @return the number.
	 */
	public static Number toNumber(double v) {
		if (Double.isNaN(v)) return Float.NaN;
		if (Double.isInfinite(v))
			return Float.valueOf(((v>=0.) ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY));
		try {
			return parse(Double.toString(v), -1);
		}
		catch (CompilerException e) {
			return Double.valueOf(v);
		}
	}
	
	/** Replies if the given number represents an integer.
	 * 
	 * @param n
	 * @return <code>true</code> if an integer, otherwise <code>false</code>.
	 */
	public static boolean isInteger(Number n) {
		double d = n.doubleValue();
		return Math.floor(d) == d;
	}
		
	/** Replies if the given number is stricly positive.
	 * 
	 * @param n
	 * @return <code>true</code> if positive, otherwise <code>false</code>.
	 */
	public static boolean isPositive(Number n) {
		return n.doubleValue()>0.;
	}

	/** Replies if the given number is stricly negative.
	 * 
	 * @param n
	 * @return <code>true</code> if negative, otherwise <code>false</code>.
	 */
	public static boolean isNegative(Number n) {
		return n.doubleValue()<0.;
	}

	/** Replies the number with the inverted sign.
	 * 
	 * @param n
	 * @return the number with inverted sign.
	 */
	public static Number negate(Number n) {
		if (isByteObject(n)) return Byte.valueOf((byte)-n.byteValue());
		if (isShortIntegerObject(n)) return Short.valueOf((short)-n.shortValue());
		if (isIntegerObject(n)) return Integer.valueOf((short)-n.intValue());
		if (isLongIntegerObject(n)) return Long.valueOf(-n.longValue());
		if (isSinglePrecisionNumberObject(n)) return Float.valueOf(-n.floatValue());
		return Double.valueOf(-n.doubleValue());
	}
	
	/** Replies the sum of the two numbers.
	 * 
	 * @param a
	 * @param b
	 * @param line
	 * @return the number with inverted sign.
	 * @throws InterpreterException
	 */
	public static Number add(Number a, Number b, int line) throws InterpreterException {
		try {
			return parse(Double.toString(a.doubleValue() + b.doubleValue()), -1);
		}
		catch (CompilerException e) {
			throw new InterpreterException(InterpreterErrorType.EXPECTING_NUMBER, line);
		}
	}

	/** Replies if the given number is a byte.
	 * 
	 * @param n
	 * @return <code>true</code> or <code>false</code>!!!
	 */
	public static boolean isByteObject(Number n) {
		return (n instanceof Byte);
	}

	/** Replies if the given number is a short integer.
	 * 
	 * @param n
	 * @return <code>true</code> or <code>false</code>!!!
	 */
	public static boolean isShortIntegerObject(Number n) {
		return (n instanceof Short);
	}

	/** Replies if the given number is an integer.
	 * 
	 * @param n
	 * @return <code>true</code> or <code>false</code>!!!
	 */
	public static boolean isIntegerObject(Number n) {
		return (n instanceof Integer) || (n instanceof AtomicInteger);
	}

	/** Replies if the given number is a long integer.
	 * 
	 * @param n
	 * @return <code>true</code> or <code>false</code>!!!
	 */
	public static boolean isLongIntegerObject(Number n) {
		return (n instanceof Long) || (n instanceof AtomicLong) || (n instanceof BigInteger);
	}

	/** Replies if the given number is a single precision number object.
	 * 
	 * @param n
	 * @return <code>true</code> or <code>false</code>!!!
	 */
	public static boolean isSinglePrecisionNumberObject(Number n) {
		return (n instanceof Float);
	}

	/** Replies if the given number is a double precision number object.
	 * 
	 * @param n
	 * @return <code>true</code> or <code>false</code>!!!
	 */
	public static boolean isDoublePrecisionNumberObject(Number n) {
		return (n instanceof Double) || (n instanceof BigDecimal);
	}
	
	/** Compare two numbers.
	 * 
	 * @param a
	 * @param b
	 * @return a negative integer is <var>a</var> is lower than <var>b</var>,
	 * a positibe integer is <var>a</var> is greated than <var>b</var>,
	 * or {@code 0} if they are equal.
	 */
	public static int compare(Number a, Number b) {
		return Double.compare(a.doubleValue(), b.doubleValue());
	}

}
