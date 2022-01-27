/**
 * class representing a terminal or non-terminal symbol
 * @author Valentin
 */

package CYK_Parser;

public class Symbol {
    
    public static final boolean NT = false;
    public static final boolean T = true;
    
    private String symbol;
    private boolean isTerminal;
    
    /**
     * constructor taking the symbol itself and information if this
     * is a terminal or non-terminal
     * @param aSymbol the symbol to set
     * @param t information if the symbol is a terminal or a non-terminal
     */
    public Symbol(String aSymbol, boolean t) {
        symbol = aSymbol;
        isTerminal = t;
    }
    
    /**
     * @return the symbol
     */
    public String getSymbol () {
        return symbol;
    }
    
    /**
     * @param aSymbol the symbol to set
     */
    public void setSymbol (String aSymbol) {
        symbol = aSymbol;
    }
    
    /**
     * @return true if this is a terminal false if not
     */
    public boolean isTerminal () {
        return isTerminal;
    }
    
    /**
     * @return string representation of this symbol
     */
    public String toString() {
        return symbol;
    }
    
        /**
     * equals method
     * @param obj other object to compare this one with
     * @return true if they are the same false if not
     */
    @Override
    public boolean equals (Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (obj.getClass() != this.getClass())
            return false;
        Symbol other = (Symbol)obj;
        if (!(this.symbol.equals(other.symbol)) || this.isTerminal != other.isTerminal)
            return false;
        return true;
    }
    
    /**
     * @return hash code for this symbol
     */
    public int hashCode() {
        return symbol.hashCode();
    }
    
    /**
     * setter method for type of symbol
     * @param t type to set to
     */
    void setType(boolean t) {
        isTerminal = t;
    }
    
}
