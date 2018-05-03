package it.cascino.time.dbpg.managmenntbean;

import java.io.Serializable;
import java.util.List;
import it.cascino.time.dbpg.model.PgArticoli;
import it.cascino.time.utils.Resources;
import it.cascino.time.dbpg.dao.PgArticoliDao;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.log4j.Logger;

public class PgArticoliDaoMng implements PgArticoliDao, Serializable{
	private static final long serialVersionUID = 1L;
	private Resources res = new Resources();
	private EntityManager em = res.getEmPg();
	private EntityTransaction utx = res.getUtxPg();
	
	Logger log = Logger.getLogger(PgArticoliDaoMng.class);
	
	@SuppressWarnings("unchecked")
	public List<PgArticoli> getAll(){
		List<PgArticoli> articoli = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("Articoli.findAll");
				articoli = (List<PgArticoli>)query.getResultList();
			}catch(NoResultException e){
				articoli = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
		}
		return articoli;
	}
	
	public PgArticoli getArticoloDaCodice(String codiceArticolo){
		PgArticoli articolo = null;
		try{
			try{
				utx.begin();
				Query query = em.createNamedQuery("Articoli.findByCodiceArticolo");
				query.setParameter("codiceArticolo", codiceArticolo);
				articolo = (PgArticoli)query.getSingleResult();
			}catch(NoResultException e){
				articolo = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
			utx.commit();
		}
		return articolo;
	}
	
	public Integer getFotoArticoloDaCodice(String codiceArticolo){
//		log.info("start: " + "> " + "getFotoArticoloDaCodice(" + codiceArticolo + ")");
		Integer foto = null;
		try{
			try{
				utx.begin();
				String sql = "select foto " +
				"from ( " +
				"select row_number() OVER () AS rownum, selord.foto  " +
				"from (select foto " +
				"from articoli_foto af join articoli a on af.articolo = a.id  " +
				"where upper(codice) = :codice " +
				"order by ordinamento, af.updtime desc) as selord) as fotord " +
				"where fotord.rownum = 1";
				Query query = em.createNativeQuery(sql);
				query.setParameter("codice", codiceArticolo);
				foto = (Integer)query.getSingleResult();
			}catch(NoResultException e){
				foto = -1;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
			utx.commit();
		}
//		log.info("stop: " + "< " + "getFotoArticoloDaCodice");
		return foto;
	}
	
	@SuppressWarnings("unchecked")
	public List<PgArticoli> getArticoloFratelliLsDaCodiceFoto(Integer idFoto){
//		log.info("start: " + "> " + "getArticoloFratelliLsDaCodiceFoto(" + idFoto + ")");
		List<PgArticoli> articoli = null;
		try{
			try{
				utx.begin();
				String sql = "select a.* " +
				"from articoli a inner join articoli_foto af on a.id = af.articolo " +
				"where af.foto = :idFoto";
				Query query = em.createNativeQuery(sql, PgArticoli.class); // Integer.class); // Native
				query.setParameter("idFoto", idFoto);
				articoli = (List<PgArticoli>)query.getResultList();
			}catch(NoResultException e){
				articoli = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
			utx.commit();
		}
//		log.info("stop: " + "< " + "getArticoloFratelliLsDaCodiceFoto");
		return articoli;
	}
	
	public boolean checkArticoloHaComePrimaFotoIdFoto(PgArticoli art, Integer idFoto){
//		log.info("start: " + "> " + "checkArticoloHaComePrimaFotoIdFoto(" + art + ", " + idFoto + ")");
		Integer foto = null;
		try{
			try{
				utx.begin();
				String sql = "select fotord.foto " +
				"from ( " +
				"select row_number() OVER () AS rownum, * " +
				"from (select foto " +
				"from articoli_foto " +
				"where articolo = :idArticolo " +
				"order by ordinamento, updtime desc) as fotordin) as fotord " +
				"where fotord.rownum = 1";
				Query query = em.createNativeQuery(sql);
				query.setParameter("idArticolo", art.getId());
				foto = (Integer)query.getSingleResult();
			}catch(NoResultException e){
				foto = -1;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
			utx.commit();
		}
//		log.info("stop: " + "< " + "checkArticoloHaComePrimaFotoIdFoto");
		return foto.equals(idFoto);
	}
	
	public String getFotoNameArticoloDaId(Integer idFoto){
//		log.info("start: " + "> " + "getFotoNameArticoloDaId(" + idFoto + ")");
		String fotoname = null;
		try{
			try{
				utx.begin();
				String sql = "select originale " +
				"from foto " +
				"where id = :idFoto";
				Query query = em.createNativeQuery(sql);
				query.setParameter("idFoto", idFoto);
				fotoname = (String)query.getSingleResult();
			}catch(NoResultException e){
				fotoname = null;
			}
			utx.commit();
		}catch(Exception e){
			log.fatal(e.toString());
			utx.commit();
		}
//		log.info("stop: " + "< " + "getFotoNameArticoloDaId");
		return fotoname;
	}
	
	// public List<String> getFotoArticoloOrdLsDaIdArticolo(String codiceArticolo){
	// log.info("tmpDEBUGtmp: " + "> " + "getFotoArticoloOrdLsDaIdArticolo(" + codiceArticolo + ")");
	// List<Foto> foto = null;
	// try{
	// try{
	// utx.begin();
	// String sql = "select f.* " +
	// "from ( " +
	// "select row_number() OVER () AS rownum, selord.foto  " +
	// "from (select foto " +
	// "from articoli_foto af join articoli a on af.articolo = a.id  " +
	// "where upper(codice) =  :codice " +
	// "order by ordinamento, af.updtime desc) as selord) as selordjoin left join foto f on selordjoin.foto = f.id";
	// Query query = em.createNativeQuery(sql, String.class); // Native
	// query.setParameter("codice", codiceArticolo);
	// foto = (List<String>)query.getResultList();
	// }catch(NoResultException e){
	// foto = null;
	// }
	// utx.commit();
	// }catch(Exception e){
	// log.fatal(e.toString());
	// }
	// log.info("tmpDEBUGtmp: " + "< " + "getFotoArticoloOrdLsDaIdArticolo");
	// return foto;
	// }
	//
	
	public void close(){
		res.close();
		log.info("chiuso");
	}
}
