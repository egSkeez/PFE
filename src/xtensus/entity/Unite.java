package xtensus.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import xtensus.ldap.model.Unit;

@Entity
@Table(name = "unite")
public class Unite extends Entite implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int uniteId;
	private String uniteNom;
	private String uniteNomAr;
	private String uniteMailHost;

	private String uniteMailUser;
	private String uniteMailPass;
	private String uniteFaxHost;
	private String uniteFaxUser;
	private String uniteCodeSonede;
	private int uniteType;
	private String uniteAccronyme;
	private String uniteParenteCodeSonede;
private Unit uniteLDAP;
	public Unite(int uniteId, String uniteNom, String uniteNomAr,
			String uniteMailHost, String uniteMailUser, String uniteMailPass,
			String uniteFaxHost, String uniteFaxUser) {
		super();
		this.uniteId = uniteId;
		this.uniteNom = uniteNom;
		this.uniteNomAr = uniteNomAr;
		this.uniteMailHost = uniteMailHost;
		this.uniteMailUser = uniteMailUser;
		this.uniteMailPass = uniteMailPass;
		this.uniteFaxHost = uniteFaxHost;
		this.uniteFaxUser = uniteFaxUser;
	}

	public Unite() {
		super();
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "uniteId", unique = true, nullable = false)
	public int getUniteId() {
		return uniteId;
	}

	@Column(name = "uniteNom")
	public String getUniteNom() {
		return uniteNom;
	}

	@Column(name = "uniteNomAr")
	public String getUniteNomAr() {
		return uniteNomAr;
	}

	@Column(name = "uniteMailHost")
	public String getUniteMailHost() {
		return uniteMailHost;
	}

	@Column(name = "uniteMailUser")
	public String getUniteMailUser() {
		return uniteMailUser;
	}

	@Column(name = "uniteMailPass")
	public String getUniteMailPass() {
		return uniteMailPass;
	}

	@Column(name = "uniteFaxHost")
	public String getUniteFaxHost() {
		return uniteFaxHost;
	}

	@Column(name = "uniteFaxUser")
	public String getUniteFaxUser() {
		return uniteFaxUser;
	}

	public void setUniteId(int uniteId) {
		this.uniteId = uniteId;
	}

	public void setUniteNom(String uniteNom) {
		this.uniteNom = uniteNom;
	}

	public void setUniteNomAr(String uniteNomAr) {
		this.uniteNomAr = uniteNomAr;
	}

	public void setUniteMailHost(String uniteMailHost) {
		this.uniteMailHost = uniteMailHost;
	}

	public void setUniteMailUser(String uniteMailUser) {
		this.uniteMailUser = uniteMailUser;
	}

	public void setUniteMailPass(String uniteMailPass) {
		this.uniteMailPass = uniteMailPass;
	}

	public void setUniteFaxHost(String uniteFaxHost) {
		this.uniteFaxHost = uniteFaxHost;
	}

	public void setUniteFaxUser(String uniteFaxUser) {
		this.uniteFaxUser = uniteFaxUser;
	}

	@Column(name = "uniteCodeSonede")
	public String getUniteCodeSonede() {
		return uniteCodeSonede;
	}

	public void setUniteCodeSonede(String uniteCodeSonede) {
		this.uniteCodeSonede = uniteCodeSonede;
	}

	@Column(name = "uniteType")
	public int getUniteType() {
		return uniteType;
	}

	public void setUniteType(int uniteType) {
		this.uniteType = uniteType;
	}

	@Column(name = "uniteAccronyme")
	public String getUniteAccronyme() {
		return uniteAccronyme;
	}

	public void setUniteAccronyme(String uniteAccronyme) {
		this.uniteAccronyme = uniteAccronyme;
	}

	@Column(name = "uniteParenteCodeSonede")
	public String getUniteParenteCodeSonede() {
		return uniteParenteCodeSonede;
	}

	public void setUniteParenteCodeSonede(String uniteParenteCodeSonede) {
		this.uniteParenteCodeSonede = uniteParenteCodeSonede;
	}
	
	

	@Override
	public String toString() {
		return "Unite [uniteId=" + uniteId + ", uniteNom=" + uniteNom
				+ ", uniteNomAr=" + uniteNomAr + ", uniteMailHost="
				+ uniteMailHost + ", uniteMailUser=" + uniteMailUser
				+ ", uniteMailPass=" + uniteMailPass + ", uniteFaxHost="
				+ uniteFaxHost + ", uniteFaxUser=" + uniteFaxUser
				+ ", uniteCodeSonede=" + uniteCodeSonede + ", uniteType="
				+ uniteType + ", uniteAccronyme=" + uniteAccronyme
				+ ", uniteParenteCodeSonede=" + uniteParenteCodeSonede + "]";
	}

	@Transient
	public Unit getUniteLDAP() {
		return uniteLDAP;
	}

	public void setUniteLDAP(Unit uniteLDAP) {
		this.uniteLDAP = uniteLDAP;
	}

}
