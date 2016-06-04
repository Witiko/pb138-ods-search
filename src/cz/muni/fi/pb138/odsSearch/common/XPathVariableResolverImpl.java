package cz.muni.fi.pb138.odsSearch.common;

import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPathVariableResolver;

/**
 * This class provides a mapping from XPath variables to strings without
 * any injection vulenrability.
 * @author Vít Novotný <witiko@mail.muni.cz>
 */
class XPathVariableResolverImpl implements XPathVariableResolver {
  Map<String, String> mapping = new HashMap<>();

  /**
   * Sets up a new variable mapping.
   * @param name ẗhe name of the XPath variable.
   * @param value the value of the XPath variable. 
   */
  public void setVariable(String name, String value)  {
    mapping.put(name, value);
  }

  @Override
  public Object resolveVariable(QName name) {
    String key = name.getLocalPart();
    return mapping.get(key);
  }
}
