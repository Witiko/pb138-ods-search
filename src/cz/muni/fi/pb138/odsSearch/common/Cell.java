package cz.muni.fi.pb138.odsSearch.common;

/**
 * This immutable class represents a cell within a spreadsheet.
 * @author Vít Novotný <witiko@mail.muni.cz>
 */
public class Cell {
    
    private Spreadsheet spreadsheet;
    private String table;
    private int rown;
    private int coln;
    private String content;
    
    /**
     * Constructs a cell that carries a reference to its containing document,
     * a table within the document, the coordinates within the table, and its
     * content.
     * @param spreadsheet the spreadsheet that contains the table.
     * @param table the table that contains the cell.
     * @param rown the number of the row that contains the cell.
     * @param coln the number of the column that contains the cell.
     * @param content the text content of the cell.
     */
    public Cell(Spreadsheet spreadsheet, String table, int rown, int coln, String content) {
        // Check the invariants.
        if (spreadsheet == null)
            throw new IllegalArgumentException("The spreadsheet must be non-null.");
        if (table == null)
            throw new IllegalArgumentException("The table name must be non-null.");
        if (rown < 0)
            throw new IllegalArgumentException("The row number must be non-negative.");
        if (coln < 0)            
            throw new IllegalArgumentException("The column number must be non-negative.");
        if (content == null)
            throw new IllegalArgumentException("The cell content must be non-null.");
        // Perform the assignment.
        this.spreadsheet = spreadsheet;
        this.table = table;
        this.rown = rown;
        this.coln = coln;
        this.content = content;
    }
    
    /**
     * Returns the source document.
     * @return the source document.
     */
    public Spreadsheet getSpreadsheet() {
        return spreadsheet;
    }
    
    /**
     * Returns the table name.
     * @return the table name.
     */
    public String getTable() {
        return table;
    }
    
    /**
     * Returns the row number.
     * @return the row number.
     */
    public int getRowNumber() {
        return rown;
    }
    
    /**
     * Returns the column number.
     * @return the column number.
     */
    public int getColumnNumber() {
        return coln;
    }
    
    /**
     * Converts a number from base 10 to base 26.
     * @param num the input number.
     * @return the number in base 26.
     */
    private String base10to26(int num) {
        StringBuilder result = new StringBuilder();
        while (num > 0) {
          num--;
          int remainder = num % 26;
          char digit = (char) (remainder + 65);
          result.append(digit);
          num = (num - remainder) / 26;
        }
        return result.reverse().toString();
    }
    
    /**
     * Returns the column name in base 26.
     * @return the column name in base 26.
     */
    public String getColumnString() {
        return base10to26(coln);
    }
    
    /**
     * Returns the cell content.
     * @return the cell content.
     */
    public String getContent() {
        return content;
    }
    
    @Override
    public int hashCode() {
        return spreadsheet.hashCode() + table.hashCode() +
                rown + coln + content.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Cell))
            return false;
        else {
            Cell that = (Cell)obj;
            return this.spreadsheet.equals(that.spreadsheet) && 
                   this.table.equals(that.table) &&
                   this.rown == that.rown && this.coln == that.coln &&
                   this.content.equals(that.content);
        }
    }
    
    @Override
    public String toString() {
        return "[Spreadsheet " + spreadsheet + ", table " + table +
                ", row " + rown + ", column " + getColumnString() + ": " +
                content + "]";
    }
    
}
