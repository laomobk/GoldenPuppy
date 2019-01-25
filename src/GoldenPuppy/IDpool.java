package GoldenPuppy;
import java.util.*;

public class IDpool
{
	private static int ID_address = 0;
	private static final ArrayList<String> ID_POOL = new ArrayList<String>();
	
	public static int getPoolSize(){return ID_POOL.size();}
	
	public static String getIDbyAddress(int addr){
		return ID_POOL.get(addr);
	}
	public static int store_ID(final String id){
		ID_POOL.add(id);
		return ID_address++;
	}
}
