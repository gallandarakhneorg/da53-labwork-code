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
package fr.utbm.info.da53.lw5.syntaxtree;

import java.io.Serializable;

import fr.utbm.info.da53.lw5.symbol.SymbolTable;

/**
 * Name of a variable.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class VariableName implements Cloneable, Serializable {
	
	private static final long serialVersionUID = -7273497109097154795L;
	
	private final String identifier;
	private final AbstractValueTreeNode arrayIndex;
	
	/**
	 * @param identifier
	 * @param arrayIndex
	 */
	public VariableName(String identifier, AbstractValueTreeNode arrayIndex) {
		assert(identifier!=null && !identifier.isEmpty());
		this.identifier = SymbolTable.formatIdentifier(identifier);
		this.arrayIndex = arrayIndex;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public VariableName clone() {
		try {
			return (VariableName)super.clone();
		}
		catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}
	
	/** Replies the identifier of the variable.
	 * 
	 * @return the identifier of the variable.
	 */
	public String id() {
		return this.identifier;
	}
	
	/** Replies the index in an array.
	 * 
	 * @return the index in an array.
	 */
	public AbstractValueTreeNode getArrayIndex() {
		return this.arrayIndex;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		if (this.arrayIndex==null) {
			return this.identifier;
		}
		return this.identifier
				+"(" //$NON-NLS-1$
				+this.arrayIndex.toString()
				+")"; //$NON-NLS-1$
	}
	
}
