package cz.muni.fi.pb138.odsSearch.common;

/**
 * This interface represents a spreadsheet that can be queried for cells
 * using full-text string search and using coordinates.
 * @author Vít Novotný <witiko@mail.muni.cz>
 */
public interface Spreadsheet extends Queriable<Cell> {

    /**
     * This method returns a cell of this spreadsheet in the specified table at
     * the specified row and column.
     * @param table the table name.
     * @param rown the row number.
     * @param coln the column number.
     * @return a cell of this table at the specified coordinates.
     */
    public Cell getCell(String table, int rown, int coln);

}
