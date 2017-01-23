/*
 * Author: Elishai Ezra, Jerusalem College of Technology
 * Version: V1.0: Dec 1st, 2015
 * 
 * This is a naive implementation of the Database class
 * that only supports MalaCards, Biomodels and NCBI's pubmed and PMC databases
 * 
*/

package database;

public class DataBase {
	
	public enum DBType {
		PUBMED, PUBMED_CENTRAL, 
		MALA_CARDS, BIO_MODELS;
	}
	
	private DBType _dbType;
	
	public DataBase (DBType dbType){ _dbType = dbType;	}
	
	public String getPath(){
		if      (_dbType.equals(DBType.PUBMED))         {return "pubmed"; }
		else if (_dbType.equals(DBType.PUBMED_CENTRAL)) {return "pmc"   ; }
		else if (_dbType.equals(DBType.MALA_CARDS))     {return "http://malacards.org/search/results/"; }
		else if (_dbType.equals(DBType.BIO_MODELS))		{return "http://www.ebi.ac.uk/biomodels-main/";	}
		else return null;
	}
	
	public DBType getType(){ return _dbType; }
	
}
