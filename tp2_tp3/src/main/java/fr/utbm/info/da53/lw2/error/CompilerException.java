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
package fr.utbm.info.da53.lw2.error;

import java.io.IOException;
import java.io.PrintStream;

/**
 * Exception in the compiler.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class CompilerException extends LoggableException {

	private static final long serialVersionUID = 3158598777138261206L;
	
	private final CompilationErrorType type;
	
	/**
	 * @param type
	 * @param e
	 */
	public CompilerException(CompilationErrorType type, Throwable e) {
		super(e);
		assert(type!=null);
		this.type = type;
	}

	/**
	 * @param type
	 * @param line
	 * @param e
	 */
	public CompilerException(CompilationErrorType type, int line, Throwable e) {
		super(line, e);
		assert(type!=null);
		this.type = type;
	}

	/**
	 * @param type
	 * @param line
	 * @param message
	 */
	public CompilerException(CompilationErrorType type, int line, String message) {
		super(line, message);
		assert(type!=null);
		this.type = type;
	}

	/**
	 * @param type
	 * @param message
	 * @param e
	 */
	public CompilerException(CompilationErrorType type, String message, Throwable e) {
		super(message, e);
		assert(type!=null);
		this.type = type;
	}
	
	/** Replies the type of the error.
	 * 
	 * @return the type of the error.
	 */
	public CompilationErrorType type() {
		return this.type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void print(PrintStream stream) throws IOException {
		ErrorRepository.printError(
				stream,
				type().getMessage(),
				line(),
				getMessage(),
				getCause());
	}

}
