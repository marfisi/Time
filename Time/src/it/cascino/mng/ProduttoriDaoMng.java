package it.cascino.mng;

import java.io.Serializable;
import java.util.List;
import it.cascino.dao.ProduttoriDao;
import it.cascino.model.Produttori;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.jboss.logging.Logger;

public class ProduttoriDaoMng implements ProduttoriDao, Serializable{
	private static final long serialVersionUID = 1L;
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Postgresql");
	private EntityManager em = emf.createEntityManager(); // Retrieve an application managed entity manager
	private EntityTransaction utx = em.getTransaction();
	
	Logger log = Logger.getLogger(ProduttoriDaoMng.class);
	
	@SuppressWarnings("unchecked")
	public List<Produttori> getAll(){
		// log.info("tmpDEBUGtmp: " + "> " + "getAll(" + ")");
		List<Produttori> produttori = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("Produttori.findAll");
				produttori = (List<Produttori>)query.getResultList();
			}catch(NoResultException e){
				produttori = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return produttori;
	}
	
	public void close(){
		em.close();
		emf.close();
		log.info("chiuso");
	}
}
