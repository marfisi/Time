package it.cascino.time.dbmysql.dao;

import java.util.List;
import it.cascino.time.dbmysql.model.MysMyartmag;

public interface MysMyartmagDao{
	List<MysMyartmag> getAll();
	
	void aggiornaXgrup(MysMyartmag myartmag[]);
	
	void resettaTabella();
	
	void close();
}
