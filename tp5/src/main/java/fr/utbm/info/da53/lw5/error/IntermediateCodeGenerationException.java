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
package fr.utbm.info.da53.lw5.error;

import java.io.IOException;
import java.io.PrintStream;

/**
 * Exception in the intermediate code generator.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class IntermediateCodeGenerationException extends LoggableException {

	private static final long serialVersionUID = 3158598777138261206L;
	
	/**
	 * @param line
	 * @param e
	 */
	public IntermediateCodeGenerationException(int line, Throwable e) {
		super(line, e);
	}

	/**
	 * @param line
	 * @param message
	 */
	public IntermediateCodeGenerationException(int line, String message) {
		super(line, message);
	}

	/**
	 * @param line
	 * @param message
	 * @param e
	 */
	public IntermediateCodeGenerationException(int line, String message, Throwable e) {
		super(line, message, e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void print(PrintStream stream) throws IOException {
		ErrorRepository.printError(
				stream,
				"three-address-code generation", //$NON-NLS-1$
				line(),
				getMessage(),
				getCause());
	}
	
}
