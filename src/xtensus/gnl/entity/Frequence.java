package xtensus.gnl.entity;

// Generated 11 mai 2013 11:49:40 by Hibernate Tools 3.4.0.Beta1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

import xtensus.entity.Entite;

/**
 * Frequence generated by hbm2java
 */
@Entity
@Table(name = "frequence")
public class Frequence extends Entite implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6203870993980150136L;
	private Integer frequenceId;
	private String frequenceLibelle;
	private String frequenceDescription;
	private Boolean frequenceSuppression;

	public Frequence() {
	}

	public Frequence(String frequenceLibelle, String frequenceDescription,
			Boolean frequenceSuppression) {
		this.frequenceLibelle = frequenceLibelle;
		this.frequenceDescription = frequenceDescription;
		this.frequenceSuppression = frequenceSuppression;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "frequenceId", unique = true, nullable = false)
	public Integer getFrequenceId() {
		return this.frequenceId;
	}

	public void setFrequenceId(Integer frequenceId) {
		this.frequenceId = frequenceId;
	}

	@Column(name = "frequenceLibelle", length = 30)
	public String getFrequenceLibelle() {
		return this.frequenceLibelle;
	}

	public void setFrequenceLibelle(String frequenceLibelle) {
		this.frequenceLibelle = frequenceLibelle;
	}

	@Column(name = "frequenceDescription", length = 65535)
	public String getFrequenceDescription() {
		return this.frequenceDescription;
	}

	public void setFrequenceDescription(String frequenceDescription) {
		this.frequenceDescription = frequenceDescription;
	}

	@Column(name = "frequenceSuppression")
	public Boolean getFrequenceSuppression() {
		return this.frequenceSuppression;
	}

	public void setFrequenceSuppression(Boolean frequenceSuppression) {
		this.frequenceSuppression = frequenceSuppression;
	}

}
