package GoldenPuppy;


import java.io.*;
import java.util.*;
public class HexHelper
{
	private static String path = "Untitled.puc";
	
	static void setPath(String p){
		p = path;
	}
	
	static void saveHex(int ar) throws FileNotFoundException, IOException{
		FileOutputStream fos = new FileOutputStream(
			path);

		int in = ar, start;
		String a ,finaL = "";
		String[] list = new String[]{"0","0","0","0","0","0","0","0"};
		char[] sp;
		System.out.println((a = Integer.toHexString(in)));

		start = list.length - (sp = a.toCharArray()).length;

		for(int i=0;start < list.length;i++){
			list[start++] = String.valueOf(sp[i]);
		}
		int count = 0;
		for(String s:list){
			if(count == 2){
				finaL += ",";
				count = 0;
			}
			finaL += s;
			count++;
		}
		System.out.println(finaL);

		for(String s:finaL.split(",")){
			System.out.print(s + " ");
			fos.write(Integer.parseInt(s,16));
		}
		fos.close();

	}
	
	static void printAsDec() throws IOException{
		FileInputStream fis = new FileInputStream(
			path);
		String s = "";
		for(int i;(i = fis.read()) != -1;){
			s+=Integer.toHexString(i);
		}
		System.out.println(Integer.parseInt(s,16));
	}
}
