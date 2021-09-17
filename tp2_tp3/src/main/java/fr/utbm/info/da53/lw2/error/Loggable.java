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
 * Exception that is loggable by the compiler.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public interface Loggable {

	/** Replies the line at which the error occurs.
	 * 
	 * @return the line of the error; or {@code -1} if unknown.
	 */
	public int line();

	/** Replies if this object is an error.
	 * 
	 * @return <code>true</code> if it is an error; <code>false</code> if it is a warning.
	 */
	public boolean isError();

	/** Replies if this object is a warning.
	 * 
	 * @return <code>true</code> if it is a warning; <code>false</code> if it is an error.
	 */
	public boolean isWarning();
	
	/** Replies the message.
	 * 
	 * @return the message.
	 */
	public String getMessage();
	
	/** Replies the cause of the message.
	 * 
	 * @return the cause of the message.
	 */
	public Throwable getCause();
	
	/** Print the message in the given stream.
	 * 
	 * @param stream
	 * @throws IOException
	 */
	public void print(PrintStream stream) throws IOException;

}
