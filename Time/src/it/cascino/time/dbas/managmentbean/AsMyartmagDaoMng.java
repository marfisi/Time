package it.cascino.time.dbas.managmentbean;

import java.io.Serializable;
import java.util.List;
import it.cascino.time.utils.Resources;
import it.cascino.time.dbas.dao.AsMyartmagDao;
import it.cascino.time.dbas.model.AsMyartmag;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class AsMyartmagDaoMng implements AsMyartmagDao, Serializable{
	private static final long serialVersionUID = 1L;
	private Resources res = new Resources();
	private EntityManager em = res.getEmAs();
	private EntityTransaction utx = res.getUtxAs();	
	
	Logger log = Logger.getLogger(AsMyartmagDaoMng.class);
	
	@SuppressWarnings("unchecked")
	public List<AsMyartmag> getAll(){
		List<AsMyartmag> articoli = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("Myartmag.findAll");
				articoli = (List<AsMyartmag>)query.getResultList();
			}catch(NoResultException e){
				articoli = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return articoli;
	}
	
	public AsMyartmag getDaOarti(String oarti){
		AsMyartmag o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("Myartmag.findByOarti");
				query.setParameter("oarti", oarti);
				o = (AsMyartmag)query.getSingleResult();
			}catch(NoResultException e){
				o = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}
	
	public void aggiornaXgrup(AsMyartmag myartmag[]){
		try{
			try{
				utx.begin();
				for(int i = 0; i < myartmag.length; i++){
					String oarti = myartmag[i].getOarti();
					String oarti_xgrup = myartmag[i].getOartiXgrup();
					if(StringUtils.equals(oarti_xgrup, "0")){
						continue;
					}
					log.info("aggiorna: " + "oarti " + oarti + ", " + "oarti_xgrup " + oarti_xgrup);
					Query query = em.createNamedQuery("Myartmag.updById");
					query.setParameter("oarti", oarti);
					query.setParameter("oarti_xgrup", oarti_xgrup);
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
	
	public void close(){
		res.close();
		log.info("chiuso");
	}
}
