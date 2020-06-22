package xtensus.entity;

import java.util.ArrayList;
import java.util.List;

public class NatureModel {
	
	private String libelle;
	private List<Nature> natures=new ArrayList<Nature>();
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public List<Nature> getNatures() {
		return natures;
	}
	public void setNatures(List<Nature> natures) {
		this.natures = natures;
	}
	
	

}
