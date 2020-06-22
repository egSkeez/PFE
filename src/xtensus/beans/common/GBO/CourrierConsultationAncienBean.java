package xtensus.beans.common.GBO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
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
import xtensus.ldap.model.Person;
import xtensus.ldap.model.Unit;
import xtensus.services.ApplicationManager;
import xtensus.workflow.beans.JBPMAccessProcessBean;
import xtensus.workflow.handlers.TraitementEtapeSuivant;

@Component
@Scope("request")
public class CourrierConsultationAncienBean {

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
	private boolean showExecuteAllButton;
	private boolean hideExecuteAllButton;
	private boolean moreChoices;
	private boolean allMailChecked;
	private boolean toValidateMailChecked;
	private boolean validatedMailChecked;
	private boolean notValidatedMailChecked;
	private boolean treatedMailChecked;
	// donnees
	private List<Transmission> listTr;
	private Variables varConsultationCourrierSecretaire;
	private Variables varConsultationCourrierSubordonne;
	private Variables varConsultationCourrierSousUnite;
	private String consultationCourrierSecretaire;
	private String consultationCourrierSubordonne;
	private String consultationCourrierSousUnite;
	private List<CourrierInformations> lstCourrierEnvoyerAncien;
//	private List<CourrierInformations> lstCourrierRecuAncien;
//	private List<TransactionDestination> lstTrDestinationRecuAncien;
	private List<CourrierInformations> listCourrier;

	private Long countCourrier;
	private Long countCourrierRecu;
	private Long countCourrierEnvoyer;
	// new
	private Integer idUser;
	private String type;
	private String type1;
	private String typeSecretaire;
	private Integer typeTransmission;
	private Integer stateTraitement;
	private Date dateDebut;
	private Date dateFin;
	private CourrierInformations selectedCourrier;
	private Courrier courrier;
	private Informations info1, info2, info3;
	private List<Informations> listInfo;
	private CourrierInformations courrierInformations;
	private Transaction newTransaction;
	private int idBoc;
	private List<Integer> listIdsSousUnit;
	private List<Integer> listIdsSubordonne;
	private Variables courrierAriverToDG;
	private List<Integer> listIdBocMembers;
	private int flagueCloture;
	private int etatCloture;
	private int flagInterne;

	public CourrierConsultationAncienBean() {

	}

	@Autowired
	public CourrierConsultationAncienBean(
			@Qualifier("applicationManager") ApplicationManager appMgr) {
		this.appMgr = appMgr;
//		ldapOperation = new LdapOperation();
		listCourrier = new ArrayList<CourrierInformations>();
		listInfo = new ArrayList<Informations>();
		info1 = new Informations();
		info2 = new Informations();
		info3 = new Informations();
		listIdsSousUnit = new ArrayList<Integer>();
		listIdsSubordonne = new ArrayList<Integer>();
	}

	@PostConstruct
	public void Initialize() {
		try {
			
			// C*
			courrierAriverToDG = appMgr.listVariablesById(13).get(0);
			// C*
			System.out.println("#ANCIEN");
			hideExecuteAllButton = true;
			countCourrier = 0L;
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
			varConsultationCourrierSecretaire = appMgr.listVariablesById(3).get(0);
			varConsultationCourrierSubordonne = appMgr.listVariablesById(4).get(0);
			varConsultationCourrierSousUnite = appMgr.listVariablesById(5).get(0);
			consultationCourrierSecretaire = varConsultationCourrierSecretaire
					.getVaraiablesValeur();
			consultationCourrierSubordonne = varConsultationCourrierSubordonne
					.getVaraiablesValeur();
			consultationCourrierSousUnite = varConsultationCourrierSousUnite.getVaraiablesValeur();
			// identify connected user
			idUser = vb.getPerson().getId();
			type = "";
			type1 = "";
			if (vb.getPerson().isBoc()) {
				listIdBocMembers = new ArrayList<Integer>();
				List<Person> listBocMembers = vb.getPerson().getAssociatedBOC().getMembersBOC();
				for (Person person : listBocMembers) {
					listIdBocMembers.add(person.getId());
				}
				type = "boc_" + vb.getPerson().getAssociatedBOC().getIdBOC();
				type1 = "";
			} else if (vb.getPerson().isResponsable()) {
				type = "unit_" + vb.getPerson().getAssociatedDirection().getIdUnit();
				type1 = "sub_" + idUser;
				// NEW

				if (vb.getPerson().getAssociatedDirection().getListUnitsChildUnit().isEmpty()) {
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
								listIdsSubordonne.add(unit.getResponsibleUnit().getId());
							} catch (Exception e) {
								System.out.println("#Sub-Unit without Responsible");
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
				type1 = "unit_" + vb.getPerson().getAssociatedDirection().getIdUnit();
			} else if (vb.getPerson().isAgent()) {
				type = "agent_" + idUser;
				type1 = "";
			}
			typeTransmission = 0;
			setStateTraitement(0);
			// fin identify connected user

			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			dateDebut = calendar.getTime();

			Calendar calendar1 = Calendar.getInstance();
			calendar1.add(Calendar.DATE, 1);
			calendar1.set(Calendar.HOUR_OF_DAY, 23);
			calendar1.set(Calendar.MINUTE, 59);
			calendar1.set(Calendar.SECOND, 59);
			calendar1.set(Calendar.MILLISECOND, 999);
			dateFin = calendar1.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<CourrierInformations> searchListCourrier(
			HashMap<String, Object> filterMap, String sortField,
			boolean descending, String typeCourrierJour,
			String categorieCourrierJour, String transmissionCourrierJour,
			String typeCourrierTraitementJour, String typeCourrierValidation,
			Integer firstIndex, Integer maxResult, Boolean forRapport,
			String courrierRubrique) {
		try {
		int	jourOrAutre = 0;
		flagueCloture=vb.getFlagCloture();
		flagInterne= vb.getFlagInterne();
			lstCourrierEnvoyerAncien = new ArrayList<CourrierInformations>();
//			lstCourrierRecuAncien = new ArrayList<CourrierInformations>();
				if (vb.getSelectedListCourrier().equals("CRmois")){
					jourOrAutre= 4;
					
				}
				if (vb.getSelectedListCourrier().equals("CRannee")){
					jourOrAutre=3;
					
				}
			if (vb.getPerson().isBoc()) {
				// Etat etat = appMgr.listEtatByRef(6).get(0);
				vb.setTransmissionCourrierJourForRapportAncien(transmissionCourrierJour);
				vb.setTypeCourrierTraitementJourForRapportAncien(typeCourrierTraitementJour);
				vb.setCategorieCourrierJourForRapportAncien(categorieCourrierJour);
				long startTime = System.currentTimeMillis();
				lstCourrierEnvoyerAncien = appMgr
						.findCourrierEnvoyerBOCByCriteria(filterMap, sortField,
								descending, jourOrAutre, dateDebut, dateFin, type, type1,
								listIdBocMembers, transmissionCourrierJour,
								typeCourrierTraitementJour, firstIndex,
								maxResult, categorieCourrierJour, forRapport, vb.getDbType(),null,flagueCloture,flagInterne);
				System.out.println("list courrier ancien dure BOCT : " + (System.currentTimeMillis() - startTime));
				for (CourrierInformations courrierInformations : lstCourrierEnvoyerAncien) {
					try {
						searchExpediteurDestinataire(courrierInformations);
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("######CAUSED BY  : "
								+ courrierInformations.getCourrierReference());
						continue;
					}
				}
			} else {
				Integer courrierRubriqueId = Integer.valueOf(courrierRubrique);
				vb.setTypeCourrierJourForRapportAncien(typeCourrierJour);
				vb.setTypeCourrierValidationJourForRapportAncien(typeCourrierValidation);
				vb.setCourrierRubrique(courrierRubrique);
				// if (typeCourrierJour.equals("Tous")
				// || typeCourrierJour.equals("Envoyes")) {
				// if (!typeCourrierValidation.equals("Avalider")) {
				long startTime = System.currentTimeMillis();
				
				System.out.println("#######$$$$ dateDebut " + dateDebut);
				System.out.println("#######$$$$ dateDebut " + dateFin);
				int i=				vb.getFlagCloture();
				lstCourrierEnvoyerAncien = appMgr
						.findCourrierEnvoyerANDRecuByCriteria(vb.getPerson()
								.isResponsable(), listIdsSousUnit,
								listIdsSubordonne, filterMap, sortField,
								descending, consultationCourrierSecretaire,
								consultationCourrierSubordonne,
								consultationCourrierSousUnite, jourOrAutre, dateDebut,
								dateFin, type, type1, typeSecretaire, idUser,
								typeTransmission, typeCourrierValidation,
								firstIndex, maxResult, forRapport,
								courrierRubriqueId, typeCourrierJour, vb.getDbType(),null,flagueCloture,flagInterne);
				System.out.println("list courrier ancien dure RESPONSIBLE : " + (System.currentTimeMillis() - startTime));
				for (CourrierInformations courrierInformations : lstCourrierEnvoyerAncien) {
					try {
						searchExpediteurDestinataire(courrierInformations);
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("######CAUSED BY : "
								+ courrierInformations.getCourrierReference());
						continue;
					}
				}
				System.out.println("list courrier ancien dure RESPONSIBLE with Exp & Dest : " + (System.currentTimeMillis() - startTime));
				// countCourrier = countCourrier + CountCourrier(2,
				// dateDebut, dateFin, type, type1, idUser,
				// typeTransmission, typeCourrierValidation);

				// }else {
				// lstCourrierEnvoyerAncien = new
				// ArrayList<CourrierInformations>();
				// }
				// }
				// if (typeCourrierJour.equals("Tous")
				// || typeCourrierJour.equals("Recu")) {
				//
				// lstTrDestinationRecuAncien = appMgr
				// .findCourrierRecuByCriteria(vb.getPerson()
				// .isResponsable(), listIdsSousUnit,
				// listIdsSubordonne, filterMap, sortField,
				// descending, consultationCourrierSecretaire,
				// consultationCourrierSubordonne,
				// consultationCourrierSousUnite, 2,
				// dateDebut, dateFin, type, type1,
				// typeSecretaire, idUser, typeTransmission,
				// typeCourrierValidation, firstRecu,
				// numberOfRowsRecu, forRapport,
				// courrierRubriqueId);
				// for (TransactionDestination transactionDestination :
				// lstTrDestinationRecuAncien) {
				// lstCourrierRecuAncien
				// .add(searchExpediteurDestinataireAndExplodeDataTransactionDestination(transactionDestination));
				// }
				//
				// }
			}
			listCourrier.clear();
			listCourrier.addAll(lstCourrierEnvoyerAncien);
			return listCourrier;
			// if (vb.getPerson().isBoc()) {
			// listCourrier.addAll(lstCourrierEnvoyerAncien);
			// } else {
			// if (typeCourrierJour.equals("Tous")) {
			// listCourrier.addAll(lstCourrierEnvoyerAncien);
			// listCourrier.addAll(lstCourrierRecuAncien);
			// } else if (typeCourrierJour.equals("Recu")) {
			// listCourrier.addAll(lstCourrierRecuAncien);
			// } else {
			// listCourrier.addAll(lstCourrierEnvoyerAncien);
			// }
			// }
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	

	public void searchExpediteurDestinataire(CourrierInformations courrierInformations)
			throws Exception {
//		Transaction transaction = appMgr.getListTransactionByIdTransaction(courrierInformations.getTransactionID()).get(0);
//		courrierInformations.setTransaction(transaction);
//		System.out.println("transaction : " + courrierInformations.getTransactionID() + " - courrier : " + courrierInformations.getCourrierID() + " - " + courrierInformations.getCourrierReference());
		Integer etatID = courrierInformations.getEtatID();
		String expType;
		Integer expTypeUser;
		Integer expLdap;
		String expNom;
		String expPrenom;
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
		if (expType.equals("Interne-Person")) {
			if (expLdap.equals(vb.getPerson().getId())) {
				courrierInformations.setCourrierRecu(0);
			}
			Person person = vb.getHashMapAllUser().get(expLdap);
			expediteur = person.getCn();
		} else if (expType.equals("Interne-Unité")) {
			if (vb.getPerson().isResponsable()
					&& expLdap.equals(vb.getPerson().getAssociatedDirection().getIdUnit())) {
				courrierInformations.setCourrierRecu(0);
			}
			Unit unit = vb.getHashMapUnit().get(expLdap);
			expediteur = unit.getNameUnit();
		} else if (expType.equals("Interne-Boc")) {
			expediteur = vb.getCentralBoc().getNameBOC();
		} else if (expType.equals("Externe")) {
			// C* pour que le bouton executer soit activé pour les courriers d'arrivé
			// provisoire .. juste pour activer l'execution des courrier arrivé pour le BOCT
			if (courrierAriverToDG.getVaraiablesValeur().equals("Non")) {
				if (vb.getPerson().getAssociatedBOC() != null && courrierInformations.getTransactionOrdre() == null && etatID.equals(5)) {
					courrierInformations.setCourrierAValider(1);
				}
			}
			// provisoire .. juste pour activer l'execution des courrier arrivé pour le BOCT
			// C* pour que le bouton executer soit activé pour les courriers d'arrivé
			if (expTypeUser == 1) {
				expediteur = expNom + " " + expPrenom + " (PP)";
			} else {
				expediteur = expNom + " (PM)";
			}
		}
		courrierInformations.setCourrierExpediteur(expediteur);
		// remplissage de l'objet TransactionDest pour l'execution du BOC ou la validation des responsable
		List<TransactionDestination> listTransactionDestination = appMgr
				.getListTransactionDestinationByIdTransaction(courrierInformations.getTransactionID());// valeur ancien #firstTransaction.getTransactionId()#

		if (!listTransactionDestination.isEmpty()) {
			courrierInformations.setTransactionDestination(listTransactionDestination
					.get(listTransactionDestination.size() - 1));
		}
		if ((etatID.equals(2) || etatID.equals(10)) && !vb.getPerson().isBoc() && !courrierInformations.getIdUtilisateur().equals(vb.getPerson().getId())) {
			courrierInformations.setCourrierAValider(1);
		}
		// pour activer l'execution des courrier qui suit un workflow pour le boct et juste la premiere execution
		if (vb.getPerson().isBoc() && courrierInformations.getCourrierCircuit().equals("workflow")) {
			if(etatID.equals(10) && courrierInformations.getTransactionOrdre().equals(1)){
				courrierInformations.setCourrierAValider(1);	
			}
		}
		// pour activer l'execution des courrier qui suit un workflow pour le boct et juste la premiere execution
		if (!courrierInformations.getIdUtilisateur().equals(vb.getPerson().getId())) {
			courrierInformations.setCourrierRecu(1);
		}
		// detinataire reel *
		StringBuilder destinataire = new StringBuilder("");
		String unitName;
		List<Transaction> allTransactions = appMgr.getTransactionByIdDossier(courrierInformations.getDossierID());
		courrierInformations.setCourrierAllTransactions(allTransactions);
		Transaction firstTransaction = allTransactions.get(allTransactions.size() -1);
		Expdest expdestExpediteurREEL = appMgr.getListExpDestByIdExpDest(firstTransaction.getExpdest().getIdExpDest()).get(0);
		courrierInformations.setExpDest(expdestExpediteurREEL);
		if (courrierInformations.getDestReelList() != null) {
			List<String> destReelList = new ArrayList<String>(Arrays.asList(courrierInformations.getDestReelList().split("\\|", -1)));
			for (int i = 0; i < destReelList.size(); i++) {
				List<String> destReelElement = new ArrayList<String>(Arrays.asList(destReelList.get(i).split(";", -1)));
//				Integer transactionId = 0;
//				if(!destReelElement.get(0).equals("")) {
//					transactionId = Integer.valueOf(destReelElement.get(0));
//				}
				Integer idExpDest = 0;
				if(!destReelElement.get(1).equals("")) {
					idExpDest = Integer.valueOf(destReelElement.get(1));
				}
				String type = destReelElement.get(2);
				Integer ldap = 0;
				if(!destReelElement.get(3).equals("")) {
					ldap = Integer.valueOf(destReelElement.get(3));
				}
				String nom = destReelElement.get(4);
				String prenom = destReelElement.get(5);
				Integer typeUser = 0;
				if(!destReelElement.get(6).equals("")) {
					typeUser = Integer.valueOf(destReelElement.get(6));
				}
				Integer idDestReelLdap = 0;
				if(!destReelElement.get(7).equals("")) {
					idDestReelLdap = Integer.valueOf(destReelElement.get(7));
				}
				String destReelType = destReelElement.get(8);
				if (idDestReelLdap != 0) {
					if (courrierInformations.getCourrierCircuit().equals("workflow")) {
						try {
							Unit unitDestinataireReel = vb.getHashMapUnit().get(idDestReelLdap);
							unitName = unitDestinataireReel.getNameUnit();
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
							if (!destinataire.toString().contains(unit.getNameUnit())){
							  destinataire.append(" / ");
							  destinataire.append(unit.getNameUnit());
							}
						} else if(destReelType.equals("Interne-Person")) {
							Person person = vb.getHashMapAllUser().get(idDestReelLdap);
							if (!destinataire.toString().contains(person.getCn())) {
								destinataire.append(" / ");
								destinataire.append(person.getCn());
							}
						} else if(destReelType.equals("Externe")) {
							if (vb.getPerson().isBoc() && !etatID.equals(6)) {
								courrierInformations.setCourrierAValider(1);
							}
							Expdestexterne destReelExterne = appMgr.getExpediteurById(idDestReelLdap).get(0);
							if (destReelExterne.getTypeutilisateur().getTypeUtilisateurId().equals(1)) {
								String dest = destReelExterne.getExpDestExternePrenom() + " " + destReelExterne.getExpDestExterneNom();
								if(!destinataire.toString().contains(dest)){
								  destinataire.append(" / ");
								  destinataire.append(dest);
								}
							} else if (destReelExterne.getTypeutilisateur().getTypeUtilisateurId().equals(2)) {
								String dest = destReelExterne.getExpDestExterneNom();
								if (!destinataire.toString().contains(dest)) {
									destinataire.append(" / ");
									destinataire.append(destReelExterne.getExpDestExterneNom());
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
							if (idExpDest.equals(transactionDestination.getId().getIdExpDest())) {
								destinataire.append(" / ");
								if (type.equals("Interne-Person")) {
									if (ldap.equals(vb.getPerson().getId())) {
										courrierInformations.setCourrierRecu(1);
										courrierInformations.setTransactionDestination(transactionDestination);
										destinataire = new StringBuilder(" / ");
										Person person = vb.getHashMapAllUser().get(ldap);
										destinataire.append(person.getCn());
										break;
									}
									Person person = vb.getHashMapAllUser().get(ldap);
									if (!destinataire.toString().contains(person.getCn())) {
										destinataire.append(person.getCn());
									}
								} else if (type.equals("Interne-Unité")) {
									if (vb.getPerson().isResponsable() && ldap.equals(vb.getPerson().getAssociatedDirection().getIdUnit())) {
										courrierInformations.setCourrierRecu(1);
										courrierInformations.setTransactionDestination(transactionDestination);
										destinataire = new StringBuilder(" / ");
										Unit unit = vb.getHashMapUnit().get(ldap);
										destinataire.append(unit.getNameUnit());
										break;
									}
									Unit unit = vb.getHashMapUnit().get(ldap);
									if (!destinataire.toString().contains(unit.getNameUnit())) {
										destinataire.append(unit.getNameUnit());
									}
								} else if (type.equals("Interne-Boc")) {
									if (vb.getPerson().isBoc()) {
										courrierInformations.setCourrierRecu(1);
										courrierInformations.setTransactionDestination(transactionDestination);
									}
									destinataire.append(vb.getCentralBoc().getNameBOC());
								} else if (type.equals("Externe")) {
									if (typeUser.equals(1)) {
										if (!destinataire.toString().contains(nom + " " + prenom + " (PP)")) {
											destinataire.append(nom + " " + prenom + " (PP)");
										}
									} else {
										if (!destinataire.toString().contains(nom + " (PM)")) {
											destinataire.append(nom + " (PM)");
										}
									}
								}
							}
						}
					}
					if (courrierInformations.getCourrierRecu() == 1 && (etatID.equals(10) || etatID.equals(2))) {
						courrierInformations.setCourrierAValider(1);
					} else {
						// provisoire .. juste pour activer l'execution des courrier arrivé pour le BOCT
						if (courrierAriverToDG.getVaraiablesValeur().equals("Non")) {
							if (!vb.getPerson().isBoc()) {
								courrierInformations.setCourrierAValider(0);
							}
						}
						// provisoire .. juste pour activer l'execution des courrier arrivé pour le BOCT
					}
					if (vb.getPerson().getAssociatedBOC() != null && courrierInformations.getCourrierRecu() == 1 && etatID.equals(5) && etatID.equals(2)) {
						courrierInformations.setCourrierAValider(1);
					}
				}
			}
		}
		destinataire.delete(0, 3);
		courrierInformations.setCourrierDestinataireReelle(destinataire.toString());
	}

	// commenté aprés la modification de la liste courrier pour qu'il affiche que les courriers et non pas les transactions 2015/01/29
	/*public void searchExpediteurDestinataire(
			CourrierInformations courrierInformations) throws Exception {
		// courrierInformations.setCourrierRecu(0);
		Courrier courrier = appMgr.listCourrierByIdTransaction(
				courrierInformations.getTransactionID()).get(0);
		courrierInformations.setCourrier(courrier);
		Transaction transaction = appMgr.getListTransactionByIdTransaction(
				courrierInformations.getTransactionID()).get(0);
		courrierInformations.setTransaction(transaction);
		Etat etat = appMgr.listEtatByRef(transaction.getEtat().getEtatId())
				.get(0);
		// TO replace info requete
		Nature nature =  appMgr.getNatureById(courrier.getNature().getNatureId()).get(0);
		courrierInformations.setCourrierNature(nature.getNatureLibelle());
		List<Transaction> allTransactions =  appMgr.getTransactionByIdDossier(transaction.getDossier().getDossierId());
		Transaction firstTransaction = allTransactions.get(allTransactions.size() -1);
		Expdest expdestExpediteurREEL = appMgr.getListExpDestByIdExpDest(firstTransaction.getExpdest().getIdExpDest()).get(0);
		courrierInformations.setExpDest(expdestExpediteurREEL);
		// TO replace info requete
		// courrierInformations.setTypeCourrier(getCategorieCourrier(transaction,
		// true));
		// expediteur
		StringBuilder expediteur = new StringBuilder("");
		if (courrierInformations.getExpDest().getTypeExpDest()
				.equals("Interne-Person")) {
			if (courrierInformations.getExpDest().getIdExpDestLdap()
					.equals(vb.getPerson().getId())) {
				courrierInformations.setCourrierRecu(0);
			}
//			expediteur.append(ldapOperation.getCnById(
//					ldapOperation.CONTEXT_USER, "uid", courrierInformations
//							.getExpDest().getIdExpDestLdap()));
			Person person = vb.getHashMapAllUser().get(
					courrierInformations.getExpDest().getIdExpDestLdap());
			expediteur.append(person.getCn());

		} else if (courrierInformations.getExpDest().getTypeExpDest()
				.equals("Interne-Unité")) {
			if (vb.getPerson().isResponsable()
					&& courrierInformations
							.getExpDest()
							.getIdExpDestLdap()
							.equals(vb.getPerson().getAssociatedDirection()
									.getIdUnit())) {
				courrierInformations.setCourrierRecu(0);
			}
//			expediteur.append(ldapOperation.getCnById(
//					ldapOperation.CONTEXT_UNIT, "departmentNumber",
//					courrierInformations.getExpDest().getIdExpDestLdap()));
			Unit unit = vb.getHashMapUnit().get(
					courrierInformations.getExpDest().getIdExpDestLdap());
			expediteur.append(unit.getNameUnit());
		} else if (courrierInformations.getExpDest().getTypeExpDest()
				.equals("Interne-Boc")) {
//			expediteur.append(ldapOperation.getCnById(
//					ldapOperation.CONTEXT_BOC, "departmentNumber",
//					courrierInformations.getExpDest().getIdExpDestLdap()));
			// Person person =
			// vb.getHashMapAllUser().get(courrierInformations.getExpDest().getIdExpDestLdap());
			// expediteur.append(person.getCn());
			expediteur.append(vb.getCentralBoc().getNameBOC());
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
			// TransactionDestination transactionDestination =
			// listTransactionDestination
			// .get(0);
			// courrierInformations
			// .setTransactionDestination(transactionDestination);
			for (TransactionDestination transactionDestination : listTransactionDestination) {
				destinataire.append(" / ");
				Expdest expDest = appMgr.getListExpDestByIdExpDest(
						transactionDestination.getId().getIdExpDest()).get(0);
				if (expDest.getTypeExpDest().equals("Interne-Person")) {
					if (expDest.getIdExpDestLdap().equals(
							vb.getPerson().getId())) {
						courrierInformations.setCourrierRecu(1);
						courrierInformations
								.setTransactionDestination(transactionDestination);
						destinataire = new StringBuilder(" / ");
//						destinataire.append(ldapOperation.getCnById(
//								ldapOperation.CONTEXT_USER, "uid",
//								expDest.getIdExpDestLdap()));
						Person person = vb.getHashMapAllUser().get(expDest.getIdExpDestLdap());
						destinataire.append(person.getCn());
						break;
					}
//					destinataire.append(ldapOperation.getCnById(
//							ldapOperation.CONTEXT_USER, "uid",
//							expDest.getIdExpDestLdap()));
					Person person = vb.getHashMapAllUser().get(expDest.getIdExpDestLdap());
					destinataire.append(person.getCn());
				} else if (expDest.getTypeExpDest().equals("Interne-Unité")) {
					if (vb.getPerson().isResponsable()
							&& expDest.getIdExpDestLdap().equals(
									vb.getPerson().getAssociatedDirection()
											.getIdUnit())) {
						courrierInformations.setCourrierRecu(1);
						courrierInformations
								.setTransactionDestination(transactionDestination);
						destinataire = new StringBuilder(" / ");
//						destinataire.append(ldapOperation.getCnById(
//								ldapOperation.CONTEXT_UNIT, "departmentNumber",
//								expDest.getIdExpDestLdap()));
						Unit unit = vb.getHashMapUnit().get(expDest.getIdExpDestLdap());
						destinataire.append(unit.getNameUnit());
						break;
					}
//					destinataire.append(ldapOperation.getCnById(
//							ldapOperation.CONTEXT_UNIT, "departmentNumber",
//							expDest.getIdExpDestLdap()));
					Unit unit = vb.getHashMapUnit().get(expDest.getIdExpDestLdap());
					destinataire.append(unit.getNameUnit());
				} else if (expDest.getTypeExpDest().equals("Interne-Boc")) {
					if (vb.getPerson().isBoc()) {
						courrierInformations.setCourrierRecu(1);
						courrierInformations
								.setTransactionDestination(transactionDestination);
					}
//					destinataire.append(ldapOperation.getCnById(
//							ldapOperation.CONTEXT_BOC, "departmentNumber",
//							expDest.getIdExpDestLdap()));
					destinataire.append(vb.getCentralBoc().getNameBOC());
				} else if (expDest.getTypeExpDest().equals("Externe")) {
					if (expDest.getExpdestexterne().getTypeutilisateur()
							.getTypeUtilisateurLibelle().equals("PP")) {
						destinataire
								.append(expDest.getExpdestexterne()
										.getExpDestExterneNom())
								.append(" ")
								.append(expDest.getExpdestexterne()
										.getExpDestExternePrenom())
								.append(" (PP)");
					} else {
						destinataire.append(
								expDest.getExpdestexterne()
										.getExpDestExterneNom())
								.append(" (PM)");
					}
				}
			}

			destinataire.delete(0, 3);
			courrierInformations.setCourrierDestinataireReelle(destinataire
					.toString());
			// && transaction.getTransactionOrdre().equals(1)
			if (courrierInformations.getCourrierRecu() == 1
					&& (etat.getEtatId().equals(10) || etat.getEtatId().equals(
							2))) {
				courrierInformations.setCourrierAValider(1);
			} else {
				courrierInformations.setCourrierAValider(0);
			}
			if(vb.getPerson().getAssociatedBOC() != null  && courrierInformations.getCourrierRecu() == 1 && etat.getEtatId().equals(5)  ){
				courrierInformations.setCourrierAValider(1);
			}

		}
	}*/
	// commenté aprés la modification de la liste courrier pour qu'il affiche que les courriers et non pas les transactions 2015/01/29

	/*public CourrierInformations searchExpediteurDestinataireAndExplodeDataTransactionDestination(
			TransactionDestination transactionDestination) {
		CourrierInformations courrierInformations = new CourrierInformations();
		courrierInformations.setTransactionDestination(transactionDestination);
		Courrier courrier = appMgr.listCourrierByIdTransaction(
				transactionDestination.getId().getIdTransaction()).get(0);
		courrierInformations.setCourrierReference(courrier
				.getCourrierReferenceCorrespondant());
		courrierInformations.setCourrierObjet(courrier.getCourrierObjet());
		courrierInformations.setCourrier(courrier);
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
		// courrierInformations.setTypeCourrier(getCategorieCourrier(
		// transactionDestination, true));
		return courrierInformations;
	}*/

	/*private String getCategorieCourrier(Transaction transaction, boolean isMail) {
		String result = "";
		String[] type = new String[2];
		if (vb.getPerson().isResponsable()) {
			if (transaction.getTransactionTypeIntervenant().contains("sub")) {
				type = transaction.getTransactionTypeIntervenant().split("_");
				if (Integer.parseInt(type[1]) == vb.getPerson().getId()) {
					if (isMail) {
						result = "A. Mes Propres Courriers Envoyés";
					} else {
						result = "A. Mes Propres Dossiers Envoyés";
					}
				} else {

					if (isMail) {
						result = "C. Les Courriers Envoyés de Mes Subordonnées";
					} else {
						result = "C. Les Dossiers Envoyés de Mes Subordonnées";
					}

				}
			} else if (transaction.getTransactionTypeIntervenant().contains(
					"unit")) {
				type = transaction.getTransactionTypeIntervenant().split("_");
				if (Integer.parseInt(type[1]) == vb.getPerson()
						.getAssociatedDirection().getIdUnit()) {
					if (isMail) {
						result = "E. Les Courriers Envoyés de Mon Unité";
					} else {
						result = "E. Les Dossiers Envoyés de Mon Unité";
					}
				} else {

					if (isMail) {
						result = "G. Les Courriers Envoyés de Mes Sous-Unités";
					} else {
						result = "G. Les Dossiers Envoyés de Mes Sous-Unités";
					}

				}
			} else if (transaction.getTransactionTypeIntervenant().contains(
					"secretary")) {
				if (isMail) {
					result = "I. Les Courriers Envoyés de Ma Secrétaire";
				} else {
					result = "I. Les Dossiers Envoyés de Ma Secrétaire";
				}
			}
		} else if (vb.getPerson().isSecretary()) {
			if (transaction.getTransactionTypeIntervenant().contains(
					"secretary")) {
				if (isMail) {
					result = "A. Mes Propres Courriers Envoyés";
				} else {
					result = "A. Mes Propres Dossiers Envoyés";
				}
			} else {
				if (isMail) {
					result = "E. Les Courriers Envoyés de Mon Unité";
				} else {
					result = "E. Les Dossiers Envoyés de Mon Unité";
				}
			}
		} else if (vb.getPerson().isAgent()) {
			if (transaction.getTransactionTypeIntervenant().contains("agent")) {
				if (isMail) {
					result = "A. Mes Propres Courriers Envoyés";
				} else {
					result = "A. Mes Propres Dossiers Envoyés";
				}
			} else {
				if (isMail) {
					result = "E. Les Courriers Envoyés de Mon Unité";
				} else {
					result = "E. Les Courriers Envoyés de Mon Unité";
				}
			}
		} else {
			result = "A. Les Courriers du Bureau d'Ordre";
			// if(transaction.getTransactionTypeIntervenant() != null){
			// result = "A. Les Courriers de Départ";
			// }else{
			// result = "B. Les Courriers d'Arrivée";
			// }

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
						result = "B. Mes Propres Courriers Reéus";
					} else {
						result = "B. Mes Propres Dossiers Reéus";
					}
				} else {
					if (isMail) {
						result = "D. Les Courriers Reéus de Mes Subordonnées";
					} else {
						result = "D. Les Dossiers Reéus de Mes Subordonnées";
					}
				}
			}

		} else if (transactionDestination.getTransactionDestTypeIntervenant()
				.contains("unit")) {
			type = transactionDestination.getTransactionDestTypeIntervenant()
					.split("_");
			if (Integer.parseInt(type[1]) == vb.getPerson()
					.getAssociatedDirection().getIdUnit()) {
				if (isMail) {
					result = "F. Les Courriers Reéus de Mon Unité";
				} else {
					result = "F. Les Dossiers Reéus de Mon Unité";
				}
			} else {
				if (isMail) {
					result = "H. Les Courriers Reéus de Mes Sous-Unités";
				} else {
					result = "H. Les Dossiers Reéus de Mes Sous-Unités";
				}
			}
		} else if (transactionDestination.getTransactionDestTypeIntervenant()
				.contains("secretary")) {
			if (isMail) {
				result = "J. Les Courriers Reéus de Ma Secrétaire";
			} else {
				result = "J. Les Dossiers Reéus de Ma Secrétaire";
			}
		}// else if (transactionDestination
			// .getTransactionDestTypeIntervenant().contains("agent")) {
		//
		// if (isMail) {
		// result = "D. Les Courriers Reéus de Mes Agents";
		// } else {
		// result = "D. Les Dossiers Reéus de Mes Agents";
		// }
		// }
		else if (vb.getPerson().isSecretary()) {
			if (transactionDestination.getTransactionDestTypeIntervenant()
					.contains("secretary")) {
				if (isMail) {
					result = "B. Mes Propres Courriers Reéus";
				} else {
					result = "B. Mes Propres Dossiers Reéus";
				}
			} else {
				if (isMail) {
					result = "F. Les Courriers Reéus de Mon Unité";
				} else {
					result = "F. Les Dossiers Reéus de Mon Unité";
				}
			}
		} else if (vb.getPerson().isAgent()) {
			if (transactionDestination.getTransactionDestTypeIntervenant()
					.contains("agent")) {
				if (isMail) {
					result = "B. Mes Propres Courriers Reéus";
				} else {
					result = "B. Mes Propres Dossiers Reéus";
				}
			} else {
				if (isMail) {
					result = "F. Les Courriers Reéus de Mon Unité";
				} else {
					result = "F. Les Dossiers Reéus de Mon Unité";
				}
			}
		} else {
			result = "A. Les Courriers Reéus du Bureau d'Ordre";
		}

		return result;
	}*/

	public void getSelectionRow() {
		try {
			vb.setSelectedListCourrier("CRannee");
			vb.setDestinataireReel("");
			Transaction transaction = new Transaction();
			CourrierInformations consulterInformations = selectedCourrier;
			if (consulterInformations.getCourrier() == null) {
				consulterInformations.setCourrier(appMgr.getCourrierByIdCourrier(consulterInformations.getCourrierID()).get(0));
			}
			if (consulterInformations.getTransaction() == null) {
				consulterInformations.setTransaction(appMgr.getListTransactionByIdTransaction(consulterInformations.getTransactionID()).get(0));
			}
			vb.setCourDossConsulterInformations(consulterInformations);// a commenté si on a renversé l'ancienne liste de courriers
			courrier = consulterInformations.getCourrier();
			vb.setCourrier(courrier);
			transaction = consulterInformations.getTransaction();
			List<TransactionDestination> listTransactionDestination = appMgr
				.getListTransactionDestinationByIdTransaction(consulterInformations.getTransactionID());// valeur ancien #firstTransaction.getTransactionId()#
			if (!listTransactionDestination.isEmpty()) {
				vb.setTransactionDestination(listTransactionDestination
						.get(listTransactionDestination.size() - 1));// 2015-02-27
				consulterInformations.setTransactionDestination(listTransactionDestination
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
								transaction.setTransactionDateConsultation(new Date());
								appMgr.update(transaction);
							}
							// ajouté lors #// C *# pour que la date de consultation des courriers arrivé par le boct soit enregistré
							TransactionDestination transactionDestination = consulterInformations
									.getTransactionDestination();
							Expdest expdest = appMgr.getListExpDestByIdExpDest(transactionDestination.getId().getIdExpDest()).get(0);
							if(vb.getPerson().isBoc() && expdest.getTypeExpDest().equals("Interne-Boc")){
								if (transactionDestination.getTransactionDestDateConsultation() == null) {
									transactionDestination
											.setTransactionDestDateConsultation(new Date());
									appMgr.update(transactionDestination);
								}
							}
							vb.setTransactionDestination(transactionDestination);
						}
					} else {
						if (transaction.getTransactionDateConsultation() == null) {
							transaction.setTransactionDateConsultation(new Date());
							appMgr.update(transaction);
						}
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			} else {
				if (consulterInformations.getCourrierRecu() == 1
						&& consulterInformations.getTransactionDestination()
								.getTransactionDestDateConsultation() == null) {
					TransactionDestination transactionDestination = new TransactionDestination();
					transactionDestination = consulterInformations.getTransactionDestination();
					transactionDestination
							.setTransactionDestDateConsultation(new Date());
					appMgr.update(transactionDestination);
					vb.setTransactionDestination(transactionDestination);
					// chargement variable log & notification
					chargementNotification(consulterInformations);
				} else if (transaction.getIdUtilisateur() == vb.getPerson().getId()
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
			vb.setDestinataireReel(consulterInformations.getCourrierDestinataireReelle());
			// ** expediteur reel
			List<TransactionAnnotation> annotations = new ArrayList<TransactionAnnotation>();
			annotations = appMgr.getAnnotationByIdTransaction(consulterInformations
					.getTransactionID());
			int lastIndex;
			int refAnnotation;
			String result = "";
			for (TransactionAnnotation ta : annotations) {
				refAnnotation = ta.getId().getIdAnnotation();
				result += appMgr.getAnnotationByIdAnotation(refAnnotation).get(0)
						.getAnnotationLibelle() + " / ";
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

	public void getSelectionRowForValidate() {
		try {
			vb.setSelectedListCourrier("CRancien");
			Transaction transaction = new Transaction();
			CourrierInformations courrierInformations = selectedCourrier;
			if (courrierInformations.getCourrier() == null) {
				courrierInformations.setCourrier(appMgr.getCourrierByIdCourrier(courrierInformations.getCourrierID()).get(0));
			}
			if (courrierInformations.getTransaction() == null) {
				courrierInformations.setTransaction(appMgr.getListTransactionByIdTransaction(courrierInformations.getTransactionID()).get(0));
			}
			vb.setCourDossConsulterInformations(courrierInformations);// commenter si on renverse l'ancienne liste de courriers
			courrier = courrierInformations.getCourrier();
			vb.setCourrier(courrier);
			transaction = courrierInformations.getTransaction();
			// if (lstCourrierRecuAncien.contains(courrierInformations)
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
			// } else if (lstCourrierEnvoyerAncien.contains(courrierInformations)
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
			vb.setDestinataireReel(courrierInformations.getCourrierDestinataireReelle());
			// **
//			int refDossier = transaction.getDossier().getDossierId();
//			List<Transaction> listTransaction = appMgr
//					.getTransactionByIdDossier(refDossier);
//			Transaction firstTransaction = listTransaction.get(listTransaction
//					.size() - 1);
//			Expdest expDest;
//			expDest = firstTransaction.getExpdest();
//			if (expDest.getTypeExpDest().equals("Interne-Person")) {
//				vb.setCopyExpReelNom(ldapOperation.getCnById(
//						ldapOperation.CONTEXT_USER, "uid",
//						expDest.getIdExpDestLdap()));
//			} else if (expDest.getTypeExpDest().equals("Interne-Unité")) {
//
//				vb.setCopyExpReelNom(ldapOperation.getUnitById(
//						expDest.getIdExpDestLdap()).getShortNameUnit());
//
//			} else if (expDest.getTypeExpDest().equals("Interne-Boc")) {
//				vb.setCopyExpReelNom(ldapOperation.getBocById(
//						expDest.getIdExpDestLdap()).getNameUnit());
//			} else if (expDest.getTypeExpDest().equals("Externe")) {
//				if (expDest.getExpdestexterne().getTypeutilisateur()
//						.getTypeUtilisateurLibelle().equals("PP")) {
//					vb.setCopyExpReelNom(expDest.getExpdestexterne()
//							.getExpDestExterneNom()
//							+ " "
//							+ expDest.getExpdestexterne()
//									.getExpDestExternePrenom() + " (PP)");
//				} else {
//					vb.setCopyExpReelNom(expDest.getExpdestexterne()
//							.getExpDestExterneNom() + " (PM)");
//				}
//			}
			//**
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
			vb.setCopyDestNom(courrierInformations
					.getCourrierDestinataireReelle());
			vb.setCopyExpNom(courrierInformations.getCourrierExpediteur());
			// vb.setCopyCourrierCommentaire(consulterInformations.getCourrierCommentaire());
			vb.setCopyCourrierCommentaire(courrierInformations.getCourrier()
					.getCourrierCommentaire());
			vb.setCopyOtherDest(courrierInformations
					.getCourrierAutreDestinataires());
			vb.setTransaction(transaction);
			
			// destinataire reel
			// C*
//			if(transaction.getTransactionDestinationReelle() != null){
//			TransactionDestinationReelle transactionDestinationReelle = appMgr.getTransactionDestinationReelById(transaction.getTransactionDestinationReelle().getTransactionDestinationReelleId());
//			if(transactionDestinationReelle != null){
//				if (courrier.getCourrierCircuit().equals("workflow")) {
//					Unit unitDestinataireReel = ldapOperation.getUnitById(transactionDestinationReelle.getTransactionDestinationReelleIdDestinataire());
//					vb.setDestinataireReel(unitDestinataireReel.getNameUnit());
//				}else{
//					if(transactionDestinationReelle.getTransactionDestinationReelleTypeDestinataire().equals("Interne-Unité")){
//						Unit unit = vb.getHashMapUnit().get(
//								transactionDestinationReelle.getTransactionDestinationReelleIdDestinataire());
//						vb.setDestinataireReel(unit.getNameUnit());
//					}else if(transactionDestinationReelle.getTransactionDestinationReelleTypeDestinataire().equals("Interne-Person")){
//						Person person = vb.getHashMapAllUser().get(
//								transactionDestinationReelle.getTransactionDestinationReelleIdDestinataire());
//						vb.setDestinataireReel(person.getCn());
//					}else if(transactionDestinationReelle.getTransactionDestinationReelleTypeDestinataire().equals("Externe")){
//						Expdestexterne destReelExterne = appMgr.getExpediteurById(transactionDestinationReelle.getTransactionDestinationReelleIdDestinataire()).get(0);
//						if(destReelExterne.getTypeutilisateur().equals(1)){
//							vb.setDestinataireReel(destReelExterne.getExpDestExternePrenom() + " " + destReelExterne.getExpDestExterneNom());
//						}else if (destReelExterne.getTypeutilisateur().equals(2)) {
//							vb.setDestinataireReel(destReelExterne.getExpDestExterneNom());
//						}
//					}else{
//						vb.setDestinataireReel("--------------------------------");
//					}
//				}
//			}
//			}else{
//				vb.setDestinataireReel(vb.getCopyDestNom());
//			}
			// C*
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void getSelectionRowForResponse() {
		try {
			vb.setSelectedListCourrier("CRancien");
			Transaction transaction = new Transaction();
			CourrierInformations courrierInformations = selectedCourrier;
			if (courrierInformations.getCourrier() == null) {
				courrierInformations.setCourrier(appMgr.getCourrierByIdCourrier(courrierInformations.getCourrierID()).get(0));
			}
			if (courrierInformations.getTransaction() == null) {
				courrierInformations.setTransaction(appMgr.getListTransactionByIdTransaction(courrierInformations.getTransactionID()).get(0));
			}
			courrier = courrierInformations.getCourrier();
			vb.setCourDossConsulterInformations(courrierInformations); // é commenté si on a renversé l'ancienne liste de courriers
			vb.setCourrier(courrier);
			transaction = courrierInformations.getTransaction();
			// if (lstCourrierRecuAncien.contains(courrierInformations)
			// && courrierInformations.getTransactionDestination()
			// .getTransactionDestDateConsultation() == null) {
			// TransactionDestination transactionDestination = new TransactionDestination();
			// transactionDestination = courrierInformations.getTransactionDestination();
			// transactionDestination.setTransactionDestDateConsultation(new Date());
			// appMgr.update(transactionDestination);
			// vb.setTransactionDestination(transactionDestination);
			// } else if(lstCourrierEnvoyerAncien.contains(courrierInformations)
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
			if (courrierInformations.getCourrierExpediteurObjet() instanceof Person) {
				Person person = new Person();
				person = (Person) courrierInformations
						.getCourrierExpediteurObjet();
				vb.setCopyListSelectedPerson(new ArrayList<Person>());
				vb.getCopyListSelectedPerson().add(
						vb.getLdapOperation().getPersonalisedUserById(person.getId()));
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
			vb.setSelectedListCourrier("CRancien");
			Transaction transaction = new Transaction();
			CourrierInformations courrierInformations = selectedCourrier;
			if (courrierInformations.getCourrier() == null) {
				courrierInformations.setCourrier(appMgr.getCourrierByIdCourrier(courrierInformations.getCourrierID()).get(0));
			}
			if (courrierInformations.getTransaction() == null) {
				courrierInformations.setTransaction(appMgr.getListTransactionByIdTransaction(courrierInformations.getTransactionID()).get(0));
			}
			courrier = courrierInformations.getCourrier();
			vb.setCourDossConsulterInformations(courrierInformations); // commenter si on renverse l'ancienne liste de courriers
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
//			if (courrierInformations.getCourrierExpediteurObjet() instanceof Person) {
//				Person person = new Person();
//				person = (Person) courrierInformations
//						.getCourrierExpediteurObjet();
//				vb.setCopyListSelectedPerson(new ArrayList<Person>());
//				vb.getCopyListSelectedPerson().add(
//						ldapOperation.getPersonalisedUserById(person.getId()));
//				vb.setDestNom(person.getCn());
//			} else if (courrierInformations.getCourrierExpediteurObjet() instanceof Unit) {
//				Unit unit = new Unit();
//				unit = (Unit) courrierInformations.getCourrierExpediteurObjet();
//				vb.setCopyListSelectedUnit(new ArrayList<Unit>());
//				vb.getCopyListSelectedUnit().add(unit);
//				vb.setDestNom(unit.getNameUnit());
//			}
			vb.setCopyDestNom(courrierInformations
					.getCourrierDestinataireReelle());
			vb.setCopyExpNom(courrierInformations.getCourrierExpediteur());
			vb.setCopyCourrierCommentaire(courrierInformations
					.getCourrierCommentaire());
			vb.setToReplay(true);
			
			//C*  seach the real expediteur :
			if (courrierInformations.getCourrierAllTransactions() == null) {
		        List<Transaction> allTransactions = appMgr.getTransactionByIdDossier(transaction.getDossier().getDossierId());
		        courrierInformations.setCourrierAllTransactions(allTransactions);
			}
	        Transaction firstTransaction = courrierInformations.getCourrierAllTransactions().get(courrierInformations.getCourrierAllTransactions().size() -1);
	        Expdest realExpediteur = appMgr.getListExpDestByIdExpDest(firstTransaction.getExpdest().getIdExpDest()).get(0);
	        courrierInformations.setExpDest(realExpediteur);
			if (realExpediteur.getTypeExpDest()
					.equals("Interne-Person")) {
//				 Person person = vb.getHashMapAllUser().get(courrierInformations.getExpDest().getIdExpDestLdap());
				 Person person = vb.getLdapOperation().getPersonalisedUserById(realExpediteur.getIdExpDestLdap());
				 vb.getCopyListSelectedPerson().add(person);
				 vb.setDestNom(person.getCn());
			} else if (realExpediteur.getTypeExpDest()
					.equals("Interne-Unité")) {
				 Unit unit = vb.getHashMapUnit().get(courrierInformations.getExpDest().getIdExpDestLdap());
				 vb.getCopyListSelectedUnit().add(unit);
				 vb.setDestNom(unit.getNameUnit());
			   //			} 
			   //			else if (realExpediteur.getTypeExpDest()
			   //					.equals("Interne-Boc")) {
			   //				expediteur.append(vb.getCentralBoc().getNameBOC());
				 
			} else if (realExpediteur.getTypeExpDest()
					.equals("Externe")) {
				Expdestexterne  realExpediteurExterne = appMgr.getExpediteurById(realExpediteur.getExpdestexterne().getIdExpDestExterne()).get(0);
				if (realExpediteurExterne
						.getTypeutilisateur().getTypeUtilisateurLibelle()
						.equals("PP")) {
					Pp pp = appMgr.getPPByReferenceExpediteur(realExpediteurExterne.getIdExpDestExterne()).get(0);
					pp.setExpdestexterne(realExpediteurExterne);
					vb.getCopyListPP().add(pp);
					vb.setDestNom(realExpediteurExterne.getExpDestExterneNom() + " " + realExpediteurExterne.getExpDestExternePrenom());

				} else {
					Pm pm = appMgr.getPMByReferenceExpediteur(realExpediteurExterne.getIdExpDestExterne()).get(0);
					pm.setExpdestexterne(realExpediteurExterne);
//					pm.setExpdestexterne(expdestexterne)
					vb.getCopyListPM().add(pm);
					vb.setDestNom(realExpediteur.getExpdestexterne().getExpDestExterneNom());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void execute() {
		Courrier courrier = new Courrier();
		courrierInformations = selectedCourrier;
		if (courrierInformations.getCourrier() == null) {
			courrierInformations.setCourrier(appMgr.getCourrierByIdCourrier(courrierInformations.getCourrierID()).get(0));
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
							courrierInformations.setTransaction(appMgr.getListTransactionByIdTransaction(courrierInformations.getTransactionID()).get(0));
						}
						validerFinProcessus(courrierInformations.getTransaction());
					} else {
						if (courrierInformations.getTransaction() == null) {
							courrierInformations.setTransaction(appMgr.getListTransactionByIdTransaction(courrierInformations.getTransactionID()).get(0));
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
			for (Transaction transaction : courrierInformations.getCourrierAllTransactions()) {
				System.out.println("####" + transaction.getTransactionId());
				TransactionDestinationReelle destinataionReel = appMgr.getTransactionDestinationReelById(transaction.getTransactionDestinationReelle().getTransactionDestinationReelleId());
				if (destinataionReel != null){
					if (!destinataionReel.getTransactionDestinationReelleTypeDestinataire().equals("Externe")) {
						// c'est un courrier d'arrivé depuis l'exterieur (PP ou PM) vers l'interne, donc il faut l'executer pour que le courrier s'entre dans le circuit de validation hierarchique 
						// ( validation hierarchique depend de la variable de parametrage #passage_hierarchique_courrier_arrive_apres_directeur_generale#
						validateTransactionToDestinationReel(transaction, destinataionReel);
//						if(destinataionReel.getTransactionDestinationReelleResponsableReponse() != null){
//							validateTransactionToDestinationReel(selectedCourrier.getTransaction(), destinataionReel);
//						}else{
//							setShowResponsableResponse(true);
//						}
					} else {
						// c'est un courrier d'un personne ou unité interne vers l'exterieur, juste il faut l'executer pour ajouter la transaction de depart d'un courrier
						executeOneTransaction(courrierInformations);
						break;
					}
				}
			}
			// C*
			//executeOneTransaction(courrierInformations); // commenté a cause de // C*
		}
	}

	private void validateTransactionToDestinationReel(Transaction transaction, TransactionDestinationReelle trDestinationReelle) {
		try {
			Variables variable = appMgr.listVariablesById(12).get(0);
			Variables variableToDGEN = appMgr.listVariablesById(13).get(0);
			boolean passageDGEN = false;
			Person generalDirector = null;
			if(variableToDGEN.getVaraiablesValeur().equals("Oui")){
				passageDGEN = true;
			} else {
				// special pour le BOC dans le cas ou la vlalidation ne passe pas par le DGEN pour la fonction #findIdDestinataireSuivant#
				Unit generalDirectorUnit = vb.getLdapOperation().getUnitById(1);
				generalDirector = generalDirectorUnit.getResponsibleUnit();
				// special pour le BOC dans le cas ou la vlalidation ne passe pas par le DGEN pour la fonction #findIdDestinataireSuivant#
			}
			status1 = false;
			status2 = false;
			TransactionDestination transactionDestination = selectedCourrier.getTransactionDestination();
			if (variable.getVaraiablesValeur().equals("Oui")) {
				Integer idDestinataireReel = trDestinationReelle.getTransactionDestinationReelleIdDestinataire();
				if (trDestinationReelle.getTransactionDestinationReelleTypeDestinataire().equals(
						"Interne-Person")) {
					Integer idDestinataireSuivant = findIdDestinataireSuivant(idDestinataireReel, vb.getPerson().getId(), true, passageDGEN, generalDirector);
					System.out.println(idDestinataireSuivant);
					if (idDestinataireSuivant.equals(idDestinataireReel)) {
						validateTransactionDestinataireFinale(transaction, transactionDestination);
					} else {
						validateTransactionDestinataireSuivant(transaction, transactionDestination,idDestinataireSuivant);
					}
				} else if (trDestinationReelle.getTransactionDestinationReelleTypeDestinataire()
						.equals("Interne-Unité")) {
					Integer idDestinataireSuivant = findIdDestinataireSuivant(idDestinataireReel, vb.getPerson().getId(), false, passageDGEN, generalDirector);
					System.out.println(idDestinataireSuivant);
					Unit unit = vb.getLdapOperation().getUnitById(idDestinataireReel);
					if (idDestinataireSuivant.equals(unit.getResponsibleUnit().getId())) {
						validateTransactionDestinataireFinale(transaction, transactionDestination);
					} else {
						validateTransactionDestinataireSuivant(transaction, transactionDestination,idDestinataireSuivant);
					}
				}
			} else {
				validateTransactionDestinataireFinale(transaction, transactionDestination);
			}
			try {
				if (transactionDestination != null) {
					if(transactionDestination
							.getTransactionDestDateTransfert() == null){
						transactionDestination
						.setTransactionDestDateTransfert(new Date());
					}
					if(transactionDestination
							.getTransactionDestDateConsultation() == null){
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

	private void validateTransactionDestinataireSuivant(Transaction transaction,TransactionDestination transactionDestination, Integer idDestinataireSuivant) throws Exception{
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
		newTransaction.setTransactionTypeIntervenant("sub_" + vb.getPerson().getId());
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
		newTransaction.setTransactionDestinationReelle(transaction.getTransactionDestinationReelle());
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
		trDest.setTransactionDestDateReponse(transactionDestination.getTransactionDestDateReponse());
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

	private void validateTransactionDestinataireFinale(Transaction transaction,TransactionDestination transactionDestination) throws Exception{
		List<TransactionAnnotation> transactionAnnotation = appMgr
		.getAnnotationByIdTransaction(transaction
				.getTransactionId());
		newTransaction = new Transaction();
		Etat etat = new Etat();
		Expdest expdest = new Expdest();
		Typetransaction typetransaction = new Typetransaction();
		TransactionDestinationId id = new TransactionDestinationId();
		TransactionDestination trDest = new TransactionDestination();
		etat = appMgr.listEtatByRef(6).get(0);
		transaction.setEtat(etat);
//		transaction.setTransactionDateReponse(new Date());
		if(transaction.getTransactionOrdre() == null){
			transaction.setTransactionOrdre(1);
		}
		appMgr.update(transaction);
		expdest.setTypeExpDest("Interne-Person");
		expdest.setIdExpDestLdap(vb.getPerson().getId());
		appMgr.insert(expdest);
//		newTransaction.setTransactionTypeIntervenant("sub_"
//				+ vb.getPerson().getId());
		newTransaction.setExpdest(expdest);
		newTransaction.setIdUtilisateur(vb.getPerson().getId());
		newTransaction.setTransactionDateTransaction(new Date());
		typetransaction = appMgr.getTypeTransactionByLibelle("Envoi")
				.get(0);
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
			  Person personDestinationReel = vb.getLdapOperation().getPersonalisedUserById(transaction.getTransactionDestinationReelle().getTransactionDestinationReelleIdDestinataire());
			  expdestFinal.setTypeExpDest("Interne-Person");
			  expdestFinal.setIdExpDestLdap(transaction.getTransactionDestinationReelle().getTransactionDestinationReelleIdDestinataire());
			  if (personDestinationReel.isResponsable()) {
				  type = "sub_"
						+ personDestinationReel.getId();
			  }else if(personDestinationReel.isSecretary()){
				  type = "secretary_"
						+ personDestinationReel.getId();
			  }else {
				  type = "agent_" + personDestinationReel.getId();
			  }
			  
			  
			  
		} else if (transaction.getTransactionDestinationReelle()
				.getTransactionDestinationReelleTypeDestinataire()
				.equals("Interne-Unité")) {
//			  Unit unit = ldapOperation.getUnitById(transaction.getTransactionDestinationReelle().getTransactionDestinationReelleIdDestinataire());
			  expdestFinal.setTypeExpDest("Interne-Unité");
			  expdestFinal.setIdExpDestLdap(transaction.getTransactionDestinationReelle().getTransactionDestinationReelleIdDestinataire());
				type = "unit_"
						+ transaction.getTransactionDestinationReelle().getTransactionDestinationReelleIdDestinataire();
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
			person = vb.getLdapOperation().getPersonalisedUserById(idDestinataireReel);
		} else {
			Unit unit = vb.getLdapOperation().getUnitById(idDestinataireReel);
			person = vb.getLdapOperation().getPersonalisedUserById(unit
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
				if (superiorUnit.getResponsibleUnit().getId() != idConnectedPerson && superiorUnit.getResponsibleUnit().getId() != generalDirector.getId()) {
					System.out.println(superiorUnit.getResponsibleUnit()
							.getId());
					return findIdDestinataireSuivant(superiorUnit
							.getResponsibleUnit().getId(), idConnectedPerson,
							true, passageDGEN, generalDirector);
				}
		}
		return person.getId();
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
				if(transactionDestination
						.getTransactionDestDateConsultation() == null){
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
			// C * // dupliquer le destinataire reel du workflow au niveau de la nouvelle transaction
			newTransaction.setTransactionDestinationReelle(transaction.getTransactionDestinationReelle());
			// C * // dupliquer le destinataire reel du workflow au niveau de la nouvelle transaction
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
			u = vb.getLdapOperation().getUnitByShortName(unite);

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

	private void executeOneTransaction(CourrierInformations courrierInformations) {
		if (courrierInformations.getCourrier() == null) {
			courrierInformations.setCourrier(appMgr.getCourrierByIdCourrier(courrierInformations.getCourrierID()).get(0));
		}
		if (courrierInformations.getTransaction() == null) {
			courrierInformations.setTransaction(appMgr.getListTransactionByIdTransaction(courrierInformations.getTransactionID()).get(0));
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
										.getTransactionDestinationReelleId(), 1).get(0);

				listTransaction = appMgr.getTransactionByIdTransactionDestinationReelle(transaction
						.getTransactionDestinationReelle().getTransactionDestinationReelleId());
				Etat etat = new Etat();
				Expdest expdest = new Expdest();
				Typetransaction typetransaction = new Typetransaction();
				TransactionDestinationId id = new TransactionDestinationId();
				TransactionDestination trDest = new TransactionDestination();
				etat = appMgr.listEtatByLibelle("Traité").get(0);
				newTransaction.setExpdest(transactionExpediteur.getExpdest());
				newTransaction.setIdUtilisateur(vb.getPerson().getId());
				newTransaction.setTransactionDateTransaction(new Date());
				typetransaction = appMgr.getTypeTransactionByLibelle("Envoi").get(0);
				newTransaction.setTypetransaction(typetransaction);
				newTransaction.setEtat(etat);
				newTransaction.setTransactionSupprimer(true);
				int newOrderNumber = transaction.getTransactionOrdre();
				newOrderNumber++;
				newTransaction.setTransactionOrdre(newOrderNumber);
				newTransaction.setDossier(transaction.getDossier());
				newTransaction.setTransactionDestinationReelle(transaction
						.getTransactionDestinationReelle());
				newTransaction.setTransactionFirst(transaction.getTransactionId());
				appMgr.insert(newTransaction);

				expdest = new Expdest();
				Expdestexterne expDestExterne = new Expdestexterne();
				TransactionDestinationReelle transactionDestinationReelle = appMgr.getTransactionDestinationReelById(transaction.getTransactionDestinationReelle().getTransactionDestinationReelleId());
				expdest.setTypeExpDest("Externe");
				expDestExterne = appMgr.getExpediteurById(
						transactionDestinationReelle
								.getTransactionDestinationReelleIdDestinataire()).get(0);
				expdest.setExpdestexterne(expDestExterne);
				transactionDestinationReelle.setTransactionDestinationReelleDateTraitement(new Date());
				appMgr.update(transactionDestinationReelle);
				appMgr.insert(expdest);

				id = new TransactionDestinationId();
				trDest = new TransactionDestination();
				id.setIdTransaction(newTransaction.getTransactionId());
				id.setIdExpDest(expdest.getIdExpDest());
				trDest.setId(id);
				appMgr.insert(trDest);

				List<TransactionAnnotation> transactionAnnotation = new ArrayList<TransactionAnnotation>();
				transactionAnnotation = appMgr.getAnnotationByIdTransaction(transaction
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
				Calendar cal = Calendar.getInstance();
			    cal.setTime(courrierInformations.getCourrierDateReceptionEnvoi());
			    int year = cal.get(Calendar.YEAR);
                // mettre a jour la reference pour indiquer qu'il est un courrier de depart
			    Integer lastId = appMgr.getCourrierLastIdByTypeOrdreAndAnnee("D", year);
				courrierInformations.getCourrier().setCourrierType("D");
				if (lastId == null || lastId == 0) {
					courrierInformations.getCourrier().setCourrierTypeOrdre(1);
				} else {
					courrierInformations.getCourrier().setCourrierTypeOrdre(lastId + 1);
				}
				courrierInformations.getCourrier().setCourrierReferenceCorrespondant(
						courrierInformations.getCourrier().getCourrierType()
								+ courrierInformations.getCourrier().getCourrierTypeOrdre());
//				courrierInformations.getCourrier().setCourrierReferenceCorrespondant("D"+courrierInformations.getCourrier().getIdCourrier());
				appMgr.update(courrierInformations.getCourrier());
				Dossier dossier = appMgr.getDossierByIdDossier(transaction.getDossier().getDossierId()).get(0);
				dossier.setDossierIntitule("Courrier_" + courrierInformations.getCourrier().getIdCourrier());
				appMgr.update(dossier);
				// mettre a jour la reference pour indiquer qu'il est un courrier de depart
				// mettre a jour la colonne #courrierDateReponseSysteme# pour indiquer que le courrier originale a été repondu
				// trouvez le courrier original
				List<Lienscourriers> liensCourriers = appMgr.getListCourrierLiensByIdCourrier(courrierInformations.getCourrier()
						.getIdCourrier());
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
						Courrier courrierOriginal = appMgr.getCourrierByIdCourrier(courrierLiens.getId().getIdCourrier()).get(0);
						courrierOriginal.setCourrierDateReponseSysteme(new Date());
						appMgr.update(courrierOriginal);
					}
				}
				}
				// trouver le courrier original
				// mettre a jour la colonne #courrierDateReponseSysteme# pour indiquer que le courrier originale a été repondu

				// inséré la date de traitement
				TransactionDestination trDestination = courrierInformations.getTransactionDestination();
				if (trDestination.getTransactionDestDateTransfert() == null) {
					trDestination.setTransactionDestDateTransfert(new Date());
					appMgr.update(trDestination);
				}
				// inséré la date de traitement
				// inséré la date de consultation
				if (trDestination.getTransactionDestDateConsultation() == null) {
					trDestination.setTransactionDestDateConsultation(new Date());
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

	public void chargementNotification(CourrierInformations consulterInformations) {
		try {
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
			if (consulterInformations.getCourrier() == null) {
				consulterInformations.setCourrier(appMgr.getCourrierByIdCourrier(consulterInformations.getCourrierID()).get(0));
			}
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
			p = vb.getLdapOperation().getUserByName(expdest);
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
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

	public Long CountCourrier(HashMap<String, Object> filterMap,
			int jourOrAutre, Date dateDebut, Date dateFin, String type,
			String type1, Integer idUser, Integer typeTransmission,
			String stateTraitement, String typeCourrier,
			String courrierRubrique, boolean forTotal) {

		// if(typeCourrier.equals("Envoyes") || typeCourrier.equals("Tous")){
		// if(!stateTraitement.equals("Avalider")){
		// countCourrierEnvoyer = appMgr.CountAllCourrierEnvoyerByCriteria(vb
		// .getPerson().isResponsable(), listIdsSousUnit,
		// listIdsSubordonne, filterMap, consultationCourrierSecretaire,
		// consultationCourrierSubordonne, consultationCourrierSousUnite,
		// jourOrAutre, dateDebut, dateFin, type, type1, typeSecretaire,
		// idUser, typeTransmission, stateTraitement, courrierRubriqueId,
		// forTotal);
		// }else{
		// countCourrierEnvoyer = 0L;
		// }
		// }
		// countCourrierEnvoyer = appMgr.CountAllCourrierEnvoyerByCriteria(vb
		// .getPerson().isResponsable(), listIdsSousUnit,
		// listIdsSubordonne, filterMap, consultationCourrierSecretaire,
		// consultationCourrierSubordonne, consultationCourrierSousUnite,
		// jourOrAutre, dateDebut, dateFin, type, type1, typeSecretaire,
		// idUser, typeTransmission, stateTraitement, courrierRubriqueId,
		// forTotal);
		// countCourrierRecu = appMgr.CountAllCourrierRecuByCriteria(vb
		// .getPerson().isResponsable(), listIdsSousUnit,
		// listIdsSubordonne, filterMap, consultationCourrierSecretaire,
		// consultationCourrierSubordonne, consultationCourrierSousUnite,
		// jourOrAutre, dateDebut, dateFin, type, type1, typeSecretaire,
		// idUser, typeTransmission, stateTraitement, courrierRubriqueId,
		// forTotal);
		// if (typeCourrier.equals("Recu")) {
		// countCourrier = countCourrierRecu;
		// } else if (typeCourrier.equals("Envoyes")) {
		// countCourrier = countCourrierEnvoyer;
		// } else {
		// countCourrier = countCourrierEnvoyer + countCourrierRecu;
		// }
		Integer courrierRubriqueId = Integer.valueOf(courrierRubrique);
		countCourrier = appMgr.CountAllCourrierEnvoyerANDRecuByCriteria(vb
				.getPerson().isResponsable(), listIdsSousUnit,
				listIdsSubordonne, filterMap, consultationCourrierSecretaire,
				consultationCourrierSubordonne, consultationCourrierSousUnite,
				jourOrAutre, dateDebut, dateFin, type, type1, typeSecretaire,
				idUser, typeTransmission, stateTraitement, courrierRubriqueId,
				forTotal, typeCourrier);
		return countCourrier;
	}

	public Long getCountCourrier(HashMap<String, Object> filterMap,
			String transmissionCourrierJour, String typeCourrierTraitementJour,
			String typeCourrier, String typeCourrierValidation,
			String categorieCourrier, String courrierRubrique, boolean forTotal) {
		Long countCourrier = 0L;

		if (vb.getPerson().isBoc()) {
			countCourrier = appMgr.CountAllCourrierBOCByCriteria(filterMap, 2,
					dateDebut, dateFin, type, type1, listIdBocMembers,
					transmissionCourrierJour, typeCourrierTraitementJour,
					categorieCourrier);
		} else {
			countCourrier = appMgr.CountAllCourrierEnvoyerANDRecuByCriteria(vb.getPerson()
					.isResponsable(), listIdsSousUnit, listIdsSubordonne, filterMap,
					consultationCourrierSecretaire, consultationCourrierSubordonne,
					consultationCourrierSousUnite, 2, dateDebut, dateFin, type, type1,
					typeSecretaire, idUser, typeTransmission, typeCourrierValidation,
					Integer.valueOf(courrierRubrique), forTotal, typeCourrier);
		}

		return countCourrier;
	}

	public void executeAllTransaction() {

	}

	public List<SelectItem> getSelectItemsTr() {
		String libelle;
		String id;
		List<SelectItem> selectItemsTr = new ArrayList<SelectItem>();

		selectItemsTr.add(new SelectItem(messageSource.getMessage(
				"toutCourrier", new Object[] {}, lm.createLocal())));
		// selectItemsTr.add(new SelectItem("Tout les courriers"));
		// for (Transmission transmission : listTr) {
		//
		// }
		for (Transmission item : listTr) {
			if (vb.getLocale().equals("ar")) {
				libelle = item.getTransmissionLibelleAr();
			} else {
				libelle = item.getTransmissionLibelle();
			}
			id = item.getTransmissionId().toString();
			selectItemsTr.add(new SelectItem(id,libelle));
		}
//		selectItemsTr.add(new SelectItem(messageSource.getMessage("AutreLabel",
//				new Object[] {}, lm.createLocal())));
		return selectItemsTr;
	}

	// Geeters & Setters ...
	public ApplicationManager getAppMgr() {
		return appMgr;
	}

	public void setAppMgr(ApplicationManager appMgr) {
		this.appMgr = appMgr;
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

	public VariableGlobaleNotification getVbn() {
		return vbn;
	}

	public void setVbn(VariableGlobaleNotification vbn) {
		this.vbn = vbn;
	}

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

	public List<Transmission> getListTr() {
		return listTr;
	}

	public void setListTr(List<Transmission> listTr) {
		this.listTr = listTr;
	}

	public Variables getVariableConsultationCourrierSecretaire() {
		return varConsultationCourrierSecretaire;
	}

	public void setVariableConsultationCourrierSecretaire(
			Variables variableConsultationCourrierSecretaire) {
		this.varConsultationCourrierSecretaire = variableConsultationCourrierSecretaire;
	}

	public Variables getVariableConsultationCourrierSubordonne() {
		return varConsultationCourrierSubordonne;
	}

	public void setVariableConsultationCourrierSubordonne(
			Variables variableConsultationCourrierSubordonne) {
		this.varConsultationCourrierSubordonne = variableConsultationCourrierSubordonne;
	}

	public Variables getVariableConsultationCourrierSousUnite() {
		return varConsultationCourrierSousUnite;
	}

	public void setVariableConsultationCourrierSousUnite(
			Variables variableConsultationCourrierSousUnite) {
		this.varConsultationCourrierSousUnite = variableConsultationCourrierSousUnite;
	}

	public void setCountCourrier(Long countCourrier) {
		this.countCourrier = countCourrier;
	}

	public void setTypeSecretaire(String typeSecretaire) {
		this.typeSecretaire = typeSecretaire;
	}

	public String getTypeSecretaire() {
		return typeSecretaire;
	}

	public void setStateTraitement(Integer stateTraitement) {
		this.stateTraitement = stateTraitement;
	}

	public Integer getStateTraitement() {
		return stateTraitement;
	}

	public Long getCountCourrierRecu() {
		return countCourrierRecu;
	}

	public void setCountCourrierRecu(Long countCourrierRecu) {
		this.countCourrierRecu = countCourrierRecu;
	}

	public CourrierInformations getSelectedCourrier() {
		return selectedCourrier;
	}

	public void setSelectedCourrier(CourrierInformations selectedCourrier) {
		this.selectedCourrier = selectedCourrier;
	}

	public boolean isShowExecuteAllButton() {
		return showExecuteAllButton;
	}

	public void setShowExecuteAllButton(boolean showExecuteAllButton) {
		this.showExecuteAllButton = showExecuteAllButton;
	}

	public boolean isHideExecuteAllButton() {
		return hideExecuteAllButton;
	}

	public void setHideExecuteAllButton(boolean hideExecuteAllButton) {
		this.hideExecuteAllButton = hideExecuteAllButton;
	}

	public boolean isMoreChoices() {
		return moreChoices;
	}

	public void setMoreChoices(boolean moreChoices) {
		this.moreChoices = moreChoices;
	}

	public boolean isAllMailChecked() {
		return allMailChecked;
	}

	public void setAllMailChecked(boolean allMailChecked) {
		this.allMailChecked = allMailChecked;
	}

	public boolean isToValidateMailChecked() {
		return toValidateMailChecked;
	}

	public void setToValidateMailChecked(boolean toValidateMailChecked) {
		this.toValidateMailChecked = toValidateMailChecked;
	}

	public boolean isValidatedMailChecked() {
		return validatedMailChecked;
	}

	public void setValidatedMailChecked(boolean validatedMailChecked) {
		this.validatedMailChecked = validatedMailChecked;
	}

	public boolean isNotValidatedMailChecked() {
		return notValidatedMailChecked;
	}

	public void setNotValidatedMailChecked(boolean notValidatedMailChecked) {
		this.notValidatedMailChecked = notValidatedMailChecked;
	}

	public boolean isTreatedMailChecked() {
		return treatedMailChecked;
	}

	public void setTreatedMailChecked(boolean treatedMailChecked) {
		this.treatedMailChecked = treatedMailChecked;
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

	public Long getCountCourrierEnvoyer() {
		return countCourrierEnvoyer;
	}

	public void setCountCourrierEnvoyer(Long countCourrierEnvoyer) {
		this.countCourrierEnvoyer = countCourrierEnvoyer;
	}
}