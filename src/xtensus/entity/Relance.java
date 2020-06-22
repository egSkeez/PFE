package xtensus.entity;

// Generated 20 janv. 2014 15:45:29 by Hibernate Tools 3.4.0.Beta1

import java.util.Calendar;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Relance generated by hbm2java
 */
@Entity
@Table(name = "relance")
public class Relance extends Entite implements java.io.Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3637939986080653407L;
	private Integer relanceId;
	private String relanceLibelle;
	private Date relanceDate;
	private int relanceIdUtilisateur;
	private Set<Transaction> transactions = new HashSet<Transaction>(0);

	public Relance() {
	}

	public Relance(Date relanceDate, int relanceIdUtilisateur) {
		this.relanceDate = relanceDate;
		this.relanceIdUtilisateur = relanceIdUtilisateur;
	}

	public Relance(String relanceLibelle, Date relanceDate,
			int relanceIdUtilisateur, Set<Transaction> transactions) {
		this.relanceLibelle = relanceLibelle;
		this.relanceDate = relanceDate;
		this.relanceIdUtilisateur = relanceIdUtilisateur;
		this.transactions = transactions;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "relanceId", unique = true, nullable = false)
	public Integer getRelanceId() {
		return this.relanceId;
	}

	public void setRelanceId(Integer relanceId) {
		this.relanceId = relanceId;
	}

	@Column(name = "relanceLibelle", length = 45)
	public String getRelanceLibelle() {
		return this.relanceLibelle;
	}

	public void setRelanceLibelle(String relanceLibelle) {
		this.relanceLibelle = relanceLibelle;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "relanceDate", nullable = false, length = 19)
	public Date getRelanceDate() {
		
		
		
		return this.relanceDate;
	}

	public void setRelanceDate(Date relanceDate) {
		
		if(relanceDate!=null){
			Calendar cal = Calendar.getInstance();
			  cal.setTime(relanceDate);
			  cal.set(Calendar.MILLISECOND, 0);
			  relanceDate = new java.sql.Timestamp(cal.getTimeInMillis());}
		this.relanceDate = relanceDate;
	}

	@Column(name = "relanceIdUtilisateur", nullable = false)
	public int getRelanceIdUtilisateur() {
		return this.relanceIdUtilisateur;
	}

	public void setRelanceIdUtilisateur(int relanceIdUtilisateur) {
		this.relanceIdUtilisateur = relanceIdUtilisateur;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "relancetransaction", joinColumns = { @JoinColumn(name = "relanceId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "transactionId", nullable = false, updatable = false) })
	public Set<Transaction> getTransactions() {
		return this.transactions;
	}

	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}

}
