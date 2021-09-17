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
package fr.utbm.info.da53.lw4.construct;

import fr.utbm.info.da53.lw4.error.IntermediateCodeGenerationException;
import fr.utbm.info.da53.lw4.threeaddresscode.ThreeAddressCode;

/**
 * Statement in TinyBasic.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public interface Statement {

	/** Generate the three-address code.
	 * 
	 * @param code is the code container to fill.
	 * @throws IntermediateCodeGenerationException when something bar occur during the execution of the statement.
	 */
	public void generate(ThreeAddressCode code) throws IntermediateCodeGenerationException;

	/** Replies the line of the statement in the Basic program.
	 * 
	 * @return the line of the statement in the Basic program.
	 */
	public int basicLine();
	
	/** Set the line of the statement in the Basic program.
	 * 
	 * @param line is the line of the statement in the Basic program.
	 */
	public void setBasicLine(int line);
	
}
