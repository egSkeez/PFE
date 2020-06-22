package xtensus.beans.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CalendarBean implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;
	private Locale locale;
	private List<String> listMois;
	
	public CalendarBean() {
		locale = Locale.FRANCE;

		listMois = new ArrayList<String>();
		listMois.add("Janvier");
		listMois.add("Fevrier");
		listMois.add("Mars");
		listMois.add("Avril");
		listMois.add("Mai");
		listMois.add("Juin");
		listMois.add("Juillet");
		listMois.add("Aout");
		listMois.add("Septembre");
		listMois.add("Octobre");
		listMois.add("Novembre");
		listMois.add("Decembre");
	
	}
	
	
	
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public void setListMois(List<String> listMois) {
		this.listMois = listMois;
	}

	public List<String> getListMois() {

		return listMois;
	}

}