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
package fr.utbm.info.da53.lw1.lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Scanner is a class to read input stream.
 * <p>
 * Assumption:
 * The Scanner does not remove the comments because it is too much complex
 * to recognize the "REM" string. It may be the beginning of an identifier.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class Scanner {
	
	/** The special character EOF.
	 */
	public static final char EOF = (char)-1;
	
	/** Current pointer position input Peeker in the source file */
	private int currentRowNumber;

	/** Current pointer position input Peeker in the source file */
	private int currentColNumber;
	
	/** Input stream of the source file */
	private Reader input;
	
	/** Next character */
	private char charac;
	
	/**
	 * 
	 * @param is is the input stream
	 * @throws IOException
	 */
	public Scanner(Reader is) throws IOException {
		if (is instanceof BufferedReader)
			this.input = is;
		else
			this.input = new BufferedReader(is);
		readLookahead();
		this.currentRowNumber = 1;
		this.currentColNumber = 1;
	}
	
	private void readLookahead() throws IOException {
		if (this.charac!=EOF) {
			this.charac = (char)this.input.read();
		}
	}
	
	/**
	 * Return the next available character, but do not consume it.
	 * 
	 * @return the next available character.
	 * @see #get()
	 */
	public char peek() {
		return this.charac;
	}
	
	/**
	 * Return the next available character, and consume it.
	 * 
	 * @return the next available character.
	 * @see #peek()
	 * @throws IOException
	 */
	public char get() throws IOException {
		char old = this.charac;
		
		if(old == '\n') {
			++this.currentRowNumber;
			this.currentColNumber = 1;
		}
		else {
			++this.currentColNumber;
		}
		
		readLookahead();
		if (Character.isWhitespace(old)) {
			while (Character.isWhitespace(this.charac)) {
				if(this.charac == '\n') {
					++this.currentRowNumber;
					this.currentColNumber = 1;
				}
				else {
					++this.currentColNumber;
				}
				readLookahead();
			}
		}
		
		return old;
	}
	
	/**
	 * Return if the "eof" character was found.
	 * @return <code>true</code> if the "eof" character was read;
	 * otherwise <code>false</code>.
	 */
	public boolean isEOF() {
		return ( this.charac == EOF );
	}
	
	/** Return the number of the column where the next character is located 
	 * in the source program.
	 * 
	 * @return the number of the column.
	 */
	public int getCurrentColumn() {
		return this.currentColNumber;
	}
	
	/** Return the number of the line where the next character is located 
	 * in the source program.
	 * 
	 * @return the number of the line.
	 */
	public int getCurrentLine() {
		return this.currentRowNumber;
	}
	
	/**
	 * Close the input stream.
	 */
	public void close() {
		try {
			this.input.close();
		}
		catch (IOException ex) {
			//
		}
	}
	
}
