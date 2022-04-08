package it.cascino.time.dbas.managmentbean;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import it.cascino.time.dbas.dao.AsAnmag0fDao;
import it.cascino.time.dbas.model.AsAnmag0f;
import it.cascino.time.utils.Resources;

public class AsAnmag0fDaoMng implements AsAnmag0fDao, Serializable{
	private static final long serialVersionUID = 1L;
	private Resources res = new Resources();
	private EntityManager em = res.getEmAs();
	// private EntityTransaction utx = res.getUtxAs();	
	
	Logger log = Logger.getLogger(AsAnmag0fDaoMng.class);
	
	@SuppressWarnings("unchecked")
	public List<AsAnmag0f> getAll(){
		List<AsAnmag0f> o = null;
		try{
			try{
				Query query = em.createNamedQuery("AsAnmag0f.findAll");
				o = (List<AsAnmag0f>)query.getResultList();
			}catch(NoResultException e){
				o = null;
			}
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}
	
	public AsAnmag0f getArticoloDaMcoda(String mcoda){
		AsAnmag0f o = null;
		try{
			try{
				Query query = em.createNamedQuery("AsAnmag0f.findByMcoda");
				query.setParameter("mcoda", mcoda);
				o = (AsAnmag0f)query.getSingleResult();
			}catch(NoResultException e){
				o = null;
			}
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}
	
	@SuppressWarnings("unchecked")
	public List<AsAnmag0f> getArticoliIngrosso(){
		List<AsAnmag0f> o = null;
		try{
			try{
				Query query = em.createNamedQuery("AsAnmag0f.findAllIngrosso");
				o = (List<AsAnmag0f>)query.getResultList();
			}catch(NoResultException e){
				o = null;
			}
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}
	
	@SuppressWarnings("unchecked")
	public List<AsAnmag0f> getArticoliDaMcompIngrosso(String mcomp){
		List<AsAnmag0f> o = null;
		try{
			try{
				Query query = em.createNamedQuery("AsAnmag0f.findByMcomp");
				query.setParameter("mcomp", mcomp);
				o = (List<AsAnmag0f>)query.getResultList();
			}catch(NoResultException e){
				o = null;
			}
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}
	
	public void close(){
		res.close();
		log.info("chiuso");
	}
}
