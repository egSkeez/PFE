package xtensus.entity;

// Generated 22 mars 2013 14:16:14 by Hibernate Tools 3.4.0.Beta1


import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name = "donneesupplementairenature")
public class DonneeSupplementaireNature extends Entite implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int donneesupplementairenatureId;
	private DonneeSupplementaire donneeSupplementaire;
	private Nature nature;
	private Transmission transmission;
	private String libelleDonnee;
	private String libelleDonnee_AR;
	private int ordre;
	private boolean obligatoire;
	private String pattern;
	private String messageAlerte;
		
	public DonneeSupplementaireNature() {

	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column (name = "donneesupplementairenatureId", length = 10, unique = true, nullable = false)
	public int getDonneesupplementairenatureId() {
		return donneesupplementairenatureId;
	}

	public void setDonneesupplementairenatureId(int donneesupplementairenatureId) {
		this.donneesupplementairenatureId = donneesupplementairenatureId;
	}	

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idDonneeSupplementaire", insertable = false, updatable = false)
	public DonneeSupplementaire getDonneeSupplementaire() {
		return donneeSupplementaire;
	}
	public void setDonneeSupplementaire(DonneeSupplementaire donneeSupplementaire) {
		this.donneeSupplementaire = donneeSupplementaire;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idNature",insertable = false, updatable = false)
	public Nature getNature() {
		return nature;
	}
	public void setNature(Nature nature) {
		this.nature = nature;
	}

	

	@Column(name = "libelleDonnee", length = 254)
	public String getLibelleDonnee() {
		return libelleDonnee;
	}


	public void setLibelleDonnee(String libelleDonnee) {
		this.libelleDonnee = libelleDonnee;
	}

	@Column(name = "libelleDonnee_AR", length = 254)
	public String getLibelleDonnee_AR() {
		return libelleDonnee_AR;
	}


	public void setLibelleDonnee_AR(String libelleDonnee_AR) {
		this.libelleDonnee_AR = libelleDonnee_AR;
	}

	
	@Column(name = "ordre")
	public int getOrdre() {
		return ordre;
	}

	public void setOrdre(int ordre) {
		this.ordre = ordre;
	}
	@Column(name = "obligatoire")
	public boolean isObligatoire() {
		//System.out.println("obligatoire :"+obligatoire);
		return obligatoire;
	}

	public void setObligatoire(boolean obligatoire) {
		//System.out.println("obligatoire :"+obligatoire);
		this.obligatoire = obligatoire;
	}
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idTrans", insertable = false, updatable = false)
	public Transmission getTransmission() {
		return transmission;
	}

	public void setTransmission(Transmission transmission) {
		this.transmission = transmission;
	}
	
	@Column(name = "pattern")
	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	
	@Column(name = "messageAlerte")
	public String getMessageAlerte() {
		return messageAlerte;
	}

	public void setMessageAlerte(String messageAlerte) {
		this.messageAlerte = messageAlerte;
	}

	@Override
	public String toString() {
		return "DonneeSupplementaireNature [donneesupplementairenatureId=" + donneesupplementairenatureId
				+ ", donneeSupplementaire=" + donneeSupplementaire
				+ ", nature=" + nature + ", libelleDonnee=" + libelleDonnee
				+ ", libelleDonnee_AR=" + libelleDonnee_AR + ", ordre=" + ordre
				+ ", obligatoire=" + obligatoire + "]";
	}


}
