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
package fr.utbm.info.da53.lw2;

import java.io.File;
import java.util.SortedMap;

import fr.utbm.info.da53.lw2.context.DebugInterpreter;
import fr.utbm.info.da53.lw2.context.Interpreter;
import fr.utbm.info.da53.lw2.context.LineBasedInterpreter;
import fr.utbm.info.da53.lw2.context.Statement;
import fr.utbm.info.da53.lw2.error.ErrorRepository;
import fr.utbm.info.da53.lw2.error.InterpreterException;
import fr.utbm.info.da53.lw2.error.LoggableException;
import fr.utbm.info.da53.lw2.parser.BasicParser;
import fr.utbm.info.da53.lw2.ui.InterpreterDialog;

/**
 * Interpreter of BASIC.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class BasicInterpreter {
	
	/** Run the parser and the interpreter.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		BasicParser parser;
		String filename;

		if(args.length == 0){
			System.out.println("Input your TinyBasic code:");
			parser = new BasicParser(System.in);
			filename = "Standard Input"; //$NON-NLS-1$
		}
		else if(args.length == 1){
			File f = new File(args[0]);
			try {
				parser = new BasicParser(new java.io.FileInputStream(f));
			}
			catch(java.io.FileNotFoundException e){
				e.printStackTrace();
				return ;
			}
			filename = f.getName();
		}
		else {
			return ;
		}
		SortedMap<Integer,Statement> code = null;
		try {
			code = parser.executeCompiler();
		}
		catch(LoggableException e) {
			ErrorRepository.add(e);
		}
		
		System.out.println("Running the code into the interpreter");
		DebugInterpreter debugContext = null;

		if (code!=null && !code.isEmpty()) {
			Interpreter interpreter = new LineBasedInterpreter(code, parser.getSymbolTable());
			try {
				debugContext = interpreter.debug();
			}
			catch (InterpreterException e) {
				ErrorRepository.add(e);
			}
		}
		
		InterpreterDialog dialog = new InterpreterDialog(filename, debugContext, !ErrorRepository.hasError());
		dialog.setVisible(true);
	}

}