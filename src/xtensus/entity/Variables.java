package xtensus.entity;

// Generated 22 mars 2013 14:16:14 by Hibernate Tools 3.4.0.Beta1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Variables generated by hbm2java
 */
@Entity
@Table(name = "variables")
public class Variables extends Entite implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2782174566817103228L;
	private Integer variablesId;
	private String variablesLibelle;
	private String varaiablesValeur;
	private String variablesSymbole;
	private String variablesContenu;

	public Variables() {
	}

	public Variables(String variablesLibelle, String varaiablesValeur,
			String variablesSymbole, String variablesContenu) {
		this.variablesLibelle = variablesLibelle;
		this.varaiablesValeur = varaiablesValeur;
		this.variablesSymbole = variablesSymbole;
		this.variablesContenu = variablesContenu;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "variablesId", unique = true, nullable = false)
	public Integer getVariablesId() {
		return this.variablesId;
	}

	public void setVariablesId(Integer variablesId) {
		this.variablesId = variablesId;
	}

	@Column(name = "variablesLibelle", length = 245)
	public String getVariablesLibelle() {
		return this.variablesLibelle;
	}

	public void setVariablesLibelle(String variablesLibelle) {
		this.variablesLibelle = variablesLibelle;
	}

	@Column(name = "varaiablesValeur", length = 245)
	public String getVaraiablesValeur() {
		return this.varaiablesValeur;
	}

	public void setVaraiablesValeur(String varaiablesValeur) {
		this.varaiablesValeur = varaiablesValeur;
	}

	@Column(name = "variablesSymbole", length = 245)
	public String getVariablesSymbole() {
		return this.variablesSymbole;
	}

	public void setVariablesSymbole(String variablesSymbole) {
		this.variablesSymbole = variablesSymbole;
	}

	@Column(name = "variablesContenu", length = 245)
	public String getVariablesContenu() {
		return this.variablesContenu;
	}

	public void setVariablesContenu(String variablesContenu) {
		this.variablesContenu = variablesContenu;
	}

}
