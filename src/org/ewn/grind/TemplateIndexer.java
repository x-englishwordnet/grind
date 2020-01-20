package org.ewn.grind;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TemplateIndexer
{
	// sense_key num_template[,num_template]*

	/**
	 * Xpath for verb sense elements
	 */
	protected static final String VERB_LEXENTRY_XPATH = '/' + XmlNames.LEXICALRESOURCE_TAG + '/' + XmlNames.LEXICON_TAG + '/' + XmlNames.LEXICALENTRY_TAG + "[" + XmlNames.LEMMA_TAG + "/@" + XmlNames.POS_ATTR + "=\"v\"]";

	private Document doc;

	public TemplateIndexer(Document doc)
	{
		super();
		this.doc = doc;
	}

	/**
	 * Make index.sense
	 * 
	 * @param ps print stream
	 * @throws XPathExpressionException
	 */
	public void makeIndex(PrintStream ps) throws XPathExpressionException
	{
		ArrayList<String> lines = new ArrayList<>();

		NodeList lexEntryNodes = XmlUtils.getXPathNodeList(VERB_LEXENTRY_XPATH, doc);
		int m = 0;
		int n = lexEntryNodes.getLength();
		for (int i = 0; i < n; i++)
		{
			Node lexEntryNode = lexEntryNodes.item(i);
			assert lexEntryNode.getNodeType() == Node.ELEMENT_NODE;
			Element lexEntryElement = (Element) lexEntryNode;

			List<Element> senseElements = XmlUtils.getChildElements(lexEntryElement, XmlNames.SENSE_TAG);
			for (Element senseElement : senseElements)
			{
				String sensekey = senseElement.getAttribute(XmlNames.SENSEKEY_ATTR);
				String templateList = senseElement.getAttribute(XmlNames.SENTENCE_TEMPLATE_ATTR);
				if (templateList.isEmpty())
					continue;
				templateList = templateList.replace("ewn-st-", "");
				String[] templates = templateList.split("\\s+");
				String line = String.format("%s %s", sensekey, Formatter.join(templates, ','));
				lines.add(line);
				m++;
			}
		}
		Collections.sort(lines);
		for (String line : lines)
		{
			ps.println(line);
		}
		System.err.println("Senses: " + m);
	}

	public static void main(String[] args) throws SAXException, ParserConfigurationException, IOException, XPathExpressionException
	{
		// Timing
		final long startTime = System.currentTimeMillis();

		// Input
		String filename = args[0];

		// XML document
		Document doc = XmlUtils.getDocument(filename, false);

		// Process
		TemplateIndexer indexer = new TemplateIndexer(doc);
		indexer.makeIndex(System.out);

		// Timing
		final long endTime = System.currentTimeMillis();
		System.err.println("Total execution time: " + (endTime - startTime) / 1000 + "s");
	}
}