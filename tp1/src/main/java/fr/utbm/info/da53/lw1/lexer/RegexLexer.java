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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.utbm.info.da53.lw1.error.SyntaxError;
import fr.utbm.info.da53.lw1.symbol.SymbolTable;
import fr.utbm.info.da53.lw1.symbol.SymbolTableEntry;
import fr.utbm.info.da53.lw1.token.ArithmeticOperatorToken;
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
import fr.utbm.info.da53.lw1.token.ReturnToken;
import fr.utbm.info.da53.lw1.token.StringLiteralToken;
import fr.utbm.info.da53.lw1.token.ThenToken;
import fr.utbm.info.da53.lw1.token.Token;

/** This is the lexical analyzer. This lexer reads the input file and apply regexs for extracting the tokens.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class RegexLexer implements Lexer {

	private final Reader stream;

	private String textContent;

	private final SymbolTable symbolTable;
	
	private final List<TokenRecognizer> recognizers = new ArrayList<>();

	private int line = 1;
	private int column = 1;

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
	public RegexLexer(Reader stream, SymbolTable symbolTable) throws IOException {
		assert(stream!=null);
		assert(symbolTable!=null);

		this.stream = stream;
		this.symbolTable = symbolTable;
		
		// Define the reserved words of the language
		defineReservedWords(this.symbolTable);

		// Define the regular expressions to be recognized
		defineRecognizers();
	}
	
	/** Defined the reserved words of the language.
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

	/** Defined the regular expressions of the language.
	 * This function may be overridden to change the regexs.
	 */
	protected void defineRecognizers() {
		this.recognizers.add(new SimpleTokenRecognizer(NumberToken.class, "[0-9]+(?:\\.[0-9]+)?"));
		this.recognizers.add(new SimpleTokenRecognizer(RelationalOperatorToken.class, "(?:<=)|(?:>=)|(?:<>)|(?:><)|[\\<\\>\\=]"));
		this.recognizers.add(new SimpleTokenRecognizer(ArithmeticOperatorToken.class, "[+\\-*/]"));
		this.recognizers.add(new SimpleTokenRecognizer(CloseParenthesisToken.class, "\\)"));
		this.recognizers.add(new SimpleTokenRecognizer(OpenParenthesisToken.class, "\\("));
		this.recognizers.add(new SimpleTokenRecognizer(StringLiteralToken.class, "\"(?:(?:\\\\.)|[^\"])*\""));
		this.recognizers.add(new StringTokenRecognizer(
				IdentifierToken.class,
				EndToken.class, "END",
				GosubToken.class, "GOSUB",
				GotoToken.class, "GOTO",
				EndToken.class, "END",
				IfToken.class, "IF",
				InputToken.class, "INPUT",
				LetToken.class, "LET",
				PrintToken.class, "PRINT",
				ReturnToken.class, "RETURN",
				ThenToken.class, "THEN"));
		this.recognizers.add(new NewlineTokenRecognizer());
	}

	/** Return the number of the column where the next character is located 
	 * in the source program.
	 * 
	 * @return the number of the column.
	 */
	public int getCurrentColumn() {
		return this.column;
	}
	
	/** Return the number of the line where the next character is located 
	 * in the source program.
	 * 
	 * @return the number of the line.
	 */
	public int getCurrentLine() {
		return this.line;
	}

	/** Dispose all the resources used by the lexer (including the input stream).
	 * 
	 * @throws IOException
	 */
	public void dispose() throws IOException {
		this.stream.close();
	}
	
	/**
	 * Read the next token from the input stream.
	 * 
	 * @return the next token, or {@code null} if no more token.
	 * @throws SyntaxError
	 */
	public Token getNextSymbol() throws SyntaxError {
		if (this.textContent == null) {
			StringBuilder content = new StringBuilder();
			try (BufferedReader breader = new BufferedReader(this.stream)) {
				String line = breader.readLine();
				while (line != null) {
					content.append(line).append("\n");
					line = breader.readLine();
				}
			} catch (IOException ex) {
				throw new SyntaxError(1, 1, ex);
			}
			this.textContent = content.toString();
		}
		if (!textContent.isBlank()) {
			Token token = null;
			do {
				TokenRecognition rec = findRecognizer(this.textContent);
				if (rec != null) {
					token = rec.createToken();
					this.textContent = this.textContent.substring(rec.endIndex());
					if (token == null) {
						// New line
						++this.line;
						this.column = 1;
					} else {
						this.column += rec.endIndex();
					}
				}
			} while (token == null && !textContent.isBlank());
			return token;
		}
		return null;
	}

	private TokenRecognition findRecognizer(String content) throws SyntaxError {
		TokenRecognition larger = null;
		for (TokenRecognizer recognizer : this.recognizers) {
			TokenRecognition rec = recognizer.matches(content);
			if (rec != null) {
				if  (larger == null) {
					larger = rec;
				} else if (rec.length() > larger.length()) {
					larger = rec;
				} else if (rec.length() == larger.length()) {
					throw new SyntaxError(getCurrentLine(), getCurrentColumn());
				}
			}
		}
		if (larger == null) {
			throw new SyntaxError(getCurrentLine(), getCurrentColumn());
		}
		return larger;
	}

	/** Token recognition.
	 * 
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $Name$ $Revision$ $Date$
	 */
	public class TokenRecognition {

		private final String lexeme;

		private final int endIndex;

		private final Class<? extends Token> tokenType;

		/** Constructor.
		 *
		 * @param lexeme the lexeme.
		 * @param tokenType the type of token.
		 * @param endIndex the index of the end of the token.
		 */
		public TokenRecognition(String lexeme, Class<? extends Token> tokenType, int endIndex) {
			this.lexeme = lexeme;
			this.tokenType = tokenType;
			this.endIndex = endIndex;
		}

		@Override
		public String toString() {
			return this.lexeme;
		}

		/** Replies the index of the last matching character for the lexeme.
		 *
		 * @return the index.
		 */
		public int endIndex() {
			return this.endIndex;
		}

		/** Create the token.
		 *
		 * @return the token.
		 */
		public Token createToken() {
			if (this.tokenType != null) {
				try {
					if (IdentifierToken.class.equals(this.tokenType)) {
						IdentifierToken token = new IdentifierToken(this.lexeme);
						SymbolTableEntry entry = RegexLexer.this.symbolTable.add(token, RegexLexer.this.getCurrentLine(), RegexLexer.this.getCurrentColumn());
						token.setSymbolTableEntry(entry);
						return token;
					} else {
						Method method = this.tokenType.getDeclaredMethod("create", String.class, Class.class);
						return (Token) method.invoke(null, this.lexeme, this.tokenType);
					}
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
			return null;
		}

		/** Replies the lexeme.
		 *
		 * @return the lexeme.
		 */
		public String lexeme() {
			return this.lexeme;
		}

		/** Replies the length of the lexeme.
		 *
		 * @return the length.
		 */
		public int length() {
			return this.lexeme.length();
		}
		
		/** Replies the type of token
		 *
		 * @return the type.
		 */
		public Class<? extends Token> tokenType() {
			return this.tokenType;
		}

	}

	/** Recognize a token with regular expression.
	 * 
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $Name$ $Revision$ $Date$
	 */
	public static interface TokenRecognizer {

		/** Replies the matched lexeme.
		 * 
		 * @param textContent the text to parse.
		 * @return the recognition, or {@code null}.
		 */
		TokenRecognition matches(String textContent);
		
	}

	/** Recognize an id-based token with regular expression.
	 * 
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $Name$ $Revision$ $Date$
	 */
	public class StringTokenRecognizer implements TokenRecognizer {

		private final Pattern pattern;

		private Class<? extends Token> defaultTokenType;

		private Map<String, Class<? extends Token>> mapping = new HashMap<>();

		/** Constructor.
		 *
		 * @param defaultTokenType default type of the token to be recognized.
		 * @param pairs of token type and keyword.
		 */
		@SuppressWarnings("unchecked")
		public StringTokenRecognizer(Class<? extends Token> defaultTokenType, Object... args) {
			this.pattern = Pattern.compile("^[ \t\f]*([_a-z][_a-z0-9]*)[ \t\f]*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			this.defaultTokenType = defaultTokenType;
			for (int i = 2; i < args.length; i += 2) {
				if (args[i-2] instanceof Class<?> && args[i-1] != null) {
					this.mapping.put(args[i-1].toString(), (Class<? extends Token>) args[i-2]);
				}
			}
		}

		@Override
		public TokenRecognition matches(String textContent) {
			Matcher matcher = this.pattern.matcher(textContent);
			if (matcher.find()) {
				String lexeme = matcher.group(1);
				for (Map.Entry<String, Class<? extends Token>> entry : this.mapping.entrySet()) {
					if (lexeme.equalsIgnoreCase(entry.getKey())) {
						return new TokenRecognition(lexeme, entry.getValue(), matcher.end(1));
					}
				}
				return new TokenRecognition(lexeme, this.defaultTokenType, matcher.end(1));
			}
			return null;
		}

	}

	/** Recognize a token with regular expression.
	 * 
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $Name$ $Revision$ $Date$
	 */
	public class SimpleTokenRecognizer implements TokenRecognizer {

		private final Class<? extends Token> tokenType;
		private final Pattern pattern;
		
		/** Constructor.
		 *
		 * @param tokenType type o the token to be recognized.
		 * @param regex the regular expression.
		 */
		public SimpleTokenRecognizer(Class<? extends Token> tokenType, String regex) {
			this.tokenType = tokenType;
			this.pattern = Pattern.compile("^[ \t\f]*(" + regex + ")[ \t\f]*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		}

		@Override
		public TokenRecognition matches(String textContent) {
			Matcher matcher = this.pattern.matcher(textContent);
			if (matcher.find()) {
				String lexeme = matcher.group(1);
				return new TokenRecognition(lexeme, this.tokenType, matcher.end(1));
			}
			return null;
		}
		
	}

	/** Recognize a newline.
	 * 
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $Name$ $Revision$ $Date$
	 */
	public class NewlineTokenRecognizer implements TokenRecognizer {

		private final Pattern pattern;

		/** Constructor.
		 */
		public NewlineTokenRecognizer() {
			this.pattern = Pattern.compile("^[ \t\f]*([\n\r])", Pattern.DOTALL);
		}

		@Override
		public TokenRecognition matches(String textContent) {
			Matcher matcher = this.pattern.matcher(textContent);
			if (matcher.find()) {
				String lexeme = matcher.group(1);
				return new TokenRecognition(lexeme, null, matcher.end(1));
			}
			return null;
		}
		
	}

}
