package xtensus.entity;

// Generated 19 nov. 2013 10:09:21 by Hibernate Tools 3.4.0.Beta1

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Expdestexterne generated by hbm2java
 */
@Entity
@Table(name = "expdestexterne")
public class Expdestexterne extends Entite implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4094493566652886516L;
	private Integer idExpDestExterne;
	private Typeutilisateur typeutilisateur;
	private String expDestExterneNom;
	private String expDestExternePrenom;
	private String expDestExterneTelephone;
	private String expDestExterneMail;
	private String expDestExterneAdresse;
	private String expDestExterneCodePostale;
	private String expDestExterneVille;
	private String expDestExternePays;
	private String expDestExterneFax;
	private Boolean expDestExterneSupprimer;
	private String expDestExterneNomAr;
	private String expDestExternePrenomAr;
	private String expDestExterneAdresseAr;
	private String expDestExterneVilleAr;
	private String expDestExternePaysAr;
	private String expDestExterneGouvernerat;
	private boolean selected;
	private Integer expDestExterneLdapUID;
	private Set<Expdest> expdests = new HashSet<Expdest>(0);
	private Set<Utilisateur> utilisateurs = new HashSet<Utilisateur>(0);
	private Set<Groupecontact> groupecontacts = new HashSet<Groupecontact>(0);
	private Set<Groupecontactmailing> groupecontactmailings = new HashSet<Groupecontactmailing>(0);
	private Set<Pm> pms = new HashSet<Pm>(0);
	private Set<Pp> pps = new HashSet<Pp>(0);

	public Expdestexterne() {
	}

	public Expdestexterne(Typeutilisateur typeutilisateur,
			String expDestExterneNom, String expDestExternePrenom,
			String expDestExterneTelephone, String expDestExterneMail,
			String expDestExterneAdresse, String expDestExterneCodePostale,
			String expDestExterneVille, String expDestExternePays,
			String expDestExterneFax, Boolean expDestExterneSupprimer,
			String expDestExterneNomAr, String expDestExternePrenomAr,
			String expDestExterneAdresseAr, String expDestExterneVilleAr,
			String expDestExternePaysAr, String expDestExterneGouvernerat,
			Set<Expdest> expdests, Set<Utilisateur> utilisateurs,
			Set<Groupecontact> groupecontacts, Set<Pm> pms, Set<Pp> pps) {
		this.typeutilisateur = typeutilisateur;
		this.expDestExterneNom = expDestExterneNom;
		this.expDestExternePrenom = expDestExternePrenom;
		this.expDestExterneTelephone = expDestExterneTelephone;
		this.expDestExterneMail = expDestExterneMail;
		this.expDestExterneAdresse = expDestExterneAdresse;
		this.expDestExterneCodePostale = expDestExterneCodePostale;
		this.expDestExterneVille = expDestExterneVille;
		this.expDestExternePays = expDestExternePays;
		this.expDestExterneFax = expDestExterneFax;
		this.expDestExterneSupprimer = expDestExterneSupprimer;
		this.expDestExterneNomAr = expDestExterneNomAr;
		this.expDestExternePrenomAr = expDestExternePrenomAr;
		this.expDestExterneAdresseAr = expDestExterneAdresseAr;
		this.expDestExterneVilleAr = expDestExterneVilleAr;
		this.expDestExternePaysAr = expDestExternePaysAr;
		this.expDestExterneGouvernerat = expDestExterneGouvernerat;
		this.expdests = expdests;
		this.utilisateurs = utilisateurs;
		this.groupecontacts = groupecontacts;
		this.pms = pms;
		this.pps = pps;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idExpDestExterne", unique = true, nullable = false)
	public Integer getIdExpDestExterne() {
		return this.idExpDestExterne;
	}

	public void setIdExpDestExterne(Integer idExpDestExterne) {
		this.idExpDestExterne = idExpDestExterne;
	}

	@ManyToOne
	@JoinColumn(name = "typeUtilisateurId")
	public Typeutilisateur getTypeutilisateur() {
		return this.typeutilisateur;
	}

	public void setTypeutilisateur(Typeutilisateur typeutilisateur) {
		this.typeutilisateur = typeutilisateur;
	}

	@Column(name = "Exp_Dest_ExterneNom", length = 254)
	public String getExpDestExterneNom() {
		return this.expDestExterneNom;
	}

	public void setExpDestExterneNom(String expDestExterneNom) {
		this.expDestExterneNom = expDestExterneNom;
	}

	@Column(name = "Exp_Dest_ExternePrenom", length = 254)
	public String getExpDestExternePrenom() {
		return this.expDestExternePrenom;
	}

	public void setExpDestExternePrenom(String expDestExternePrenom) {
		this.expDestExternePrenom = expDestExternePrenom;
	}

	@Column(name = "Exp_Dest_ExterneTelephone", length = 50)
	public String getExpDestExterneTelephone() {
		return this.expDestExterneTelephone;
	}

	public void setExpDestExterneTelephone(String expDestExterneTelephone) {
		this.expDestExterneTelephone = expDestExterneTelephone;
	}

	@Column(name = "Exp_Dest_ExterneMail", length = 254)
	public String getExpDestExterneMail() {
		return this.expDestExterneMail;
	}

	public void setExpDestExterneMail(String expDestExterneMail) {
		this.expDestExterneMail = expDestExterneMail;
	}

	@Column(name = "Exp_Dest_ExterneAdresse", length = 254)
	public String getExpDestExterneAdresse() {
		return this.expDestExterneAdresse;
	}

	public void setExpDestExterneAdresse(String expDestExterneAdresse) {
		this.expDestExterneAdresse = expDestExterneAdresse;
	}

	@Column(name = "Exp_Dest_ExterneCodePostale", length = 20)
	public String getExpDestExterneCodePostale() {
		return this.expDestExterneCodePostale;
	}

	public void setExpDestExterneCodePostale(String expDestExterneCodePostale) {
		this.expDestExterneCodePostale = expDestExterneCodePostale;
	}

	@Column(name = "Exp_Dest_ExterneVille", length = 254)
	public String getExpDestExterneVille() {
		return this.expDestExterneVille;
	}

	public void setExpDestExterneVille(String expDestExterneVille) {
		this.expDestExterneVille = expDestExterneVille;
	}

	@Column(name = "Exp_Dest_ExternePays", length = 254)
	public String getExpDestExternePays() {
		return this.expDestExternePays;
	}

	public void setExpDestExternePays(String expDestExternePays) {
		this.expDestExternePays = expDestExternePays;
	}

	@Column(name = "Exp_Dest_ExterneFax", length = 50)
	public String getExpDestExterneFax() {
		return this.expDestExterneFax;
	}

	public void setExpDestExterneFax(String expDestExterneFax) {
		this.expDestExterneFax = expDestExterneFax;
	}

	@Column(name = "Exp_Dest_ExterneSupprimer")
	public Boolean getExpDestExterneSupprimer() {
		return this.expDestExterneSupprimer;
	}

	public void setExpDestExterneSupprimer(Boolean expDestExterneSupprimer) {
		this.expDestExterneSupprimer = expDestExterneSupprimer;
	}

	@Column(name = "Exp_Dest_ExterneNom_AR", length = 254)
	public String getExpDestExterneNomAr() {
		return this.expDestExterneNomAr;
	}

	public void setExpDestExterneNomAr(String expDestExterneNomAr) {
		this.expDestExterneNomAr = expDestExterneNomAr;
	}

	@Column(name = "Exp_Dest_ExternePrenom_AR", length = 254)
	public String getExpDestExternePrenomAr() {
		return this.expDestExternePrenomAr;
	}

	public void setExpDestExternePrenomAr(String expDestExternePrenomAr) {
		this.expDestExternePrenomAr = expDestExternePrenomAr;
	}

	@Column(name = "Exp_Dest_ExterneAdresse_AR", length = 254)
	public String getExpDestExterneAdresseAr() {
		return this.expDestExterneAdresseAr;
	}

	public void setExpDestExterneAdresseAr(String expDestExterneAdresseAr) {
		this.expDestExterneAdresseAr = expDestExterneAdresseAr;
	}

	@Column(name = "Exp_Dest_ExterneVille_AR", length = 254)
	public String getExpDestExterneVilleAr() {
		return this.expDestExterneVilleAr;
	}

	public void setExpDestExterneVilleAr(String expDestExterneVilleAr) {
		this.expDestExterneVilleAr = expDestExterneVilleAr;
	}

	@Column(name = "Exp_Dest_ExternePays_AR", length = 254)
	public String getExpDestExternePaysAr() {
		return this.expDestExternePaysAr;
	}

	public void setExpDestExternePaysAr(String expDestExternePaysAr) {
		this.expDestExternePaysAr = expDestExternePaysAr;
	}

	@Column(name = "Exp_Dest_ExterneGouvernerat", length = 254)
	public String getExpDestExterneGouvernerat() {
		return this.expDestExterneGouvernerat;
	}

	public void setExpDestExterneGouvernerat(String expDestExterneGouvernerat) {
		this.expDestExterneGouvernerat = expDestExterneGouvernerat;
	}

	@OneToMany( mappedBy = "expdestexterne")
	public Set<Expdest> getExpdests() {
		return this.expdests;
	}

	public void setExpdests(Set<Expdest> expdests) {
		this.expdests = expdests;
	}

	@OneToMany(mappedBy = "expdestexterne")
	public Set<Utilisateur> getUtilisateurs() {
		return this.utilisateurs;
	}

	public void setUtilisateurs(Set<Utilisateur> utilisateurs) {
		this.utilisateurs = utilisateurs;
	}

	/*@ManyToMany(fetch = FetchType.LAZY, mappedBy = "expdestexternes")
	public Set<Groupecontact> getGroupecontacts() {
		return this.groupecontacts;
	}*/

	public void setGroupecontacts(Set<Groupecontact> groupecontacts) {
		this.groupecontacts = groupecontacts;
	}
	
	@OneToMany( mappedBy = "expdestexterne")
	public Set<Groupecontactmailing> getGroupecontactmailings() {
		return this.groupecontactmailings;
	}

	public void setGroupecontactmailings(
			Set<Groupecontactmailing> groupecontactmailings) {
		this.groupecontactmailings = groupecontactmailings;
	}

	@OneToMany( mappedBy = "expdestexterne")
	public Set<Pm> getPms() {
		return this.pms;
	}

	public void setPms(Set<Pm> pms) {
		this.pms = pms;
	}

	@OneToMany(mappedBy = "expdestexterne")
	public Set<Pp> getPps() {
		return this.pps;
	}

	public void setPps(Set<Pp> pps) {
		this.pps = pps;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Transient
	public boolean isSelected() {
		return selected;
	}
	@Column(name = "Exp_Dest_ExterneLdap_UID")
	public Integer getExpDestExterneLdapUID() {
		return expDestExterneLdapUID;
	}

	public void setExpDestExterneLdapUID(Integer expDestExterneLdapUID) {
		this.expDestExterneLdapUID = expDestExterneLdapUID;
	}

}
