package xtensus.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "employe")
public class Employe extends Entite implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int employerId;
	private String employerNom;
	private String employerNomAr;
	private String employerCodeSonede;
	private String uniteCodeSonede; 
	private String employeMatricule;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "employeId", unique = true, nullable = false)
	public int getEmployerId() {
		return employerId;
	}

	public void setEmployerId(int employerId) {
		this.employerId = employerId;
	}

	@Column(name = "employeCodeSonede")
	public String getEmployerCodeSonede() {
		return employerCodeSonede;
	}

	@Column(name = "employeNom")
	public String getEmployerNom() {
		return employerNom;
	}

	public void setEmployerNom(String employerNom) {
		this.employerNom = employerNom;
	}

	@Column(name = "employeNomAr")
	public String getEmployerNomAr() {
		return employerNomAr;
	}

	public void setEmployerNomAr(String employerNomAr) {
		this.employerNomAr = employerNomAr;
	}

	public void setEmployerCodeSonede(String employerCodeSonede) {
		this.employerCodeSonede = employerCodeSonede;
	}

	public Employe() {
		super();
	}

	@Column(name = "uniteCodeSonede")
	public String getUniteCodeSonede() {
		return uniteCodeSonede;
	}

	public void setUniteCodeSonede(String uniteCodeSonede) {
		this.uniteCodeSonede = uniteCodeSonede;
	}

	public void setEmployeMatricule(String employeMatricule) {
		this.employeMatricule = employeMatricule;
	}
	
	@Column(name = "employeMatricule")
	public String getEmployeMatricule() {
		return employeMatricule;
	}

	@Override
	public String toString() {
		return "Employe [employerId=" + employerId + ", employerNom="
				+ employerNom + ", employerNomAr=" + employerNomAr
				+ ", employerCodeSonede=" + employerCodeSonede
				+ ", uniteCodeSonede=" + uniteCodeSonede
				+ ", employeMatricule=" + employeMatricule + "]";
	}

}
