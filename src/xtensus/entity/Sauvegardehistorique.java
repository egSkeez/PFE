package xtensus.entity;

// Generated 12 nov. 2013 11:39:33 by Hibernate Tools 3.4.0.Beta1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Sauvegardehistorique generated by hbm2java
 */
@Entity
@Table(name = "sauvegardehistorique")
public class Sauvegardehistorique extends Entite implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -692767932159995749L;
	private Integer idsauvegardeHistorique;
	private Date sauvegardeHistoriqueDate;
	private String sauvegardeHistoriqueNomFichier;

	public Sauvegardehistorique() {
	}

	public Sauvegardehistorique(Date sauvegardeHistoriqueDate,
			String sauvegardeHistoriqueNomFichier) {
		this.sauvegardeHistoriqueDate = sauvegardeHistoriqueDate;
		this.sauvegardeHistoriqueNomFichier = sauvegardeHistoriqueNomFichier;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idsauvegardeHistorique", unique = true, nullable = false)
	public Integer getIdsauvegardeHistorique() {
		return this.idsauvegardeHistorique;
	}

	public void setIdsauvegardeHistorique(Integer idsauvegardeHistorique) {
		this.idsauvegardeHistorique = idsauvegardeHistorique;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "sauvegardeHistoriqueDate", length = 10)
	public Date getSauvegardeHistoriqueDate() {
		return this.sauvegardeHistoriqueDate;
	}

	public void setSauvegardeHistoriqueDate(Date sauvegardeHistoriqueDate) {
		this.sauvegardeHistoriqueDate = sauvegardeHistoriqueDate;
	}

	@Column(name = "sauvegardeHistoriqueNomFichier", length = 65535)
	public String getSauvegardeHistoriqueNomFichier() {
		return this.sauvegardeHistoriqueNomFichier;
	}

	public void setSauvegardeHistoriqueNomFichier(
			String sauvegardeHistoriqueNomFichier) {
		this.sauvegardeHistoriqueNomFichier = sauvegardeHistoriqueNomFichier;
	}

}
