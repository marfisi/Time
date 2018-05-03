package it.cascino.time.dbas.dao;

import java.util.List;
import it.cascino.time.dbas.model.AsTabe20f;

public interface AsTabe20fDao{
	List<AsTabe20f> getAll();

	AsTabe20f getMarchio(String tbele);

	void close();
}
