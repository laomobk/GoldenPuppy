package PuObjects;

public class PuInt{
	
	public int value = 0;
	
	public PuInt(int v) {
		this.value = v; 
	}
	
	public PuInt add(PuInt right) {
		return new PuInt(this.value + right.value);
	}

	public PuInt sub(PuInt right) {
		return new PuInt(this.value  - right.value);	
	}

	public PuInt muit(PuInt right) {
		return new PuInt(this.value - right.value);
	}

	public PuInt divi(PuInt right) {
		return new PuInt(this.value - right.value);
	}

}
