package xtensus.beans.common.GBO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import xtensus.beans.common.LanguageManagerBean;
import xtensus.beans.common.ListeDestinatairesModel;
import xtensus.beans.common.VariableGlobale;
import xtensus.beans.utils.ComposantDynamique;
import xtensus.beans.utils.CourrierConsulterInformations;
import xtensus.beans.utils.CourrierDossierListe;
import xtensus.beans.utils.CourrierInformations;
import xtensus.beans.utils.ListeDetailsDynamique;
import xtensus.entity.Annotation;
import xtensus.entity.AoConsultation;
import xtensus.entity.Classement_archivage_niveau_02;
import xtensus.entity.Cheque;
import xtensus.entity.Confidentialite;
import xtensus.entity.Courrier;
import xtensus.entity.CourrierDonneeSupplementaire;
import xtensus.entity.CourrierDossier;
import xtensus.entity.CourrierLiens;
import xtensus.entity.DonneeSupplementaireNature;
import xtensus.entity.Dossier;
import xtensus.entity.Classement_archivage_niveau_01;
import xtensus.entity.Expdest;
import xtensus.entity.Expdestexterne;
import xtensus.entity.Lienscourriers;
import xtensus.entity.Nature;
import xtensus.entity.NatureCategorie;
import xtensus.entity.Transaction;
import xtensus.entity.TransactionAnnotation;
import xtensus.entity.TransactionAnnotationId;
import xtensus.entity.TransactionDestination;
import xtensus.entity.TransactionDestinationReelle;
import xtensus.entity.Transmission;
import xtensus.entity.Unite;
import xtensus.entity.Urgence;
import xtensus.entity.Utilisateur;
import xtensus.entity.Variables;
import xtensus.ldap.business.LdapFunction;
import xtensus.ldap.business.LdapOperation;
import xtensus.ldap.model.Person;
import xtensus.services.ApplicationManager;
import xtensus.services.Export;

@Component
@Scope("request")
public class courrierDetailsIncludeBean {

	private ApplicationManager appMgr;
	private Export export;
	private Courrier courrier;
	private Dossier dossier;
	private TransactionAnnotation cA;
	private TransactionAnnotationId cI;
	private boolean status;
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
	private Confidentialite cf;
	private List<String> listeDestinataireAnnotation;

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
	private boolean disbledConsultationLiens = false;
	private boolean affichageDocument;
	private boolean affichageDetailsAccuse;
	private boolean statusAccuseReception;
	private boolean showResponseButton;
	private boolean hideResponseButton;
	private String align;
	private Classement_archivage_niveau_01 etages;
	private Classement_archivage_niveau_02 armoire;
	private boolean showUpdateClassement;
	// private boolean EtageChanged;
	private Classement_archivage_niveau_01 ancienEtages;
	private String from;
	private boolean showForValidate;
	private CourrierInformations ci;
	private String codeUniqueCourrier = "";
	private String expditeurUnite;
	private List<Variables> var;
	private String cupSRV;
	private String messageModif;
	private boolean statusCanModif;
	private boolean affichagePassageBO;
	private String boutonBordereau;
	private DataModel listeDestinatairesDM;
	private String type;
	private int idBoc;
	private boolean statV1;
	private boolean statV2;
	// KHA
	private List<ListeDestinatairesModel> destinataireRepondre;
	// [JS]
	private List<DonneeSupplementaireNature> listDSN;
	private List<DonneeSupplementaireNature> listDSNTransmission;
	private Properties msg;
	private ComposantDynamique composantDynamique;
	private List<ComposantDynamique> D;
	private CourrierDonneeSupplementaire courrierDS;
	private CourrierDonneeSupplementaire cds;
	private List<ComposantDynamique> listCD;
	private List<ComposantDynamique> listCDTransmission;
	private boolean showPanelCheque;
	private ListDataModel listCheques;
	private boolean afficheChampsSpecTansmission;
	private boolean showPanelAOC;
	private String numeroAoConsultation;
	private AoConsultation aoConsultation;
	private String heure1;
	private String heure3;
	private String heure2;
private boolean nn;
private boolean disbledBontonConsultation; 
//[JS] : 2020-03-18
private boolean affichePanelReceptionPhysique;
//[JS] : 2020-03-28
private int recordsListeAnnotation;
private List<ListeDestinatairesModel> listeIntermediaire;
private String boutonNoteTransmission;
private String refValise;
private String courrierReferenceCorrespondantExp;

	// /
	@Autowired
	public courrierDetailsIncludeBean(
			@Qualifier("applicationManager") ApplicationManager appMgr) {
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
		listDSNTransmission = new ArrayList<DonneeSupplementaireNature>();
		listCD = new ArrayList<ComposantDynamique>();
		listCDTransmission = new ArrayList<ComposantDynamique>();
		courrierDS = new CourrierDonneeSupplementaire();
		cds = new CourrierDonneeSupplementaire();
		categorieNature = new NatureCategorie();
		listeDestinataireAnnotation = new ArrayList<String>();
		listCheques = new ListDataModel();
		// /
		//[JS] : ========= 2020-03-28
		listeIntermediaire=new ArrayList<ListeDestinatairesModel>();
	}

	@PostConstruct
	public void Initialize() {

		// vb.getListSelectedItem().clear();
		// kha 12-02-2019
		destinataireRepondre = new ArrayList<ListeDestinatairesModel>();
		// vb.setDestNom(null);
		// vb.setExpNom(null);
		statusCloturer = false;
		statusActive = false;
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
		
		
		ci = vb.getCourDossConsulterInformations();


		if (vb.getLocale().equals("ar")) {
			align = "right";
		} else {
			align = "left";
		}
		// *** AC
		from = vb.getRedirect();
		vb.setRedirect("");
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
			} catch (Exception e) {
				showResponseButton = false;
				hideResponseButton = true;
				showForValidate = false;
			}

			annotationResult = "";
			courrier = vb.getCourrier();
			System.out.println("courrier ID====> "+courrier.getIdCourrier());
			//JS:05-06-2020		
			Courrier cr=appMgr.getCourrierByIdCourrier(courrier.getIdCourrier()).get(0);
			Courrier courrierFK = cr.getIdcourrierFK();
			System.out.println("courrierFK  "+courrierFK);
			if(courrierFK !=null){
			refValise="["+courrierFK.getCourrierReferenceCorrespondant()+"]";
			}else{
				refValise="";
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
					boutonBordereau = "false";
				} else {
					boutonBordereau = "true";
				}
			}
			if (!vb.isRoleNoteTransmission()) {
				boutonNoteTransmission = "false";
			} else {
				if (courrier.getCourrierType() != null
						&& courrier.getCourrierType().equals("D")) {
					boutonNoteTransmission = "true";
				} else {
					boutonNoteTransmission = "false";
				}
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
			// //[KBS] :Sowh Panel Chèque 2019-11-18 KBS
			Integer natureID = nature.getNatureId();
			
			if (natureID == 44 || natureID == 46) {
				if (courrier.getAoConsultationId() != null) {
					showPanelAOC = true;
					int idaoconsultation = courrier.getAoConsultationId()
							.getAoConsultationId();
					aoConsultation = appMgr.getListAoConsultation(
							idaoconsultation).get(0);
					numeroAoConsultation = aoConsultation
							.getAoConsultationNumero();
					heure1 = aoConsultation.getAoConsultationDateLimiteOffre()
							.toString().substring(0, 10)
							+ " à "
							+ aoConsultation.getAoConsultationDateLimiteOffre()
									.toString().substring(11, 16);
					heure3 = aoConsultation
							.getAoConsultationDateSeanceCommission().toString()
							.substring(0, 10)
							+ " à "
							+ aoConsultation
									.getAoConsultationDateSeanceCommission()
									.toString().substring(11, 16);
					if (aoConsultation.getAoConsultationDelaisProlongation() != null) {
						heure2 = aoConsultation
								.getAoConsultationDelaisProlongation()
								.toString().substring(0, 10)
								+ " à "
								+ aoConsultation
										.getAoConsultationDelaisProlongation()
										.toString().substring(11, 16);
					}
				}
			}
			// [JS]
			// Load fichier Properties
			ExternalContext jsfContext = FacesContext.getCurrentInstance()
					.getExternalContext();
			ServletContext servletContext = (ServletContext) jsfContext
					.getContext();
			String webContentRoot = servletContext.getRealPath("/");
			String pathConfigFile = webContentRoot + File.separator + "WEB-INF"
					+ File.separator + "classes" + File.separator + "messages_"
					+ vb.getLocalFr() + ".properties";
			msg = new Properties();
			try {
				msg.load(new FileInputStream(pathConfigFile));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			listDSN = appMgr.getListDonneeSupplementaireNatureAffectes(nature
					.getNatureId());
			listCD = new ArrayList<ComposantDynamique>();
			Class aClass = cds.getClass();
			courrierDS = appMgr.getDonneeSupplementaireCourrier(courrier
					.getIdCourrier());
			List<ListeDetailsDynamique> listeDetails = new ArrayList<ListeDetailsDynamique>();
			if (courrierDS != null) {
				if (listDSN != null && listDSN.size() > 0) {
					showDonneSupp = true;
					for (int i = 0; i < listDSN.size(); i++) {
						ListeDetailsDynamique dd = new ListeDetailsDynamique();
						composantDynamique = new ComposantDynamique();
						String libelle = listDSN.get(i).getLibelleDonnee();
						String libelleNature = msg.getProperty(libelle);
						dd.setLibelleChamp(libelleNature);
						composantDynamique.setName(libelleNature);
						composantDynamique.setType(listDSN.get(i)
								.getDonneeSupplementaire()
								.getTypeDonneeSupplementaire());
						composantDynamique.setIdChamps(listDSN.get(i)
								.getDonneeSupplementaire()
								.getIdDonneeSupplementaire());

						listCD.add(composantDynamique);
						int idchamp = listCD.get(i).getIdChamps();
						String typeChamp = listCD.get(i).getType();

						String methodName = "getColonne" + idchamp;
						Method m = null;
						m = aClass.getMethod(methodName);
						Object resultat = m.invoke(courrierDS, new Object[0]);
						dd.setContenuChamp(resultat.toString());
						if (typeChamp.equals("RADIO")) {
							if (resultat.equals("true")) {
								resultat = "Oui";
							} else
								resultat = "Non";
						}
						composantDynamique.setColonne(resultat);
						listeDetails.add(dd);
					}

				}
			}
			
			Transaction tr = vb.getTransaction();
			Expdest cupExpDest1;
			cupExpDest1 = new Expdest();
			cupExpDest1 = appMgr.getListExpDestByIdExpDest(
					tr.getExpdest().getIdExpDest()).get(0);
			// if mode de transmission Porteur ou RP ou LR
			if (courrier.getTransmission().getTransmissionId() == 1
					|| courrier.getTransmission().getTransmissionId() == 5
					|| courrier.getTransmission().getTransmissionId() == 6) {

				if (tr.getCourrierType().equals("A")) {
					afficheChampsSpecTansmission = true;
					//[JS]: 2020-05-07
					//test affichage panel information spécifique courrier A , Tr=5/6 et Expditeur est externe 
					System.out.println("cupExpDest.getTypeExpDest() "+cupExpDest1.getTypeExpDest());
					if( (cupExpDest1.getTypeExpDest().equals("Interne-Person")|| cupExpDest1.getTypeExpDest().equals("Interne-Unité"))  &&(courrier.getTransmission().getTransmissionId() == 5 || courrier.getTransmission().getTransmissionId() == 6 )){
						afficheChampsSpecTansmission = false;	
					}else{
						afficheChampsSpecTansmission = true;	
					}
					System.out.println("afficheChampsSpecTansmission "+afficheChampsSpecTansmission);
				} else{
					afficheChampsSpecTansmission = false;
				//[JS} :2020-05-07
				//test affichage panel information spécifique courrier A , Tr=5/6 et Expditeur est externe 
				TransactionDestinationReelle destinataionReel = appMgr
				.getTransactionDestinationReelById(tr
						.getTransactionDestinationReelle()
						.getTransactionDestinationReelleId());
				System.out.println("Type Dest ==> "+destinataionReel.getTransactionDestinationReelleTypeDestinataire());
				if(destinataionReel.getTransactionDestinationReelleTypeDestinataire().equals("Externe") &&(courrier.getTransmission().getTransmissionId() == 5 || courrier.getTransmission().getTransmissionId() == 6 ))
					afficheChampsSpecTansmission = true;}
			} else {

				afficheChampsSpecTansmission = true;
			}
			// }else{
			// afficheChampsSpecTansmission=false;
			// }

			listDSNTransmission = appMgr
					.getListDonneeSupplementaireTransmissionAffectes(courrier
							.getTransmission().getTransmissionId());
			System.out.println("##### listDSNTransmission == "
					+ listDSNTransmission.size());
			listCDTransmission = new ArrayList<ComposantDynamique>();
			aClass = cds.getClass();
			courrierDS = appMgr.getDonneeSupplementaireCourrier(courrier
					.getIdCourrier());
			List<ListeDetailsDynamique> listeDetailsTransmission = new ArrayList<ListeDetailsDynamique>();
			if (courrierDS != null) {
				if (listDSNTransmission != null
						&& listDSNTransmission.size() > 0) {
					showDonneSupp = true;
					for (int i = 0; i < listDSNTransmission.size(); i++) {
						composantDynamique = new ComposantDynamique();
						ListeDetailsDynamique dd = new ListeDetailsDynamique();
						String libelle = listDSNTransmission.get(i)
								.getLibelleDonnee();
						String libelleNature = msg.getProperty(libelle);
						dd.setLibelleChamp(libelleNature);
						composantDynamique.setName(libelleNature);
						composantDynamique.setType(listDSNTransmission.get(i)
								.getDonneeSupplementaire()
								.getTypeDonneeSupplementaire());
						composantDynamique.setIdChamps(listDSNTransmission
								.get(i).getDonneeSupplementaire()
								.getIdDonneeSupplementaire());
						composantDynamique.setPattern(listDSNTransmission
								.get(i).getPattern());
						composantDynamique.setMessageAlerte(listDSNTransmission
								.get(i).getMessageAlerte());
						listCDTransmission.add(composantDynamique);
						int idchamp = listCDTransmission.get(i).getIdChamps();
						String typeChamp = listCDTransmission.get(i).getType();
						String methodName = "getColonne" + idchamp;
						Method m = null;
						// if(methodName != null){
						m = aClass.getMethod(methodName);
						Object resultat = m.invoke(courrierDS, new Object[0]);
						if (resultat != null) {
							dd.setContenuChamp(resultat.toString());
							if (typeChamp.equals("RADIO")) {
								if (resultat.equals("true")) {
									resultat = "Oui";
								} else
									resultat = "Non";
							}
							composantDynamique.setColonne(resultat);
							System.out.println("#### résultat == " + resultat);
							listeDetailsTransmission.add(dd);
						}
					}

				}

			}
			// //////////////////////////////////////////////////////
			vb.setListComposantDynamiqueTransmission(listCDTransmission);
			vb.setListeDetailsDynamiques(listeDetails);
			vb.setListeDetailsDynamiquesTransmission(listeDetailsTransmission);

			// //////////////////////////////////////////////////////
			// [JS]

			vb.setNature(nature);

			confidentialite = appMgr.getConfidentialiteById(
					courrier.getConfidentialite().getConfidentialiteId())
					.get(0);
			vb.setConfidentialite(confidentialite);

			urgence = appMgr.getUrgenceById(
					courrier.getUrgence().getUrgenceId()).get(0);
			vb.setUrgence(urgence);

			List<Transmission> ListTransmission = appMgr
					.getTransmissionById(courrier.getTransmission()
							.getTransmissionId());
			if (ListTransmission != null && ListTransmission.size() > 0) {
				transmission = ListTransmission.get(0);

				vb.setTransmission(transmission);
				vb.setSelectedItemsTr(transmission.getTransmissionId()
						.toString());
			}
			listArmoire = appMgr.listArmoireByEtat("Libre");

			CourrierDossier courrierDossier = new CourrierDossier();
			courrierDossier = appMgr.getCourrierDossierByIdCourrier(
					courrier.getIdCourrier()).get(0);
			int refDossier = courrierDossier.getId().getDossierId();
			vb.setReferenceDossier(refDossier);
			transaction = vb.getTransaction();
			if (transaction == null) {
				transaction = appMgr.getListTransactionByIdTransaction(
						ci.getTransactionID()).get(0);
				vb.setTransaction(transaction);

			}
			int refTransaction = transaction.getTransactionId();
			List<TransactionAnnotation> annotations = new ArrayList<TransactionAnnotation>();
			System.out.println("refTransaction  "+refTransaction);
			annotations = appMgr.getAnnotationByIdTransaction(refTransaction);

			int refEtat = 0;
			if(transaction.getEtat()!=null)
				refEtat = transaction.getEtat().getEtatId();
				
			etatTransaction="";
			if (refEtat != 0) {
				if (vb.getLocale().equals("ar")) {
					etatTransaction = appMgr.listEtatByRef(refEtat).get(0)
							.getEtatLibelleAr();
				} else {
					etatTransaction = appMgr.listEtatByRef(refEtat).get(0)
							.getEtatLibelle();
				}
			}
			
			if (vb.getPerson().isBoc()) {
				accuseReception = true;
				affichagePassageBO = false;
			} else {
				affichagePassageBO = true;
			}
			if (appMgr.accuseReceptionByIdCourrier(courrier.getIdCourrier())
					.size() != 0) {
				setAffichageDetailsAccuse(true);
			}
		

			int nbDoc = appMgr
					.getDocumentByIdCourrier(courrier.getIdCourrier()).size();
			if (nbDoc != 0) {
				setAffichageDocument(true);
			}

			String annotationLibelle;
			int lastIndex;
			int refAnnotation;

			// AH : affecter à chaque Destinataire ses Annotations
			Transaction firstTransaction = vb.getAllTransactions().get(vb.getAllTransactions().size() - 1);
			courrierReferenceCorrespondantExp=firstTransaction.getCourrierReferenceCorrespondant();
			List<ListeDestinatairesModel> listDestinataires = vb.getListeDestinataire();
			List<Integer> listeIdDest = new ArrayList<Integer>();
			
			if(listDestinataires!=null && listDestinataires.size()>0){
			for (ListeDestinatairesModel destinataire : listDestinataires) {
				 
				int idDest = 0;
				
				if ((vb.getPerson().isResponsable() || vb.getPerson().isSecretary()) && !vb.getPerson().isBoc()) {
					if (vb.getPerson().getAssociatedDirection().getIdUnit() == destinataire
							.getDestinataireId()) {
						idDest = vb.getPerson().getId();
					} else {
						idDest = destinataire.getDestinataireId();
					}
				}
				listeIdDest.add(idDest);
				
				String concatenationAnnotation = "";
				if (destinataire.getChooseAnnotationType() != null	&& destinataire.getChooseAnnotationType().equals("tous")) {
					System.out.println("DANS TYPE TOUS");
					listeDestinataireAnnotation = destinataire.getListeAnnotations();
					if (listeDestinataireAnnotation != null && listeDestinataireAnnotation.size() > 0) {

						if (listeDestinataireAnnotation.size() == 1 && Integer.valueOf(listeDestinataireAnnotation.get(0)) == 10) {
							
							concatenationAnnotation = destinataire.getOtherAnnotation();
							System.out.println("## 1 ## "+concatenationAnnotation);
						} else {
							for (int i = 0; i < listeDestinataireAnnotation.size(); i++) {
								Annotation annotation = appMgr.getAnnotationByIdAnotation(Integer.valueOf(listeDestinataireAnnotation.get(i))).get(0);
								concatenationAnnotation += annotation.getAnnotationLibelle() + " / ";
							}
							System.out.println("## 2 ## "+concatenationAnnotation);
						}
						if (concatenationAnnotation != null
								&& !concatenationAnnotation.equals("")) {
							int lastIndexBar = concatenationAnnotation
									.lastIndexOf("/");
							if (lastIndexBar > 0)
								concatenationAnnotation = concatenationAnnotation
										.substring(0, lastIndexBar);
						}
						destinataire.setAnnotations(concatenationAnnotation);

						}
					}

					else if (destinataire.getChooseAnnotationType() != null
							&& destinataire.getChooseAnnotationType().equals(
									"autre")) {
						concatenationAnnotation = destinataire
								.getOtherAnnotation();
						destinataire.setAnnotations(concatenationAnnotation);
					}

				}
			}

			listeDestinatairesDM = new ListDataModel();

			List<ListeDestinatairesModel> listeDestinataireAnno = new ArrayList<ListeDestinatairesModel>();
			if(listDestinataires!=null)
			for (ListeDestinatairesModel dest : listDestinataires) {
				
				if (dest.getAnnotations() != null
						&& dest.getAnnotations().trim().length() > 0) {

					if (!vb.getPerson().isBoc()) {

						if (dest.getDestinataireId() == vb.getPerson().getId()) {

							listeDestinataireAnno.add(dest);
						} else {

							if (vb.getPerson().isResponsable()
									|| vb.getPerson().isSecretary()) {

								if (dest.getDestinataireId() == vb.getPerson()
										.getAssociatedDirection().getIdUnit().intValue()) {

									listeDestinataireAnno.add(dest);

								}
							}
						}

					}
				}
			}
			//boc ou editeur ou exp
			if (vb.getPerson().isBoc() || transaction.getIdUtilisateur().intValue() == vb.getPerson().getId()) {
				
				if(listDestinataires!=null && listDestinataires.size()>0)
				for(ListeDestinatairesModel dest :listDestinataires){

					if(dest.getChooseAnnotationType()!= null && ((dest.getOtherAnnotation()!=null && dest.getOtherAnnotation()!="") || (dest.getAnnotations()!=null)&&(dest.getAnnotations()!=""))){
						
						listeIntermediaire.add(dest);

					}
				}
				listeDestinatairesDM.setWrappedData(listeIntermediaire);

				vb.setListAnnotations(listeIntermediaire);
				
				recordsListeAnnotation=listeIntermediaire.size();
				
			} 
			else if(!(vb.getPerson().isBoc())){
				
//2020-06-05
				System.out.println("LE CONNECTE NOT BOC");
				//Test si c'est l'expéditeur
				String intervenant=firstTransaction.getTransactionTypeIntervenant();
				if(intervenant.startsWith("unit")){
					String inter=intervenant.substring(5);
					int idUnitExp=Integer.parseInt(inter);

					if(vb.getPerson().getAssociatedDirection().getIdUnit().intValue()==idUnitExp){
//2020-06-05
						System.out.println("LE CONNECTE Expéditeur");						
						
					for(ListeDestinatairesModel dest :listDestinataires){
						
					System.out.println("###TR ANNOTATION "+dest.getTransactionId());
						if(dest.getChooseAnnotationType()!= null && ((dest.getOtherAnnotation()!=null && dest.getOtherAnnotation()!="") || (dest.getAnnotations()!=null)&&(dest.getAnnotations()!=""))){
							listeIntermediaire.add(dest);

							}
						}
					}
					else{
						System.out.println("######AH AH ");
						for(ListeDestinatairesModel anno:listeDestinataireAnno){				
						
							if(anno.getChooseAnnotationType()!= null && ((anno.getOtherAnnotation()!=null && anno.getOtherAnnotation()!="") || (anno.getAnnotations()!=null)&&(anno.getAnnotations()!=""))){
								listeIntermediaire.add(anno);
							}
					}
					listeDestinatairesDM.setWrappedData(listeIntermediaire);
					vb.setListAnnotations(listeIntermediaire);
					recordsListeAnnotation=listeIntermediaire.size();

					}
					listeDestinatairesDM.setWrappedData(listeIntermediaire);
//					listeDestinatairesDM.setWrappedData(list);
					vb.setListAnnotations(listeIntermediaire);
					
					System.out.println("size listeIntermediaire===> "+listeIntermediaire.size());
					recordsListeAnnotation=listeIntermediaire.size();
					
					
				}
				
			}
			else {
				System.out.println(">>>>>>>>>>>>>>>>>>>>C'est dans ELSE de l'annotation ");
				// vérifier si c'est l'expéditeur
				
				
				for(ListeDestinatairesModel anno:listeDestinataireAnno){
						//test si annotation not vide
						
						if(anno.getChooseAnnotationType()!= null && ((anno.getOtherAnnotation()!=null && anno.getOtherAnnotation()!="") || (anno.getAnnotations()!=null)&&(anno.getAnnotations()!=""))){
						listeIntermediaire.add(anno);
						}
				}
				listeDestinatairesDM.setWrappedData(listeIntermediaire);
				vb.setListAnnotations(listeIntermediaire);
				recordsListeAnnotation=listeIntermediaire.size();

//				listeDestinatairesDM.setWrappedData(listeDestinataireAnno);
//				vb.setListAnnotations(listeDestinataireAnno);
				//[JS] : Fin 2020-03-28===================================================
				
			}

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
								selectedItemsAnnotation.add(String
										.valueOf(refAnnotation));
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
								selectedItemsAnnotation.add(String
										.valueOf(refAnnotation));
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
			// KHA

			if (cupExpDest != null
					&& cupExpDest.getTypeExpDest().equals("Interne-Person")
					&& cupExpDest.getIdExpDestLdap() != null) {
				int MonID;
				int j = 0;
				boolean findPerson = false;

				do {
					MonID = vb.getCopyLdapListUser().get(j).getId();
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
				if (vb.getPersonCodeUnique() != null
						&& vb.getPersonCodeUnique().getAssociatedDirection() != null) {
					cupSRV = vb.getPersonCodeUnique().getAssociatedDirection()
							.getShortNameUnit();
				}
				if (vb.getPersonCodeUnique() != null
						&& vb.getPersonCodeUnique().getAssociatedBOC() != null) {
					cupSRV = vb.getPersonCodeUnique().getAssociatedBOC()
							.getNameBOC();
				}
				if (vb.getPersonCodeUnique() != null
						&& vb.getPersonCodeUnique().getAssociatedService() != null) {
					cupSRV = vb.getPersonCodeUnique().getAssociatedService()
							.getNameService();
					cupSRV = vb.getPersonCodeUnique().getAssociatedService()
							.getAssociatedDirection().getNameDirection();
				}

			} else if (cupExpDest != null
					&& cupExpDest.getTypeExpDest().equals("Interne-Unité")
					&& cupExpDest.getIdExpDestLdap() != null) {
				int MonID;
				int j = 0;
				boolean findunite = false;

				do {
					MonID = vb.getCopyLdapListUnit().get(j).getIdUnit();
					if (MonID == cupExpDest.getIdExpDestLdap().intValue()) {
						findunite = true;
						// vb.setPersonCodeUnique(vb.getCopyLdapListUser().get(j));
						// vbn.setPerson(vb.getCopyLdapListUser().get(j));
						cupSRV = vb.getCopyLdapListUnit().get(j)
								.getShortNameUnit();
						break;
					} else {
						j++;
					}
				} while (!findunite && j < vb.getCopyLdapListUnit().size());

			}

			else {
				// XTRN==>EXT
				cupSRV = "EXT";
			}
			// codeUniqueCourrier=vb.getCodeUniqueCourrier();
			// //
			// ***********************************************************************
			// //
			// --------------------------------------MM----------------------------
			// // Test
			// var=appMgr.getListVariableByLibelle();
			// var = appMgr
			// .listVariablesByLibelle("code_courrier_unique_personnalisable");
			//
			// codeUniqueCourrier = var.get(0).getVaraiablesValeur();
			// codeUniqueCourrier = codeUniqueCourrier.replace("[ID]", vb
			// .getCourrier().getCourrierReferenceCorrespondant() + "");
			//
			// codeUniqueCourrier = codeUniqueCourrier.replace("[Annee]",
			// new Date().getYear() + 1900 + "");
			// codeUniqueCourrier = codeUniqueCourrier.replace("[Mois]",
			// new Date().getMonth() + 1 + "");
			// // XTE : Si le courrier est ajouté par un non Boc, il aura le
			// type à
			// // NULL--------------------------------------------------
			// if (vb.getCourrier().getCourrierType() != null) {
			// codeUniqueCourrier = codeUniqueCourrier.replace("[Sens]", vb
			// .getCourrier().getCourrierType());
			// } else {
			// codeUniqueCourrier = codeUniqueCourrier.replace("[Sens]", "I");
			// }
			// codeUniqueCourrier = codeUniqueCourrier.replace("[SRV]", cupSRV);
			// // KHA====
			// vb.setCourrierCodeUnique(codeUniqueCourrier);
			//
			codeUniqueCourrier = vb.getCodeUniqueCourrier();
			// ====
			// [ID][Annee][Mois]//[SRV]/[Sens]/
			// ***********************************************************************
			// --------------------------------------MM----------------------------
			// Test
			List<ChequeModel> listExpositionsTab2 = new ArrayList<ChequeModel>();
			List<Cheque> listeCheques = appMgr
					.getListeChequeByCourrier(courrier.getIdCourrier());
			for (Cheque cheque : listeCheques) {
				ChequeModel fdm = new ChequeModel();
				fdm.setOperation(1);
				fdm.setBoutonPlus(true);
				fdm.setBoutonSupprimer(false);
				fdm.setChequeBanque(cheque.getChequeBanque());
				fdm.setChequeBarre(cheque.getChequeBarre());
				fdm.setChequeBeneficiaire(cheque.getChequeBeneficiaire());
				fdm.setChequeDate(cheque.getChequeDate());
				fdm.setChequeMontant(cheque.getChequeMontant());
				fdm.setChequeNum(cheque.getChequeNum());
				listExpositionsTab2.add(fdm);

			}
			// //[KBS] :Sowh Panel Chèque 2019-12-25 KBS
			if (listExpositionsTab2 != null && listExpositionsTab2.size() > 0) {
				if (natureID == 38 || natureID == 59 || natureID == 80) {
					showPanelCheque = true;
				} else {
					showPanelCheque = false;
				}
			}

			listCheques.setWrappedData(listExpositionsTab2);
			vb.setListChequeTableau(listExpositionsTab2);
				//[JS] : 2020-03-18
			Transaction transactionn = new Transaction();
			List<Transaction> listTr = appMgr.getTransactionByIdDossier(courrierDossier.getId().getDossierId());
			int lastIndexT = listTr.size();
			transactionn=listTr.get(lastIndexT-1);
			int idEditeur=transactionn.getIdUtilisateur();
			if(vb.getPerson().isBoc()){		
				
				if(transaction.getTransactionDestinationReelle().getTransactionDestinationReelleTypeDestinataire().equals("Externe") && vb.getPerson().getId()==idEditeur){
					affichePanelReceptionPhysique=false;
				}else{
					affichePanelReceptionPhysique=true;
				}
			}else{
				//[JS] 2020-06-05
				//champ récéption physique ne s'affiche lorsque le destinataire est externe et le personne connecté n'est pas l'éditeur de courrier
				int uniteID = 0;
				if(cupExpDest.getIdExpDestLdap() != null){
					System.out.println("cupExpDest.getTypeExpDest() "+cupExpDest.getTypeExpDest());
					if(cupExpDest.getTypeExpDest().equals("Interne-Person")){
						Person person=ldapOperation.getPersonalisedUserById(cupExpDest.getIdExpDestLdap().intValue());
						uniteID=person.getAssociatedDirection().getIdUnit();
					}else{
						uniteID=cupExpDest.getIdExpDestLdap();
					}
					
				}
				
		
				if(transaction.getTransactionDestinationReelle().getTransactionDestinationReelleTypeDestinataire().equals("Externe")
						&& courrier.getCourrierAvecDocumentPhysique()==false &&  cupExpDest.getIdExpDestLdap() != null
						&& vb.getPerson().getAssociatedDirection().getIdUnit().intValue() == uniteID && vb.getPerson().getId()!=idEditeur){
					affichePanelReceptionPhysique=false;
				}else{
				affichePanelReceptionPhysique=true;
				}
			}
			//========================
		} catch (Exception e) {
			e.printStackTrace();
		}

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
					if (courrier1.getTransmission().getTransmissionId() == 10) {
						disbledConsultationLiens = true;
					}
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

	// *****************Getters &Setters*******************************

	public String valide() {
		return ("OK");
	}

	public ApplicationManager getAppMgr() {
		return appMgr;
	}

	public void setAppMgr(ApplicationManager appMgr) {
		this.appMgr = appMgr;
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

	public Dossier getDossier() {
		return dossier;
	}

	public void setDossier(Dossier dossier) {
		this.dossier = dossier;
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

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
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

	public boolean isShowDonneSupp() {
		return showDonneSupp;
	}

	public void setShowDonneSupp(boolean showDonneSupp) {
		this.showDonneSupp = showDonneSupp;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate1() {
		return date1;
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
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

	public NatureCategorie getCategorieNature() {
		return categorieNature;
	}

	public void setCategorieNature(NatureCategorie categorieNature) {
		this.categorieNature = categorieNature;
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

	public Urgence getUrgence() {
		return urgence;
	}

	public void setUrgence(Urgence urgence) {
		this.urgence = urgence;
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

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public List<CourrierConsulterInformations> getCourrierConsulterInformations() {
		return courrierConsulterInformations;
	}

	public void setCourrierConsulterInformations(
			List<CourrierConsulterInformations> courrierConsulterInformations) {
		this.courrierConsulterInformations = courrierConsulterInformations;
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

	public VariableGlobale getVb() {
		return vb;
	}

	public void setVb(VariableGlobale vb) {
		this.vb = vb;
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

	public Confidentialite getCf() {
		return cf;
	}

	public void setCf(Confidentialite cf) {
		this.cf = cf;
	}

	public String getEtatTransaction() {
		return etatTransaction;
	}

	public void setEtatTransaction(String etatTransaction) {
		this.etatTransaction = etatTransaction;
	}

	public List<String> getSelectedItemsAnnotation() {
		return selectedItemsAnnotation;
	}

	public void setSelectedItemsAnnotation(List<String> selectedItemsAnnotation) {
		this.selectedItemsAnnotation = selectedItemsAnnotation;
	}

	public List<Annotation> getListAnnotations() {
		return listAnnotations;
	}

	public void setListAnnotations(List<Annotation> listAnnotations) {
		this.listAnnotations = listAnnotations;
	}

	public String getReponse1() {
		return reponse1;
	}

	public void setReponse1(String reponse1) {
		this.reponse1 = reponse1;
	}

	public String getAnnotationResult() {
		return annotationResult;
	}

	public void setAnnotationResult(String annotationResult) {
		this.annotationResult = annotationResult;
	}

	public boolean isSelect1() {
		return select1;
	}

	public void setSelect1(boolean select1) {
		this.select1 = select1;
	}

	public boolean isStatusClassement() {
		return statusClassement;
	}

	public void setStatusClassement(boolean statusClassement) {
		this.statusClassement = statusClassement;
	}

	public boolean isStatusNonClasse() {
		return statusNonClasse;
	}

	public void setStatusNonClasse(boolean statusNonClasse) {
		this.statusNonClasse = statusNonClasse;
	}

	public boolean isStatusClasseSucces() {
		return statusClasseSucces;
	}

	public void setStatusClasseSucces(boolean statusClasseSucces) {
		this.statusClasseSucces = statusClasseSucces;
	}

	public boolean isStatusClasseErreur() {
		return statusClasseErreur;
	}

	public void setStatusClasseErreur(boolean statusClasseErreur) {
		this.statusClasseErreur = statusClasseErreur;
	}

	public String getMessageInfoCourrierClassement() {
		return messageInfoCourrierClassement;
	}

	public void setMessageInfoCourrierClassement(
			String messageInfoCourrierClassement) {
		this.messageInfoCourrierClassement = messageInfoCourrierClassement;
	}

	public String getSelectedItemArmoire() {
		return selectedItemArmoire;
	}

	public void setSelectedItemArmoire(String selectedItemArmoire) {
		this.selectedItemArmoire = selectedItemArmoire;
	}

	public String getSelectedItemEtages() {
		return selectedItemEtages;
	}

	public void setSelectedItemEtages(String selectedItemEtages) {
		this.selectedItemEtages = selectedItemEtages;
	}

	public List<Classement_archivage_niveau_02> getListArmoire() {
		return listArmoire;
	}

	public void setListArmoire(List<Classement_archivage_niveau_02> listArmoire) {
		this.listArmoire = listArmoire;
	}

	public List<Classement_archivage_niveau_01> getListEtages() {
		return listEtages;
	}

	public void setListEtages(List<Classement_archivage_niveau_01> listEtages) {
		this.listEtages = listEtages;
	}

	public boolean isEtatDescription() {
		return etatDescription;
	}

	public void setEtatDescription(boolean etatDescription) {
		this.etatDescription = etatDescription;
	}

	public boolean isEtatEnvoyerAuxAutre() {
		return etatEnvoyerAuxAutre;
	}

	public void setEtatEnvoyerAuxAutre(boolean etatEnvoyerAuxAutre) {
		this.etatEnvoyerAuxAutre = etatEnvoyerAuxAutre;
	}

	public boolean isEtatDateReponse() {
		return etatDateReponse;
	}

	public void setEtatDateReponse(boolean etatDateReponse) {
		this.etatDateReponse = etatDateReponse;
	}

	public boolean isEtatDescriptionTransaction() {
		return etatDescriptionTransaction;
	}

	public void setEtatDescriptionTransaction(boolean etatDescriptionTransaction) {
		this.etatDescriptionTransaction = etatDescriptionTransaction;
	}

	public boolean isEtatkeywords() {
		return etatkeywords;
	}

	public void setEtatkeywords(boolean etatkeywords) {
		this.etatkeywords = etatkeywords;
	}

	public boolean isAccuseReception() {
		return accuseReception;
	}

	public void setAccuseReception(boolean accuseReception) {
		this.accuseReception = accuseReception;
	}

	public String getLienOutput() {
		return lienOutput;
	}

	public void setLienOutput(String lienOutput) {
		this.lienOutput = lienOutput;
	}

	public boolean isShowMonitoringButton() {
		return showMonitoringButton;
	}

	public void setShowMonitoringButton(boolean showMonitoringButton) {
		this.showMonitoringButton = showMonitoringButton;
	}

	public LdapOperation getLdapOperation() {
		return ldapOperation;
	}

	public void setLdapOperation(LdapOperation ldapOperation) {
		this.ldapOperation = ldapOperation;
	}

	public boolean isNotLinkedMail() {
		return notLinkedMail;
	}

	public void setNotLinkedMail(boolean notLinkedMail) {
		this.notLinkedMail = notLinkedMail;
	}

	public boolean isLinkedMail() {
		return linkedMail;
	}

	public void setLinkedMail(boolean linkedMail) {
		this.linkedMail = linkedMail;
	}

	public long getNbrCourrierLies() {
		return nbrCourrierLies;
	}

	public void setNbrCourrierLies(long nbrCourrierLies) {
		this.nbrCourrierLies = nbrCourrierLies;
	}

	public boolean isAffichageDocument() {
		return affichageDocument;
	}

	public void setAffichageDocument(boolean affichageDocument) {
		this.affichageDocument = affichageDocument;
	}

	public boolean isAffichageDetailsAccuse() {
		return affichageDetailsAccuse;
	}

	public void setAffichageDetailsAccuse(boolean affichageDetailsAccuse) {
		this.affichageDetailsAccuse = affichageDetailsAccuse;
	}

	public boolean isStatusAccuseReception() {
		return statusAccuseReception;
	}

	public void setStatusAccuseReception(boolean statusAccuseReception) {
		this.statusAccuseReception = statusAccuseReception;
	}

	public boolean isShowResponseButton() {
		return showResponseButton;
	}

	public void setShowResponseButton(boolean showResponseButton) {
		this.showResponseButton = showResponseButton;
	}

	public boolean isHideResponseButton() {
		return hideResponseButton;
	}

	public void setHideResponseButton(boolean hideResponseButton) {
		this.hideResponseButton = hideResponseButton;
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

	public boolean isShowUpdateClassement() {
		return showUpdateClassement;
	}

	public void setShowUpdateClassement(boolean showUpdateClassement) {
		this.showUpdateClassement = showUpdateClassement;
	}

	public Classement_archivage_niveau_01 getAncienEtages() {
		return ancienEtages;
	}

	public void setAncienEtages(Classement_archivage_niveau_01 ancienEtages) {
		this.ancienEtages = ancienEtages;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public boolean isShowForValidate() {
		return showForValidate;
	}

	public void setShowForValidate(boolean showForValidate) {
		this.showForValidate = showForValidate;
	}

	public CourrierInformations getCi() {
		return ci;
	}

	public void setCi(CourrierInformations ci) {
		this.ci = ci;
	}

	public String getCodeUniqueCourrier() {
		return codeUniqueCourrier;
	}

	public void setCodeUniqueCourrier(String codeUniqueCourrier) {
		this.codeUniqueCourrier = codeUniqueCourrier;
	}

	public String getExpditeurUnite() {
		return expditeurUnite;
	}

	public void setExpditeurUnite(String expditeurUnite) {
		this.expditeurUnite = expditeurUnite;
	}

	public List<Variables> getVar() {
		return var;
	}

	public void setVar(List<Variables> var) {
		this.var = var;
	}

	public String getCupSRV() {
		return cupSRV;
	}

	public void setCupSRV(String cupSRV) {
		this.cupSRV = cupSRV;
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

	public String getBoutonBordereau() {
		return boutonBordereau;
	}

	public void setBoutonBordereau(String boutonBordereau) {
		this.boutonBordereau = boutonBordereau;
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

	public int getIdBoc() {
		return idBoc;
	}

	public void setIdBoc(int idBoc) {
		this.idBoc = idBoc;
	}

	public boolean isStatV1() {
		return statV1;
	}

	public void setStatV1(boolean statV1) {
		this.statV1 = statV1;
	}

	public boolean isStatV2() {
		return statV2;
	}

	public void setStatV2(boolean statV2) {
		this.statV2 = statV2;
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

	public Properties getMsg() {
		return msg;
	}

	public void setMsg(Properties msg) {
		this.msg = msg;
	}

	public ComposantDynamique getComposantDynamique() {
		return composantDynamique;
	}

	public void setComposantDynamique(ComposantDynamique composantDynamique) {
		this.composantDynamique = composantDynamique;
	}

	public List<ComposantDynamique> getD() {
		return D;
	}

	public void setD(List<ComposantDynamique> d) {
		D = d;
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

	public List<ComposantDynamique> getListCD() {
		return listCD;
	}

	public void setListCD(List<ComposantDynamique> listCD) {
		this.listCD = listCD;
	}

	public List<String> getL() {
		return listeDestinataireAnnotation;
	}

	public void setL(List<String> l) {
		this.listeDestinataireAnnotation = l;
	}

	public List<DonneeSupplementaireNature> getListDSNTransmission() {
		return listDSNTransmission;
	}

	public void setListDSNTransmission(
			List<DonneeSupplementaireNature> listDSNTransmission) {
		this.listDSNTransmission = listDSNTransmission;
	}

	public List<ComposantDynamique> getListCDTransmission() {
		return listCDTransmission;
	}

	public void setListCDTransmission(
			List<ComposantDynamique> listCDTransmission) {
		this.listCDTransmission = listCDTransmission;
	}

	public boolean isShowPanelCheque() {
		return showPanelCheque;
	}

	public void setShowPanelCheque(boolean showPanelCheque) {
		this.showPanelCheque = showPanelCheque;
	}

	public ListDataModel getListCheques() {
		return listCheques;
	}

	public void setListCheques(ListDataModel listCheques) {
		this.listCheques = listCheques;
	}

	public boolean isAfficheChampsSpecTansmission() {
		return afficheChampsSpecTansmission;
	}

	public void setAfficheChampsSpecTansmission(
			boolean afficheChampsSpecTansmission) {
		this.afficheChampsSpecTansmission = afficheChampsSpecTansmission;
	}

	public boolean isShowPanelAOC() {
		return showPanelAOC;
	}

	public void setShowPanelAOC(boolean showPanelAOC) {
		this.showPanelAOC = showPanelAOC;
	}

	public String getNumeroAoConsultation() {
		return numeroAoConsultation;
	}

	public void setNumeroAoConsultation(String numeroAoConsultation) {
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

	public boolean isAffichagePassageBO() {
		return affichagePassageBO;
	}

	public void setAffichagePassageBO(boolean affichagePassageBO) {
		this.affichagePassageBO = affichagePassageBO;
	}

	public void setDisbledConsultationLiens(boolean disbledConsultationLiens) {
		this.disbledConsultationLiens = disbledConsultationLiens;
	}

	public boolean isDisbledConsultationLiens() {
		return disbledConsultationLiens;
	}

	public boolean isDisbledBontonConsultation() {
		return disbledBontonConsultation;
	}

	public void setDisbledBontonConsultation(boolean disbledBontonConsultation) {
		this.disbledBontonConsultation = disbledBontonConsultation;
	}
	public void setAffichePanelReceptionPhysique(
			boolean affichePanelReceptionPhysique) {
		this.affichePanelReceptionPhysique = affichePanelReceptionPhysique;
	}

	public boolean isAffichePanelReceptionPhysique() {
		return affichePanelReceptionPhysique;
	}

	public int getRecordsListeAnnotation() {
		return recordsListeAnnotation;
	}

	public void setRecordsListeAnnotation(int recordsListeAnnotation) {
		this.recordsListeAnnotation = recordsListeAnnotation;
	}

	public List<ListeDestinatairesModel> getListeIntermediaire() {
		return listeIntermediaire;
	}

	public void setListeIntermediaire(
			List<ListeDestinatairesModel> listeIntermediaire) {
		this.listeIntermediaire = listeIntermediaire;
	}

	public String getBoutonNoteTransmission() {
		return boutonNoteTransmission;
	}

	public void setBoutonNoteTransmission(String boutonNoteTransmission) {
		this.boutonNoteTransmission = boutonNoteTransmission;
	}

	public String getRefValise() {
		return refValise;
	}

	public void setRefValise(String refValise) {
		this.refValise = refValise;
	}

	public String getCourrierReferenceCorrespondantExp() {
		return courrierReferenceCorrespondantExp;
	}

	public void setCourrierReferenceCorrespondantExp(
			String courrierReferenceCorrespondantExp) {
		this.courrierReferenceCorrespondantExp = courrierReferenceCorrespondantExp;
	}


}
