package GoldenPuppy;
import java.util.*;

import PuObjects.PuInt;

import java.io.*;



public class PVM0
{
	
	interface HEX{
		int NULL = 0x0,
		load_constf = 0x1,
		load_const = 0x2,
		load_int = 0x6,
		load_float_fast = 0x3,
		store_float = 0x4,
		store_int = 0x7,
		cls = 0x5,
		print = 0x8;
	}
	
	private PVM0(){}
	
	private static HexReader reader;
	private static PrintStream out;
	
	private static Stack<PuInt> Pu_int_Stack = new Stack<PuInt>();
	private static Stack<Float> Pu_float_Stack = new Stack<Float>();
	private static Stack<Integer> Pu_address_Stack = new Stack<Integer>();
	
	private static ArrayList<PuInt> Pu_global = new ArrayList<PuInt>();
	
	private static final int RAM_SIZE = 8192;
	private static final int INT_SIZE = 32;
	private static final int FLOAT_SIZE = 32;
	
	private static int now_mem_pointer = 0;
	
	private static byte[] V_MEM = new byte[RAM_SIZE];
	
	public static
	void setPath(String p) throws FileNotFoundException{
		reader = new HexReader(p);
	}
	
	public static
	void setPrintStream(PrintStream ps){
		out = ps;
	}
	
	public static
	void runInstr(final int CODE){
		if(out != null && reader != null){
			parseInstr(CODE);
		}else{
			out.println("PVM Unsuccessfully initialized !");
			return;
		}
	}
	
	private static
	int parseAddress(String addr) {
		return Integer.parseInt(addr, 16);
	}
	
	private static 
	void parseInstr(final int CODE){
		
		int item = 0;
		
		switch(CODE){
			case HEX.load_const:
				if(reader.peek() != HEX.NULL){
					String s1 = String.valueOf(reader.pop()),
						   s2 = String.valueOf(reader.pop()),
						   s3 = String.valueOf(reader.pop());
					int ppos = Integer.parseInt(s1+s2+s3, 16);
					Pu_int_Stack.push(new PuInt(ConstPool.getInt(ppos)));
				}
				else{
					out.println("PVM Error :Unknown instruction ");
				}
				break;
//			case HEX.load_constf:
//				if(reader.peek() != HEX.NULL){
//					String s1 = String.valueOf(reader.pop()),
//							   s2 = String.valueOf(reader.pop()),
//							   s3 = String.valueOf(reader.pop());
//					int ppos = Integer.parseInt(s1+s2+s3, 16);
//					Pu_float_Stack.push((float)((ConstPool.getFloat(ppos)[0]) / (ConstPool.getFloat(ppos)[1])));
//				}
//				else{
//					out.println("PVM Error :Unknown instruction ");
//				}
//				break;
//				
			case HEX.store_int:
				if(reader.peek() != HEX.NULL){
					//Cut the ID address into three HEX numbers 
					String temp = Integer.toHexString(reader.pop()),
						   temp2 = Integer.toHexString(reader.pop()),
						   temp3 = Integer.toHexString(reader.pop()),
						   finaL;
					int addr_temp;
					
					finaL = temp3 + temp2 + temp;
					Pu_address_Stack.push((addr_temp = Integer.parseInt(finaL,16)));
					
					Pu_global.add(new PuInt(addr_temp)); // 这是指向ID池的地址
					Pu_global.add(Pu_int_Stack.pop());//这是变量本身的值
					
					/*
					 * 综上两行所述，变量在Pu_global大概是这样的：
					 * =============		============
					 * =[NAME_ADDR]=		=    Foo   =
					 * =============   ---> ============
					 * =  [VALUE]  =		=   2019   =
					 * =============		============
					 * 
					 * 要是数组溢出那就是PVM了问题了呗~
					 * */
					
					
				}
				else{
					out.println("PVM Error :Unknown instruction ");
				}
				break;
				
			case HEX.load_int:
				if(reader.peek() != HEX.NULL) {
					
					int addr_temp = parseAddress(reader.popStr()+
												 reader.popStr()+
												 reader.popStr());
					try {
						Pu_int_Stack.push(Pu_global.get(addr_temp+1));
					}catch(IndexOutOfBoundsException e) {
						out.println("PVM Error :Can not get value successfully!");
					}
				}else{
					out.println("PVM Error :Unknown instruction ");
				}
				break;
			
			case HEX.print:
				if(reader.peek() != HEX.NULL) {
					
					out.println(Pu_int_Stack);
					reader.pop();
					reader.pop();
					reader.pop();
				
				}else{
					out.println("PVM Error :Unknown instruction ");
				}
				break;
		}
	}
	
	
	public static void main(String[] args) {
		ConstPool.addInt(10);
		byte[] instr = {0x1,0x0,0x0,0x0,
						0x8,0x0,0x0,0x0};
		
		for(byte b:instr) {
			parseInstr(b);
		}
	}
	
}
