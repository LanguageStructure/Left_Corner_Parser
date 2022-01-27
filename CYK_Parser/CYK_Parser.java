/**           
 * Requires file with CNF grammar and all non terminals in first line and each
 * rule in new line!!!
 */     
package CYK_Parser;
    
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
 
public class CYK_Parser {

    boolean[][][] table;
    HashMap<Symbol, List<Symbol[]>> grammar;
    Symbol[] nonTerminals;
    Symbol startSymbol;

    public CYK_Parser(String fileName) throws FileNotFoundException {

        Scanner fileScan = new Scanner(new File(fileName));
        String line;
        line = fileScan.nextLine();
        String[] ntLine = line.split("\\s+");
        nonTerminals = new Symbol[ntLine.length];
        Symbol start = new Symbol(ntLine[0], Symbol.NT);
        nonTerminals[0] = start;
        startSymbol = start;
        for (int i = 1; i < ntLine.length; i++) {
            nonTerminals[i] = new Symbol(ntLine[i], Symbol.NT);
        }

//        int numRules = 0;
//        while(fileScan.hasNextLine()) {
//            line = fileScan.nextLine();
//            if (line.matches(".+ -> .+"))
//                numRules++;;
//        }

        //fileScan.reset();
        grammar = new HashMap<Symbol, List<Symbol[]>>();
        for (Symbol s : nonTerminals) {
            grammar.put(s, null);
        }

        while (fileScan.hasNextLine()) {
            line = fileScan.nextLine();
            if (line.matches(".+ -> .+")) {
                String lineSplit[] = line.split("\\s+");
                String[] rhsStr = lineSplit[1].split("\\s+");
                Symbol lhs = new Symbol(lineSplit[0], Symbol.NT);
                Symbol[] rhsSym = new Symbol[rhsStr.length];
                if (rhsStr.length > 1) {
                    for (int i = 0; i < rhsStr.length; i++) {
                        rhsSym[i] = new Symbol(rhsStr[i], Symbol.NT);
                    }
                } else {
                    Symbol newSym = new Symbol(rhsStr[0], Symbol.NT);
                    if (Arrays.asList(nonTerminals).contains(newSym)) {
                        rhsSym[0] = newSym;
                    } else {
                        newSym.setType(Symbol.T);
                        rhsSym[0] = newSym;
                    }
                }
                if (grammar.get(lhs) != null) {
                    grammar.get(lhs).add(rhsSym);
                } else {
                    ArrayList newRhs = new ArrayList<Symbol[]>();
                    newRhs.add(rhsSym);
                    grammar.put(lhs, newRhs);
                }
            }
        }
    }
    

    public boolean parse(String input) {

        //asuming everything went fine creating grammar

        String[] inputSplit = input.split("\\s+");
        int inputLength = inputSplit.length;
        table = new boolean[inputLength][inputLength][nonTerminals.length];
        for (int i = 0; i < inputLength; i++) {
            for (int j = 0; j < nonTerminals.length; j++) {
                table[i][i][j] = false;
            }
        }
        for (int i = 0; i < inputLength; i++) {
            for (int j = 0; j < nonTerminals.length; j++) {
                for (Symbol[] sa : grammar.get(nonTerminals[j])) {
                    if (sa.length == 1 && sa[0].toString().equals(inputSplit[i]))
                        table[i][0][j] = true;
                }
            }
        }
        for (int i = 1; i <= inputLength-1; i++) {
            for (int j = 0; j <= inputLength - i+1; j++) {
                for (int k = 0; k <= i-1; k++) {
                    for (int l = 0; l < nonTerminals.length; l++) {
                        for (Symbol[] sa : grammar.get(nonTerminals[l])) {
                            if (sa.length == 2 && table[j][k][getIndex(sa[0])] && table[j+k][i-k][getIndex(sa[1])])
                                table[j][i][l] = true;
                        }
                    }
                }
            }
        }
        
        for (int i = 0; i < nonTerminals.length; i++) 
            if (table[0][inputLength-1][i])
                return true;
        
        return false;
    }

    private int getIndex(Symbol symbol) {
        for (int i = 0; i < nonTerminals.length; i++) {
            if (nonTerminals[i].equals(symbol))
                return i;
        }
        return -1;
    }
    
        public static void main(String[] args) throws FileNotFoundException {

            CYK_Parser parser = new CYK_Parser("grammar.txt");
            System.out.println(parser.parse("I saw her duck with a telescope"));
        }
}
