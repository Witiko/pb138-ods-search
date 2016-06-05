/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pb138.odsSearch.common;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class tests the implementation of the queryFixedString method of the
 * class cz.muni.fi.pb138.odsSearch.common.OdsFinder.
 * @author Matej Majer, Vít Novotný
 */
public class SpreadsheetImplTest {
    List<Spreadsheet> spreadsheets = new ArrayList<>();
    
    public SpreadsheetImplTest() throws SpreadsheetImplException {
        for (File f : new File[] { new File("resources/example1.ods"),
                                   new File("resources/example2.ods") } )
            spreadsheets.add(new SpreadsheetImpl(f));
    }

    /**
     * This tests performs a case sensitive exact match query.
     */
    @Test
    public void caseSensitiveExactMatch01() {
        for (Spreadsheet s : spreadsheets) {
            Set<Cell> expected = new HashSet<>();
            expected.add(new Cell(s, "IMDB Top 250 movies",
                    15, 1, "The Lord of the Rings: The Two Towers"));
            Set<Cell> returned = s.queryFixedString(
                    "The Lord of the Rings: The Two Towers", true, true);
            Assert.assertTrue(expected.equals(returned));
        }
    }

    @Test
    public void caseSensiveExatcMatch02() {
        for (Spreadsheet s : spreadsheets) {
            Set<Cell> expected = new HashSet<>();
            expected.add(new Cell(s, "MLB 2016 Standings", 11, 2, "Detroit Tigers"));
            Set<Cell> returned = s.queryFixedString(
                    "Detroit Tigers", true, true);
            Assert.assertTrue(expected.equals(returned));
        }
    }

    @Test
    public void caseSensiveExatcMatch03() {
        for (Spreadsheet s : spreadsheets) {
            Set<Cell> expected = new HashSet<>();
            expected.add(new Cell(s, "Popular baby names", 7, 3, "Mia"));
            Set<Cell> returned = s.queryFixedString(
                    "Mia", true, true);
            Assert.assertTrue(expected.equals(returned));
        }
    }

    /**
     * This tests performs a case sensitive partial match query.
     */
    @Test
    public void caseSensivePartialMatch01() {
        for (Spreadsheet s : spreadsheets) {
            Set<Cell> expected = new HashSet<>();
            expected.add(new Cell(s, "MLB 2016 Standings", 7, 2, "Cleveland Indians"));
            expected.add(new Cell(s, "IMDB Top 250 movies", 102, 1, "Indiana Jones and the Last Crusade"));
            Set<Cell> returned = s.queryFixedString(
                    "Indian", true, false);
            Assert.assertTrue(expected.equals(returned));
        }
    }

    @Test
    public void caseSensivePartialMatch02() {
        for (Spreadsheet s : spreadsheets) {
            Set<Cell> expected = new HashSet<>();
            expected.add(new Cell(s, "IMDB Top 250 movies", 9, 1, "The Good, the Bad and the Ugly"));
            expected.add(new Cell(s, "IMDB Top 250 movies", 17, 1, "Goodfellas"));
            expected.add(new Cell(s, "IMDB Top 250 movies", 115, 1, "Good Will Hunting"));
            Set<Cell> returned = s.queryFixedString(
                    "Good", true, false);
            Assert.assertTrue(expected.equals(returned));
        }
    }

    @Test
    public void caseSensivePartialMatch03() {
        for (Spreadsheet s : spreadsheets) {
            Set<Cell> expected = new HashSet<>();
            expected.add(new Cell(s, "IMDB Top 250 movies", 168, 1, "Mary a Max"));
            expected.add(new Cell(s, "IMDB Top 250 movies", 227, 1, "The Martian"));
            expected.add(new Cell(s, "MLB 2016 Standings", 3, 2, "Seattle Mariners"));
            Set<Cell> returned = s.queryFixedString(
                    "Mar", true, false);
            Assert.assertTrue(expected.equals(returned));
        }
    }

    /**
     * This tests performs a case insensitive exact match query.
     */
    @Test
    public void caseInsensitiveExactMatch01() {
        for (Spreadsheet s : spreadsheets) {
            Set<Cell> expected = new HashSet<>();
            expected.add(new Cell(s, "Popular baby names", 4, 2, "Alexander"));
            expected.add(new Cell(s, "Popular baby names", 9, 2, "ALEXANDER"));
            Set<Cell> returned = s.queryFixedString(
                    "Alexander", false, true);
            Assert.assertTrue(expected.equals(returned));
        }
    }

    @Test
    public void caseInsensitiveExactMatch02() {
        for (Spreadsheet s : spreadsheets) {
            Set<Cell> expected = new HashSet<>();
            expected.add(new Cell(s, "IMDB Top 250 movies", 114, 1, "Up"));
            Set<Cell> returned = s.queryFixedString(
                    "up", false, true);
            Assert.assertTrue(expected.equals(returned));
        }
    }

    @Test
    public void caseInsensitiveExactMatch03() {
        for (Spreadsheet s : spreadsheets) {
            Set<Cell> expected = new HashSet<>();
            expected.add(new Cell(s, "Popular baby names", 4, 2, "Alexander"));
            expected.add(new Cell(s, "Popular baby names", 9, 2, "ALEXANDER"));
            Set<Cell> returned = s.queryFixedString(
                    "ALEXandeR", false, true);
            Assert.assertTrue(expected.equals(returned));
        }
    }
    /**
     * This tests performs a case insensitive partial match query.
     */
    @Test
    public void caseInsensitivePartialMatch01() {
        for (Spreadsheet s : spreadsheets) {
            Set<Cell> expected = new HashSet<>();
            expected.add(new Cell(s, "IMDB Top 250 movies", 230, 1, "Fanny and Alexander"));
            expected.add(new Cell(s, "Popular baby names", 4, 2, "Alexander"));
            expected.add(new Cell(s, "Popular baby names", 9, 2, "ALEXANDER"));
            Set<Cell> returned = s.queryFixedString(
                    "ALEXANDER", false, false);
            Assert.assertTrue(expected.equals(returned));

        }
    }

    @Test
    public void caseInsensitivePartialMatch02() {
        for (Spreadsheet s : spreadsheets) {
            Set<Cell> expected = new HashSet<>();
            expected.add(new Cell(s, "IMDB Top 250 movies", 50, 1,
                    "Dr. Strangelove or: How I Learned to Stop Worrying and Love the Bomb"));
            expected.add(new Cell(s, "MLB 2016 Standings", 13, 2, "Los Angeles Angels"));
            Set<Cell> returned = s.queryFixedString(
                    "AnGEl", false, false);
            Assert.assertTrue(expected.equals(returned));

        }
    }

    @Test
    public void caseInsensitivePartialMatch03() {
        for (Spreadsheet s : spreadsheets) {
            Set<Cell> expected = new HashSet<>();
            expected.add(new Cell(s, "IMDB Top 250 movies", 2, 1, "The Godfather"));
            expected.add(new Cell(s, "IMDB Top 250 movies", 3, 1, "The Godfather: Part II"));
            Set<Cell> returned = s.queryFixedString(
                    "godFather", false, false);
            Assert.assertTrue(expected.equals(returned));

        }
    }

    /**
     * This tests performs search using getCell.
     */
    @Test
    public void getCellTest01() {
        for (Spreadsheet s : spreadsheets) {
            Cell expected  = new Cell(s, "IMDB Top 250 movies", 25, 1, "The Usual Suspects");
            Cell returned = s.getCell("IMDB Top 250 movies", 25, 1);
            System.out.println("Expected: " + expected.toString());
            System.out.println("Returned: " + returned.toString());
            Assert.assertTrue(expected.equals(returned));
        }
    }

    @Test
    public void getCellTest02() {
        for (Spreadsheet s : spreadsheets) {
            Cell expected  = new Cell(s, "MLB 2016 Standings", 11, 2, "Detroit Tigers");
            Cell returned = s.getCell("IMDB Top 250 movies", 11, 2);
            System.out.println("Expected: " + expected.toString());
            System.out.println("Returned: " + returned.toString());
            Assert.assertTrue(expected.equals(returned));
        }
    }

    @Test
    public void getCellTest03() {
        for (Spreadsheet s : spreadsheets) {
            Cell expected  = new Cell(s, "Popular baby names", 2, 3, "Olivia");
            Cell returned = s.getCell("Popular baby names", 2, 3);
            System.out.println("Expected: " + expected.toString());
            System.out.println("Returned: " + returned.toString());
            Assert.assertTrue(expected.equals(returned));
        }
    }
}
