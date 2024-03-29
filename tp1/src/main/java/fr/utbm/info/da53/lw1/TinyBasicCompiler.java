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
package fr.utbm.info.da53.lw1;

import java.io.File;
import java.io.FileReader;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import fr.utbm.info.da53.lw1.lexer.CharacterPerCharacterLexer;
import fr.utbm.info.da53.lw1.lexer.Lexer;
import fr.utbm.info.da53.lw1.lexer.RegexLexer;
import fr.utbm.info.da53.lw1.symbol.SymbolTable;
import fr.utbm.info.da53.lw1.token.Token;
import fr.utbm.info.da53.lw1.util.LocaleUtil;

/** This is the TinyBasic compiler.
 * 
 * @author Jonathan DEMANGE &lt;jonathan.demange@utbm.fr&gt;
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class TinyBasicCompiler {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// Get the prefered source directory
		Preferences prefs = Preferences.userNodeForPackage(TinyBasicCompiler.class);
		String directory = prefs.get("DIRECTORY", null); //$NON-NLS-1$
		File dir = null;
		if (directory!=null && !directory.isEmpty()) {
			File f = new File(directory);
			if (f.isDirectory())
				dir = f;
		}
		
		// Select the file to read
		JFileChooser chooser = new JFileChooser(dir);
		chooser.setFileFilter(new TinyBasicFileFilter());
		
		if (chooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
			File inputFile = chooser.getSelectedFile();
			
			// Save the prefered source directory
			prefs.put("DIRECTORY", inputFile.getParent()); //$NON-NLS-1$
			
			// Parse the file
			SymbolTable symbolTable = new SymbolTable();
			
			int lexerType = JOptionPane.showConfirmDialog(
					null,
					LocaleUtil.getString(TinyBasicCompiler.class, "LEXER_TYPE"),
					LocaleUtil.getString(TinyBasicCompiler.class, "LEXER_TYPE_TITLE"),
				    JOptionPane.YES_NO_OPTION);
			
			Lexer lexer;
			if (lexerType == JOptionPane.YES_OPTION) {
				lexer = new RegexLexer(new FileReader(inputFile), symbolTable);
			} else { 
				lexer = new CharacterPerCharacterLexer(new FileReader(inputFile), symbolTable);
			}
			
			Token token = lexer.getNextToken();
			int previousLine = 1;
			boolean isFirstColumn = true;
			
			while (token!=null) {
				if (previousLine!=lexer.getCurrentLine()) {
					previousLine = lexer.getCurrentLine();
					System.out.println();
					isFirstColumn = true;
				}
				if (!isFirstColumn) System.out.print(" "); //$NON-NLS-1$
				System.out.print(token.toString());
				isFirstColumn = false;
				token = lexer.getNextToken();
			}
			
			lexer.dispose();
		}
	}
	
}
