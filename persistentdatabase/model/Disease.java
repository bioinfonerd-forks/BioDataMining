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
public class Disease implements Persistable{
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.TABLE)
	 private String id;
	 
	 public static String ENTITY_NAME = "Disease";
	 
	 private String name;
	 private String URLlink;
	 
	 private List<Aneurysm> aneurysms = new ArrayList<Aneurysm>();
	 private List<Article>  articles  = new ArrayList<Article>();
	 private List<Model>    models    = new ArrayList<Model>();
	 
	 public void setName(String _name)  {	 name = _name;	 }
	 public void setURLlink(String link){	 URLlink = link; }
	 
	 public void addAneurysm(Aneurysm aneurysm) { aneurysms.add(aneurysm); }
	 public void addArticle(Article article)    { articles.add(article);   }
	 public void addModel(Model model)          { models.add(model);       }
		
	 public List<Aneurysm> getLinkedAneurysms() { return aneurysms; }
	 public List<Article> getLinkedArticles()   { return articles; }
	 public List<Model>   getLinkedModels()     { return models;   }
		 
	 public String toString(){
		 return "Name: " + name + "\n" +
				 "Link at MalaCards: " + URLlink;
	 }

	 public String getName()         {	 return name;      }
	 public String getEntityName()   { return ENTITY_NAME; }
	 public String getIdIdentidier() { return "name";      }
	 public String getId()           { return name;   	   }

}
