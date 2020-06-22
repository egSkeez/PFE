package xtensus.beans.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.directory.DirContext;
import javax.servlet.ServletContext;

import org.drools.lang.DRLParser.boolean_key_return;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import xtensus.beans.common.GBO.ChequeModel;
import xtensus.beans.common.GBO.ModelCourrierComplet;
import xtensus.beans.utils.ComposantDynamique;
import xtensus.beans.utils.CourrierConsulterInformations;
import xtensus.beans.utils.CourrierDossierListe;
import xtensus.beans.utils.CourrierInformations;
import xtensus.beans.utils.ExpediteurType;
import xtensus.beans.utils.ListeDetailsDynamique;
import xtensus.beans.utils.ListeDocument;
import xtensus.beans.utils.RecherchePmModel;
import xtensus.beans.utils.RecherchePpModel;
import xtensus.beans.utils.RechercheUnitModel;
import xtensus.beans.utils.RechercheUserModel;
import xtensus.beans.utils.RoleModel;
import xtensus.beans.utils.RechercheMulticritereModel;
import xtensus.beans.utils.RecherchePmModel;
import xtensus.beans.utils.RecherchePpModel;
import xtensus.beans.utils.RechercheUnitModel;
import xtensus.beans.utils.RechercheUserModel;
import xtensus.beans.utils.RoleModel;
//import xtensus.beans.utils.SuiviCourrierCourrier;
//import xtensus.beans.utils.UserBean;
import xtensus.entity.Annotation;
import xtensus.entity.AoConsultation;
import xtensus.entity.Classement_archivage_niveau_02;
import xtensus.entity.Confidentialite;
import xtensus.entity.Courrier;
import xtensus.entity.CourrierDonneeSupplementaire;
import xtensus.entity.Document;
import xtensus.entity.DonneeSupplementaireNature;
import xtensus.entity.Dossier;
import xtensus.entity.Classement_archivage_niveau_01;
import xtensus.entity.Etat;
import xtensus.entity.Expdestexterne;
import xtensus.entity.Fax;
import xtensus.entity.FaxMail;
import xtensus.entity.File;
import xtensus.entity.Groupecontact;
import xtensus.entity.Mail;
import xtensus.entity.Nature;
import xtensus.entity.NatureCategorie;
import xtensus.entity.Pm;
import xtensus.entity.Pp;
import xtensus.entity.Proprietes;
import xtensus.entity.Societe;
import xtensus.entity.Suivitransmissioncourrier;
import xtensus.entity.Sujetmailing;
import xtensus.entity.Transaction;
import xtensus.entity.TransactionDestination;
import xtensus.entity.TransactionDocument;
import xtensus.entity.Transmission;
import xtensus.entity.Typetransaction;
import xtensus.entity.Typeutilisateur;
import xtensus.entity.Unite;
import xtensus.entity.Urgence;
import xtensus.entity.Utilisateur;
import xtensus.entity.Variables;
//import xtensus.faxmail.beans.AttachmentFileBean;
import xtensus.faxmail.utils.GedUtils;
import xtensus.gnl.entity.Evenement;
import xtensus.gnl.entity.Message;
import xtensus.gnl.entity.Notification;
import xtensus.gnl.entity.Trace;
import xtensus.gnl.entity.Xtelog;
import xtensus.ldap.business.LdapOperation;
import xtensus.ldap.model.BOC;
import xtensus.ldap.model.Group;
import xtensus.ldap.model.ItemSelected;
import xtensus.ldap.model.Person;
import xtensus.ldap.model.Unit;
import xtensus.services.ApplicationManager;

import com.xtensus.dal.core.DMSAccessLayer;

@Component
@Scope("session")
public class VariableGlobale implements Serializable {
private boolean sonede=true;
private String docScanne= "test";
//private List<AttachmentFileBean> attachmentFileBeanList;
private List<String> listMesages;


	public List<String> getListMesages() {
	return listMesages;
}

public void setListMesages(List<String> listMesages) {
	this.listMesages = listMesages;
}

	public String getDocScanne() {
		System.out.println("DANS GET docScanne"+docScanne);
	return docScanne;
}

public void setDocScanne(String docScanne) {
	System.out.println("DANS SET docScanne"+docScanne);
	this.docScanne = docScanne;
}

	public boolean isSonede() {
		return sonede;
	}

	public void setSonede(boolean sonede) {
		this.sonede = sonede;
	}
	private String nomDirecteur="مصباح هلالي";
	
	public String getNomDirecteur() {
		return nomDirecteur;
	}

	public void setNomDirecteur(String nomDirecteur) {
		this.nomDirecteur = nomDirecteur;
	}
	/***
	 * taha
	 * 
	 */
	private List<ListeDocument> ListeDocument;
	
	public List<ListeDocument> getListeDocument() {
		return ListeDocument;
	}

	public void setListeDocument(List<ListeDocument> listeDocument) {
		ListeDocument = listeDocument;
	}

	private NatureCategorie natureCategorie;
	private Integer courrierExecuter;
	/**
	 * HB
	 */
	List<ListeDetailsDynamique> listeDetailsTransmission;
	private boolean execute=false;
	private static final long serialVersionUID = 3557998144037761877L;
	private Annotation annotation;
	private Confidentialite confidentialite;
	private CourrierConsulterInformations consulterInformations;
	private Nature nature;
	private Proprietes proprietes;
	private Urgence urgence;
	private Transmission transmission;
	private Etat etat;
	private Dossier dossier;
	private Courrier courrier = new Courrier();
	private Transaction transaction;
	private Utilisateur utilisateur;
	private Document document;
	private Typeutilisateur typeutilisateur;
	private Typetransaction typetransaction;
	private Variables variables;
	// private String role;
	private List<String> role = new ArrayList<String>();
	private boolean itemDisabledForAssociatedUnit;
	private boolean roleUser = false;
	private boolean connected = false;
	private Expdestexterne expdestexterne = new Expdestexterne();
	private Expdestexterne expdestexterneNotif = new Expdestexterne();
	private Expdestexterne distinataire;
	private List<Expdestexterne> listDistinataire;
	private Pp pp;
	private Pm pm;
	private int nombrePP;
	private int newUserId;
	private int newGroupId;
	private int referenceDossier;
	private String expNom="";
	private String destNom="";
	private String copyDestNom;
	private String copyExpNom;
	private String copyExpReelNom;
	private String copyCourrierCommentaire;
	private String copyAnnotationResult;
	private String stateSession = "";
	private String redirect = "";
	private boolean toReplay = false;
	private String copyOtherDest;
	private List<Courrier> listCourrier;
//	private UserBean userBean;
	private BOC boc;
	private BOC centralBoc;
	private Unit direction;
	private Person person;
	private Person personCodeUnique;
	private List<ItemSelected> listSelectedItem = new ArrayList<ItemSelected>();
	private List<Object> copyLdapData;
	private List<Object> copyListNewStructure;
	private List<Object> copyListSelectedObject = new ArrayList<Object>();
	private List<Object> copyListSelectedObjectExp = new ArrayList<Object>();
	private List<Person> copyLdapListUser;
	private List<Person> copyLdapListAllUser;
	private List<Person> copyListSelectedPerson = new ArrayList<Person>();
	private List<BOC> copyListSelectedBoc = new ArrayList<BOC>();
	private List<Unit> copyListSelectedUnit = new ArrayList<Unit>();
	private List<Person> copyLdapListOtherUser;
	private List<Pp> copyListPP = new ArrayList<Pp>();
	private List<Pm> copyListPM = new ArrayList<Pm>();
	private List<Group> copyLdapListGroup;
	private List<Unit> copyLdapListUnit;
	private List<CourrierConsulterInformations> copyListCourriersEnvoyesJour;
	private List<CourrierConsulterInformations> copyListCourriersRecusJour;
	private List<CourrierConsulterInformations> copyListCourriersRecusAvaliderJour;
	private List<CourrierConsulterInformations> copyListCourriersTousJour;
	private List<CourrierConsulterInformations> copyListCourriersEnvoyes;
	private List<CourrierConsulterInformations> copyListCourriersRecus;
	private List<CourrierConsulterInformations> copyListCourriersRecusAvalider;
	private List<CourrierConsulterInformations> copyListCourriersTous;
	private List<CourrierConsulterInformations> copyListDossiersEnvoyesJour;
	private List<CourrierConsulterInformations> copyListDossiersRecusJour;
	private List<CourrierConsulterInformations> copyListDossiersTousJour;
	private List<CourrierConsulterInformations> copyListDossiersEnvoyes;
	private List<CourrierConsulterInformations> copyListDossiersRecus;
	private List<CourrierConsulterInformations> copyListDossiersTous;
	private List<CourrierConsulterInformations> copyListCourriersDeValidation;
	private List<CourrierConsulterInformations> copyListCourriersTraite;
	private List<CourrierConsulterInformations> copyListCourriersAValider;
	private List<CourrierConsulterInformations> copyListCourriersValide;
	private List<CourrierConsulterInformations> copyListCourriersNonValide;
//	private CourrierConsulterInformations courDossConsulterInformations;
	private CourrierInformations courDossConsulterInformations;
	private List<CourrierDossierListe> listCourriesrAffectes;
	private CourrierDossierListe courrierDossierListe;
	private DirContext dirContext = null;
	private String locale = "fr_FR";
	private String localFr="fr";
	private RechercheMulticritereModel rechercheMulticritere = new RechercheMulticritereModel();;
	private Societe societe;
	private String pathJasper;
	private TransactionDestination transactionDestination;
	private Person user;
	private Classement_archivage_niveau_02 armoire;
	private Classement_archivage_niveau_01 etages;
	public boolean statusPerson;
	private AoConsultation aoConsultation;	
	private Courrier valise;
	private boolean flagValise;
	private int flagModif;
	private boolean courrierRefOriginal=false;
	// Boc
	private List<CourrierConsulterInformations> copyListCourriersTousBoc;
	private List<CourrierConsulterInformations> copyListCourriersTousBocTraite;
	private List<CourrierConsulterInformations> copyListCourriersTousBocNonTraite;
	private List<CourrierConsulterInformations> copyListCourriersTousBocArrive;
	private List<CourrierConsulterInformations> copyListCourriersTousBocArriveTraite;
	private List<CourrierConsulterInformations> copyListCourriersTousBocArriveNonTraite;
	private List<CourrierConsulterInformations> copyListCourriersTousBocDepart;
	private List<CourrierConsulterInformations> copyListCourriersTousBocDepartTraite;
	private List<CourrierConsulterInformations> copyListCourriersTousBocDepartNonTraite;
	private List<CourrierConsulterInformations> copyListCourriersMailBoc;
	private List<CourrierConsulterInformations> copyListCourriersMailBocTraite;
	private List<CourrierConsulterInformations> copyListCourriersMailBocNonTraite;
	private List<CourrierConsulterInformations> copyListCourriersMailBocArrive;
	private List<CourrierConsulterInformations> copyListCourriersMailBocArriveTraite;
	private List<CourrierConsulterInformations> copyListCourriersMailBocArriveNonTraite;
	private List<CourrierConsulterInformations> copyListCourriersMailBocDepart;
	private List<CourrierConsulterInformations> copyListCourriersMailBocDepartTraite;
	private List<CourrierConsulterInformations> copyListCourriersMailBocDepartNonTraite;
	private List<CourrierConsulterInformations> copyListCourriersFaxBoc;
	private List<CourrierConsulterInformations> copyListCourriersFaxBocTraite;
	private List<CourrierConsulterInformations> copyListCourriersFaxBocNonTraite;
	private List<CourrierConsulterInformations> copyListCourriersFaxBocArrive;
	private List<CourrierConsulterInformations> copyListCourriersFaxBocArriveTraite;
	private List<CourrierConsulterInformations> copyListCourriersFaxBocArriveNonTraite;
	private List<CourrierConsulterInformations> copyListCourriersFaxBocDepart;
	private List<CourrierConsulterInformations> copyListCourriersFaxBocDepartTraite;
	private List<CourrierConsulterInformations> copyListCourriersFaxBocDepartNonTraite;
	private List<CourrierConsulterInformations> copyListCourriersAutreBoc;
	private List<CourrierConsulterInformations> copyListCourriersAutreBocTraite;
	private List<CourrierConsulterInformations> copyListCourriersAutreBocNonTraite;
	private List<CourrierConsulterInformations> copyListCourriersAutreBocArrive;
	private List<CourrierConsulterInformations> copyListCourriersAutreBocArriveTraite;
	private List<CourrierConsulterInformations> copyListCourriersAutreBocArriveNonTraite;
	private List<CourrierConsulterInformations> copyListCourriersAutreBocDepart;
	private List<CourrierConsulterInformations> copyListCourriersAutreBocDepartTraite;
	private List<CourrierConsulterInformations> copyListCourriersAutreBocDepartNonTraite;
	private boolean showMonitoringButtonForDest;
	private List<CourrierDossierListe> listCourriersAffectes;
	private Courrier linkedCourrier;
	private List<String> listSelectedAnnotations;
	/************* Mail && Fax ****************/
	private Mail mail;
	private boolean inTransfertBean = false;
	private Fax fax;
	private GedUtils gedUtils;
	private List<BOC> listTousBos = new ArrayList<BOC>();
	private List<Unit> listTousUnit= new ArrayList<Unit>();
	/************ GNL ***********/
	private Xtelog log;
	private Trace trace;
	private Evenement evenement;
	private Notification notification;
	private List<Notification> listNotification;
	private Message message;

	private List<Person> copyListSelectedPersonNotif = new ArrayList<Person>();
	private List<Unit> copyListSelectedUnitNotif = new ArrayList<Unit>();
	private List<Pp> copyListPPNotif = new ArrayList<Pp>();
	private List<Pm> copyListPMNotif = new ArrayList<Pm>();

//	private SuiviCourrierCourrier suiviCourrierCourrier;
	private String afficheTitre="";
	private String afficheTitreList="";
	private int idUser;
	private int idUnit;
	private RechercheUnitModel rechercheUnitModel = new RechercheUnitModel();
	private RechercheUserModel rechercheUserModel = new RechercheUserModel();
	private List<RechercheUserModel> listUser = new ArrayList<RechercheUserModel>();
	private List<RechercheUnitModel> listUnit = new ArrayList<RechercheUnitModel>();
	private boolean openedSTPannel = false;
	private String typeIntervenant = "utilisateur";
	private String redirectUser;

	private Courrier nouveauCourrier = new Courrier();
	private String copySelectedItemNature = "";
	private String copySelectedItemConf = "";
	private String copySelectedItemUg = "";
	private String copySelectedItemsTr = "";
	private List<String> copySelectedItemsAnnotation = new ArrayList<String>();
	private String necessiteReponse = "";
	private String copyTypeExpediteur = "MoiMeme";
	private RecherchePpModel recherchePpModel = new RecherchePpModel();
	private RecherchePmModel recherchePmModel = new RecherchePmModel();
	private RecherchePpModel recherchePpModelForDetails = new RecherchePpModel();
	private RecherchePmModel recherchePmModelForDetails = new RecherchePmModel();
	private List<RecherchePpModel> listPp = new ArrayList<RecherchePpModel>();
	private List<RecherchePmModel> listPm = new ArrayList<RecherchePmModel>();
	private RoleModel roleModel;
	private Group group;
	private Groupecontact groupecontact;
	private Sujetmailing sujetmailing;
	// A ajouter
	private boolean userAffectedToUnit;
	private boolean inRestoration = false;

	// ** khaled saoudi
	private FaxMail faxMail; // ceci est ajouté au besoin de changer l'implementation de la classe ConsulterFaxBean comme la classe ConsulterMailBean
	private String documentType;
	private List<CourrierConsulterInformations> listCourrierForLiens = new ArrayList<CourrierConsulterInformations>();

	private String typeCourrierJourForRapport;
	private String typeCourrierValidationJourForRapport;
	private String transmissionCourrierJourForRapport;
	private String typeCourrierTraitementJourForRapport;
	private String categorieCourrierJourForRapport;
	private String typeCourrierJourForRapportAncien = "Tous";
	private String typeCourrierValidationJourForRapportAncien = "";
	private String transmissionCourrierJourForRapportAncien = "Tout les courriers";
	private String typeCourrierTraitementJourForRapportAncien = "tous";
	private String categorieCourrierJourForRapportAncien = "T";
	private String courrierRubrique = "6";
	private String courrierRubriqueJour;
	private List<CourrierInformations> listCourriers = new ArrayList<CourrierInformations>();
	//
	private Courrier courrierTempValue;
	private List<String> selectedAnnotationItems;
	private String otherAnnotation;
	private String chooseAnnotation;
	private String typeSender;
	private String typeCourrier;
	private String typeDateRechMulti;
	private RechercheMulticritereModel recherche;
	private HashMap<Integer, Person> hashMapAllUser;
	private HashMap<Integer, Unit> hashMapUnit;
	private String destinataireReel;
	private String referenceDestinataireReel;
	private String selectedListCourrier = "CRjour";
	private String contactPays;
	private CourrierInformations courrierInformations;
	private boolean categorieParNature;
	private boolean showReceptionPhysique;
	List<Transaction> allTransactions;
	private String affichagePanelRecption;
	//** khaled saoudi
	/**
	 * 
	 * CMIS
	 * 
	 */
	// ---------- COUCHE ACCES GED ----------------
	private String scannedTempFileName;
	private String scannedTempFolder;
	private String documentPath = "";
	private String namingConfigFilePath;
	// private String namingConfigFilePath =
	// "C:\\WorkspaceSIGA\\GBO_v1.g\\WebContent\\WEB-INF\\GEDConfig\\config.xml";
	private File uploadedFile;
	private byte[] uploadedData;
	private byte[]  scannedData;
	private String uploadType = "";
	private DMSAccessLayer dmsAccessLayer;
	// ---------- COUCHE ACCES GED ----------------

	private List<Date> datesSauvgardes;

	private String activeCTab;

	private LdapOperation ldapOperation;

	private int numberOfRows;
	private HashMap<String, Object> filterMap;
	private String sortField;
	private boolean descending;
	private boolean oldPage;
	private Integer firstPageJour;
	private Integer firstPageMois;
	private Integer firstPageAnnee;
	private Integer firstPageAncien;
	private Integer firstPageDJour;
	private Integer firstPageDAncien;
	private Integer rechercheRowCount;
	private boolean rechercheCountVisited;
	private String courrierId;
	private String courrierRef;
	private Integer numRowCourrier;
	private Courrier courrierTest;
	private String dbType;
	private String expditeurUnite;
	private String changerDoc="nonChanger";
	private List<ListeDestinatairesModel> listAnnotations;
//KHA *** 
private Etat transactionDestEtatReceptionPhysique;
private String transactionDestCommentaireReceptionPhysique;
private Date transactionDestDateReceptionPhysique;
private TransactionDocument transactionDocument; 
// KHA : ROLE_BORDEREAU_ENVOI : ajouté le 15-03-2019
private boolean roleBordereau;
//KHA : Sous Titre Rapport : ajouté le 19-03-2019
private String sousTitreRapportBoc;
private String  sousTitreRapportResponsable;
private String  sousTitreRapportSecAgent;
private String sousTitreRapportRelance;
//
private boolean roleNoteTransmission;
//[JS]
 private String natureParCategorie;
 private Integer topVignt=20;
 private boolean affichTempsReponse;
 private String pagePrecedente;
 private boolean popupAffectation;
 private int aoConsultationId;
 private int flagNature;
 private CourrierDonneeSupplementaire courrierDonneeSupplementaire;
 private boolean typeUploadFichier=false;
 private String expediteurCourrierRepondre;

 private boolean affichePanelAjoutUpload=true;
 private boolean affichePanelModifUpload=false;
 private int flagLies=2;
 //[JS]===== 2020-03-24
 
 private boolean etatAccusesReception=true;
 private int flagDocuPhysique=1;
 //[JS] ======= 2020-03-27
 
 private Date vbDateConsultationCourrier;
 private boolean courrierAExcecuter;
 //==============
 
 //Prendre valeur "1" lors de la 1ére fois de la redirecion 
 // vers page de transfert
 private int premiereEntreeTransfert;
 private boolean afficheLienLiees;
 private String codeUniqueCourrier;
 //variable permet d'afficher que mode transamision lors de modif Courrier
 private boolean modeEnveloppe;
 private List<ComposantDynamique> listComposantDynamiqueTransmission;
private List<DonneeSupplementaireNature> donneeSupplementaireNature;
private List<ComposantDynamique> listComposantDynamiqueNature;
private List<ChequeModel> listChequesSave;
//=== KHA : code unique courrier
 private String courrierCodeUnique;
 //=== KHA : rapport courrier
 private Unit selectedUnit;
 private Date selectedDateD;
 private Date selectedDateF;
 // ==== KHA : list unit ldap
 private List<Unit> listUnitldap;
 // ====== kha rapport cheque 
 private int idCheque;
 private int idReclammation;
 private int idRapidePoste;
 private int idLettreRecommandee;
 private String typeDateRapport;
 private int jourOuAutr;
//AH : Début ======================================================================================
private List<ListeDestinatairesModel>listeDestinataire;
private int destinataireId;
//KBS:
private int flagCloture ;
private boolean flagAjout;
private String selectedItemCategorie;
private int flagInterne;
private List<ListeDetailsDynamique> listeDetailsDynamiques;
private List<ListeDetailsDynamique> listeDetailsDynamiquesTransmission;
private Boolean flagCheque;
private boolean flagTransfert;
//AH : synchronisation
private List<Unite> listNouvellesUnites = new ArrayList<Unite>();
private List<Unite> listUnitesModifiees = new ArrayList<Unite>();
private List<ChequeModel> listChequeTableau= new ArrayList<ChequeModel>();
private List<CourrierInformations> listCourriersInformationsAffecte;
/////////KBS 2019-12-09
private String choixMois;
private String choixAnnee;
////////////////////////
////////KBS 2019-12-12
private String datePeriodeAnneeAR;
private String datePeriodeAnneeFR;
private int idNatureForTestRapport;
private List<ExpediteurType> listConsulter;
private boolean notAdd;
private boolean notAddCourrier;
private List<CourrierInformations> listeCourriersInformation;
private int firstRow;
private List<AoConsultation> AoConsultationList;
private List<CourrierInformations> listValise;
private boolean masquerPanelDetailCourrier=true;
private boolean showInputDay;
private boolean showInputMonth;
private boolean showInputYear;
private boolean showdateF;;
private Suivitransmissioncourrier accuseInformation;
private String destinataireOffre;
public int getIdNatureForTestRapport() {
	return idNatureForTestRapport;
}

public void setIdNatureForTestRapport(int idNatureForTestRapport) {
	this.idNatureForTestRapport = idNatureForTestRapport;
}

public String getDatePeriodeAnneeAR() {
	return datePeriodeAnneeAR;
}

public void setDatePeriodeAnneeAR(String datePeriodeAnneeAR) {
	this.datePeriodeAnneeAR = datePeriodeAnneeAR;
}

public String getDatePeriodeAnneeFR() {
	return datePeriodeAnneeFR;
}

public void setDatePeriodeAnneeFR(String datePeriodeAnneeFR) {
	this.datePeriodeAnneeFR = datePeriodeAnneeFR;
}

public String getChoixMois() {
	return choixMois;
}

public void setChoixMois(String choixMois) {
	this.choixMois = choixMois;
}

public String getChoixAnnee() {
	return choixAnnee;
}

public void setChoixAnnee(String choixAnnee) {
	this.choixAnnee = choixAnnee;
}

/////////////////////////

////////KBS 2019-12-10
private int firstYear;


public int getFirstYear() {
	return firstYear;
}

public void setFirstYear(int firstYear) {
	this.firstYear = firstYear;
}
/////////////////////////
public int getDestinataireId() {
	return destinataireId;
}

public void setDestinataireId(int destinataireId) {
	this.destinataireId = destinataireId;
}

public List<ListeDestinatairesModel> getListeDestinataire() {
	return listeDestinataire;
}

public void setListeDestinataire(List<ListeDestinatairesModel> listeDestinataire) {
	this.listeDestinataire = listeDestinataire;
}
//AH : Fin ======================================================================================
	//Interface pour exécuter la requete
	@Autowired
	ApplicationManager appMgr ;
//Attribut pour tester la disponibiliée de la fonctionnalité GED
	private String gedStatus ;
	
	private String rendered;
	
	private BOC noeudEncours;

	public BOC getNoeudEncours() {
		return noeudEncours;
	}

	public void setNoeudEncours(BOC noeudEncours) {
		this.noeudEncours = noeudEncours;
	}

	public VariableGlobale() {
	}

	public String getScannedTempFileName() {
		return scannedTempFileName;
	}

	public void setScannedTempFileName(String scannedTempFileName) {
		this.scannedTempFileName = scannedTempFileName;
	}

	public String getScannedTempFolder() {
		return scannedTempFolder;
	}

	public void setScannedTempFolder(String scannedTempFolder) {
		this.scannedTempFolder = scannedTempFolder;
	}

	public String getDocumentPath() {
		return documentPath;
	}

	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}

	private String selectedItemsTr;
	private String selectedItemNature;
	
	
	
	public String getSelectedItemsTr() {
		return selectedItemsTr;
	}

	public void setSelectedItemsTr(String selectedItemsTr) {
		this.selectedItemsTr = selectedItemsTr;
	}

	
	
	public String getSelectedItemNature() {
		return selectedItemNature;
	}

	public void setSelectedItemNature(String selectedItemNature) {
		this.selectedItemNature = selectedItemNature;
	}

	public String getNamingConfigFilePath() {
		// get context path
//		ExternalContext jsfContext = FacesContext.getCurrentInstance()
//				.getExternalContext();
//		ServletContext servletContext = (ServletContext) jsfContext
//				.getContext();
//		String webContentRoot = servletContext.getRealPath("/");
//		String res = webContentRoot.substring(0, 16) + "GBO_v1.f";
//		System.out.println(res);
//		webContentRoot = res;
//		// fichier xml
//		String pathConfigFile = webContentRoot
//				+ "\\WebContent\\WEB-INF\\GEDConfig\\config.xml";
		try {
			Resource rsrc = new ClassPathResource("../GEDConfig/config.xml");
			String pathConfigFile = rsrc.getFile().getAbsolutePath();
//			System.out.println("VariableGlobale ---> Config File : " + pathConfigFile);
			namingConfigFilePath = pathConfigFile;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("EXCEPTION in VariableGlobale ---> Config File ####");
		}
		return namingConfigFilePath;
	}

	public void setNamingConfigFilePath(String namingConfigFilePath) {
		this.namingConfigFilePath = namingConfigFilePath;
	}

	public File getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(File uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public byte[] getUploadedData() {
		return uploadedData;
	}

	public void setUploadedData(byte[] uploadedData) {
		this.uploadedData = uploadedData;
	}

	public String getUploadType() {
		return uploadType;
	}

	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}

	public DMSAccessLayer getDmsAccessLayer() {
		return dmsAccessLayer;
	}

	public void setDmsAccessLayer(DMSAccessLayer dmsAccessLayer) {
		this.dmsAccessLayer = dmsAccessLayer;
	}

	public Annotation getAnnotation() {
		return annotation;
	}

	public void setAnnotation(Annotation annotation) {
		this.annotation = annotation;
	}

	public Confidentialite getConfidentialite() {
		return confidentialite;
	}

	public void setConfidentialite(Confidentialite confidentialite) {
		this.confidentialite = confidentialite;
	}

	public Nature getNature() {
		return nature;
	}

	public void setNature(Nature nature) {
		this.nature = nature;
	}

	public Proprietes getProprietes() {
		return proprietes;
	}

	public void setProprietes(Proprietes proprietes) {
		this.proprietes = proprietes;
	}

	public Urgence getUrgence() {
		return urgence;
	}

	public void setUrgence(Urgence urgence) {
		this.urgence = urgence;
	}

	public void setDossier(Dossier dossier) {
		this.dossier = dossier;
	}

	public Dossier getDossier() {
		return dossier;
	}

	public void setCourrier(Courrier courrier) {
		this.courrier = courrier;
	}

	public Courrier getCourrier() {
		return courrier;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setTypeutilisateur(Typeutilisateur typeutilisateur) {
		this.typeutilisateur = typeutilisateur;
	}

	public Typeutilisateur getTypeutilisateur() {
		return typeutilisateur;
	}

	public Expdestexterne getExpdestexterne() {
		return expdestexterne;
	}

	public void setExpdestexterne(Expdestexterne expdestexterne) {
		this.expdestexterne = expdestexterne;
	}

	public Expdestexterne getDistinataire() {
		return distinataire;
	}

	public void setDistinataire(Expdestexterne distinataire) {
		this.distinataire = distinataire;
	}

	public List<Expdestexterne> getListDistinataire() {
		return listDistinataire;
	}

	public void setListDistinataire(List<Expdestexterne> listDistinataire) {
		this.listDistinataire = listDistinataire;
	}

	public void setExpNom(String expNom) {
		this.expNom = expNom;
	}

	public String getExpNom() {
		return expNom;
	}

	public void setDestNom(String destNom) {
//		System.out.println("dans set de VG : "+destNom);
		this.destNom = destNom;
	}

	public String getDestNom() {
		//System.out.println("dans get de VG : "+destNom);

		return destNom;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public Document getDocument() {
		return document;
	}

	public void setTransmission(Transmission transmission) {
		this.transmission = transmission;
	}

	public Transmission getTransmission() {
		return transmission;
	}

	public void setPp(Pp pp) {
		this.pp = pp;
	}

	public Pp getPp() {
		return pp;
	}

	public void setPm(Pm pm) {
		this.pm = pm;
	}

	public Pm getPm() {
		return pm;
	}

	public void setListCourrier(List<Courrier> listCourrier) {
		this.listCourrier = listCourrier;
	}

	public List<Courrier> getListCourrier() {
		return listCourrier;
	}

	public void setNombrePP(int nombrePP) {
		this.nombrePP = nombrePP;
	}

	public int getNombrePP() {
		return nombrePP;
	}

	public void setEtat(Etat etat) {
		this.etat = etat;
	}

	public Etat getEtat() {
		return etat;
	}

	public void setTypetransaction(Typetransaction typetransaction) {
		this.typetransaction = typetransaction;
	}

	public Typetransaction getTypetransaction() {
		return typetransaction;
	}

	public void setVariables(Variables variables) {
		this.variables = variables;
	}

	public Variables getVariables() {
		return variables;
	}
//
//	public void setUserBean(UserBean userBean) {
//		this.userBean = userBean;
//	}
//
//	public UserBean getUserBean() {
//		return userBean;
//	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Person getPerson() {
		return person;
	}

	public void setListSelectedItem(List<ItemSelected> listSelectedItem) {
		this.listSelectedItem = listSelectedItem;
	}

	public List<ItemSelected> getListSelectedItem() {
		return listSelectedItem;
	}

	public void setCopyLdapData(List<Object> copyLdapData) {
		this.copyLdapData = copyLdapData;
	}

	public List<Object> getCopyLdapData() {
		return copyLdapData;
	}

	public void setCopyListNewStructure(List<Object> copyListNewStructure) {
		this.copyListNewStructure = copyListNewStructure;
	}

	public List<Object> getCopyListNewStructure() {
		return copyListNewStructure;
	}

	public void setCopyLdapListUser(List<Person> copyLdapListUser) {
		this.copyLdapListUser = copyLdapListUser;
	}

	public List<Person> getCopyLdapListUser() {
		return copyLdapListUser;
	}

	public void setCopyLdapListAllUser(List<Person> copyLdapListAllUser) {
		this.copyLdapListAllUser = copyLdapListAllUser;
	}

	public List<Person> getCopyLdapListAllUser() {
		return copyLdapListAllUser;
	}

	public void setCopyLdapListOtherUser(List<Person> copyLdapListOtherUser) {
		this.copyLdapListOtherUser = copyLdapListOtherUser;
	}

	public List<Person> getCopyLdapListOtherUser() {
		return copyLdapListOtherUser;
	}

	public void setCopyLdapListGroup(List<Group> copyLdapListGroup) {
		this.copyLdapListGroup = copyLdapListGroup;
	}

	public List<Group> getCopyLdapListGroup() {
		return copyLdapListGroup;
	}

	public void setCopyListSelectedPerson(List<Person> copyListSelectedPerson) {
		this.copyListSelectedPerson = copyListSelectedPerson;
	}

	public List<Person> getCopyListSelectedPerson() {
		return copyListSelectedPerson;
	}

	public void setCopyListSelectedObject(List<Object> copyListSelectedObject) {
		this.copyListSelectedObject = copyListSelectedObject;
	}

	public List<Object> getCopyListSelectedObject() {
		return copyListSelectedObject;
	}

	public void setCopyListPP(List<Pp> copyListPP) {
		this.copyListPP = copyListPP;
	}

	public List<Pp> getCopyListPP() {
		return copyListPP;
	}

	public void setCopyListPM(List<Pm> copyListPM) {
		this.copyListPM = copyListPM;
	}

	public List<Pm> getCopyListPM() {
		return copyListPM;
	}

	public void setCopyDestNom(String copyDestNom) {
		this.copyDestNom = copyDestNom;
	}

	public String getCopyDestNom() {
		return copyDestNom;
	}

	public void setDirection(Unit direction) {
		this.direction = direction;
	}

	public Unit getDirection() {
		return direction;
	}

	public void setBoc(BOC boc) {
		this.boc = boc;
	}

	public BOC getBoc() {
		return boc;
	}

	public void setNewUserId(int newUserId) {
		this.newUserId = newUserId;
	}

	public int getNewUserId() {
		return newUserId;
	}

	public void setNewGroupId(int newGroupId) {
		this.newGroupId = newGroupId;
	}

	public int getNewGroupId() {
		return newGroupId;
	}

	public void setCentralBoc(BOC centralBoc) {
		this.centralBoc = centralBoc;
	}

	public BOC getCentralBoc() {
		return centralBoc;
	}

	public void setCopyListSelectedBoc(List<BOC> copyListSelectedBoc) {
		this.copyListSelectedBoc = copyListSelectedBoc;
	}

	public List<BOC> getCopyListSelectedBoc() {
		return copyListSelectedBoc;
	}

	public void setCopyListSelectedUnit(List<Unit> copyListSelectedUnit) {
		this.copyListSelectedUnit = copyListSelectedUnit;
	}

	public List<Unit> getCopyListSelectedUnit() {
		return copyListSelectedUnit;
	}

	public void setCopyListCourriersEnvoyesJour(
			List<CourrierConsulterInformations> copyListCourriersEnvoyesJour) {
		this.copyListCourriersEnvoyesJour = copyListCourriersEnvoyesJour;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersEnvoyesJour() {
		return copyListCourriersEnvoyesJour;
	}

	public void setCopyListCourriersRecusJour(
			List<CourrierConsulterInformations> copyListCourriersRecusJour) {
		this.copyListCourriersRecusJour = copyListCourriersRecusJour;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersRecusJour() {
		return copyListCourriersRecusJour;
	}

	public void setCopyLdapListUnit(List<Unit> copyLdapListUnit) {
		this.copyLdapListUnit = copyLdapListUnit;
	}

	public List<Unit> getCopyLdapListUnit() {
		return copyLdapListUnit;
	}

	public void setItemDisabledForAssociatedUnit(boolean itemDisabledForAssociatedUnit) {
		this.itemDisabledForAssociatedUnit = itemDisabledForAssociatedUnit;
	}

	public boolean isItemDisabledForAssociatedUnit() {
		return itemDisabledForAssociatedUnit;
	}

	public void setCopyExpNom(String copyExpNom) {
		this.copyExpNom = copyExpNom;
	}

	public String getCopyExpNom() {
		return copyExpNom;
	}

	public void setCopyCourrierCommentaire(String copyCourrierCommentaire) {
		this.copyCourrierCommentaire = copyCourrierCommentaire;
	}

	public String getCopyCourrierCommentaire() {
		return copyCourrierCommentaire;
	}

	public void setCopyAnnotationResult(String copyAnnotationResult) {
		this.copyAnnotationResult = copyAnnotationResult;
	}

	public String getCopyAnnotationResult() {
		return copyAnnotationResult;
	}

	public void setRoleUser(boolean roleUser) {
		this.roleUser = roleUser;
	}

	public boolean isRoleUser() {
		return roleUser;
	}

	public void setStateSession(String stateSession) {
		this.stateSession = stateSession;
	}

	public String getStateSession() {
		return stateSession;
	}

	public void setCopyOtherDest(String copyOtherDest) {
		this.copyOtherDest = copyOtherDest;
	}

	public String getCopyOtherDest() {
		return copyOtherDest;
	}

	public void setConsulterInformations(CourrierConsulterInformations consulterInformations) {
		this.consulterInformations = consulterInformations;
	}

	public CourrierConsulterInformations getConsulterInformations() {
		return consulterInformations;
	}

	public void setToReplay(boolean toReplay) {
		this.toReplay = toReplay;
	}

	public boolean isToReplay() {
		return toReplay;
	}

	public void setCopyExpReelNom(String copyExpReelNom) {
		this.copyExpReelNom = copyExpReelNom;
	}

	public String getCopyExpReelNom() {
		return copyExpReelNom;
	}

	public void setReferenceDossier(int referenceDossier) {
		this.referenceDossier = referenceDossier;
	}

	public int getReferenceDossier() {
		return referenceDossier;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	public String getRedirect() {
		return redirect;
	}

//	public void setCourDossConsulterInformations(
//			CourrierConsulterInformations courDossConsulterInformations) {
//		this.courDossConsulterInformations = courDossConsulterInformations;
//	}
//
//	public CourrierConsulterInformations getCourDossConsulterInformations() {
//		return courDossConsulterInformations;
//	}

	public void setCopyListCourriersRecusAvaliderJour(
			List<CourrierConsulterInformations> copyListCourriersRecusAvaliderJour) {
		this.copyListCourriersRecusAvaliderJour = copyListCourriersRecusAvaliderJour;
	}

	public CourrierInformations getCourDossConsulterInformations() {
		return courDossConsulterInformations;
	}

	public void setCourDossConsulterInformations(CourrierInformations courDossConsulterInformations) {
		this.courDossConsulterInformations = courDossConsulterInformations;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersRecusAvaliderJour() {
		return copyListCourriersRecusAvaliderJour;
	}

	public void setCopyListCourriersTousJour(
			List<CourrierConsulterInformations> copyListCourriersTousJour) {
		this.copyListCourriersTousJour = copyListCourriersTousJour;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersTousJour() {
		return copyListCourriersTousJour;
	}

	public void setCopyListCourriersEnvoyes(
			List<CourrierConsulterInformations> copyListCourriersEnvoyes) {
		this.copyListCourriersEnvoyes = copyListCourriersEnvoyes;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersEnvoyes() {
		return copyListCourriersEnvoyes;
	}

	public void setCopyListCourriersRecus(List<CourrierConsulterInformations> copyListCourriersRecus) {
		this.copyListCourriersRecus = copyListCourriersRecus;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersRecus() {
		return copyListCourriersRecus;
	}

	public void setCopyListCourriersRecusAvalider(
			List<CourrierConsulterInformations> copyListCourriersRecusAvalider) {
		this.copyListCourriersRecusAvalider = copyListCourriersRecusAvalider;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersRecusAvalider() {
		return copyListCourriersRecusAvalider;
	}

	public void setCopyListCourriersTous(List<CourrierConsulterInformations> copyListCourriersTous) {
		this.copyListCourriersTous = copyListCourriersTous;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersTous() {
		return copyListCourriersTous;
	}

	public void setCopyListDossiersEnvoyesJour(
			List<CourrierConsulterInformations> copyListDossiersEnvoyesJour) {
		this.copyListDossiersEnvoyesJour = copyListDossiersEnvoyesJour;
	}

	public List<CourrierConsulterInformations> getCopyListDossiersEnvoyesJour() {
		return copyListDossiersEnvoyesJour;
	}

	public void setCopyListDossiersRecusJour(
			List<CourrierConsulterInformations> copyListDossiersRecusJour) {
		this.copyListDossiersRecusJour = copyListDossiersRecusJour;
	}

	public List<CourrierConsulterInformations> getCopyListDossiersRecusJour() {
		return copyListDossiersRecusJour;
	}

	public void setCopyListDossiersTousJour(
			List<CourrierConsulterInformations> copyListDossiersTousJour) {
		this.copyListDossiersTousJour = copyListDossiersTousJour;
	}

	public List<CourrierConsulterInformations> getCopyListDossiersTousJour() {
		return copyListDossiersTousJour;
	}

	public void setCopyListDossiersEnvoyes(
			List<CourrierConsulterInformations> copyListDossiersEnvoyes) {
		this.copyListDossiersEnvoyes = copyListDossiersEnvoyes;
	}

	public List<CourrierConsulterInformations> getCopyListDossiersEnvoyes() {
		return copyListDossiersEnvoyes;
	}

	public void setCopyListDossiersRecus(List<CourrierConsulterInformations> copyListDossiersRecus) {
		this.copyListDossiersRecus = copyListDossiersRecus;
	}

	public List<CourrierConsulterInformations> getCopyListDossiersRecus() {
		return copyListDossiersRecus;
	}

	public void setCopyListDossiersTous(List<CourrierConsulterInformations> copyListDossiersTous) {
		this.copyListDossiersTous = copyListDossiersTous;
	}

	public List<CourrierConsulterInformations> getCopyListDossiersTous() {
		return copyListDossiersTous;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setListCourriesrAffectes(List<CourrierDossierListe> listCourriesrAffectes) {
		this.listCourriesrAffectes = listCourriesrAffectes;
	}

	public List<CourrierDossierListe> getListCourriesrAffectes() {
		return listCourriesrAffectes;
	}

	public void setCourrierDossierListe(CourrierDossierListe courrierDossierListe) {
		this.courrierDossierListe = courrierDossierListe;
	}

	public CourrierDossierListe getCourrierDossierListe() {
		return courrierDossierListe;
	}

	public void setDirContext(DirContext dirContext) {
		this.dirContext = dirContext;
	}

	public DirContext getDirContext() {
		return dirContext;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getLocale() {
		return locale;
	}

	public void setSociete(Societe societe) {
		this.societe = societe;
	}

	public Societe getSociete() {
		return societe;
	}

	public void setPathJasper(String pathJasper) {
		this.pathJasper = pathJasper;
	}

	public String getPathJasper() {
		ExternalContext jsfContext = FacesContext.getCurrentInstance().getExternalContext();
		ServletContext servletContext = (ServletContext) jsfContext.getContext();

		String webContentRoot = servletContext.getRealPath("/");
		// String res = webContentRoot.substring(0, 16) + "GBO_v1.k";
		// webContentRoot = res;
		// Specify compiled jasper file(.jasper)
		String pathJ = webContentRoot + "/WEB-INF//rapports//";
		pathJasper = pathJ;
		return pathJasper;
	}

	public void setMail(Mail mail) {
		this.mail = mail;
	}

	public Mail getMail() {
		return mail;
	}

	public void setGedUtils(GedUtils gedUtils) {
		this.gedUtils = gedUtils;
	}

	public GedUtils getGedUtils() {
		return gedUtils;
	}

	public void setFax(Fax fax) {
		this.fax = fax;
	}

	public Fax getFax() {
		return fax;
	}

	public void setInTransfertBean(boolean inTransfertBean) {
		this.inTransfertBean = inTransfertBean;
	}

	public boolean isInTransfertBean() {
		return inTransfertBean;
	}

	public void setArmoire(Classement_archivage_niveau_02 armoire) {
		this.armoire = armoire;
	}

	public Classement_archivage_niveau_02 getArmoire() {
		return armoire;
	}

	public void setEtages(Classement_archivage_niveau_01 etages) {
		this.etages = etages;
	}

	public Classement_archivage_niveau_01 getEtages() {
		return etages;
	}

	public List<Object> getCopyListSelectedObjectExp() {
		return copyListSelectedObjectExp;
	}

	public void setCopyListSelectedObjectExp(List<Object> copyListSelectedObjectExp) {
		this.copyListSelectedObjectExp = copyListSelectedObjectExp;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersDeValidation() {
		return copyListCourriersDeValidation;
	}

	public void setCopyListCourriersDeValidation(
			List<CourrierConsulterInformations> copyListCourriersDeValidation) {
		this.copyListCourriersDeValidation = copyListCourriersDeValidation;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersTraite() {
		return copyListCourriersTraite;
	}

	public void setCopyListCourriersTraite(
			List<CourrierConsulterInformations> copyListCourriersTraite) {
		this.copyListCourriersTraite = copyListCourriersTraite;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersAValider() {
		return copyListCourriersAValider;
	}

	public void setCopyListCourriersAValider(
			List<CourrierConsulterInformations> copyListCourriersAValider) {
		this.copyListCourriersAValider = copyListCourriersAValider;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersValide() {
		return copyListCourriersValide;
	}

	public void setCopyListCourriersValide(
			List<CourrierConsulterInformations> copyListCourriersValide) {
		this.copyListCourriersValide = copyListCourriersValide;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersNonValide() {
		return copyListCourriersNonValide;
	}

	public void setCopyListCourriersNonValide(
			List<CourrierConsulterInformations> copyListCourriersNonValide) {
		this.copyListCourriersNonValide = copyListCourriersNonValide;
	}

	public RechercheMulticritereModel getRechercheMulticritere() {
		return rechercheMulticritere;
	}

	public void setRechercheMulticritere(RechercheMulticritereModel rechercheMulticritere) {
		this.rechercheMulticritere = rechercheMulticritere;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersTousBoc() {
		return copyListCourriersTousBoc;
	}

	public void setCopyListCourriersTousBoc(
			List<CourrierConsulterInformations> copyListCourriersTousBoc) {
		this.copyListCourriersTousBoc = copyListCourriersTousBoc;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersTousBocTraite() {
		return copyListCourriersTousBocTraite;
	}

	public void setCopyListCourriersTousBocTraite(
			List<CourrierConsulterInformations> copyListCourriersTousBocTraite) {
		this.copyListCourriersTousBocTraite = copyListCourriersTousBocTraite;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersTousBocNonTraite() {
		return copyListCourriersTousBocNonTraite;
	}

	public void setCopyListCourriersTousBocNonTraite(
			List<CourrierConsulterInformations> copyListCourriersTousBocNonTraite) {
		this.copyListCourriersTousBocNonTraite = copyListCourriersTousBocNonTraite;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersTousBocArrive() {
		return copyListCourriersTousBocArrive;
	}

	public void setCopyListCourriersTousBocArrive(
			List<CourrierConsulterInformations> copyListCourriersTousBocArrive) {
		this.copyListCourriersTousBocArrive = copyListCourriersTousBocArrive;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersTousBocArriveTraite() {
		return copyListCourriersTousBocArriveTraite;
	}

	public void setCopyListCourriersTousBocArriveTraite(
			List<CourrierConsulterInformations> copyListCourriersTousBocArriveTraite) {
		this.copyListCourriersTousBocArriveTraite = copyListCourriersTousBocArriveTraite;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersTousBocArriveNonTraite() {
		return copyListCourriersTousBocArriveNonTraite;
	}

	public void setCopyListCourriersTousBocArriveNonTraite(
			List<CourrierConsulterInformations> copyListCourriersTousBocArriveNonTraite) {
		this.copyListCourriersTousBocArriveNonTraite = copyListCourriersTousBocArriveNonTraite;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersTousBocDepart() {
		return copyListCourriersTousBocDepart;
	}

	public void setCopyListCourriersTousBocDepart(
			List<CourrierConsulterInformations> copyListCourriersTousBocDepart) {
		this.copyListCourriersTousBocDepart = copyListCourriersTousBocDepart;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersTousBocDepartTraite() {
		return copyListCourriersTousBocDepartTraite;
	}

	public void setCopyListCourriersTousBocDepartTraite(
			List<CourrierConsulterInformations> copyListCourriersTousBocDepartTraite) {
		this.copyListCourriersTousBocDepartTraite = copyListCourriersTousBocDepartTraite;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersTousBocDepartNonTraite() {
		return copyListCourriersTousBocDepartNonTraite;
	}

	public void setCopyListCourriersTousBocDepartNonTraite(
			List<CourrierConsulterInformations> copyListCourriersTousBocDepartNonTraite) {
		this.copyListCourriersTousBocDepartNonTraite = copyListCourriersTousBocDepartNonTraite;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersMailBoc() {
		return copyListCourriersMailBoc;
	}

	public void setCopyListCourriersMailBoc(
			List<CourrierConsulterInformations> copyListCourriersMailBoc) {
		this.copyListCourriersMailBoc = copyListCourriersMailBoc;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersMailBocTraite() {
		return copyListCourriersMailBocTraite;
	}

	public void setCopyListCourriersMailBocTraite(
			List<CourrierConsulterInformations> copyListCourriersMailBocTraite) {
		this.copyListCourriersMailBocTraite = copyListCourriersMailBocTraite;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersMailBocNonTraite() {
		return copyListCourriersMailBocNonTraite;
	}

	public void setCopyListCourriersMailBocNonTraite(
			List<CourrierConsulterInformations> copyListCourriersMailBocNonTraite) {
		this.copyListCourriersMailBocNonTraite = copyListCourriersMailBocNonTraite;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersMailBocArrive() {
		return copyListCourriersMailBocArrive;
	}

	public void setCopyListCourriersMailBocArrive(
			List<CourrierConsulterInformations> copyListCourriersMailBocArrive) {
		this.copyListCourriersMailBocArrive = copyListCourriersMailBocArrive;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersMailBocArriveTraite() {
		return copyListCourriersMailBocArriveTraite;
	}

	public void setCopyListCourriersMailBocArriveTraite(
			List<CourrierConsulterInformations> copyListCourriersMailBocArriveTraite) {
		this.copyListCourriersMailBocArriveTraite = copyListCourriersMailBocArriveTraite;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersMailBocArriveNonTraite() {
		return copyListCourriersMailBocArriveNonTraite;
	}

	public void setCopyListCourriersMailBocArriveNonTraite(
			List<CourrierConsulterInformations> copyListCourriersMailBocArriveNonTraite) {
		this.copyListCourriersMailBocArriveNonTraite = copyListCourriersMailBocArriveNonTraite;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersMailBocDepart() {
		return copyListCourriersMailBocDepart;
	}

	public void setCopyListCourriersMailBocDepart(
			List<CourrierConsulterInformations> copyListCourriersMailBocDepart) {
		this.copyListCourriersMailBocDepart = copyListCourriersMailBocDepart;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersMailBocDepartTraite() {
		return copyListCourriersMailBocDepartTraite;
	}

	public void setCopyListCourriersMailBocDepartTraite(
			List<CourrierConsulterInformations> copyListCourriersMailBocDepartTraite) {
		this.copyListCourriersMailBocDepartTraite = copyListCourriersMailBocDepartTraite;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersMailBocDepartNonTraite() {
		return copyListCourriersMailBocDepartNonTraite;
	}

	public void setCopyListCourriersMailBocDepartNonTraite(
			List<CourrierConsulterInformations> copyListCourriersMailBocDepartNonTraite) {
		this.copyListCourriersMailBocDepartNonTraite = copyListCourriersMailBocDepartNonTraite;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersFaxBoc() {
		return copyListCourriersFaxBoc;
	}

	public void setCopyListCourriersFaxBoc(
			List<CourrierConsulterInformations> copyListCourriersFaxBoc) {
		this.copyListCourriersFaxBoc = copyListCourriersFaxBoc;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersFaxBocTraite() {
		return copyListCourriersFaxBocTraite;
	}

	public void setCopyListCourriersFaxBocTraite(
			List<CourrierConsulterInformations> copyListCourriersFaxBocTraite) {
		this.copyListCourriersFaxBocTraite = copyListCourriersFaxBocTraite;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersFaxBocNonTraite() {
		return copyListCourriersFaxBocNonTraite;
	}

	public void setCopyListCourriersFaxBocNonTraite(
			List<CourrierConsulterInformations> copyListCourriersFaxBocNonTraite) {
		this.copyListCourriersFaxBocNonTraite = copyListCourriersFaxBocNonTraite;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersFaxBocArrive() {
		return copyListCourriersFaxBocArrive;
	}

	public void setCopyListCourriersFaxBocArrive(
			List<CourrierConsulterInformations> copyListCourriersFaxBocArrive) {
		this.copyListCourriersFaxBocArrive = copyListCourriersFaxBocArrive;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersFaxBocArriveTraite() {
		return copyListCourriersFaxBocArriveTraite;
	}

	public void setCopyListCourriersFaxBocArriveTraite(
			List<CourrierConsulterInformations> copyListCourriersFaxBocArriveTraite) {
		this.copyListCourriersFaxBocArriveTraite = copyListCourriersFaxBocArriveTraite;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersFaxBocArriveNonTraite() {
		return copyListCourriersFaxBocArriveNonTraite;
	}

	public void setCopyListCourriersFaxBocArriveNonTraite(
			List<CourrierConsulterInformations> copyListCourriersFaxBocArriveNonTraite) {
		this.copyListCourriersFaxBocArriveNonTraite = copyListCourriersFaxBocArriveNonTraite;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersFaxBocDepart() {
		return copyListCourriersFaxBocDepart;
	}

	public void setCopyListCourriersFaxBocDepart(
			List<CourrierConsulterInformations> copyListCourriersFaxBocDepart) {
		this.copyListCourriersFaxBocDepart = copyListCourriersFaxBocDepart;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersFaxBocDepartTraite() {
		return copyListCourriersFaxBocDepartTraite;
	}

	public void setCopyListCourriersFaxBocDepartTraite(
			List<CourrierConsulterInformations> copyListCourriersFaxBocDepartTraite) {
		this.copyListCourriersFaxBocDepartTraite = copyListCourriersFaxBocDepartTraite;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersFaxBocDepartNonTraite() {
		return copyListCourriersFaxBocDepartNonTraite;
	}

	public void setCopyListCourriersFaxBocDepartNonTraite(
			List<CourrierConsulterInformations> copyListCourriersFaxBocDepartNonTraite) {
		this.copyListCourriersFaxBocDepartNonTraite = copyListCourriersFaxBocDepartNonTraite;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersAutreBoc() {
		return copyListCourriersAutreBoc;
	}

	public void setCopyListCourriersAutreBoc(
			List<CourrierConsulterInformations> copyListCourriersAutreBoc) {
		this.copyListCourriersAutreBoc = copyListCourriersAutreBoc;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersAutreBocTraite() {
		return copyListCourriersAutreBocTraite;
	}

	public void setCopyListCourriersAutreBocTraite(
			List<CourrierConsulterInformations> copyListCourriersAutreBocTraite) {
		this.copyListCourriersAutreBocTraite = copyListCourriersAutreBocTraite;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersAutreBocNonTraite() {
		return copyListCourriersAutreBocNonTraite;
	}

	public void setCopyListCourriersAutreBocNonTraite(
			List<CourrierConsulterInformations> copyListCourriersAutreBocNonTraite) {
		this.copyListCourriersAutreBocNonTraite = copyListCourriersAutreBocNonTraite;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersAutreBocArrive() {
		return copyListCourriersAutreBocArrive;
	}

	public void setCopyListCourriersAutreBocArrive(
			List<CourrierConsulterInformations> copyListCourriersAutreBocArrive) {
		this.copyListCourriersAutreBocArrive = copyListCourriersAutreBocArrive;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersAutreBocArriveTraite() {
		return copyListCourriersAutreBocArriveTraite;
	}

	public void setCopyListCourriersAutreBocArriveTraite(
			List<CourrierConsulterInformations> copyListCourriersAutreBocArriveTraite) {
		this.copyListCourriersAutreBocArriveTraite = copyListCourriersAutreBocArriveTraite;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersAutreBocArriveNonTraite() {
		return copyListCourriersAutreBocArriveNonTraite;
	}

	public void setCopyListCourriersAutreBocArriveNonTraite(
			List<CourrierConsulterInformations> copyListCourriersAutreBocArriveNonTraite) {
		this.copyListCourriersAutreBocArriveNonTraite = copyListCourriersAutreBocArriveNonTraite;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersAutreBocDepart() {
		return copyListCourriersAutreBocDepart;
	}

	public void setCopyListCourriersAutreBocDepart(
			List<CourrierConsulterInformations> copyListCourriersAutreBocDepart) {
		this.copyListCourriersAutreBocDepart = copyListCourriersAutreBocDepart;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersAutreBocDepartTraite() {
		return copyListCourriersAutreBocDepartTraite;
	}

	public void setCopyListCourriersAutreBocDepartTraite(
			List<CourrierConsulterInformations> copyListCourriersAutreBocDepartTraite) {
		this.copyListCourriersAutreBocDepartTraite = copyListCourriersAutreBocDepartTraite;
	}

	public List<CourrierConsulterInformations> getCopyListCourriersAutreBocDepartNonTraite() {
		return copyListCourriersAutreBocDepartNonTraite;
	}

	public void setCopyListCourriersAutreBocDepartNonTraite(
			List<CourrierConsulterInformations> copyListCourriersAutreBocDepartNonTraite) {
		this.copyListCourriersAutreBocDepartNonTraite = copyListCourriersAutreBocDepartNonTraite;
	}

	public void setRole(List<String> role) {
		this.role = role;
	}

	public List<String> getRole() {
		return role;
	}

	public void setTransactionDestination(TransactionDestination transactionDestination) {
		this.transactionDestination = transactionDestination;
	}

	public TransactionDestination getTransactionDestination() {
		return transactionDestination;
	}

	public void setUser(Person user) {
		this.user = user;
	}

	public Person getUser() {
		return user;
	}

	public void setShowMonitoringButtonForDest(boolean showMonitoringButtonForDest) {
		this.showMonitoringButtonForDest = showMonitoringButtonForDest;
	}

	public boolean isShowMonitoringButtonForDest() {
		return showMonitoringButtonForDest;
	}

	public void setLinkedCourrier(Courrier linkedCourrier) {
		this.linkedCourrier = linkedCourrier;
	}

	public Courrier getLinkedCourrier() {
		return linkedCourrier;
	}

	public Xtelog getLog() {
		return log;
	}

	public void setLog(Xtelog log) {
		this.log = log;
	}

	public Trace getTrace() {
		return trace;
	}

	public void setTrace(Trace trace) {
		this.trace = trace;
	}

	public Evenement getEvenement() {
		return evenement;
	}

	public void setEvenement(Evenement evenement) {
		this.evenement = evenement;
	}

	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}

	public List<Notification> getListNotification() {
		return listNotification;
	}

	public void setListNotification(List<Notification> listNotification) {
		this.listNotification = listNotification;
	}

	public void setCopyListSelectedPersonNotif(List<Person> copyListSelectedPersonNotif) {
		this.copyListSelectedPersonNotif = copyListSelectedPersonNotif;
	}

	public List<Person> getCopyListSelectedPersonNotif() {
		return copyListSelectedPersonNotif;
	}

	public void setCopyListSelectedUnitNotif(List<Unit> copyListSelectedUnitNotif) {
		this.copyListSelectedUnitNotif = copyListSelectedUnitNotif;
	}

	public List<Unit> getCopyListSelectedUnitNotif() {
		return copyListSelectedUnitNotif;
	}

	public void setCopyListPPNotif(List<Pp> copyListPPNotif) {
		this.copyListPPNotif = copyListPPNotif;
	}

	public List<Pp> getCopyListPPNotif() {
		return copyListPPNotif;
	}

	public void setCopyListPMNotif(List<Pm> copyListPMNotif) {
		this.copyListPMNotif = copyListPMNotif;
	}

	public List<Pm> getCopyListPMNotif() {
		return copyListPMNotif;
	}

	public void setListSelectedAnnotations(List<String> listSelectedAnnotations) {
		this.listSelectedAnnotations = listSelectedAnnotations;
	}

	public List<String> getListSelectedAnnotations() {
		return listSelectedAnnotations;
	}

	public void setListCourriersAffectes(List<CourrierDossierListe> listCourriersAffectes) {
		this.listCourriersAffectes = listCourriersAffectes;
	}

	public List<CourrierDossierListe> getListCourriersAffectes() {
		return listCourriersAffectes;
	}

//	public void setSuiviCourrierCourrier(SuiviCourrierCourrier suiviCourrierCourrier) {
//		this.suiviCourrierCourrier = suiviCourrierCourrier;
//	}
//
//	public SuiviCourrierCourrier getSuiviCourrierCourrier() {
//		return suiviCourrierCourrier;
//	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUnit(int idUnit) {
		this.idUnit = idUnit;
	}

	public int getIdUnit() {
		return idUnit;
	}

	public void setRechercheUnitModel(RechercheUnitModel rechercheUnitModel) {
		this.rechercheUnitModel = rechercheUnitModel;
	}

	public RechercheUnitModel getRechercheUnitModel() {
		return rechercheUnitModel;
	}

	public void setRechercheUserModel(RechercheUserModel rechercheUserModel) {
		this.rechercheUserModel = rechercheUserModel;
	}

	public RechercheUserModel getRechercheUserModel() {
		return rechercheUserModel;
	}

	public void setListUser(List<RechercheUserModel> listUser) {
		this.listUser = listUser;
	}

	public List<RechercheUserModel> getListUser() {
		return listUser;
	}

	public void setListUnit(List<RechercheUnitModel> listUnit) {
		this.listUnit = listUnit;
	}

	public List<RechercheUnitModel> getListUnit() {
		return listUnit;
	}

	public void setOpenedSTPannel(boolean openedSTPannel) {
		this.openedSTPannel = openedSTPannel;
	}

	public boolean isOpenedSTPannel() {
		return openedSTPannel;
	}

	public void setTypeIntervenant(String typeIntervenant) {
		this.typeIntervenant = typeIntervenant;
	}

	public String getTypeIntervenant() {
		return typeIntervenant;
	}

	public void setRedirectUser(String redirectUser) {
		this.redirectUser = redirectUser;
	}

	public String getRedirectUser() {
		return redirectUser;
	}

	public Courrier getNouveauCourrier() {
		return nouveauCourrier;
	}

	public void setNouveauCourrier(Courrier nouveauCourrier) {
		this.nouveauCourrier = nouveauCourrier;
	}

	public String getCopySelectedItemNature() {
		return copySelectedItemNature;
	}

	public void setCopySelectedItemNature(String copySelectedItemNature) {
		this.copySelectedItemNature = copySelectedItemNature;
	}

	public String getCopySelectedItemConf() {
		return copySelectedItemConf;
	}

	public void setCopySelectedItemConf(String copySelectedItemConf) {
		this.copySelectedItemConf = copySelectedItemConf;
	}

	public String getCopySelectedItemUg() {
		return copySelectedItemUg;
	}

	public void setCopySelectedItemUg(String copySelectedItemUg) {
		this.copySelectedItemUg = copySelectedItemUg;
	}

	public String getCopySelectedItemsTr() {
		return copySelectedItemsTr;
	}

	public void setCopySelectedItemsTr(String copySelectedItemsTr) {
		this.copySelectedItemsTr = copySelectedItemsTr;
	}

	public List<String> getCopySelectedItemsAnnotation() {
		return copySelectedItemsAnnotation;
	}

	public void setCopySelectedItemsAnnotation(List<String> copySelectedItemsAnnotation) {
		this.copySelectedItemsAnnotation = copySelectedItemsAnnotation;
	}

	public String getNecessiteReponse() {
		return necessiteReponse;
	}

	public void setNecessiteReponse(String necessiteReponse) {
		this.necessiteReponse = necessiteReponse;
	}

	public String getCopyTypeExpediteur() {
		return copyTypeExpediteur;
	}

	public void setCopyTypeExpediteur(String copyTypeExpediteur) {
		this.copyTypeExpediteur = copyTypeExpediteur;
	}

	public RecherchePpModel getRecherchePpModel() {
		return recherchePpModel;
	}

	public void setRecherchePpModel(RecherchePpModel recherchePpModel) {
		this.recherchePpModel = recherchePpModel;
	}

	public RecherchePmModel getRecherchePmModel() {
		return recherchePmModel;
	}

	public void setRecherchePmModel(RecherchePmModel recherchePmModel) {
		this.recherchePmModel = recherchePmModel;
	}
//
	public RecherchePpModel getRecherchePpModelForDetails() {
		return recherchePpModelForDetails;
	}

	public void setRecherchePpModelForDetails(RecherchePpModel recherchePpModelForDetails) {
		this.recherchePpModelForDetails = recherchePpModelForDetails;
	}

	public RecherchePmModel getRecherchePmModelForDetails() {
		return recherchePmModelForDetails;
	}
//
	public void setRecherchePmModelForDetails(RecherchePmModel recherchePmModelForDetails) {
		this.recherchePmModelForDetails = recherchePmModelForDetails;
	}

	public List<RecherchePpModel> getListPp() {
		return listPp;
	}

	public void setListPp(List<RecherchePpModel> listPp) {
		this.listPp = listPp;
	}
//
	public List<RecherchePmModel> getListPm() {
		return listPm;
	}

	public void setListPm(List<RecherchePmModel> listPm) {
		this.listPm = listPm;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Message getMessage() {
		return message;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Group getGroup() {
		return group;
	}

	public void setUserAffectedToUnit(boolean userAffectedToUnit) {
		this.userAffectedToUnit = userAffectedToUnit;
	}

	public boolean isUserAffectedToUnit() {
		return userAffectedToUnit;
	}

//	public void setRoleModel(RoleModel roleModel) {
//		this.roleModel = roleModel;
//	}
//
//	public RoleModel getRoleModel() {
//		return roleModel;
//	}

	public void setGroupecontact(Groupecontact groupecontact) {
		this.groupecontact = groupecontact;
	}

	public Groupecontact getGroupecontact() {
		return groupecontact;
	}

	public void setSujetmailing(Sujetmailing sujetmailing) {
		this.sujetmailing = sujetmailing;
	}

	public Sujetmailing getSujetmailing() {
		return sujetmailing;
	}

	public void setDatesSauvgardes(List<Date> datesSauvgardes) {
		this.datesSauvgardes = datesSauvgardes;
	}

	public List<Date> getDatesSauvgardes() {
		return datesSauvgardes;
	}

	public void setInRestoration(boolean inRestoration) {
		this.inRestoration = inRestoration;
	}

	public boolean isInRestoration() {
		return inRestoration;
	}

	public FaxMail getFaxMail() {
		return faxMail;
	}

	public void setFaxMail(FaxMail faxMail) {
		this.faxMail = faxMail;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public List<CourrierConsulterInformations> getListCourrierForLiens() {
		return listCourrierForLiens;
	}

	public void setListCourrierForLiens(List<CourrierConsulterInformations> listCourrierForLiens) {
		this.listCourrierForLiens = listCourrierForLiens;
	}

	public String getTransmissionCourrierJourForRapport() {
		return transmissionCourrierJourForRapport;
	}

	public void setTransmissionCourrierJourForRapport(String transmissionCourrierJourForRapport) {
		//System.out.println("KHA : transmissionCourrierJourForRapport= "+transmissionCourrierJourForRapport);
		this.transmissionCourrierJourForRapport = transmissionCourrierJourForRapport;
	}

	public String getTypeCourrierTraitementJourForRapport() {
		return typeCourrierTraitementJourForRapport;
	}

	public void setTypeCourrierTraitementJourForRapport(String typeCourrierTraitementJourForRapport) {
		this.typeCourrierTraitementJourForRapport = typeCourrierTraitementJourForRapport;
	}

	public String getCategorieCourrierJourForRapport() {
		return categorieCourrierJourForRapport;
	}

	public void setCategorieCourrierJourForRapport(String categorieCourrierJourForRapport) {
		this.categorieCourrierJourForRapport = categorieCourrierJourForRapport;
	}

	public String getTypeCourrierJourForRapport() {
		return typeCourrierJourForRapport;
	}

	public void setTypeCourrierJourForRapport(String typeCourrierJourForRapport) {
		this.typeCourrierJourForRapport = typeCourrierJourForRapport;
	}

	public String getTypeCourrierValidationJourForRapport() {
		return typeCourrierValidationJourForRapport;
	}

	public void setTypeCourrierValidationJourForRapport(String typeCourrierValidationJourForRapport) {
		this.typeCourrierValidationJourForRapport = typeCourrierValidationJourForRapport;
	}

	public String getTypeCourrierJourForRapportAncien() {
		return typeCourrierJourForRapportAncien;
	}

	public void setTypeCourrierJourForRapportAncien(String typeCourrierJourForRapportAncien) {
		this.typeCourrierJourForRapportAncien = typeCourrierJourForRapportAncien;
	}

	public String getTypeCourrierValidationJourForRapportAncien() {
		return typeCourrierValidationJourForRapportAncien;
	}

	public void setTypeCourrierValidationJourForRapportAncien(
			String typeCourrierValidationJourForRapportAncien) {
		this.typeCourrierValidationJourForRapportAncien = typeCourrierValidationJourForRapportAncien;
	}

	public String getTransmissionCourrierJourForRapportAncien() {
		return transmissionCourrierJourForRapportAncien;
	}

	public void setTransmissionCourrierJourForRapportAncien(
			String transmissionCourrierJourForRapportAncien) {
		this.transmissionCourrierJourForRapportAncien = transmissionCourrierJourForRapportAncien;
	}

	public String getTypeCourrierTraitementJourForRapportAncien() {
		return typeCourrierTraitementJourForRapportAncien;
	}

	public void setTypeCourrierTraitementJourForRapportAncien(
			String typeCourrierTraitementJourForRapportAncien) {
		this.typeCourrierTraitementJourForRapportAncien = typeCourrierTraitementJourForRapportAncien;
	}

	public String getCategorieCourrierJourForRapportAncien() {
		return categorieCourrierJourForRapportAncien;
	}

	public void setCategorieCourrierJourForRapportAncien(
			String categorieCourrierJourForRapportAncien) {
		this.categorieCourrierJourForRapportAncien = categorieCourrierJourForRapportAncien;
	}

	public String getCourrierRubrique() {
		return courrierRubrique;
	}

	public void setCourrierRubrique(String courrierRubrique) {
		this.courrierRubrique = courrierRubrique;
	}

	public String getCourrierRubriqueJour() {
		return courrierRubriqueJour;
	}

	public void setCourrierRubriqueJour(String courrierRubriqueJour) {
		this.courrierRubriqueJour = courrierRubriqueJour;
	}

	public Courrier getCourrierTempValue() {
		return courrierTempValue;
	}

	public void setCourrierTempValue(Courrier courrierTempValue) {
		this.courrierTempValue = courrierTempValue;
	}

	public List<String> getSelectedAnnotationItems() {
		return selectedAnnotationItems;
	}

	public void setSelectedAnnotationItems(List<String> selectedAnnotationItems) {
		this.selectedAnnotationItems = selectedAnnotationItems;
	}

	public String getOtherAnnotation() {
		return otherAnnotation;
	}

	public void setOtherAnnotation(String otherAnnotation) {
		this.otherAnnotation = otherAnnotation;
	}

	public String getChooseAnnotation() {
		return chooseAnnotation;
	}

	public void setChooseAnnotation(String chooseAnnotation) {
		this.chooseAnnotation = chooseAnnotation;
	}

	public String getTypeSender() {
		return typeSender;
	}

	public void setTypeSender(String typeSender) {
		this.typeSender = typeSender;
	}

	public String getTypeCourrier() {
		return typeCourrier;
	}

	public void setTypeCourrier(String typeCourrier) {
		this.typeCourrier = typeCourrier;
	}

	public RechercheMulticritereModel getRecherche() {
		return recherche;
	}

	public void setRecherche(RechercheMulticritereModel recherche) {
		this.recherche = recherche;
	}

	public HashMap<Integer, Person> getHashMapAllUser() {
		return hashMapAllUser;
	}

	public void setHashMapAllUser(HashMap<Integer, Person> hashMapAllUser) {
		this.hashMapAllUser = hashMapAllUser;
	}

	public HashMap<Integer, Unit> getHashMapUnit() {
		return hashMapUnit;
	}

	public void setHashMapUnit(HashMap<Integer, Unit> hashMapUnit) {
		this.hashMapUnit = hashMapUnit;
	}

	public String getTypeDateRechMulti() {
		return typeDateRechMulti;
	}

	public void setTypeDateRechMulti(String typeDateRechMulti) {
		this.typeDateRechMulti = typeDateRechMulti;
	}
	
	public String getDestinataireReel() {
		return destinataireReel;
	}

	public void setDestinataireReel(String destinataireReel) {
		this.destinataireReel = destinataireReel;
	}

	public String getSelectedListCourrier() {
		return selectedListCourrier;
	}

	public void setSelectedListCourrier(String selectedListCourrier) {
		this.selectedListCourrier = selectedListCourrier;
	}

	public String getContactPays() {
		return contactPays;
	}

	public void setContactPays(String contactPays) {
		this.contactPays = contactPays;
	}

	public String getActiveCTab() {
		return activeCTab;
	}

	public void setActiveCTab(String activeCTab) {
		this.activeCTab = activeCTab;
	}

	public LdapOperation getLdapOperation() {
		if(ldapOperation == null){
			ldapOperation = new LdapOperation();
		}
		return ldapOperation;
	}

	public void setLdapOperation(LdapOperation ldapOperation) {
		this.ldapOperation = ldapOperation;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public HashMap<String, Object> getFilterMap() {
		return filterMap;
	}

	public void setFilterMap(HashMap<String, Object> filterMap) {
		this.filterMap = filterMap;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public boolean isDescending() {
		return descending;
	}

	public void setDescending(boolean descending) {
		this.descending = descending;
	}

	public Integer getFirstPageJour() {
		return firstPageJour;
	}

	public void setFirstPageJour(Integer firstPageJour) {
		this.firstPageJour = firstPageJour;
	}

	public Integer getFirstPageAncien() {
		return firstPageAncien;
	}

	public void setFirstPageAncien(Integer firstPageAncien) {
		this.firstPageAncien = firstPageAncien;
	}

	public boolean isOldPage() {
		return oldPage;
	}

	public void setOldPage(boolean oldPage) {
		this.oldPage = oldPage;
	}

	public Integer getFirstPageDJour() {
		return firstPageDJour;
	}

	public void setFirstPageDJour(Integer firstPageDJour) {
		this.firstPageDJour = firstPageDJour;
	}

	public Integer getFirstPageDAncien() {
		return firstPageDAncien;
	}

	public void setFirstPageDAncien(Integer firstPageDAncien) {
		this.firstPageDAncien = firstPageDAncien;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public Integer getRechercheRowCount() {
		return rechercheRowCount;
	}

	public void setRechercheRowCount(Integer rechercheRowCount) {
		this.rechercheRowCount = rechercheRowCount;
	}

	public boolean isRechercheCountVisited() {
		return rechercheCountVisited;
	}

	public void setRechercheCountVisited(boolean rechercheCountVisited) {
		this.rechercheCountVisited = rechercheCountVisited;
	}

	public Integer getFirstPageMois() {
		return firstPageMois;
	}

	public void setFirstPageMois(Integer firstPageMois) {
		this.firstPageMois = firstPageMois;
	}

	public Integer getFirstPageAnnee() {
		return firstPageAnnee;
	}

	public void setFirstPageAnnee(Integer firstPageAnnee) {
		this.firstPageAnnee = firstPageAnnee;
	}

	public Integer getNumRowCourrier() {
		return numRowCourrier;
	}

	public void setNumRowCourrier(Integer numRowCourrier) {
		this.numRowCourrier = numRowCourrier;
	}

	public Integer getCourrierExecuter() {
		return courrierExecuter;
	}

	public void setCourrierExecuter(Integer courrierExecuter) {
		this.courrierExecuter = courrierExecuter;
	}

	public List<CourrierInformations> getListCourriers() {
		return listCourriers;
	}

	public void setListCourriers(List<CourrierInformations> listCourriers) {
		this.listCourriers = listCourriers;
	}

	public String getCourrierId() {
		return courrierId;
	}

	public void setCourrierId(String courrierId) {
		this.courrierId = courrierId;
	}

	public Courrier getCourrierTest() {
		return courrierTest;
	}

	public void setCourrierTest(Courrier courrierTest) {
		this.courrierTest = courrierTest;
	}

	public String getCourrierRef() {
		return courrierRef;
	}

	public void setCourrierRef(String courrierRef) {
		this.courrierRef = courrierRef;
	}

	public String getExpditeurUnite() {
		return expditeurUnite;
	}

	public void setExpditeurUnite(String expditeurUnite) {
		this.expditeurUnite = expditeurUnite;
	}

	public Person getPersonCodeUnique() {
		return personCodeUnique;
	}

	public void setPersonCodeUnique(Person personCodeUnique) {
		this.personCodeUnique = personCodeUnique;
	}
	public void resetVG() {
		 setCopyListSelectedPerson(new ArrayList<Person>());
		 setCopyListPP(new ArrayList<Pp>());
		 setCopyListPM(new ArrayList<Pm>());
		 setListSelectedItem(new ArrayList<ItemSelected>());
		 setCopyListSelectedBoc(new ArrayList<BOC>());
		 setCopyListSelectedUnit(new ArrayList<Unit>());
		 setCopyListSelectedObject(new ArrayList<Object>());
		 setCopyListSelectedObjectExp(new ArrayList<Object>());
		 setDestNom(null);
		 setExpNom(null);
		 setCourrierTempValue(null);
		 setSelectedAnnotationItems(null);
		 setOtherAnnotation("");
		 setChooseAnnotation("tous");
		 setTypeSender("MonUnite");
		 setTypeCourrier("arrive");
	}
	public String getChangerDoc() {
		return changerDoc;
	}

	public void setChangerDoc(String changerDoc) {
		this.changerDoc = changerDoc;
	}

	public void setGedStatus(String gedStatus) {
		this.gedStatus = gedStatus;
	}

	public String getGedStatus() {
		if (appMgr.listVariablesByLibelle("ged_status").size()==0 || appMgr.listVariablesByLibelle("ged_status").get(0).getVaraiablesValeur()=="true") {
			
			//Si la variable ne correspond pas é l'id 26, elle sera initialisée par "true" 
			return "true";
			
		} else {
			
			//Sinon récupérer la valeur de la variable qui est "false"
			return appMgr.listVariablesByLibelle("ged_status").get(0).getVaraiablesValeur();
			
		}
	}

	public void setRendered(String rendered) {
		
		this.rendered = rendered;
	}

	public String getRendered() {
		if (appMgr.listVariablesByLibelle("rendered_button").size()==0 || appMgr.listVariablesByLibelle("rendered_button").get(0).getVaraiablesValeur()=="true") {
			//Si la variable ne correspond pas é l'id 26, elle sera initialisée par "true" 
			return "true";
			
		} else {
			//Sinon récupérer la valeur de la variable qui est "false"
			return appMgr.listVariablesByLibelle("rendered_button").get(0).getVaraiablesValeur();
			 
		}
		
	}

	public void setNatureCategorie(NatureCategorie natureCategorie) {
		this.natureCategorie = natureCategorie;
	}

	public NatureCategorie getNatureCategorie() {
		return natureCategorie;
	}


	public Etat getTransactionDestEtatReceptionPhysique() {
		return transactionDestEtatReceptionPhysique;
	}

	public void setTransactionDestEtatReceptionPhysique(
			Etat transactionDestEtatReceptionPhysique) {
		this.transactionDestEtatReceptionPhysique = transactionDestEtatReceptionPhysique;
	}

	public String getTransactionDestCommentaireReceptionPhysique() {
		return transactionDestCommentaireReceptionPhysique;
	}

	public void setTransactionDestCommentaireReceptionPhysique(
			String transactionDestCommentaireReceptionPhysique) {
		this.transactionDestCommentaireReceptionPhysique = transactionDestCommentaireReceptionPhysique;
	}

	public Date getTransactionDestDateReceptionPhysique() {
		return transactionDestDateReceptionPhysique;
	}

	public void setTransactionDestDateReceptionPhysique(
			Date transactionDestDateReceptionPhysique) {
		this.transactionDestDateReceptionPhysique = transactionDestDateReceptionPhysique;
	}

	public TransactionDocument getTransactionDocument() {
		return transactionDocument;
	}

	public void setTransactionDocument(TransactionDocument transactionDocument) {
		this.transactionDocument = transactionDocument;
	}
	

	public void setLocalFr(String localFr) {
		this.localFr = localFr;
	}

	public String getLocalFr() {
		return localFr;
	}

	public boolean isRoleBordereau() {
		return roleBordereau;
	}

	public void setRoleBordereau(boolean roleBordereau) {
		this.roleBordereau = roleBordereau;
	}

	public String getSousTitreRapportBoc() {
		return sousTitreRapportBoc;
	}

	public void setSousTitreRapportBoc(String sousTitreRapportBoc) {
		this.sousTitreRapportBoc = sousTitreRapportBoc;
	}

	public String getSousTitreRapportResponsable() {
		return sousTitreRapportResponsable;
	}


	public void setSousTitreRapportResponsable(String sousTitreRapportResponsable) {
		this.sousTitreRapportResponsable = sousTitreRapportResponsable;
	}

	public void setSousTitreRapportSecAgent(String sousTitreRapportSecAgent) {
		this.sousTitreRapportSecAgent = sousTitreRapportSecAgent;
	}

	public String getSousTitreRapportSecAgent() {
		return sousTitreRapportSecAgent;
	}


	
	public String getSousTitreRapportRelance() {
		return sousTitreRapportRelance;
	}

	public void setSousTitreRapportRelance(String sousTitreRapportRelance) {
		this.sousTitreRapportRelance = sousTitreRapportRelance;
	}

	public void setExecute(boolean execute) {
		this.execute = execute;
	}

	public boolean isExecute() {
		return execute;
	}


	public String getNatureParCategorie() {
		return natureParCategorie;
	}

	public void setNatureParCategorie(String natureParCategorie) {
		this.natureParCategorie = natureParCategorie;
	}


	public void setTopVignt(Integer topVignt) {
		this.topVignt = topVignt;
	}

	public String getCourrierCodeUnique() {
		
		return courrierCodeUnique;
	}

	public Integer getTopVignt() {
		return topVignt;
	}


	public void setCourrierCodeUnique(String courrierCodeUnique) {
		this.courrierCodeUnique = courrierCodeUnique;
	}

	public Unit getSelectedUnit() {
		return selectedUnit;
	}

	public void setSelectedUnit(Unit selectedUnit) {
		this.selectedUnit = selectedUnit;
	}

	 
	public Date getSelectedDateD() {
		return selectedDateD;
	}

	public void setSelectedDateD(Date selectedDateD) {
		this.selectedDateD = selectedDateD;
	}

	public Date getSelectedDateF() {
		return selectedDateF;
	}

	public void setSelectedDateF(Date selectedDateF) {
		this.selectedDateF = selectedDateF;
	}

	public List<Unit> getListUnitldap() {
		return listUnitldap;
	}

	public void setListUnitldap(List<Unit> listUnitldap) {
		this.listUnitldap = listUnitldap;
	}

	public ApplicationManager getAppMgr() {
		return appMgr;
	}

	public void setAppMgr(ApplicationManager appMgr) {
		this.appMgr = appMgr;
	}

	public int getIdCheque() {
		idCheque=80;
		return idCheque=80;
	}

	public void setIdCheque(int idCheque) {
		 
		this.idCheque = idCheque;
	}

	public int getIdReclammation() {
		idReclammation= 66;
		return idReclammation;
	}

	public void setIdReclammation(int idReclammation) {
		 
		this.idReclammation = idReclammation;
	}

	public int getIdRapidePoste() {
		idRapidePoste= 5;
		return idRapidePoste;
	}

	public void setIdRapidePoste(int idRapidePoste) {
	 
		
		this.idRapidePoste = idRapidePoste;
	}

	public int getIdLettreRecommandee() {
		idLettreRecommandee= 6;
		return idLettreRecommandee;
	}

	public void setIdLettreRecommandee(int idLettreRecommandee) {
		 
		this.idLettreRecommandee = idLettreRecommandee;
	}

	public String getTypeDateRapport() {
		return typeDateRapport;
	}

	public void setTypeDateRapport(String typeDateRapport) {
		this.typeDateRapport = typeDateRapport;
	}

	public int getJourOuAutr() {
		return jourOuAutr;
	}

	public void setJourOuAutr(int jourOuAutr) {
		this.jourOuAutr = jourOuAutr;
	}

	public void setAffichTempsReponse(boolean affichTempsReponse) {
		this.affichTempsReponse = affichTempsReponse;
	}

	public boolean isAffichTempsReponse() {
		return affichTempsReponse;
	}

	public void setReferenceDestinataireReel(String referenceDestinataireReel) {
		this.referenceDestinataireReel = referenceDestinataireReel;
	}

	public String getReferenceDestinataireReel() {
		return referenceDestinataireReel;
	}

	public boolean isStatusPerson() {
		return statusPerson;
	}

	public void setStatusPerson(boolean statusPerson) {
		this.statusPerson = statusPerson;
	}

	public AoConsultation getAoConsultation() {
		return aoConsultation;
	}

	public void setAoConsultation(AoConsultation aoConsultation) {
		this.aoConsultation = aoConsultation;
	}
	public CourrierInformations getCourrierInformations() {
		return courrierInformations;
	}


	public void setCourrierInformations(CourrierInformations courrierInformations) {
		this.courrierInformations = courrierInformations;
	}

	public Courrier getValise() {
		return valise;
	}

	public void setValise(Courrier valise) {
		this.valise = valise;
	}

	public boolean isFlagValise() {
		return flagValise;
	}

	public void setFlagValise(boolean flagValise) {
		this.flagValise = flagValise;
	}
	

	public void setPagePrecedente(String pagePrecedente) {
		this.pagePrecedente = pagePrecedente;
	}

	public String getPagePrecedente() {
		return pagePrecedente;
	}

	public void setPopupAffectation(boolean popupAffectation) {
		this.popupAffectation = popupAffectation;
	}

	public boolean isPopupAffectation() {
		return popupAffectation;
	}

	public void setAoConsultationId(int aoConsultationId) {
		this.aoConsultationId = aoConsultationId;
	}

	public int getAoConsultationId() {
		return aoConsultationId;
	}

	public List<Unite> getListNouvellesUnites() {
		return listNouvellesUnites;
	}

	public void setListNouvellesUnites(List<Unite> listNouvellesUnites) {
		this.listNouvellesUnites = listNouvellesUnites;
	}

	public List<Unite> getListUnitesModifiees() {
		return listUnitesModifiees;
	}

	public void setListUnitesModifiees(List<Unite> listUnitesModifiees) {
		this.listUnitesModifiees = listUnitesModifiees;
	}

	public List<BOC> getListTousBos() {
		return listTousBos;
	}

	public void setListTousBos(List<BOC> listTousBos) {
		this.listTousBos = listTousBos;
	}

	public boolean isCategorieParNature() {
		return categorieParNature;
	}

	public void setCategorieParNature(boolean categorieParNature) {
		this.categorieParNature = categorieParNature;
	}

	public void setPremiereEntreeTransfert(int premiereEntreeTransfert) {
		this.premiereEntreeTransfert = premiereEntreeTransfert;
	}

	public int getPremiereEntreeTransfert() {
		return premiereEntreeTransfert;
	}

	public int getFlagCloture() {
		return flagCloture;
	}

	public void setFlagCloture(int flagCloture) {
		this.flagCloture = flagCloture;
	}

	public boolean isFlagAjout() {
		return flagAjout;
	}

	public void setFlagAjout(boolean flagAjout) {
		this.flagAjout = flagAjout;
	}

	public String getSelectedItemCategorie() {
		return selectedItemCategorie;
	}

	public void setSelectedItemCategorie(String selectedItemCategorie) {
		this.selectedItemCategorie = selectedItemCategorie;
	}

	public int getFlagInterne() {
		return flagInterne;
	}

	public void setFlagInterne(int flagInterne) {
		this.flagInterne = flagInterne;
	}

	public List<Unit> getListTousUnit() {
		return listTousUnit;
	}

	public void setListTousUnit(List<Unit> listTousUnit) {
		this.listTousUnit = listTousUnit;
	}

	public void setAfficheLienLiees(boolean afficheLienLiees) {
		this.afficheLienLiees = afficheLienLiees;
	}

	public boolean isAfficheLienLiees() {
		return afficheLienLiees;
	}

	public void setCodeUniqueCourrier(String codeUniqueCourrier) {
		this.codeUniqueCourrier = codeUniqueCourrier;
	}

	public String getCodeUniqueCourrier() {
		return codeUniqueCourrier;
	}

	public void setAfficheTitre(String afficheTitre) {
		this.afficheTitre = afficheTitre;
	}

	public String getAfficheTitre() {
		return afficheTitre;
	}

	public void setAfficheTitreList(String afficheTitreList) {
		this.afficheTitreList = afficheTitreList;
	}

	public String getAfficheTitreList() {
		return afficheTitreList;
	}

	public List<ChequeModel> getListChequeTableau() {
		return listChequeTableau;
	}

	public void setListChequeTableau(List<ChequeModel> listChequeTableau) {
		this.listChequeTableau = listChequeTableau;
	}


	public void setShowReceptionPhysique(boolean showReceptionPhysique) {
		this.showReceptionPhysique = showReceptionPhysique;
	}

	public Boolean getFlagCheque() {
		return flagCheque;
	}

	public boolean isShowReceptionPhysique() {
		return showReceptionPhysique;
	}

	public void setFlagCheque(Boolean flagCheque) {
		this.flagCheque = flagCheque;
	}

	public List<ListeDetailsDynamique> getListeDetailsDynamiques() {
		return listeDetailsDynamiques;
	}

	public void setListeDetailsDynamiques(
			List<ListeDetailsDynamique> listeDetailsDynamiques) {
		this.listeDetailsDynamiques = listeDetailsDynamiques;
	}

	public void setModeEnveloppe(boolean modeEnveloppe) {
		this.modeEnveloppe = modeEnveloppe;
	}

	public boolean isModeEnveloppe() {
		return modeEnveloppe;
	}

	public List<ListeDetailsDynamique> getListeDetailsDynamiquesTransmission() {
		return listeDetailsDynamiquesTransmission;
	}

	public void setListeDetailsDynamiquesTransmission(
			List<ListeDetailsDynamique> listeDetailsDynamiquesTransmission) {
		this.listeDetailsDynamiquesTransmission = listeDetailsDynamiquesTransmission;
	}

	public List<ListeDetailsDynamique> getListeDetailsTransmission() {
		return listeDetailsTransmission;
	}

	public void setListeDetailsTransmission(
			List<ListeDetailsDynamique> listeDetailsTransmission) {
		this.listeDetailsTransmission = listeDetailsTransmission;
	}

	public boolean isFlagTransfert() {
		return flagTransfert;
	}

	public void setFlagTransfert(boolean flagTransfert) {
		this.flagTransfert = flagTransfert;
	}

	public void setListComposantDynamiqueTransmission(
			List<ComposantDynamique> listComposantDynamiqueTransmission) {
		this.listComposantDynamiqueTransmission = listComposantDynamiqueTransmission;
	}

	public List<ComposantDynamique> getListComposantDynamiqueTransmission() {
		return listComposantDynamiqueTransmission;
	}

	public void setDonneeSupplementaireNature(
			List<DonneeSupplementaireNature> donneeSupplementaireNature) {
		this.donneeSupplementaireNature = donneeSupplementaireNature;
	}

	public List<DonneeSupplementaireNature> getDonneeSupplementaireNature() {
		return donneeSupplementaireNature;
	}

	public void setListComposantDynamiqueNature(
			List<ComposantDynamique> listComposantDynamiqueNature) {
		this.listComposantDynamiqueNature = listComposantDynamiqueNature;
	}

	public List<ComposantDynamique> getListComposantDynamiqueNature() {
		return listComposantDynamiqueNature;
	}

	public void setListChequesSave(List<ChequeModel> listChequesSave) {
		this.listChequesSave = listChequesSave;
	}

	public List<ChequeModel> getListChequesSave() {
		return listChequesSave;
	}

	public List<CourrierInformations> getListCourriersInformationsAffecte() {
		return listCourriersInformationsAffecte;
	}

	public void setListCourriersInformationsAffecte(
			List<CourrierInformations> listCourriersInformationsAffecte) {
		this.listCourriersInformationsAffecte = listCourriersInformationsAffecte;
	}

	public void setFlagNature(int flagNature) {
		this.flagNature = flagNature;
	}

	public int getFlagNature() {
		return flagNature;
	}

	public void setFlagModif(int flagModif) {
		this.flagModif = flagModif;
	}

	public int getFlagModif() {
		return flagModif;
	}

	public void setCourrierDonneeSupplementaire(
			CourrierDonneeSupplementaire courrierDonneeSupplementaire) {
		this.courrierDonneeSupplementaire = courrierDonneeSupplementaire;
	}

	public CourrierDonneeSupplementaire getCourrierDonneeSupplementaire() {
		return courrierDonneeSupplementaire;
	}

	public void setTypeUploadFichier(boolean typeUploadFichier) {
		this.typeUploadFichier = typeUploadFichier;
	}

	public boolean isTypeUploadFichier() {
		return typeUploadFichier;
	}

	public void setCourrierRefOriginal(boolean courrierRefOriginal) {
		this.courrierRefOriginal = courrierRefOriginal;
	}

	public boolean isCourrierRefOriginal() {
		return courrierRefOriginal;
	}

	public List<Transaction> getAllTransactions() {
		return allTransactions;
	}

	public void setAllTransactions(List<Transaction> allTransactions) {
		this.allTransactions = allTransactions;
	}

	public byte[] getScannedData() {
		return scannedData;
	}

	public void setScannedData(byte[] scannedData) {
		this.scannedData = scannedData;
	}

	public List<ExpediteurType> getListConsulter() {
		return listConsulter;
	}

	public void setListConsulter(List<ExpediteurType> listConsulter) {
		this.listConsulter = listConsulter;
	}

	public boolean isNotAdd() {
		return notAdd;
	}

	public void setNotAdd(boolean notAdd) {
		this.notAdd = notAdd;
	}

	public List<ListeDestinatairesModel> getListAnnotations() {
		return listAnnotations;
	}

	public void setListAnnotations(List<ListeDestinatairesModel> listAnnotations) {
		this.listAnnotations = listAnnotations;
	}

	public List<CourrierInformations> getListeCourriersInformation() {
		return listeCourriersInformation;
	}

	public void setListeCourriersInformation(
			List<CourrierInformations> listeCourriersInformation) {
		this.listeCourriersInformation = listeCourriersInformation;
	}

	public int getFirstRow() {
		return firstRow;
	}

	public void setFirstRow(int firstRow) {
		this.firstRow = firstRow;
	}

	public int getFlagLies() {
		return flagLies;
	}

	public void setFlagLies(int flagLies) {
		this.flagLies = flagLies;
	}

	public void setAffichePanelAjoutUpload(boolean affichePanelAjoutUpload) {
		this.affichePanelAjoutUpload = affichePanelAjoutUpload;
	}

	public boolean isAffichePanelAjoutUpload() {
		return affichePanelAjoutUpload;
	}

	public boolean isAffichePanelModifUpload() {
		return affichePanelModifUpload;
	}

	public void setAffichePanelModifUpload(boolean affichePanelModifUpload) {
		this.affichePanelModifUpload = affichePanelModifUpload;
	}

	public List<AoConsultation> getAoConsultationList() {
		return AoConsultationList;
	}

	public void setAoConsultationList(List<AoConsultation> aoConsultationList) {
		AoConsultationList = aoConsultationList;
	}

	public List<CourrierInformations> getListValise() {
		return listValise;
	}

	public void setListValise(List<CourrierInformations> listValise) {
		this.listValise = listValise;
	}

	public boolean isShowInputDay() {
		return showInputDay;
	}

	public void setShowInputDay(boolean showInputDay) {
		this.showInputDay = showInputDay;
	}

	public boolean isShowInputMonth() {
		return showInputMonth;
	}

	public void setShowInputMonth(boolean showInputMonth) {
		this.showInputMonth = showInputMonth;
	}

	public boolean isShowInputYear() {
		return showInputYear;
	}

	public void setShowInputYear(boolean showInputYear) {
		this.showInputYear = showInputYear;
	}

	public boolean isShowdateF() {
		return showdateF;
	}

	public void setShowdateF(boolean showdateF) {
		this.showdateF = showdateF;
	}

	public Expdestexterne getExpdestexterneNotif() {
		return expdestexterneNotif;
	}

	public void setExpdestexterneNotif(Expdestexterne expdestexterneNotif) {
		this.expdestexterneNotif = expdestexterneNotif;
	}

//	public List<AttachmentFileBean> getAttachmentFileBeanList() {
//		return attachmentFileBeanList;
//	}
//
//	public void setAttachmentFileBeanList(
//			List<AttachmentFileBean> attachmentFileBeanList) {
//		this.attachmentFileBeanList = attachmentFileBeanList;
//	}

	public String getAffichagePanelRecption() {
		return affichagePanelRecption;
	}

	public void setAffichagePanelRecption(String affichagePanelRecption) {
		this.affichagePanelRecption = affichagePanelRecption;
	}

	public void setMasquerPanelDetailCourrier(boolean masquerPanelDetailCourrier) {
		//System.out.println("setMasquerPanelDetailCourrier "+masquerPanelDetailCourrier );
		this.masquerPanelDetailCourrier = masquerPanelDetailCourrier;
	}

	public boolean isMasquerPanelDetailCourrier() {
		return masquerPanelDetailCourrier;
	}
	//[JS]============ 2020-03-24==============
	public boolean isEtatAccusesReception() {
		return etatAccusesReception;
	}

	public void setEtatAccusesReception(boolean etatAccusesReception) {
		this.etatAccusesReception = etatAccusesReception;
	}

	public int getFlagDocuPhysique() {
		return flagDocuPhysique;
	}

	public void setFlagDocuPhysique(int flagDocuPhysique) {
		this.flagDocuPhysique = flagDocuPhysique;
	}

	public Date getVbDateConsultationCourrier() {
		return vbDateConsultationCourrier;
	}

	public void setVbDateConsultationCourrier(Date vbDateConsultationCourrier) {
		this.vbDateConsultationCourrier = vbDateConsultationCourrier;
	}

	public Suivitransmissioncourrier getAccuseInformation() {
		return accuseInformation;
	}

	public void setAccuseInformation(Suivitransmissioncourrier accuseInformation) {
		this.accuseInformation = accuseInformation;
	}

	public boolean isCourrierAExcecuter() {
		return courrierAExcecuter;
	}

	public void setCourrierAExcecuter(boolean courrierAExcecuter) {
		this.courrierAExcecuter = courrierAExcecuter;
	}

	public boolean isRoleNoteTransmission() {
		return roleNoteTransmission;
	}

	public void setRoleNoteTransmission(boolean roleNoteTransmission) {
		this.roleNoteTransmission = roleNoteTransmission;
	}

	public void setNotAddCourrier(boolean notAddCourrier) {
		this.notAddCourrier = notAddCourrier;
	}

	public boolean isNotAddCourrier() {
		return notAddCourrier;
	}

	public String getDestinataireOffre() {
		return destinataireOffre;
	}

	public void setDestinataireOffre(String destinataireOffre) {
		this.destinataireOffre = destinataireOffre;
	}

	public void setExpediteurCourrierRepondre(String expediteurCourrierRepondre) {
		this.expediteurCourrierRepondre = expediteurCourrierRepondre;
	}

	public String getExpediteurCourrierRepondre() {
		return expediteurCourrierRepondre;
	}

	
	}