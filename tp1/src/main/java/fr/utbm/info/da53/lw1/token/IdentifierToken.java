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

import java.lang.ref.WeakReference;

import fr.utbm.info.da53.lw1.symbol.SymbolTableEntry;
import fr.utbm.info.da53.lw1.symbol.SymbolTablePointer;

/** This is the token for all the identifiers.
 * 
 * @author Jonathan DEMANGE &lt;jonathan.demange@utbm.fr&gt;
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class IdentifierToken extends Token implements SymbolTablePointer {
	
	private WeakReference<SymbolTableEntry> symbolTablePointer = null;
	
	/**
	 * @param lexeme is the lexeme for the identifier.
	 */
	public IdentifierToken(String lexeme) {
		super(lexeme.toLowerCase());
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void setSymbolTableEntry(SymbolTableEntry entry) {
		if (entry==null) {
			this.symbolTablePointer = null;
		}
		else {
			this.symbolTablePointer = new WeakReference<SymbolTableEntry>(entry);
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public SymbolTableEntry getSymbolTableEntry() {
		return this.symbolTablePointer==null ? null : this.symbolTablePointer.get();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("<ID,"); //$NON-NLS-1$
		b.append(lexeme());
		b.append(","); //$NON-NLS-1$
		if (this.symbolTablePointer!=null) {
			b.append("0x"); //$NON-NLS-1$
			b.append(Integer.toHexString(System.identityHashCode(this.symbolTablePointer)));
		}
		else {
			b.append("0x0"); //$NON-NLS-1$
		}
		b.append(">"); //$NON-NLS-1$
		return b.toString();
	}
	
}

