package it.cascino.time.dbas.dao;

import java.util.List;
import it.cascino.time.dbas.model.AsMyfotxar;

public interface AsMyfotxarDao{
	List<AsMyfotxar> getAll();
	
	void salva(AsMyfotxar p);
		
	void close();
}
