package GoldenPuppy;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

public class Interpreter {
	
	private int[] codes ,code_block;
	private int[] consts;
	private Stack<Object> stack = new Stack<Object>();
	private Stack<Integer> addr_stack = new Stack<Integer>();
	private HashMap<Integer,int[]> beautiful_code = new HashMap<Integer,int[]>();
	//String => NAME   Integer => ADDR
	private HashMap<String, Integer> global_veriable = new HashMap<String, Integer>();
	private String[] IDs;
	private int pointer,constpool_size,namespace_size;
	private int line_count = 0;
	
	private PrintStream out = System.out;
	
	public Interpreter(int[] codes) {
		this.codes = codes;
	}
	
	interface HEX{
		int NULL = 0x0,
		load_constf = 0x1,
		load_const = 0x2,
		load_int = 0x6,
		load_float_fast = 0x3,
		store_float = 0x4,
		store_int = 0x7,
		cls = 0x5,
		print = 0x8,
		println = 0x9,
		
		add = 0xa,
		sub = 0xb,
		muit = 0xc,
		div = 0xd;
	}
	
	private void get_property_size() {
		constpool_size = codes[pointer++];
		namespace_size = codes[pointer++];
		
		consts = new int[constpool_size];
		IDs = new String[namespace_size];
		line_count++;
	}
	
	private void set_names(String[] ids) {
		this.IDs = ids;
	}
	
	private void get_consts() {
		String p1,p2,p3,p4;
		int temp_value ,state;
		for(int i = 0;i < constpool_size;i++) {
			state = codes[pointer++];
			p1 = Integer.toHexString(codes[pointer++]);
			p2 = Integer.toHexString(codes[pointer++]);
			p3 = Integer.toHexString(codes[pointer++]);
			p4 = Integer.toHexString(codes[pointer++]);
			try {
				temp_value = Integer.parseInt(p1+p2+p3+p4 ,16);
				if(state == 0) {
					consts[i] = temp_value;
				}else {
					consts[i] = -temp_value;					
				}
			}catch(NumberFormatException e) {
				out.println("Not a correct number");
				e.printStackTrace();
				return;
			}
			line_count++;
		}
	}
	
	private void cut_codes(){
		int line_count = 1;
		
		beautiful_code.put(0,new int[]{codes[0],codes[1]});
		
		int[] temp = new int[]{0,0,0,0,0};
		for(int i = 2,j = 0;i < codes.length;i++,j++){
			if(j % 5 == 0 && j != 0){
				beautiful_code.put(line_count++,Arrays.copyOf(temp,temp.length));
				j = 0;
			}
			temp[j] = codes[i];
		}
		beautiful_code.put(line_count++,Arrays.copyOf(temp,temp.length));
	}
	
	private void run() {
		
		String t;
		pointer = 0;
		
		code_block = beautiful_code.get(line_count);
		int code = code_block[pointer] ,addr;
		
		System.out.println("Now in " + pointer + " line :"+line_count);
		switch (code) {
		case HEX.load_const:
			t = getAddr(4,code_block);
			addr = Integer.parseInt(t ,16);
			
			addr_stack.push(addr);
			LOAD_CONST(addr);
			
			pointer++;
			break;
			
		case HEX.store_int:
			t = getAddr(4,code_block);
			addr = Integer.parseInt(t ,16);
			
			STORE_GLOBAL(addr);
			pointer++;
			break;
			
		case HEX.load_int:
			t = getAddr(4,code_block);
			addr = Integer.parseInt(t ,16);
			
			LOAD_INT(addr);
			pointer++;
			break;
			
		case HEX.print:
			out.print(stack.pop());
			break;
			
		case HEX.println:
			out.println(stack.pop());
			break;
			
		case HEX.add:
			ADD();
			break;
			
		case HEX.sub:
			SUB();
			break;
		
		case HEX.div:
			DIV();
			break;
			
		case HEX.muit:
			MUIT();
			break;
		
		default:
			System.out.println("Bad code : "+code + " at "+pointer);
			break;
		}
		
		line_count++;
	}
	
	/*
	 * @param const_addr  => 指向常量池的地址
	 * */
	
	private void LOAD_CONST(int const_addr) {
		stack.push(consts[const_addr]);
	}
	/*
	 * @param const_addr  => 指向ID池的地址
	 * */
	private void STORE_GLOBAL(int const_addr) {
		stack.pop(); //形式上而已，因为global_variable存的是地址
		global_veriable.put(IDs[const_addr], addr_stack.pop());
		System.out.println(IDs[const_addr] +"　ＰＵＳＨＥＤ");
	}
	/*
	 * @param const_addr  => 指向ID池的地址
	 * */
	private void LOAD_INT(int const_addr) {
		stack.push(
				consts[global_veriable.get(IDs[const_addr])]);
		
		System.out.println("global name "+IDs[const_addr]+" := "+consts[global_veriable.get(IDs[const_addr])] + " ＬＯＡＤ");
	}
	
	private void ADD(){
		stack.push((int)stack.pop()+(int)stack.pop());
	}
	
	private void SUB(){
		int a = stack.pop(),b = stack.pop();
		stack.push(b-a);
	}
	
	private void DIV(){
		int a = stack.pop(),b = stack.pop();
		stack.push(b/a);
	}
	
	private void MUIT(){
		stack.push((int)stack.pop()*(int)stack.pop());
	}
	
	
	private String getAddr(int time,int[] block){
		String temp = "";
		for(int i = 0;i < time;i++){
			temp += Integer.toHexString(code_block[++pointer]);
		}
		return temp;
	}
	
	private String getAddr(){
		return getAddr(4,code_block);
	}
	
	public static void main(String[] args) {
		int[] c = {2,0,
					0,2,3,4,5,
					0,6,0,0,4,
					2,0,0,0,0,
					2,0,0,0,1,
					11,0,0,0,0,
					8,0,0,0,0};
		Interpreter i = new Interpreter(c);
		
		i.get_property_size();
		i.get_consts();
		i.set_names(new String[] {"ABC"});
		
		i.cut_codes();
		
		System.out.println("===out===");
		
		i.run();
		i.run();
		i.run();
		i.run();
		
		
		System.out.println("\n===out===\n");
		
		System.out.println("======Virtual machine Info=====");
		
		System.out.print("Consts:\n\t");
		for(int e:i.consts) {
			System.out.print(e +" ");
		}
		System.out.print("\nStack :\n\t");
		for (int j : i.stack) {
			System.out.print(j + " ");
		}
		System.out.println("\nbeautiful_code:");
		for(int ie:i.beautiful_code.keySet()){
			int[] temp = i.beautiful_code.get(ie);
			System.out.print("\t"+ie+" : ");
			for(int iee:temp) System.out.print(iee + " ");
			System.out.println();
		}
	}
}
