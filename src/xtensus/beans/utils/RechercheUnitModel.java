package xtensus.beans.utils;

public class RechercheUnitModel{
	
	private String idUnit;
	private String nameUnit;
	private String shortnameUnit;
	private String descriptionUnit;
	private String nameResponsable;
	private String nameSecretary;
	
	public RechercheUnitModel() {
		this.idUnit = "";
		this.nameUnit = "";
		this.shortnameUnit = "";
		this.descriptionUnit = "";
		this.nameResponsable = "";
		this.nameSecretary = "";
	}
	
	//Getters and Setters
	public void setIdUnit(String idUnit) {
		this.idUnit = idUnit;
	}
	public String getIdUnit() {
		return idUnit;
	}
	public void setNameUnit(String nameUnit) {
		this.nameUnit = nameUnit;
	}
	public String getNameUnit() {
		return nameUnit;
	}
	public void setNameResponsable(String nameResponsable) {
		this.nameResponsable = nameResponsable;
	}
	public String getNameResponsable() {
		return nameResponsable;
	}
	public void setNameSecretary(String nameSecretary) {
		this.nameSecretary = nameSecretary;
	}
	public String getNameSecretary() {
		return nameSecretary;
	}

	public void setShortnameUnit(String shortnameUnit) {
		this.shortnameUnit = shortnameUnit;
	}

	public String getShortnameUnit() {
		return shortnameUnit;
	}

	public void setDescriptionUnit(String descriptionUnit) {
		this.descriptionUnit = descriptionUnit;
	}

	public String getDescriptionUnit() {
		return descriptionUnit;
	}
}
