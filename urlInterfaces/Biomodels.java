/*
 * Author: Elishai Ezra, Jerusalem College of Technology
 * Version: V1.0: Dec 1st, 2015
 * 
*/

package urlInterfaces;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import database.Query;

public class Biomodels {

	public static List<Document> callBiomodels(Query query) 
			throws IOException, ParserConfigurationException, SAXException {
		
		final String mainURL = query.getDataBase().getPath();
		final String biomodelsFetchURL  = "download?mid=";
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		List<Document> docs = new ArrayList<Document>();
		
		List<String> _terms = query.getIds();

		for (int i =0; i < _terms.size(); i++){
			
			String queryStr = mainURL + biomodelsFetchURL;
			queryStr += _terms.get(i);
			
			System.out.println("Query URL: " + queryStr);				
	        URLConnection urlConn = new URL(queryStr).openConnection();
	        
	        docs.add(builder.parse(urlConn.getInputStream()));

		}
		
		return docs;
	}
}
