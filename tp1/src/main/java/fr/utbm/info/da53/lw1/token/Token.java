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
 * An abstract class to define token used by the lexical analyzer.
 *
 * @author Jonathan DEMANGE &lt;jonathan.demange@utbm.fr&gt;
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public abstract class Token implements Comparable<Token> {
	
	/** Lexeme associated to the token.
	 */
	private final String lexeme;
	
	/**
	 * @param lexeme
	 */
	protected Token(String lexeme) {
		assert(lexeme!=null && !lexeme.isEmpty());
		this.lexeme = lexeme;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract String toString();
	
	/** Replies the lexeme associated to this token.
	 * 
	 * @return the lexeme
	 */
	public String lexeme() {
		return this.lexeme;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return 37 + this.lexeme.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (o!=null && getClass().equals(o.getClass())) {
			// The Basic language is case insensitive
			return this.lexeme.equalsIgnoreCase(((Token)o).lexeme);
		}
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(Token o) {
		if (o==null) return Integer.MIN_VALUE;
		// The Basic language is case insensitive
		return this.lexeme.compareToIgnoreCase(o.lexeme);
	}
	
}