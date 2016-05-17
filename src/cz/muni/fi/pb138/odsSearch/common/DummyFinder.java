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
public class DummyFinder implements Queriable<Cell> {
    
    File document;
    
    /**
     * Constructs a finder attached to a document.
     * @param document the attached document.
     */
    public DummyFinder(File document) {
        this.document = document;
    }

    @Override
    public Set<Cell> queryFixedString(String query, boolean caseSensitive, boolean exactMatch) {
        return new HashSet<>(Arrays.asList(new Cell[] {
            new Cell(document, "fooTable", 123, 456, query + ", caseSensitive: " +
                    caseSensitive + ", exactMatch: " + exactMatch)
        }));
    }
    
}
