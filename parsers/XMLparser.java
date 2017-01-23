/*
 * Author: Elishai Ezra, Jerusalem College of Technology
 * Version: V1.0: Dec 1st, 2015
 * 
*/

package parsers;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class XMLparser {

	protected List<Document> _doc;
	
	public abstract void parse();
	
	protected List<Element> getChildrensByName(Element el, String childName) {
		NodeList children = el.getChildNodes();
		List<Element> elements = new ArrayList<Element>(28);
		for(int i=0; i<children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals(childName) ) {
				elements.add( (Element)child );
			}
		}
		return elements;
	}
	
	protected Element getChildByName(Element el, String childName) {
		NodeList children = el.getChildNodes();
		for(int i=0; i<children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals(childName) ) {
				return (Element)child;
			}
		}
		return null;
	}
	
	protected  String getTextContent(Element el) {
		String answer = "";
		NodeList children = el.getChildNodes();
		for(int i=0; i<children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.TEXT_NODE) {
				answer += child.getNodeValue();
			}
		}
		return answer;
	}

	
}
