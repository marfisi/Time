package it.cascino.dao;

import java.util.List;
import it.cascino.model.Produttori;

public interface ProduttoriDao{
	List<Produttori> getAll();
	
	void close();
}
