package xtensus.entity;

// Generated 22 mars 2013 14:16:14 by Hibernate Tools 3.4.0.Beta1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Etat generated by hbm2java
 */
@Entity
@Table(name = "etat")
public class Etat extends Entite implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5099642744033110687L;
	private Integer etatId;
	private String etatLibelle;
	private String etatLibelleAr;
	private Integer etatOrdre;
	private Integer etatCategorie;
	private String etatDescription;
	private String etatDescriptionAr;
	private Set<Transaction> transactions = new HashSet<Transaction>(0);

	public Etat() {
	}

	public Etat(String etatLibelle,String etatLibelleAr, Integer etatOrdre, Integer etatCategorie, String etatDescription,
			Set<Transaction> transactions) {
		this.etatLibelle = etatLibelle;
		this.etatOrdre = etatOrdre;
		this.etatCategorie = etatCategorie;
		this.etatDescription = etatDescription;
		this.transactions = transactions;
		this.etatLibelleAr = etatLibelleAr;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "etatId", unique = true, nullable = false)
	public Integer getEtatId() {
		return this.etatId;
	}

	public void setEtatId(Integer etatId) {
		this.etatId = etatId;
	}

	@Column(name = "etatLibelle", length = 254)
	public String getEtatLibelle() {
		return this.etatLibelle;
	}

	public void setEtatLibelle(String etatLibelle) {
		this.etatLibelle = etatLibelle;
	}

	@Column(name = "etatOrdre")
	public Integer getEtatOrdre() {
		return this.etatOrdre;
	}

	public void setEtatOrdre(Integer etatOrdre) {
		this.etatOrdre = etatOrdre;
	}
	
	@Column(name = "etatCategorie")
	public Integer getEtatCategorie() {
		return this.etatCategorie;
	}

	public void setEtatCategorie(Integer etatCategorie) {
		this.etatCategorie = etatCategorie;
	}

	@Column(name = "etatDescription", length = 65535)
	public String getEtatDescription() {
		return this.etatDescription;
	}

	public void setEtatDescription(String etatDescription) {
		this.etatDescription = etatDescription;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "etat")
	public Set<Transaction> getTransactions() {
		return this.transactions;
	}

	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}
	@Column(name = "etatLibelleAr", length = 254)
	public String getEtatLibelleAr() {
		return etatLibelleAr;
	}

	public void setEtatLibelleAr(String etatLibelleAr) {
		this.etatLibelleAr = etatLibelleAr;
	}
	@Column(name = "etatDescriptionAr", length = 65535)
	public String getEtatDescriptionAr() {
		return etatDescriptionAr;
	}

	public void setEtatDescriptionAr(String etatDescriptionAr) {
		this.etatDescriptionAr = etatDescriptionAr;
	}

}
