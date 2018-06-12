package it.cascino.time.dbmysql.managmentbean;

import java.io.Serializable;
import java.util.List;
import it.cascino.time.dbmysql.model.MysMyartmag;
import it.cascino.time.utils.Resources;
import it.cascino.time.dbas.model.AsAnmar0f;
import it.cascino.time.dbmysql.dao.MysMyartmagDao;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class MysMyartmagDaoMng implements MysMyartmagDao, Serializable{
	private static final long serialVersionUID = 1L;
	private Resources res = new Resources();
	private EntityManager em = res.getEmMysql();
	private EntityTransaction utx = res.getUtxMysql();
	
	Logger log = Logger.getLogger(MysMyartmagDaoMng.class);
	
	@SuppressWarnings("unchecked")
	public List<MysMyartmag> getAll(){
		List<MysMyartmag> articoli = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("Myartmag.findAll");
				articoli = (List<MysMyartmag>)query.getResultList();
			}catch(NoResultException e){
				articoli = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return articoli;
	}
	
	public MysMyartmag getDaOarti(String oarti){
		MysMyartmag o = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("Myartmag.findByOarti");
				query.setParameter("oarti", oarti);
				o = (MysMyartmag)query.getSingleResult();
			}catch(NoResultException e){
				o = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return o;
	}
	
	public void aggiornaXgrup(MysMyartmag myartmag[]){
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
	
	public void resettaTabella(){
		try{
			try{
				utx.begin();
				String sql = "update myartmag set oarti_xgrup='0' where csoci!='' and iarti!='' and oarti!=''";
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
		res.close();
		log.info("chiuso");
	}
}
