package it.cascino.time.dbmysql.dao;

import java.util.List;
import it.cascino.time.dbmysql.model.MysMyprecod;

public interface MysMyprecodDao{
	List<MysMyprecod> getAll();
	
	void salva(MysMyprecod p);
	
	void aggiorna(MysMyprecod p);
	
	void elimina(MysMyprecod p);
	
	MysMyprecod getMyprecodDaCprecDarti(Integer idPrecDarti);
	
	void svuotaTabella();
	
	void close();
}
