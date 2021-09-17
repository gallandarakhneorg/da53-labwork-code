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

import java.io.Serializable;

/**
 * Warning that is loggable by the compiler.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public abstract class Warning implements Loggable, Serializable {

	private static final long serialVersionUID = 3635161025422394080L;
	
	private final String message;
	private final Throwable cause;
	private final int line;
	
	/**
	 * @param e
	 */
	public Warning(Throwable e) {
		this.cause = e;
		if (this.cause!=null) {
			this.message = this.cause.getMessage();
		}
		else {
			this.message = null;
		}
		this.line = -1;
	}

	/**
	 * @param line
	 * @param e
	 */
	public Warning(int line, Throwable e) {
		this.cause = e;
		if (this.cause!=null) {
			this.message = this.cause.getMessage();
		}
		else {
			this.message = null;
		}
		this.line = line;
	}

	/**
	 * @param line
	 * @param message
	 */
	public Warning(int line, String message) {
		this.cause = null;
		this.message = message;
		this.line = line;
	}

	/**
	 * @param message
	 * @param e
	 */
	public Warning(String message, Throwable e) {
		this.cause = e;
		if (this.cause!=null) {
			this.message = this.cause.getMessage();
		}
		else {
			this.message = null;
		}
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
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isWarning() {
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Throwable getCause() {
		return this.cause;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMessage() {
		return this.message;
	}

}
