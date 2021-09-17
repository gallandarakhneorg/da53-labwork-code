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

/**
 * Exception that is loggable by the compiler.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public abstract class LoggableException extends Exception implements Loggable {

	private static final long serialVersionUID = -6781814841106569735L;
	
	private final int line;
	
	/**
	 * @param e
	 */
	public LoggableException(Throwable e) {
		super(e);
		this.line = -1;
	}

	/**
	 * @param line
	 * @param e
	 */
	public LoggableException(int line, Throwable e) {
		super(e);
		this.line = line;
	}

	/**
	 * @param line
	 * @param message
	 */
	public LoggableException(int line, String message) {
		super(message);
		this.line = line;
	}

	/**
	 * @param message
	 * @param e
	 */
	public LoggableException(String message, Throwable e) {
		super(message, e);
		this.line = -1;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int line() {
		return this.line;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isError() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isWarning() {
		return false;
	}

}
