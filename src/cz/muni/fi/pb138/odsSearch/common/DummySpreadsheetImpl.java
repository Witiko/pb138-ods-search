package cz.muni.fi.pb138.odsSearch.common;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is a mock up of the OdsFinder class for the purpose of the
 * testing of the gui.MainFrame and cli.Main classes.
 * @author Vít Novotný <witiko@mail.muni.cz>
 */
public final class DummySpreadsheetImpl implements Spreadsheet {
    
    File document;
    
    /**
     * Constructs a finder attached to a document.
     * @param document the attached document.
     */
    public DummySpreadsheetImpl(File document) {
        if (document == null)
            throw new IllegalArgumentException("The document must be non-null.");
        this.document = document;
    }

    @Override
    public Set<Cell> queryFixedString(String query,
            boolean caseSensitive, boolean exactMatch) {
        return new HashSet<>(Arrays.asList(new Cell[] {
            new Cell(this, "fooTable", 123, 456, query + ", caseSensitive: " +
                    caseSensitive + ", exactMatch: " + exactMatch)
        }));
    }

    @Override
    public Cell getCell(String table, int rown, int coln) {
        return new Cell(this, table, rown, coln, "fooContent");
    }
    
    @Override
    public int hashCode() {
        return document.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof DummySpreadsheetImpl))
            return false;
        else {
            DummySpreadsheetImpl that = (DummySpreadsheetImpl)obj;
            return this.document.equals(that.document);
        }
    }
    
    @Override
    public String toString() {
        return document.toString();
    }
    
}
