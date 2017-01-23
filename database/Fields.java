/*
 * Author: Elishai Ezra, Jerusalem College of Technology
 * Version: V1.0: Dec 1st, 2015
 * 
 * This is a naive implementation of the Fields class
 * that only supports Fields oriented searches for NCBI's pubmed and PMC databases
 * 
*/

package database;

import java.util.Map;
import java.util.TreeMap;

public class Fields{
	
	public enum SearchFields {
		JOURNAL, PUBLICATION_DATA;
	}
	
	private Map<SearchFields, String> fieldsDictionary = new TreeMap<SearchFields, String>();
	private Map<SearchFields, String> termsMap = new TreeMap<SearchFields, String>();
	
	public Fields(){
		fieldsDictionary.put(SearchFields.JOURNAL, "[journal]");
		fieldsDictionary.put(SearchFields.PUBLICATION_DATA, "[pdat]");
	}
	
	public void addTerm (SearchFields key, String value) { termsMap.put(key, value);  }
	
	public Map<SearchFields, String> getTermsMap() { return termsMap;                 }
	public String getField (SearchFields sf)       { return fieldsDictionary.get(sf); }
	public boolean isEmpty()                       { return termsMap.isEmpty();       }
}


