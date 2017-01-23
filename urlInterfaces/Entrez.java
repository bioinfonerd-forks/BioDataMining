/*
 * Author: Elishai Ezra, Jerusalem College of Technology
 * Version: V1.0: Dec 1st, 2015
 * 
*/

package urlInterfaces;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import database.DataBase.DBType;
import database.Fields;
import database.Query;
import database.Fields.SearchFields;

public class Entrez {
	
	public static List<String> searchEntrez(Query query) throws MalformedURLException, IOException, InterruptedException {
		
		List<String> uilist = new ArrayList<String>();
		
		final String entrezSearchURL = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?";
		
		String queryStr = entrezSearchURL + buildQuery(query) + "&rettype=uilist";
		System.out.println("Query URL: " + queryStr);
		
		DataInputStream dataIn= new DataInputStream(
				new URL(queryStr).openStream());
		
		InputStreamReader dataStr = new InputStreamReader(dataIn);
		
		BufferedReader dataBfr = new BufferedReader(dataStr);
		
		String s = dataBfr.readLine();
		
		while (s != null){	
			if (s.startsWith("<Id>"))
				uilist.add(s.substring(4, s.length()-5));
			s = dataBfr.readLine();
		}
		
		return uilist;
	}

	public static Document callEntrez(Query query) 
			throws IOException, ParserConfigurationException, SAXException {
		
		final String entrezFetchURL  = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?";
		
		String queryStr = entrezFetchURL + buildQuery(query);
		System.out.println("Query URL: " + queryStr);
		
        URLConnection urlConn = new URL(queryStr).openConnection();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputStream IS = (urlConn.getInputStream());
		Document doc = builder.parse(IS);

		return doc;
	}
	
	private static String buildQuery(Query query){
		
		if ((query.getDataBase().getType().equals(DBType.PUBMED)) ||
		    (query.getDataBase().getType().equals(DBType.PUBMED_CENTRAL))) {
			
			String queryStr = "db=" + query.getDataBase().getPath();
			
			List<String> _ids = query.getIds();
			Fields _fields = query.getFields();
			List<String> _terms = query.getTerms();
			
			if (!_ids.isEmpty()){
				queryStr += "&id=";
				
				for (int i = 0; i < _ids.size() - 1; i++)
					queryStr += _ids.get(i) + ",";
				queryStr +=   _ids.get(_ids.size()-1);
			}
			
			if ((!_fields.isEmpty()) || (!_terms.isEmpty()))
				queryStr += "&term=";
			
			if (!_fields.isEmpty()){
				for(Map.Entry<SearchFields,String> entry : _fields.getTermsMap().entrySet()) {
					String value = entry.getValue();
					queryStr += value;
					queryStr += _fields.getField(entry.getKey()) + "+";
				}
			}
			
			if (!_terms.isEmpty()){
				for (int i = 0; i < _terms.size() - 1; i++) {
					queryStr += _terms.get(i) + "+AND+";
				}
				queryStr += _terms.get(_terms.size() - 1);
			}
			
			queryStr += "&retmode=xml";
			return queryStr;
		} else {
			System.err.println("Wrong Database");
			return null;
		}
	}

}
