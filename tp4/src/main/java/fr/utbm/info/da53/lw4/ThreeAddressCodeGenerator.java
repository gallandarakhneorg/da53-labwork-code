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
package fr.utbm.info.da53.lw4;

import java.io.File;
import java.util.SortedMap;
import java.util.TreeMap;

import fr.utbm.info.da53.lw4.construct.Statement;
import fr.utbm.info.da53.lw4.error.ErrorRepository;
import fr.utbm.info.da53.lw4.error.IntermediateCodeGenerationException;
import fr.utbm.info.da53.lw4.error.LoggableException;
import fr.utbm.info.da53.lw4.interpreter.ThreeAddressCodeInterpreter;
import fr.utbm.info.da53.lw4.parser.BasicParser;
import fr.utbm.info.da53.lw4.threeaddresscode.ThreeAddressCode;

/**
 * Generator of three-address code.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class ThreeAddressCodeGenerator {
	
	/** Run the parser and the interpreter.
	 * 
	 * @param args
	 * @throws IntermediateCodeGenerationException 
	 */
	public static void main(String args[]) throws IntermediateCodeGenerationException {
		BasicParser parser;
		
		if(args.length==0){
			parser = new BasicParser(System.in);
		}
		else {
			File f = new File(args[0]);
			try {
				parser = new BasicParser(new java.io.FileInputStream(f));
			}
			catch(java.io.FileNotFoundException e){
				e.printStackTrace();
				return ;
			}
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
		
		// Interpreter
		ThreeAddressCodeInterpreter interpreter = new ThreeAddressCodeInterpreter(_3code);
		interpreter.run();
	}

}