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
package fr.utbm.info.da53.lw4.interpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import fr.utbm.info.da53.lw4.threeaddresscode.ThreeAddressCode;
import fr.utbm.info.da53.lw4.threeaddresscode.ThreeAddressRecord;
import fr.utbm.info.da53.lw4.util.Util;

/**
 * Interpreter of three-address code.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class ThreeAddressCodeInterpreter {

	private final ThreeAddressCode code;
	
	/**
	 * @param code is the code to interprete.
	 */
	public ThreeAddressCodeInterpreter(ThreeAddressCode code) {
		this.code = code;
	}
	
	/** Run the interpreter.
	 */
	public void run() {
		ExecutionContext rootContext = new ExecutionContext();
		boolean stop = false;
		while (!stop) {
			ThreeAddressRecord record = rootContext.getRecord(this.code);
			if (record!=null) {
				stop = run(record, rootContext);
			}
			else {
				stop = true;
			}
		}
	}
	
	private boolean run(ThreeAddressRecord record, ExecutionContext context) {
		switch(record.instruction()) {
		case ADDITION:
			add(record, context);
			break;
		case SUBSTRACTION:
			substract(record, context);
			break;
		case MULTIPLICATION:
			multiply(record, context);
			break;
		case DIVISION:
			divide(record, context);
			break;
		case MINUS:
			minus(record, context);
			break;
		case BOOLEAN_AND:
			and(record, context);
			break;
		case BOOLEAN_OR:
			or(record, context);
			break;
		case BOOLEAN_XOR:
			xor(record, context);
			break;
		case BOOLEAN_NOT:
			not(record, context);
			break;
		case BOOLEAN_EQUAL:
			equal(record, context);
			break;
		case BOOLEAN_DIFF:
			diff(record, context);
			break;
		case BOOLEAN_GREATER:
			gt(record, context);
			break;
		case BOOLEAN_GREATER_EQUAL:
			ge(record, context);
			break;
		case BOOLEAN_LESS:
			lt(record, context);
			break;
		case BOOLEAN_LESS_EQUAL:
			le(record, context);
			break;
		case EXIT:
			return true;
		case JUMP:
			jmp(record, context);
			break;
		case JUMP_IF_FALSE:
			jmpf(record, context);
			break;
		case JUMP_IF_TRUE:
			jmpt(record, context);
			break;
		case SET:
			set(record, context);
			break;
		case PRINT:
			print(record, context);
			break;
		case ERROR:
			error(record, context);
			break;
		case READ:
			read(record, context);
			break;
		case CALL_PARAMETER:
			param(record, context);
			break;
		case CALL:
			callFunction(record, context);
			break;
		case FORMAL_PARAMETER:
			formalParameter(record, context);
			break;
		case RETURN:
			returnFunction(record, context);
			break;
		case ARRAY_GET:
			getArray(record, context);
			break;
		case ARRAY_SET:
			setArray(record, context);
			break;
		}
		return false;
	}
	
	private void setArray(ThreeAddressRecord record, ExecutionContext context) {
		String index = record.getArgument1();
		String value = record.getArgument2();
		String array = record.getResult();
		
		value = context.getValue(value);
		index = context.getValue(index);
		
		context.setArrayElement(array, index, value);
		context.moveOrdinalCounter();
	}

	private void getArray(ThreeAddressRecord record, ExecutionContext context) {
		String array = record.getArgument1();
		String index = record.getArgument2();
		String result = record.getResult();

		index = context.getValue(index);
		String value = context.getArrayElement(array, index);

		context.setValue(result, value);
		context.moveOrdinalCounter();
	}

	private void returnFunction(ThreeAddressRecord record, ExecutionContext context) {
		context.returnFunction(record.getArgument1());
	}

	private void formalParameter(ThreeAddressRecord record, ExecutionContext context) {
		String n = record.getArgument1();
		if (!Util.isInteger(n))
			throw new RuntimeException("the parameter of the formal_parameter must be an integer."); //$NON-NLS-1$
		int number = Integer.parseInt(n);
		
		String result = record.getResult();
		
		String value = context.getFormalParameter(number);
		
		context.setValue(result, value);
		context.moveOrdinalCounter();
	}

	private void callFunction(ThreeAddressRecord record, ExecutionContext context) {
		String adr = record.getArgument1();
		int position = context.getInstructionPosition(adr, this.code);

		String n = record.getArgument2();
		n = context.getValue(n);
		if (!Util.isInteger(n))
			throw new RuntimeException("the second parameter of CALL must be an integer"); //$NON-NLS-1$
		int nbParams = Integer.parseInt(n);
		
		String result = record.getResult();
		
		context.callFunction(position, nbParams, result);
	}

	private void param(ThreeAddressRecord record, ExecutionContext context) {
		String value = record.getArgument1();
		value = context.getValue(value);
		context.addParameter(value);
		context.moveOrdinalCounter();
	}

	private void read(ThreeAddressRecord record, ExecutionContext context) {
		String variable = record.getResult();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line;
		try {
			line = reader.readLine();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		if (!Util.isNumber(line) && !Util.isBoolean(line))
			line = Util.stringify(line);
		context.setValue(variable, line);
		context.moveOrdinalCounter();
	}

	private void print(ThreeAddressRecord record, ExecutionContext context) {
		String value = record.getArgument1();
		value = context.getValue(value);
		if (Util.isString(value)) {
			System.out.print(Util.unstringify(value));
		}
		else {
			System.out.print(value);
		}
		context.moveOrdinalCounter();
	}

	private void error(ThreeAddressRecord record, ExecutionContext context) {
		String value = record.getArgument1();
		value = context.getValue(value);
		if (Util.isString(value)) {
			System.err.println(Util.unstringify(value));
		}
		else {
			System.err.println(value);
		}
		System.exit(1);
	}

	private void set(ThreeAddressRecord record, ExecutionContext context) {
		String value = record.getArgument1();
		String result = record.getResult();
		value = context.getValue(value);
		context.setValue(result, value);
		context.moveOrdinalCounter();
	}

	private void jmp(ThreeAddressRecord record, ExecutionContext context) {
		String address = record.getArgument1();
		int position = context.getInstructionPosition(address, this.code);
		context.moveOrdinalCounter(position);
	}
	
	private void jmpf(ThreeAddressRecord record, ExecutionContext context) {
		String address = record.getArgument1();
		int position = context.getInstructionPosition(address, this.code);
		String arg = record.getArgument2();
		arg = context.getValue(arg);
		if (position>=0 && Util.isBoolean(arg)) {
			if (Boolean.parseBoolean(arg))
				context.moveOrdinalCounter();
			else
				context.moveOrdinalCounter(position);
		}
		else
			throw new RuntimeException("Illegal instruction"); //$NON-NLS-1$
	}

	private void jmpt(ThreeAddressRecord record, ExecutionContext context) {
		String address = record.getArgument1();
		int position = context.getInstructionPosition(address, this.code);
		String arg = record.getArgument2();
		arg = context.getValue(arg);
		if (position>=0 && Util.isBoolean(arg)) {
			if (Boolean.parseBoolean(arg))
				context.moveOrdinalCounter(position);
			else
				context.moveOrdinalCounter();
		}
		else
			throw new RuntimeException("Illegal instruction"); //$NON-NLS-1$
	}

	private void lt(ThreeAddressRecord record, ExecutionContext context) {
		String left = record.getArgument1();
		String right = record.getArgument2();
		String r = record.getResult();
		
		left = context.getValue(left);
		right = context.getValue(right);
		
		String value;
		
		if (Util.isString(left) && Util.isString(right)) {
			value = Boolean.toString(left.compareTo(right)<0);
		}
		else if (Util.isNumber(left) && Util.isNumber(right)) {
			value = Boolean.toString(Double.parseDouble(left) < Double.parseDouble(right));
		}
		else {
			throw new RuntimeException("Illegal type of operand"); //$NON-NLS-1$
		}
		
		context.setValue(r, value);
		context.moveOrdinalCounter();
	}

	private void le(ThreeAddressRecord record, ExecutionContext context) {
		String left = record.getArgument1();
		String right = record.getArgument2();
		String r = record.getResult();
		
		left = context.getValue(left);
		right = context.getValue(right);
		
		String value;
		
		if (Util.isString(left) && Util.isString(right)) {
			value = Boolean.toString(left.compareTo(right)<=0);
		}
		else if (Util.isNumber(left) && Util.isNumber(right)) {
			value = Boolean.toString(Double.parseDouble(left) <= Double.parseDouble(right));
		}
		else {
			throw new RuntimeException("Illegal type of operand"); //$NON-NLS-1$
		}
		
		context.setValue(r, value);
		context.moveOrdinalCounter();
	}

	private void gt(ThreeAddressRecord record, ExecutionContext context) {
		String left = record.getArgument1();
		String right = record.getArgument2();
		String r = record.getResult();
		
		left = context.getValue(left);
		right = context.getValue(right);
		
		String value;
		
		if (Util.isString(left) && Util.isString(right)) {
			value = Boolean.toString(left.compareTo(right)>0);
		}
		else if (Util.isNumber(left) && Util.isNumber(right)) {
			value = Boolean.toString(Double.parseDouble(left) > Double.parseDouble(right));
		}
		else {
			throw new RuntimeException("Illegal type of operand"); //$NON-NLS-1$
		}
		
		context.setValue(r, value);
		context.moveOrdinalCounter();
	}

	private void ge(ThreeAddressRecord record, ExecutionContext context) {
		String left = record.getArgument1();
		String right = record.getArgument2();
		String r = record.getResult();
		
		left = context.getValue(left);
		right = context.getValue(right);
		
		String value;
		
		if (Util.isString(left) && Util.isString(right)) {
			value = Boolean.toString(left.compareTo(right)>=0);
		}
		else if (Util.isNumber(left) && Util.isNumber(right)) {
			value = Boolean.toString(Double.parseDouble(left) >= Double.parseDouble(right));
		}
		else {
			throw new RuntimeException("Illegal type of operand"); //$NON-NLS-1$
		}
		
		context.setValue(r, value);
		context.moveOrdinalCounter();
	}

	private void equal(ThreeAddressRecord record, ExecutionContext context) {
		String left = record.getArgument1();
		String right = record.getArgument2();
		String r = record.getResult();
		
		left = context.getValue(left);
		right = context.getValue(right);
		
		String value;
		
		if (Util.isString(left) && Util.isString(right)) {
			value = Boolean.toString(left.equals(right));
		}
		else if (Util.isNumber(left) && Util.isNumber(right)) {
			value = Boolean.toString(Double.parseDouble(left) == Double.parseDouble(right));
		}
		else if (Util.isBoolean(left) && Util.isBoolean(right)) {
			value = Boolean.toString(Boolean.parseBoolean(left) == Boolean.parseBoolean(right));
		}
		else {
			throw new RuntimeException("Illegal type of operand"); //$NON-NLS-1$
		}
		
		context.setValue(r, value);
		context.moveOrdinalCounter();
	}

	private void diff(ThreeAddressRecord record, ExecutionContext context) {
		String left = record.getArgument1();
		String right = record.getArgument2();
		String r = record.getResult();
		
		left = context.getValue(left);
		right = context.getValue(right);
		
		String value;
		
		if (Util.isString(left) && Util.isString(right)) {
			value = Boolean.toString(!left.equals(right));
		}
		else if (Util.isNumber(left) && Util.isNumber(right)) {
			value = Boolean.toString(Double.parseDouble(left) != Double.parseDouble(right));
		}
		else if (Util.isBoolean(left) && Util.isBoolean(right)) {
			value = Boolean.toString(Boolean.parseBoolean(left) != Boolean.parseBoolean(right));
		}
		else {
			throw new RuntimeException("Illegal type of operand"); //$NON-NLS-1$
		}
		
		context.setValue(r, value);
		context.moveOrdinalCounter();
	}

	private void and(ThreeAddressRecord record, ExecutionContext context) {
		String left = record.getArgument1();
		String right = record.getArgument2();
		String r = record.getResult();
		
		left = context.getValue(left);
		right = context.getValue(right);
		
		String value;
		
		if (Util.isBoolean(left)) {
			if (Util.isBoolean(right)) {
				value = Boolean.toString(Boolean.parseBoolean(left) && Boolean.parseBoolean(right));
			}
			else {
				throw new RuntimeException("Right operand is not a boolean value"); //$NON-NLS-1$
			}
		}
		else if (Util.isBoolean(right)) {
			throw new RuntimeException("Left operand is not a boolean value"); //$NON-NLS-1$
		}
		else {
			throw new RuntimeException("Invalid operand type"); //$NON-NLS-1$
		}
		
		context.setValue(r, value);
		context.moveOrdinalCounter();
	}

	private void or(ThreeAddressRecord record, ExecutionContext context) {
		String left = record.getArgument1();
		String right = record.getArgument2();
		String r = record.getResult();
		
		left = context.getValue(left);
		right = context.getValue(right);
		
		String value;
		
		if (Util.isBoolean(left)) {
			if (Util.isBoolean(right)) {
				value = Boolean.toString(Boolean.parseBoolean(left) || Boolean.parseBoolean(right));
			}
			else {
				throw new RuntimeException("Right operand is not a boolean value"); //$NON-NLS-1$
			}
		}
		else if (Util.isBoolean(right)) {
			throw new RuntimeException("Left operand is not a boolean value"); //$NON-NLS-1$
		}
		else {
			throw new RuntimeException("Invalid operand type"); //$NON-NLS-1$
		}
		
		context.setValue(r, value);
		context.moveOrdinalCounter();
	}

	private void xor(ThreeAddressRecord record, ExecutionContext context) {
		String left = record.getArgument1();
		String right = record.getArgument2();
		String r = record.getResult();
		
		left = context.getValue(left);
		right = context.getValue(right);
		
		String value;
		
		if (Util.isBoolean(left)) {
			if (Util.isBoolean(right)) {
				value = Boolean.toString(Boolean.parseBoolean(left) ^ Boolean.parseBoolean(right));
			}
			else {
				throw new RuntimeException("Right operand is not a boolean value"); //$NON-NLS-1$
			}
		}
		else if (Util.isBoolean(right)) {
			throw new RuntimeException("Left operand is not a boolean value"); //$NON-NLS-1$
		}
		else {
			throw new RuntimeException("Invalid operand type"); //$NON-NLS-1$
		}
		
		context.setValue(r, value);
		context.moveOrdinalCounter();
	}

	private void not(ThreeAddressRecord record, ExecutionContext context) {
		String left = record.getArgument1();
		String r = record.getResult();
		
		left = context.getValue(left);
		
		String value;
		
		if (Util.isBoolean(left)) {
			value = Boolean.toString(!Boolean.parseBoolean(left));
		}
		else {
			throw new RuntimeException("Invalid operand type"); //$NON-NLS-1$
		}
		
		context.setValue(r, value);
		context.moveOrdinalCounter();
	}

	private void add(ThreeAddressRecord record, ExecutionContext context) {
		String left = record.getArgument1();
		String right = record.getArgument2();
		String r = record.getResult();
		
		left = context.getValue(left);
		right = context.getValue(right);
		
		String value;
		
		if (Util.isString(left)) {
			String rv;
			if (Util.isArray(right)) {
				String[] tab = Util.parseArray(right);
				for(int i=0; i<tab.length; ++i) {
					tab[i] = Util.unstringify(tab[i]);
				}
				rv = Arrays.toString(tab);
			}
			else {
				rv = Util.unstringify(right);
			}
			value = Util.stringify(Util.unstringify(left) + rv);
		}
		else if (Util.isString(right)) {
			String lv;
			if (Util.isArray(left)) {
				String[] tab = Util.parseArray(left);
				for(int i=0; i<tab.length; ++i) {
					tab[i] = Util.unstringify(tab[i]);
				}
				lv = Arrays.toString(tab);
			}
			else {
				lv = Util.unstringify(left);
			}
			value = Util.stringify(lv + Util.unstringify(right));
		}
		else if (Util.isNumber(left)) {
			if (Util.isNumber(right)) {
				value = Util.toString(Double.parseDouble(left) + Double.parseDouble(right));
			}
			else {
				throw new RuntimeException("Right operand is not a number"); //$NON-NLS-1$
			}
		}
		else if (Util.isNumber(right)) {
			throw new RuntimeException("Left operand is not a number"); //$NON-NLS-1$
		}
		else {
			throw new RuntimeException("Invalid operand type"); //$NON-NLS-1$
		}
		
		context.setValue(r, value);
		context.moveOrdinalCounter();
	}
	
	private void substract(ThreeAddressRecord record, ExecutionContext context) {
		String left = record.getArgument1();
		String right = record.getArgument2();
		String r = record.getResult();
		
		left = context.getValue(left);
		right = context.getValue(right);
		
		String value;
		
		if (Util.isNumber(left)) {
			if (Util.isNumber(right)) {
				value = Util.toString(Double.parseDouble(left) - Double.parseDouble(right));
			}
			else {
				throw new RuntimeException("Right operand is not a number"); //$NON-NLS-1$
			}
		}
		else if (Util.isNumber(right)) {
			throw new RuntimeException("Left operand is not a number"); //$NON-NLS-1$
		}
		else {
			throw new RuntimeException("Invalid operand type"); //$NON-NLS-1$
		}
		
		context.setValue(r, value);
		context.moveOrdinalCounter();
	}

	private void multiply(ThreeAddressRecord record, ExecutionContext context) {
		String left = record.getArgument1();
		String right = record.getArgument2();
		String r = record.getResult();
		
		left = context.getValue(left);
		right = context.getValue(right);
		
		String value;
		
		if (Util.isNumber(left)) {
			if (Util.isNumber(right)) {
				value = Util.toString(Double.parseDouble(left) * Double.parseDouble(right));
			}
			else {
				throw new RuntimeException("Right operand is not a number"); //$NON-NLS-1$
			}
		}
		else if (Util.isNumber(right)) {
			throw new RuntimeException("Left operand is not a number"); //$NON-NLS-1$
		}
		else {
			throw new RuntimeException("Invalid operand type"); //$NON-NLS-1$
		}
		
		context.setValue(r, value);
		context.moveOrdinalCounter();
	}

	private void divide(ThreeAddressRecord record, ExecutionContext context) {
		String left = record.getArgument1();
		String right = record.getArgument2();
		String r = record.getResult();
		
		left = context.getValue(left);
		right = context.getValue(right);
		
		String value;
		
		if (Util.isNumber(left)) {
			if (Util.isNumber(right)) {
				double op = Double.parseDouble(right);
				if (op==0.) throw new RuntimeException("division by zero");  //$NON-NLS-1$
				value = Util.toString(Double.parseDouble(left) / op);
			}
			else {
				throw new RuntimeException("Right operand is not a number"); //$NON-NLS-1$
			}
		}
		else if (Util.isNumber(right)) {
			throw new RuntimeException("Left operand is not a number"); //$NON-NLS-1$
		}
		else {
			throw new RuntimeException("Invalid operand type"); //$NON-NLS-1$
		}
		
		context.setValue(r, value);
		context.moveOrdinalCounter();
	}

	private void minus(ThreeAddressRecord record, ExecutionContext context) {
		String left = record.getArgument1();
		String r = record.getResult();
		
		left = context.getValue(left);
		
		String value;
		
		if (Util.isNumber(left)) {
			value = Util.toString(-Double.parseDouble(left));
		}
		else {
			throw new RuntimeException("Invalid operand type"); //$NON-NLS-1$
		}
		
		context.setValue(r, value);
		context.moveOrdinalCounter();
	}
	
}
