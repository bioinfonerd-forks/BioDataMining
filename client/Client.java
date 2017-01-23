/*
 * Author: Elishai Ezra, Jerusalem College of Technology
 * Version: V1.0: Dec 1st, 2015
 * 
 * Before you begin, please add the following jar files to
 * your project's build path:
 * -> commons-csv-1.2.jar           @ https://commons.apache.org/proper/commons-csv/
 * -> derby.jar                     @ https://db.apache.org/derby/
 * -> eclipselink.jar     			@ https://eclipse.org/eclipselink/downloads/
 * -> javax.persistence_2.1.0.jar   @ http://mvnrepository.com/artifact/org.eclipse.persistence/javax.persistence/2.1.0
 * -> jsoup-1.8.3.jar				@ http://jsoup.org/download
 * 
 * The following modification to the persistence.xml (@ META-INF folder)
 * are also needed:
 * -> change the value of the property: 'javax.persistence.jdbc.url' to
 *    your project path 
 * 
 * If this framework is used for the construction of a new databse
 * please note the following:
 * -> modify the classes' list in the persistence.xml file (META-INF folder)
 *    to consist your entity classes
 * -> each of your entity classes has to be annotated with @<Entity name>, 
 *    and implement the 'Persistable' class
 * 
 */

package client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.util.List;

import org.w3c.dom.Document;

import database.DataBase.DBType;
import database.Fields.SearchFields;
import database.Query;
import database.Query.SearchType;
import urlInterfaces.Biomodels;
import urlInterfaces.Entrez;
import urlInterfaces.MalaCards;
import parsers.BiomodelsParser;
import parsers.CSVparser;
import parsers.MalaCardsParser;
import parsers.PubmedParser;
import persistentdatabase.main.PersistAgent;
import persistentdatabase.model.Aneurysm;
import persistentdatabase.model.Article;
import persistentdatabase.model.Disease;
import persistentdatabase.model.Model;
import persistentdatabase.model.Persistable;

public class Client {
	
	// path to local repository
	final static String modelsPath = "/Users/Zorkd/Dropbox/1AFiles/NBEL/Articles/Aneurysms/Models/";
	final static String logPath    = System.getProperty("user.dir");
	
	public static void main(String[] args) throws Exception{
		
		//Persistent agent
		PersistAgent persistAgent = new PersistAgent();
		
		/*--------------------------------------------------------------------------------
		 * Example 1: Fetching articles from Pubmed
		 * Fetching two articles from Pubmed using their ids
		 *--------------------------------------------------------------------------------
		*/
		
		Query query = new Query();
		query.setDatabase(DBType.PUBMED);
		query.addId("23371018");
		query.addId("10227670");
		query.setSearchType(SearchType.FETCH);	
		
		// Calling Entrez
		Document xmlDocs = Entrez.callEntrez(query);
		
		// Parse answer into a list of articles
		PubmedParser parser = new PubmedParser(xmlDocs);
		parser.parse();
		List<Article> articles = parser.getArticles();
		
		for (Article article: articles)
			persistAgent.PersistObject(article);
		
		// Print persisted articles
		persistAgent.showObjects(Article.ENTITY_NAME);
		
		// Article's abstracts are stored in files (generated at the project's folder)
		// Here, abstract are derived from the 'holder' Article objects
		List<Persistable> articlesPer = persistAgent.getObjectsList(Article.ENTITY_NAME);
		for (int i = 0; i < articlesPer.size(); i++){
			Article article = (Article) articlesPer.get(i);
			System.out.println(article.getAbstract());
			System.out.println("---------------------------------------");
		}
		
		/*--------------------------------------------------------------------------------
		 * Example 2: searching articles in Pubmed
		 * Ids of articles are derived from searching the terms "breast" and "cancer"
		 * in the journal 'Science' in 2009
		 *--------------------------------------------------------------------------------
		*/
		
		Query query2 = new Query();
		query2.setDatabase(DBType.PUBMED);
		query2.addTerm("breast");
		query2.addTerm("cancer");
		query2.addField(SearchFields.JOURNAL,          "science");
		query2.addField(SearchFields.PUBLICATION_DATA, "2009"   );
		query2.setSearchType(SearchType.SEARCH);
		List<String> results = Entrez.searchEntrez(query2);
		
		for (String result: results)
			System.out.println(result);
		
		/*--------------------------------------------------------------------------------
	     * Example 3: Retrieving diseases from MalaCards database
	     * Searching all disease cards from Malacards, which are related to 'aneurysms' 
		 *--------------------------------------------------------------------------------
		*/
		
		Query query3 = new Query();
		query3.setDatabase(DBType.MALA_CARDS);
		query3.addTerm("aneurysm");
		org.jsoup.nodes.Document results2 = MalaCards.callMalaCards(query3);
		
		MalaCardsParser parser2 = new  MalaCardsParser(results2, query3);
		parser2.parse();
		List<Disease> diseases = parser2.getDiseases();

		for (Disease disease: diseases){
			persistAgent.PersistObject(disease);
		}
		
		persistAgent.showObjects(Disease.ENTITY_NAME);
		
		/*--------------------------------------------------------------------------------
	     * Example 4: Retrieving models from the Biomodels database
		 * with their ids
		 *--------------------------------------------------------------------------------
		*/   
		
		Query query4 = new Query();
		query4.setDatabase(DBType.BIO_MODELS);
		query4.addId("BIOMD0000000058");
		query4.addId("BIOMD0000000291");
		
		List<Document> res = Biomodels.callBiomodels(query4);
		BiomodelsParser parser3 = new BiomodelsParser(res, query4.getIds());
		parser3.parse();
		List<Model> models = parser3.getModels();
		
		for (Model model: models){
			persistAgent.PersistObject(model);
		}
		
		persistAgent.showObjects(Model.ENTITY_NAME);

		/*--------------------------------------------------------------------------------
	     * Retrieving aneurysms' models and patients' clinical data from local repository
	     * Data was downloaded from the aneurisk web repository at: http://ecm2.mathcs.emory.edu/aneuriskweb/index
	     * and stored in the 'modelPath' library which was specified
	     * in the beginning of this class 
		 *--------------------------------------------------------------------------------
	    */
	    
		String[] aneurysmIds = getAneurysmModelsIds();
		
		for (String model: aneurysmIds){
			CSVparser cParser = new CSVparser
					(modelsPath + model +"/manifest.csv");
			cParser.parse();
			Aneurysm aneurysm = cParser.getAneurysm();
			aneurysm.setImage(modelsPath + model + "/image.png");
			aneurysm.setSTLmodel(modelsPath + model + "/surface/model.stl");
			persistAgent.PersistObject(aneurysm);
		}
		persistAgent.showObjects(Aneurysm.ENTITY_NAME);

	}
	
	//--------------------------------------------------------------------------------
	// HELPERS
	//--------------------------------------------------------------------------------
	
	// Scan aneurysms folder (that contains the aneurisk repository for data retrieval
	private static String[] getAneurysmModelsIds(){
		
		File file = new File(modelsPath);
		String[] directories = file.list(new FilenameFilter() {
		  public boolean accept(File current, String name) {
		    return new File(current, name).isDirectory();
		  }
		});
		
		return directories;
	}
	
	@SuppressWarnings("unused")
	private static void addToLog(String log) throws FileNotFoundException{
		
		String filePath = logPath + "/files/log.txt";
		
		File file = new File(filePath);
		file.getParentFile().mkdirs();
		PrintWriter out = new PrintWriter(file);
		out.println(log + "/n");
		out.close();
		
	}
}
