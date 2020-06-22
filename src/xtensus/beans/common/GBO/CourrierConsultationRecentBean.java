package xtensus.beans.common.GBO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.persistence.Tuple;

import org.apache.poi.hssf.util.HSSFColor.TAN;
import org.mvel2.ast.UntilNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import xtensus.MethdesGeneriques.MethodesGenerique;
import xtensus.aop.LogClass;
import xtensus.beans.common.LanguageManagerBean;
import xtensus.beans.common.ListeDestinatairesModel;
import xtensus.beans.common.VariableGlobale;
import xtensus.beans.common.VariableGlobaleNotification;
import xtensus.beans.utils.CourrierInformations;
import xtensus.beans.utils.Informations;
import xtensus.entity.Annotation;
import xtensus.entity.Courrier;
import xtensus.entity.CourrierDossier;
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
public class CourrierConsultationRecentBean {

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
	private List<CourrierInformations> lstCourrierEnvoyer;
	private List<CourrierInformations> listCourrier;
	private String typeUserDes;
	private boolean disbledBontonConsultation=false;
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
	private Variables courrierValidationHearchique;
	private List<Integer> listIdBocMembers;
	private String month;
	private int year;
	private Date dateCourrier;
	private int idBocExpediteur;
	private int idBocDestinataire;
	private Person destinationReel;
	private int IdExpediteur;
	private int idUserDes;

	// JS
	private String texteTypeCourrier;
	private String codeUniqueCourrier = "";
	private List<Variables> var;
	private boolean courrierPointer=false;
	// code de test
	public String hidden1;
	public String hidden2;
    private List<CourrierInformations> listCourriersInformationsAffecte;
    private Boolean etatReceptionPhysique;
	@Autowired
	private UserAgentProcessor userAgent;
	// For filter and Sort
	private String sortField;
	private boolean descending;
	private HashMap<String, Object> filterMap = new HashMap<String, Object>();

	// Filtre recherche
	private String typeCourrierValidationMois;
	private Boolean checkedTypeCourrierValidationMois;
	private String transmissionCourrierMois;
	private String typeCourrierTraitementMois;
	private String categorieCourrierMois;
	private String typeCourrierMois;
	private String courrierRubrique;
	private String styleMessage;
	private String variabledeTest = "test";
	private String variabledeTest2;
	private Integer variableCourrie = 5;
    private int lastIndex;
    private boolean showModificationButton;
  //[JS] - 2020-03-17
	ArrayList<SelectItem> selectItemsTr2 = new ArrayList<SelectItem>();
	private String selectedItemsTr;	
	private TransactionDestination transactionDestination2;
	
	//|JS]================
	//[JS] : 2020-04-30: test sur date reception physique
	private boolean ajoutReceptionPhysiqueOK;
	
	//===============================
	public String getVariabledeTest() {
		
		return variabledeTest;
	}

	public void setVariabledeTest(String variabledeTest) {
		
		this.variabledeTest = variabledeTest;
	}

	// ***********************
	// AH : ajouter pour récupérer laliste des Destinataire avec leurs
	// Annotations
	private List<ListeDestinatairesModel> destinatairesAvecAnnotations;
	// KHA 12-02-2019
	private List<ListeDestinatairesModel> destinataireRepondre;
	// KHA - 25-03-2019
	private List<ItemSelected> listSelectedItem;
	private String userInput = "";

	public List<ListeDestinatairesModel> getDestinataireRepondre() {
		return destinataireRepondre;
	}

	public String submit() {
		this.userInput = "The user has entered \"" + this.userInput + " \"";
		return "";
	}

	public void setDestinataireRepondre(
			List<ListeDestinatairesModel> destinataireRepondre) {
		this.destinataireRepondre = destinataireRepondre;
	}

	// KHA
	private Unit unitSup;

	public CourrierConsultationRecentBean() {

	}

	private TransactionAnnotation ta;
	private Transaction tr;
	private int referenceSomeA;
	private boolean receptionphysiqueNonLivre;
	private boolean existeBOSansMembres;
	private int responsableBocDest=0;
	   private List<CourrierInformations> listCourriersLiees ;

	@Autowired
	public CourrierConsultationRecentBean(
			@Qualifier("applicationManager") ApplicationManager appMgr) {
		this.appMgr = appMgr;
		ldapOperation = new LdapOperation();
		listCourrier = new ArrayList<CourrierInformations>();
		listInfo = new ArrayList<Informations>();
		info1 = new Informations();
		info2 = new Informations();
		info3 = new Informations();
		listIdsSousUnit = new ArrayList<Integer>();
		listIdsSubordonne = new ArrayList<Integer>();
		dateCourrier = new Date();
		ta = new TransactionAnnotation();
		tr = new Transaction();
		listCourriersInformationsAffecte=new ArrayList<CourrierInformations>();
		listCourriersLiees=new ArrayList<CourrierInformations>();


	}

	@PostConstruct
	public void Initialize() {
		try {
			
			etatReceptionPhysique = false;

			// C*
			courrierAriverToDG = appMgr.listVariablesById(13).get(0);
			// C*
			
			hideExecuteAllButton = true;
			countCourrier = 0L;
			listTr = appMgr.getList(Transmission.class);
			setShowTab(true);
			setBocOption(false);
			setUserOption(true);

			typeCourrierValidationMois = "";
			transmissionCourrierMois = "Tout les courriers";
			typeCourrierTraitementMois = "tous";
			categorieCourrierMois = "T";
			typeCourrierMois = "Tous";
			courrierRubrique = "6";
			checkedTypeCourrierValidationMois = false;

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
								System.out
										.println("#Sub-Unit without Responsible");
							}
						}
					}
				}
							
				
				//Liste des directions de sous BO 
				
				if (consultationCourrierSousUnite.equals("Non")) {
					consultationCourrierSousUnite = "Non";
					consultationCourrierSubordonne = "Non";
				} else{
				for (BOC boc : vb.getPerson().getAssociatedDirection()
						.getListBOChildUnit()) {
					
					 List<Unit> listUnites = boc.getListDirectionsChildBOC();
						System.out.println("listUnites sous Boc  :"+listUnites.size());

					 for(Unit unite : listUnites){
						
							if (consultationCourrierSousUnite.equals("Oui")) {
								
								listIdsSousUnit.add(unite.getIdUnit());
							}
							if (consultationCourrierSubordonne.equals("Oui")) {
								try {
									listIdsSubordonne.add(unite.getResponsibleUnit()
											.getId());
								} catch (Exception e) {
									System.out
											.println("#Sub-Unit without Responsible");
								}
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
			typeTransmission = 0;
			setStateTraitement(0);
			// fin identify connected user


			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			dateDebut = calendar.getTime();

			// calendar.add(Calendar.DATE, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			dateFin = calendar.getTime();

			String calMonth = calendar.getDisplayName(Calendar.MONTH,
					Calendar.LONG, Locale.FRANCE);
			month = calMonth.substring(0, 3);
			if (calMonth.length() > 0) {
				month += ".";
			}

			if (userAgent.isPhone()) {
				//2020-06-09
				searchListCourrierAnnee("annee", filterMap, sortField, descending,
						"Tous", "T", "Tout les courriers", "tous", "", 0, 10,
						false, courrierRubrique);
			}
			year = calendar.get(Calendar.YEAR);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<CourrierInformations> searchListCourrier(String recent,
			HashMap<String, Object> filterMap, String sortField,
			boolean descending, String typeCourrierJour,
			String categorieCourrier, String transmissionCourrierJour,
			String typeCourrierTraitementJour, String typeCourrierValidation,
			Integer firstIndex, Integer maxResult, Boolean forRapport,
			String courrierRubrique) {
		//System.out.println(" Search List Courrier ");
		try {
			Calendar calendar = Calendar.getInstance();
			if (recent.equals("annee")) {
				calendar.set(Calendar.MONTH, 0);
			}
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			dateDebut = calendar.getTime();
			String sousTitreDeJour = " ";
			lstCourrierEnvoyer = new ArrayList<CourrierInformations>();
			if (vb.getPerson().isBoc()) {
				vb.setTransmissionCourrierJourForRapportAncien(transmissionCourrierJour);
				vb.setTypeCourrierTraitementJourForRapportAncien(typeCourrierTraitementJour);
				vb.setCategorieCourrierJourForRapportAncien(categorieCourrier);

				// ---------------KHA : Sous Titre Rapport : ajouté le
				// 19-03-2019-------
				if ((transmissionCourrierJour.equals("Tout les courriers") || transmissionCourrierJour
						.equals("Tous les courriers"))
						&& typeCourrierTraitementJour.equals("tous")
						&& categorieCourrier.equals("T")) {
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
					if (!categorieCourrier.equals("T")) {
						result = result
								+ getCategorieListeCourriers(categorieCourrier);

					}
					System.out.println("result = " + result);

					if (result.endsWith(" -")) {
						sousTitreDeJour = result.substring(0,
								result.length() - 2);
					} else
						sousTitreDeJour = result;
					//System.out.println("===========================");
					System.out.println("sousTitreDeJour = " + sousTitreDeJour);
					//System.out.println("===========================");
				}
				vb.setSousTitreRapportBoc(sousTitreDeJour);
				// ----------------------- FIN : KHA

				long startTime = System.currentTimeMillis();
				lstCourrierEnvoyer = appMgr.findCourrierEnvoyerBOCByCriteria(
						filterMap, sortField, descending, 4, dateDebut,
						dateFin, type, type1, listIdBocMembers,
						transmissionCourrierJour, typeCourrierTraitementJour,
						firstIndex, maxResult, categorieCourrier, forRapport,
						vb.getDbType(), null, 0, 0);
				System.out.println("list courrier ancien dure BOCT : "
						+ (System.currentTimeMillis() - startTime));
				for (CourrierInformations courrierInformations : lstCourrierEnvoyer) {
					try {
						//System.out.println("1234 : "
								//+ courrierInformations.getDestReelList());

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

				// ------------------------ KHA : SOUS TITRE autre BOC
				// --------------------

				if (vb.getPerson().isResponsable()) {
					// --------- 1: si connectee est un responsable
					if (typeCourrierJour.equals("Tous")
							&& courrierRubrique.equals("1")) {
						sousTitreDeJour = "Tous";

					} else {
						String result = "";
						if (!typeCourrierJour.equals("Tous")) {
							result = getTypeCourrierListeCourriers(typeCourrierJour)
									+ " -";
						}
						if (!courrierRubrique.equals("1")) {
							result = result
									+ getRubriqueListeCourriers(courrierRubriqueId);
						}

						System.out.println("result = " + result);

						if (result.endsWith(" -")) {
							sousTitreDeJour = result.substring(0,
									result.length() - 2);
						} else
							sousTitreDeJour = result;
						//System.out.println("===========================");
						System.out.println("sousTitreDeJour = "
								+ sousTitreDeJour);
						//System.out.println("===========================");
						vb.setSousTitreRapportResponsable(sousTitreDeJour);
//						System.out.println("SousTitreRapportResponsable = "
//								+ vb.getSousTitreRapportResponsable());

					}

				} else {
					// --------- 2: si connectee est un Secretaire ou Agent
					sousTitreDeJour = getTypeCourrierListeCourriers(typeCourrierJour);
					vb.setSousTitreRapportSecAgent(sousTitreDeJour);
				}
//				System.out.println("SousTitreRapportResponsable=  "
//						+ vb.getSousTitreRapportResponsable());
//				System.out.println("SousTitreRapportSecAgent=  "
//						+ vb.getSousTitreRapportSecAgent());
//				System.out.println("sousTitreDeJour=  " + sousTitreDeJour);

				// ----------------------------FIN :
				// KHA--------------------------------------------

				// if (typeCourrierJour.equals("Tous")
				// || typeCourrierJour.equals("Envoyes")) {
				// if (!typeCourrierValidation.equals("Avalider")) {
				long startTime = System.currentTimeMillis();
				lstCourrierEnvoyer = appMgr
						.findCourrierEnvoyerANDRecuByCriteria(vb.getPerson()
								.isResponsable(), listIdsSousUnit,
								listIdsSubordonne, filterMap, sortField,
								descending, consultationCourrierSecretaire,
								consultationCourrierSubordonne,
								consultationCourrierSousUnite, 4, dateDebut,
								dateFin, type, type1, typeSecretaire, idUser,
								typeTransmission, typeCourrierValidation,
								firstIndex, maxResult, forRapport,
								courrierRubriqueId, typeCourrierJour, vb
										.getDbType(), null, 0, 0);
				System.out.println("list courrier ancien dure RESPONSIBLE : "
						+ (System.currentTimeMillis() - startTime));
				for (CourrierInformations courrierInformations : lstCourrierEnvoyer) {
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
						.println("list courrier ancien dure RESPONSIBLE with Exp & Dest : "
								+ (System.currentTimeMillis() - startTime));
			}
			listCourrier.clear();
			listCourrier.addAll(lstCourrierEnvoyer);
//			System.out.println("listCourrier 2 " + listCourrier.size());
			texteTypeCourrier = "<form method='post' id='form'><table width=100% border=1 class='datatable table table-striped table-bordered' >";
			if (listCourrier.size() > 0) {
				for (int k = 0; k < listCourrier.size(); k++) {
//					System.out
//							.println("Style" + listCourrier.get(k).getStyle());
					int idCourrier = listCourrier.get(k).getCourrierID();
					userInput = listCourrier.get(k)
							.getCourrierDestinataireReelle();
					texteTypeCourrier += "<tr onclick='fctClick(this)'>"
							+ "<td valign=top width=50% styleClass='#{courrierConsultationRecentBean.styleMessage}'><input type='hidden' id='input_1' value="
							+ idCourrier
							+ " name="
							+ idCourrier
							+ "/><a href=''><ul class='list-unstyled'><li>"
							+ listCourrier.get(k)
									.getCourrierDestinataireReelle() + "<ul>"
							+ "<li>"
							+ listCourrier.get(k).getCourrierDestinataireReelleDirection()
							+ "</li>" + "<li>" + "<li>"
							+ listCourrier.get(k).getCourrierObjet()
							+ "</li>" + "<li>"
							+ listCourrier.get(k).getCourrierCommentaire()
							+ "</li></ul></li></ul></a>";

					texteTypeCourrier += "</td>";

					texteTypeCourrier += "</tr>";

				}

				texteTypeCourrier += "</table></form>";

			}

			return listCourrier;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<CourrierInformations> searchListCourrierAnnee(String recent,
			HashMap<String, Object> filterMap, String sortField,
			boolean descending, String typeCourrierJour,
			String categorieCourrier, String transmissionCourrierJour,
			String typeCourrierTraitementJour, String typeCourrierValidation,
			Integer firstIndex, Integer maxResult, Boolean forRapport,
			String courrierRubrique) {
		try {
			Calendar calendar = Calendar.getInstance();
			if (recent.equals("annee")) {
				calendar.set(Calendar.MONTH, 0);
			}
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			dateDebut = calendar.getTime();
			String sousTitreDeJour = "";
			lstCourrierEnvoyer = new ArrayList<CourrierInformations>();
			if (vb.getPerson().isBoc()) {
				vb.setTransmissionCourrierJourForRapportAncien(transmissionCourrierJour);
				vb.setTypeCourrierTraitementJourForRapportAncien(typeCourrierTraitementJour);
				vb.setCategorieCourrierJourForRapportAncien(categorieCourrier);

				// ---------------KHA : Sous Titre Rapport : ajouté le
				// 19-03-2019-------
				if ((transmissionCourrierJour.equals("Tout les courriers") || transmissionCourrierJour
						.equals("Tous les courriers"))
						&& typeCourrierTraitementJour.equals("tous")
						&& categorieCourrier.equals("T")) {
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
					if (!categorieCourrier.equals("T")) {
						result = result
								+ getCategorieListeCourriers(categorieCourrier);

					}
					System.out.println("result = " + result);

					if (result.endsWith(" -")) {
						sousTitreDeJour = result.substring(0,
								result.length() - 2);
					} else
						sousTitreDeJour = result;
					//System.out.println("===========================");
					System.out.println("sousTitreDeJour = " + sousTitreDeJour);
					//System.out.println("===========================");
				}
				vb.setSousTitreRapportBoc(sousTitreDeJour);
				// ----------------------- FIN : KHA

				long startTime = System.currentTimeMillis();
				lstCourrierEnvoyer = appMgr.findCourrierEnvoyerBOCByCriteria(
						filterMap, sortField, descending, 3, dateDebut,
						dateFin, type, type1, listIdBocMembers,
						transmissionCourrierJour, typeCourrierTraitementJour,
						firstIndex, maxResult, categorieCourrier, forRapport,
						vb.getDbType(), null, 0, 0);
				System.out.println("list courrier ancien dure BOCT : "
						+ (System.currentTimeMillis() - startTime));
				for (CourrierInformations courrierInformations : lstCourrierEnvoyer) {
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

				// ------------------------ KHA : SOUS TITRE autre BOC
				// --------------------

				if (vb.getPerson().isResponsable()) {
					// --------- 1: si connectee est un responsable
					if (typeCourrierJour.equals("Tous")
							&& courrierRubrique.equals("1")) {
						sousTitreDeJour = "Tous";

					} else {
						String result = "";
						if (!typeCourrierJour.equals("Tous")) {
							result = getTypeCourrierListeCourriers(typeCourrierJour)
									+ " -";
						}
						if (!courrierRubrique.equals("1")) {
							result = result
									+ getRubriqueListeCourriers(courrierRubriqueId);
						}

						System.out.println("result = " + result);

						if (result.endsWith(" -")) {
							sousTitreDeJour = result.substring(0,
									result.length() - 2);
						} else
							sousTitreDeJour = result;
						//System.out.println("===========================");
						System.out.println("sousTitreDeJour = "
								+ sousTitreDeJour);
						//System.out.println("===========================");
						vb.setSousTitreRapportResponsable(sousTitreDeJour);
						System.out.println("SousTitreRapportResponsable = "
								+ vb.getSousTitreRapportResponsable());

					}

				} else {
					// --------- 2: si connectee est un Secretaire ou Agent
					sousTitreDeJour = getTypeCourrierListeCourriers(typeCourrierJour);
					vb.setSousTitreRapportSecAgent(sousTitreDeJour);
				}

				// ----------------------------FIN :
				// KHA--------------------------------------------

				// if (typeCourrierJour.equals("Tous")
				// || typeCourrierJour.equals("Envoyes")) {
				// if (!typeCourrierValidation.equals("Avalider")) {
				long startTime = System.currentTimeMillis();
				lstCourrierEnvoyer = appMgr
						.findCourrierEnvoyerANDRecuByCriteria(vb.getPerson()
								.isResponsable(), listIdsSousUnit,
								listIdsSubordonne, filterMap, sortField,
								descending, consultationCourrierSecretaire,
								consultationCourrierSubordonne,
								consultationCourrierSousUnite, 3, dateDebut,
								dateFin, type, type1, typeSecretaire, idUser,
								typeTransmission, typeCourrierValidation,
								firstIndex, maxResult, forRapport,
								courrierRubriqueId, typeCourrierJour, vb
										.getDbType(), null, 0, 0);
				System.out.println("list courrier ancien dure RESPONSIBLE : "
						+ (System.currentTimeMillis() - startTime));
				for (CourrierInformations courrierInformations : lstCourrierEnvoyer) {
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
						.println("list courrier ancien dure RESPONSIBLE with Exp & Dest : "
								+ (System.currentTimeMillis() - startTime));
			}
			listCourrier.clear();
			listCourrier.addAll(lstCourrierEnvoyer);

			return listCourrier;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void searchExpediteurDestinataire(
			CourrierInformations courrierInformations) throws Exception {
		System.out.println("AH : DANS searchExpediteurDestinataire ");
		// []
		Transaction transaction = appMgr.getListTransactionByIdTransaction(
				courrierInformations.getTransactionID()).get(0);
		System.out.println("transaction Refere Courrier : "
				+ transaction.getCourrierReferenceCorrespondant());
		courrierInformations.setTransaction(transaction);
		courrierInformations.setTransmission(appMgr.getCourrierByIdCourrier(
				courrierInformations.getCourrierID()).get(0).getTransmission());


		courrierInformations.setCourrier(appMgr.getCourrierByIdCourrier(
				courrierInformations.getCourrierID()).get(0));


		destinatairesAvecAnnotations = new ArrayList<ListeDestinatairesModel>();
		listSelectedItem = new ArrayList<ItemSelected>();

		List<Object> listSelectedObject = new ArrayList<Object>();
		List<Person> listSelectedPerson = new ArrayList<Person>();
		List<Pp> listSelectetdPP = new ArrayList<Pp>();
		List<Pm> listSelectetdPM = new ArrayList<Pm>();
		List<Unit> listSelectetdUnit = new ArrayList<Unit>();
		List<BOC> listSelectetdBoc = new ArrayList<BOC>();

		Integer etatID = courrierInformations.getEtatID();
		System.out.println("get id transaction :"
				+ courrierInformations.getTransactionID());
		// get transaction destinataire by id transaction
		List<TransactionDestination> listDestinataire = appMgr.getDestinationByIdTransaction(courrierInformations.getTransactionID());


		if(listDestinataire != null && listDestinataire.size()>0){
			TransactionDestination BocSuivant = listDestinataire.get(0);

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
		String destinataireExpediteur = "";
		
		// KHA : variableExecution ===>Si la valeur de la variable est à Oui
		// nous avons tous les types membre du Bureau d’Ordre
		// a l’accès à l’exécution d’un Courrier.
		// Si la valeur de la variable à Non, seul le responsable du Bureau
		// d’ordre exécute le Courrier.
		Variables variableExecution = appMgr.listVariablesByLibelle(
				"execution_courrier_par_tous_types_membre_bo").get(0);

		System.out.println("Type Expiditeur:" + expType);
		if (expType.equals("Interne-Person")) {
			System.out.println(" DANS  Interne-Person");
			if (expLdap.equals(vb.getPerson().getId())) {
				courrierInformations.setCourrierRecu(0);
			}

			// : Pas de Passage par DG
			// System.out.println("courrierAriverToDG :"+courrierAriverToDG.getVaraiablesValeur());

			// hidden par KHA// if
			// (courrierAriverToDG.getVaraiablesValeur().equals("Non")) {
			/***
			 * test pour que boc execute un courrier
			 */
			System.out.println("courrierInformations.getTransactionOrdre :"
					+ courrierInformations.getTransactionOrdre());
			System.out.println("etat :" + etatID);

			// KHA : Seulement le responsable BO peut exécuter
			if (variableExecution.getVaraiablesValeur().equals("Non")) {
				if (vb.getPerson().getAssociatedBOC() != null
						&& courrierInformations.getTransactionOrdre() == null
						&& etatID.equals(5)) {
					//System.out.println("2019-05-18 welcome ");
					courrierInformations.setCourrierAValider(1);
				} 
//				else

//					System.out
//							.println(" //[]: test pour que boc execute un courrier de destination interne-Person avec ordre de transaction != null");
				// []: test pour que boc execute un courrier de destination
				// interne-Person avec ordre de transaction != null
				// if(vb.getPerson().getAssociatedBOC() != null &&
				// courrierInformations.getTransactionOrdre() != null &&
				// etatID.equals(5)){
				// KHA : ajouté le test si le connectee est resp BO

				if (vb.getPerson().getAssociatedBOC() != null
						&& vb.getPerson().isResponsableBO()
						&& courrierInformations.getTransactionOrdre() != null
						&& etatID.equals(5)) {
//					System.out.println("-->courrier Interne à executer ");
					courrierInformations.setCourrierAValider(1);

				}
			}
			// KHA : touls les Membres(Agent/Responsable) de BO peuvent exécuter
			else {
				// [] 2019-05-18 :ajouter condition where BOC connecté a le meme
				// id que le boc qui sera executer courrier

				if (vb.getPerson().getAssociatedBOC() != null
						&& courrierInformations.getTransactionOrdre() != null
						&& etatID.equals(5)
						) {
					//System.out.println("-->courrier Interne à executer ");
					courrierInformations.setCourrierAValider(1);

				}

			}
			// }
			Person person = vb.getHashMapAllUser().get(expLdap);
			//System.out.println("person : " + person);
			Person p = vb.getLdapOperation().getPersonalisedUserById(
					person.getId());
			//System.out.println("person ================> " + p);
			if (p.isResponsable() || p.isAgent() || p.isSecretary()) {
				//System.out.println("===========> "
//						+ p.getAssociatedDirection().getShortNameUnit());
				destinataireExpediteur = p.getAssociatedDirection()
						.getShortNameUnit();
				//System.out.println("directionPerson : "
//						+ destinataireExpediteur);

			}
			//System.out.println("person.isboc 2: " + p.isBoc());
			if (p.getAssociatedBOC() != null) {
				//System.out.println("person.isboc 3: " + p.isBoc());
				destinataireExpediteur = "BOC";

			}
			expediteur = person.getCn();
		} else if (expType.equals("Interne-Unité")) {
			//System.out.println(" DANS execute Interne-Unité");
			if (vb.getPerson().isResponsable()
					&& !vb.getPerson().isBoc()
					&& expLdap.equals(vb.getPerson().getAssociatedDirection()
							.getIdUnit())) {
				courrierInformations.setCourrierRecu(0);
			}
			// hidden par KHA //if
			// (courrierAriverToDG.getVaraiablesValeur().equals("Non")) {
			/***
			 * test pour que boc execute un courrier
			 */

			if (variableExecution.getVaraiablesValeur().equals("Non")) {

				if (vb.getPerson().getAssociatedBOC() != null
						&& courrierInformations.getTransactionOrdre() == null
						&& etatID.equals(5)) {
					courrierInformations.setCourrierAValider(1);

				} 
//				else
//					System.out
//							.println(" //[]: test pour que boc execute un courrier de destination interne-unité avec ordre de transaction != null");

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
				if(courrierInformations.getTransmission().getTransmissionId() != null){
					if(courrierInformations.getTransmission().getTransmissionId()==11){
					//Enveloppe : Caché bouton execution 
					System.out.println("### 8===> 1 ###");
					courrierInformations.setCourrierAValider(0);
					System.out.println("Affiche bouton Execute 1 : "+courrierInformations.getCourrierAValider());}
				}
				
				List<CourrierLiens> list = appMgr.getCourrierLiensByCourrierId(courrierInformations.getCourrierID());

				
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
			// if (courrierAriverToDG.getVaraiablesValeur().equals("Non")) {
			/***
			 * test pour que boc execute un courrier
			 */

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

			// }
		} else if (expType.equals("Externe")) {

			System.out.println(" DANS execute Externe");
			// C* pour que le bouton executer soit activé pour les courriers
			// d'arrivé
			// provisoire .. juste pour activer l'execution des courrier arrivé
			// pour le BOCT
			// if (courrierAriverToDG.getVaraiablesValeur().equals("Non")) {
			// if (vb.getPerson().getAssociatedBOC() != null &&
			// courrierInformations.getTransactionOrdre() == null &&
			// etatID.equals(5)) {
			// KHA
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
			// }
			// provisoire .. juste pour activer l'execution des courrier arrivé
			// pour le BOCT
			// C* pour que le bouton executer soit activé pour les courriers
			// d'arrivé
			
			List<Transaction> allTransactions = appMgr
			.getTransactionByIdDossier(courrierInformations
					.getDossierID());


	courrierInformations.setCourrierAllTransactions(allTransactions);

	Transaction firstTransaction = allTransactions.get(allTransactions
			.size() - 1);
			if(courrierInformations.getCourrierAllTransactions()!=null && courrierInformations.getCourrierAllTransactions().size()>0)
			{ List<Transaction> tousTR = courrierInformations.getCourrierAllTransactions();
			System.out.println(firstTransaction.getTransactionId());
			if(firstTransaction.getExpdest().getTypeExpDest().equals("Externe")){
				expTypeUser=firstTransaction.getExpdest().getExpdestexterne().getTypeutilisateur().getTypeUtilisateurId();
				System.out.println("expTypeUser  "+expTypeUser);
				expediteur=firstTransaction.getExpdest().getExpdestexterne().getExpDestExterneNom();
				if(expTypeUser == 1)
					expediteur=expediteur+ " "+firstTransaction.getExpdest().getExpdestexterne().getExpDestExternePrenom()+" (PP)";
				else 
					expediteur=expediteur+ " (PM)";
				destinataireExpediteur = "EXT";
			}
				
			}
//			if (expTypeUser == 1) {
//				expediteur = expNom + " " + expPrenom + " (PP)";
//				destinataireExpediteur = "EXT";
//			} else {
//				expediteur = expNom + " (PM)";
//				destinataireExpediteur = "EXT";
//			}
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
			

		courrierInformations
				.setCourrierDestinataireReelleDirection(codeUniqueCourrier);

		
		List<TransactionDestination> listTransactionDestination = appMgr
				.getListTransactionDestinationByIdTransaction(courrierInformations
						.getTransactionID());// valeur ancien
												// #firstTransaction.getTransactionId()#
		System.out.println("listTransactionDestination : "
				+ listTransactionDestination.size());
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
		}
		// pour activer l'execution des courrier qui suit un workflow pour le
		// boct et juste la premiere execution
		if (vb.getPerson().isBoc()
				&& courrierInformations.getCourrierCircuit().equals("workflow")) {
			if (etatID.equals(10)
					&& courrierInformations.getTransactionOrdre().equals(1)) {
				courrierInformations.setCourrierAValider(1);
			}
		}
		
			if( !vb.getPerson().isBoc()&& destinataireExpediteur.equals(vb.getPerson().getAssociatedDirection().getShortNameUnit()))
			courrierInformations.setCourrierRecu(0);
			else{
				courrierInformations.setCourrierRecu(1);
			}
//		}
		// detinataire reel *
		StringBuilder destinataire = new StringBuilder("");
		StringBuilder destinataireCourrierReference = new StringBuilder("");

		String unitName;
		// AH
		ListeDestinatairesModel destR;
		// List<String> destinataireCourrierReference=new ArrayList<String>();

		List<Transaction> allTransactions = appMgr
				.getTransactionByIdDossier(courrierInformations.getDossierID());
		courrierInformations.setCourrierAllTransactions(allTransactions);

		List<Transaction> allTransactionsByEtat = appMgr
				.getTransactionByIdDossierByEtat(courrierInformations
						.getDossierID());
		courrierInformations
				.setCourrierAllTransactionsByEtat(allTransactionsByEtat);
		
	
		Transaction firstTransaction = allTransactions.get(allTransactions
				.size() - 1);
	

		Expdest expdestExpediteurREEL = appMgr.getListExpDestByIdExpDest(
				firstTransaction.getExpdest().getIdExpDest()).get(0);
		System.out.println("2019-08-06 expdestExpediteurREEL : "
				+ expdestExpediteurREEL);
		courrierInformations.setExpDest(expdestExpediteurREEL);
		System.out.println("courrierInformations.getDestReelList() :"
				+ courrierInformations.getDestReelList());
		if (courrierInformations.getDestReelList() != null) {
			// AH
			destR = new ListeDestinatairesModel();
//			System.out.println("2019-06-08 : "
//					+ courrierInformations.getDestReelList());
			List<String> destReelList = new ArrayList<String>(
					Arrays.asList(courrierInformations.getDestReelList().split(
							"\\|", -1)));
//			System.out.println(" destReelList size  = " + destReelList.size());
			for (int i = 0; i < destReelList.size(); i++) {
				List<String> destReelElement = new ArrayList<String>(
						Arrays.asList(destReelList.get(i).split(";", -1)));
				

				Integer idExpDest = 0;
//				System.out.println("2019-06-10 : destReelElement.get(1) : "
//						+ destReelElement.get(1));
				if(destReelElement.size()>8){
				if (!destReelElement.get(1).equals("")) {
					idExpDest = Integer.valueOf(destReelElement.get(1));
				}
				String type = destReelElement.get(2);

				Integer ldap = 0;
//				System.out.println("2019-06-10 : destReelElement.get(3) : "
//						+ destReelElement.get(3));

				if (!destReelElement.get(3).equals("")) {
					ldap = Integer.valueOf(destReelElement.get(3));
//					System.out.println("2019-06-10 :  ldaaaaaaaaaaaap : "
//							+ ldap);
				}
				
				String nom = destReelElement.get(4);
				String prenom = destReelElement.get(5);

//				System.out.println("2019-06-10 : nom : " + nom);
//				System.out.println("2019-06-10 : prenom : " + prenom);

				Integer typeUser = 0;
//				System.out.println("2019-06-10 : destReelElement.get(6) : "
//						+ destReelElement.get(6));

				if (!destReelElement.get(6).equals("")) {
					typeUser = Integer.valueOf(destReelElement.get(6));
//					System.out.println("2019-06-10 : typeUser : " + typeUser);

				}
				Integer idDestReelLdap = 0;
//				System.out.println("2019-06-10 : destReelElement.get(7) :"
//						+ destReelElement.get(7));
				if (!destReelElement.get(7).equals("")) {
					idDestReelLdap = Integer.valueOf(destReelElement.get(7));
					
				}
//				System.out.println("2019-06-10 : destReelElement.get(8) :"
//						+ destReelElement.get(8));

				String destReelType = destReelElement.get(8);
			

				if ((vb.isSonede()&& idDestReelLdap==0 && destReelType.equals("Interne-Unité"))||idDestReelLdap != 0) {

					// : workflow
					// ----------------------------------------------------------------------------------------------------

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

						} catch (Exception e) {
							unitName = "Inconnue";
							e.printStackTrace();
						}
						destinataire.append(" / ");
						destinataire.append(unitName);

						// [JS]:Reference Courrier de chaque Destinataire
						destinataireCourrierReference.append(unitName);
						
						List<Transaction> listTransaction = appMgr.getReferenceCourrierByDestinataire(idDestReelLdap, courrierInformations
								.getDossierID());
						if (listTransaction != null
								&& listTransaction.size() > 0)
							destinataireCourrierReference
									.append(" [")
									.append(listTransaction
											.get(0)
											.getCourrierReferenceCorrespondant())
									.append("]"+" / ");
//						destinataireCourrierReference.append("<br/>");

						break;
					} else {
						if (destReelType.equals("Interne-Unité")) {
							
							Unit unit = vb.getHashMapUnit().get(idDestReelLdap);
						

							if (!destinataire.toString().contains(
									unit.getNameUnit())) {
								destinataire.append(" / ");
							
								destinataire.append(unit.getNameUnit());

								// [JS] :Référence de Courrier pour chaque
								// destinataire
								destinataireCourrierReference.append(unit
										.getNameUnit());
							
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
//								destinataireCourrierReference.append("<br/>");
//								System.out
//										.println("2019-06-2019 Destinataire + Référence : "
//												+ destinataireCourrierReference
//														.toString());

								destR = new ListeDestinatairesModel();
//								System.out.println("idDestReelLdap :"
//										+ idDestReelLdap);
								destR.setDestinataireId(idDestReelLdap);
								destR.setDestinataireName(unit.getNameUnit());
//								System.out.println("AH AJOUT "
//										+ unit.getNameUnit());
//								System.out
//										.println("============== affecter annotations unit 1 ===============");

								// List<Annotation> l =
								// appMgr.listeAnnotationParDestinataireEtTransaction(idDestReelLdap,courrierInformations.getDossierID());
								List<Annotation> listeAnnotationParDestinataire = new ArrayList<Annotation>();

								if (appMgr
										.listeAnnotationParDestinataireEtTransactionReell(
												idDestReelLdap,
												courrierInformations
														.getDossierID()) != null) {
									System.out
											.println(" KHA===> Destinataire reel enregistre dans table transactionDestinationRelle");
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
									System.out
											.println(" KHA===> Destinataire reel enregistre dans table expdest");
								}
								
								String otherAnnotation="";
								if (listeAnnotationParDestinataire != null) {
									List<String> listAnnotationDest = new ArrayList<String>();
									for (Annotation a : listeAnnotationParDestinataire) {

										listAnnotationDest.add(String.valueOf(a.getAnnotationId()));
										System.out.println("listAnnotationDest = "+ listAnnotationDest);
										
										TransactionAnnotation ta = appMgr
												.getTransactionByIdAnnotation(a.getAnnotationId())
												.get(0);
										destR.setChooseAnnotationType("tous");
										//Récupérer la transaction de l'annotaion
										if(a.getAnnotationId().intValue()==10){
										 Transaction tr = listTransaction.get(0);
										 otherAnnotation=tr.getTransactionCommentaireAnnotation();
										 System.out.println("####### otherAnnotation "+otherAnnotation);
										 destR.setOtherAnnotation(otherAnnotation);
										 destR.setChooseAnnotationType("autre");
										 }
									}
									destR.setListeAnnotations(listAnnotationDest);
									
									// destR.setCourrierReferenceCorrespondant(tr.getCourrierReferenceCorrespondant());
								}
//								System.out.println("destR.ListeAnnotations = "
//										+ destR.getListeAnnotations());
//
//								System.out
//										.println("============== Fin : affecter annotations  unit 1===============");

								destinatairesAvecAnnotations.add(destR);

//								System.out
//										.println("==============2===============");
								ItemSelected itemSelected = new ItemSelected();
								itemSelected.setItemSelectedId(idDestReelLdap);
								itemSelected.setItemSelectedName(unit
										.getNameUnit());
								listSelectedItem.add(itemSelected);

								Object object = (Object) unit;
								listSelectedObject.add(object);
								listSelectetdUnit.add(unit);
//								System.out
//										.println("============== ===============");

							}
						} 
						else if (destReelType.equals("Interne-Person")) {
							System.out.println("2019-06-09 idDestReelLdap : "
									+ idDestReelLdap);
							Person person = vb.getHashMapAllUser().get(
									idDestReelLdap);
							System.out.println("Interne-Person");
							System.out.println(" : Person ### :"
									+ person.getNom());
							if (!destinataire.toString().contains(
									person.getCn())) {

								destinataire.append(" / ");
								System.out
										.println("Ajout Personne dans liste destinataires");
								System.out.println("Cn Person :"
										+ person.getCn());
								destinataire.append(person.getCn());

								// [JS] :Référence de Courrier pour chaque
								// destinataire
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
											.append("]"+" / ");
//								destinataireCourrierReference.append("<br/>");
								System.out
										.println("2019-06-2019 Destinataire + Référence : "
												+ destinataireCourrierReference
														.toString());

								System.out
										.println("########## destR ##############");
								destR = new ListeDestinatairesModel();
								destR.setDestinataireId(idDestReelLdap);
								destR.setDestinataireName(person.getCn());

								System.out
										.println("============== affecter annotations Person ===============");

								// ajouté le 16-04-2019 : pour séparer les
								// destinataires enregistrés dans table
								// transactionDestinataireReel et dans table
								// expdest

								// List<Annotation> l =
								// appMgr.listeAnnotationParDestinataireEtTransaction(idDestReelLdap,courrierInformations.getDossierID());
								List<Annotation> l = new ArrayList<Annotation>();
								if (appMgr
										.listeAnnotationParDestinataireEtTransactionReell(
												idDestReelLdap,
												courrierInformations
														.getDossierID()) != null) {
									System.out
											.println(" KHA :Person Dans  listeAnnotationParDestinataireEtTransactionReell");
									l = appMgr
											.listeAnnotationParDestinataireEtTransactionReell(
													idDestReelLdap,
													courrierInformations
															.getDossierID());
								} else {
									System.out
											.println(" KHA :Person Dans  listeAnnotationParDestinataireEtTransactionExpDest");
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

										listAnnotationDest.add(String.valueOf(a
												.getAnnotationId()));
										System.out
												.println("listAnnotationDest = "
														+ listAnnotationDest);
										// []
										ta = appMgr
												.getTransactionByIdAnnotation(
														a.getAnnotationId())
												.get(0);
										tr = appMgr
												.getListTransactionByIdTransaction(
														ta.getId()
																.getIdTransaction())
												.get(0);
									}
									destR.setListeAnnotations(listAnnotationDest);
									// destR.setCourrierReferenceCorrespondant(tr.getCourrierReferenceCorrespondant());

								}
								System.out.println("destR.ListeAnnotations = "
										+ destR.getListeAnnotations());

								System.out
										.println("============== Fin : affecter annotations Person===============");
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
							System.out.println("### Externe");
							/*
							 * if (vb.getPerson().isBoc() && !etatID.equals(6))
							 * { courrierInformations.setCourrierAValider(1);
							 * 
							 * }
							 */

							// KHA : executer externe si etat à 5

							if (vb.getPerson().isBoc() && etatID.equals(5)) {
								System.out
										.println("KHA : avalider si vb.getPerson().isBoc() && etatID.equals(5)");

								if (variableExecution.getVaraiablesValeur()
										.equals("Non")) {
									if (vb.getPerson().isResponsableBO()) {

										courrierInformations
												.setCourrierAValider(1);
									}
								} else {
									System.out.println(" ");
									courrierInformations.setCourrierAValider(1);
								}
							}
							System.out.println("idDestReelLdap :"
									+ idDestReelLdap);
							Expdestexterne destReelExterne = appMgr
									.getExpediteurById(idDestReelLdap).get(0);
							if (destReelExterne.getTypeutilisateur()
									.getTypeUtilisateurId().equals(1)) {
								String dest = destReelExterne
										.getExpDestExternePrenom()
										+ " "
										+ destReelExterne
												.getExpDestExterneNom();
								System.out.println("dest :" + dest);
								if (!destinataire.toString().contains(dest)) {
									destinataire.append(" / ");
									destinataire.append(dest);

									// [JS] :Référence de Courrier pour chaque
									// destinataire
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
//									destinataireCourrierReference
//											.append("<br/>");
									System.out
											.println("2019-06-2019 Destinataire + Référence : "
													+ destinataireCourrierReference
															.toString());

									destR = new ListeDestinatairesModel();
									destR.setDestinataireId(idDestReelLdap);
									destR.setDestinataireName(dest);
									System.out
											.println("type1 AH AJOUT " + dest);
									System.out
											.println("============== affecter annotations Externe ===============");
									System.out.println("");
									List<Annotation> l = appMgr
											.listeAnnotationParDestinataireEtTransactionReell(
													idDestReelLdap,
													courrierInformations
															.getDossierID());

									if (l != null) {
										List<String> listAnnotationDest = new ArrayList<String>();
										for (Annotation a : l) {

											listAnnotationDest
													.add(String.valueOf(a
															.getAnnotationId()));
											System.out
													.println("listAnnotationDest = "
															+ listAnnotationDest);
											// []
											ta = appMgr
													.getTransactionByIdAnnotation(
															a.getAnnotationId())
													.get(0);
											tr = appMgr
													.getListTransactionByIdTransaction(
															ta.getId()
																	.getIdTransaction())
													.get(0);
										}
										destR.setListeAnnotations(listAnnotationDest);
										// destR.setCourrierReferenceCorrespondant(tr.getCourrierReferenceCorrespondant());
									}
									System.out
											.println("destR.ListeAnnotations = "
													+ destR.getListeAnnotations());

									System.out
											.println("============== Fin : affecter annotations Externe===============");
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

									// [JS] :Référence de Courrier pour chaque
									// destinataire
									destinataireCourrierReference.append(dest);
									System.out.println("Id Expdest Externe :"
											+ destReelExterne
													.getIdExpDestExterne());
									List<Transaction> listTransaction = appMgr
											.getReferenceCourrierByDestinataire(destReelExterne
													.getIdExpDestExterne(), courrierInformations
													.getDossierID());
									if (listTransaction != null
											&& listTransaction.size() > 0)
										destinataireCourrierReference
												.append(" [")
												.append(listTransaction
														.get(0)
														.getCourrierReferenceCorrespondant())
												.append("]"+" / ");
//									destinataireCourrierReference
//											.append("<br/>");
									System.out
											.println("2019-06-2019 Destinataire + Référence : "
													+ destinataireCourrierReference
															.toString());

									// KHA ajouté 08-02-2019
									destR = new ListeDestinatairesModel();
									destR.setDestinataireId(idDestReelLdap);
									destR.setDestinataireName(dest);
									System.out
											.println("type2 AH AJOUT " + dest);

									System.out
											.println("============== affecter annotations destReelExterne ===============");
									System.out.println("idDestReelLdap= "
											+ idDestReelLdap);
									System.out.println("Dossier id ="
											+ courrierInformations
													.getDossierID());
									List<Annotation> l = appMgr
											.listeAnnotationParDestinataireEtTransactionReell(
													idDestReelLdap,
													courrierInformations
															.getDossierID());
									System.out.println("KHA===> l size =  "
											+ l.size());
									if (l != null) {
										List<String> listAnnotationDest = new ArrayList<String>();
										for (Annotation a : l) {

											listAnnotationDest
													.add(String.valueOf(a
															.getAnnotationId()));
											System.out
													.println("listAnnotationDest = "
															+ listAnnotationDest);
											// []
											ta = appMgr
													.getTransactionByIdAnnotation(
															a.getAnnotationId())
													.get(0);
											tr = appMgr
													.getListTransactionByIdTransaction(
															ta.getId()
																	.getIdTransaction())
													.get(0);
										}
										destR.setListeAnnotations(listAnnotationDest);
										// destR.setCourrierReferenceCorrespondant(tr.getCourrierReferenceCorrespondant());
									}
									System.out
											.println("destR.ListeAnnotations = "
													+ destR.getListeAnnotations());

									System.out
											.println("============== Fin : affecter annotations destReelExterne===============");

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
							destinataireCourrierReference.append("--------");
						}
					}
				} else {
					// courrier qui n'a pas des étaps de validation
					if (!listTransactionDestination.isEmpty()) {
						for (TransactionDestination transactionDestination : listTransactionDestination) {
							// if
							// (idExpDest.equals(transactionDestination.getId().getIdExpDest()))
							// {
//							destinataire.append(" / ");
							if (type.equals("Interne-Person")) {
								if (ldap.equals(vb.getPerson().getId())) {
									courrierInformations.setCourrierRecu(1);
								}
								courrierInformations
										.setTransactionDestination(transactionDestination);
								// destinataire = new StringBuilder(" / ");

								Person person = vb.getHashMapAllUser()
										.get(ldap);
								if (!destinataire.toString().contains(
										person.getCn())) {
									destinataire.append(" / ");
									destinataire.append(person.getCn());

									// [JS] :Référence de Courrier pour chaque
									// destinataire
									destinataireCourrierReference.append(person
											.getCn());
									List<Transaction> listTransaction = appMgr
											.getReferenceCourrierByDestinataire(ldap, courrierInformations
													.getDossierID());
									if (listTransaction != null
											&& listTransaction.size() > 0)
										destinataireCourrierReference
												.append(" [")
												.append(listTransaction
														.get(0)
														.getCourrierReferenceCorrespondant())
												.append("]"+" / ");

									// destinataire.append(transactionDestination.getId().get)
									// KHA - ajouté 08-02-2019
									destR = new ListeDestinatairesModel();
									destR.setDestinataireId(ldap);
									destR.setDestinataireName(person.getCn());

									// List<Annotation> l =
									// appMgr.listeAnnotationParDestinataireEtTransaction(ldap,courrierInformations.getDossierID());
									List<Annotation> l = new ArrayList<Annotation>();
									if (appMgr
											.listeAnnotationParDestinataireEtTransactionReell(
													ldap, courrierInformations
															.getDossierID()) != null) {
										l = appMgr
												.listeAnnotationParDestinataireEtTransactionReell(
														idDestReelLdap,
														courrierInformations
																.getDossierID());
									} else {

										l = appMgr
												.listeAnnotationParDestinataireEtTransactionExpDest(
														ldap,
														courrierInformations
																.getDossierID());
									}
									if (l != null) {
										List<String> listAnnotationDest = new ArrayList<String>();
										for (Annotation a : l) {

											listAnnotationDest
													.add(String.valueOf(a
															.getAnnotationId()));
											System.out
													.println("listAnnotationDest = "
															+ listAnnotationDest);
											// []
											ta = appMgr
													.getTransactionByIdAnnotation(
															a.getAnnotationId())
													.get(0);
											tr = appMgr
													.getListTransactionByIdTransaction(
															ta.getId()
																	.getIdTransaction())
													.get(0);
										}
										destR.setListeAnnotations(listAnnotationDest);
										// destR.setCourrierReferenceCorrespondant(tr.getCourrierReferenceCorrespondant());
									}

									destinatairesAvecAnnotations.add(destR);

									ItemSelected itemSelected = new ItemSelected();
									itemSelected.setItemSelectedId(ldap);
									itemSelected.setItemSelectedName(person
											.getCn());
									listSelectedItem.add(itemSelected);
									Object object = (Object) person;
									listSelectedObject.add(object);
									listSelectedPerson.add(person);

									// break;

									
								}
							} else if (type.equals("Interne-Unité")) {
								if (vb.getPerson().isResponsable()
										&& ldap.equals(vb.getPerson()
												.getAssociatedDirection()
												.getIdUnit())) {
									courrierInformations.setCourrierRecu(1);
								}
								courrierInformations
										.setTransactionDestination(transactionDestination);
								// destinataire = new StringBuilder(" / ");
								Unit unit = vb.getHashMapUnit().get(ldap);
								if (!destinataire.toString().contains(
										unit.getNameUnit())) {
									destinataire.append(" / ");
									destinataire.append(unit.getNameUnit());
									// [JS] :Référence de Courrier pour chaque
									// destinataire
									destinataireCourrierReference.append(unit
											.getNameUnit());
									List<Transaction> listTransaction = appMgr
											.getReferenceCourrierByDestinataire(ldap, courrierInformations
													.getDossierID());
									if (listTransaction != null
											&& listTransaction.size() > 0)
										destinataireCourrierReference
												.append(" [")
												.append(listTransaction
														.get(0)
														.getCourrierReferenceCorrespondant())
												.append("]"+" / ");
//									destinataireCourrierReference
//											.append("<br/>");

									destR = new ListeDestinatairesModel();
									destR.setDestinataireId(ldap);
									destR.setDestinataireName(unit
											.getNameUnit());

									// List<Annotation> l =
									// appMgr.listeAnnotationParDestinataireEtTransaction(ldap,courrierInformations.getDossierID());
									List<Annotation> l = new ArrayList<Annotation>();
									if (appMgr
											.listeAnnotationParDestinataireEtTransactionReell(
													ldap, courrierInformations
															.getDossierID()) != null) {
										l = appMgr
												.listeAnnotationParDestinataireEtTransactionReell(
														idDestReelLdap,
														courrierInformations
																.getDossierID());
									} else {

										l = appMgr
												.listeAnnotationParDestinataireEtTransactionExpDest(
														ldap,
														courrierInformations
																.getDossierID());
									}
									if (l != null) {
										List<String> listAnnotationDest = new ArrayList<String>();
										for (Annotation a : l) {

											listAnnotationDest
													.add(String.valueOf(a
															.getAnnotationId()));
											// []
											ta = appMgr
													.getTransactionByIdAnnotation(
															a.getAnnotationId())
													.get(0);
											tr = appMgr
													.getListTransactionByIdTransaction(
															ta.getId()
																	.getIdTransaction())
													.get(0);
													destR.setChooseAnnotationType("tous");
										}
										destR.setListeAnnotations(listAnnotationDest);
										// destR.setCourrierReferenceCorrespondant(tr.getCourrierReferenceCorrespondant());
									}

									destinatairesAvecAnnotations.add(destR);
									ItemSelected itemSelected = new ItemSelected();

									itemSelected.setItemSelectedId(ldap);
									itemSelected.setItemSelectedName(unit
											.getNameUnit());
									listSelectedItem.add(itemSelected);
									Object object = (Object) unit;
									listSelectedObject.add(object);
									listSelectetdUnit.add(unit);

									// break;
								}
								

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

									List<Annotation> l = new ArrayList<Annotation>();
									if (appMgr
											.listeAnnotationParDestinataireEtTransactionReell(
													ldap, courrierInformations
															.getDossierID()) != null) {
										l = appMgr
												.listeAnnotationParDestinataireEtTransactionReell(
														idDestReelLdap,
														courrierInformations
																.getDossierID());
									} else {

										l = appMgr
												.listeAnnotationParDestinataireEtTransactionExpDest(
														ldap,
														courrierInformations
																.getDossierID());
									}
									if (l != null) {
										List<String> listAnnotationDest = new ArrayList<String>();
										for (Annotation a : l) {

											listAnnotationDest
													.add(String.valueOf(a
															.getAnnotationId()));
											
											// []
											ta = appMgr
													.getTransactionByIdAnnotation(
															a.getAnnotationId())
													.get(0);
											tr = appMgr
													.getListTransactionByIdTransaction(
															ta.getId()
																	.getIdTransaction())
													.get(0);
										}
										destR.setListeAnnotations(listAnnotationDest);
									}
								
									destinatairesAvecAnnotations.add(destR);
									ItemSelected itemSelected = new ItemSelected();
									itemSelected.setItemSelectedId(ldap);
									itemSelected.setItemSelectedName(vb
											.getCentralBoc().getNameBOC());
									listSelectedItem.add(itemSelected);

								}

							} else if (type.equals("Externe")) {
								if (typeUser.equals(1)) {
									if (!destinataire.toString().contains(
											nom + " " + prenom + " (PP)")) {
										destinataire.append(" / ");
										destinataire.append(nom + " " + prenom
												+ " (PP)");
										destR = new ListeDestinatairesModel();
										destR.setDestinataireId(ldap);
										destR.setDestinataireName(nom + " "
												+ prenom + " (PP)");

										List<Annotation> l = appMgr
												.listeAnnotationParDestinataireEtTransactionReell(
														ldap,
														courrierInformations
																.getDossierID());

										if (l != null) {
											List<String> listAnnotationDest = new ArrayList<String>();
											for (Annotation a : l) {

												listAnnotationDest
														.add(String.valueOf(a
																.getAnnotationId()));
												System.out
														.println("listAnnotationDest = "
																+ listAnnotationDest);
												// []
												ta = appMgr
														.getTransactionByIdAnnotation(
																a.getAnnotationId())
														.get(0);
												tr = appMgr
														.getListTransactionByIdTransaction(
																ta.getId()
																		.getIdTransaction())
														.get(0);
											}
											destR.setListeAnnotations(listAnnotationDest);
											// destR.setCourrierReferenceCorrespondant(tr.getCourrierReferenceCorrespondant());
										}


										destinatairesAvecAnnotations.add(destR);

										
										ItemSelected itemSelected = new ItemSelected();
										itemSelected.setItemSelectedId(ldap);
										itemSelected.setItemSelectedName(nom
												+ " " + prenom);
										listSelectedItem.add(itemSelected);
										Pp pp = (Pp) appMgr
												.getPPByReferenceExpediteur(
														ldap).get(0);
										Object object = (Object) pp;
										listSelectedObject.add(object);
										listSelectetdPP.add(pp);

									

									}
								} else {
									if (!destinataire.toString().contains(
											nom + " (PM)")) {

										destinataire.append(" / ");
										destinataire.append(nom + " (PM)");
										// KHA - ajouté 08-02-2019
										destR = new ListeDestinatairesModel();
										destR.setDestinataireId(ldap);
										destR.setDestinataireName(nom + " (PM)");
										

										List<Annotation> l = appMgr
												.listeAnnotationParDestinataireEtTransactionReell(
														ldap,
														courrierInformations
																.getDossierID());

										if (l != null) {
											List<String> listAnnotationDest = new ArrayList<String>();
											for (Annotation a : l) {

												listAnnotationDest
														.add(String.valueOf(a
																.getAnnotationId()));
												
												
												ta = appMgr
														.getTransactionByIdAnnotation(
																a.getAnnotationId())
														.get(0);
												tr = appMgr
														.getListTransactionByIdTransaction(
																ta.getId()
																		.getIdTransaction())
														.get(0);
											}
											destR.setListeAnnotations(listAnnotationDest);
											// destR.setCourrierReferenceCorrespondant(tr.getCourrierReferenceCorrespondant());
										}
										
										
										destinatairesAvecAnnotations.add(destR);
									
										ItemSelected itemSelected = new ItemSelected();
										itemSelected.setItemSelectedId(ldap);
										itemSelected.setItemSelectedName(nom
												+ " " + prenom);
										listSelectedItem.add(itemSelected);
										Pm pm = (Pm) appMgr
												.getPMByReferenceExpediteur(
														ldap).get(0);
										Object object = (Object) pm;
										listSelectedObject.add(object);
										listSelectetdPM.add(pm);
									

									}
								}
							}
							// }
						}
					}
					if (courrierInformations.getCourrierRecu() == 1
							&& (etatID.equals(10) || etatID.equals(2))) {

						courrierInformations.setCourrierAValider(1);
					} else {
						// provisoire .. juste pour activer l'execution des
						// courrier arrivé pour le BOCT
						if (courrierAriverToDG.getVaraiablesValeur().equals(
								"Non")) {
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
				courrierInformations.setListSelectedItemDest(listSelectedItem);
				courrierInformations.setListSelectedObject(listSelectedObject);

				courrierInformations.setListSelectedPerson(listSelectedPerson);
				courrierInformations.setListSelectetdUnit(listSelectetdUnit);
				courrierInformations.setListSelectetdBoc(listSelectetdBoc);

					}
				}

			}

			destinataire.delete(0, 3);

			courrierInformations.setCourrierDestinataireReelle(destinataire
					.toString());
		
		lastIndex = destinataireCourrierReference.lastIndexOf("/");
		System.out.println("lastIndex "+lastIndex);
		System.out.println("destinataireCourrierReference "+destinataireCourrierReference);
		//2020-06-08
		if(lastIndex>0)
			courrierInformations.setReferenceDestinataireReelle(destinataireCourrierReference
					.substring(0, lastIndex));
		
		courrierInformations
				.setListeDestinatairesAvecAnnotations(destinatairesAvecAnnotations);
		
		if ((courrierInformations.getTransactionDateConsultation() == null && courrierInformations
				.getCourrierRecu() != 1)
				|| (courrierInformations.getTransactionDestination()
						.getTransactionDestDateConsultation() == null && courrierInformations
						.getCourrierRecu() == 1)) {
			
			courrierInformations
					.setStyle("tableau_liste_courrier_non_consulte_gras");
			styleMessage = "tableau_liste_courrier_non_consulte_gras";
		}
		else{
			courrierInformations
			.setStyle("tableau_liste_courrier_consulte");
		}
		
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
		
			if (vb.getPerson().isResponsable() && !vb.getPerson().isBoc()) {
				if (vb.getPerson().getAssociatedDirection().getIdUnit() == d
						.getDestinataireId()) {
					idDest = vb.getPerson().getId();
				} else {
					
					idDest = d.getDestinataireId();
				}
			}else{
			System.out.println("");
				
			}
			listeIdDest.add(idDest);
			//
		}
		if(vb.getPerson().isBoc()){
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
				System.out.println("heloo");
					
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
		
		
		// récuperer liste des memebres de bureau d'ordre connecté
		List<Integer> listeIdMembresBOc=new ArrayList<Integer>();
		if(vb.getPerson().isBoc()){
		List<Person> listMembresBoc = vb.getPerson().getAssociatedBOC().getMembersBOC();
		for(Person membres:listMembresBoc){

			listeIdMembresBOc.add(membres.getId());
		}
		}
		
		Courrier courrier=appMgr.getCourrierByIdCourrier(courrierInformations.getCourrierID()).get(0);	
		if (courrier.getCourrierAvecDocumentPhysique() != null
				&& courrier.getCourrierAvecDocumentPhysique() == true ) {
			
			/* si Le personne connecté est un responsable et qui est l'éditeur  : bouton reception physique ne s'affiche pas**/
			if (vb.getPerson().isResponsable()  && !vb.getPerson().isBoc()) {
				if (cupExpDest.getIdExpDestLdap() != null
						&& vb.getPerson().getAssociatedDirection()
								.getIdUnit().equals( cupExpDest
								.getIdExpDestLdap())){
				etatReceptionPhysique = false;
				}else if(listeIdDest.contains(vb.getPerson().getId())){
					
					etatReceptionPhysique = true;
					if(appMgr.getListCourrierAvecReceptionPhysiqueByEtat(courrier.getIdCourrier(),transaction.getTransactionId()).size() !=0 ){
						etatReceptionPhysique = false;
						
					}
					
				}

			}	
					
			
				//is BOC et n'est pas l'editeur de courrier DONC FAIT LE VALIDATION HEARCHQIUE		
			
			else if ((vb.getPerson().isBoc() && !listeIdMembresBOc.contains(idEditeur))
					|| listeIdDest.contains(vb.getPerson().getId())
					|| (cupExpDest.getIdExpDestLdap()!=null && vb.getPerson().getId() == cupExpDest.getIdExpDestLdap().intValue())){
				// si le connectee est l'expediteur, le destinataire reel ou le
			// BO
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

			courrierInformations
					.setCourrierAvecDocumentPhysique(etatReceptionPhysique);
		}
	}

	public void getSelectionRowModifModeTransmission() {
	
		}
	public void getSelectionRow() {
		try {

			vb.setSelectedListCourrier("CRmois");
			vb.setDestinataireReel("");
			vb.setPremiereEntreeTransfert(1);
			Transaction transaction = new Transaction();
			CourrierInformations consulterInformations = selectedCourrier;
			
			vb.setAllTransactions(consulterInformations.getCourrierAllTransactions());
			vb.setCourrierInformations(selectedCourrier);
			Courrier courrier=appMgr.getCourrierByIdCourrier(consulterInformations.getCourrierID()).get(0);
			selectedItemsTr=courrier.getTransmission().getTransmissionId().toString();
			vb.setMasquerPanelDetailCourrier(true);	
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
			System.out
					.println("***********************************************************");
			vb.setListSelectedItem(consulterInformations
					.getListSelectedItemDest());
			System.out.println("ListSelectedItem size = "
					+ vb.getListSelectedItem().size());

			if (consulterInformations.getListSelectedObject() != null) {
				vb.setCopyListSelectedObject(consulterInformations
						.getListSelectedObject());
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
			courrier = consulterInformations.getCourrier();
			vb.setCourrier(courrier);
			transaction = consulterInformations.getTransaction();
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
									transactionDestination
											.setTransactionDestDateConsultation(new Date());
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
				
				

				if (consulterInformations.getCourrierRecu() == 1
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
//					chargementNotification(consulterInformations);
				} else if (transaction.getIdUtilisateur() == vb.getPerson()
						.getId()
						&& transaction.getTransactionDateConsultation() == null) {
					transaction.setTransactionDateConsultation(new Date());
					appMgr.update(transaction);
				}
			}
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
			vb.setDestinataireReel(consulterInformations.getReferenceDestinataireReelle());
			
			//vb.setDestinataireReel(consulterInformations
				//	.getCourrierDestinataireReelle());
			
			
			vb.setReferenceDestinataireReel(consulterInformations
					.getReferenceDestinataireReelle());

			// AH
			vb.setListeDestinataire(consulterInformations.getListeDestinatairesAvecAnnotations());

			
			
			

			// ***
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
			//Vider les listes des detinatires avant de transferer le courrier pour ne pas garder le destinataire de l'ancien courrier
			vb.setCopyListSelectedUnit(new ArrayList<Unit>());
			vb.setCopyListSelectedPerson(new ArrayList<Person>());
			vb.setCopyListPP(new ArrayList<Pp>());
			vb.setCopyListPM(new ArrayList<Pm>());
			//[JS]: 2020-04-30
			commentaireReceptionPhysique=null;

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
		

	}

	public void getSelectionRowForResponse() {
		
	}

	public void repondre() {
		
	}

	
	
	public void execute() {
		
					
					

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
		// selectItemsTr.add(new
		// SelectItem(messageSource.getMessage("AutreLabel",
		// new Object[] {}, lm.createLocal())));
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
	public Long getCountCourrierAnnee(String recent,
			HashMap<String, Object> filterMap, String transmissionCourrierJour,
			String typeCourrierTraitementJour, String typeCourrier,
			String typeCourrierValidation, String categorieCourrier,
			String courrierRubrique, boolean forTotal) {
		Long countCourrier = 0L;
		Calendar calendar = Calendar.getInstance();
		if (recent.equals("annee")) {
			calendar.set(Calendar.MONTH, 0);
		}
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		dateDebut = calendar.getTime();

		if (vb.getPerson().isBoc()) {
			countCourrier = appMgr.CountAllCourrierBOCByCriteria(filterMap, 13,
					dateDebut, dateFin, type, type1, listIdBocMembers,
					transmissionCourrierJour, typeCourrierTraitementJour,
					categorieCourrier);
		} else {
			countCourrier = appMgr.CountAllCourrierEnvoyerANDRecuByCriteria(vb
					.getPerson().isResponsable(), listIdsSousUnit,
					listIdsSubordonne, filterMap,
					consultationCourrierSecretaire,
					consultationCourrierSubordonne,
					consultationCourrierSousUnite, 12, dateDebut, dateFin,
					type, type1, typeSecretaire, idUser, typeTransmission,
					typeCourrierValidation, Integer.valueOf(courrierRubrique),
					forTotal, typeCourrier);
		}

		return countCourrier;
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
	public Long getCountCourrier(String recent,
			HashMap<String, Object> filterMap, String transmissionCourrierJour,
			String typeCourrierTraitementJour, String typeCourrier,
			String typeCourrierValidation, String categorieCourrier,
			String courrierRubrique, boolean forTotal) {
		Long countCourrier = 0L;
		Calendar calendar = Calendar.getInstance();
		if (recent.equals("annee")) {
			calendar.set(Calendar.MONTH, 0);
		}
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		dateDebut = calendar.getTime();
		// AAAA
		if (vb.getPerson().isBoc()) {
			countCourrier = appMgr.CountAllCourrierBOCByCriteria(filterMap, 14,
					dateDebut, dateFin, type, type1, listIdBocMembers,
					transmissionCourrierJour, typeCourrierTraitementJour,
					categorieCourrier);
		} else {
			countCourrier = appMgr.CountAllCourrierEnvoyerANDRecuByCriteria(vb
					.getPerson().isResponsable(), listIdsSousUnit,
					listIdsSubordonne, filterMap,
					consultationCourrierSecretaire,
					consultationCourrierSubordonne,
					consultationCourrierSousUnite, 13, dateDebut, dateFin,
					type, type1, typeSecretaire, idUser, typeTransmission,
					typeCourrierValidation, Integer.valueOf(courrierRubrique),
					forTotal, typeCourrier);
		}

		return countCourrier;
	}
	public Long getCountCourrierEnvoyer() {
		return countCourrierEnvoyer;
	}

	public void setCountCourrierEnvoyer(Long countCourrierEnvoyer) {
		this.countCourrierEnvoyer = countCourrierEnvoyer;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int ReturnBocAssociéeUnite(Unit u) {

		if (u.getAssociatedBOC() != null)
			return u.getAssociatedBOC().getIdBOC();
		else
			return ReturnBocAssociéeUnite(u.getAssociatedUnit());

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


	
				
					
	public Date getDateCourrier() {
		return dateCourrier;
	}

	public void setDateCourrier(Date dateCourrier) {
		this.dateCourrier = dateCourrier;
	}

	public int getIdBocExpediteur() {
		return idBocExpediteur;
	}

	public void setIdBocExpediteur(int idBocExpediteur) {
		this.idBocExpediteur = idBocExpediteur;
	}

	public int getIdExpediteur() {
		return IdExpediteur;
	}

	public void setIdExpediteur(int idExpediteur) {
		IdExpediteur = idExpediteur;
	}

	public void setIdBocDestinataire(int idBocDestinataire) {
		this.idBocDestinataire = idBocDestinataire;
	}

	public int getIdBocDestinataire() {
		return idBocDestinataire;
	}

	public void setDestinationReel(Person destinationReel) {
		this.destinationReel = destinationReel;
	}

	public Person getDestinationReel() {
		return destinationReel;
	}

	public String getTypeUserDes() {
		return typeUserDes;
	}

	public void setTypeUserDes(String typeUserDes) {
		this.typeUserDes = typeUserDes;
	}

	public String getTexteTypeCourrier() {
		return texteTypeCourrier;
	}

	public void setTexteTypeCourrier(String texteTypeCourrier) {
		this.texteTypeCourrier = texteTypeCourrier;
	}

	public void setStyleMessage(String styleMessage) {
		this.styleMessage = styleMessage;
	}

	public String getStyleMessage() {
		return styleMessage;
	}

	public void setUserInput(String userInput) {
		this.userInput = userInput;
	}

	public String getUserInput() {
		return userInput;
	}

	public void setVariableCourrie(Integer variableCourrie) {
		
		this.variableCourrie = variableCourrie;
	}

	public Integer getVariableCourrie() {
		

		return variableCourrie;
	}

	public void setVariabledeTest2(String variabledeTest2) {
		this.variabledeTest2 = variabledeTest2;
	}

	public String getVariabledeTest2() {
		return variabledeTest2;
	}

	public void setCodeUniqueCourrier(String codeUniqueCourrier) {
		this.codeUniqueCourrier = codeUniqueCourrier;
	}

	public String getCodeUniqueCourrier() {
		return codeUniqueCourrier;
	}

	public void method() {
		System.out.println("methode ");
		Map<String, String> map = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		String name1 = (String) map.get("name1");
		String name2 = (String) map.get("name2");
		

	}

	public void setMyStrings() {
		

		Map<String, String> requestParamMap = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();

		String string1 = requestParamMap.get("string1"); // Hello
		String string2 = requestParamMap.get("string2"); // World
		

	}

	// Code de Test
	public void action() {

		
		String value = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("hidden1");
		List<Courrier> listCourrier = appMgr.getCourrierByIdCourrier(Integer
				.valueOf(value));
		if (listCourrier != null) {
			Courrier courrier = listCourrier.get(0);
			vb.setCourrier(courrier);
			status1 = true;
			
		}
		String value2 = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("hidden2");
		
		vb.setCopyExpReelNom(value2);
		
		String value8 = FacesContext.getCurrentInstance().getExternalContext()
		.getRequestParameterMap().get("hidden8");
		vb.setCodeUniqueCourrier(value8);
		String value3 = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("hidden3");
	
		String value4 = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("hidden4");
		List<Transaction> listTransaction = appMgr
				.getListTransactionByIdTransaction(Integer.valueOf(value4));
		vb.setAllTransactions(listTransaction);
		if (listTransaction != null) {
			Transaction tr = listTransaction.get(0);			

			vb.setTransaction(tr);
		}
		vb.setDestinataireReel(value3+vb.getTransaction().getCourrierReferenceCorrespondant());

		String value5 = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("hidden5");
		String value6 = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("hidden6");
		String value7 = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("hidden7");

		

		ListeDestinatairesModel destR = new ListeDestinatairesModel();
		destinatairesAvecAnnotations = new ArrayList<ListeDestinatairesModel>();

		destR.setDestinataireId(Integer.valueOf(value5));
		destR.setDestinataireName(value6);
		// destR.setAnnotations(value7);

		// /lioste des annotations
		List<Annotation> l = new ArrayList<Annotation>();
		// String value0 = FacesContext.getCurrentInstance().
		// getExternalContext().getRequestParameterMap().get("hidden0");
		

		List<CourrierDossier> listCourrierDossier = appMgr
				.getCourrierDossierByIdCourrier(Integer.valueOf(value));
		if (listCourrierDossier != null) {
			CourrierDossier cd = listCourrierDossier.get(0);
			int value0 = cd.getId().getDossierId();
			

			if (appMgr.listeAnnotationParDestinataireEtTransactionReell(
					Integer.valueOf(value5), value0) != null) {
				
				l = appMgr.listeAnnotationParDestinataireEtTransactionReell(
						Integer.valueOf(value5), value0);
			} else {

				l = appMgr.listeAnnotationParDestinataireEtTransactionExpDest(
						Integer.valueOf(value5), value0);
				
			}

			if (l != null) {
				List<String> listAnnotationDest = new ArrayList<String>();
				for (Annotation a : l) {
					
					listAnnotationDest.add(String.valueOf(a.getAnnotationId()));
					
					// []
					ta = appMgr.getTransactionByIdAnnotation(
							a.getAnnotationId()).get(0);
					// tr=appMgr.getListTransactionByIdTransaction(ta.getId().getIdTransaction()).get(0);
				}
				destR.setListeAnnotations(listAnnotationDest);
				// destR.setCourrierReferenceCorrespondant(tr.getCourrierReferenceCorrespondant());
			}
			

			destinatairesAvecAnnotations.add(destR);
			vb.setListeDestinataire(destinatairesAvecAnnotations);

		}

		setHidden1(value);
		vb.setAfficheLienLiees(false);
		// return "start";
	}


	public String getHidden1() {
		

		return hidden1;
	}

	public void setHidden1(String hidden1) {
		

		this.hidden1 = hidden1;
	}

	public String getHidden2() {
		
		return hidden2;
	}

	public void setHidden2(String hidden2) {
		

		this.hidden2 = hidden2;
	}

	public List<CourrierInformations> getListCourrier() {
		return listCourrier;
	}

	public void setListCourrier(List<CourrierInformations> listCourrier) {
		this.listCourrier = listCourrier;
	}

	public boolean isReceptionphysiqueNonLivre() {
		return receptionphysiqueNonLivre;
	}

	public void setReceptionphysiqueNonLivre(boolean receptionphysiqueNonLivre) {
		this.receptionphysiqueNonLivre = receptionphysiqueNonLivre;
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
	public boolean isDisbledBontonConsultation() {
		return disbledBontonConsultation;
	}

	public void setDisbledBontonConsultation(boolean disbledBontonConsultation) {
		this.disbledBontonConsultation = disbledBontonConsultation;
	}

	public boolean isShowModificationButton() {
		return showModificationButton;
	}

	public void setShowModificationButton(boolean showModificationButton) {
		this.showModificationButton = showModificationButton;
	}

	public String getSelectedItemsTr() {
		return selectedItemsTr;
	}

	public void setSelectedItemsTr(String selectedItemsTr) {
		this.selectedItemsTr = selectedItemsTr;
	}
public void updateModeTrRecent() {
		try {
//			System.out.println("{{{{{{{{{{{{{{{{{{{{{{{ updateb  }}}}}}}}}}}}}}}}}}}}}}}}");
			courrierInformations=vb.getCourrierInformations();
//			System.out.println("courrierInformations==================> "+courrierInformations);
		Courrier courrier=appMgr.getCourrierByIdCourrier(courrierInformations.getCourrierID()).get(0);
		System.out.println("courrier==> "+courrier.getTransmission().getTransmissionId());
		courrier.setTransmission(appMgr.getTransmissionById(
				Integer.valueOf(selectedItemsTr)).get(0));
	
			appMgr.update(courrier);
			status1=true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			status2=true;
			e.printStackTrace();
		}
	}

	// |JS} =================================================
	// [JS] : 2020-03-16 =====================================================
	private Date dateReceptionPhysique;
	private String commentaireReceptionPhysique;
	private boolean erreur;

	public Date getDateReceptionPhysique() {
//		System.out.println("GET DateReceptionPhysique ================>  "+dateReceptionPhysique);
		return dateReceptionPhysique;
	}

	public void setDateReceptionPhysique(Date dateReceptionPhysique) {
//		System.out.println("SET DateReceptionPhysique ================>  "+dateReceptionPhysique);

		this.dateReceptionPhysique = dateReceptionPhysique;
	}

	public String getCommentaireReceptionPhysique() {
//		System.out.println("GET CommentaireReceptionPhysique( ================>  "+commentaireReceptionPhysique);

		return commentaireReceptionPhysique;
	}

	public void setCommentaireReceptionPhysique(
			String commentaireReceptionPhysique) {
//		System.out.println("SET CommentaireReceptionPhysique( ================>  "+commentaireReceptionPhysique);

		this.commentaireReceptionPhysique = commentaireReceptionPhysique;
	}

    public void valider(){
    	
    	status1=false;
    	status2=false;
    	erreur =false;
    	ajoutReceptionPhysiqueOK=true;

    	if(dateReceptionPhysique==null){
    		ajoutReceptionPhysiqueOK=false;
    	}
    	if(!ajoutReceptionPhysiqueOK){
    		FacesContext.getCurrentInstance().addMessage(
					"messages",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "* Date de réception est un champs obligatoire ",
							""));
    	}else{
    	try{
    		TransactionDestination transactionDestination = vb.getTransactionDestination();
    		Date dateDuJour = new Date();
    		if(dateReceptionPhysique.compareTo(dateDuJour)==0 || 
    				dateReceptionPhysique.compareTo(dateDuJour)<0){
    			Etat etat = new Etat();
    			etat = appMgr.listEtatByRef(8).get(0);
    			transactionDestination.setTransactionDestEtatReceptionPhysique(etat);
    			transactionDestination.setTransactionDestDateReceptionPhysique(dateReceptionPhysique);
    			transactionDestination.setTransactionDestCommentaireReceptionPhysique(commentaireReceptionPhysique);
				appMgr.update(transactionDestination);
				status1=true;    
    	} else{
    		System.out.println("#### La date de réception physique doit être inférieure ou égale à la date d'aujourd'hui ");
    		erreur =true;	 
    		 }
     	}catch(Exception e){

    		e.printStackTrace();
    		status2=true;

    	}
    	}
    	
    }
	public TransactionDestination getTransactionDestination2() {
		return transactionDestination2;
	}

	public void setTransactionDestination2(
			TransactionDestination transactionDestination2) {
		this.transactionDestination2 = transactionDestination2;
	}
	  public void setSelectItemsTr2(ArrayList<SelectItem> selectItemsTr2) {
			this.selectItemsTr2 = selectItemsTr2;
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

		public void setAjoutReceptionPhysiqueOK(boolean ajoutReceptionPhysiqueOK) {
			this.ajoutReceptionPhysiqueOK = ajoutReceptionPhysiqueOK;
		}

		public boolean isAjoutReceptionPhysiqueOK() {
			return ajoutReceptionPhysiqueOK;
		}

		public List<CourrierInformations> getListCourriersLiees() {
			return listCourriersLiees;
		}

		public void setListCourriersLiees(List<CourrierInformations> listCourriersLiees) {
			this.listCourriersLiees = listCourriersLiees;
		}

		public boolean isErreur() {
			return erreur;
		}

		public void setErreur(boolean erreur) {
			this.erreur = erreur;
		}

	
	
	

}