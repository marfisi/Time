package it.cascino.time.dbmysql.managmentbean;

import java.io.Serializable;
import java.util.List;
import it.cascino.time.dbmysql.model.MysMyprecod;
import it.cascino.time.utils.Resources;
import it.cascino.time.dbmysql.dao.MysMyprecodDao;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.log4j.Logger;

public class MysMyprecodDaoMng implements MysMyprecodDao, Serializable{
	private static final long serialVersionUID = 1L;
	private Resources res = new Resources();
	private EntityManager em = res.getEmMysql();
	private EntityTransaction utx = res.getUtxMysql();
	
	Logger log = Logger.getLogger(MysMyprecodDaoMng.class);
	
	@SuppressWarnings("unchecked")
	public List<MysMyprecod> getAll(){
		List<MysMyprecod> precodici = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("Myartmag.findAll");
				precodici = (List<MysMyprecod>)query.getResultList();
			}catch(NoResultException e){
				precodici = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return precodici;
	}
	
	public void salva(MysMyprecod precodice){
		try{
			try{
				utx.begin();
				// precodice.setId(null);
				log.info("salva: " + precodice.toString());
				em.persist(precodice);
			}finally{
				utx.commit();
			}
		}catch(Exception e){
			log.fatal(e.toString());
		}
	}
	
	public void aggiorna(MysMyprecod precodice){
		try{
			try{
				utx.begin();
				log.info("aggiorna: " + precodice.toString());
				em.merge(precodice);
			}finally{
				utx.commit();
			}
		}catch(Exception e){
			log.fatal(e.toString());
		}
	}
	
	public void elimina(MysMyprecod precodiceElimina){
		// log.info("tmpDEBUGtmp: " + "> " + "elimina(" + produttoreElimina + ")");
		try{
			try{
				utx.begin();
				MysMyprecod precodice = em.find(MysMyprecod.class, precodiceElimina.getCprecDarti());
				log.info("elimina: " + precodice.toString());
				em.remove(precodice);
			}finally{
				utx.commit();
			}
		}catch(Exception e){
			log.fatal(e.toString());
		}
	}
	
	public MysMyprecod getMyprecodDaCprecDarti(Integer idPrecDarti){
		MysMyprecod precodice = new MysMyprecod();
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("Myprecod.findByCprecDarti", MysMyprecod.class);
				query.setParameter("cprec_darti", Integer.toString(idPrecDarti));
				precodice = (MysMyprecod)query.getSingleResult();
			}catch(NoResultException e){
				precodice = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return precodice;
	}
	
	public void svuotaTabella(){
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("Myprecod.svuota");
				query.executeUpdate();
			}finally{
				utx.commit();
			}
		}catch(Exception e){
			log.fatal(e.toString());
		}
	}
	
	public void close(){
		res.close();
		log.info("chiuso");
	}
}
