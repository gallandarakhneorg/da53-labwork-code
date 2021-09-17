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
package fr.utbm.info.da53.lw5.threeaddresscode;

/**
 * Bases of any address.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public enum AddressBase {

	/** The base of the address is the first address in the heap.
	 */
	HEAP {
		@Override
		public boolean isReadOnly() {
			return false;
		}
	},

	/** The base of the address is the start of the current
	 * memory context.
	 */
	MEMORY_CONTEXT {
		@Override
		public boolean isReadOnly() {
			return false;
		}
	},

	/** The base of the address is the first address of the program.
	 */
	PROGRAM_START {
		@Override
		public boolean isReadOnly() {
			return true;
		}
	},
	
	/** The base of the address is the address of the program just
	 * after the last instruction of the program.
	 */
	PROGRAM_END {
		@Override
		public boolean isReadOnly() {
			return true;
		}
	};
	
	/** Replies if the base of the address corresponds to read-only region
	 * of the memory.
	 * 
	 * @return <code>true</code> if the region is supposed to be read-only;
	 * <code>false</code> otherwise.
	 */
	public abstract boolean isReadOnly();

}
