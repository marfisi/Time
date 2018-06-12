package it.cascino.time.dbas.model;

import java.io.Serializable;
import javax.persistence.*;
import org.apache.commons.lang3.StringUtils;

/**
* The persistent class for the cas_dat/anmar0f database table.
* 
*/
@Entity(name="Anmar0f")
@NamedQueries({
	@NamedQuery(name = "AsAnmar0f.findAll", query = "SELECT a FROM Anmar0f a order by a.mcomp asc"),
	@NamedQuery(name = "AsAnmar0f.findByMcomp", query = "SELECT a FROM Anmar0f a WHERE a.mcomp = :mcomp")
})
public class AsAnmar0f implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String mcomp;
	private String mdes1;
	private String mdes2;
	private String mdes3;
	private String mfoto;
	private String modif;

	@Id
	public String getMcomp(){
		return mcomp;
	}

	public void setMcomp(String mcomp){
		this.mcomp = mcomp;
	}

	public String getMdes1(){
		return mdes1;
	}

	public void setMdes1(String mdes1){
		this.mdes1 = mdes1;
	}

	public String getMdes2(){
		return mdes2;
	}

	public void setMdes2(String mdes2){
		this.mdes2 = mdes2;
	}

	public String getMdes3(){
		return mdes3;
	}

	public void setMdes3(String mdes3){
		this.mdes3 = mdes3;
	}

	public String getMfoto(){
		return mfoto;
	}

	public void setMfoto(String mfoto){
		this.mfoto = mfoto;
	}

	public String getModif(){
		return modif;
	}

	public void setModif(String modif){
		this.modif = modif;
	}

	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mcomp == null) ? 0 : mcomp.hashCode());
		result = prime * result + ((mdes1 == null) ? 0 : mdes1.hashCode());
		result = prime * result + ((mdes2 == null) ? 0 : mdes2.hashCode());
		result = prime * result + ((mdes3 == null) ? 0 : mdes3.hashCode());
		result = prime * result + ((mfoto == null) ? 0 : mfoto.hashCode());
		result = prime * result + ((modif == null) ? 0 : modif.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof AsAnmar0f){
			if(this.mcomp == ((AsAnmar0f)obj).mcomp){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}	

	@Override
	public String toString(){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".") + 1));
		stringBuilder.append("[");
		stringBuilder.append("mcomp=" + StringUtils.trim(mcomp)).append(", ");
		stringBuilder.append("mdes1=" + StringUtils.trim(mdes1)).append(", ");
		stringBuilder.append("mdes2=" + StringUtils.trim(mdes2)).append(", ");
		stringBuilder.append("mdes3=" + StringUtils.trim(mdes3)).append(", ");
		stringBuilder.append("mfoto=" + StringUtils.trim(mfoto)).append(", ");
		stringBuilder.append("modif=" + StringUtils.trim(modif));
		stringBuilder.append("]");
		return stringBuilder.toString();
	}
}