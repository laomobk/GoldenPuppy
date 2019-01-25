package Parser;
import java.util.*;

enum types{
	END,
	PACKED
}

class Rule{
	String value = null;
	
	Rule(String str){
		this.value = str;
	}
	
	
}

public class Parser
{
	private HashMap<types,Parser> self = new HashMap<types,Parser>();
	
	Parser addItem(String end,types type){
		self.put(type,new Parser());
		return this;
	}

}
