package it.cascino.time.dbpg.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * The persistent class for the articoli database table.
 * 
 */
@Entity(name="Articoli")
@NamedQueries({
		@NamedQuery(name = "Articoli.findAll", query = "SELECT a FROM Articoli a order by a.codice asc"),
		@NamedQuery(name = "Articoli.findById", query = "SELECT a FROM Articoli a WHERE a.id = :id"),
		@NamedQuery(name = "Articoli.findByCodiceArticolo", query = "SELECT a FROM Articoli a WHERE a.codice = :codiceArticolo")
})
public class PgArticoli implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String codice;
	private String nome;
	private String descrizione;
	private Integer produttore;
	private String modello;
	private Integer articoloFornitore;
	private Integer tipo;
	private Timestamp updtime;
	
	public PgArticoli(){
	}
	
	public PgArticoli(Integer id, Integer articoloFornitore, String codice, String descrizione, String modello, String nome, Integer produttore, Integer tipo, Timestamp updtime){
		super();
		this.id = id;
		this.codice = codice;
		this.nome = nome;
		this.descrizione = descrizione;
		this.produttore = produttore;
		this.modello = modello;
		this.articoloFornitore = articoloFornitore;
		this.tipo = tipo;
		this.updtime = updtime;
	}
	
	@Id
//	@SequenceGenerator(name = "ARTICOLI_ID_GENERATOR", sequenceName = "ARTICOLI_ID_SEQ", allocationSize = 1, initialValue = 1)
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ARTICOLI_ID_GENERATOR")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId(){
		return this.id;
	}
	
	public void setId(Integer id){
		this.id = id;
	}
	
	public String getCodice(){
		return this.codice;
	}
	
	public void setCodice(String codice){
		this.codice = codice;
	}
	
	public String getNome(){
		return this.nome;
	}
	
	public void setNome(String nome){
		this.nome = nome;
	}
	
	public String getDescrizione(){
		return this.descrizione;
	}
	
	public void setDescrizione(String descrizione){
		this.descrizione = descrizione;
	}
	
	public Integer getProduttore(){
		return this.produttore;
	}
	
	public void setProduttore(Integer produttore){
		this.produttore = produttore;
	}
	
	public String getModello(){
		return this.modello;
	}
	
	public void setModello(String modello){
		this.modello = modello;
	}
	
	@Column(name = "articolo_fornitore")
	public Integer getArticoloFornitore(){
		return this.articoloFornitore;
	}
	
	public void setArticoloFornitore(Integer articoloFornitore){
		this.articoloFornitore = articoloFornitore;
	}
	
	public Integer getTipo(){
		return this.tipo;
	}
	
	public void setTipo(Integer tipo){
		this.tipo = tipo;
	}
	
	@Transient
	@Temporal(TemporalType.TIMESTAMP)
	public Timestamp getUpdtime(){
		return this.updtime;
	}
	
	public void setUpdtime(Timestamp updtime){
		this.updtime = updtime;
	}
	
	@Override
	public String toString(){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".") + 1));
		stringBuilder.append("[");
		if(id != null){
			stringBuilder.append("id=" + id).append(", ");
			stringBuilder.append("codice=" + codice).append(", ");
			stringBuilder.append("nome=" + nome).append(", ");
			stringBuilder.append("descrizione=" + descrizione).append(", ");
			stringBuilder.append("produttore=" + produttore).append(", ");
			stringBuilder.append("modello=" + modello).append(", ");
			stringBuilder.append("articoloFornitore=" + articoloFornitore).append(", ");
			stringBuilder.append("tipo=" + tipo);
		}else{
			stringBuilder.append("id=1");
		}
		stringBuilder.append("]");
		return stringBuilder.toString();
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof PgArticoli){
			if(this.id == ((PgArticoli)obj).id){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((codice == null) ? 0 : codice.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((descrizione == null) ? 0 : descrizione.hashCode());
		result = prime * result + ((produttore == null) ? 0 : produttore.hashCode());
		result = prime * result + ((modello == null) ? 0 : modello.hashCode());
		result = prime * result + ((articoloFornitore == null) ? 0 : articoloFornitore.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		return result;
	}
}