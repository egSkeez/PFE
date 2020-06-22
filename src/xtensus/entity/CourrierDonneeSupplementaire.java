package xtensus.entity;

// Generated 22 mars 2013 14:16:14 by Hibernate Tools 3.4.0.Beta1




import java.util.Calendar;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name = "courrierdonneesupplementaire")
public class CourrierDonneeSupplementaire extends Entite implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2776529473688045525L;
	private Integer idCourrierDonneeSupplementaire;
	private Courrier idCourrier;
	private String colonne1;
	private String colonne2;
	private String colonne3;
	private String colonne4;
	private String colonne5;
	private String colonne6;
	private String colonne7;
	private String colonne8;
	private String colonne9;
	private String colonne10;
	private String colonne11;
	private String colonne12;
	private String colonne13;
	private Date colonne14;
	private Date colonne15;
	private Date colonne16;
	private Date colonne17;
	private Date colonne18;
	private Date colonne19;
	private Date colonne20;
	private String colonne21;
	private Boolean colonne22;


	public CourrierDonneeSupplementaire() {
	}



	public CourrierDonneeSupplementaire(Courrier idCourrier, String colonne1,
			String colonne2, String colonne3, String colonne4, String colonne5,
			String colonne6, String colonne7, String colonne8, String colonne9,
			String colonne10, String colonne11, String colonne12,
			String colonne13, Date colonne14, Date colonne15,
			Date colonne16, Date colonne17, Date colonne18,
			Date colonne19, Date colonne20, String colonne21,
			boolean colonne22) {
		super();
		this.idCourrier = idCourrier;
		this.colonne1 = colonne1;
		this.colonne2 = colonne2;
		this.colonne3 = colonne3;
		this.colonne4 = colonne4;
		this.colonne5 = colonne5;
		this.colonne6 = colonne6;
		this.colonne7 = colonne7;
		this.colonne8 = colonne8;
		this.colonne9 = colonne9;
		this.colonne10 = colonne10;
		this.colonne11 = colonne11;
		this.colonne12 = colonne12;
		this.colonne13 = colonne13;
		this.colonne14 = colonne14;
		this.colonne15 = colonne15;
		this.colonne16 = colonne16;
		this.colonne17 = colonne17;
		this.colonne18 = colonne18;
		this.colonne19 = colonne19;
		this.colonne20 = colonne20;
		this.colonne21 = colonne21;
		this.colonne22 = colonne22;
		
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idCourrierDonneeSupplementaire", unique = true, nullable = false)
	public Integer getIdCourrierDonneeSupplementaire() {
		return idCourrierDonneeSupplementaire;
	}
	public void setIdCourrierDonneeSupplementaire(
			Integer idCourrierDonneeSupplementaire) {
		this.idCourrierDonneeSupplementaire = idCourrierDonneeSupplementaire;
	}
		
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idCourrier")
	public Courrier getIdCourrier() {
		return idCourrier;
	}

	public void setIdCourrier(Courrier idCourrier) {
		this.idCourrier = idCourrier;
	}

	@Column(name = "colonne1", length = 254)
	public String getColonne1() {
		return colonne1;
	}
	public void setColonne1(String colonne1) {
		System.out.println("colonne1  "+colonne1);
		this.colonne1 = colonne1;
	}
	@Column(name = "colonne2", length = 254)
	public String getColonne2() {
		return colonne2;
	}
	public void setColonne2(String colonne2) {
		this.colonne2 = colonne2;
	}
	@Column(name = "colonne3", length = 254)
	public String getColonne3() {
		return colonne3;
	}
	public void setColonne3(String colonne3) {
		this.colonne3 = colonne3;
	}
	@Column(name = "colonne4", length = 254)
	public String getColonne4() {
		return colonne4;
	}
	public void setColonne4(String colonne4) {
		this.colonne4 = colonne4;
	}
	@Column(name = "colonne5", length = 254)
	public String getColonne5() {
		return colonne5;
	}
	public void setColonne5(String colonne5) {
		this.colonne5 = colonne5;
	}
	@Column(name = "colonne6", length = 254)
	public String getColonne6() {
		return colonne6;
	}
	public void setColonne6(String colonne6) {
		this.colonne6 = colonne6;
	}
	@Column(name = "colonne7", length = 254)
	public String getColonne7() {
		return colonne7;
	}
	public void setColonne7(String colonne7) {
		this.colonne7 = colonne7;
	}
	@Column(name = "colonne8", length = 254)
	public String getColonne8() {
		return colonne8;
	}
	public void setColonne8(String colonne8) {
		this.colonne8 = colonne8;
	}
	@Column(name = "colonne9", length = 254)
	public String getColonne9() {
		return colonne9;
	}
	public void setColonne9(String colonne9) {
		this.colonne9 = colonne9;
	}
	@Column(name = "colonne10", length = 254)
	public String getColonne10() {
		return colonne10;
	}
	public void setColonne10(String colonne10) {
		this.colonne10 = colonne10;
	}

	@Column(name = "colonne11", length = 254)
	public String getColonne11() {
		return colonne11;
	}
	public void setColonne11(String colonne11) {
		this.colonne11 = colonne11;
	}
	@Column(name = "colonne12", length = 254)
	public String getColonne12() {
		return colonne12;
	}
	public void setColonne12(String colonne12) {
		this.colonne12 = colonne12;
	}

	public String getColonne13() {
		return colonne13;
	}

	@Column(name = "colonne13", length = 254)
	public void setColonne13(String colonne13) {
		this.colonne13 = colonne13;
	}
	@Temporal(TemporalType.DATE)
	@Column(name = "colonne14")
	public Date getColonne14() {
		return colonne14;
	}
	public void setColonne14(Date colonne14) {
		this.colonne14 = colonne14;
	}

	@Column(name = "colonne15", length = 254)
	public Date getColonne15() {
		return colonne15;
	}
	public void setColonne15(Date colonne15) {
		this.colonne15 = colonne15;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "colonne16", length = 23)
	public Date getColonne16() {
		return colonne16;
	}

	public void setColonne16(Date colonne16) {
		this.colonne16 = colonne16;
	}


	@Temporal(TemporalType.DATE)
	@Column(name = "colonne17", length = 23)
	public Date getColonne17() {
		return colonne17;
	}
	public void setColonne17(Date colonne17) {
		this.colonne17 = colonne17;
	}


	@Temporal(TemporalType.DATE)
	@Column(name = "colonne18", length = 23)
	public Date getColonne18() {
		return colonne18;
	}
	public void setColonne18(Date colonne18) {
		this.colonne18 = colonne18;
	}


	@Temporal(TemporalType.DATE)
	@Column(name = "colonne19", length = 23)
	public Date getColonne19() {
		return colonne19;
	}
	public void setColonne19(Date colonne19) {
		this.colonne19 = colonne19;
	}


	@Temporal(TemporalType.DATE)
	@Column(name = "colonne20", length = 23)
	public Date getColonne20() {
		return colonne20;
	}
	public void setColonne20(Date colonne20) {
		this.colonne20 = colonne20;
	}


	@Column(name = "colonne21", length = 100)

	public String getColonne21() {
		return colonne21;
	}



	public void setColonne21(String colonne21) {
		this.colonne21 = colonne21;
	}


	@Column(name = "colonne22")
	public Boolean getColonne22() {
		return colonne22;
	}


	
	public void setColonne22(Boolean colonne22) {
		this.colonne22 = colonne22;
	}



	@Override
	public String toString() {
		return "CourrierDonneeSupplementaire [idCourrierDonneeSupplementaire="
				+ idCourrierDonneeSupplementaire + ", idCourrier=" + idCourrier
				+ ", colonne1=" + colonne1 + ", colonne2=" + colonne2
				+ ", colonne3=" + colonne3 + ", colonne4=" + colonne4
				+ ", colonne5=" + colonne5 + ", colonne6=" + colonne6
				+ ", colonne7=" + colonne7 + ", colonne8=" + colonne8
				+ ", colonne9=" + colonne9 + ", colonne10=" + colonne10
				+ ", colonne11=" + colonne11 + ", colonne12=" + colonne12
				+ ", colonne13=" + colonne13 + ", colonne14=" + colonne14
				+ ", colonne15=" + colonne15 + ", colonne16=" + colonne16
				+ ", colonne17=" + colonne17 + ", colonne18=" + colonne18
				+ ", colonne19=" + colonne19 + ", colonne20=" + colonne20
				+ ", colonne21=" + colonne21 + ", colonne22=" + colonne22 + "]";
	}



//	@Column(name = "colonne22", length = 100)
//	public boolean isColonne22() {
//		return colonne22;
//	}
//
//
//
//	public void setColonne22(boolean colonne22) {
//		this.colonne22 = colonne22;
//	}



	




	

}
