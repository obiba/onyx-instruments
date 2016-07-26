/*******************************************************************************
 * Copyright (c) 2016 OBiBa. All rights reserved.
 *  
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *  
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.obiba.onyx.jade.instrument.ndd;

import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 */
public class CommandDataExtractor extends XMLDataExtractor<CommandData> {

  public CommandDataExtractor(XPath xpath, Document doc) {
    super();
    init(xpath, doc);
  }

  /**
   * Extracts participant information data
   */
  public CommandData extractData() throws XPathExpressionException {
    CommandData data = new CommandData();
    data.setType(extractAttributeValue("//Command", "Type"));
    Map<String, String> parameters = new HashMap<>();
    NodeList nodes = (NodeList)xpath.evaluate("//Command/Parameter", doc, XPathConstants.NODESET);

    for (int i =0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      parameters.put(node.getAttributes().getNamedItem("Name").getNodeValue(), node.getTextContent());
    }

    data.setParameters(parameters);

    return data;
  }

}
