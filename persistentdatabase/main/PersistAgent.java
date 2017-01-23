/*
 * Author: Elishai Ezra, Jerusalem College of Technology
 * Version: V1.0: Dec 1st, 2015
 * 
*/

package persistentdatabase.main;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import persistentdatabase.model.Persistable;

public class PersistAgent {
	
	private final String PERSISTENCE_UNIT_NAME = "DATA-BASE";
	private EntityManagerFactory factory;
	private EntityManager entityManager;
	
	public PersistAgent(){	
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		entityManager = factory.createEntityManager();
	}
	
	public void PersistObject(Persistable object){
		// Begin a new local transaction so that we can persist a new entity
		entityManager.getTransaction().begin();	
		
		Query q = entityManager.createQuery
				("SELECT p FROM " + object.getEntityName()+ " p WHERE p." + object.getIdIdentidier() 
				+ " = :" + object.getIdIdentidier());
		q. setParameter(object.getIdIdentidier(), object.getId());
		
		boolean isNotExist = (q.getResultList().size() == 0);

		if (isNotExist)
		    entityManager.persist(object);
		else 
			System.err.println("Entry: " + object.getId() + " is already exist");
		
		entityManager.getTransaction().commit();
	}
	
	public void showObjects (String className){
		
		Query q = entityManager.createQuery("SELECT m FROM " + className + "  m");
	    System.out.println(q);
	    
	    @SuppressWarnings("unchecked")
		List<Persistable> objectList = q.getResultList();
	    for (Persistable object : objectList) {
	      System.out.println("---------------------------------------");
	      System.out.println(object);
	    }
	    System.out.println("---------------------------------------");	
	}
	
	@SuppressWarnings("unchecked")
	public List<Persistable> getObjectsList(String className){
		Query q = entityManager.createQuery("SELECT m FROM " + className + " m");
	    return q.getResultList();
	}
		
}

