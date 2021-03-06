package xtensus.gnl.entity;

// Generated 11 mai 2013 11:49:40 by Hibernate Tools 3.4.0.Beta1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import xtensus.entity.Entite;

/**
 * Evenement generated by hbm2java
 */
@Entity
@Table(name = "evenement")
public class Evenement extends Entite implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4973858472988913535L;
	private Integer evenementId;
	private TypeEvenement typeEvenement;
	private Admin admin;
	private String evenementLibelle;
	private String evenementDescription;
	private Boolean evenementSuppression;
	private String evenementNomVariable;
	private Set<Notification> notifications = new HashSet<Notification>(0);
	private Set<Xtelog> xtelogs = new HashSet<Xtelog>(0);
	private Set<Groupegnl> groupegnls = new HashSet<Groupegnl>(0);
	private Set<Parametre> parametres = new HashSet<Parametre>(0);

	public Evenement() {
	}

	public Evenement(TypeEvenement typeEvenement, Admin admin,
			String evenementLibelle, String evenementDescription,
			Boolean evenementSuppression, String evenementNomVariable, Set<Notification> notifications,
			Set<Xtelog> xtelogs, Set<Groupegnl> groupegnls,
			Set<Parametre> parametres) {
		this.typeEvenement = typeEvenement;
		this.admin = admin;
		this.evenementLibelle = evenementLibelle;
		this.evenementDescription = evenementDescription;
		this.evenementSuppression = evenementSuppression;
		this.evenementNomVariable = evenementNomVariable;
		this.notifications = notifications;
		this.xtelogs = xtelogs;
		this.groupegnls = groupegnls;
		this.parametres = parametres;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "evenementId", unique = true, nullable = false)
	public Integer getEvenementId() {
		return this.evenementId;
	}

	public void setEvenementId(Integer evenementId) {
		this.evenementId = evenementId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "adminId")
	public Admin getAdmin() {
		return this.admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	@Column(name = "evenementLibelle", length = 256)
	public String getEvenementLibelle() {
		return this.evenementLibelle;
	}

	public void setEvenementLibelle(String evenementLibelle) {
		this.evenementLibelle = evenementLibelle;
	}

	@Column(name = "evenementDescription", length = 65535)
	public String getEvenementDescription() {
		return this.evenementDescription;
	}

	public void setEvenementDescription(String evenementDescription) {
		this.evenementDescription = evenementDescription;
	}

	@Column(name = "evenementSuppression")
	public Boolean getEvenementSuppression() {
		return this.evenementSuppression;
	}

	public void setEvenementSuppression(Boolean evenementSuppression) {
		this.evenementSuppression = evenementSuppression;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "evenement")
	public Set<Notification> getNotifications() {
		return this.notifications;
	}

	public void setNotifications(Set<Notification> notifications) {
		this.notifications = notifications;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "eventlog", catalog = "xtensusgnl", joinColumns = { @JoinColumn(name = "evenementId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "logId", nullable = false, updatable = false) })
	public Set<Xtelog> getXtelogs() {
		return this.xtelogs;
	}

	public void setXtelogs(Set<Xtelog> xtelogs) {
		this.xtelogs = xtelogs;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "eventgroupe", catalog = "xtensusgnl", joinColumns = { @JoinColumn(name = "evenementId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "groupeId", nullable = false, updatable = false) })
	public Set<Groupegnl> getGroupes() {
		return this.groupegnls;
	}

	public void setGroupes(Set<Groupegnl> groupegnls) {
		this.groupegnls = groupegnls;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "evenement")
	public Set<Parametre> getParametres() {
		return this.parametres;
	}

	public void setParametres(Set<Parametre> parametres) {
		this.parametres = parametres;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "typeEvenementId", nullable = false)
	public TypeEvenement getTypeEvenement() {
		return typeEvenement;
	}

	public void setTypeEvenement(TypeEvenement typeEvenement) {
		this.typeEvenement = typeEvenement;
	}

	@Column(name = "evenementNomVariable", length = 45)
	public String getEvenementNomVariable() {
		return evenementNomVariable;
	}
	
	public void setEvenementNomVariable(String evenementNomVariable) {
		this.evenementNomVariable = evenementNomVariable;
	}
}
