package cz.muni.fi.pb138.odsSearch.common;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * This class represents a finder attached to a single Open Document
 * Spreadsheet. This finder provides methods to query the document cells.
 * @author 
 */
public class OdsFinder implements Queriable<Cell> {
    
    File document;
    
    /**
     * Constructs a finder attached to a document.
     * @param document the attached document.
     */
    public OdsFinder(File document) throws IOException {
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
        throw new UnsupportedOperationException("Not Yet Implemented.");
        /*
           2) Validate the document against the Open Document Relax NG schema (stored in the file schema.rng):
              See task 2 and <http://stackoverflow.com/questions/1541253/how-to-validate-an-xml-document-using-a-relax-ng-schema-and-jaxp>.
        
           Remark:
              Major office suites (Google Documents, Libre Office) produce so-called Extended Open Documents:
              <http://docs.oasis-open.org/office/v1.2/os/OpenDocument-v1.2-os-part1.html#__RefHeading__440348_826425813>
              These may contain foreign elements and attributes that make the documents invalid with respect to the Relax NG schema.
              Therefore, we will (probably) just make a well-formedness check here in the final version, so that the tool does not reject these documents.
        
           3) Check that this document is an Open Document Spreadsheet:
              <http://docs.oasis-open.org/office/v1.2/os/OpenDocument-v1.2-os-part1.html#a2_2_4OpenDocument_Spreadsheet_Document>
        */
    }
    
    /**
     * Returns the document attached to the finder.
     * @return the document attached to the finder.
     */
    public File getDocument() {
        return document;
    }
    
    @Override
    public Set<Cell> queryFixedString(String query,
            boolean caseSensitive, boolean exactMatch) {
        /*
           It may be appropriate to use XPath here (see the Saxon library)
           rather than just walking over all the O(n) cells in a DOM tree.
        */
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }
    
}
