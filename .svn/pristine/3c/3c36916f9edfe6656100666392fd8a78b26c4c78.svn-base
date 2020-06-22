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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import xtensus.entity.Entite;

/**
 
 */
@Entity
@Table(name = "admin")
public class Admin extends Entite implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5503759457553176242L;
	private Integer adminId;
	private String adminNom;
	private String adminCin;
	private String adminLogin;
	private String motDePasse;
	private String adminAdresse;
	private String adminMail;
	private Integer adminTelephone;
	private Boolean adminsuppression;
	private Set<Evenement> evenements = new HashSet<Evenement>(0);

	public Admin() {
	}

	public Admin(String adminNom, String adminCin, String adminLogin,
			String motDePasse, String adminAdresse, String adminMail,
			Integer adminTelephone, Boolean adminsuppression,
			Set<Evenement> evenements) {
		this.adminNom = adminNom;
		this.adminCin = adminCin;
		this.adminLogin = adminLogin;
		this.motDePasse = motDePasse;
		this.adminAdresse = adminAdresse;
		this.adminMail = adminMail;
		this.adminTelephone = adminTelephone;
		this.adminsuppression = adminsuppression;
		this.evenements = evenements;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "adminId", unique = true, nullable = false)
	public Integer getAdminId() {
		return this.adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	@Column(name = "adminNom", length = 20)
	public String getAdminNom() {
		return this.adminNom;
	}

	public void setAdminNom(String adminNom) {
		this.adminNom = adminNom;
	}

	@Column(name = "adminCIN", length = 10)
	public String getAdminCin() {
		return this.adminCin;
	}

	public void setAdminCin(String adminCin) {
		this.adminCin = adminCin;
	}

	@Column(name = "adminLogin", length = 20)
	public String getAdminLogin() {
		return this.adminLogin;
	}

	public void setAdminLogin(String adminLogin) {
		this.adminLogin = adminLogin;
	}

	@Column(name = "motDePasse", length = 20)
	public String getMotDePasse() {
		return this.motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}

	@Column(name = "adminAdresse", length = 50)
	public String getAdminAdresse() {
		return this.adminAdresse;
	}

	public void setAdminAdresse(String adminAdresse) {
		this.adminAdresse = adminAdresse;
	}

	@Column(name = "adminMail", length = 50)
	public String getAdminMail() {
		return this.adminMail;
	}

	public void setAdminMail(String adminMail) {
		this.adminMail = adminMail;
	}

	@Column(name = "adminTelephone")
	public Integer getAdminTelephone() {
		return this.adminTelephone;
	}

	public void setAdminTelephone(Integer adminTelephone) {
		this.adminTelephone = adminTelephone;
	}

	@Column(name = "adminsuppression")
	public Boolean getAdminsuppression() {
		return this.adminsuppression;
	}

	public void setAdminsuppression(Boolean adminsuppression) {
		this.adminsuppression = adminsuppression;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "admin")
	public Set<Evenement> getEvenements() {
		return this.evenements;
	}

	public void setEvenements(Set<Evenement> evenements) {
		this.evenements = evenements;
	}

	public String setAdminNom() {
		// TODO Auto-generated method stub
		return null;
	}

}
