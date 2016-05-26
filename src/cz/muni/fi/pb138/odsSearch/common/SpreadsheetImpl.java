package cz.muni.fi.pb138.odsSearch.common;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * This class represents an Open Document Spreadsheet.
 * @author 
 */
public class SpreadsheetImpl implements Spreadsheet {
    
    private File document;
    
    /**
     * Constructs an internal representation of an Open Document Spreadsheet.
     * @param document the attached document.
     * @throws IOException an exception encountered during the preparation
     * of the input XML document.
     */
    public SpreadsheetImpl(File document) throws IOException {
        this.document = document;
        /*
          1) Test, whether the Open Document is an archive (such as example1.ods),
             or a single XML file (such as example2.ods) as per the specification:
             <http://docs.oasis-open.org/office/v1.2/os/OpenDocument-v1.2-os-part1.html#OpenDocument_Document>
        */
        try {
            ZipFile archive = new ZipFile(document);
            // ...
            // The file is a ZIP archive.
        } catch (ZipException e) {
            // The file is a XML document.
        }
        /*
          2) Convert the document to an intermediary format via XSLT.
          3) Validate the intermediary document against a XML Schema.
          4) Store the intermediary document into an instance variable, so that
             it can be used by the queryFixedString and getCell methods.
        */
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }
    
    @Override
    public Set<Cell> queryFixedString(String query,
            boolean caseSensitive, boolean exactMatch) {
        /*
           Use XPath to retrieve a cell with the given text content.
        */
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Override
    public Cell getCell(String table, int rown, int coln) {
        /*
            Use XPath to retrieve a cell in the given table at the given rown and coln.
        */
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }
    
    @Override
    public int hashCode() {
        return document.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof SpreadsheetImpl))
            return false;
        else {
            SpreadsheetImpl that = (SpreadsheetImpl)obj;
            return this.document.equals(that.document);
        }
    }
    
    @Override
    public String toString() {
        return document.toString();
    }
    
}
