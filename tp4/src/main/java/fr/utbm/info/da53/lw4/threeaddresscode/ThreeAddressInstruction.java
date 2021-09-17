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
package fr.utbm.info.da53.lw4.threeaddresscode;

/**
 * List of the instructions supported by the
 * three-address code.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public enum ThreeAddressInstruction {

	/** Stop any interpreter.
	 */
	EXIT {
		@Override
		public String toString(String arg1, String arg2, String result) {
			return displayQuadruple(name(), arg1, null, null);
		}
	},

	/** a = b
	 */
	SET {
		@Override
		public String toString(String arg1, String arg2, String result) {
			return displayQuadruple(name(), arg1, null, result);
		}
	},
	
	/** a = b + c
	 */
	ADDITION {
		@Override
		public String toString(String arg1, String arg2, String result) {
			return displayQuadruple(name(), arg1, arg2, result);
		}
	},
	
	/** a = b - c
	 */
	SUBSTRACTION {
		@Override
		public String toString(String arg1, String arg2, String result) {
			return displayQuadruple(name(), arg1, arg2, result);
		}
	},

	/** a = b * c
	 */
	MULTIPLICATION {
		@Override
		public String toString(String arg1, String arg2, String result) {
			return displayQuadruple(name(), arg1, arg2, result);
		}
	},

	/** a = b / c
	 */
	DIVISION {
		@Override
		public String toString(String arg1, String arg2, String result) {
			return displayQuadruple(name(), arg1, arg2, result);
		}
	},

	/** a = - b
	 */
	MINUS {
		@Override
		public String toString(String arg1, String arg2, String result) {
			return displayQuadruple(name(), arg1, null, result);
		}
	},

	/** a = b = c
	 */
	BOOLEAN_EQUAL {
		@Override
		public String toString(String arg1, String arg2, String result) {
			return displayQuadruple(name(), arg1, arg2, result);
		}
	},

	/** a = b != c
	 */
	BOOLEAN_DIFF {
		@Override
		public String toString(String arg1, String arg2, String result) {
			return displayQuadruple(name(), arg1, arg2, result);
		}
	},

	/** a = b &lt; c
	 */
	BOOLEAN_LESS {
		@Override
		public String toString(String arg1, String arg2, String result) {
			return displayQuadruple(name(), arg1, arg2, result);
		}
	},

	/** a = b &gt; c
	 */
	BOOLEAN_GREATER {
		@Override
		public String toString(String arg1, String arg2, String result) {
			return displayQuadruple(name(), arg1, arg2, result);
		}
	},

	/** a = b &lt;= c
	 */
	BOOLEAN_LESS_EQUAL {
		@Override
		public String toString(String arg1, String arg2, String result) {
			return displayQuadruple(name(), arg1, arg2, result);
		}
	},

	/** a = b &gt;= c
	 */
	BOOLEAN_GREATER_EQUAL {
		@Override
		public String toString(String arg1, String arg2, String result) {
			return displayQuadruple(name(), arg1, arg2, result);
		}
	},

	/** a = ! b
	 */
	BOOLEAN_NOT {
		@Override
		public String toString(String arg1, String arg2, String result) {
			return displayQuadruple(name(), arg1, null, result);
		}
	},

	/** a = b and c
	 */
	BOOLEAN_AND {
		@Override
		public String toString(String arg1, String arg2, String result) {
			return displayQuadruple(name(), arg1, arg2, result);
		}
	},

	/** a = b or c
	 */
	BOOLEAN_OR {
		@Override
		public String toString(String arg1, String arg2, String result) {
			return displayQuadruple(name(), arg1, arg2, result);
		}
	},

	/** a = b xor c
	 */
	BOOLEAN_XOR {
		@Override
		public String toString(String arg1, String arg2, String result) {
			return displayQuadruple(name(), arg1, arg2, result);
		}
	},
	
	/** Jump to adr.
	 */
	JUMP {
		@Override
		public String toString(String arg1, String arg2, String result) {
			return displayQuadruple(name(), arg1, null, null);
		}
	},
	
	/** Jump to adr if a is true.
	 */
	JUMP_IF_TRUE {
		@Override
		public String toString(String arg1, String arg2, String result) {
			return displayQuadruple(name(), arg1, arg2, null);
		}
	},

	/** Jump to adr if a is false.
	 */
	JUMP_IF_FALSE {
		@Override
		public String toString(String arg1, String arg2, String result) {
			return displayQuadruple(name(), arg1, arg2, null);
		}
	},

	/** Procedure call parameter.
	 */
	CALL_PARAMETER {
		@Override
		public String toString(String arg1, String arg2, String result) {
			return displayQuadruple(name(), arg1, null, null);
		}
	},

	/** Procedure call.
	 */
	CALL {
		@Override
		public String toString(String arg1, String arg2, String result) {
			return displayQuadruple(name(), arg1, arg2, result);
		}
	},

	/** Replies the formal parameter of a function.
	 */
	FORMAL_PARAMETER {
		@Override
		public String toString(String arg1, String arg2, String result) {
			return displayQuadruple(name(), arg1, null, result);
		}
	},

	/** Procedure return.
	 */
	RETURN {
		@Override
		public String toString(String arg1, String arg2, String result) {
			return displayQuadruple(name(), arg1, null, null);
		}
	},
	
	/** Read a value from the standard input.
	 */
	READ {
		@Override
		public String toString(String arg1, String arg2, String result) {
			return displayQuadruple(name(), null, null, result);
		}
	},

	/** Print a value on the standard input.
	 */
	PRINT {
		@Override
		public String toString(String arg1, String arg2, String result) {
			return displayQuadruple(name(), arg1, null, null);
		}
	},
	
	/** Print an error message and exit.
	 */
	ERROR {
		@Override
		public String toString(String arg1, String arg2, String result) {
			return displayQuadruple(name(), arg1, null, null);
		}
	},
	
	/** a = b[c]
	 */
	ARRAY_GET {
		@Override
		public String toString(String arg1, String arg2, String result) {
			return displayQuadruple(name(), arg1, arg2, result);
		}
	},
	
	/** a[b] = c
	 */
	ARRAY_SET {
		@Override
		public String toString(String arg1, String arg2, String result) {
			return displayQuadruple(name(), arg1, arg2, result);
		}
	};

	/** Replies a string representation of this instruction.
	 * 
	 * @param arg1
	 * @param arg2
	 * @param result
	 * @return the string representation.
	 */
	public abstract String toString(String arg1, String arg2, String result);
	
	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #ADDITION} instruction.
	 * 
	 * @param result is the variable that receive the addition result.
	 * @param left is the variable that contains the left operand value.
	 * @param right is the variable that contains the right operand value.
	 * @return the record.
	 */
	public static ThreeAddressRecord addition(String result, String left, String right) {
		return new ThreeAddressRecord(ADDITION, left, right, result);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #SUBSTRACTION} instruction.
	 * 
	 * @param result is the variable that receive the substraction result.
	 * @param left is the variable that contains the left operand value.
	 * @param right is the variable that contains the right operand value.
	 * @return the record.
	 */
	public static ThreeAddressRecord substraction(String result, String left, String right) {
		return new ThreeAddressRecord(SUBSTRACTION, left, right, result);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #MULTIPLICATION} instruction.
	 * 
	 * @param result is the variable that receive the multiplication result.
	 * @param left is the variable that contains the left operand value.
	 * @param right is the variable that contains the right operand value.
	 * @return the record.
	 */
	public static ThreeAddressRecord multiplication(String result, String left, String right) {
		return new ThreeAddressRecord(MULTIPLICATION, left, right, result);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #DIVISION} instruction.
	 * 
	 * @param result is the variable that receive the division result.
	 * @param left is the variable that contains the left operand value.
	 * @param right is the variable that contains the right operand value.
	 * @return the record.
	 */
	public static ThreeAddressRecord division(String result, String left, String right) {
		return new ThreeAddressRecord(DIVISION, left, right, result);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #SET} instruction.
	 * 
	 * @param variable is the name of the variable to set.
	 * @param value is the name of the variable that contains the value.
	 * @return the record.
	 */
	public static ThreeAddressRecord set(String variable, String value) {
		return new ThreeAddressRecord(SET, value, null, variable);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #EXIT} instruction.
	 * 
	 * @return the record.
	 */
	public static ThreeAddressRecord exit() {
		return new ThreeAddressRecord(EXIT);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #JUMP_IF_FALSE} instruction.
	 * 
	 * @param value is the value to test.
	 * @param label is the label to go to.
	 * @return the record.
	 */
	public static ThreeAddressRecord jumpIfFalse(String value, String label) {
		return new ThreeAddressRecord(JUMP_IF_FALSE, label, value, null);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #JUMP_IF_TRUE} instruction.
	 * 
	 * @param value is the value to test.
	 * @param label is the label to go to.
	 * @return the record.
	 */
	public static ThreeAddressRecord jumpIfTrue(String value, String label) {
		return new ThreeAddressRecord(JUMP_IF_TRUE, label, value, null);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #JUMP} instruction.
	 * 
	 * @param label is the label to go to.
	 * @return the record.
	 */
	public static ThreeAddressRecord jump(String label) {
		return new ThreeAddressRecord(JUMP, label, null, null);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #BOOLEAN_LESS} instruction.
	 * 
	 * @param result is the name of the variable that contains the comparison result.
	 * @param left is the left operand of the operator.
	 * @param right is the right operand of the operator.
	 * @return the record.
	 */
	public static ThreeAddressRecord lt(String result, String left, String right) {
		return new ThreeAddressRecord(BOOLEAN_LESS, left, right, result);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #BOOLEAN_LESS_EQUAL} instruction.
	 * 
	 * @param result is the name of the variable that contains the comparison result.
	 * @param left is the left operand of the operator.
	 * @param right is the right operand of the operator.
	 * @return the record.
	 */
	public static ThreeAddressRecord le(String result, String left, String right) {
		return new ThreeAddressRecord(BOOLEAN_LESS_EQUAL, left, right, result);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #BOOLEAN_GREATER} instruction.
	 * 
	 * @param result is the name of the variable that contains the comparison result.
	 * @param left is the left operand of the operator.
	 * @param right is the right operand of the operator.
	 * @return the record.
	 */
	public static ThreeAddressRecord gt(String result, String left, String right) {
		return new ThreeAddressRecord(BOOLEAN_GREATER, left, right, result);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #BOOLEAN_GREATER_EQUAL} instruction.
	 * 
	 * @param result is the name of the variable that contains the comparison result.
	 * @param left is the left operand of the operator.
	 * @param right is the right operand of the operator.
	 * @return the record.
	 */
	public static ThreeAddressRecord ge(String result, String left, String right) {
		return new ThreeAddressRecord(BOOLEAN_GREATER_EQUAL, left, right, result);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #BOOLEAN_EQUAL} instruction.
	 * 
	 * @param result is the name of the variable that contains the comparison result.
	 * @param left is the left operand of the operator.
	 * @param right is the right operand of the operator.
	 * @return the record.
	 */
	public static ThreeAddressRecord eq(String result, String left, String right) {
		return new ThreeAddressRecord(BOOLEAN_EQUAL, left, right, result);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #BOOLEAN_DIFF} instruction.
	 * 
	 * @param result is the name of the variable that contains the comparison result.
	 * @param left is the left operand of the operator.
	 * @param right is the right operand of the operator.
	 * @return the record.
	 */
	public static ThreeAddressRecord ne(String result, String left, String right) {
		return new ThreeAddressRecord(BOOLEAN_DIFF, left, right, result);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #PRINT} instruction.
	 * 
	 * @param value is the value to print on the standard output.
	 * @return the record.
	 */
	public static ThreeAddressRecord print(String value) {
		return new ThreeAddressRecord(PRINT, value, null, null);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #ERROR} instruction.
	 * 
	 * @param errorMessage is the error message to print.
	 * @return the record.
	 */
	public static ThreeAddressRecord error(String errorMessage) {
		return new ThreeAddressRecord(ERROR, errorMessage, null, null);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #READ} instruction.
	 * 
	 * @param variable is the name of the variable to read from the standard input.
	 * @return the record.
	 */
	public static ThreeAddressRecord read(String variable) {
		return new ThreeAddressRecord(READ, null, null, variable);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #CALL} instruction.
	 * 
	 * @param label is the label of the function.
	 * @param nbParameters is the number of parameters to pass to the sub-function.
	 * @return the record.
	 */
	public static ThreeAddressRecord procedure(String label, int nbParameters) {
		return new ThreeAddressRecord(CALL, label, Integer.toString(nbParameters), null);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #CALL} instruction.
	 * 
	 * @param label is the label of the function.
	 * @param nbParameters is the number of parameters to pass to the sub-function.
	 * @param result is the name of the varaible that may contains the result.
	 * @return the record.
	 */
	public static ThreeAddressRecord function(String label, int nbParameters, String result) {
		return new ThreeAddressRecord(CALL, label, Integer.toString(nbParameters), result);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #CALL_PARAMETER} instruction.
	 * 
	 * @param variable is the name of the variable to pass to the sub-program.
	 * @return the record.
	 */
	public static ThreeAddressRecord param(String variable) {
		return new ThreeAddressRecord(CALL_PARAMETER, variable, null, null);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #FORMAL_PARAMETER} instruction.
	 * 
	 * @param variable is the name of the variable to retreive the value of the format
	 * parameter.
	 * @param formalParameterNumber is the number of the formal parameter to retreive.
	 * @return the record.
	 */
	public static ThreeAddressRecord formalParam(String variable, int formalParameterNumber) {
		return new ThreeAddressRecord(FORMAL_PARAMETER, Integer.toString(formalParameterNumber), null, variable);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #RETURN} instruction.
	 * 
	 * @return the record.
	 */
	public static ThreeAddressRecord returnProcedure() {
		return new ThreeAddressRecord(RETURN, null, null, null);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #RETURN} instruction.
	 * 
	 * @param value is the value to return to the caller.
	 * @return the record.
	 */
	public static ThreeAddressRecord returnFunction(String value) {
		return new ThreeAddressRecord(RETURN, value, null, null);
	}
	
	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #ARRAY_GET} instruction.
	 * 
	 * @param elementValue is the value of the element to retreive from the array.
	 * @param array is the array.
	 * @param index is the position of the element to reply.
	 * @return the record.
	 */
	public static ThreeAddressRecord getArray(String elementValue, String array, String index) {
		return new ThreeAddressRecord(ARRAY_GET, array, index, elementValue);
	}
	
	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #ARRAY_SET} instruction.
	 * 
	 * @param array is the array.
	 * @param index is the position of the element to set.
	 * @param elementValue is the value of the element to put into the array.
	 * @return the record.
	 */
	public static ThreeAddressRecord setArray(String array, String index, String elementValue) {
		return new ThreeAddressRecord(ARRAY_SET, index, elementValue, array);
	}

	private static final int COLUMN_SIZE = 20;
	
	/** Display a quadruple.
	 * 
	 * @param inst
	 * @param a1
	 * @param a2
	 * @param r
	 * @return the string representation as quadruple.
	 */
	public static String displayQuadruple(String inst, String a1, String a2, String r) {
		return	displayColumnElement(inst)
				+" | " //$NON-NLS-1$
				+displayColumnElement(a1)
				+" | " //$NON-NLS-1$
				+displayColumnElement(a2)
				+" | " //$NON-NLS-1$
				+displayColumnElement(r);
	}

	private static String displayColumnElement(String v) {
		StringBuilder b = new StringBuilder();
		if (v!=null) b.append(v);
		while (b.length()<COLUMN_SIZE) {
			b.append(" "); //$NON-NLS-1$
		}
		return b.toString();
	}

}