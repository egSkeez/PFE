package xtensus.ldap.model;

import xtensus.beans.utils.RecherchePmModel;
import xtensus.beans.utils.RecherchePpModel;
import xtensus.beans.utils.RechercheUnitModel;
import xtensus.beans.utils.RechercheUserModel;

public class ItemSelected {

	private int itemSelectedId;
	private String itemSelectedIdString;
	private String itemSelectedName;
	private Object selectedObject;
	private RechercheUserModel rechercheUserModel;
	private RechercheUnitModel rechercheUnitModel;
	private RecherchePpModel recherchePpModel;
	private RecherchePmModel recherchePmModel;
	private boolean fromSearch;
	// C*
	private boolean responseResponsible;
	private boolean disableResponseResponsible;
	// C*
	
	
	
	public ItemSelected(){
		this.fromSearch = false;
	}
	
	public int getItemSelectedId() {
		return itemSelectedId;
	}
	public void setItemSelectedId(int itemSelectedId) {
		this.itemSelectedId = itemSelectedId;
	}
	public String getItemSelectedName() {
		return itemSelectedName;
	}
	public void setItemSelectedName(String itemSelectedName) {
		this.itemSelectedName = itemSelectedName;
	}

	public void setSelectedObject(Object selectedObject) {
		this.selectedObject = selectedObject;
	}

	public Object getSelectedObject() {
		return selectedObject;
	}

	public void setFromSearch(boolean fromSearch) {
		this.fromSearch = fromSearch;
	}

	public boolean isFromSearch() {
		return fromSearch;
	}

	public void setRechercheUserModel(RechercheUserModel rechercheUserModel) {
		this.rechercheUserModel = rechercheUserModel;
	}

	public RechercheUserModel getRechercheUserModel() {
		return rechercheUserModel;
	}

	public void setRechercheUnitModel(RechercheUnitModel rechercheUnitModel) {
		this.rechercheUnitModel = rechercheUnitModel;
	}

	public RechercheUnitModel getRechercheUnitModel() {
		return rechercheUnitModel;
	}

	public void setRecherchePpModel(RecherchePpModel recherchePpModel) {
		this.recherchePpModel = recherchePpModel;
	}

	public RecherchePpModel getRecherchePpModel() {
		return recherchePpModel;
	}

	public void setRecherchePmModel(RecherchePmModel recherchePmModel) {
		this.recherchePmModel = recherchePmModel;
	}

	public RecherchePmModel getRecherchePmModel() {
		return recherchePmModel;
	}

	public boolean isResponseResponsible() {
		return responseResponsible;
	}

	public void setResponseResponsible(boolean responseResponsible) {
		this.responseResponsible = responseResponsible;
	}

	public boolean isDisableResponseResponsible() {
		return disableResponseResponsible;
	}

	public void setDisableResponseResponsible(boolean disableResponseResponsible) {
		this.disableResponseResponsible = disableResponseResponsible;
	}

	public String getItemSelectedIdString() {
		return itemSelectedIdString;
	}

	public void setItemSelectedIdString(String itemSelectedIdString) {
		this.itemSelectedIdString = itemSelectedIdString;
	}
	
	
	
}
