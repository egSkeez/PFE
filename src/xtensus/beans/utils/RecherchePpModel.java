package xtensus.beans.utils;

public class RecherchePpModel {
	
	private int idPp;
	private String prenomPp;
	private String nomPp;
	private String cinPp;
	private String telephonePp;
	private String faxPp;
	private String telephonePortablePp;
	private String mailPp;
	private String adressePp;
	private String paysPp;
	private String villePp;
	private String gouvernoratPp;
	private String codePostalPp;
	
	public RecherchePpModel(){
		this.nomPp = "";
		this.cinPp = "";
		this.telephonePp = "";
		this.faxPp = "";
		this.telephonePortablePp = "";
		this.mailPp = "";
		this.adressePp = "";
		this.paysPp = "";
		this.villePp = "";
		this.gouvernoratPp = "";
		this.codePostalPp = "";
	}

//	Getters & Setters
	public void setPrenomPp(String prenomPp) {
		this.prenomPp = prenomPp;
	}

	public String getPrenomPp() {
		return prenomPp;
	}

	public void setNomPp(String nomPp) {
		this.nomPp = nomPp;
	}

	public String getNomPp() {
		return nomPp;
	}

	public void setMailPp(String mailPp) {
		this.mailPp = mailPp;
	}

	public String getMailPp() {
		return mailPp;
	}

	public void setAdressePp(String adressePp) {
		this.adressePp = adressePp;
	}

	public String getAdressePp() {
		return adressePp;
	}

	public void setPaysPp(String paysPp) {
		this.paysPp = paysPp;
	}

	public String getPaysPp() {
		return paysPp;
	}

	public void setVillePp(String villePp) {
		this.villePp = villePp;
	}

	public String getVillePp() {
		return villePp;
	}

	public void setGouvernoratPp(String gouvernoratPp) {
		this.gouvernoratPp = gouvernoratPp;
	}

	public String getGouvernoratPp() {
		return gouvernoratPp;
	}

	public void setCinPp(String cinPp) {
		this.cinPp = cinPp;
	}

	public String getCinPp() {
		return cinPp;
	}

	public void setTelephonePp(String telephonePp) {
		this.telephonePp = telephonePp;
	}

	public String getTelephonePp() {
		return telephonePp;
	}

	public void setFaxPp(String faxPp) {
		this.faxPp = faxPp;
	}

	public String getFaxPp() {
		return faxPp;
	}

	public void setTelephonePortablePp(String telephonePortablePp) {
		this.telephonePortablePp = telephonePortablePp;
	}

	public String getTelephonePortablePp() {
		return telephonePortablePp;
	}

	public void setCodePostalPp(String codePostalPp) {
		this.codePostalPp = codePostalPp;
	}

	public String getCodePostalPp() {
		return codePostalPp;
	}

	public void setIdPp(int idPp) {
		this.idPp = idPp;
	}

	public int getIdPp() {
		return idPp;
	}


}
