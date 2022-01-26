package Grammar;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Main {

	public Main(Grammar g){
		HashSet<Rule> rules = new HashSet<Rule>();
		for(Rule r: g.getRules()){
			if(r.getRhs().length==1)
			{	
				rules.add(r);
			}
		}
		//disolve independence
		int size = 0;
		HashSet<Rule> toDelete = new HashSet<Rule>();
		while(size != rules.size()){
			size = rules.size();
			List<String> nonterminals = Arrays.asList(g.getNonTerminals());
			HashSet<Rule> newRules = new HashSet<Rule>();
			for(Rule r: rules){
				if(nonterminals.contains(r.getRhs()[0])){
					toDelete.add(r);
					String toSearch = r.getRhs()[0].trim();
					for(Rule r2: rules){
						if(r2.getLhs().trim().equals(toSearch)){
							Rule newRule = new Rule(r.getLhs(),r2.getRhs());
							newRules.add(newRule);
						}
					}
				}
			}
			rules.addAll(newRules);
		}
		//Delete nonterminals
		rules.removeAll(toDelete);
		
		//output
		System.out.println("Result: "+rules.size()+" Rules");
		for(Rule r: rules){
			System.out.println(r.toString());
		}
		
		HashMap<String,Integer> amount = new HashMap<String,Integer>();
		
		for(Rule r: rules){
			String terminal = r.getRhs()[0];
			if(amount.get(terminal)==null)
				amount.put(terminal,0);
			amount.put(terminal, amount.get(terminal)+1);
		}
		
		//output:
		double total = 0.0;
		for(String terminal : amount.keySet()){
			System.out.println(terminal + " : "+amount.get(terminal));
			total += amount.get(terminal);
		}
		System.out.println("Average: "+total/amount.size());
		
		
	}
	
	
	
	public static void main(String[] args){
		new Main(new Grammar("g2.txt"));
	}
	
	
}
