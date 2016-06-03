import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import javafx.scene.control.Cell;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.lang.model.element.Element;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.nio.file.FileSystem;
import java.util.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.ZipEntry;

/**
 * This class represents an Open Document Spreadsheet.
 * @author Matej Majer
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

            ZipEntry content = archive.getEntry("content.xml");
            if(content != null) document = content;
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

        //transformation
        private File transformed = new File("transformed.xml");
        try {
            TransformerFactory tf = TransformerFactory.newInstance();

            Transformer transformer = tf.newTransformer(
                    new StreamSource(new File("transform.xsl")));

            transformer.transform(
                    new StreamSource(new File(document)),
                    new StreamResult(transformed));
        } catch(TransformerException e) {

        }
            //validation
        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File("transformed.xsd"));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(transformed)));
        } catch (Exception e) {

        }


        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Override
    public Set<Cell> queryFixedString(String query,
                                      boolean caseSensitive, boolean exactMatch) {
        private XPath xpath = XPathFactory.newInstance().newXPath();
        String expression;

        if(caseSensitive == false) {
            String upCase = query.toUpperCase();
            String lowCase = query.toLowerCase();
            if(exactMatch == true) {
                expression = "//table/cell[translate(.,upCase,lowCase) = lowCase]";
            } else {

                expression = "//table/cell[text()[contains(translate(.,upCase,lowCase) = lowCase)]";
            }
        }
        else {
            if(exactMatch == false) {
                expression = "//table/cell[contains(.,query)"
            } else {
                expression = "//table/cell[text() = query"
            }
        }

        NodeList nodes = xpath.compile(expression).evaluate(document, XPathConstants.NODESET);
        Set<Cell> cells = new HashSet<>();
        int row;
        int col;
        String content;
        String delims = ""
        for(int i = 0; i < nodes.getLength(); ++i) {
            org.w3c.dom.Element element = (org.w3c.dom.Element) nodes.item(i);
            String tableName = element.getParentNode().getNodeName();
            String content = element.getNodeValue();

            row = element.getAttribute("rown");
            col = element.getAttribute("coln");
            cells.add(new Cell(document, tableName, row, col, content));

        }

        throw new UnsupportedOperationException("Not Yet Implemented.");
        return cells;

        /*
           Use XPath to retrieve a cell with the given text content.
           for (query,1,0): //table/cell[contains(.,'query')]
           for (query,1,1): //table/cell[text()='query']
           XPath 1.0:
            for ('THe godFather',0,1): //table/cell[translate(.,'THE GODFATHER','the godfather')='the godfather']
            for ('GodfaTher',0,0): //table/cell[text()[contains(translate(.,'GODFATHER','godfather'),'godfather')]]
           XPath 2.0:
            for ('THe godFather',0,1): //table/cell[matches(.,'THe godFather','i')]
            for ('GodfaTher',0,0): //table/cell[contains(lower-case(.),'godfather')]
        */

    }

    @Override
    public Cell getCell(String table, int rown, int coln) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        String expression = "//table[@name = table]/cell[@rown = rown and @coln = coln]";
        /*
            Use XPath to retrieve a cell in the given table at the given rown and coln.
            for ("Top list", 10, 1): //table[@name='Top list']/cell[@rown=10 and @coln=1]
        */
        org.w3c.dom.Element element = (org.w3c.dom.Element) xpath.compile(expression).evaluate(document, XPathConstants.NODE);
        throw new UnsupportedOperationException("Not Yet Implemented.");

        return new Cell(document, table, row, col, element.getNodeValue());


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
