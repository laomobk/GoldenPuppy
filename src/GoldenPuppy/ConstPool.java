package GoldenPuppy;

import java.util.ArrayList;

public class ConstPool {
	private static final ArrayList<Integer> STATIC_CONST_POOL = new ArrayList<Integer>();
	private static final ArrayList<Integer> STATIC_CONSTF_POOL = new ArrayList<Integer>();
	static final void addInt(int i) {STATIC_CONST_POOL.add(i);}
	static final int getInt(final int i) {return STATIC_CONST_POOL.get(i);}
	static final int[] getFloat(final int i) {return new int[] {STATIC_CONSTF_POOL.get(i),STATIC_CONSTF_POOL.get(i+1)};}
}
