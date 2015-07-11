package it.cascino.dao;

import java.util.List;
import it.cascino.model.Articoli;

public interface ArticoliDao{
	List<Articoli> getAll();
	
	Articoli getArticoloDaCodice(String codiceArticolo);
	
	Integer getFotoArticoloDaCodice(String codiceArticolo);
	
	List<Articoli> getArticoloFratelliLsDaCodiceFoto(Integer idFoto);
	
	String getFotoNameArticoloDaId(Integer idFoto);
	
	boolean checkArticoloHaComePrimaFotoIdFoto(Articoli art, Integer idFoto);
	
	void close();
}
