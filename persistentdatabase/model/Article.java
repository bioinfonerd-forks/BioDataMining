/*
 * Author: Elishai Ezra, Jerusalem College of Technology
 * Version: V1.0: Dec 1st, 2015
 * 
*/

package persistentdatabase.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Article implements Persistable{
	
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  private String id;
  
  public static String ENTITY_NAME = "Article";
  
  private String PMID;
  private String title;
  private String leadAuthor;
  private String pDate;
  private String journal;
  private String volume;
  private String issue;
  private String DOI;
  private String abst;
  
  private List<Disease> diseases   = new ArrayList<Disease>();
  private List<Aneurysm> aneurysms = new ArrayList<Aneurysm>();
  private List<Model>   models     = new ArrayList<Model>();

  public String toString(){
	  return "ID: "      + PMID       + "\n" +
			 "TITLE:  "  + title      + "\n" +
			 "AUTHOR: "  + leadAuthor + "\n" +
			 "JOURNAL: " + journal + " " + volume + "(" + issue + ")" 
			 			 + ", " + pDate + ", " + "DOI: " + DOI;
  }

  public void setId(String _id)            { PMID       = _id;      }
  public void setTitle(String _title)      { title      = _title;   }
  public void setAuthor(String _author)    { leadAuthor = _author;  }
  public void setYear(String _year)        { pDate      = _year;    }
  public void setJournal(String _journal)  { journal    = _journal; }
  public void setVolume(String _volume)    { volume     = _volume;  }
  public void setIssue(String _issue)      { issue      = _issue;   }
  public void setDOI(String _DOI)          { DOI        = _DOI;     }
  public void setAbstract(String _abst)    { abst       = _abst;    }
  
  public void addDisease(Disease disease)    { diseases.add(disease);   }
  public void addAneurysm(Aneurysm aneurysm) { aneurysms.add(aneurysm); }
  public void addModel(Model model)          { models.add(model);       }
	
  public List<Disease> getLinkedDiseases()   { return diseases; }
  public List<Aneurysm> getLinkedAneurysms() { return aneurysms; }
  public List<Model>   getLinkedModels()     { return models;   }
  
  public String getAbstract() { 
	String abstStr = "";
	try { abstStr = new String(Files.readAllBytes(Paths.get(abst))); } 
	catch (IOException e) {	e.printStackTrace(); }
	return abstStr;          
  }

  public String getId()			  { return PMID;		}
  public String getEntityName()   { return ENTITY_NAME;	}
  public String getIdIdentidier() { return "PMID";    	}
 
  
}