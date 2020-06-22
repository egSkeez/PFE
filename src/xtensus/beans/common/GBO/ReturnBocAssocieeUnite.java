package xtensus.beans.common.GBO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import xtensus.aop.LogClass;
import xtensus.beans.common.LanguageManagerBean;
import xtensus.beans.common.ListeDestinatairesModel;
import xtensus.beans.common.VariableGlobale;
import xtensus.beans.common.VariableGlobaleNotification;
import xtensus.beans.utils.CourrierInformations;
import xtensus.beans.utils.Informations;
import xtensus.entity.Courrier;
import xtensus.entity.CourrierLiens;
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
public class ReturnBocAssocieeUnite {

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
	// private List<CourrierInformations> lstCourrierRecuJour;
	// private List<TransactionDestination> lstTrDestinationRecuJour;
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
	// AH : ajouter pour récupérer laliste des Destinataire avec leurs
	// Annotations
	private List<ListeDestinatairesModel> destinatairesAvecAnnotations;
	// KHA
	private List<ListeDestinatairesModel> destinataireRepondre;
	// KHA - 25-03-2019
	private List<ItemSelected> listSelectedItem;

	public ReturnBocAssocieeUnite() {

	}

	@Autowired
	public ReturnBocAssocieeUnite(
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
		System.out.println("CourrierConsultationJourBean");
	}

	@PostConstruct
	public void Initialize() {
		try {

			// KHA - 25-03-2019
			listSelectedItem = new ArrayList<ItemSelected>();
			destinataireRepondre = new ArrayList<ListeDestinatairesModel>();
			// SM

			System.out.println("***qsdqsd*******: "
					+ vb.getSelectedListCourrier());
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
			varConsultationCourrierSecretaire = appMgr.listVariablesById(3)
					.get(0);
			varConsultationCourrierSubordonne = appMgr.listVariablesById(4)
					.get(0);
			varConsultationCourrierSousUnite = appMgr.listVariablesById(5).get(
					0);
			consultationCourrierSecretaire = varConsultationCourrierSecretaire
					.getVaraiablesValeur();
			consultationCourrierSubordonne = varConsultationCourrierSubordonne
					.getVaraiablesValeur();
			System.out
					.println("----------kha : consultationCourrierSubordonne = "
							+ consultationCourrierSubordonne);
			consultationCourrierSousUnite = varConsultationCourrierSousUnite
					.getVaraiablesValeur();
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
				// NEW
				if (vb.getPerson().getAssociatedDirection()
						.getListUnitsChildUnit().isEmpty()) {
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
								System.out
										.println("#Sub-Unit without Responsible");
							}
						}
					}
				}
				// FIn NEW
				if (consultationCourrierSecretaire.equals("Oui")) {
					try {
						typeSecretaire = "secretary_"
								+ vb.getPerson().getAssociatedDirection()
										.getSecretaryUnit().getId();
					} catch (NullPointerException e) {
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
			System.out.println("*****dateDebut**** : " + dateDebut);

			Calendar calendar1 = Calendar.getInstance();
			// calendar1.add(Calendar.DATE, 1);
			calendar1.set(Calendar.HOUR_OF_DAY, 23);
			calendar1.set(Calendar.MINUTE, 59);
			calendar1.set(Calendar.SECOND, 59);
			calendar1.set(Calendar.MILLISECOND, 999);
			dateFin = calendar1.getTime();

			System.out.println("*****dateFin**** : " + dateFin);

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
			lstCourrierJour = new ArrayList<CourrierInformations>();
			String sousTitreDeJour = "";
			// lstCourrierRecuJour = new ArrayList<CourrierInformations>();
			if (vb.getPerson().isBoc()) {

				// Etat etat = appMgr.listEtatByRef(6).get(0);

				vb.setTransmissionCourrierJourForRapport(transmissionCourrierJour);
				vb.setTypeCourrierTraitementJourForRapport(typeCourrierTraitementJour);
				vb.setCategorieCourrierJourForRapport(categorieCourrierJour);
				// 2014-10-09 commented caused by request changes
				System.out.println("transmissionCourrierJour ="
						+ transmissionCourrierJour);
				System.out.println("*****transmissionCourrierJour**** : "
						+ transmissionCourrierJour);

				// ---------------KHA : Sous Titre Rapport : ajouté le
				// 19-03-2019-------
				System.out.println(" KHA : 0 : si connectee 	BOC ");
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
					System.out.println("result = " + result);

					if (result.endsWith(" -")) {
						sousTitreDeJour = result.substring(0,
								result.length() - 2);
					} else
						sousTitreDeJour = result;
					System.out.println("===========================");
					System.out.println("sousTitreDeJour = " + sousTitreDeJour);

				}
				vb.setSousTitreRapportBoc(sousTitreDeJour);
				System.out.println("KHA SousTitreRapportBoc = "
						+ vb.getSousTitreRapportBoc());
				// ----------------------- FIN : KHA

				// liste courrier envoyee par BOCT
				lstCourrierJour = appMgr.findCourrierEnvoyerBOCByCriteria(
						filterMap, sortField, descending, 5, dateDebut,
						dateFin, type, type1, listIdBocMembers,
						transmissionCourrierJour, typeCourrierTraitementJour,
						firstIndex, maxResult, categorieCourrierJour,
						forRapport, vb.getDbType());
				// 2014-10-09 commented caused by request changes
				long startTime = System.currentTimeMillis();
				for (CourrierInformations courrierInformations : lstCourrierJour) {
					try {
						searchExpediteurDestinataire(courrierInformations);
					} catch (Exception e) {

						e.printStackTrace();
						System.out.println("######CAUSED BY : "
								+ courrierInformations.getCourrierReference());
						continue;
					}
					// if(categorieCourrierJour.equals("D") &&
					// courrierInformations.getCourrierRecu() == 1){
					// listToRemove.add(courrierInformations);
					// lstCourrierEnvoyerJour.remove(courrierInformations);
					// }
				}
				System.out.println("list courrier jour dure BOCT : "
						+ (System.currentTimeMillis() - startTime));

				// for (CourrierInformations courrierInformations :
				// listToRemove) {
				// lstCourrierEnvoyerJour.remove(courrierInformations);
				// }

			} else {

				// int countRecu = getCountCourrierRecu().intValue();
				// int countEnvoi = getCountCourrierEnvoyer().intValue();
				// int max = maxResult / 2;
				// int firstRecu = firstIndex;
				// int firstEnvoi = firstIndex;
				// int numberOfRowsRecu = maxResult;
				// int numberOfRowsEnvoyer = maxResult;
				// if (typeCourrierJour.equals("Tous") &&
				// !vb.getPerson().isBoc()
				// && (countRecu > max || countEnvoi > max)) {
				// if ((countRecu > max && countEnvoi > max)) {
				// if (firstIndex != 0) {
				// firstRecu = firstIndex / 2;
				// firstEnvoi = firstIndex / 2;
				// }
				// numberOfRowsRecu = max;
				// numberOfRowsEnvoyer = max;
				// } else if (countRecu > max && countEnvoi <= max) {
				// if (firstIndex != 0) {
				// firstRecu = (firstIndex / 2) + 2;
				// firstEnvoi = 0;
				// numberOfRowsEnvoyer = 0;
				// numberOfRowsRecu = maxResult;
				// } else {
				// firstEnvoi = 0;
				// numberOfRowsEnvoyer = countEnvoi;
				// numberOfRowsRecu = maxResult - numberOfRowsEnvoyer;
				// }
				//
				// } else if (countEnvoi > max && countRecu <= max) {
				// if (firstIndex != 0) {
				// firstEnvoi = (firstIndex / 2) + 2;
				// firstRecu = 0;
				// numberOfRowsRecu = 0;
				// numberOfRowsEnvoyer = maxResult;
				// } else {
				// firstRecu = 0;
				// numberOfRowsRecu = countRecu;
				// numberOfRowsEnvoyer = maxResult - numberOfRowsRecu;
				// }
				// }
				// }
				Integer courrierRubriqueJourId = Integer
						.valueOf(courrierRubriqueJour);

				vb.setTypeCourrierJourForRapport(typeCourrierJour);
				vb.setTypeCourrierValidationJourForRapport(typeCourrierValidationJour);
				vb.setCourrierRubriqueJour(courrierRubriqueJour);

				// ------------------------ KHA : SOUS TITRE autre BOC
				// --------------------

				System.out
						.println(" KHA : 1: si connectee est un responsable ");
				if (vb.getPerson().isResponsable()) {
					// --------- 1: si connectee est un responsable
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

						System.out.println("result = " + result);

						if (result.endsWith(" -")) {
							sousTitreDeJour = result.substring(0,
									result.length() - 2);
						} else {
							sousTitreDeJour = result;
						}
						System.out.println("===========================");
						System.out.println("sousTitreDeJour = "
								+ sousTitreDeJour);
						System.out.println("===========================");
						vb.setSousTitreRapportResponsable(sousTitreDeJour);
						System.out.println("SousTitreRapportResponsable = "
								+ vb.getSousTitreRapportResponsable());
					}

				} else {
					// --------- 2: si connectee est un Secretaire ou Agent
					sousTitreDeJour = getTypeCourrierListeCourriers(typeCourrierJour);
					vb.setSousTitreRapportSecAgent(sousTitreDeJour);
					System.out.println("SousTitreRapportSecAgent = "
							+ vb.getSousTitreRapportSecAgent());
				}

				// ----------------------------FIN :
				// KHA--------------------------------------------

				// if (typeCourrierJour.equals("Tous")
				// || typeCourrierJour.equals("Envoyes")) {
				// 2014-10-09 commented caused by request changes
				// if (!typeCourrierValidationJour.equals("Avalider")) {
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
						vb.getDbType());
				System.out.println("list courrier jour dure RESPONSIBLE : "
						+ (System.currentTimeMillis() - startTime));
				// 2014-10-09 commented caused by request changes
				for (CourrierInformations courrierInformations : lstCourrierJour) {
					try {

						searchExpediteurDestinataire(courrierInformations);
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("######CAUSED BY : "
								+ courrierInformations.getCourrierReference());
						continue;
					}

				}
				System.out
						.println("list courrier jour dure RESPONSIBLE with Exp & Dest : "
								+ (System.currentTimeMillis() - startTime));
				// } else {
				// lstCourrierEnvoyerJour = new
				// ArrayList<CourrierInformations>();
				// }
				// }
				// if (typeCourrierJour.equals("Tous")
				// || typeCourrierJour.equals("Recu")) {
				// // 2014-10-10 commented caused by request changes
				// lstTrDestinationRecuJour = appMgr
				// .findCourrierRecuByCriteria(vb.getPerson()
				// .isResponsable(), listIdsSousUnit,
				// listIdsSubordonne, filterMap, sortField,
				// descending, consultationCourrierSecretaire,
				// consultationCourrierSubordonne,
				// consultationCourrierSousUnite, 1,
				// dateDebut, dateFin, type, type1,
				// typeSecretaire, idUser, typeTransmission,
				// typeCourrierValidationJour, firstRecu,
				// numberOfRowsRecu, forRapport,
				// courrierRubriqueJourId);
				// // 2014-10-10 commented caused by request changes
				// for (TransactionDestination transactionDestination :
				// lstTrDestinationRecuJour) {
				// lstCourrierRecuJour
				// .add(searchExpediteurDestinataireAndExplodeDataTransactionDestination(transactionDestination));
				// }
				//
				// }
			}
			// if(init == true){
			// listCourrierJour.addAll(lstCourrierEnvoyerJour);
			// listCourrierJour.addAll(lstCourrierRecuJour);
			// }else{
			listCourrierJour.clear();
			listCourrierJour.addAll(lstCourrierJour);
			// if (vb.getPerson().isBoc()) {
			// listCourrierJour.addAll(lstCourrierEnvoyerJour);
			// } else {
			// if (typeCourrierJour.equals("Tous")) {
			// listCourrierJour.addAll(lstCourrierEnvoyerJour);
			// listCourrierJour.addAll(lstCourrierRecuJour);
			// } else if (typeCourrierJour.equals("Recu")) {
			// listCourrierJour.addAll(lstCourrierRecuJour);
			// } else {
			// listCourrierJour.addAll(lstCourrierEnvoyerJour);
			// }
			// }

			return listCourrierJour;
			// }
			// DM
			// listCourrierJourDM.setWrappedData(listCourrierJour);
			// listCourrierDM.setWrappedData(listCourrier);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void searchExpediteurDestinataire(
			CourrierInformations courrierInformations) throws Exception {
		System.out
				.println("********************************************************************");
		System.out.println("AH : DANS searchExpediteurDestinataire ");
		// AH :
		destinatairesAvecAnnotations = new ArrayList<ListeDestinatairesModel>();
		listSelectedItem = new ArrayList<ItemSelected>();
		List<Object> listSelectedObject = new ArrayList<Object>();
		List<Person> listSelectedPerson = new ArrayList<Person>();
		List<Pp> listSelectetdPP = new ArrayList<Pp>();
		List<Pm> listSelectetdPM = new ArrayList<Pm>();
		List<Unit> listSelectetdUnit = new ArrayList<Unit>();
		List<BOC> listSelectetdBoc = new ArrayList<BOC>();

		// get transaction destinataire by id transaction
		TransactionDestination BocSuivant = appMgr
				.getDestinationByIdTransaction(
						courrierInformations.getTransactionID()).get(0);

		// Transaction transaction =
		// appMgr.getListTransactionByIdTransaction(courrierInformations.getTransactionID()).get(0);
		// courrierInformations.setTransaction(transaction);
		// System.out.println("transaction : " +
		// courrierInformations.getTransactionID() + " - courrier : " +
		// courrierInformations.getCourrierID() + " - " +
		// courrierInformations.getCourrierReference());
		Integer etatID = courrierInformations.getEtatID();
		String expType;
		Integer expTypeUser;
		Integer expLdap;
		String expNom;
		String expPrenom;

		// System.out.println("l'id de la personne connceter"+vb.getPerson().getId());
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
		// []
		Transaction transaction = appMgr.getListTransactionByIdTransaction(
				courrierInformations.getTransactionID()).get(0);
		System.out.println("transaction Refere Courrier : "
				+ transaction.getCourrierReferenceCorrespondant());
		courrierInformations.setTransaction(transaction);
		System.out.println("2019-05-21 : "
				+ courrierInformations.getTransaction()
						.getCourrierReferenceCorrespondant());

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

			// Ajouté le 2019-06-29
			// KHA : Seulement le responsable BO peut exécuter
			if (variableExecution.getVaraiablesValeur().equals("Non")) {
				if (vb.getPerson().getAssociatedBOC() != null
						&& courrierInformations.getTransactionOrdre() == null
						&& etatID.equals(5)) {
					System.out.println("2019-05-18 welcome ");
					courrierInformations.setCourrierAValider(1);
				} else

					System.out
							.println(" //[]: test pour que boc execute un courrier de destination interne-Person avec ordre de transaction != null");
				// []: test pour que boc execute un courrier de destination
				// interne-Person avec ordre de transaction != null
				// if(vb.getPerson().getAssociatedBOC() != null &&
				// courrierInformations.getTransactionOrdre() != null &&
				// etatID.equals(5)){
				// KHA : ajouté le test si le connectee est resp BO
				System.out
						.println("Mois => vb.getPerson().getAssociatedBOC()  : "
								+ vb.getPerson().getAssociatedBOC());
				System.out
						.println("Mois => courrierInformations.getTransactionOrdre()  : "
								+ courrierInformations.getTransactionOrdre());
				System.out.println("Mois => etatID  : " + etatID);
				System.out.println("Mois => responsable : "
						+ vb.getPerson().isResponsableBO());
				if (vb.getPerson().getAssociatedBOC() != null
						&& vb.getPerson().isResponsableBO()
						&& courrierInformations.getTransactionOrdre() != null
						&& etatID.equals(5)) {
					System.out.println("-->courrier Interne à executer ");
					System.out.println("hello I'm Here");
					courrierInformations.setCourrierAValider(1);

				}
			}
			// KHA : touls les Membres(Agent/Responsable) de BO peuvent exécuter
			else {
				// [] 2019-05-18 :ajouter condition where BOC connecté a le meme
				// id que le boc qui sera executer courrier

				System.out
						.println("2019-05-18 : vb.getPerson().getAssociatedBOC() : "
								+ vb.getPerson().getAssociatedBOC());
				System.out.println("2019-05-18 : etatID.equals(5) : "
						+ etatID.equals(5));
				System.out
						.println("2019-05-18 : BocSuivant.getTransactionDestIdIntervenant() : "
								+ BocSuivant.getTransactionDestIdIntervenant());

				if (vb.getPerson().getAssociatedBOC() != null
						&& courrierInformations.getTransactionOrdre() != null
						&& etatID.equals(5)
						&& vb.getPerson().getAssociatedBOC().getIdBOC() == BocSuivant
								.getTransactionDestIdIntervenant()) {
					System.out.println("-->courrier Interne à executer ");
					courrierInformations.setCourrierAValider(1);

				}

			}
			// }
			Person person = vb.getHashMapAllUser().get(expLdap);
			System.out.println("person : " + person);
			Person p = vb.getLdapOperation().getPersonalisedUserById(
					person.getId());
			System.out.println("person ================> " + p);
			if (p.isResponsable() || p.isAgent() || p.isSecretary()) {
				System.out.println("===========> "
						+ p.getAssociatedDirection().getShortNameUnit());
				destinataireExpediteur = p.getAssociatedDirection()
						.getShortNameUnit();
				System.out.println("directionPerson : "
						+ destinataireExpediteur);

			}
			System.out.println("person.isboc 2: " + p.isBoc());
			if (p.getAssociatedBOC() != null) {
				System.out.println("person.isboc 3: " + p.isBoc());
				destinataireExpediteur = "BOC";

			}
			expediteur = person.getCn();

			expediteur = person.getCn();

		} else if (expType.equals("Interne-Unité")) {
			if (vb.getPerson().isResponsable()
					&& expLdap.equals(vb.getPerson().getAssociatedDirection()
							.getIdUnit())) {
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
			// Ajouté le 2019-06-22
			if (variableExecution.getVaraiablesValeur().equals("Non")) {

				if (vb.getPerson().getAssociatedBOC() != null
						&& courrierInformations.getTransactionOrdre() == null
						&& etatID.equals(5)) {
					courrierInformations.setCourrierAValider(1);

				} else
					System.out
							.println(" //[]: test pour que boc execute un courrier de destination interne-unité avec ordre de transaction != null");

				// ajouter partie connected person is responsable responsable
				// ==========KHA
				// []: test pour que boc execute un courrier de destination
				// interne-unité avec ordre de transaction != null
				// if(vb.getPerson().getAssociatedBOC() != null &&
				// courrierInformations.getTransactionOrdre() != null &&
				// etatID.equals(5)){
				// KHA : ajouté le test si le connectee est resp BO
				if (vb.getPerson().getAssociatedBOC() != null
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
			// }
			Unit unit = vb.getHashMapUnit().get(expLdap);
			System.out.println("unit : " + unit);
			destinataireExpediteur = unit.getShortNameUnit();
			System.out.println("destinataireExpediteur : "
					+ destinataireExpediteur);
			expediteur = unit.getNameUnit();

		} else if (expType.equals("Interne-Boc")) {
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
			// Ajouté le 2019-06-29
			System.out.println(" DANS execute Interne-Boc");
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

		} else if (expType.equals("Externe")) {

			// C* pour que le bouton executer soit activé pour les courriers
			// d'arrivé
			// provisoire .. juste pour activer l'execution des courrier arrivé
			// pour le BOCT
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
			// provisoire .. juste pour activer l'execution des courrier arrivé
			// pour le BOCT
			// C* pour que le bouton executer soit activé pour les courriers
			// d'arrivé
			if (expTypeUser == 1) {
				expediteur = expNom + " " + expPrenom + " (PP)";
				destinataireExpediteur = "EXT";

			} else {
				expediteur = expNom + " (PM)";
				destinataireExpediteur = "EXT";

			}
		}
		courrierInformations.setCourrierExpediteur(expediteur);
		courrierInformations
				.setCourrierDestinataireReelleDirection(destinataireExpediteur);
		// remplissage de l'objet TransactionDest pour l'execution du BOC ou la
		// validation des responsable
		System.out.println("courrierInformationsgetTransactionID"
				+ courrierInformations.getTransactionID());
		List<TransactionDestination> listTransactionDestination = appMgr
				.getListTransactionDestinationByIdTransaction(courrierInformations
						.getTransactionID());// valeur ancien
		System.out.println("listTransactionDestination="
				+ listTransactionDestination.size()); // #firstTransaction.getTransactionId()#
		if (!listTransactionDestination.isEmpty()) {
			courrierInformations
					.setTransactionDestination(listTransactionDestination
							.get(listTransactionDestination.size() - 1));
		}
		if ((etatID.equals(2) || etatID.equals(10))
				&& !vb.getPerson().isBoc()
				&& !courrierInformations.getIdUtilisateur().equals(
						vb.getPerson().getId())) {
			courrierInformations.setCourrierAValider(1);
			System.out.println("executer 2 controle");
		}
		// pour activer l'execution des courrier qui suit un workflow pour le
		// boct et juste la premiere execution
		if (vb.getPerson().isBoc()
				&& courrierInformations.getCourrierCircuit().equals("workflow")) {
			if (etatID.equals(10)
					&& courrierInformations.getTransactionOrdre().equals(1)) {
				courrierInformations.setCourrierAValider(1);
				System.out.println("executer 3 controle");
			}
		}
		// pour activer l'execution des courrier qui suit un workflow pour le
		// boct et juste la premiere execution
		if (!courrierInformations.getIdUtilisateur().equals(
				vb.getPerson().getId())) {
			courrierInformations.setCourrierRecu(1);
		}

		// detinataire reel *
		StringBuilder destinataire = new StringBuilder("");
		StringBuilder destinataireCourrierReference = new StringBuilder("");

		String unitName;
		// AH
		ListeDestinatairesModel destR;
		// KHA : destinataires reel dans le cas de modification

		List<Transaction> allTransactions = appMgr
				.getTransactionByIdDossier(courrierInformations.getDossierID());

		courrierInformations.setCourrierAllTransactions(allTransactions);

		List<Transaction> allTransactionsByEtat = appMgr
				.getTransactionByIdDossierByEtat(courrierInformations
						.getDossierID());
		courrierInformations
				.setCourrierAllTransactionsByEtat(allTransactionsByEtat);
		System.out.println("size alla transaction by Etat : "
				+ allTransactionsByEtat.size());
		for (Transaction tr : allTransactionsByEtat) {
			System.out.println("2019-06-10 id transaction : "
					+ tr.getTransactionId());
		}
		Transaction firstTransaction = allTransactions.get(allTransactions
				.size() - 1);
		Expdest expdestExpediteurREEL = appMgr.getListExpDestByIdExpDest(
				firstTransaction.getExpdest().getIdExpDest()).get(0);
		courrierInformations.setExpDest(expdestExpediteurREEL);
		if (courrierInformations.getDestReelList() != null) {

			destR = new ListeDestinatairesModel();
			List<String> destReelList = new ArrayList<String>(
					Arrays.asList(courrierInformations.getDestReelList().split(
							"\\|", -1)));
			for (int i = 0; i < destReelList.size(); i++) {
				List<String> destReelElement = new ArrayList<String>(
						Arrays.asList(destReelList.get(i).split(";", -1)));
				// Integer transactionId = 0;
				// if(!destReelElement.get(0).equals("")) {
				// transactionId = Integer.valueOf(destReelElement.get(0));
				// }
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
					idDestReelLdap = Integer.valueOf(destReelElement.get(7));
				}
				String destReelType = destReelElement.get(8);

				if (idDestReelLdap != 0) {
					if (courrierInformations.getCourrierCircuit().equals(
							"workflow")) {
						try {
							Unit unitDestinataireReel = vb.getHashMapUnit()
									.get(idDestReelLdap);
							unitName = unitDestinataireReel.getNameUnit();
							System.out
									.println("==============1===============");
							ItemSelected itemSelected = new ItemSelected();
							itemSelected.setItemSelectedId(idDestReelLdap);
							itemSelected.setItemSelectedName(unitName);
							itemSelected
									.setSelectedObject(unitDestinataireReel);
							listSelectedItem.add(itemSelected);
							//
							Object object = (Object) unitDestinataireReel;
							listSelectedObject.add(object);
							listSelectetdUnit.add(unitDestinataireReel);

							System.out
									.println("============== ===============");
						} catch (Exception e) {
							unitName = "Inconnue";
							e.printStackTrace();
						}
						destinataire.append(" / ");

						destinataire.append(unitName);

						break;
					} else {

						if (destReelType.equals("Interne-Unité")) {

							Unit unit = vb.getHashMapUnit().get(idDestReelLdap);

							if (!destinataire.toString().contains(
									unit.getNameUnit())) {
								destinataire.append(" / ");

								destinataire.append(unit.getNameUnit());

								destR = new ListeDestinatairesModel();
								destR.setDestinataireId(idDestReelLdap);
								destR.setDestinataireName(unit.getNameUnit());
								System.out.println("AH AJOUT "
										+ unit.getNameUnit());
								destinatairesAvecAnnotations.add(destR);
								//
								System.out
										.println("==============2===============");
								ItemSelected itemSelected = new ItemSelected();
								itemSelected.setItemSelectedId(idDestReelLdap);
								itemSelected.setItemSelectedName(unit
										.getNameUnit());
								listSelectedItem.add(itemSelected);

								Object object = (Object) unit;
								listSelectedObject.add(object);
								listSelectetdUnit.add(unit);
								System.out
										.println("============== ===============");
							}
						} else if (destReelType.equals("Interne-Person")) {

							Person person = vb.getHashMapAllUser().get(
									idDestReelLdap);
							if (!destinataire.toString().contains(
									person.getCn())) {
								destinataire.append(" / ");
								destinataire.append(person.getCn());
								destR = new ListeDestinatairesModel();
								destR.setDestinataireId(idDestReelLdap);
								destR.setDestinataireName(person.getCn());
								System.out
										.println("AH AJOUT " + person.getCn());
								destinatairesAvecAnnotations.add(destR);
								System.out
										.println("==============3===============");
								ItemSelected itemSelected = new ItemSelected();
								itemSelected.setItemSelectedId(idDestReelLdap);
								itemSelected
										.setItemSelectedName(person.getCn());
								listSelectedItem.add(itemSelected);

								Object object = (Object) person;
								listSelectedObject.add(object);
								listSelectedPerson.add(person);
								System.out
										.println("============== ===============");
							}

						} else if (destReelType.equals("Externe")) {

							if (vb.getPerson().isBoc() && !etatID.equals(6)) {
								courrierInformations.setCourrierAValider(1);
								System.out.println("executer 4 controle");
							}
							Expdestexterne destReelExterne = appMgr
									.getExpediteurById(idDestReelLdap).get(0);
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
									System.out.println("AH AJOUT " + dest);
									destinatairesAvecAnnotations.add(destR);

									System.out
											.println("==============4===============");
									ItemSelected itemSelected = new ItemSelected();
									itemSelected
											.setItemSelectedId(idDestReelLdap);
									itemSelected.setItemSelectedName(dest);
									listSelectedItem.add(itemSelected);

									System.out
											.println("============== ===============");
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
									System.out.println("AH AJOUT type 2 "
											+ dest);
									destinatairesAvecAnnotations.add(destR);
									System.out
											.println("==============5===============");
									ItemSelected itemSelected = new ItemSelected();
									itemSelected
											.setItemSelectedId(idDestReelLdap);
									itemSelected.setItemSelectedName(dest);
									listSelectedItem.add(itemSelected);
									System.out
											.println("============== ===============");

								}

							}
						} else {
							destinataire.append("--------");
						}
					}

				} else {
					// courrier qui n'a pas des étaps de validation

					if (!listTransactionDestination.isEmpty()) {
						for (TransactionDestination transactionDestination : listTransactionDestination) {
							if (idExpDest.equals(transactionDestination.getId()
									.getIdExpDest())) {

								// destinataire.append(" / ");
								if (type.equals("Interne-Person")) {

									// if (ldap.equals(vb.getPerson().getId()))
									// {

									courrierInformations.setCourrierRecu(1);
									courrierInformations
											.setTransactionDestination(transactionDestination);
									// destinataire = new StringBuilder(" / ");
									Person person = vb.getHashMapAllUser().get(
											ldap);
									if (!destinataire.toString().contains(
											person.getCn())) {
										destinataire.append(" / ");
										destinataire.append(person.getCn());
										destR = new ListeDestinatairesModel();
										destR.setDestinataireId(ldap);
										destR.setDestinataireName(person
												.getCn());
										destinatairesAvecAnnotations.add(destR);
										// break;

										System.out
												.println("==============6===============");
										ItemSelected itemSelected = new ItemSelected();
										itemSelected.setItemSelectedId(ldap);
										itemSelected.setItemSelectedName(person
												.getCn());
										listSelectedItem.add(itemSelected);

										Object object = (Object) person;
										listSelectedObject.add(object);
										listSelectedPerson.add(person);
										System.out
												.println("============== ===============");
									}
									// }
									/*
									 * Person person =
									 * vb.getHashMapAllUser().get( ldap); if
									 * (!destinataire.toString().contains(
									 * person.getCn())) {
									 * destinataire.append(person.getCn()); }
									 */

								} else if (type.equals("Interne-Unité")) {
									System.out.println("ldap=" + ldap);

									if (vb.getPerson().isResponsable()
											&& ldap.equals(vb.getPerson()
													.getAssociatedDirection()
													.getIdUnit())) {
										courrierInformations.setCourrierRecu(1);
										courrierInformations
												.setTransactionDestination(transactionDestination);
										// destinataire = new
										// StringBuilder(" / ");
										Unit unit = vb.getHashMapUnit().get(
												ldap);
										if (!destinataire.toString().contains(
												unit.getNameUnit())) {
											destinataire.append(" / ");
											destinataire.append(unit
													.getNameUnit());
											destR = new ListeDestinatairesModel();
											destR.setDestinataireId(ldap);
											destR.setDestinataireName(unit
													.getNameUnit());
											destinatairesAvecAnnotations
													.add(destR);
											System.out
													.println("==============7===============");
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
											System.out
													.println("============== ===============");
											// break;
										}

									}
									// Unit unit =
									// vb.getHashMapUnit().get(ldap);

								} else if (type.equals("Interne-Boc")) {

									if (vb.getPerson().isBoc()) {
										courrierInformations.setCourrierRecu(1);
										courrierInformations
												.setTransactionDestination(transactionDestination);
									}
									if (!destinataire.toString().contains(
											vb.getCentralBoc().getNameBOC())) {
										destinataire.append(" / ");
										destinataire.append(vb.getCentralBoc()
												.getNameBOC());
										destR = new ListeDestinatairesModel();
										destR.setDestinataireId(ldap);
										destR.setDestinataireName(vb
												.getCentralBoc().getNameBOC());
										destinatairesAvecAnnotations.add(destR);

										System.out
												.println("==============8===============");
										ItemSelected itemSelected = new ItemSelected();
										itemSelected.setItemSelectedId(ldap);
										itemSelected.setItemSelectedName(vb
												.getCentralBoc().getNameBOC());
										listSelectedItem.add(itemSelected);

										System.out
												.println("============== ===============");
									}
								} else if (type.equals("Externe")) {

									if (typeUser.equals(1)) {
										if (!destinataire.toString().contains(
												nom + " " + prenom + " (PP)")) {
											destinataire.append(" / ");
											destinataire.append(nom + " "
													+ prenom + " (PP)");
											destR = new ListeDestinatairesModel();
											destR.setDestinataireId(ldap);
											destR.setDestinataireName(nom + " "
													+ prenom + " (PP)");
											destinatairesAvecAnnotations
													.add(destR);
											System.out
													.println("==============9===============");
											ItemSelected itemSelected = new ItemSelected();
											itemSelected
													.setItemSelectedId(ldap);
											itemSelected
													.setItemSelectedName(nom
															+ " " + prenom);
											listSelectedItem.add(itemSelected);

											System.out
													.println("============== ===============");

										}
									} else {
										if (!destinataire.toString().contains(
												nom + " (PM)")) {
											destinataire.append(" / ");
											destinataire.append(nom + " (PM)");
											destR = new ListeDestinatairesModel();
											destR.setDestinataireId(ldap);
											destR.setDestinataireName(nom
													+ " (PM)");
											destinatairesAvecAnnotations
													.add(destR);

											System.out
													.println("==============10===============");
											ItemSelected itemSelected = new ItemSelected();
											itemSelected
													.setItemSelectedId(ldap);
											itemSelected
													.setItemSelectedName(nom
															+ " " + prenom);
											listSelectedItem.add(itemSelected);
											System.out
													.println("============== ===============");
										}
									}
								}
							}
						}
					}
					if (courrierInformations.getCourrierRecu() == 1
							&& (etatID.equals(10) || etatID.equals(2))) {
						courrierInformations.setCourrierAValider(1);
						System.out.println("executer 5 controle");
					} else {
						// provisoire .. juste pour activer l'execution des
						// courrier arrivé pour le BOCT
						if (courrierAriverToDG.getVaraiablesValeur().equals(
								"Non")) {
							if (!vb.getPerson().isBoc()) {
								courrierInformations.setCourrierAValider(0);
								System.out.println("Non executer 1 controle");
							}
						}
						// provisoire .. juste pour activer l'execution des
						// courrier arrivé pour le BOCT
					}
					if (vb.getPerson().getAssociatedBOC() != null
							&& courrierInformations.getCourrierRecu() == 1
							&& etatID.equals(5) && etatID.equals(2)) {
						courrierInformations.setCourrierAValider(1);
						System.out.println("executer 6 controle");
					}
				}
				// KHA =========
				courrierInformations.setListSelectedItemDest(listSelectedItem);
				courrierInformations.setListSelectedObject(listSelectedObject);
				courrierInformations.setListSelectedPerson(listSelectedPerson);
				courrierInformations.setListSelectetdUnit(listSelectetdUnit);
				courrierInformations.setListSelectetdBoc(listSelectetdBoc);
				// ========
				// ========
			}
		}
		destinataire.delete(0, 3);

		System.out.println("AH DESTINATAIRE " + destinataire);
		System.out
				.println("AH : Liste des destinataires:====================================");

		for (ListeDestinatairesModel d : destinatairesAvecAnnotations) {
			System.out.println(d.getDestinataireName());
		}
		courrierInformations.setCourrierDestinataireReelle(destinataire
				.toString());
		courrierInformations
				.setListeDestinatairesAvecAnnotations(destinatairesAvecAnnotations);
	}

	public void getSelectionRowJour() {
		try {
			vb.setSelectedListCourrier("CRjour");
			vb.setDestinataireReel("");
			Transaction transaction = new Transaction();
			CourrierInformations consulterInformations = selectedCourrier;
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
				System.out.println("CopyListSelectedUnit size = "
						+ vb.getCopyListSelectedUnit().size());
			}
			if (consulterInformations.getListSelectetdBoc() != null) {
				vb.setCopyListSelectedBoc(consulterInformations
						.getListSelectetdBoc());
				System.out.println("CopyListSelectedBoc size = "
						+ vb.getCopyListSelectedBoc().size());
			}
			System.out
					.println("***********************************************************");
			vb.setCourDossConsulterInformations(consulterInformations);// a
																		// commenté
																		// si on
																		// a
																		// renversé
																		// l'ancienne
																		// liste
																		// de
																		// courriers
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
							if (vb.getPerson().isBoc()
									&& expdest.getTypeExpDest().equals(
											"Interne-Boc")) {
								if (transactionDestination
										.getTransactionDestDateConsultation() == null) {
									// transactionDestination
									// .setTransactionDestDateConsultation(new
									// Date());
									// System.out.println("********* : "+transactionDestination.getTransactionDestDateConsultation());
									// System.out.println("***new Date()****** : "+new
									// Date());
									appMgr.update(transactionDestination);
								}
							}
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
			} else {
				if (consulterInformations.getTransactionDestination() == null) {
				} else

				if (consulterInformations.getCourrierRecu() == 1
						&& consulterInformations.getTransactionDestination()
								.getTransactionDestDateConsultation() == null) {
					System.out.println("entrer dans condition if ");
					TransactionDestination transactionDestination = new TransactionDestination();
					transactionDestination = consulterInformations
							.getTransactionDestination();
					transactionDestination
							.setTransactionDestDateConsultation(new Date());
					appMgr.update(transactionDestination);
					vb.setTransactionDestination(transactionDestination);
					// chargement variable log & notification
					chargementNotification(consulterInformations);
				} else if (transaction.getIdUtilisateur() == vb.getPerson()
						.getId()
						&& transaction.getTransactionDateConsultation() == null) {
					transaction.setTransactionDateConsultation(new Date());
					appMgr.update(transaction);
				}
			}
			System.out.println("");
			vb.setCopyDestNom(consulterInformations
					.getCourrierDestinataireReelle());
			System.out.println();
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
			System.out.println("JOUR 2019-06-25");
			vb.setReferenceDestinataireReel(consulterInformations
					.getReferenceDestinataireReelle());

			// AH

			vb.setListeDestinataire(consulterInformations
					.getListeDestinatairesAvecAnnotations());

			// AH : Ajouter une requete pour extraire la liste des annotation
			// par destinataire
			for (ListeDestinatairesModel d : consulterInformations
					.getListeDestinatairesAvecAnnotations()) {
				System.out.println(d.getDestinataireName());
			}

			// ** expediteur reel

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
			/*
			 * LogClass logClass = new LogClass(); logClass.addTrack(
			 * "consultation", "Evénement de log de consultation du courrier " +
			 * courrier.getIdCourrier() + "-" +
			 * courrier.getCourrierReferenceCorrespondant(), vb.getPerson(),
			 * "INFO", appMgr);
			 */
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
			vb.setCourDossConsulterInformations(courrierInformations);// a
																		// commenter
																		// si on
																		// a
																		// renversé
																		// l'ancienne
																		// liste
																		// de
																		// courriers
			courrier = courrierInformations.getCourrier();
			vb.setCourrier(courrier);
			transaction = courrierInformations.getTransaction();
			// if (lstCourrierRecuJour.contains(courrierInformations)
			// && courrierInformations.getTransactionDestination()
			// .getTransactionDestDateConsultation() == null) {
			// TransactionDestination transactionDestination = new
			// TransactionDestination();
			// transactionDestination = courrierInformations
			// .getTransactionDestination();
			// transactionDestination
			// .setTransactionDestDateConsultation(new Date());
			// appMgr.update(transactionDestination);
			// vb.setTransactionDestination(transactionDestination);
			// } else if (lstCourrierEnvoyerJour.contains(courrierInformations)
			// && transaction.getTransactionDateConsultation() == null) {
			// transaction.setTransactionDateConsultation(new Date());
			// appMgr.update(transaction);
			// }
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
			// ** expediteur reel
			vb.setCopyExpReelNom(courrierInformations.getCourrierExpediteur());
			// ** destinataire reel
			vb.setDestinataireReel(courrierInformations
					.getCourrierDestinataireReelle());

			// **
			// int refDossier = transaction.getDossier().getDossierId();
			// List<Transaction> listTransaction = appMgr
			// .getTransactionByIdDossier(refDossier);
			// Transaction firstTransaction =
			// listTransaction.get(listTransaction
			// .size() - 1);
			// Expdest expDest;
			// expDest = firstTransaction.getExpdest();
			// if (expDest.getTypeExpDest().equals("Interne-Person")) {
			// vb.setCopyExpReelNom(ldapOperation.getCnById(
			// ldapOperation.CONTEXT_USER, "uid",
			// expDest.getIdExpDestLdap()));
			// } else if (expDest.getTypeExpDest().equals("Interne-Unité")) {
			//
			// vb.setCopyExpReelNom(ldapOperation.getUnitById(
			// expDest.getIdExpDestLdap()).getShortNameUnit());
			//
			// } else if (expDest.getTypeExpDest().equals("Interne-Boc")) {
			// vb.setCopyExpReelNom(ldapOperation.getBocById(
			// expDest.getIdExpDestLdap()).getNameUnit());
			// } else if (expDest.getTypeExpDest().equals("Externe")) {
			// if (expDest.getExpdestexterne().getTypeutilisateur()
			// .getTypeUtilisateurLibelle().equals("PP")) {
			// vb.setCopyExpReelNom(expDest.getExpdestexterne()
			// .getExpDestExterneNom()
			// + " "
			// + expDest.getExpdestexterne()
			// .getExpDestExternePrenom() + " (PP)");
			// } else {
			// vb.setCopyExpReelNom(expDest.getExpdestexterne()
			// .getExpDestExterneNom() + " (PM)");
			// }
			// }
			// **
			// if (consulterInformations.getTransaction()
			// .getTransactionDestinationReelle() != null) {
			// TransactionDestinationReelle transactionDestinationReelle = new
			// TransactionDestinationReelle();
			// transactionDestinationReelle = consulterInformations
			// .getTransaction().getTransactionDestinationReelle();
			// Expdest expDest = new Expdest();
			// Transaction transaction1 = new Transaction();
			// transaction1 = appMgr
			// .getTransactionByIdTransactionAndFirstOrder(
			// transactionDestinationReelle
			// .getTransactionDestinationReelleId(),
			// 1).get(0);
			// expDest = transaction1.getExpdest();
			// if (expDest.getTypeExpDest().equals("Interne-Person")) {
			// vb.setCopyExpReelNom(ldapOperation.getCnById(
			// ldapOperation.CONTEXT_USER, "uid",
			// expDest.getIdExpDestLdap()));
			// } else if (expDest.getTypeExpDest().equals("Interne-Unité")) {
			// vb.setCopyExpReelNom(ldapOperation.getCnById(
			// ldapOperation.CONTEXT_UNIT, "departmentNumber",
			// expDest.getIdExpDestLdap()));
			// }
			// } else {
			// vb.setCopyExpReelNom(vb.getCopyExpNom());
			// }
			// **
			vb.setCopyDestNom(courrierInformations
					.getCourrierDestinataireReelle());
			vb.setCopyExpNom(courrierInformations.getCourrierExpediteur());
			// **
			// vb.setCopyCourrierCommentaire(consulterInformations.getCourrierCommentaire());
			vb.setCopyCourrierCommentaire(courrierInformations
					.getCourrierCommentaire());
			vb.setCopyOtherDest(courrierInformations
					.getCourrierAutreDestinataires());
			vb.setTransaction(transaction);

			// destinataire reel
			// C*
			// if(transaction.getTransactionDestinationReelle() != null){
			// TransactionDestinationReelle transactionDestinationReelle =
			// appMgr.getTransactionDestinationReelById(transaction.getTransactionDestinationReelle().getTransactionDestinationReelleId());
			// if(transactionDestinationReelle != null){
			// if (courrier.getCourrierCircuit().equals("workflow")) {
			// Unit unitDestinataireReel =
			// ldapOperation.getUnitById(transactionDestinationReelle.getTransactionDestinationReelleIdDestinataire());
			// vb.setDestinataireReel(unitDestinataireReel.getNameUnit());
			// }else{
			// if(transactionDestinationReelle.getTransactionDestinationReelleTypeDestinataire().equals("Interne-Unité")){
			// Unit unit = vb.getHashMapUnit().get(
			// transactionDestinationReelle.getTransactionDestinationReelleIdDestinataire());
			// vb.setDestinataireReel(unit.getNameUnit());
			// }else
			// if(transactionDestinationReelle.getTransactionDestinationReelleTypeDestinataire().equals("Interne-Person")){
			// Person person = vb.getHashMapAllUser().get(
			// transactionDestinationReelle.getTransactionDestinationReelleIdDestinataire());
			// vb.setDestinataireReel(person.getCn());
			// }else
			// if(transactionDestinationReelle.getTransactionDestinationReelleTypeDestinataire().equals("Externe")){
			// Expdestexterne destReelExterne =
			// appMgr.getExpediteurById(transactionDestinationReelle.getTransactionDestinationReelleIdDestinataire()).get(0);
			// if(destReelExterne.getTypeutilisateur().equals(1)){
			// vb.setDestinataireReel(destReelExterne.getExpDestExternePrenom()
			// + " " + destReelExterne.getExpDestExterneNom());
			// }else if (destReelExterne.getTypeutilisateur().equals(2)) {
			// vb.setDestinataireReel(destReelExterne.getExpDestExterneNom());
			// }
			// }else{
			// vb.setDestinataireReel("--------------------------------");
			// }
			// }
			// }
			// }else{
			// vb.setDestinataireReel(vb.getCopyDestNom());
			// }
			// C*
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
			vb.setCourDossConsulterInformations(courrierInformations);// a
																		// commenté
																		// si on
																		// a
																		// renversé
																		// l'ancienne
																		// liste
																		// de
																		// courriers
			vb.setCourrier(courrier);
			transaction = courrierInformations.getTransaction();
			// if (lstCourrierRecuJour.contains(courrierInformations)
			// && courrierInformations.getTransactionDestination()
			// .getTransactionDestDateConsultation() == null) {
			// TransactionDestination transactionDestination = new
			// TransactionDestination();
			// transactionDestination = courrierInformations
			// .getTransactionDestination();
			// transactionDestination
			// .setTransactionDestDateConsultation(new Date());
			// appMgr.update(transactionDestination);
			// vb.setTransactionDestination(transactionDestination);
			// } else if (lstCourrierEnvoyerJour.contains(courrierInformations)
			// && transaction.getTransactionDateConsultation() == null) {
			// transaction.setTransactionDateConsultation(new Date());
			// appMgr.update(transaction);
			// }
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
			vb.setCourDossConsulterInformations(courrierInformations);// a
																		// commenté
																		// si on
																		// a
																		// renversé
																		// l'ancienne
																		// liste
																		// de
																		// courriers
			vb.setCourrier(courrier);
			transaction = courrierInformations.getTransaction();
			// if (lstCourrierRecuJour.contains(courrierInformations)
			// && courrierInformations.getTransactionDestination()
			// .getTransactionDestDateConsultation() == null) {
			// TransactionDestination transactionDestination = new
			// TransactionDestination();
			// transactionDestination = courrierInformations
			// .getTransactionDestination();
			// transactionDestination
			// .setTransactionDestDateConsultation(new Date());
			// appMgr.update(transactionDestination);
			// vb.setTransactionDestination(transactionDestination);
			// } else if (lstCourrierEnvoyerJour.contains(courrierInformations)
			// && transaction.getTransactionDateConsultation() == null) {
			// transaction.setTransactionDateConsultation(new Date());
			// appMgr.update(transaction);
			// }
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
			// if (courrierInformations.getCourrierExpediteurObjet() instanceof
			// Person) {
			// Person person = new Person();
			// person = (Person) courrierInformations
			// .getCourrierExpediteurObjet();
			// vb.setCopyListSelectedPerson(new ArrayList<Person>());
			// vb.getCopyListSelectedPerson().add(
			// ldapOperation.getPersonalisedUserById(person.getId()));
			// vb.setDestNom(person.getCn());
			// } else if (courrierInformations.getCourrierExpediteurObjet()
			// instanceof Unit) {
			// Unit unit = new Unit();
			// unit = (Unit) courrierInformations.getCourrierExpediteurObjet();
			// vb.setCopyListSelectedUnit(new ArrayList<Unit>());
			// vb.getCopyListSelectedUnit().add(unit);
			// vb.setDestNom(unit.getNameUnit());
			// }
			vb.setCopyDestNom(courrierInformations
					.getCourrierDestinataireReelle());
			vb.setCopyExpNom(courrierInformations.getCourrierExpediteur());
			vb.setCopyCourrierCommentaire(courrierInformations
					.getCourrierCommentaire());
			vb.setToReplay(true);

			// C* seach the real expediteur :
			if (courrierInformations.getCourrierAllTransactions() == null) {
				List<Transaction> allTransactions = appMgr
						.getTransactionByIdDossier(transaction.getDossier()
								.getDossierId());
				courrierInformations
						.setCourrierAllTransactions(allTransactions);
			}
			Transaction firstTransaction = courrierInformations
					.getCourrierAllTransactions().get(
							courrierInformations.getCourrierAllTransactions()
									.size() - 1);
			Expdest realExpediteur = appMgr.getListExpDestByIdExpDest(
					firstTransaction.getExpdest().getIdExpDest()).get(0);
			courrierInformations.setExpDest(realExpediteur);
			if (realExpediteur.getTypeExpDest().equals("Interne-Person")) {
				// Person person =
				// vb.getHashMapAllUser().get(courrierInformations.getExpDest().getIdExpDestLdap());

				Person person = ldapOperation
						.getPersonalisedUserById(courrierInformations
								.getExpDest().getIdExpDestLdap());
				vb.getCopyListSelectedPerson().add(person);
				vb.setDestNom(person.getCn());
				// *** kha- ajoute 11-02-2019
				vb.setDestinataireId(person.getId());
				ListeDestinatairesModel dest = new ListeDestinatairesModel();
				dest.setDestinataireId(person.getId());
				dest.setDestinataireName(person.getCn());
				destinataireRepondre.add(dest);
				vb.setListeDestinataire(destinataireRepondre);
				// ***
			} else if (realExpediteur.getTypeExpDest().equals("Interne-Unité")) {
				Unit unit = vb.getHashMapUnit().get(
						courrierInformations.getExpDest().getIdExpDestLdap());

				vb.getCopyListSelectedUnit().add(unit);
				vb.setDestNom(unit.getNameUnit());
				// *** kha- ajoute 11-02-2019
				vb.setDestinataireId(unit.getIdUnit());
				ListeDestinatairesModel dest = new ListeDestinatairesModel();
				dest.setDestinataireId(unit.getIdUnit());
				dest.setDestinataireName(unit.getNameUnit());

				destinataireRepondre.add(dest);
				vb.setListeDestinataire(destinataireRepondre);
				// ****
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
					// *** kha- ajoute 11-02-2019
					vb.setDestinataireId(pp.getExpdestexterne()
							.getIdExpDestExterne());
					ListeDestinatairesModel dest = new ListeDestinatairesModel();
					dest.setDestinataireId(pp.getExpdestexterne()
							.getIdExpDestExterne());
					dest.setDestinataireName(pp.getExpdestexterne()
							.getExpDestExterneNom());

					destinataireRepondre.add(dest);
					vb.setListeDestinataire(destinataireRepondre);
					// /****
				} else {
					Pm pm = appMgr.getPMByReferenceExpediteur(
							realExpediteurExterne.getIdExpDestExterne()).get(0);

					pm.setExpdestexterne(realExpediteurExterne);
					// pm.setExpdestexterne(expdestexterne)
					vb.getCopyListPM().add(pm);
					vb.setDestNom(realExpediteur.getExpdestexterne()
							.getExpDestExterneNom());
					// *** kha- ajoute 11-02-2019
					vb.setDestinataireId(pm.getExpdestexterne()
							.getIdExpDestExterne());
					ListeDestinatairesModel dest = new ListeDestinatairesModel();
					dest.setDestinataireId(pm.getExpdestexterne()
							.getIdExpDestExterne());
					dest.setDestinataireName(pm.getExpdestexterne()
							.getExpDestExterneNom());
					destinataireRepondre.add(dest);
					vb.setListeDestinataire(destinataireRepondre);
					// ****
				}
			}
			for (ListeDestinatairesModel d : destinataireRepondre) {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void executeJour() {
		System.out.println("AH :: Dans executeJour");
		Courrier courrier = new Courrier();
		courrierInformations = selectedCourrier;
		if (courrierInformations.getCourrier() == null) {
			courrierInformations.setCourrier(appMgr.getCourrierByIdCourrier(
					courrierInformations.getCourrierID()).get(0));
		}
		courrier = courrierInformations.getCourrier();
		Etat etat = new Etat();
		etat = appMgr.listEtatByRef(courrierInformations.getEtatID()).get(0);
		if (etat.getEtatLibelle().equals("Faire suivre")) {
			String circuitCourrier = courrier.getCourrierCircuit();
			if (circuitCourrier.equals("workflow")) {
				int refNature = courrier.getNature().getNatureId();
				int etatActuelle = courrier.getCourrierEtatActuelleWorkflow();
				Workflow workflow = new Workflow();
				int nb = appMgr.listWorkflowByIdNature(refNature).size();
				if (nb != 0) {
					/********* WorkFlow Request ********/
					workflow = appMgr.listWorkflowByIdNature(refNature).get(0);
					String processId = workflow.getWorkflowTitre();
					int idLastNode = workflow.getWorkflowIdLastNode();
					if (etatActuelle == idLastNode) {
						if (courrierInformations.getTransaction() == null) {
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
						if (courrierInformations.getTransaction() == null) {
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
								.startProcessTraitementCourrier(processId,
										etatActuelle);
						List<TransactionDestination> listTrDest = appMgr
								.getListTransactionDestinationByIdTransaction(courrierInformations
										.getTransactionID());
						validateWorkflow(etapeSuivant,
								courrierInformations.getTransaction(),
								listTrDest.get(listTrDest.size() - 1), courrier);
					}
				}
			}
		} else {
			// C*
			for (Transaction transaction : courrierInformations
					.getCourrierAllTransactions()) {
				System.out.println("####" + transaction.getTransactionId());
				TransactionDestinationReelle destinataionReel = appMgr
						.getTransactionDestinationReelById(transaction
								.getTransactionDestinationReelle()
								.getTransactionDestinationReelleId());

				if (destinataionReel != null) {
					if (!destinataionReel
							.getTransactionDestinationReelleTypeDestinataire()
							.equals("Externe")) {
						// c'est un courrier d'arrivé depuis l'exterieur (PP ou
						// PM) vers l'interne, donc il faut l'executer pour que
						// le courrier s'entre dans le circuit de validation
						// hierarchique
						// ( validation hierarchique depend de la variable de
						// parametrage
						// #passage_hierarchique_courrier_arrive_apres_directeur_generale#
						validateTransactionToDestinationReel(transaction,
								destinataionReel);
						// if(destinataionReel.getTransactionDestinationReelleResponsableReponse()
						// != null){
						// validateTransactionToDestinationReel(selectedCourrier.getTransaction(),
						// destinataionReel);
						// }else{
						// setShowResponsableResponse(true);
						// }
					} else {
						
						// c'est un courrier d'un personne ou unité interne vers
						// l'exterieur, juste il faut l'executer pour ajouter la
						// transaction de depart d'un courrier
						executeOneTransaction(courrierInformations);
						break;
					}
				}
			}
			// C*
			// executeOneTransaction(courrierInformations); // commenté a cause
			// de // C*
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

	private void validateTransactionToDestinationReel(Transaction transaction,
			TransactionDestinationReelle trDestinationReelle) {
		System.out.println("AH DANS validateTransactionToDestinationReel");
		try {
			//v12 : validation_hierarchique_courrier_arrive
			//v13 : envoie_courrier_arrive_directeur_generale
			
		//	Variables variable = appMgr.listVariablesById(12).get(0);
			Variables variable = appMgr.listVariablesByLibelle("validation_hierarchique_courrier_arrive").get(0);
			
			//Variables variableToDGEN = appMgr.listVariablesById(13).get(0);

			Variables variableToDGEN = appMgr.listVariablesByLibelle("envoie_courrier_arrive_directeur_generale").get(0);
			boolean passageDGEN = false;
			Person generalDirector = null;
			Unit generalDirectorUnit = null;
			if (variableToDGEN.getVaraiablesValeur().equals("Oui")) {
				passageDGEN = true;
				// special pour le BOC dans le cas ou la vlalidation ne passe
				// pas par le DGEN pour la fonction #findIdDestinataireSuivant#
				generalDirectorUnit = ldapOperation.getUnitById(1);
				generalDirector = generalDirectorUnit.getResponsibleUnit();
				// special pour le BOC dans le cas ou la vlalidation ne passe
				// pas par le DGEN pour la fonction #findIdDestinataireSuivant#
			} else {
				// special pour le BOC dans le cas ou la vlalidation ne passe
				// pas par le DGEN pour la fonction #findIdDestinataireSuivant#
				generalDirectorUnit = ldapOperation.getUnitById(1);
				if(generalDirectorUnit!=null)
				generalDirector = generalDirectorUnit.getResponsibleUnit();
				// special pour le BOC dans le cas ou la vlalidation ne passe
				// pas par le DGEN pour la fonction #findIdDestinataireSuivant#
			}
			status1 = false;
			status2 = false;
			TransactionDestination transactionDestination = selectedCourrier
					.getTransactionDestination();
			if (variable.getVaraiablesValeur().equals("Oui")) {
				Integer idDestinataireReel = trDestinationReelle
						.getTransactionDestinationReelleIdDestinataire();
				if (trDestinationReelle
						.getTransactionDestinationReelleTypeDestinataire()
						.equals("Interne-Person")) {
					// le destinataire est une personne
					if (idDestinataireReel != generalDirector.getId()) {
						Integer idDestinataireSuivant = findIdDestinataireSuivant(
								idDestinataireReel, vb.getPerson().getId(),
								true, passageDGEN, generalDirector);
						System.out.println(idDestinataireSuivant);
						if (idDestinataireSuivant.equals(idDestinataireReel)) {
							validateTransactionDestinataireFinale(transaction,
									transactionDestination);
						} else {
							validateTransactionDestinataireSuivant(transaction,
									transactionDestination,
									idDestinataireSuivant);
						}
					} else {
						validateTransactionDestinataireFinale(transaction,
								transactionDestination);
					}
				} else if (trDestinationReelle
						.getTransactionDestinationReelleTypeDestinataire()
						.equals("Interne-Unité")) {
					if (idDestinataireReel != generalDirectorUnit.getIdUnit()) {
						Integer idDestinataireSuivant = findIdDestinataireSuivant(
								idDestinataireReel, vb.getPerson().getId(),
								false, passageDGEN, generalDirector);
						System.out.println("AH :: idDestinataireSuivant :: "+idDestinataireSuivant);
						Unit unit = ldapOperation
								.getUnitById(idDestinataireReel);
						if (idDestinataireSuivant.equals(unit
								.getResponsibleUnit().getId())) {
							validateTransactionDestinataireFinale(transaction,
									transactionDestination);
						} else {
							validateTransactionDestinataireSuivant(transaction,
									transactionDestination,
									idDestinataireSuivant);
						}
					} else {
						validateTransactionDestinataireFinale(transaction,
								transactionDestination);
					}
				}
			} else {
				System.out.println("-->Variable Passage par DG :"
						+ variableToDGEN.getVaraiablesValeur());
				System.out.println("-->Variable Passage héarchique :"
						+ variable.getVaraiablesValeur());
				System.out.println("Valeur de la variable executé :"
						+ vb.isExecute());
				if (vb.isExecute()) {
					System.out
							.println("Méthode de validation un courrier interne");
					validateTransactionDestinataireFinale(transaction,
							transactionDestination);
				} else {
					System.out
							.println("Methode d'execution un courrier interne par BO");
					// [] Methode qui permet d'execute un courrier de
					// destination interne par BO
					executeTransactionInterne(transaction, courrierInformations);
					System.out.println("2019-05-28 : "
							+ courrierInformations.getTransactionID());
					System.out.println("2019-05-29 : "
							+ transaction.getTransactionId());

				}
			}
			try {
				if (transactionDestination != null) {
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
			status1 = true;
		} catch (Exception e) {
			status2 = true;
			e.printStackTrace();
		}
	}

	private void validateTransactionDestinataireSuivant(
			Transaction transaction,
			TransactionDestination transactionDestination,
			Integer idDestinataireSuivant) throws Exception {
		System.out.println("AH :: DANS validateTransactionDestinataireSuivant ");
		List<TransactionAnnotation> transactionAnnotation = appMgr
				.getAnnotationByIdTransaction(transaction.getTransactionId());
		newTransaction = new Transaction();
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
		newTransaction = new Transaction();
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
		Calendar cal = Calendar.getInstance();
		cal.setTime(courrierInformations.getCourrierDateReceptionEnvoi());
		int year = cal.get(Calendar.YEAR);
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

	private void executeOneTransaction(CourrierInformations courrierInformations) {
		System.out.println("DANS  executeOneTransaction");
		if (courrierInformations.getCourrier() == null) {
			courrierInformations.setCourrier(appMgr.getCourrierByIdCourrier(
					courrierInformations.getCourrierID()).get(0));
		}
		if (courrierInformations.getTransaction() == null) {
			courrierInformations.setTransaction(appMgr
					.getListTransactionByIdTransaction(
							courrierInformations.getTransactionID()).get(0));
		}
		setStatus1(false);
		setStatus2(false);
		Transaction transaction = new Transaction();
		transaction = courrierInformations.getTransaction();
		Transaction newTransaction;
		Transaction transactionExpediteur;
		List<Transaction> listTransaction = new ArrayList<Transaction>();
		try {
			if (transaction.getTransactionDestinationReelle() != null) {
				newTransaction = new Transaction();
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

				// mettre a jour la reference pour indiquer qu'il est un
				// courrier de depart
				Integer lastId = appMgr.getCourrierLastIdByTypeOrdreAndAnnee(
						"A", year);
				courrierInformations.getCourrier().setCourrierType("A");
				if (lastId == null || lastId == 0) {
					courrierInformations.getCourrier().setCourrierTypeOrdre(1);
				} else {
					courrierInformations.getCourrier().setCourrierTypeOrdre(
							lastId + 1);
				}
				courrierInformations.getCourrier()
						.setCourrierReferenceCorrespondant(
								courrierInformations.getCourrier()
										.getCourrierType()
										+ courrierInformations.getCourrier()
												.getCourrierTypeOrdre());
				// courrierInformations.getCourrier().setCourrierReferenceCorrespondant("D"+courrierInformations.getCourrier().getIdCourrier());
				appMgr.update(courrierInformations.getCourrier());
				Dossier dossier = appMgr.getDossierByIdDossier(
						transaction.getDossier().getDossierId()).get(0);
				dossier.setDossierIntitule("Courrier_"
						+ courrierInformations.getCourrier().getIdCourrier());
				appMgr.update(dossier);
				// mettre a jour la reference pour indiquer qu'il est un
				// courrier de depart
				// mettre a jour la colonne #courrierDateReponseSysteme# pour
				// indiquer que le courrier originale a été repondu
				// trouvez le courrier original
				List<Lienscourriers> liensCourriers = appMgr
						.getListCourrierLiensByIdCourrier(courrierInformations
								.getCourrier().getIdCourrier());
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
					}
					}
				}
				// trouver le courrier original
				// mettre a jour la colonne #courrierDateReponseSysteme# pour
				// indiquer que le courrier originale a été repondu
				// inséré la date de traitement
				TransactionDestination trDestination = courrierInformations
						.getTransactionDestination();
				System.out.println("trDestination"
						+ trDestination.getTransactionDestTypeIntervenant());
				if (trDestination.getTransactionDestDateTransfert() == null) {

					trDestination.setTransactionDestDateTransfert(new Date());
					appMgr.update(trDestination);
				}
				// inséré la date de traitement
				// inséré la date de consultation
				if (trDestination.getTransactionDestDateConsultation() == null) {
					trDestination
							.setTransactionDestDateConsultation(new Date());
					appMgr.update(trDestination);
				}
				// inséré la date de consultation
			}
			setStatus1(true);
		} catch (Exception e) {
			setStatus2(true);
			e.printStackTrace();
		}
	}

	public void validerFinProcessus(Transaction transaction) {
		setStatus1(false);
		setStatus2(false);
		try {
			Etat etat = new Etat();
			etat = appMgr.listEtatByLibelle("Validé").get(0);
			transaction.setEtat(etat);
			transaction.setTransactionDateReponse(new Date());
			appMgr.update(transaction);
			setStatus1(true);
		} catch (Exception e) {
			setStatus2(true);
			e.printStackTrace();
		}
	}

	public void validateWorkflow(TraitementEtapeSuivant etapeSuivant,
			Transaction transaction,
			TransactionDestination transactionDestination, Courrier courrier) {
		setStatus1(false);
		setStatus2(false);
		try {
			if (transactionDestination != null) {
				if (transactionDestination.getTransactionDestDateConsultation() == null) {
					transactionDestination
							.setTransactionDestDateConsultation(new Date());
				}
			}
			transactionDestination.setTransactionDestDateTransfert(new Date());
			appMgr.update(transactionDestination);
			newTransaction = new Transaction();
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
			setStatus1(true);

		} catch (Exception e) {
			setStatus2(true);
			e.printStackTrace();
		}
	}

	private void getIdBocByUnit(Unit unit) {
		System.out.println("unit // : " + unit);
		System.out.println("Associate Unit //  : " + unit.getAssociatedUnit());
		if (unit.getAssociatedUnit() != null && unit.getIdUnit() != null) {
			getIdBocByUnit(unit.getAssociatedUnit());
			System.out.println("dans <> null");
		} 
		else if(unit.getAssociatedBOC()!=null){
			idBoc = unit.getAssociatedBOC().getIdBOC();
			unitSup = unit;
			System.out.println("première Unité sous un BOC : " + unit);
			System.out.println("id BOC :" + idBoc);
		}
		else{
			System.out.println("DANS else unit.getAssociatedUnit() != null && unit.getAssociatedBOC()!=null ");
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

		if (consulterInformations.getCourrier() == null) {
			consulterInformations.setCourrier(appMgr.getCourrierByIdCourrier(
					consulterInformations.getCourrierID()).get(0));
		}
		if (consulterInformations.getTransaction() == null) {
			consulterInformations.setTransaction(appMgr
					.getListTransactionByIdTransaction(
							consulterInformations.getTransactionID()).get(0));
		}
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

	public Long getCountCourrier(HashMap<String, Object> filterMap,
			String transmissionCourrierJour, String typeCourrierTraitementJour,
			String typeCourrierJour, String typeCourrierValidationJour,
			String categorieCourrierJour, String courrierRubriqueJour,
			boolean forTotal) {
		Long countCourrier = 0L;

		// if (categorieCourrierJour.equals("T")) {
		// countCourrier =
		// appMgr.CountAllCourrierBOCByCriteria(filterMap, 1,
		// dateDebut, dateFin, typeSecretaire, type1, idUser,
		// transmissionCourrierJour, typeCourrierTraitementJour, "");
		// countCourrierEnvoyer = appMgr.CountAllCourrierBOCByCriteria(
		// filterMap, 1, dateDebut, dateFin, type, type1, idUser,
		// transmissionCourrierJour, typeCourrierTraitementJour,
		// "D");
		// countCourrierRecu = appMgr.CountAllCourrierBOCByCriteria(
		// filterMap, 1, dateDebut, dateFin, type, type1, idUser,
		// transmissionCourrierJour, typeCourrierTraitementJour,
		// "A");
		// countCourrier = countCourrierEnvoyer + countCourrierRecu;
		// countCourrier = countCourrierEnvoyer =
		// appMgr.CountAllCourrierBOCByCriteria(
		// filterMap, 1, dateDebut, dateFin, type, type1, idUser,
		// transmissionCourrierJour, typeCourrierTraitementJour,
		// "T");
		// } else {
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

		// if(typeCourrierJour.equals("Envoyes") ||
		// typeCourrierJour.equals("Tous")){
		// if(!stateTraitement.equals("Avalider")){
		// countCourrierEnvoyer = appMgr.CountAllCourrierEnvoyerByCriteria(vb
		// .getPerson().isResponsable(), listIdsSousUnit,
		// listIdsSubordonne, filterMap, consultationCourrierSecretaire,
		// consultationCourrierSubordonne, consultationCourrierSousUnite,
		// jourOrAutre, dateDebut, dateFin, type, type1, typeSecretaire,
		// idUser, typeTransmission, stateTraitement,
		// courrierRubriqueJourId, forTotal);
		// }else{
		// countCourrierEnvoyer = 0L;
		// }
		// }
		// countCourrierRecu = appMgr.CountAllCourrierRecuByCriteria(vb
		// .getPerson().isResponsable(), listIdsSousUnit,
		// listIdsSubordonne, filterMap, consultationCourrierSecretaire,
		// consultationCourrierSubordonne, consultationCourrierSousUnite,
		// jourOrAutre, dateDebut, dateFin, type, type1, typeSecretaire,
		// idUser, typeTransmission, stateTraitement,
		// courrierRubriqueJourId, forTotal);
		// if (typeCourrierJour.equals("Recu")) {
		// return countCourrierRecu;
		// } else if (typeCourrierJour.equals("Envoyes")) {
		// return countCourrierEnvoyer;
		// } else {
		// return countCourrierEnvoyer + countCourrierRecu;
		// }
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
		/*
		 * OLD for (int j = 0; j < listTr.size(); j++) { if
		 * (vb.getLocale().equals("ar")) { libelle =
		 * listTr.get(j).getTransmissionLibelleAr(); } else { libelle =
		 * listTr.get(j).getTransmissionLibelle(); } selectItemsTr.add(new
		 * SelectItem(id,libelle));
		 * 
		 * }
		 */
		for (Transmission item : listTr) {
			if (vb.getLocale().equals("ar")) {
				libelle = item.getTransmissionLibelleAr();
			} else {
				libelle = item.getTransmissionLibelle();
			}
			id = item.getTransmissionId().toString();
			selectItemsTr.add(new SelectItem(id, libelle));
		}

		// selectItemsTr.add(new
		// SelectItem(messageSource.getMessage("AutreLabel",
		// new Object[] {}, lm.createLocal())));

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

	// public CourrierInformations
	// searchExpediteurDestinataireAndExplodeDataTransactionDestination(
	// TransactionDestination transactionDestination) {
	// CourrierInformations courrierInformations = new CourrierInformations();
	// courrierInformations.setTransactionDestination(transactionDestination);
	// Courrier courrier = appMgr.listCourrierByIdTransaction(
	// transactionDestination.getId().getIdTransaction()).get(0);
	// courrierInformations.setCourrierReference(courrier
	// .getCourrierReferenceCorrespondant());
	// courrierInformations.setCourrierObjet(courrier.getCourrierObjet());
	// courrierInformations.setCourrier(courrier);
	// Nature nature = appMgr
	// .getNatureById(courrier.getNature().getNatureId()).get(0);
	// courrierInformations.setCourrierNature(nature.getNatureLibelle());
	//
	// Transaction transaction = appMgr.getListTransactionByIdTransaction(
	// transactionDestination.getId().getIdTransaction()).get(0);
	// courrierInformations.setTransaction(transaction);
	// System.out.println("é : " + transaction.getTransactionId());
	// courrierInformations.setTransactionID(transaction.getTransactionId());
	// courrierInformations.setCourrierDateReceptionEnvoi(transaction
	// .getTransactionDateTransaction());
	// courrierInformations.setCourrierRecu(1);
	// if (transaction.getTransactionDestinationReelle() != null
	// && transaction.getEtat().getEtatId().equals(2)) {
	// courrierInformations.setCourrierAValider(1);
	// } else {
	// courrierInformations.setCourrierAValider(0);
	// }
	// // expediteur
	// Expdest expDest = appMgr.getListExpDestByIdExpDest(
	// transaction.getExpdest().getIdExpDest()).get(0);
	// if (expDest.getTypeExpDest().equals("Interne-Person")) {
	// Person person = ldapOperation.getUserById(expDest
	// .getIdExpDestLdap());
	// courrierInformations.setCourrierExpediteur(person.getCn());
	// courrierInformations.setCourrierExpediteurObjet(person);
	// } else if (expDest.getTypeExpDest().equals("Interne-Unité")) {
	// Unit unit = ldapOperation.getUnitById(expDest.getIdExpDestLdap());
	// courrierInformations.setCourrierExpediteur(unit.getNameUnit());
	// courrierInformations.setCourrierExpediteurObjet(unit);
	// } else if (expDest.getTypeExpDest().equals("Interne-Boc")) {
	// Unit unit = ldapOperation.getBocById(expDest.getIdExpDestLdap());
	// courrierInformations.setCourrierExpediteur(unit.getNameUnit());
	// courrierInformations.setCourrierExpediteurObjet(unit);
	// } else if (expDest.getTypeExpDest().equals("Externe")) {
	// if (expDest.getExpdestexterne().getTypeutilisateur()
	// .getTypeUtilisateurLibelle().equals("PP")) {
	// courrierInformations.setCourrierExpediteur(expDest
	// .getExpdestexterne().getExpDestExterneNom()
	// + " "
	// + expDest.getExpdestexterne().getExpDestExternePrenom()
	// + " (PP)");
	// courrierInformations.setCourrierExpediteurObjet(expDest
	// .getExpdestexterne());
	// } else {
	// courrierInformations.setCourrierExpediteur(expDest
	// .getExpdestexterne().getExpDestExterneNom() + " (PM)");
	// courrierInformations.setCourrierExpediteurObjet(expDest
	// .getExpdestexterne());
	// }
	// }
	// // destinataire
	// expDest = new Expdest();
	// if (transaction.getTransactionDestinationReelle() != null) {
	// TransactionDestinationReelle transactionDestinationReelle;
	// Expdestexterne expDestExterne;
	// transactionDestinationReelle = transaction
	// .getTransactionDestinationReelle();
	// if (transactionDestinationReelle
	// .getTransactionDestinationReelleTypeDestinataire().equals(
	// "Externe")) {
	// Etat etat = appMgr.listEtatByRef(
	// transaction.getEtat().getEtatId()).get(0);
	// if (etat.getEtatLibelle().equals("Simple")) {
	// expDest = appMgr.getListExpDestByIdExpDest(
	// transactionDestination.getId().getIdExpDest()).get(
	// 0);
	// if (expDest.getTypeExpDest().equals("Interne-Person")) {
	// courrierInformations
	// .setCourrierDestinataireReelle(ldapOperation
	// .getCnById(ldapOperation.CONTEXT_USER,
	// "uid",
	// expDest.getIdExpDestLdap()));
	// } else if (expDest.getTypeExpDest().equals("Interne-Unité")) {
	// courrierInformations
	// .setCourrierDestinataireReelle(ldapOperation
	// .getCnById(ldapOperation.CONTEXT_UNIT,
	// "departmentNumber",
	// expDest.getIdExpDestLdap()));
	// } else if (expDest.getTypeExpDest().equals("Interne-Boc")) {
	// courrierInformations
	// .setCourrierDestinataireReelle(ldapOperation
	// .getCnById(ldapOperation.CONTEXT_BOC,
	// "departmentNumber",
	// expDest.getIdExpDestLdap()));
	// }
	// } else {
	// expDestExterne = new Expdestexterne();
	// expDestExterne = appMgr
	// .getExpediteurById(
	// transactionDestinationReelle
	// .getTransactionDestinationReelleIdDestinataire())
	// .get(0);
	// if (expDestExterne.getTypeutilisateur()
	// .getTypeUtilisateurLibelle().equals("PP")) {
	// courrierInformations
	// .setCourrierDestinataireReelle(expDestExterne
	// .getExpDestExterneNom()
	// + " "
	// + expDestExterne
	// .getExpDestExternePrenom()
	// + " (PP)");
	// } else {
	// courrierInformations
	// .setCourrierDestinataireReelle(expDestExterne
	// .getExpDestExterneNom() + " (PM)");
	// }
	// }
	// }
	// } else {
	// expDest = new Expdest();
	// expDest = appMgr.getListExpDestByIdExpDest(
	// transactionDestination.getId().getIdExpDest()).get(0);
	// if (expDest.getTypeExpDest().equals("Interne-Person")) {
	// courrierInformations
	// .setCourrierDestinataireReelle(ldapOperation.getCnById(
	// ldapOperation.CONTEXT_USER, "uid",
	// expDest.getIdExpDestLdap()));
	// } else if (expDest.getTypeExpDest().equals("Interne-Unité")) {
	// courrierInformations
	// .setCourrierDestinataireReelle(ldapOperation.getCnById(
	// ldapOperation.CONTEXT_UNIT, "departmentNumber",
	// expDest.getIdExpDestLdap()));
	// } else if (expDest.getTypeExpDest().equals("Interne-Boc")) {
	// courrierInformations
	// .setCourrierDestinataireReelle(ldapOperation.getCnById(
	// ldapOperation.CONTEXT_BOC, "departmentNumber",
	// expDest.getIdExpDestLdap()));
	// }
	// }
	// if ((transaction.getTransactionDestinationReelle() != null
	// && (transaction.getEtat().getEtatLibelle().equals("A valider") ||
	// transaction
	// .getEtat().getEtatLibelle().equals("Non traité")) || transaction
	// .getEtat().getEtatLibelle().equals("Faire suivre"))) {
	// courrierInformations.setCourrierAValider(1);
	// } else {
	// courrierInformations.setCourrierAValider(0);
	// }
	// // courrierInformations.setTypeCourrier(getCategorieCourrier(
	// // transactionDestination, true));
	// return courrierInformations;
	// }

	/*
	 * private String getCategorieCourrier(Transaction transaction, boolean
	 * isMail) { String result = ""; String[] type = new String[2]; if
	 * (vb.getPerson().isResponsable()) { if
	 * (transaction.getTransactionTypeIntervenant().contains("sub")) { type =
	 * transaction.getTransactionTypeIntervenant().split("_"); if
	 * (Integer.parseInt(type[1]) == vb.getPerson().getId()) { if (isMail) {
	 * result = "A. Mes Propres Courriers Envoyés"; } else { result =
	 * "A. Mes Propres Dossiers Envoyés"; } } else {
	 * 
	 * if (isMail) { result = "C. Les Courriers Envoyés de Mes Subordonnées"; }
	 * else { result = "C. Les Dossiers Envoyés de Mes Subordonnées"; }
	 * 
	 * } } else if (transaction.getTransactionTypeIntervenant().contains(
	 * "unit")) { type = transaction.getTransactionTypeIntervenant().split("_");
	 * if (Integer.parseInt(type[1]) == vb.getPerson()
	 * .getAssociatedDirection().getIdUnit()) { if (isMail) { result =
	 * "E. Les Courriers Envoyés de Mon Unité"; } else { result =
	 * "E. Les Dossiers Envoyés de Mon Unité"; } } else {
	 * 
	 * if (isMail) { result = "G. Les Courriers Envoyés de Mes Sous-Unités"; }
	 * else { result = "G. Les Dossiers Envoyés de Mes Sous-Unités"; }
	 * 
	 * } } else if (transaction.getTransactionTypeIntervenant().contains(
	 * "secretary")) { if (isMail) { result =
	 * "I. Les Courriers Envoyés de Ma Secrétaire"; } else { result =
	 * "I. Les Dossiers Envoyés de Ma Secrétaire"; } } } else if
	 * (vb.getPerson().isSecretary()) { if
	 * (transaction.getTransactionTypeIntervenant().contains( "secretary")) { if
	 * (isMail) { result = "A. Mes Propres Courriers Envoyés"; } else { result =
	 * "A. Mes Propres Dossiers Envoyés"; } } else { if (isMail) { result =
	 * "E. Les Courriers Envoyés de Mon Unité"; } else { result =
	 * "E. Les Dossiers Envoyés de Mon Unité"; } } } else if
	 * (vb.getPerson().isAgent()) { if
	 * (transaction.getTransactionTypeIntervenant().contains("agent")) { if
	 * (isMail) { result = "A. Mes Propres Courriers Envoyés"; } else { result =
	 * "A. Mes Propres Dossiers Envoyés"; } } else { if (isMail) { result =
	 * "E. Les Courriers Envoyés de Mon Unité"; } else { result =
	 * "E. Les Courriers Envoyés de Mon Unité"; } } } else { result =
	 * "A. Les Courriers du Bureau d'Ordre"; //
	 * if(transaction.getTransactionTypeIntervenant() != null){ // result =
	 * "A. Les Courriers de Départ"; // }else{ // result =
	 * "B. Les Courriers d'Arrivée"; // } }
	 * 
	 * return result; }
	 * 
	 * private String getCategorieCourrier( TransactionDestination
	 * transactionDestination, boolean isMail) { String result = ""; String[]
	 * type = new String[2]; if (vb.getPerson().isResponsable()) { if
	 * (transactionDestination.getTransactionDestTypeIntervenant()
	 * .contains("sub")) { type = transactionDestination
	 * .getTransactionDestTypeIntervenant().split("_"); if
	 * (Integer.parseInt(type[1]) == vb.getPerson().getId()) { if (isMail) {
	 * result = "B. Mes Propres Courriers Reéus"; } else { result =
	 * "B. Mes Propres Dossiers Reéus"; } } else { if (isMail) { result =
	 * "D. Les Courriers Reéus de Mes Subordonnées"; } else { result =
	 * "D. Les Dossiers Reéus de Mes Subordonnées"; } } }
	 * 
	 * } else if (transactionDestination.getTransactionDestTypeIntervenant()
	 * .contains("unit")) { type =
	 * transactionDestination.getTransactionDestTypeIntervenant() .split("_");
	 * if (Integer.parseInt(type[1]) == vb.getPerson()
	 * .getAssociatedDirection().getIdUnit()) { if (isMail) { result =
	 * "F. Les Courriers Reéus de Mon Unité"; } else { result =
	 * "F. Les Dossiers Reéus de Mon Unité"; } } else { if (isMail) { result =
	 * "H. Les Courriers Reéus de Mes Sous-Unités"; } else { result =
	 * "H. Les Dossiers Reéus de Mes Sous-Unités"; } } } else if
	 * (transactionDestination.getTransactionDestTypeIntervenant()
	 * .contains("secretary")) { if (isMail) { result =
	 * "J. Les Courriers Reéus de Ma Secrétaire"; } else { result =
	 * "J. Les Dossiers Reéus de Ma Secrétaire"; } }// else if
	 * (transactionDestination //
	 * .getTransactionDestTypeIntervenant().contains("agent")) { // // if
	 * (isMail) { // result = "D. Les Courriers Reéus de Mes Agents"; // } else
	 * { // result = "D. Les Dossiers Reéus de Mes Agents"; // } // } else if
	 * (vb.getPerson().isSecretary()) { if
	 * (transactionDestination.getTransactionDestTypeIntervenant()
	 * .contains("secretary")) { if (isMail) { result =
	 * "B. Mes Propres Courriers Reéus"; } else { result =
	 * "B. Mes Propres Dossiers Reéus"; } } else { if (isMail) { result =
	 * "F. Les Courriers Reéus de Mon Unité"; } else { result =
	 * "F. Les Dossiers Reéus de Mon Unité"; } } } else if
	 * (vb.getPerson().isAgent()) { if
	 * (transactionDestination.getTransactionDestTypeIntervenant()
	 * .contains("agent")) { if (isMail) { result =
	 * "B. Mes Propres Courriers Reéus"; } else { result =
	 * "B. Mes Propres Dossiers Reéus"; } } else { if (isMail) { result =
	 * "F. Les Courriers Reéus de Mon Unité"; } else { result =
	 * "F. Les Dossiers Reéus de Mon Unité"; } } } else { result =
	 * "A. Les Courriers Reéus du Bureau d'Ordre"; }
	 * 
	 * return result; }
	 */

	// 2019-06-24
	public boolean findBocExpBocDest(Transaction transaction) {

		Transaction transactionExpediteur = new Transaction();
		int idExp, idDest;
		Person personneRechercheExp = new Person();
		Person personneRechercheDes = new Person();
		boolean findPersonExp = false;
		boolean findPersonDest = false;
		Unit unitDest = new Unit();

		int k = 0;
		int m = 0;

		transactionExpediteur = appMgr
				.getTransactionExpediteurByIdTransactionDestinationReelle(
						transaction.getTransactionDestinationReelle()
								.getTransactionDestinationReelleId(), 1).get(0);
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

		System.out.println("2019-05-16 Type Expdests : " + typeExpdests);
		System.out.println("2019-05-16:Type Intervenant : "
				+ transactionExpediteur.getTransactionTypeIntervenant());
		System.out.println("2019-05-16 : taille listTransactionsDest : "
				+ listTransactionsDest.size());

		// [] :Expédtiteur

		if (typeExpdests.equals("Externe")) {
			System.out.println("2019-05-16: Externe");
			IdExpediteur = transactionExpediteur.getExpdest()
					.getExpdestexterne().getIdExpDestExterne();

		} else if (typeExpdests.equals("Interne-Unité")) {
			System.out.println("2019-05-16: Interne-Unité");

			// []:Id Expediteur
			IdExpediteur = transactionExpediteur.getExpdest()
					.getIdExpDestLdap();
			System.out.println("2019-05-16  IdExpediteur : " + IdExpediteur);

			// Unit unit=vb.getLdapOperation().getUnitById(IdExpediteur);
			// System.out.println("2019-05-16 unit : "+unit);
			// getIdBocByUnit(unit);
			idBocExpediteur = listExpd.get(0).getIdExpDestLdap();
			// idBocExpediteur=unit.getAssociatedBOC().getIdBOC();
			System.out.println("2019-05-16 : idBocExpediteur : "
					+ idBocExpediteur);

		} else {
			System.out.println("2019-05-16: Interne-Person");
			IdExpediteur = transactionExpediteur.getExpdest()
					.getIdExpDestLdap();
			do {
				idExp = vb.getCopyLdapListUser().get(k).getId();
				System.out
						.println("==============================================");
				System.out.println("2019-05-16 : id Expdest :" + idExp);
				System.out.println("2019-05-16 : id IdExpediteur :"
						+ IdExpediteur);
				System.out
						.println("==============================================");

				if (idExp == IdExpediteur) {
					findPersonExp = true;
					personneRechercheExp = vb.getCopyLdapListUser().get(k);
				} else {
					k++;
				}

			} while (!findPersonExp && k < vb.getCopyLdapListUser().size());

			idBocExpediteur = ReturnBocAssocieeUnite(personneRechercheExp
					.getAssociatedDirection());

		}
		System.out
				.println("================ Fin Expéditeur ==============================================");

		System.out
				.println("================== Début Destinataire ==============================================");

		// [] :2019-05-16: Destinataire
		String tyeDestinataire = transactionExpediteur
				.getTransactionDestinationReelle()
				.getTransactionDestinationReelleTypeDestinataire();
		if (tyeDestinataire.equals("Externe")) {
			System.out.println("2019-05-16: Externe");

		} else if (tyeDestinataire.equals("Interne-Unité")) {
			System.out.println("2019-05-16: Interne-Unité");

			System.out.println("2019-05-16: Interne-Unité");
			// [2019-06-27]
			int idUnitDes = transactionExpediteur
					.getTransactionDestinationReelle()
					.getTransactionDestinationReelleIdDestinataire();

			System.out.println("2019-06-27 idUnitDes " + idUnitDes);

			do {

				idDest = vb.getCopyLdapListUnit().get(m).getIdUnit();
				System.out
						.println("==============================================");
				System.out.println(" ID DEST =" + idDest);
				System.out.println(" ID User Des = " + idUnitDes);
				System.out
						.println("==============================================");

				if (idDest == idUnitDes) {
					findPersonDest = true;
					unitDest = vb.getCopyLdapListUnit().get(m);
					break;
				} else {
					m++;
				}

			} while (!findPersonDest && m < vb.getCopyLdapListUnit().size());

			System.out.println("# Personne Recherche DES :" + unitDest);
			Unit uni = ldapOperation.getUnitById(unitDest.getIdUnit());
			idBocDestinataire = ReturnBocAssocieeUnite(uni);
			System.out.println("2019-05-16 : idBocDestinataire : "
					+ idBocDestinataire);
		} else {
			System.out.println("2019-05-16 Destinataire : Interne-Person");

			int idUserDes = transactionExpediteur
					.getTransactionDestinationReelle()
					.getTransactionDestinationReelleIdDestinataire();

			do {

				idDest = vb.getCopyLdapListUser().get(m).getId();
				System.out
						.println("==============================================");
				System.out.println("ID DEST :" + idDest);
				System.out.println(" ID User Des : " + idUserDes);
				System.out
						.println("==============================================");
				if (idDest == idUserDes) {
					findPersonDest = true;
					personneRechercheDes = vb.getCopyLdapListUser().get(m);
				} else {
					m++;
				}

			} while (!findPersonDest && m < vb.getCopyLdapListUser().size());

			System.out.println("# Personne Recherche DES :"
					+ personneRechercheDes);

			idBocDestinataire = ReturnBocAssocieeUnite(personneRechercheDes
					.getAssociatedDirection());
			System.out.println("2019-05-16 : idBocDestinataire : "
					+ idBocDestinataire);

		}

		System.out
				.println("=============================Fin Destinataire ============================");

		// KHA : unité superieur sous 1er BO
		Unit uniteSuperieurDestinataire = unitSup;

		System.out.println("id Boc Destinataire :" + idBocDestinataire);
		System.out.println("id Boc Expediteur:" + idBocExpediteur);

		// : Mettre à jour Table Transaction Après exécution :Changer
		// etat de courrier Non traité -> traité

		// [] ID Boc de l'unité Connecté == ID BOC
		// Destinataire=======================================================
		// -------------------------------------------------------------------------------------

		int idBos = vb.getPerson().getAssociatedBOC().getIdBOC();
		System.out.println("2019-05-16 idBoc :" + idBos);

		if (idBos == idBocDestinataire) {
			System.out.println("idBos == idBocDestinataire");
			return true;
		} else {
			System.out.println("idBos <> idBocDestinataire");

			return false;

		}

	}

	private void executeTransactionInterne(Transaction transaction,
			CourrierInformations courrierInformations) {

		System.out.println("### Méthode executeTransactionInterne ###");

		System.out.println(" 2019-05-24 Transaction courrierInformations :"
				+ courrierInformations.getTransaction().getTransactionId());
		// System.out.println(" 2019-05-24 ID Etat courrierInformations :"+courrierInformations.getTransaction().getEtat().getEtatId());
		System.out.println("2019-05-29 : " + transaction.getTransactionId());

		System.out.println("ID Courrier :"
				+ courrierInformations.getCourrier().getIdCourrier());

		if (courrierInformations.getCourrier() == null) {
			System.out.println("-->If Courrier == null ");
			System.out.println("--> ID Courrier dans If :"
					+ courrierInformations.getCourrierID());
			// set Courrier dans Courrier informations
			courrierInformations.setCourrier(appMgr.getCourrierByIdCourrier(
					courrierInformations.getCourrierID()).get(0));

		}

		System.out.println("ID Transaction  :"
				+ courrierInformations.getTransaction());
		if (courrierInformations.getTransaction() == null) {
			System.out.println("-->If Transaction == null ");
			System.out.println("--> ID Transaction dans If :"
					+ courrierInformations.getTransactionID());
			courrierInformations.setTransaction(appMgr
					.getListTransactionByIdTransaction(
							courrierInformations.getTransactionID()).get(0));

		}

		setStatus1(false);
		setStatus2(false);
		int etatId = courrierInformations.getTransaction().getEtat()
				.getEtatId();
		System.out.println("2019-05-21 ID transaction :"
				+ courrierInformations.getTransaction().getId());
		System.out.println("2019-05-20: Etat transaction: "
				+ courrierInformations.getTransaction().getEtat().getEtatId());
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
		System.out.println("vb.getCourDossConsulterInformations() : "
				+ vb.getCourDossConsulterInformations());
		TransactionDestination transactionDestination = vb
				.getCourDossConsulterInformations().getTransactionDestination();
		System.out.println("transactionDestination : "
				+ vb.getCourDossConsulterInformations());

		//
		try {

			System.out.println(" : Destination Réel de transaction :"
					+ transaction.getTransactionDestinationReelle()
							.getTransactionDestinationReelleTypeDestinataire());

			if (transaction.getTransactionDestinationReelle() != null) {

				//
				// ---------------------------------------------------------------------------------------------------
				newTransaction = new Transaction();
				transactionExpediteur = new ArrayList<Transaction>();
				Etat etat = new Etat();
				Expdest expdest = new Expdest();
				Typetransaction typetransaction = new Typetransaction();
				TransactionDestinationId id = new TransactionDestinationId();
				TransactionDestination trDest = new TransactionDestination();

				System.out.println(" ID Destination Reelle  :"
						+ transaction.getTransactionDestinationReelle()
								.getTransactionDestinationReelleId());

				// : Transaction By id Transaction Destinataire Reele et ordre
				// =1
				// ----------------------------------------------------------------------------------------------------
				transactionExpediteur = appMgr
						.getTransactionExpediteurByIdTransactionDestinationReelle(
								transaction.getTransactionDestinationReelle()
										.getTransactionDestinationReelleId(), 1);
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
				System.out.println("2019-05-222 : size Liste de transaction :"
						+ listTransaction.size());

				listTransactionByEtat = appMgr
						.getTransactionByIdTransactionDestinationReelleByEtat(transaction
								.getTransactionDestinationReelle()
								.getTransactionDestinationReelleId());

				// : Variable utilisé pour comparer si BO Destinataire == BO
				// PErsonne Connecté
				// ----------------------------------------------------------------------------------------------------
				boolean passageParBoc = false;
				System.out.println("alooooooooooo : "
						+ transactionExpediteur.size());
				for (Transaction tra : transactionExpediteur) {
					System.out.println("=======jalila ========");
					System.out
							.println("2019-05-28 : " + tra.getTransactionId());
					idUserDes = tra.getTransactionDestinationReelle()
							.getTransactionDestinationReelleIdDestinataire();
					System.out.println("2019-05-28 : " + idUserDes);
					System.out.println("========jalila=============");
					typeUserDes = tra.getTransactionDestinationReelle()
							.getTransactionDestinationReelleTypeDestinataire();

				}
				System.out.println("2019-05-28 idUserDes: " + idUserDes);

				// KHA : unité superieur sous 1er BO
				// boc De destinataire
				// [2019-06-25]
				int idDest;
				Person personneRechercheDes = new Person();
				Unit unitRechecheDes = new Unit();
				boolean findPersonDest = false;
				boolean findUnitDest = false;

				int k = 0;
				System.out.println("typeUserDes : " + typeUserDes);
				if (typeUserDes.equals("Interne-Person")) {
					do {

						idDest = vb.getCopyLdapListUser().get(k).getId();
						System.out.println("ID LDAP :" + idDest);
						System.out.println("ID DEST :" + idUserDes);
						if (idDest == idUserDes) {
							findPersonDest = true;
							personneRechercheDes = vb.getCopyLdapListUser()
									.get(k);
						} else {
							k++;
						}

					} while (!findPersonDest
							&& k < vb.getCopyLdapListUser().size());
					System.out.println("# Personne Recherche DES :"
							+ personneRechercheDes);

					getIdBocByUnit(personneRechercheDes
							.getAssociatedDirection());

				} else {
					do {

						idDest = vb.getCopyLdapListUnit().get(k).getIdUnit();
						System.out.println("ID LDAP :" + idDest);
						System.out.println("ID DEST :" + idUserDes);
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
					System.out.println("unite Jour // : " + unite);
					System.out.println("ID Unite Jour // : "
							+ unite.getIdUnit());
					getIdBocByUnit(unite);
				}

				int idBocDestinataire = idBoc;

				System.out.println("# Personne Recherche BOS :"
						+ idBocDestinataire);
				// KHA : unité superieur sous 1er BO
				Unit uniteSuperieurDestinataire = unitSup;
				System.out.println("2019-05-22: unitSup : " + unitSup);

				System.out.println("2019-05-21 etat : " + etatId);

				// [] findBocExpBocDest : retourne true si boc dest et bos
				// connecté sous même BO------------------------------
				// -------------------------------------------------------------------------------------
				passageParBoc = findBocExpBocDest(transaction);
				System.out.println("2019-05-31 passageParBoc : sous meme BO? "
						+ passageParBoc);

				System.out.println("2019-05-25 : "
						+ courrierInformations.getTransaction()
								.getCourrierReferenceCorrespondant());
				// [] : on execute les Courrier where son Etat = 5
				// ----------------------------------------------
				// -------------------------------------------------------------------------------------
				if (etatId == 5) {

					if (passageParBoc) {

						System.out
								.println("L'Expéditeur et le destinataire existent sous même BOS");

						List<Variables> listVariables = appMgr
								.listVariablesByLibelle("validation_hierarchique_courrier_arrive");

						if (listVariables != null && listVariables.size() > 0) {

							Variables variable = listVariables.get(0);

							// if (variable.getVaraiablesValeur().equals("Non"))
							// {

							// : Mettre à jour Table Transaction
							// Après===========================================================================================
							// exécution :Changer etat de courrier Non
							// Non traité -> traité
							// -------------------------------------------------------------------------------------
							etat = appMgr.listEtatByLibelle("Traité").get(0);
							System.out.println("2019-05-22 : Expdest : "
									+ transactionExpediteur.get(0).getExpdest()
											.getIdExpDest());

							System.out
									.println("set id expdest de la Transaction de l'expéditeur d'ordre 1 :"
											+ transactionExpediteur.get(0)
													.getExpdest()
													.getTypeExpDest());

							newTransaction.setExpdest(transactionExpediteur
									.get(0).getExpdest());

							// set id utilisateur qui execute Courrier
							// ----------------------------------------------
							// exmple BOS1:repensable Fawzi Mnawer=>id=4
							// ----------------------------------------------
							newTransaction.setIdUtilisateur(vb.getPerson()
									.getId());

							// set Date
							// Transaction----------------------------------------------
							newTransaction
									.setTransactionDateTransaction(new Date());

							// set Type
							// Transaction----------------------------------------------
							typetransaction = appMgr
									.getTypeTransactionByLibelle("Envoi")
									.get(0);
							newTransaction.setTypetransaction(typetransaction);

							// set Etat Courrier pour dire qu'il est
							// traité----------------------------------------------
							newTransaction.setEtat(etat);
							newTransaction.setTransactionSupprimer(true);

							// set Ordre Courrier
							// ---------------------------------------------
							int newOrderNumber = transaction
									.getTransactionOrdre();
							System.out.println("newOrderNumber :"
									+ newOrderNumber);
							//newOrderNumber++;
							// newOrderNumber sera incrémenter
							newTransaction.setTransactionOrdre(newOrderNumber);

							newTransaction.setDossier(transaction.getDossier());
							newTransaction
									.setTransactionDestinationReelle(transaction
											.getTransactionDestinationReelle());
							System.out.println("2019-05-22: ID Transaction :"
									+ transaction.getTransactionId());
							newTransaction.setTransactionFirst(transaction
									.getTransactionId());

							// : Mettre à jour reference courrier dans
							// transaction
							// 2019-05-16
							// ===========================================================================================

							Calendar calendar = Calendar.getInstance();
							// calendar.setTime(dateCourrier);
							System.out
									.println("[] courrier.getCourrierType() : "
											+ courrier.getCourrierType());

							// Courrier Départs et Courrier Interne
							// ===========================================================================================
							System.out.println("2019-05-31======1=======> : "
									+ transaction.getCourrierType());

							// [JS] [2019-06-14] : 1ére Etape : Mise à jour Dans
							// table Courrier
							if (courrier.getCourrierType().equals("I")) {
								System.out.println("Courrier de Reference I ");

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
								System.out.println("[D] => lastId  :"
										+ lastId);
								courrierInformations.getCourrier()
										.setCourrierType("D");
								
								if (lastId != null) {
									System.out.println("lastID != null");
									System.out
											.println("2019-05-22 : last Id : "
													+ lastId);
									System.out
											.println("2019-05-22 Type Ordre : "
													+ newTransaction
															.getCourrierTypeOrdre());
									courrierInformations.getCourrier()
											.setCourrierTypeOrdre(lastId + 1);

								}
								else {
									System.out.println("lastID == null ");
									courrierInformations.getCourrier()
											.setCourrierTypeOrdre(1);

								}

							}

							// [JS] [2019-06-14] : 2ére Etape : Mise à jour Dans
							// table Transaction
							// [Ref Courrier : I]
							if (transaction.getCourrierType().equals("I")) {
								System.out
										.println("Transaction  de Reference I ");

								Integer lastId = appMgr
										.CountAllCourrierBOCByTransaction(courrierInformations.getDossierID(),
												"A",
												year,
												"boc_"
														+ String.valueOf(vb
																.getPerson()
																.getAssociatedBOC()
																.getIdBOC()),
												listIdBocMembers);
								System.out.println("[Arrivée] => lastId  :"
										+ lastId);

								newTransaction
										.setCourrierDateReceptionAnnee(calendar
												.get(Calendar.YEAR));

								newTransaction.setCourrierType("A");

								if (lastId == null || lastId == 0) {
									System.out
											.println("lastID == null && lastIdByTransaction == null ");
									newTransaction.setCourrierTypeOrdre(1);
								}

								if (lastId != null) {
									System.out
											.println("lastID != null && lastIdByTransaction == null ");
									System.out.println("last Id : " + lastId);
									newTransaction
											.setCourrierTypeOrdre(lastId + 1);
								}

							}

							// [JS] [2019-06-14] : 2ére Etape : Mise à jour Dans
							// table Transaction
							// [Ref Courrier : D ]
							if (transaction.getCourrierType().equals("D")) {

								Integer lastId = appMgr
										.CountAllCourrierBOCByTransaction(courrierInformations.getDossierID(),
												"A",
												year,
												"boc_"
														+ String.valueOf(vb
																.getPerson()
																.getAssociatedBOC()
																.getIdBOC()),
												listIdBocMembers);
								System.out.println("[Arrivée] => lastId  :"
										+ lastId);

								newTransaction
										.setCourrierDateReceptionAnnee(calendar
												.get(Calendar.YEAR));

								newTransaction.setCourrierType("A");
								courrierInformations.getTransaction()
										.setCourrierType("A");

								if ((lastId == null || lastId == 0)) {
									System.out.println("lastID == null");
									newTransaction.setCourrierTypeOrdre(1);
								}

								if (lastId != null) {
									System.out.println("lastID != null");
									System.out.println("last Id : " + lastId);
									newTransaction
											.setCourrierTypeOrdre(lastId + 1);
								}

							} else
							// [JS] [2019-06-14] : 2ére Etape : Mise à jour Dans
							// table Transaction
							// [Ref Courrier : A ]

							if (transaction.getCourrierType().equals("A")) {

								System.out
										.println("2019-05-31======3======> : "
												+ newTransaction
														.getCourrierType());

								System.out
										.println("2019-05-31======2======> : "
												+ transaction.getCourrierType());

								System.out.println("test le 2019-06-27 :"
										+ vb.getPerson().getAssociatedBOC()
												.getIdBOC());

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
								System.out.println("[Départ] => lastId  :"
										+ lastId);

								// [] : Ref Courrier

								newTransaction
										.setCourrierDateReceptionAnnee(calendar
												.get(Calendar.YEAR));

								// [] : Metrre à jour Ref dans Transaction et
								// dans Courrier
								newTransaction.setCourrierType("D");
								courrierInformations.getCourrier()
										.setCourrierType("D");

								if (lastId == null || lastId == 0) {
									System.out.println("lastID == null ");
									newTransaction.setCourrierTypeOrdre(1);
									courrierInformations.getCourrier()
											.setCourrierTypeOrdre(1);

								}

								if (lastId != null) {
									System.out.println("lastID != null ");
									System.out
											.println("2019-05-22 : last Id : "
													+ lastId);
									newTransaction
											.setCourrierTypeOrdre(lastId + 1);
									System.out
											.println("2019-05-22 Type Ordre : "
													+ newTransaction
															.getCourrierTypeOrdre());
									courrierInformations.getCourrier()
											.setCourrierTypeOrdre(lastId + 1);

								}

							}
							// update Ref Courrier dans table courrier
							System.out.println("courrier Type : "
									+ courrierInformations.getCourrier()
											.getCourrierType());
							System.out.println("courrier Type Ordre : "
									+ courrierInformations.getCourrier()
											.getCourrierTypeOrdre());
							courrierInformations
									.getCourrier()
									.setCourrierReferenceCorrespondant(
											courrierInformations.getCourrier()
													.getCourrierType()
													+ courrierInformations
															.getCourrier()
															.getCourrierTypeOrdre());
							appMgr.update(courrierInformations.getCourrier());

							System.out.println("2019-05-25 test :"
									+ courrierInformations.getTransaction()
											.getTransactionId());

							// Transaction
							// transaction2=courrierInformations.getTransaction();
							// System.out.println("2019-05-25 update : "+transaction2.getTransactionId());
							// appMgr.update(transaction2);

							System.out
									.println("[Transaction]=>Type Courrier  :"
											+ newTransaction.getCourrierType());
							System.out
									.println("[Transaction]=>Courrier Type Ordre :"
											+ newTransaction
													.getCourrierTypeOrdre());

							System.out.println("### help me :"
									+ transaction.getTransactionId());

							List<Transaction> transaction2s = appMgr
									.getListTransactionByIdTransaction(transaction
											.getTransactionId());
							System.out.println("2019-05-31 : "
									+ transaction2s.size());
							for (Transaction t : transaction2s) {
								System.out.println("====== 2019-05-31 =======");
								System.out.println(t.getTransactionId());
								System.out.println("=====================");
							}
							if (transaction2s != null
									&& transaction2s.size() > 0) {
								Transaction transaction2 = transaction2s.get(0);
								System.out
										.println("2019-05-25 : Type Courrier "
												+ courrierInformations
														.getCourrier()
														.getCourrierType());
								System.out.println("2019-05-25:  Type Ordre : "
										+ courrierInformations.getCourrier()
												.getCourrierTypeOrdre());
								transaction2
										.setCourrierReferenceCorrespondant(courrierInformations
												.getCourrier()
												.getCourrierType()
												+ courrierInformations
														.getCourrier()
														.getCourrierTypeOrdre());
								etat = appMgr.listEtatByLibelle("Traité")
										.get(0);
								transaction2.setEtat(etat);
								appMgr.update(transaction2);
							}

							// [] : Mettre à jour référence courrier dans
							// transaction
							newTransaction
									.setCourrierReferenceCorrespondant(newTransaction
											.getCourrierType()
											+ newTransaction
													.getCourrierTypeOrdre());
							appMgr.insert(newTransaction);

							System.out.println("2019-05-24 type : "
									+ courrierInformations.getTransaction()
											.getCourrierType());
							System.out.println("2019-05-24 ordre : "
									+ courrierInformations.getTransaction()
											.getCourrierTypeOrdre());

							// : Fin insertion dans la table Transaction
							// ===========================================================================================
							// -------------------------------------------------------------------------------------

							// : Début Test si Destinataire intern-Person ou
							// interne-Unité
							// expdest-------------------------------------------------------------------------------------------------

							expdest = new Expdest();
							System.out
									.println("2019-05-22 : ID Transaction Destinataire Réel :"
											+ transaction
													.getTransactionDestinationReelle()
													.getTransactionDestinationReelleId());
							TransactionDestinationReelle transactionDestinationReelle = appMgr
									.getTransactionDestinationReelById(transaction
											.getTransactionDestinationReelle()
											.getTransactionDestinationReelleId());
							System.out
									.println("2019-05-22 : Type Destination Reelle :"
											+ transactionDestinationReelle
													.getTransactionDestinationReelleTypeDestinataire());

							if (variable.getVaraiablesValeur().equals("Non")) {

								// [] :Type Destinataire Reelle :Interne-Person
								// Insertion Dans
								// Expdest-------------------------------------------------------------------------------------------------

								if (transaction
										.getTransactionDestinationReelle()
										.getTransactionDestinationReelleTypeDestinataire()
										.equals("Interne-Person")) {
									System.out
											.println("###  Type Interne-Person ###");
									expdest.setTypeExpDest("Interne-Person");
									Person personDestinationReel = vb
											.getLdapOperation()
											.getPersonalisedUserById(
													transaction
															.getTransactionDestinationReelle()
															.getTransactionDestinationReelleIdDestinataire());
									System.out
											.println("ID personDestinationReel :"
													+ personDestinationReel
															.getId()
													+ "Nom personDestinationReel : "
													+ personDestinationReel
															.getNom());
									expdest.setIdExpDestLdap(personDestinationReel
											.getId());
									System.out
											.println("2019-05-25 destinataire Reel :"
													+ personDestinationReel);
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

								} else

								// [] :type Destinataire Reelle :Interne-Unité
								// Insertion Dans
								// Expdest-------------------------------------------------------------------------------------------------

								if (transaction
										.getTransactionDestinationReelle()
										.getTransactionDestinationReelleTypeDestinataire()
										.equals("Interne-Unité")) {

									System.out
											.println("###  Type Interne-Unité ###");

									expdest.setTypeExpDest("Interne-Unité");
									Unit unit = vb
											.getLdapOperation()
											.getUnitById(
													transaction
															.getTransactionDestinationReelle()
															.getTransactionDestinationReelleIdDestinataire());
									System.out.println("unite :"
											+ unit.getShortNameUnit());
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

								System.out
										.println("2019-05-20 ID transaction type intervennant : "
												+ trDest.getTransactionDestTypeIntervenant());
								System.out
										.println("2019-05-20 ID transaction Dest : "
												+ trDest.getId().getIdExpDest());
								appMgr.insert(trDest);
								System.out
										.println("### Insertionstion : transactionDest ");

								// : Fin insert transactionDestination
								// -------------------------------------------------------------------------------------------------

								// update etat dans table transaction : etat5->
								// etat6
								for (Transaction transaction1 : listTransaction) {
									System.out.println("Id transaction : "
											+ transaction1.getTransactionId());
									System.out.println("etat transaction : "
											+ transaction1.getEtat()
													.getEtatLibelle());
									transaction1.setEtat(etat);
									appMgr.update(transaction1);
									System.out
											.println("### Insertionstion : update Transaction ");
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

							} else {
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

									System.out.println("idDestinataireReel : "
											+ idDestinataireReel);
									System.out
											.println("uniteSuperieurDestinataire : "
													+ uniteSuperieurDestinataire);
									System.out
											.println("uniteSuperieurDestinataire.getResponsibleUnit().getId() : "
													+ uniteSuperieurDestinataire
															.getResponsibleUnit()
															.getId());

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
										System.out
												.println("2019-06-10 d transaction : "
														+ newTransaction
																.getTransactionId());
										id.setIdTransaction(newTransaction
												.getTransactionId());
										id.setIdExpDest(expdest.getIdExpDest());
										System.out.println("2019-06-10 : "
												+ uniteSuperieurDestinataire
														.getResponsibleUnit()
														.getId());
										String type = "sub_"
												+ uniteSuperieurDestinataire
														.getResponsibleUnit()
														.getId();
										System.out.println("2019-06-10 Type : "
												+ type);
										trDest.setTransactionDestTypeIntervenant(type);
										trDest.setTransactionDestIdIntervenant(uniteSuperieurDestinataire
												.getResponsibleUnit().getId());
										trDest.setId(id);
										System.out
												.println("2019-05-20 ID transaction type intervennant : "
														+ trDest.getTransactionDestTypeIntervenant());
										System.out
												.println("2019-05-20 ID transaction Dest : "
														+ trDest.getId()
																.getIdExpDest());
										appMgr.insert(trDest);
										System.out
												.println("### Insertionstion : transactionDest ");

										// : Fin insert transactionDestination
										// -------------------------------------------------------------------------------------------------

										// update etat dans table transaction :
										// etat5-> etat6
										System.out
												.println("size listTransactionByEtat : "
														+ listTransactionByEtat);
										for (Transaction transaction1 : listTransactionByEtat) {
											System.out
													.println("Id transaction : "
															+ transaction1
																	.getTransactionId());
											System.out
													.println("etat transaction : "
															+ transaction1
																	.getEtat()
																	.getEtatLibelle());
											etat = appMgr.listEtatByLibelle(
													"Traité").get(0);
											transaction1.setEtat(etat);
											appMgr.update(transaction1);
											System.out
													.println("### Insertionstion : update Transaction ");

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
										System.out
												.println("2019-05-20 ID transaction type intervennant : "
														+ trDest.getTransactionDestTypeIntervenant());
										String type = "sub_"
												+ idDestinataireReel;
										trDest.setTransactionDestTypeIntervenant(type);
										trDest.setTransactionDestIdIntervenant(idDestinataireReel);
										System.out
												.println("2019-05-20 ID transaction Dest : "
														+ trDest.getId()
																.getIdExpDest());
										appMgr.insert(trDest);
										System.out
												.println("### Insertionstion : transactionDest ");

										// : Fin insert transactionDestination
										// -------------------------------------------------------------------------------------------------

										// update etat dans table transaction :
										// etat5-> etat6
										// for (Transaction transaction1 :
										// listTransaction) {
										// System.out.println("Id transaction : "+transaction1.getTransactionId());
										// System.out.println("etat transaction : "+transaction1.getEtat().getEtatLibelle());
										// transaction1.setEtat(etat);
										// appMgr.update(transaction1);
										// System.out.println("### Insertionstion : update Transaction ");
										// }
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

										// Unit : Destinataire suivant n'est pas
										// le Destinataire reel
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
						System.out
								.println("=================================== Modification Ref Courrier 2019-05-15===========================================");

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

						System.out
								.println("=================================== Fin Modification Ref Courrier 2019-05-15===========================================");

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
						System.out
								.println("### Insertionstion : update dossier ");

						setStatus1(true);

					}

					else {

						System.out
								.println("IF ID BOC Expéditeur différent de L'ID de Destinataire");

						// ajout ligne dans transaction
						etat = appMgr.listEtatByLibelle("Non traité").get(0);
						System.out.println("###****"
								+ transactionExpediteur.get(0).getExpdest());

						System.out
								.println("set id expdest de la Transaction de l'expéditeur d'ordre 1 :"
										+ transactionExpediteur.get(0)
												.getExpdest());
						newTransaction.setExpdest(transactionExpediteur.get(0)
								.getExpdest());

						// set id utilisateur qui execute Courrier : exmple
						// BOS1:repensable Fawzi Mnawer=>id=4
						newTransaction.setIdUtilisateur(vb.getPerson().getId());

						// set date de transaction
						newTransaction
								.setTransactionDateTransaction(new Date());

						typetransaction = appMgr.getTypeTransactionByLibelle(
								"Envoi").get(0);
						newTransaction.setTypetransaction(typetransaction);

						newTransaction.setEtat(etat);
						newTransaction.setTransactionSupprimer(true);

						int newOrderNumber = transaction.getTransactionOrdre();
						System.out.println("newOrderNumber :" + newOrderNumber);
						//newOrderNumber++;
						// newOrderNumber sera incrémenter
						newTransaction.setTransactionOrdre(newOrderNumber);

						newTransaction.setDossier(transaction.getDossier());

						newTransaction
								.setTransactionDestinationReelle(transaction
										.getTransactionDestinationReelle());
						// if (vb.getPerson().isResponsable()) {
						// newTransaction.setTransactionTypeIntervenant("sub_"
						// + String.valueOf(vb.getPerson().getId()));
						// } else if (vb.getPerson().isSecretary()) {
						// newTransaction.setTransactionTypeIntervenant("secretary_"
						// + String.valueOf(vb.getPerson().getId()));
						// }else if(vb.getPerson().isAgent()){
						// newTransaction.setTransactionTypeIntervenant("agent_"
						// + String.valueOf(vb.getPerson().getId()));
						// }
						appMgr.insert(newTransaction);
						newTransaction.setTransactionFirst(newTransaction
								.getTransactionId());
						appMgr.update(newTransaction);

						// [] Ajout Réference Courrier
						// --------------------------------------------------------------------------------------------------------------
						System.out
								.println("=================================== Modification ===========================================");
						Calendar calendar = Calendar.getInstance();
						// calendar.setTime(dateCourrier);
						System.out.println("[] courrier.getCourrierType() : "
								+ transaction.getCourrierType());

						if (transaction.getCourrierType().equals("I")) {

							// Courrier de reference "I"

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
							System.out
									.println("[Départ] => lastId  :" + lastId);

							// [] : Ref Courrier

							newTransaction
									.setCourrierDateReceptionAnnee(calendar
											.get(Calendar.YEAR));

							newTransaction.setCourrierType("D");
							courrierInformations.getCourrier().setCourrierType(
									"D");
							courrierInformations.getTransaction()
									.setCourrierType("D");

							if (lastId != null) {
								System.out
										.println("lastID != null && lastIdByTransaction == null ");
								System.out.println("last Id : " + lastId);
								newTransaction.setCourrierTypeOrdre(lastId + 1);
								courrierInformations.getCourrier()
										.setCourrierTypeOrdre(lastId + 1);

							}
							else
							{
								System.out.println("lastID == null ");
								newTransaction.setCourrierTypeOrdre(1);
								courrierInformations.getCourrier()
										.setCourrierTypeOrdre(1);

							}

						}

						if (transaction.getCourrierType().equals("A")) {
							System.out
									.println("Type Courrier <===============> A ");
							newTransaction
									.setCourrierDateReceptionAnnee(calendar
											.get(Calendar.YEAR));
							newTransaction.setCourrierType("A");
							courrierInformations.getCourrier().setCourrierType(
									"A");
							courrierInformations.getTransaction()
									.setCourrierType("A");
							System.out.println("2019-05-30 CourrierTypeOrdre "
									+ transaction.getCourrierTypeOrdre());
							newTransaction.setCourrierTypeOrdre(transaction
									.getCourrierTypeOrdre());
							courrierInformations.getCourrier()
									.setCourrierTypeOrdre(
											courrier.getCourrierTypeOrdre());

						}

						if (transaction.getCourrierType().equals("D")) {
							System.out
									.println("Type Courrier <===============> D ");

							newTransaction
									.setCourrierDateReceptionAnnee(calendar
											.get(Calendar.YEAR));

							newTransaction.setCourrierType("D");
							courrierInformations.getTransaction()
									.setCourrierType("D");
							System.out.println("2019-05-30 CourrierTypeOrdre "
									+ transaction.getCourrierTypeOrdre());
							newTransaction.setCourrierTypeOrdre(transaction
									.getCourrierTypeOrdre());
							courrierInformations.getCourrier()
									.setCourrierTypeOrdre(
											courrier.getCourrierTypeOrdre());

						}
						System.out.println("[Transaction]=>Type Courrier  :"
								+ newTransaction.getCourrierType());
						System.out
								.println("[Transaction]=>Courrier Type Ordre :"
										+ newTransaction.getCourrierTypeOrdre());

						// [] : Mettre à jour référence courrier dans
						// transaction
						newTransaction
								.setCourrierReferenceCorrespondant(newTransaction
										.getCourrierType()
										+ newTransaction.getCourrierTypeOrdre());

						appMgr.insert(newTransaction);

						// []-2019-05-17 Mettre à jour référence de Courrier
						// I==> D

						courrierInformations
								.getCourrier()
								.setCourrierReferenceCorrespondant(
										courrierInformations.getCourrier()
												.getCourrierType()
												+ courrierInformations
														.getCourrier()
														.getCourrierTypeOrdre());
						appMgr.update(courrierInformations.getCourrier());

						System.out.println("### help : "
								+ courrierInformations.getTransaction()
										.getTransactionId());
						System.out.println("### help me :"
								+ transaction.getTransactionId());
						List<Transaction> transaction2s = appMgr
								.getListTransactionByIdTransaction(transaction
										.getTransactionId());
						System.out.println("sizzzzzzzzzzzzzzze : "
								+ transaction2s.size());
						if (transaction2s != null && transaction2s.size() > 0) {
							Transaction transaction2 = transaction2s.get(0);
							System.out.println("2019-05-25 : Type Courrier "
									+ courrierInformations.getCourrier()
											.getCourrierType());
							System.out.println("2019-05-25:  Type Ordre : "
									+ courrierInformations.getCourrier()
											.getCourrierTypeOrdre());
							transaction2
									.setCourrierReferenceCorrespondant(courrierInformations
											.getCourrier().getCourrierType()
											+ courrierInformations
													.getCourrier()
													.getCourrierTypeOrdre());
							appMgr.update(transaction2);
						}

						// ****************************************************************************************************************

						System.out
								.println("===================================Fin Modification ===========================================");

						// Fin
						// --------------------------------------------------------------------------------------------------------------

						// : Fin insertion dans la table Transaction
						// ===========================================================================================
						// -------------------------------------------------------------------------------------
						vb.setTransaction(newTransaction);

						// : Début Test si Destinataire intern-Person ou
						// interne-Unité
						// expdest-------------------------------------------------------------------------------------------------
						expdest = new Expdest();
						System.out.println("Transaction Destinataire Réel ID :"
								+ transaction.getTransactionDestinationReelle()
										.getTransactionDestinationReelleId());
						TransactionDestinationReelle transactionDestinationReelle = appMgr
								.getTransactionDestinationReelById(transaction
										.getTransactionDestinationReelle()
										.getTransactionDestinationReelleId());
						System.out
								.println("Type Destination Reelle :"
										+ transactionDestinationReelle
												.getTransactionDestinationReelleTypeDestinataire());

						// [] :type Destinataire Reelle :Interne-Person
						if (transaction
								.getTransactionDestinationReelle()
								.getTransactionDestinationReelleTypeDestinataire()
								.equals("Interne-Person")) {

							System.out.println("###  Type Interne-Person ###");
							Person personDestinationReel = vb
									.getLdapOperation()
									.getPersonalisedUserById(
											transaction
													.getTransactionDestinationReelle()
													.getTransactionDestinationReelleIdDestinataire());

							// ******************BOS 2

							System.out.println("Personne Destin");
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
							System.out.println("#Connected recherché : "
									+ personneRecherche);

							getIdBocByUnit(personneRecherche
									.getAssociatedDirection());
							idBocDestinataire = idBoc;

							System.out.println("2019-05-18 : ID BOC" + idBoc);
							expdest.setTypeExpDest("Interne-Boc");
							expdest.setIdExpDestLdap(idBocDestinataire);
							String typeIntervenant = "boc_"
									+ String.valueOf(idBoc);
							appMgr.insert(expdest);

							System.out.println("2019-05-18 : affiche expdest :"
									+ expdest.getTypeExpDest());
							System.out.println("2019-05-18 id boc ! "
									+ expdest.getIdExpDestLdap());

							// XTE : Insertion
							// transactionDestination---------------------------------------------------------------------------------------------
							id.setIdTransaction(vb.getTransaction()
									.getTransactionId());
							id.setIdExpDest(expdest.getIdExpDest());
							trDest.setId(id);
							trDest.setTransactionDestIdIntervenant(idBoc);
							System.out
									.println("insertion Num 1 ==> 2019-05-17");
							trDest.setTransactionDestTypeIntervenant(typeIntervenant);
							appMgr.insert(trDest);

						} else
						// [] :type Destinataire Reelle :Interne-Unité
						// [2019-06-27]
						if (transaction
								.getTransactionDestinationReelle()
								.getTransactionDestinationReelleTypeDestinataire()
								.equals("Interne-Unité")) {

							System.out.println("###  Type Interne-Unité ###");
							// [Modification] le 2019-06-26
							// expdest.setTypeExpDest("Interne-Unité");
							Unit unit = vb
									.getLdapOperation()
									.getUnitById(
											transaction
													.getTransactionDestinationReelle()
													.getTransactionDestinationReelleIdDestinataire());
							// ******************BOS 2
							System.out.println("BOS / unite : " + unit);
							System.out.println("Personne Destin");
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
							System.out.println("#Unite recherché : "
									+ unitRecherche);
							Unit u = ldapOperation.getUnitById(unitRecherche
									.getIdUnit());
							getIdBocByUnit(u);
							// idBocDestinataire = idBoc;
							System.out
									.println("2019-06-26 idBocDestinataire : "
											+ idBoc);

							System.out.println("2019-05-18 : ID BOC" + idBoc);
							expdest.setTypeExpDest("Interne-Boc");
							System.out.println("Bonjour le 2019-06-27 : "
									+ idBoc);
							expdest.setIdExpDestLdap(idBoc);
							String typeIntervenant = "boc_"
									+ String.valueOf(idBoc);
							appMgr.insert(expdest);

							// System.out.println("unite :"
							// + unit.getShortNameUnit());
							// expdest.setIdExpDestLdap(unit.getIdUnit());
							//
							// System.out.println("2019-05-18 : affiche expdest :"
							// + expdest.getTypeExpDest());
							// System.out.println("2019-05-18 id boc ! "
							// + expdest.getIdExpDestLdap());

							// XTE : Insertion
							// transactionDestination---------------------------------------------------------------------------------------------
							id.setIdTransaction(vb.getTransaction()
									.getTransactionId());
							id.setIdExpDest(expdest.getIdExpDest());
							trDest.setId(id);
							trDest.setTransactionDestIdIntervenant(idBoc);
							System.out
									.println("insertion Num 1 ==> 2019-05-17");
							trDest.setTransactionDestTypeIntervenant(typeIntervenant);
							appMgr.insert(trDest);
						}

						// : Fin Test
						// expdest-------------------------------------------------------------------------------------------------

						// : set date de traitement de transaction
						transactionDestinationReelle
								.setTransactionDestinationReelleDateTraitement(new Date());
						appMgr.update(transactionDestinationReelle);
						appMgr.insert(expdest);

						// : Fin insert dans expdest et mise à jour de
						// transactionDestinationReelle
						// expdest-------------------------------------------------------------------------------------------------

						// id = new TransactionDestinationId();
						// trDest = new TransactionDestination();
						// System.out.println("ID transaction :"+newTransaction.getTransactionId());
						// id.setIdTransaction(newTransaction.getTransactionId());
						// System.out.println(" id expdest "+expdest.getIdExpDest());
						// id.setIdExpDest(expdest.getIdExpDest());
						// trDest.setId(id);
						// appMgr.insert(trDest);
						//
						// : Fin insert transactionDestination
						// -------------------------------------------------------------------------------------------------

						// update etat dans table transaction : etat5-> etat6

						for (Transaction transaction1 : listTransaction) {
							etat = appMgr.listEtatByLibelle("Traité").get(0);
							transaction1.setEtat(etat);
							appMgr.update(transaction1);
						}

						Calendar cal = Calendar.getInstance();
						cal.setTime(courrierInformations
								.getCourrierDateReceptionEnvoi());
						int year = cal.get(Calendar.YEAR);

						// Modification 2019-05-15
						// : Mettre a jour la reference pour indiquer qu'il est
						// un courrier de depart
						// -------------------------------------------------------------------------------------------------

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

						// Fin Modification
						appMgr.update(courrierInformations.getCourrier());
						// : Fin Mettre a jour la reference pour indiquer qu'il
						// est
						// un courrier de depart
						// -------------------------------------------------------------------------------------------------

						// : Mettre à jour Dossier et update libelle dossier
						// Courrier_Ix->Courrier_x avec x un entier
						// -------------------------------------------------------------------------------------------------
						Dossier dossier = appMgr.getDossierByIdDossier(
								transaction.getDossier().getDossierId()).get(0);
						System.out.println("test 2019-05-18 : "
								+ courrierInformations.getCourrier()
										.getIdCourrier());
						dossier.setDossierIntitule("Courrier_"
								+ courrierInformations.getCourrier()
										.getIdCourrier());
						appMgr.update(dossier);
						System.out.println("mise à jour dossier ");
					}
					setStatus1(true);
				}
			} else {
				System.out.println("esssssssssssssssaber etat 6");
			}
		} catch (Exception e) {
			setStatus2(true);
			e.printStackTrace();
		}

	}

	public int ReturnBocAssocieeUnite(Unit u) {

		if (u.getAssociatedBOC() != null)
			return u.getAssociatedBOC().getIdBOC();
		else
			return ReturnBocAssocieeUnite(u.getAssociatedUnit());

	}

	public void setTypeUserDes(String typeUserDes) {
		this.typeUserDes = typeUserDes;
	}

	public String getTypeUserDes() {
		return typeUserDes;
	}

}
