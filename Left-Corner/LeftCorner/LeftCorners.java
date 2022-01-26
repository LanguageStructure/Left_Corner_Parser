package LeftCorner;

import Grammar.*;

import java.util.*;


public class LeftCorners {

    private Grammar g;
    private HashMap<String,HashSet<String>> leftCornerTables = new HashMap<String,HashSet<String>>();
    private ArrayList<String> leftCorners = new ArrayList<String>();
    private ArrayList<String> done = new ArrayList<String>();

    public LeftCorners(Grammar g,String start){
                  this.g = g;
        getLeftCorners(start);
        finishLeftCorners();
    }

    private void getLeftCorners(String actual){
             ArrayList<String> result = getLeftCorner(actual);
             for(String s: result){
                  getLeftCorners(s);
             }
    }

    private int numberOfLeftCorners(){
        int result = 0;
        for(String s: leftCornerTables.keySet()){
            result += leftCornerTables.get(s).size();
        }
        return result;
    }

    private void finishLeftCorners(){
        int old = 0;
        List nonterminals =  Arrays.asList(g.getNonTerminals());
        while(old != numberOfLeftCorners()){
            old = numberOfLeftCorners();
            for(String s: leftCornerTables.keySet()){
                  if(done.contains(s))
                      continue;
                ArrayList<String> toAdd = new ArrayList<String>();
                for(String symbol: leftCornerTables.get(s)){
                    if(nonterminals.contains(symbol)){
                        toAdd.addAll(leftCornerTables.get(symbol));
                    }
                }
                leftCornerTables.get(s).addAll(toAdd);
            }

        }
        for(String s: leftCornerTables.keySet()){
            if(done.contains(s))
                continue;
            ArrayList<String> toDelete = new ArrayList<String>();
            for(String symbol: leftCornerTables.get(s)){
                if(nonterminals.contains(symbol)){
                    toDelete.add(symbol);
                }
            }
            leftCornerTables.get(s).removeAll(toDelete);
        }
    }

    private ArrayList<String> getLeftCorner(String nt){
        ArrayList<String> result = new ArrayList<String>();
        List l = Arrays.asList(g.getTerminals());
        for(Rule r : g.getRules(nt)){
             if(!leftCorners.contains(r.getRhs()[0]) && !l.contains(r.getRhs()[0])) {
                result.add(r.getRhs()[0]);
             }
             if(leftCornerTables.get(nt) == null)
                 leftCornerTables.put(nt,new HashSet<String>());
            leftCornerTables.get(nt).add(r.getRhs()[0]);
        }
        leftCorners.addAll(result);
        return result;
    }

    public String toString(){
        StringBuilder str = new StringBuilder();
        for(String s: leftCornerTables.keySet()){
            str.append(s+" : "+Arrays.toString(leftCornerTables.get(s).toArray())+"\n");
        }
        return str.toString();
    }


    public static void main(String[] args){
            Grammar g = new Grammar("atis-grammar-norm.txt");
            LeftCorners l = new LeftCorners(g,"SIGMA");
            System.out.println(l.toString());
    }


}
