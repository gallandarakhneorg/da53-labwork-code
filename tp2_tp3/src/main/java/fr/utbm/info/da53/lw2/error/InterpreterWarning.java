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
 * Warning in the interpreter.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class InterpreterWarning extends Warning {

	private static final long serialVersionUID = -3644456024343212494L;
	
	private final InterpreterErrorType type;
	
	/**
	 * @param type
	 * @param e
	 */
	public InterpreterWarning(InterpreterErrorType type, Throwable e) {
		super(e);
		assert(type!=null);
		this.type = type;
	}

	/**
	 * @param type
	 * @param line
	 */
	public InterpreterWarning(InterpreterErrorType type, int line) {
		super(line, (String)null);
		assert(type!=null);
		this.type = type;
	}

	/**
	 * @param type
	 * @param line
	 * @param e
	 */
	public InterpreterWarning(InterpreterErrorType type, int line, Throwable e) {
		super(line, e);
		assert(type!=null);
		this.type = type;
	}

	/**
	 * @param type
	 * @param line
	 * @param message
	 */
	public InterpreterWarning(InterpreterErrorType type, int line, String message) {
		super(line, message);
		assert(type!=null);
		this.type = type;
	}

	/**
	 * @param type
	 * @param message
	 * @param e
	 */
	public InterpreterWarning(InterpreterErrorType type, String message, Throwable e) {
		super(message, e);
		assert(type!=null);
		this.type = type;
	}
	
	/** Replies the type of the error.
	 * 
	 * @return the type of the error.
	 */
	public InterpreterErrorType type() {
		return this.type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void print(PrintStream stream) throws IOException {
		ErrorRepository.printWarning(
				stream,
				type().getMessage(),
				line(),
				getMessage(),
				getCause());
	}

}
