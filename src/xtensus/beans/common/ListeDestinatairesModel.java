package xtensus.beans.common;

import java.util.List;

public class ListeDestinatairesModel {
	private int destinataireId;
	private String destinataireName;
	private List<String> listeAnnotations;
	private List<String> selectedItemsAnnotation;
	private String annotations;
	private String destinataireType;
	//AH : Si on a choisi le bouton Radio autre
	private String chooseAnnotationType;
	private String otherAnnotation;
	private boolean affichageAnnotation;
	private String display;
	private String displayListe;
	private int transactionId;
	
	public String getDisplayListe() {
		return displayListe;
	}

	public void setDisplayListe(String displayListe) {
		this.displayListe = displayListe;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public List<String> getListeAnnotations() {
		return listeAnnotations;
	}

	public void setListeAnnotations(List<String> listeAnnotations) {
		this.listeAnnotations = listeAnnotations;
	}

	public int getDestinataireId() {
		return destinataireId;
	}

	public void setDestinataireId(int destinataireId) {
		this.destinataireId = destinataireId;
	}

	public String getDestinataireName() {
		return destinataireName;
	}

	public void setDestinataireName(String destinataireName) {
		this.destinataireName = destinataireName;
	}

	public String getAnnotations() {
		return annotations;
	}

	public void setAnnotations(String annotations) {
		this.annotations = annotations;
	}

	public String getChooseAnnotationType() {
		return chooseAnnotationType;
	}

	public void setChooseAnnotationType(String chooseAnnotationType) {
		this.chooseAnnotationType = chooseAnnotationType;
	}

	public String getOtherAnnotation() {
		return otherAnnotation;
	}

	public void setOtherAnnotation(String otherAnnotation) {
		this.otherAnnotation = otherAnnotation;
	}
	
	

	public boolean isAffichageAnnotation() {
		return affichageAnnotation;
	}

	public void setAffichageAnnotation(boolean affichageAnnotation) {
		this.affichageAnnotation = affichageAnnotation;
	}

	public String getDestinataireType() {
		return destinataireType;
	}

	public void setDestinataireType(String destinataireType) {
		this.destinataireType = destinataireType;
	}

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}
	

	public List<String> getSelectedItemsAnnotation() {
		return selectedItemsAnnotation;
	}

	public void setSelectedItemsAnnotation(List<String> selectedItemsAnnotation) {
		this.selectedItemsAnnotation = selectedItemsAnnotation;
	}



	

}
