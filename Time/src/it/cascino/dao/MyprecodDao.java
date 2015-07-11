package it.cascino.dao;

import java.util.List;
import it.cascino.model.Myprecod;

public interface MyprecodDao{
	List<Myprecod> getAll();
	
	void salva(Myprecod p);
	
	void aggiorna(Myprecod p);
	
	void elimina(Myprecod p);
	
	Myprecod getMyprecodDaCprecDarti(Integer idPrecDarti);
	
	void svuotaTabella();
	
	void close();
}
