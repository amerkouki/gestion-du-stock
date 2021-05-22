package stage_v2;

public class item {
	private String id;
	private String anCode;
	private String code;
	private String desgnation ;
	private String unite;
	private String stock;
	private String consommation;
	private String cout;
	private String ref;
	private String fournisseur;
	private String machine;
	
	public item(String id,String anCode ,String code,String desgnation ,String unite,String stock,String consommation,String cout,String ref,String fournisseur,String machine)
	{
		this.id=id;
		this.anCode=anCode;
		this.code=code;
		this.desgnation=desgnation;
		this.unite=unite;
		this.stock=stock;
		this.consommation=consommation;
		this.cout=cout;
		this.ref=ref;
		this.fournisseur=fournisseur;
		this.machine=machine;
	}
	
	public String getId () {return id;}
	public String getAnCode() {return anCode;}
	public String getCode() {return code;}
	public String getdesgnation() {return desgnation;}
	public String getunite() {return unite;}
	public String getstock() {return stock;}
	public String getconsommation() {return consommation;}
	public String getcout() {return cout;}
	public String getref() {return ref;}
	public String getfournisseur() {return fournisseur;}
	public String getmachine() {return machine;}
	
	public void setId (String val) {id=val;}
	public void setAnCode(String val) { anCode=val;}
	public void setCode(String val) { code=val;}
	public void setdesgnation(String val) { desgnation=val;}
	public void setunite(String val) { unite=val;}
	public void setstock(String val) { stock=val;}
	public void setconsommation(String val) { consommation=val;}
	public void setcout(String val) { cout=val;}
	public void setref(String val) { ref=val;}
	public void setfournisseur(String val) { fournisseur=val;}
	public void setmachine(String val) { machine=val;}
}
