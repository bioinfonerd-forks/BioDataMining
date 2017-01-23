/*
 * Author: Elishai Ezra, Jerusalem College of Technology
 * Version: V1.0: Dec 1st, 2015
 * 
*/

package parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import persistentdatabase.model.Model;

public class BiomodelsParser extends XMLparser{
	
	List<Model> _models;
	List<String> _ids;
	
	public BiomodelsParser (List<Document> doc, List<String> ids) {
		_models = new ArrayList<Model>();
		_doc = doc;
		_ids = ids;
	}
	
	public List<Model> getModels() { return _models; }

	public void parse() {
		
		for (int i = 0; i < _doc.size(); i++){
			
			Element sbmlElement = _doc.get(i).getDocumentElement();
			Element modelElement = getChildByName(sbmlElement, "model");
			Element notesElement = getChildByName(modelElement, "notes");
			Element bodyElement = getChildByName(notesElement, "body");
			List<Element> ParagraphElement = getChildrensByName(bodyElement, "p");
			
			String descStr = "";
			
			for (Element element: ParagraphElement)
				descStr += getTextContent(element);
			
			Model model = new Model();
			
			try {
				String projectPath = System.getProperty("user.dir");
				String filePath = projectPath + "/files/models/" + _ids.get(i) + "_model.txt";
				
				File file = new File(filePath);
				file.getParentFile().mkdirs();
				PrintWriter out = new PrintWriter(file);
				
				String prtAbstract =  descStr.toString().replace("\t", " ");
				out.println(prtAbstract);
				out.close();
				model.setDescription(filePath);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			model.setID(_ids.get(i));
			_models.add(model);	
		}
	}

}
