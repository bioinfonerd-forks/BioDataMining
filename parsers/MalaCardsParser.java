/*
 * Author: Elishai Ezra, Jerusalem College of Technology
 * Version: V1.0: Dec 1st, 2015
 * 
*/

package parsers;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import persistentdatabase.model.Disease;
import database.Query;

public class MalaCardsParser {
	
	private Document _doc;
	private Query _query;
	private List<Disease> _diseases;
	
	public MalaCardsParser (Document doc, Query query){
		_doc = doc;
		_query = query;
		_diseases = new ArrayList<Disease>();
	}
	
	public List<Disease> getDiseases() { return _diseases; }
	
	public void parse() {

		// get page title
		String title = _doc.title();
		System.out.println("title : " + title);

		String term = _query.getTerms().get(0);
		final int LIMIT = 20;
		int count = 0;
		
		// get all links
		Elements links = _doc.select("a[href]");
		for (Element link : links) {
			if ( link.attr("href").contains(term) && (count<LIMIT)){
				Disease disease = new Disease();
				disease.setURLlink( link.attr("href"));
				disease.setName(link.text());
				_diseases.add(disease);
				count++;
			}	
		}
	  }
}
