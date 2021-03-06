package xtensus.beans.utils;

public class ComposantDynamique {
	private String name;
	private String type;
	private Object colonne;
	private int idChamps;
	private int index;
	private boolean champOblig;
	private String pattern; 
	private String messageAlerte;
	


	public int getIdChamps() {
		return idChamps;
		
	}

	public void setIdChamps(int idChamps) {
		this.idChamps = idChamps;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return "ComposantDynamique [name=" + name + ", type=" + type
				+ ", colonne=" + colonne + ", idChamps=" + idChamps
				+ ", index=" + index + "]";
	}

	public void setColonne(Object colonne) {
		this.colonne = colonne;
	}

	public Object getColonne() {
		return colonne;
	}

	public void setChampOblig(boolean champOblig) {
		this.champOblig = champOblig;
	}

	public boolean isChampOblig() {
		return champOblig;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getMessageAlerte() {
		return messageAlerte;
	}

	public void setMessageAlerte(String messageAlerte) {
		this.messageAlerte = messageAlerte;
	}

	
}
