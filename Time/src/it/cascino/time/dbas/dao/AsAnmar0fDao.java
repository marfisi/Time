package it.cascino.time.dbas.dao;

import java.util.List;
import it.cascino.time.dbas.model.AsAnmar0f;

public interface AsAnmar0fDao{
	List<AsAnmar0f> getAll();
	
	Boolean aggiorna(AsAnmar0f o);

	AsAnmar0f getGruppoDaMcomp(String mcomp);

	void detach(Object entity);
	
	void close();
}
