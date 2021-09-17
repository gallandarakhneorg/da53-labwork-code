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
package fr.utbm.info.da53.lw5.threeaddresscode;

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
		public String toString(Address arg1, Address arg2, Address result) {
			if (arg1!=null)
				return "EXIT "+arg1; //$NON-NLS-1$
			return "EXIT"; //$NON-NLS-1$
		}
	},
	
	/** a = b
	 */
	SET {
		@Override
		public String toString(Address arg1, Address arg2, Address result) {
			return result+" = "+arg1; //$NON-NLS-1$
		}
	},
	
	/** a = b + c
	 */
	ADDITION {
		@Override
		public String toString(Address arg1, Address arg2, Address result) {
			return result+" = ("+arg1+" + "+arg2+")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	},
	
	/** a = b - c
	 */
	SUBSTRACTION {
		@Override
		public String toString(Address arg1, Address arg2, Address result) {
			return result+" = ("+arg1+" - "+arg2+")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	},

	/** a = b * c
	 */
	MULTIPLICATION {
		@Override
		public String toString(Address arg1, Address arg2, Address result) {
			return result+" = ("+arg1+" * "+arg2+")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	},

	/** a = b / c
	 */
	DIVISION {
		@Override
		public String toString(Address arg1, Address arg2, Address result) {
			return result+" = ("+arg1+" / "+arg2+")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	},

	/** a = - b
	 */
	MINUS {
		@Override
		public String toString(Address arg1, Address arg2, Address result) {
			return result+" = -"+arg1; //$NON-NLS-1$
		}
	},

	/** a = b = c
	 */
	BOOLEAN_EQUAL {
		@Override
		public String toString(Address arg1, Address arg2, Address result) {
			return result+" = ("+arg1+" == "+arg2+")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	},

	/** a = b != c
	 */
	BOOLEAN_DIFF {
		@Override
		public String toString(Address arg1, Address arg2, Address result) {
			return result+" = ("+arg1+" != "+arg2+")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	},

	/** a = b &lt; c
	 */
	BOOLEAN_LESS {
		@Override
		public String toString(Address arg1, Address arg2, Address result) {
			return result+" = ("+arg1+" << "+arg2+")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	},

	/** a = b &gt; c
	 */
	BOOLEAN_GREATER {
		@Override
		public String toString(Address arg1, Address arg2, Address result) {
			return result+" = ("+arg1+" >> "+arg2+")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	},

	/** a = b &lt;= c
	 */
	BOOLEAN_LESS_EQUAL {
		@Override
		public String toString(Address arg1, Address arg2, Address result) {
			return result+" = ("+arg1+" <= "+arg2+")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	},

	/** a = b &gt;= c
	 */
	BOOLEAN_GREATER_EQUAL {
		@Override
		public String toString(Address arg1, Address arg2, Address result) {
			return result+" = ("+arg1+" >= "+arg2+")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	},

	/** a = ! b
	 */
	BOOLEAN_NOT {
		@Override
		public String toString(Address arg1, Address arg2, Address result) {
			return result+" = ! "+arg1; //$NON-NLS-1$
		}
	},

	/** a = b and c
	 */
	BOOLEAN_AND {
		@Override
		public String toString(Address arg1, Address arg2, Address result) {
			return result+" = ("+arg1+" && "+arg2+")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	},

	/** a = b or c
	 */
	BOOLEAN_OR {
		@Override
		public String toString(Address arg1, Address arg2, Address result) {
			return result+" = ("+arg1+" || "+arg2+")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	},

	/** a = b xor c
	 */
	BOOLEAN_XOR {
		@Override
		public String toString(Address arg1, Address arg2, Address result) {
			return result+" = ("+arg1+" ^ "+arg2+")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	},
	
	/** Jump to adr.
	 */
	JUMP {
		@Override
		public String toString(Address arg1, Address arg2, Address result) {
			return "JMP "+arg1; //$NON-NLS-1$
		}
	},
	
	/** Jump to adr if a is true.
	 */
	JUMP_IF_TRUE {
		@Override
		public String toString(Address arg1, Address arg2, Address result) {
			return "JMP "+arg1+" IF "+arg2; //$NON-NLS-1$ //$NON-NLS-2$
		}
	},

	/** Jump to adr if a is false.
	 */
	JUMP_IF_FALSE {
		@Override
		public String toString(Address arg1, Address arg2, Address result) {
			return "JMP "+arg1+" IF NOT "+arg2; //$NON-NLS-1$ //$NON-NLS-2$
		}
	},

	/** Procedure call parameter.
	 */
	CALL_PARAMETER {
		@Override
		public String toString(Address arg1, Address arg2, Address result) {
			return "PARAM "+arg1; //$NON-NLS-1$
		}
	},

	/** Replies the formal parameter of a function.
	 */
	FORMAL_PARAMETER {
		@Override
		public String toString(Address arg1, Address arg2, Address result) {
			return result+" = PARAM "+arg1; //$NON-NLS-1$
		}
	},

	/** Procedure call.
	 */
	CALL {
		@Override
		public String toString(Address arg1, Address arg2, Address result) {
			if (result!=null) {
				return result+" = CALL "+arg1+", "+arg2; //$NON-NLS-1$ //$NON-NLS-2$
			}
			return "CALL "+arg1+", "+arg2; //$NON-NLS-1$ //$NON-NLS-2$
		}
	},

	/** Procedure return.
	 */
	RETURN {
		@Override
		public String toString(Address arg1, Address arg2, Address result) {
			if (arg1!=null)
				return "RETURN "+arg1; //$NON-NLS-1$
			return "RETURN"; //$NON-NLS-1$
		}
	},
	
	/** Read a value from the standard input.
	 */
	READ {
		@Override
		public String toString(Address arg1, Address arg2, Address result) {
			return result+" = READ"; //$NON-NLS-1$
		}
	},

	/** Print a value on the standard input.
	 */
	PRINT {
		@Override
		public String toString(Address arg1, Address arg2, Address result) {
			return "PRINT "+arg1; //$NON-NLS-1$
		}
	},
	
	/** a = b[c]
	 */
	ARRAY_GET {
		@Override
		public String toString(Address arg1, Address arg2, Address result) {
			return result+" = "+arg1+"("+arg2+")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	},
	
	/** a[b] = c
	 */
	ARRAY_SET {
		@Override
		public String toString(Address arg1, Address arg2, Address result) {
			return result+"("+arg1+") = "+arg2; //$NON-NLS-1$ //$NON-NLS-2$
		}
	};

	/** Replies a string representation of this instruction.
	 * 
	 * @param arg1
	 * @param arg2
	 * @param result
	 * @return the string representation.
	 */
	public abstract String toString(Address arg1, Address arg2, Address result);
	
	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #ADDITION} instruction.
	 * 
	 * @param result is the variable that receive the addition result.
	 * @param left is the variable that contains the left operand value.
	 * @param right is the variable that contains the right operand value.
	 * @return the record.
	 */
	public static ThreeAddressRecord addition(Address result, Address left, Address right) {
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
	public static ThreeAddressRecord substraction(Address result, Address left, Address right) {
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
	public static ThreeAddressRecord multiplication(Address result, Address left, Address right) {
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
	public static ThreeAddressRecord division(Address result, Address left, Address right) {
		return new ThreeAddressRecord(DIVISION, left, right, result);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #SET} instruction.
	 * 
	 * @param variable is the name of the variable to set.
	 * @param value is the name of the variable that contains the value.
	 * @return the record.
	 */
	public static ThreeAddressRecord set(Address variable, Address value) {
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
	 * {@link #EXIT} instruction.
	 * 
	 * @param exitCode
	 * @return the record.
	 */
	public static ThreeAddressRecord exit(Address exitCode) {
		return new ThreeAddressRecord(EXIT, exitCode, null, null);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #JUMP_IF_FALSE} instruction.
	 * 
	 * @param value is the value to test.
	 * @param adr is the address to go to.
	 * @return the record.
	 */
	public static ThreeAddressRecord jumpIfFalse(Address value, Address adr) {
		return new ThreeAddressRecord(JUMP_IF_FALSE, adr, value, null);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #JUMP_IF_TRUE} instruction.
	 * 
	 * @param value is the value to test.
	 * @param adr is the address to go to.
	 * @return the record.
	 */
	public static ThreeAddressRecord jumpIfTrue(Address value, Address adr) {
		return new ThreeAddressRecord(JUMP_IF_TRUE, adr, value, null);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #JUMP} instruction.
	 * 
	 * @param adr is the address to go to.
	 * @return the record.
	 */
	public static ThreeAddressRecord jump(Address adr) {
		return new ThreeAddressRecord(JUMP, adr, null, null);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #BOOLEAN_LESS} instruction.
	 * 
	 * @param result is the name of the variable that contains the comparison result.
	 * @param left is the left operand of the operator.
	 * @param right is the right operand of the operator.
	 * @return the record.
	 */
	public static ThreeAddressRecord lt(Address result, Address left, Address right) {
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
	public static ThreeAddressRecord le(Address result, Address left, Address right) {
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
	public static ThreeAddressRecord gt(Address result, Address left, Address right) {
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
	public static ThreeAddressRecord ge(Address result, Address left, Address right) {
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
	public static ThreeAddressRecord eq(Address result, Address left, Address right) {
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
	public static ThreeAddressRecord ne(Address result, Address left, Address right) {
		return new ThreeAddressRecord(BOOLEAN_DIFF, left, right, result);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #PRINT} instruction.
	 * 
	 * @param value is the value to print on the standard output.
	 * @return the record.
	 */
	public static ThreeAddressRecord print(Address value) {
		return new ThreeAddressRecord(PRINT, value, null, null);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #READ} instruction.
	 * 
	 * @param variable is the name of the variable to read from the standard input.
	 * @return the record.
	 */
	public static ThreeAddressRecord read(Address variable) {
		return new ThreeAddressRecord(READ, null, null, variable);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #CALL} instruction.
	 * 
	 * @param adr is the address of the function.
	 * @param nbParameters is the number of parameters to pass to the sub-function.
	 * @return the record.
	 */
	public static ThreeAddressRecord procedure(Address adr, int nbParameters) {
		return new ThreeAddressRecord(CALL, adr, new Address(nbParameters), null);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #CALL} instruction.
	 * 
	 * @param adr is the address of the function.
	 * @param nbParameters is the number of parameters to pass to the sub-function.
	 * @param result is the name of the varaible that may contains the result.
	 * @return the record.
	 */
	public static ThreeAddressRecord function(Address adr, int nbParameters, Address result) {
		return new ThreeAddressRecord(CALL, adr, new Address(nbParameters), result);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #CALL_PARAMETER} instruction.
	 * 
	 * @param variable is the name of the variable to pass to the sub-program.
	 * @return the record.
	 */
	public static ThreeAddressRecord param(Address variable) {
		return new ThreeAddressRecord(CALL_PARAMETER, variable, null, null);
	}

	/** Create a {@link ThreeAddressRecord} for the 
	 * {@link #FORMAL_PARAMETER} instruction.
	 * 
	 * @param result is the name of the variable that may contains the value of the formal parameter.
	 * @param variable is the name of the variable to pass to the sub-program.
	 * @return the record.
	 */
	public static ThreeAddressRecord param(Address result, Address variable) {
		return new ThreeAddressRecord(FORMAL_PARAMETER, variable, null, result);
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
	public static ThreeAddressRecord returnFunction(Address value) {
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
	public static ThreeAddressRecord getArray(Address elementValue, Address array, Address index) {
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
	public static ThreeAddressRecord setArray(Address array, Address index, Address elementValue) {
		return new ThreeAddressRecord(ARRAY_SET, index, elementValue, array);
	}

}
