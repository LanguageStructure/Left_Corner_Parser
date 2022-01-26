package Unger;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import Grammar.*;

public class Parser {

	private static int steps = 0;

	private Grammar g;
	private List<String> nt;

	public Parser(Grammar g) {
		this.g = g;
		nt = Arrays.asList(g.getNonTerminals());
	}

	public boolean findParses(String[] input, String lhs, ArrayList<String> as) {
		steps++;
		ArrayList<String> clone = cloneList(as);
		boolean found = false;
		// Fall für Länge 1
		if (input.length == 1) {
			if (lhs.equals(input[0]))
			{
				found = true;
			}
			for(Rule r : g.getRules()){
				if(r.getLhs().equals(lhs) && r.getRhs().length == 1){
					if(r.getRhs()[0].equals(input[0])){
						as.add("("+lhs+","+input[0]+")");
						//printList(as);
						found = true;
					}
					else if(nt.contains(r.getRhs()[0])){
						as.add("("+lhs+","+r.getRhs()[0]+")");
						//printList(as);
						found = findParses(input,r.getRhs()[0],as);
					}
				}
			}
			
		} else {
			// Table for each rule...
			ArrayList<Rule> rules = getRules(lhs);
			for (int j = 0; j < rules.size(); j++) {
				ArrayList<String> clone4 = cloneList(as);
				Rule r = rules.get(j);
				if(r.getRhs().length > input.length)
					continue;
				System.out.println(r);
				as.add("("+lhs+","+Arrays.toString(r.getRhs())+")");
				printList(as);
				ArrayList<String[][]> lis = getPossibilities(input,
						r.getRhs().length);
				HashSet<String[][]> next = new HashSet<String[][]>();
				for (String[][] str : lis) {

					boolean possible = checkRule(str, r);
					for (String[] part : str) {
						System.out.print(Arrays.toString(part));
					}
					if (possible) {
						next.add(str);
					} else {
						System.out.print("FAIL");
					}
					System.out.println();

				}
		
				System.out.println("\n");
				for (String[][] moeglichkeit : next) {
					ArrayList<String> clone2 = cloneList(as);
					boolean foundOne = true;
					for (int x = 0; x < moeglichkeit.length; x++) {
						ArrayList<String> clone3 = cloneList(as);
						boolean works = findParses(moeglichkeit[x], r.getRhs()[x],
										as);
						foundOne = foundOne && works;
						if(!works){
							as.clear();
							as.addAll(clone3);
						}
						
					}
					if(!foundOne){
						as.clear();
						as.addAll(clone2);
					}
					found = found || foundOne;
				}

				if(!found){
					as.clear();
					as.addAll(clone4);
				}
			}
			
		}
		if(found){
			System.out.println("FOUND: "+lhs+" for "+Arrays.toString(input)+"\n");
		}
		else{
			as.clear();
			as.addAll(clone);
		}
		return found;
	}

	private void printList(ArrayList<String> as) {
		System.out.print("AS: ");
		for(int i=0; i<as.size();i++)
			System.out.print(as.get(i));
		System.out.println();
	}

	private boolean checkRule(String[][] str, Rule r) {
		for (int i = 0; i < r.getRhs().length; i++) {
			String symbol = r.getRhs()[i];
			if (symbol.split(" ").length == 1 && !nt.contains(symbol)
					&& !(str[i].length == 1 && str[i][0].equals(symbol))) {
				return false;
			}

		}
		return true;
	}

	private ArrayList<Rule> getRules(String lhs) {
		ArrayList<Rule> rules = g.getRules(lhs);
		ArrayList<Rule> erg = new ArrayList<Rule>();
		while (!rules.isEmpty()) {
			int shortest = 100;
			int index = 0;
			String term = "...";
			for (int i = 0; i < rules.size(); i++) {
				if (rules.get(i).getRhs().length < shortest) {
					index = i;
					shortest = rules.get(i).getRhs().length;
					term = Arrays.toString(rules.get(i).getRhs());
				}
				if (rules.get(i).getRhs().length == shortest) {
					if (0 > term.compareTo(Arrays.toString(rules.get(i)
							.getRhs()))) {
						term = Arrays.toString(rules.get(i).getRhs());
						index = i;
						shortest = rules.get(i).getRhs().length;
					}
				}
			}
			erg.add(rules.get(index));
			rules.removeAll(erg);
		}
		return erg;
	}

	private static ArrayList<String> cloneList(ArrayList<String> list) {
		ArrayList<String> clone = new ArrayList<String>(list.size());
		for (String item : list)
			clone.add(item);
		return clone;
	}

	public static void main(String[] args) {
		Parser p = new Parser(new Grammar("g.txt"));
		String sentence[] = {"chris", "used", "the", "pen", "and", "the", "chalk", "or", "the", "computer"};
		//String sentence[] = { "chris", "used", "the", "pen" };
		System.out.println(p.findParses(sentence, "S", new ArrayList<String>()));
		System.out.println(steps);
	}

	// !!!!
	private static ArrayList<String[][]> getPossibilities(String[] input, int n) {
		ArrayList<String[][]> erg = new ArrayList<String[][]>();
		String[] partitions = getPartitions(input.length, n);
		for (String s : partitions) {
			String result[][] = new String[n][];
			int actual = 0;
			for (int i = 0; i < s.length(); i++) {
				int neu = actual + Integer.valueOf(s.charAt(i) + "");
				result[i] = Arrays.copyOfRange(input, actual, neu);
				actual = neu;
			}
			erg.add(result);
		}
		return erg;
	}

	private static String[] getPartitions(int length, int n) {
		ArrayList<String> res = new ArrayList<String>();
		partition(length, n, res);
		HashSet<String> perm = new HashSet<String>();
		for (String str : res) {
			perm(str, perm);
		}
		String[] s = new String[perm.size()];
		s = perm.toArray(s);
		return s;
	}

	public static void partition(int n, int num, ArrayList<String> str) {
		partition(n, n, "", str, num);
	}

	public static void partition(int n, int max, String prefix,
			ArrayList<String> str, int num) {
		if (n == 0) {
			if (prefix.split(" ").length == num + 1)
				str.add(prefix);
			return;
		}

		for (int i = Math.min(max, n); i >= 1; i--) {
			partition(n - i, i, prefix + " " + i, str, num);
		}
	}

	public static void perm(String s, HashSet<String> perm) {
		perm("", s, perm);
	}

	private static void perm(String prefix, String s, HashSet<String> perm) {
		int N = s.length();
		if (N == 0) {
			perm.add(prefix.replace(" ", ""));
		} else {
			for (int i = 0; i < N; i++)
				perm(prefix + s.charAt(i),
						s.substring(0, i) + s.substring(i + 1, N), perm);
		}
	}
	// !!!
}
