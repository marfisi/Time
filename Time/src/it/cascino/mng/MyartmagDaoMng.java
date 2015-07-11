package it.cascino.mng;

import java.io.Serializable;
import java.util.List;
import it.cascino.model.Myartmag;
import it.cascino.dao.MyartmagDao;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class MyartmagDaoMng implements MyartmagDao, Serializable{
	private static final long serialVersionUID = 1L;
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Time");
	private EntityManager em = emf.createEntityManager();
	private EntityTransaction utx = em.getTransaction();
	
	Logger log = Logger.getLogger(MyartmagDaoMng.class);
	
	@SuppressWarnings("unchecked")
	public List<Myartmag> getAll(){
		List<Myartmag> articoli = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("Myartmag.findAll");
				articoli = (List<Myartmag>)query.getResultList();
			}catch(NoResultException e){
				articoli = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return articoli;
	}
	
	// public void definisciFratelloMaggiore(String oarti, String oarti_xgrup){
	// // Myartmag articolo = new Myartmag(oarti, oarti_xgrup);
	// // int upd = -1;
	// try{
	// try{
	// utx.begin();
	// // log.info("aggiorna: " + articolo.getOarti() + ", " + articolo.getOartiXgrup());
	// log.info("aggiorna: " + "oarti " + oarti + ", " + "oarti_xgrup " + oarti_xgrup);
	// // em.merge(articolo);
	// String sql = "update myartmag " +
	// "set oarti_xgrup = :oarti_xgrup " +
	// "where oarti = :oarti";
	// Query query = em.createNativeQuery(sql);
	// query.setParameter("oarti", oarti);
	// query.setParameter("oarti_xgrup", oarti_xgrup);
	// // upd =
	// query.executeUpdate();
	// }finally{
	// utx.commit();
	// }
	// }catch(Exception e){
	// log.fatal(e.toString());
	// }
	// }
	//
	// public void definisciFratelloMaggiore(List<String> oartiLs, List<String> oarti_xgrupLs){
	// // int upd = -1;
	// try{
	// try{
	// utx.begin();
	// Iterator<String> iterOarti = oartiLs.iterator();
	// Iterator<String> iterOartiXgrup = oarti_xgrupLs.iterator();
	// while(iterOarti.hasNext()){
	// String oarti = iterOarti.next();
	// String oarti_xgrup = iterOartiXgrup.next();
	// log.info("aggiorna: " + "oarti " + oarti + ", " + "oarti_xgrup " + oarti_xgrup);
	// String sql = "update myartmag " +
	// "set oarti_xgrup = :oarti_xgrup " +
	// "where oarti = :oarti";
	// Query query = em.createNativeQuery(sql);
	// query.setParameter("oarti", oarti);
	// query.setParameter("oarti_xgrup", oarti_xgrup);
	// // upd =
	// query.executeUpdate();
	// }
	// }finally{
	// utx.commit();
	// }
	// }catch(Exception e){
	// log.fatal(e.toString());
	// }
	// }
	
	// public void aggiornaXgrupCprec(String oartiAry[], String oarti_xgrupAry[], String cprec_dartiAry[]){
	// // int upd = -1;
	// try{
	// try{
	// utx.begin();
	// for(int i = 0; i < oartiAry.length; i++){
	// String oarti = oartiAry[i];
	// String oarti_xgrup = oarti_xgrupAry[i];
	// String cprec_darti = cprec_dartiAry[i];
	// log.info("aggiorna: " + "oarti " + oarti + ", " + "oarti_xgrup " + oarti_xgrup + ", " + "cprec_darti " + cprec_darti);
	// Query query = em.createNamedQuery("Myartmag.updById");
	// query.setParameter("oarti", oarti);
	// query.setParameter("oarti_xgrup", oarti_xgrup);
	// query.setParameter("cprec_darti", cprec_darti);
	// // upd =
	// query.executeUpdate();
	// }
	// }finally{
	// utx.commit();
	// }
	// }catch(Exception e){
	// log.fatal(e.toString());
	// }
	// }
	
	public void aggiornaXgrupCprec(Myartmag myartmag[]){
		// int upd = -1;
		try{
			try{
				utx.begin();
				for(int i = 0; i < myartmag.length; i++){
					String oarti = myartmag[i].getOarti();
					String oarti_xgrup = myartmag[i].getOartiXgrup();
					String cprec_darti = myartmag[i].getCprecDarti();
					if(StringUtils.equals(oarti_xgrup, "0") && StringUtils.equals(cprec_darti, "1")){
						continue;
					}
					log.info("aggiorna: " + "oarti " + oarti + ", " + "oarti_xgrup " + oarti_xgrup + ", " + "cprec_darti " + cprec_darti);
					Query query = em.createNamedQuery("Myartmag.updById");
					query.setParameter("oarti", oarti);
					query.setParameter("oarti_xgrup", oarti_xgrup);
					query.setParameter("cprec_darti", cprec_darti);
					// upd =
					query.executeUpdate();
					if(i % 20 == 0){ // 20, same as the JDBC batch size
						em.flush();
						em.clear();
					}
				}
			}finally{
				utx.commit();
			}
		}catch(Exception e){
			log.fatal(e.toString());
		}
	}
	
	public void resettaTabella(){
		try{
			try{
				utx.begin();
				String sql = "update myartmag set oarti_xgrup='0', cprec_darti='1' where csoci!='' and iarti!='' and oarti!=''";
				Query query = em.createNativeQuery(sql);
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
