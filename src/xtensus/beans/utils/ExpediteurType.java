package xtensus.beans.utils;

import xtensus.entity.Entite;
import xtensus.entity.Expdestexterne;
import xtensus.entity.Pm;
import xtensus.entity.Pp;

public class ExpediteurType extends Entite {
	private String type;
	private Expdestexterne expdestexterne;
	private String societe;
	private Pp pp;
	private Pm pm;
	private boolean sentByEmail;
	private boolean sentByFax;
	private boolean sentByPorter;
	private boolean sentByPostal;

	public String getSociete() {
		return societe;
	}

	public void setSociete(String societe) {
		this.societe = societe;
	}

	public Expdestexterne getExpdestexterne() {
		return expdestexterne;
	}

	public void setExpdestexterne(Expdestexterne expdestexterne) {
		this.expdestexterne = expdestexterne;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setPp(Pp pp) {
		this.pp = pp;
	}

	public Pp getPp() {
		return pp;
	}

	public void setPm(Pm pm) {
		this.pm = pm;
	}

	public Pm getPm() {
		return pm;
	}

	public void setSentByEmail(boolean sentByEmail) {
		this.sentByEmail = sentByEmail;
	}

	public boolean isSentByEmail() {
		return sentByEmail;
	}

	public void setSentByFax(boolean sentByFax) {
		this.sentByFax = sentByFax;
	}

	public boolean isSentByFax() {
		return sentByFax;
	}

	public void setSentByPorter(boolean sentByPorter) {
		this.sentByPorter = sentByPorter;
	}

	public boolean isSentByPorter() {
		return sentByPorter;
	}

	public void setSentByPostal(boolean sentByPostal) {
		this.sentByPostal = sentByPostal;
	}

	public boolean isSentByPostal() {
		return sentByPostal;
	}

}
