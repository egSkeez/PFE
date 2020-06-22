package xtensus.entity;

// Generated 22 mars 2013 14:16:14 by Hibernate Tools 3.4.0.Beta1


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name = "donneesupplementaire")
public class DonneeSupplementaire extends Entite implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2776529473688045525L;
	private Integer idDonneeSupplementaire;
	private String libelleDonneeSupplementaire;
	private String typeDonneeSupplementaire;

	public DonneeSupplementaire() {
	}


	public DonneeSupplementaire(Integer idDonneeSupplementaire,
			String libelleDonneeSupplementaire, String typeDonneeSupplementaire) {
		super();
		this.idDonneeSupplementaire = idDonneeSupplementaire;
		this.libelleDonneeSupplementaire = libelleDonneeSupplementaire;
		this.typeDonneeSupplementaire=typeDonneeSupplementaire; 
	}


	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idDonneeSupplementaire", unique = true, nullable = false)
	public Integer getIdDonneeSupplementaire() {
		return idDonneeSupplementaire;
	}


	public void setIdDonneeSupplementaire(Integer idDonneeSupplementaire) {
		this.idDonneeSupplementaire = idDonneeSupplementaire;
	}

	@Column(name = "libelleDonneeSupplementaire", length = 254)
	public String getLibelleDonneeSupplementaire() {
		return libelleDonneeSupplementaire;
	}


	public void setLibelleDonneeSupplementaire(String libelleDonneeSupplementaire) {
		this.libelleDonneeSupplementaire = libelleDonneeSupplementaire;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public void setTypeDonneeSupplementaire(String typeDonneeSupplementaire) {
		this.typeDonneeSupplementaire = typeDonneeSupplementaire;
	}

	@Column(name = "typeDonneeSupplementaire", length = 254)
	public String getTypeDonneeSupplementaire() {
		return typeDonneeSupplementaire;
	}


	@Override
	public String toString() {
		return "DonneeSupplementaire [idDonneeSupplementaire="
				+ idDonneeSupplementaire + ", libelleDonneeSupplementaire="
				+ libelleDonneeSupplementaire + ", typeDonneeSupplementaire="
				+ typeDonneeSupplementaire + "]";
	}

	

}
