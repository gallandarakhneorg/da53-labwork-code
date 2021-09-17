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

import javax.swing.filechooser.FileFilter;

import fr.utbm.info.da53.lw1.util.LocaleUtil;

/** File filter for TinyBasic source programs.
 * 
 * @author Jonathan DEMANGE &lt;jonathan.demange@utbm.fr&gt;
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class TinyBasicFileFilter extends FileFilter {

	/** File extension for TinyBasic source programs.
	 */
	public static final String EXTENSION = ".tb"; //$NON-NLS-1$
	
	@Override
	public boolean accept(File file){
		return file.isDirectory() || (file.getName().toLowerCase().endsWith(EXTENSION));
	}
	
	@Override
	public String getDescription(){
		return LocaleUtil.getString(TinyBasicFileFilter.class, "NAME", EXTENSION); //$NON-NLS-1$
	}
	
}
