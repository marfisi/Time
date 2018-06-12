package it.cascino.time.dbas.dao;

import java.util.List;
import it.cascino.time.dbas.model.AsAnmar0f;

public interface AsAnmar0fDao{
	List<AsAnmar0f> getAll();
	
//	void salva(AsAnmar0f a);
//	
//	void aggiorna(AsAnmar0f a);
//	
//	void elimina(AsAnmar0f a);

	AsAnmar0f getGruppoDaMcomp(String mcomp);

	void close();
}
