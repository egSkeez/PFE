package xtensus.beans.common.GBO;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import xtensus.aop.LogClass;
import xtensus.beans.common.LanguageManagerBean;
import xtensus.beans.common.ListeDestinatairesModel;
import xtensus.beans.common.VariableGlobale;
import xtensus.beans.common.GBO.CourrierConsultationJourBean;
import xtensus.beans.utils.ComposantDynamique;
import xtensus.beans.utils.CourrierConsulterInformations;
import xtensus.beans.utils.CourrierDossierListe;
import xtensus.beans.utils.CourrierInformations;
import xtensus.entity.Annotation;
import xtensus.entity.AoConsultation;
import xtensus.entity.Classement_archivage_niveau_02;
import xtensus.entity.Confidentialite;
import xtensus.entity.Courrier;
import xtensus.entity.CourrierDonneeSupplementaire;
import xtensus.entity.CourrierDossier;
import xtensus.entity.CourrierDossierId;
import xtensus.entity.CourrierLiens;
import xtensus.entity.DonneeSupplementaireNature;
import xtensus.entity.Dossier;
import xtensus.entity.Classement_archivage_niveau_01;
import xtensus.entity.Etat;
import xtensus.entity.Expdest;
import xtensus.entity.Expdestexterne;
import xtensus.entity.Lienscourriers;
import xtensus.entity.Nature;
import xtensus.entity.NatureCategorie;
import xtensus.entity.Pm;
import xtensus.entity.Pp;
import xtensus.entity.Suivitransmissioncourrier;
import xtensus.entity.Transaction;
import xtensus.entity.TransactionAnnotation;
import xtensus.entity.TransactionAnnotationId;
import xtensus.entity.TransactionDestination;
import xtensus.entity.TransactionDestinationId;
import xtensus.entity.TransactionDestinationReelle;
import xtensus.entity.Transmission;
import xtensus.entity.Typedossier;
import xtensus.entity.Typetransaction;
import xtensus.entity.Urgence;
import xtensus.entity.Utilisateur;
import xtensus.entity.Variables;
import xtensus.entity.Workflow;
import xtensus.ldap.business.LdapOperation;
import xtensus.ldap.model.BOC;
import xtensus.ldap.model.Person;
import xtensus.ldap.model.Unit;
import xtensus.services.ApplicationManager;
import xtensus.services.Export;
import xtensus.workflow.beans.JBPMAccessProcessBean;
import xtensus.workflow.handlers.TraitementEtapeSuivant;

@Component
@Scope("request")
public class CourrierDetailsBean {

	private ApplicationManager appMgr;
	private Export export;
	private Courrier courrier;
	private Dossier dossier;
	private TransactionAnnotation cA;
	private TransactionAnnotationId cI;
	private boolean status;
	private boolean status4;
	private boolean statusCloturer;
	private boolean statusActive;
	private boolean status1;
	private boolean status2;
	private boolean status3;
	private boolean showDonneSupp = false;
	private Date date;
	private Date date1;
	private Transaction transaction;
	private TransactionDestination transactionDestination;
	private TransactionAnnotation transactionAnnotation;
	private Nature nature;
	private NatureCategorie categorieNature;
	private Transmission transmission;
	private Confidentialite confidentialite;
	private Urgence urgence;
	private Expdestexterne expdestexterne;
	private List<Expdestexterne> listDestExpdestexternes;
	private Utilisateur utilisateur;
	private List<CourrierConsulterInformations> courrierConsulterInformations;
	private Date dateReception;
	private Date dateReponse;
	@Autowired
	private VariableGlobale vb;
	@Autowired
	private LanguageManagerBean lm;
	@Autowired
	private MessageSource messageSource;
	private String message;
	private String messageDoc;
	private Confidentialite cf;

	private String etatTransaction;
	private List<String> selectedItemsAnnotation;
	List<Annotation> listAnnotations;
	private String reponse1;
	private String annotationResult;
	private boolean select1 = false;

	private boolean statusClassement;
	private boolean statusNonClasse;
	private boolean statusClasseSucces;
	private boolean statusClasseErreur;
	private String messageInfoCourrierClassement;
	private String selectedItemArmoire;
	private String selectedItemEtages;
	private List<Classement_archivage_niveau_02> listArmoire;
	private List<Classement_archivage_niveau_01> listEtages;

	private boolean etatDescription;
	private boolean etatEnvoyerAuxAutre;
	private boolean etatDateReponse;
	private boolean etatDescriptionTransaction;
	private boolean etatkeywords;
	private boolean accuseReception;
	private String lienOutput;
	private boolean showMonitoringButton;
	private LdapOperation ldapOperation;

	private boolean notLinkedMail;
	private boolean linkedMail;
	private long nbrCourrierLies;

	private boolean affichageDocument;
	private boolean affichageDetailsAccuse;
	// [JS] 2019-07-24
	private boolean affichageReceptionPhysique;
	 private List<CourrierInformations> listCourriersLiees ;
	// Fin [JS]
	private boolean statusAccuseReception;
	private boolean showResponseButton;
	private boolean showModificationButton;
	private boolean showAjoutDocumentButton;
	private boolean hideResponseButton;
	private String align;
	private Classement_archivage_niveau_01 etages;
	private Classement_archivage_niveau_02 armoire;
	private boolean showUpdateClassement;
	private Integer idUser;
	private String type1;
	private String typeSecretaire;
	private Integer typeTransmission;
	// private boolean EtageChanged;
	private Classement_archivage_niveau_01 ancienEtages;
	private String from;
	private boolean showForValidate;
	private CourrierInformations ci;
	private String codeUniqueCourrier;
	private String expditeurUnite;
	private List<Variables> var;
	private String cupSRV;
	private String messageModif;
	private boolean statusCanModif;
	private Date dateDebut;
	private Date dateFin;
	private List<Integer> listIdsSousUnit;
	private List<Integer> listIdsSubordonne;
	private String consultationCourrierSecretaire;
	private String consultationCourrierSubordonne;
	private String consultationCourrierSousUnite;
	private String boutonBordereau;
	private DataModel listeDestinatairesDM;
	private HashMap<String, Object> filterMap = new HashMap<String, Object>();
	private String sortField;
	private String typeCourrier;
	private boolean descending;
	private String type;
	private int idBoc;
	private boolean statV1;
	private boolean statV2;
	// KHA
	private List<ListeDestinatairesModel> destinataireRepondre;
	private Boolean etatReceptionPhysique;
	// [JS]
	private List<DonneeSupplementaireNature> listDSN;
	private Properties msg;
	private ComposantDynamique composantDynamique;
	private List<ComposantDynamique> D;
	private CourrierDonneeSupplementaire courrierDS;
	private CourrierDonneeSupplementaire cds;
	private List<ComposantDynamique> listCD;
	private Boolean affichePanelReceptionPhysique;
	private CourrierInformations courrierInformations;
	private boolean existeBOSansMembres;
	private int lastIndex;
	private int idUserDes;
	private String typeUserDes;
	private List<Integer> listIdBocMembers;
	private boolean receptionphysiqueNonLivre;
	private boolean showPanelAOC;
	private Integer numeroAoConsultation;
	private AoConsultation aoConsultation;
	private String heure1;
	private String heure3;
	private String heure2;
	private boolean showRapp;
    @Autowired
    private CourrierConsultationJourBean courrierConsultationRecentBean;
	private List<CourrierInformations> listCourriersInformationsAffecte;
	private int idBocExpediteur;
	private int idBocDestinataire;
	private int IdExpediteur;
	private Unit unitSup;
	private int referenceSomeA;
	private int responsableBocDest=0;	
	private int typePocesseur;
	private int pocesseurId;
	private boolean existeNiveau02;
	private DataModel listCourrierAffecteDM;
	@Autowired
	private CourrierConsultationRecentBean courrierConsultation;
	private long records2;
	private boolean showLienButton;
	private boolean afficherListeLies;
	private String boutonNoteTransmission;
	private boolean affichageBoutonLien;
	private boolean showPanelCheque;
	// /
	@Autowired
	public CourrierDetailsBean(
			@Qualifier("applicationManager") ApplicationManager appMgr,
			@Qualifier("export") Export export) {
		this.appMgr = appMgr;
		this.export = export;
		courrier = new Courrier();
		dossier = new Dossier();
		// *** kha
		ci = new CourrierInformations();
		transaction = new Transaction();
		transactionDestination = new TransactionDestination();
		transactionAnnotation = new TransactionAnnotation();
		nature = new Nature();
		transmission = new Transmission();
		confidentialite = new Confidentialite();
		urgence = new Urgence();
		expdestexterne = new Expdestexterne();
		listDestExpdestexternes = new ArrayList<Expdestexterne>();
		utilisateur = new Utilisateur();
		courrierConsulterInformations = new ArrayList<CourrierConsulterInformations>();
		listArmoire = new ArrayList<Classement_archivage_niveau_02>();
		listEtages = new ArrayList<Classement_archivage_niveau_01>();
		date = new Date();
		date1 = new Date();
		selectedItemsAnnotation = new ArrayList<String>();
		cA = new TransactionAnnotation();
		cI = new TransactionAnnotationId();
		listAnnotations = new ArrayList<Annotation>();
		ldapOperation = new LdapOperation();
		// [JS]
		listDSN = new ArrayList<DonneeSupplementaireNature>();
		listCD = new ArrayList<ComposantDynamique>();
		courrierDS = new CourrierDonneeSupplementaire();
		cds = new CourrierDonneeSupplementaire();
		categorieNature = new NatureCategorie();
		listIdBocMembers = new ArrayList<Integer>();
		listCourriersLiees=new ArrayList<CourrierInformations>();
		listIdsSousUnit = new ArrayList<Integer>();
		listIdsSubordonne = new ArrayList<Integer>();
		listCourriersInformationsAffecte = new ArrayList<CourrierInformations>();
		// /
		System.out
				.println("****** BeanInjecte CourrierDetailsBean ******");
	}

	@PostConstruct
	public void Initialize() {
		vb.setMasquerPanelDetailCourrier(true);
		 typePocesseur=0;
		 pocesseurId=0;
		if(vb.getPerson().isBoc()){
			typePocesseur=1;
			pocesseurId=vb.getPerson().getAssociatedBOC().getIdBOC();
		}
		else{
			typePocesseur=2;
			pocesseurId=vb.getPerson().getAssociatedDirection().getIdUnit();
		}
		List<Variables> listeVariable = appMgr
		.listVariablesByLibelle("classement_archivage_nombre_niveau");
		int nombreNiveau = 1;
		existeNiveau02 = false;
		if (listeVariable != null && listeVariable.size() > 0) {
			try {
					nombreNiveau = Integer.parseInt(listeVariable.get(0)
				.getVaraiablesValeur());
			} catch (Exception e) {
		nombreNiveau = 1;
	}
}
		// Si nous avons 2 niveau ou plus nous aura une liste déroulante de
		// niveau 2 à afficher
		if (nombreNiveau >= 2) {
			existeNiveau02 = true;
		}
		// vb.getListSelectedItem().clear();
		// kha 12-02-2019
		destinataireRepondre = new ArrayList<ListeDestinatairesModel>();
		transaction = vb.getTransaction();
		System.out.println("repeat");
		courrier = vb.getCourrier();
		statusCloturer = false;
		statusActive = false;
//		System.out.println("****************************************");
//		System.out.println(vb.getCourrier().getCourrierEtatCloture());
//		System.out.println("****************************************");
		etatReceptionPhysique = false;
		ci = vb.getCourDossConsulterInformations();
		//2019-07-27
		affichePanelReceptionPhysique=false;
		//Fin
		showModificationButton=true;
		showAjoutDocumentButton=true;
		Expdest cupExpDest1;
		cupExpDest1 = new Expdest();
		if (transaction != null){
		List<Expdest> listExpDestByIdExpDest = appMgr.getListExpDestByIdExpDest(transaction.getExpdest().getIdExpDest());
		cupExpDest1 = listExpDestByIdExpDest.get(0);}
		//L'utilisateur n'a pas le droit de modifier courrier après l'exécution 
		if(ci != null){
		if(vb.getPerson().isBoc()){
//				System.out.println("================= Connecté Is BOC =====================");		
				List<ListeDestinatairesModel> list = vb.getListeDestinataire();
				List<Integer> listeIdDest = new ArrayList<Integer>();
				List<Integer> listeIdMembresBOc=new ArrayList<Integer>();
				if(vb.getPerson().isBoc()){
				List<Person> listMembresBoc = vb.getPerson().getAssociatedBOC().getMembersBOC();
				for(Person membres:listMembresBoc){
					listeIdMembresBOc.add(membres.getId());
				}
				}
//				System.out.println("courrier.getIdCourrier() "+courrier.getIdCourrier());
				CourrierDossier courrierDossier1 = appMgr.getCourrierDossierByIdCourrier(courrier.getIdCourrier())
				.get(0);
				Expdest cupExpDest;
				cupExpDest = new Expdest();
				cupExpDest = appMgr.getListExpDestByIdExpDest(
						transaction.getExpdest().getIdExpDest()).get(0);
				int refdossier = courrierDossier1.getId().getDossierId();
				Transaction transactionn = new Transaction();
				List<Transaction> listTr = appMgr.getTransactionByIdDossier(refdossier);
				lastIndex=listTr.size();
				transactionn=listTr.get(lastIndex-1);
				int idEditeur=transactionn.getIdUtilisateur();
				if(list!=null)
				for (ListeDestinatairesModel d : list) {
					int idDest = 0;
					// KHA reception physique
					if (vb.getPerson().isResponsable() && !vb.getPerson().isBoc()) {
						if (vb.getPerson().getAssociatedDirection().getIdUnit() == d
								.getDestinataireId()) {
							idDest = vb.getPerson().getId();
						} else {
							System.out
									.println(" personne connectee n'est pas responsable");
							idDest = d.getDestinataireId();
						}
					}else{
						
					}
					listeIdDest.add(idDest);
				}
				if((!listeIdMembresBOc.contains(idEditeur))
						|| listeIdDest.contains(vb.getPerson().getId()) || ci.getTransaction().getEtat().getEtatId()==6 
						){
					showModificationButton=false;
					showAjoutDocumentButton=false;
//					showAjoutDocumentButton=true;
						vb.setShowReceptionPhysique(true);
				}else{
					showModificationButton=true;
					showAjoutDocumentButton=true;
				}
				if(courrier.getIdcourrierFK()!=null){
					// AH : C'est le cas que le courrier est affecté à une valise pas de modifiaction
					showModificationButton=false;
				}
		}else{
			if (cupExpDest1.getIdExpDestLdap() != null
					&& vb.getPerson().getAssociatedDirection()
							.getIdUnit().intValue() == cupExpDest1
							.getIdExpDestLdap().intValue()){
				List<TransactionDestination> transactionDestinations = appMgr.getDestinationByIdTransaction(transaction.getTransactionId());
				if(transactionDestinations!=null && transactionDestinations.size()>0){				
					TransactionDestination tr = transactionDestinations.get(0);
					if(tr.getTransactionDestDateConsultation() != null || ci.getTransaction().getEtat().getEtatId()==6 ){
						showModificationButton=false;
						showAjoutDocumentButton=false;
					}else{
						showModificationButton=true;
						showAjoutDocumentButton=true;
						}
					}
				} else {
					showModificationButton = false;
					showAjoutDocumentButton = false;
				}
			}
		}
		
		if(courrier.getTransmission().getTransmissionId()==10){
			showAjoutDocumentButton = false;
		}
		// codeUniqueCourrier=vb.getCopyExpReelNom();
		if (vb.getPerson().isBoc()) {
			if (vb.getCourrier().getCourrierEtatCloture() == 1) {
				statusCloturer = false;
				statusActive = true;
			} else {
				statusCloturer = true;
				statusActive = false;
			}
		}
		if (vb.getLocale().equals("ar")) {
			align = "right";
		} else {
			align = "left";
		}
		// *** AC
//		System.out.println("----vb.getRedirect()--- : " + vb.getRedirect());
		from = vb.getRedirect();
		System.out.println("----from initialize--- : " + from);
//		vb.setRedirect("");
		// **
		setEtatDescription(false);
		setEtatEnvoyerAuxAutre(false);
		etatDateReponse = false;
		etatDescriptionTransaction = false;
		etatkeywords = false;
		accuseReception = false;
		showMonitoringButton = vb.isShowMonitoringButtonForDest();
		affichageDocument = false;
		affichageDetailsAccuse = false;
		affichageReceptionPhysique=false;
		try {
			try {
				if (ci.getCourrierRecu() == 1) {
					showResponseButton = true;
					hideResponseButton = false;
				} else {
					showResponseButton = false;
					hideResponseButton = true;
				}
				if (ci.getCourrierAValider() == 1) {
					showForValidate = true;
				}
				/////////////////////KBS 2019-12-24//////////
				if (vb.getPerson().isBoc()) {
					showResponseButton = false;
					hideResponseButton = false;
				}
				////////////////////////////////////////////
			} catch (Exception e) {
				showResponseButton = false;
				hideResponseButton = true;
				showForValidate = false;
			}

			annotationResult = "";
			//si Connect est un BO central affiche bouton reponse courier 	
			if(vb.getPerson().isBoc() && vb.getPerson().getAssociatedBOC().getIdBOC()==1){							
					showResponseButton = true;			
			}
			// XTE : On ajoute le test Type !=null, car si le courrier est
			// ajouté par un non Boc, il aura le type à NULL-------------
			/*
			 * if (courrier.getCourrierType()!=null &&
			 * courrier.getCourrierType().equals("D")) { boutonBordereau =
			 * "false"; } else { boutonBordereau = "true"; }
			 */
			// ----------------------- KHA : ROLE_BORDEREAU_ENVOI : ajouté le
			// 15-03-2019
			if (!vb.isRoleBordereau()) {
				boutonBordereau = "false";
			} else {
				if (courrier.getCourrierType() != null
						&& courrier.getCourrierType().equals("D")) {
					boutonBordereau = "true";
				} else {
					boutonBordereau = "false";
				}
			}
			if (!vb.isRoleNoteTransmission()) {
				boutonNoteTransmission = "false";
			} else {
				
					boutonNoteTransmission = "true";
				
			}
			// ----------------------- Fin KHA
			if (courrier == null) {
				if (ci.getCourrier() == null) {
					courrier = appMgr.getCourrierByIdCourrier(
							ci.getCourrierID()).get(0);
				} else {
					courrier = ci.getCourrier();
				}
				vb.setCourrier(courrier);
			}

			nature = appMgr.getNatureById(courrier.getNature().getNatureId())
					.get(0);
			categorieNature = nature.getNatureCategorie();
			
			// [JS]
//			System.out.println("Nature Sélectionné :" + nature);
			// Load fichier Properties
			ExternalContext jsfContext = FacesContext.getCurrentInstance()
					.getExternalContext();
			ServletContext servletContext = (ServletContext) jsfContext
					.getContext();
//			System.out.println("Contenu de variable local :" + vb.getLocalFr());
			String webContentRoot = servletContext.getRealPath("/");
			String pathConfigFile = webContentRoot + File.separator + "WEB-INF"
					+ File.separator + "classes" + File.separator + "messages_"
					+ vb.getLocalFr() + ".properties";
			msg = new Properties();
//			System.out.println("Path Fichier :" + pathConfigFile);
			try {
				msg.load(new FileInputStream(pathConfigFile));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (vb.getPerson().isBoc()){
			if (courrier.getTransmission().getTransmissionId()== 11 ){
				ci.setCourrierAValider(0);
			}
			}
			listDSN = appMgr.getListDonneeSupplementaireNatureAffectes(nature
					.getNatureId());
			listCD = new ArrayList<ComposantDynamique>();
			Class aClass = cds.getClass();
			courrierDS = appMgr.getDonneeSupplementaireCourrier(courrier
					.getIdCourrier());

			if (listDSN != null && listDSN.size() > 0) {
				showDonneSupp = true;
				for (int i = 0; i < listDSN.size(); i++) {
					composantDynamique = new ComposantDynamique();
//					System.out.println("==================================");
					String libelle = listDSN.get(i).getLibelleDonnee();
//					System.out.println("Libellé " + libelle);
					String libelleNature = msg.getProperty(libelle);
//					System.out.println("Libellé :" + libelleNature);
//					System.out.println("==================================");
					composantDynamique.setName(libelleNature);
					composantDynamique.setType(listDSN.get(i)
							.getDonneeSupplementaire()
							.getTypeDonneeSupplementaire());
					composantDynamique.setIdChamps(listDSN.get(i).getDonneeSupplementaire()
							.getIdDonneeSupplementaire());

					listCD.add(composantDynamique);
					int idchamp = listCD.get(i).getIdChamps();
					String typeChamp = listCD.get(i).getType();

					String methodName = "getColonne" + idchamp;
					Method m = null;
					m = aClass.getMethod(methodName);
//					System.out.format("Methode : %s%n", m.toGenericString());
					Object resultat = m.invoke(courrierDS, new Object[0]);
//					System.out.println("Resultat :" + resultat);
					if (typeChamp.equals("RADIO")) {
						if (resultat.equals("true")) {
							resultat = "Oui";
						} else
							resultat = "Non";
					}
					composantDynamique.setColonne(resultat);
				}
			}
			vb.setListComposantDynamiqueNature(listCD);
			// [JS]
			vb.setNature(nature);
			confidentialite = appMgr.getConfidentialiteById(
					courrier.getConfidentialite().getConfidentialiteId())
					.get(0);
			vb.setConfidentialite(confidentialite);

			urgence = appMgr.getUrgenceById(
					courrier.getUrgence().getUrgenceId()).get(0);
			vb.setUrgence(urgence);

			transmission = appMgr.getTransmissionById(
					courrier.getTransmission().getTransmissionId()).get(0);
			if (transmission.getTransmissionId()==1){
				showRapp = true;
			}else{
				showRapp =false;
			}
			vb.setTransmission(transmission);

			//listArmoire = appMgr.listArmoireByEtat("Libre");

			CourrierDossier courrierDossier = new CourrierDossier();
			courrierDossier = appMgr.getCourrierDossierByIdCourrier(
					courrier.getIdCourrier()).get(0);
			int refDossier = courrierDossier.getId().getDossierId();
			vb.setReferenceDossier(refDossier);
			
			if (transaction == null) {
				transaction = appMgr.getListTransactionByIdTransaction(
						ci.getTransactionID()).get(0);
				vb.setTransaction(transaction);

			}
			if(transaction.getEtat().getEtatId()==5){
				vb.setCourrierAExcecuter(true);
			}
			else{
				vb.setCourrierAExcecuter(false);
			}
			int refTransaction = transaction.getTransactionId();
			
			List<TransactionAnnotation> annotations = new ArrayList<TransactionAnnotation>();
			annotations = appMgr.getAnnotationByIdTransaction(refTransaction);

			int refEtat = transaction.getEtat().getEtatId();
			if (vb.getLocale().equals("ar")) {
				etatTransaction = appMgr.listEtatByRef(refEtat).get(0)
						.getEtatLibelleAr();
			} else {
				etatTransaction = appMgr.listEtatByRef(refEtat).get(0)
						.getEtatLibelle();
			}

			/***** Acces gestion accses reception ***/
			//[JS] : 2020-03-17
			Transaction tr=vb.getTransaction();    
			if(tr.getTransactionDestinationReelle()!=null){
			String typeDest=tr.getTransactionDestinationReelle().getTransactionDestinationReelleTypeDestinataire();			 
			if (vb.getPerson().isBoc() && typeDest.equals("Externe") && courrier.getCourrierType().equals("D") ) {
				accuseReception = true;
			}
			}
			if (appMgr.accuseReceptionByIdCourrier(courrier.getIdCourrier())
					.size() != 0) {
				setAffichageDetailsAccuse(true);
			}
			//[JS] 2019-07-24
			//courrier en cours admet une récéption physique 
//			System.out.println(" 2019-07-24 : "+appMgr.getListCourrierAvecReceptionPhysique(courrier.getIdCourrier()).size());
//			if(appMgr.getListCourrierAvecReceptionPhysique(courrier.getIdCourrier()).size() !=0)
//			{ 
////			if(vb.getPerson().isBoc() || vb.get){
//				affichageReceptionPhysique=true;
//			}
////			}
			/***** Affichage listDocument *******/
			int nbDoc = appMgr
					.getDocumentByIdCourrier(courrier.getIdCourrier()).size();
			if (nbDoc != 0) {
				setAffichageDocument(true);
			}

			String annotationLibelle;
			int lastIndex;
			int refAnnotation;
			// AH : affecter à chaque Destinataire ses Annotations

			List<ListeDestinatairesModel> list = vb.getListeDestinataire();
			List<Integer> listeIdDest = new ArrayList<Integer>();
			List<Integer> listeIdMembresBOc=new ArrayList<Integer>();
			if(vb.getPerson().isBoc()){
			List<Person> listMembresBoc = vb.getPerson().getAssociatedBOC().getMembersBOC();
			for(Person membres:listMembresBoc){

				listeIdMembresBOc.add(membres.getId());
			}
			}
			if(list!=null){
			for (ListeDestinatairesModel d : list) {
				int idDest = 0;
				// KHA reception physique
				if (vb.getPerson().isResponsable() && !vb.getPerson().isBoc()) {
//					System.out.println(" personne connectee responsable");
//					System.out.println("Vb.getPerson ===============> "+vb.getPerson());
//					System.out.println("vb.getPerson().getAssociatedDirection().getIdUnit()===>"+vb.getPerson().getAssociatedDirection().getIdUnit());
//					System.out.println("d.getDestinataireId()=================================> "+d.getDestinataireId());
					if (vb.getPerson().getAssociatedDirection().getIdUnit() == d
							.getDestinataireId()) {
						idDest = vb.getPerson().getId();
					} else {
						System.out
								.println(" personne connectee n'est pas responsable");
						idDest = d.getDestinataireId();
					}
				}else{
				System.out.println("heloo");
					
				}
				listeIdDest.add(idDest);
				//

				List<String> l = new ArrayList<String>();
				l = d.getListeAnnotations();
				// [Modifcation] le 2019-06-26
				if (l != null && l.size() > 0) {
					String concatenationAnnotation = "";
					System.out
							.println("KHA===> concatiner la liste des annotations : Debut");
					for (int i = 0; i < l.size(); i++) {
						Annotation annotation = appMgr
								.getAnnotationByIdAnotation(
										Integer.valueOf(l.get(i))).get(0);
						concatenationAnnotation += annotation
								.getAnnotationLibelle() + " / ";
					}
					System.out
							.println("KHA===> concatiner la liste des annotations : Fin");
					if (!concatenationAnnotation.equals("")) {
						int lastIndexBar = concatenationAnnotation
								.lastIndexOf("/");
						concatenationAnnotation = concatenationAnnotation
								.substring(0, lastIndexBar);
						System.out.println("KHA : concatenationAnnotation= "
								+ concatenationAnnotation);
					}
					d.setAnnotations(concatenationAnnotation);
				}
			}
		}
			// List<Annotation> l =
			// appMgr.listeAnnotationsParDestinataireAndTransaction(d.getDestinataireId(),refDossier);
			/*
			 * List<Annotation> l =
			 * appMgr.listeAnnotationParDestinataireEtTransaction
			 * (d.getDestinataireId(),refDossier); // List<Annotation> l =
			 * appMgr
			 * .listeAnnotationsParDestinataireEtTransaction(d.getDestinataireId
			 * (),refDossier,"Externe"); // KHA
			 * 
			 * List<String> listeAnnotationDest= new ArrayList<String>();
			 * 
			 * 
			 * String concatenationAnnotation=""; for(Annotation t: l){
			 * System.out.println(t.getAnnotationLibelle());
			 * 
			 * // KHA
			 * listeAnnotationDest.add(String.valueOf(t.getAnnotationId()));
			 * concatenationAnnotation+=t.getAnnotationLibelle()+" / "; }
			 * if(!concatenationAnnotation.equals("")) { int lastIndexBar =
			 * concatenationAnnotation.lastIndexOf("/"); concatenationAnnotation
			 * = concatenationAnnotation.substring(0, lastIndexBar);
			 * System.out.println
			 * ("1 concatenationAnnotation"+concatenationAnnotation); }
			 * System.out
			 * .println("2 concatenationAnnotation"+concatenationAnnotation);
			 * 
			 * d.setAnnotations(concatenationAnnotation);
			 * System.out.println("d.destinataire= "+d.getDestinataireName());
			 * System
			 * .out.println("d.annotations= "+d.getAnnotations().toString());
			 * //KHA d.setListeAnnotations(listeAnnotationDest);
			 * 
			 * }
			 */

			listeDestinatairesDM = new ListDataModel();
			//System.out.println("liste 2019-10-21 : "+list.size());
			listeDestinatairesDM.setWrappedData(list);

			// KHA :parcourir la liste des annotations à charger dans picklist
			List<Annotation> annotation = new ArrayList<Annotation>();
			try {
				annotation = appMgr.getAnnotationByIdAnotation(annotations
						.get(0).getId().getIdAnnotation());
				if (annotation.size() != 0) {
					if (annotation.get(0).getAnnotationLibelle()
							.equals("Autre")) {
						annotationResult = annotationResult
								+ transaction
										.getTransactionCommentaireAnnotation()
								+ " / ";
						selectedItemsAnnotation.add(transaction
								.getTransactionCommentaireAnnotation());
						listAnnotations.add(appMgr.getAnnotationByLibelle(
								"Autre").get(0));
					} else {
						if (vb.getLocale().equals("ar")) {
							for (TransactionAnnotation ta : annotations) {
								refAnnotation = ta.getId().getIdAnnotation();
								annotationLibelle = appMgr
										.getAnnotationByIdAnotation(
												refAnnotation).get(0)
										.getAnnotationLibelleAr();
								annotationResult = annotationResult
										+ annotationLibelle + " / ";
								selectedItemsAnnotation.add(String.valueOf(refAnnotation));
								listAnnotations.add(appMgr
										.getAnnotationByIdAnotation(
												refAnnotation).get(0));
							}
						} else {
							for (TransactionAnnotation ta : annotations) {
								refAnnotation = ta.getId().getIdAnnotation();
								annotationLibelle = appMgr
										.getAnnotationByIdAnotation(
												refAnnotation).get(0)
										.getAnnotationLibelle();
								annotationResult = annotationResult
										+ annotationLibelle + " / ";
								selectedItemsAnnotation.add(String.valueOf(refAnnotation));
								listAnnotations.add(appMgr
										.getAnnotationByIdAnotation(
												refAnnotation).get(0));
							}
						}
					}

					if (!annotationResult.equals("")) {
						lastIndex = annotationResult.lastIndexOf("/");
						annotationResult = annotationResult.substring(0,
								lastIndex);
					}
					vb.setListSelectedAnnotations(selectedItemsAnnotation);
					vb.setCopyAnnotationResult(annotationResult);
				}
			} catch (Exception e) {
				vb.setListSelectedAnnotations(new ArrayList<String>());
				vb.setCopyAnnotationResult("");
			}

			reponse1 = courrier.getCourrierNecessiteReponse();

			if (reponse1.equals("Oui")) {
				etatDateReponse = true;
			}

			if (courrier.getCourrierCommentaire() != null) {
				if (!(courrier.getCourrierCommentaire().equals(""))) {
					setEtatDescription(true);
				}
			}
			if (vb.getCopyOtherDest() != null) {
				if (!(vb.getCopyOtherDest().equals(""))) {
					setEtatEnvoyerAuxAutre(true);
				}
			}
			if (transaction.getTransactionCommentaire() != null) {
				if (!(transaction.getTransactionCommentaire().equals(""))) {
					etatDescriptionTransaction = true;
				}
			}
			if (courrier.getKeywords() != null) {
				if (!(courrier.getKeywords().equals(""))) {
					etatkeywords = true;
				}
			}
			verificationLienCourrier();

			// ***********************************************************************
			// --------------------------------------MM----------------------------------------------------------------------------------
			// --------------------------------------------------------------------------------------------------------------------------
			// Code Unique courrier
			// cup :: Code Unique Parametrable

			Expdest cupExpDest;
			cupExpDest = new Expdest();
			cupExpDest = appMgr.getListExpDestByIdExpDest(
					transaction.getExpdest().getIdExpDest()).get(0);
			int transactionEnCours=transaction.getTransactionId();

			System.out.println("courrier.getIdCourrier() "+courrier.getIdCourrier());
			CourrierDossier courrierDossier1 = appMgr.getCourrierDossierByIdCourrier(courrier.getIdCourrier())
			.get(0);

			int refdossier = courrierDossier1.getId().getDossierId();
			Transaction transactionn = new Transaction();
			List<Transaction> listTr = appMgr.getTransactionByIdDossier(refdossier);
			lastIndex=listTr.size();
			transactionn=listTr.get(lastIndex-1);
			int idEditeur=transactionn.getIdUtilisateur();
			showLienButton=true;
			//Cacher le bouton lien pour le courrier de Nature enveloppe id=81 et le connecté n'est pas le posseseur de courrier
			if(courrier.getNature().getNatureId().intValue()==81){
				if(vb.getPerson().getId()!=idEditeur || transactionn.getEtat().getEtatId().intValue()==6 )
				showLienButton=false;
			}
			
			if (courrier.getCourrierAvecDocumentPhysique() != null
					&& courrier.getCourrierAvecDocumentPhysique() == true ) {
			

				
				/* si Le personne connecté est un responsable et qui est l'éditeur  : bouton reception physique ne s'affiche pas**/
				if (vb.getPerson().isResponsable()  && !vb.getPerson().isBoc()) {
					if (cupExpDest.getIdExpDestLdap() != null
							&& vb.getPerson().getAssociatedDirection()
									.getIdUnit() .equals( cupExpDest
									.getIdExpDestLdap())){
						System.out
								.println("si l'expediteur est l'editeur de courrier");
					etatReceptionPhysique = false;
					System.out.println("###1");
					}else if(listeIdDest.contains(vb.getPerson().getId())){
						System.out.println("###22");
						System.out
						.println("Destinataire Finale");
						etatReceptionPhysique = true;
						if(appMgr.getListCourrierAvecReceptionPhysiqueByEtat(courrier.getIdCourrier(),transaction.getTransactionId()).size() !=0){
							System.out.println("### Reception  physique Validé ### ");
							etatReceptionPhysique = false;
							
						}
						
					}

				}		
				//is BOC et n'est pas l'editeur de courrier DONC FAIT LE VALIDATION HEARCHQIUE		
				
				else if ((vb.getPerson().isBoc() && !listeIdMembresBOc.contains(idEditeur))
						|| listeIdDest.contains(vb.getPerson().getId())
						|| vb.getPerson().getId() == cupExpDest
								.getIdExpDestLdap()){
					// si le connectee est l'expediteur, le destinataire reel ou le
				// BO
				etatReceptionPhysique = true;
//				System.out.println("###2");
//				System.out.println("courrier =======>"+courrier.getIdCourrier());
//				System.out.println("Transaction ====> " +transaction.getTransactionId());
				if(appMgr.getListCourrierAvecReceptionPhysiqueByEtat(courrier.getIdCourrier(),transaction.getTransactionId()).size() !=0 || transaction.getEtat().getEtatId()==6){
//					System.out.println("### Reception  physique Validé ### ");
					etatReceptionPhysique = false;
				}
				}
			}
//			if(courrier.getCourrierAvecDocumentPhysique() != null  
//					&& courrier.getCourrierAvecDocumentPhysique() == true && appMgr.getListCourrierAvecReceptionPhysiqueByEtat(courrier.getIdCourrier(),transactionEnCours).size() !=0){
//				System.out.println("###3");
//				etatReceptionPhysique = false;
//
//			}
			if(courrier.getCourrierAvecDocumentPhysique() != null
					&& courrier.getCourrierAvecDocumentPhysique() != true){
				System.out.println("KHA ===> reception physique : non ");
				etatReceptionPhysique = false;
				System.out.println("###4");
			}
			//Ajoute Consition affichage Detail Reception physique 
			System.out.println("etatReceptionPhysique===============================>"+etatReceptionPhysique);
			System.out.println("courrier.getCourrierAvecDocumentPhysique()=====================> "+courrier.getCourrierAvecDocumentPhysique());
			System.out.println("#### 555 "+appMgr.getListCourrierAvecReceptionPhysiqueByEtat(courrier.getIdCourrier(),transactionEnCours).size());
			if(idEditeur==vb.getPerson().getId()){
				affichageReceptionPhysique=false;
			}else{
			if((courrier.getCourrierAvecDocumentPhysique() != null 
					&& courrier.getCourrierAvecDocumentPhysique() == true )){
				// si le connecté est BO récupéré sa transaction des réception et pas celle encours
				if(vb.getPerson().isBoc()){
					
					int idtransactionBO= appMgr.getTransactionReceptionPhysique(vb.getPerson().getAssociatedBOC().getIdBOC(),refdossier);
					transactionEnCours=idtransactionBO;
					System.out.println("c'est un BO ==>transactionEnCours = "+transactionEnCours);
					List<TransactionDestination> listTrDest = appMgr.getDestinationByIdTransaction(transactionEnCours);
					if(listTrDest!=null && listTrDest.size()>0){
						vb.setTransactionDestination(listTrDest.get(0));
					}
					
				}
					if( appMgr.getListCourrierAvecReceptionPhysiqueByEtat(courrier.getIdCourrier(),transactionEnCours).size() !=0){
				System.out.println("$$$$$ etatReceptionPhysique + "+etatReceptionPhysique);
				if(etatReceptionPhysique)
				affichageReceptionPhysique=false;
				else {
					affichageReceptionPhysique=true;
				System.out.println("affiche panel reception ");
				}
			}
					}
			}
			
			if (cupExpDest!=null && cupExpDest.getTypeExpDest().equals("Interne-Person")&& cupExpDest.getIdExpDestLdap() != null) {
				System.out.println("++cupExpDest+++ : " + cupExpDest.getIdExpDestLdap().intValue());
				int MonID;
				int j = 0;
				boolean findPerson = false;

				do {
					MonID = vb.getCopyLdapListUser().get(j).getId();
					System.out.println("++MONID+++ : " + MonID);
					if (MonID == cupExpDest.getIdExpDestLdap().intValue()) {
						findPerson = true;
						vb.setPersonCodeUnique(vb.getCopyLdapListUser().get(j));
						// vbn.setPerson(vb.getCopyLdapListUser().get(j));
					} else {
						j++;
					}
				} while (!findPerson && j < vb.getCopyLdapListUser().size());
				//
				// cupSRV=MonID+"";
//				System.out.println("vb.getPersonCodeUnique() : "
//						+ vb.getPersonCodeUnique().getCn());
				if (vb.getPersonCodeUnique()!= null && vb.getPersonCodeUnique().getAssociatedDirection() != null) {
					cupSRV = vb.getPersonCodeUnique().getAssociatedDirection()
							.getShortNameUnit();
					System.out.println("1" + cupSRV);
				}
				if (vb.getPersonCodeUnique()!= null && vb.getPersonCodeUnique().getAssociatedBOC() != null) {
					cupSRV = vb.getPersonCodeUnique().getAssociatedBOC()
							.getNameBOC();
					System.out.println("2" + cupSRV);
				}
				if (vb.getPersonCodeUnique()!= null && vb.getPersonCodeUnique().getAssociatedService() != null) {
					cupSRV = vb.getPersonCodeUnique().getAssociatedService()
							.getNameService();
					System.out.println("3" + cupSRV);
					cupSRV = vb.getPersonCodeUnique().getAssociatedService()
							.getAssociatedDirection().getNameDirection();
					System.out.println("4" + cupSRV);
				}

			} 
			else if (cupExpDest!=null && cupExpDest.getTypeExpDest().equals("Interne-Unité")&& cupExpDest.getIdExpDestLdap() != null) {
				System.out.println("++cupExpDest+++ : " + cupExpDest.getIdExpDestLdap().intValue());
				int MonID;
				int j = 0;
				boolean findunite = false;

				do {
					MonID = vb.getCopyLdapListUnit().get(j).getIdUnit();
					System.out.println("++MONID+++ : " + MonID);
					if (MonID == cupExpDest.getIdExpDestLdap().intValue()) {
						findunite = true;
					//	vb.setPersonCodeUnique(vb.getCopyLdapListUser().get(j));
						// vbn.setPerson(vb.getCopyLdapListUser().get(j));
						cupSRV=vb.getCopyLdapListUnit().get(j).getShortNameUnit();
						break;
					} else {
						j++;
					}
				} while (!findunite && j < vb.getCopyLdapListUnit().size());
				
			}
			else {
				System.out.println("-----Dans ELSE----");
				cupSRV = "XTRN";
			}
			codeUniqueCourrier=vb.getCodeUniqueCourrier();
			System.out.println("codeUniqueCourrier=====================>"+codeUniqueCourrier);
			// ***********************************************************************
			// --------------------------------------MM----------------------------
			// Test
			// var=appMgr.getListVariableByLibelle();
			/*
			 * Commenter le 2019-10-12
			 */
//			var = appMgr
//					.listVariablesByLibelle("code_courrier_unique_personnalisable");
//             System.out.println("2019-10-12 : "+ci.getTransaction().getCourrierReferenceCorrespondant());
//			codeUniqueCourrier = var.get(0).getVaraiablesValeur();
//			codeUniqueCourrier = codeUniqueCourrier.replace("[ID]", ci.getTransaction().getCourrierReferenceCorrespondant() + "");
//			codeUniqueCourrier = codeUniqueCourrier.replace("[Annee]",
//					new Date().getYear() + 1900 + "");
//			codeUniqueCourrier = codeUniqueCourrier.replace("[Mois]",
//					new Date().getMonth() + 1 + "");
//			// XTE : Si le courrier est ajouté par un non Boc, il aura le type à
//			// NULL--------------------------------------------------
//			if (ci.getTransaction().getCourrierType() != null) {
//				codeUniqueCourrier = codeUniqueCourrier.replace("[Sens]", 
//						ci.getTransaction().getCourrierType());
//			} else {
//				codeUniqueCourrier = codeUniqueCourrier.replace("[Sens]", "I");
//			}
//			System.out.println("courrier type = "
//					+ ci.getTransaction().getCourrierType() );
//			System.out.println("cupSRV================> "+cupSRV);
//			codeUniqueCourrier = codeUniqueCourrier.replace("[SRV]", cupSRV);
			// KHA====
			// vb.setCourrierCodeUnique(codeUniqueCourrier);
			// ====
			// [ID][Annee][Mois]//[SRV]/[Sens]/
			// ***********************************************************************
			// --------------------------------------MM----------------------------
			// Test
			
			if(nature.getNatureId()== 44 || nature.getNatureId()== 46){
				if(courrier.getAoConsultationId() != null ){
			showPanelAOC = true;
			numeroAoConsultation = courrier.getAoConsultationId().getAoConsultationId();
			int idaoconsultation = courrier.getAoConsultationId().getAoConsultationId();
			aoConsultation = appMgr.getListAoConsultation(idaoconsultation).get(0);
			heure1 = aoConsultation.getAoConsultationDateLimiteOffre().toString();
			heure3 = aoConsultation.getAoConsultationDateSeanceCommission().toString();
			if(aoConsultation.getAoConsultationDelaisProlongation()!= null ){
			heure2 = aoConsultation.getAoConsultationDelaisProlongation().toString();
			}
			}
			}
			messageDoc = "Ce courrier est déjà exécuté Vous ne pouvez pas le modifier.";
			listCourrierAffecteDM = new ListDataModel();
		//Les courriers liés
			//Les initialisations necessaires pour la requet
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			dateDebut = calendar.getTime();
			
			//********* Fin *********************//
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			dateFin = calendar.getTime();
			if (vb.getPerson() != null) {
			idUser = vb.getPerson().getId();
			}
			type = "";
			type1 = "";
			if (vb.getPerson().isBoc()) {
				type = "boc_" + vb.getPerson().getAssociatedBOC().getIdBOC();
				type1 = "";
			} else if (vb.getPerson().isResponsable()) {
				type = "unit_"
						+ vb.getPerson().getAssociatedDirection().getIdUnit();
				type1 = "sub_" + idUser;
				// NEW

				for (Unit unit : vb.getPerson().getAssociatedDirection()
						.getListUnitsChildUnit()) {
					
					listIdsSousUnit.add(unit.getIdUnit());
					 
					try {
						listIdsSubordonne
								.add(unit.getResponsibleUnit().getId());
						
					} catch (Exception e) {
						System.out.println("#Sub-Unit without Responsible");
					}

				}
				// FIn NEW
				try {
					typeSecretaire = "secretary_"
							+ vb.getPerson().getAssociatedDirection()
									.getSecretaryUnit().getId();
					
				} catch (NullPointerException e) {
					consultationCourrierSecretaire = "Non";
				}

			} else if (vb.getPerson().isSecretary()) {
				type = "secretary_" + idUser;
				type1 = "unit_"
						+ vb.getPerson().getAssociatedDirection().getIdUnit();
			} else if (vb.getPerson().isAgent()) {
				type = "agent_" + idUser;
				type1 = "";
			}

			List<CourrierInformations> courriersInformationsAffectes=new ArrayList<CourrierInformations>();
			listCourriersInformationsAffecte=new ArrayList<CourrierInformations>();
			courriersInformationsAffectes=appMgr.findCourrierEnvoyerANDRecuByCriteriaLies(vb.getPerson()
					.isResponsable(), listIdsSousUnit,
					listIdsSubordonne, filterMap, sortField,
					descending, consultationCourrierSecretaire,
					consultationCourrierSubordonne,
					consultationCourrierSousUnite, 7, dateDebut,
					dateFin, type, type1, typeSecretaire, idUser,
					typeTransmission, "",
					0, 10, false,6, "Tous", vb.getDbType(),courrier.getIdCourrier(),0,0);				
			System.out.println("Courriers Liées Affectées :  "+courriersInformationsAffectes.size());
			for (CourrierInformations courrierInformations : courriersInformationsAffectes) {
				courrierConsultation.searchExpediteurDestinataire(courrierInformations);
			}
			listCourriersInformationsAffecte.addAll(courriersInformationsAffectes);
			
			System.out.println("2019-10-28 listCourriersInformationsAffecte ===>"+listCourriersInformationsAffecte.size());
			
			listCourrierAffecteDM.setWrappedData(listCourriersInformationsAffecte);
			vb.setListCourriersInformationsAffecte(listCourriersInformationsAffecte);
		
			if (nature.getNatureId() == 38 || nature.getNatureId() == 59 || nature.getNatureId() == 80) {
				showPanelCheque = true;
			} else {
				showPanelCheque = false;
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void transfert() {
		
		vb.setFlagTransfert(true);	
		 List<Object> copyListSelectedObjectExpediteur = new ArrayList<Object>();
		 vb.setPremiereEntreeTransfert(1);
		
	
		if(vb.getPerson().isBoc() && vb.getPerson().getAssociatedBOC().getIdBOC()==1){
		
			vb.setTypeCourrier("depart");
			//acces au bouton transfert à pares consulter un courrier à partir de la liste 
			if(vb.getCourDossConsulterInformations()!=null){
			CourrierInformations consulterInformations = vb.getCourDossConsulterInformations();
			//l'expéditeur de courrier transfere = destinataire de courrier originale 
			vb.getPerson().setNom(consulterInformations.getCourrierDestinataireReelle());
			vb.getPerson().setPrenom(null);		
			
			if (consulterInformations.getListSelectedObject() != null) {
				if(consulterInformations.getListSelectedObject().get(0) instanceof Person){
					Person person = (Person) vb
					.getCopyListSelectedObjectExp().get(0);
					Person p=ldapOperation.getPersonalisedUserById(person.getId());
					copyListSelectedObjectExpediteur.add(p);
					vb.setCopyListSelectedObject(copyListSelectedObjectExpediteur);				
					vb.setCopyListSelectedObjectExp(copyListSelectedObjectExpediteur);
				}else{
					
					copyListSelectedObjectExpediteur.add(consulterInformations
							.getListSelectedObject().get(0));
				vb.setCopyListSelectedObject(copyListSelectedObjectExpediteur);
				vb.setCopyListSelectedObjectExp(copyListSelectedObjectExpediteur);	
				}
			}
			}else{
			   //acces au bouton transfert après ajout d'un courrier  	
			    List<ListeDestinatairesModel> listDestinataires = vb.getListeDestinataire();
				vb.getPerson().setNom(listDestinataires.get(0).getDestinataireName());
				vb.getPerson().setPrenom(null);
				Unit unit=new Unit();
				if(listDestinataires.get(0).getDestinataireType().equals("Unit")){
			     unit=ldapOperation.getUnitById(listDestinataires.get(0).getDestinataireId());
			     copyListSelectedObjectExpediteur.add(unit);
				}else{
					if(listDestinataires.get(0).getDestinataireType().equals("Person")){
						Person person=ldapOperation.getPersonalisedUserById(listDestinataires.get(0).getDestinataireId());
						copyListSelectedObjectExpediteur.add(person);
					}
				}
			
			
				vb.setCopyListSelectedObject(copyListSelectedObjectExpediteur);
				vb.setCopyListSelectedObjectExp(copyListSelectedObjectExpediteur);	
			}
			
			
		}
		
		vb.setListeDestinataire(new ArrayList<ListeDestinatairesModel>());
		vb.setCopyListSelectedUnit(new ArrayList<Unit>());
		vb.setListeDestinataire(new ArrayList<ListeDestinatairesModel>());
	}

	public void verificationLienCourrier() {
		Courrier courrier1;
		nbrCourrierLies = 0;
		vb.setListCourriersAffectes(new ArrayList<CourrierDossierListe>());
		List<Lienscourriers> liensCourriers = new ArrayList<Lienscourriers>();
		List<CourrierLiens> courrierLiens;
		liensCourriers = appMgr.getListCourrierLiensByIdCourrier(courrier
				.getIdCourrier());
		if (!liensCourriers.isEmpty()) {
			notLinkedMail = false;
			linkedMail = true;
			CourrierDossierListe courrierDossierListe;
			System.out.println("size Lien1 : " + liensCourriers.size());
			for (Lienscourriers lienscourriers : liensCourriers) {
				courrierLiens = new ArrayList<CourrierLiens>();
				courrierLiens = appMgr
						.getListLiensCourrierByIdCourrierLien(lienscourriers
								.getLiensCourrier());
				for (CourrierLiens courrierLiens2 : courrierLiens) {
					courrierDossierListe = new CourrierDossierListe();
					nbrCourrierLies = nbrCourrierLies + 1;
					courrier1 = new Courrier();
					courrier1 = appMgr.getCourrierByIdCourrier(
							courrierLiens2.getId().getIdCourrier()).get(0);
					System.out.println("Courrier ref : "
							+ courrier1.getCourrierReferenceCorrespondant());
					System.out.println("Courrier Id  : "
							+ courrier1.getIdCourrier());
					courrierDossierListe.setCourrier(courrier1);
					courrierDossierListe.setConfidentialite(appMgr
							.getConfidentialiteById(
									courrier1.getConfidentialite()
											.getConfidentialiteId()).get(0));
					courrierDossierListe.setUrgence(appMgr.getUrgenceById(
							courrier1.getUrgence().getUrgenceId()).get(0));
					courrierDossierListe.setTransmission(appMgr
							.getTransmissionById(
									courrier1.getTransmission()
											.getTransmissionId()).get(0));
					courrierDossierListe.setNature(appMgr.getNatureById(
							courrier1.getNature().getNatureId()).get(0));
					vb.getListCourriersAffectes().add(courrierDossierListe);
				}
			}
			if (nbrCourrierLies == 0) {
				notLinkedMail = true;
				linkedMail = false;
			}
		} else {
			notLinkedMail = true;
			linkedMail = false;
		}
	}

	public void reply() {
		try {
			// modifier a cause de redeveloppement de la liste courriers
			// CourrierConsulterInformations consulterInformations = new
			// CourrierConsulterInformations();
			// consulterInformations = vb.getCourDossConsulterInformations();
			// courrier = consulterInformations.getCourrier();
			// vb.setCourDossConsulterInformations(consulterInformations);
			CourrierInformations consulterInformations = new CourrierInformations();
			consulterInformations = vb.getCourDossConsulterInformations();
			if (consulterInformations.getCourrier() == null) {
				consulterInformations.setCourrier(appMgr
						.getCourrierByIdCourrier(
								consulterInformations.getCourrierID()).get(0));
			}
			// [JS]: 2019-05-20 Afficher référence dans transaction
			if (consulterInformations.getTransaction() == null) {
				consulterInformations.setTransaction(appMgr
						.getListTransactionByIdTransaction(
								consulterInformations.getTransactionID())
						.get(0));

			}
			courrier = consulterInformations.getCourrier();
			vb.setCourDossConsulterInformations(consulterInformations);
			// fin modifier a cause de redeveloppement de la liste courriers
			vb.setCourrier(courrier);
			// if (consulterInformations.getCourrierExpediteurObjet() instanceof
			// Person) {
			// Person person = new Person();
			// person = (Person) consulterInformations
			// .getCourrierExpediteurObjet();
			// vb.setCopyListSelectedPerson(new ArrayList<Person>());
			// vb.getCopyListSelectedPerson().add(
			// ldapOperation.getPersonalisedUserById(person.getId()));
			// vb.setDestNom(person.getCn());
			// } else if (consulterInformations.getCourrierExpediteurObjet()
			// instanceof Unit) {
			// Unit unit = new Unit();
			// unit = (Unit) consulterInformations
			// .getCourrierExpediteurObjet();
			// vb.setCopyListSelectedUnit(new ArrayList<Unit>());
			// vb.getCopyListSelectedUnit().add(unit);
			// vb.setDestNom(unit.getNameUnit());
			// }
			transaction = consulterInformations.getTransaction();
			
			vb.setCopyDestNom(consulterInformations
					.getCourrierDestinataireReelle());
			vb.setCopyExpNom(consulterInformations.getCourrierExpediteur());
			vb.setCopyCourrierCommentaire(consulterInformations
					.getCourrierCommentaire());
			vb.setToReplay(true);
			// C* search the real expediteur :
			if (consulterInformations.getCourrierAllTransactions() == null) {
				List<Transaction> allTransactions = appMgr
						.getTransactionByIdDossier(consulterInformations
								.getDossierID());
				consulterInformations
						.setCourrierAllTransactions(allTransactions);
			}
			Transaction firstTransaction = consulterInformations
					.getCourrierAllTransactions().get(
							consulterInformations.getCourrierAllTransactions()
									.size() - 1);
			Expdest realExpediteur = appMgr.getListExpDestByIdExpDest(
					firstTransaction.getExpdest().getIdExpDest()).get(0);
			consulterInformations.setExpDest(realExpediteur);
			//2020-06-12
			//l'expéditeur
			if(vb.getPerson().isBoc() && vb.getPerson().getAssociatedBOC().getIdBOC()==1){
				System.out.println("consulterInformations.getCourrierDestinataireReelle() "+consulterInformations.getCourrierDestinataireReelle());
				vb.setExpediteurCourrierRepondre(consulterInformations.getCourrierDestinataireReelle());
				}		
			if (realExpediteur.getTypeExpDest().equals("Interne-Person")) {
				// Person person =
				// vb.getHashMapAllUser().get(courrierInformations.getExpDest().getIdExpDestLdap());
				Person person = ldapOperation
						.getPersonalisedUserById(consulterInformations
								.getExpDest().getIdExpDestLdap());
				vb.getCopyListSelectedPerson().add(person);
				vb.setDestNom(person.getCn());
				// *** kha- ajoute 12-02-2019
				vb.setDestinataireId(person.getId());
				ListeDestinatairesModel dest = new ListeDestinatairesModel();
				dest.setDestinataireId(person.getId());
				dest.setDestinataireName(person.getCn());
				destinataireRepondre.add(dest);
				vb.setListeDestinataire(destinataireRepondre);

			} else if (realExpediteur.getTypeExpDest().equals("Interne-Unité")) {
				Unit unit = vb.getHashMapUnit().get(
						consulterInformations.getExpDest().getIdExpDestLdap());
				vb.getCopyListSelectedUnit().add(unit);
				vb.setDestNom(unit.getNameUnit());
				// *** kha- ajoute 12-02-2019
				vb.setDestinataireId(unit.getIdUnit());
				ListeDestinatairesModel dest = new ListeDestinatairesModel();
				dest.setDestinataireId(unit.getIdUnit());
				dest.setDestinataireName(unit.getNameUnit());

				destinataireRepondre.add(dest);
				vb.setListeDestinataire(destinataireRepondre);
				// }
				// else if (realExpediteur.getTypeExpDest()
				// .equals("Interne-Boc")) {
				// expediteur.append(vb.getCentralBoc().getNameBOC());

			} else if (realExpediteur.getTypeExpDest().equals("Externe")) {
				Expdestexterne realExpediteurExterne = appMgr
						.getExpediteurById(
								realExpediteur.getExpdestexterne()
										.getIdExpDestExterne()).get(0);
				if (realExpediteurExterne.getTypeutilisateur()
						.getTypeUtilisateurLibelle().equals("PP")) {
					Pp pp = appMgr.getPPByReferenceExpediteur(
							realExpediteurExterne.getIdExpDestExterne()).get(0);
					pp.setExpdestexterne(realExpediteurExterne);
					vb.getCopyListPP().add(pp);
					vb.setDestNom(realExpediteurExterne.getExpDestExterneNom()
							+ " "
							+ realExpediteurExterne.getExpDestExternePrenom());
					// *** kha- ajoute 12-02-2019
					vb.setDestinataireId(pp.getExpdestexterne()
							.getIdExpDestExterne());
					ListeDestinatairesModel dest = new ListeDestinatairesModel();
					dest.setDestinataireId(pp.getExpdestexterne()
							.getIdExpDestExterne());
					dest.setDestinataireName(pp.getExpdestexterne()
							.getExpDestExterneNom());

					destinataireRepondre.add(dest);
					vb.setListeDestinataire(destinataireRepondre);
				} else {
					Pm pm = appMgr.getPMByReferenceExpediteur(
							realExpediteurExterne.getIdExpDestExterne()).get(0);
					pm.setExpdestexterne(realExpediteurExterne);
					// pm.setExpdestexterne(expdestexterne)
					vb.getCopyListPM().add(pm);
					vb.setDestNom(realExpediteur.getExpdestexterne()
							.getExpDestExterneNom());
					// *** kha- ajoute 12-02-2019
					vb.setDestinataireId(pm.getExpdestexterne()
							.getIdExpDestExterne());
					ListeDestinatairesModel dest = new ListeDestinatairesModel();
					dest.setDestinataireId(pm.getExpdestexterne()
							.getIdExpDestExterne());
					dest.setDestinataireName(pm.getExpdestexterne()
							.getExpDestExterneNom());
					destinataireRepondre.add(dest);
					vb.setListeDestinataire(destinataireRepondre);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void modify() {
//		System.out.println("modiiiiif========="+vb.getCodeUniqueCourrier());
//		System.out.println(vb.getDestinataireReel());
		vb.setDestNom(vb.getDestinataireReel());
		vb.setExpNom(vb.getCopyExpReelNom());
		vb.setUploadType("");
		vb.setFlagDocuPhysique(1);
		vb.setAffichePanelModifUpload(false);
		vb.setMasquerPanelDetailCourrier(true);
		vb.setAffichePanelAjoutUpload(true);
		//2020-06-08
		vb.setAoConsultation(null);
		codeUniqueCourrier=vb.getCodeUniqueCourrier();
		status = false;
		 status4=false;
		 vb.setFlagModif(1);
		statusCanModif = false;
		//2020-06-08
//		if(courrier.getAoConsultationId()!=null)
//		vb.setAoConsultation(courrier.getAoConsultationId());
		String result = "";
		Expdest expDest;
		System.out.println("AH  :  " + transaction.getTransactionId());
		
		
		List<Transaction> allTransactions = appMgr
		.getTransactionByIdDossier(transaction.getDossier()
				.getDossierId());
		vb.setAllTransactions(allTransactions);
		
		System.out.println(vb.getDestinataireReel());
		List<TransactionDestination> transactionDestinations = new ArrayList<TransactionDestination>();
		transactionDestinations = appMgr
				.getDestinationByIdTransaction(transaction.getTransactionId());
		
		// List<Transaction> listTransaction =
		// appMgr.getTransactionByIdDossier(transaction
		// .getDossier().getDossierId());
		// !vb.getPerson().isBoc()
		// && listTransaction.size() != 1
		try {
			System.out.println("courrier ++"
					+ vb.getCourrier().getCourrierCircuit());
			if ((vb.getCourrier().getCourrierCircuit()
					.equalsIgnoreCase("workflow"))
					|| vb.getCourrier().getCourrierEtatCloture() == 1) {
				status = true;
				status4=true;

				if (vb.getCourrier().getCourrierEtatCloture() == 1) {
					setMessage(messageSource.getMessage(
							"cantModifyCourrierCloturer", new Object[] {},
							lm.createLocal()));
				} else {
					setMessage(messageSource.getMessage(
							"cantModifyCourrierWorkflow", new Object[] {},
							lm.createLocal()));
				}

			} 
			else {
				
//				System.out.println("transaction +++"
//						+ transaction.getIdUtilisateur());
//				System.out.println("transaction.getIdUtilisateur() "+transaction.getIdUtilisateur());
//				System.out.println("vb.getPerson().getId() "+vb.getPerson().getId());
				if (transaction.getIdUtilisateur() != vb.getPerson().getId()) {
				
					
							// Vous n'avez pas le droit de modifier ce courrier.vous
							// n'étes pas son Editeur.
							setMessage(messageSource.getMessage("cannotModifyMail",
									new Object[] {}, lm.createLocal()));
							setMessageDoc(messageSource.getMessage("cannotModifyDoc",
									new Object[] {}, lm.createLocal()));
							status = true;
							status4=true;
					
//						 System.out.println("Dans else");
//						 System.out.println("courrier.getTransmission().getTransmissionId() : "+courrier.getTransmission().getTransmissionId());
						 if(courrier.getTransmission().getTransmissionId()==11 && vb .getPerson().isBoc()){
						 status=false;
						 status4=true;
						 vb.setModeEnveloppe(true);
						 }
					 
				} else {
					

					if (transaction.getTransactionDateConsultation()!=null  &&

						transactionDestinations.get(0).getTransactionDestDateConsultation()!=null &&  !vb.getPerson().isBoc()) {

						setMessage(messageSource.getMessage(
								"cantModifyCourrier", new Object[] {},
								lm.createLocal()));
						status = true;
						status4=true;

					} else {

//						System.out.println("transactionDestinations +++"
//								+ transactionDestinations.isEmpty());
						for (TransactionDestination transactionDestination : transactionDestinations) {
							expDest = new Expdest();
							expDest = appMgr.getListExpDestByIdExpDest(
									transactionDestination.getId()
											.getIdExpDest()).get(0);
							System.out.println("expdest ++" + expDest);
							if (expDest.getTypeExpDest().equals(
									"Interne-Person")) {
								result = result
										+ ldapOperation.getCnById(
												ldapOperation.CONTEXT_USER,
												"uid",
												expDest.getIdExpDestLdap())
										+ " / ";
							} else if (expDest.getTypeExpDest().equals(
									"Interne-Unité")) {
								result = result
										+ ldapOperation.getCnById(
												ldapOperation.CONTEXT_UNIT,
												"departmentNumber",
												expDest.getIdExpDestLdap())
										+ " / ";
							} else if (expDest.getTypeExpDest().equals(
									"Externe")) {
								if (expDest.getExpdestexterne()
										.getTypeutilisateur()
										.getTypeUtilisateurLibelle()
										.equals("PP")) {
									result = result
											+ expDest.getExpdestexterne()
													.getExpDestExterneNom()
											+ " "
											+ expDest.getExpdestexterne()
													.getExpDestExternePrenom()
											+ " (PP)" + " / ";
								} else {
									result = result
											+ expDest.getExpdestexterne()
													.getExpDestExterneNom()
											+ " (PM)" + " / ";
								}
							}
							// #!transactionDestination.getTransactionDestTypeIntervenant().equals("boc_1")"
							// ajouté pour donner l'exception au courrier
							// d'arrivé
							// qui sont envoyé automatiquement au nom du Bureau
							// d'ordre en premier lieu

							// if
							// (!transactionDestination.getTransactionDestTypeIntervenant().equals("boc_1")
							// && transactionDestination
							// .getTransactionDestDateConsultation() != null) {
							// status = true;
							// setMessage(messageSource.getMessage(
							// "confirmModifyMail", new Object[] {},
							// lm.createLocal()));
							// break;
							// }
						}
						if (transaction.getIdUtilisateur() == vb.getPerson()
								.getId() && !status && !status4) {
							if (!result.equals("")) {
								int lastIndex = result.lastIndexOf("/");
								result = result.substring(0, lastIndex);
								vb.setDestNom(result);
							}
							if (vb.getPerson().isBoc()) {
								vb.setExpNom(vb.getCopyExpNom());
								vb.setDestNom(vb.getDestinataireReel());
								// vb.setExpNom(vb.getCourDossConsulterInformations()
								// .getCourrierExpediteur());
							}

							System.out
									.println(vb.getDestNom()
											+ "::::::::::::::::::::::"
											+ vb.getExpNom());
							System.out.println(vb.getCopyExpNom()
									+ "::::::::::::::::::::::"
									+ vb.getDestinataireReel());
						}
					}
				}
			}
			vb.setListComposantDynamiqueTransmission(new ArrayList<ComposantDynamique>());
			vb.setListComposantDynamiqueNature(new ArrayList<ComposantDynamique>());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e.toString());
		}
	}

	public void classement() {
		statusClassement = false;
		statusNonClasse = false;
		selectedItemArmoire = "";
		selectedItemEtages = "";
		
		if(transaction.getClassement_archivage_niveau_01()==null){
		//if (courrier.getEtages() == null) {
			setMessage(messageSource.getMessage("ClassementCourrierNonFait",
					new Object[] {}, lm.createLocal()));
			statusNonClasse = true;
		} else {
			// Etages etages = new Etages();
			// Armoire armoire = new Armoire();
			//int refEtage = courrier.getEtages().getIdetages();
			int refEtage = transaction.getClassement_archivage_niveau_01().getClassement_archivage_niveau_01_Id();
			etages = appMgr.listEtagesById(refEtage).get(0);
			vb.setEtages(etages);
			int refArmoire = etages.getClassement_archivage_niveau_02().getClassement_archivage_niveau_02_Id();
			armoire = appMgr.listArmoireById(refArmoire).get(0);
			vb.setArmoire(armoire);
			setMessage(messageSource.getMessage("ClassementCourrierFait",
					new Object[] {}, lm.createLocal()));
			String ar = messageSource.getMessage("Armoire", new Object[] {},
					lm.createLocal());
			String et = messageSource.getMessage("Etages", new Object[] {},
					lm.createLocal());

			messageInfoCourrierClassement = ar + " : "
					+ armoire.getClassement_archivage_niveau_02_Libelle() + " => " + et + " : "
					+ etages.getClassement_archivage_niveau_01_Libelle();
			statusClassement = true;
		}
	}

	public void updateClassement() {
		if(existeNiveau02){
		selectedItemArmoire = armoire.getClassement_archivage_niveau_02_Id().toString();

		
		listEtages = appMgr.listEtagesByIdArmoire(Integer
				.valueOf(selectedItemArmoire));
		}
		else 
			
			listEtages= appMgr.getListClassementNiveau01ByTypeConnecte(
					pocesseurId, typePocesseur);
			
		selectedItemEtages = etages.getClassement_archivage_niveau_01_Id().toString();
		showUpdateClassement = true;
	}

	public void validateUpdateClassement() {
		statusClasseSucces = false;
		statusClasseErreur = false;
		if(existeNiveau02){

		if (selectedItemArmoire != "" && selectedItemEtages != "") {
			try {
				courrier.setCourrierDateClassement(new Date());
				appMgr.update(courrier);
				/******************** Changement etat Etages && Armoires ***********************/
				// decrementation de nombre de dossier de l'ancienne etage
				etages.setClassement_archivage_niveau_01_NombreDossiers(etages.getClassement_archivage_niveau_01_NombreDossiers() - 1);
				appMgr.update(etages);
				// incrementation de nombre de dossier de la nouvelle etage
				// int refEtage = courrier.getEtages().getIdetages();
				Classement_archivage_niveau_01 et = appMgr.listEtagesById(
						Integer.valueOf(selectedItemEtages)).get(0);
				int nbDossier = et.getClassement_archivage_niveau_01_NombreDossiers();
				nbDossier++;
				et.setClassement_archivage_niveau_01_NombreDossiers(nbDossier);
				appMgr.update(et);

			} catch (Exception e) {
				e.printStackTrace();
			}
			setMessage(messageSource.getMessage(
					"changerClassementCourrierSucces", new Object[] {},
					lm.createLocal()));
			statusClasseSucces = true;
		} else {
			setMessage(messageSource.getMessage(
					"changerClassementCourrierErreur", new Object[] {},
					lm.createLocal()));
			statusClasseErreur = true;
		}
		}
		else{
			if (selectedItemEtages != "") {
				try {
					courrier.setCourrierDateClassement(new Date());
					appMgr.update(courrier);
					/******************** Changement etat Etages && Armoires ***********************/
					// decrementation de nombre de dossier de l'ancienne etage
					etages.setClassement_archivage_niveau_01_NombreDossiers(etages.getClassement_archivage_niveau_01_NombreDossiers() - 1);
					appMgr.update(etages);
					// incrementation de nombre de dossier de la nouvelle etage
					// int refEtage = courrier.getEtages().getIdetages();
					Classement_archivage_niveau_01 et = appMgr.listEtagesById(
							Integer.valueOf(selectedItemEtages)).get(0);
					int nbDossier = et.getClassement_archivage_niveau_01_NombreDossiers();
					nbDossier++;
					et.setClassement_archivage_niveau_01_NombreDossiers(nbDossier);
					appMgr.update(et);

				} catch (Exception e) {
					e.printStackTrace();
				}
				setMessage(messageSource.getMessage(
						"changerClassementCourrierSucces", new Object[] {},
						lm.createLocal()));
				statusClasseSucces = true;
			} else {
				setMessage(messageSource.getMessage(
						"changerClassementCourrierErreur", new Object[] {},
						lm.createLocal()));
				statusClasseErreur = true;
			}
			
			
		}
	}

	public void getConfirmDeclassement() {
		setMessage(messageSource.getMessage("declassementConfirmation",
				new Object[] {}, lm.createLocal()));
	}

	public void deleteClassement() {
		try {
			//courrier.setEtages(null);
			transaction.setClassement_archivage_niveau_01(null);
			appMgr.update(transaction);
			// decrementation de nombre de dossier de l'ancienne etage
			etages.setClassement_archivage_niveau_01_NombreDossiers(etages.getClassement_archivage_niveau_01_NombreDossiers() - 1);
			appMgr.update(etages);
			setMessage(messageSource.getMessage("declassementCourrierSucces",
					new Object[] {}, lm.createLocal()));
		} catch (Exception e) {
			setMessage(messageSource.getMessage("declassementCourrierErreur",
					new Object[] {}, lm.createLocal()));
			e.printStackTrace();
		}
	}

	// fonction de selection des armoire
	public List<SelectItem> getSelectItemsArmoire() {
		String libelle;
		List<SelectItem> selectItemsArmoire = new ArrayList<SelectItem>();
		selectItemsArmoire.add(new SelectItem(""));
		listArmoire=appMgr.getListClassementNiveau02ByTypeConnecte(pocesseurId,typePocesseur);
		for (int j = 0; j <= listArmoire.size() - 1; j++) {
			Integer idArmoire = listArmoire.get(j).getClassement_archivage_niveau_02_Id();
			if (vb.getLocale().equals("ar")) {
				libelle = listArmoire.get(j).getClassement_archivage_niveau_02_LibelleAr();
			} else {
				libelle = listArmoire.get(j).getClassement_archivage_niveau_02_Libelle();
			}
			// selectItemsArmoire.add(new SelectItem(listArmoire.get(j)
			// .getArmoireLibelle()));
			selectItemsArmoire.add(new SelectItem(String.valueOf(idArmoire),
					libelle));
		}
		return selectItemsArmoire;
	}

	public void chargerEtages(ActionEvent evt) {

		listEtages = new ArrayList<Classement_archivage_niveau_01>();
		listEtages = appMgr.listEtagesByIdArmoire(Integer
				.valueOf(selectedItemArmoire));
//		System.out.println("selectedItemArmoire : " + selectedItemArmoire);
		getSelectItemsEtages();
//		System.out.println("@" + evt.getComponent().getId());
		if (evt.getComponent().getId().equals("suppUpdateChargeEtages")) {
			selectedItemEtages = "";
		}

	}

	public void chargerSelectedEtages(ActionEvent evt) {
		if (evt.getComponent().getId().equals("supportEtages")) {
			// EtageChanged = true;
			// setAncienEtages(etages);
		}
		Classement_archivage_niveau_01 et = appMgr.listEtagesById(Integer.valueOf(selectedItemEtages))
				.get(0);
	//	courrier.setEtages(et);
		transaction.setClassement_archivage_niveau_01(et);
	}

	public List<SelectItem> getSelectItemsEtages() {
		String libelle;
		List<SelectItem> selectItemsEtages = new ArrayList<SelectItem>();
		selectItemsEtages.add(new SelectItem(""));
		
		listEtages= appMgr.getListClassementNiveau01ByTypeConnecte(
				pocesseurId, typePocesseur);
		
		for (int j = 0; j <= listEtages.size() - 1; j++) {
			Integer idEtage = listEtages.get(j).getClassement_archivage_niveau_01_Id();
			if (vb.getLocale().equals("ar")) {
				libelle = listEtages.get(j).getClassement_archivage_niveau_01_LibelleAr();
			} else {
				libelle = listEtages.get(j).getClassement_archivage_niveau_01_Libelle();
			}
			selectItemsEtages.add(new SelectItem(String.valueOf(idEtage),
					libelle));
		}
		// The values are the keys passed to the selectItem property.
		// The labels are those what you see on the menu.
		return selectItemsEtages;

	}

	public void classementCourrier() {
		statusClasseSucces = false;
		statusClasseErreur = false;
		if(existeNiveau02){

		if (selectedItemArmoire != "" && selectedItemEtages != "") {
			try {
				//transaction.setClassement_archivage_niveau_01(classement_archivage_niveau_01);
				courrier.setCourrierDateClassement(new Date());
				appMgr.update(courrier);
				/******************** Changement etat Etages && Armoires ***********************/
				// int refEtage = courrier.getEtages().getIdetages();
				Classement_archivage_niveau_01 et = appMgr.listEtagesById(
						Integer.valueOf(selectedItemEtages)).get(0);
				int nbDossier = et.getClassement_archivage_niveau_01_NombreDossiers();
				nbDossier++;
				et.setClassement_archivage_niveau_01_NombreDossiers(nbDossier);
				appMgr.update(et);

			} catch (Exception e) {
				System.out.println("erreur mettre ajour courrier");
				e.printStackTrace();
			}
			setMessage(messageSource.getMessage("ClassementCourrierSucces",
					new Object[] {}, lm.createLocal()));
			statusClasseSucces = true;
		} else {
			setMessage(messageSource.getMessage("ClassementCourrierErreur",
					new Object[] {}, lm.createLocal()));
			statusClasseErreur = true;
		}
		}
		else{

			if (selectedItemEtages != "") {
				try {
					//transaction.setClassement_archivage_niveau_01(classement_archivage_niveau_01);
					courrier.setCourrierDateClassement(new Date());
					appMgr.update(courrier);
					/******************** Changement etat Etages && Armoires ***********************/
					// int refEtage = courrier.getEtages().getIdetages();
					Classement_archivage_niveau_01 et = appMgr.listEtagesById(
							Integer.valueOf(selectedItemEtages)).get(0);
					int nbDossier = et.getClassement_archivage_niveau_01_NombreDossiers();
					nbDossier++;
					et.setClassement_archivage_niveau_01_NombreDossiers(nbDossier);
					appMgr.update(et);

				} catch (Exception e) {
					System.out.println("erreur mettre ajour courrier");
					e.printStackTrace();
				}
				setMessage(messageSource.getMessage("ClassementCourrierSucces",
						new Object[] {}, lm.createLocal()));
				statusClasseSucces = true;
			} else {
				setMessage(messageSource.getMessage("ClassementCourrierErreur",
						new Object[] {}, lm.createLocal()));
				statusClasseErreur = true;
			}
			
			
		}

	}

	public void evenementChoixTransfert(ActionEvent evt) {
		if (reponse1.equals("Non")) {
			setSelect1(false);
		} else {
			setSelect1(true);
		}
	}

	public String accesAccuseReception() {
		vb.setMasquerPanelDetailCourrier(false);
		String res = "NoPass";
		courrier = vb.getCourrier();
		statusAccuseReception = false;
		vb.setFlagDocuPhysique(2);
		int r = transaction.getEtat().getEtatId();
		if (appMgr.listEtatByRef(r).get(0).getEtatId().equals(6)) {
			List<Suivitransmissioncourrier> listAccuse = appMgr.accuseReceptionByIdCourrier(courrier.getIdCourrier());
			if (listAccuse.size() == 0) {
				res = "passAjouter";
			} else {
				Suivitransmissioncourrier accuse = listAccuse.get(listAccuse.size()-1);
				if(accuse.getEtat().getEtatId().intValue()==8)
				res = "passConsulter";
				else
					res = "passModifier";
			}
		} /*
		 * else { setMessage(messageSource.getMessage(
		 * "AccuseReceptionVerificationTraitement", new Object[] {},
		 * lm.createLocal())); statusAccuseReception = true; }
		 */
		return res;
	}

	public String goToValidate() {
		return "fromCourrierDetails";
	}

	// *** AC: Fonction retour en arriere
	public String backwardPage() {
		vb.setOldPage(true);
		if (from.equals("rechercheToCourrierDetails")) {
			return "rechercheToCourrierDetails";
		} 
		else if(from.equals("detailToLien")){
			//Mettre à jour le context par l"ancient et refresh la mêmem page
		
			return "";
		}
		else {
			if (vb.getSelectedListCourrier().equals("CRjour")) {
				vb.setSelectedListCourrier("CRjour");
			} else if (vb.getSelectedListCourrier().equals("CRmois")) {
				vb.setSelectedListCourrier("CRmois");
			} else {
				vb.setSelectedListCourrier("CRannee");
			}
			return "courrierDetailsToListeCourrier";
		}
	}

	// --------------------------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------MM--------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------
	// -----Cest une methode pour reactiver un courrier
	// -------------------------------------------------------------------------------

	public void cloturerCourrier() throws Exception {
		Courrier courrierClot = new Courrier();
		courrierClot = vb.getCourrier();
		courrierClot.setCourrierEtatCloture(1);
		appMgr.update(courrierClot);
		LogClass logClass = new LogClass();
//		System.out.println("###########"+vb.getCourrier().getIdCourrier());
//		System.out.println("###########"+vb.getCourrier().getCourrierReferenceCorrespondant());
//		System.out.println("###########"+vb.getPerson());
		logClass.addTrack("Cloture d'un courrier","Evénement de log de cloture d'un courrier "
						+ vb.getCourrier().getIdCourrier() + "-"
						+ vb.getCourrier().getCourrierReferenceCorrespondant(),
				vb.getPerson(), "INFO", appMgr);

	}

	// --------------------------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------------------------------MM--------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------
	// -----Cest une methode pour cloturer un
	// courrier---------------------------------------------------------------------------------

	public void reactiverCourrier() throws Exception {
		Courrier courrierClot = new Courrier();
		courrierClot = vb.getCourrier();
		courrierClot.setCourrierEtatCloture(0);
		appMgr.update(courrierClot);
		LogClass logClass = new LogClass();
		logClass.addTrack("Réactivation d'un courrier",
				"Evénement de log de réactivation d'un courrier "
						+ vb.getCourrier().getIdCourrier() + "-"
						+ vb.getCourrier().getCourrierObjet(), vb.getPerson(),
				"INFO", appMgr);
	}

	// MM
	// Methode pour la copie d'un courrier
	//
	public void copierCourrier() {
		courrier = new Courrier();

		Dossier dossier = new Dossier();
		Typedossier typedossier = new Typedossier();
		Transaction nouvelleTransaction = new Transaction();
		Etat etat = new Etat();
		TransactionDestinationId id;
		TransactionDestination trDest;
		Expdest expdest;
		Typetransaction typetransaction;
		Transaction copyNouvelleTransaction;
		TransactionDestinationReelle transactionDestinationReelle;
		id = new TransactionDestinationId();
		try {

			// C*
			Variables passageCourrierArriveAuDG = appMgr.listVariablesById(13)
					.get(0);
			// C*

			courrier.setCourrierDateReception(vb.getCourrier()
					.getCourrierDateReception());
			courrier.setCourrierDateReceptionReelle(vb.getCourrier()
					.getCourrierDateReceptionReelle());
			courrier.setNature(vb.getCourrier().getNature()); // **
			courrier.setTransmission(vb.getCourrier().getTransmission());
			courrier.setConfidentialite(vb.getCourrier().getConfidentialite());
			courrier.setUrgence(vb.getCourrier().getUrgence());
			courrier.setCourrierNecessiteReponse(vb.getCourrier()
					.getCourrierNecessiteReponse());
			courrier.setCourrierDateSysteme(new Date());
			courrier.setCourrierSupprime(true);
			courrier.setCourrierCircuit("Libre");
			courrier.setCourrierObjet(vb.getCourrier().getCourrierObjet());
			courrier.setKeywords(vb.getCourrier().getKeywords());
			courrier.setCourrierflagArchive(0);
			courrier.setCourrierReferenceCorrespondant(vb.getCourrier()
					.getCourrierReferenceCorrespondant());
			courrier.setCourrierCommentaire(vb.getCourrier()
					.getCourrierCommentaire());
			courrier.setCourrierOldDateOper(vb.getCourrier()
					.getCourrierOldDateOper());
			courrier.setCourrierCommentaireAr(vb.getCourrier()
					.getCourrierObjetAr());
			courrier.setCourrierDateArchivage(vb.getCourrier()
					.getCourrierDateArchivage());
			courrier.setCourrierDateClassement(vb.getCourrier()
					.getCourrierDateClassement());
			courrier.setCourrierOldNum(vb.getCourrier().getCourrierOldNum());
			courrier.setCourrierType(vb.getCourrier().getCourrierType());
			courrier.setCourrierTypeOrdre(vb.getCourrier()
					.getCourrierTypeOrdre());

			// -----------------------------------------------------------------------------------------------------------------------------
			// MM : Dossier début
			// ==========================================================================================================
			/**** Ajout Dossier *******/

			// dossier.setDossierDateCreation(new Date());
			// dossier.setDossierDescription(courrier.getCourrierCommentaire());
			// dossier.setDossierIntitule("Courrier_" +
			// courrier.getCourrierReferenceCorrespondant());
			// dossier.setDossierSupprime(true);
			// typedossier = appMgr.getTypeDossierById(1).get(0);
			// dossier.setTypedossier(typedossier);
			// appMgr.insert(dossier);

			// MM:Fin Insersion dans la table Dossier

			// vb.setDossier(dossier);
			// MM : Dossier fin
			// ============================================================================================================
			// -----------------------------------------------------------------------------------------------------------------------------

			courrier.setCourrierOldDateOper(2016);
			// courrier.setIdCourrier(null);
			// courrier.setIdCourrier(new
			// Integer(vb.getCourrier().getIdCourrier()+5));
			// courrier.setIdCourrier(vb.getCourrier().getIdCourrier()+3);
			// MM: Fin insersion Courrier

			dossier.setConfidentialite(vb.getCourrier().getConfidentialite());
			dossier.setDossierDateCreation(new Date());
			dossier.setDossierDescription(vb.getCourrier()
					.getCourrierCommentaire());
			dossier.setDossierIntitule("Courrier_"
					+ vb.getCourrier().getCourrierReferenceCorrespondant());
			dossier.setDossierSupprime(true);
			typedossier = appMgr.getTypeDossierById(1).get(0);
			dossier.setTypedossier(typedossier);
			appMgr.insert(dossier);
			vb.setDossier(dossier);
			// MM: Fin insersion Courrier
			appMgr.insert(courrier);
			// ansien courrier
			CourrierDossier courrierDoss = appMgr
					.getCourrierDossierByIdCourrier(
							vb.getCourrier().getIdCourrier()).get(0);
			int refdossier = courrierDoss.getId().getDossierId();
			List<Transaction> transactionList = new ArrayList<Transaction>();
			transactionList = appMgr.getTransactionByIdDossier(refdossier);
			Transaction transaction = new Transaction();
			transaction.setDossier(dossier);

			// copy courrier
			CourrierDossierId courrierDossierId = new CourrierDossierId();
			CourrierDossier courrierDossier = new CourrierDossier();
			courrierDossierId.setDossierId(dossier.getDossierId());
			courrierDossierId.setIdCourrier(courrier.getIdCourrier());
			courrierDossier.setId(courrierDossierId);
			// MM: insersion dans la table CourrierDossier.

			appMgr.insert(courrierDossier);
			// zone 2

			List<TransactionDestinationReelle> transactDestReel = appMgr
					.listTransactionDestinationReelByDossierID(vb.getCourrier()
							.getIdCourrier());
			TransactionDestinationReelle transactDestReelOreginal = new TransactionDestinationReelle();
			TransactionDestinationReelle transactDestReelCopy;

			if (transactDestReel.size() > 0) {
				transactDestReelOreginal = transactDestReel.get(0);
				transactDestReelCopy = new TransactionDestinationReelle();
				transactDestReelCopy
						.setTransactionDestinationReelleDateReception(transactDestReelOreginal
								.getTransactionDestinationReelleDateReception());
				transactDestReelCopy
						.setTransactionDestinationReelleDateTraitement(transactDestReelOreginal
								.getTransactionDestinationReelleDateTraitement());
				transactDestReelCopy
						.setTransactionDestinationReelleIdDestinataire(transactDestReelOreginal
								.getTransactionDestinationReelleIdDestinataire());
				transactDestReelCopy
						.setTransactionDestinationReelleTypeDestinataire(transactDestReelOreginal
								.getTransactionDestinationReelleTypeDestinataire());
				appMgr.insert(transactDestReelCopy);

			}

			// fin

			expdest = new Expdest();
			List<Expdest> expdestListTest = new ArrayList<Expdest>();
			List<TransactionDestination> listTransactionDestination = new ArrayList<TransactionDestination>();
			for (Transaction transaction2 : transactionList) {
				int refExped = transaction2.getExpdest().getIdExpDest();
				int refTransacation = transaction2.getTransactionId();
				expdestListTest.addAll(appMgr
						.getListExpDestByIdExpDest(refExped));
				transaction.setExpdest(expdestListTest.get(0));
				listTransactionDestination
						.addAll(appMgr
								.getListTransactionDestinationByIdTransaction(refTransacation));
			}
			for (Expdest e : expdestListTest) {
				Transaction t = new Transaction();
				t = appMgr.getListTransactionByIdExpDest(e.getIdExpDest()).get(
						0);
				appMgr.insert(e);
				t.setExpdest(e);
				t.setDossier(dossier);
				appMgr.insert(t);
				id.setIdTransaction(t.getTransactionId());
			}
			expdestListTest = new ArrayList<Expdest>();
			for (TransactionDestination transactionDestination : listTransactionDestination) {
				int refExped = transactionDestination.getId().getIdExpDest();
				expdestListTest.addAll(appMgr
						.getListExpDestByIdExpDest(refExped));
			}
			appMgr.insert(transaction);
			for (Expdest e : expdestListTest) {
				trDest = new TransactionDestination();
				trDest = appMgr.getListTransactionDestinationByIdExpDest(
						e.getIdExpDest()).get(0);
				appMgr.insert(e);
				id.setIdExpDest(e.getIdExpDest());
				trDest.setId(id);
				appMgr.insert(trDest);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void copierCourrier12() throws Exception {
		Courrier courrierAnsien = new Courrier();
		Courrier courrierNouv = new Courrier();
		courrierAnsien = vb.getCourrier();
		courrierNouv = vb.getCourrier();
		courrierNouv.setCourrierReferenceCorrespondant(vb.getCourrier()
				.getCourrierReferenceCorrespondant());
		Dossier dossier = new Dossier();
		Typedossier typedossier = new Typedossier();
		Transaction nouvelleTransaction = new Transaction();
		Etat etat = new Etat();
		TransactionDestinationId id;
		id = new TransactionDestinationId();
		TransactionDestination trDest;
		Expdest expdest;
		Typetransaction typetransaction;
		Transaction copyNouvelleTransaction;
		TransactionDestinationReelle transactionDestinationReelle;
		try {
			Variables passageCourrierArriveAuDG = appMgr.listVariablesById(13)
					.get(0);
			dossier.setConfidentialite(courrierAnsien.getConfidentialite());
			dossier.setDossierDateCreation(new Date());
			dossier.setDossierDescription(courrierAnsien
					.getCourrierCommentaire());
			dossier.setDossierIntitule("Courrier_"
					+ courrierAnsien.getCourrierReferenceCorrespondant());
			dossier.setDossierSupprime(true);
			typedossier = appMgr.getTypeDossierById(1).get(0);
			dossier.setTypedossier(typedossier);
			appMgr.insert(dossier);
			vb.setDossier(dossier);
			// MM: Fin insersion Courrier
			vb.setCourrier(courrierNouv);
			// ansien courrier
			CourrierDossier courrierDoss = appMgr
					.getCourrierDossierByIdCourrier(
							courrierAnsien.getIdCourrier()).get(0);
			int refdossier = courrierDoss.getId().getDossierId();
			List<Transaction> transactionList = new ArrayList<Transaction>();
			transactionList = appMgr.getTransactionByIdDossier(refdossier);
			// copy courrier
			CourrierDossierId courrierDossierId = new CourrierDossierId();
			CourrierDossier courrierDossier = new CourrierDossier();
			courrierDossierId.setDossierId(dossier.getDossierId());
			courrierDossierId.setIdCourrier(courrierNouv.getIdCourrier());
			courrierDossier.setId(courrierDossierId);
			// MM: insersion dans la table CourrierDossier.
			appMgr.insert(courrierDossier);
			expdest = new Expdest();
			List<Expdest> expdestListTest = new ArrayList<Expdest>();
			List<TransactionDestination> listTransactionDestination = new ArrayList<TransactionDestination>();
			for (Transaction transaction2 : transactionList) {
				int refExped = transaction2.getExpdest().getIdExpDest();
				int refTransacation = transaction2.getTransactionId();
				expdestListTest.addAll(appMgr
						.getListExpDestByIdExpDest(refExped));
				listTransactionDestination
						.addAll(appMgr
								.getListTransactionDestinationByIdTransaction(refTransacation));
			}
			for (Expdest e : expdestListTest) {
				Transaction t = new Transaction();
				t = appMgr.getListTransactionByIdExpDest(e.getIdExpDest()).get(
						0);
				appMgr.insert(e);
				t.setExpdest(e);
				t.setDossier(dossier);
				appMgr.insert(t);
				id.setIdTransaction(t.getTransactionId());
			}
			expdestListTest = new ArrayList<Expdest>();
			for (TransactionDestination transactionDestination : listTransactionDestination) {
				int refExped = transactionDestination.getId().getIdExpDest();
				expdestListTest.addAll(appMgr
						.getListExpDestByIdExpDest(refExped));
			}
			for (Expdest e : expdestListTest) {
				trDest = new TransactionDestination();
				trDest = appMgr.getListTransactionDestinationByIdExpDest(
						e.getIdExpDest()).get(0);
				appMgr.insert(e);
				id.setIdExpDest(e.getIdExpDest());
				trDest.setId(id);
				appMgr.insert(trDest);
			}
		} catch (Exception e) {

		}
		//
		// CourrierDossier courrierDossier =
		// appMgr.getCourrierDossierByIdCourrier(vb.getCourrier().getIdCourrier()).get(0);
		//
		// int refdossier = courrierDossier.getId().getDossierId();
		// List<Transaction> transactionList = new ArrayList<Transaction>();
		// /*********************** transaction By dossier
		// ***********************/
		// transactionList = appMgr.getTransactionByIdDossier(refdossier);
		// System.out.println("taille transaction Liste" +
		// transactionList.size());
		// /*********************** transaction dest Reel By dossier
		// ***********************/
		// List<TransactionDestinationReelle> transactDestReel44 =
		// appMgr.listTransactionDestinationReelByDossierID(vb.getCourrier().getIdCourrier());
		// List<TransactionDestinationReelle> transactDestReel = new
		// ArrayList<TransactionDestinationReelle>();
		// if (transactDestReel44.size() > 0) {
		//
		// for (TransactionDestinationReelle tt : transactDestReel44) {
		// if (transactDestReel.size() == 0) {
		// transactDestReel.add(tt);
		// }
		// }
		// }
		//
		// else {
		// TransactionDestinationReelle tr = new TransactionDestinationReelle();
		// tr.setTransactionDestinationReelleTypeDestinataire("");
		// transactDestReel.add(tr);
		// }
		//
		//
		// List<Expdest> expdestListTest = new ArrayList<Expdest>();
		// List<TransactionDestination> listTransactionDestination = new
		// ArrayList<TransactionDestination>();
		// for (Transaction transaction2 : transactionList) {
		// int refExped = transaction2.getExpdest().getIdExpDest();
		// int refTransacation = transaction2.getTransactionId();
		// expdestListTest.addAll(appMgr.getListExpDestByIdExpDest(refExped));
		// listTransactionDestination.addAll(appMgr.getListTransactionDestinationByIdTransaction(refTransacation));
		// }
		// for (TransactionDestination transactionDestination :
		// listTransactionDestination) {
		// int refExped = transactionDestination.getId().getIdExpDest();
		// expdestListTest.addAll(appMgr.getListExpDestByIdExpDest(refExped));
		// }
		// List<Expdestexterne> expdestExt2 = new ArrayList<Expdestexterne>();
		// for (Expdest expd : expdestListTest) {
		// List<Expdestexterne> expdestExt = new ArrayList<Expdestexterne>();
		// if (expd.getExpdestexterne() != null) {
		// expdestExt2.addAll(appMgr.getExpediteurById(expd.getExpdestexterne().getIdExpDestExterne()));
		// }
		// }
		// System.out.println("size : " + expdestExt2.size());
		// if (expdestExt2.size() == 0) {
		// Expdestexterne exp = new Expdestexterne();
		// exp.setExpDestExterneAdresse("");
		// expdestExt2.add(exp);
		// }
	}

	public void ouvrirfile() throws Exception {
		File pdfFile = new File(
				"C:/Users/Marouane Maalaoui/Desktop/cc rendu/cc rendu/7.pdf");
		Desktop.getDesktop().open(pdfFile);
	}

	// ** KHA
	public void executeJour() {
		Courrier courrier = new Courrier();
		courrierInformations = vb.getCourDossConsulterInformations();
	/*------------------------------------------------------------
		 * Vérifier est ce que le bureau d'ordre connecté est l'éditeur de courrier 
		 */
		List<Integer> listeIdMembresBOc=new ArrayList<Integer>();
		int idEditeur;
		CourrierDossier courrierDossier1 =  appMgr.getCourrierDossierByIdCourrier(courrierInformations.getCourrierID()).get(0);
			int refdossier = courrierDossier1.getId().getDossierId();
			Transaction transactionn = new Transaction();
			List<Transaction> listTr = appMgr.getTransactionByIdDossier(refdossier);
			lastIndex=listTr.size();
			transactionn=listTr.get(lastIndex-1);
			idEditeur=transactionn.getIdUtilisateur();
//			System.out.println("Id Editeur ====>"+idEditeur);	
		


	
		
		System.out.println("****************************************");
		List<Person> listMembresBoc = vb.getPerson().getAssociatedBOC().getMembersBOC();
		for(Person membres:listMembresBoc){
			listeIdMembresBOc.add(membres.getId());
		}

		//######################################################################################################
		// parcourir la liste des transaction et vérifier si existe des BO sans Membres
		existeBOSansMembres = false;
		
		
		for (Transaction transaction : courrierInformations
				.getCourrierAllTransactions()) {
		 List<Transaction> transactionExpediteur;
							transactionExpediteur = new ArrayList<Transaction>();
							transactionExpediteur = appMgr
													.getTransactionExpediteurByIdTransactionDestinationReelle(
															transaction.getTransactionDestinationReelle()
																	.getTransactionDestinationReelleId(), 1);
							for (Transaction tra : transactionExpediteur) {
												idUserDes = tra.getTransactionDestinationReelle()
														.getTransactionDestinationReelleIdDestinataire();
												typeUserDes = tra.getTransactionDestinationReelle()
														.getTransactionDestinationReelleTypeDestinataire();
											}
							int idDest;
							Unit unitRechecheDes = new Unit();
							boolean findPersonDest = false;
							boolean findUnitDest = false;
							Person personneRechercheDes = new Person();
							int k = 0;
							if (typeUserDes.equals("Interne-Person")||typeUserDes.equals("Interne-Unité") ){
											if (typeUserDes.equals("Interne-Person")) {

												do {

													idDest = vb.getCopyLdapListUser().get(k).getId();

													if (idDest == idUserDes) {
														findPersonDest = true;
														personneRechercheDes = vb.getCopyLdapListUser()
																.get(k);
													} else {
														k++;
													}

												} while (!findPersonDest
														&& k < vb.getCopyLdapListUser().size());
												getIdBocByUnit(personneRechercheDes
														.getAssociatedDirection());

											} 
											else if (typeUserDes.equals("Interne-Unité")){
												do {

													idDest = vb.getCopyLdapListUnit().get(k).getIdUnit();

													if (idDest == idUserDes) {
														findUnitDest = true;
														unitRechecheDes = vb.getCopyLdapListUnit().get(k);
													} else {
														k++;
													}

												} while (!findUnitDest
														&& k < vb.getCopyLdapListUnit().size());

												Unit unite = ldapOperation.getUnitById(unitRechecheDes
														.getIdUnit());

												getIdBocByUnit(unite);
											}
											int idBocDestinataire = idBoc;
											BOC bocDest = new BOC();
											bocDest = ldapOperation
																.getBocByID(idBocDestinataire);
														listIdBocMembers = new ArrayList<Integer>();
														
														List<Person> listBocMembers = bocDest
																.getMembersBOC();
														if(listBocMembers!=null && listBocMembers.size()>0)
														{
															//BO avec Membres
														}
														else {
															//Le BO Dest n'a pas des Membres
															existeBOSansMembres = true;
															break;
														}
											
												System.out
																		.println("bocDest.getNameBOC() "+bocDest.getNameBOC());
																System.out.println(" <><><><><><><> listBocMembers.size() : "+ listBocMembers.size());
														
											}
											else if(typeUserDes.equals("Externe")){
												existeBOSansMembres = false;
											}
											
															
		}
	System.out.println("existeBOSansMembres ==========> "+existeBOSansMembres);

	//######################################################################################################		
		
		if(!existeBOSansMembres){
		receptionphysiqueNonLivre = false;
		// [JS] Ajouté le 2019-07-30
		List<TransactionDestination> listTransactionDest = appMgr
				.getDestinationByIdTransaction(courrierInformations
						.getTransaction().getTransactionId());
		System.out.println("size listTransactionDest : "
				+ listTransactionDest.size());
		if (listTransactionDest != null && listTransactionDest.size() > 0) {
			TransactionDestination trDest = listTransactionDest.get(0);
			List<Courrier> listeCourriers = appMgr
					.getCourrierByIdCourrier(courrierInformations
							.getCourrierID());
			if (listeCourriers != null && listeCourriers.size() > 0) {
				Courrier cr = listeCourriers.get(0);

				System.out.println();
				if (trDest.getTransactionDestEtatReceptionPhysique() != null) {

					if (cr.getCourrierAvecDocumentPhysique() == true && !listeIdMembresBOc.contains(idEditeur)
							&& trDest.getTransactionDestEtatReceptionPhysique().getEtatId() == 9) {

						receptionphysiqueNonLivre = true;
					}

				} 
					else {
					// soit pas de reception physique soit avec l'état 8
					// receptionné

					// Fin JS
//					System.out.println("[receptionphysiqueNonLivre] : "
//							+ receptionphysiqueNonLivre);
				}
			
				
				if (!receptionphysiqueNonLivre) {
						if (courrierInformations.getCourrier() == null) {
							courrierInformations.setCourrier(appMgr
									.getCourrierByIdCourrier(
											courrierInformations
													.getCourrierID()).get(0));
						}

						courrier = courrierInformations.getCourrier();
						Etat etat = new Etat();
						etat = appMgr.listEtatByRef(
								courrierInformations.getEtatID()).get(0);

						if (etat.getEtatLibelle().equals("Faire suivre")) {
							String circuitCourrier = courrier
									.getCourrierCircuit();
							if (circuitCourrier.equals("workflow")) {
								int refNature = courrier.getNature()
										.getNatureId();
								int etatActuelle = courrier
										.getCourrierEtatActuelleWorkflow();
								Workflow workflow = new Workflow();
								int nb = appMgr.listWorkflowByIdNature(
										refNature).size();
								if (nb != 0) {
									/********* WorkFlow Request ********/
									workflow = appMgr.listWorkflowByIdNature(
											refNature).get(0);
									String processId = workflow
											.getWorkflowTitre();
									int idLastNode = workflow
											.getWorkflowIdLastNode();
									if (etatActuelle == idLastNode) {
										if (courrierInformations
												.getTransaction() == null) {
											courrierInformations
													.setTransaction(appMgr
															.getListTransactionByIdTransaction(
																	courrierInformations
																			.getTransactionID())
															.get(0));
										}
										validerFinProcessus(courrierInformations
												.getTransaction());
									} else {
										if (courrierInformations
												.getTransaction() == null) {
											courrierInformations
													.setTransaction(appMgr
															.getListTransactionByIdTransaction(
																	courrierInformations
																			.getTransactionID())
															.get(0));
										}
										TraitementEtapeSuivant etapeSuivant = new TraitementEtapeSuivant();
										JBPMAccessProcessBean jbpmAccessProcessBean = new JBPMAccessProcessBean();
										etapeSuivant = jbpmAccessProcessBean
												.startProcessTraitementCourrier(
														processId, etatActuelle);
										List<TransactionDestination> listTrDest = appMgr
												.getListTransactionDestinationByIdTransaction(courrierInformations
														.getTransactionID());
										validateWorkflow(etapeSuivant,
												courrierInformations
														.getTransaction(),
												listTrDest.get(listTrDest
														.size() - 1), courrier);
									}
								}
							}
						} else {

							// C*

							// [] : Liste de Tous Les Transactions where Etat= 5
							// => Non Traité
							System.out
									.println("Taille liste CourrierAllTransactions :"
											+ courrierInformations
													.getCourrierAllTransactionsByEtat()
													.size());

							for (Transaction transaction : courrierInformations
									.getCourrierAllTransactionsByEtat()) {
								// System.out.println("=============================");
								// System.out.println(transaction.getTransactionId());
								// System.out.println(transaction.getEtat().getEtatId());
								// System.out.println("=============================");

								// [] : Get Destinataire Réel by
								// System.out.println("-->ID Transaction Destination Reelle "
								// +
								// transaction.getTransactionDestinationReelle()
								// .getTransactionDestinationReelleId());
								TransactionDestinationReelle destinataionReel = appMgr
										.getTransactionDestinationReelById(transaction
												.getTransactionDestinationReelle()
												.getTransactionDestinationReelleId());

								// System.out
								// .println("--> destinataionReel :"
								// + destinataionReel
								// .getTransactionDestinationReelleTypeDestinataire());

								// : Courrier interne de type destination réel
								// :interne-unité ou
								// interne-Persone et necessite pas une
								// validation héarchique
								// ---------------------------------------

								if (destinataionReel != null) {

									// System.out
									// .println("2019-05-31 => Destination Reelle"
									// + destinataionReel
									// .getTransactionDestinationReelleTypeDestinataire());

									if (!destinataionReel
											.getTransactionDestinationReelleTypeDestinataire()
											.equals("Externe")) {
										
										listCourriersLiees = courriersLiees(courrierInformations);
//										System.out.println("listCourriersLiees "+listCourriersLiees.size());
										if(listCourriersLiees != null && listCourriersLiees.size()>0){
											
//											System.out.println("transaction==>"+transaction.getTransactionId());
//											System.out.println("destinataionReel==>"+destinataionReel.getTransactionDestinationReelleId());
											executeCourrierElementaire(transaction,destinataionReel,courrierInformations);
										
										}else{
										// c'est un courrier d'arrivé depuis
										// l'exterieur (PP ou
										// PM) vers l'interne, donc il faut
										// l'executer pour que
										// le courrier s'entre dans le circuit
										// de validation
										// hierarchique
										// ( validation hierarchique depend de
										// la variable de
										// parametrage
										// #passage_hierarchique_courrier_arrive_apres_directeur_generale#
										// System.out
										// .println(" -->Faire Appel à la méthode de validation ");
										validateTransactionToDestinationReel(
												transaction, destinataionReel);

									}
										} else {
										// c'est un courrier d'un personne ou
										// unité interne vers
										// l'exterieur, juste il faut l'executer
										// pour ajouter la
										// transaction de depart d'un courrier
										// System.out
										// .println(" -->Faire Appel à la méthode execute One Transaction");
										executeOneTransaction(courrierInformations);
										break;

									}

								}
							}
						}
						vb.setCourrierAExcecuter(false);
					}
				else{
					System.out.println("Le courrier n'est pas encore réceptionné ");
				}
			}
		}
	}
		
		

	//Ancienne Méthode d'execution 	
//		Courrier courrierr = new Courrier();
//
//		ci = vb.getCourDossConsulterInformations();
//		if (ci.getCourrier() == null) {
//			ci.setCourrier(appMgr.getCourrierByIdCourrier(ci.getCourrierID())
//					.get(0));
//		}
//		courrierr = ci.getCourrier();
//		Etat etat = new Etat();
//		etat = appMgr.listEtatByRef(ci.getEtatID()).get(0);
//		if (etat.getEtatLibelle().equals("Faire suivre")) {
//			String circuitCourrier = courrierr.getCourrierCircuit();
//			if (circuitCourrier.equals("workflow")) {
//				System.out.println("**Faire suivre-workflow");
//				int refNature = courrierr.getNature().getNatureId();
//				int etatActuelle = courrierr.getCourrierEtatActuelleWorkflow();
//				Workflow workflow = new Workflow();
//				int nb = appMgr.listWorkflowByIdNature(refNature).size();
//				if (nb != 0) {
//					/********* WorkFlow Request ********/
//					workflow = appMgr.listWorkflowByIdNature(refNature).get(0);
//					String processId = workflow.getWorkflowTitre();
//					int idLastNode = workflow.getWorkflowIdLastNode();
//					if (etatActuelle == idLastNode) {
//						if (ci.getTransaction() == null) {
//							ci.setTransaction(appMgr
//									.getListTransactionByIdTransaction(
//											ci.getTransactionID()).get(0));
//						}
//
//						validerFinProcessus(ci.getTransaction());
//
//					} else {
//						if (ci.getTransaction() == null) {
//							ci.setTransaction(appMgr
//									.getListTransactionByIdTransaction(
//											ci.getTransactionID()).get(0));
//						}
//
//						TraitementEtapeSuivant etapeSuivant = new TraitementEtapeSuivant();
//						JBPMAccessProcessBean jbpmAccessProcessBean = new JBPMAccessProcessBean();
//						etapeSuivant = jbpmAccessProcessBean
//								.startProcessTraitementCourrier(processId,
//										etatActuelle);
//						List<TransactionDestination> listTrDest = appMgr
//								.getListTransactionDestinationByIdTransaction(ci
//										.getTransactionID());
//						validateWorkflow(etapeSuivant, ci.getTransaction(),
//								listTrDest.get(listTrDest.size() - 1),
//								courrierr);
//					}
//				}
//			}
//		} else {
//			// C*
//			for (Transaction transaction : ci.getCourrierAllTransactions()) {
//				System.out.println("####" + transaction.getTransactionId());
//				TransactionDestinationReelle destinataionReel = appMgr
//						.getTransactionDestinationReelById(transaction
//								.getTransactionDestinationReelle()
//								.getTransactionDestinationReelleId());
//				if (destinataionReel != null) {
//
//					if (!destinataionReel
//							.getTransactionDestinationReelleTypeDestinataire()
//							.equals("Externe")) {
//
//						// c'est un courrier d'arrivé depuis l'exterieur (PP ou
//						// PM) vers l'interne, donc il faut l'executer pour que
//						// le courrier s'entre dans le circuit de validation
//						// hierarchique
//						// ( validation hierarchique depend de la variable de
//						// parametrage
//						// #passage_hierarchique_courrier_arrive_apres_directeur_generale#
//						validateTransactionToDestinationReel(transaction,
//								destinataionReel);
//						// if(destinataionReel.getTransactionDestinationReelleResponsableReponse()
//						// != null){
//						// validateTransactionToDestinationReel(selectedCourrier.getTransaction(),
//						// destinataionReel);
//						// }else{
//						// setShowResponsableResponse(true);
//						// }
//					} else {
//						// c'est un courrier d'un personne ou unité interne vers
//						// l'exterieur, juste il faut l'executer pour ajouter la
//						// transaction de depart d'un courrier
//						executeOneTransaction(ci);
//						break;
//					}
//				}
//			}
//			// C*
//			// executeOneTransaction(courrierInformations); // commenté a cause
//			// de // C*
//		}

	}

public List<CourrierInformations> courriersLiees(CourrierInformations ci){
		
		int IdCourrier = ci.getCourrierID();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		dateDebut = calendar.getTime();
		System.out.println("*****dateDebut**** : " + dateDebut);

		Calendar calendar1 = Calendar.getInstance();
		// calendar1.add(Calendar.DATE, 1);
		calendar1.set(Calendar.HOUR_OF_DAY, 23);
		calendar1.set(Calendar.MINUTE, 59);
		calendar1.set(Calendar.SECOND, 59);
		calendar1.set(Calendar.MILLISECOND, 999);
		dateFin = calendar1.getTime();
		
		// [JS] : Liste des Courriers Affectées
		List<CourrierInformations> courriersInformationsAffectes = new ArrayList<CourrierInformations>();
		type = "";
		type1 = "";
		if (vb.getPerson().isBoc()) {
			type = "boc_" + vb.getPerson().getAssociatedBOC().getIdBOC();
			type1 = "";
		} else if (vb.getPerson().isResponsable()) {
			type = "unit_"
					+ vb.getPerson().getAssociatedDirection().getIdUnit();
			type1 = "sub_" + idUser;
			// NEW

			for (Unit unit : vb.getPerson().getAssociatedDirection()
					.getListUnitsChildUnit()) {
				
				listIdsSousUnit.add(unit.getIdUnit());
				 
				try {
					listIdsSubordonne
							.add(unit.getResponsibleUnit().getId());
					
				} catch (Exception e) {
					System.out.println("#Sub-Unit without Responsible");
				}

			}
			// FIn NEW
			try {
				if(vb.getPerson().getAssociatedDirection()!=null && vb.getPerson().getAssociatedDirection()
						.getSecretaryUnit()!=null ){
					
				
				typeSecretaire = "secretary_"
						+ vb.getPerson().getAssociatedDirection()
								.getSecretaryUnit().getId();
				}
				else{
					consultationCourrierSecretaire = "Non";
//					System.out.println("PAS DE Secretaire");
				}
				
			} catch (NullPointerException e) {
				e.printStackTrace();
				consultationCourrierSecretaire = "Non";
			}

		} else if (vb.getPerson().isSecretary()) {
			type = "secretary_" + idUser;
			type1 = "unit_"
					+ vb.getPerson().getAssociatedDirection().getIdUnit();
		} else if (vb.getPerson().isAgent()) {
			type = "agent_" + idUser;
			type1 = "";
		}
		// fin identify connected user
		
		System.out.println("consultationCourrierSecretaire "+consultationCourrierSecretaire);
		courriersInformationsAffectes = appMgr
				.findCourrierEnvoyerANDRecuByCriteria(vb.getPerson()
						.isResponsable(), listIdsSousUnit, listIdsSubordonne,
						filterMap, sortField, descending,
						consultationCourrierSecretaire,
						consultationCourrierSubordonne,
						consultationCourrierSousUnite, 13, dateDebut, dateFin,
						type, type1, typeSecretaire, idUser, typeTransmission,
						"", 0, 10, false, 6, "Tous", vb.getDbType(),
						IdCourrier, 0, 0);
		System.out.println("Courriers Liées Affectées Pour l'enveloppe :  "	+ courriersInformationsAffectes.size());
		for (CourrierInformations courrierInformations : courriersInformationsAffectes) {
			try {
				courrierConsultationRecentBean.searchExpediteurDestinataire(courrierInformations);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		listCourriersInformationsAffecte=new ArrayList<CourrierInformations>();
		listCourriersInformationsAffecte.addAll(courriersInformationsAffectes);
	
		
		return listCourriersInformationsAffecte;
		
	}
	//Execution les courriers Liées à Courrier Enveloppe
	public void executeCourrierElementaire(Transaction tr,TransactionDestinationReelle destinataionReel,CourrierInformations ci){
	validateTransactionToDestinationReel(tr, destinataionReel);
		
		listCourriersInformationsAffecte=new ArrayList<CourrierInformations>();
		listCourriersInformationsAffecte=courriersLiees(ci);
	 System.out.println("listCourriersInformationsAffecte "+listCourriersInformationsAffecte.size());
		for (CourrierInformations cr : listCourriersInformationsAffecte) {
			System.out.println("1### "+cr.getCourrierID());
			System.out.println("2### "+cr.getTransaction().getTransactionId());
			Courrier crrr=appMgr.getCourrierByIdCourrier(cr.getCourrierID()).get(0);
			System.out.println("3### "+crrr.getTransmission().getTransmissionLibelle());
			
			validateTransactionToDestinationReel(cr.getTransaction(), cr
					.getTransaction().getTransactionDestinationReelle());
	
		}
		
	}
	

	public void validerFinProcessus(Transaction transaction) {
		statV1 = false;
		statV2 = false;
		try {
			Etat etat = new Etat();
			etat = appMgr.listEtatByLibelle("Validé").get(0);
			transaction.setEtat(etat);
			transaction.setTransactionDateReponse(new Date());
			appMgr.update(transaction);
			statV1 = true;
		} catch (Exception e) {
			statV2 = true;
			e.printStackTrace();
		}
	}

//	private void executeOneTransaction(CourrierInformations courrierInformations) {
//		if (courrierInformations.getCourrier() == null) {
//			courrierInformations.setCourrier(appMgr.getCourrierByIdCourrier(
//					courrierInformations.getCourrierID()).get(0));
//		}
//		if (courrierInformations.getTransaction() == null) {
//			courrierInformations.setTransaction(appMgr
//					.getListTransactionByIdTransaction(
//							courrierInformations.getTransactionID()).get(0));
//		}
//		statV1 = false;
//		statV2 = false;
//		Transaction transaction = new Transaction();
//		transaction = courrierInformations.getTransaction();
//		Transaction newTransaction = new Transaction();;
//		Transaction transactionExpediteur;
//		List<Transaction> listTransaction = new ArrayList<Transaction>();
//		try {
//			if (transaction.getTransactionDestinationReelle() != null) {
//				
//				transactionExpediteur = new Transaction();
//
//				transactionExpediteur = appMgr
//						.getTransactionExpediteurByIdTransactionDestinationReelle(
//								transaction.getTransactionDestinationReelle()
//										.getTransactionDestinationReelleId(), 1)
//						.get(0);
//
//				listTransaction = appMgr
//						.getTransactionByIdTransactionDestinationReelle(transaction
//								.getTransactionDestinationReelle()
//								.getTransactionDestinationReelleId());
//				Etat etat = new Etat();
//				Expdest expdest = new Expdest();
//				Typetransaction typetransaction = new Typetransaction();
//				TransactionDestinationId id = new TransactionDestinationId();
//				TransactionDestination trDest = new TransactionDestination();
//				etat = appMgr.listEtatByLibelle("Traité").get(0);
//				newTransaction.setExpdest(transactionExpediteur.getExpdest());
//				newTransaction.setIdUtilisateur(vb.getPerson().getId());
//				newTransaction.setTransactionDateTransaction(new Date());
//				typetransaction = appMgr.getTypeTransactionByLibelle("Envoi")
//						.get(0);
//				newTransaction.setTypetransaction(typetransaction);
//				newTransaction.setEtat(etat);
//				newTransaction.setTransactionSupprimer(true);
//				int newOrderNumber = transaction.getTransactionOrdre();
//				newOrderNumber++;
//				newTransaction.setTransactionOrdre(newOrderNumber);
//				newTransaction.setDossier(transaction.getDossier());
//				newTransaction.setTransactionDestinationReelle(transaction
//						.getTransactionDestinationReelle());
//				newTransaction.setTransactionFirst(transaction
//						.getTransactionId());
//				appMgr.insert(newTransaction);
//
//				expdest = new Expdest();
//				Expdestexterne expDestExterne = new Expdestexterne();
//				TransactionDestinationReelle transactionDestinationReelle = appMgr
//						.getTransactionDestinationReelById(transaction
//								.getTransactionDestinationReelle()
//								.getTransactionDestinationReelleId());
//				expdest.setTypeExpDest("Externe");
//				expDestExterne = appMgr
//						.getExpediteurById(
//								transactionDestinationReelle
//										.getTransactionDestinationReelleIdDestinataire())
//						.get(0);
//				expdest.setExpdestexterne(expDestExterne);
//				transactionDestinationReelle
//						.setTransactionDestinationReelleDateTraitement(new Date());
//				appMgr.update(transactionDestinationReelle);
//				appMgr.insert(expdest);
//
//				id = new TransactionDestinationId();
//				trDest = new TransactionDestination();
//				id.setIdTransaction(newTransaction.getTransactionId());
//				id.setIdExpDest(expdest.getIdExpDest());
//				trDest.setTransactionDestTypeIntervenant(type);
//				trDest.setId(id);
//				appMgr.insert(trDest);
//
//				List<TransactionAnnotation> transactionAnnotation = new ArrayList<TransactionAnnotation>();
//				transactionAnnotation = appMgr
//						.getAnnotationByIdTransaction(transaction
//								.getTransactionId());
//				TransactionAnnotationId cI = new TransactionAnnotationId();
//				TransactionAnnotation cA = new TransactionAnnotation();
//				for (TransactionAnnotation tr : transactionAnnotation) {
//					cI.setIdAnnotation(tr.getId().getIdAnnotation());
//					cI.setIdTransaction(newTransaction.getTransactionId());
//					cA.setId(cI);
//					appMgr.insert(cA);
//					cA = new TransactionAnnotation();
//					cI = new TransactionAnnotationId();
//				}
//
//				for (Transaction transaction1 : listTransaction) {
//					transaction1.setEtat(etat);
//					appMgr.update(transaction1);
//				}
//				Calendar cal = Calendar.getInstance();
//				cal.setTime(courrierInformations
//						.getCourrierDateReceptionEnvoi());
//				int year = cal.get(Calendar.YEAR);
//				// mettre a jour la reference pour indiquer qu'il est un
//				// courrier de depart
//				Integer lastId = appMgr.getCourrierLastIdByTypeOrdreAndAnnee(
//						"D", year);
//				courrierInformations.getCourrier().setCourrierType("D");
//				if (lastId == null || lastId == 0) {
//					courrierInformations.getCourrier().setCourrierTypeOrdre(1);
//				} else {
//					courrierInformations.getCourrier().setCourrierTypeOrdre(
//							lastId + 1);
//				}
//				courrierInformations.getCourrier()
//						.setCourrierReferenceCorrespondant(
//								courrierInformations.getCourrier()
//										.getCourrierType()
//										+ courrierInformations.getCourrier()
//												.getCourrierTypeOrdre());
//				// courrierInformations.getCourrier().setCourrierReferenceCorrespondant("D"+courrierInformations.getCourrier().getIdCourrier());
//				appMgr.update(courrierInformations.getCourrier());
//				
//				Dossier dossier = appMgr.getDossierByIdDossier(
//						transaction.getDossier().getDossierId()).get(0);
//				dossier.setDossierIntitule("Courrier_"
//						+ courrierInformations.getCourrier().getIdCourrier());
//				appMgr.update(dossier);
//				// mettre a jour la reference pour indiquer qu'il est un
//				// courrier de depart
//				// mettre a jour la colonne #courrierDateReponseSysteme# pour
//				// indiquer que le courrier originale a été repondu
//				// trouvez le courrier original
//				List<Lienscourriers> liensCourriers = appMgr
//						.getListCourrierLiensByIdCourrier(courrierInformations
//								.getCourrier().getIdCourrier());
//				if (!liensCourriers.isEmpty()) {
//					List<Integer> listIdLien = new ArrayList<Integer>();
//					for (Lienscourriers liens : liensCourriers) {
//						listIdLien.add(liens.getLiensCourrier());
//					}
//					CourrierLiens courrierLiens = appMgr
//							.getCourrierLienByListIdLienAndTypeLien(listIdLien,
//									1);
//					if (courrierLiens != null) {
//						Courrier courrierOriginal = appMgr
//								.getCourrierByIdCourrier(
//										courrierLiens.getId().getIdCourrier())
//								.get(0);
//						courrierOriginal
//								.setCourrierDateReponseSysteme(new Date());
//						appMgr.update(courrierOriginal);
//					}
//				}
//				// trouver le courrier original
//				// mettre a jour la colonne #courrierDateReponseSysteme# pour
//				// indiquer que le courrier originale a été repondu
//				// inséré la date de traitement
//				TransactionDestination trDestination = courrierInformations
//						.getTransactionDestination();
//				System.out.println("trDestination"
//						+ trDestination.getTransactionDestIdIntervenant());
//				if (trDestination.getTransactionDestDateTransfert() == null) {
//					trDestination.setTransactionDestDateTransfert(new Date());
//					appMgr.update(trDestination);
//				}
//				// inséré la date de traitement
//				// inséré la date de consultation
//				if (trDestination.getTransactionDestDateConsultation() == null) {
//					trDestination
//							.setTransactionDestDateConsultation(new Date());
//					appMgr.update(trDestination);
//				}
//				// inséré la date de consultation
//			}
//			
//			statV1 = true;
//			//AH mettre à jour la transaction pour visualiser les nouvelle odification
//			vb.setTransaction(newTransaction);
//		} catch (Exception e) {
//			statV2 = true;
//			e.printStackTrace();
//		}
//	}
	public  void executeOneTransaction(CourrierInformations courrierInformations) {
		if (courrierInformations.getCourrier() == null) {
			courrierInformations.setCourrier(appMgr.getCourrierByIdCourrier(
					courrierInformations.getCourrierID()).get(0));
		}
		
		Courrier cr=appMgr.getCourrierByIdCourrier(
				courrierInformations.getCourrier().getIdCourrier()).get(0);
		if (courrierInformations.getTransaction() == null) {
			courrierInformations.setTransaction(appMgr
					.getListTransactionByIdTransaction(
							courrierInformations.getTransactionID()).get(0));
		}
		setStatV1(false);
		setStatV2(false);
		Transaction transaction = new Transaction();
		transaction = courrierInformations.getTransaction();
		Transaction newTransaction = new Transaction();
		Transaction transactionExpediteur;
		List<Transaction> listTransaction = new ArrayList<Transaction>();
		try {
			if (transaction.getTransactionDestinationReelle() != null) {
				
				transactionExpediteur = new Transaction();

				transactionExpediteur = appMgr
						.getTransactionExpediteurByIdTransactionDestinationReelle(
								transaction.getTransactionDestinationReelle()
										.getTransactionDestinationReelleId(), 1)
						.get(0);

				listTransaction = appMgr
						.getTransactionByIdTransactionDestinationReelle(transaction
								.getTransactionDestinationReelle()
								.getTransactionDestinationReelleId());
				Etat etat = new Etat();
				Expdest expdest = new Expdest();
				Typetransaction typetransaction = new Typetransaction();
				TransactionDestinationId id = new TransactionDestinationId();
				TransactionDestination trDest = new TransactionDestination();
				etat = appMgr.listEtatByLibelle("Traité").get(0);
				newTransaction.setExpdest(transactionExpediteur.getExpdest());
				newTransaction.setIdUtilisateur(vb.getPerson().getId());
				newTransaction.setTransactionDateTransaction(new Date());
				typetransaction = appMgr.getTypeTransactionByLibelle("Envoi")
						.get(0);
				newTransaction.setTypetransaction(typetransaction);
				newTransaction.setEtat(etat);
				newTransaction.setTransactionSupprimer(true);
				int newOrderNumber = transaction.getTransactionOrdre();
				newOrderNumber++;
				newTransaction.setTransactionOrdre(newOrderNumber);
				newTransaction.setDossier(transaction.getDossier());
				newTransaction.setTransactionDestinationReelle(transaction
						.getTransactionDestinationReelle());
				newTransaction.setTransactionFirst(transaction
						.getTransactionId());

				appMgr.insert(newTransaction);

				expdest = new Expdest();
				Expdestexterne expDestExterne = new Expdestexterne();
				TransactionDestinationReelle transactionDestinationReelle = appMgr
						.getTransactionDestinationReelById(transaction
								.getTransactionDestinationReelle()
								.getTransactionDestinationReelleId());
				expdest.setTypeExpDest("Externe");
				expDestExterne = appMgr
						.getExpediteurById(
								transactionDestinationReelle
										.getTransactionDestinationReelleIdDestinataire())
						.get(0);
				expdest.setExpdestexterne(expDestExterne);
				vb.setExpdestexterneNotif(expDestExterne);
				transactionDestinationReelle
						.setTransactionDestinationReelleDateTraitement(new Date());
				appMgr.update(transactionDestinationReelle);
				appMgr.insert(expdest);

				id = new TransactionDestinationId();
				trDest = new TransactionDestination();
				id.setIdTransaction(newTransaction.getTransactionId());
				id.setIdExpDest(expdest.getIdExpDest());
				trDest.setTransactionDestTypeIntervenant(type);
				trDest.setId(id);
				appMgr.insert(trDest);

				List<TransactionAnnotation> transactionAnnotation = new ArrayList<TransactionAnnotation>();
				transactionAnnotation = appMgr
						.getAnnotationByIdTransaction(transaction
								.getTransactionId());
				TransactionAnnotationId cI = new TransactionAnnotationId();
				TransactionAnnotation cA = new TransactionAnnotation();
				for (TransactionAnnotation tr : transactionAnnotation) {
					cI.setIdAnnotation(tr.getId().getIdAnnotation());
					cI.setIdTransaction(newTransaction.getTransactionId());
					cA.setId(cI);
					appMgr.insert(cA);
					cA = new TransactionAnnotation();
					cI = new TransactionAnnotationId();
				}

				for (Transaction transaction1 : listTransaction) {
					transaction1.setEtat(etat);
					appMgr.update(transaction1);
				}
										  
//				Date dateCourrier = courrierInformations.getCourrier()
//						.getCourrierDateReception();
				Date dateCourrier = cr
				.getCourrierDateReception();
				
				int year = dateCourrier.getYear() + 1900;

				System.out.println("year   " + year);
				System.out.println("Boc  "
						+ "boc_"
						+ String.valueOf(vb.getPerson().getAssociatedBOC()
								.getIdBOC()));

				if (cr.getCourrierType()
						.equals("I")) {

					Integer lastId2 = appMgr.CountAllCourrierBOCByTransaction(transaction.getDossier().getDossierId(),
							"D",
							year,
							"boc_"
									+ String.valueOf(vb.getPerson()
											.getAssociatedBOC().getIdBOC()),
							listIdBocMembers);
					cr.setCourrierType("D");

					if (lastId2 != null) {
						cr
								.setCourrierTypeOrdre(lastId2 + 1);

					} else {
						cr
								.setCourrierTypeOrdre(1);
					}

						cr
							.setCourrierReferenceCorrespondant(
									cr
											.getCourrierType()
											+ cr
													.getCourrierTypeOrdre());
					appMgr.update(cr);
					// XTE - AH : Dans le cas ou le courrier est I màj la ref du
					// Courrier dans la transaction
					List<Transaction> transactions = appMgr
							.getListTransactionByIdTransaction(transaction
									.getTransactionId());
					if (transactions != null && transactions.size() > 0) {
						transaction = transactions.get(0);

						transaction
								.setCourrierReferenceCorrespondant(cr
										.getCourrierReferenceCorrespondant());

						transaction.setCourrierType(cr.getCourrierType());
						transaction.setCourrierTypeOrdre(cr.getCourrierTypeOrdre());
						appMgr.update(transaction);
					}

				}

				// /////////////////////////////////////////
										  
							
											  
									 
				// courrierInformations.getCourrier().setCourrierReferenceCorrespondant("D"+courrierInformations.getCourrier().getIdCourrier());

				// XTE - AH : Mettre à jour la reference dans la dernière
				// transaction ajoutée
				newTransaction.setCourrierTypeOrdre(cr.getCourrierTypeOrdre());

				newTransaction.setCourrierType(cr.getCourrierType());
				newTransaction.setCourrierDateReceptionAnnee(transaction
						.getCourrierDateReceptionAnnee());
				newTransaction
						.setCourrierReferenceCorrespondant(cr
								.getCourrierReferenceCorrespondant());	
				newTransaction.setTransactionCommentaireAnnotation(transaction.getTransactionCommentaireAnnotation());
				appMgr.update(newTransaction);

				Dossier dossier = appMgr.getDossierByIdDossier(
						transaction.getDossier().getDossierId()).get(0);
				dossier.setDossierIntitule("Courrier_"
						+ cr.getIdCourrier());
				appMgr.update(dossier);
				// mettre a jour la reference pour indiquer qu'il est un
				// courrier de depart
				// mettre a jour la colonne #courrierDateReponseSysteme# pour
				// indiquer que le courrier originale a été repondu
				// trouvez le courrier original
				List<Lienscourriers> liensCourriers = appMgr
						.getListCourrierLiensByIdCourrier(cr.getIdCourrier());
				if (!liensCourriers.isEmpty()) {
					List<Integer> listIdLien = new ArrayList<Integer>();
					for (Lienscourriers liens : liensCourriers) {
						listIdLien.add(liens.getLiensCourrier());
					}
					List<CourrierLiens> courrierLiensList=appMgr
					.getCourrierLienByListIdLienAndTypeLien(listIdLien,
							1);
					if(courrierLiensList!=null && courrierLiensList.size()>0){
						
						CourrierLiens courrierLiens = courrierLiensList.get(0);
					
					if (courrierLiens != null) {
						Courrier courrierOriginal = appMgr
								.getCourrierByIdCourrier(
										courrierLiens.getId().getIdCourrier())
								.get(0);
						courrierOriginal
								.setCourrierDateReponseSysteme(new Date());
						appMgr.update(courrierOriginal);
					}}
				}
				// trouver le courrier original
				// mettre a jour la colonne #courrierDateReponseSysteme# pour
				// indiquer que le courrier originale a été repondu
				// inséré la date de traitement

				TransactionDestination trDestinationInter = courrierInformations
						.getTransactionDestination();
				List<TransactionDestination> listTrDestination = appMgr
						.getDestinationByIdTransaction(courrierInformations
								.getTransaction().getTransactionId());
				if (listTrDestination != null && listTrDestination.size() > 0) {
					TransactionDestination trDestination = listTrDestination
							.get(0);

					if (trDestination.getTransactionDestDateTransfert() == null) {

						trDestination
								.setTransactionDestDateTransfert(new Date());
						appMgr.update(trDestination);
					}
					// inséré la date de traitement
					// inséré la date de consultation
					// [JS] : set Date De Consultation lors de consultation de
					// courrier et non pas au niveau execution
					// if (trDestination.getTransactionDestDateConsultation() ==
					// null) {
					// trDestination
					// .setTransactionDestDateConsultation(new Date());
					// appMgr.update(trDestination);
					// }
				}
				// inséré la date de consultation
			}
			setStatV1(true);
			vb.setTransaction(newTransaction);
			//Si le mode de transmission Mail envoier un mail au destinataire.
			if(cr.getTransmission().getTransmissionId()==4)
			{
				
//				vb.getExpdestexterneNotif();
//				System.out.println("ID de l'expéditeur à notifier "+vb.getExpdestexterneNotif().getIdExpDestExterne());
//				System.out.println("ID de l'expéditeur à notifier "+vb.getExpdestexterneNotif().getExpDestExterneMail());
			if(vb.getExpdestexterneNotif().getExpDestExterneMail()!=null && vb.getExpdestexterneNotif().getExpDestExterneMail().length()>0){
//				envoyerMailNotificationDestinataireExterne(courrierInformations.getCourrier().getCourrierObjet(),courrierInformations.getCourrier().getCourrierCommentaire(),vb.getExpdestexterneNotif().getExpDestExterneMail());
				List<String> listD= new ArrayList<String>();
				String adresseDestinataire=vb.getExpdestexterneNotif().getExpDestExterneMail();
				listD.add(adresseDestinataire);
				//sendEmailSSLWithAttachemnt(courrierInformations.getCourrier().getCourrierObjet(),courrierInformations.getCourrier().getCourrierCommentaire(),listD,courrierInformations.getCourrier().getIdCourrier());
			}
			}
			
		} catch (Exception e) {
			setStatus2(true);
			e.printStackTrace();
		}
	}


	public void validateWorkflow(TraitementEtapeSuivant etapeSuivant,
			Transaction transaction,
			TransactionDestination transactionDestination, Courrier courrier) {
		
		statV1 = false;
		statV2 = false;
		try {
			if (transactionDestination != null) {
				if (transactionDestination.getTransactionDestDateConsultation() == null) {
					transactionDestination
							.setTransactionDestDateConsultation(new Date());
				}
			}
			transactionDestination.setTransactionDestDateTransfert(new Date());
			appMgr.update(transactionDestination);
			Transaction newTransaction = new Transaction();
			Etat etat = new Etat();
			Expdest expdest = new Expdest();
			Typetransaction typetransaction = new Typetransaction();
			TransactionDestinationId id = new TransactionDestinationId();
			TransactionDestination trDest = new TransactionDestination();
			etat = appMgr.listEtatByLibelle("Validé").get(0);
			transaction.setEtat(etat);
			transaction.setTransactionDateReponse(new Date());
			appMgr.update(transaction);
			/**************************************************************/
			expdest.setTypeExpDest("Interne-Person");
			expdest.setIdExpDestLdap(vb.getPerson().getId());
			appMgr.insert(expdest);
			if (vb.getPerson().isResponsable()) {
				newTransaction.setTransactionTypeIntervenant("sub_"
						+ String.valueOf(vb.getPerson().getId()));
			} else if (vb.getPerson().isSecretary()) {
				newTransaction.setTransactionTypeIntervenant("secretary_"
						+ String.valueOf(vb.getPerson().getId()));
			}
			newTransaction.setExpdest(expdest);
			newTransaction.setIdUtilisateur(vb.getPerson().getId());
			newTransaction.setTransactionDateTransaction(new Date());
			typetransaction = appMgr.getTypeTransactionByLibelle("Envoi")
					.get(0);
			newTransaction.setTypetransaction(typetransaction);
			// Faire suivre
			etat = appMgr.listEtatByLibelle("Faire suivre").get(0);
			newTransaction.setEtat(etat);
			newTransaction.setTransactionSupprimer(true);
			int newOrderNumber = transaction.getTransactionOrdre();
			newOrderNumber++;
			newTransaction.setTransactionOrdre(newOrderNumber);
			newTransaction.setDossier(transaction.getDossier());
			// C * // dupliquer le destinataire reel du workflow au niveau de la
			// nouvelle transaction
			newTransaction.setTransactionDestinationReelle(transaction
					.getTransactionDestinationReelle());
			// C * // dupliquer le destinataire reel du workflow au niveau de la
			// nouvelle transaction
			newTransaction.setTransactionFirst(transaction.getTransactionId());
			appMgr.insert(newTransaction);

			/********** Workflow **********/

			long resultat = etapeSuivant.getIdNodeSuivante();
			int etatActuelle = (int) resultat;
			courrier.setCourrierEtatActuelleWorkflow(etatActuelle);
			appMgr.update(courrier);

			String result = "";
			Unit u = new Unit();
			String unite = etapeSuivant.getEtatSuivant();
			// Unite
			u = ldapOperation.getUnitByShortName(unite);

			/*
			 * Person pr = new Person(); // Person pr = u.getResponsibleUnit();
			 * // Envoi Responsable result = result + pr.getCn() + " / ";
			 * expdest = new Expdest(); id = new TransactionDestinationId();
			 * trDest = new TransactionDestination();
			 * expdest.setTypeExpDest("Interne-Person");
			 * expdest.setIdExpDestLdap(pr.getId()); appMgr.insert(expdest);
			 * id.setIdTransaction(newTransaction.getTransactionId());
			 * id.setIdExpDest(expdest.getIdExpDest()); trDest.setId(id);
			 * trDest.setTransactionDestTypeIntervenant("sub_" +
			 * String.valueOf(pr.getId()));
			 * trDest.setTransactionDestDateReponse(
			 * transactionDestination.getTransactionDestDateReponse());
			 * appMgr.insert(trDest);
			 */
			// Envoi Unité
			result = result + u.getNameUnit() + " / ";
			expdest = new Expdest();
			id = new TransactionDestinationId();
			trDest = new TransactionDestination();
			if (unite.equals("BOC")) {
				if (vb.getPerson().getAssociatedDirection() != null) {
					getIdBocByUnit(vb.getPerson().getAssociatedDirection());
				} else {
					idBoc = vb.getPerson().getAssociatedBOC().getIdBOC();
				}
				expdest.setTypeExpDest("Interne-Boc");
				expdest.setIdExpDestLdap(idBoc);
				appMgr.insert(expdest);
				id.setIdTransaction(newTransaction.getTransactionId());
				id.setIdExpDest(expdest.getIdExpDest());
				trDest.setId(id);
				trDest.setTransactionDestTypeIntervenant("boc_" + idBoc);
			} else {
				expdest.setTypeExpDest("Interne-Unité");
				expdest.setIdExpDestLdap(u.getIdUnit());
				appMgr.insert(expdest);
				id.setIdTransaction(newTransaction.getTransactionId());
				id.setIdExpDest(expdest.getIdExpDest());
				trDest.setId(id);
				trDest.setTransactionDestTypeIntervenant("unit_"
						+ String.valueOf(u.getIdUnit()));
			}
			appMgr.insert(trDest);

			TransactionAnnotationId cI = new TransactionAnnotationId();
			TransactionAnnotation cA = new TransactionAnnotation();
			List<TransactionAnnotation> transactionAnnotation = new ArrayList<TransactionAnnotation>();
			transactionAnnotation = appMgr
					.getAnnotationByIdTransaction(transaction
							.getTransactionId());
			for (TransactionAnnotation tr : transactionAnnotation) {
				cI.setIdAnnotation(tr.getId().getIdAnnotation());
				cI.setIdTransaction(newTransaction.getTransactionId());
				cA.setId(cI);
				appMgr.insert(cA);
				cA = new TransactionAnnotation();
				cI = new TransactionAnnotationId();
			}
			statV1 = true;

		} catch (Exception e) {
			statV2 = true;
			e.printStackTrace();
		}
	}

	private void getIdBocByUnit(Unit unit) {
		if (unit.getAssociatedUnit() != null) {
			getIdBocByUnit(unit.getAssociatedUnit());
		} else {
			idBoc = unit.getAssociatedBOC().getIdBOC();
		}
	}

	private void validateTransactionToDestinationReel(Transaction transaction,
			TransactionDestinationReelle trDestinationReelle) {
		try {

			Variables variable = appMgr.listVariablesByLibelle("validation_hierarchique_courrier_arrive").get(0);
			Variables variableToDGEN = appMgr.listVariablesByLibelle("envoie_courrier_arrive_directeur_generale").get(0);
			boolean passageDGEN = false;
			Person generalDirector = null;
			Unit generalDirectorUnit = null;
			// XET - AH : Si nous avons
			// envoie_courrier_arrive_directeur_generale= Oui
			if (variableToDGEN.getVaraiablesValeur().equals("Oui")) {
				passageDGEN = true;
				// special pour le BOC dans le cas ou la vlalidation ne passe
				// pas par le DGEN pour la fonction #findIdDestinataireSuivant#
				generalDirectorUnit = ldapOperation.getUnitById(0);
				generalDirector = generalDirectorUnit.getResponsibleUnit();
				// special pour le BOC dans le cas ou la vlalidation ne passe
				// pas par le DGEN pour la fonction #findIdDestinataireSuivant#
			}
			// XET - AH : Si nous avons
			// envoie_courrier_arrive_directeur_generale= Oui
			else {
				// special pour le BOC dans le cas ou la vlalidation ne passe
				// pas par le DGEN pour la fonction #findIdDestinataireSuivant#
				generalDirectorUnit = ldapOperation.getUnitById(0);
				generalDirector = generalDirectorUnit.getResponsibleUnit();
				// special pour le BOC dans le cas ou la vlalidation ne passe
				// pas par le DGEN pour la fonction #findIdDestinataireSuivant#
			}
			statV1 = false;
			statV2 = false;
			List<TransactionDestination> listTransactionDest = appMgr
			.getDestinationByIdTransaction(courrierInformations
					.getTransaction().getTransactionId());
			TransactionDestination transactionDestination = new TransactionDestination() ;
			if (listTransactionDest != null && listTransactionDest.size() > 0) 
				transactionDestination = listTransactionDest.get(0);
			//TransactionDestination transactionDestination = selectedCourrier.getTransactionDestination();
			transactionDestination = appMgr.getDestinationByIdTransaction(transactionDestination.getId().getIdTransaction()).get(0);
			// XET - AH Si nous avons une validation hiérarchique arrivé
			if (variable.getVaraiablesValeur().equals("Oui")) {
				System.out.println("AH >>>>> validation_hierarchique_courrier_arrive == Oui");
				// si le courrier à D et l'expéditeur et le destinataire ne sont pas sou le même BO
				
				Integer idDestinataireReel = trDestinationReelle.getTransactionDestinationReelleIdDestinataire();
				System.out.println("AH >>>>> idDestinataireReel "+ idDestinataireReel);
				
				if( transaction.getCourrierType().equals("D"))	{
					if(isBocExp(transaction))
					executeTransactionInterne(transaction, courrierInformations);
				}
							
				
				// Vérifier si le destinataire réelle sous le même BO qui
				// exécute
				// Si "OUI"
				else if (transaction.getCourrierType().equals("I")) {
					System.out
							.println("##### La Ref est I c'est une première exécution d'un courrier ajouté par Direction");
					executeTransactionInterne(transaction, courrierInformations);
				} else {
					System.out.println("##### Ce n'est un Courrier interne "+ transaction.getCourrierType());

					// Si "NON" : passer le courrier au BO suivant
					if (trDestinationReelle.getTransactionDestinationReelleTypeDestinataire().equals("Interne-Person")) {
						// Le destinaite est Person
						System.out.println("Le destinaite réel est Person");
						if (idDestinataireReel != generalDirector.getId()) {
							System.out.println("idDestinataireReel != generalDirector.getId()");
							Integer idDestinataireSuivant = findIdDestinataireSuivant(idDestinataireReel, vb.getPerson().getId(),true, passageDGEN, generalDirector);
							// XTE - AH : si le destinataire suivant est le
							// destinataire final
							if (idDestinataireSuivant.equals(idDestinataireReel)) {
								System.out.println("##### Au dest Finale ");
								validateTransactionDestinataireFinale(transaction, transactionDestination);
							} else {
								System.out.println("#####  !au dest Finale ");
								validateTransactionDestinataireSuivant(transaction, transactionDestination,idDestinataireSuivant);
							}
						} else {
							System.out.println("Dans l'unite DG");
							validateTransactionDestinataireFinale(transaction,
									transactionDestination);
						}
					}
					else if (trDestinationReelle
							.getTransactionDestinationReelleTypeDestinataire()
							.equals("Interne-Unité")) {
						System.out.println("#####  Le destinaite réel est UNITE");
						if (idDestinataireReel != generalDirectorUnit
								.getIdUnit()) {
							System.out.println("#####  Ce n'est pas la DG");
							Integer idDestinataireSuivant = findIdDestinataireSuivant(
									idDestinataireReel, vb.getPerson().getId(),
									false, passageDGEN, generalDirector);

							Unit unit = ldapOperation
									.getUnitById(idDestinataireReel);
							System.out
.println(">>>>>> idDestinataireSuivant :: "
											+ idDestinataireSuivant);
							System.out
									.println(">>>>>> unit.getResponsibleUnit().getId() "
											+ unit.getResponsibleUnit().getId());
							if (idDestinataireSuivant.equals(unit
									.getResponsibleUnit().getId())) {
								System.out
										.println("#####  aller à : validateTransactionDestinataireFinale ");
								validateTransactionDestinataireFinale(
										transaction, transactionDestination);
							} else {
								System.out
										.println("#####  aller à : validateTransactionDestinataireSuivant ");
								validateTransactionDestinataireSuivant(
										transaction, transactionDestination,
										idDestinataireSuivant);
							}
						} else {
							System.out
									.println("#####  Dans else de idDestinataireReel != generalDirectorUnit.getIdUnit()");
							validateTransactionDestinataireFinale(transaction,
									transactionDestination);
						}
					}
				}
			}
			else {
				System.out.println("-->Variable Passage par DG :"+ variableToDGEN.getVaraiablesValeur());
				System.out.println("-->Variable Passage héarchique :"+ variable.getVaraiablesValeur());
				System.out.println("Valeur de la variable executé :"+ vb.isExecute());
				if (vb.isExecute()) {
					System.out.println("DANS vb.isExecute()");
					validateTransactionDestinataireFinale(transaction,
							transactionDestination);
				} else {
					System.out
							.println("Methode d'execution un courrier interne par BO");
					// [] Methode qui permet d'execute un courrier de
					// destination interne par BO
					executeTransactionInterne(transaction, courrierInformations);
				}
			}
			try {
				if (transactionDestination != null) {
					transactionDestination.getId();
					// AAA

					if (transactionDestination
							.getTransactionDestDateTransfert() == null) {
						transactionDestination
								.setTransactionDestDateTransfert(new Date());
					}
					if (transactionDestination
							.getTransactionDestDateConsultation() == null) {
						transactionDestination
								.setTransactionDestDateConsultation(new Date());
					}
					appMgr.update(transactionDestination);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			statV1 = true;
		} catch (Exception e) {
			statV2 = true;
			e.printStackTrace();
		}
	}

	
//	public boolean findBocExpBocDest(Transaction transaction) {
//
//		Transaction transactionExpediteur = new Transaction();
//		int idExp, idDest;
//		Person personneRechercheExp = new Person();
//		Person personneRechercheDes = new Person();
//		Unit unitDest = new Unit();
//		boolean findPersonExp = false;
//		boolean findPersonDest = false;
//		int k = 0;
//		int m = 0;
//
//		transactionExpediteur = appMgr
//				.getTransactionExpediteurByIdTransactionDestinationReelle(
//						transaction.getTransactionDestinationReelle()
//								.getTransactionDestinationReelleId(), 1).get(0);
//		List<TransactionDestination> listTransactionsDest = appMgr
//				.getDestinationByIdTransaction(transactionExpediteur
//						.getTransactionId());
//		TransactionDestination transactionDest = listTransactionsDest.get(0);
//		List<Expdest> listExpd = appMgr
//				.getListExpDestByIdExpDest(transactionDest.getId()
//						.getIdExpDest());
//
//		// type Expediteur
//		String typeExpdests = transactionExpediteur.getExpdest()
//				.getTypeExpDest();
//
//		System.out.println("2019-05-16 Type Expdests : " + typeExpdests);
//		System.out.println("2019-05-16:Type Intervenant : "
//				+ transactionExpediteur.getTransactionTypeIntervenant());
//		System.out.println("2019-05-16 : taille listTransactionsDest : "
//				+ listTransactionsDest.size());
//
//		// [] :Expédtiteur
//
//		if (typeExpdests.equals("Externe")) {
//			System.out.println("2019-05-16: Externe");
//			IdExpediteur = transactionExpediteur.getExpdest()
//					.getExpdestexterne().getIdExpDestExterne();
//
//		} else if (typeExpdests.equals("Interne-Unité")) {
//			System.out.println("2019-05-16: Interne-Unité");
//
//			// []:Id Expediteur
//			IdExpediteur = transactionExpediteur.getExpdest()
//					.getIdExpDestLdap();
//			System.out.println("2019-05-16  IdExpediteur : " + IdExpediteur);
//
//			// Unit unit=vb.getLdapOperation().getUnitById(IdExpediteur);
//			// System.out.println("2019-05-16 unit : "+unit);
//			// getIdBocByUnit(unit);
//			idBocExpediteur = listExpd.get(0).getIdExpDestLdap();
//			// idBocExpediteur=unit.getAssociatedBOC().getIdBOC();
//			System.out.println("2019-05-16 : idBocExpediteur : "
//					+ idBocExpediteur);
//
//		} else {
//			System.out.println("2019-05-16: Interne-Person");
//			IdExpediteur = transactionExpediteur.getExpdest()
//					.getIdExpDestLdap();
//			do {
//				idExp = vb.getCopyLdapListUser().get(k).getId();
//				System.out
//						.println("==============================================");
//				System.out.println("2019-05-16 : id Expdest :" + idExp);
//				System.out.println("2019-05-16 : id IdExpediteur :"
//						+ IdExpediteur);
//				System.out
//						.println("==============================================");
//
//				if (idExp == IdExpediteur) {
//					findPersonExp = true;
//					personneRechercheExp = vb.getCopyLdapListUser().get(k);
//				} else {
//					k++;
//				}
//
//			} while (!findPersonExp && k < vb.getCopyLdapListUser().size());
//
//			idBocExpediteur = ReturnBocAssociéeUnite(personneRechercheExp
//					.getAssociatedDirection());
//
//		}
//		System.out
//				.println("================ Fin Expéditeur ==============================================");
//
//		System.out
//				.println("================== Début Destinataire ==============================================");
//
//		// [] :2019-05-16: Destinataire
//		String tyeDestinataire = transactionExpediteur
//				.getTransactionDestinationReelle()
//				.getTransactionDestinationReelleTypeDestinataire();
//
//		System.out.println("tyeDestinataire : 0" + tyeDestinataire);
//		if (tyeDestinataire.equals("Externe")) {
//			System.out.println("2019-05-16: Externe");
//
//		} else if (tyeDestinataire.equals("Interne-Unité")) {
//			System.out.println("2019-05-16: Interne-Unité");
//
//			int idUnitDes = transactionExpediteur
//					.getTransactionDestinationReelle()
//					.getTransactionDestinationReelleIdDestinataire();
//
//			System.out.println("2019-06-27 idUnitDes " + idUnitDes);
//
//			do {
//
//				idDest = vb.getCopyLdapListUnit().get(m).getIdUnit();
//				System.out
//						.println("==============================================");
//				System.out.println(" ID DEST =" + idDest);
//				System.out.println(" ID User Des = " + idUnitDes);
//				System.out
//						.println("==============================================");
//
//				if (idDest == idUnitDes) {
//					findPersonDest = true;
//					unitDest = vb.getCopyLdapListUnit().get(m);
//					break;
//				} else {
//					m++;
//				}
//
//			} while (!findPersonDest && m < vb.getCopyLdapListUnit().size());
//
//			System.out.println("# Personne Recherche DES :" + unitDest);
//			Unit uni = ldapOperation.getUnitById(unitDest.getIdUnit());
//			getIdBocByUnit(uni);
//			System.out.println("2019-05-16 : idBocDestinataire : "
//					+ idBocDestinataire);
//			// [2019-06-27]
//			// Unit unit = vb.getLdapOperation().getUnitById(idUserDes);
//			//
//			// getIdBocByUnit(unit);
//			// idBocDestinataire = idBoc;
//			// // idBocDestinataire=unit.getAssociatedBOC().getIdBOC();
//			// System.out.println("2019-05-16 : idBocDestinataire : "
//			// + idBocDestinataire);
//		} else {
//			System.out.println("2019-05-16 Destinataire : Interne-Person");
//
//			int idUserDes = transactionExpediteur
//					.getTransactionDestinationReelle()
//					.getTransactionDestinationReelleIdDestinataire();
//
//			do {
//
//				idDest = vb.getCopyLdapListUser().get(m).getId();
//				System.out
//						.println("==============================================");
//				System.out.println("ID DEST :" + idDest);
//				System.out.println(" ID User Des : " + idUserDes);
//				System.out
//						.println("==============================================");
//				if (idDest == idUserDes) {
//					findPersonDest = true;
//					personneRechercheDes = vb.getCopyLdapListUser().get(m);
//				} else {
//					m++;
//				}
//
//			} while (!findPersonDest && m < vb.getCopyLdapListUser().size());
//
//			System.out.println("# Personne Recherche DES :"
//					+ personneRechercheDes);
//
//			getIdBocByUnit(personneRechercheDes.getAssociatedDirection());
//			System.out.println("2019-05-16 : idBocDestinataire : "
//					+ idBocDestinataire);
//
//		}
//
//		System.out
//				.println("=============================Fin Destinataire ============================");
//
//		// KHA : unité superieur sous 1er BO
//		Unit uniteSuperieurDestinataire = unitSup;
//
//		System.out.println("id Boc Destinataire :" + idBocDestinataire);
//		System.out.println("id Boc Expediteur:" + idBocExpediteur);
//
//		// : Mettre à jour Table Transaction Après exécution :Changer
//		// etat de courrier Non traité -> traité
//
//		// [] ID Boc de l'unité Connecté == ID BOC
//		// Destinataire=======================================================
//		// -------------------------------------------------------------------------------------
//
//		int idBos = vb.getPerson().getAssociatedBOC().getIdBOC();
//		System.out.println("2019-05-16 idBoc :" + idBos);
//
//		if (idBos == idBoc) {
//			System.out.println("idBos == idBocDestinataire");
//			return true;
//		} else {
//			System.out.println("idBos <> idBocDestinataire");
//
//			return false;
//
//		}
//
//	}

	/**
	 * Cette méthode est utilisé pour vérifier si l'expéditeur est sous le BO
	 * connecté
	 */
	public boolean isBocExp(Transaction transaction) {

		Transaction transactionExpediteur = new Transaction();
		int idExp;
		Person personneRechercheExp = new Person();
		boolean findPersonExp = false;
		int k = 0;

		transactionExpediteur = appMgr
				.getTransactionExpediteurByIdTransactionDestinationReelle(
						transaction.getTransactionDestinationReelle()
								.getTransactionDestinationReelleId(), 1).get(0);
		System.out.println("$$$ " + transactionExpediteur);
		List<TransactionDestination> listTransactionsDest = appMgr
				.getDestinationByIdTransaction(transactionExpediteur
						.getTransactionId());

		TransactionDestination transactionDest = listTransactionsDest.get(0);

		List<Expdest> listExpd = appMgr
				.getListExpDestByIdExpDest(transactionDest.getId()
						.getIdExpDest());

		// type Expediteur
		String typeExpdests = transactionExpediteur.getExpdest()
				.getTypeExpDest();

		// /////////////////////// Debut Recherche Expéditeur dans LDAP
		if (typeExpdests.equals("Externe")) {
			// System.out.println("l'expéditeur est externe");
			IdExpediteur = transactionExpediteur.getExpdest()
					.getExpdestexterne().getIdExpDestExterne();
		} else if (typeExpdests.equals("Interne-Unité")) {

			// A tester si le BO associé à cet Unité et celui responsable à
			// l'exécution
			int idUniteExpediteur = transactionExpediteur.getExpdest()
					.getIdExpDestLdap();
			Unit u = ldapOperation.getUnitById(idUniteExpediteur);
			int idBocUnite = ReturnBocAssocieeUnite(u);
			System.out.println("idBocUnite :: " + idBocUnite);
			// Chercher le BO responsable à l'exécution dans transactionDest
			int boResponsableExecution = listExpd.get(0).getIdExpDestLdap();

			// si le même si non ça reste à 0
			if (idBocUnite == boResponsableExecution)
				idBocExpediteur = listExpd.get(0).getIdExpDestLdap();
		} else {
			IdExpediteur = transactionExpediteur.getExpdest()
					.getIdExpDestLdap();
			do {
				idExp = vb.getCopyLdapListUser().get(k).getId();

				if (idExp == IdExpediteur) {
					findPersonExp = true;
					personneRechercheExp = vb.getCopyLdapListUser().get(k);
				} else {
					k++;
				}
			} while (!findPersonExp && k < vb.getCopyLdapListUser().size());
			int idBocUnite = ReturnBocAssocieeUnite(personneRechercheExp
					.getAssociatedDirection());
			System.out.println("idBocUnite :: " + idBocUnite);
			// Chercher le BO responsable à l'exécution dans transactionDest
			int boResponsableExecution = listExpd.get(0).getIdExpDestLdap();

			// si le même si non ça reste à 0
			if (idBocUnite == boResponsableExecution)
				idBocExpediteur = listExpd.get(0).getIdExpDestLdap();
		}
		// /////////////////////// FIN Recherche Destinataire LDAP
		// ///////////////////////

		int idBosConnecte = vb.getPerson().getAssociatedBOC().getIdBOC();
		System.out.println("idBosConnecte  " + idBosConnecte
				+ " && idBocExpediteur " + idBocExpediteur);
		if (idBosConnecte == idBocExpediteur) {
			// System.out.println("idBos == idBocDestinataire");
			return true;
		} else {
			// System.out.println("idBos <> idBocDestinataire");
			return false;
		}

	}

	public int ReturnBocAssocieeUnite(Unit u) {
		/*
		 * if (u.getAssociatedBOC() != null) return
		 * u.getAssociatedBOC().getIdBOC(); else if (u.getAssociatedUnit() !=
		 * null) return ReturnBocAssocieeUnite(u.getAssociatedUnit()); else
		 * return 0;
		 */

		if (u.getAssociatedUnit() != null && u.getIdUnit() != null) {

			Unit unite = ldapOperation.getUnitById(u.getAssociatedUnit()
					.getIdUnit());
			return ReturnBocAssocieeUnite(unite);

		} else if (u.getAssociatedBOC() != null) {
			return u.getAssociatedBOC().getIdBOC();

		} else {

			System.out
					.println("DANS else unit.getAssociatedUnit() != null && unit.getAssociatedBOC()!=null ");
			return 0;
		}

	}


	Transaction creerNewTransaction(Expdest expdest, int idUtilisateur,
			String etatLibelle, String typetransactionLibelle,
			int newOrderNumber, int first, Dossier dossier,
			TransactionDestinationReelle transactionDestinationReelle) {
		Transaction newTransaction = new Transaction();

		newTransaction.setExpdest(expdest);
		newTransaction.setIdUtilisateur(idUtilisateur);

		newTransaction.setTransactionDateTransaction(new Date());

		// set Type Transaction----------------------------------------------
		Typetransaction typetransaction = appMgr.getTypeTransactionByLibelle(
				typetransactionLibelle).get(0);
		newTransaction.setTypetransaction(typetransaction);
		Etat etat = appMgr.listEtatByLibelle(etatLibelle).get(0);

		newTransaction.setEtat(etat);
		newTransaction.setTransactionSupprimer(true);
		newTransaction.setTransactionOrdre(newOrderNumber);

		newTransaction.setDossier(dossier);
		newTransaction
				.setTransactionDestinationReelle(transactionDestinationReelle);

		newTransaction.setTransactionFirst(first);

		return newTransaction;
	}

	/**
	 * Cette méthode est utilisé pour vérifier si le destinataire est sous le BO
	 * connecté
	 */
	public boolean isBocDest(Transaction transaction) {

		Transaction transactionExpediteur = new Transaction();
		int idDest;
		Person personneRechercheDes = new Person();
		boolean findPersonDest = false;
		int m = 0;

		transactionExpediteur = appMgr
				.getTransactionExpediteurByIdTransactionDestinationReelle(
						transaction.getTransactionDestinationReelle()
								.getTransactionDestinationReelleId(), 1).get(0);
		String tyeDestinataire = transactionExpediteur
				.getTransactionDestinationReelle()
				.getTransactionDestinationReelleTypeDestinataire();

		// /////////////////////// Debut Recherche Destinataire LDAP
		// ///////////////////////

		if (tyeDestinataire.equals("Externe")) {
			// System.out.println("2019-05-16: Externe");
		} else if (tyeDestinataire.equals("Interne-Unité")) {
			// Recherche de l'ID Destinataire (Dest = Unité) dans LDAP
			int idUnitDes = transactionExpediteur
					.getTransactionDestinationReelle()
					.getTransactionDestinationReelleIdDestinataire();
			// do {
			// idDest = vb.getCopyLdapListUnit().get(m).getIdUnit();
			// if (idDest == idUnitDes) {
			// findPersonDest = true;
			// unitDest = vb.getCopyLdapListUnit().get(m);
			// break;
			// } else {
			// m++;
			// }
			// } while (!findPersonDest && m < vb.getCopyLdapListUnit().size());

			Unit uni = ldapOperation.getUnitById(idUnitDes);
			System.out.println(">>>>>>>>>>>>>>>>>>> uni" + uni);
			idBocDestinataire = ReturnBocAssocieeUnite(uni);
			// FFFF
		} else {
			// Recherche de l'ID Destinataire (Dest = Person) dans LDAP
			int idUserDes = transactionExpediteur
					.getTransactionDestinationReelle()
					.getTransactionDestinationReelleIdDestinataire();
			do {
				idDest = vb.getCopyLdapListUser().get(m).getId();
				if (idDest == idUserDes) {
					findPersonDest = true;
					personneRechercheDes = vb.getCopyLdapListUser().get(m);
				} else {
					m++;
				}
			} while (!findPersonDest && m < vb.getCopyLdapListUser().size());
			idBocDestinataire = ReturnBocAssocieeUnite(personneRechercheDes
					.getAssociatedDirection());
		}
		// /////////////////////// FIN Recherche Destinataire LDAP
		// ///////////////////////

		int idBosConnecte = vb.getPerson().getAssociatedBOC().getIdBOC();
		System.out.println("idBosConnecte  " + idBosConnecte
				+ " && idBocDestinataire " + idBocDestinataire);
		if (idBosConnecte == idBocDestinataire) {
			// System.out.println("idBos == idBocDestinataire");
			return true;
		} else {
			// System.out.println("idBos <> idBocDestinataire");
			return false;
		}

	}

	
	
	// XTE - AH : Ancienne methode d'exécution Modifier avec SR
	private void executeTransactionInterne(Transaction transaction,
			CourrierInformations courrierInformations) {

		System.out.println("### Méthode executeTransactionInterne ###");

		if (courrierInformations.getCourrier() == null) {
			courrierInformations.setCourrier(appMgr.getCourrierByIdCourrier(
					courrierInformations.getCourrierID()).get(0));

		}

		if (courrierInformations.getTransaction() == null) {
			courrierInformations.setTransaction(appMgr
					.getListTransactionByIdTransaction(
							courrierInformations.getTransactionID()).get(0));
		}

		setStatV1(false);
		setStatV2(false);
		int etatId = courrierInformations.getTransaction().getEtat().getEtatId();

		// Transaction transaction = new Transaction();
		// transaction = courrierInformations.getTransaction();
		Transaction newTransaction;
		List<Transaction> transactionExpediteur;
		List<Transaction> listTransaction = new ArrayList<Transaction>();
		List<Transaction> listTransactionByEtat = new ArrayList<Transaction>();

		// KHA :
		courrier = courrierInformations.getCourrier();
		// transaction=courrierInformations.getTransaction();
		vb.setCourDossConsulterInformations(courrierInformations);

		TransactionDestination transactionDestination = vb.getCourDossConsulterInformations().getTransactionDestination();
		//System.out.println(">>>>>>>>>##### courrier.getCourrierAvecDocumentPhysique()");
		if(transactionDestination.getTransactionDestEtatReceptionPhysique()!=null)
			System.out.println(transactionDestination.getTransactionDestEtatReceptionPhysique().getEtatId());
		else 
			System.out.println("l'état de la Recep Phy est null");
		
		try {

			if (transaction.getTransactionDestinationReelle() != null) {

				newTransaction = new Transaction();
				transactionExpediteur = new ArrayList<Transaction>();
				Etat etat = new Etat();
				Expdest expdest = new Expdest();
				Typetransaction typetransaction = new Typetransaction();
				TransactionDestinationId id = new TransactionDestinationId();
				TransactionDestination trDest = new TransactionDestination();

				// : Transaction By id Transaction Destinataire Reele et ordre=1
				System.out.println("JS 2019-08-05 "
						+ transaction.getTransactionDestinationReelle()
								.getTransactionDestinationReelleId());
				transactionExpediteur = appMgr
						.getTransactionExpediteurByIdTransactionDestinationReelle(
								transaction.getTransactionDestinationReelle()
										.getTransactionDestinationReelleId(), 1);
				System.out.println("size transactionExpediteur : "
						+ transactionExpediteur.size());
				boolean expediteurExterne = false;
				if (transactionExpediteur != null
						&& transactionExpediteur.size() > 0) {
					Expdest expedest = transactionExpediteur.get(0)
							.getExpdest();
					if (expedest != null) {
						if (expedest.getExpdestexterne() != null) {
							System.out.println("l'expéditeur est externe");
							expediteurExterne = true;
						}

					}
				}
				// System.out.println("tr exp : "+transactionExpediteur.get(0).getExpdest());
				// SI l'expéditeur Externe

				//
				// System.out.println("2019-05-22 ID Expéditeur :"+
				// transactionExpediteur.getExpdest().getIdExpDest());
				// System.out.println("2019-05-22 Type Expéditeur :"+
				// transactionExpediteur.getExpdest().getTypeExpDest());
				// System.out.println("2019-05-22 ID Destinataire Reelle :"+
				// transactionExpediteur.getTransactionDestinationReelle().getTransactionDestinationReelleId());
				// System.out.println("2019-05-22 Type Destinataire : "+
				// transactionExpediteur.getTransactionDestinationReelle().getTransactionDestinationReelleTypeDestinataire());
				//

				// :get liste
				// Transaction---------------------------------------------------------------------------
				// (Transaction Expéditeur+transaction Expéditeur après
				// execution) By id destination reelle--------------

				listTransaction = appMgr
						.getTransactionByIdTransactionDestinationReelle(transaction
								.getTransactionDestinationReelle()
								.getTransactionDestinationReelleId());

				listTransactionByEtat = appMgr
						.getTransactionByIdTransactionDestinationReelleByEtat(transaction
								.getTransactionDestinationReelle()
								.getTransactionDestinationReelleId());

				// : Variable utilisé pour comparer si BO Destinataire == BO
				// PErsonne Connecté
				// ----------------------------------------------------------------------------------------------------
				boolean destInBocConnecte = false;

				for (Transaction tra : transactionExpediteur) {
					idUserDes = tra.getTransactionDestinationReelle()
							.getTransactionDestinationReelleIdDestinataire();
					typeUserDes = tra.getTransactionDestinationReelle()
							.getTransactionDestinationReelleTypeDestinataire();
				}

				// KHA : unité superieur sous 1er BO boc De destinataire
				int idDest;
				Person personneRechercheDes = new Person();
				Unit unitRechecheDes = new Unit();
				boolean findPersonDest = false;
				boolean findUnitDest = false;

				int k = 0;

				if (typeUserDes.equals("Interne-Person")) {

					do {

						idDest = vb.getCopyLdapListUser().get(k).getId();

						if (idDest == idUserDes) {
							findPersonDest = true;
							personneRechercheDes = vb.getCopyLdapListUser()
									.get(k);
						} else {
							k++;
						}

					} while (!findPersonDest
							&& k < vb.getCopyLdapListUser().size());
					getIdBocByUnit(personneRechercheDes
							.getAssociatedDirection());

				} else {
					do {

						idDest = vb.getCopyLdapListUnit().get(k).getIdUnit();

						if (idDest == idUserDes) {
							findUnitDest = true;
							unitRechecheDes = vb.getCopyLdapListUnit().get(k);
						} else {
							k++;
						}

					} while (!findUnitDest
							&& k < vb.getCopyLdapListUnit().size());

					Unit unite = ldapOperation.getUnitById(unitRechecheDes
							.getIdUnit());

					getIdBocByUnit(unite);
				}

				int idBocDestinataire = idBoc;

				System.out.println("# id BOC Destinataire :"
						+ idBocDestinataire);
				// KHA : unité superieur sous 1er BO
				Unit uniteSuperieurDestinataire = unitSup;

				// [] findBocExpBocDest : retourne true si boc dest et bos
				// connecté sous même BO------------------------------
				// -------------------------------------------------------------------------------------
				// destInBocConnecte = isBocDest(transaction);
				System.out.println("isBocDest(transaction) "+ isBocDest(transaction));
				System.out.println("isBocExp(transaction)"+ isBocExp(transaction));

				// [] : on execute les Courrier where son Etat = 5
				// ----------------------------------------------
				// -------------------------------------------------------------------------------------
				if (etatId == 5) {

//					if (expediteurExterne) {
//						System.out
//								.println("DANS le TEST de l'expéditeur EXTN ");
//
//					}

					// XTE - AH : l'expéditeur et le destinataire sous le même
					// BO
					String referenceCourrierTypeNewTr = "";

					int referenceCourrierNumeroNewTr = 0;
					String referenceCourrierCompletNewTr = "";

					String referenceCourrierTypeOldTr = "";
					int referenceCourrierNumeroOldTr = 0;
					String referenceCourrierCompletOldTr = "";

					int annee = courrierInformations.getCourrier()
							.getCourrierOldDateOper();
					int year = annee;
					if (isBocDest(transaction)) {

						if (isBocExp(transaction)) {
							System.out
									.println("Le destinataire et l'expéditeur sous  BOS connecté");
							referenceCourrierTypeNewTr = "I*";

							referenceCourrierTypeOldTr = "I*";
							Integer lastId = appMgr
									.CountAllCourrierRefIByTransaction("I",
											annee);
							if (lastId != null) {
								lastId++;
							} else {

								lastId = 1;
							}
							referenceCourrierNumeroNewTr = lastId;
							referenceCourrierNumeroOldTr = lastId;

						} else {
//							System.out
//									.println("==========================================> jalila");
//							System.out
//									.println("Le destinataire seulement sous  BOS connecté (l'expéditeur non)");
//							System.out
//									.println("==========>>>>>>>>>>>>> transaction "
//											+ transaction.getTransactionId());
//							System.out
//									.println("==========>>>>>>>>>>>>> transaction type "
//											+ transaction.getCourrierType());
//							System.out
//									.println("==========>>>>>>>>>>>>> transaction "
//											+ transaction
//													.getCourrierTypeOrdre());

							referenceCourrierTypeOldTr = transaction
									.getCourrierType();
							referenceCourrierNumeroOldTr = transaction
									.getCourrierTypeOrdre();

							if (transaction.getCourrierType().equals("A")) {
								referenceSomeA = transaction.getCourrierTypeOrdre();
							}
							System.out
									.println(">>>>>>>>>>>>>>>> referenceSomeA "
											+ referenceSomeA);
							referenceCourrierTypeNewTr = "A";
							referenceCourrierNumeroNewTr = referenceSomeA;

						}

						List<Variables> listVariables = appMgr
								.listVariablesByLibelle("validation_hierarchique_courrier_arrive");

						if (listVariables != null && listVariables.size() > 0) {

							Variables variable = listVariables.get(0);

							int newOrderNumber = transaction
									.getTransactionOrdre();

							newOrderNumber++;

							newTransaction = creerNewTransaction(
									transactionExpediteur.get(0).getExpdest(),
									vb.getPerson().getId(), "Traité", "Envoi",
									newOrderNumber,
									transaction.getTransactionId(),
									transaction.getDossier(),
									transaction
											.getTransactionDestinationReelle());

							// 1ére Etape : Mise à jour Dans table Courrier
							if (courrier.getCourrierType().equals("I")) {
								System.out
										.println("AH =====> Mise à jour courrier");
								Integer lastId = appMgr
										.CountAllCourrierBOCByTransaction(courrierInformations.getDossierID(),
												"D",
												year,
												"boc_"
														+ String.valueOf(vb
																.getPerson()
																.getAssociatedBOC()
																.getIdBOC()),
												listIdBocMembers);
								
								
								List<Courrier> listeCourrier=appMgr.getCourrierByIdCourrier(courrierInformations.getCourrier().getIdCourrier().intValue());
								if(listeCourrier!=null && listeCourrier.size()>0)
									courrier=listeCourrier.get(0);
								courrier
										.setCourrierType("D");
								if (lastId != null) {
									courrier
											.setCourrierTypeOrdre(lastId + 1);
								} else {
									courrier
											.setCourrierTypeOrdre(1);
								}
								
								courrier
									.setCourrierReferenceCorrespondant(
											courrierInformations.getCourrier()
													.getCourrierType()
													+ courrierInformations
															.getCourrier()
															.getCourrierTypeOrdre());
							System.out
									.println("REF Courrier  : "
											+ courrierInformations
													.getCourrier()
													.getCourrierReferenceCorrespondant());
							appMgr.update(courrier);
							courrierInformations
							.setCourrier(courrier);
								
							}

							// update Ref Courrier dans table courrier
							System.out.println("Type Courrier : "
									+ courrierInformations.getCourrier()
											.getCourrierType());
							System.out.println("Type Ordre : "
									+ courrierInformations.getCourrier()
											.getCourrierTypeOrdre());

							//courrierInformations
							//		.getCourrier()
							//		.setCourrierReferenceCorrespondant(
							//				courrierInformations.getCourrier()
							//						.getCourrierType()
							//						+ courrierInformations
							//								.getCourrier()
							//								.getCourrierTypeOrdre());
							System.out
									.println("REF Courrier  : "
											+ courrierInformations
													.getCourrier()
													.getCourrierReferenceCorrespondant());
							//appMgr.update(courrierInformations.getCourrier());

							List<Transaction> transaction2s = appMgr
									.getListTransactionByIdTransaction(transaction
											.getTransactionId());

							if (transaction2s != null
									&& transaction2s.size() > 0) {
								Transaction transaction2 = transaction2s.get(0);
								// transaction2.setCourrierReferenceCorrespondant(courrierInformations.getCourrier().getCourrierType()
								// +
								// courrierInformations.getCourrier().getCourrierTypeOrdre());

								// transaction2.setCourrierType(courrierInformations.getCourrier().getCourrierType());
								//
								// transaction2.setCourrierTypeOrdre(courrierInformations.getCourrier().getCourrierTypeOrdre());

								etat = appMgr.listEtatByLibelle("Traité")
										.get(0);
								transaction2.setEtat(etat);
								appMgr.update(transaction2);
//								System.out.println("TR2 :: "+transaction2.getTransactionCommentaireAnnotation());
								newTransaction.setTransactionCommentaireAnnotation(transaction2.getTransactionCommentaireAnnotation());
							
							}

							// [] : Mettre à jour référence courrier dans
							// transaction
							newTransaction
									.setCourrierReferenceCorrespondant(newTransaction
											.getCourrierType()
											+ newTransaction
													.getCourrierTypeOrdre());

							// ///////////////////////////// UPDATE Référence
							// Transaction New /////////////////////////

							newTransaction.setCourrierDateReceptionAnnee(annee);
							newTransaction
									.setCourrierType(referenceCourrierTypeNewTr);
							newTransaction
									.setCourrierTypeOrdre(referenceCourrierNumeroNewTr);
							newTransaction
									.setCourrierReferenceCorrespondant(referenceCourrierTypeNewTr
											+ referenceCourrierNumeroNewTr);

							// ///////////////////////////// FIN UPDATE
							// Référence Transaction New
							// /////////////////////////

							appMgr.insert(newTransaction);

							// : Fin insertion dans la table Transaction
							// ===========================================================
							// -----------------------------------------------------------

							// : Début Test si Destinataire intern-Person ou
							// interne-Unité expdest------------------------

							expdest = new Expdest();

							TransactionDestinationReelle transactionDestinationReelle = appMgr
									.getTransactionDestinationReelById(transaction
											.getTransactionDestinationReelle()
											.getTransactionDestinationReelleId());

							// XTE - AH : Si la variable
							// "validation_hierarchique_courrier_arrive"= Non
							if (variable.getVaraiablesValeur().equals("Non")) {

								// [] :Type Destinataire Reelle :Interne-Person
								// Insertion Dans Expdest---
								if (transaction
										.getTransactionDestinationReelle()
										.getTransactionDestinationReelleTypeDestinataire()
										.equals("Interne-Person")) {

									expdest.setTypeExpDest("Interne-Person");
									Person personDestinationReel = vb
											.getLdapOperation()
											.getPersonalisedUserById(
													transaction
															.getTransactionDestinationReelle()
															.getTransactionDestinationReelleIdDestinataire());

									expdest.setIdExpDestLdap(personDestinationReel
											.getId());

									if (personDestinationReel.isResponsable()) {
										type = "sub_"
												+ personDestinationReel.getId();
									} else if (personDestinationReel
											.isSecretary()) {
										type = "secretary_"
												+ personDestinationReel.getId();

									} else {
										type = "agent_"
												+ personDestinationReel.getId();
									}
									trDest.setTransactionDestIdIntervenant(personDestinationReel
											.getId());
									trDest.setTransactionDestDateReponse(courrierInformations
											.getCourrier()
											.getCourrierDateReponse());
											
									
											
											
								} 
								else

								// [] :type Destinataire Reelle :Interne-Unité
								// Insertion Dans Expdest-----

								if (transaction
										.getTransactionDestinationReelle()
										.getTransactionDestinationReelleTypeDestinataire()
										.equals("Interne-Unité")) {

									expdest.setTypeExpDest("Interne-Unité");
									Unit unit = vb
											.getLdapOperation()
											.getUnitById(
													transaction
															.getTransactionDestinationReelle()
															.getTransactionDestinationReelleIdDestinataire());

									expdest.setIdExpDestLdap(unit.getIdUnit());
									type = "unit_"
											+ transaction
													.getTransactionDestinationReelle()
													.getTransactionDestinationReelleIdDestinataire();
									trDest.setTransactionDestIdIntervenant(transaction
											.getTransactionDestinationReelle()
											.getTransactionDestinationReelleIdDestinataire());

								}
								appMgr.insert(expdest);
								// : Fin Insertion Dans
								// expdest-------------------------------------------------------------------------------------------------

								// : set date de traitement de transaction
								transactionDestinationReelle
										.setTransactionDestinationReelleDateTraitement(new Date());
								appMgr.update(transactionDestinationReelle);

								// : Fin Insertion Dans
								// Transaction Dest
								// -------------------------------------------------------------------------------------------------
								id = new TransactionDestinationId();
								trDest = new TransactionDestination();
								id.setIdTransaction(newTransaction
										.getTransactionId());
								id.setIdExpDest(expdest.getIdExpDest());
								trDest.setId(id);
								trDest.setTransactionDestTypeIntervenant(type);
								trDest.setTransactionDestDateReponse(courrierInformations
										.getCourrier().getCourrierDateReponse());
										if(transactionDestination.getTransactionDestEtatReceptionPhysique()!=null){
										Etat etatNonReceptionPhysique = new Etat();
										etatNonReceptionPhysique.setEtatId(9);
										trDest.setTransactionDestEtatReceptionPhysique(etatNonReceptionPhysique);
									}
								appMgr.insert(trDest);

								// : Fin insert transactionDestination
								// -------------------------------------------------------------------------------------------------

								// update etat dans table transaction : etat5 ->
								// etat6
								for (Transaction transaction1 : listTransaction) {

									// ///////////////////////////// UPDATE
									// Référence Transaction Old
									// /////////////////////////

									transaction1
											.setCourrierDateReceptionAnnee(annee);
									transaction1
											.setCourrierType(referenceCourrierTypeOldTr);
									transaction1
											.setCourrierTypeOrdre(referenceCourrierNumeroOldTr);
									transaction1
											.setCourrierReferenceCorrespondant(referenceCourrierTypeOldTr
													+ referenceCourrierNumeroOldTr);

									// ///////////////////////////// FIN UPDATE
									// Référence Transaction Old
									// /////////////////////////

									transaction1.setEtat(etat);
									appMgr.update(transaction1);

								}

								// List<Transaction>
								// transactions=appMgr.getListTransactionByIdTransaction(transaction.getTransactionId());
								// System.out.println("2019-05-31 : "+transactions.size());
								// for(Transaction t:transactions)
								// {
								// System.out.println("====== 2019-05-31 =======");
								// System.out.println(t.getTransactionId());
								// System.out.println("=====================");
								// }
								// if(transactions!=null &&
								// transactions.size()>0){
								// Transaction transaction2=transactions.get(0);
								// System.out.println("2019-05-25 : Type Courrier "+courrierInformations.getCourrier().getCourrierType());
								// System.out.println("2019-05-25:  Type Ordre : "+courrierInformations.getCourrier().getCourrierTypeOrdre());
								// transaction2.setCourrierReferenceCorrespondant(courrierInformations.getCourrier().getCourrierType()
								// +courrierInformations.getCourrier().getCourrierTypeOrdre());
								// appMgr.update(transaction2);
								// }
								//

							}
							// XTE - AH : si la
							// "validation_hierarchique_courrier_arrive"= Oui
							else {
								System.out
										.println("########### Validation #######################");
								// // KHA : validation hiérarchique :
								// Destinataire Suivant
								Integer idDestinataireReel = transaction
										.getTransactionDestinationReelle()
										.getTransactionDestinationReelleIdDestinataire();

								// KHA : Si destinataire reel de type Person
								if (transaction
										.getTransactionDestinationReelle()
										.getTransactionDestinationReelleTypeDestinataire()
										.equals("Interne-Person")) {

									// Person : Destinataire suivant n'est pas
									// le Destinataire reel

									if (idDestinataireReel != uniteSuperieurDestinataire
											.getResponsibleUnit().getId()) {

										etat = appMgr.listEtatByLibelle(
												"A valider").get(0);
										newTransaction.setEtat(etat);
										appMgr.update(newTransaction);
										expdest.setTypeExpDest("Interne-Person");
										expdest.setIdExpDestLdap(uniteSuperieurDestinataire
												.getResponsibleUnit().getId());
										appMgr.insert(expdest);
										id = new TransactionDestinationId();
										trDest = new TransactionDestination();

										id.setIdTransaction(newTransaction
												.getTransactionId());
										id.setIdExpDest(expdest.getIdExpDest());

										String type = "sub_"
												+ uniteSuperieurDestinataire
														.getResponsibleUnit()
														.getId();

										trDest.setTransactionDestTypeIntervenant(type);
										trDest.setTransactionDestIdIntervenant(uniteSuperieurDestinataire
												.getResponsibleUnit().getId());
										trDest.setId(id);
										if(transactionDestination.getTransactionDestEtatReceptionPhysique()!=null){
										Etat etatNonReceptionPhysique = new Etat();
										etatNonReceptionPhysique.setEtatId(9);
										trDest.setTransactionDestEtatReceptionPhysique(etatNonReceptionPhysique);
									}
										appMgr.insert(trDest);

										// : Fin insert transactionDestination
										// -------------------------------------------------------------------------------------------------

										// update etat dans table transaction
										// :etat5-> etat6

										for (Transaction transaction1 : listTransactionByEtat) {

											etat = appMgr.listEtatByLibelle(
													"Traité").get(0);
											transaction1.setEtat(etat);
											appMgr.update(transaction1);

										}

										//
										// Execution ==> Validation
										validateTransactionDestinataireSuivant(
												transaction,
												transactionDestination,
												uniteSuperieurDestinataire
														.getResponsibleUnit()
														.getId());

									} else {
										expdest.setTypeExpDest("Interne-Unité");
										expdest.setIdExpDestLdap(idDestinataireReel);
										appMgr.insert(expdest);
										id = new TransactionDestinationId();
										trDest = new TransactionDestination();
										id.setIdTransaction(newTransaction
												.getTransactionId());
										id.setIdExpDest(expdest.getIdExpDest());
										trDest.setId(id);

										String type = "sub_"
												+ idDestinataireReel;
										trDest.setTransactionDestTypeIntervenant(type);
										trDest.setTransactionDestIdIntervenant(idDestinataireReel);
										if(transactionDestination.getTransactionDestEtatReceptionPhysique()!=null){
										Etat etatNonReceptionPhysique = new Etat();
										etatNonReceptionPhysique.setEtatId(9);
										trDest.setTransactionDestEtatReceptionPhysique(etatNonReceptionPhysique);
									}
										appMgr.insert(trDest);

										// : Fin insert transactionDestination
										// -------------------------------------------------------------------------------------------------

										// update etat dans table transaction :
										// etat5-> etat6
										for (Transaction transaction1 : listTransaction) {
											// System.out.println("Id transaction : "+transaction1.getTransactionId());
											// System.out.println("etat transaction : "+transaction1.getEtat().getEtatLibelle());
											transaction1.setEtat(etat);
											appMgr.update(transaction1);
											// System.out.println("### Insertionstion : update Transaction ");
										}
										//

										validateTransactionDestinataireFinale(
												transaction,
												transactionDestination);
									}
								}

								// KHA : Si destinataire reel de type Unit
								if (transaction
										.getTransactionDestinationReelle()
										.getTransactionDestinationReelleTypeDestinataire()
										.equals("Interne-Unité")) {

									if (idDestinataireReel != uniteSuperieurDestinataire
											.getIdUnit()) {

										// Unit : Destinataire suivant n'est
										// pasle Destinataire reel
										validateTransactionDestinataireSuivant(
												transaction,
												transactionDestination,
												uniteSuperieurDestinataire
														.getResponsibleUnit()
														.getId());
									} else {
										validateTransactionDestinataireFinale(
												transaction,
												transactionDestination);
									}
								}

							}

						}

						// : Mettre a jour la reference pour indiquer qu'il est
						// un courrier de depart
						// -------------------------------------------------------------------------------------------------

						// [] Modification le 2019-05-15
						// Integer lastId =
						// appMgr.getCourrierLastIdByTypeOrdreAndAnnee("D",
						// year);
						// System.out.println("Last Id :"+lastId);
						// courrierInformations.getCourrier().setCourrierType("D");
						// if (lastId == null || lastId == 0) {
						// courrierInformations.getCourrier().setCourrierTypeOrdre(1);
						// } else {
						// courrierInformations.getCourrier().setCourrierTypeOrdre(lastId
						// + 1);
						// }
						// courrierInformations.getCourrier()
						// .setCourrierReferenceCorrespondant(
						// courrierInformations.getCourrier()
						// .getCourrierType()
						// + courrierInformations.getCourrier()
						// .getCourrierTypeOrdre());
						// appMgr.update(courrierInformations.getCourrier());

						// : Fin Mettre a jour la reference pour indiquer qu'il
						// est
						// un courrier de depart
						// -------------------------------------------------------------------------------------------------

						// : Mettre à jour Dossier et update libelle dossier
						// Courrier_Ix->Courrier_x avec x un entier
						// -------------------------------------------------------------------------------------------------
						Dossier dossier = appMgr.getDossierByIdDossier(
								transaction.getDossier().getDossierId()).get(0);
						dossier.setDossierIntitule("Courrier_"
								+ courrierInformations.getCourrier()
										.getIdCourrier());
						appMgr.update(dossier);

						setStatV1(true);

					}
					// ///////////////////////////////Fin
					// isBocDest////////////////////////////////////////////////

					else {

						if (isBocExp(transaction)) {
							System.out
									.println("L'expéditeur seulement  sous  BOS connecté ( Le destinataire non)");

							if (transaction.getCourrierType().equals("I")) {

								Integer lastId = appMgr
										.CountAllCourrierBOCByTransaction(courrierInformations.getDossierID(),
												"D",
												year,
												"boc_"
														+ String.valueOf(vb
																.getPerson()
																.getAssociatedBOC()
																.getIdBOC()),
												listIdBocMembers);

								referenceCourrierTypeNewTr = "D";
								referenceCourrierTypeOldTr = "D";

								if (lastId != null) {
									lastId++;
								} else {

									lastId = 1;
								}
								referenceCourrierNumeroNewTr = lastId;
								referenceCourrierNumeroOldTr = lastId;

								courrierInformations.getCourrier()
										.setCourrierType("D");
								courrierInformations.getCourrier()
										.setCourrierTypeOrdre(lastId);

							} 
							else {
								System.out.println("Le courrier de D ");

								referenceCourrierTypeNewTr = "D";
								referenceCourrierTypeOldTr = "D";

								referenceCourrierNumeroNewTr = transaction
										.getCourrierTypeOrdre();
								referenceCourrierNumeroOldTr = transaction
										.getCourrierTypeOrdre();
							}
						} 
						else {
							
							// Récupérer le BOC de Destinataire 
							System.out.println("################# La reference reste tel qu'elle était ################# "+transaction.getCourrierType());
							referenceCourrierTypeNewTr = transaction.getCourrierType();
							//referenceCourrierTypeOldTr = transaction.getCourrierType();

							referenceCourrierNumeroNewTr = transaction.getCourrierTypeOrdre();
							//referenceCourrierNumeroOldTr = transaction.getCourrierTypeOrdre();
							System.out
									.println("CAS NE S'APPLIQUE PAS : Expéditeur et Destinataire ne sont pas tout les deux sous le BO connecté!!!!");
						}

						// /////////////////////////// Debut insertTransaction
						// /////////////////////////////
						System.out
								.println("///////////////////////////// Debut insertTransaction /////////////////////////////");
						etat = appMgr.listEtatByLibelle("Traité").get(0);
						newTransaction.setExpdest(transactionExpediteur.get(0)
								.getExpdest());
						newTransaction.setIdUtilisateur(vb.getPerson().getId());
						newTransaction
								.setTransactionDateTransaction(new Date());
						typetransaction = appMgr.getTypeTransactionByLibelle(
								"Envoi").get(0);
						newTransaction.setTypetransaction(typetransaction);
						newTransaction.setEtat(etat);
						newTransaction.setTransactionSupprimer(true);
						int newOrderNumber = transaction.getTransactionOrdre();
						newOrderNumber++;
						newTransaction.setTransactionOrdre(newOrderNumber);
						newTransaction.setDossier(transaction.getDossier());
						newTransaction
								.setTransactionDestinationReelle(transaction
										.getTransactionDestinationReelle());
						appMgr.insert(newTransaction);
						System.out.println("trID "
								+ newTransaction.getTransactionId());
						newTransaction.setTransactionFirst(newTransaction
								.getTransactionId());
						appMgr.update(newTransaction);
						// /////////////////////////// Fin insertTransaction
						// /////////////////////////////

						// ///////////////////////////// UPDATE Référence
						// Transaction New /////////////////////////

						newTransaction.setCourrierDateReceptionAnnee(annee);
						newTransaction
								.setCourrierType(referenceCourrierTypeNewTr);
						newTransaction
								.setCourrierTypeOrdre(referenceCourrierNumeroNewTr);
						newTransaction
								.setCourrierReferenceCorrespondant(referenceCourrierTypeNewTr
										+ referenceCourrierNumeroNewTr);
						appMgr.insert(newTransaction);
						// ///////////////////////////// FIN UPDATE Référence
						// Transaction New /////////////////////////
						// Récupérer le courrier par ID 
						List<Courrier> listeCourrier=appMgr.getCourrierByIdCourrier(courrierInformations.getCourrier().getIdCourrier().intValue());
						if(listeCourrier!=null && listeCourrier.size()>0)
							courrier=listeCourrier.get(0);
						
						courrier.setCourrierType(referenceCourrierTypeNewTr);
						courrier.setCourrierTypeOrdre(referenceCourrierNumeroNewTr);
						
						courrier.setCourrierReferenceCorrespondant(
								courrier
								.getCourrierType()
								+ courrier
										.getCourrierTypeOrdre());
						
//						courrierInformations
//								.getCourrier()
//								.setCourrierReferenceCorrespondant(
//										courrierInformations.getCourrier()
//												.getCourrierType()
//												+ courrierInformations
//														.getCourrier()
//														.getCourrierTypeOrdre());
						
//						appMgr.update(courrierInformations.getCourrier());
						appMgr.update(courrier);

						// ///////////////////////////// UPDATE Référence
						// Transaction OLD /////////////////////////
						List<Transaction> transaction2s = appMgr
								.getListTransactionByIdTransaction(transaction
										.getTransactionId());
						if (transaction2s != null && transaction2s.size() > 0) {
							Transaction transaction2 = transaction2s.get(0);

							transaction2.setCourrierDateReceptionAnnee(annee);
							transaction2
									.setCourrierType(referenceCourrierTypeOldTr);
							transaction2
									.setCourrierTypeOrdre(referenceCourrierNumeroOldTr);
							transaction2
									.setCourrierReferenceCorrespondant(referenceCourrierTypeOldTr
											+ referenceCourrierNumeroOldTr);

							appMgr.update(transaction2);
						}
						// ///////////////////////////// UPDATE Référence
						// Transaction OLD /////////////////////////

						// ****************************************************************************************************************

						vb.setTransaction(newTransaction);

						// : Début Test si Destinataire intern-Person ou
						// interne-Unité
						// expdest-------------------------------------------------------------------------------------------------
						expdest = new Expdest();
						TransactionDestinationReelle transactionDestinationReelle = appMgr
								.getTransactionDestinationReelById(transaction
										.getTransactionDestinationReelle()
										.getTransactionDestinationReelleId());

						// [] :type Destinataire Reelle :Interne-Person
						if (transaction
								.getTransactionDestinationReelle()
								.getTransactionDestinationReelleTypeDestinataire()
								.equals("Interne-Person")) {
							Person personDestinationReel = vb
									.getLdapOperation()
									.getPersonalisedUserById(
											transaction
													.getTransactionDestinationReelle()
													.getTransactionDestinationReelleIdDestinataire());

							// ******************BOS 2

							int idUserDest = personDestinationReel.getId();
							int id1;
							Person personneRecherche = new Person();
							boolean findPerson = false;
							int j = 0;
							do {
								id1 = vb.getCopyLdapListUser().get(j).getId();
								if (id1 == idUserDest) {
									findPerson = true;
									personneRecherche = vb
											.getCopyLdapListUser().get(j);
								} else {
									j++;
								}
							} while (!findPerson
									&& j < vb.getCopyLdapListUser().size());

							getIdBocByUnit(personneRecherche
									.getAssociatedDirection());
							idBocDestinataire = idBoc;

							expdest.setTypeExpDest("Interne-Boc");
							expdest.setIdExpDestLdap(idBocDestinataire);
							String typeIntervenant = "boc_"
									+ String.valueOf(idBoc);
							appMgr.insert(expdest);

							// XTE : Insertion
							// transactionDestination---------------------------------------------------------------------------------------------
							id.setIdTransaction(vb.getTransaction()
									.getTransactionId());
							id.setIdExpDest(expdest.getIdExpDest());
							trDest.setId(id);
							trDest.setTransactionDestIdIntervenant(idBoc);
							trDest.setTransactionDestTypeIntervenant(typeIntervenant);
							if(transactionDestination.getTransactionDestEtatReceptionPhysique()!=null){
										Etat etatNonReceptionPhysique = new Etat();
										etatNonReceptionPhysique.setEtatId(8);
										trDest.setTransactionDestEtatReceptionPhysique(etatNonReceptionPhysique);
										System.out.println("le commentaired de la réception :: "+transactionDestination.getTransactionDestCommentaireReceptionPhysique());
										trDest.setTransactionDestCommentaireReceptionPhysique(transactionDestination.getTransactionDestCommentaireReceptionPhysique());
										trDest.setTransactionDestDateReceptionPhysique(transactionDestination.getTransactionDestDateReceptionPhysique());
									
									}
							appMgr.insert(trDest);

						} 
						else
						// [] :type Destinataire Reelle :Interne-Unité
						// [2019-06-27]
						if (transaction
								.getTransactionDestinationReelle()
								.getTransactionDestinationReelleTypeDestinataire()
								.equals("Interne-Unité")) {

							// expdest.setTypeExpDest("Interne-Unité");
							Unit unit = vb
									.getLdapOperation()
									.getUnitById(
											transaction
													.getTransactionDestinationReelle()
													.getTransactionDestinationReelleIdDestinataire());
							// ******************BOS 2

							int idUnitDest = unit.getIdUnit();
							int id1;
							Unit unitRecherche = new Unit();
							boolean findUnite = false;
							int j = 0;
							do {
								id1 = vb.getCopyLdapListUnit().get(j)
										.getIdUnit();
								if (id1 == idUnitDest) {
									findUnite = true;
									unitRecherche = vb.getCopyLdapListUnit()
											.get(j);
									break;
								} else {
									j++;
								}
							} while (!findUnite
									&& j < vb.getCopyLdapListUnit().size());

							Unit u = ldapOperation.getUnitById(unitRecherche
									.getIdUnit());
							System.out.println("Unité Dest : " + u);
							getIdBocByUnit(u);
							idBocDestinataire = idBoc;
							expdest.setTypeExpDest("Interne-Boc");

							expdest.setIdExpDestLdap(idBoc);
							String typeIntervenant = "boc_"
									+ String.valueOf(idBoc);
							appMgr.insert(expdest);

							id.setIdTransaction(vb.getTransaction()
									.getTransactionId());
							id.setIdExpDest(expdest.getIdExpDest());
							trDest.setId(id);
							trDest.setTransactionDestIdIntervenant(idBoc);

							trDest.setTransactionDestTypeIntervenant(typeIntervenant);
							if(transactionDestination.getTransactionDestEtatReceptionPhysique()!=null){
										Etat etatNonReceptionPhysique = new Etat();
										etatNonReceptionPhysique.setEtatId(8);
										trDest.setTransactionDestEtatReceptionPhysique(etatNonReceptionPhysique);
										System.out.println("le commentaired de la réception :: "+transactionDestination.getTransactionDestCommentaireReceptionPhysique());
										trDest.setTransactionDestCommentaireReceptionPhysique(transactionDestination.getTransactionDestCommentaireReceptionPhysique());
										trDest.setTransactionDestDateReceptionPhysique(transactionDestination.getTransactionDestDateReceptionPhysique());
									
									}
							appMgr.insert(trDest);
							// AAAAA
						}

						// : Fin Test
						// expdest-------------------------------------------------------------------------------------------------

						// : set date de traitement de transaction
						transactionDestinationReelle
								.setTransactionDestinationReelleDateTraitement(new Date());
						appMgr.update(transactionDestinationReelle);
						appMgr.insert(expdest);

						for (Transaction transaction1 : listTransaction) {
							etat = appMgr.listEtatByLibelle("Traité").get(0);
							transaction1.setEtat(etat);
							appMgr.update(transaction1);
						}

						// Calendar cal = Calendar.getInstance();
						// cal.setTime(courrierInformations.getCourrierDateReceptionEnvoi());
						// int year = cal.get(Calendar.YEAR);

						//appMgr.update(courrierInformations.getCourrier());
						Dossier dossier = appMgr.getDossierByIdDossier(
								transaction.getDossier().getDossierId()).get(0);
						dossier.setDossierIntitule("Courrier_"
								+ courrierInformations.getCourrier()
										.getIdCourrier());
						appMgr.update(dossier);

						// ///////////////////////////////////////////////////////////////////////////////////////////
						// /////////////////////////// Transaction Ajoutée pour
						// BOC DEST/////////////////////////////
						// ///////////////////////////////////////////////////////////////////////////////////////////
						BOC bocDest = new BOC();
						if (isBocExp(transaction)|| (!isBocExp(transaction)&& !isBocDest(transaction))) {
							System.out
									.println("AH ==> DANS !isBocDest(transaction) && isBocExp(transaction) ou les DEUX ne SONT pas sous BO CONNECTEE");
							// List<BOC>
							//AAAA
							// bocs=vb.getCentralBoc().getListChildBOCsBOC();
							System.out.println("idBocDestinataire = "
									+ idBocDestinataire);
							bocDest = ldapOperation
									.getBocByID(idBocDestinataire);
							// for(BOC b: bocs){
							// if(b.getIdBOC()==idBocDestinataire){
							// bocDest=b;
							// break;
							// }
							// }
							listIdBocMembers = new ArrayList<Integer>();
							System.out.println("Liste Membre ");
							List<Person> listBocMembers = bocDest
									.getMembersBOC();
							for (Person person : listBocMembers) {
								listIdBocMembers.add(person.getId());
								System.out.println(person.getId());
								if (person.isResponsableBO()) {
									responsableBocDest = person.getId();
								}
							}

							Integer lastId = appMgr
									.CountAllCourrierBOCByTransaction(courrierInformations.getDossierID(),"A",
											year, "boc_" + idBocDestinataire,
											listIdBocMembers);
											System.out
									.println("#### $$$$  ##### idBocDestinataire  "+ idBocDestinataire);

							referenceCourrierTypeNewTr = "A";

							if (lastId != null) {
								lastId++;
							} else {

								lastId = 1;
							}
							referenceCourrierNumeroNewTr = lastId;
							System.out
									.println("AH  ====>id responsableBocDest : "
											+ responsableBocDest);
												System.out
									.println("#### $$$$  ##### lastId  "+ lastId);
							// /////////////////////////// Debut
							// insertTransaction /////////////////////////////
							etat = appMgr.listEtatByLibelle("Non traité")
									.get(0);
							Transaction newTransactionBocDest = new Transaction();
							newTransactionBocDest
									.setExpdest(transactionExpediteur.get(0)
											.getExpdest());

							if (responsableBocDest != 0)
								newTransactionBocDest
										.setIdUtilisateur(responsableBocDest);
							else
								newTransactionBocDest
										.setIdUtilisateur(listIdBocMembers
												.get(0));

							newTransactionBocDest
									.setTransactionDateTransaction(new Date());
							typetransaction = appMgr
									.getTypeTransactionByLibelle("Envoi")
									.get(0);
							newTransactionBocDest
									.setTypetransaction(typetransaction);
							newTransactionBocDest.setEtat(etat);
							newTransactionBocDest.setTransactionSupprimer(true);
							int newOrderNumberDest = transaction
									.getTransactionOrdre();
							newOrderNumberDest++;
							newTransactionBocDest
									.setTransactionOrdre(newOrderNumberDest);
							newTransactionBocDest.setDossier(transaction
									.getDossier());
							newTransactionBocDest
									.setTransactionDestinationReelle(transaction
											.getTransactionDestinationReelle());

							newTransactionBocDest
									.setCourrierDateReceptionAnnee(annee);
							newTransactionBocDest
									.setCourrierType(referenceCourrierTypeNewTr);
							newTransactionBocDest
									.setCourrierTypeOrdre(referenceCourrierNumeroNewTr);
							newTransactionBocDest
									.setCourrierReferenceCorrespondant(referenceCourrierTypeNewTr
											+ referenceCourrierNumeroNewTr);
							newTransactionBocDest.setTransactionCommentaireAnnotation(transaction.getTransactionCommentaireAnnotation());
							appMgr.insert(newTransactionBocDest);
							newTransactionBocDest
									.setTransactionFirst(newTransactionBocDest
											.getTransactionId());
							System.out
									.println("newTransactionBocDest.getTransactionId() "
											+ newTransactionBocDest
													.getTransactionId());
													
							System.out
									.println("newTransactionBocDest référence "
											+ referenceCourrierNumeroNewTr);						
							appMgr.update(newTransactionBocDest);
							// /////////////////////////// Fin insertTransaction
							// /////////////////////////////

							// : Début Test si Destinataire intern-Person ou
							// interne-Unité
							// expdest-------------------------------------------------------------------------------------------------
							expdest = new Expdest();
							transactionDestinationReelle = appMgr
									.getTransactionDestinationReelById(transaction
											.getTransactionDestinationReelle()
											.getTransactionDestinationReelleId());

							// [] :type Destinataire Reelle :Interne-Person
							if (transaction
									.getTransactionDestinationReelle()
									.getTransactionDestinationReelleTypeDestinataire()
									.equals("Interne-Person")) {
								Person personDestinationReel = vb
										.getLdapOperation()
										.getPersonalisedUserById(
												transaction
														.getTransactionDestinationReelle()
														.getTransactionDestinationReelleIdDestinataire());

								// ******************BOS 2

								int idUserDest = personDestinationReel.getId();
								int id1;
								Person personneRecherche = new Person();
								boolean findPerson = false;
								int j = 0;
								do {
									id1 = vb.getCopyLdapListUser().get(j)
											.getId();
									if (id1 == idUserDest) {
										findPerson = true;
										personneRecherche = vb
												.getCopyLdapListUser().get(j);
									} else {
										j++;
									}
								} while (!findPerson
										&& j < vb.getCopyLdapListUser().size());

								getIdBocByUnit(personneRecherche
										.getAssociatedDirection());
								idBocDestinataire = idBoc;

								expdest.setTypeExpDest("Interne-Boc");
								expdest.setIdExpDestLdap(idBocDestinataire);
								String typeIntervenant = "boc_"
										+ String.valueOf(idBoc);
								appMgr.insert(expdest);

								// XTE : Insertion
								// transactionDestination---------------------------------------------------------------------------------------------

								TransactionDestination trDestNew = new TransactionDestination();
								id.setIdTransaction(newTransactionBocDest
										.getTransactionId());
								id.setIdExpDest(expdest.getIdExpDest());
								trDestNew.setId(id);
								trDestNew
										.setTransactionDestIdIntervenant(idBoc);
								trDestNew
										.setTransactionDestTypeIntervenant(typeIntervenant);
										if(transactionDestination.getTransactionDestEtatReceptionPhysique()!=null){
										Etat etatNonReceptionPhysique = new Etat();
										etatNonReceptionPhysique.setEtatId(9);
										trDestNew.setTransactionDestEtatReceptionPhysique(etatNonReceptionPhysique);
									}
								appMgr.insert(trDestNew);

							} else
							// [] :type Destinataire Reelle :Interne-Unité
							// [2019-06-27]
							if (transaction
									.getTransactionDestinationReelle()
									.getTransactionDestinationReelleTypeDestinataire()
									.equals("Interne-Unité")) {
								System.out
										.println("############################################[2019-06-27]");
								// expdest.setTypeExpDest("Interne-Unité");
								Unit unit = vb
										.getLdapOperation()
										.getUnitById(
												transaction
														.getTransactionDestinationReelle()
														.getTransactionDestinationReelleIdDestinataire());
								// ******************BOS 2

								int idUnitDest = unit.getIdUnit();
								int id1;
								Unit unitRecherche = new Unit();
								boolean findUnite = false;
								int j = 0;
								do {
									id1 = vb.getCopyLdapListUnit().get(j)
											.getIdUnit();
									if (id1 == idUnitDest) {
										findUnite = true;
										unitRecherche = vb
												.getCopyLdapListUnit().get(j);
										break;
									} else {
										j++;
									}
								} while (!findUnite
										&& j < vb.getCopyLdapListUnit().size());

								Unit u = ldapOperation
										.getUnitById(unitRecherche.getIdUnit());
								getIdBocByUnit(u);

								expdest.setTypeExpDest("Interne-Boc");

								expdest.setIdExpDestLdap(idBoc);
								String typeIntervenant = "boc_"
										+ String.valueOf(idBoc);
								appMgr.insert(expdest);
								System.out
										.println("////////////////////////////Inserton dans trDest ");
								System.out.println("expdest.getIdExpDest() "
										+ expdest.getIdExpDest());
								TransactionDestination trDestDest = new TransactionDestination();
								id.setIdTransaction(newTransactionBocDest
										.getTransactionId());
								id.setIdExpDest(expdest.getIdExpDest());
								trDestDest.setId(id);
								trDestDest
										.setTransactionDestIdIntervenant(idBoc);
								System.out
										.println("newTransactionBocDest.getTransactionId() "
												+ newTransactionBocDest
														.getTransactionId());
								trDestDest
										.setTransactionDestTypeIntervenant(typeIntervenant);
										if(transactionDestination.getTransactionDestEtatReceptionPhysique()!=null){
										Etat etatNonReceptionPhysique = new Etat();
										etatNonReceptionPhysique.setEtatId(9);
										trDestDest.setTransactionDestEtatReceptionPhysique(etatNonReceptionPhysique);
									}
								appMgr.insert(trDestDest);
								System.out
										.println("***************************************************************************** ");
								System.out
										.println("Fin ajout dans table trDest avec ");
								System.out.println(trDest.getId()
										.getIdTransaction());
								System.out.println(trDest.getId()
										.getIdExpDest());

							}

						}
						// ///////////////////////////////////////////////////////////////////////////////////////////
						// ///////////////////////////FIN Transaction Ajoutée
						// pour BOC DEST/////////////////////////////
						// ///////////////////////////////////////////////////////////////////////////////////////////

					}
					setStatV1(true);
				}
			} else {
				System.out.println(" etat 6");
			}
		} catch (Exception e) {
			setStatV2(true);
			e.printStackTrace();
		}

	}
	
	
	
	
	private void validateTransactionDestinataireSuivant(
			Transaction transaction,
			TransactionDestination transactionDestination,
			Integer idDestinataireSuivant) throws Exception {
		List<TransactionAnnotation> transactionAnnotation = appMgr
				.getAnnotationByIdTransaction(transaction.getTransactionId());
		Transaction newTransaction = new Transaction();
		Etat etat = new Etat();
		Expdest expdest = new Expdest();
		Typetransaction typetransaction = new Typetransaction();
		TransactionDestinationId id = new TransactionDestinationId();
		TransactionDestination trDest = new TransactionDestination();
		etat = appMgr.listEtatByRef(4).get(0);
		transaction.setEtat(etat);
		if (transaction.getTransactionOrdre() == null) {
			transaction.setTransactionOrdre(1);
		}
		appMgr.update(transaction);
		expdest.setTypeExpDest("Interne-Person");
		expdest.setIdExpDestLdap(vb.getPerson().getId());
		appMgr.insert(expdest);
		newTransaction.setTransactionTypeIntervenant("sub_"
				+ vb.getPerson().getId());
		// newTransaction.setTransactionCommentaire(commentaireTransaction);
		newTransaction.setExpdest(expdest);
		newTransaction.setIdUtilisateur(vb.getPerson().getId());
		newTransaction.setTransactionDateTransaction(new Date());
		typetransaction = appMgr.getTypeTransactionByLibelle("Envoi").get(0);
		newTransaction.setTypetransaction(typetransaction);
		etat = appMgr.listEtatByRef(2).get(0);
		newTransaction.setEtat(etat);
		newTransaction.setTransactionSupprimer(true);
		int ordreTransaction = transaction.getTransactionOrdre();
		newTransaction.setTransactionOrdre(++ordreTransaction);
		newTransaction.setDossier(transaction.getDossier());
		newTransaction.setTransactionDestinationReelle(transaction
				.getTransactionDestinationReelle());
		newTransaction.setTransactionFirst(transaction.getTransactionId());
		appMgr.insert(newTransaction);
		Expdest expdestSuivant = new Expdest();

		expdestSuivant.setTypeExpDest("Interne-Person");
		expdestSuivant.setIdExpDestLdap(idDestinataireSuivant);
		appMgr.insert(expdestSuivant);
		String type = "sub_" + idDestinataireSuivant;
		id = new TransactionDestinationId();
		trDest = new TransactionDestination();
		id.setIdTransaction(newTransaction.getTransactionId());
		id.setIdExpDest(expdestSuivant.getIdExpDest());
		trDest.setTransactionDestTypeIntervenant(type);
		trDest.setId(id);
		trDest.setTransactionDestDateReponse(transactionDestination
				.getTransactionDestDateReponse());
		appMgr.insert(trDest);
		TransactionAnnotationId cI = new TransactionAnnotationId();
		TransactionAnnotation cA = new TransactionAnnotation();
		for (TransactionAnnotation tr : transactionAnnotation) {
			cI.setIdAnnotation(tr.getId().getIdAnnotation());
			cI.setIdTransaction(newTransaction.getTransactionId());
			cA.setId(cI);
			appMgr.insert(cA);
			cA = new TransactionAnnotation();
			cI = new TransactionAnnotationId();
		}
	}

	private void validateTransactionDestinataireFinale(Transaction transaction,
			TransactionDestination transactionDestination) throws Exception {
		List<TransactionAnnotation> transactionAnnotation = appMgr
				.getAnnotationByIdTransaction(transaction.getTransactionId());
		List<Transaction> transactions=appMgr.getListTransactionByIdTransaction(transaction.getTransactionId().intValue());
		if(transactions!=null && transactions.size()>0)
			transaction=transactions.get(0);
		Transaction newTransaction = new Transaction();
		Etat etat = new Etat();
		Expdest expdest = new Expdest();
		Typetransaction typetransaction = new Typetransaction();
		TransactionDestinationId id = new TransactionDestinationId();
		TransactionDestination trDest = new TransactionDestination();
		etat = appMgr.listEtatByRef(6).get(0);
		transaction.setEtat(etat);
		// transaction.setTransactionDateReponse(new Date());
		if (transaction.getTransactionOrdre() == null) {
			transaction.setTransactionOrdre(1);
		}
		vb.setTransaction(transaction);
		appMgr.update(transaction);
		expdest.setTypeExpDest("Interne-Person");
		expdest.setIdExpDestLdap(vb.getPerson().getId());
		appMgr.insert(expdest);
		// newTransaction.setTransactionTypeIntervenant("sub_"
		// + vb.getPerson().getId());
		newTransaction.setExpdest(expdest);
		newTransaction.setIdUtilisateur(vb.getPerson().getId());
		newTransaction.setTransactionDateTransaction(new Date());
		typetransaction = appMgr.getTypeTransactionByLibelle("Envoi").get(0);
		newTransaction.setTypetransaction(typetransaction);
		etat = appMgr.listEtatByRef(6).get(0);
		newTransaction.setEtat(etat);
		newTransaction.setTransactionSupprimer(true);
		int ordreTransaction = transaction.getTransactionOrdre();
		newTransaction.setTransactionOrdre(++ordreTransaction);
		newTransaction.setDossier(transaction.getDossier());
		newTransaction.setTransactionDestinationReelle(transaction
				.getTransactionDestinationReelle());
		newTransaction.setTransactionFirst(transaction.getTransactionId());
		appMgr.insert(newTransaction);
		String type = "";
		Expdest expdestFinal = new Expdest();
		if (transaction.getTransactionDestinationReelle()
				.getTransactionDestinationReelleTypeDestinataire()
				.equals("Interne-Person")) {
			Person personDestinationReel = ldapOperation
					.getPersonalisedUserById(transaction
							.getTransactionDestinationReelle()
							.getTransactionDestinationReelleIdDestinataire());
			expdestFinal.setTypeExpDest("Interne-Person");
			expdestFinal.setIdExpDestLdap(transaction
					.getTransactionDestinationReelle()
					.getTransactionDestinationReelleIdDestinataire());
			if (personDestinationReel.isResponsable()) {
				type = "sub_" + personDestinationReel.getId();
			} else if (personDestinationReel.isSecretary()) {
				type = "secretary_" + personDestinationReel.getId();
			} else {
				type = "agent_" + personDestinationReel.getId();
			}
		} else if (transaction.getTransactionDestinationReelle()
				.getTransactionDestinationReelleTypeDestinataire()
				.equals("Interne-Unité")) {
			// Unit unit =
			// ldapOperation.getUnitById(transaction.getTransactionDestinationReelle().getTransactionDestinationReelleIdDestinataire());
			expdestFinal.setTypeExpDest("Interne-Unité");
			expdestFinal.setIdExpDestLdap(transaction
					.getTransactionDestinationReelle()
					.getTransactionDestinationReelleIdDestinataire());
			type = "unit_"
					+ transaction.getTransactionDestinationReelle()
							.getTransactionDestinationReelleIdDestinataire();
		}
		appMgr.insert(expdestFinal);
		id = new TransactionDestinationId();
		trDest = new TransactionDestination();
		id.setIdTransaction(newTransaction.getTransactionId());
		id.setIdExpDest(expdestFinal.getIdExpDest());
		trDest.setTransactionDestTypeIntervenant(type);
		trDest.setId(id);
		trDest.setTransactionDestDateReponse(transactionDestination
				.getTransactionDestDateReponse());
		appMgr.insert(trDest);
		TransactionAnnotationId cI = new TransactionAnnotationId();
		TransactionAnnotation cA = new TransactionAnnotation();
		for (TransactionAnnotation tr : transactionAnnotation) {
			cI.setIdAnnotation(tr.getId().getIdAnnotation());
			cI.setIdTransaction(newTransaction.getTransactionId());
			cA.setId(cI);
			appMgr.insert(cA);
			cA = new TransactionAnnotation();
			cI = new TransactionAnnotationId();
		}
	}

	private Integer findIdDestinataireSuivant(Integer idDestinataireReel,
			Integer idConnectedPerson, Boolean destinataireReelIsPerson,
			boolean passageDGEN, Person generalDirector) {

		Person person;
		if (destinataireReelIsPerson) {
			person = ldapOperation.getPersonalisedUserById(idDestinataireReel);
		} else {
			Unit unit = ldapOperation.getUnitById(idDestinataireReel);
			person = ldapOperation.getPersonalisedUserById(unit
					.getResponsibleUnit().getId());
		}
		System.out.println(person.getAssociatedDirection().getShortNameUnit());
		Unit superiorUnit;
		if (person.isResponsable()) {
			superiorUnit = person.getAssociatedDirection().getAssociatedUnit();
		} else {
			superiorUnit = person.getAssociatedDirection();
		}
		if (passageDGEN) {
			if (superiorUnit.getResponsibleUnit().getId() != idConnectedPerson) {
				System.out.println(superiorUnit.getResponsibleUnit().getId());
				return findIdDestinataireSuivant(superiorUnit
						.getResponsibleUnit().getId(), idConnectedPerson, true,
						passageDGEN, generalDirector);
			}
		} else {
			// if (person.getId() != generalDirector.getId()) {
			if (superiorUnit.getResponsibleUnit().getId() != idConnectedPerson
					&& superiorUnit.getResponsibleUnit().getId() != generalDirector
							.getId()) {
				System.out.println(superiorUnit.getResponsibleUnit().getId());
				return findIdDestinataireSuivant(superiorUnit
						.getResponsibleUnit().getId(), idConnectedPerson, true,
						passageDGEN, generalDirector);
			}
			// }else{
			// }
		}
		return person.getId();
	}

	// ************Getter & Setter********************

	public VariableGlobale getVb() {
		return vb;
	}

	public void setVb(VariableGlobale vb) {
		this.vb = vb;
	}

	public Export getExport() {
		return export;
	}

	public void setExport(Export export) {
		this.export = export;
	}

	public Courrier getCourrier() {
		return courrier;
	}

	public void setCourrier(Courrier courrier) {
		this.courrier = courrier;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public TransactionDestination getTransactionDestination() {
		return transactionDestination;
	}

	public void setTransactionDestination(
			TransactionDestination transactionDestination) {
		this.transactionDestination = transactionDestination;
	}

	public TransactionAnnotation getTransactionAnnotation() {
		return transactionAnnotation;
	}

	public void setTransactionAnnotation(
			TransactionAnnotation transactionAnnotation) {
		this.transactionAnnotation = transactionAnnotation;
	}

	public Nature getNature() {
		return nature;
	}

	public void setNature(Nature nature) {
		this.nature = nature;
	}

	public Transmission getTransmission() {
		return transmission;
	}

	public void setTransmission(Transmission transmission) {
		this.transmission = transmission;
	}

	public Confidentialite getConfidentialite() {
		return confidentialite;
	}

	public void setConfidentialite(Confidentialite confidentialite) {
		this.confidentialite = confidentialite;
	}

	public Expdestexterne getExpdestexterne() {
		return expdestexterne;
	}

	public void setExpdestexterne(Expdestexterne expdestexterne) {
		this.expdestexterne = expdestexterne;
	}

	public List<Expdestexterne> getListDestExpdestexternes() {
		return listDestExpdestexternes;
	}

	public void setListDestExpdestexternes(
			List<Expdestexterne> listDestExpdestexternes) {
		this.listDestExpdestexternes = listDestExpdestexternes;
	}

	public LanguageManagerBean getLm() {
		return lm;
	}

	public void setLm(LanguageManagerBean lm) {
		this.lm = lm;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Urgence getUrgence() {
		return urgence;
	}

	public void setUrgence(Urgence urgence) {
		this.urgence = urgence;
	}

	public Date getDateReception() {
		return dateReception;
	}

	public void setDateReception(Date dateReception) {
		this.dateReception = dateReception;
	}

	public Date getDateReponse() {
		return dateReponse;
	}

	public void setDateReponse(Date dateReponse) {
		this.dateReponse = dateReponse;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setDossier(Dossier dossier) {
		this.dossier = dossier;
	}

	public Dossier getDossier() {
		return dossier;
	}

	public void setCourrierConsulterInformations(
			List<CourrierConsulterInformations> courrierConsulterInformations) {
		this.courrierConsulterInformations = courrierConsulterInformations;
	}

	public List<CourrierConsulterInformations> getCourrierConsulterInformations() {
		return courrierConsulterInformations;
	}

	public boolean isStatus1() {
		return status1;
	}

	public void setStatus1(boolean status1) {
		this.status1 = status1;
	}

	public boolean isStatus2() {
		return status2;
	}

	public void setStatus2(boolean status2) {
		this.status2 = status2;
	}

	public boolean isStatus3() {
		return status3;
	}

	public void setStatus3(boolean status3) {
		this.status3 = status3;
	}

	public void setSelectedItemsAnnotation(List<String> selectedItemsAnnotation) {
		this.selectedItemsAnnotation = selectedItemsAnnotation;
	}

	public List<String> getSelectedItemsAnnotation() {
		return selectedItemsAnnotation;
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	public Date getDate1() {
		return date1;
	}

	public void setReponse1(String reponse1) {
		this.reponse1 = reponse1;
	}

	public String getReponse1() {
		return reponse1;
	}

	public void setSelect1(boolean select1) {
		this.select1 = select1;
	}

	public boolean isSelect1() {
		return select1;
	}

	public TransactionAnnotation getcA() {
		return cA;
	}

	public void setcA(TransactionAnnotation cA) {
		this.cA = cA;
	}

	public TransactionAnnotationId getcI() {
		return cI;
	}

	public void setcI(TransactionAnnotationId cI) {
		this.cI = cI;
	}

	public List<Annotation> getListAnnotations() {
		return listAnnotations;
	}

	public void setListAnnotations(List<Annotation> listAnnotations) {
		this.listAnnotations = listAnnotations;
	}

	public void setAnnotationResult(String annotationResult) {
		this.annotationResult = annotationResult;
	}

	public String getAnnotationResult() {
		return annotationResult;
	}

	public void setEtatTransaction(String etatTransaction) {
		this.etatTransaction = etatTransaction;
	}

	public String getEtatTransaction() {
		return etatTransaction;
	}

	public void setStatusClassement(boolean statusClassement) {
		this.statusClassement = statusClassement;
	}

	public boolean isStatusClassement() {
		return statusClassement;
	}

	public void setStatusNonClasse(boolean statusNonClasse) {
		this.statusNonClasse = statusNonClasse;
	}

	public boolean isStatusNonClasse() {
		return statusNonClasse;
	}

	public void setMessageInfoCourrierClassement(
			String messageInfoCourrierClassement) {
		this.messageInfoCourrierClassement = messageInfoCourrierClassement;
	}

	public String getMessageInfoCourrierClassement() {
		return messageInfoCourrierClassement;
	}

	public void setStatusClasseSucces(boolean statusClasseSucces) {
		this.statusClasseSucces = statusClasseSucces;
	}

	public boolean isStatusClasseSucces() {
		return statusClasseSucces;
	}

	public void setSelectedItemArmoire(String selectedItemArmoire) {
		this.selectedItemArmoire = selectedItemArmoire;
	}

	public String getSelectedItemArmoire() {
		System.out.println("getSelectedItemArmoire" + selectedItemArmoire);
		return selectedItemArmoire;
	}

	public void setSelectedItemEtages(String selectedItemEtages) {
		this.selectedItemEtages = selectedItemEtages;
	}

	public String getSelectedItemEtages() {
		System.out.println("getSelectedItemEtages" + selectedItemEtages);
		return selectedItemEtages;
	}

	public void setListArmoire(List<Classement_archivage_niveau_02> listArmoire) {
		this.listArmoire = listArmoire;
	}

	public List<Classement_archivage_niveau_02> getListArmoire() {
		return listArmoire;
	}

	public void setListEtages(List<Classement_archivage_niveau_01> listEtages) {
		this.listEtages = listEtages;
	}

	public List<Classement_archivage_niveau_01> getListEtages() {
		return listEtages;
	}

	public void setStatusClasseErreur(boolean statusClasseErreur) {
		this.statusClasseErreur = statusClasseErreur;
	}

	public boolean isStatusClasseErreur() {
		return statusClasseErreur;
	}

	public void setEtatDescription(boolean etatDescription) {
		this.etatDescription = etatDescription;
	}

	public boolean isEtatDescription() {
		return etatDescription;
	}

	public void setEtatEnvoyerAuxAutre(boolean etatEnvoyerAuxAutre) {
		this.etatEnvoyerAuxAutre = etatEnvoyerAuxAutre;
	}

	public boolean isEtatEnvoyerAuxAutre() {
		return etatEnvoyerAuxAutre;
	}

	public void setEtatDateReponse(boolean etatDateReponse) {
		this.etatDateReponse = etatDateReponse;
	}

	public boolean isEtatDateReponse() {
		return etatDateReponse;
	}

	public void setEtatDescriptionTransaction(boolean etatDescriptionTransaction) {
		this.etatDescriptionTransaction = etatDescriptionTransaction;
	}

	public boolean isEtatDescriptionTransaction() {
		return etatDescriptionTransaction;
	}

	public void setEtatkeywords(boolean etatkeywords) {
		this.etatkeywords = etatkeywords;
	}

	public boolean isEtatkeywords() {
		return etatkeywords;
	}

	public void setLienOutput(String lienOutput) {
		this.lienOutput = lienOutput;
	}

	public String getLienOutput() {
		return lienOutput;
	}

	public void setAccuseReception(boolean accuseReception) {
		this.accuseReception = accuseReception;
	}

	public boolean isAccuseReception() {
		return accuseReception;
	}

	public void setShowMonitoringButton(boolean showMonitoringButton) {
		this.showMonitoringButton = showMonitoringButton;
	}

	public boolean isShowMonitoringButton() {
		return showMonitoringButton;
	}

	public LdapOperation getLdapOperation() {
		return ldapOperation;
	}

	public void setLdapOperation(LdapOperation ldapOperation) {
		this.ldapOperation = ldapOperation;
	}

	public void setNotLinkedMail(boolean notLinkedMail) {
		this.notLinkedMail = notLinkedMail;
	}

	public boolean isNotLinkedMail() {
		return notLinkedMail;
	}

	public void setLinkedMail(boolean linkedMail) {
		this.linkedMail = linkedMail;
	}

	public boolean isLinkedMail() {
		return linkedMail;
	}

	public void setNbrCourrierLies(long nbrCourrierLies) {
		this.nbrCourrierLies = nbrCourrierLies;
	}

	public long getNbrCourrierLies() {
		return nbrCourrierLies;
	}

	public void setAffichageDocument(boolean affichageDocument) {
		this.affichageDocument = affichageDocument;
	}

	public boolean isAffichageDocument() {
		return affichageDocument;
	}

	public void setAffichageDetailsAccuse(boolean affichageDetailsAccuse) {
		this.affichageDetailsAccuse = affichageDetailsAccuse;
	}

	public boolean isAffichageDetailsAccuse() {
		return affichageDetailsAccuse;
	}

	public void setStatusAccuseReception(boolean statusAccuseReception) {
		this.statusAccuseReception = statusAccuseReception;
	}

	public boolean isStatusAccuseReception() {
		return statusAccuseReception;
	}

	public void setShowResponseButton(boolean showResponseButton) {
		this.showResponseButton = showResponseButton;
	}

	public boolean isShowResponseButton() {
		return showResponseButton;
	}

	public void setHideResponseButton(boolean hideResponseButton) {
		this.hideResponseButton = hideResponseButton;
	}

	public boolean isHideResponseButton() {
		return hideResponseButton;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public Classement_archivage_niveau_01 getEtages() {
		return etages;
	}

	public void setEtages(Classement_archivage_niveau_01 etages) {
		this.etages = etages;
	}

	public Classement_archivage_niveau_02 getArmoire() {
		return armoire;
	}

	public void setArmoire(Classement_archivage_niveau_02 armoire) {
		this.armoire = armoire;
	}

	public void setShowUpdateClassement(boolean showUpdateClassement) {
		this.showUpdateClassement = showUpdateClassement;
	}

	public boolean isShowUpdateClassement() {
		return showUpdateClassement;
	}

	public void setAncienEtages(Classement_archivage_niveau_01 ancienEtages) {
		this.ancienEtages = ancienEtages;
	}

	public Classement_archivage_niveau_01 getAncienEtages() {
		return ancienEtages;
	}

	public void setShowForValidate(boolean showForValidate) {
		this.showForValidate = showForValidate;
	}

	public boolean isShowForValidate() {
		return showForValidate;
	}

	public CourrierInformations getCi() {
		return ci;
	}

	public void setCi(CourrierInformations ci) {
		this.ci = ci;
	}

	public boolean isStatusCloturer() {
		return statusCloturer;
	}

	public void setStatusCloturer(boolean statusCloturer) {
		this.statusCloturer = statusCloturer;
	}

	public boolean isStatusActive() {
		return statusActive;
	}

	public void setStatusActive(boolean statusActive) {
		this.statusActive = statusActive;
	}

	public String getCodeUniqueCourrier() {
		return codeUniqueCourrier;
	}

	public void setCodeUniqueCourrier(String codeUniqueCourrier) {
		this.codeUniqueCourrier = codeUniqueCourrier;
	}

	public String getMessageModif() {
		return messageModif;
	}

	public void setMessageModif(String messageModif) {
		this.messageModif = messageModif;
	}

	public boolean isStatusCanModif() {
		return statusCanModif;
	}

	public void setStatusCanModif(boolean statusCanModif) {
		this.statusCanModif = statusCanModif;
	}

	public void setBoutonBordereau(String boutonBordereau) {
		this.boutonBordereau = boutonBordereau;
	}

	public String getBoutonBordereau() {
		return boutonBordereau;
	}

	public void setStatV2(boolean statV2) {
		this.statV2 = statV2;
	}

	public boolean isStatV2() {
		return statV2;
	}

	public void setStatV1(boolean statV1) {
		this.statV1 = statV1;
	}

	public boolean isStatV1() {
		return statV1;
	}

	public int getIdBoc() {
		return idBoc;
	}

	public void setIdBoc(int idBoc) {
		this.idBoc = idBoc;
	}

	public DataModel getListeDestinatairesDM() {
		return listeDestinatairesDM;
	}

	public void setListeDestinatairesDM(DataModel listeDestinatairesDM) {
		this.listeDestinatairesDM = listeDestinatairesDM;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getEtatReceptionPhysique() {
		System.out.println("etatReceptionPhysique dans get "+etatReceptionPhysique);
		return etatReceptionPhysique;
	}

	public void setEtatReceptionPhysique(Boolean etatReceptionPhysique) {
		System.out.println("etatReceptionPhysique dans set "+etatReceptionPhysique);

		this.etatReceptionPhysique = etatReceptionPhysique;
	}

	public List<ListeDestinatairesModel> getDestinataireRepondre() {
		return destinataireRepondre;
	}

	public void setDestinataireRepondre(
			List<ListeDestinatairesModel> destinataireRepondre) {
		this.destinataireRepondre = destinataireRepondre;
	}

	public List<DonneeSupplementaireNature> getListDSN() {
		return listDSN;
	}

	public void setListDSN(List<DonneeSupplementaireNature> listDSN) {
		this.listDSN = listDSN;
	}

	public ComposantDynamique getComposantDynamique() {
		return composantDynamique;
	}

	public void setComposantDynamique(ComposantDynamique composantDynamique) {
		this.composantDynamique = composantDynamique;
	}

	public List<ComposantDynamique> getListCD() {
		return listCD;
	}

	public void setListCD(List<ComposantDynamique> listCD) {
		this.listCD = listCD;
	}

	public CourrierDonneeSupplementaire getCourrierDS() {
		return courrierDS;
	}

	public void setCourrierDS(CourrierDonneeSupplementaire courrierDS) {
		this.courrierDS = courrierDS;
	}

	public CourrierDonneeSupplementaire getCds() {
		return cds;
	}

	public void setCds(CourrierDonneeSupplementaire cds) {
		this.cds = cds;
	}

	public void setCategorieNature(NatureCategorie categorieNature) {
		this.categorieNature = categorieNature;
	}

	public NatureCategorie getCategorieNature() {
		return categorieNature;
	}

	public void setShowDonneSupp(boolean showDonneSupp) {
		this.showDonneSupp = showDonneSupp;
	}

	public boolean isShowDonneSupp() {
		return showDonneSupp;
	}

	public void setAffichageReceptionPhysique(boolean affichageReceptionPhysique) {
		this.affichageReceptionPhysique = affichageReceptionPhysique;
	}

	public boolean isAffichageReceptionPhysique() {
		return affichageReceptionPhysique;
	}

	public void setAffichePanelReceptionPhysique(
			Boolean affichePanelReceptionPhysique) {
		this.affichePanelReceptionPhysique = affichePanelReceptionPhysique;
	}

	public Boolean getAffichePanelReceptionPhysique() {
		return affichePanelReceptionPhysique;
	}
	
	public void setShowModificationButton(boolean showModificationButton) {
		this.showModificationButton = showModificationButton;
	}

	public boolean isShowModificationButton() {
		return showModificationButton;
	}
	public void setReceptionphysiqueNonLivre(boolean receptionphysiqueNonLivre) {
		this.receptionphysiqueNonLivre = receptionphysiqueNonLivre;
	}

	public boolean isReceptionphysiqueNonLivre() {
		return receptionphysiqueNonLivre;
	}

	public boolean isShowPanelAOC() {
		return showPanelAOC;
	}

	public void setShowPanelAOC(boolean showPanelAOC) {
		this.showPanelAOC = showPanelAOC;
	}

	public Integer getNumeroAoConsultation() {
		return numeroAoConsultation;
	}

	public void setNumeroAoConsultation(Integer numeroAoConsultation) {
		this.numeroAoConsultation = numeroAoConsultation;
	}

	public AoConsultation getAoConsultation() {
		return aoConsultation;
	}

	public void setAoConsultation(AoConsultation aoConsultation) {
		this.aoConsultation = aoConsultation;
	}

	public String getHeure1() {
		return heure1;
	}

	public void setHeure1(String heure1) {
		this.heure1 = heure1;
	}

	public String getHeure3() {
		return heure3;
	}

	public void setHeure3(String heure3) {
		this.heure3 = heure3;
	}

	public String getHeure2() {
		return heure2;
	}

	public void setHeure2(String heure2) {
		this.heure2 = heure2;
	}

	public void setStatus4(boolean status4) {
		this.status4 = status4;
	}

	public boolean isStatus4() {
		return status4;
	}

	public void setShowAjoutDocumentButton(boolean showAjoutDocumentButton) {
		this.showAjoutDocumentButton = showAjoutDocumentButton;
	}

	public boolean isShowAjoutDocumentButton() {
		return showAjoutDocumentButton;
	}

	public boolean isShowRapp() {
		return showRapp;
	}

	public void setShowRapp(boolean showRapp) {
		this.showRapp = showRapp;
	}

	public void setMessageDoc(String messageDoc) {
		this.messageDoc = messageDoc;
	}

	public String getMessageDoc() {
		return messageDoc;
	}
	public void setListCourriersLiees(List<CourrierInformations> listCourriersLiees) {
		this.listCourriersLiees = listCourriersLiees;
	}

	public List<CourrierInformations> getListCourriersLiees() {
		return listCourriersLiees;
	}

	public void setCourrierConsultationRecentBean(
			CourrierConsultationJourBean courrierConsultationRecentBean) {
		this.courrierConsultationRecentBean = courrierConsultationRecentBean;
	}

	public CourrierConsultationJourBean getCourrierConsultationRecentBean() {
		return courrierConsultationRecentBean;
	}

	public int getTypePocesseur() {
		return typePocesseur;
	}

	public void setTypePocesseur(int typePocesseur) {
		this.typePocesseur = typePocesseur;
	}

	public int getPocesseurId() {
		return pocesseurId;
	}

	public void setPocesseurId(int pocesseurId) {
		this.pocesseurId = pocesseurId;
	}

	public boolean isExisteNiveau02() {
	//	System.out.println("DANS get existeNiveau02  "+existeNiveau02);
		return existeNiveau02;
	}

	public void setExisteNiveau02(boolean existeNiveau02) {
		this.existeNiveau02 = existeNiveau02;
	}
	@SuppressWarnings("unchecked")
	public long getRecords2() {
		if (listCourrierAffecteDM != null
				&& listCourrierAffecteDM.getWrappedData() != null)
			records2 = ((List<CourrierDossierListe>) listCourrierAffecteDM
					.getWrappedData()).size();
			
		else
			records2 = 0;
		return records2;
	}
	
	public void setRecords2(long records2) {
		this.records2 = records2;
	}

	public DataModel getListCourrierAffecteDM() {
		return listCourrierAffecteDM;
	}

	public void setListCourrierAffecteDM(DataModel listCourrierAffecteDM) {
		this.listCourrierAffecteDM = listCourrierAffecteDM;
	}

	public void getSelectedRow(){
		System.out.println("2020-06-08 getSelectedRow");
		vb.setRedirect("detailToLien");
		vb.setCourDossConsulterInformations(new CourrierInformations());
		CourrierInformations courrierInformations =new CourrierInformations();	
		courrierInformations = (CourrierInformations) listCourrierAffecteDM
				.getRowData();	
		
		vb.setDestinataireReel("");
		vb.setPremiereEntreeTransfert(1);
		Transaction transaction = new Transaction();
		CourrierInformations consulterInformations = courrierInformations;

		vb.setDestinataireReel(consulterInformations
				.getCourrierDestinataireReelle());
		vb.setCopyExpReelNom(consulterInformations.getCourrierExpediteur());
		vb.setCodeUniqueCourrier(consulterInformations
				.getCourrierDestinataireReelleDirection());

		if (consulterInformations.getCourrier() == null) {
			consulterInformations.setCourrier(appMgr
					.getCourrierByIdCourrier(
							consulterInformations.getCourrierID()).get(0));
		}
		if (consulterInformations.getTransaction() == null) {
			consulterInformations.setTransaction(appMgr
					.getListTransactionByIdTransaction(
							consulterInformations.getTransactionID())
					.get(0));
		}

		if (consulterInformations.getListSelectedObject() != null) {
			vb.setCopyListSelectedObject(consulterInformations
					.getListSelectedObject());
			vb.setCopyListSelectedObjectExp(consulterInformations
					.getListSelectedObject());
			System.out.println("CopyListSelectedObjectExp size = "
					+ vb.getCopyListSelectedObjectExp().size());
		}
		if (consulterInformations.getListSelectedPerson() != null) {
			vb.setCopyListSelectedPerson(consulterInformations
					.getListSelectedPerson());
			System.out.println("CopyListSelectedPerson size = "
					+ vb.getCopyListSelectedPerson().size());
		}
		if (consulterInformations.getListSelectetdUnit() != null) {
			vb.setCopyListSelectedUnit(consulterInformations
					.getListSelectetdUnit());
			
		}
		if (consulterInformations.getListSelectetdBoc() != null) {
			vb.setCopyListSelectedBoc(consulterInformations
					.getListSelectetdBoc());
			
		}

		vb.setCourDossConsulterInformations(consulterInformations);
		// a commenté si on a renversé l'ancienne liste de courriers
		consulterInformations.setCourrier(appMgr.getCourrierByIdCourrier(
				consulterInformations.getCourrierID()).get(0));
		courrier = consulterInformations.getCourrier();

		vb.setCourrier(courrier);
		transaction = appMgr.getListTransactionByIdTransaction(
				consulterInformations.getTransactionID()).get(0);
		List<TransactionDestination> listTransactionDestination = appMgr
				.getListTransactionDestinationByIdTransaction(consulterInformations
						.getTransactionID());// valeur ancien
												// #firstTransaction.getTransactionId()#

		if (!listTransactionDestination.isEmpty()) {
			vb.setTransactionDestination(listTransactionDestination
					.get(listTransactionDestination.size() - 1));// 2015-02-27
			consulterInformations
					.setTransactionDestination(listTransactionDestination
							.get(listTransactionDestination.size() - 1));

		}

		if (vb.getPerson().isBoc()) {
			
		vb.setCopyDestNom(consulterInformations
				.getCourrierDestinataireReelle());

		vb.setCopyExpNom(consulterInformations.getCourrierExpediteur());
		vb.setCopyCourrierCommentaire(consulterInformations
				.getCourrierCommentaire());
		vb.setCopyOtherDest(consulterInformations
				.getCourrierAutreDestinataires());
		vb.setTransaction(transaction);
		// ** expediteur reel
		vb.setCopyExpReelNom(consulterInformations.getCourrierExpediteur());
		// ** destinataire reel
		vb.setDestinataireReel(consulterInformations
				.getCourrierDestinataireReelle());

		vb.setReferenceDestinataireReel(consulterInformations
				.getReferenceDestinataireReelle());
vb.setListeDestinataire(consulterInformations
		.getListeDestinatairesAvecAnnotations());
List<ListeDestinatairesModel> list = vb.getListeDestinataire();

		// ** expediteur reel

		// Vider les listes des detinatires avant de transferer le courrier
		// pour ne pas garder le destinataire de l'ancien courrier
		vb.setCopyListSelectedUnit(new ArrayList<Unit>());
		vb.setCopyListSelectedPerson(new ArrayList<Person>());
		vb.setCopyListPP(new ArrayList<Pp>());
		vb.setCopyListPM(new ArrayList<Pm>());
		
		List<TransactionAnnotation> annotations = new ArrayList<TransactionAnnotation>();
		annotations = appMgr
				.getAnnotationByIdTransaction(consulterInformations
						.getTransactionID());

		int lastIndex;
		int refAnnotation;
		String result = "";
		for (TransactionAnnotation ta : annotations) {

			refAnnotation = ta.getId().getIdAnnotation();
			result += appMgr.getAnnotationByIdAnotation(refAnnotation)
					.get(0).getAnnotationLibelle()
					+ " / ";

		}
		if (!result.equals("")) {
			lastIndex = result.lastIndexOf("/");
			result = result.substring(0, lastIndex);
		}
		vb.setCopyAnnotationResult(result);
		}
		/*
		 * LogClass logClass = new LogClass(); logClass.addTrack(
		 * 
		 * 
		 * "consultation", "Evénement de log de consultation du courrier " +
		 * courrier.getIdCourrier() + "-" +
		 * courrier.getCourrierReferenceCorrespondant(), vb.getPerson(),
		 * "INFO", appMgr);
		 */
		vb.setLinkedCourrier(courrierInformations.getCourrier());
		vb.setCourDossConsulterInformations(courrierInformations);
//		vb.setRedirect("redirectFromCDLToCDS");
//		return "redirectFromCDLToCDS";
		
		System.out.println("resultat 2020-06-08==============> "+vb.getRedirect());
	}

	public boolean isShowLienButton() {
		return showLienButton;
	}

	public void setShowLienButton(boolean showLienButton) {
		this.showLienButton = showLienButton;
	}

	public boolean isAfficherListeLies() {
		if(listCourriersInformationsAffecte!=null&& listCourriersInformationsAffecte.size()>0)
			afficherListeLies=true;
		else 
			afficherListeLies=false;
		return afficherListeLies;
	}

	public void setAfficherListeLies(boolean afficherListeLies) {
		this.afficherListeLies = afficherListeLies;
	}

	public String getBoutonNoteTransmission() {
		return boutonNoteTransmission;
	}

	public void setBoutonNoteTransmission(String boutonNoteTransmission) {
		this.boutonNoteTransmission = boutonNoteTransmission;
	}

	public boolean isAffichageBoutonLien() {
		return affichageBoutonLien;
	}

	public void setAffichageBoutonLien(boolean affichageBoutonLien) {
		this.affichageBoutonLien = affichageBoutonLien;
	}

	public boolean isShowPanelCheque() {
		return showPanelCheque;
	}

	public void setShowPanelCheque(boolean showPanelCheque) {
		this.showPanelCheque = showPanelCheque;
	}
	
	
}