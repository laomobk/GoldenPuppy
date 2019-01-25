package GoldenPuppy;
import java.io.*;
import java.util.*;

public class HexReader extends Thread
{
	private FileInputStream fis;
	private Stack<Integer> temp_stack = new Stack<Integer>();
	private static final int MIN_STACK_LENGTH = 3;
	
	public HexReader(String path) throws FileNotFoundException{
		fis = new FileInputStream(path);
		run();
	}
	
	public void run(){
		try{
		while(true)
			if(temp_stack.size() < MIN_STACK_LENGTH){
				while(temp_stack.size() < MIN_STACK_LENGTH){
					temp_stack.push(fis.read());
				}
			}
		}catch(IOException e){}
	}
	
	public int pop(){
		return temp_stack.pop();
	}
	public int peek(){
		return temp_stack.peek();
	}
}
