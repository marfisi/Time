package it.cascino.model;

import java.io.Serializable;
//import javax.inject.Inject;
import javax.persistence.*;
//import org.hibernate.annotations.GenericGenerator;
//import org.jboss.logging.Logger;
//import java.sql.Timestamp;

/**
* The persistent class for the myartmag database table.
* 
*/
@Entity(name="Myartmag")
@NamedQueries({
		@NamedQuery(name = "Myartmag.findAll", query = "SELECT a FROM Myartmag a order by a.oarti asc"),
		@NamedQuery(name = "Myartmag.findById", query = "SELECT a FROM Myartmag a WHERE a.oarti = :oarti"),
		@NamedQuery(name = "Myartmag.updById", query = "UPDATE Myartmag a set a.oartiXgrup = :oarti_xgrup, a.cprecDarti = :cprec_darti where a.oarti = :oarti")
})
public class Myartmag implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Logger
	 */
//	@Inject
//	private Logger log;
	
//	private Integer id;
	private String oarti;
	private String oarti_xgrup;
	private String cprec_darti;
	
	public Myartmag(){
	}
	
	public Myartmag(String oarti, String oarti_xgrup, String cprec_darti){
		super();
		this.oarti = oarti;
		this.oarti_xgrup = oarti_xgrup;
		this.cprec_darti = cprec_darti;
	}
	
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	public Integer getId(){
//		return this.id;
//	}
//	
//	public void setId(Integer id){
//		this.id = id;
//	}

	
	@Id
//	@GeneratedValue // (strategy = GenerationType.AUTO)
//	@Id
//	@GeneratedValue(generator="system-uuid")
//	@GenericGenerator(name="system-uuid", strategy = "uuid")
//	@Column(name = "oarti", unique = true)
	public String getOarti(){
		return this.oarti;
	}
	
	public void setOarti(String oarti){
		this.oarti = oarti;
	}
	
	@Column(name = "oarti_xgrup")
	public String getOartiXgrup(){
		return this.oarti_xgrup;
	}
	
	public void setOartiXgrup(String oarti_xgrup){
		this.oarti_xgrup = oarti_xgrup;
	}
	
	@Column(name = "cprec_darti")
	public String getCprecDarti(){
		return this.cprec_darti;
	}
	
	public void setCprecDarti(String cprec_darti){
		this.cprec_darti = cprec_darti;
	}
	
	@Override
	public String toString(){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".") + 1));
		stringBuilder.append("[");
//		if(id != null){
			stringBuilder.append("oarti=" + oarti).append(", ");
			stringBuilder.append("oarti_xgrup=" + oarti_xgrup).append(", ");
			stringBuilder.append("cprec_darti=" + cprec_darti);
//		}else{
//			stringBuilder.append("id=1");
//		}
		stringBuilder.append("]");
		return stringBuilder.toString();
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof Articoli){
			if(this.oarti == ((Myartmag)obj).oarti){
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
		result = prime * result + ((oarti == null) ? 0 : oarti.hashCode());
		result = prime * result + ((oarti_xgrup == null) ? 0 : oarti_xgrup.hashCode());
		result = prime * result + ((cprec_darti == null) ? 0 : cprec_darti.hashCode());
		return result;
	}
}