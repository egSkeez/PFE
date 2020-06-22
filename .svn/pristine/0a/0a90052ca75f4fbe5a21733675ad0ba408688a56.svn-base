package xtensus.beans.common.GBO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import xtensus.aop.LogClass;
import xtensus.beans.common.LanguageManagerBean;
import xtensus.beans.common.VariableGlobale;
import xtensus.beans.common.VariableGlobaleNotification;
import xtensus.beans.utils.CourrierInformations;
import xtensus.beans.utils.Informations;
import xtensus.entity.Courrier;
import xtensus.entity.Etat;
import xtensus.entity.Expdest;
import xtensus.entity.Expdestexterne;
import xtensus.entity.Nature;
import xtensus.entity.Transaction;
import xtensus.entity.TransactionAnnotation;
import xtensus.entity.TransactionDestination;
import xtensus.entity.TransactionDestinationReelle;
import xtensus.entity.Transmission;
import xtensus.entity.Variables;
import xtensus.ldap.business.LdapOperation;
import xtensus.ldap.model.Person;
import xtensus.ldap.model.Unit;
import xtensus.services.ApplicationManager;

@Component
@Scope("request")
public class CourrierConsultationBean {

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

	// Donnees
	List<CourrierInformations> lstCourrierEnvoyerJour;
	List<CourrierInformations> lstCourrierEnvoyerAncien;
	List<TransactionDestination> lstTrDestinationRecuJour;
	List<TransactionDestination> lstTrDestinationRecuAncien;
	List<CourrierInformations> lstCourrierRecuJour;
	List<CourrierInformations> lstCourrierRecuAncien;
	private List<CourrierInformations> listCourrier;
	private List<CourrierInformations> listCourrierJour;
	private DataModel listCourrierDM;
	private DataModel listCourrierJourDM;
	private DataModel listDossierDM;
	private DataModel listDossierJourDM;
	private Variables variableConsultationCourrierSecretaire;
	private Variables variableConsultationCourrierSubordonne;
	private Variables variableConsultationCourrierSousUnite;
	private long records = 0;
	private long recordsJour = 0;
	private long recordsDossier = 0;
	private long recordsDossierJour = 0;
	private Courrier courrier;
	private Informations info1, info2, info3, info4;
	private List<Informations> listInfo;
	private List<Transmission> listTr;
	// Interface disabled and rendred
	private boolean showTab;
	private boolean bocOption;
	private boolean userOption;
	private String typeCourrierValidationJour;
	private String typeCourrierValidation;
	private String transmissionCourrierJour;
	private String typeCourrierTraitementJour;
	private String transmissionCourrier;
	private String typeCourrierTraitement;
	private String categorieCourrierJour;
	private String categorieCourrier;
	private String typeCourrier;
	private String typeCourrierJour;
	private String typeDossier;
	private String typeDossierJour;
	private boolean moreChoices;
	private boolean moreChoicesJour;
	private boolean showExecuteAllButtonJour;
	private boolean showExecuteAllButton;
	private boolean hideExecuteAllButtonJour;
	private boolean hideExecuteAllButton;
	private Boolean init;

	public CourrierConsultationBean() {

	}

	@Autowired
	public CourrierConsultationBean(
			@Qualifier("applicationManager") ApplicationManager appMgr) {
		this.appMgr = appMgr;
		ldapOperation = new LdapOperation();
		// Donnés
		listCourrierDM = new ListDataModel();
		listCourrierJourDM = new ListDataModel();
		listDossierDM = new ListDataModel();
		listDossierJourDM = new ListDataModel();
		listCourrierJour = new ArrayList<CourrierInformations>();
		listCourrier = new ArrayList<CourrierInformations>();
		listInfo = new ArrayList<Informations>();
		info1 = new Informations();
		info2 = new Informations();
		info3 = new Informations();
	}

	@PostConstruct
	public void Initialize() {
		try {
			 
			
		
			init = true;
//			listTr = appMgr.getList(Transmission.class);
			showTab = true;
			bocOption = false;
			userOption = true;
			if (vb.getPerson() != null) {
				if (vb.getPerson().isBoc()) {
					showTab = false;
					bocOption = true;
					userOption = false;
				}
			}
			// Copier-Coller pour interface fonctionnement
			typeCourrierValidationJour = "";
			typeCourrierValidation = "";
			transmissionCourrierJour = "Tout les courriers";
			transmissionCourrier = "Tout les courriers";
			typeCourrierTraitementJour = "tous";
			typeCourrierTraitement = "tous";
			categorieCourrierJour = "T";
			categorieCourrier = "tous";
			typeCourrier = "Tous";
			typeCourrierJour = "Tous";
			typeDossier = "Tous";
			typeDossierJour = "Tous";
			moreChoices = false;
			moreChoicesJour = false;
			showExecuteAllButtonJour = false;
			showExecuteAllButton = false;
			hideExecuteAllButtonJour = true;
			hideExecuteAllButton = true;
			// Copier-Coller pour interface fonctionnement

			variableConsultationCourrierSecretaire = appMgr.listVariablesById(3).get(0);
			variableConsultationCourrierSubordonne = appMgr.listVariablesById(4).get(0);
			variableConsultationCourrierSousUnite = appMgr.listVariablesById(5).get(0);
			//searchListCourrier(typeCourrierJour, categorieCourrierJour, transmissionCourrierJour, typeCourrierTraitementJour);
//			listCourrierJour.addAll(lstCourrierEnvoyerJour);
//			listCourrierJour.addAll(lstCourrierRecuJour);
//			listCourrier.addAll(lstCourrierEnvoyerAncien);
//			listCourrier.addAll(lstCourrierRecuAncien);
//
//			// DM
//			listCourrierJourDM.setWrappedData(listCourrierJour);
//			listCourrierDM.setWrappedData(listCourrier);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void searchListCourrier(String typeCourrierJour, String categorieCourrierJour, String transmissionCourrierJour, String typeCourrierTraitementJour) {
		try {
			// identify connected user
//			Integer idUser = vb.getPerson().getId();
//			String type = "";
//			String type1 = "";
//			if (vb.getPerson().isBoc()) {
//				type = "boc_" + vb.getPerson().getAssociatedBOC().getIdBOC();
//				type1 = "";
//			} else if (vb.getPerson().isResponsable()) {
//				type = "unit_"
//						+ vb.getPerson().getAssociatedDirection().getIdUnit();
//				type1 = "sub_" + idUser;
//			} else if (vb.getPerson().isSecretary()) {
//				type = "secretary_" + idUser;
//				type1 = "unit_"
//						+ vb.getPerson().getAssociatedDirection().getIdUnit();
//			} else if (vb.getPerson().isAgent()) {
//				type = "agent_" + idUser;
//				type1 = "";
//			}
			// fin identify connected user
//			Integer typeTransmission = 0;
//			Integer stateTraitement = 0;

//			Calendar calendar = Calendar.getInstance();
//			calendar.set(Calendar.HOUR_OF_DAY, 0);
//			calendar.set(Calendar.MINUTE, 0);
//			calendar.set(Calendar.SECOND, 0);
//			calendar.set(Calendar.MILLISECOND, 0);
//			Date dateDebut = calendar.getTime();

//			Calendar calendar1 = Calendar.getInstance();
//			calendar1.add(Calendar.DATE, 1);
//			calendar1.set(Calendar.HOUR_OF_DAY, 23);
//			calendar1.set(Calendar.MINUTE, 59);
//			calendar1.set(Calendar.SECOND, 59);
//			calendar1.set(Calendar.MILLISECOND, 999);
//			Date dateFin = calendar1.getTime();
//			int firstIndex = 0;
//			int maxResult = 50;
			lstCourrierEnvoyerJour = new ArrayList<CourrierInformations>();
			lstCourrierEnvoyerAncien = new ArrayList<CourrierInformations>();
			lstCourrierRecuJour = new ArrayList<CourrierInformations>();
			lstCourrierRecuAncien = new ArrayList<CourrierInformations>();
			if (vb.getPerson().isBoc()) {
				// Etat etat = appMgr.listEtatByRef(6).get(0);
				// 2014-10-09 commented caused by request changes
//				lstCourrierEnvoyerJour = appMgr
//						.findCourrierEnvoyerBOCByCriteria(1, dateDebut,
//								dateFin, type, type1, idUser, transmissionCourrierJour,
//								typeCourrierTraitementJour, firstIndex, maxResult, categorieCourrierJour);
//				lstCourrierEnvoyerAncien = appMgr
//						.findCourrierEnvoyerBOCByCriteria(2, dateDebut,
//								dateFin, type, type1, idUser, transmissionCourrierJour,
//								typeCourrierTraitementJour, firstIndex, maxResult, categorieCourrierJour);
				// 2014-10-09 commented caused by request changes
				for (CourrierInformations courrierInformations : lstCourrierEnvoyerJour) {
					searchExpediteurDestinataire(courrierInformations);
				}
				// lstTrDestinationRecuJour =
				// appMgr.findCourrierRecuBOCByCriteria(1, dateDebut, dateFin,
				// type, type1, idUser, typeTransmission, stateTraitement,
				// firstIndex, maxResult, etat.getEtatId());
				// for (TransactionDestination transactionDestination :
				// lstTrDestinationRecuJour) {
				// lstCourrierRecuJour.add(searchExpediteurDestinataireAndExplodeDataTransactionDestination(transactionDestination));
				// }
			} else {
				if (typeCourrierJour.equals("Tous")
						|| typeCourrierJour.equals("Envoyes")) {
					// 2014-10-09 commented caused by request changes
//					lstCourrierEnvoyerJour = appMgr
//							.findCourrierEnvoyerByCriteria(1, dateDebut,
//									dateFin, type, type1, idUser,
//									typeTransmission, typeCourrierValidationJour,
//									firstIndex, maxResult);
//					lstCourrierEnvoyerAncien = appMgr
//							.findCourrierEnvoyerByCriteria(2, dateDebut,
//									dateFin, type, type1, idUser,
//									typeTransmission, typeCourrierValidation,
//									firstIndex, maxResult);
					// 2014-10-09 commented caused by request changes
					for (CourrierInformations courrierInformations : lstCourrierEnvoyerAncien) {
						searchExpediteurDestinataire(courrierInformations);
					}
					for (CourrierInformations courrierInformations : lstCourrierEnvoyerJour) {
						searchExpediteurDestinataire(courrierInformations);
					}
					
				} 
				if (typeCourrierJour.equals("Tous")
						|| typeCourrierJour.equals("Recu")) {
					// 2014-10-10 commented caused by request changes
//					lstTrDestinationRecuJour = appMgr
//							.findCourrierRecuByCriteria(1, dateDebut, dateFin,
//									type, type1, idUser, typeTransmission,
//									typeCourrierValidationJour, firstIndex, maxResult);
//					lstTrDestinationRecuAncien = appMgr
//							.findCourrierRecuByCriteria(2, dateDebut, dateFin,
//									type, type1, idUser, typeTransmission,
//									typeCourrierValidation, firstIndex, maxResult);
					// 2014-10-10 commented caused by request changes
					for (TransactionDestination transactionDestination : lstTrDestinationRecuAncien) {
						lstCourrierRecuAncien
								.add(searchExpediteurDestinataireAndExplodeDataTransactionDestination(transactionDestination));
					}
					for (TransactionDestination transactionDestination : lstTrDestinationRecuJour) {
						lstCourrierRecuJour
								.add(searchExpediteurDestinataireAndExplodeDataTransactionDestination(transactionDestination));
					}
					
					
				}
			}
			if(init == true){
				listCourrierJour.addAll(lstCourrierEnvoyerJour);
				listCourrierJour.addAll(lstCourrierRecuJour);
				listCourrier.addAll(lstCourrierEnvoyerAncien);
				listCourrier.addAll(lstCourrierRecuAncien);
			}else{
				listCourrierJour.clear();
				listCourrier.clear();
				if(typeCourrierJour.equals("Tous")){
					listCourrierJour.addAll(lstCourrierEnvoyerJour);
					listCourrierJour.addAll(lstCourrierRecuJour);
					listCourrier.addAll(lstCourrierEnvoyerAncien);
					listCourrier.addAll(lstCourrierRecuAncien);
				}else if (typeCourrierJour.equals("Recu")){
					listCourrierJour.addAll(lstCourrierRecuJour);
					listCourrier.addAll(lstCourrierRecuAncien);
				}else{
					listCourrierJour.addAll(lstCourrierEnvoyerJour);
					listCourrier.addAll(lstCourrierEnvoyerAncien);
				}
			}
			// DM
			listCourrierJourDM.setWrappedData(listCourrierJour);
			listCourrierDM.setWrappedData(listCourrier);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getSelectionRow() {
		try {
			Transaction transaction = new Transaction();
			CourrierInformations consulterInformations = (CourrierInformations) listCourrierDM
					.getRowData();
			//vb.setCourDossConsulterInformations(consulterInformations); A Décommenté
			if (consulterInformations.getCourrier() == null) {
				consulterInformations.setCourrier(appMgr.getCourrierByIdCourrier(consulterInformations.getCourrierID()).get(0));
			}
			if (consulterInformations.getTransaction() == null) {
				consulterInformations.setTransaction(appMgr.getListTransactionByIdTransaction(consulterInformations.getTransactionID()).get(0));
			}
			courrier = consulterInformations.getCourrier();
			vb.setCourrier(courrier);
			transaction = consulterInformations.getTransaction();

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

				if (transaction.getIdUtilisateur() != vb.getPerson().getId()
						|| listCourrier.contains(consulterInformations)) {
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
					}
				} else {
					if (transaction.getTransactionDateConsultation() == null) {
						transaction.setTransactionDateConsultation(new Date());
						appMgr.update(transaction);
					}
				}

			} else {
				if (listCourrier.contains(consulterInformations)
						|| transaction.getTransactionDestinationReelle() != null) {
					vb.setShowMonitoringButtonForDest(true);
				} else {
					vb.setShowMonitoringButtonForDest(false);
				}

				if (lstCourrierRecuAncien.contains(consulterInformations)
						&& consulterInformations.getTransactionDestination()
								.getTransactionDestDateConsultation() == null) {
					TransactionDestination transactionDestination = new TransactionDestination();
					transactionDestination = consulterInformations
							.getTransactionDestination();
					transactionDestination
							.setTransactionDestDateConsultation(new Date());
					appMgr.update(transactionDestination);
					vb.setTransactionDestination(transactionDestination);
					// chargement variable log & notification
					chargementNotification(consulterInformations);

				} else if (lstCourrierEnvoyerAncien
						.contains(consulterInformations)
						&& transaction.getTransactionDateConsultation() == null) {
					transaction.setTransactionDateConsultation(new Date());
					appMgr.update(transaction);
				}

			}
			vb.setCopyDestNom(consulterInformations
					.getCourrierDestinataireReelle());
			vb.setCopyExpNom(consulterInformations.getCourrierExpediteur());
			vb.setCopyCourrierCommentaire(consulterInformations.getCourrier()
					.getCourrierCommentaire());
			vb.setCopyOtherDest(consulterInformations
					.getCourrierAutreDestinataires());
			vb.setTransaction(transaction);

			// ** destinataire reel
			int refDossier = transaction.getDossier().getDossierId();
			List<Transaction> listTransaction = appMgr
					.getTransactionByIdDossier(refDossier);
			Transaction firstTransaction = listTransaction.get(listTransaction
					.size() - 1);
			Expdest expDest;
			expDest = firstTransaction.getExpdest();
			if (expDest.getTypeExpDest().equals("Interne-Person")) {
				vb.setCopyExpReelNom(ldapOperation.getCnById(
						ldapOperation.CONTEXT_USER, "uid",
						expDest.getIdExpDestLdap()));
			} else if (expDest.getTypeExpDest().equals("Interne-Unité")) {

				vb.setCopyExpReelNom(ldapOperation.getUnitById(
						expDest.getIdExpDestLdap()).getShortNameUnit());

			} else if (expDest.getTypeExpDest().equals("Interne-Boc")) {
				vb.setCopyExpReelNom(ldapOperation.getBocById(
						expDest.getIdExpDestLdap()).getNameUnit());
			} else if (expDest.getTypeExpDest().equals("Externe")) {
				if (expDest.getExpdestexterne().getTypeutilisateur()
						.getTypeUtilisateurLibelle().equals("PP")) {
					vb.setCopyExpReelNom(expDest.getExpdestexterne()
							.getExpDestExterneNom()
							+ " "
							+ expDest.getExpdestexterne()
									.getExpDestExternePrenom() + " (PP)");
				} else {
					vb.setCopyExpReelNom(expDest.getExpdestexterne()
							.getExpDestExterneNom() + " (PM)");
				}
			}
			// **
			List<TransactionAnnotation> annotations = new ArrayList<TransactionAnnotation>();
			annotations = appMgr
					.getAnnotationByIdTransaction(consulterInformations
							.getTransaction().getTransactionId());
			String annotationLibelle;
			int lastIndex;
			int refAnnotation;
			String result = "";
			for (TransactionAnnotation ta : annotations) {
				refAnnotation = ta.getId().getIdAnnotation();
				annotationLibelle = appMgr
						.getAnnotationByIdAnotation(refAnnotation).get(0)
						.getAnnotationLibelle();
				result = result + annotationLibelle + " / ";
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

	public void getSelectionRowJour() {
		try {
			Transaction transaction = new Transaction();
			CourrierInformations consulterInformations = (CourrierInformations) listCourrierJourDM
					.getRowData();
//			vb.setCourDossConsulterInformations(consulterInformations);  A Décommenté
			if (consulterInformations.getCourrier() == null) {
				consulterInformations.setCourrier(appMgr.getCourrierByIdCourrier(consulterInformations.getCourrierID()).get(0));
			}
			if (consulterInformations.getTransaction() == null) {
				consulterInformations.setTransaction(appMgr.getListTransactionByIdTransaction(consulterInformations.getTransactionID()).get(0));
			}
			courrier = consulterInformations.getCourrier();
			vb.setCourrier(courrier);
			transaction = consulterInformations.getTransaction();

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

			} else {
				if (listCourrierJour.contains(consulterInformations)
						|| transaction.getTransactionDestinationReelle() != null) {
					vb.setShowMonitoringButtonForDest(true);
				} else {
					vb.setShowMonitoringButtonForDest(false);
				}

				if (lstCourrierRecuJour.contains(consulterInformations)
						&& consulterInformations.getTransactionDestination()
								.getTransactionDestDateConsultation() == null) {
					TransactionDestination transactionDestination = new TransactionDestination();
					transactionDestination = consulterInformations
							.getTransactionDestination();
					transactionDestination
							.setTransactionDestDateConsultation(new Date());
					appMgr.update(transactionDestination);
					vb.setTransactionDestination(transactionDestination);
					// chargement variable log & notification
					chargementNotification(consulterInformations);

				} else if (lstCourrierEnvoyerJour
						.contains(consulterInformations)
						&& transaction.getTransactionDateConsultation() == null) {
					transaction.setTransactionDateConsultation(new Date());
					appMgr.update(transaction);
				}

			}
			vb.setCopyDestNom(consulterInformations
					.getCourrierDestinataireReelle());
			vb.setCopyExpNom(consulterInformations.getCourrierExpediteur());
			vb.setCopyCourrierCommentaire(consulterInformations.getCourrier()
					.getCourrierCommentaire());
			vb.setCopyOtherDest(consulterInformations
					.getCourrierAutreDestinataires());
			vb.setTransaction(transaction);

			// ** destinataire reel
			int refDossier = transaction.getDossier().getDossierId();
			List<Transaction> listTransaction = appMgr
					.getTransactionByIdDossier(refDossier);
			Transaction firstTransaction = listTransaction.get(listTransaction
					.size() - 1);
			Expdest expDest;
			expDest = firstTransaction.getExpdest();
			if (expDest.getTypeExpDest().equals("Interne-Person")) {
				vb.setCopyExpReelNom(ldapOperation.getCnById(
						ldapOperation.CONTEXT_USER, "uid",
						expDest.getIdExpDestLdap()));
			} else if (expDest.getTypeExpDest().equals("Interne-Unité")) {

				vb.setCopyExpReelNom(ldapOperation.getUnitById(
						expDest.getIdExpDestLdap()).getShortNameUnit());

			} else if (expDest.getTypeExpDest().equals("Interne-Boc")) {
				vb.setCopyExpReelNom(ldapOperation.getBocById(
						expDest.getIdExpDestLdap()).getNameUnit());
			} else if (expDest.getTypeExpDest().equals("Externe")) {
				if (expDest.getExpdestexterne().getTypeutilisateur()
						.getTypeUtilisateurLibelle().equals("PP")) {
					vb.setCopyExpReelNom(expDest.getExpdestexterne()
							.getExpDestExterneNom()
							+ " "
							+ expDest.getExpdestexterne()
									.getExpDestExternePrenom() + " (PP)");
				} else {
					vb.setCopyExpReelNom(expDest.getExpdestexterne()
							.getExpDestExterneNom() + " (PM)");
				}
			}
			// **
			List<TransactionAnnotation> annotations = new ArrayList<TransactionAnnotation>();
			annotations = appMgr
					.getAnnotationByIdTransaction(consulterInformations
							.getTransaction().getTransactionId());
			String annotationLibelle;
			int lastIndex;
			int refAnnotation;
			String result = "";
			for (TransactionAnnotation ta : annotations) {
				refAnnotation = ta.getId().getIdAnnotation();
				annotationLibelle = appMgr
						.getAnnotationByIdAnotation(refAnnotation).get(0)
						.getAnnotationLibelle();
				result = result + annotationLibelle + " / ";
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

	// *** Log && Notification ***//
	public void chargementNotification(
			CourrierInformations consulterInformations) {

		// Transaction transaction = new Transaction();
		// transaction = consulterInformations.getTransaction();
		// *** Log && Notification ***//
		vbn.setEvenementNomVariableNotif("event_consult_courrier_notif");
		vbn.setNotificationNomVariablAdmin("consult_courrier_admin");
		vbn.setNotificationNomVariableDestinataire("consult_courrier_dest");
		vbn.setEvenementNomVariableLog("event_consult_courrier_log");
		vbn.setNomExpediteur("Administrator");
		vbn.setMailExpediteur("xtexte2@gmail.com");
		// ***Fin Log & notification***//
		List<Person> listPersonDest = new ArrayList<Person>();

		String expdest = consulterInformations.getCourrierExpediteur();
		Courrier courrierConsult = new Courrier();
		courrierConsult = consulterInformations.getCourrier();

		Date dateSystem = new Date();
		SimpleDateFormat formaterDate = null;
		formaterDate = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

		String infoCourrier = "La réference de ce courrier est : "

		+ courrierConsult.getCourrierReferenceCorrespondant();

		vbn.setInfo(infoCourrier);
		vbn.setNomClass(CourrierAjoutBean.class.getName());
		vbn.setTypeLog("INFO");
		Person p = new Person();
		p = ldapOperation.getUserByName(expdest);
		listPersonDest.add(p);

		// Chargement Hard Codé de les elements à remplir dans la classe
		// informations
		info1.setVar("#p");
		info1.setContenu(vb.getPerson().getNom());
		info2.setVar("#I");
		info2.setContenu(infoCourrier);
		info3.setVar("#d");
		info3.setContenu(formaterDate.format(dateSystem));
		listInfo.add(info1);
		listInfo.add(info2);
		listInfo.add(info3);
		vbn.setListInformations(listInfo);
		vbn.setTypeObject("TEST");
		vbn.setCopyListSelectedPersonNotif(listPersonDest);
		vbn.setPerson(vb.getPerson());
	}

	public CourrierInformations searchExpediteurDestinataireAndExplodeDataTransactionDestination(
			TransactionDestination transactionDestination) {
		System.out.println("AH  DANS searchExpediteurDestinataireAndExplodeDataTransactionDestination");
		CourrierInformations courrierInformations = new CourrierInformations();
		courrierInformations.setTransactionDestination(transactionDestination);
		Courrier courrier = appMgr.listCourrierByIdTransaction(
				transactionDestination.getId().getIdTransaction()).get(0);
		courrierInformations.setCourrierReference(courrier
				.getCourrierReferenceCorrespondant());
		courrierInformations.setCourrierObjet(courrier.getCourrierObjet());
		courrierInformations.setCourrier(courrier);
		courrierInformations.setTransmission(courrier.getTransmission());
		Nature nature = appMgr
				.getNatureById(courrier.getNature().getNatureId()).get(0);
		courrierInformations.setCourrierNature(nature.getNatureLibelle());

		Transaction transaction = appMgr.getListTransactionByIdTransaction(
				transactionDestination.getId().getIdTransaction()).get(0);
		courrierInformations.setTransaction(transaction);
		courrierInformations.setCourrierDateReceptionEnvoi(transaction
				.getTransactionDateTransaction());
		courrierInformations.setCourrierRecu(1);
		if (transaction.getTransactionDestinationReelle() != null
				&& transaction.getEtat().getEtatId().equals(2)) {
			courrierInformations.setCourrierAValider(1);
		} else {
			courrierInformations.setCourrierAValider(0);
		}
		// expediteur
		Expdest expDest = appMgr.getListExpDestByIdExpDest(
				transaction.getExpdest().getIdExpDest()).get(0);
		if (expDest.getTypeExpDest().equals("Interne-Person")) {
			Person person = ldapOperation.getUserById(expDest
					.getIdExpDestLdap());
			courrierInformations.setCourrierExpediteur(person.getCn());
			courrierInformations.setCourrierExpediteurObjet(person);
		} else if (expDest.getTypeExpDest().equals("Interne-Unité")) {
			Unit unit = ldapOperation.getUnitById(expDest.getIdExpDestLdap());
			courrierInformations.setCourrierExpediteur(unit.getNameUnit());
			courrierInformations.setCourrierExpediteurObjet(unit);
		} else if (expDest.getTypeExpDest().equals("Interne-Boc")) {
			Unit unit = ldapOperation.getBocById(expDest.getIdExpDestLdap());
			courrierInformations.setCourrierExpediteur(unit.getNameUnit());
			courrierInformations.setCourrierExpediteurObjet(unit);
		} else if (expDest.getTypeExpDest().equals("Externe")) {
			if (expDest.getExpdestexterne().getTypeutilisateur()
					.getTypeUtilisateurLibelle().equals("PP")) {
				courrierInformations.setCourrierExpediteur(expDest
						.getExpdestexterne().getExpDestExterneNom()
						+ " "
						+ expDest.getExpdestexterne().getExpDestExternePrenom()
						+ " (PP)");
				courrierInformations.setCourrierExpediteurObjet(expDest
						.getExpdestexterne());
			} else {
				courrierInformations.setCourrierExpediteur(expDest
						.getExpdestexterne().getExpDestExterneNom() + " (PM)");
				courrierInformations.setCourrierExpediteurObjet(expDest
						.getExpdestexterne());
			}
		}
		// destinataire
		expDest = new Expdest();
		if (transaction.getTransactionDestinationReelle() != null) {
			TransactionDestinationReelle transactionDestinationReelle;
			Expdestexterne expDestExterne;
			transactionDestinationReelle = transaction
					.getTransactionDestinationReelle();
			if (transactionDestinationReelle
					.getTransactionDestinationReelleTypeDestinataire().equals(
							"Externe")) {
				Etat etat = appMgr.listEtatByRef(
						transaction.getEtat().getEtatId()).get(0);
				if (etat.getEtatLibelle().equals("Simple")) {
					expDest = appMgr.getListExpDestByIdExpDest(
							transactionDestination.getId().getIdExpDest()).get(
							0);
					if (expDest.getTypeExpDest().equals("Interne-Person")) {
						courrierInformations
								.setCourrierDestinataireReelle(ldapOperation
										.getCnById(ldapOperation.CONTEXT_USER,
												"uid",
												expDest.getIdExpDestLdap()));
					} else if (expDest.getTypeExpDest().equals("Interne-Unité")) {
						courrierInformations
								.setCourrierDestinataireReelle(ldapOperation
										.getCnById(ldapOperation.CONTEXT_UNIT,
												"departmentNumber",
												expDest.getIdExpDestLdap()));
					} else if (expDest.getTypeExpDest().equals("Interne-Boc")) {
						courrierInformations
								.setCourrierDestinataireReelle(ldapOperation
										.getCnById(ldapOperation.CONTEXT_BOC,
												"departmentNumber",
												expDest.getIdExpDestLdap()));
					}
				} else {
					expDestExterne = new Expdestexterne();
					expDestExterne = appMgr
							.getExpediteurById(
									transactionDestinationReelle
											.getTransactionDestinationReelleIdDestinataire())
							.get(0);
					if (expDestExterne.getTypeutilisateur()
							.getTypeUtilisateurLibelle().equals("PP")) {
						courrierInformations
								.setCourrierDestinataireReelle(expDestExterne
										.getExpDestExterneNom()
										+ " "
										+ expDestExterne
												.getExpDestExternePrenom()
										+ " (PP)");
					} else {
						courrierInformations
								.setCourrierDestinataireReelle(expDestExterne
										.getExpDestExterneNom() + " (PM)");
					}
				}
			}
		} else {
			expDest = new Expdest();
			expDest = appMgr.getListExpDestByIdExpDest(
					transactionDestination.getId().getIdExpDest()).get(0);
			if (expDest.getTypeExpDest().equals("Interne-Person")) {
				courrierInformations
						.setCourrierDestinataireReelle(ldapOperation.getCnById(
								ldapOperation.CONTEXT_USER, "uid",
								expDest.getIdExpDestLdap()));
			} else if (expDest.getTypeExpDest().equals("Interne-Unité")) {
				courrierInformations
						.setCourrierDestinataireReelle(ldapOperation.getCnById(
								ldapOperation.CONTEXT_UNIT, "departmentNumber",
								expDest.getIdExpDestLdap()));
			} else if (expDest.getTypeExpDest().equals("Interne-Boc")) {
				courrierInformations
						.setCourrierDestinataireReelle(ldapOperation.getCnById(
								ldapOperation.CONTEXT_BOC, "departmentNumber",
								expDest.getIdExpDestLdap()));
			}
		}
		if ((transaction.getTransactionDestinationReelle() != null
				&& (transaction.getEtat().getEtatLibelle().equals("A valider") || transaction
						.getEtat().getEtatLibelle().equals("Non traité")) || transaction
				.getEtat().getEtatLibelle().equals("Faire suivre"))) {
			courrierInformations.setCourrierAValider(1);
		} else {
			courrierInformations.setCourrierAValider(0);
		}
		courrierInformations.setTypeCourrier(getCategorieCourrier(
				transactionDestination, true));
		return courrierInformations;
	}

	public void searchExpediteurDestinataire(
			CourrierInformations courrierInformations) throws Exception {
		courrierInformations.setCourrierRecu(0);
		courrierInformations.setCourrierAValider(0);

		Courrier courrier = appMgr.listCourrierByIdTransaction(
				courrierInformations.getTransactionID()).get(0);
		courrierInformations.setCourrier(courrier);
		courrierInformations.setTransmission(courrier.getTransmission());
		Transaction transaction = appMgr.getListTransactionByIdTransaction(
				courrierInformations.getTransactionID()).get(0);
		courrierInformations.setTransaction(transaction);
		courrierInformations.setTypeCourrier(getCategorieCourrier(transaction, true));
		// expediteur
		StringBuilder expediteur = new StringBuilder("");
		if (courrierInformations.getExpDest().getTypeExpDest()
				.equals("Interne-Person")) {
			expediteur.append(ldapOperation.getCnById(
					ldapOperation.CONTEXT_USER, "uid", courrierInformations
							.getExpDest().getIdExpDestLdap()));

		} else if (courrierInformations.getExpDest().getTypeExpDest()
				.equals("Interne-Unité")) {
			expediteur.append(ldapOperation.getCnById(
					ldapOperation.CONTEXT_UNIT, "departmentNumber",
					courrierInformations.getExpDest().getIdExpDestLdap()));
		} else if (courrierInformations.getExpDest().getTypeExpDest()
				.equals("Interne-Boc")) {
			expediteur.append(ldapOperation.getCnById(
					ldapOperation.CONTEXT_BOC, "departmentNumber",
					courrierInformations.getExpDest().getIdExpDestLdap()));
		} else if (courrierInformations.getExpDest().getTypeExpDest()
				.equals("Externe")) {
			if (courrierInformations.getExpDest().getExpdestexterne()
					.getTypeutilisateur().getTypeUtilisateurLibelle()
					.equals("PP")) {
				expediteur
						.append(courrierInformations.getExpDest()
								.getExpdestexterne().getExpDestExterneNom())
						.append(" ")
						.append(courrierInformations.getExpDest()
								.getExpdestexterne().getExpDestExternePrenom())
						.append(" (PP)");

			} else {
				expediteur.append(
						courrierInformations.getExpDest().getExpdestexterne()
								.getExpDestExterneNom()).append(" (PM)");
			}
		}
		courrierInformations.setCourrierExpediteur(expediteur.toString());
		// destinataire
		StringBuilder destinataire = new StringBuilder("");
		List<TransactionDestination> listTransactionDestination = appMgr
				.getListTransactionDestinationByIdTransaction(courrierInformations
						.getTransactionID());
		if (!listTransactionDestination.isEmpty()) {
			TransactionDestination transactionDestination = listTransactionDestination
					.get(0);
			courrierInformations
					.setTransactionDestination(transactionDestination);
			Expdest expDest = appMgr.getListExpDestByIdExpDest(
					transactionDestination.getId().getIdExpDest()).get(0);
			if (expDest.getTypeExpDest().equals("Interne-Person")) {
				destinataire.append(ldapOperation.getCnById(
						ldapOperation.CONTEXT_USER, "uid",
						expDest.getIdExpDestLdap()));
			} else if (expDest.getTypeExpDest().equals("Interne-Unité")) {
				destinataire.append(ldapOperation.getCnById(
						ldapOperation.CONTEXT_UNIT, "departmentNumber",
						expDest.getIdExpDestLdap()));

			} else if (expDest.getTypeExpDest().equals("Interne-Boc")) {
				destinataire.append(ldapOperation.getCnById(
						ldapOperation.CONTEXT_BOC, "departmentNumber",
						expDest.getIdExpDestLdap()));
			} else if (expDest.getTypeExpDest().equals("Externe")) {
				if (expDest.getExpdestexterne().getTypeutilisateur()
						.getTypeUtilisateurLibelle().equals("PP")) {
					destinataire
							.append(expDest.getExpdestexterne()
									.getExpDestExterneNom())
							.append(" ")
							.append(expDest.getExpdestexterne()
									.getExpDestExternePrenom()).append(" (PP)");
				} else {
					destinataire.append(
							expDest.getExpdestexterne().getExpDestExterneNom())
							.append(" (PM)");
				}
			}
			courrierInformations.setCourrierDestinataireReelle(destinataire
					.toString());
		}
	}
	private String getCategorieCourrier(Transaction transaction, boolean isMail) {
		String result = "";
		String[] type = new String[2];
		if (vb.getPerson().isResponsable()) {
			if (transaction.getTransactionTypeIntervenant().contains("sub")) {
				type = transaction.getTransactionTypeIntervenant().split("_");
				if (Integer.parseInt(type[1]) == vb.getPerson().getId()) {
					if (isMail) {
						result = "A. Mes Propres Courriers";
					} else {
						result = "A. Mes Propres Dossiers";
					}
				} else {
					if (variableConsultationCourrierSecretaire
							.getVaraiablesValeur().equals("Non")
							&& variableConsultationCourrierSubordonne
									.getVaraiablesValeur().equals("Oui")
							&& variableConsultationCourrierSousUnite
									.getVaraiablesValeur().equals("Non")) {
						if (isMail) {
							result = "D. Les Courriers de Mes Subordonnées";
						} else {
							result = "D. Les Dossiers de Mes Subordonnées";
						}
					} else if (variableConsultationCourrierSecretaire
							.getVaraiablesValeur().equals("Non")
							&& variableConsultationCourrierSubordonne
									.getVaraiablesValeur().equals("Oui")
							&& variableConsultationCourrierSousUnite
									.getVaraiablesValeur().equals("Oui")
							|| variableConsultationCourrierSecretaire
									.getVaraiablesValeur().equals("Oui")
							&& variableConsultationCourrierSubordonne
									.getVaraiablesValeur().equals("Oui")
							&& variableConsultationCourrierSousUnite
									.getVaraiablesValeur().equals("Non")) {
						if (isMail) {
							result = "E. Les Courriers de Mes Subordonnées";
						} else {
							result = "E. Les Dossiers de Mes Subordonnées";
						}
					} else {
						if (isMail) {
							result = "F. Les Courriers de Mes Subordonnées";
						} else {
							result = "F. Les Dossiers de Mes Subordonnées";
						}
					}
				}
			} else if (transaction.getTransactionTypeIntervenant().contains(
					"unit")) {
				type = transaction.getTransactionTypeIntervenant().split("_");
				if (Integer.parseInt(type[1]) == vb.getPerson()
						.getAssociatedDirection().getIdUnit()) {
					if (isMail) {
						result = "B. Les Courriers de Mon Unité";
					} else {
						result = "B. Les Dossiers de Mon Unité";
					}
				} else {
					if (variableConsultationCourrierSecretaire
							.getVaraiablesValeur().equals("Non")
							&& variableConsultationCourrierSubordonne
									.getVaraiablesValeur().equals("Non")
							&& variableConsultationCourrierSousUnite
									.getVaraiablesValeur().equals("Oui")) {
						if (isMail) {
							result = "C. Les Courriers de Mes Sous-Unités";
						} else {
							result = "C. Les Dossiers de Mes Sous-Unités";
						}
					} else if (variableConsultationCourrierSecretaire
							.getVaraiablesValeur().equals("Non")
							&& variableConsultationCourrierSubordonne
									.getVaraiablesValeur().equals("Oui")
							&& variableConsultationCourrierSousUnite
									.getVaraiablesValeur().equals("Oui")
							|| variableConsultationCourrierSecretaire
									.getVaraiablesValeur().equals("Oui")
							&& variableConsultationCourrierSubordonne
									.getVaraiablesValeur().equals("Non")
							&& variableConsultationCourrierSousUnite
									.getVaraiablesValeur().equals("Oui")) {
						if (isMail) {
							result = "D. Les Courriers de Mes Sous-Unités";
						} else {
							result = "D. Les Dossiers de Mes Sous-Unités";
						}
					} else {
						if (isMail) {
							result = "E. Les Courriers de Mes Sous-Unités";
						} else {
							result = "E. Les Dossiers de Mes Sous-Unités";
						}
					}

				}
			} else if (transaction.getTransactionTypeIntervenant().contains(
					"secretary")) {
				if (isMail) {
					result = "C. Les Courriers de Ma Secrétaire";
				} else {
					result = "C. Les Dossiers de Ma Secrétaire";
				}
			} else if (transaction.getTransactionTypeIntervenant().contains(
					"agent")) {
				if (variableConsultationCourrierSecretaire
						.getVaraiablesValeur().equals("Non")
						&& variableConsultationCourrierSubordonne
								.getVaraiablesValeur().equals("Oui")
						&& variableConsultationCourrierSousUnite
								.getVaraiablesValeur().equals("Non")
						|| variableConsultationCourrierSecretaire
								.getVaraiablesValeur().equals("Non")
						&& variableConsultationCourrierSubordonne
								.getVaraiablesValeur().equals("Oui")
						&& variableConsultationCourrierSousUnite
								.getVaraiablesValeur().equals("Oui")) {
					if (isMail) {
						result = "C. Les Courriers de Mes Agents";
					} else {
						result = "C. Les Dossiers de Mes Agents";
					}
				} else {
					if (isMail) {
						result = "D. Les Courriers de Mes Agents";
					} else {
						result = "D. Les Dossiers de Mes Agents";
					}
				}
			}
		} else if (vb.getPerson().isSecretary()) {
			if (transaction.getTransactionTypeIntervenant().contains(
					"secretary")) {
				if (isMail) {
					result = "A. Mes Propres Courriers";
				} else {
					result = "A. Mes Propres Dossiers";
				}
			} else {
				if (isMail) {
					result = "B. Les Courriers de Mon Unité";
				} else {
					result = "B. Les Dossiers de Mon Unité";
				}
			}
		} else if (vb.getPerson().isAgent()) {
			if (transaction.getTransactionTypeIntervenant().contains("agent")) {
				if (isMail) {
					result = "A. Mes Propres Courriers";
				} else {
					result = "A. Mes Propres Dossiers";
				}
			} else {
				if (isMail) {
					result = "B. Les Courriers de Mon Bureau d'Ordre";
				} else {
					result = "B. Les Dossiers de Mon Bureau d'Ordre";
				}
			}
		} else {
			result = "A. Les Courriers du Bureau d'Ordre";
		}

		return result;
	}
	private String getCategorieCourrier(
			TransactionDestination transactionDestination, boolean isMail) {
		String result = "";
		String[] type = new String[2];
		if (vb.getPerson().isResponsable()) {
			if (transactionDestination.getTransactionDestTypeIntervenant()
					.contains("sub")) {
				type = transactionDestination
						.getTransactionDestTypeIntervenant().split("_");
				if (Integer.parseInt(type[1]) == vb.getPerson().getId()) {
					if (isMail) {
						result = "A. Mes Propres Courriers";
					} else {
						result = "A. Mes Propres Dossiers";
					}
				} else {
					if (variableConsultationCourrierSecretaire
							.getVaraiablesValeur().equals("Non")
							&& variableConsultationCourrierSubordonne
									.getVaraiablesValeur().equals("Oui")
							&& variableConsultationCourrierSousUnite
									.getVaraiablesValeur().equals("Non")) {
						if (isMail) {
							result = "D. Les Courriers de Mes Subordonnées";
						} else {
							result = "D. Les Dossiers de Mes Subordonnées";
						}
					} else if (variableConsultationCourrierSecretaire
							.getVaraiablesValeur().equals("Non")
							&& variableConsultationCourrierSubordonne
									.getVaraiablesValeur().equals("Oui")
							&& variableConsultationCourrierSousUnite
									.getVaraiablesValeur().equals("Oui")
							|| variableConsultationCourrierSecretaire
									.getVaraiablesValeur().equals("Oui")
							&& variableConsultationCourrierSubordonne
									.getVaraiablesValeur().equals("Oui")
							&& variableConsultationCourrierSousUnite
									.getVaraiablesValeur().equals("Non")) {
						if (isMail) {
							result = "E. Les Courriers de Mes Subordonnées";
						} else {
							result = "E. Les Dossiers de Mes Subordonnées";
						}
					} else {
						if (isMail) {
							result = "F. Les Courriers de Mes Subordonnées";
						} else {
							result = "F. Les Dossiers de Mes Subordonnées";
						}
					}
				}
			} else if (transactionDestination
					.getTransactionDestTypeIntervenant().contains("unit")) {
				type = transactionDestination
						.getTransactionDestTypeIntervenant().split("_");
				if (Integer.parseInt(type[1]) == vb.getPerson()
						.getAssociatedDirection().getIdUnit()) {
					if (isMail) {
						result = "B. Les Courriers de Mon Unité";
					} else {
						result = "B. Les Dossiers de Mon Unité";
					}
				} else {
					if (variableConsultationCourrierSecretaire
							.getVaraiablesValeur().equals("Non")
							&& variableConsultationCourrierSubordonne
									.getVaraiablesValeur().equals("Non")
							&& variableConsultationCourrierSousUnite
									.getVaraiablesValeur().equals("Oui")) {
						if (isMail) {
							result = "C. Les Courriers de Mes Sous-Unités";
						} else {
							result = "C. Les Dossiers de Mes Sous-Unités";
						}
					} else if (variableConsultationCourrierSecretaire
							.getVaraiablesValeur().equals("Non")
							&& variableConsultationCourrierSubordonne
									.getVaraiablesValeur().equals("Oui")
							&& variableConsultationCourrierSousUnite
									.getVaraiablesValeur().equals("Oui")
							|| variableConsultationCourrierSecretaire
									.getVaraiablesValeur().equals("Oui")
							&& variableConsultationCourrierSubordonne
									.getVaraiablesValeur().equals("Non")
							&& variableConsultationCourrierSousUnite
									.getVaraiablesValeur().equals("Oui")) {
						if (isMail) {
							result = "D. Les Courriers de Mes Sous-Unités";
						} else {
							result = "D. Les Dossiers de Mes Sous-Unités";
						}
					} else {
						if (isMail) {
							result = "E. Les Courriers de Mes Sous-Unités";
						} else {
							result = "E. Les Dossiers de Mes Sous-Unités";
						}
					}

				}
			} else if (transactionDestination
					.getTransactionDestTypeIntervenant().contains("secretary")) {
				if (isMail) {
					result = "C. Les Courriers de Ma Secrétaire";
				} else {
					result = "C. Les Dossiers de Ma Secrétaire";
				}
			} else if (transactionDestination
					.getTransactionDestTypeIntervenant().contains("agent")) {
				if (variableConsultationCourrierSecretaire
						.getVaraiablesValeur().equals("Non")
						&& variableConsultationCourrierSubordonne
								.getVaraiablesValeur().equals("Oui")
						&& variableConsultationCourrierSousUnite
								.getVaraiablesValeur().equals("Non")
						|| variableConsultationCourrierSecretaire
								.getVaraiablesValeur().equals("Non")
						&& variableConsultationCourrierSubordonne
								.getVaraiablesValeur().equals("Oui")
						&& variableConsultationCourrierSousUnite
								.getVaraiablesValeur().equals("Oui")) {
					if (isMail) {
						result = "C. Les Courriers de Mes Agents";
					} else {
						result = "C. Les Dossiers de Mes Agents";
					}
				} else {
					if (isMail) {
						result = "D. Les Courriers de Mes Agents";
					} else {
						result = "D. Les Dossiers de Mes Agents";
					}
				}
			}
		} else if (vb.getPerson().isSecretary()) {
			if (transactionDestination.getTransactionDestTypeIntervenant()
					.contains("secretary")) {
				if (isMail) {
					result = "A. Mes Propres Courriers";
				} else {
					result = "A. Mes Propres Dossiers";
				}
			} else {
				if (isMail) {
					result = "B. Les Courriers de Mon Unité";
				} else {
					result = "B. Les Dossiers de Mon Unité";
				}
			}
		} else if (vb.getPerson().isAgent()) {
			if (transactionDestination.getTransactionDestTypeIntervenant()
					.contains("agent")) {
				if (isMail) {
					result = "A. Mes Propres Courriers";
				} else {
					result = "A. Mes Propres Dossiers";
				}
			} else {
				if (isMail) {
					result = "B. Les Courriers de Mon Bureau d'Ordre";
				} else {
					result = "B. Les Dossiers de Mon Bureau d'Ordre";
				}
			}
		} else {
			result = "A. Les Courriers du Bureau d'Ordre";
		}

		return result;
	}
	public void test() {
		System.out.println("TESS I'M HERE");
	}

	public void eventChooseTypeCourrierJour(ActionEvent event) {
		 init = false;
		 if(event.getComponent().getId().equals("selectoneRadioId")){
            searchListCourrier(typeCourrierJour, categorieCourrierJour, transmissionCourrierJour, typeCourrierTraitementJour);
		 }else{
			searchListCourrier(typeCourrier, categorieCourrier, transmissionCourrier, typeCourrierTraitement) ;
		 }
	}
    public void eventChooseDepartArriveCourrierJour(ActionEvent event){
    	init = false;
    	UIComponent u = event.getComponent();
    	System.out.println("ID* : " + u.getId());
    	if(event.getComponent().getId().equals("sel")){
    	  searchListCourrier(typeCourrierJour, categorieCourrierJour, transmissionCourrierJour, typeCourrierTraitementJour);
    	}else{
    	  searchListCourrier(typeCourrier, categorieCourrier, transmissionCourrier, typeCourrierTraitement) ;
    	}
    }
    public void eventChooseTransmissionCourrierJour(ActionEvent event){
    	init = false;
    	if(event.getComponent().getId().equals("selectoneRadioIdBoc")){
    	    searchListCourrier(typeCourrierJour, categorieCourrierJour, transmissionCourrierJour, typeCourrierTraitementJour);
    	}else{
    		searchListCourrier(typeCourrier, categorieCourrier, transmissionCourrier, typeCourrierTraitement);
    	}
    }
    public void eventChooseTraitementCourrierJour(ActionEvent event){
    	init = false;
    	if(event.getComponent().getId().equals("selectoneRadioId111")){
         	searchListCourrier(typeCourrierJour, categorieCourrierJour, transmissionCourrierJour, typeCourrierTraitementJour);
    	}else{
    		searchListCourrier(typeCourrier, categorieCourrier, transmissionCourrier, typeCourrierTraitement);
    	}
    }
    public void eventChooseTypeCourrierValidationJour(ActionEvent event){
    	init = false;
    	if(event.getComponent().getId().equals("selectoneRadioId1")){
    	searchListCourrier(typeCourrierJour, categorieCourrierJour, transmissionCourrierJour, typeCourrierValidationJour);
    	}else{
    		searchListCourrier(typeCourrier, categorieCourrier, transmissionCourrier, typeCourrierTraitement) ;
    	}
    }
	// * Getters selectItems
	public List<SelectItem> getSelectItemsTr() {
		String libelle;
		List<SelectItem> selectItemsTr = new ArrayList<SelectItem>();

		selectItemsTr.add(new SelectItem(messageSource.getMessage(
				"toutCourrier", new Object[] {}, lm.createLocal())));
		for (int j = listTr.size() - 1; j > 1; j--) {
			if (vb.getLocale().equals("ar")) {
				libelle = listTr.get(j).getTransmissionLibelleAr();
			} else {
				libelle = listTr.get(j).getTransmissionLibelle();
			}
			selectItemsTr.add(new SelectItem(libelle));
		}
		selectItemsTr.add(new SelectItem(messageSource.getMessage("AutreLabel",
				new Object[] {}, lm.createLocal())));
		return selectItemsTr;
	}

	// * Getters & Setters
	public boolean isShowTab() {
		return showTab;
	}

	public void setShowTab(boolean showTab) {
		this.showTab = showTab;
	}

	public boolean isBocOption() {
		return bocOption;
	}

	public void setBocOption(boolean bocOption) {
		this.bocOption = bocOption;
	}

	public boolean isUserOption() {
		return userOption;
	}

	public void setUserOption(boolean userOption) {
		this.userOption = userOption;
	}

	public ApplicationManager getAppMgr() {
		return appMgr;
	}

	public void setAppMgr(ApplicationManager appMgr) {
		this.appMgr = appMgr;
	}

	public DataModel getListCourrierDM() {
		return listCourrierDM;
	}

	public void setListCourrierDM(DataModel listCourrierDM) {
		this.listCourrierDM = listCourrierDM;
	}

	public DataModel getListCourrierJourDM() {
		return listCourrierJourDM;
	}

	public void setListCourrierJourDM(DataModel listCourrierJourDM) {
		this.listCourrierJourDM = listCourrierJourDM;
	}

	public DataModel getListDossierDM() {
		return listDossierDM;
	}

	public void setListDossierDM(DataModel listDossierDM) {
		this.listDossierDM = listDossierDM;
	}

	public DataModel getListDossierJourDM() {
		return listDossierJourDM;
	}

	public void setListDossierJourDM(DataModel listDossierJourDM) {
		this.listDossierJourDM = listDossierJourDM;
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

	public LdapOperation getLdapOperation() {
		return ldapOperation;
	}

	public void setLdapOperation(LdapOperation ldapOperation) {
		this.ldapOperation = ldapOperation;
	}

	public String getTypeCourrierValidationJour() {
		return typeCourrierValidationJour;
	}

	public void setTypeCourrierValidationJour(String typeCourrierValidationJour) {
		this.typeCourrierValidationJour = typeCourrierValidationJour;
	}

	public String getTypeCourrierValidation() {
		return typeCourrierValidation;
	}

	public void setTypeCourrierValidation(String typeCourrierValidation) {
		this.typeCourrierValidation = typeCourrierValidation;
	}

	public String getTransmissionCourrierJour() {
		return transmissionCourrierJour;
	}

	public void setTransmissionCourrierJour(String transmissionCourrierJour) {
		this.transmissionCourrierJour = transmissionCourrierJour;
	}

	public String getTypeCourrierTraitementJour() {
		return typeCourrierTraitementJour;
	}

	public void setTypeCourrierTraitementJour(String typeCourrierTraitementJour) {
		this.typeCourrierTraitementJour = typeCourrierTraitementJour;
	}

	public String getTransmissionCourrier() {
		return transmissionCourrier;
	}

	public void setTransmissionCourrier(String transmissionCourrier) {
		this.transmissionCourrier = transmissionCourrier;
	}

	public String getTypeCourrierTraitement() {
		return typeCourrierTraitement;
	}

	public void setTypeCourrierTraitement(String typeCourrierTraitement) {
		this.typeCourrierTraitement = typeCourrierTraitement;
	}

	public String getCategorieCourrierJour() {
		return categorieCourrierJour;
	}

	public void setCategorieCourrierJour(String categorieCourrierJour) {
		this.categorieCourrierJour = categorieCourrierJour;
	}

	public String getCategorieCourrier() {
		return categorieCourrier;
	}

	public void setCategorieCourrier(String categorieCourrier) {
		this.categorieCourrier = categorieCourrier;
	}

	public String getTypeCourrier() {
		return typeCourrier;
	}

	public void setTypeCourrier(String typeCourrier) {
		this.typeCourrier = typeCourrier;
	}

	public String getTypeCourrierJour() {
		return typeCourrierJour;
	}

	public void setTypeCourrierJour(String typeCourrierJour) {
		this.typeCourrierJour = typeCourrierJour;
	}

	public String getTypeDossier() {
		return typeDossier;
	}

	public void setTypeDossier(String typeDossier) {
		this.typeDossier = typeDossier;
	}

	public String getTypeDossierJour() {
		return typeDossierJour;
	}

	public void setTypeDossierJour(String typeDossierJour) {
		this.typeDossierJour = typeDossierJour;
	}

	public boolean isMoreChoices() {
		return moreChoices;
	}

	public void setMoreChoices(boolean moreChoices) {
		this.moreChoices = moreChoices;
	}

	public boolean isMoreChoicesJour() {
		return moreChoicesJour;
	}

	public void setMoreChoicesJour(boolean moreChoicesJour) {
		this.moreChoicesJour = moreChoicesJour;
	}

	public boolean isShowExecuteAllButtonJour() {
		return showExecuteAllButtonJour;
	}

	public void setShowExecuteAllButtonJour(boolean showExecuteAllButtonJour) {
		this.showExecuteAllButtonJour = showExecuteAllButtonJour;
	}

	public boolean isShowExecuteAllButton() {
		return showExecuteAllButton;
	}

	public void setShowExecuteAllButton(boolean showExecuteAllButton) {
		this.showExecuteAllButton = showExecuteAllButton;
	}

	public boolean isHideExecuteAllButtonJour() {
		return hideExecuteAllButtonJour;
	}

	public void setHideExecuteAllButtonJour(boolean hideExecuteAllButtonJour) {
		this.hideExecuteAllButtonJour = hideExecuteAllButtonJour;
	}

	public boolean isHideExecuteAllButton() {
		return hideExecuteAllButton;
	}

	public void setHideExecuteAllButton(boolean hideExecuteAllButton) {
		this.hideExecuteAllButton = hideExecuteAllButton;
	}

	public Variables getVariableConsultationCourrierSecretaire() {
		return variableConsultationCourrierSecretaire;
	}

	public void setVariableConsultationCourrierSecretaire(
			Variables variableConsultationCourrierSecretaire) {
		this.variableConsultationCourrierSecretaire = variableConsultationCourrierSecretaire;
	}

	public Variables getVariableConsultationCourrierSubordonne() {
		return variableConsultationCourrierSubordonne;
	}

	public void setVariableConsultationCourrierSubordonne(
			Variables variableConsultationCourrierSubordonne) {
		this.variableConsultationCourrierSubordonne = variableConsultationCourrierSubordonne;
	}

	public Variables getVariableConsultationCourrierSousUnite() {
		return variableConsultationCourrierSousUnite;
	}

	public void setVariableConsultationCourrierSousUnite(
			Variables variableConsultationCourrierSousUnite) {
		this.variableConsultationCourrierSousUnite = variableConsultationCourrierSousUnite;
	}

	@SuppressWarnings("unchecked")
	public long getRecords() {
		if (listCourrierDM.getWrappedData() == null) {
			records = 0;
		} else {
			records = ((List<CourrierInformations>) listCourrierDM
					.getWrappedData()).size();
		}
		return records;
	}

	public void setRecords(long records) {
		this.records = records;
	}

	@SuppressWarnings("unchecked")
	public long getRecordsJour() {
		if (listCourrierJourDM.getWrappedData() == null) {
			recordsJour = 0;
		} else {
			recordsJour = ((List<CourrierInformations>) listCourrierJourDM
					.getWrappedData()).size();
		}
		return recordsJour;
	}

	public void setRecordsJour(long recordsJour) {
		this.recordsJour = recordsJour;
	}

	@SuppressWarnings("unchecked")
	public long getRecordsDossier() {
		if (listDossierDM.getWrappedData() == null) {
			recordsDossier = 0;
		} else {
			recordsDossier = ((List<CourrierInformations>) listDossierDM
					.getWrappedData()).size();
		}
		return recordsDossier;
	}

	public void setRecordsDossier(long recordsDossier) {
		this.recordsDossier = recordsDossier;
	}

	@SuppressWarnings("unchecked")
	public long getRecordsDossierJour() {
		if (listDossierJourDM.getWrappedData() == null) {
			recordsDossierJour = 0;
		} else {
			recordsDossierJour = ((List<CourrierInformations>) listDossierJourDM
					.getWrappedData()).size();
		}
		return recordsDossierJour;
	}

	public void setRecordsDossierJour(long recordsDossierJour) {
		this.recordsDossierJour = recordsDossierJour;
	}

	public List<CourrierInformations> getListCourrier() {
		return listCourrier;
	}

	public void setListCourrier(List<CourrierInformations> listCourrier) {
		this.listCourrier = listCourrier;
	}

	public List<CourrierInformations> getListCourrierJour() {
		return listCourrierJour;
	}

	public void setListCourrierJour(List<CourrierInformations> listCourrierJour) {
		this.listCourrierJour = listCourrierJour;
	}

	public VariableGlobaleNotification getVbn() {
		return vbn;
	}

	public void setVbn(VariableGlobaleNotification vbn) {
		this.vbn = vbn;
	}

	public List<CourrierInformations> getLstCourrierEnvoyerJour() {
		return lstCourrierEnvoyerJour;
	}

	public void setLstCourrierEnvoyerJour(
			List<CourrierInformations> lstCourrierEnvoyerJour) {
		this.lstCourrierEnvoyerJour = lstCourrierEnvoyerJour;
	}

	public List<CourrierInformations> getLstCourrierEnvoyerAncien() {
		return lstCourrierEnvoyerAncien;
	}

	public void setLstCourrierEnvoyerAncien(
			List<CourrierInformations> lstCourrierEnvoyerAncien) {
		this.lstCourrierEnvoyerAncien = lstCourrierEnvoyerAncien;
	}

	public List<TransactionDestination> getLstTrDestinationRecuJour() {
		return lstTrDestinationRecuJour;
	}

	public void setLstTrDestinationRecuJour(
			List<TransactionDestination> lstTrDestinationRecuJour) {
		this.lstTrDestinationRecuJour = lstTrDestinationRecuJour;
	}

	public List<TransactionDestination> getLstTrDestinationRecuAncien() {
		return lstTrDestinationRecuAncien;
	}

	public void setLstTrDestinationRecuAncien(
			List<TransactionDestination> lstTrDestinationRecuAncien) {
		this.lstTrDestinationRecuAncien = lstTrDestinationRecuAncien;
	}

	public List<CourrierInformations> getLstCourrierRecuJour() {
		return lstCourrierRecuJour;
	}

	public void setLstCourrierRecuJour(
			List<CourrierInformations> lstCourrierRecuJour) {
		this.lstCourrierRecuJour = lstCourrierRecuJour;
	}

	public List<CourrierInformations> getLstCourrierRecuAncien() {
		return lstCourrierRecuAncien;
	}

	public void setLstCourrierRecuAncien(
			List<CourrierInformations> lstCourrierRecuAncien) {
		this.lstCourrierRecuAncien = lstCourrierRecuAncien;
	}

	public Courrier getCourrier() {
		return courrier;
	}

	public void setCourrier(Courrier courrier) {
		this.courrier = courrier;
	}

	public Informations getInfo1() {
		return info1;
	}

	public void setInfo1(Informations info1) {
		this.info1 = info1;
	}

	public Informations getInfo2() {
		return info2;
	}

	public void setInfo2(Informations info2) {
		this.info2 = info2;
	}

	public Informations getInfo3() {
		return info3;
	}

	public void setInfo3(Informations info3) {
		this.info3 = info3;
	}

	public Informations getInfo4() {
		return info4;
	}

	public void setInfo4(Informations info4) {
		this.info4 = info4;
	}

	public List<Informations> getListInfo() {
		return listInfo;
	}

	public void setListInfo(List<Informations> listInfo) {
		this.listInfo = listInfo;
	}
}