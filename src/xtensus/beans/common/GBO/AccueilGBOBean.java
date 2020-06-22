package xtensus.beans.common.GBO;

import java.io.FileInputStream;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;

import javax.annotation.PostConstruct;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.itextpdf.text.log.SysoLogger;

import xtensus.beans.common.EmailUtil;
import xtensus.beans.common.LanguageManagerBean;
import xtensus.beans.common.VariableGlobale;
import xtensus.beans.common.sauvgardeRestauration.SQLtoDB;
import xtensus.beans.utils.CourrierConsulterInformations;
//import xtensus.beans.utils.StatistiqueConfidentialite;
//import xtensus.beans.utils.StatistiqueUrgence;
import xtensus.dao.utils.DMSConnexionImplement;
import xtensus.entity.Confidentialite;
import xtensus.entity.Courrier;
import xtensus.entity.Urgence;
import xtensus.entity.Variables;
import xtensus.ldap.model.Person;
import xtensus.ldap.model.Unit;
import xtensus.services.ApplicationManager;
import xtensus.services.Export;

@Component()
@Scope("request")
public class AccueilGBOBean {

	private ApplicationManager appMgr;

	public boolean statusAide;
	@Autowired
	private VariableGlobale vb;
	// @Autowired
	// private CourrierConsultationBean consultationBean;
	/***** Courrier ********/
	private int annee;
	private int courrierArrivee;
	private int courrierDepart;
	private String pourcentageCourrierArrivee;
	private String pourcentageCourrierDepart;
	private int courrierTotal;
	private int courrierNouveaux;
	private int courrierAValider;
	/***** Dossier *****/
	private int mesDossier;
	private int dossierArrivee;
	private int dossierDepart;
	private String pourcentageDossierArrivee;
	private String pourcentageDossierDepart;
	private int dossierTotal;
	/***** Necessitnat de reponse *****/
	private int necessitnatReponseTotal;
	private int necessitnatReponseDemain;
	private int necessitnatReponseSemaine;
	private int necessitnatReponseDateDepasse;
//	private List<StatistiqueConfidentialite> statistiqueConfidentialites;
//	private List<StatistiqueUrgence> statistiqueUrgences;
	private DataModel statistiqueConfidentialitesDM;
	private DataModel statistiqueUrgencesDM;
	private DeconnexionBean deconnexionBean;
	private int countConfdentialite;
	private int countUrgence;
	

	private String editorValue;

	@Autowired
	private LanguageManagerBean lm;
	@Autowired
	private MessageSource messageSource;

	private boolean showStatisticButton;
	private boolean hideStatisticButton;
	private boolean showMailErrorMessage;
	private boolean showFaxErrorMessage;
	private boolean showLoginErrorMessage;
	// NEW
	private String typeForStatRelance;
	private Integer idUser;
	private String type;
	private String type1;
	private String typeSecretaire;
	private Integer typeTransmission;
//	private Integer stateTraitement;
	private Date dateDebut;
	private Date dateFin;
	private String consultCourrierSecretaire;
	private String consultCourrierSubordonne;
	private String consultCourrierSousUnite;
	private List<Integer> listIdsSousUnit;
	private List<Integer> listIdsSubordonne;
	private boolean status;
	private boolean refreshedCourrier = true;
	private boolean refreshedDossier = true;
	private boolean refreshedRelance = true;
	private boolean refreshedQualification = true;
	private boolean refreshed = true;
	private List<Integer> listIdBocMembers;

	private List<Variables> variables;

	private String semaineCalendaire;

	private Integer confidentialiteTotal;
	private Integer urgenceTotal;

	private String mailErrorColor = "black";
	private String ldapErrorColor = "black";
	private String gedErrorColor = "black";
	private String dbErrorColor = "black";
	private boolean boc=false;
//	private boolean variableUserAgent;
	@Autowired
    private UserAgentProcessor userAgentProcessor;

	private String type2;

	@Autowired
	public AccueilGBOBean(
			@Qualifier("applicationManager") ApplicationManager appMgr) {
		this.setAppMgr(appMgr);
		statistiqueConfidentialitesDM = new ListDataModel();
		statistiqueUrgencesDM = new ListDataModel();
//		statistiqueConfidentialites = new ArrayList<StatistiqueConfidentialite>();
//		statistiqueUrgences = new ArrayList<StatistiqueUrgence>();
		listIdsSousUnit = new ArrayList<Integer>();
		listIdsSubordonne = new ArrayList<Integer>();
		deconnexionBean = new DeconnexionBean();
	}

	public AccueilGBOBean() {

	}

	// @PostConstruct
	// public void Initialize() {
	// System.out.println("Debut Acceuil");
	// courrierArrivee=0;
	// courrierDepart=0;
	// courrierTotal=0;
	// courrierNouveaux=0;
	// necessitnatReponseDemain=0;
	// necessitnatReponseSemaine=0;
	// necessitnatReponseDateDepasse=0;
	// courrierAValider=0;
	// courrierNouveaux=0;
	// dossierArrivee=0;
	// dossierDepart=0;
	// dossierTotal=0;
	// mesDossier=0;
	// pourcentageDossierArrivee = "0,0";
	// pourcentageCourrierDepart = "0,0";
	// pourcentageCourrierArrivee = "0,0";
	// pourcentageCourrierDepart = "0,0";
	// List<StatistiqueUrgence> listStatUrgence = new
	// ArrayList<StatistiqueUrgence>();
	// List<StatistiqueConfidentialite> listStatConfid = new
	// ArrayList<StatistiqueConfidentialite>();
	// statistiqueUrgencesDM.setWrappedData(listStatUrgence);
	// statistiqueConfidentialitesDM.setWrappedData(listStatConfid);
	// showMailErrorMessage = EmailUtil.checkConnection();
	// showFaxErrorMessage = FaxUtil.checkConnection();
	// System.out.println("FIN Acceuil");
	// }
	@PostConstruct
	public void Initialize() {
		try {
			System.out.println("userAgentProcessor.isPhone() : "+userAgentProcessor.isPhone());
//			if(userAgentProcessor.isPhone())
//			{ System.out.println("is phone ");
//				variableUserAgent=true;
//			}else{
//				variableUserAgent=false;
//			}
			showLoginErrorMessage = false;
			if (vb.getPerson() != null) {
				status = true;
				pourcentageDossierArrivee = "0,0";
				pourcentageCourrierDepart = "0,0";
				pourcentageCourrierArrivee = "0,0";
				pourcentageCourrierDepart = "0,0";
//				statistiqueUrgencesDM
//						.setWrappedData(new ArrayList<StatistiqueUrgence>());
//				statistiqueConfidentialitesDM
//						.setWrappedData(new ArrayList<StatistiqueConfidentialite>());
				
				
				if (!vb.getPerson().isBoc()) {
					System.out.println("IS NOT BOC");
					showStatisticButton = false;
					hideStatisticButton = true;
					vb.setAffichTempsReponse(false);
				} else {
					System.out.println("IS BOC");
					showStatisticButton = false;
					hideStatisticButton = true;
					vb.setAffichTempsReponse(true);

				}

				try {
					variables = appMgr.getList(Variables.class);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				for (Variables var : variables) {
					//optimisation MM
					switch(var.getVariablesId() ){
					case 3:
						consultCourrierSecretaire = var.getVaraiablesValeur(); 
						break;
					case 4:
						consultCourrierSubordonne = var.getVaraiablesValeur();
						break;
					case 5:
						consultCourrierSousUnite = var.getVaraiablesValeur();
						break;
					case 11:
						semaineCalendaire = var.getVaraiablesValeur();
						break;
					
					}
		
				}
				// Runnable runAcceuil = new Runnable() {
				//
				// @Override
				// public void run() {
				// // TODO Auto-generated method stub
				//
				//
				// }
				// };
				// new Thread(runAcceuil).start();
				// showFaxErrorMessage = FaxUtil.checkConnection();
				// **#################################

				// **

				// Runnable alfrescoThread = new Runnable() {
				//
				// @Override
				// public void run() {
				// try {
				// Resource rsrc = new ClassPathResource(
				// "/paramAlfresco.properties");
				// String pathConfigFile = rsrc.getFile()
				// .getAbsolutePath();
				// Properties props = new Properties();
				// props.load(new FileInputStream(pathConfigFile));
				// String URL = props.getProperty("alfresco.Url");
				// String namingConfigFilePath = vb
				// .getNamingConfigFilePath();
				// String login = props.getProperty("alfresco.login");
				// String mdp = props.getProperty("alfresco.password");
				// vb.setDmsAccessLayer(DMSConnexionImplement
				// .getConnexionGed(login, mdp, URL,
				// namingConfigFilePath));
				// } catch (Exception e) {
				// e.printStackTrace();
				// System.out.println("Error to load paramAlfresco");
				// }
				// }
				// };
				// new Thread(alfrescoThread).start();
				// showGedErrorMessage = DMSConnexionImplement.connetionStatus;
			} else {
				showLoginErrorMessage = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createFile() {
		Export export = new Export();
		try {
			export.exportToTXT("inserts");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void calculateStatisticsEmailCheck() {
		if (!showLoginErrorMessage) {
			checkLdapConnection();
			checkDbConnection();
			checkGedConnection();
			checkMailConnection();
		}
	}
	// time out mail
	//************MM***************
	//creation d'un thread pour le time out
	//***********debut MM******************
	//***********fin MM********************
	
	public void runTaskInLessThanGivenMs(int runtimeInMs, final Callable limitedRuntimeTask, final Callable timeBreachedTask) {
	    Thread thread = new Thread(new Runnable() {
	        @Override
	        public void run() {
	            try {
	                System.out.println("Started limitedRuntimeTask");
	                limitedRuntimeTask.call();
	                System.out.println("Finished limitedRuntimeTask in time");
	            } catch (Exception e) {
	            	e.printStackTrace();
	            }
	        }
	    });
	    thread.start();

	    long endTimeMillis = System.currentTimeMillis() + runtimeInMs;

	    while (thread.isAlive()) {
	        if (System.currentTimeMillis() > endTimeMillis) {
	            System.out.println("LmitedRuntimeTask did not finish in time (" + runtimeInMs + ")ms. It will run in vain.");
	            if(timeBreachedTask != null ){
	                try {
	                    System.out.println("Executing timeBreachedTask");
	                    timeBreachedTask.call();
	                    System.out.println("Finished timeBreachedTask");
	                } catch (Exception e) {
	                	e.printStackTrace();
	                }
	            }
	            return;
	        }
	        try {
	            Thread.sleep(10);
	        }
	        catch (InterruptedException t) {t.printStackTrace();}
	    }
	}
	public void checkMailConnection() {
		if (EmailUtil.checkConnection()) {
			mailErrorColor = "red";
			System.out.println("mail bad");
		} else {
			mailErrorColor = "#336600";
			System.out.println("mail ok");
		}
	}

	public void checkLdapConnection() {
		if (vb.getPerson() == null) {
			ldapErrorColor = "red";
			System.out.println("ldap bad");
		} else {
			ldapErrorColor = "#336600";
			System.out.println("ldap ok");
		}
	}

	public void checkGedConnection() {
		try {
			Resource rsrc = new ClassPathResource("/paramAlfresco.properties");
			String pathConfigFile = rsrc.getFile().getAbsolutePath();
			Properties props = new Properties();
			props.load(new FileInputStream(pathConfigFile));
			String URL = props.getProperty("alfresco.Url");
			String namingConfigFilePath = vb.getNamingConfigFilePath();
			String login = props.getProperty("alfresco.login");
			String mdp = props.getProperty("alfresco.password");
			vb.setDmsAccessLayer(DMSConnexionImplement.getConnexionGed(login, mdp, URL, namingConfigFilePath));
//			showGedErrorMessage = DMSConnexionImplement.connetionStatus;
			if (vb.getDmsAccessLayer() != null) {
				gedErrorColor = "#336600";
				System.out.println("ged ok");
			} else {
				gedErrorColor = "red";
				System.out.println("ged bad");
			}
		} catch (Exception e) {
			gedErrorColor = "red";
			System.out.println("ged bad");
			e.printStackTrace();
		}
	}

	public void checkDbConnection() {
		try {
			Connection con = SQLtoDB.getConnection();
			if (con == null || con.isClosed()) {
				dbErrorColor = "red";
				System.out.println("db bad");
			} else {
				dbErrorColor = "#336600";
				System.out.println("db ok");
			}
		} catch (Exception e) {
			dbErrorColor = "red";
			System.out.println("db bad");
			e.printStackTrace();
		}
	}
// debut stat Courrier
	public void calculateStatisticsCourrier() {
		System.out.println("2020-06-10 : calculateStatisticsCourrier ");
		long debut = System.currentTimeMillis();
		 
		//Traitements...
		 
		System.out.println("//Traitements...");
		if (!showLoginErrorMessage) {
			boolean isResponsable = vb.getPerson().isResponsable();
			
			if (vb.getPerson() != null) {
				Date date = new Date();
				SimpleDateFormat maDateLongue = new SimpleDateFormat("yyyy");
				annee = Integer.parseInt(maDateLongue.format(date));
	
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
				} else if (isResponsable) {
					type = "unit_"
							+ vb.getPerson().getAssociatedDirection().getIdUnit();
					type1 = "sub_" + idUser;
					// // NEW
					//
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
					// // FIn NEW
					try {
						typeSecretaire = "secretary_"
								+ vb.getPerson().getAssociatedDirection()
										.getSecretaryUnit().getId();
					} catch (NullPointerException e) {
						e.printStackTrace();
						consultCourrierSecretaire = "Non";
					}
	
				} else if (vb.getPerson().isSecretary()) {
					type = "secretary_" + idUser;
					type1 = "unit_"
							+ vb.getPerson().getAssociatedDirection().getIdUnit();
				} else if (vb.getPerson().isAgent()) {
					type = "agent_" + idUser;
					type1 = "";
				}
				// // fin identify connected user
				typeTransmission = 0;
//				stateTraitement = 0;
	
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
				HashMap<String, Object> filterHashMap = new HashMap<String, Object>();
				DecimalFormat df = new DecimalFormat("0.0");
	
				if (vb.getPerson().isBoc()) {
					
					courrierArrivee = appMgr.CountAllCourrierBOCByCriteriaStat(
							filterHashMap, 0, date, date, type, type1, listIdBocMembers, "",
							"", "A").intValue();
					courrierDepart = appMgr.CountAllCourrierBOCByCriteriaStat(
							filterHashMap, 0, date, date, type, type1, listIdBocMembers, "",
							"", "D").intValue();
					courrierTotal = courrierArrivee + courrierDepart;
//					courrierTotal = appMgr.CountAllCourrierBOCByCriteria(filterHashMap, 0, date, date, type, type1,
//							idUser, "", "", "T").intValue();
					courrierNouveaux = appMgr.CountAllCourrierBOCByCriteriaStat(
							filterHashMap, 1, this.dateDebut, this.dateFin, type,
							type1, listIdBocMembers, "", "", "A").intValue();
					courrierAValider = appMgr.CountAllCourrierBOCByCriteriaStat(
							filterHashMap, 0, date, date, type, type1, listIdBocMembers, "",
							"nonTraite", "A").intValue();
				} else {
					courrierArrivee = appMgr.CountAllCourrierEnvoyerANDRecuByCriteria(
							isResponsable, listIdsSousUnit, listIdsSubordonne,
							filterHashMap, consultCourrierSecretaire, consultCourrierSubordonne,
							consultCourrierSousUnite, 0, date, date, type, type1, typeSecretaire,
							idUser, typeTransmission, "", 6, true, "Recu").intValue();
					courrierDepart = appMgr.CountAllCourrierEnvoyerANDRecuByCriteria(
							isResponsable, listIdsSousUnit, listIdsSubordonne,
							filterHashMap, consultCourrierSecretaire, consultCourrierSubordonne,
							consultCourrierSousUnite, 0, date, date, type, type1, typeSecretaire,
							idUser, typeTransmission, "", 6, true, "Envoyes").intValue();
//					courrierTotal = appMgr.CountAllCourrierBOCByCriteria(filterHashMap, 2, dateDebut, dateFin, type,
//							type1, listIdBocMembers, "", "Tous", "T").intValue();
					courrierTotal = appMgr.CountAllCourrierEnvoyerANDRecuByCriteria(
							isResponsable, listIdsSousUnit, listIdsSubordonne,
							filterHashMap, consultCourrierSecretaire, consultCourrierSubordonne,
							consultCourrierSousUnite, 0, date, date, type, type1, typeSecretaire,
							idUser, typeTransmission, "", 6, true, "Tous").intValue();
//					courrierTotal = courrierArrivee + courrierDepart;
					courrierAValider = appMgr.CountAllCourrierEnvoyerANDRecuByCriteria(
							isResponsable, listIdsSousUnit, listIdsSubordonne,
							filterHashMap, consultCourrierSecretaire, consultCourrierSubordonne,
							consultCourrierSousUnite, 0, date, date, type, type1, typeSecretaire,
							idUser, typeTransmission, "Avalider", 6, true, "Recu").intValue();
					courrierNouveaux = appMgr.CountAllCourrierEnvoyerANDRecuByCriteria(
							isResponsable, listIdsSousUnit, listIdsSubordonne,
							filterHashMap, consultCourrierSecretaire, consultCourrierSubordonne,
							consultCourrierSousUnite, 3, this.dateDebut, this.dateFin, type, type1,
							typeSecretaire, idUser, typeTransmission, "", 6, true, "Recu").intValue();
				}
				if (courrierTotal == 0) {
					pourcentageCourrierArrivee = "0,0";
					pourcentageCourrierDepart = "0,0";
				} else {
					pourcentageCourrierArrivee = df.format((float) courrierArrivee
							* 100 / (float) courrierTotal);
					pourcentageCourrierDepart = df.format((float) courrierDepart
							* 100 / (float) courrierTotal);
				}
				refreshedCourrier = false;
			}
		}
		System.out.println("temps de reponse methode calculateStatisticsCourrier");
		System.out.println(System.currentTimeMillis()-debut);
	}
//Fin stat courrier
	public void calculateStatisticsDossier() {
		long debut = System.currentTimeMillis();
		 
		//Traitements...

		if (!showLoginErrorMessage) {
			boolean isResponsable = vb.getPerson().isResponsable();
			if (vb.getPerson() != null) {
				Date date = new Date();
				SimpleDateFormat maDateLongue = new SimpleDateFormat("yyyy");
				annee = Integer.parseInt(maDateLongue.format(date));
	
				// // identify connected user
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
					typeForStatRelance = "boc_"
							+ String.valueOf(vb.getPerson().getId());
				} else if (isResponsable) {
					type = "unit_"
							+ vb.getPerson().getAssociatedDirection().getIdUnit();
					type1 = "sub_" + idUser;
					typeForStatRelance = "sub_"
							+ String.valueOf(vb.getPerson().getId());
					// // NEW
					//
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
					// // FIn NEW
					try {
						typeSecretaire = "secretary_"
								+ vb.getPerson().getAssociatedDirection()
										.getSecretaryUnit().getId();
					} catch (NullPointerException e) {
						consultCourrierSecretaire = "Non";
					}
	
				} else if (vb.getPerson().isSecretary()) {
					type = "secretary_" + idUser;
					type1 = "unit_"
							+ vb.getPerson().getAssociatedDirection().getIdUnit();
					typeForStatRelance = "secretary_"
							+ String.valueOf(vb.getPerson().getId());
				} else if (vb.getPerson().isAgent()) {
					type = "agent_" + idUser;
					type1 = "";
					typeForStatRelance = "agent_"
							+ String.valueOf(vb.getPerson().getId());
				}
				// // fin identify connected user
	
				HashMap<String, Object> filterHashMap = new HashMap<String, Object>();
				DecimalFormat df = new DecimalFormat("0.0");
				//
				// // calcule statistique
				if (!vb.getPerson().isBoc()) {
					// // Dossiers
					dossierArrivee = appMgr.countAllDossierRecu(
							isResponsable, listIdsSousUnit,
							listIdsSubordonne, filterHashMap,
							consultCourrierSecretaire,
							consultCourrierSubordonne,
							consultCourrierSousUnite, 0, date, date, type,
							type1, typeSecretaire, idUser, "", 6, true).intValue();
					dossierDepart = appMgr.countAllDossierEnvoyer(
							isResponsable, listIdsSousUnit,
							listIdsSubordonne, filterHashMap,
							consultCourrierSecretaire,
							consultCourrierSubordonne,
							consultCourrierSousUnite, 0, date, date, type,
							type1, typeSecretaire, idUser, "", 6, true).intValue();
					dossierTotal = dossierArrivee + dossierDepart;
					mesDossier = appMgr.countDossierByIdProprietaire(idUser, 2)
							.intValue();
					// // calcul des pourcentages
					if (dossierTotal == 0) {
						pourcentageDossierArrivee = "0,0";
						pourcentageCourrierDepart = "0,0";
					} else {
						pourcentageDossierArrivee = df
								.format((float) dossierArrivee * 100
										/ (float) dossierTotal);
						pourcentageDossierDepart = df.format((float) dossierDepart
								* 100 / (float) dossierTotal);
					}
				}
			}
			refreshedDossier = false;
		}
		System.out.println("temps d'execution methode calculateStatisticsDossier"); 
		System.out.println(System.currentTimeMillis()-debut);
	}

	@SuppressWarnings("deprecation")
	public void calculateStatisticsRelance() {
		long debut = System.currentTimeMillis();
		 
		//Traitements...

		if (!showLoginErrorMessage) {
			boolean isResponsable = vb.getPerson().isResponsable();
			if (vb.getPerson() != null) {
				Date date = new Date();
				SimpleDateFormat maDateLongue = new SimpleDateFormat("yyyy");
				annee = Integer.parseInt(maDateLongue.format(date));
	
				// // identify connected user
				idUser = vb.getPerson().getId();
				if (vb.getPerson().isBoc()) {
					listIdBocMembers = new ArrayList<Integer>();
					List<Person> listBocMembers = vb.getPerson().getAssociatedBOC().getMembersBOC();
					for (Person person : listBocMembers) {
						listIdBocMembers.add(person.getId());
					}
//					typeForStatRelance = "boc_"
//							+ String.valueOf(vb.getPerson().getId());
					typeForStatRelance = "boc_"
						+ String.valueOf(vb.getPerson().getAssociatedBOC().getIdBOC());
				} else if (isResponsable) {
					typeForStatRelance = "sub_"
							+ String.valueOf(vb.getPerson().getId());
					if (vb.getPerson().getAssociatedDirection() != null) {
						 type2 = "unit_"
								+ String.valueOf(vb.getPerson()
										.getAssociatedDirection().getIdUnit());
					}
					// // NEW
					//
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
					// // FIn NEW
					try {
						typeSecretaire = "secretary_"
								+ vb.getPerson().getAssociatedDirection()
										.getSecretaryUnit().getId();
					} catch (NullPointerException e) {
						consultCourrierSecretaire = "Non";
					}
	
				} else if (vb.getPerson().isSecretary()) {
					typeForStatRelance = "secretary_"
							+ String.valueOf(vb.getPerson().getId());
				} else if (vb.getPerson().isAgent()) {
					typeForStatRelance = "agent_"
							+ String.valueOf(vb.getPerson().getId());
				}
				// // fin identify connected user
	
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
	
				//
				// for Statistique Relance
				Calendar calendarRelance = Calendar.getInstance();
	//			String semaineCalendaire = appMgr.listVariablesById(11).get(0).getVaraiablesValeur();
				// // Relance
				// // Demain
				Date dateDebut = calendarRelance.getTime();
				calendarRelance.add(Calendar.DATE, 1);
	
				Date dateFin = calendarRelance.getTime();
				dateDebut.setHours(23);
				dateFin.setHours(23);
				dateDebut.setMinutes(59);
				dateFin.setMinutes(59);
				dateDebut.setSeconds(59);
				dateFin.setSeconds(59);
				// // cette Semaine
				calendarRelance = Calendar.getInstance();
				Date dateJour = calendarRelance.getTime();
				if (semaineCalendaire.equals("1")) {// semaine calendaire
					calendarRelance.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
					calendarRelance.add(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 4);
				} else { // semaine non calendaire
					calendar.add(Calendar.DATE, 7);
				}
				Date dateFinSemaine = calendar.getTime();
				dateFinSemaine.setHours(00);
				dateJour.setHours(00);
				dateFinSemaine.setMinutes(00);
				dateJour.setMinutes(00);
				dateFinSemaine.setSeconds(00);
				dateJour.setSeconds(00);
				// // date depasser
				calendarRelance = Calendar.getInstance();
				Date dateCourant = calendarRelance.getTime();
				if (vb.getPerson().isBoc()) {
					// Relance
					// Demain
//					necessitnatReponseDemain = appMgr.countCourrierForRelanceBOC(dateDebut, dateFin).intValue();
					necessitnatReponseDemain = 0;
					//appMgr.countCourrierForRelanceBOC(typeForStatRelance,dateDebut, dateFin).intValue();
					// cette Semaine
//					necessitnatReponseSemaine = appMgr.countCourrierForRelanceBOC(dateJour, dateFinSemaine).intValue();
					necessitnatReponseSemaine = 0;
					//appMgr.countCourrierForRelanceBOC(typeForStatRelance,dateJour, dateFinSemaine).intValue();
					// date depasser
//					necessitnatReponseDateDepasse = appMgr.countCourrierForRelanceOutOfDateBOC(dateCourant).intValue();
					necessitnatReponseDateDepasse =0;
					//appMgr.countCourrierForRelanceOutOfDateBOC(typeForStatRelance,dateCourant).intValue();
				} else {
					// // Relance
					// // Demain
					
					necessitnatReponseDemain = appMgr.countCourrierForRelanceByType(typeForStatRelance, dateDebut, dateFin)
							.intValue();
					if(type2 !=null && type2.length()>0 )
						necessitnatReponseDemain+=appMgr.countCourrierForRelanceByType(type2, dateDebut, dateFin)
						.intValue();
					System.out.println(System.currentTimeMillis()-debut);
					// // cette Semaine
					necessitnatReponseSemaine = appMgr.countCourrierForRelanceByType(typeForStatRelance, dateJour,
							dateFinSemaine).intValue();
					if(type2 !=null && type2.length()>0 )
						necessitnatReponseSemaine += appMgr.countCourrierForRelanceByType(type2, dateJour,
								dateFinSemaine).intValue();
					System.out.println(System.currentTimeMillis()-debut);
					// // date depasser
					necessitnatReponseDateDepasse = appMgr.countCourrierForRelanceOutOfDateByType(typeForStatRelance,
							dateCourant).intValue();
					if(type2 !=null && type2.length()>0 )
						necessitnatReponseDateDepasse += appMgr.countCourrierForRelanceOutOfDateByType(type2,
								dateCourant).intValue();
					System.out.println(System.currentTimeMillis()-debut);
				}

//AH : 2020-06-17 : ça ne doit as être la somme
				necessitnatReponseTotal = necessitnatReponseDemain
						+ necessitnatReponseSemaine + necessitnatReponseDateDepasse;
			}
			refreshedRelance = false;
		}
		System.out.println("temps de reponse methode calculateStatisticsRelance"); 
		System.out.println(System.currentTimeMillis()-debut);
	}

	public void calculateStatisticsQualification() {
		System.out.println("AH :: calculateStatisticsQualification");
		long debut = System.currentTimeMillis();
//		statistiqueUrgencesDM.setWrappedData(new ArrayList<StatistiqueUrgence>());
//		statistiqueConfidentialitesDM.setWrappedData(new ArrayList<StatistiqueConfidentialite>()); 
		//Traitements...

		if (!showLoginErrorMessage) {
			boolean isResponsable = vb.getPerson().isResponsable();
			if (vb.getPerson() != null) {
				Date date = new Date();
				SimpleDateFormat maDateLongue = new SimpleDateFormat("yyyy");
				annee = Integer.parseInt(maDateLongue.format(date));
	
				// // identify connected user
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
					typeForStatRelance = "boc_"
							+ String.valueOf(vb.getPerson().getId());
				} else if (isResponsable) {
					type = "unit_"
							+ vb.getPerson().getAssociatedDirection().getIdUnit();
					type1 = "sub_" + idUser;
					// // NEW
					//
					for (Unit unit : vb.getPerson().getAssociatedDirection()
							.getListUnitsChildUnit()) {
						listIdsSousUnit.add(unit.getIdUnit());
						try {
							listIdsSubordonne
									.add(unit.getResponsibleUnit().getId());
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("#Sub-Unit without Responsible");
						}
					}
					// // FIn NEW
					try {
						typeSecretaire = "secretary_"
								+ vb.getPerson().getAssociatedDirection()
										.getSecretaryUnit().getId();
					} catch (NullPointerException e) {
						consultCourrierSecretaire = "Non";
					}
	
				} else if (vb.getPerson().isSecretary()) {
					type = "secretary_" + idUser;
					type1 = "unit_"
							+ vb.getPerson().getAssociatedDirection().getIdUnit();
					typeForStatRelance = "secretary_"
							+ String.valueOf(vb.getPerson().getId());
				} else if (vb.getPerson().isAgent()) {
					type = "agent_" + idUser;
					type1 = "";
					typeForStatRelance = "agent_"
							+ String.valueOf(vb.getPerson().getId());
				}
				
				
				List<Urgence> listUrgence = appMgr.listUrgenceByOrdre();
				
				List<Confidentialite> listConfidentialite = appMgr.ConfidentialiteByOrdre();
				
//				List<StatistiqueUrgence> listStatUrgence = new ArrayList<StatistiqueUrgence>();
//				List<StatistiqueConfidentialite> listStatConfid = new ArrayList<StatistiqueConfidentialite>();
				confidentialiteTotal = 0;
				urgenceTotal = 0;
				if (vb.getPerson().isBoc()) {
					List<Object[]> confidentialiteCounts = appMgr.countCourrierRecuBOCByConfidentialite(type, idUser, "A");
//					for (Confidentialite confidentialite : listConfidentialite) {
//						System.out.println("AH AH : "+confidentialite);
//						StatistiqueConfidentialite statistiqueConfidentialite = new StatistiqueConfidentialite();
//						statistiqueConfidentialite.setConfidentialite(confidentialite);
						
//						for (Object[] o : confidentialiteCounts) {
//							Integer count = ((Long) o[0]).intValue();
//							Integer confidentialiteId = (Integer) o[1];
//							System.out.println("##### AH 2 : "+o.toString());
////							if (confidentialiteId.equals(confidentialite.getConfidentialiteId())) {
////								statistiqueConfidentialite.setCount(count.intValue());}
////								
////								else{
////									statistiqueConfidentialite.setCount(0);
////								}
////								
////								confidentialiteTotal += statistiqueConfidentialite.getCount();
////							}
////						listStatConfid.add(statistiqueConfidentialite);
////						System.out.println("##### AH "+statistiqueConfidentialite);
//						}
					
					System.out.println(System.currentTimeMillis()-debut);
					List<Object[]> urgenceCounts = appMgr.countCourrierRecuBOCByUrgence(type, idUser, "A");
//					for (Urgence urgence : listUrgence) {
////						System.out.println("AH AH 2: "+urgence);
//						StatistiqueUrgence statistiqueUrgences = new StatistiqueUrgence();
//						statistiqueUrgences.setUrgence(urgence);
//						for (Object[] o : urgenceCounts) {
//							Integer count = ((Long) o[0]).intValue();
//							Integer urgenceId = (Integer) o[1];
//							
//							if (urgenceId.equals(urgence.getUrgenceId())) {
//								statistiqueUrgences.setCount(count.intValue());
//							}else{
//								statistiqueUrgences.setCount(0);
//							}
//							urgenceTotal += statistiqueUrgences.getCount();
//							
//						}
//						listStatUrgence.add(statistiqueUrgences);
//						
//					}
				} else {
					
					List<Object[]> confidentialiteCounts = appMgr.countCourrierRecuByConfidentialite(isResponsable,
							listIdsSousUnit, listIdsSubordonne, consultCourrierSecretaire, consultCourrierSubordonne,
							consultCourrierSousUnite, type, type1, typeSecretaire, idUser);
//					for (Confidentialite confidentialite : listConfidentialite) {
//						StatistiqueConfidentialite statistiqueConfidentialite = new StatistiqueConfidentialite();
//						statistiqueConfidentialite.setConfidentialite(confidentialite);
//						
//						for (Object[] o : confidentialiteCounts) {
//							Integer count = ((Long) o[0]).intValue();
//							Integer confidentialiteId = (Integer) o[1];
//							if (confidentialiteId.equals(confidentialite.getConfidentialiteId())) {
//								statistiqueConfidentialite.setCount(count.intValue());}
//								
//								else{
//									statistiqueConfidentialite.setCount(0);
//								}
//								
//								confidentialiteTotal += statistiqueConfidentialite.getCount();
//							}
//						listStatConfid.add(statistiqueConfidentialite);
//						}

					List<Object[]> urgenceCounts = appMgr.countCourrierRecuByUrgence(isResponsable,
							listIdsSousUnit, listIdsSubordonne, consultCourrierSecretaire, consultCourrierSubordonne,
							consultCourrierSousUnite, type, type1, typeSecretaire, idUser);
					System.out.println(System.currentTimeMillis()-debut);
//					for (Urgence urgence : listUrgence) {
//						StatistiqueUrgence statistiqueUrgences = new StatistiqueUrgence();
//						statistiqueUrgences.setUrgence(urgence);
//						
//						for (Object[] o : urgenceCounts) {
//							Integer count = ((Long) o[0]).intValue();
//							Integer urgenceId = (Integer) o[1];
//							if (urgenceId.equals(urgence.getUrgenceId())) {
//								statistiqueUrgences.setCount(count.intValue());
//							}else{
//								statistiqueUrgences.setCount(0);
//							}
//								
//								urgenceTotal += statistiqueUrgences.getCount();
//							
//						}
//						listStatUrgence.add(statistiqueUrgences);
//					}
				}
				System.out.println(System.currentTimeMillis()-debut);
				statistiqueUrgencesDM= new ListDataModel();
//				statistiqueUrgencesDM.setWrappedData(listStatUrgence);
				
				statistiqueConfidentialitesDM=new ListDataModel();
//				 statistiqueConfidentialitesDM.setWrappedData(listStatConfid);
				
			}
			refreshedQualification = false;
		}
		System.out.println("temps d'execution methode calculateStatisticsQualification");
		System.out.println(System.currentTimeMillis()-debut);
	}

	@SuppressWarnings("deprecation")
	public void calculateStatistics() {
		System.out.println("AH :: calculateStatistics");
		long debut = System.currentTimeMillis();
		 
		//Traitements...

		boolean isResponsable = vb.getPerson().isResponsable();
		if (vb.getPerson() != null) {
			Date date = new Date();
			SimpleDateFormat maDateLongue = new SimpleDateFormat("yyyy");
			annee = Integer.parseInt(maDateLongue.format(date));
			try {
				variables = appMgr.getList(Variables.class);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			String semaineCalendaire = "";
			for (Variables var : variables) {
				if (var.getVariablesId() == 3) {
					consultCourrierSecretaire = var.getVaraiablesValeur();
				}
				if (var.getVariablesId() == 4) {
					consultCourrierSubordonne = var.getVaraiablesValeur();
				}
				if (var.getVariablesId() == 5) {
					consultCourrierSousUnite = var.getVaraiablesValeur();
				}
				if (var.getVariablesId() == 11) {
					semaineCalendaire = var.getVaraiablesValeur();
				}
			}
//			consultCourrierSecretaire = appMgr.listVariablesById(3).get(0).getVaraiablesValeur();
//			consultCourrierSubordonne = appMgr.listVariablesById(4).get(0).getVaraiablesValeur();
//			consultCourrierSousUnite = appMgr.listVariablesById(5).get(0).getVaraiablesValeur();

			// // identify connected user
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
				typeForStatRelance = "boc_"
						+ String.valueOf(vb.getPerson().getId());
			} else if (isResponsable) {
				type = "unit_"
						+ vb.getPerson().getAssociatedDirection().getIdUnit();
				type1 = "sub_" + idUser;
				typeForStatRelance = "sub_"
						+ String.valueOf(vb.getPerson().getId());
				// // NEW
				//
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
				// // FIn NEW
				try {
					typeSecretaire = "secretary_"
							+ vb.getPerson().getAssociatedDirection()
									.getSecretaryUnit().getId();
				} catch (NullPointerException e) {
					consultCourrierSecretaire = "Non";
				}

			} else if (vb.getPerson().isSecretary()) {
				type = "secretary_" + idUser;
				type1 = "unit_"
						+ vb.getPerson().getAssociatedDirection().getIdUnit();
				typeForStatRelance = "secretary_"
						+ String.valueOf(vb.getPerson().getId());
			} else if (vb.getPerson().isAgent()) {
				type = "agent_" + idUser;
				type1 = "";
				typeForStatRelance = "agent_"
						+ String.valueOf(vb.getPerson().getId());
			}
			// // fin identify connected user
			typeTransmission = 0;
//			stateTraitement = 0;

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
			HashMap<String, Object> filterHashMap = new HashMap<String, Object>();
			DecimalFormat df = new DecimalFormat("0.0");
			//
			// // calcule statistique
			List<Urgence> listUrgence = appMgr.listUrgenceByOrdre();
			List<Confidentialite> listConfidentialite = appMgr
					.ConfidentialiteByOrdre();
//			List<StatistiqueUrgence> listStatUrgence = new ArrayList<StatistiqueUrgence>();
//			List<StatistiqueConfidentialite> listStatConfid = new ArrayList<StatistiqueConfidentialite>();
			// for Statistique Relance
			Calendar calendarRelance = Calendar.getInstance();
//			String semaineCalendaire = appMgr.listVariablesById(11).get(0).getVaraiablesValeur();
			// // Relance
			// // Demain
			Date dateDebut = calendarRelance.getTime();
			calendarRelance.add(Calendar.DATE, 1);

			Date dateFin = calendarRelance.getTime();
			dateDebut.setHours(23);
			dateFin.setHours(23);
			dateDebut.setMinutes(59);
			dateFin.setMinutes(59);
			dateDebut.setSeconds(59);
			dateFin.setSeconds(59);
			// // cette Semaine
			calendarRelance = Calendar.getInstance();
			Date dateJour = calendarRelance.getTime();
			if (semaineCalendaire.equals("1")) {// semaine calendaire
				calendarRelance.set(Calendar.DAY_OF_WEEK,
						calendar.getFirstDayOfWeek());
				calendarRelance.add(Calendar.DAY_OF_WEEK,
						calendar.getFirstDayOfWeek() + 4);
			} else { // semaine non calendaire
				calendar.add(Calendar.DATE, 7);
			}
			Date dateFinSemaine = calendar.getTime();
			dateFinSemaine.setHours(00);
			dateJour.setHours(00);
			dateFinSemaine.setMinutes(00);
			dateJour.setMinutes(00);
			dateFinSemaine.setSeconds(00);
			dateJour.setSeconds(00);
			// // date depasser
			calendarRelance = Calendar.getInstance();
			Date dateCourant = calendarRelance.getTime();
			if (vb.getPerson().isBoc()) {
				
				courrierArrivee = appMgr.CountAllCourrierBOCByCriteriaStat(
						filterHashMap, 0, date, date, type, type1, listIdBocMembers, "",
						"", "A").intValue();
				courrierDepart = appMgr.CountAllCourrierBOCByCriteriaStat(
						filterHashMap, 0, date, date, type, type1, listIdBocMembers, "",
						"", "D").intValue();
				courrierTotal = courrierArrivee + courrierDepart;
//				courrierTotal = appMgr.CountAllCourrierBOCByCriteria(
//						filterHashMap, 0, date, date, type, type1, idUser, "",
//						"", "T").intValue();
				courrierNouveaux = appMgr.CountAllCourrierBOCByCriteriaStat(
						filterHashMap, 1, this.dateDebut, this.dateFin, type,
						type1, listIdBocMembers, "", "", "A").intValue();
				System.out.println("debut "+this.dateDebut+" fin "+this.dateFin);
				courrierAValider = appMgr.CountAllCourrierBOCByCriteriaStat(
						filterHashMap, 0, date, date, type, type1, listIdBocMembers, "",
						"nonTraite", "A").intValue();

				List<Object[]> confidentialiteCounts = appMgr.countCourrierRecuBOCByConfidentialite(type, idUser, "A");
//				for (Confidentialite confidentialite : listConfidentialite) {
//					System.out.println("AH 1 : "+confidentialite);
//					for (Object[] o : confidentialiteCounts) {
//						Integer count = ((Long) o[0]).intValue();
//						Integer confidentialiteId = (Integer) o[1];
//						StatistiqueConfidentialite statistiqueConfidentialite = new StatistiqueConfidentialite();
//						statistiqueConfidentialite.setConfidentialite(confidentialite);
//						if (confidentialiteId.equals(confidentialite.getConfidentialiteId())) {
//							statistiqueConfidentialite.setCount(count.intValue());}
//							
//							else{
//								statistiqueConfidentialite.setCount(0);
//							}
//							listStatConfid.add(statistiqueConfidentialite);
//							confidentialiteTotal += count;
//						}
//					}
				List<Object[]> urgenceCounts = appMgr.countCourrierRecuBOCByUrgence(type, idUser, "A");
//				for (Urgence urgence : listUrgence) {
//					System.out.println("AH "+urgence);
//					for (Object[] o : urgenceCounts) {
//						Integer count = ((Long) o[0]).intValue();
//						Integer urgenceId = (Integer) o[1];
//						StatistiqueUrgence statistiqueUrgences = new StatistiqueUrgence();
//						statistiqueUrgences.setUrgence(urgence);
//						if (urgenceId.equals(urgence.getUrgenceId())) {
//							statistiqueUrgences.setCount(count.intValue());
//						}else{
//							statistiqueUrgences.setCount(0);
//						}
//							listStatUrgence.add(statistiqueUrgences);
//							urgenceTotal += count;
//						
//					}
//				}
				// Relance
				// Demain

//				necessitnatReponseDemain = appMgr.countCourrierForRelanceBOC(
//						dateDebut, dateFin).intValue();
				necessitnatReponseDemain =0;
				//appMgr.countCourrierForRelanceBOC(type,dateDebut, dateFin).intValue();
				// cette Semaine
//				necessitnatReponseSemaine = appMgr.countCourrierForRelanceBOC(
//						dateJour, dateFinSemaine).intValue();
				necessitnatReponseSemaine =0;
				//appMgr.countCourrierForRelanceBOC(type,dateJour, dateFinSemaine).intValue();
				// date depasser
//				necessitnatReponseDateDepasse = appMgr
//						.countCourrierForRelanceOutOfDateBOC(dateCourant)
//						.intValue();
				necessitnatReponseDateDepasse = 0;
				//appMgr.countCourrierForRelanceOutOfDateBOC(type,dateCourant).intValue();

			} else {
				
				courrierArrivee = appMgr.CountAllCourrierEnvoyerANDRecuByCriteria(
						isResponsable, listIdsSousUnit, listIdsSubordonne,
						filterHashMap, consultCourrierSecretaire, consultCourrierSubordonne,
						consultCourrierSousUnite, 0, date, date, type, type1, typeSecretaire,
						idUser, typeTransmission, "", 6, true, "Recu").intValue();
				courrierDepart = appMgr.CountAllCourrierEnvoyerANDRecuByCriteria(
						isResponsable, listIdsSousUnit, listIdsSubordonne,
						filterHashMap, consultCourrierSecretaire, consultCourrierSubordonne,
						consultCourrierSousUnite, 0, date, date, type, type1, typeSecretaire,
						idUser, typeTransmission, "", 6, true, "Envoyes").intValue();
//				courrierTotal = appMgr.CountAllCourrierBOCByCriteria(filterHashMap, 2, dateDebut, dateFin, type, type1,
//						listIdBocMembers, "", "Tous", "T").intValue();
				courrierTotal = appMgr.CountAllCourrierEnvoyerANDRecuByCriteria(
						isResponsable, listIdsSousUnit, listIdsSubordonne,
						filterHashMap, consultCourrierSecretaire, consultCourrierSubordonne,
						consultCourrierSousUnite, 0, date, date, type, type1, typeSecretaire,
						idUser, typeTransmission, "", 6, true, "Tous").intValue();
//				courrierTotal = courrierArrivee + courrierDepart;
				courrierAValider = appMgr.CountAllCourrierEnvoyerANDRecuByCriteria(
						isResponsable, listIdsSousUnit, listIdsSubordonne,
						filterHashMap, consultCourrierSecretaire, consultCourrierSubordonne,
						consultCourrierSousUnite, 0, date, date, type, type1, typeSecretaire,
						idUser, typeTransmission, "Avalider", 6, true, "Recu").intValue();
				courrierNouveaux = appMgr.CountAllCourrierEnvoyerANDRecuByCriteria(
						isResponsable, listIdsSousUnit, listIdsSubordonne,
						filterHashMap, consultCourrierSecretaire, consultCourrierSubordonne,
						consultCourrierSousUnite, 1, this.dateDebut, this.dateFin, type, type1,
						typeSecretaire, idUser, typeTransmission, "", 6, true, "Recu").intValue();
				// // Dossiers
				//
				dossierArrivee = appMgr.countAllDossierRecu(
						isResponsable, listIdsSousUnit,
						listIdsSubordonne, filterHashMap,
						consultCourrierSecretaire,
						consultCourrierSubordonne,
						consultCourrierSousUnite, 0, date, date, type,
						type1, typeSecretaire, idUser, "", 6, true).intValue();
				dossierDepart = appMgr.countAllDossierEnvoyer(
						isResponsable, listIdsSousUnit,
						listIdsSubordonne, filterHashMap,
						consultCourrierSecretaire,
						consultCourrierSubordonne,
						consultCourrierSousUnite, 0, date, date, type,
						type1, typeSecretaire, idUser, "", 6, true).intValue();
				dossierTotal = dossierArrivee + dossierDepart;
				mesDossier = appMgr.countDossierByIdProprietaire(idUser, 2)
						.intValue();
				// // calcul des pourcentages
				if (dossierTotal == 0) {
					pourcentageDossierArrivee = "0,0";
					pourcentageCourrierDepart = "0,0";
				} else {
					pourcentageDossierArrivee = df
							.format((float) dossierArrivee * 100
									/ (float) dossierTotal);
					pourcentageDossierDepart = df.format((float) dossierDepart
							* 100 / (float) dossierTotal);
				}
				List<Object[]> confidentialiteCounts = appMgr.countCourrierRecuByConfidentialite(isResponsable,
						listIdsSousUnit, listIdsSubordonne, consultCourrierSecretaire, consultCourrierSubordonne,
						consultCourrierSousUnite, type, type1, typeSecretaire, idUser);
//				for (Confidentialite confidentialite : listConfidentialite) {
//					for (Object[] o : confidentialiteCounts) {
//						Integer count = ((Long) o[0]).intValue();
//						Integer confidentialiteId = (Integer) o[1];
//						StatistiqueConfidentialite statistiqueConfidentialite = new StatistiqueConfidentialite();
//						statistiqueConfidentialite.setConfidentialite(confidentialite);
//						if (confidentialiteId.equals(confidentialite.getConfidentialiteId())) {
//							statistiqueConfidentialite.setCount(count.intValue());}
//							
//							else{
//								statistiqueConfidentialite.setCount(0);
//							}
//							listStatConfid.add(statistiqueConfidentialite);
//							confidentialiteTotal += count;
//						}
//					}
				List<Object[]> urgenceCounts = appMgr.countCourrierRecuByUrgence(isResponsable,
						listIdsSousUnit, listIdsSubordonne, consultCourrierSecretaire, consultCourrierSubordonne,
						consultCourrierSousUnite, type, type1, typeSecretaire, idUser);
//				for (Urgence urgence : listUrgence) {
//					System.out.println("AH "+urgence);
//					for (Object[] o : urgenceCounts) {
//						Integer count = ((Long) o[0]).intValue();
//						Integer urgenceId = (Integer) o[1];
//						StatistiqueUrgence statistiqueUrgences = new StatistiqueUrgence();
//						statistiqueUrgences.setUrgence(urgence);
//						if (urgenceId.equals(urgence.getUrgenceId())) {
//							statistiqueUrgences.setCount(count.intValue());
//						} else {
//							statistiqueUrgences.setCount(0);
//						}
//						listStatUrgence.add(statistiqueUrgences);
//						urgenceTotal += count;
//
//					}
//				}

				//
				// // Relance
				// // Demain
				necessitnatReponseDemain = appMgr
						.countCourrierForRelanceByType(typeForStatRelance,
								dateDebut, dateFin).intValue();
				// // cette Semaine
				necessitnatReponseSemaine = appMgr
						.countCourrierForRelanceByType(typeForStatRelance,
								dateJour, dateFinSemaine).intValue();
				// // date depasser
				necessitnatReponseDateDepasse = appMgr
						.countCourrierForRelanceOutOfDateByType(
								typeForStatRelance, dateCourant).intValue();
			}
			necessitnatReponseTotal = necessitnatReponseDemain
					+ necessitnatReponseSemaine + necessitnatReponseDateDepasse;
			if (courrierTotal == 0) {
				pourcentageCourrierArrivee = "0,0";
				pourcentageCourrierDepart = "0,0";
			} else {
				pourcentageCourrierArrivee = df.format((float) courrierArrivee
						* 100 / (float) courrierTotal);
				pourcentageCourrierDepart = df.format((float) courrierDepart
						* 100 / (float) courrierTotal);
			}
			statistiqueUrgencesDM= new ListDataModel();
//			statistiqueUrgencesDM.setWrappedData(listStatUrgence);
			
			statistiqueConfidentialitesDM=new ListDataModel();
//			statistiqueConfidentialitesDM.setWrappedData(listStatConfid);
			showMailErrorMessage = EmailUtil.checkConnection();
			refreshed = false;
		}
		refreshed = false;
		 System.out.println("temps d'execution methode calculateStatistics");
		System.out.println(System.currentTimeMillis()-debut);
	}

	// *************************ANCIEN**************************************
	// CourrierConsultationBean
	// if (vb.getPerson().isBoc()) {
	// int courrierArriveeJour = consultationBean
	// .getListCourriersTousBocArriveJour().size();
	// int courrierDepartJour = consultationBean
	// .getListCourriersTousBocDepartJour().size();
	// courrierArrivee = consultationBean
	// .getListCourriersTousBocArrive().size()
	// + courrierArriveeJour;
	// courrierDepart = consultationBean
	// .getListCourriersTousBocDepart().size()
	// + courrierDepartJour;
	//
	// courrierNouveaux = courrierArriveeJour;
	// courrierTotal = courrierArrivee + courrierDepart;
	// } else {
	// int courrierArriveeJour = consultationBean
	// .getListCourriersRecusJour().size();
	// int courrierDepartJour = consultationBean
	// .getListCourriersEnvoyesJour().size();
	// courrierArrivee = consultationBean.getListCourriersRecus()
	// .size() + courrierArriveeJour;
	// courrierDepart = consultationBean.getListCourriersEnvoyes()
	// .size() + courrierDepartJour;
	//
	// courrierNouveaux = consultationBean.getNouveauCourrier();
	// courrierTotal = courrierArrivee + courrierDepart;
	// }
	// if (courrierTotal != 0) {
	// pourcentageCourrierArrivee = (courrierArrivee * 100)
	// / courrierTotal;
	// pourcentageCourrierDepart = (courrierDepart * 100)
	// / courrierTotal;
	// } else {
	// pourcentageCourrierArrivee = 0;
	// pourcentageCourrierDepart = 0;
	// }

	// if (vb.getPerson().isBoc()) {
	// courrierAValider = consultationBean
	// .getListCourriersTousBocNonTraite().size()
	// + consultationBean
	// .getListCourriersTousBocNonTraiteJour().size();
	// } else {
	// courrierAValider = consultationBean
	// .getListCourriersRecusAvalider().size()
	// + consultationBean.getListCourriersRecusJourAvalider()
	// .size();
	// }
	/***** Dossier ********/
	// mesDossier = appMgr.listDossierByIdProprietaire(refUserConnecte)
	// .size();
	// int dossierArriveeJour = consultationBean
	// .getListDossiersRecusJour().size();
	// int dossierDepartJour = consultationBean
	// .getListDossiersEnvoyesJour().size();
	// dossierArrivee = consultationBean.getListDossiersRecus().size()
	// + dossierArriveeJour;
	// dossierDepart = consultationBean.getListDossiersEnvoyes().size()
	// + dossierDepartJour;
	// dossierTotal = dossierArrivee + dossierDepart;
	// if (dossierTotal != 0) {
	// pourcentageDossierArrivee = (dossierArrivee * 100)
	// / dossierTotal;
	// pourcentageDossierDepart = (dossierDepart * 100) / dossierTotal;
	// } else {
	// pourcentageDossierArrivee = 0;
	// pourcentageDossierDepart = 0;
	// }
	// /*************** Necessitnat de reponse ************/
	// necessitnatReponseTotal = 0;
	// necessitnatReponseAujourdhui = 0;
	// necessitnatReponseSemaine = 0;
	// necessitnatReponseMois = 0;
	//
	// statiqtiqueCourrierNecessitantReponce(consultationBean
	// .getListCourriersRecus());
	// statiqtiqueCourrierNecessitantReponce(consultationBean
	// .getListCourriersRecusJour());
	// Deja comment�
	/*
	 * statiqtiqueCourrierNecessitantReponce(consultationBean
	 * .getListCourriersEnvoyes());
	 * statiqtiqueCourrierNecessitantReponce(consultationBean
	 * .getListCourriersEnvoyesJour());
	 */
	//
	// /*************** Urgence ************/
	// List<Urgence> urgences = new ArrayList<Urgence>();
	// urgences = appMgr.listUrgenceByOrdre();
	// for (Urgence u : urgences) {
	// int refU = u.getUrgenceId();
	// countUrgence = statiqtiqueUrgence(
	// consultationBean.getListCourriersRecus(), refU)
	// + statiqtiqueUrgence(
	// consultationBean.getListCourriersRecusJour(),
	// refU);
	// StatistiqueUrgence su = new StatistiqueUrgence(countUrgence, u);
	// statistiqueUrgences.add(su);
	// }
	// /*************** Confidentialite ************/
	// List<Confidentialite> confidentialites = new
	// ArrayList<Confidentialite>();
	// confidentialites = appMgr.ConfidentialiteByOrdre();
	// for (Confidentialite c : confidentialites) {
	// int refC = c.getConfidentialiteId();
	// countConfdentialite = statiqtiqueConfidentialite(
	// consultationBean.getListCourriersRecus(), refC)
	// + statiqtiqueConfidentialite(
	// consultationBean.getListCourriersRecusJour(),
	// refC);
	// StatistiqueConfidentialite sc = new StatistiqueConfidentialite(
	// countConfdentialite, c);
	// statistiqueConfidentialites.add(sc);
	// }
	// statistiqueUrgencesDM.setWrappedData(statistiqueUrgences);
	// statistiqueConfidentialitesDM
	// .setWrappedData(statistiqueConfidentialites);
	// }

	public int statiqtiqueUrgence(List<CourrierConsulterInformations> list,
			int idUrgence) {
		int res = 0;
		for (CourrierConsulterInformations cci : list) {
			Courrier cr = new Courrier();
			cr = cci.getCourrier();
			int ref = cr.getUrgence().getUrgenceId();
			if (ref == idUrgence) {
				res++;
			}
		}
		return res;
	}

	public int statiqtiqueConfidentialite(
			List<CourrierConsulterInformations> list, int idConfidentialite) {
		int res = 0;
		for (CourrierConsulterInformations cci : list) {
			Courrier cr = new Courrier();
			cr = cci.getCourrier();
			int ref = cr.getConfidentialite().getConfidentialiteId();
			if (ref == idConfidentialite) {
				res++;
			}
		}
		return res;
	}

	// public void statiqtiqueCourrierNecessitantReponce(
	// List<CourrierConsulterInformations> list) {
	// necessitnatReponseTotal = 0;
	// necessitnatReponseAujourdhui = 0;
	// necessitnatReponseSemaine = 0;
	// necessitnatReponseMois = 0;
	// Date d = new Date();
	// for (CourrierConsulterInformations cci : list) {
	// Courrier cr = new Courrier();
	// cr = cci.getCourrier();
	// if (cr.getCourrierNecessiteReponse().equals("Oui")) {
	// necessitnatReponseTotal++;
	// if (d.equals(cr.getCourrierDateReponse())) {
	// necessitnatReponseAujourdhui++;
	// }
	// Calendar c1 = Calendar.getInstance();
	// c1.setTime(d);
	// int NumeroSemaineActuelle = c1.get(c1.WEEK_OF_YEAR);
	// Calendar c2 = Calendar.getInstance();
	// c2.setTime(cr.getCourrierDateReponse());
	// int NumeroSemaine = c2.get(c2.WEEK_OF_YEAR);
	// if (NumeroSemaineActuelle == NumeroSemaine) {
	// necessitnatReponseSemaine++;
	// }
	// int mois = cr.getCourrierDateReponse().getMonth() + 1;
	// int moisActuelle = d.getMonth() + 1;
	// if (mois == moisActuelle) {
	// necessitnatReponseMois++;
	// }
	// }
	// }
	// }

	/*********** Getters && Setters *****************/
	public boolean isStatusAide() {
		return statusAide;
	}

	public void setStatusAide(boolean statusAide) {
		this.statusAide = statusAide;
	}

	public void setVb(VariableGlobale vb) {
		this.vb = vb;
	}

	public VariableGlobale getVb() {
		return vb;
	}

	public void setAppMgr(ApplicationManager appMgr) {
		this.appMgr = appMgr;
	}

	public ApplicationManager getAppMgr() {
		return appMgr;
	}

	// public CourrierConsultationBean getConsultationBean() {
	// return consultationBean;
	// }
	//
	// public void setConsultationBean(CourrierConsultationBean
	// consultationBean) {
	// this.consultationBean = consultationBean;
	// }

	public int getAnnee() {
		return annee;
	}

	public void setAnnee(int annee) {
		this.annee = annee;
	}

	public int getCourrierArrivee() {
		return courrierArrivee;
	}

	public void setCourrierArrivee(int courrierArrivee) {
		this.courrierArrivee = courrierArrivee;
	}

	public int getCourrierDepart() {
		return courrierDepart;
	}

	public void setCourrierDepart(int courrierDepart) {
		this.courrierDepart = courrierDepart;
	}

	public String getPourcentageCourrierArrivee() {
		return pourcentageCourrierArrivee;
	}

	public void setPourcentageCourrierArrivee(String pourcentageCourrierArrivee) {
		this.pourcentageCourrierArrivee = pourcentageCourrierArrivee;
	}

	public String getPourcentageCourrierDepart() {
		return pourcentageCourrierDepart;
	}

	public void setPourcentageCourrierDepart(String pourcentageCourrierDepart) {
		this.pourcentageCourrierDepart = pourcentageCourrierDepart;
	}

	public int getCourrierTotal() {
		return courrierTotal;
	}

	public void setCourrierTotal(int courrierTotal) {
		this.courrierTotal = courrierTotal;
	}

	public int getCourrierNouveaux() {
		return courrierNouveaux;
	}

	public void setCourrierNouveaux(int courrierNouveaux) {
		this.courrierNouveaux = courrierNouveaux;
	}

	public int getMesDossier() {
		return mesDossier;
	}

	public void setMesDossier(int mesDossier) {
		this.mesDossier = mesDossier;
	}

	public int getDossierArrivee() {
		return dossierArrivee;
	}

	public void setDossierArrivee(int dossierArrivee) {
		this.dossierArrivee = dossierArrivee;
	}

	public int getDossierDepart() {
		return dossierDepart;
	}

	public void setDossierDepart(int dossierDepart) {
		this.dossierDepart = dossierDepart;
	}

	public int getDossierTotal() {
		return dossierTotal;
	}

	public void setDossierTotal(int dossierTotal) {
		this.dossierTotal = dossierTotal;
	}

	public void setCourrierAValider(int courrierAValider) {
		this.courrierAValider = courrierAValider;
	}

	public int getCourrierAValider() {
		return courrierAValider;
	}

	public int getNecessitnatReponseTotal() {
		return necessitnatReponseTotal;
	}

	public void setNecessitnatReponseTotal(int necessitnatReponseTotal) {
		this.necessitnatReponseTotal = necessitnatReponseTotal;
	}

	public int getNecessitnatReponseSemaine() {
		return necessitnatReponseSemaine;
	}

	public void setNecessitnatReponseSemaine(int necessitnatReponseSemaine) {
		this.necessitnatReponseSemaine = necessitnatReponseSemaine;
	}

	public int getNecessitnatReponseDemain() {
		return necessitnatReponseDemain;
	}

	public void setNecessitnatReponseDemain(int necessitnatReponseDemain) {
		this.necessitnatReponseDemain = necessitnatReponseDemain;
	}

	public int getNecessitnatReponseDateDepasse() {
		return necessitnatReponseDateDepasse;
	}

	public void setNecessitnatReponseDateDepasse(
			int necessitnatReponseDateDepasse) {
		this.necessitnatReponseDateDepasse = necessitnatReponseDateDepasse;
	}

//	public void setStatistiqueConfidentialites(
//			List<StatistiqueConfidentialite> statistiqueConfidentialites) {
//		this.statistiqueConfidentialites = statistiqueConfidentialites;
//	}
//
//	public List<StatistiqueConfidentialite> getStatistiqueConfidentialites() {
//		return statistiqueConfidentialites;
//	}
//
//	public void setStatistiqueUrgences(
//			List<StatistiqueUrgence> statistiqueUrgences) {
//		this.statistiqueUrgences = statistiqueUrgences;
//	}
//
//	public List<StatistiqueUrgence> getStatistiqueUrgences() {
//		return statistiqueUrgences;
//	}

	public void setStatistiqueConfidentialitesDM(
			DataModel statistiqueConfidentialitesDM) {
		this.statistiqueConfidentialitesDM = statistiqueConfidentialitesDM;
	}

	public DataModel getStatistiqueConfidentialitesDM() {
		return statistiqueConfidentialitesDM;
	}

	public void setStatistiqueUrgencesDM(DataModel statistiqueUrgencesDM) {
		this.statistiqueUrgencesDM = statistiqueUrgencesDM;
	}

	public DataModel getStatistiqueUrgencesDM() {
		return statistiqueUrgencesDM;
	}

	public void setCountConfdentialite(int countConfdentialite) {
		this.countConfdentialite = countConfdentialite;
	}

	public int getCountConfdentialite() {
		return countConfdentialite;
	}

	public int getCountUrgence() {
		return countUrgence;
	}

	public void setCountUrgence(int countUrgence) {
		this.countUrgence = countUrgence;
	}

	public void setEditorValue(String editorValue) {
		this.editorValue = editorValue;
	}

	public String getEditorValue() {
		return editorValue;
	}

	public void setShowStatisticButton(boolean showStatisticButton) {
		this.showStatisticButton = showStatisticButton;
	}

	public boolean isShowStatisticButton() {
		return showStatisticButton;
	}

	public void setHideStatisticButton(boolean hideStatisticButton) {
		this.hideStatisticButton = hideStatisticButton;
	}

	public boolean isHideStatisticButton() {
		return hideStatisticButton;
	}

	public void setLm(LanguageManagerBean lm) {
		this.lm = lm;
	}

	public LanguageManagerBean getLm() {
		return lm;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setShowMailErrorMessage(boolean showMailErrorMessage) {
		this.showMailErrorMessage = showMailErrorMessage;
	}

	public boolean isShowMailErrorMessage() {
		return showMailErrorMessage;
	}

	public void setShowFaxErrorMessage(boolean showFaxErrorMessage) {
		this.showFaxErrorMessage = showFaxErrorMessage;
	}

	public boolean isShowFaxErrorMessage() {
		return showFaxErrorMessage;
	}

	public String getPourcentageDossierArrivee() {
		return pourcentageDossierArrivee;
	}

	public void setPourcentageDossierArrivee(String pourcentageDossierArrivee) {
		this.pourcentageDossierArrivee = pourcentageDossierArrivee;
	}

	public String getPourcentageDossierDepart() {
		return pourcentageDossierDepart;
	}

	public void setPourcentageDossierDepart(String pourcentageDossierDepart) {
		this.pourcentageDossierDepart = pourcentageDossierDepart;
	}

	

	public boolean isRefreshed() {
		return refreshed;
	}

	public void setRefreshed(boolean refreshed) {
		this.refreshed = refreshed;
	}

	public void setDeconnexionBean(DeconnexionBean deconnexionBean) {
		this.deconnexionBean = deconnexionBean;
	}

	public DeconnexionBean getDeconnexionBean() {
		return deconnexionBean;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean getStatus() {
		return status;
	}

	public boolean isShowLoginErrorMessage() {
		return showLoginErrorMessage;
	}

	public void setShowLoginErrorMessage(boolean showLoginErrorMessage) {
		this.showLoginErrorMessage = showLoginErrorMessage;
	}

	public boolean isRefreshedCourrier() {
		return refreshedCourrier;
	}

	public void setRefreshedCourrier(boolean refreshedCourrier) {
		this.refreshedCourrier = refreshedCourrier;
	}

	public boolean isRefreshedDossier() {
		return refreshedDossier;
	}

	public void setRefreshedDossier(boolean refreshedDossier) {
		this.refreshedDossier = refreshedDossier;
	}

	public boolean isRefreshedRelance() {
		return refreshedRelance;
	}

	public void setRefreshedRelance(boolean refreshedRelance) {
		this.refreshedRelance = refreshedRelance;
	}

	public boolean isRefreshedQualification() {
		return refreshedQualification;
	}

	public void setRefreshedQualification(boolean refreshedQualification) {
		this.refreshedQualification = refreshedQualification;
	}

	public Integer getConfidentialiteTotal() {
		return confidentialiteTotal;
	}

	public void setConfidentialiteTotal(Integer confidentialiteTotal) {
		this.confidentialiteTotal = confidentialiteTotal;
	}

	public Integer getUrgenceTotal() {
		return urgenceTotal;
	}

	public void setUrgenceTotal(Integer urgenceTotal) {
		this.urgenceTotal = urgenceTotal;
	}

	public String getMailErrorColor() {
		return mailErrorColor;
	}

	public void setMailErrorColor(String mailErrorColor) {
		this.mailErrorColor = mailErrorColor;
	}

	public String getLdapErrorColor() {
		return ldapErrorColor;
	}

	public void setLdapErrorColor(String loginErrorColor) {
		this.ldapErrorColor = loginErrorColor;
	}

	public String getGedErrorColor() {
		return gedErrorColor;
	}

	public void setGedErrorColor(String gedErrorColor) {
		this.gedErrorColor = gedErrorColor;
	}

	public String getDbErrorColor() {
		return dbErrorColor;
	}

	public void setDbErrorColor(String dbErrorColor) {
		this.dbErrorColor = dbErrorColor;
	}

	public void setBoc(boolean boc) {
		this.boc = boc;
	}

	public boolean isBoc() {
		return boc;
	}


	public void setUserAgentProcessor(UserAgentProcessor userAgentProcessor) {
		this.userAgentProcessor = userAgentProcessor;
	}

	public UserAgentProcessor getUserAgentProcessor() {
		return userAgentProcessor;
	}
}