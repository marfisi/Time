package it.cascino.time.dbmysql.dao;

import java.util.List;
import it.cascino.time.dbmysql.model.MysMyfotxar;

public interface MysMyfotxarDao{
	List<MysMyfotxar> getAll();
	
	void salva(MysMyfotxar p);
	
//	void aggiorna(MysMyfotxar p);
	
//	void elimina(MysMyfotxar p);
	
	// MysMyfotxar getMyfotxarDaCprecDarti(Integer idPrecDarti);
	
	void svuotaTabella();
	
	void close();
}
