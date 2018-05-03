package it.cascino.time.dbmysql.managmentbean;

import java.io.Serializable;
import java.util.List;
import it.cascino.time.dbmysql.model.MysMyfotxar;
import it.cascino.time.utils.Resources;
import it.cascino.time.dbmysql.dao.MysMyfotxarDao;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.log4j.Logger;

public class MysMyfotxarDaoMng implements MysMyfotxarDao, Serializable{
	private static final long serialVersionUID = 1L;
	private Resources res = new Resources();
	private EntityManager em = res.getEmMysql();
	private EntityTransaction utx = res.getUtxMysql();
	
	Logger log = Logger.getLogger(MysMyfotxarDaoMng.class);
	
	@SuppressWarnings("unchecked")
	public List<MysMyfotxar> getAll(){
		List<MysMyfotxar> o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("Myfotxar.findAll");
				o = (List<MysMyfotxar>)query.getResultList();
			}catch(NoResultException e){
				o = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}
	
	public void salva(MysMyfotxar o){
		try{
			try{
				utx.begin();
				// precodice.setId(null);
				log.info("salva: " + o.toString());
				em.persist(o);
			}finally{
				utx.commit();
			}
		}catch(Exception e){
			log.fatal(e.toString());
		}
	}
	
//	public void aggiorna(MysMyfotxar o){
//		try{
//			try{
//				utx.begin();
//				log.info("aggiorna: " + o.toString());
//				em.merge(o);
//			}finally{
//				utx.commit();
//			}
//		}catch(Exception e){
//			log.fatal(e.toString());
//		}
//	}
	
//	public void elimina(MysMyfotxar oElimina){
//		// log.info("tmpDEBUGtmp: " + "> " + "elimina(" + produttoreElimina + ")");
//		try{
//			try{
//				utx.begin();
//				MysMyfotxar o = em.find(MysMyfotxar.class, oElimina.getCprecDarti());
//				log.info("elimina: " + o.toString());
//				em.remove(o);
//			}finally{
//				utx.commit();
//			}
//		}catch(Exception e){
//			log.fatal(e.toString());
//		}
//	}
	
//	public MysMyfotxar getMyfotxarDaCprecDarti(Integer idPrecDarti){
//		MysMyfotxar o = new MysMyfotxar();
//		try{
//			try{
//				utx.begin();
//				Query query = em.createNamedQuery("Myfotxar.findByCprecDarti", MysMyfotxar.class);
//				query.setParameter("cprec_darti", Integer.toString(idPrecDarti));
//				o = (MysMyfotxar)query.getSingleResult();
//			}catch(NoResultException e){
//				o = null;
//			}
//			utx.commit();
//		}catch(Exception e){
//			log.fatal(e.toString());
//		}
//		return o;
//	}
	
	public void svuotaTabella(){
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("Myfotxar.svuota");
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
