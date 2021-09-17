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
 * This is the token for all the arithmetic operators.
 * 
 * @author Jonathan DEMANGE &lt;jonathan.demange@utbm.fr&gt;
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class ArithmeticOperatorToken extends Token {

	/** Factory method for this token.
	 *
	 * @param lexeme the lexeme.
	 * @param tokenType the token type, usually this type.
	 */
	public static ArithmeticOperatorToken create(String lexeme, Class<? extends Token> tokenType) {
		ArithmeticOperatorType type;
		switch (lexeme) {
		case "+":
			type = ArithmeticOperatorType.PLUS;
			break;
		case "-":
			type = ArithmeticOperatorType.MINUS;
			break;
		case "/":
			type = ArithmeticOperatorType.DIVIDE;
			break;
		case "*":
			type = ArithmeticOperatorType.MULTIPLY;
			break;
		default:
			throw new RuntimeException("unsupported arithmetic operator: " + lexeme);
		}
		return new ArithmeticOperatorToken(lexeme.charAt(0), type);
	}

	/**
	 * This is the enumeration of all the supported arithmetic operators.
	 * 
	 * @author Jonathan DEMANGE &lt;jonathan.demange@utbm.fr&gt;
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $Name$ $Revision$ $Date$
	 */
	public static enum ArithmeticOperatorType {
		/** Addition.
		 */
		PLUS,
		/** substraction.
		 */
		MINUS,
		/** Division.
		 */
		DIVIDE,
		/** Multiplication.
		 */
		MULTIPLY;
	}
	
	private final ArithmeticOperatorType type;

	/**
	 * @param lexeme is the lexeme associated to this token.
	 * @param type is the type of the operator.
	 */
	public ArithmeticOperatorToken(char lexeme, ArithmeticOperatorType type) {
		super(Character.toString(lexeme));
		assert(type!=null);
		this.type = type;
	}
	
	/** Replies the type of this operator.
	 * 
	 * @return the type of this operator.
	 */
	public ArithmeticOperatorType type() {
		return this.type;
	}
	
	/** {@InheritDoc}
	 */
	@Override
	public String toString() {
		return "<OP," //$NON-NLS-1$
				+ this.type
				+">"; //$NON-NLS-1$
	}
	
}
