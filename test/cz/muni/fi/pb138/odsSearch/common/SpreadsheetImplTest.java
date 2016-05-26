/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pb138.odsSearch.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.junit.Assert;
import org.junit.Test;

/**
 * This class tests the implementation of the queryFixedString method of the
 * class cz.muni.fi.pb138.odsSearch.common.OdsFinder.
 * @author Vít Novotný
 */
public class SpreadsheetImplTest {
    List<Spreadsheet> spreadsheets = new ArrayList<>();
    
    public SpreadsheetImplTest() throws IOException {
        for (File f : new File[] { new File("resources/example1.ods"),
                                   new File("resources/example2.ods") } )
            spreadsheets.add(new SpreadsheetImpl(f));
    }

    /**
     * This tests performs a case sensitive exact match query.
     */
    @Test
    public void caseSensitiveExactMatch01() {
        for (Spreadsheet f : spreadsheets) {
            Set<Cell> expected = new TreeSet<>();
            expected.add(new Cell(f, "IMDB Top 250 movies",
                    14, 0, "The Lord of the Rings:The Two Towers"));
            Set<Cell> returned = f.queryFixedString(
                    "The Lord of the Rings:The Two Towers", true, true);
            Assert.assertTrue(expected.equals(returned));
        }
    }

    /*

       Other case sensitive, exact matches:

         1) Check that querying "Detroit Tigers" returns a single cell
            inside the "MLB 2016 Standings" table.
         2) Check that querying "Mia" returns a single cell inside
            the "Popular baby names" table.

       Case sensitive partial matches:

         1) Check that querying "Indian" returns the cell "Indiana Jones
            and the Last Crusade" from the "IMDB Top 250 movies" table,
            and the cell "Cleveland Indians" from the "MLB 2016 Standings"
            table.
    
        Case insensitive exact matches:
    
         1) Check that querying "Alexander" returns the cells "Alexander"
            and "ALEXANDER" from the "Popular baby names" table.
    
        Case insensitive partial matches:
    
         1) Check that querying "Alexander" returns the cells "Alexander"
            and "ALEXANDER" from the "Popular baby names" table, and the
            "Fanny and Alexander" cell from the "IMDB Top 250 movies" table.

    */
    
}
