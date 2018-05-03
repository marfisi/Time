package it.cascino.time.dbas.model;

import java.io.Serializable;
import javax.persistence.*;
import org.apache.commons.lang3.StringUtils;

/**
* The persistent class for the cas_dat/tabe20f database table.
* 
*/
@Entity(name="Tabe20f")
@NamedQueries({
	@NamedQuery(name = "AsTabe20f.findAll", query = "SELECT t FROM Tabe20f t WHERE t.tbnot = 'MARC' order by t.tbele"),
	@NamedQuery(name = "AsTabe20f.findByTbele", query = "SELECT t FROM Tabe20f t WHERE t.tbnot = 'MARC' and t.tbele = :tbele")
})
public class AsTabe20f implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String tbnot;
	private String tbele;
	private String tbdes;
	private String tbcom;

	public AsTabe20f(){
	}
	
	public AsTabe20f(String tbnot, String tbele, String tbdes, String tbcom){
		super();
		this.tbnot = tbnot;
		this.tbele = tbele;
		this.tbdes = tbdes;
		this.tbcom = tbcom;
	}

	public String getTbnot(){
		return tbnot;
	}

	public void setTbnot(String tbnot){
		this.tbnot = tbnot;
	}

	@Id
	public String getTbele(){
		return tbele;
	}

	public void setTbele(String tbele){
		this.tbele = tbele;
	}

	public String getTbdes(){
		return tbdes;
	}

	public void setTbdes(String tbdes){
		this.tbdes = tbdes;
	}

	public String getTbcom(){
		return tbcom;
	}

	public void setTbcom(String tbcom){
		this.tbcom = tbcom;
	}

	@Override
	public String toString(){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".") + 1));
		stringBuilder.append("[");
		stringBuilder.append("tbnot=" + StringUtils.trim(tbnot)).append(", ");
		stringBuilder.append("tbele=" + StringUtils.trim(tbele)).append(", ");
		stringBuilder.append("tbdes=" + StringUtils.trim(tbdes)).append(", ");
		stringBuilder.append("tbcom=" + StringUtils.trim(tbcom));
		stringBuilder.append("]");
		return stringBuilder.toString();
	}

	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tbele== null) ? 0 : tbele.hashCode());
		result = prime * result + ((tbcom == null) ? 0 : tbcom.hashCode());
		result = prime * result + ((tbdes == null) ? 0 : tbdes.hashCode());
		result = prime * result + ((tbnot == null) ? 0 : tbnot.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof AsTabe20f){
			if(this.tbele == ((AsTabe20f)obj).tbele){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
}