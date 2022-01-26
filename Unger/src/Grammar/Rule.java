package Grammar;
import java.util.Arrays;


public class Rule {


	private String lhs = "";
	private String[] rhs;
	
	public Rule(String lhs, String[] rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}
	
	public String toString(){
		return lhs + " -> "+ Arrays.toString(rhs).replace("[", "").replace("]", "").replace(",", " ");
	}
	
	public boolean goesTo(String[] rhs){
		if(this.rhs.length != rhs.length)
			return false;
		for(int i = 0;i<rhs.length;i++)
		{
			if(!rhs[i].equals(this.rhs[i]))
				return false;
		}
		return true;
	}

	public String getLhs() {
		return lhs;
	}

	public String[] getRhs() {
		return rhs;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lhs == null) ? 0 : lhs.hashCode());
		result = prime * result + Arrays.hashCode(rhs);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rule other = (Rule) obj;
		if (lhs == null) {
			if (other.lhs != null)
				return false;
		} else if (!lhs.equals(other.lhs))
			return false;
		if (!Arrays.equals(rhs, other.rhs))
			return false;
		return true;
	}
	
	

}
