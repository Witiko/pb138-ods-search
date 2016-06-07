package cz.muni.fi.pb138.odsSearch.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.validation.SchemaFactory;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This class represents an Open Document Spreadsheet.
 * @author Matej Majer, Vít Novotný <witiko@mail.muni.cz>
 */
public final class SpreadsheetImpl implements Spreadsheet {

    private File document;
    private byte[] xmlData;

    /**
     * Constructs an internal representation of an Open Document Spreadsheet.
     * @param document the attached document.
     * @throws SpreadsheetImplException an exception encountered during the
     * preparation of the input XML document.
     */
    public SpreadsheetImpl(File document) throws SpreadsheetImplException {
        this.document = document;
        InputStream inputStream;
        
        // Produce an input stream with an XML document.
        try {
            // Is the file a ZIP archive?
            // <http://docs.oasis-open.org/office/v1.2/os/OpenDocument-v1.2-os-part1.html#OpenDocument_Document>
            ZipFile archive = new ZipFile(document);
            
            // If it is, does it contain the `content.xml` file?
            ZipEntry entry = archive.getEntry("content.xml");
            if (entry != null)
                // If so, extract `content.xml`.
                try {
                    inputStream = archive.getInputStream(entry);
                } catch(IOException e) {
                    throw new SpreadsheetImplException("An exception was "
                            + "encountered while extracting the file "
                            + "`content.xml` out of the ZIP archive.", e);
                }
            else
                throw new SpreadsheetImplException("The file `" + document +
                        "` is not an Open Document Spreadsheet. The file is a "
                        + "ZIP archive, but does not contain the `content.xml` "
                        + "file.");
        } catch (ZipException e) {
            // If it is not, read the file verbatim.
            try {
                inputStream = new FileInputStream(document);
            } catch(FileNotFoundException f) {
                throw new SpreadsheetImplException("The file `" + document +
                        "` cannot be found.", f);
            }
        } catch (IOException e) {
            throw new SpreadsheetImplException("The file `" + document +
                    "` cannot be opened.", e);            
        }
        
        /* Try to convert the XML document into an intermediary document via
           XSLT. This will also parse the input document and therefore check
           its well-formedness. */
        InputStream transformInputStream = this.getClass().
                getResourceAsStream("transform.xsl");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); 
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer(new StreamSource(
                    transformInputStream));
        } catch(TransformerConfigurationException e) {
            throw new SpreadsheetImplException("Couldn't create a XSLT "
                    + "transformer.", e);
        }
        try {
            transformer.transform(new StreamSource(inputStream),
                    new StreamResult(outputStream));
        } catch(TransformerException e) {
            throw new SpreadsheetImplException("Couldn't perform the XSLT "
                    + "transformation.", e);
        }
        xmlData = outputStream.toByteArray();
        
        // Validate the intermediary document against a XML Schema.
        URL schemaURL = this.getClass().getResource("transformed.xsd");
        SchemaFactory sf =  SchemaFactory.newInstance(
                XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema;
        try {
            schema = sf.newSchema(schemaURL);
        } catch (SAXException e) {
            throw new SpreadsheetImplException("Couldn't create a XML "
                    + "schema instance.", e);
        }
        Validator validator = schema.newValidator();
        try {
            validator.validate(new StreamSource(
                    new ByteArrayInputStream(xmlData)));
        } catch (SAXException e) {
            throw new SpreadsheetImplException("The XML Schema validation "
                    + "failed.", e);
        } catch (IOException e) {
            throw new SpreadsheetImplException("Couldn't perform the XML Schema "
                    + "validation.", e);
        }
    }
    
    /**
     * Performs an XQuery on the intermediary document and returns a set of
     * corresponding cells.
     * @param xpath the XPath object.
     * @param query the XPath expression.
     * @return the set of corresponding cells.
     */
    private Set<Cell> performXPathQuery(XPath xpath, String query) {
        
        // Perform the query.
        NodeList nodes;
        try {
            nodes = (NodeList) xpath.compile(query).evaluate(
                    new InputSource(new ByteArrayInputStream(xmlData)),
                    XPathConstants.NODESET);
        } catch(XPathExpressionException e) {
            throw new RuntimeException("There has been an error in the XPath "
                    + "expression.", e);
        }
        
        // Transform the query result to a set of cells.
        Set<Cell> cells = new HashSet<>();
        for(int i = 0; i < nodes.getLength(); ++i) {
            Element element = (Element) nodes.item(i);
            String tableName = ((Element) element.getParentNode()).
                    getAttribute("name");
            Node textNode = element.getFirstChild();
            String content = textNode != null ? textNode.getNodeValue() : "";
            int rown = Integer.parseInt(element.getAttribute("rown"));
            int coln = Integer.parseInt(element.getAttribute("coln"));
            cells.add(new Cell(this, tableName, rown, coln, content));
        }
        
        return cells;
    }

    @Override
    public Set<Cell> queryFixedString(String query,
                                      boolean caseSensitive, boolean exactMatch) {
        // Create a new XPath expression.
        XPathFactory xf = XPathFactory.newInstance();
        XPath xpath = xf.newXPath();
        String expression;
        if(caseSensitive) {
            if(exactMatch)
                expression = "//cell[./text() = $query]";
            else
                expression = "//cell[contains(./text(), $query)]";
        } else {
            if(exactMatch) 
                expression = "//cell[translate(./text(), $upper, "
                        + "$lower) = $lower]";
            else
                expression = "//cell[contains(translate(./text(), "
                        + "$upper, $lower), $lower)]";
        }
        XPathVariableResolverImpl vars = new XPathVariableResolverImpl();
        vars.setVariable("query", query);
        vars.setVariable("lower", query.toLowerCase(Locale.getDefault()));
        vars.setVariable("upper", query.toUpperCase(Locale.getDefault()));
        xpath.setXPathVariableResolver(vars);
        
        // Perform the query.
        Set<Cell> result = performXPathQuery(xpath, expression);
        Iterator<Cell> i = result.iterator();
        return performXPathQuery(xpath, expression);
    }

    @Override
    public Cell getCell(String table, int rown, int coln) {
        // Create a new XPath expression.
        XPathFactory xf = XPathFactory.newInstance();
        XPath xpath = xf.newXPath();
        String expression = "//table[@name = $table]/cell[@rown = $rown and "
                + "@coln = $coln]";
        XPathVariableResolverImpl vars = new XPathVariableResolverImpl();
        vars.setVariable("table", table);
        vars.setVariable("rown", Integer.toString(rown));
        vars.setVariable("coln", Integer.toString(coln));
        xpath.setXPathVariableResolver(vars);
        
        // Perform the query.
        Set<Cell> result = performXPathQuery(xpath, expression);
        Iterator<Cell> i = result.iterator();
        return i.hasNext() ? i.next() : null;
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
