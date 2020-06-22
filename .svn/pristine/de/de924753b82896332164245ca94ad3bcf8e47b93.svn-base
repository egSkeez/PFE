package xtensus.beans.common;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("languageManager")
@Scope(value = "session")
public class LanguageManagerBean {
	
	private String locale = "fr_FR";
	private String headerAlignment = "left";
	private String calendar = "calander.js";
	private String css = "xte-style-gbo.css";
	private String cssmobile = "xte-style-gbo-web.css";
	private String direction = "ltr";
	private String divButtonHeader = "right";
	private boolean statusFormFr = true;
	private boolean statusFormAr = false;
	private String directionToolBar= "bottom-right";
	@Autowired
	private VariableGlobale vb;
	
	private String currentLocation;
	private int test = 0;


	public int getTest() {
		return test;
	}
	
	public void setArabLanguage(){
		System.out.println("Arabe");
		setLocale("ar");
		vb.setLocale("ar");
		setHeaderAlignment("right");
		setCss("xte-style-gbo-AR.css");
		setCalendar("calander-AR.js");
		direction="rtl";
		setDivButtonHeader("left");
		setStatusFormAr(true);
		setStatusFormFr(false);
	}
	
	
	public void setFrenchLangauge(){
		System.out.println("Franéais");
		setLocale("fr_FR");
		vb.setLocale("fr_FR");
		setHeaderAlignment("left");
		setCss("xte-style-gbo.css");
		setCalendar("calander.js");
		direction="ltl";
		setDivButtonHeader("right");
		setStatusFormAr(false);
		setStatusFormFr(true);
		
	}

	public void setTest(int test) {
		this.test = test;
	}


	public String getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = currentLocation;
	}

	public String changeLanguage() {
		return currentLocation;
	}


	// this method extract the language parameter and the country parameter
	public Locale createLocal() {
		if(locale.equals("fr_FR")){
			return  Locale.FRENCH;
		}
		if(locale.equals("ar")){
			for (Locale loc : Locale.getAvailableLocales()) {
				if(loc.getLanguage().toString().equals("ar")){
					return loc;
				}
			}
		}
		return null;

	}

	public void setCss(String css) {
		this.css = css;
	}

	public String getCss() {
		return css;
	}

	public void setHeaderAlignment(String headerAlignment) {
		this.headerAlignment = headerAlignment;
	}

	public String getHeaderAlignment() {
		return headerAlignment;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getLocale() {
		return locale;
	}

	public void setCalendar(String calendar) {
		this.calendar = calendar;
	}

	public String getCalendar() {
		return calendar;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getDirection() {
		return direction;
	}

	public void setDivButtonHeader(String divButtonHeader) {
		this.divButtonHeader = divButtonHeader;
	}

	public String getDivButtonHeader() {
		return divButtonHeader;
	}

	public void setDirectionToolBar(String directionToolBar) {
		this.directionToolBar = directionToolBar;
	}

	public String getDirectionToolBar() {
		return directionToolBar;
	}

	public void setVb(VariableGlobale vb) {
		this.vb = vb;
	}

	public VariableGlobale getVb() {
		return vb;
	}

	public void setStatusFormFr(boolean statusFormFr) {
		this.statusFormFr = statusFormFr;
	}

	public boolean getStatusFormFr() {
		return statusFormFr;
	}

	public void setStatusFormAr(boolean statusFormAr) {
		this.statusFormAr = statusFormAr;
	}

	public boolean getStatusFormAr() {
		return statusFormAr;
	}

	public String getCssmobile() {
		return cssmobile;
	}

	public void setCssmobile(String cssmobile) {
		this.cssmobile = cssmobile;
	}
	

}
