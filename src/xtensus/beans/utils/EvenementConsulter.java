package xtensus.beans.utils;

import xtensus.gnl.entity.Evenement;
import xtensus.gnl.entity.TypeEvenement;

public class EvenementConsulter {
	
	private TypeEvenement typeEvent;
	private Evenement evenement;
	private String type;
	
	public void setTypeEvent(TypeEvenement typeEvent) {
		this.typeEvent = typeEvent;
	}
	public TypeEvenement getTypeEvent() {
		return typeEvent;
	}
	public void setEvenement(Evenement evenement) {
		this.evenement = evenement;
	}
	public Evenement getEvenement() {
		return evenement;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}

	
}
