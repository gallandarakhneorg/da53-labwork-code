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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.SortedMap;
import java.util.TreeMap;

import fr.utbm.info.da53.lw5.construct.Statement;
import fr.utbm.info.da53.lw5.error.ErrorRepository;
import fr.utbm.info.da53.lw5.error.IntermediateCodeGenerationException;
import fr.utbm.info.da53.lw5.error.LoggableException;
import fr.utbm.info.da53.lw5.parser.BasicParser;
import fr.utbm.info.da53.lw5.threeaddresscode.ThreeAddressCode;

/**
 * Generator of three-address code.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class TinyBasicCompiler {
	
	/** Run the parser and the generator.
	 * 
	 * @param args
	 * @throws IntermediateCodeGenerationException 
	 * @throws IOException 
	 */
	@SuppressWarnings("resource")
	public static void main(String args[]) throws IntermediateCodeGenerationException, IOException {
		BasicParser parser;
		OutputStream out;
		File f;

		switch(args.length) {
		case 0:
			throw new IOException("no output file"); //$NON-NLS-1$
		case 1:
			parser = new BasicParser(System.in);
			f = new File(args[0]);
			out = new FileOutputStream(f);
			break;
		default:
			f = new File(args[0]);
			parser = new BasicParser(new java.io.FileInputStream(f));
			f = new File(args[1]);
			out = new FileOutputStream(f);
			break;
		}

		// Parse
		SortedMap<Integer,Statement> code = null;
		try {
			code = parser.executeCompiler();
		}
		catch(LoggableException e) {
			ErrorRepository.add(e);
		}
		
		if (code==null) {
			code = new TreeMap<Integer, Statement>();
		}
		
		// Generation
		ThreeAddressCode _3code = new ThreeAddressCode(parser.getSymbolTable());
		
		for(Statement statement : code.values()) {
			statement.generate(_3code);
		}
		
		_3code.finalizeGeneration();
		
		// Output
		System.out.println(_3code.toString());
		
		// Byte code
		byte[] byteCode = _3code.getByteCode();
		out.write(byteCode);
		
		System.out.println("Written: "+byteCode.length+" bytes"); //$NON-NLS-1$ //$NON-NLS-2$
	}

}