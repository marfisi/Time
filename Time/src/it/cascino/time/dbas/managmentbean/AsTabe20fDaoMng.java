package it.cascino.time.dbas.managmentbean;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import it.cascino.time.dbas.dao.AsTabe20fDao;
import it.cascino.time.dbas.model.AsTabe20f;
import it.cascino.time.utils.Resources;

public class AsTabe20fDaoMng implements AsTabe20fDao, Serializable{
	private static final long serialVersionUID = 1L;
	private Resources res = new Resources();
	private EntityManager em = res.getEmAs();
	private EntityTransaction utx = res.getUtxAs();	
	
	Logger log = Logger.getLogger(AsTabe20fDaoMng.class);
	
	@SuppressWarnings("unchecked")
	public List<AsTabe20f> getAll(){
		List<AsTabe20f> t = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("AsTabe20f.findAll");
				t = (List<AsTabe20f>)query.getResultList();
			}catch(NoResultException e){
				t = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return t;
	}
	
	public AsTabe20f getMarchio(String tbele){
		AsTabe20f t = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("AsTabe20f.findByTbele");
				query.setParameter("tbele", tbele);
				t = (AsTabe20f)query.getSingleResult();
			}catch(NoResultException e){
				t = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return t;
	}
	
	public void close(){
		res.close();
		log.info("chiuso");
	}
}
