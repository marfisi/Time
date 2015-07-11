package it.cascino.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * The persistent class for the produttori database table.
 * 
 */
@Entity(name="Produttori")
@NamedQueries({
		@NamedQuery(name = "Produttori.findAll", query = "SELECT p FROM Produttori p WHERE p.id != 1 order by p.nome")
})
public class Produttori implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String nome;
	private String dati;
	private Integer idFoto;
	private Timestamp updtime;
	
	public Produttori(){
	}
	
	public Produttori(Integer id, String nome, String dati, Integer idFoto, Timestamp updtime){
		super();
		this.id = id;
		this.nome = nome;
		this.dati = dati;
		this.idFoto = idFoto;
		this.updtime = updtime;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId(){
		return this.id;
	}
	
	public void setId(Integer id){
		this.id = id;
	}
	
	public String getNome(){
		return this.nome;
	}
	
	public void setNome(String nome){
		this.nome = nome;
	}
	
	public String getDati(){
		return this.dati;
	}
	
	public void setDati(String dati){
		this.dati = dati;
	}
	
	@Column(name = "foto")
	public Integer getIdFoto(){
		return this.idFoto;
	}
	
	public void setIdFoto(Integer idFoto){
		this.idFoto = idFoto;
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
			stringBuilder.append("nome=" + nome).append(", ");
			stringBuilder.append("dati=" + dati).append(", ");
			stringBuilder.append("foto=" + idFoto);
		}else{
			stringBuilder.append("id=1");
		}
		stringBuilder.append("]");
		return stringBuilder.toString();
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof Produttori){
			if(this.id == ((Produttori)obj).id){
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
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((dati == null) ? 0 : dati.hashCode());
		result = prime * result + ((idFoto == null) ? 0 : idFoto.hashCode());
		return result;
	}
}