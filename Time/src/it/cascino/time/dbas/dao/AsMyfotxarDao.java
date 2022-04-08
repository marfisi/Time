package it.cascino.time.dbas.dao;

import java.util.List;
import it.cascino.time.dbas.model.AsMyfotxar;

public interface AsMyfotxarDao{
	List<AsMyfotxar> getAll();
	
	void salva(AsMyfotxar o);
	
	void detach(Object entity);
		
	void close();
}
