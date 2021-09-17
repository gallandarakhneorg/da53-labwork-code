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
package fr.utbm.info.da53.lw5.vm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Manager of the heap.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
class HeapManager {

	private final long baseAddress;
	private final Map<Long,Integer> addressMapping = new TreeMap<Long,Integer>();
	private final List<HeapRegion> regions = new ArrayList<HeapRegion>();
	
	/**
	 * @param baseAddress
	 */
	public HeapManager(long baseAddress) {
		this.baseAddress = baseAddress;
	}
	
	/** Reset the content of the heap.
	 */
	public void reset() {
		this.addressMapping.clear();
		this.regions.clear();
	}
	
	/** Replies the region at the given address.
	 * 
	 * @param adr
	 * @return the region, never <code>null</code>.
	 */
	public HeapRegion getRegion(long adr) {
		Integer a = this.addressMapping.get(adr);
		if (a==null) throw new RuntimeException("heap region not found: "+adr); //$NON-NLS-1$
		int idx = a.intValue();
		if (idx<0 || idx>=this.regions.size())
			throw new RuntimeException("heap region not found: "+adr); //$NON-NLS-1$
		return this.regions.get(idx);
	}
	
	/** Create a region.
	 * 
	 * @return the region, never <code>null</code>.
	 */
	public HeapRegion newRegion() {
		long adr = this.baseAddress + this.addressMapping.size();
		HeapRegion region = new HeapRegion(adr);
		this.addressMapping.put(adr, this.regions.size());
		this.regions.add(region);
		return region;
	}

}