package Grammar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;


public class Grammar{

	private ArrayList<Rule> rules = new ArrayList<Rule>();
	
	//Read the grammar
	public Grammar(String file){
		try{
			BufferedReader in = new BufferedReader(new FileReader(file));
			String zeile = null;
			while ((zeile = in.readLine()) != null) {
				addRule(zeile);
			}
			in.close();
		}
		catch(FileNotFoundException e){
			System.out.println("File not found");
		} 
		catch (IOException e) {
			System.out.println("Reading error");
		}
		catch(IllegalArgumentException e){
			System.out.println("Rule "+e+" not valid");
		}
	}
	
	//Add rule
	private void addRule(String zeile) throws IllegalArgumentException{
		try{	
			String[] sides = zeile.split("->");
			String lhs = sides[0].trim();
			String[] rhs = Arrays.copyOfRange(sides[1].split(" "),1,sides[1].split(" ").length);
			for(int i=0;i<rhs.length;i++)
				rhs[i] = rhs[i].trim();
			rules.add(new Rule(lhs,rhs));
		}
		catch(Exception e){
			throw new IllegalArgumentException(zeile);
		}
		
	}
	
	public void addRule(Rule r){
		if(!rules.contains(r))
			rules.add(r);
	}
	
	public String toString(){
		StringBuilder str = new StringBuilder();
		for(Rule r : rules){
			str.append(r.toString());
			str.append("\n");
		}
		return str.toString();
	}
	
	
	//Regel finden die zu bestimmter rhs ableitet
	//Find the appropriate rule, or return null
	public String findRuleFor(String[] rhs){
		for(Rule r: rules){
			if (r.goesTo(rhs))
				return r.getLhs();
		}
		return null;
	}
	
	//Find rules for non-terminals 
	public ArrayList<Rule> getRules(String lhs){
		ArrayList<Rule> temp = new ArrayList<Rule>();
		for(Rule r: rules){
			if(r.getLhs().equals(lhs))
				temp.add(r);
		}
		return temp;
	}
	
	
	//Return non Terminals
	public String[] getNonTerminals(){
		HashSet<String> set = new HashSet<String>();
		for(Rule r: rules){
			set.add(r.getLhs());
		}
		String[] result = new String[set.size()];
		result = set.toArray(result);
		return result;
	}

	//Return terminals
	public String[] getTerminals(){
		HashSet<String> set = new HashSet<String>();
		for(Rule r: rules){
			for(String str : r.getRhs()){
				if(str.equals(str.toLowerCase()))
					set.add(str);
			}
		}
		String[] result = new String[set.size()];
		result = set.toArray(result);
		return result;
	}
	
	//Rules
	public ArrayList<Rule> getRules(){
		return rules;
	}
	
	//Delete rules
	public void deleteRule(Rule r){
		rules.remove(r);
	}
	
	//save
	public void save(String file){
		try{
			BufferedWriter wr = new BufferedWriter(new FileWriter(file));
			for(Rule r: rules){
				wr.append(r.toString());
				wr.append("\n");
			}
			wr.flush();
			wr.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
