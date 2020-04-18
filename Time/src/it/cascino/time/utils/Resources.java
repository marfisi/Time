package it.cascino.time.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Resources{
	private EntityManagerFactory emfAs = null;
	private EntityManager emAs = null;
	private EntityTransaction utxAs = null;
	
	public Resources(){
		super();
		if(emfAs == null) {
			initAs();
		}
	}
	
	private void initAs(){
		emfAs = Persistence.createEntityManagerFactory("DB2AS400");
		emAs = emfAs.createEntityManager();
		utxAs = emAs.getTransaction();
	}
		
	public void close(){
		if(emfAs != null) {
			closeAs();
		}
	}

	private void closeAs(){
		emAs.close();
		emfAs.close();
	}
	
	public EntityManagerFactory getEmfAs(){
		return emfAs;
	}
	
	public void setEmfAs(EntityManagerFactory emfAs){
		this.emfAs = emfAs;
	}
	
	public EntityManager getEmAs(){
		return emAs;
	}
	
	public void setEmAs(EntityManager emAs){
		this.emAs = emAs;
	}
	
	public EntityTransaction getUtxAs(){
		return utxAs;
	}
	
	public void setUtxAs(EntityTransaction utxAs){
		this.utxAs = utxAs;
	}
}
