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

import java.io.IOException;
import java.io.Reader;

import fr.utbm.info.da53.lw1.error.IllegalCharacterError;
import fr.utbm.info.da53.lw1.error.InvalidNumberFormatError;
import fr.utbm.info.da53.lw1.error.SyntaxError;
import fr.utbm.info.da53.lw1.error.Warnings;
import fr.utbm.info.da53.lw1.symbol.SymbolTable;
import fr.utbm.info.da53.lw1.symbol.SymbolTableEntry;
import fr.utbm.info.da53.lw1.token.ArithmeticOperatorToken;
import fr.utbm.info.da53.lw1.token.ArithmeticOperatorToken.ArithmeticOperatorType;
import fr.utbm.info.da53.lw1.token.CloseParenthesisToken;
import fr.utbm.info.da53.lw1.token.EndToken;
import fr.utbm.info.da53.lw1.token.GosubToken;
import fr.utbm.info.da53.lw1.token.GotoToken;
import fr.utbm.info.da53.lw1.token.IdentifierToken;
import fr.utbm.info.da53.lw1.token.IfToken;
import fr.utbm.info.da53.lw1.token.InputToken;
import fr.utbm.info.da53.lw1.token.LetToken;
import fr.utbm.info.da53.lw1.token.NumberToken;
import fr.utbm.info.da53.lw1.token.OpenParenthesisToken;
import fr.utbm.info.da53.lw1.token.PrintToken;
import fr.utbm.info.da53.lw1.token.RelationalOperatorToken;
import fr.utbm.info.da53.lw1.token.RelationalOperatorToken.RelationalOperatorType;
import fr.utbm.info.da53.lw1.token.ReturnToken;
import fr.utbm.info.da53.lw1.token.StringLiteralToken;
import fr.utbm.info.da53.lw1.token.ThenToken;
import fr.utbm.info.da53.lw1.token.Token;

/** This is the lexical analyzer. This lexer reads character-per-character the input file
 * (through the Scanner) and detect the correct token with "switch".
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class CharacterPerCharacterLexer implements Lexer {
	
	private final Scanner peeker;
	private final SymbolTable symbolTable;
	
	/**
	 * @pre:
	 *  - fileName is not nul
	 * 
	 * @post:
	 *  - A Scanner is created with its input pointer on the first character of file
	 *  - The state of the lexical analysor is initialized
	 *  - The line number is 1 
	 *  - The column number is 1
	 *
	 * @param stream
	 * @param symbolTable
	 * @throws IOException
	 */
	public CharacterPerCharacterLexer(Reader stream, SymbolTable symbolTable) throws IOException {
		assert(stream!=null);
		assert(symbolTable!=null);
		
		this.peeker = new Scanner(stream);
		this.symbolTable = symbolTable;
		
		// Define the reserved words of the language
		defineReservedWords(this.symbolTable);
	}
	
	/** Defined the reserved words of the language
	 * This function may be overridden to change the reserved words.
	 * 
	 * @param table is the symbol table to fill.
	 */
	protected void defineReservedWords(SymbolTable table) {
		table.addReservedWord(new PrintToken());
		table.addReservedWord(new IfToken());
		table.addReservedWord(new ThenToken());
		table.addReservedWord(new GotoToken());
		table.addReservedWord(new InputToken());
		table.addReservedWord(new LetToken());
		table.addReservedWord(new GosubToken());
		table.addReservedWord(new ReturnToken());
		table.addReservedWord(new EndToken());
	}
	
	/** Return the number of the column where the next character is located 
	 * in the source program.
	 * 
	 * @return the number of the column.
	 */
	public int getCurrentColumn() {
		return this.peeker.getCurrentColumn();
	}
	
	/** Return the number of the line where the next character is located 
	 * in the source program.
	 * 
	 * @return the number of the line.
	 */
	public int getCurrentLine() {
		return this.peeker.getCurrentLine();
	}

	/** Dispose all the resources used by the lexer (including the input stream).
	 * 
	 * @throws IOException
	 */
	public void dispose() throws IOException {
		this.peeker.close();
	}
	
	/**
	 * Read the next token from the input stream.
	 * 
	 * @return the next token, or <code>null</code> if no more token.
	 * @throws SyntaxError
	 */
	public Token getNextSymbol() throws SyntaxError {
		Token token;
		
		try
		{
			// Loop until EOF, or a token was found
			while (!this.peeker.isEOF()) {
				token = null;

				eatWhiteSpaces();

				switch(this.peeker.peek()) {
				case '(':
					return new OpenParenthesisToken(this.peeker.get());
				case ')':
					return new CloseParenthesisToken(this.peeker.get());
				case '+':
					return new ArithmeticOperatorToken(this.peeker.get(), ArithmeticOperatorType.PLUS);
				case '-':
					return new ArithmeticOperatorToken(this.peeker.get(), ArithmeticOperatorType.MINUS);
				case '*':
					return new ArithmeticOperatorToken(this.peeker.get(), ArithmeticOperatorType.MULTIPLY);
				case '/':
					return new ArithmeticOperatorToken(this.peeker.get(), ArithmeticOperatorType.DIVIDE);
				case '=':
					return parseEqualSign();
				case '<':
					return parseLowerSign();
				case '>':
					return parseUpperSign();
				case '"':
					return parseStringLiteral();
				}
	
				if (Character.isDigit(this.peeker.peek())) {
					return parseNumber(Scanner.EOF);
				}

				if (Character.isLetter(this.peeker.peek()) || this.peeker.peek()=='_') {
					token = parseIdentifier();
					if (token!=null) {
						return token;
					}
				}
				else if (this.peeker.peek()!=Scanner.EOF) {
					throw new IllegalCharacterError(
							this.peeker.getCurrentLine(),
							this.peeker.getCurrentColumn(),
							this.peeker.get());
				}
	
			}
		}
		catch(SyntaxError e) {
			throw e;
		}
		catch(Throwable e) {
			throw new SyntaxError(this.peeker.getCurrentLine(), this.peeker.getCurrentColumn(), e);
		}

		// No more token
		this.peeker.close();
		return null;
	}
	
	private void eatWhiteSpaces() throws IOException {
		if (Character.isWhitespace(this.peeker.peek())) {
			//
			// Try to eat the white space
			//
			this.peeker.get();
		}
	}
	
	private Token parseEqualSign() throws IOException {
		StringBuffer lexemeBuffer = new StringBuffer();
		lexemeBuffer.append(this.peeker.get());
		
		RelationalOperatorType type;
		
		switch(this.peeker.peek()) {
		case '>':
			lexemeBuffer.append(this.peeker.get());
			type = RelationalOperatorType.GE;
			break;
		case '<':
			lexemeBuffer.append(this.peeker.get());
			type = RelationalOperatorType.LE;
			break;
		default:
			type = RelationalOperatorType.EQ;
		}
		
		return new RelationalOperatorToken(lexemeBuffer.toString(), type);
	}
	
	private Token parseLowerSign() throws IOException {
		StringBuffer lexemeBuffer = new StringBuffer();
		lexemeBuffer.append(this.peeker.get());

		RelationalOperatorType type;
		
		switch(this.peeker.peek()) {
		case '>':
			lexemeBuffer.append(this.peeker.get());
			type = RelationalOperatorType.NE;
			break;
		case '=':
			lexemeBuffer.append(this.peeker.get());
			type = RelationalOperatorType.LE;
			break;
		default:
			type = RelationalOperatorType.LT;
		}
		
		return new RelationalOperatorToken(lexemeBuffer.toString(), type);
	}
	
	private Token parseUpperSign() throws IOException {
		StringBuffer lexemeBuffer = new StringBuffer();
		lexemeBuffer.append(this.peeker.get());

		RelationalOperatorType type;
		
		switch(this.peeker.peek()) {
		case '<':
			lexemeBuffer.append(this.peeker.get());
			type = RelationalOperatorType.NE;
			break;
		case '=':
			lexemeBuffer.append(this.peeker.get());
			type = RelationalOperatorType.GE;
			break;
		default:
			type = RelationalOperatorType.GT;
		}
		
		return new RelationalOperatorToken(lexemeBuffer.toString(), type);
	}
	
	private Token parseIdentifier() throws IOException {
		//
		// Identifier recognition
		//
		int line = this.peeker.getCurrentLine();
		int column = this.peeker.getCurrentColumn();

		StringBuffer lexemeBuffer = new StringBuffer();
		do {
			lexemeBuffer.append(this.peeker.get());
		}
		while ((Character.isLetterOrDigit(this.peeker.peek()) || this.peeker.peek()=='_'));

		assert(lexemeBuffer.length()>0);

		String lexeme = lexemeBuffer.toString();

		// An identifier was recognized, but it may be a reserwed word or the REM statement.
		if ("REM".equalsIgnoreCase(lexeme)) { //$NON-NLS-1$
			while (this.peeker.get()!='\n') {
				// Eat the comment
			}
			return null; // No token recognized, try to recognize the next one
		}

		SymbolTableEntry entry = this.symbolTable.get(lexemeBuffer.toString());
		if (entry!=null) return entry.token();

		Token identifier = new IdentifierToken(lexeme);
		this.symbolTable.add(identifier, line, column);

		return identifier;
	}

	private Token parseNumber(char prefix) throws IOException, SyntaxError {
		StringBuffer lexemeBuffer = new StringBuffer();
		// Sign
		if (prefix==Scanner.EOF) {
			if (this.peeker.peek()=='+' || this.peeker.peek()=='-') {
				lexemeBuffer.append(this.peeker.get());
			}
		}
		else {
			lexemeBuffer.append(prefix);
		}
		boolean fractionalPartMandatory = false;
		// Integer part
		if (Character.isDigit(this.peeker.peek())) {
			do {
				lexemeBuffer.append(this.peeker.get());
			}
			while (Character.isDigit(this.peeker.peek()));
		}
		else if (this.peeker.peek()!='.') {
			throw new InvalidNumberFormatError(this.peeker.getCurrentLine(), this.peeker.getCurrentColumn());
		}
		else {
			fractionalPartMandatory = true;
		}
		
		// Fractional part
		if (this.peeker.peek()=='.') {
			lexemeBuffer.append(this.peeker.get());
			if (fractionalPartMandatory &&
				!Character.isDigit(this.peeker.peek())) {
				throw new InvalidNumberFormatError(this.peeker.getCurrentLine(), this.peeker.getCurrentColumn());
			}
			while (Character.isDigit(this.peeker.peek())) {
				lexemeBuffer.append(this.peeker.get());
			}
		}
		// Exponential part
		if (this.peeker.peek()=='e' || this.peeker.peek()=='E') {
			lexemeBuffer.append(Character.toUpperCase(this.peeker.get()));
			if (this.peeker.peek()=='+' || this.peeker.peek()=='-') {
				lexemeBuffer.append(this.peeker.get());
			}
			if (!Character.isDigit(this.peeker.peek())) {
				throw new InvalidNumberFormatError(this.peeker.getCurrentLine(), this.peeker.getCurrentColumn());
			}
			while (Character.isDigit(this.peeker.peek())) {
				lexemeBuffer.append(this.peeker.get());
			}
		}
		
		return new NumberToken(lexemeBuffer.toString());
	}
	
	private Token parseStringLiteral() throws IOException {
		char c;
		StringBuffer lexemeBuffer = new StringBuffer();
		
		this.peeker.get(); // eat first "
		
		while (this.peeker.peek()!='"') {
			c = this.peeker.get();
			if (c==Scanner.EOF) {
				Warnings.missingQuoteCharacter(this.peeker.getCurrentLine(), this.peeker.getCurrentColumn());
				break;
			}
			lexemeBuffer.append(c);
		}
		
		this.peeker.get(); // eat last "
		
		return new StringLiteralToken(lexemeBuffer.toString());
	}
	
}
