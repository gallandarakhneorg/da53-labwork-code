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
package fr.utbm.info.da53.lw5;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import fr.utbm.info.da53.lw5.error.IntermediateCodeGenerationException;
import fr.utbm.info.da53.lw5.vm.TinyVirtualMachine;

/**
 * Run three-address code in virtual machine.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class TinyBasicVM {
	
	/** Run the virtual machine.
	 * 
	 * @param args
	 * @throws IntermediateCodeGenerationException 
	 * @throws IOException 
	 */
	public static void main(String args[]) throws IntermediateCodeGenerationException, IOException {
		File f;

		switch(args.length) {
		case 0:
			throw new IOException("no input file"); //$NON-NLS-1$
		default:
			f = new File(args[0]);
			break;
		}
		
		System.out.println("Running: "+f); //$NON-NLS-1$

		byte[] buffer = new byte[1024];
		byte[] byteCode = new byte[0];
		
		try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f))) {
			int len = bis.read(buffer);
			while (len>0) {
				byte[] b = new byte[byteCode.length+len];
				System.arraycopy(byteCode, 0, b, 0, byteCode.length);
				System.arraycopy(buffer, 0, b, byteCode.length, len);
				byteCode = b;
				len = bis.read(buffer);
			}
		}

		TinyVirtualMachine vm = new TinyVirtualMachine(byteCode);
		vm.run();
	}

}