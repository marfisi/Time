package it.cascino.mng;

import java.io.Serializable;
import java.util.List;
import it.cascino.model.Myprecod;
import java.util.Iterator;
import it.cascino.dao.MyprecodDao;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.apache.log4j.Logger;

public class MyprecodDaoMng implements MyprecodDao, Serializable{
	private static final long serialVersionUID = 1L;
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Time");
	private EntityManager em = emf.createEntityManager();
	private EntityTransaction utx = em.getTransaction();
	
	Logger log = Logger.getLogger(MyprecodDaoMng.class);
	
	@SuppressWarnings("unchecked")
	public List<Myprecod> getAll(){
		List<Myprecod> precodici = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("Myartmag.findAll");
				precodici = (List<Myprecod>)query.getResultList();
			}catch(NoResultException e){
				precodici = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return precodici;
	}
	
	public void salva(Myprecod precodice){
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
	
	public void aggiorna(Myprecod precodice){
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
	
	public void elimina(Myprecod precodiceElimina){
		// log.info("tmpDEBUGtmp: " + "> " + "elimina(" + produttoreElimina + ")");
		try{
			try{
				utx.begin();
				Myprecod precodice = em.find(Myprecod.class, precodiceElimina.getCprecDarti());
				log.info("elimina: " + precodice.toString());
				em.remove(precodice);
			}finally{
				utx.commit();
			}
		}catch(Exception e){
			log.fatal(e.toString());
		}
	}
	
	public Myprecod getMyprecodDaCprecDarti(Integer idPrecDarti){
		Myprecod precodice = new Myprecod();
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("Myprecod.findByCprecDarti", Myprecod.class);
				query.setParameter("cprec_darti", Integer.toString(idPrecDarti));
				precodice = (Myprecod)query.getSingleResult();
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
		em.close();
		emf.close();
		log.info("chiuso");
	}
}
