package it.cascino.time.dbas.managmentbean;

import java.io.Serializable;
import java.util.List;
import it.cascino.time.dbas.dao.AsMyfotxarDao;
import it.cascino.time.dbas.model.AsMyfotxar;
import it.cascino.time.utils.Resources;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.log4j.Logger;

public class AsMyfotxarDaoMng implements AsMyfotxarDao, Serializable{
	private static final long serialVersionUID = 1L;
	private Resources res = new Resources();
	private EntityManager em = res.getEmAs();
	private EntityTransaction utx = res.getUtxAs();	
	
	Logger log = Logger.getLogger(AsMyfotxarDaoMng.class);
	
	@SuppressWarnings("unchecked")
	public List<AsMyfotxar> getAll(){
		List<AsMyfotxar> o = null;
		try{
			try{
				Query query = em.createNamedQuery("Myfotxar.findAll");
				o = (List<AsMyfotxar>)query.getResultList();
			}catch(NoResultException e){
				o = null;
			}
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}
	
	public void salva(AsMyfotxar o){
		try{
			try{
				utx.begin();
				log.info("salva: " + o.toString());
				em.persist(o);
			}finally{
				utx.commit();
			}
		}catch(Exception e){
			log.fatal(e.toString());
		}
	}
	
	public void detach(Object entity){
		em.detach(entity);
	}
	
	public void close(){
		res.close();
		log.info("chiuso");
	}
}
