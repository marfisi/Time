package it.cascino.time.dbpg.managmenntbean;

import java.io.Serializable;
import java.util.List;
import it.cascino.time.dbpg.model.PgProduttori;
import it.cascino.time.utils.Resources;
import it.cascino.time.dbpg.dao.PgProduttoriDao;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.jboss.logging.Logger;

public class PgProduttoriDaoMng implements PgProduttoriDao, Serializable{
	private static final long serialVersionUID = 1L;
	private Resources res = new Resources();
	private EntityManager em = res.getEmPg();
	private EntityTransaction utx = res.getUtxPg();
	
	Logger log = Logger.getLogger(PgProduttoriDaoMng.class);
	
	@SuppressWarnings("unchecked")
	public List<PgProduttori> getAll(){
		// log.info("tmpDEBUGtmp: " + "> " + "getAll(" + ")");
		List<PgProduttori> produttori = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("Produttori.findAll");
				produttori = (List<PgProduttori>)query.getResultList();
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
		res.close();
		log.info("chiuso");
	}
}
