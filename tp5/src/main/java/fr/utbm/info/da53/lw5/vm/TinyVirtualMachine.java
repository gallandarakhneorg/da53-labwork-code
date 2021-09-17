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

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;

import fr.utbm.info.da53.lw5.error.CompilerException;
import fr.utbm.info.da53.lw5.type.NumberUtil;
import fr.utbm.info.da53.lw5.util.Util;

/**
 * Virtual machine to run the tiny basic byte code.
 * 
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 */
public class TinyVirtualMachine implements Runnable {

	private final byte[] byteCode;
	private final long programSize;
	private final long codeSize;
	private final boolean isAllGlobal;
	private final HeapManager heap;
	
	/**
	 * @param byteCode is the byte code to run.
	 */
	public TinyVirtualMachine(byte[] byteCode) {
		if (byteCode[0]!='L'
			||byteCode[1]!='O'
			||byteCode[2]!='4'
			||byteCode[3]!='6'
			||byteCode[4]!='B'
			||byteCode[5]!='C') {
			throw new RuntimeException("The provided set of bytes is not a byte code"); //$NON-NLS-1$
		}
		if (byteCode[6]!='1'
			||byteCode[7]!='0') {
			throw new RuntimeException("Unsupported byte code version"); //$NON-NLS-1$
		}
		int options = byteCode[8];
		this.isAllGlobal = (options & 0x01) != 0;
		this.programSize = Util.getLong(byteCode, 9, 0);
		this.codeSize = Util.getLong(byteCode, 9, 1);
		this.byteCode = new byte[byteCode.length - 9 - 2*NumberUtil.SIZEOF_LONG];
		System.arraycopy(byteCode, 9 + 2*NumberUtil.SIZEOF_LONG, this.byteCode, 0, this.byteCode.length);
		this.heap = new HeapManager(this.programSize);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() {
		try {
			boolean b1, b2;
			Number a1, a2, r, n1, n2;
			Object v, v2;
			StackContext child, stack = new StackContext(this.isAllGlobal);
			int adr;
			long instruction;
			Instruction inst;
			Object[] params;
			String s;
			HeapRegion h;
			
			while (true) {
				adr = stack.getOrdinalCounter();
				stack.setOrdinalCounter(adr + 4 * NumberUtil.SIZEOF_LONG);
				
				instruction = Util.getLong(this.byteCode, adr, 0);
				
				a1 = TinyByteCode.parseByteCodeArgument1(this.byteCode, adr, instruction);
				a2 = TinyByteCode.parseByteCodeArgument2(this.byteCode, adr, instruction);
				r = TinyByteCode.parseByteCodeResult(this.byteCode, adr, instruction);
				inst = Instruction.fromByteCode(TinyByteCode.parseByteCodeInstruction(instruction));
				
				//System.err.println(toString(adr, inst, a1, a2, r));
				
				switch(inst) {
				case EXIT: // EXIT [a1]
					System.exit(a1.intValue());
					break;
				case SET: // r = a1
					setValue(stack, r, getValue(stack, a1));
					break;
				case ADDITION: // r = a1 + a2
					v = getValue(stack, a1);
					v2 = getValue(stack, a2);
					if (isString(v) || isString(v2)) {
						String s1 = toString(v);
						String s2 = toString(v2);
						StringBuilder b = new StringBuilder();
						b.append(s1);
						b.append(s2);
						setValue(stack, r, b.toString());
					}
					else {
						n1 = getNumber(stack, a1);
						n2 = getNumber(stack, a2);
						double dv = n1.doubleValue() + n2.doubleValue();
						setValue(stack, r, toNumber(dv, n1, n2));
					}
					break;
				case SUBSTRACTION: // r = a1 - a2
					n1 = getNumber(stack, a1);
					n2 = getNumber(stack, a2);
					double dv = n1.doubleValue() - n2.doubleValue();
					setValue(stack, r, toNumber(dv, n1, n2));
					break;
				case MULTIPLICATION: // r = a1 * a2
					n1 = getNumber(stack, a1);
					n2 = getNumber(stack, a2);
					dv = n1.doubleValue() * n2.doubleValue();
					setValue(stack, r, toNumber(dv, n1, n2));
					break;
				case DIVISION: // r = a1 / a2
					n1 = getNumber(stack, a1);
					n2 = getNumber(stack, a2);
					dv = n1.doubleValue() / n2.doubleValue();
					setValue(stack, r, toNumber(dv, n1, n2));
					break;
				case MINUS: // r = - a1
					n1 = getNumber(stack, a1);
					dv = - n1.doubleValue();
					setValue(stack, r, toNumber(dv, n1, n1));
					break;
				case EQUAL: // r = (a1==a2)
					v = getValue(stack, a1);
					v2 = getValue(stack, a2);
					if (v instanceof Number && v2 instanceof Number) {
						b1 = ((Number)v).doubleValue() == ((Number)v2).doubleValue();
					}
					else {
						b1 = v.equals(v2);
					}
					setValue(stack, r, b1);
					break;
				case DIFF: // r = (a1!=a2)
					v = getValue(stack, a1);
					v2 = getValue(stack, a2);
					if (v instanceof Number && v2 instanceof Number) {
						b1 = ((Number)v).doubleValue() != ((Number)v2).doubleValue();
					}
					else {
						b1 = !v.equals(v2);
					}
					setValue(stack, r, b1);
					break;
				case LESS: // r = (a1<a2)
					n1 = getNumber(stack, a1);
					n2 = getNumber(stack, a2);
					setValue(stack, r, NumberUtil.compare(n1, n2) < 0);
					break;
				case GREAT: // r = (a1>a2)
					n1 = getNumber(stack, a1);
					n2 = getNumber(stack, a2);
					setValue(stack, r, NumberUtil.compare(n1, n2) > 0);
					break;
				case LESS_EQUAL: // r = (a1<=a2)
					n1 = getNumber(stack, a1);
					n2 = getNumber(stack, a2);
					setValue(stack, r, NumberUtil.compare(n1, n2) <= 0);
					break;
				case GREAT_EQUAL: // r = (a1>=a2)
					n1 = getNumber(stack, a1);
					n2 = getNumber(stack, a2);
					setValue(stack, r, NumberUtil.compare(n1, n2) >= 0);
					break;
				case NOT: // r = !a1
					b1 = getBoolean(stack, a1);
					setValue(stack, r, !b1);
					break;
				case AND: // r = (a1 && a2)
					b1 = getBoolean(stack, a1);
					b2 = getBoolean(stack, a2);
					setValue(stack, r, b1 && b2);
					break;
				case OR: // r = (a1 || a2)
					b1 = getBoolean(stack, a1);
					b2 = getBoolean(stack, a2);
					setValue(stack, r, b1 || b2);
					break;
				case XOR: // r = (a1 ^ a2)
					b1 = getBoolean(stack, a1);
					b2 = getBoolean(stack, a2);
					setValue(stack, r, b1 ^ b2);
					break;
				case JUMP: // JUMP a1
					stack.setOrdinalCounter(toOrdinalCounter(stack, a1));
					break;
				case JUMP_IF: // JUMP a1 IF a2
					n1 = getNumber(stack, a2);
					if (n1.intValue()!=0) {
						stack.setOrdinalCounter(toOrdinalCounter(stack, a1));
					}
					break;
				case JUMP_IF_NOT: // JUMP a1 IF NOT a2
					n1 = getNumber(stack, a2);
					if (n1.intValue()==0) {
						stack.setOrdinalCounter(toOrdinalCounter(stack, a1));
					}
					break;
				case PARAM: // PARAM a1
					stack.addCallParameter(getValue(stack, a1));
					break;
				case GET_PARAM: // r = PARAM a1
					v = stack.getFormalParameter(getNumber(stack, a1).intValue());
					setValue(stack, r, v);
					break;
				case CALL: // r = CALL a1, a2
					params = stack.consumeParameters(getNumber(stack, a2).intValue());
					child = new StackContext(stack);
					child.setOrdinalCounter(toOrdinalCounter(stack,a1));
					child.setFormalParameters(params);
					if (r instanceof Address && ((Address)r).intValue()!=0) {
						child.setReturnAddress((Address)r);
					}
					stack = child;
					break;
				case RETURN: // RETURN [a1]
					Address ret = stack.getReturnAddress();
					if (ret!=null) {
						v = getValue(stack, a1);
					}
					else {
						v = null;
					}
					stack = stack.getParent();
					if (ret!=null) {
						assert(v!=null);
						setValue(stack, ret, v);
					}
					break;
				case READ: // r = READ
					s = new BufferedReader(new InputStreamReader(System.in)).readLine();
					if (Util.isNumber(s)) {
						v = NumberUtil.parse(s, 0);
					}
					else {
						v = s;
					}
					setValue(stack, r, v);
					break;
				case PRINT: // PRINT a1
					v = getValue(stack, a1);
					System.out.print(toString(v));
					break;
				case ARRAY: // r = a1[a2]
					n1 = getNumber(stack, a2);
					n2 = getNumber(stack, a1);
					h = this.heap.getRegion(n2.longValue());
					v = h.getValueAt(n1.intValue() / NumberUtil.SIZEOF_LONG);
					setValue(stack, r, v);
					break;
				case SET_ARRAY: // r[a1] = a2
					n1 = getNumber(stack, a1);
					v = getValue(stack, a2);
					n2 = getNumber(stack, r);
					if (n2.longValue()<this.programSize) {
						// Address is for the code segment or the stack.
						// Force the allocation in the heap.
						h = this.heap.newRegion();
						setValue(stack, r, new Address(h.address()));
					}
					else {
						// Address is for the heap
						h = this.heap.getRegion(n2.longValue());
					}
					assert(h!=null);
					h.setValueAt(n1.intValue() / NumberUtil.SIZEOF_LONG, v);
					break;
				default:
					throw new RuntimeException("Unsupported instruction: 0x"+Long.toHexString(instruction)); //$NON-NLS-1$
				}
			}		
		}
		catch(CompilerException e) {
			throw new IOError(e);
		}
		catch(IOException e) {
			throw new IOError(e);
		}
	}
	
	private Object getValue(StackContext stack, Number adr) {
		if (adr instanceof Address) {
			int a = adr.intValue();
			if (a<0) {
				return stack.getValueAt(-a-1);
			}
			if (a>=this.programSize) {
				HeapRegion h = this.heap.getRegion(a);
				return h.getValue();
			}
			if (a>=this.codeSize) {
				StringBuilder str = new StringBuilder();
				while (this.byteCode[a]!='\0') {
					str.append((char)this.byteCode[a]);
					++a;
				}
				return str.toString();
			}
		}
		return adr;
	}

	private Number getNumber(StackContext stack, Number adr) {
		return (Number)getValue(stack, adr);
	}

	private boolean getBoolean(StackContext stack, Number adr) {
		return getNumber(stack, adr).intValue()!=0;
	}

	private void setValue(StackContext stack, Number result, Object value) {
		int ra = result.intValue();
		if (ra<0) {
			Number n;
			if (value instanceof Number) {
				n = (Number)value;
			}
			else if (value instanceof CharSequence) {
				HeapRegion region = this.heap.newRegion();
				region.setValueAt(0, value);
				n = new Address(region.address());
			}
			else if (value instanceof Boolean) {
				n = ((Boolean)value).booleanValue() ? 1 : 0;
			}
			else {
				throw new RuntimeException("Unsupported type expression for: "+value); //$NON-NLS-1$
			}
			stack.setValueAt(-ra-1, n);
		}
		else if (ra>=this.programSize) {
			HeapRegion h = this.heap.getRegion(ra);
			h.setValueAt(0, value);
		}
		else {
			throw new RuntimeException("Cannot put a value into the segment code"); //$NON-NLS-1$
		}
	}
	
	private Number toNumber(double v, Number v1, Number v2) {
		if (v1 instanceof Address || v2 instanceof Address)
			return new Address((long)v);
		return NumberUtil.toNumber(v);
	}

	private int toOrdinalCounter(StackContext context, Number v) {
		Number n = getNumber(context, v);
		return n.intValue();
	}

	private boolean isString(Object value) {
		if (value instanceof Address) {
			int adr = ((Address)value).intValue();
			if (adr>=this.programSize) {
				HeapRegion region = this.heap.getRegion(adr);
				if (region!=null) {
					return String.class.equals(region.typeAt(0));
				}
			}
		}
		else if (value instanceof CharSequence) {
			return true;
		}
		return false;
	}

	private String toString(Object value) {
		if (value instanceof Address) {
			int adr = ((Address)value).intValue();
			if (adr>=this.programSize) {
				HeapRegion region = this.heap.getRegion(adr);
				if (region!=null) {
					Object v = region.getValue();
					return v.toString();
				}
			}
		}
		return value.toString();
	}
	
	/** Create the string representation for the given byte code instruction.
	 * 
	 * @param address is the address of the byte code instruction.
	 * @param instruction is the instruction to run.
	 * @param a1 is the first parameter of the instruction.
	 * @param a2 is the second parameter of the instruction.
	 * @param r is the result container.
	 * @return the string representation.
	 */
	public static String toString(int address, Instruction instruction, Number a1, Number a2, Number r) {
		String p1 = a1.toString();
		if (NumberUtil.isInteger(a1)) {
			long v = a1.longValue();
			if (v<0) {
				p1 = "#"+(-v-1); //$NON-NLS-1$
			}
			else if (a1 instanceof Address) {
				if (v==0)
					p1 = "NULL"; //$NON-NLS-1$
				else
					p1 = "@"+v; //$NON-NLS-1$
			}
		}
		String p2 = a2.toString();
		if (NumberUtil.isInteger(a2)) {
			long v = a2.longValue();
			if (v<0) {
				p2 = "#"+(-v-1); //$NON-NLS-1$
			}
			else if (a2 instanceof Address) {
				if (v==0)
					p2 = "NULL"; //$NON-NLS-1$
				else
					p2 = "@"+v; //$NON-NLS-1$
			}
		}
		String rr = r.toString();
		if (NumberUtil.isInteger(r)) {
			long v = r.longValue();
			if (v<0) {
				rr = "#"+(-v-1); //$NON-NLS-1$
			}
			else if (r instanceof Address) {
				if (v==0)
					rr = "NULL"; //$NON-NLS-1$
				else
					rr = "@"+v; //$NON-NLS-1$
			}
		}
		return address
				+":\t" //$NON-NLS-1$
				+instruction.name()
				+"\t" //$NON-NLS-1$
				+p1
				+"\t" //$NON-NLS-1$
				+p2
				+"\t" //$NON-NLS-1$
				+rr;
	}

	/**
	 * Virtual machine to run the tiny basic byte code.
	 * 
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $Name$ $Revision$ $Date$
	 */
	private static enum Instruction {
		/** */
		EXIT,
		/** */
		SET,
		/** */
		ADDITION,
		/** */
		SUBSTRACTION,
		/** */
		MULTIPLICATION,
		/** */
		DIVISION,
		/** */
		MINUS,
		/** */
		EQUAL,
		/** */
		DIFF,
		/** */
		LESS,
		/** */
		GREAT,
		/** */
		LESS_EQUAL,
		/** */
		GREAT_EQUAL,
		/** */
		NOT,
		/** */
		AND,
		/** */
		OR,
		/** */
		XOR,
		/** */
		JUMP,
		/** */
		JUMP_IF,
		/** */
		JUMP_IF_NOT,
		/** */
		PARAM,
		/** */
		GET_PARAM,
		/** */
		CALL,
		/** */
		RETURN,
		/** */
		READ,
		/** */
		PRINT,
		/** */
		ARRAY,
		/** */
		SET_ARRAY;
		
		/** Replies the instruction for the given instruction code.
		 * 
		 * @param instruction is the byte's code instruction
		 * @return the instruction.
		 */
		public static Instruction fromByteCode(long instruction) {
			return values()[(int)instruction];
		}
		
	}
	
}