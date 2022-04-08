package it.cascino.time.dbas.managmentbean;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import it.cascino.time.dbas.dao.AsAnmar0fDao;
import it.cascino.time.dbas.model.AsAnmar0f;
import it.cascino.time.utils.Resources;

public class AsAnmar0fDaoMng implements AsAnmar0fDao, Serializable{
	private static final long serialVersionUID = 1L;
	private Resources res = new Resources();
	private EntityManager em = res.getEmAs();
	private EntityTransaction utx = res.getUtxAs();	
	
	Logger log = Logger.getLogger(AsAnmar0fDaoMng.class);
	
	@SuppressWarnings("unchecked")
	public List<AsAnmar0f> getAll(){
		List<AsAnmar0f> o = null;
		try{
			try{
				Query query = em.createNamedQuery("AsAnmar0f.findAll");
				o = (List<AsAnmar0f>)query.getResultList();
			}catch(NoResultException e){
				o = null;
			}
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}
	
	public Boolean aggiorna(AsAnmar0f o){
		try{
			try{
				utx.begin();
				log.info("aggiorna: " + o.toString());
				em.merge(o);
			}finally{
				utx.commit();
			}
		}catch(Exception e){
			log.fatal(e.toString());
			return false;
		}
		return true;
	}
	
	public AsAnmar0f getGruppoDaMcomp(String mcomp){
		AsAnmar0f o = null;
		try{
			try{
				Query query = em.createNamedQuery("AsAnmar0f.findByMcomp");
				query.setParameter("mcomp", mcomp);
				o = (AsAnmar0f)query.getSingleResult();
			}catch(NoResultException e){
				o = null;
			}
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}
	
	public void detach(Object entity){
		em.detach(entity);
	}
	
	public void close(){
		res.close();
		log.info("chiuso");
	}
}
