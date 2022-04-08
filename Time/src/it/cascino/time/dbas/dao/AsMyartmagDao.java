package it.cascino.time.dbas.dao;

import java.util.List;
import it.cascino.time.dbas.model.AsMyartmag;

public interface AsMyartmagDao{
	List<AsMyartmag> getAll();
	
	AsMyartmag getDaOarti(String oarti);
	
	void aggiornaXgrup(AsMyartmag myartmag[]);
	
//	void detach(Object entity);
	
	void close();
}
