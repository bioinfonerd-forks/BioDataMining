/*
 * Author: Elishai Ezra, Jerusalem College of Technology
 * Version: V1.0: Dec 1st, 2015
 * 
*/

package persistentdatabase.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Model implements Persistable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	String id;
	
	public static String ENTITY_NAME = "Model";
	
	String bioModelsID;
	String description;
	
	private List<Aneurysm> aneurysms = new ArrayList<Aneurysm>();
	private List<Article>  articles  = new ArrayList<Article>();
	private List<Disease>  diseases  = new ArrayList<Disease>();
	
	public void setID(String _id)         { bioModelsID = _id; }
	public void setDescription(String _d) { description = _d;  }
	
	public void addAneurysm(Aneurysm aneurysm) { aneurysms.add(aneurysm); }
	public void addArticle(Article article)    { articles.add(article);   }
	public void addDisease(Disease disease)    { diseases.add(disease);   }
		
	public List<Aneurysm> getLinkedAneurysms() { return aneurysms; }
	public List<Article>  getLinkedArticles()  { return articles;  }
	public List<Disease>  getLinkedDiseases()  { return diseases;  }	
	
	public String toString() { 
		return "Id: " + bioModelsID + "\nDescription: " + description;
	}
	
	public String getId()           { return bioModelsID;   }
	public String getEntityName()   { return ENTITY_NAME;	}
	public String getIdIdentidier() { return "bioModelsID";	}
	
}
