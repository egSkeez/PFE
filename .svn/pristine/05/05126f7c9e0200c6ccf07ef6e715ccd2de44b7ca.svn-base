package xtensus.ldap.model;

import java.util.List;

public class Person implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7230578214907789665L;
	private int id;
	private String cn;
	private String nom;
	private String prenom;
	private String telephoneNumber;
	private String email;
	private String adresse;
	private String fax;
	private Integer codePostal;
	private String login;
	private String pwd;
	private String title;
	private String shortName;
	private Unit associatedDirection;
	private Service associatedService;
	private BOC associatedBOC;
	private boolean responsable;
	private boolean secretary;
	private boolean agent;
	private boolean boc;
	private List<Group> listAffectedGroups;
	private String rowKeyPerson;
	private boolean responsibleResponse;
	private String prenomUserAr;
	private String nomUserAr;
	//==============KHA agent/responsable BO
	private boolean responsableBO;
	private boolean agentBO;
	//================
	public Person() {
		this.responsable = false;
		this.secretary = false;
		this.agent = false;
		this.boc = false;
		this.associatedDirection = null;
		this.associatedService = null;
		this.associatedBOC = null;
		this.shortName = "";
	}

	public Person(int id, String cn, String nom, String prenom,
			String shortName, String telephoneNumber, String email,
			String adresse, String fax, Integer codePostal, String login,
			String pwd) {
		this.id = id;
		this.cn = cn;
		this.nom = nom;
		this.prenom = prenom;
		this.shortName = shortName;
		this.telephoneNumber = telephoneNumber;
		this.email = email;
		this.adresse = adresse;
		this.fax = fax;
		this.codePostal = codePostal;
		this.login = login;
		this.pwd = pwd;
	}

	public Person(int id, String cn, String nom, String prenom,
			String shortName, String telephoneNumber, String email,
			String adresse, String fax, Integer codePostal, String login,
			String pwd, boolean responsable, boolean secretary) {
		this.id = id;
		this.cn = cn;
		this.nom = nom;
		this.prenom = prenom;
		this.shortName = shortName;
		this.telephoneNumber = telephoneNumber;
		this.email = email;
		this.adresse = adresse;
		this.fax = fax;
		this.codePostal = codePostal;
		this.login = login;
		this.pwd = pwd;
		this.responsable = responsable;
		this.secretary = secretary;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public String getCn() {
		return cn;
	}

	public void setRowKeyPerson(String rowKeyPerson) {
		this.rowKeyPerson = rowKeyPerson;
	}

	public String getRowKeyPerson() {
		return rowKeyPerson;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getLogin() {
		return login;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getPwd() {
		return pwd;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public Integer getCodePostal() {
		return codePostal;
	}

	public void setCodePostal(Integer codePostal) {
		this.codePostal = codePostal;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getFax() {
		return fax;
	}

	public void setListAffectedGroups(List<Group> listAffectedGroups) {
		this.listAffectedGroups = listAffectedGroups;
	}

	public List<Group> getListAffectedGroups() {
		return listAffectedGroups;
	}

	public Unit getAssociatedDirection() {
		return associatedDirection;
	}

	public void setAssociatedDirection(Unit associatedDirection) {
		this.associatedDirection = associatedDirection;
	}

	public Service getAssociatedService() {
		return associatedService;
	}

	public void setAssociatedService(Service associatedService) {
		this.associatedService = associatedService;
	}

	public BOC getAssociatedBOC() {
		return associatedBOC;
	}

	public void setAssociatedBOC(BOC associatedBOC) {
		this.associatedBOC = associatedBOC;
	}

	public boolean isResponsable() {
		return responsable;
	}

	public void setResponsable(boolean responsable) {
		this.responsable = responsable;
	}

	public boolean isSecretary() {
		return secretary;
	}

	public void setSecretary(boolean secretary) {
		this.secretary = secretary;
	}

	public boolean isAgent() {
		return agent;
	}

	public void setAgent(boolean agent) {
		this.agent = agent;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setBoc(boolean boc) {
		this.boc = boc;
	}

	public boolean isBoc() {
		return boc;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setResponsibleResponse(boolean responsibleResponse) {
		this.responsibleResponse = responsibleResponse;
	}

	public boolean isResponsibleResponse() {
		return responsibleResponse;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", cn=" + cn + ", nom=" + nom + ", prenom="
				+ prenom + ", telephoneNumber=" + telephoneNumber + ", email="
				+ email + ", adresse=" + adresse + ", fax=" + fax
				+ ", codePostal=" + codePostal + ", login=" + login + ", pwd="
				+ pwd + ", title=" + title + ", shortName=" + shortName
				+ ", associatedDirection=" + associatedDirection
				+ ", associatedService=" + associatedService
				+ ", associatedBOC=" + associatedBOC + ", responsable="
				+ responsable + ", secretary=" + secretary + ", agent=" + agent
				+ ", boc=" + boc + ", listAffectedGroups=" + listAffectedGroups
				+ ", rowKeyPerson=" + rowKeyPerson + ", responsibleResponse="
				+ responsibleResponse + "]";
	}

	public String getPrenomUserAr() {
		return prenomUserAr;
	}

	public void setPrenomUserAr(String prenomUserAr) {
		this.prenomUserAr = prenomUserAr;
	}

	public String getNomUserAr() {
		return nomUserAr;
	}

	public void setNomUserAr(String nomUserAr) {
		this.nomUserAr = nomUserAr;
	}

	public boolean isResponsableBO() {
		return responsableBO;
	}

	public void setResponsableBO(boolean responsableBO) {
		this.responsableBO = responsableBO;
	}

	public boolean isAgentBO() {
		return agentBO;
	}

	public void setAgentBO(boolean agentBO) {
		this.agentBO = agentBO;
	}

}
