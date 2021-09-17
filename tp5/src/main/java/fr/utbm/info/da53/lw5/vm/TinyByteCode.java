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
package fr.utbm.info.da53.lw5.vm;

import fr.utbm.info.da53.lw5.threeaddresscode.ThreeAddressInstruction;
import fr.utbm.info.da53.lw5.util.Util;

/**
 * Constants for the tiny byte code.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class TinyByteCode {

	/** Argument 1 is an address.
	 */
	public static final long ARG_1_ADDRESS = 0x100000000l;

	/** Argument 1 is a double constant.
	 */
	public static final long ARG_1_DOUBLE = 0x200000000l;

	/** Argument 1 is a long constant.
	 */
	public static final long ARG_1_LONG = 0x400000000l;

	/** Argument 2 is an address.
	 */
	public static final long ARG_2_ADDRESS = 0x800000000l;

	/** Argument 2 is a double constant.
	 */
	public static final long ARG_2_DOUBLE = 0x1000000000l;

	/** Argument 2 is a long constant.
	 */
	public static final long ARG_2_LONG = 0x2000000000l;

	/** Result is an address.
	 */
	public static final long RESULT_ADDRESS = 0x4000000000l;

	/** Result is a double constant.
	 */
	public static final long RESULT_DOUBLE = 0x8000000000l;

	/** Result is a long constant.
	 */
	public static final long RESULT_LONG = 0x10000000000l;
	
	/** Build the byte code instruction for the given
	 * three-address code instruction.
	 * 
	 * @param instruction
	 * @param arg1
	 * @param arg2
	 * @param result
	 * @return the byte code instruction.
	 */
	public static long buildByteCodeInstruction(
			ThreeAddressInstruction instruction,
			ByteCodeParameterType arg1,
			ByteCodeParameterType arg2,
			ByteCodeParameterType result) {
		long i = instruction.ordinal();
		switch(arg1) {
		case ADDRESS:
			i = i | ARG_1_ADDRESS;
			break;
		case DOUBLE_CONSTANT:
			i = i | ARG_1_DOUBLE;
			break;
		case LONG_CONSTANT:
			i = i | ARG_1_LONG;
			break;
		}
		switch(arg2) {
		case ADDRESS:
			i = i | ARG_2_ADDRESS;
			break;
		case DOUBLE_CONSTANT:
			i = i | ARG_2_DOUBLE;
			break;
		case LONG_CONSTANT:
			i = i | ARG_2_LONG;
			break;
		}
		switch(result) {
		case ADDRESS:
			i = i | RESULT_ADDRESS;
			break;
		case DOUBLE_CONSTANT:
			i = i | RESULT_DOUBLE;
			break;
		case LONG_CONSTANT:
			i = i | RESULT_LONG;
			break;
		}
		return i;
	}

	/** Extract the instruction code from the byte code record.
	 * 
	 * @param recordInstruction
	 * @return the byte code instruction.
	 */
	public static long parseByteCodeInstruction(long recordInstruction) {
		return recordInstruction & 0xFFFFFFFFl;
	}

	/** Extract the first argument from the byte code record.
	 *
	 * @param byteCode
	 * @param adr
	 * @param recordInstruction
	 * @return the argument.
	 */
	public static Number parseByteCodeArgument1(byte[] byteCode, int adr, long recordInstruction) {
		if ((recordInstruction & TinyByteCode.ARG_1_LONG)!=0)
			return Util.getLong(byteCode, adr, 1);
		if ((recordInstruction & TinyByteCode.ARG_1_DOUBLE)!=0)
			return Util.getDouble(byteCode, adr, 1);
		return new Address(Util.getLong(byteCode, adr, 1));
	}

	/** Extract the second argument from the byte code record.
	 *
	 * @param byteCode
	 * @param adr
	 * @param recordInstruction
	 * @return the argument.
	 */
	public static Number parseByteCodeArgument2(byte[] byteCode, int adr, long recordInstruction) {
		if ((recordInstruction & TinyByteCode.ARG_2_LONG)!=0)
			return Util.getLong(byteCode, adr, 2);
		if ((recordInstruction & TinyByteCode.ARG_2_DOUBLE)!=0)
			return Util.getDouble(byteCode, adr, 2);
		return new Address(Util.getLong(byteCode, adr, 2));
	}

	/** Extract the result argument from the byte code record.
	 *
	 * @param byteCode
	 * @param adr
	 * @param recordInstruction
	 * @return the result.
	 */
	public static Number parseByteCodeResult(byte[] byteCode, int adr, long recordInstruction) {
		if ((recordInstruction & TinyByteCode.RESULT_LONG)!=0)
			return Util.getLong(byteCode, adr, 3);
		if ((recordInstruction & TinyByteCode.RESULT_DOUBLE)!=0)
			return Util.getDouble(byteCode, adr, 3);
		return new Address(Util.getLong(byteCode, adr, 3));
	}

	/**
	 * Constants for the tiny byte code.
	 * 
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $Name$ $Revision$ $Date$
	 */
	public enum ByteCodeParameterType {
		
		/** Double constant.
		 */
		DOUBLE_CONSTANT,
		
		/** Long constant.
		 */
		LONG_CONSTANT,
		
		/** Address.
		 */
		ADDRESS;

	}
	
}