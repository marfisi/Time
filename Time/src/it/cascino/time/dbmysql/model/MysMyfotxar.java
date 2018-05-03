package it.cascino.time.dbmysql.model;

import java.io.Serializable;
import javax.persistence.*;

/**
* The persistent class for the myfotxar database table.
* 
*/
@Entity(name="Myfotxar")
@NamedQueries({
		@NamedQuery(name = "Myfotxar.findAll", query = "SELECT p FROM Myfotxar p"),
		@NamedQuery(name = "Myfotxar.svuota", query = "DELETE FROM Myfotxar p WHERE p.csoci = 'CASC'")
})
public class MysMyfotxar implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String csoci;
	private String oarti;
	private String crevi_ddocm;
	private String ctipo_ddocm;
	private String tfile_ddocm;
	
	public MysMyfotxar(){
	}

	public MysMyfotxar(String csoci, String oarti, String crevi_ddocm, String ctipo_ddocm, String tfile_ddocm){
		super();
		this.csoci = csoci;
		this.oarti = oarti;
		this.crevi_ddocm = crevi_ddocm;
		this.ctipo_ddocm = ctipo_ddocm;
		this.tfile_ddocm = tfile_ddocm;
	}
	
	public String getCsoci(){
		return csoci;
	}

	public void setCsoci(String csoci){
		this.csoci = csoci;
	}
	
	@Id
	public String getOarti(){
		return oarti;
	}

	public void setOarti(String oarti){
		this.oarti = oarti;
	}
	
	@Column(name = "crevi_ddocm")
	public String getCreviDdocm(){
		return crevi_ddocm;
	}

	public void setCreviDdocm(String crevi_ddocm){
		this.crevi_ddocm = crevi_ddocm;
	}

	@Column(name = "ctipo_ddocm")
	public String getCtipoDdocm(){
		return ctipo_ddocm;
	}

	public void setCtipoDdocm(String ctipo_ddocm){
		this.ctipo_ddocm = ctipo_ddocm;
	}

	@Column(name = "tfile_ddocm")
	public String getTfileDdocm(){
		return tfile_ddocm;
	}

	public void setTfileDdocm(String tfile_ddocm){
		this.tfile_ddocm = tfile_ddocm;
	}

	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((crevi_ddocm == null) ? 0 : crevi_ddocm.hashCode());
		result = prime * result + ((csoci == null) ? 0 : csoci.hashCode());
		result = prime * result + ((ctipo_ddocm == null) ? 0 : ctipo_ddocm.hashCode());
		result = prime * result + ((oarti == null) ? 0 : oarti.hashCode());
		result = prime * result + ((tfile_ddocm == null) ? 0 : tfile_ddocm.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof MysMyfotxar){
			if((this.oarti == ((MysMyfotxar)obj).oarti)&&(this.tfile_ddocm == ((MysMyfotxar)obj).tfile_ddocm)){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	@Override
	public String toString(){
		return "MysMyfotxar [csoci=" + csoci + ", oarti=" + oarti + ", crevi_ddocm=" + crevi_ddocm + ", ctipo_ddocm=" + ctipo_ddocm + ", tfile_ddocm=" + tfile_ddocm + "]";
	}
}