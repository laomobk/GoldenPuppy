package GoldenPuppy;
import java.util.*;
import java.io.*;



public class PVM
{
	
	interface HEX{
		int NULL = 0x0,
		load_constf = 0x1,
		load_const = 0x2,
		load_int_fast = 0x6,
		load_float_fast = 0x3,
		store_float = 0x4,
		store_int = 0x7,
		cls = 0x5;
	}
	
	private PVM(){}
	
	private static HexReader reader;
	private static PrintStream out;
	
	private static Stack<Integer> Pu_int_Stack = new Stack<Integer>();
	private static Stack<Integer> Pu_float_Stack = new Stack<Integer>();
	private static Stack<Integer> Pu_address_Stack = new Stack<Integer>();
	
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
	void parseInstr(final int CODE){
		
		int item = 0;
		
		switch(CODE){
			case HEX.load_const:
				if(reader.peek() != HEX.NULL){
					Pu_int_Stack.push(reader.pop());
					reader.pop(); //NULL
					reader.pop();
				}
				else{
					out.println("PVM Error :Unknown instruction ");
				}
				break;
			case HEX.load_constf:
				if(reader.peek() != HEX.NULL){
					Pu_float_Stack.push(reader.pop()); //Rule : Original number
					Pu_float_Stack.push(reader.pop()); //Rule : Moving distance of point 
					
					reader.pop();
				}
				else{
					out.println("PVM Error :Unknown instruction ");
				}
				break;
				
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
					IDpool.getIDbyAddress(addr_temp);
					// "CF" + "FA" + "BB" 
					
				}
				else{
					out.println("PVM Error :Unknown instruction ");
				}
				break;
		}
	}
	
}
