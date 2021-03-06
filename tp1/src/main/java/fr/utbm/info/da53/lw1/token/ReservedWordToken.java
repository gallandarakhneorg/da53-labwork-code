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

/** This is the token for all the reserved word.
 * 
 * @author Jonathan DEMANGE &lt;jonathan.demange@utbm.fr&gt;
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public abstract class ReservedWordToken extends Token {
	
	/**
	 * @param lexeme is the lexeme for the reserved word.
	 */
	public ReservedWordToken(String lexeme) {
		super(lexeme.toUpperCase());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "<" //$NON-NLS-1$
				+lexeme()
				+">"; //$NON-NLS-1$
	}

}

