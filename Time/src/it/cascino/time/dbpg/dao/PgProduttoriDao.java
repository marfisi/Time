package it.cascino.time.dbpg.dao;

import java.util.List;
import it.cascino.time.dbpg.model.PgProduttori;

public interface PgProduttoriDao{
	List<PgProduttori> getAll();
	
	void close();
}
