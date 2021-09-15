/* 
 * $Id$
 * 
 * Copyright (c) 2012-2021 Stephane GALLAND, Jonathan DEMANGE.
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
 * This is the token for all the comparison operators.
 * 
 * @author Jonathan DEMANGE &lt;jonathan.demange@utbm.fr&gt;
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class RelationalOperatorToken extends Token {
	
	/**
	 * This is the enumeration of all the supported comparison operators.
	 * 
	 * @author Jonathan DEMANGE &lt;jonathan.demange@utbm.fr&gt;
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $Name$ $Revision$ $Date$
	 */
	public static enum RelationalOperatorType {
		/** Lower, stricly.
		 */
		LT,
		/** Lower or equal.
		 */
		LE,
		/** Equal.
		 */
		EQ,
		/** Different.
		 */
		NE,
		/** Greater, stricly.
		 */
		GT,
		/** Greater or equal.
		 */
		GE;
	}
	
	private final RelationalOperatorType type;
	
	/**
	 * @param lexeme is the lexeme associated to this token.
	 * @param type is the type of the operator.
	 */
	public RelationalOperatorToken(String lexeme, RelationalOperatorType type) {
		super(lexeme);
		assert(type!=null);
		this.type = type;
	}
	
	/** Replies the type of this operator.
	 * 
	 * @return the type of this operator.
	 */
	public RelationalOperatorType type() {
		return this.type;
	}
	
	/** {@InheritDoc}
	 */
	@Override
	public String toString() {
		return "<RELOP," //$NON-NLS-1$
				+ this.type
				+">"; //$NON-NLS-1$
	}
	
}
