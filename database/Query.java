/*
 * Author: Elishai Ezra, Jerusalem College of Technology
 * Version: V1.0: Dec 1st, 2015
 * 
*/

package database;

import java.util.ArrayList;
import java.util.List;

import database.DataBase.DBType;
import database.Fields.SearchFields;

public class Query {
	
	public enum SearchType{
		SEARCH, FETCH;
	}
	
	private DataBase _database;
	private SearchType _searchType = SearchType.FETCH;
	private List<String> _ids = new ArrayList<String>();
	private List<String> _terms = new ArrayList<String>();
	private Fields _fields = new Fields ();
	
	public void setSearchType(SearchType st) { _searchType = st;	             }
	public void setDatabase(DBType dbType)   { _database = new DataBase(dbType); }
	
	public DataBase getDataBase()     { return _database;   }
	public List<String> getIds()      { return _ids;	    }
	public SearchType getSearchType() { return _searchType;	}
	public  List<String> getTerms()   {	return _terms;   	}
	public Fields getFields()         {	return _fields;  	}
	
	public void addTerm(String term)                     { _terms.add(term);            }
	public void addId(String id)                         { _ids.add(id);   	            }
	public void addField(SearchFields key, String value) { _fields.addTerm(key, value);	}
	

	

}
