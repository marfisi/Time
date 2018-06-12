package it.cascino.time.dbmysql.dao;

import java.util.List;
import it.cascino.time.dbmysql.model.MysMyartmag;

public interface MysMyartmagDao{
	List<MysMyartmag> getAll();
	
	MysMyartmag getDaOarti(String oarti);
	
	void aggiornaXgrup(MysMyartmag myartmag[]);
	
	void resettaTabella();
	
	void close();
}
