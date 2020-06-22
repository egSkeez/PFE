package xtensus.ldap.model;

import java.util.ArrayList;
import java.util.List;

public class Unit {

	private Integer idUnit;
	private String nameUnit;
	private String shortNameUnit;
	private Person responsibleUnit;
	private Person secretaryUnit;
	private List<Person> membersUnit;
	private List<Unit> listUnitsChildUnit;
	private List<Unit> listAdjoiningUnitsUnit;
	private List<BOC> listBOChildUnit;
	private Unit associatedUnit;
	private BOC associatedBOC;
	private String descriptionUnit;
	private String rowKeyDirection;
	private List<String> titleUnit;
	// C*
	private boolean responsibleResponse;
	// C*
	
	public Unit(){
		this.associatedUnit = null;
		this.associatedBOC = null;
		this.responsibleUnit = null;
		this.secretaryUnit = null;
		this.listUnitsChildUnit = new ArrayList<Unit>();
		this.membersUnit = new ArrayList<Person>();
	}
	
	public Unit(Integer idUnit ,String nameUnit ,Person responsibleUnit ,Person secretaryUnit ,List<Person> membersUnit ,List<Unit> listUnitsChildUnit ,List<Service> listService ,Unit associatedUnit ,int niveauDirection){
		this.idUnit = idUnit;
		this.nameUnit = nameUnit;
		this.responsibleUnit = responsibleUnit;
		this.secretaryUnit = secretaryUnit;
		this.membersUnit = membersUnit;
		this.listUnitsChildUnit = listUnitsChildUnit;
		this.associatedUnit = associatedUnit;
	}
	
	public Unit(Integer idUnit ,String nameUnit ,Person responsibleUnit ,Person secretaryUnit ,List<Person> membersUnit ,List<Unit> listUnitsChildUnit ,List<Service> listService ,BOC associatedBOC ,int niveauDirection){
		this.idUnit = idUnit;
		this.nameUnit = nameUnit;
		this.responsibleUnit = responsibleUnit;
		this.secretaryUnit = secretaryUnit;
		this.membersUnit = membersUnit;
		this.listUnitsChildUnit = listUnitsChildUnit;
		this.associatedBOC = associatedBOC;
	}

	public Integer getIdUnit() {
		return idUnit;
	}

	public void setIdUnit(Integer idUnit) {
		this.idUnit = idUnit;
	}

	public String getNameUnit() {
		return nameUnit;
	}

	public void setNameUnit(String nameUnit) {
		this.nameUnit = nameUnit;
	}

	public Person getResponsibleUnit() {
		return responsibleUnit;
	}

	public void setResponsibleUnit(Person responsibleUnit) {
		this.responsibleUnit = responsibleUnit;
	}

	public Person getSecretaryUnit() {
		return secretaryUnit;
	}

	public void setSecretaryUnit(Person secretaryUnit) {
		this.secretaryUnit = secretaryUnit;
	}

	public List<Person> getMembersUnit() {
		return membersUnit;
	}

	public void setMembersUnit(List<Person> membersUnit) {
		this.membersUnit = membersUnit;
	}

	public List<Unit> getListUnitsChildUnit() {
		return listUnitsChildUnit;
	}

	public void setListUnitsChildUnit(
			List<Unit> listUnitsChildUnit) {
		this.listUnitsChildUnit = listUnitsChildUnit;
	}

	public Unit getAssociatedUnit() {
		return associatedUnit;
	}

	public void setAssociatedUnit(Unit associatedUnit) {
		this.associatedUnit = associatedUnit;
	}

	public BOC getAssociatedBOC() {
		return associatedBOC;
	}

	public void setAssociatedBOC(BOC associatedBOC) {
		this.associatedBOC = associatedBOC;
	}

	public String getDescriptionUnit() {
		return descriptionUnit;
	}

	public void setDescriptionUnit(String descriptionUnit) {
		this.descriptionUnit = descriptionUnit;
	}

	public void setRowKeyDirection(String rowKeyDirection) {
		this.rowKeyDirection = rowKeyDirection;
	}

	public String getRowKeyDirection() {
		return rowKeyDirection;
	}

	public void setListAdjoiningUnitsUnit(
			List<Unit> listAdjoiningUnitsUnit) {
		this.listAdjoiningUnitsUnit = listAdjoiningUnitsUnit;
	}

	public List<Unit> getListAdjoiningUnitsUnit() {
		return listAdjoiningUnitsUnit;
	}

	public void setTitleUnit(List<String> titleUnit) {
		this.titleUnit = titleUnit;
	}

	public List<String> getTitleUnit() {
		return titleUnit;
	}

	public void setShortNameUnit(String shortNameUnit) {
		this.shortNameUnit = shortNameUnit;
	}

	public String getShortNameUnit() {
		return shortNameUnit;
	}

	public void setResponsibleResponse(boolean responsibleResponse) {
		this.responsibleResponse = responsibleResponse;
	}

	public boolean isResponsibleResponse() {
		return responsibleResponse;
	}
	
	
	

	public List<BOC> getListBOChildUnit() {
		return listBOChildUnit;
	}

	public void setListBOChildUnit(List<BOC> listBOChildUnit) {
		this.listBOChildUnit = listBOChildUnit;
	}

	@Override
	public String toString() {
		return "Unit [idUnit=" + idUnit + ", nameUnit=" + nameUnit
				+ ", shortNameUnit=" + shortNameUnit + ", associatedUnit="
				+ associatedUnit + ", associatedBOC=" + associatedBOC
				+ ", descriptionUnit=" + descriptionUnit + ", rowKeyDirection="
				+ rowKeyDirection + ", titleUnit=" + titleUnit
				+ ", responsibleResponse=" + responsibleResponse + "]";
	}
	
}
