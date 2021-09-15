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
package fr.utbm.info.da53.lw1.symbol;

/**
 * This interface describes an object that has a pointer on
 * a symbol table entry.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public interface SymbolTablePointer {
		
	/** Set the pointer to the symbol entry.
	 * 
	 * @param entry is the pointer to the symbol entry.
	 */
	public void setSymbolTableEntry(SymbolTableEntry entry);

	/** Return the pointer to the symbol entry.
	 * 
	 * @return the pointer to the symbol entry.
	 */
	public SymbolTableEntry getSymbolTableEntry();

}
