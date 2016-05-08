package cz.muni.fi.pb138.odsSearch.common;

import java.io.File;

/**
 * This immutable class represents a cell within a spreadsheet.
 * @author Vít Novotný <witiko@mail.muni.cz>
 */
public class Cell {
    
    private File document;
    private String table;
    private int row;
    private int column;
    private String content;
    
    /**
     * Constructs a cell that carries a reference to its containing document,
     * a table within the document, the coordinates within the table, and its
     * content.
     * @param document the document that contains the cell.
     * @param table the table that contains the cell.
     * @param row the row that contains the cell.
     * @param column the column that contains the cell.
     * @param content the text content of the cell.
     */
    public Cell(File document, String table, int row, int column, String content) {
        // Check the invariants.
        if (document == null)
            throw new IllegalArgumentException("The document must be non-null.");
        if (table == null)
            throw new IllegalArgumentException("The table name must be non-null.");
        if (row < 0)
            throw new IllegalArgumentException("The row number must be non-negative.");
        if (column < 0)            
            throw new IllegalArgumentException("The column number must be non-negative.");
        if (content == null)
            throw new IllegalArgumentException("The cell content must be non-null.");
        // Perform the assignment.
        this.document = document;
        this.table = table;
        this.row = row;
        this.column = column;
        this.content = content;
    }
    
    /**
     * Returns the source document.
     * @return the source document.
     */
    public File getDocument() {
        return document;
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
    public int getRow() {
        return row;
    }
    
    /**
     * Returns the column number.
     * @return the column number.
     */
    public int getColumn() {
        return column;
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
        return document.hashCode() + table.hashCode() +
                row + column + content.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Cell))
            return false;
        else {
            Cell that = (Cell)obj;
            return this.document.equals(that.document) && 
                    this.table.equals(that.table) &&
                    this.row == that.row && this.column == that.column &&
                    this.content.equals(that.content);
        }
    }
    
    @Override
    public String toString() {
        return "[Document " + document + ", table " + table + ", row " + row +
                "column " + column + ": " + content + "]";
    }
    
}
