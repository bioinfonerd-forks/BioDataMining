Rapid development of entity-based data models for bioinformatics with persistence object-oriented design and structured interfaces

Author: Elishai Ezra, Jerusalem College of Technology
Version: V1.1: Jan 23, 2017
 
Before you begin, please add the following jar files to
your project's build path:
 * commons-csv-1.2.jar @ https://commons.apache.org/proper/commons-csv/
 * derby.jar @ https://db.apache.org/derby/
 * eclipselink.jar @ https://eclipse.org/eclipselink/downloads/
 * javax.persistence_2.1.0.jar @ http://mvnrepository.com/artifact/org.eclipse.persistence/javax.persistence/2.1.0
 * jsoup-1.8.3.jar @ http://jsoup.org/download
 
The following modification to the persistence.xml (@ META-INF folder)
are also needed:
 * change the value of the property: 'javax.persistence.jdbc.url' to your project path 

If this framework is used for the construction of a new database, please note the following:
 * Modify the classes' list in the persistence.xml file (META-INF folder) to consist your entity classes
 * Each of your entity classes has to be annotated with @<Entity name>, 
 * Each entity has to implement the 'Persistable' class

This tutorial is also given in project folder Client

Few examples are given:
* Example 1: Fetching articles from Pubmed using their ids
* Example 2: Searching articles’ ids from Pubmed	by searching terms 
* Example 3: Fetching diseases from MalaCards database by searching terms
* Example 4: Fetching models from the Biomodels database with their ids
* Example 5: Retrieving aneurysms' models and patients' clinical data from local repository

———— Defining persistency agent ————
PersistAgent persistAgent = new PersistAgent();
————————————————————————————————————
OUTPUT: (connection data)
[EL Info]: 2017-01-23 15:29:32.242--ServerSession(2145040140)--EclipseLink, version: Eclipse Persistence Services - 2.6.1.v20150916-55dc7c3
[EL Info]: connection: 2017-01-23 15:29:32.649--ServerSession(2145040140)--/file:/Users/Zorkd/Documents/workspace/project/bin/_people login successful

————————————————————————————————————————
 Example 1: Fetching articles from Pubmed
 Fetching two articles from Pubmed using their ids: 23371018, 10227670
————————————————————————————————————————

———— create query ———— 
Query query = new Query();
query.setDatabase(DBType.PUBMED);
query.addId("23371018");
query.addId("10227670");
query.setSearchType(SearchType.FETCH);	
—————————————————-————
Generated structured URL:
https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id=23371018,10227670&retmode=xml

———— calling Entrez ———— 
Document xmlDocs = Entrez.callEntrez(query);
—————————————————-—————-
		
————  data parsing -————- 
PubmedParser parser = new PubmedParser(xmlDocs);
parser.parse();
List<Article> articles = parser.getArticles();
———————————————————--————
	
————  data persistency  ———— 
for (Article article: articles)
	persistAgent.PersistObject(article);
————————————————————————————-

———— data retrieval —————
persistAgent.showObjects(Article.ENTITY_NAME);

* Article's abstracts are stored in files (generated at the project's folder)
* Here, abstract are derived from the 'holder' Article objects

List<Persistable> articlesPer = persistAgent.getObjectsList(Article.ENTITY_NAME);
for (int i = 0; i < articlesPer.size(); i++){
	Article article = (Article) articlesPer.get(i);
	System.out.println(article.getAbstract());
}
————————————————————————————-

OUTPUT:

---------------------------------------
ID: 23371018
TITLE:  Non-dimensional analysis of retinal microaneurysms: critical threshold for treatment.
AUTHOR: Ezra Elishai
JOURNAL: Integrative biology : quantitative biosciences from nano to macro 5(3), 2013, DOI: 10.1039/c3ib20259c
---------------------------------------
ID: 10227670
TITLE:  Three dimensional analysis of microaneurysms in the human diabetic retina.
AUTHOR: Moore J
JOURNAL: Journal of anatomy 194 ( Pt 1)(?), 1999, DOI: ?
---------------------------------------

————————————————————————————————————————
 Example 2: Searching articles’ ids from Pubmed	by searching terms 
 searching Pubmed articles’ Ids by searching the terms "breast" and "cancer"
 in the journal 'Science' in 2009
————————————————————————————————————————
	
———— create query ———— 	
Query query = new Query();
query.setDatabase(DBType.PUBMED);
query.addTerm("breast");
query.addTerm("cancer");
query.addField(SearchFields.JOURNAL,          "science");
query.addField(SearchFields.PUBLICATION_DATA, "2009"   );
—————————————————-————
Generated structured URL:
Query URL: https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&term=science[journal]+2009[pdat]+breast+AND+cancer&retmode=xml&rettype=uilist

———— calling Entrez ———— 
query.setSearchType(SearchType.SEARCH);
List<String> results = Entrez.searchEntrez(query);
—————————————————-—————-

———— data retrieval —————		
for (String result: results)
	System.out.println(result);
—————————————————————————-

OUTPUT:
19729658
19407191
19264984

————————————————————————————————————————
 Example 3: Fetching diseases from MalaCards database by searching terms
 Searching all disease cards from Malacards, which are related to 'aneurysms'
————————————————————————————————————————

———— create query ———— 
Query query = new Query();
query.setDatabase(DBType.MALA_CARDS);
query.addTerm("aneurysm");
—————————————————-————
Generated structured URL: 
http://malacards.org/search/results/aneurysm

———— calling MalaCards ———— 
org.jsoup.nodes.Document results = MalaCards.callMalaCards(query);
—————————————————-—————————

————  data parsing -————- 
MalaCardsParser parser = new  MalaCardsParser(results, query);
parser.parse();
List<Disease> diseases = parser.getDiseases();
———————————————————--————
	
————  data persistency  ———— 
for (Disease disease: diseases)
	persistAgent.PersistObject(disease);
————————————————————————————-

———— data retrieval —————
persistAgent.showObjects(Disease.ENTITY_NAME);
————————————————————————————-
		
OUTPUT:
---------------------------------------
Name: Aortic Aneurysm
Link at MalaCards: /card/aortic_aneurysm?search=aneurysm
---------------------------------------
Name: Aneurysm
Link at MalaCards: /card/aneurysm?search=aneurysm
---------------------------------------
Name: Abdominal Aortic Aneurysm
Link at MalaCards: /card/abdominal_aortic_aneurysm?search=aneurysm
---------------------------------------
Name: Intracranial Aneurysm
Link at MalaCards: /card/intracranial_aneurysm?search=aneurysm
---------------------------------------
Name: Familial Thoracic Aortic Aneurysm and Dissection
Link at MalaCards: /card/familial_thoracic_aortic_aneurysm_and_dissection?search=aneurysm
---------------------------------------
Name: Thoracic Aortic Aneurysm
Link at MalaCards: /card/thoracic_aortic_aneurysm?search=aneurysm
---------------------------------------
Name: Aortic Aneurysm, Familial Thoracic 4
Link at MalaCards: /card/aortic_aneurysm_familial_thoracic_4?search=aneurysm
---------------------------------------
Name: Coronary Aneurysm
Link at MalaCards: /card/coronary_aneurysm?search=aneurysm
---------------------------------------
Name: Angiopathy, Hereditary, with Nephropathy, Aneurysms, and Muscle Cramps
Link at MalaCards: /card/angiopathy_hereditary_with_nephropathy_aneurysms_and_muscle_cramps?search=aneurysm
---------------------------------------
Name: Aneurysmal Bone Cysts
Link at MalaCards: /card/aneurysmal_bone_cysts?search=aneurysm
---------------------------------------
Name: Cerebral Aneurysms
Link at MalaCards: /card/cerebral_aneurysms?search=aneurysm
---------------------------------------
Name: Intracranial Berry Aneurysm
Link at MalaCards: /card/intracranial_berry_aneurysm?search=aneurysm
---------------------------------------
Name: Loeys-Dietz Syndrome
Link at MalaCards: /card/loeys_dietz_syndrome?search=aneurysm
---------------------------------------
Name: Heart Aneurysm
Link at MalaCards: /card/heart_aneurysm?search=aneurysm
---------------------------------------
Name: Vein of Galen Aneurysm
Link at MalaCards: /card/vein_of_galen_aneurysm?search=aneurysm
---------------------------------------
		
————————————————————————————————————————
 Example 4: Fetching models from the Biomodels database with their ids
 Retrieving models from the Biomodels database with their ids (BIOMD0000000058, BIOMD0000000291)
————————————————————————————————————————	

———— create query ———— 
Query query = new Query();
query.setDatabase(DBType.BIO_MODELS);
query.addId("BIOMD0000000058");
query.addId("BIOMD0000000291");
—————————————————-————
Generated structured URLs: 
http://www.ebi.ac.uk/biomodels-main/download?mid=BIOMD0000000058
http://www.ebi.ac.uk/biomodels-main/download?mid=BIOMD0000000291

———— calling MalaCards ———— 
List<Document> res = Biomodels.callBiomodels(query);
—————————————————-—————————

————  data parsing -————- 
BiomodelsParser parser = new BiomodelsParser(res, query.getIds());
parser.parse();
List<Model> models = parser.getModels();
———————————————————--————
	
————  data persistency  ———— 
for (Model model: models)
	persistAgent.PersistObject(model);
————————————————————————————-

———— data retrieval —————
persistAgent.showObjects(Model.ENTITY_NAME);
————————————————————————————-		

OUTPUT:
---------------------------------------
Id: BIOMD0000000058
Description: /Users/Zorkd/Documents/workspace/project/files/models/BIOMD0000000058_model.txt
---------------------------------------
Id: BIOMD0000000291
Description: /Users/Zorkd/Documents/workspace/project/files/models/BIOMD0000000291_model.txt
---------------------------------------

————————————————————————————————————————
 Example 5: Retrieving aneurysms' models and patients' clinical data from local repository
 Retrieving aneurysms' models and patients' clinical data from local repository
 Data was downloaded from the aneurisk web repository at: http://ecm2.mathcs.emory.edu/aneuriskweb/index
 and stored in the 'modelPath' library which was specified in the beginning of this class 
————————————————————————————————————————

———— Getting aneurysms id from folders’ names —————

File file = new File(modelsPath);
String[] directories = file.list(new FilenameFilter() {
  public boolean accept(File current, String name) {
    return new File(current, name).isDirectory();
  }
});
———————————————————————————————————————————————————

————  data parsing and persistency -————- 
for (String model: aneurysmIds){
	CSVparser cParser = new CSVparser(modelsPath + model +"/manifest.csv");
	cParser.parse();
	Aneurysm aneurysm = cParser.getAneurysm();
	aneurysm.setImage(modelsPath + model + "/image.png");
	aneurysm.setSTLmodel(modelsPath + model + "/surface/model.stl");
	persistAgent.PersistObject(aneurysm);
}
—————————————————————————————————————————
	
———— data retrieval —————
persistAgent.showObjects(Aneurysm.ENTITY_NAME);
————————————————————————————-

OUTPUT:
--------------------------------------
Patient ID: C0001
SEX: F, AGE: 53
Aneurysm type: LAT, location:ICA, status: U
---------------------------------------
Patient ID: C0002
SEX: F, AGE: 35
Aneurysm type: LAT, location:ICA, status: U
---------------------------------------
Patient ID: C0003
SEX: F, AGE: 43
Aneurysm type: TER, location:ICA, status: U
---------------------------------------
Patient ID: C0004
SEX: F, AGE: 60
Aneurysm type: TER, location:ICA, status: U
---------------------------------------
Patient ID: C0005
SEX: F, AGE: 26
Aneurysm type: LAT, location:ICA, status: R
---------------------------------------
Patient ID: C0006
SEX: F, AGE: 45
Aneurysm type: LAT, location:ICA, status: U
---------------------------------------
Patient ID: C0007
SEX: F, AGE: 44
Aneurysm type: LAT, location:ICA, status: U
---------------------------------------
Patient ID: C0008
SEX: M, AGE: 68
Aneurysm type: TER, location:ACA, status: R
---------------------------------------
Patient ID: C0009
SEX: F, AGE: 39
Aneurysm type: TER, location:ACA, status: R
---------------------------------------
Patient ID: C0010
SEX: F, AGE: 63
Aneurysm type: TER, location:ACA, status: U
---------------------------------------
Patient ID: C0011
SEX: M, AGE: 48
Aneurysm type: TER, location:ACA, status: U
---------------------------------------
Patient ID: C0012
SEX: M, AGE: 62
Aneurysm type: TER, location:ACA, status: R
---------------------------------------
Patient ID: C0013
SEX: F, AGE: 85
Aneurysm type: LAT, location:ICA, status: R
---------------------------------------
Patient ID: C0014
SEX: F, AGE: 68
Aneurysm type: LAT, location:ICA, status: U
---------------------------------------
Patient ID: C0015
SEX: F, AGE: 56
Aneurysm type: TER, location:ICA, status: R
---------------------------------------
Patient ID: C0016
SEX: F, AGE: 42
Aneurysm type: LAT, location:ICA, status: U
---------------------------------------
Patient ID: C0017
SEX: F, AGE: 54
Aneurysm type: LAT, location:ICA, status: R
---------------------------------------
Patient ID: C0018
SEX: F, AGE: 36
Aneurysm type: LAT, location:ICA, status: R
---------------------------------------
Patient ID: C0019
SEX: M, AGE: 48
Aneurysm type: TER, location:MCA, status: U
---------------------------------------
Patient ID: C0020
SEX: F, AGE: 51
Aneurysm type: TER, location:MCA, status: U
---------------------------------------
Patient ID: C0021
SEX: M, AGE: 36
Aneurysm type: TER, location:ACA, status: R
---------------------------------------
Patient ID: C0022
SEX: M, AGE: 55
Aneurysm type: LAT, location:ICA, status: U
---------------------------------------
Patient ID: C0023
SEX: M, AGE: 52
Aneurysm type: TER, location:MCA, status: R
---------------------------------------
Patient ID: C0024
SEX: F, AGE: 35
Aneurysm type: LAT, location:ICA, status: U
---------------------------------------
Patient ID: C0025
SEX: M, AGE: 74
Aneurysm type: LAT, location:ICA, status: U
---------------------------------------
Patient ID: C0026
SEX: F, AGE: 42
Aneurysm type: LAT, location:ICA, status: U
---------------------------------------
Patient ID: C0027
SEX: F, AGE: 64
Aneurysm type: LAT, location:ICA, status: R
---------------------------------------
Patient ID: C0028b
SEX: F, AGE: 77
Aneurysm type: TER, location:MCA, status: U
---------------------------------------
Patient ID: C0029
SEX: M, AGE: 43
Aneurysm type: TER, location:MCA, status: R
---------------------------------------
Patient ID: C0030
SEX: F, AGE: 59
Aneurysm type: TER, location:MCA, status: U
---------------------------------------
Patient ID: C0031
SEX: F, AGE: 74
Aneurysm type: LAT, location:ICA, status: R
---------------------------------------
Patient ID: C0033
SEX: M, AGE: 69
Aneurysm type: TER, location:ACA, status: U
---------------------------------------
Patient ID: C0034
SEX: F, AGE: 42
Aneurysm type: LAT, location:ICA, status: U
---------------------------------------
Patient ID: C0035
SEX: F, AGE: 57
Aneurysm type: LAT, location:ICA, status: U
---------------------------------------
Patient ID: C0036
SEX: M, AGE: 52
Aneurysm type: LAT, location:ICA, status: U
---------------------------------------
Patient ID: C0037
SEX: M, AGE: 53
Aneurysm type: TER, location:ICA, status: R
---------------------------------------
Patient ID: C0038
SEX: F, AGE: 74
Aneurysm type: LAT, location:ICA, status: R
---------------------------------------
Patient ID: C0039
SEX: F, AGE: 59
Aneurysm type: LAT, location:ICA, status: U
---------------------------------------
Patient ID: C0040
SEX: M, AGE: 62
Aneurysm type: LAT, location:ICA, status: U
---------------------------------------
Patient ID: C0041
SEX: F, AGE: 73
Aneurysm type: LAT, location:ICA, status: U
---------------------------------------
Patient ID: C0042
SEX: F, AGE: 45
Aneurysm type: LAT, location:ICA, status: U
---------------------------------------
Patient ID: C0043
SEX: F, AGE: 66
Aneurysm type: LAT, location:BAS, status: R
---------------------------------------
Patient ID: C0044
SEX: M, AGE: 61
Aneurysm type: LAT, location:MCA, status: U
---------------------------------------
Patient ID: C0045
SEX: M, AGE: 38
Aneurysm type: TER, location:MCA, status: U
---------------------------------------
Patient ID: C0046
SEX: M, AGE: 56
Aneurysm type: TER, location:ICA, status: U
---------------------------------------
Patient ID: C0047
SEX: F, AGE: 49
Aneurysm type: TER, location:ACA, status: R
---------------------------------------
Patient ID: C0048
SEX: M, AGE: 43
Aneurysm type: TER, location:ACA, status: R
---------------------------------------
Patient ID: C0049
SEX: M, AGE: 41
Aneurysm type: TER, location:MCA, status: U
---------------------------------------
Patient ID: C0050
SEX: M, AGE: 51
Aneurysm type: TER, location:MCA, status: R
---------------------------------------
Patient ID: C0051
SEX: M, AGE: 53
Aneurysm type: TER, location:MCA, status: R
---------------------------------------
Patient ID: C0052
SEX: M, AGE: 55
Aneurysm type: TER, location:ACA, status: R
---------------------------------------
Patient ID: C0053
SEX: M, AGE: 45
Aneurysm type: TER, location:ACA, status: R
---------------------------------------
Patient ID: C0054
SEX: M, AGE: 62
Aneurysm type: TER, location:BAS, status: U
---------------------------------------
Patient ID: C0055
SEX: M, AGE: 65
Aneurysm type: TER, location:ACA, status: U
---------------------------------------
Patient ID: C0056
SEX: F, AGE: 51
Aneurysm type: TER, location:ACA, status: R
---------------------------------------
Patient ID: C0057a
SEX: M, AGE: 71
Aneurysm type: TER, location:ACA, status: R
---------------------------------------
Patient ID: C0057b
SEX: M, AGE: 71
Aneurysm type: TER, location:MCA, status: U
---------------------------------------
Patient ID: C0059
SEX: M, AGE: 41
Aneurysm type: TER, location:ACA, status: R
---------------------------------------
Patient ID: C0060
SEX: M, AGE: 49
Aneurysm type: TER, location:ACA, status: U
---------------------------------------
Patient ID: C0061
SEX: F, AGE: 24
Aneurysm type: TER, location:ACA, status: R
---------------------------------------
Patient ID: C0062
SEX: F, AGE: 68
Aneurysm type: TER, location:ICA, status: R
---------------------------------------
Patient ID: C0063
SEX: F, AGE: 56
Aneurysm type: LAT, location:ICA, status: U
---------------------------------------
Patient ID: C0064
SEX: F, AGE: 64
Aneurysm type: LAT, location:ICA, status: U
---------------------------------------
Patient ID: C0065
SEX: M, AGE: 47
Aneurysm type: LAT, location:ICA, status: R
---------------------------------------
Patient ID: C0066
SEX: F, AGE: 58
Aneurysm type: TER, location:ICA, status: U
---------------------------------------
Patient ID: C0067
SEX: F, AGE: 42
Aneurysm type: LAT, location:ICA, status: U
---------------------------------------
Patient ID: C0068
SEX: F, AGE: 33
Aneurysm type: TER, location:BAS, status: R
---------------------------------------
Patient ID: C0069
SEX: F, AGE: 43
Aneurysm type: TER, location:MCA, status: U
---------------------------------------
Patient ID: C0070
SEX: F, AGE: 63
Aneurysm type: TER, location:MCA, status: U
---------------------------------------
Patient ID: C0071
SEX: M, AGE: 51
Aneurysm type: TER, location:MCA, status: U
---------------------------------------
Patient ID: C0072
SEX: M, AGE: 72
Aneurysm type: TER, location:MCA, status: U
---------------------------------------
Patient ID: C0073
SEX: F, AGE: 49
Aneurysm type: TER, location:MCA, status: R
---------------------------------------
Patient ID: C0074a
SEX: F, AGE: 32
Aneurysm type: LAT, location:ICA, status: U
---------------------------------------
Patient ID: C0074b
SEX: F, AGE: 32
Aneurysm type: TER, location:MCA, status: U
---------------------------------------
Patient ID: C0075
SEX: F, AGE: 74
Aneurysm type: LAT, location:ICA, status: U
---------------------------------------
Patient ID: C0076
SEX: F, AGE: 78
Aneurysm type: LAT, location:ICA, status: U
---------------------------------------
Patient ID: C0077
SEX: M, AGE: 63
Aneurysm type: TER, location:MCA, status: U
---------------------------------------
Patient ID: C0078
SEX: F, AGE: 28
Aneurysm type: TER, location:MCA, status: U
---------------------------------------
Patient ID: C0079
SEX: F, AGE: 65
Aneurysm type: TER, location:ACA, status: U
---------------------------------------
Patient ID: C0080
SEX: F, AGE: 38
Aneurysm type: TER, location:ACA, status: R
---------------------------------------
Patient ID: C0081
SEX: F, AGE: 74
Aneurysm type: TER, location:ACA, status: R
---------------------------------------
Patient ID: C0082
SEX: F, AGE: 35
Aneurysm type: TER, location:ACA, status: R
---------------------------------------
Patient ID: C0083
SEX: F, AGE: 66
Aneurysm type: TER, location:ACA, status: R
---------------------------------------
Patient ID: C0084
SEX: M, AGE: 49
Aneurysm type: TER, location:ACA, status: R
---------------------------------------
Patient ID: C0085
SEX: F, AGE: 64
Aneurysm type: LAT, location:ICA, status: R
---------------------------------------
Patient ID: C0086
SEX: F, AGE: 61
Aneurysm type: LAT, location:ICA, status: U
---------------------------------------
Patient ID: C0087
SEX: F, AGE: 59
Aneurysm type: TER, location:ICA, status: U
---------------------------------------
Patient ID: C0088a
SEX: F, AGE: 59
Aneurysm type: LAT, location:ICA, status: U
---------------------------------------
Patient ID: C0088b
SEX: F, AGE: 59
Aneurysm type: LAT, location:ICA, status: U
---------------------------------------
Patient ID: C0089
SEX: F, AGE: 48
Aneurysm type: LAT, location:ICA, status: U
---------------------------------------
Patient ID: C0090
SEX: F, AGE: 42
Aneurysm type: LAT, location:ICA, status: U
---------------------------------------
Patient ID: C0091
SEX: M, AGE: 49
Aneurysm type: TER, location:BAS, status: U
---------------------------------------
Patient ID: C0092
SEX: M, AGE: 62
Aneurysm type: TER, location:MCA, status: R
---------------------------------------
Patient ID: C0093
SEX: M, AGE: 67
Aneurysm type: TER, location:MCA, status: U
---------------------------------------
Patient ID: C0094
SEX: F, AGE: 36
Aneurysm type: TER, location:MCA, status: R
---------------------------------------
Patient ID: C0095
SEX: F, AGE: 43
Aneurysm type: TER, location:BAS, status: F
---------------------------------------
Patient ID: C0096
SEX: M, AGE: 67
Aneurysm type: TER, location:BAS, status: R
---------------------------------------
Patient ID: C0097
SEX: F, AGE: 84
Aneurysm type: LAT, location:ICA, status: R
---------------------------------------
Patient ID: C0098
SEX: F, AGE: 59
Aneurysm type: TER, location:MCA, status: R
---------------------------------------
Patient ID: C0099
SEX: F, AGE: 42
Aneurysm type: TER, location:MCA, status: U
---------------------------------------
	
		
