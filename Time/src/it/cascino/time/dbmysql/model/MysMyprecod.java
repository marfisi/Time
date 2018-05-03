package it.cascino.time.dbmysql.model;

import java.io.Serializable;
import java.sql.Timestamp;
//import javax.inject.Inject;
import javax.persistence.*;
//import org.hibernate.annotations.GenericGenerator;
//import org.jboss.logging.Logger;
//import java.sql.Timestamp;

/**
* The persistent class for the myprecod database table.
* 
*/
@Entity(name="Myprecod")
@NamedQueries({
		@NamedQuery(name = "Myprecod.findAll", query = "SELECT p FROM Myprecod p"),
		@NamedQuery(name = "Myprecod.findByCprecDarti", query = "SELECT p FROM Myprecod p WHERE p.cprecDarti = :cprec_darti"),
		@NamedQuery(name = "Myprecod.svuota", query = "DELETE FROM Myprecod p WHERE p.cprecDarti != '1'")
})
public class MysMyprecod implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Logger
	 */
//	@Inject
//	private Logger log;
	
//	private Integer id;
	private String csoci;
	private String cprec_darti;
	private Timestamp iprec_darti;
	private String tprec_darti;
	
	public MysMyprecod(){
	}
	
	public MysMyprecod(String csoci, String cprec_darti, Timestamp iprec_darti, String tprec_darti){
		super();
		this.csoci = csoci;
		this.cprec_darti = cprec_darti;
		this.iprec_darti = iprec_darti;
		this.tprec_darti = tprec_darti;
	}

	public String getCsoci(){
		return csoci;
	}

	public void setCsoci(String csoci){
		this.csoci = csoci;
	}
	
	@Id
	@Column(name = "cprec_darti")
	public String getCprecDarti(){
		return this.cprec_darti;
	}
	
	public void setCprecDarti(String cprec_darti){
		this.cprec_darti = cprec_darti;
	}

	@Column(name = "iprec_darti")
	public Timestamp getIprecDarti(){
		return iprec_darti;
	}

	public void setIprecDarti(Timestamp iprec_darti){
		this.iprec_darti = iprec_darti;
	}

	@Column(name = "tprec_darti")
	public String getTprecDarti(){
		return tprec_darti;
	}

	public void setTprecDarti(String tprec_darti){
		this.tprec_darti = tprec_darti;
	}
	
	@Override
	public String toString(){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".") + 1));
		stringBuilder.append("[");
		stringBuilder.append("csoci=" + csoci).append(", ");
		stringBuilder.append("cprec_darti=" + cprec_darti).append(", ");
		stringBuilder.append("iprec_darti=" + iprec_darti).append(", ");
		stringBuilder.append("tprec_darti=" + tprec_darti);
		stringBuilder.append("]");
		return stringBuilder.toString();
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof MysMyprecod){
			if(this.cprec_darti == ((MysMyprecod)obj).cprec_darti){
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
		result = prime * result + ((csoci == null) ? 0 : csoci.hashCode());
		result = prime * result + ((cprec_darti == null) ? 0 : cprec_darti.hashCode());
		result = prime * result + ((iprec_darti == null) ? 0 : iprec_darti.hashCode());
		result = prime * result + ((tprec_darti == null) ? 0 : tprec_darti.hashCode());
		return result;
	}
}