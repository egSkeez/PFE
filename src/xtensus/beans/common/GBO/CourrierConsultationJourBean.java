package xtensus.beans.common.GBO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import xtensus.MethdesGeneriques.MethodesGenerique;
import xtensus.aop.LogClass;
import xtensus.beans.common.EmailUtil;
import xtensus.beans.common.Ged;
import xtensus.beans.common.LanguageManagerBean;
import xtensus.beans.common.ListeDestinatairesModel;
import xtensus.beans.common.VariableGlobale;
import xtensus.beans.common.VariableGlobaleNotification;
import xtensus.beans.utils.CourrierInformations;
import xtensus.beans.utils.Informations;
import xtensus.beans.utils.NotificationListAddress;
import xtensus.dao.utils.DMSConnexionImplement;
import xtensus.entity.Annotation;
import xtensus.entity.Courrier;
import xtensus.entity.CourrierDossier;
import xtensus.entity.CourrierLiens;
import xtensus.entity.Document;
import xtensus.entity.Dossier;
import xtensus.entity.Etat;
import xtensus.entity.Expdest;
import xtensus.entity.Expdestexterne;
import xtensus.entity.Lienscourriers;
import xtensus.entity.Pm;
import xtensus.entity.Pp;
import xtensus.entity.Transaction;
import xtensus.entity.TransactionAnnotation;
import xtensus.entity.TransactionAnnotationId;
import xtensus.entity.TransactionDestination;
import xtensus.entity.TransactionDestinationId;
import xtensus.entity.TransactionDestinationReelle;
import xtensus.entity.Transmission;
import xtensus.entity.Typetransaction;
import xtensus.entity.Variables;
import xtensus.entity.Workflow;
import xtensus.ldap.business.LdapOperation;
import xtensus.ldap.model.BOC;
import xtensus.ldap.model.ItemSelected;
import xtensus.ldap.model.Person;
import xtensus.ldap.model.Unit;
import xtensus.services.ApplicationManager;
import xtensus.workflow.beans.JBPMAccessProcessBean;
import xtensus.workflow.handlers.TraitementEtapeSuivant;

@Component
@Scope("request")
public class CourrierConsultationJourBean {

	// General
	private ApplicationManager appMgr;
	@Autowired
	private VariableGlobale vb;
	@Autowired
	private LanguageManagerBean lm;
	@Autowired
	private MessageSource messageSource;
	private String message;
	private LdapOperation ldapOperation;
	@Autowired
	private VariableGlobaleNotification vbn;

	// Interface fonctionnement
	private boolean showTab;
	private boolean bocOption;
	private boolean userOption;
	private boolean status1;
	private boolean status2;

	// donnees
	private List<Transmission> listTr;
	private Variables varConsultationCourrierSecretaire;
	private Variables varConsultationCourrierSubordonne;
	private Variables varConsultationCourrierSousUnite;
	private List<CourrierInformations> listCourrierJour;
	private List<CourrierInformations> lstCourrierJour;
	private CourrierInformations selectedCourrier;
	private Courrier courrier;
	private Informations info1, info2, info3;
	private List<Informations> listInfo;
	private CourrierInformations courrierInformations;
	private Transaction newTransaction;
	private int idBoc;
	private int idUserDes;
	private String typeUserDes;
	private Unit unitSup;
	private int year;
	private int IdExpediteur;
	// new
	private Integer idUser;
	private String type;
	private String type1;
	private String typeSecretaire;
	private Integer typeTransmission;
	// private Integer stateTraitement;
	private Date dateDebut;
	private Date dateFin;
	private List<Integer> listIdsSousUnit;
	private List<Integer> listIdsSubordonne;
	private String consultationCourrierSecretaire;
	private String consultationCourrierSubordonne;
	private String consultationCourrierSousUnite;
	private Long countCourrier;
	private Long countCourrierRecu;
	private Long countCourrierEnvoyer;
	private Variables courrierAriverToDG;
	private boolean showResponsableResponse;
	private List<Integer> listIdBocMembers;
	private int idBocExpediteur;
	private int idBocDestinataire;
	// AH : ajouter pour récupérer la liste des Destinataires avec leurs
	// Annotations
	private List<ListeDestinatairesModel> destinatairesAvecAnnotations;
	// KHA
	private List<ListeDestinatairesModel> destinataireRepondre;
	// KHA - 25-03-2019
	private List<ItemSelected> listSelectedItem;
	private int referenceSomeA;

	private boolean receptionphysiqueNonLivre;
	private int flagueCloture;
	private int flagInterne;
	private String codeUniqueCourrier = "";
	private String cupSRV;
	Transaction newTransactionBocDest;
	int responsableBocDest = 0;
	private boolean executer = true;
	private boolean existeBOSansMembres;	
	private Boolean etatReceptionPhysique;
	private boolean courrierPointer=false;
	// 2019-11-25
	private HashMap<String, Object> filterMap = new HashMap<String, Object>();
   private List<CourrierInformations> listCourriersLiees ;
	private String sortField;
	private boolean descending;
	private List<CourrierInformations> listCourriersInformationsAffecte;
	private int lastIndex;
	private boolean disbledBontonConsultation=false;
	//JS: 2020-03-16
	private String align;
	private TransactionDestination transactionDestination2;
	private String selectedItemsTr;	
	private boolean showModificationButton;
	ArrayList<SelectItem> selectItemsTr2 = new ArrayList<SelectItem>();
	private boolean ajoutReceptionPhysiqueOK;
	public CourrierConsultationJourBean() {

	}

	@Autowired
	public CourrierConsultationJourBean(
			@Qualifier("applicationManager") ApplicationManager appMgr) {
		this.appMgr = appMgr;
		// ldapOperation = new LdapOperation();
		listCourrierJour = new ArrayList<CourrierInformations>();
		listInfo = new ArrayList<Informations>();
		info1 = new Informations();
		info2 = new Informations();
		info3 = new Informations();
		listIdsSousUnit = new ArrayList<Integer>();
		listIdsSubordonne = new ArrayList<Integer>();
		newTransactionBocDest = new Transaction();
		listCourriersInformationsAffecte = new ArrayList<CourrierInformations>();
		listCourriersLiees=new ArrayList<CourrierInformations>();
	}

	@PostConstruct
	public void Initialize() {
		try {

			// KHA - 25-03-2019
			listSelectedItem = new ArrayList<ItemSelected>();
			destinataireRepondre = new ArrayList<ListeDestinatairesModel>();
			// SM
			etatReceptionPhysique = false;
			courrierPointer=false;
			ldapOperation = vb.getLdapOperation();
			// C*
			courrierAriverToDG = appMgr.listVariablesById(13).get(0);
			// C*
			listTr = appMgr.getList(Transmission.class);
			setShowTab(true);
			setBocOption(false);
			setUserOption(true);
			if (vb.getPerson() != null) {
				if (vb.getPerson().isBoc()) {
					setShowTab(false);
					setBocOption(true);
					setUserOption(false);
				}
			}
			//|JS] : 2020-03-16
			if (vb.getLocale().equals("ar")) {
				align = "right";
			} else {
				align = "left";
			}
			
			
			varConsultationCourrierSecretaire = appMgr.listVariablesById(3)
					.get(0);
			varConsultationCourrierSubordonne = appMgr.listVariablesById(4)
					.get(0);
			varConsultationCourrierSousUnite = appMgr.listVariablesById(5).get(
					0);
			if(!vb.getPerson().isBoc() && vb.getPerson().isResponsable()){
			consultationCourrierSecretaire = varConsultationCourrierSecretaire
					.getVaraiablesValeur();
			consultationCourrierSubordonne = varConsultationCourrierSubordonne
					.getVaraiablesValeur();
			consultationCourrierSousUnite = varConsultationCourrierSousUnite
					.getVaraiablesValeur();
			}else{
				
							consultationCourrierSecretaire ="Non";
								consultationCourrierSubordonne ="Non";
								consultationCourrierSousUnite="Non";
			}
			// identify connected user
			idUser = vb.getPerson().getId();
			type = "";
			type1 = "";
			if (vb.getPerson().isBoc()) {
				listIdBocMembers = new ArrayList<Integer>();
				List<Person> listBocMembers = vb.getPerson().getAssociatedBOC()
						.getMembersBOC();
				for (Person person : listBocMembers) {
					listIdBocMembers.add(person.getId());
				}
				type = "boc_" + vb.getPerson().getAssociatedBOC().getIdBOC();
				type1 = "";
			} else if (vb.getPerson().isResponsable()) {
				type = "unit_"
						+ vb.getPerson().getAssociatedDirection().getIdUnit();
				type1 = "sub_" + idUser;

				if (consultationCourrierSousUnite.equals("Non")) {
					consultationCourrierSousUnite = "Non";
					consultationCourrierSubordonne = "Non";
				} else {		
					for (Unit unit : vb.getPerson().getAssociatedDirection()
							.getListUnitsChildUnit()) {
						if (consultationCourrierSousUnite.equals("Oui")) {
							listIdsSousUnit.add(unit.getIdUnit());
						}
						if (consultationCourrierSubordonne.equals("Oui")) {
							try {
								listIdsSubordonne.add(unit.getResponsibleUnit()
										.getId());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					
				}

				if (consultationCourrierSousUnite.equals("Non")) {
					consultationCourrierSousUnite = "Non";
					consultationCourrierSubordonne = "Non";
				} else {
					for (BOC boc : vb.getPerson().getAssociatedDirection()
							.getListBOChildUnit()) {
						List<Unit> listUnites = boc.getListDirectionsChildBOC();
						for (Unit unite : listUnites) {
							if (consultationCourrierSousUnite.equals("Oui")) {
								listIdsSousUnit.add(unite.getIdUnit());
							}
							if (consultationCourrierSubordonne.equals("Oui")) {
								try {
									listIdsSubordonne.add(unite
											.getResponsibleUnit().getId());
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}

						}					

				}
				if (consultationCourrierSecretaire.equals("Oui")) {
					try {
						if (vb.getPerson().getAssociatedDirection() != null
								&& vb.getPerson().getAssociatedDirection()
										.getSecretaryUnit() != null) {
							typeSecretaire = "secretary_"
									+ vb.getPerson().getAssociatedDirection()
											.getSecretaryUnit().getId();
						} else {
							consultationCourrierSecretaire = "Non";
						}
					} catch (NullPointerException e) {
						e.printStackTrace();
						consultationCourrierSecretaire = "Non";
					}
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
			typeTransmission = 0;
			// stateTraitement = 0;

			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			dateDebut = calendar.getTime();
			Calendar calendar1 = Calendar.getInstance();
			// calendar1.add(Calendar.DATE, 1);
			calendar1.set(Calendar.HOUR_OF_DAY, 23);
			calendar1.set(Calendar.MINUTE, 59);
			calendar1.set(Calendar.SECOND, 59);
			calendar1.set(Calendar.MILLISECOND, 999);
			dateFin = calendar1.getTime();
			// for Rapport
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<CourrierInformations> searchListCourrier(
			HashMap<String, Object> filterMap, String sortField,
			boolean descending, String typeCourrierJour,
			String categorieCourrierJour, String transmissionCourrierJour,
			String typeCourrierTraitementJour,
			String typeCourrierValidationJour, Integer firstIndex,
			Integer maxResult, boolean forRapport, String courrierRubriqueJour) {
		try {
			flagInterne = vb.getFlagInterne();
			flagueCloture = vb.getFlagCloture();
			
			lstCourrierJour = new ArrayList<CourrierInformations>();
			String sousTitreDeJour = "";

			if (vb.getPerson().isBoc()) {
				vb.setTransmissionCourrierJourForRapport(transmissionCourrierJour);
				vb.setTypeCourrierTraitementJourForRapport(typeCourrierTraitementJour);
				vb.setCategorieCourrierJourForRapport(categorieCourrierJour);

				if ((transmissionCourrierJour.equals("Tout les courriers") || transmissionCourrierJour
						.equals("Tous les courriers"))
						&& typeCourrierTraitementJour.equals("tous")
						&& categorieCourrierJour.equals("T")) {
					sousTitreDeJour = "Tous";
				} else {
					String result = "";
					if (!transmissionCourrierJour.equals("Tout les courriers")) {
						if (!transmissionCourrierJour
								.equals("Tous les courriers")) {
							result = getTypeTransmissionListeCourriers(transmissionCourrierJour)
									+ " -";
						}
					}
					if (!typeCourrierTraitementJour.equals("tous")) {
						result = result
								+ getTypeTraitememtListeCourriers(typeCourrierTraitementJour)
								+ " -";
					}
					if (!categorieCourrierJour.equals("T")) {
						result = result
								+ getCategorieListeCourriers(categorieCourrierJour);

					}

					if (result.endsWith(" -")) {
						sousTitreDeJour = result.substring(0,
								result.length() - 2);
					} else
						sousTitreDeJour = result;
				}
				vb.setSousTitreRapportBoc(sousTitreDeJour);

				lstCourrierJour = appMgr.findCourrierEnvoyerBOCByCriteria(
						filterMap, sortField, descending, 5, dateDebut,
						dateFin, type, type1, listIdBocMembers,
						transmissionCourrierJour, typeCourrierTraitementJour,
						firstIndex, maxResult, categorieCourrierJour,
						forRapport, vb.getDbType(), null, flagueCloture,
						flagInterne);
				// 2014-10-09 commented caused by request changes
				long startTime = System.currentTimeMillis();
				for (CourrierInformations courrierInformations : lstCourrierJour) {
					try {
						
						searchExpediteurDestinataire(courrierInformations);
					} catch (Exception e) {

						e.printStackTrace();
						
						continue;
					}
					
				}
				

			} else {

				
				Integer courrierRubriqueJourId = Integer
						.valueOf(courrierRubriqueJour);

				vb.setTypeCourrierJourForRapport(typeCourrierJour);
				vb.setTypeCourrierValidationJourForRapport(typeCourrierValidationJour);
				vb.setCourrierRubriqueJour(courrierRubriqueJour);

				if (vb.getPerson().isResponsable()) {

					if (typeCourrierJour.equals("Tous")
							&& courrierRubriqueJour.equals("1")) {
						sousTitreDeJour = "Tous";

					} else {
						String result = "";
						if (!typeCourrierJour.equals("Tous")) {
							result = getTypeCourrierListeCourriers(typeCourrierJour)
									+ " -";
						}
						if (!courrierRubriqueJour.equals("1")) {
							result = result
									+ getRubriqueListeCourriers(courrierRubriqueJourId);
						}

						if (result.endsWith(" -")) {
							sousTitreDeJour = result.substring(0,
									result.length() - 2);
						} else {
							sousTitreDeJour = result;
						}
						vb.setSousTitreRapportResponsable(sousTitreDeJour);
					}

				} else {
					// --------- 2: si connectee est un Secretaire ou Agent
					sousTitreDeJour = getTypeCourrierListeCourriers(typeCourrierJour);
					vb.setSousTitreRapportSecAgent(sousTitreDeJour);
				}

				long startTime = System.currentTimeMillis();
				lstCourrierJour = appMgr.findCourrierEnvoyerANDRecuByCriteria(
						vb.getPerson().isResponsable(), listIdsSousUnit,
						listIdsSubordonne, filterMap, sortField, descending,
						consultationCourrierSecretaire,
						consultationCourrierSubordonne,
						consultationCourrierSousUnite, 5, dateDebut, dateFin,
						type, type1, typeSecretaire, idUser, typeTransmission,
						typeCourrierValidationJour, firstIndex, maxResult,
						forRapport, courrierRubriqueJourId, typeCourrierJour,
						vb.getDbType(), null, flagueCloture, flagInterne);

				for (CourrierInformations courrierInformations : lstCourrierJour) {
					try {
						searchExpediteurDestinataire(courrierInformations);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}

				}

			}

			listCourrierJour.clear();
			listCourrierJour.addAll(lstCourrierJour);
			return listCourrierJour;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void searchExpediteurDestinataire(
			CourrierInformations courrierInformations) throws Exception {
				
		destinatairesAvecAnnotations = new ArrayList<ListeDestinatairesModel>();
		listSelectedItem = new ArrayList<ItemSelected>();

		List<Object> listSelectedObject = new ArrayList<Object>();
		List<Person> listSelectedPerson = new ArrayList<Person>();
		List<Pp> listSelectetdPP = new ArrayList<Pp>();
		List<Pm> listSelectetdPM = new ArrayList<Pm>();
		List<Unit> listSelectetdUnit = new ArrayList<Unit>();
		List<BOC> listSelectetdBoc = new ArrayList<BOC>();
							   
		// get transaction destinataire by id transaction

		List<TransactionDestination> listDestinataires = appMgr
				.getDestinationByIdTransaction(courrierInformations
						.getTransactionID());
		if (listDestinataires != null && listDestinataires.size() > 0) {
			TransactionDestination BocSuivant = listDestinataires.get(0);
			Integer etatID = courrierInformations.getEtatID();
			String expType;
			Integer expTypeUser;
			Integer expLdap;
			String expNom;
			String expPrenom;
			String expediteurReel="";
			if (courrierInformations.getCourrierOldNum() == null) {
				expType = courrierInformations.getExpType();
				expTypeUser = courrierInformations.getExpTypeUser();
				expLdap = courrierInformations.getExpLdap();
				expNom = courrierInformations.getExpNom();
				expPrenom = courrierInformations.getExpPrenom();
			} else {
				expType = courrierInformations.getExpTypeOld();
				expTypeUser = courrierInformations.getExpTypeUserOld();
				expLdap = courrierInformations.getExpLdapOld();
				expNom = courrierInformations.getExpNomOld();
				expPrenom = courrierInformations.getExpPrenomOld();
			}
			// expediteur reel
			String expediteur = "";

			// Ajouté le 2019-06-09
			String destinataireExpediteur = "";
			Variables variableExecution = appMgr.listVariablesByLibelle(
					"execution_courrier_par_tous_types_membre_bo").get(0);

			Transaction transaction = appMgr.getListTransactionByIdTransaction(courrierInformations.getTransactionID()).get(0);

			courrierInformations.setTransaction(transaction);

			Courrier courrierValise=appMgr.getCourrierByIdCourrier(courrierInformations.getCourrierID()).get(0);
			if(courrierValise.getCourrierDatePointage()==null){
				courrierInformations.setPointage(false);
			}else{
				courrierInformations.setPointage(true);
			}
			
			courrierInformations.setTransmission(appMgr.getCourrierByIdCourrier(courrierInformations.getCourrierID()).get(0).getTransmission());

			if (expType.equals("Interne-Person")) {
				if (expLdap.equals(vb.getPerson().getId())) {
					courrierInformations.setCourrierRecu(0);
				}
				if (courrierAriverToDG.getVaraiablesValeur().equals("Non")) {
					/***
					 * test pour que boc execute un courrier
					 */
					if (vb.getPerson().getAssociatedBOC() != null
							&& courrierInformations.getTransactionOrdre() == null
							&& etatID.equals(5)) {
						courrierInformations.setCourrierAValider(1);

					}
				}

				if (variableExecution.getVaraiablesValeur().equals("Non")) {
					if (vb.getPerson().getAssociatedBOC() != null
							&& courrierInformations.getTransactionOrdre() == null
							&& etatID.equals(5)) {

						courrierInformations.setCourrierAValider(1);
					} else
					if (vb.getPerson().getAssociatedBOC() != null
							&& vb.getPerson().isResponsableBO()
							&& courrierInformations.getTransactionOrdre() != null
							&& etatID.equals(5)) {
						courrierInformations.setCourrierAValider(1);
					}
				}
				else {
					if (vb.getPerson().getAssociatedBOC() != null
							&& courrierInformations.getTransactionOrdre() != null
							&& etatID.equals(5)
							) {
						courrierInformations.setCourrierAValider(1);

					}

				}

				Person person = vb.getHashMapAllUser().get(expLdap);

				Person p = vb.getLdapOperation().getPersonalisedUserById(
						person.getId());

				if (p.isResponsable() || p.isAgent() || p.isSecretary()) {

					destinataireExpediteur = p.getAssociatedDirection()
							.getShortNameUnit();
				}

				if (p.getAssociatedBOC() != null) {

					destinataireExpediteur = "BOC";

				}
				expediteur = person.getCn();

			} 
			else if (expType.equals("Interne-Unité")) {

				if (vb.getPerson().isResponsable()
						&& !vb.getPerson().isBoc()
						&& expLdap.equals(vb.getPerson()
								.getAssociatedDirection().getIdUnit())) {
					courrierInformations.setCourrierRecu(0);
				}

				/***
				 * test pour que boc execute un courrier
				 */
				if (courrierAriverToDG.getVaraiablesValeur().equals("Non")) {

					if (vb.getPerson().getAssociatedBOC() != null
							&& courrierInformations.getTransactionOrdre() == null
							&& etatID.equals(5)) {
						courrierInformations.setCourrierAValider(1);

					}
				}

				/***
				 * test pour que boc execute un courrier
				 */
				if (variableExecution.getVaraiablesValeur().equals("Non")) {

					if (vb.getPerson().getAssociatedBOC() != null
							&& courrierInformations.getTransactionOrdre() == null
							&& etatID.equals(5)) {
						courrierInformations.setCourrierAValider(1);

					} else

					// KHA : ajouté le test si le connectee est resp BO
					if (vb.getPerson().getAssociatedBOC() != null
							&& vb.getPerson().isResponsableBO()
							&& courrierInformations.getTransactionOrdre() != null
							&& etatID.equals(5)) {
						courrierInformations.setCourrierAValider(1);
					}

				}
				
				else if (vb.getPerson().getAssociatedBOC() != null
						&& courrierInformations.getTransactionOrdre() != null
						&& etatID.equals(5)) {
					courrierInformations.setCourrierAValider(1);
					// 2019-11-25 : Enveloppe => Masquer bouton execution
					// lorsque type transmission enveloppe

					if (courrierInformations.getTransmission().getTransmissionId()==11) {
						// Enveloppe : Caché bouton execution
						courrierInformations.setCourrierAValider(0);
					}
					
					if(courrierInformations.getTransmission().getTransmissionId()==9){
						
						//AH : c'est pour le blocage de l'exécution pour BO expéditeur pour les courrier avec MT "Valise"
						
						List<Transaction> tousTransactions = appMgr.getTransactionByIdDossier(courrierInformations.getDossierID());
						Transaction premiereTransaction = tousTransactions.get(tousTransactions.size() - 1);
							List<TransactionDestination> transactionDestinataires = appMgr.getListTransactionDestinationByIdTransaction(premiereTransaction.getTransactionId());
						if(transactionDestinataires!=null && transactionDestinataires.size()>0){
						int idBOExp=transactionDestinataires.get(0).getTransactionDestIdIntervenant();
						if(vb.getPerson().getAssociatedBOC().getIdBOC()==idBOExp)
							courrierInformations.setCourrierAValider(0);
						}	
						
						
					}
					
					List<CourrierLiens> list = appMgr.getCourrierLiensByCourrierId(courrierInformations.getCourrierID());

					for(CourrierLiens liensCourrier:list){
						int courrierLien=liensCourrier.getId().getLiensCourrier();
						List<Lienscourriers> list2 = appMgr.getCourrierL(courrierLien);
					}
				

				}

				Unit unit = vb.getHashMapUnit().get(expLdap);
				destinataireExpediteur = unit.getShortNameUnit();
				expediteur = unit.getNameUnit();
			}
			else if (expType.equals("Interne-Boc")) {
				expediteur = vb.getCentralBoc().getNameBOC();
				/***
				 * test pour que boc execute un courrier
				 */
				if (courrierAriverToDG.getVaraiablesValeur().equals("Non")) {

					if (vb.getPerson().getAssociatedBOC() != null
							&& courrierInformations.getTransactionOrdre() == null
							&& etatID.equals(5)) {
						courrierInformations.setCourrierAValider(1);

					}
				}
				/***
				 * test pour que boc execute un courrier
				 */
				if (variableExecution.getVaraiablesValeur().equals("Non")) {
					if (vb.getPerson().getAssociatedBOC() != null
							&& courrierInformations.getTransactionOrdre() == null
							&& etatID.equals(5)) {
						courrierInformations.setCourrierAValider(1);

					}

					else if (vb.getPerson().getAssociatedBOC() != null
							&& vb.getPerson().isResponsableBO()
							&& courrierInformations.getTransactionOrdre() != null
							&& etatID.equals(5)) {
						courrierInformations.setCourrierAValider(1);
					}
				} else if (vb.getPerson().getAssociatedBOC() != null
						&& courrierInformations.getTransactionOrdre() != null
						&& etatID.equals(5)) {
					courrierInformations.setCourrierAValider(1);

				}

			} 
			else if (expType.equals("Externe")) {
				if (courrierAriverToDG.getVaraiablesValeur().equals("Non")) {
					if (vb.getPerson().getAssociatedBOC() != null
							&& courrierInformations.getTransactionOrdre() == null
							&& etatID.equals(5)) {
						courrierInformations.setCourrierAValider(1);
					}
				}

				if (variableExecution.getVaraiablesValeur().equals("Non")) {
					if (vb.getPerson().getAssociatedBOC() != null
							&& courrierInformations.getTransactionOrdre() == null
							&& etatID.equals(5)) {
						courrierInformations.setCourrierAValider(1);
					} else if (vb.getPerson().getAssociatedBOC() != null
							&& vb.getPerson().isResponsableBO()
							&& courrierInformations.getTransactionOrdre() != null
							&& etatID.equals(5)) {
						courrierInformations.setCourrierAValider(1);
					}
				} else if (vb.getPerson().getAssociatedBOC() != null
						&& courrierInformations.getTransactionOrdre() != null
						&& etatID.equals(5)) {
					courrierInformations.setCourrierAValider(1);

				}
				List<Transaction> allTransactions = appMgr
				.getTransactionByIdDossier(courrierInformations
						.getDossierID());
	
		courrierInformations.setCourrierAllTransactions(allTransactions);
		Transaction firstTransaction = allTransactions.get(allTransactions
				.size() - 1);
				if(courrierInformations.getCourrierAllTransactions()!=null && courrierInformations.getCourrierAllTransactions().size()>0)
				{ List<Transaction> tousTR = courrierInformations.getCourrierAllTransactions();
				
				if(firstTransaction.getExpdest().getTypeExpDest().equals("Externe")){
					expTypeUser=firstTransaction.getExpdest().getExpdestexterne().getTypeutilisateur().getTypeUtilisateurId();
					
					expediteur=firstTransaction.getExpdest().getExpdestexterne().getExpDestExterneNom();
					if(expTypeUser == 1)
						expediteur=expediteur+ " "+firstTransaction.getExpdest().getExpdestexterne().getExpDestExternePrenom()+" (PP)";
					else 
						expediteur=expediteur+ " (PM)";
					destinataireExpediteur = "EXT";
				}
					
				}
				expediteurReel=expediteur;
			}
			
			courrierInformations.setCourrierExpediteur(expediteur);
			
			boolean connecteIsBoc=vb.getPerson().isBoc();
			Integer courrierEtat = courrierInformations.getTransaction().getEtat().getEtatId();
			List<BOC> listeBOS = vb.getListTousBos();
			List<Person> listTousUtilisateur = vb.getCopyLdapListUser();
			
			String anneeCourrier = "";
			String moisCourier="";
			List<Courrier> listCourriers = appMgr.listCourrierByIdTransaction(transaction.getTransactionId());
			if(listCourriers!=null && listCourriers.size()>0){
				Courrier courrierConsulte = listCourriers.get(0);
				anneeCourrier=courrierConsulte.getCourrierOldDateOper().toString();
				moisCourier=courrierConsulte.getCourrierDateReceptionMois().toString();
			}
			
			int idUtilisateurTransaction = courrierInformations.getTransaction().getIdUtilisateur();

			codeUniqueCourrier=MethodesGenerique.generationCodeUniqueCourrier(vb.getPerson(),appMgr, transaction,anneeCourrier,moisCourier, destinataireExpediteur, connecteIsBoc, courrierEtat, listeBOS, listTousUtilisateur,idUtilisateurTransaction);
			
			courrierInformations.setCourrierDestinataireReelleDirection(codeUniqueCourrier);
			vb.setCodeUniqueCourrier(courrierInformations.getCourrierDestinataireReelleDirection());
			
			List<TransactionDestination> listTransactionDestination = appMgr.getListTransactionDestinationByIdTransaction(courrierInformations.getTransactionID());

			if (!listTransactionDestination.isEmpty()) {
				// C'est la dernière transactionDEST
				courrierInformations.setTransactionDestination(listTransactionDestination
								.get(listTransactionDestination.size() - 1));
			}
			if ((etatID.equals(2) || etatID.equals(10))
					&& !vb.getPerson().isBoc()
					&& !courrierInformations.getIdUtilisateur().equals(
							vb.getPerson().getId())) {
				courrierInformations.setCourrierAValider(1);

			}
			// pour activer l'execution des courriers qui suit un workflow pour
			// le
			// boct et juste la premiere execution
			if (vb.getPerson().isBoc()
					&& courrierInformations.getCourrierCircuit().equals(
							"workflow")) {
				if (etatID.equals(10)
						&& courrierInformations.getTransactionOrdre().equals(1)) {
					courrierInformations.setCourrierAValider(1);
				}
			}

			if(!vb.getPerson().isBoc()&& destinataireExpediteur.equals(vb.getPerson().getAssociatedDirection().getShortNameUnit()))
				courrierInformations.setCourrierRecu(0);
				else{
					courrierInformations.setCourrierRecu(1);
				}

			// detinataire reel *
			StringBuilder destinataire = new StringBuilder("");
			StringBuilder destinataireCourrierReference = new StringBuilder("");

			String unitName;

			ListeDestinatairesModel destR;
	
			List<Transaction> allTransactions = appMgr.getTransactionByIdDossier(courrierInformations.getDossierID());
			courrierInformations.setCourrierAllTransactions(allTransactions);

			List<Transaction> allTransactionsByEtat = appMgr.getTransactionByIdDossierByEtat(courrierInformations
					.getDossierID());
			courrierInformations
				.setCourrierAllTransactionsByEtat(allTransactionsByEtat);

			Transaction firstTransaction = allTransactions.get(allTransactions
					.size() - 1);

			Expdest expdestExpediteurREEL = appMgr.getListExpDestByIdExpDest(
					firstTransaction.getExpdest().getIdExpDest()).get(0);
			courrierInformations.setExpDest(expdestExpediteurREEL);
		
			if(expdestExpediteurREEL.getTypeExpDest().equals("Interne-Unité")){	
				Unit u=ldapOperation.getUnitById(expdestExpediteurREEL.getIdExpDestLdap());
				//Unit u=ldapOperation.getUnitById(stringId);
				expediteurReel=u.getNameUnit();
			}
			else if(expdestExpediteurREEL.getTypeExpDest().equals("Interne-Person")){
				Person p=vb.getLdapOperation().getPersonalisedUserById(
						expdestExpediteurREEL.getIdExpDestLdap());
				expediteurReel=p.getPrenom()+ " "+p.getNom();
			}
			
			courrierInformations.setCourrierExpediteur(expediteurReel);
			
			if (courrierInformations.getDestReelList() != null) {
				destR = new ListeDestinatairesModel();
				List<String> destReelList = new ArrayList<String>(
						Arrays.asList(courrierInformations.getDestReelList()
								.split("\\|", -1)));

				for (int i = 0; i < destReelList.size(); i++) {
					List<String> destReelElement = new ArrayList<String>(
							Arrays.asList(destReelList.get(i).split(";", -1)));

					Integer idExpDest = 0;

					if (!destReelElement.get(1).equals("")) {
						idExpDest = Integer.valueOf(destReelElement.get(1));

					}

					String type = destReelElement.get(2);
					Integer ldap = 0;
					if (!destReelElement.get(3).equals("")) {
						ldap = Integer.valueOf(destReelElement.get(3));
					}

					String nom = destReelElement.get(4);
					String prenom = destReelElement.get(5);
					Integer typeUser = 0;
					if (!destReelElement.get(6).equals("")) {
						typeUser = Integer.valueOf(destReelElement.get(6));
					}
					Integer idDestReelLdap = 0;
					if (!destReelElement.get(7).equals("")) {
						idDestReelLdap = Integer
								.valueOf(destReelElement.get(7));
					}
					
					String destReelType = destReelElement.get(8);
					 String ref ="";
					 int idTRans = Integer.parseInt(destReelElement.get(0));
						List<Transaction> listTrans = appMgr.getListTransactionByIdTransaction(idTRans);
						if(listTrans!=null && listTrans.size()>0){
							ref = "["+listTrans.get(0).getCourrierReferenceCorrespondant()+"]";
							
						}
						
//2020-06-06						
						System.out.println("### 2020-06-06 ## ref = "+ref);
						if ((vb.isSonede()&& idDestReelLdap==0 && destReelType.equals("Interne-Unité"))||idDestReelLdap != 0) {
						
						System.out.println("### 2020-06-06 ## destReelType = "+destReelType);
						
						if (courrierInformations.getCourrierCircuit().equals(
								"workflow")) {
							try {
								Unit unitDestinataireReel = vb.getHashMapUnit()
										.get(idDestReelLdap);
								unitName = unitDestinataireReel.getNameUnit();

								ItemSelected itemSelected = new ItemSelected();
								itemSelected.setItemSelectedId(idDestReelLdap);
								itemSelected.setItemSelectedName(unitName);
								itemSelected.setSelectedObject(unitDestinataireReel);
								listSelectedItem.add(itemSelected);
								Object object = (Object) unitDestinataireReel;
								listSelectedObject.add(object);
								listSelectetdUnit.add(unitDestinataireReel);

							} catch (Exception e) {
								unitName = "Inconnue";
								e.printStackTrace();
							}
							destinataire.append(" / ");
							destinataire.append(unitName);

							break;
						} 
						else {
							
							if (destReelType.equals("Interne-Unité")) {
								Unit unit = vb.getHashMapUnit().get(
										idDestReelLdap);
								if (!destinataire.toString().contains(unit.getNameUnit())) {								
									destinataire.append(" / ");									
									destinataire.append(unit.getNameUnit());
									destinataireCourrierReference.append(unit
											.getNameUnit());
									List<Transaction> listTransaction = appMgr
											.getReferenceCourrierByDestinataire(idDestReelLdap, courrierInformations
													.getDossierID());											
									destinataireCourrierReference
											.append(" [")
											.append(listTransaction
													.get(0)
													.getCourrierReferenceCorrespondant())
											.append("]"+ " / ");
									destR = new ListeDestinatairesModel();
									destR.setDestinataireId(idDestReelLdap);
									destR.setDestinataireName(unit
											.getNameUnit());
									List<Annotation> listeAnnotationParDestinataire = new ArrayList<Annotation>();
									if (appMgr
											.listeAnnotationParDestinataireEtTransactionReell(
													idDestReelLdap,
													courrierInformations
															.getDossierID()) != null) {
										listeAnnotationParDestinataire = appMgr
												.listeAnnotationParDestinataireEtTransactionReell(
														idDestReelLdap,
														courrierInformations
																.getDossierID());
										
									} else {
										listeAnnotationParDestinataire = appMgr
												.listeAnnotationParDestinataireEtTransactionExpDest(
														idDestReelLdap,
														courrierInformations
																.getDossierID());
									}
									String otherAnnotation="";
									if (listeAnnotationParDestinataire != null) {
										List<String> listAnnotationDest = new ArrayList<String>();
										for (Annotation a : listeAnnotationParDestinataire) {
											listAnnotationDest.add(String.valueOf(a.getAnnotationId()));
											TransactionAnnotation ta = appMgr
													.getTransactionByIdAnnotation(a.getAnnotationId())
													.get(0);
											destR.setChooseAnnotationType("tous");
											if(a.getAnnotationId().intValue()==10){
											 Transaction tr = listTransaction.get(0);
											 otherAnnotation=tr.getTransactionCommentaireAnnotation();
											 destR.setOtherAnnotation(otherAnnotation);
											 destR.setChooseAnnotationType("autre");
											 }
										}
										destR.setListeAnnotations(listAnnotationDest);
									}
									destinatairesAvecAnnotations.add(destR);
									ItemSelected itemSelected = new ItemSelected();
									itemSelected
											.setItemSelectedId(idDestReelLdap);
									itemSelected.setItemSelectedName(unit
											.getNameUnit());
									listSelectedItem.add(itemSelected);

									Object object = (Object) unit;
									listSelectedObject.add(object);
									listSelectetdUnit.add(unit);
								

								}

							} 
							else if (destReelType.equals("Interne-Person")) {

								Person person = vb.getHashMapAllUser().get(
										idDestReelLdap);
								if (!destinataire.toString().contains(
										person.getCn())) {
									destinataire.append(" / ");
									destinataire.append(person.getCn());
									destinataireCourrierReference.append(person
											.getCn());
									List<Transaction> listTransaction = appMgr
											.getReferenceCourrierByDestinataire(idDestReelLdap, courrierInformations
													.getDossierID());
									if (listTransaction != null
											&& listTransaction.size() > 0)
										destinataireCourrierReference
												.append(" [")
												.append(listTransaction
														.get(0)
														.getCourrierReferenceCorrespondant())
												.append("]"+ " / ");
									destR = new ListeDestinatairesModel();
									destR.setDestinataireId(idDestReelLdap);
									destR.setDestinataireName(person.getCn());
									List<Annotation> l = new ArrayList<Annotation>();
									if (appMgr
											.listeAnnotationParDestinataireEtTransactionReell(
													idDestReelLdap,
													courrierInformations
															.getDossierID()) != null) {
										l = appMgr
												.listeAnnotationParDestinataireEtTransactionReell(
														idDestReelLdap,
														courrierInformations
																.getDossierID());
									} else {
										l = appMgr
												.listeAnnotationParDestinataireEtTransactionExpDest(
														idDestReelLdap,
														courrierInformations
																.getDossierID());

									}
									if (l != null) {
										List<String> listAnnotationDest = new ArrayList<String>();
										List<String> listRefCourrierDest = new ArrayList<String>();
										for (Annotation a : l) {

											listAnnotationDest
													.add(String.valueOf(a
															.getAnnotationId()));
											TransactionAnnotation ta = appMgr
													.getTransactionByIdAnnotation(
															a.getAnnotationId())
													.get(0);
											Transaction tr = appMgr
													.getListTransactionByIdTransaction(
															ta.getId()
																	.getIdTransaction())
													.get(0);
										}
										destR.setListeAnnotations(listAnnotationDest);
									}

									destinatairesAvecAnnotations.add(destR);
									
									ItemSelected itemSelected = new ItemSelected();
									itemSelected
											.setItemSelectedId(idDestReelLdap);
									itemSelected.setItemSelectedName(person
											.getCn());
									listSelectedItem.add(itemSelected);
									Object object = (Object) person;
									listSelectedObject.add(object);
									listSelectedPerson.add(person);
								}

							} 
							else if (destReelType.equals("Externe")) {

								if (vb.getPerson().isBoc() && !etatID.equals(6)) {
									courrierInformations.setCourrierAValider(1);

								}
								Expdestexterne destReelExterne = appMgr
										.getExpediteurById(idDestReelLdap).get(
												0);
								if (destReelExterne.getTypeutilisateur()
										.getTypeUtilisateurId().equals(1)) {
									String dest = destReelExterne
											.getExpDestExternePrenom()
											+ " "
											+ destReelExterne
													.getExpDestExterneNom();
									if (!destinataire.toString().contains(dest)) {
										destinataire.append(" / ");
										destinataire.append(dest);
										destR = new ListeDestinatairesModel();
										destR.setDestinataireId(idDestReelLdap);
										destR.setDestinataireName(dest);

										destinataireCourrierReference.append(dest);
										List<Transaction> listTransaction = appMgr
										.getReferenceCourrierByDestinataire(idDestReelLdap, courrierInformations
												.getDossierID());
										if (listTransaction != null
										&& listTransaction.size() > 0)
											destinataireCourrierReference
											.append(" [")
											.append(listTransaction
													.get(0)
													.getCourrierReferenceCorrespondant())
											.append("]"+ " / ");
										
										destinatairesAvecAnnotations.add(destR);
										ItemSelected itemSelected = new ItemSelected();
										itemSelected
												.setItemSelectedId(idDestReelLdap);
										itemSelected.setItemSelectedName(dest);
										listSelectedItem.add(itemSelected);

									}

								} else if (destReelExterne.getTypeutilisateur()
										.getTypeUtilisateurId().equals(2)) {
									String dest = destReelExterne
											.getExpDestExterneNom();
									if (!destinataire.toString().contains(dest)) {
										destinataire.append(" / ");
										destinataire.append(destReelExterne
												.getExpDestExterneNom());
										destR = new ListeDestinatairesModel();
										destR.setDestinataireId(idDestReelLdap);
										destR.setDestinataireName(dest);
										destinataireCourrierReference.append(dest);
										List<Transaction> listTransaction = appMgr
										.getReferenceCourrierByDestinataire(idDestReelLdap, courrierInformations
												.getDossierID());
										if (listTransaction != null
										&& listTransaction.size() > 0)
											destinataireCourrierReference
											.append(" [")
											.append(listTransaction
													.get(0)
													.getCourrierReferenceCorrespondant())
											.append("]"+" / ");
										
										destinatairesAvecAnnotations.add(destR);
										ItemSelected itemSelected = new ItemSelected();
										itemSelected
												.setItemSelectedId(idDestReelLdap);
										itemSelected.setItemSelectedName(dest);
										listSelectedItem.add(itemSelected);

									}

								}
							} else {
								
							}
						}

					}
					else {
						
						if (!listTransactionDestination.isEmpty()) {
							for (TransactionDestination transactionDestination : listTransactionDestination) {							
									if (type.equals("Interne-Person")) {
										courrierInformations.setCourrierRecu(1);
										courrierInformations.setTransactionDestination(transactionDestination);
										Person person = vb.getHashMapAllUser().get(ldap);
										if (!destinataire.toString().contains(person.getCn())) {
											destinataire.append(" / ");
											destinataire.append(person.getCn());
											destR = new ListeDestinatairesModel();
											destR.setDestinataireId(ldap);
											destR.setDestinataireName(person.getCn());
											destinatairesAvecAnnotations.add(destR);
											ItemSelected itemSelected = new ItemSelected();
											itemSelected.setItemSelectedId(ldap);
											itemSelected.setItemSelectedName(person.getCn());
											listSelectedItem.add(itemSelected);

											Object object = (Object) person;
											listSelectedObject.add(object);
											listSelectedPerson.add(person);
											
										}

									} 
									else if (type.equals("Interne-Unité")) {
										courrierInformations
												.setTransactionDestination(transactionDestination);

										Unit unit = vb.getHashMapUnit().get(
												ldap);
												
										if (!destinataire.toString().contains(
												unit.getNameUnit())) {
											destinataire.append(" / ");
											destinataire.append(unit
													.getNameUnit());
													destinataireCourrierReference.append(unit
											.getNameUnit());
											
											List<Transaction> listTransaction = appMgr
											.getReferenceCourrierByDestinataire(ldap, courrierInformations
													.getDossierID());
									if (listTransaction != null
											&& listTransaction.size() > 0){
										destinataireCourrierReference
												.append(" [")
												.append(listTransaction
														.get(0)
														.getCourrierReferenceCorrespondant())
												.append("]" +" / ");}
												else{

												}

											destR = new ListeDestinatairesModel();
											destR.setDestinataireId(ldap);
											destR.setDestinataireName(unit
													.getNameUnit());
											
											ItemSelected itemSelected = new ItemSelected();
											itemSelected
													.setItemSelectedId(ldap);
											itemSelected
													.setItemSelectedName(unit
															.getNameUnit());
											listSelectedItem.add(itemSelected);

											Object object = (Object) unit;
											listSelectedObject.add(object);
											listSelectetdUnit.add(unit);
								
											List<Annotation> listeAnnotationParDestinataire = new ArrayList<Annotation>();

									if (appMgr
											.listeAnnotationParDestinataireEtTransactionReell(
													ldap,
													courrierInformations
															.getDossierID()) != null) {
										
										listeAnnotationParDestinataire = appMgr
												.listeAnnotationParDestinataireEtTransactionReell(
														ldap,
														courrierInformations
																.getDossierID());
										
									} else {
										listeAnnotationParDestinataire = appMgr
												.listeAnnotationParDestinataireEtTransactionExpDest(
														ldap,
														courrierInformations
																.getDossierID());
									}
									String otherAnnotation="";
									if (listeAnnotationParDestinataire != null) {
										List<String> listAnnotationDest = new ArrayList<String>();
										for (Annotation a : listeAnnotationParDestinataire) {

											listAnnotationDest.add(String.valueOf(a.getAnnotationId()));
											
											
											TransactionAnnotation ta = appMgr
													.getTransactionByIdAnnotation(a.getAnnotationId())
													.get(0);
											destR.setChooseAnnotationType("tous");
											//Récupérer la transaction de l'annotaion
											if(a.getAnnotationId().intValue()==10){
											 Transaction tr = listTransaction.get(0);
											 otherAnnotation=tr.getTransactionCommentaireAnnotation();
											 destR.setOtherAnnotation(otherAnnotation);
											 destR.setChooseAnnotationType("autre");
											 }
										}
										destR.setListeAnnotations(listAnnotationDest);
									}

											destinatairesAvecAnnotations
													.add(destR);

										}

									} 
									else if (type.equals("Interne-Boc")) {

										if (vb.getPerson().isBoc()) {
											courrierInformations
													.setCourrierRecu(1);
											courrierInformations
													.setTransactionDestination(transactionDestination);
										}
										if (!destinataire.toString()
												.contains(
														vb.getCentralBoc()
																.getNameBOC())) {
											destinataire.append(" / ");
											destinataire.append(vb
													.getCentralBoc()
													.getNameBOC());
											destR = new ListeDestinatairesModel();
											destR.setDestinataireId(ldap);
											destR.setDestinataireName(vb
													.getCentralBoc()
													.getNameBOC());
											destinatairesAvecAnnotations
													.add(destR);
											ItemSelected itemSelected = new ItemSelected();
											itemSelected
													.setItemSelectedId(ldap);
											itemSelected.setItemSelectedName(vb
													.getCentralBoc()
													.getNameBOC());
											listSelectedItem.add(itemSelected);

										}
									}
									else if (type.equals("Externe")) {
										if (typeUser.equals(1)) {
											if (!destinataire.toString()
													.contains(
															nom + " " + prenom
																	+ " (PP)")) {
												destinataire.append(" / ");
												destinataire.append(nom + " "
														+ prenom + " (PP)");
												destR = new ListeDestinatairesModel();
												destR.setDestinataireId(ldap);
												destR.setDestinataireName(nom
														+ " " + prenom
														+ " (PP)");
												destinatairesAvecAnnotations
														.add(destR);
												
												ItemSelected itemSelected = new ItemSelected();
												itemSelected
														.setItemSelectedId(ldap);
												itemSelected
														.setItemSelectedName(nom
																+ " " + prenom);
												listSelectedItem
														.add(itemSelected);

											}
										} else {
											if (!destinataire.toString()
													.contains(nom + " (PM)")) {
												destinataire.append(" / ");
												destinataire.append(nom
														+ " (PM)");
												destR = new ListeDestinatairesModel();
												destR.setDestinataireId(ldap);
												destR.setDestinataireName(nom
														+ " (PM)");
												destinatairesAvecAnnotations
														.add(destR);

												
												ItemSelected itemSelected = new ItemSelected();
												itemSelected
														.setItemSelectedId(ldap);
												itemSelected
														.setItemSelectedName(nom
																+ " " + prenom);
												listSelectedItem
														.add(itemSelected);

											}
										}
									}
								//}
							}
						}
						if (courrierInformations.getCourrierRecu() == 1
								&& (etatID.equals(10) || etatID.equals(2))) {
							courrierInformations.setCourrierAValider(1);
							
						} 
						else {
							// provisoire .. juste pour activer l'execution des
							// courrier arrivé pour le BOCT
							if (courrierAriverToDG.getVaraiablesValeur()
									.equals("Non")) {
								if (!vb.getPerson().isBoc()) {
									courrierInformations.setCourrierAValider(0);
								}
							}
							// provisoire .. juste pour activer l'execution des
							// courrier arrivé pour le BOCT
						}
						if (vb.getPerson().getAssociatedBOC() != null
								&& courrierInformations.getCourrierRecu() == 1
								&& etatID.equals(5) && etatID.equals(2)) {
							courrierInformations.setCourrierAValider(1);
						}
					}
					
					// KHA =========
					courrierInformations
							.setListSelectedItemDest(listSelectedItem);
					courrierInformations
							.setListSelectedObject(listSelectedObject);
					courrierInformations
							.setListSelectedPerson(listSelectedPerson);
					courrierInformations
							.setListSelectetdUnit(listSelectetdUnit);
					courrierInformations.setListSelectetdBoc(listSelectetdBoc);				
				}
			}
			destinataire.delete(0, 3);
			courrierInformations.setCourrierDestinataireReelle(destinataire.toString());
			courrierInformations
					.setListeDestinatairesAvecAnnotations(destinatairesAvecAnnotations);
//////////
			lastIndex = destinataireCourrierReference.lastIndexOf("/");
			courrierInformations
				.setReferenceDestinataireReelle(destinataireCourrierReference
						.substring(0, lastIndex));
			

			/***
			 * test pour que bouton réception physique s'affiche 
			 */

			Expdest cupExpDest;
			cupExpDest = new Expdest();
			
			cupExpDest = appMgr.getListExpDestByIdExpDest(transaction.getExpdest().getIdExpDest()).get(0);	
	
			CourrierDossier courrierDossier1 = appMgr.getCourrierDossierByIdCourrier(courrierInformations.getCourrierID()).get(0);
			int refdossier = courrierDossier1.getId().getDossierId();

			Transaction transactionn = new Transaction();
			List<Transaction> listTr = appMgr.getTransactionByIdDossier(refdossier);
			lastIndex=listTr.size();
			transactionn=listTr.get(lastIndex-1);
			int idEditeur=transactionn.getIdUtilisateur();
			if(vb.getPerson().isBoc()){
				List<ListeDestinatairesModel> list = courrierInformations.getListeDestinatairesAvecAnnotations();
				List<Integer> listeIdDest = new ArrayList<Integer>();
				Courrier courrier=appMgr.getCourrierByIdCourrier(courrierInformations.getCourrierID()).get(0);
				for (ListeDestinatairesModel d : list) {
					int idDest = 0;
					// KHA reception physique
					if (vb.getPerson().isResponsable() && !vb.getPerson().isBoc()) {
						if (vb.getPerson().getAssociatedDirection().getIdUnit() == d
								.getDestinataireId()) {
							idDest = vb.getPerson().getId();
						} else {
							
							idDest = d.getDestinataireId();
						}
					}else{
					
						
					}
					listeIdDest.add(idDest);
				}
				
				
				// si existe une transaction avec l'état 6 exécuté donc le BO connecté n'est pas le BO expéditeur
				boolean boExpediteur = true;
				for(Transaction tt:allTransactions){
					if(tt.getEtat().getEtatId()==6)
						{boExpediteur = false;
						break;
						
						}
				}
				if(boExpediteur &&(!listIdBocMembers.contains(idEditeur))
						&& courrierInformations.getTransaction().getEtat().getEtatId()!=6){
					showModificationButton=true;
					}else{
					showModificationButton=false;
				}
				
				if(courrier.getIdcourrierFK()!=null){
					// AH : C'est le cas que le courrier est affecté à une valise pas de modifiaction
					showModificationButton=false;
				}
				courrierInformations.setShowModificationButton(showModificationButton);
			}
			
				if(courrierInformations.getTransmission().getTransmissionId()==10){
					disbledBontonConsultation=false;
					//si mode enveloppe à ne pas exécuter
					courrierInformations.setCourrierAValider(0);
						if(vb.getPerson().isBoc() && vb.getPerson().getId()!=idEditeur){
						//à ne pas consulter et à ne pas exécuter
							disbledBontonConsultation=true;
							}
				courrierInformations.setDisbledBontonConsultation(disbledBontonConsultation);
				
			}

			List<ListeDestinatairesModel> list = destinatairesAvecAnnotations;
			List<Integer> listeIdDest = new ArrayList<Integer>();
			for (ListeDestinatairesModel d : list) {
				int idDest = 0;
				// KHA reception physique
				if (vb.getPerson().isResponsable() && !vb.getPerson().isBoc()) {
					if (vb.getPerson().getAssociatedDirection().getIdUnit() == d
							.getDestinataireId()) {
						idDest = vb.getPerson().getId();
					} else {
						
						idDest = d.getDestinataireId();
					}
				}else{
				
					
				}
				listeIdDest.add(idDest);
				//
			}
			
			// récuperer liste des memebres de bureau d'ordre connecté
			List<Integer> listeIdMembresBOc=new ArrayList<Integer>();
			if(vb.getPerson().isBoc()){
			List<Person> listMembresBoc = vb.getPerson().getAssociatedBOC().getMembersBOC();
			for(Person membres:listMembresBoc){

				listeIdMembresBOc.add(membres.getId());
			}
			}
			//****************************
		
			Courrier courrier=appMgr.getCourrierByIdCourrier(courrierInformations.getCourrierID()).get(0);	
			
			if (courrier.getCourrierAvecDocumentPhysique() != null
					&& courrier.getCourrierAvecDocumentPhysique() == true ) {
				
				/* si Le personne connecté est un responsable et qui est l'éditeur  : bouton reception physique ne s'affiche pas**/
				if (vb.getPerson().isResponsable()  && !vb.getPerson().isBoc()) {
					if (cupExpDest.getIdExpDestLdap() != null
							&& vb.getPerson().getAssociatedDirection()
									.getIdUnit()  .equals( cupExpDest
									.getIdExpDestLdap())){

					etatReceptionPhysique = false;
					}else if(listeIdDest.contains(vb.getPerson().getId())){
						etatReceptionPhysique = true;
						if(appMgr.getListCourrierAvecReceptionPhysiqueByEtat(courrier.getIdCourrier(),transaction.getTransactionId()).size() !=0 ){
							
							etatReceptionPhysique = false;
							
						}
						
					}

				}	

				else if ((vb.getPerson().isBoc() && !listeIdMembresBOc.contains(idEditeur))
						|| listeIdDest.contains(vb.getPerson().getId())
						|| vb.getPerson().getId() == cupExpDest
								.getIdExpDestLdap().intValue()){
				etatReceptionPhysique = true;
				if(appMgr.getListCourrierAvecReceptionPhysiqueByEtat(courrier.getIdCourrier(),transaction.getTransactionId()).size() !=0 || transaction.getEtat().getEtatId()==6){
					etatReceptionPhysique = false;
				}
				}
			}
			
			if(courrier.getCourrierAvecDocumentPhysique() != null
					&& courrier.getCourrierAvecDocumentPhysique() != true){
				etatReceptionPhysique = false;
			}
	
			courrierInformations.setCourrierAvecDocumentPhysique(etatReceptionPhysique);
			
			if ((courrierInformations.getTransactionDateConsultation() == null && courrierInformations
					.getCourrierRecu() != 1)
					|| (courrierInformations.getTransactionDestination()
							.getTransactionDestDateConsultation() == null && courrierInformations
							.getCourrierRecu() == 1)) {
				courrierInformations.setStyle("tableau_liste_courrier_non_consulte_gras");
			}
			else{
				courrierInformations.setStyle("tableau_liste_courrier_consulte");
			}
			
			
			
		}
	}


	private Date dateReceptionPhysique;
	private String commentaireReceptionPhysique;
	private boolean erreur;
	
	public Date getDateReceptionPhysique() {
		return dateReceptionPhysique;
	}

	public void setDateReceptionPhysique(Date dateReceptionPhysique) {
		this.dateReceptionPhysique = dateReceptionPhysique;
	}

	public String getCommentaireReceptionPhysique() {
		return commentaireReceptionPhysique;
	}

	public void setCommentaireReceptionPhysique(String commentaireReceptionPhysique) {
		this.commentaireReceptionPhysique = commentaireReceptionPhysique;
	}

public void valider(){
    	
    
    	
    }
    
    
    public void updateModeTr() {
    	try {
			
			courrierInformations=vb.getCourrierInformations();
			Courrier courrier=appMgr.getCourrierByIdCourrier(courrierInformations.getCourrierID()).get(0);
			courrier.setTransmission(appMgr.getTransmissionById(Integer.valueOf(selectedItemsTr)).get(0));
	
			appMgr.update(courrier);		
			status1=true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			status2=true;
			e.printStackTrace();
		}
	}
	
	
    public void getSelectionRowJourForTR(){
    	
    }
	public void getSelectionRowJourModifModeTransmission() {


	}
   
    
	public void getSelectionRowJour() {
		try {
			vb.setSelectedListCourrier("CRjour");
			vb.setDestinataireReel("");
			vb.setPremiereEntreeTransfert(1);
			Transaction transaction = new Transaction();
			CourrierInformations consulterInformations = selectedCourrier;	
			vb.setAllTransactions(consulterInformations.getCourrierAllTransactions());
			Courrier courrier=appMgr.getCourrierByIdCourrier(consulterInformations.getCourrierID()).get(0);
			selectedItemsTr=courrier.getTransmission().getTransmissionId().toString();			
			vb.setMasquerPanelDetailCourrier(true);
			vb.setCodeUniqueCourrier(consulterInformations.getCourrierDestinataireReelleDirection());

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
			}
			if (consulterInformations.getListSelectedPerson() != null) {
				vb.setCopyListSelectedPerson(consulterInformations
						.getListSelectedPerson());
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
							.getTransactionID());

			if (!listTransactionDestination.isEmpty()) {
				vb.setTransactionDestination(listTransactionDestination
						.get(listTransactionDestination.size() - 1));// 2015-02-27
				consulterInformations
						.setTransactionDestination(listTransactionDestination
								.get(listTransactionDestination.size() - 1));

			}

			if (vb.getPerson().isBoc()) {
				if (consulterInformations.getCourrierDestinataireReelle()
						.contains("(PP)")
						|| consulterInformations
								.getCourrierDestinataireReelle().contains(
										"(PM)")) {
					vb.setShowMonitoringButtonForDest(false);
				} else {
					vb.setShowMonitoringButtonForDest(true);
				}
				try {
					if (transaction.getIdUtilisateur() != vb.getPerson()
							.getId()
							|| listCourrierJour.contains(consulterInformations)) {

						if (transaction.getIdUtilisateur() != vb.getPerson()
								.getId()) {

							TransactionDestination transactionDestination = new TransactionDestination();

							transactionDestination = consulterInformations
									.getTransactionDestination();
							if (transactionDestination
									.getTransactionDestDateConsultation() == null) {
								transactionDestination
										.setTransactionDestDateConsultation(new Date());
								appMgr.update(transactionDestination);
							}
							vb.setTransactionDestination(transactionDestination);
						} else {
							if (transaction.getTransactionDateConsultation() == null) {

								transaction
										.setTransactionDateConsultation(new Date());
								appMgr.update(transaction);
							}
							// ajouté lors #// C *# pour que la date de
							// consultation des courriers arrivé par le boct
							// soit enregistré
							TransactionDestination transactionDestination = consulterInformations
									.getTransactionDestination();

							Expdest expdest = appMgr.getListExpDestByIdExpDest(
									transactionDestination.getId()
											.getIdExpDest()).get(0);

							vb.setTransactionDestination(transactionDestination);
						}
					} else {
						if (transaction.getTransactionDateConsultation() == null) {
							transaction
									.setTransactionDateConsultation(new Date());
							appMgr.update(transaction);
						}
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			} 
			else {

				if (consulterInformations.getCourrierRecu() == 1
						&& consulterInformations.getTransactionDestination()
								.getTransactionDestDateConsultation() == null) {
					
					TransactionDestination transactionDestination = new TransactionDestination();
					transactionDestination = consulterInformations.getTransactionDestination();
					transactionDestination.setTransactionDestDateConsultation(new Date());
					appMgr.update(transactionDestination);
					vb.setTransactionDestination(transactionDestination);
					// chargement variable log & notification
//					chargementNotification(consulterInformations);
				}

				else if (transaction.getIdUtilisateur() == vb.getPerson()
						.getId()
						&& transaction.getTransactionDateConsultation() == null) {
					transaction.setTransactionDateConsultation(new Date());
					appMgr.update(transaction);
				}
			}

			vb.setCopyDestNom(consulterInformations.getCourrierDestinataireReelle());
			vb.setCopyExpNom(consulterInformations.getCourrierExpediteur());
			vb.setCopyCourrierCommentaire(consulterInformations.getCourrierCommentaire());
			vb.setCopyOtherDest(consulterInformations.getCourrierAutreDestinataires());
			vb.setTransaction(transaction);
			// ** expediteur reel
			vb.setCopyExpReelNom(consulterInformations.getCourrierExpediteur());
			// ** destinataire reel
//consulterInformations.getCourrierDestinataireReelle();
			
			
//2020-06-06
			System.out.println("REF ==> "+consulterInformations.getReferenceDestinataireReelle());
			
			vb.setDestinataireReel(consulterInformations.getReferenceDestinataireReelle());
			vb.setCourrierInformations(consulterInformations);
			vb.setReferenceDestinataireReel(consulterInformations.getReferenceDestinataireReelle());
			vb.setListeDestinataire(consulterInformations.getListeDestinatairesAvecAnnotations());

			// ** expediteur reel

			// Vider les listes des detinatires avant de transferer le courrier
			// pour ne pas garder le destinataire de l'ancien courrier
			vb.setCopyListSelectedUnit(new ArrayList<Unit>());
			vb.setCopyListSelectedPerson(new ArrayList<Person>());
			vb.setCopyListPP(new ArrayList<Pp>());
			vb.setCopyListPM(new ArrayList<Pm>());
			commentaireReceptionPhysique=null;
			List<TransactionAnnotation> annotations = new ArrayList<TransactionAnnotation>();
			annotations = appMgr.getAnnotationByIdTransaction(consulterInformations
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
			LogClass logClass = new LogClass();
			logClass.addTrack(
					"consultation",
					"Evénement de log de consultation du courrier "
							+ courrier.getIdCourrier() + "-"
							+ courrier.getCourrierReferenceCorrespondant(),
					vb.getPerson(), "INFO", appMgr);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getSelectionRowJourForValidate() {
		try {
			vb.setSelectedListCourrier("CRjour");
			Transaction transaction = new Transaction();
			CourrierInformations courrierInformations = selectedCourrier;
			if (courrierInformations.getCourrier() == null) {
				courrierInformations.setCourrier(appMgr
						.getCourrierByIdCourrier(
								courrierInformations.getCourrierID()).get(0));
			}
			if (courrierInformations.getTransaction() == null) {
				courrierInformations
						.setTransaction(appMgr
								.getListTransactionByIdTransaction(
										courrierInformations.getTransactionID())
								.get(0));
			}
			vb.setCourDossConsulterInformations(courrierInformations);
			// a commenter si on a renversé l'ancienne liste de courriers
			courrier = courrierInformations.getCourrier();
			vb.setCourrier(courrier);
			transaction = courrierInformations.getTransaction();

			switch (courrierInformations.getCourrierRecu()) {
			case 1:
				TransactionDestination transactionDestination = courrierInformations
						.getTransactionDestination();
				vb.setTransactionDestination(transactionDestination);
				if (courrierInformations.getTransactionDestination()
						.getTransactionDestDateConsultation() == null) {
					transactionDestination
							.setTransactionDestDateConsultation(new Date());
					appMgr.update(transactionDestination);
				}

				break;

			case 0:
				if (transaction.getTransactionDateConsultation() == null) {
					transaction.setTransactionDateConsultation(new Date());
					appMgr.update(transaction);
				}
				break;
			}
			vb.setCopyExpReelNom(courrierInformations.getCourrierExpediteur());
			vb.setDestinataireReel(courrierInformations.getCourrierDestinataireReelle());
			vb.setCopyDestNom(courrierInformations.getCourrierDestinataireReelle());
			vb.setCopyExpNom(courrierInformations.getCourrierExpediteur());
			vb.setCopyCourrierCommentaire(courrierInformations.getCourrierCommentaire());
			vb.setCopyOtherDest(courrierInformations.getCourrierAutreDestinataires());
			vb.setTransaction(transaction);
			vb.setListeDestinataire(courrierInformations.getListeDestinatairesAvecAnnotations());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getSelectionRowJourForResponse() {
		try {
			vb.setSelectedListCourrier("CRjour");
			Transaction transaction = new Transaction();
			CourrierInformations courrierInformations = selectedCourrier;
			if (courrierInformations.getCourrier() == null) {
				courrierInformations.setCourrier(appMgr
						.getCourrierByIdCourrier(
								courrierInformations.getCourrierID()).get(0));
			}
			if (courrierInformations.getTransaction() == null) {
				courrierInformations
						.setTransaction(appMgr
								.getListTransactionByIdTransaction(
										courrierInformations.getTransactionID())
								.get(0));
			}
			courrier = courrierInformations.getCourrier();
			vb.setCourDossConsulterInformations(courrierInformations);

			vb.setCourrier(courrier);
			transaction = courrierInformations.getTransaction();

			switch (courrierInformations.getCourrierRecu()) {
			case 1:
				if (courrierInformations.getTransactionDestination()
						.getTransactionDestDateConsultation() == null) {
					TransactionDestination transactionDestination = courrierInformations
							.getTransactionDestination();
					transactionDestination
							.setTransactionDestDateConsultation(new Date());
					appMgr.update(transactionDestination);

				}
				break;

			case 0:
				if (transaction.getTransactionDateConsultation() == null) {
					transaction.setTransactionDateConsultation(new Date());
					appMgr.update(transaction);
				}
				break;
			}
			if (courrierInformations.getCourrierExpediteurObjet() instanceof Person) {
				Person person = new Person();
				person = (Person) courrierInformations
						.getCourrierExpediteurObjet();
				vb.setCopyListSelectedPerson(new ArrayList<Person>());
				vb.getCopyListSelectedPerson().add(
						ldapOperation.getPersonalisedUserById(person.getId()));
				vb.setDestNom(person.getCn());
			} else if (courrierInformations.getCourrierExpediteurObjet() instanceof Unit) {
				Unit unit = new Unit();
				unit = (Unit) courrierInformations.getCourrierExpediteurObjet();
				vb.setCopyListSelectedUnit(new ArrayList<Unit>());
				vb.getCopyListSelectedUnit().add(unit);
				vb.setDestNom(unit.getNameUnit());
			}
			vb.setCopyDestNom(courrierInformations
					.getCourrierDestinataireReelle());
			vb.setCopyExpNom(courrierInformations.getCourrierExpediteur());
			vb.setCopyCourrierCommentaire(courrierInformations
					.getCourrierCommentaire());
			vb.setToReplay(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void repondre() {
	
	}
	
	

	
	/**
	 * 
	 * XTE - AH : Methode appelée lors de clic sur le bouton Exécuter dans la
	 * liste des Courrier d'aujourd'hui
	 * 
	 **/
	public void executeJour() {
		
	}


	public Long getCountCourrier(HashMap<String, Object> filterMap,
			String transmissionCourrierJour, String typeCourrierTraitementJour,
			String typeCourrierJour, String typeCourrierValidationJour,
			String categorieCourrierJour, String courrierRubriqueJour,
			boolean forTotal) {
		Long countCourrier = 0L;

		if (vb.getPerson().isBoc()) {
			countCourrier = appMgr.CountAllCourrierBOCByCriteria(filterMap, 15,
					dateDebut, dateFin, type, type1, listIdBocMembers,
					transmissionCourrierJour, typeCourrierTraitementJour,
					categorieCourrierJour);
			// }
		} else {
			countCourrier = CountCourrier(filterMap, 14, dateDebut, dateFin,
					type, type1, idUser, typeTransmission,
					typeCourrierValidationJour, typeCourrierJour,
					courrierRubriqueJour, forTotal);
		}

		return countCourrier;
	}

	public Long CountCourrier(HashMap<String, Object> filterMap,
			int jourOrAutre, Date dateDebut, Date dateFin, String type,
			String type1, Integer idUser, Integer typeTransmission,
			String stateTraitement, String typeCourrierJour,
			String courrierRubriqueJour, boolean forTotal) {
		Integer courrierRubriqueJourId = Integer.valueOf(courrierRubriqueJour);
		countCourrier = appMgr.CountAllCourrierEnvoyerANDRecuByCriteria(vb
				.getPerson().isResponsable(), listIdsSousUnit,
				listIdsSubordonne, filterMap, consultationCourrierSecretaire,
				consultationCourrierSubordonne, consultationCourrierSousUnite,
				jourOrAutre, dateDebut, dateFin, type, type1, typeSecretaire,
				idUser, typeTransmission, stateTraitement,
				courrierRubriqueJourId, forTotal, typeCourrierJour);
		return countCourrier;
	}

	// ---------------------- KHA : sous titre rapport------------
	public String getCategorieListeCourriers(String s) {
		String categorieListeCourriers = "";
		if (s.equals("T")) {
			categorieListeCourriers = "Tous";

		} else if (s.equals("A")) {
			categorieListeCourriers = "Arrivé";
		} else if (s.equals("D")) {
			categorieListeCourriers = "Départ";
		}
		return categorieListeCourriers;

	}

	public String getTypeTraitememtListeCourriers(String s) {
		String typeTraitememtListeCourriers = "";
		if (s.equals("tous")) {
			typeTraitememtListeCourriers = "Tous";

		} else if (s.equals("traite")) {
			typeTraitememtListeCourriers = "Traité";
		} else if (s.equals("nonTraite")) {
			typeTraitememtListeCourriers = "Non Traité";
		}
		return typeTraitememtListeCourriers;

	}

	public String getTypeTransmissionListeCourriers(String s) {
		String typeTransmissionListeCourriers = "";
		if (s.equals("Tous les courriers") || s.equals("Tout les courriers")) {
			typeTransmissionListeCourriers = "Tous";

		} else {
			typeTransmissionListeCourriers = appMgr
					.getTransmissionById(Integer.valueOf(s)).get(0)
					.getTransmissionLibelle();
		}

		return typeTransmissionListeCourriers;

	}

	// ------------------------ KHA : SOUS TITRE autre BOC --------------------
	public String getTypeCourrierListeCourriers(String s) {
		String typeCourriers = "";
		if (s.equals("Tous")) {
			typeCourriers = "Tous";

		} else if (s.equals("Recu")) {
			typeCourriers = "Reçus";
		} else if (s.equals("Envoyes")) {
			typeCourriers = "Envoyés";
		}

		return typeCourriers;
	}

	public String getRubriqueListeCourriers(int courrierRubriqueId) {
		String RubriqueCourriers = "";
		switch (courrierRubriqueId) {
		case 1:
			RubriqueCourriers = "Tous";
			break;
		case 2:
			RubriqueCourriers = "Mes Courriers";
			break;
		case 3:
			RubriqueCourriers = "Mon Unité";
			break;
		case 4:
			RubriqueCourriers = "De mes Subordonnées";
			break;
		case 5:
			RubriqueCourriers = "De ma Secretaire ";
			break;
		case 6:
			RubriqueCourriers = "De mes Sous Unités";
			break;

		default:
			break;
		}
		return RubriqueCourriers;
	}

	// --------------------------------

	// * Getters selectItems
	// SM : Optimisation pour la liste des modes de transmission affichées sous
	// la forme de bouton radio
	public List<SelectItem> getSelectItemsTr() {
		String libelle;
		String id;
		List<SelectItem> selectItemsTr = new ArrayList<SelectItem>();

		selectItemsTr.add(new SelectItem(messageSource.getMessage(
				"toutCourrier", new Object[] {}, lm.createLocal())));

		for (Transmission item : listTr) {
			if (vb.getLocale().equals("ar")) {
				libelle = item.getTransmissionLibelleAr();
			} else {
				libelle = item.getTransmissionLibelle();
			}
			id = item.getTransmissionId().toString();
			selectItemsTr.add(new SelectItem(id, libelle));
		}

		return selectItemsTr;
	}

	public CourrierInformations getSelectedCourrier() {
		return selectedCourrier;
	}

	public void setSelectedCourrier(CourrierInformations selectedCourrier) {
		this.selectedCourrier = selectedCourrier;
	}

	public Courrier getCourrier() {
		return courrier;
	}

	public void setCourrier(Courrier courrier) {
		this.courrier = courrier;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setShowTab(boolean showTab) {
		this.showTab = showTab;
	}

	public boolean isShowTab() {
		return showTab;
	}

	public void setBocOption(boolean bocOption) {
		this.bocOption = bocOption;
	}

	public boolean isBocOption() {
		return bocOption;
	}

	public void setUserOption(boolean userOption) {
		this.userOption = userOption;
	}

	public boolean isUserOption() {
		return userOption;
	}

	public void setStatus1(boolean status1) {
		this.status1 = status1;
	}

	public boolean isStatus1() {
		return status1;
	}

	public void setStatus2(boolean status2) {
		this.status2 = status2;
	}

	public boolean isStatus2() {
		return status2;
	}

	public VariableGlobale getVb() {
		return vb;
	}

	public void setVb(VariableGlobale vb) {
		this.vb = vb;
	}

	public String getConsultationCourrierSecretaire() {
		return consultationCourrierSecretaire;
	}

	public void setConsultationCourrierSecretaire(
			String consultationCourrierSecretaire) {
		this.consultationCourrierSecretaire = consultationCourrierSecretaire;
	}

	public String getConsultationCourrierSubordonne() {
		return consultationCourrierSubordonne;
	}

	public void setConsultationCourrierSubordonne(
			String consultationCourrierSubordonne) {
		this.consultationCourrierSubordonne = consultationCourrierSubordonne;
	}

	public String getConsultationCourrierSousUnite() {
		return consultationCourrierSousUnite;
	}

	public void setConsultationCourrierSousUnite(
			String consultationCourrierSousUnite) {
		this.consultationCourrierSousUnite = consultationCourrierSousUnite;
	}

	public Long getCountCourrierRecu() {
		return countCourrierRecu;
	}

	public void setCountCourrierRecu(Long countCourrierRecu) {
		this.countCourrierRecu = countCourrierRecu;
	}

	public Long getCountCourrierEnvoyer() {
		return countCourrierEnvoyer;
	}

	public void setCountCourrierEnvoyer(Long countCourrierEnvoyer) {
		this.countCourrierEnvoyer = countCourrierEnvoyer;
	}

	public Long getCountCourrier() {
		return countCourrier;
	}

	public void setCountCourrier(Long countCourrier) {
		this.countCourrier = countCourrier;
	}

	public void setShowResponsableResponse(boolean showResponsableResponse) {
		this.showResponsableResponse = showResponsableResponse;
	}

	public boolean getShowResponsableResponse() {
		return showResponsableResponse;
	}

	public List<ListeDestinatairesModel> getDestinatairesAvecAnnotations() {
		return destinatairesAvecAnnotations;
	}

	public void setDestinatairesAvecAnnotations(
			List<ListeDestinatairesModel> destinatairesAvecAnnotations) {
		this.destinatairesAvecAnnotations = destinatairesAvecAnnotations;
	}

	public List<ListeDestinatairesModel> getDestinataireRepondre() {
		return destinataireRepondre;
	}

	public void setDestinataireRepondre(
			List<ListeDestinatairesModel> destinataireRepondre) {
		this.destinataireRepondre = destinataireRepondre;
	}

	public List<ItemSelected> getListSelectedItem() {
		return listSelectedItem;
	}

	public void setListSelectedItem(List<ItemSelected> listSelectedItem) {
		this.listSelectedItem = listSelectedItem;
	}

	public CourrierInformations getCourrierInformations() {
		return courrierInformations;
	}

	public void setCourrierInformations(CourrierInformations courrierInformations) {
		this.courrierInformations = courrierInformations;
	}

	
	public int ReturnBocAssocieeUnite(Unit u) {
		if (u.getAssociatedUnit() != null && u.getIdUnit() != null) {
			Unit unite = ldapOperation.getUnitById(u.getAssociatedUnit()
					.getIdUnit());
			return ReturnBocAssocieeUnite(unite);

		} else if (u.getAssociatedBOC() != null) {
			return u.getAssociatedBOC().getIdBOC();

		} else {

			return 0;
		}

	}

	public void setTypeUserDes(String typeUserDes) {
		this.typeUserDes = typeUserDes;
	}

	public String getTypeUserDes() {
		return typeUserDes;
	}

	public void setReceptionphysiqueNonLivre(boolean receptionphysiqueNonLivre) {
		this.receptionphysiqueNonLivre = receptionphysiqueNonLivre;
	}

	public boolean isReceptionphysiqueNonLivre() {
		return receptionphysiqueNonLivre;
	}

	public String getCupSRV() {
		return cupSRV;
	}

	public void setCupSRV(String cupSRV) {
		this.cupSRV = cupSRV;
	}

	public String getCodeUniqueCourrier() {
		return codeUniqueCourrier;
	}

	public void setCodeUniqueCourrier(String codeUniqueCourrier) {
		this.codeUniqueCourrier = codeUniqueCourrier;
	}

	public boolean isExecuter() {
		return executer;
	}

	public void setExecuter(boolean executer) {
		this.executer = executer;
	}

	public boolean isExisteBOSansMembres() {
		return existeBOSansMembres;
	}

	public void setExisteBOSansMembres(boolean existeBOSansMembres) {
		this.existeBOSansMembres = existeBOSansMembres;
	}

	public void setEtatReceptionPhysique(Boolean etatReceptionPhysique) {
		this.etatReceptionPhysique = etatReceptionPhysique;
	}

	public Boolean getEtatReceptionPhysique() {
		return etatReceptionPhysique;
	}

	public void setCourrierPointer(boolean courrierPointer) {
		this.courrierPointer = courrierPointer;
	}

	public boolean isCourrierPointer() {
		return courrierPointer;
	}

	public void setListCourriersLiees(List<CourrierInformations> listCourriersLiees) {
		this.listCourriersLiees = listCourriersLiees;
	}

	public List<CourrierInformations> getListCourriersLiees() {
		return listCourriersLiees;
	}

	public boolean isDisbledBontonConsultation() {
		return disbledBontonConsultation;
	}

	public void setDisbledBontonConsultation(boolean disbledBontonConsultation) {
		this.disbledBontonConsultation = disbledBontonConsultation;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public TransactionDestination getTransactionDestination2() {
		return transactionDestination2;
	}

	public void setTransactionDestination2(
			TransactionDestination transactionDestination2) {
		this.transactionDestination2 = transactionDestination2;
	}

	public String getSelectedItemsTr() {
		return selectedItemsTr;
	}

	public void setSelectedItemsTr(String selectedItemsTr) {
		
		this.selectedItemsTr = selectedItemsTr;
	}

	public boolean isShowModificationButton() {
		return showModificationButton;
	}

	public void setShowModificationButton(boolean showModificationButton) {
		this.showModificationButton = showModificationButton;
	}

	public List<SelectItem> getSelectItemsTr2() {
		String libelle;
		List<SelectItem> selectItemsTr = new ArrayList<SelectItem>();
		selectItemsTr.add(new SelectItem(""));
		for (int j = 0; j <= listTr.size() - 1; j++) {
			Integer idTr = listTr.get(j).getTransmissionId();
			if (vb.getLocale().equals("ar")) {
				libelle = listTr.get(j).getTransmissionLibelleAr();
			} else {
				libelle = listTr.get(j).getTransmissionLibelle();
			}
			selectItemsTr.add(new SelectItem(String.valueOf(idTr), libelle));
		}
		return selectItemsTr;
	}

	public void setSelectItemsTr2(ArrayList<SelectItem> selectItemsTr2) {
		this.selectItemsTr2 = selectItemsTr2;
	}

	public void setAjoutReceptionPhysiqueOK(boolean ajoutReceptionPhysiqueOK) {
		this.ajoutReceptionPhysiqueOK = ajoutReceptionPhysiqueOK;
	}

	public boolean isAjoutReceptionPhysiqueOK() {
		return ajoutReceptionPhysiqueOK;
	}	
	public boolean isErreur() {
		return erreur;
	}

	public void setErreur(boolean erreur) {
		this.erreur = erreur;
	}
}