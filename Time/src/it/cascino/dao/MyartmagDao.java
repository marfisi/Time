package it.cascino.dao;

import java.util.List;
//import org.apache.log4j.Logger;
import it.cascino.model.Myartmag;

public interface MyartmagDao{
	List<Myartmag> getAll();
	
//	void definisciFratelloMaggiore(String oarti, String oarti_xgrup);
//	void definisciFratelloMaggiore(List<String> oartiLs, List<String> oarti_xgrupLs);
//	void aggiornaXgrupCprec(String oartiAry[], String oarti_xgrupAry[], String cprec_dartiAry[]);
	void aggiornaXgrupCprec(Myartmag myartmag[]);
	
	void resettaTabella();
	
	void close();
}
