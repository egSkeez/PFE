package xtensus.entity;

// Generated 4 oct. 2013 12:20:32 by Hibernate Tools 3.4.0.Beta1

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Courrier generated by hbm2java
 */
@Entity
@Table(name = "courrier")
public class Courrier extends Entite implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5109888493616261512L;
	private Integer idCourrier;
	private Transmission transmission;
	private Nature nature;
	private Classement_archivage_niveau_01 classement_archivage_niveau_01;
	private Urgence urgence;
	private Confidentialite confidentialite;
	private String courrierNecessiteReponse;
	private Date courrierDateReponse;
	private String courrierReferenceCorrespondant;
	private String courrierCommentaire;
	private String courrierObjet;
	private Date courrierDateReception;
	private Date courrierDateReceptionReelle;
	private String courrierCircuit;
	private String keywords;
	private Date courrierDateSysteme;
	private Date courrierDateReponseSysteme;
	private Boolean courrierSupprime;
	private Integer courrierDelegueId;
	private Integer courrierSupprimeDelegueId;
	private Date courrierSupprimeDate;
	private String courrierCommentaireAr;
	private String courrierObjetAr;
	private Integer courrierEtatActuelleWorkflow;
	private Integer courrierflagArchive;
	private Date courrierDateClassement;
	private Date courrierDateArchivage;
	private String courrierType;
	private int courrierTypeOrdre;
	private Integer courrierOldNum;
	private Integer courrierOldDateOper;
	private Integer courrierOldIntt;
	private int courrierEtatCloture;
	private String courrierCopyTransfere;
	private Integer courrierDateReceptionMois;
	private String courrierFlagInterne;
	private Boolean courrierNecessitePassageParBO;

	private Set<Document> documents = new HashSet<Document>(0);
	private Set<Suivitransmissioncourrier> suivitransmissioncourriers = new HashSet<Suivitransmissioncourrier>(
			0);
	private Set<Proprietes> proprieteses = new HashSet<Proprietes>(0);
	private Set<Dossier> dossiers = new HashSet<Dossier>(0);
	private Set<Lienscourriers> lienscourrierses = new HashSet<Lienscourriers>(
			0);
	private Set<Commentaire> commentaires = new HashSet<Commentaire>(0);
	private Set<Workflow> workflows = new HashSet<Workflow>(0);
	private Set<Lienscourriers> lienscourrierses_1 = new HashSet<Lienscourriers>(
			0);
   // KHA ajoutee 01-03-2019
	private Boolean courrierAvecDocumentPhysique ;
	private AoConsultation aoConsultationId;
	private String courrierFormat;
	private Date courrierDatePointage;
	private Courrier idcourrierFK;
	private String courrierRefOriginale;
	private String courrierTypeTransport;
	
	
	public Courrier() {
	}

	public Courrier(Transmission transmission, Nature nature, Urgence urgence,
			Confidentialite confidentialite) {
		this.transmission = transmission;
		this.nature = nature;
		this.urgence = urgence;
		this.confidentialite = confidentialite;
	}

	public Courrier(Integer idCourrier, Transmission transmission,
			Nature nature, Classement_archivage_niveau_01 etages, Urgence urgence,
			Confidentialite confidentialite, String courrierNecessiteReponse,
			Date courrierDateReponse, String courrierReferenceCorrespondant,
			String courrierCommentaire, String courrierObjet,
			Date courrierDateReception, Date courrierDateReceptionReelle,
			String courrierCircuit, String keywords, Date courrierDateSysteme,
			Date courrierDateReponseSysteme, Boolean courrierSupprime,
			Integer courrierDelegueId, Integer courrierSupprimeDelegueId,
			Date courrierSupprimeDate, String courrierCommentaireAr,
			String courrierObjetAr, Integer courrierEtatActuelleWorkflow,
			Integer courrierflagArchive, Date courrierDateClassement,
			Date courrierDateArchivage, String courrierType,
			int courrierTypeOrdre, Set<Document> documents,
			Set<Suivitransmissioncourrier> suivitransmissioncourriers,
			Set<Proprietes> proprieteses, Set<Dossier> dossiers,
			Set<Lienscourriers> lienscourrierses,Set<Commentaire> commentaires, 
			Set<Workflow> workflows,Set<Lienscourriers> lienscourrierses_1,String courrierFormat,
			Date courrierDatePointage,Courrier idcourrierFK) {
		super();
		this.idCourrier = idCourrier;
		this.transmission = transmission;
		this.nature = nature;
		this.classement_archivage_niveau_01 = etages;
		this.urgence = urgence;
		this.confidentialite = confidentialite;
		this.courrierNecessiteReponse = courrierNecessiteReponse;
		this.courrierDateReponse = courrierDateReponse;
		this.courrierReferenceCorrespondant = courrierReferenceCorrespondant;
		this.courrierCommentaire = courrierCommentaire;
		this.courrierObjet = courrierObjet;
		this.courrierDateReception = courrierDateReception;
		this.courrierDateReceptionReelle = courrierDateReceptionReelle;
		this.courrierCircuit = courrierCircuit;
		this.keywords = keywords;
		this.courrierDateSysteme = courrierDateSysteme;
		this.courrierDateReponseSysteme = courrierDateReponseSysteme;
		this.courrierSupprime = courrierSupprime;
		this.courrierDelegueId = courrierDelegueId;
		this.courrierSupprimeDelegueId = courrierSupprimeDelegueId;
		this.courrierSupprimeDate = courrierSupprimeDate;
		this.courrierCommentaireAr = courrierCommentaireAr;
		this.courrierObjetAr = courrierObjetAr;
		this.courrierEtatActuelleWorkflow = courrierEtatActuelleWorkflow;
		this.courrierflagArchive = courrierflagArchive;
		this.courrierDateClassement = courrierDateClassement;
		this.courrierDateArchivage = courrierDateArchivage;
		this.courrierType = courrierType;
		this.courrierTypeOrdre = courrierTypeOrdre;
		this.documents = documents;
		this.suivitransmissioncourriers = suivitransmissioncourriers;
		this.proprieteses = proprieteses;
		this.dossiers = dossiers;
		this.lienscourrierses = lienscourrierses;
		this.commentaires = commentaires;
		this.workflows = workflows;
		this.lienscourrierses_1 = lienscourrierses_1;
		this.courrierFormat= courrierFormat;
		this.courrierDatePointage= courrierDatePointage;
		this.idcourrierFK= idcourrierFK;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idCourrier", unique = true)
	public Integer getIdCourrier() {
		return this.idCourrier;
	}

	public void setIdCourrier(Integer idCourrier) {
		this.idCourrier = idCourrier;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idTransmission" )
	public Transmission getTransmission() {
		return this.transmission;
	}

	public void setTransmission(Transmission transmission) {
		this.transmission = transmission;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idNature")
	public Nature getNature() {
		return this.nature;
	}

	public void setNature(Nature nature) {
		this.nature = nature;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "classement_archivage_niveau_01_Id")
	public Classement_archivage_niveau_01 getClassement_archivage_niveau_01() {
		return this.classement_archivage_niveau_01;
	}

	public void setClassement_archivage_niveau_01(Classement_archivage_niveau_01 etages) {
		this.classement_archivage_niveau_01 = etages;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idUrgence")
	public Urgence getUrgence() {
		return this.urgence;
	}

	public void setUrgence(Urgence urgence) {
		this.urgence = urgence;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idConfidentialite")
	public Confidentialite getConfidentialite() {
		return this.confidentialite;
	}

	public void setConfidentialite(Confidentialite confidentialite) {
		this.confidentialite = confidentialite;
	}

	@Column(name = "courrierNecessiteReponse", length = 100)
	public String getCourrierNecessiteReponse() {
		return this.courrierNecessiteReponse;
	}

	public void setCourrierNecessiteReponse(String courrierNecessiteReponse) {
		this.courrierNecessiteReponse = courrierNecessiteReponse;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "courrierDateReponse", length = 19)
	public Date getCourrierDateReponse() {
		return this.courrierDateReponse;
	}

	public void setCourrierDateReponse(Date courrierDateReponse) {
		this.courrierDateReponse = courrierDateReponse;
	}

	@Column(name = "courrierReferenceCorrespondant", length = 100)
	public String getCourrierReferenceCorrespondant() {
		return this.courrierReferenceCorrespondant;
	}

	public void setCourrierReferenceCorrespondant(
			String courrierReferenceCorrespondant) {
		this.courrierReferenceCorrespondant = courrierReferenceCorrespondant;
	}

	@Column(name = "courrierCommentaire", length = 16277215)
	public String getCourrierCommentaire() {
		return this.courrierCommentaire;
	}

	public void setCourrierCommentaire(String courrierCommentaire) {
		this.courrierCommentaire = courrierCommentaire;
	}

	@Column(name = "courrierObjet", length = 254)
	public String getCourrierObjet() {
		return this.courrierObjet;
	}

	public void setCourrierObjet(String courrierObjet) {
		this.courrierObjet = courrierObjet;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "courrierDateReception", length = 10)
	public Date getCourrierDateReception() {
		if(courrierDateReception!=null){
			Calendar cal = Calendar.getInstance();
			  cal.setTime(courrierDateReception);
			  cal.set(Calendar.MILLISECOND, 0);
			  courrierDateReception = new java.sql.Timestamp(cal.getTimeInMillis());}
		
		
		return this.courrierDateReception;
	}

	public void setCourrierDateReception(Date courrierDateReception) {
		this.courrierDateReception = courrierDateReception;
	}

	@Column(name = "courrierCircuit", length = 54)
	public String getCourrierCircuit() {
		return this.courrierCircuit;
	}

	public void setCourrierCircuit(String courrierCircuit) {
		this.courrierCircuit = courrierCircuit;
	}

	@Column(name = "keywords", length = 254)
	public String getKeywords() {
		return this.keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "courrierDateSysteme", length = 10)
	public Date getCourrierDateSysteme() {
		return this.courrierDateSysteme;
	}

	public void setCourrierDateSysteme(Date courrierDateSysteme) {
		this.courrierDateSysteme = courrierDateSysteme;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "courrierDateReponseSysteme", length = 10)
	public Date getCourrierDateReponseSysteme() {
		return this.courrierDateReponseSysteme;
	}

	public void setCourrierDateReponseSysteme(Date courrierDateReponseSysteme) {
		this.courrierDateReponseSysteme = courrierDateReponseSysteme;
	}

	@Column(name = "courrierSupprime")
	public Boolean getCourrierSupprime() {
		return this.courrierSupprime;
	}

	public void setCourrierSupprime(Boolean courrierSupprime) {
		this.courrierSupprime = courrierSupprime;
	}

	@Column(name = "courrierDelegueId")
	public Integer getCourrierDelegueId() {
		return this.courrierDelegueId;
	}

	public void setCourrierDelegueId(Integer courrierDelegueId) {
		this.courrierDelegueId = courrierDelegueId;
	}

	@Column(name = "courrierSupprimeDelegueId")
	public Integer getCourrierSupprimeDelegueId() {
		return this.courrierSupprimeDelegueId;
	}

	public void setCourrierSupprimeDelegueId(Integer courrierSupprimeDelegueId) {
		this.courrierSupprimeDelegueId = courrierSupprimeDelegueId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "courrierSupprimeDate", length = 10)
	public Date getCourrierSupprimeDate() {
		return this.courrierSupprimeDate;
	}

	public void setCourrierSupprimeDate(Date courrierSupprimeDate) {
		this.courrierSupprimeDate = courrierSupprimeDate;
	}

	@Column(name = "courrierCommentaire_AR", length = 16277215)
	public String getCourrierCommentaireAr() {
		return this.courrierCommentaireAr;
	}

	public void setCourrierCommentaireAr(String courrierCommentaireAr) {
		this.courrierCommentaireAr = courrierCommentaireAr;
	}

	@Column(name = "courrierObjet_AR", length = 254)
	public String getCourrierObjetAr() {
		return this.courrierObjetAr;
	}

	public void setCourrierObjetAr(String courrierObjetAr) {
		this.courrierObjetAr = courrierObjetAr;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "courrier")
	public Set<Document> getDocuments() {
		return this.documents;
	}

	public void setDocuments(Set<Document> documents) {
		this.documents = documents;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "courrier")
	public Set<Suivitransmissioncourrier> getSuivitransmissioncourriers() {
		return this.suivitransmissioncourriers;
	}

	public void setSuivitransmissioncourriers(
			Set<Suivitransmissioncourrier> suivitransmissioncourriers) {
		this.suivitransmissioncourriers = suivitransmissioncourriers;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "courrierproprietes", joinColumns = { @JoinColumn(name = "idCourrier", updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "idPropriete", updatable = false) })
	public Set<Proprietes> getProprieteses() {
		return this.proprieteses;
	}

	public void setProprieteses(Set<Proprietes> proprieteses) {
		this.proprieteses = proprieteses;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "courrierdossier", joinColumns = { @JoinColumn(name = "idCourrier", updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "dossierId", updatable = false) })
	public Set<Dossier> getDossiers() {
		return this.dossiers;
	}

	public void setDossiers(Set<Dossier> dossiers) {
		this.dossiers = dossiers;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "courriers")
	public Set<Lienscourriers> getLienscourrierses() {
		return this.lienscourrierses;
	}

	public void setLienscourrierses(Set<Lienscourriers> lienscourrierses) {
		this.lienscourrierses = lienscourrierses;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "courrier")
	public Set<Commentaire> getCommentaires() {
		return this.commentaires;
	}

	public void setCommentaires(Set<Commentaire> commentaires) {
		this.commentaires = commentaires;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "corrierworkflow", joinColumns = { @JoinColumn(name = "idCourrier", updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "idWorkflow", updatable = false) })
	public Set<Workflow> getWorkflows() {
		return this.workflows;
	}

	public void setWorkflows(Set<Workflow> workflows) {
		this.workflows = workflows;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "courrier")
	public Set<Lienscourriers> getLienscourrierses_1() {
		return this.lienscourrierses_1;
	}

	public void setLienscourrierses_1(Set<Lienscourriers> lienscourrierses_1) {
		this.lienscourrierses_1 = lienscourrierses_1;
	}

	public void setCourrierEtatActuelleWorkflow(
			Integer courrierEtatActuelleWorkflow) {
		this.courrierEtatActuelleWorkflow = courrierEtatActuelleWorkflow;
	}

	@Column(name = "courrierEtatActuelleWorkflow")
	public Integer getCourrierEtatActuelleWorkflow() {
		return courrierEtatActuelleWorkflow;
	}

	public void setCourrierflagArchive(Integer courrierflagArchive) {
		this.courrierflagArchive = courrierflagArchive;
	}

	@Column(name = "courrierflagArchive")
	public Integer getCourrierflagArchive() {
		return courrierflagArchive;
	}

	public void setCourrierDateClassement(Date courrierDateClassement) {
		this.courrierDateClassement = courrierDateClassement;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "courrierDateClassement", length = 10)
	public Date getCourrierDateClassement() {
		return courrierDateClassement;
	}

	public void setCourrierDateArchivage(Date courrierDateArchivage) {
		this.courrierDateArchivage = courrierDateArchivage;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "courrierDateArchivage", length = 10)
	public Date getCourrierDateArchivage() {
		if(courrierDateArchivage!=null){
			Calendar cal = Calendar.getInstance();
			  cal.setTime(courrierDateArchivage);
			  cal.set(Calendar.MILLISECOND, 0);
			  courrierDateArchivage = new java.sql.Timestamp(cal.getTimeInMillis());}
		return courrierDateArchivage;
	}

	public void setCourrierDateReceptionReelle(Date courrierDateReceptionReelle) {
		this.courrierDateReceptionReelle = courrierDateReceptionReelle;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "courrierDateCourrierReelle", length = 10)
	public Date getCourrierDateReceptionReelle() {
		if(courrierDateReceptionReelle!=null){
			Calendar cal = Calendar.getInstance();
			  cal.setTime(courrierDateReceptionReelle);
			  cal.set(Calendar.MILLISECOND, 0);
			  courrierDateReceptionReelle = new java.sql.Timestamp(cal.getTimeInMillis());}
		
		return courrierDateReceptionReelle;
	}

	public void setCourrierType(String courrierType) {
		this.courrierType = courrierType;
	}
	
	@Column(name = "courrierType")
	public String getCourrierType() {
		return courrierType;
	}

	public void setCourrierTypeOrdre(int courrierTypeOrdre) {
		this.courrierTypeOrdre = courrierTypeOrdre;
	}
	
	@Column(name = "courrierTypeOrdre")
	public int getCourrierTypeOrdre() {
		return courrierTypeOrdre;
	}

	@Column(name = "courrierOldNum")
	public Integer getCourrierOldNum() {
		return courrierOldNum;
	}

	public void setCourrierOldNum(Integer courrierOldNum) {
		this.courrierOldNum = courrierOldNum;
	}

	@Column(name = "courrierDateReceptionAnnee")
	public Integer getCourrierOldDateOper() {
		return courrierOldDateOper;
	}

	public void setCourrierOldDateOper(Integer courrierOldDateOper) {
		this.courrierOldDateOper = courrierOldDateOper;
	}

	@Column(name = "courrierOldIntt")
	public Integer getCourrierOldIntt() {
		return courrierOldIntt;
	}

	public void setCourrierOldIntt(Integer courrierOldIntt) {
		this.courrierOldIntt = courrierOldIntt;
	}
	@Column(name = "courrierEtatCloture")
	public int getCourrierEtatCloture() {
		return courrierEtatCloture;
	}

	public void setCourrierEtatCloture(int courrierEtatCloture) {
		this.courrierEtatCloture = courrierEtatCloture;
	}
	@Column(name = "courrierCopyTransfere")
	public String getCourrierCopyTransfere() {
		return courrierCopyTransfere;
	}
	
	public void setCourrierCopyTransfere(String courrierCopyTransfere) {
		this.courrierCopyTransfere = courrierCopyTransfere;
	}
	@Column(name = "courrierDateReceptionMois")
	public Integer getCourrierDateReceptionMois() {
		return courrierDateReceptionMois;
	}

	public void setCourrierDateReceptionMois(Integer courrierDateReceptionMois) {
		this.courrierDateReceptionMois = courrierDateReceptionMois;
	}

	public void setCourrierFlagInterne(String courrierFlagInterne) {
		this.courrierFlagInterne = courrierFlagInterne;
	}

	public String getCourrierFlagInterne() {
		return courrierFlagInterne;
	}
    @Column(name="courrierAvecDocumentPhysique")
	public Boolean getCourrierAvecDocumentPhysique() {
		return courrierAvecDocumentPhysique;
	}

	public void setCourrierAvecDocumentPhysique(Boolean courrierAvecDocumentPhysique) {
		this.courrierAvecDocumentPhysique = courrierAvecDocumentPhysique;
	}

	public void setCourrierNecessitePassageParBO(
			Boolean courrierNecessitePassageParBO) {
		this.courrierNecessitePassageParBO = courrierNecessitePassageParBO;
	}

    @Column(name="courrierNecessitePassageParBO")
	public Boolean getCourrierNecessitePassageParBO() {
		return courrierNecessitePassageParBO;
	}	
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idaoConsultation")
	public AoConsultation getAoConsultationId() {
		return aoConsultationId;	}

	public void setAoConsultationId(AoConsultation aoConsultationId) {
		this.aoConsultationId = aoConsultationId;

	}

	
	@Column(name = "courrierFormat", length = 250)
	public String getCourrierFormat() {
		return courrierFormat;
	}

	public void setCourrierFormat(String courrierFormat) {
		this.courrierFormat = courrierFormat;
	}
	
	@Column(name = "courrierDatePointage")
	public Date getCourrierDatePointage() {
		if(courrierDatePointage!=null){
			Calendar cal = Calendar.getInstance();
			  cal.setTime(courrierDatePointage);
			  cal.set(Calendar.MILLISECOND, 0);
			  courrierDatePointage = new java.sql.Timestamp(cal.getTimeInMillis());}
		return courrierDatePointage;
	}

	public void setCourrierDatePointage(Date courrierDatePointage) {
		this.courrierDatePointage = courrierDatePointage;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idcourrierFK")
	public Courrier getIdcourrierFK() {
		return idcourrierFK;
	}

	public void setIdcourrierFK(Courrier idcourrierFK) {
		this.idcourrierFK = idcourrierFK;
	}

	public void setCourrierRefOriginale(String courrierRefOriginale) {
		this.courrierRefOriginale = courrierRefOriginale;
	}

	@Column(name = "courrierRefOriginale")
	public String getCourrierRefOriginale() {
		return courrierRefOriginale;
	}

	public void setCourrierTypeTransport(String courrierTypeTransport) {
		this.courrierTypeTransport = courrierTypeTransport;
	}
	@Column(name = "courrierTypeTransport")
	public String getCourrierTypeTransport() {
		return courrierTypeTransport;
	}
	
}