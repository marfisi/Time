package it.cascino.time.dbas.model;

import java.io.Serializable;
import javax.persistence.*;

/**
* The persistent class for the cas_dat/anmag0f database table.
* 
*/
@Entity(name="Anmag0f")
@NamedQueries({
	@NamedQuery(name = "AsAnmag0f.findAll", query = "SELECT a FROM Anmag0f a WHERE a.atama != 'A' and a.atama != 'S' order by a.mcoda asc"),
	@NamedQuery(name = "AsAnmag0f.findByMcoda", query = "SELECT a FROM Anmag0f a WHERE a.atama != 'A' and a.atama != 'S' and a.mcoda = :mcoda "),
	@NamedQuery(name = "AsAnmag0f.findAllIngrosso", query = "SELECT a FROM Anmag0f a WHERE a.atama != 'A' and a.atama != 'S' and ((a.mdepi = 1) or (a.mdepi = 3))"),
	@NamedQuery(name = "AsAnmag0f.findByMcomp", query = "SELECT a FROM Anmag0f a WHERE a.atama != 'A' and a.atama != 'S' and ((a.mdepi = 1) or (a.mdepi = 3)) and a.mcomp= :mcomp order by a.mcoda asc")
})
public class AsAnmag0f implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Logger
	 */
//	@Inject
//	private Logger log;
	
	private String atama;
	private String mcoda;
	private String mdesc;
	private String mdepi;
	private String mcomp;
	
	public AsAnmag0f(){
	}
	
	public AsAnmag0f(String atama, String mcoda, String mdesc, String mdepi, String mcomp){
		super();
		this.atama = atama;
		this.mcoda = mcoda;
		this.mdesc = mdesc;
		this.mdepi = mdepi;
		this.mcomp = mcomp;
	}

	public String getAtama(){
		return atama;
	}

	public void setAtama(String atama){
		this.atama = atama;
	}

	@Id
	public String getMcoda(){
		return mcoda;
	}

	public void setMcoda(String mcoda){
		this.mcoda = mcoda;
	}
	
	public String getMdesc(){
		return mdesc;
	}

	public void setMdesc(String mdesc){
		this.mdesc = mdesc;
	}

	public String getMdepi(){
		return mdepi;
	}

	public void setMdepi(String mdepi){
		this.mdepi = mdepi;
	}

	public String getMcomp(){
		return mcomp;
	}

	public void setMcomp(String mcomp){
		this.mcomp = mcomp;
	}

	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + ((atama == null) ? 0 : atama.hashCode());
		result = prime * result + ((mcoda == null) ? 0 : mcoda.hashCode());
		result = prime * result + ((mdepi == null) ? 0 : mdepi.hashCode());
		result = prime * result + ((mdesc == null) ? 0 : mdesc.hashCode());
		result = prime * result + ((mcomp == null) ? 0 : mcomp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof AsAnmag0f){
			if(this.mcoda == ((AsAnmag0f)obj).mcoda){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}

	@Override
	public String toString(){
		return "AsAnmag0f [atama=" + atama + ", mcoda=" + mcoda + ", mdesc=" + mdesc + ", mcomp=" + mcomp + "]";
	}
}