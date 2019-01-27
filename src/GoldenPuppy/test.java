package GoldenPuppy;

import java.io.*;
import java.util.ArrayList;

public class test
{
   public static void main(String args[]) throws IOException
   {
	   HexHelper h = new HexHelper();
	   h.saveHex(2147483647);
	   h.printAsDec();
	   
	   System.out.println(Integer.parseInt("7fffffff",16));
   } 
} 