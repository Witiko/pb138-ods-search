package cz.muni.fi.pb138.odsSearch.gui;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a column number and the related utility functions.
 * @author Vít Novotný <witiko@mail.muni.cz>
 */
final class ColumnNumber implements Comparable<ColumnNumber> {
    
    private final int coln;
    private static final Map<Integer, ColumnNumber> pool = new HashMap<>();
    
    /**
     * Creates a new column number object attached to a column number.
     * @param coln the column number.
     */
    private ColumnNumber(int coln) {
        this.coln = coln;
        pool.put(coln, this);
    }
    
    /**
     * Returns a column number object attached to a column number.
     * @param coln the column number.
     * @return a column number object attached to a column number.
     */
    public static ColumnNumber get(int coln) {
        return pool.containsKey(coln) ? pool.get(coln) : new ColumnNumber(coln);
    }
    
    /**
     * Returns the column number in base 26.
     * @return the column number in base 26.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        int num = coln;
        while (num > 0) {
          num--;
          int remainder = num % 26;
          char digit = (char) (remainder + 65);
          result.append(digit);
          num = (num - remainder) / 26;
        }
        return result.reverse().toString();
    }

    @Override
    public int compareTo(ColumnNumber that) {
        return Integer.compare(this.coln, that.coln);
    }
}
