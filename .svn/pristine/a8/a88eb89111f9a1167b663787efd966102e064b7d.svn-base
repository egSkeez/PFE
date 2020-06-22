package xtensus.aop;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import xtensus.beans.common.EmailUtil;
import xtensus.beans.common.VariableGlobaleNotification;
import xtensus.beans.utils.Informations;
import xtensus.beans.utils.NotificationListAddress;
import xtensus.entity.Expdestexterne;
import xtensus.entity.Pm;
import xtensus.entity.Pp;
import xtensus.gnl.entity.Evenement;
import xtensus.gnl.entity.Message;
import xtensus.gnl.entity.Notification;
import xtensus.gnl.entity.NotificationVariable;
import xtensus.gnl.entity.Templatelog;
import xtensus.gnl.entity.Templatenotification;
import xtensus.gnl.entity.UserNotification;
import xtensus.gnl.entity.VariablesLog;
import xtensus.gnl.entity.VariablesNotification;
import xtensus.gnl.entity.Xtelog;
import xtensus.ldap.business.LdapOperation;
import xtensus.ldap.model.Person;
import xtensus.ldap.model.Unit;
import xtensus.services.ApplicationManager;

@Aspect
public class TracingAfterReturningAdvice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4677524881438026891L;
	private ApplicationManager appMgr;
	private static Logger logger;

	private VariablesNotification variableNotification;
	private List<NotificationVariable> listVariableNotificationRef;
	private List<VariablesNotification> listVariableNotification;
	private List<Evenement> listEvenementNotification, listEvenementLog;
	private Evenement evenementNotification, evenementLog;
	private List<Notification> listNotificationAdmin;
	private List<Notification> listNotificationDestinataire;
	private List<Xtelog> listLogs;
	private NotificationListAddress notificationNomDestinataire;
	private List<NotificationListAddress> listMailNomDestinataire;
	private Notification notificationAdmin;
	private Notification notificationDestinataire;
	private Xtelog log;
	private Message messageExp, messageDest;
	private List<Templatenotification> listTemplateNotificationAdmin;
	private List<Templatenotification> listTemplateNotificationDestinataire;
	private List<Templatelog> listTemplateLog;
	private Templatenotification templateNotificationAdmin;
	private Templatenotification templateNotificationDestinataire;
	private Templatelog templateLog;
	private String evenementNomVariableNotif;
	private String evenementNomVariableLog;
	private String templateNotificationTexteAdmin;
	private String templateNotificationTexteDestinataire;
	private String templateLogTexte;
	private Object typeObject;
	private String info;
	private String userLog;
	private String dateLog;
	private String typeLog;
	private String notificationNomVariablAdmin;
	private String notificationNomVariableDestinataire;
	private String notificationNomVariableAdminPrincipal;
	private UserNotification userNotification;
	private List<UserNotification> listUserNotification;
	// private boolean etatNotificationAdmin
	private LdapOperation ldapOperation;
	private boolean etatNotificationDestinataire;
	private boolean etatNotificationAdmin;
	private boolean etatNotificationAdminGeneral;
	private String nomExpediteur;
	private String mailExpediteur;

	private EmailUtil emailUtil;
	@Autowired
	private VariableGlobaleNotification vb;

	public TracingAfterReturningAdvice() {
	}

	@Autowired
	public TracingAfterReturningAdvice(
			@Qualifier("applicationManager") ApplicationManager appMgr) {
		this.appMgr = appMgr;
		listEvenementNotification = new ArrayList<Evenement>();
		listEvenementLog = new ArrayList<Evenement>();

		setEvenementNotification(new Evenement());
		setEvenementLog(new Evenement());

		listNotificationAdmin = new ArrayList<Notification>();
		listNotificationDestinataire = new ArrayList<Notification>();

		setListLogs(new ArrayList<Xtelog>());
		setNotificationAdmin(new Notification());
		setNotificationDestinataire(new Notification());
		setLog(new Xtelog());

		listTemplateNotificationAdmin = new ArrayList<Templatenotification>();
		listTemplateNotificationDestinataire = new ArrayList<Templatenotification>();
		listTemplateLog = new ArrayList<Templatelog>();
		setTemplateNotificationAdmin(new Templatenotification());
		setTemplateNotificationDestinataire(new Templatenotification());
		setTemplateLog(new Templatelog());

		listVariableNotificationRef = new ArrayList<NotificationVariable>();
		listVariableNotification = new ArrayList<VariablesNotification>();

		ldapOperation = new LdapOperation();
		emailUtil = new EmailUtil();
		notificationNomDestinataire = new NotificationListAddress();
		listMailNomDestinataire = new ArrayList<NotificationListAddress>();

	}

	@PostConstruct
	public void Initialize() {

		System.out.println("****Lancement Aspect avec succes****");
	}

	// Initialize Notification Admin
	public void InitialzeNotificationAdmin() {
		try {

			info = vb.getInfo();
			listEvenementNotification = appMgr
					.listEvenementBylibelle(evenementNomVariableNotif);
			evenementNotification = listEvenementNotification.get(0);
			int refEvenement = evenementNotification.getEvenementId();

			listNotificationAdmin = appMgr
					.listNotificationByEvenementAndLibelle(refEvenement,
							notificationNomVariablAdmin);
			notificationAdmin = listNotificationAdmin.get(0);
			listTemplateNotificationAdmin = appMgr
					.listTemplateNotificationByNotification(notificationAdmin
							.getNotificationId());
			templateNotificationAdmin = listTemplateNotificationAdmin.get(0);
			listVariableNotificationRef = appMgr
					.listVariablesNotificationByNotification(notificationAdmin
							.getNotificationId());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Initialize Notification Destinataire
	public void InitialzeNotificationDestinataire() {
		try {
			info = vb.getInfo();

			listEvenementNotification = appMgr
					.listEvenementBylibelle(evenementNomVariableNotif);
			evenementNotification = listEvenementNotification.get(0);
			int refEvenement = evenementNotification.getEvenementId();

			listNotificationDestinataire = appMgr
					.listNotificationByEvenementAndLibelle(refEvenement,
							notificationNomVariableDestinataire);
			notificationDestinataire = listNotificationDestinataire.get(0);
			listTemplateNotificationDestinataire = appMgr
					.listTemplateNotificationByNotification(notificationDestinataire
							.getNotificationId());
			templateNotificationDestinataire = listTemplateNotificationDestinataire
					.get(0);
			listVariableNotificationRef = appMgr
					.listVariablesNotificationByNotification(notificationDestinataire
							.getNotificationId());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void InitialzeLog() {
		try {
			logger = Logger.getLogger(TracingAfterReturningAdvice.class);

			listEvenementLog = appMgr
					.listEvenementBylibelle(evenementNomVariableLog);
			evenementLog = listEvenementLog.get(0);

			listLogs = appMgr.listLogByEvenement(evenementLog.getEvenementId());
			log = listLogs.get(0);

			listTemplateLog = appMgr.listTemplateLogByLog(log.getLogId());
			templateLog = listTemplateLog.get(0);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Aspect pour les beans GBO
	@AfterReturning(pointcut = "execution(* xtensus.beans.common.GBO.*.ajouterCourrier(..))", returning = "result")
	public void logAfterReturning(JoinPoint joinPoint, Object result) {
		// vbg.setNomClass(joinPoint.getSignature().getClass().getName());
		try {
			// Chargement des parametres Mails
			// **
			ExternalContext jsfContext = FacesContext.getCurrentInstance()
					.getExternalContext();
			ServletContext servletContext = (ServletContext) jsfContext
					.getContext();
			String webContentRoot = servletContext.getRealPath("/");
			String pathConfigFile = webContentRoot
					+ File.separator
					+"WEB-INF"+File.separator+"MailFax"+File.separator+"mailCentral.properties";
			Properties props = new Properties();
			props.load(new FileInputStream(pathConfigFile));
			// **
			// Chargement des parametres Mails

			nomExpediteur = vb.getNomExpediteur();
			mailExpediteur = vb.getMailExpediteur();
			evenementNomVariableNotif = vb.getEvenementNomVariableNotif();
			evenementNomVariableLog = vb.getEvenementNomVariableLog();
			notificationNomVariablAdmin = vb.getNotificationNomVariablAdmin();
			notificationNomVariableDestinataire = vb
					.getNotificationNomVariableDestinataire();
			vb.setNomExpediteur("Admin");
			vb.setMailExpediteur(props.getProperty("mail.session.user"));

			// Traitement si la libellé d'admin est remplit
			if (evenementNomVariableNotif != null
					&& notificationNomVariablAdmin != null) {

				int refUser = vb.getPerson().getId();

				listEvenementNotification = appMgr
						.listEvenementBylibelle(evenementNomVariableNotif);
				// [AH]
				if (listEvenementNotification != null
						&& listEvenementNotification.size() > 0)
					evenementNotification = listEvenementNotification.get(0);

				int refEvenement = 0;
//				if (evenementNotification != null)
//					refEvenement = evenementNotification.getEvenementId();
				listNotificationAdmin = appMgr
						.listNotificationByEvenementAndLibelle(refEvenement,
								notificationNomVariablAdmin);
				if (listNotificationAdmin != null
						& listNotificationAdmin.size() > 0)
					notificationAdmin = listNotificationAdmin.get(0);
				// int refNot = notificationAdmin.getNotificationId();
				// listUserNotification = appMgr
				// .listNotificationByUserAndNotification(refUser, refNot);
				// if (listUserNotification.size() != 0) {
				// userNotification = listUserNotification.get(0);
				// etatNotificationAdmin = userNotification.isEtat();
				// } else {
				// etatNotificationAdmin = false;
				// }
//				if(notificationAdmin!=null)
//					etatNotificationAdmin = notificationAdmin
//						.getNotificationActivation();

				// if (refNot != 0) {
				// etatNotificationAdminGeneral = notificationAdmin
				// .getNotificationActivation();
				// }

			}

			// Traitement si la libellé de destinataire est remplit
			// Traitement si la libellé de destinataire est remplit
			if (evenementNomVariableNotif != null
					&& notificationNomVariableDestinataire != null) {

				int refUser = vb.getPerson().getId();

				listEvenementNotification = appMgr
						.listEvenementBylibelle(evenementNomVariableNotif);
				evenementNotification = listEvenementNotification.get(0);
				int refEvenement = evenementNotification.getEvenementId();
				listNotificationDestinataire = appMgr
						.listNotificationByEvenementAndLibelle(refEvenement,
								notificationNomVariableDestinataire);
				notificationDestinataire = listNotificationDestinataire.get(0);
				etatNotificationDestinataire = notificationDestinataire
						.getNotificationActivation();
				// int refNot = notificationDestinataire.getNotificationId();
				// listUserNotification = appMgr
				// .listNotificationByUserAndNotification(refUser, refNot);
				// if (listUserNotification.size() != 0) {
				// userNotification = listUserNotification.get(0);
				// etatNotificationDestinataire = userNotification.isEtat();
				// } else {
				// etatNotificationDestinataire = false;
				// }
				// if (refNot != 0) {
				// etatNotificationAdminGeneral = notificationDestinataire
				// .getNotificationActivation();
				// }

			}

			// Traitement de notification après verification de tt es reglé pr
			// notification admin

			if ((evenementNomVariableNotif != null
					&& notificationNomVariablAdmin != null && etatNotificationAdmin == true)) { // &&
																								// (etatNotificationAdminGeneral
																								// ==
																								// true)
				InitialzeNotificationAdmin();
				templateNotificationTexteAdmin = templateNotificationAdmin
						.getTemplateNotificationDescription();
				System.out.println(templateNotificationTexteAdmin);
				List<Informations> listInfo = new ArrayList<Informations>();
				listInfo = vb.getListInformations();
				int i = 0;
				while (i < listInfo.size()) {
					if (listInfo.get(i).getVar().equals("#p")) {
						listInfo.get(i).setContenu("Admin");
					}
					i++;
				}
				templateNotificationTexteAdmin = changeVariableNotification(
						templateNotificationTexteAdmin, info);

				// Insertion d'un nouveau message (mail)
				messageExp = new Message();
				messageExp.setNotification(notificationAdmin);
				messageExp.setIdUser(0);
				messageExp.setMessageTexte(templateNotificationTexteAdmin);
				try {
					appMgr.insert(messageExp);
				} catch (Exception e) {
					System.err.println("Erreur dans le message");
					System.err.println(e);
				}
				String SubjectNotification = notificationAdmin
						.getNotificationLibelle();
				String mailAdmin = props.getProperty("mailFrom");
				String nomAdmin = "Admin";

				notificationNomDestinataire.setMailDestinataire(mailAdmin);
				notificationNomDestinataire.setNomDestinataire(nomAdmin);
				listMailNomDestinataire.add(notificationNomDestinataire);

				System.out.println(templateNotificationTexteAdmin);

				try {
					emailUtil.sendEmailSSL(SubjectNotification,
							templateNotificationTexteAdmin,
							listMailNomDestinataire, nomExpediteur,
							mailExpediteur);
					System.out.println("Envoie effectué avec succes");
				} catch (NoSuchProviderException e) {
					System.err
							.println("Pas de transport disponible pour ce protocole");
					System.err.println(e);
				} catch (AddressException e) {
					System.err.println("Adresse invalide");
					System.err.println(e);
				} catch (MessagingException e) {
					System.err.println("Erreur dans le message");
					System.err.println(e);
				}

			}

			// Traitement de notification après verification de tt es reglé pr
			// notification destinataire
			if ((evenementNomVariableNotif != null
					&& notificationNomVariableDestinataire != null && etatNotificationDestinataire == true)) // &&
																												// (etatNotificationAdminGeneral
																												// ==
																												// true)
			{//AAAA
				InitialzeNotificationDestinataire();
				List<Informations> listInfo = new ArrayList<Informations>();
				listMailNomDestinataire = new ArrayList<NotificationListAddress>();
				listInfo = vb.getListInformations();

				String SubjectNotification = notificationDestinataire
						.getNotificationLibelle();

				if (!vb.getCopyListSelectedPersonNotif().isEmpty()) {
					Person person1;
					for (Person person : vb.getCopyListSelectedPersonNotif()) {
						person1 = new Person();
						person1 = ldapOperation.getUserById(person.getId());
						int i = 0;
						while (i < listInfo.size()) {
							if (listInfo.get(i).getVar().equals("#p")) {
								listInfo.get(i).setContenu(person1.getNom());
							}
							i++;
						}
						notificationNomDestinataire.setMailDestinataire(person1
								.getEmail());
						notificationNomDestinataire.setNomDestinataire(person1
								.getPrenom() + " " + person1.getNom());
						listMailNomDestinataire
								.add(notificationNomDestinataire);

						templateNotificationTexteDestinataire = templateNotificationDestinataire
								.getTemplateNotificationDescription();
						System.out
								.println(templateNotificationTexteDestinataire);
						templateNotificationTexteDestinataire = changeVariableNotification(
								templateNotificationTexteDestinataire, info);
						// Insertion d'un nouveau message (mail)
						messageDest = new Message();
						messageDest.setNotification(notificationDestinataire);
						messageDest.setIdUser(person1.getId());
						messageDest
								.setMessageTexte(templateNotificationTexteDestinataire);
						try {
							appMgr.insert(messageDest);
						} catch (Exception e) {
							System.err
									.println("Erreur dans le message mail Dest");
							System.err.println(e);
						}

						try {
							emailUtil.sendEmailSSL(SubjectNotification,
									templateNotificationTexteDestinataire,
									listMailNomDestinataire, nomExpediteur,
									mailExpediteur);
							System.out.println("Envoie effectué avec succes");
						} catch (NoSuchProviderException e) {
							e.printStackTrace();
							System.err
									.println("Pas de transport disponible pour ce protocole");
							System.err.println(e);
						} catch (AddressException e) {
							System.err.println("Adresse invalide");
							System.err.println(e);
						} catch (MessagingException e) {
							System.err.println("Erreur dans le message");
							System.err.println(e);
						}
						listMailNomDestinataire = new ArrayList<NotificationListAddress>();
					}
				}
System.out.println("getCopyListSelectedUnitNotif :  "+vb.getCopyListSelectedUnitNotif().size());
				if (!vb.getCopyListSelectedUnitNotif().isEmpty()) {
					Person person;
					Unit unit1;
					for (Unit unit : vb.getCopyListSelectedUnitNotif()) {
						unit1 = new Unit();
						unit1 = ldapOperation.getUnitById(unit.getIdUnit());
						System.out.println(unit1.getNameUnit());
						person = new Person();
						person = unit1.getResponsibleUnit();
						if(person==null || person.getId()==0){
							continue;
						}
						System.out.println("Person : "+person);
						int i = 0;
						while (i < listInfo.size()) {
							if (listInfo.get(i).getVar().equals("#p")) {
								listInfo.get(i).setContenu(person.getNom());
							}
							i++;
						}
						notificationNomDestinataire.setMailDestinataire(person
								.getEmail());
						notificationNomDestinataire.setNomDestinataire(person
								.getPrenom() + " " + person.getNom());
						listMailNomDestinataire
								.add(notificationNomDestinataire);

						templateNotificationTexteDestinataire = templateNotificationDestinataire
								.getTemplateNotificationDescription();
						System.out
								.println(templateNotificationTexteDestinataire);
						templateNotificationTexteDestinataire = changeVariableNotification(
								templateNotificationTexteDestinataire, info);

						// Insertion d'un nouveau message (mail)
						messageDest = new Message();
						messageDest.setNotification(notificationDestinataire);
						messageDest.setIdUser(person.getId());
						messageDest
								.setMessageTexte(templateNotificationTexteDestinataire);
						try {
							appMgr.insert(messageDest);
						} catch (Exception e) {
							System.err.println("Erreur dans le message");
							System.err.println(e);
						}

						try {
							emailUtil.sendEmailSSL(SubjectNotification,
									templateNotificationTexteDestinataire,
									listMailNomDestinataire, nomExpediteur,
									mailExpediteur);
							System.out.println("Envoie effectué avec succes");
						} catch (NoSuchProviderException e) {
							System.err
									.println("Pas de transport disponible pour ce protocole");
							System.err.println(e);
						} catch (AddressException e) {
							System.err.println("Adresse invalide");
							System.err.println(e);
						} catch (MessagingException e) {
							System.err.println("Erreur dans le message");
							System.err.println(e);
						}
						listMailNomDestinataire = new ArrayList<NotificationListAddress>();
					}
				}

				

			}

			if (evenementNomVariableLog != null) {
				InitialzeLog();
				vb.setLogType("INFO");
				templateLogTexte = templateLog.getTemplateLogVariable();
				System.out.println(templateLogTexte);
				templateLogTexte = changeVariableLog(templateLogTexte);
				System.out.println(templateLogTexte);

				URL u = getClass().getClassLoader().getResource(
						"xtensus/aop/log4j.xml");
				DOMConfigurator.configure(u);
				userLog = vb.getPerson().getPrenom() + " "
						+ vb.getPerson().getNom();
				dateLog = vb.getLogDate();
				typeLog = vb.getLogType();
				logger.info(templateLogTexte);

				// Insertion du trace dans la base de donnée
				// Trace trace = new Trace();
				// trace.setXtelog(log);
				// trace.setTypelog("INFO");
				// trace.setUserTexte(userLog);
				// trace.setDateTexte(logDate);
				// trace.setTraceTexte(templateLogTexte);

				try {
					// appMgr.insert(trace);
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("Erreur dans le Trace");
					System.err.println(e);
				}
			}

			System.out.println("logAfterReturning() is running!");
			System.out.println("hijacked : "
					+ joinPoint.getSignature().getName());
			System.out.println("Method returned value is : " + result);
			System.out.println("******");
			nomExpediteur = null;
			mailExpediteur = null;
			evenementNomVariableNotif = null;
			evenementNomVariableLog = null;
			notificationNomVariablAdmin = null;
			notificationNomVariableDestinataire = null;
			vb.setNomExpediteur(null);
			vb.setMailExpediteur(null);
			vb.setEvenementNomVariableNotif(null);
			vb.setEvenementNomVariableLog(null);
			vb.setNotificationNomVariablAdmin(null);
			vb.setNotificationNomVariableDestinataire(null);
			vb.setCopyListSelectedPersonNotif(new ArrayList<Person>());
			vb.setCopyListPPNotif(new ArrayList<Pp>());
			vb.setCopyListPMNotif(new ArrayList<Pm>());
			vb.setCopyListSelectedUnitNotif(new ArrayList<Unit>());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Aspect pour les beans GBO Update
	@AfterReturning(pointcut = "execution(* xtensus.beans.common.GBO.*.update(..))", returning = "result")
	public void aspectModifCourrier(JoinPoint joinPoint, Object result) {
		// vbg.setNomClass(joinPoint.getSignature().getClass().getName());
		// Chargement des parametres Mails
		// **
		try {

			ExternalContext jsfContext = FacesContext.getCurrentInstance()
					.getExternalContext();
			ServletContext servletContext = (ServletContext) jsfContext
					.getContext();
			String webContentRoot = servletContext.getRealPath("/");
			String pathConfigFile = webContentRoot
					+File.separator+"WEB-INF"+File.separator+"MailFax"+File.separator+"mailCentral.properties";
			Properties props = new Properties();
			props.load(new FileInputStream(pathConfigFile));

			// **
			// Chargement des parametres Mails
			nomExpediteur = vb.getNomExpediteur();
			mailExpediteur = vb.getMailExpediteur();
			evenementNomVariableNotif = vb.getEvenementNomVariableNotif();
			evenementNomVariableLog = vb.getEvenementNomVariableLog();
			notificationNomVariablAdmin = vb.getNotificationNomVariablAdmin();
			notificationNomVariableDestinataire = vb
					.getNotificationNomVariableDestinataire();
			vb.setNomExpediteur("Admin");
			// vb.setMailExpediteur("no-reply@elmedina.tn");
			vb.setMailExpediteur(props.getProperty("mail.session.user"));
			// Traitement si la libellé d'admin est remplit
			if (evenementNomVariableNotif != null
					&& notificationNomVariablAdmin != null) {

				int refUser = vb.getPerson().getId();

				listEvenementNotification = appMgr
						.listEvenementBylibelle(evenementNomVariableNotif);
				evenementNotification = listEvenementNotification.get(0);
				int refEvenement = evenementNotification.getEvenementId();
				listNotificationAdmin = appMgr
						.listNotificationByEvenementAndLibelle(refEvenement,
								notificationNomVariablAdmin);
				notificationAdmin = listNotificationAdmin.get(0);
				etatNotificationAdmin = notificationAdmin
						.getNotificationActivation();
				// int refNot = notificationAdmin.getNotificationId();
				// listUserNotification = appMgr
				// .listNotificationByUserAndNotification(refUser, refNot);
				// if (listUserNotification.size() != 0) {
				// userNotification = listUserNotification.get(0);
				// etatNotificationAdmin = userNotification.isEtat();
				// } else {
				// etatNotificationAdmin = false;
				// }
				// if (refNot != 0) {
				// etatNotificationAdminGeneral = notificationAdmin
				// .getNotificationActivation();
				// }

			}

			// Traitement si la libellé de destinataire est remplit
			if (evenementNomVariableNotif != null
					&& notificationNomVariableDestinataire != null) {

				int refUser = vb.getPerson().getId();

				listEvenementNotification = appMgr
						.listEvenementBylibelle(evenementNomVariableNotif);
				evenementNotification = listEvenementNotification.get(0);
				int refEvenement = evenementNotification.getEvenementId();
				listNotificationDestinataire = appMgr
						.listNotificationByEvenementAndLibelle(refEvenement,
								notificationNomVariableDestinataire);
				notificationDestinataire = listNotificationDestinataire.get(0);
				etatNotificationDestinataire = notificationDestinataire
						.getNotificationActivation();
				// int refNot = notificationDestinataire.getNotificationId();
				// listUserNotification = appMgr
				// .listNotificationByUserAndNotification(refUser, refNot);
				// if (listUserNotification.size() != 0) {
				// userNotification = listUserNotification.get(0);
				// etatNotificationDestinataire = userNotification.isEtat();
				// } else {
				// etatNotificationDestinataire = false;
				// }
				// if (refNot != 0) {
				// etatNotificationAdminGeneral = notificationDestinataire
				// .getNotificationActivation();
				// }

			}

			// Traitement de notification après verification de tt es reglé pr
			// notification admin
			if ((evenementNomVariableNotif != null
					&& notificationNomVariablAdmin != null && etatNotificationAdmin == true) // &&
																								// (etatNotificationAdminGeneral
																								// ==
																								// true)
			) {
				InitialzeNotificationAdmin();
				templateNotificationTexteAdmin = templateNotificationAdmin
						.getTemplateNotificationDescription();
				System.out.println(templateNotificationTexteAdmin);
				List<Informations> listInfo = new ArrayList<Informations>();
				listInfo = vb.getListInformations();
				int i = 0;
				while (i < listInfo.size()) {
					if (listInfo.get(i).getVar().equals("#p")) {
						listInfo.get(i).setContenu("Admin");
					}
					i++;
				}
				templateNotificationTexteAdmin = changeVariableNotification(
						templateNotificationTexteAdmin, info);

				// Insertion d'un nouveau message (mail)
				messageExp = new Message();
				messageExp.setNotification(notificationAdmin);
				messageExp.setIdUser(0);
				messageExp.setMessageTexte(templateNotificationTexteAdmin);
				try {
					appMgr.insert(messageExp);
				} catch (Exception e) {
					System.err
							.println("Erreur dans le message de la base de donnée admin");
					System.err.println(e);
				}
				String SubjectNotification = notificationAdmin
						.getNotificationLibelle();
				// String mailAdmin = "gbo@elmedina.tn";
				String mailAdmin = props.getProperty("mailFrom");
				String nomAdmin = "Administrator";

				notificationNomDestinataire.setMailDestinataire(mailAdmin);
				notificationNomDestinataire.setNomDestinataire(nomAdmin);
				listMailNomDestinataire.add(notificationNomDestinataire);

				System.out.println(templateNotificationTexteAdmin);

				try {
					emailUtil.sendEmailSSL(SubjectNotification,
							templateNotificationTexteAdmin,
							listMailNomDestinataire, nomExpediteur,
							mailExpediteur);
					System.out.println("Envoie effectué avec succes");
				} catch (NoSuchProviderException e) {
					System.err
							.println("Pas de transport disponible pour ce protocole");
					System.err.println(e);
				} catch (AddressException e) {
					System.err.println("Adresse invalide");
					System.err.println(e);
				} catch (MessagingException e) {
					System.err.println("Erreur dans le message mail");
					System.err.println(e);
				}

			}

			// Traitement de notification après verification de tt es reglé pr
			// notification destinataire
			if ((evenementNomVariableNotif != null
					&& notificationNomVariableDestinataire != null && etatNotificationDestinataire == true) // &&
																											// (etatNotificationAdminGeneral
																											// ==
																											// true)
			) {
				InitialzeNotificationDestinataire();
				List<Informations> listInfo = new ArrayList<Informations>();
				listMailNomDestinataire = new ArrayList<NotificationListAddress>();
				listInfo = vb.getListInformations();
				templateNotificationTexteDestinataire = templateNotificationDestinataire
						.getTemplateNotificationDescription();
				System.out.println(templateNotificationTexteDestinataire);
				templateNotificationTexteDestinataire = changeVariableNotification(
						templateNotificationTexteDestinataire, info);

				String SubjectNotification = notificationDestinataire
						.getNotificationLibelle();

				if (!vb.getCopyListSelectedPersonNotif().isEmpty()) {
					Person person1;
					for (Person person : vb.getCopyListSelectedPersonNotif()) {
						person1 = new Person();
						person1 = ldapOperation.getUserById(person.getId());
						int i = 0;
						while (i < listInfo.size()) {
							if (listInfo.get(i).getVar().equals("#p")) {
								listInfo.get(i).setContenu(person1.getNom());
							}
							i++;
						}
						notificationNomDestinataire.setMailDestinataire(person1
								.getEmail());
						notificationNomDestinataire.setNomDestinataire(person1
								.getPrenom() + " " + person1.getNom());
						listMailNomDestinataire
								.add(notificationNomDestinataire);

						templateNotificationTexteDestinataire = templateNotificationDestinataire
								.getTemplateNotificationDescription();
						System.out
								.println(templateNotificationTexteDestinataire);
						templateNotificationTexteDestinataire = changeVariableNotification(
								templateNotificationTexteDestinataire, info);

						// Insertion d'un nouveau message (mail)
						messageDest = new Message();
						messageDest.setNotification(notificationDestinataire);
						messageDest.setIdUser(person1.getId());
						messageDest
								.setMessageTexte(templateNotificationTexteDestinataire);

						try {
							appMgr.insert(messageDest);
						} catch (Exception e) {
							System.err
									.println("Erreur dans le message de la base de donnée desu");
							System.err.println(e);
						}

						try {
							emailUtil.sendEmailSSL(SubjectNotification,
									templateNotificationTexteDestinataire,
									listMailNomDestinataire, nomExpediteur,
									mailExpediteur);
							System.out.println("Envoie effectué avec succes");
						} catch (NoSuchProviderException e) {
							System.err
									.println("Pas de transport disponible pour ce protocole");
							System.err.println(e);
						} catch (AddressException e) {
							System.err.println("Adresse invalide");
							System.err.println(e);
						} catch (MessagingException e) {
							System.err.println("Erreur dans le message mail");
							System.err.println(e);
						}
						listMailNomDestinataire = new ArrayList<NotificationListAddress>();
					}
				}

				if (!vb.getCopyListSelectedUnitNotif().isEmpty()) {
					Person person;
					Unit unit1;
					for (Unit unit : vb.getCopyListSelectedUnitNotif()) {
						unit1 = new Unit();
						unit1 = ldapOperation.getUnitById(unit.getIdUnit());
						person = new Person();
						person = unit1.getResponsibleUnit();
						int i = 0;
						while (i < listInfo.size()) {
							if (listInfo.get(i).getVar().equals("#p")) {
								listInfo.get(i).setContenu(person.getNom());
							}
							i++;
						}
						notificationNomDestinataire.setMailDestinataire(person
								.getEmail());
						notificationNomDestinataire.setNomDestinataire(person
								.getPrenom() + " " + person.getNom());
						listMailNomDestinataire
								.add(notificationNomDestinataire);

						templateNotificationTexteDestinataire = templateNotificationDestinataire
								.getTemplateNotificationDescription();
						templateNotificationTexteDestinataire = changeVariableNotification(
								templateNotificationTexteDestinataire, info);
						System.out
								.println(templateNotificationTexteDestinataire);

						// Insertion d'un nouveau message (mail)
						messageDest = new Message();
						messageDest.setNotification(notificationDestinataire);
						messageDest.setIdUser(person.getId());
						messageDest
								.setMessageTexte(templateNotificationTexteDestinataire);
						try {
							appMgr.insert(messageDest);
						} catch (Exception e) {
							System.err
									.println("Erreur dans le message de la base de donnée desu");
							System.err.println(e);
						}

						try {
							emailUtil.sendEmailSSL(SubjectNotification,
									templateNotificationTexteDestinataire,
									listMailNomDestinataire, nomExpediteur,
									mailExpediteur);
							System.out.println("Envoie effectué avec succes");
						} catch (NoSuchProviderException e) {
							System.err
									.println("Pas de transport disponible pour ce protocole");
							System.err.println(e);
						} catch (AddressException e) {
							System.err.println("Adresse invalide");
							System.err.println(e);
						} catch (MessagingException e) {
							System.err.println("Erreur dans le message mail");
							System.err.println(e);
						}
						listMailNomDestinataire = new ArrayList<NotificationListAddress>();
					}
				}

				if (!vb.getCopyListPMNotif().isEmpty()) {
					Expdestexterne expDest;
					for (Pm pm : vb.getCopyListPMNotif()) {
						expDest = pm.getExpdestexterne();
						int i = 0;
						while (i < listInfo.size()) {
							if (listInfo.get(i).getVar().equals("#p")) {
								listInfo.get(i).setContenu(
										expDest.getExpDestExterneNom());
							}
							i++;
						}
						notificationNomDestinataire.setMailDestinataire(expDest
								.getExpDestExterneMail());
						notificationNomDestinataire.setNomDestinataire(expDest
								.getExpDestExternePrenom()
								+ " "
								+ expDest.getExpDestExterneNom());
						listMailNomDestinataire
								.add(notificationNomDestinataire);

						templateNotificationTexteDestinataire = templateNotificationDestinataire
								.getTemplateNotificationDescription();
						templateNotificationTexteDestinataire = changeVariableNotification(
								templateNotificationTexteDestinataire, info);
						System.out
								.println(templateNotificationTexteDestinataire);

						// Insertion d'un nouveau message (mail)
						messageDest = new Message();
						messageDest.setNotification(notificationDestinataire);
						messageDest.setIdUser(expDest.getIdExpDestExterne());
						messageDest
								.setMessageTexte(templateNotificationTexteDestinataire);
						try {
							appMgr.insert(messageDest);
						} catch (Exception e) {
							System.err
									.println("Erreur dans le message de la base de donnée desu");
							System.err.println(e);
						}

						try {
							emailUtil.sendEmailSSL(SubjectNotification,
									templateNotificationTexteDestinataire,
									listMailNomDestinataire, nomExpediteur,
									mailExpediteur);
							System.out.println("Envoie effectué avec succes");
						} catch (NoSuchProviderException e) {
							System.err
									.println("Pas de transport disponible pour ce protocole");
							System.err.println(e);
						} catch (AddressException e) {
							System.err.println("Adresse invalide");
							System.err.println(e);
						} catch (MessagingException e) {
							System.err.println("Erreur dans le message");
							System.err.println(e);
						}
						listMailNomDestinataire = new ArrayList<NotificationListAddress>();
					}

				}
				if (!vb.getCopyListPPNotif().isEmpty()) {
					Expdestexterne expDest;
					for (Pp pp : vb.getCopyListPPNotif()) {
						expDest = pp.getExpdestexterne();
						int i = 0;
						while (i < listInfo.size()) {
							if (listInfo.get(i).getVar().equals("#p")) {
								listInfo.get(i).setContenu(
										expDest.getExpDestExterneNom());
							}
							i++;
						}
						notificationNomDestinataire.setMailDestinataire(expDest
								.getExpDestExterneMail());
						notificationNomDestinataire.setNomDestinataire(expDest
								.getExpDestExternePrenom()
								+ " "
								+ expDest.getExpDestExterneNom());
						listMailNomDestinataire
								.add(notificationNomDestinataire);

						templateNotificationTexteDestinataire = templateNotificationDestinataire
								.getTemplateNotificationDescription();
						templateNotificationTexteDestinataire = changeVariableNotification(
								templateNotificationTexteDestinataire, info);
						System.out
								.println(templateNotificationTexteDestinataire);

						// Insertion d'un nouveau message (mail)
						messageDest = new Message();
						messageDest.setNotification(notificationDestinataire);
						messageDest.setIdUser(expDest.getIdExpDestExterne());
						messageDest
								.setMessageTexte(templateNotificationTexteDestinataire);
						try {
							appMgr.insert(messageDest);
						} catch (Exception e) {
							System.err
									.println("Erreur dans le message de la base de donnée desu");
							System.err.println(e);
						}

						try {
							emailUtil.sendEmailSSL(SubjectNotification,
									templateNotificationTexteDestinataire,
									listMailNomDestinataire, nomExpediteur,
									mailExpediteur);
							System.out.println("Envoie effectué avec succes");
						} catch (NoSuchProviderException e) {
							System.err
									.println("Pas de transport disponible pour ce protocole");
							System.err.println(e);
						} catch (AddressException e) {
							System.err.println("Adresse invalide");
							System.err.println(e);
						} catch (MessagingException e) {
							System.err.println("Erreur dans le message");
							System.err.println(e);
						}
						listMailNomDestinataire = new ArrayList<NotificationListAddress>();
					}
				}

			}

			if (evenementNomVariableLog != null) {
				InitialzeLog();
				vb.setLogType("INFO");
				templateLogTexte = templateLog.getTemplateLogVariable();
				System.out.println(templateLogTexte);
				templateLogTexte = changeVariableLog(templateLogTexte);
				System.out.println(templateLogTexte);

				URL u = getClass().getClassLoader().getResource(
						"xtensus/aop/log4j.xml");
				DOMConfigurator.configure(u);
				userLog = vb.getPerson().getPrenom() + " "
						+ vb.getPerson().getNom();
				dateLog = vb.getLogDate();
				typeLog = vb.getLogType();
				logger.info(templateLogTexte);

				// Insertion du trace dans la base de donnée
				// Trace trace = new Trace();
				// trace.setXtelog(log);
				// trace.setTypelog("INFO");
				// trace.setUserTexte(userLog);
				// trace.setDateTexte(logDate);
				// trace.setTraceTexte(templateLogTexte);

				try {
					// appMgr.insert(trace);
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("Erreur dans le Trace");
					System.err.println(e);
				}
			}

			System.out.println("logAfterReturning() is running!");
			System.out.println("hijacked : "
					+ joinPoint.getSignature().getName());
			System.out.println("Method returned value is : " + result);
			System.out.println("******");
			nomExpediteur = null;
			mailExpediteur = null;
			evenementNomVariableNotif = null;
			evenementNomVariableLog = null;
			notificationNomVariablAdmin = null;
			notificationNomVariableDestinataire = null;
			vb.setNomExpediteur(null);
			vb.setMailExpediteur(null);
			vb.setEvenementNomVariableNotif(null);
			vb.setEvenementNomVariableLog(null);
			vb.setNotificationNomVariablAdmin(null);
			vb.setNotificationNomVariableDestinataire(null);
			vb.setCopyListSelectedPersonNotif(new ArrayList<Person>());
			vb.setCopyListPPNotif(new ArrayList<Pp>());
			vb.setCopyListPMNotif(new ArrayList<Pm>());
			vb.setCopyListSelectedUnitNotif(new ArrayList<Unit>());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Aspect pour les beans GBO consult row ancien
	@AfterReturning(pointcut = "execution(* xtensus.beans.common.GBO.*.getSelectionRowJour(..))", returning = "result")
	public void aspectConsultCourrier(JoinPoint joinPoint, Object result) {
		// vbg.setNomClass(joinPoint.getSignature().getClass().getName());
		try {
			ExternalContext jsfContext = FacesContext.getCurrentInstance()
					.getExternalContext();
			ServletContext servletContext = (ServletContext) jsfContext
					.getContext();
			String webContentRoot = servletContext.getRealPath("/");
			String pathConfigFile = webContentRoot
					+ File.separator+"WEB-INF"+File.separator+"MailFax"+File.separator+"mailCentral.properties";
			Properties props = new Properties();
			props.load(new FileInputStream(pathConfigFile));
			vb.setMailExpediteur(props.getProperty("mail.session.user"));
			nomExpediteur = vb.getNomExpediteur();
			mailExpediteur = vb.getMailExpediteur();
			evenementNomVariableNotif = vb.getEvenementNomVariableNotif();
			evenementNomVariableLog = vb.getEvenementNomVariableLog();
			notificationNomVariablAdmin = vb.getNotificationNomVariablAdmin();
			notificationNomVariableDestinataire = vb
					.getNotificationNomVariableDestinataire();

			// Traitement si la libellé d'admin est remplit
			if (evenementNomVariableNotif != null
					&& notificationNomVariablAdmin != null) {

				int refUser = vb.getPerson().getId();

				listEvenementNotification = appMgr
						.listEvenementBylibelle(evenementNomVariableNotif);
				evenementNotification = listEvenementNotification.get(0);
				int refEvenement = evenementNotification.getEvenementId();
				listNotificationAdmin = appMgr
						.listNotificationByEvenementAndLibelle(refEvenement,
								notificationNomVariablAdmin);
				notificationAdmin = listNotificationAdmin.get(0);
				etatNotificationAdmin = notificationAdmin
						.getNotificationActivation();
				// int refNot = notificationAdmin.getNotificationId();
				// listUserNotification = appMgr
				// .listNotificationByUserAndNotification(refUser, refNot);
				// if (listUserNotification.size() != 0) {
				// userNotification = listUserNotification.get(0);
				// etatNotificationAdmin = userNotification.isEtat();
				// } else {
				// etatNotificationAdmin = false;
				// }
				// if (refNot != 0) {
				// etatNotificationAdmin = notificationAdmin
				// .getNotificationActivation();
				// }

			}

			// Traitement si la libellé de destinataire est remplit
			if (evenementNomVariableNotif != null
					&& notificationNomVariableDestinataire != null) {

				int refUser = vb.getPerson().getId();

				listEvenementNotification = appMgr
						.listEvenementBylibelle(evenementNomVariableNotif);
				evenementNotification = listEvenementNotification.get(0);
				int refEvenement = evenementNotification.getEvenementId();
				listNotificationDestinataire = appMgr
						.listNotificationByEvenementAndLibelle(refEvenement,
								notificationNomVariableDestinataire);
				notificationDestinataire = listNotificationDestinataire.get(0);
				etatNotificationDestinataire = notificationDestinataire
						.getNotificationActivation();
				// int refNot = notificationDestinataire.getNotificationId();
				// listUserNotification = appMgr
				// .listNotificationByUserAndNotification(refUser, refNot);
				// if (listUserNotification.size() != 0) {
				//
				// userNotification = listUserNotification.get(0);
				// etatNotificationDestinataire = userNotification.isEtat();
				// } else {
				// etatNotificationDestinataire = false;
				// }
				// if (refNot != 0) {
				// etatNotificationAdmin = notificationDestinataire
				// .getNotificationActivation();
				// }

			}

			// Traitement de notification après verification de tt es reglé pr
			// notification admin
			if ((evenementNomVariableNotif != null
					&& notificationNomVariablAdmin != null && etatNotificationAdmin == true) // &&
																								// (etatNotificationAdminGeneral
																								// ==
																								// true)
			) {
				InitialzeNotificationAdmin();
				templateNotificationTexteAdmin = templateNotificationAdmin
						.getTemplateNotificationDescription();
				System.out.println(templateNotificationTexteAdmin);
				List<Informations> listInfo = new ArrayList<Informations>();
				listInfo = vb.getListInformations();
				int i = 0;
				while (i < listInfo.size()) {
					if (listInfo.get(i).getVar().equals("#p")) {
						listInfo.get(i).setContenu("Admin");
					}
					i++;
				}
				templateNotificationTexteAdmin = changeVariableNotification(
						templateNotificationTexteAdmin, info);

				// Insertion d'un nouveau message (mail)
				messageExp = new Message();
				messageExp.setNotification(notificationAdmin);
				messageExp.setIdUser(0);
				messageExp.setMessageTexte(templateNotificationTexteAdmin);
				try {
					appMgr.insert(messageExp);
				} catch (Exception e) {
					e.printStackTrace();
					System.err
							.println("Erreur dans le message de la base de donnée admin");
					System.err.println(e);
				}
				String SubjectNotification = notificationAdmin
						.getNotificationLibelle();
				// String mailAdmin = "gbo@elmedina.tn";
				String mailAdmin = props.getProperty("mailFrom");
				String nomAdmin = "Administrator";

				notificationNomDestinataire.setMailDestinataire(mailAdmin);
				notificationNomDestinataire.setNomDestinataire(nomAdmin);
				listMailNomDestinataire.add(notificationNomDestinataire);

				System.out.println(templateNotificationTexteAdmin);

				try {
					emailUtil.sendEmailSSL(SubjectNotification,
							templateNotificationTexteAdmin,
							listMailNomDestinataire, nomExpediteur,
							mailExpediteur);
					System.out.println("Envoie effectué avec succes");
				} catch (NoSuchProviderException e) {
					e.printStackTrace();
					System.err
							.println("Pas de transport disponible pour ce protocole");
					System.err.println(e);
				} catch (AddressException e) {
					e.printStackTrace();
					System.err.println("Adresse invalide");
					System.err.println(e);
				} catch (MessagingException e) {
					e.printStackTrace();
					System.err.println("Erreur dans le message mail");
					System.err.println(e);
				}

			}

			// Traitement de notification après verification de tt es reglé pr
			// notification destinataire
			if ((evenementNomVariableNotif != null
					&& notificationNomVariableDestinataire != null && etatNotificationDestinataire == true) // &&
																											// (etatNotificationAdminGeneral
																											// ==
																											// true)
			) {
				InitialzeNotificationDestinataire();
				List<Informations> listInfo = new ArrayList<Informations>();
				listMailNomDestinataire = new ArrayList<NotificationListAddress>();
				listInfo = vb.getListInformations();
				templateNotificationTexteDestinataire = templateNotificationDestinataire
						.getTemplateNotificationDescription();
				System.out.println(templateNotificationTexteDestinataire);

				String SubjectNotification = notificationDestinataire
						.getNotificationLibelle();

				if (!vb.getCopyListSelectedPersonNotif().isEmpty()) {
					Person person1;
					for (Person person : vb.getCopyListSelectedPersonNotif()) {
						person1 = new Person();
						person1 = ldapOperation.getUserById(person.getId());
						int i = 0;
						while (i < listInfo.size()) {
							if (listInfo.get(i).getVar().equals("#p")) {
								listInfo.get(i).setContenu(person1.getNom());
							}
							i++;
						}
						notificationNomDestinataire.setMailDestinataire(person1
								.getEmail());
						notificationNomDestinataire.setNomDestinataire(person1
								.getPrenom() + " " + person1.getNom());
						listMailNomDestinataire
								.add(notificationNomDestinataire);

						templateNotificationTexteDestinataire = templateNotificationDestinataire
								.getTemplateNotificationDescription();
						templateNotificationTexteDestinataire = changeVariableNotification(
								templateNotificationTexteDestinataire, info);
						System.out
								.println(templateNotificationTexteDestinataire);

						// Insertion d'un nouveau message (mail)
						messageDest = new Message();
						messageDest.setNotification(notificationDestinataire);
						messageDest.setIdUser(person1.getId());
						messageDest
								.setMessageTexte(templateNotificationTexteDestinataire);
						try {
							appMgr.insert(messageDest);
						} catch (Exception e) {
							e.printStackTrace();
							System.err
									.println("Erreur dans le message mail Dest");
							System.err.println(e);
						}

						try {
							emailUtil.sendEmailSSL(SubjectNotification,
									templateNotificationTexteDestinataire,
									listMailNomDestinataire, nomExpediteur,
									mailExpediteur);
							System.out.println("Envoie effectué avec succes");
						} catch (NoSuchProviderException e) {
							e.printStackTrace();
							System.err
									.println("Pas de transport disponible pour ce protocole");
							System.err.println(e);
						} catch (AddressException e) {
							e.printStackTrace();
							System.err.println("Adresse invalide");
							System.err.println(e);
						} catch (MessagingException e) {
							e.printStackTrace();
							System.err.println("Erreur dans le message mail");
							System.err.println(e);
						}
						listMailNomDestinataire = new ArrayList<NotificationListAddress>();
					}
				}

				if (!vb.getCopyListSelectedUnitNotif().isEmpty()) {
					Person person;
					Unit unit1;
					for (Unit unit : vb.getCopyListSelectedUnitNotif()) {
						unit1 = new Unit();
						unit1 = ldapOperation.getUnitById(unit.getIdUnit());
						person = new Person();
						person = unit1.getResponsibleUnit();
						int i = 0;
						while (i < listInfo.size()) {
							if (listInfo.get(i).getVar().equals("#p")) {
								listInfo.get(i).setContenu(person.getNom());
							}
							i++;
						}
						notificationNomDestinataire.setMailDestinataire(person
								.getEmail());
						notificationNomDestinataire.setNomDestinataire(person
								.getPrenom() + " " + person.getNom());
						listMailNomDestinataire
								.add(notificationNomDestinataire);

						templateNotificationTexteDestinataire = templateNotificationDestinataire
								.getTemplateNotificationDescription();
						templateNotificationTexteDestinataire = changeVariableNotification(
								templateNotificationTexteDestinataire, info);
						System.out
								.println(templateNotificationTexteDestinataire);

						// Insertion d'un nouveau message (mail)
						messageDest = new Message();
						messageDest.setNotification(notificationDestinataire);
						messageDest.setIdUser(person.getId());
						messageDest
								.setMessageTexte(templateNotificationTexteDestinataire);
						try {
							appMgr.insert(messageDest);
						} catch (Exception e) {
							e.printStackTrace();
							System.err
									.println("Erreur dans le message mail Dest");
							System.err.println(e);
						}

						try {
							emailUtil.sendEmailSSL(SubjectNotification,
									templateNotificationTexteDestinataire,
									listMailNomDestinataire, nomExpediteur,
									mailExpediteur);
							System.out.println("Envoie effectué avec succes");
						} catch (NoSuchProviderException e) {
							e.printStackTrace();
							System.err
									.println("Pas de transport disponible pour ce protocole");
							System.err.println(e);
						} catch (AddressException e) {
							e.printStackTrace();
							System.err.println("Adresse invalide");
							System.err.println(e);
						} catch (MessagingException e) {
							e.printStackTrace();
							System.err.println("Erreur dans le message mail");
							System.err.println(e);
						}
						listMailNomDestinataire = new ArrayList<NotificationListAddress>();
					}
				}

				if (!vb.getCopyListPMNotif().isEmpty()) {
					Expdestexterne expDest;
					for (Pm pm : vb.getCopyListPMNotif()) {
						expDest = pm.getExpdestexterne();
						int i = 0;
						while (i < listInfo.size()) {
							if (listInfo.get(i).getVar().equals("#p")) {
								listInfo.get(i).setContenu(
										expDest.getExpDestExterneNom());
							}
							i++;
						}
						notificationNomDestinataire.setMailDestinataire(expDest
								.getExpDestExterneMail());
						notificationNomDestinataire.setNomDestinataire(expDest
								.getExpDestExternePrenom()
								+ " "
								+ expDest.getExpDestExterneNom());
						listMailNomDestinataire
								.add(notificationNomDestinataire);

						templateNotificationTexteDestinataire = templateNotificationDestinataire
								.getTemplateNotificationDescription();
						templateNotificationTexteDestinataire = changeVariableNotification(
								templateNotificationTexteDestinataire, info);
						System.out
								.println(templateNotificationTexteDestinataire);

						// Insertion d'un nouveau message (mail)
						messageDest = new Message();
						messageDest.setNotification(notificationDestinataire);
						messageDest.setIdUser(expDest.getIdExpDestExterne());
						messageDest
								.setMessageTexte(templateNotificationTexteDestinataire);
						try {
							appMgr.insert(messageDest);
						} catch (Exception e) {
							e.printStackTrace();
							System.err
									.println("Erreur dans le message mail Dest");
							System.err.println(e);
						}

						try {
							emailUtil.sendEmailSSL(SubjectNotification,
									templateNotificationTexteDestinataire,
									listMailNomDestinataire, nomExpediteur,
									mailExpediteur);
							System.out.println("Envoie effectué avec succes");
						} catch (NoSuchProviderException e) {
							e.printStackTrace();
							System.err
									.println("Pas de transport disponible pour ce protocole");
							System.err.println(e);
						} catch (AddressException e) {
							e.printStackTrace();
							System.err.println("Adresse invalide");
							System.err.println(e);
						} catch (MessagingException e) {
							e.printStackTrace();
							System.err.println("Erreur dans le message");
							System.err.println(e);
						}
						listMailNomDestinataire = new ArrayList<NotificationListAddress>();
					}

				}
				if (!vb.getCopyListPPNotif().isEmpty()) {
					Expdestexterne expDest;
					for (Pp pp : vb.getCopyListPPNotif()) {
						expDest = pp.getExpdestexterne();
						int i = 0;
						while (i < listInfo.size()) {
							if (listInfo.get(i).getVar().equals("#p")) {
								listInfo.get(i).setContenu(
										expDest.getExpDestExterneNom());
							}
							i++;
						}
						notificationNomDestinataire.setMailDestinataire(expDest
								.getExpDestExterneMail());
						notificationNomDestinataire.setNomDestinataire(expDest
								.getExpDestExternePrenom()
								+ " "
								+ expDest.getExpDestExterneNom());
						listMailNomDestinataire
								.add(notificationNomDestinataire);

						templateNotificationTexteDestinataire = templateNotificationDestinataire
								.getTemplateNotificationDescription();
						templateNotificationTexteDestinataire = changeVariableNotification(
								templateNotificationTexteDestinataire, info);
						System.out
								.println(templateNotificationTexteDestinataire);

						// Insertion d'un nouveau message (mail)
						messageDest = new Message();
						messageDest.setNotification(notificationDestinataire);
						messageDest.setIdUser(expDest.getIdExpDestExterne());
						messageDest
								.setMessageTexte(templateNotificationTexteDestinataire);
						try {
							appMgr.insert(messageDest);
						} catch (Exception e) {
							e.printStackTrace();
							System.err
									.println("Erreur dans le message mail Dest");
							System.err.println(e);
						}

						try {
							emailUtil.sendEmailSSL(SubjectNotification,
									templateNotificationTexteDestinataire,
									listMailNomDestinataire, nomExpediteur,
									mailExpediteur);
							System.out.println("Envoie effectué avec succes");
						} catch (NoSuchProviderException e) {
							e.printStackTrace();
							System.err
									.println("Pas de transport disponible pour ce protocole");
							System.err.println(e);
						} catch (AddressException e) {
							e.printStackTrace();
							System.err.println("Adresse invalide");
							System.err.println(e);
						} catch (MessagingException e) {
							e.printStackTrace();
							System.err.println("Erreur dans le message");
							System.err.println(e);
						}
						listMailNomDestinataire = new ArrayList<NotificationListAddress>();
					}
				}

			}

			if (evenementNomVariableLog != null) {
				InitialzeLog();
				vb.setLogType("INFO");
				templateLogTexte = templateLog.getTemplateLogVariable();
				System.out.println(templateLogTexte);
				templateLogTexte = changeVariableLog(templateLogTexte);
				System.out.println(templateLogTexte);

				URL u = getClass().getClassLoader().getResource(
						"xtensus/aop/log4j.xml");
				DOMConfigurator.configure(u);
				userLog = vb.getPerson().getPrenom() + " "
						+ vb.getPerson().getNom();
				dateLog = vb.getLogDate();
				typeLog = vb.getLogType();
				logger.info(templateLogTexte);

				// Insertion du trace dans la base de donnée
				// Trace trace = new Trace();
				// trace.setXtelog(log);
				// trace.setTypelog("INFO");
				// trace.setUserTexte(userLog);
				// trace.setDateTexte(logDate);
				// trace.setTraceTexte(templateLogTexte);

				try {
					// appMgr.insert(trace);
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("Erreur dans le Trace");
					System.err.println(e);
				}
			}

			System.out.println("logAfterReturning() is running!getSelectionRowJour");
			System.out.println("hijacked : "
					+ joinPoint.getSignature().getName());
			System.out.println("Method returned value is : " + result);
			System.out.println("******");

			nomExpediteur = null;
			mailExpediteur = null;
			evenementNomVariableNotif = null;
			evenementNomVariableLog = null;
			notificationNomVariablAdmin = null;
			notificationNomVariableDestinataire = null;

			vb.setNomExpediteur(null);
			vb.setMailExpediteur(null);
			vb.setEvenementNomVariableNotif(null);
			vb.setEvenementNomVariableLog(null);
			vb.setNotificationNomVariablAdmin(null);
			vb.setNotificationNomVariableDestinataire(null);
			vb.setCopyListSelectedPersonNotif(new ArrayList<Person>());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Aspect pour les beans GBO consult row ancien
	@AfterReturning(pointcut = "execution(* xtensus.beans.common.GBO.*.getSelectionRow(..))", returning = "result")
	public void aspectConsultCourrierJour(JoinPoint joinPoint, Object result) {
		// vbg.setNomClass(joinPoint.getSignature().getClass().getName());
		try {
			ExternalContext jsfContext = FacesContext.getCurrentInstance()
					.getExternalContext();
			ServletContext servletContext = (ServletContext) jsfContext
					.getContext();
			String webContentRoot = servletContext.getRealPath("/");
			String pathConfigFile = webContentRoot
					+ File.separator+"WEB-INF"+File.separator+"MailFax"+File.separator+"mailCentral.properties";
			Properties props = new Properties();
			props.load(new FileInputStream(pathConfigFile));
			vb.setMailExpediteur(props.getProperty("mail.session.user"));
			nomExpediteur = vb.getNomExpediteur();
			mailExpediteur = vb.getMailExpediteur();
			evenementNomVariableNotif = vb.getEvenementNomVariableNotif();
			evenementNomVariableLog = vb.getEvenementNomVariableLog();
			notificationNomVariablAdmin = vb.getNotificationNomVariablAdmin();
			notificationNomVariableDestinataire = vb
					.getNotificationNomVariableDestinataire();

			// Traitement si la libellé d'admin est remplit
			if (evenementNomVariableNotif != null
					&& notificationNomVariablAdmin != null) {

				int refUser = vb.getPerson().getId();

				listEvenementNotification = appMgr
						.listEvenementBylibelle(evenementNomVariableNotif);
				evenementNotification = listEvenementNotification.get(0);
				int refEvenement = evenementNotification.getEvenementId();
				listNotificationAdmin = appMgr
						.listNotificationByEvenementAndLibelle(refEvenement,
								notificationNomVariablAdmin);
				notificationAdmin = listNotificationAdmin.get(0);
				etatNotificationAdmin = notificationAdmin
						.getNotificationActivation();
				// int refNot = notificationAdmin.getNotificationId();
				// listUserNotification = appMgr
				// .listNotificationByUserAndNotification(refUser, refNot);
				// if (listUserNotification.size() != 0) {
				// userNotification = listUserNotification.get(0);
				// etatNotificationAdmin = userNotification.isEtat();
				// } else {
				// etatNotificationAdmin = false;
				// }
				// if (refNot != 0) {
				// etatNotificationAdmin = notificationAdmin
				// .getNotificationActivation();
				// }

			}

			// Traitement si la libelle de destinataire est remplit
			if (evenementNomVariableNotif != null
					&& notificationNomVariableDestinataire != null) {

				int refUser = vb.getPerson().getId();

				listEvenementNotification = appMgr
						.listEvenementBylibelle(evenementNomVariableNotif);
				evenementNotification = listEvenementNotification.get(0);
				int refEvenement = evenementNotification.getEvenementId();
				listNotificationDestinataire = appMgr
						.listNotificationByEvenementAndLibelle(refEvenement,
								notificationNomVariableDestinataire);
				notificationDestinataire = listNotificationDestinataire.get(0);
				etatNotificationDestinataire = notificationDestinataire
						.getNotificationActivation();
				// int refNot = notificationDestinataire.getNotificationId();
				// listUserNotification = appMgr
				// .listNotificationByUserAndNotification(refUser, refNot);
				// if (listUserNotification.size() != 0) {
				// userNotification = listUserNotification.get(0);
				// etatNotificationDestinataire = userNotification.isEtat();
				// } else {
				// etatNotificationDestinataire = false;
				// }
				// if (refNot != 0) {
				// etatNotificationAdmin = notificationDestinataire
				// .getNotificationActivation();
				// }

			}

			// Traitement de notification après verification de tt es reglé pr
			// notification admin
			if ((evenementNomVariableNotif != null
					&& notificationNomVariablAdmin != null && etatNotificationAdmin == true) // &&
																								// (etatNotificationAdminGeneral
																								// ==
																								// true)
			) {
				InitialzeNotificationAdmin();
				templateNotificationTexteAdmin = templateNotificationAdmin
						.getTemplateNotificationDescription();
				System.out.println(templateNotificationTexteAdmin);
				List<Informations> listInfo = new ArrayList<Informations>();
				listInfo = vb.getListInformations();
				int i = 0;
				while (i < listInfo.size()) {
					if (listInfo.get(i).getVar().equals("#p")) {
						listInfo.get(i).setContenu("Admin");
					}
					i++;
				}
				templateNotificationTexteAdmin = changeVariableNotification(
						templateNotificationTexteAdmin, info);

				// Insertion d'un nouveau message (mail)
				messageExp = new Message();
				messageExp.setNotification(notificationAdmin);
				messageExp.setIdUser(0);
				messageExp.setMessageTexte(templateNotificationTexteAdmin);
				try {
					appMgr.insert(messageExp);
				} catch (Exception e) {
					System.err
							.println("Erreur dans le message de la base de donnée admin");
					System.err.println(e);
				}
				String SubjectNotification = notificationAdmin
						.getNotificationLibelle();
				// String mailAdmin = "gbo@elmedina.tn";
				String mailAdmin = props.getProperty("mailFrom");
				String nomAdmin = "Admin";

				notificationNomDestinataire.setMailDestinataire(mailAdmin);
				notificationNomDestinataire.setNomDestinataire(nomAdmin);
				listMailNomDestinataire.add(notificationNomDestinataire);

				System.out.println(templateNotificationTexteAdmin);

				try {
					emailUtil.sendEmailSSL(SubjectNotification,
							templateNotificationTexteAdmin,
							listMailNomDestinataire, nomExpediteur,
							mailExpediteur);
					System.out.println("Envoie effectué avec succes");
				} catch (NoSuchProviderException e) {
					System.err
							.println("Pas de transport disponible pour ce protocole");
					System.err.println(e);
				} catch (AddressException e) {
					System.err.println("Adresse invalide");
					System.err.println(e);
				} catch (MessagingException e) {
					System.err.println("Erreur dans le message mail");
					System.err.println(e);
				}

			}

			// Traitement de notification après verification de tt es reglé pr
			// notification destinataire
			if ((evenementNomVariableNotif != null
					&& notificationNomVariableDestinataire != null && etatNotificationDestinataire == true) // &&
																											// (etatNotificationAdminGeneral
																											// ==
																											// true)
			) {
				InitialzeNotificationDestinataire();
				List<Informations> listInfo = new ArrayList<Informations>();
				listMailNomDestinataire = new ArrayList<NotificationListAddress>();
				listInfo = vb.getListInformations();
				templateNotificationTexteDestinataire = templateNotificationDestinataire
						.getTemplateNotificationDescription();
				System.out.println(templateNotificationTexteDestinataire);

				String SubjectNotification = notificationDestinataire
						.getNotificationLibelle();

				if (!vb.getCopyListSelectedPersonNotif().isEmpty()) {
					Person person1;
					for (Person person : vb.getCopyListSelectedPersonNotif()) {
						person1 = new Person();
						person1 = ldapOperation.getUserById(person.getId());
						int i = 0;
						while (i < listInfo.size()) {
							if (listInfo.get(i).getVar().equals("#p")) {
								listInfo.get(i).setContenu(person1.getNom());
							}
							i++;
						}
						notificationNomDestinataire.setMailDestinataire(person1
								.getEmail());
						notificationNomDestinataire.setNomDestinataire(person1
								.getPrenom() + " " + person1.getNom());
						listMailNomDestinataire
								.add(notificationNomDestinataire);

						templateNotificationTexteDestinataire = templateNotificationDestinataire
								.getTemplateNotificationDescription();
						templateNotificationTexteDestinataire = changeVariableNotification(
								templateNotificationTexteDestinataire, info);
						System.out
								.println(templateNotificationTexteDestinataire);

						// Insertion d'un nouveau message (mail)
						messageDest = new Message();
						messageDest.setNotification(notificationDestinataire);
						messageDest.setIdUser(person1.getId());
						messageDest
								.setMessageTexte(templateNotificationTexteDestinataire);
						try {
							appMgr.insert(messageDest);
						} catch (Exception e) {
							System.err
									.println("Erreur dans le message mail Dest");
							System.err.println(e);
						}

						try {
							emailUtil.sendEmailSSL(SubjectNotification,
									templateNotificationTexteDestinataire,
									listMailNomDestinataire, nomExpediteur,
									mailExpediteur);
							System.out.println("Envoie effectué avec succes");
						} catch (NoSuchProviderException e) {
							System.err
									.println("Pas de transport disponible pour ce protocole");
							System.err.println(e);
						} catch (AddressException e) {
							System.err.println("Adresse invalide");
							System.err.println(e);
						} catch (MessagingException e) {
							System.err.println("Erreur dans le message mail");
							System.err.println(e);
						}
						listMailNomDestinataire = new ArrayList<NotificationListAddress>();
					}
				}

				if (!vb.getCopyListSelectedUnitNotif().isEmpty()) {
					Person person;
					Unit unit1;
					for (Unit unit : vb.getCopyListSelectedUnitNotif()) {
						unit1 = new Unit();
						unit1 = ldapOperation.getUnitById(unit.getIdUnit());
						person = new Person();
						person = unit1.getResponsibleUnit();
						int i = 0;
						while (i < listInfo.size()) {
							if (listInfo.get(i).getVar().equals("#p")) {
								listInfo.get(i).setContenu(person.getNom());
							}
							i++;
						}
						notificationNomDestinataire.setMailDestinataire(person
								.getEmail());
						notificationNomDestinataire.setNomDestinataire(person
								.getPrenom() + " " + person.getNom());
						listMailNomDestinataire
								.add(notificationNomDestinataire);

						templateNotificationTexteDestinataire = templateNotificationDestinataire
								.getTemplateNotificationDescription();
						templateNotificationTexteDestinataire = changeVariableNotification(
								templateNotificationTexteDestinataire, info);
						System.out
								.println(templateNotificationTexteDestinataire);

						// Insertion d'un nouveau message (mail)
						messageDest = new Message();
						messageDest.setNotification(notificationDestinataire);
						messageDest.setIdUser(person.getId());
						messageDest
								.setMessageTexte(templateNotificationTexteDestinataire);
						try {
							appMgr.insert(messageDest);
						} catch (Exception e) {
							System.err
									.println("Erreur dans le message mail Dest");
							System.err.println(e);
						}

						try {
							emailUtil.sendEmailSSL(SubjectNotification,
									templateNotificationTexteDestinataire,
									listMailNomDestinataire, nomExpediteur,
									mailExpediteur);
							System.out.println("Envoie effectué avec succes");
						} catch (NoSuchProviderException e) {
							System.err
									.println("Pas de transport disponible pour ce protocole");
							System.err.println(e);
						} catch (AddressException e) {
							System.err.println("Adresse invalide");
							System.err.println(e);
						} catch (MessagingException e) {
							System.err.println("Erreur dans le message mail");
							System.err.println(e);
						}
						listMailNomDestinataire = new ArrayList<NotificationListAddress>();
					}
				}

				if (!vb.getCopyListPMNotif().isEmpty()) {
					Expdestexterne expDest;
					for (Pm pm : vb.getCopyListPMNotif()) {
						expDest = pm.getExpdestexterne();
						int i = 0;
						while (i < listInfo.size()) {
							if (listInfo.get(i).getVar().equals("#p")) {
								listInfo.get(i).setContenu(
										expDest.getExpDestExterneNom());
							}
							i++;
						}
						notificationNomDestinataire.setMailDestinataire(expDest
								.getExpDestExterneMail());
						notificationNomDestinataire.setNomDestinataire(expDest
								.getExpDestExternePrenom()
								+ " "
								+ expDest.getExpDestExterneNom());
						listMailNomDestinataire
								.add(notificationNomDestinataire);

						templateNotificationTexteDestinataire = templateNotificationDestinataire
								.getTemplateNotificationDescription();
						templateNotificationTexteDestinataire = changeVariableNotification(
								templateNotificationTexteDestinataire, info);
						System.out
								.println(templateNotificationTexteDestinataire);

						// Insertion d'un nouveau message (mail)
						messageDest = new Message();
						messageDest.setNotification(notificationDestinataire);
						messageDest.setIdUser(expDest.getIdExpDestExterne());
						messageDest
								.setMessageTexte(templateNotificationTexteDestinataire);
						try {
							appMgr.insert(messageDest);
						} catch (Exception e) {
							System.err
									.println("Erreur dans le message mail Dest");
							System.err.println(e);
						}

						try {
							emailUtil.sendEmailSSL(SubjectNotification,
									templateNotificationTexteDestinataire,
									listMailNomDestinataire, nomExpediteur,
									mailExpediteur);
							System.out.println("Envoie effectué avec succes");
						} catch (NoSuchProviderException e) {
							System.err
									.println("Pas de transport disponible pour ce protocole");
							System.err.println(e);
						} catch (AddressException e) {
							System.err.println("Adresse invalide");
							System.err.println(e);
						} catch (MessagingException e) {
							System.err.println("Erreur dans le message");
							System.err.println(e);
						}
						listMailNomDestinataire = new ArrayList<NotificationListAddress>();
					}

				}
				if (!vb.getCopyListPPNotif().isEmpty()) {
					Expdestexterne expDest;
					for (Pp pp : vb.getCopyListPPNotif()) {
						expDest = pp.getExpdestexterne();
						int i = 0;
						while (i < listInfo.size()) {
							if (listInfo.get(i).getVar().equals("#p")) {
								listInfo.get(i).setContenu(
										expDest.getExpDestExterneNom());
							}
							i++;
						}
						notificationNomDestinataire.setMailDestinataire(expDest
								.getExpDestExterneMail());
						notificationNomDestinataire.setNomDestinataire(expDest
								.getExpDestExternePrenom()
								+ " "
								+ expDest.getExpDestExterneNom());
						listMailNomDestinataire
								.add(notificationNomDestinataire);

						templateNotificationTexteDestinataire = templateNotificationDestinataire
								.getTemplateNotificationDescription();
						templateNotificationTexteDestinataire = changeVariableNotification(
								templateNotificationTexteDestinataire, info);
						System.out
								.println(templateNotificationTexteDestinataire);

						// Insertion d'un nouveau message (mail)
						messageDest = new Message();
						messageDest.setNotification(notificationDestinataire);
						messageDest.setIdUser(expDest.getIdExpDestExterne());
						messageDest
								.setMessageTexte(templateNotificationTexteDestinataire);
						try {
							appMgr.insert(messageDest);
						} catch (Exception e) {
							System.err
									.println("Erreur dans le message mail Dest");
							System.err.println(e);
						}

						try {
							emailUtil.sendEmailSSL(SubjectNotification,
									templateNotificationTexteDestinataire,
									listMailNomDestinataire, nomExpediteur,
									mailExpediteur);
							System.out.println("Envoie effectué avec succes");
						} catch (NoSuchProviderException e) {
							e.printStackTrace();
							System.err
									.println("Pas de transport disponible pour ce protocole");
							System.err.println(e);
						} catch (AddressException e) {
							System.err.println("Adresse invalide");
							System.err.println(e);
						} catch (MessagingException e) {
							System.err.println("Erreur dans le message");
							System.err.println(e);
						}
						listMailNomDestinataire = new ArrayList<NotificationListAddress>();
					}
				}

			}

			if (evenementNomVariableLog != null) {
				InitialzeLog();
				vb.setLogType("INFO");
				templateLogTexte = templateLog.getTemplateLogVariable();
				System.out.println(templateLogTexte);
				templateLogTexte = changeVariableLog(templateLogTexte);
				System.out.println(templateLogTexte);

				URL u = getClass().getClassLoader().getResource(
						"xtensus/aop/log4j.xml");
				DOMConfigurator.configure(u);
				userLog = vb.getPerson().getPrenom() + " "
						+ vb.getPerson().getNom();
				dateLog = vb.getLogDate();
				typeLog = vb.getLogType();
				logger.info(templateLogTexte);

				// Insertion du trace dans la base de donnée
				// Trace trace = new Trace();
				// trace.setXtelog(log);
				// trace.setTypelog("INFO");
				// trace.setUserTexte(userLog);
				// trace.setDateTexte(logDate);
				// trace.setTraceTexte(templateLogTexte);

				try {
					// appMgr.insert(trace);
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("Erreur dans le Trace");
					System.err.println(e);
				}
			}

			System.out.println("logAfterReturning() is running! ");
			System.out.println("hijacked : "
					+ joinPoint.getSignature().getName());
			System.out.println("Method returned value is : " + result);
			System.out.println("******");

			nomExpediteur = null;
			mailExpediteur = null;
			evenementNomVariableNotif = null;
			evenementNomVariableLog = null;
			notificationNomVariablAdmin = null;
			notificationNomVariableDestinataire = null;

			vb.setNomExpediteur(null);
			vb.setMailExpediteur(null);
			vb.setEvenementNomVariableNotif(null);
			vb.setEvenementNomVariableLog(null);
			vb.setNotificationNomVariablAdmin(null);
			vb.setNotificationNomVariableDestinataire(null);
			vb.setCopyListSelectedPersonNotif(new ArrayList<Person>());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*********** VariableNotificationbean **************/
	private List<VariablesNotification> variablesNotificationList;
	private List<VariablesLog> variablesLogList;
	private String typeEvenement;
	private String templateTexteFinal;
	private Date dateNotification;
	private Date datelog;
	private List<Informations> listInformation;
	private String logUser;
	private String logDate;
	private String logType;

	/*********** Methode **************/

	public String changeVariableNotification(String templateTexteOrigine,
			String info) {

		// typeObject = vb.getTypeObject();
		listInformation = vb.getListInformations();
		try {
			variablesNotificationList = appMgr
					.getList(VariablesNotification.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			System.err.println("Erreur dans le Trace");
			System.err.println(e1);
		}
		templateTexteFinal = templateTexteOrigine;

		for (Informations inf : listInformation) {
			System.out.println("inf.getVar() "+inf.getVar());
			System.out.println("inf.getContenu()  "+inf.getContenu());
			String tempInfoVar = inf.getVar();
			String tempInfoCont = inf.getContenu();
if(tempInfoCont==null)tempInfoCont="";
			for (VariablesNotification vn : variablesNotificationList) {
				String valvr = vn.getVariableValeur();
				String tmp;
				String txtReplaceValvr = tempInfoCont;

				int lgFindvar = valvr.length();
				int lgReplaceVar = txtReplaceValvr.length();

				for (int k = 0; k < (templateTexteFinal.length()); k++) {
					try {
						tmp = templateTexteFinal.substring(k, k + lgFindvar);
					} catch (Exception e) {
						break;
					}
					if (tmp.equalsIgnoreCase(valvr) && tmp.equals(tempInfoVar)) {
						templateTexteFinal = templateTexteFinal.substring(0, k)
								+ txtReplaceValvr
								+ templateTexteFinal.substring(k + lgFindvar,
										templateTexteFinal.length());
						k = k + lgReplaceVar;
					}
				}
			}
		}

		return templateTexteFinal;
	}

	// change variables de logs
	public String changeVariableLog(String templateTexteOrigine) {
		try {

			datelog = new Date();
			// typeObject = vb.getTypeObject();
			SimpleDateFormat formaterDate = null, formaterDateHeure = null;
			formaterDate = new SimpleDateFormat("dd/MM/yyyy");
			formaterDateHeure = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			try {
				variablesLogList = appMgr.getList(VariablesLog.class);
			} catch (Exception e1) {
				System.err.println("Erreur chargement");
				System.err.println(e1);
			}
			templateTexteFinal = templateTexteOrigine;

			for (VariablesLog vn : variablesLogList) {
				String valvr = vn.getVariableValeur();
				int lgFindvar = valvr.length();
				String tmp;

				String txtReplaceNom = vb.getPerson().getPrenom() + " "
						+ vb.getPerson().getNom();
				String txtReplaceDate = formaterDate.format(datelog);
				String txtReplaceDateHeure = formaterDateHeure.format(datelog);
				String txtReplaceEvenement = evenementLog.getEvenementLibelle();
				String txtReplaceNomClasse = vb.getNomClass();
				String txtReplaceTypeLog = vb.getTypeLog();
				logDate = txtReplaceDateHeure;

				int lgReplaceNom = txtReplaceNom.length();
				int lgReplaceDate = txtReplaceDate.length();
				int lgReplaceDateHeure = txtReplaceDateHeure.length();
				int lgReplaceEvenement = txtReplaceEvenement.length();
				int lgReplaceNomClasse = txtReplaceNomClasse.length();
				int lgReplaceTypeLog = txtReplaceTypeLog.length();

				for (int k = 0; k < (templateTexteFinal.length()); k++) {
					try {
						tmp = templateTexteFinal.substring(k, k + lgFindvar);
					} catch (Exception e) {
						break;
					}
					if (tmp.equalsIgnoreCase(valvr) && tmp.equals("#Nom")) {
						templateTexteFinal = templateTexteFinal.substring(0, k)
								+ txtReplaceNom
								+ templateTexteFinal.substring(k + lgFindvar,
										templateTexteFinal.length());
						k = k + lgReplaceNom;
						logUser = txtReplaceNom;
					}
					if (tmp.equalsIgnoreCase(valvr)
							&& tmp.equals("#DateSystem")) {
						templateTexteFinal = templateTexteFinal.substring(0, k)
								+ txtReplaceDate
								+ templateTexteFinal.substring(k + lgFindvar,
										templateTexteFinal.length());
						k = k + lgReplaceDate;
						// logDate = txtReplaceDate;
					}
					if (tmp.equalsIgnoreCase(valvr)
							&& tmp.equals("#Date/Heure")) {
						templateTexteFinal = templateTexteFinal.substring(0, k)
								+ txtReplaceDateHeure
								+ templateTexteFinal.substring(k + lgFindvar,
										templateTexteFinal.length());
						k = k + lgReplaceDateHeure;
						// logDate = txtReplaceDate;
					}
					if (tmp.equalsIgnoreCase(valvr) && tmp.equals("#Evenement")) {
						templateTexteFinal = templateTexteFinal.substring(0, k)
								+ txtReplaceEvenement
								+ templateTexteFinal.substring(k + lgFindvar,
										templateTexteFinal.length());
						k = k + lgReplaceEvenement;
					}
					if (tmp.equalsIgnoreCase(valvr) && tmp.equals("#Nom_class")) {
						templateTexteFinal = templateTexteFinal.substring(0, k)
								+ txtReplaceNomClasse
								+ templateTexteFinal.substring(k + lgFindvar,
										templateTexteFinal.length());
						k = k + lgReplaceNomClasse;
					}
					if (tmp.equalsIgnoreCase(valvr) && tmp.equals("#Type_Log")) {
						templateTexteFinal = templateTexteFinal.substring(0, k)
								+ txtReplaceTypeLog
								+ templateTexteFinal.substring(k + lgFindvar,
										templateTexteFinal.length());
						k = k + lgReplaceTypeLog;
						logType = txtReplaceTypeLog;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return templateTexteFinal;
	}

	/*********** Getters && setteres ********************/
	public ApplicationManager getAppMgr() {
		return appMgr;
	}

	public void setAppMgr(ApplicationManager appMgr) {
		this.appMgr = appMgr;
	}

	public Object getTypeObject() {
		return typeObject;
	}

	public void setTypeObject(Object typeObject) {
		this.typeObject = typeObject;
	}

	public void setVariableNotification(
			VariablesNotification variableNotification) {
		this.variableNotification = variableNotification;
	}

	public VariablesNotification getVariableNotification() {
		return variableNotification;
	}

	public void setListVariableNotificationRef(
			List<NotificationVariable> listVariableNotificationRef) {
		this.listVariableNotificationRef = listVariableNotificationRef;
	}

	public List<NotificationVariable> getListVariableNotificationRef() {
		return listVariableNotificationRef;
	}

	public void setListVariableNotification(
			List<VariablesNotification> listVariableNotification) {
		this.listVariableNotification = listVariableNotification;
	}

	public List<VariablesNotification> getListVariableNotification() {
		return listVariableNotification;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public void setListEvenementNotification(
			List<Evenement> listEvenementNotification) {
		this.listEvenementNotification = listEvenementNotification;
	}

	public List<Evenement> getListEvenementNotification() {
		return listEvenementNotification;
	}

	public void setEvenementNotification(Evenement evenementNotification) {
		this.evenementNotification = evenementNotification;
	}

	public Evenement getEvenementNotification() {
		return evenementNotification;
	}

	public void setListEvenementLog(List<Evenement> listEvenementLog) {
		this.listEvenementLog = listEvenementLog;
	}

	public List<Evenement> getListEvenementLog() {
		return listEvenementLog;
	}

	public void setEvenementLog(Evenement evenementLog) {
		this.evenementLog = evenementLog;
	}

	public Evenement getEvenementLog() {
		return evenementLog;
	}

	public void setListTemplateLog(List<Templatelog> listTemplateLog) {
		this.listTemplateLog = listTemplateLog;
	}

	public List<Templatelog> getListTemplateLog() {
		return listTemplateLog;
	}

	public void setTemplateLog(Templatelog templateLog) {
		this.templateLog = templateLog;
	}

	public Templatelog getTemplateLog() {
		return templateLog;
	}

	public void setListLogs(List<Xtelog> listLogs) {
		this.listLogs = listLogs;
	}

	public List<Xtelog> getListLogs() {
		return listLogs;
	}

	public void setLog(Xtelog log) {
		this.log = log;
	}

	public Xtelog getLog() {
		return log;
	}

	public String getTemplateLogTexte() {
		return templateLogTexte;
	}

	public void setTemplateLogTexte(String templateLogTexte) {
		this.templateLogTexte = templateLogTexte;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		TracingAfterReturningAdvice.logger = logger;
	}

	public void setUserLog(String userLog) {
		this.userLog = userLog;
	}

	public String getUserLog() {
		return userLog;
	}

	public void setDateLog(String dateLog) {
		this.dateLog = dateLog;
	}

	public String getDateLog() {
		return dateLog;
	}

	public void setTypeLog(String typeLog) {
		this.typeLog = typeLog;
	}

	public String getTypeLog() {
		return typeLog;
	}

	public void setUserNotification(UserNotification userNotification) {
		this.userNotification = userNotification;
	}

	public UserNotification getUserNotification() {
		return userNotification;
	}

	public void setListUserNotification(
			List<UserNotification> listUserNotification) {
		this.listUserNotification = listUserNotification;
	}

	public List<UserNotification> getListUserNotification() {
		return listUserNotification;
	}

	public void setListNotificationAdmin(
			List<Notification> listNotificationAdmin) {
		this.listNotificationAdmin = listNotificationAdmin;
	}

	public List<Notification> getListNotificationAdmin() {
		return listNotificationAdmin;
	}

	public void setListNotificationDestinataire(
			List<Notification> listNotificationDestinataire) {
		this.listNotificationDestinataire = listNotificationDestinataire;
	}

	public List<Notification> getListNotificationDestinataire() {
		return listNotificationDestinataire;
	}

	public void setNotificationAdmin(Notification notificationAdmin) {
		this.notificationAdmin = notificationAdmin;
	}

	public Notification getNotificationAdmin() {
		return notificationAdmin;
	}

	public void setNotificationDestinataire(
			Notification notificationDestinataire) {
		this.notificationDestinataire = notificationDestinataire;
	}

	public Notification getNotificationDestinataire() {
		return notificationDestinataire;
	}

	public void setListTemplateNotificationAdmin(
			List<Templatenotification> listTemplateNotificationAdmin) {
		this.listTemplateNotificationAdmin = listTemplateNotificationAdmin;
	}

	public List<Templatenotification> getListTemplateNotificationAdmin() {
		return listTemplateNotificationAdmin;
	}

	public void setTemplateNotificationAdmin(
			Templatenotification templateNotificationAdmin) {
		this.templateNotificationAdmin = templateNotificationAdmin;
	}

	public Templatenotification getTemplateNotificationAdmin() {
		return templateNotificationAdmin;
	}

	public void setListTemplateNotificationDestinataire(
			List<Templatenotification> listTemplateNotificationDestinataire) {
		this.listTemplateNotificationDestinataire = listTemplateNotificationDestinataire;
	}

	public List<Templatenotification> getListTemplateNotificationDestinataire() {
		return listTemplateNotificationDestinataire;
	}

	public void setTemplateNotificationDestinataire(
			Templatenotification templateNotificationDestinataire) {
		this.templateNotificationDestinataire = templateNotificationDestinataire;
	}

	public Templatenotification getTemplateNotificationDestinataire() {
		return templateNotificationDestinataire;
	}

	public void setTemplateNotificationTexteAdmin(
			String templateNotificationTexteAdmin) {
		this.templateNotificationTexteAdmin = templateNotificationTexteAdmin;
	}

	public String getTemplateNotificationTexteAdmin() {
		return templateNotificationTexteAdmin;
	}

	public void setTemplateNotificationTexteDestinataire(
			String templateNotificationTexteDestinataire) {
		this.templateNotificationTexteDestinataire = templateNotificationTexteDestinataire;
	}

	public String getTemplateNotificationTexteDestinataire() {
		return templateNotificationTexteDestinataire;
	}

	public void setEtatNotificationAdmin(boolean etatNotificationAdmin) {
		this.etatNotificationAdmin = etatNotificationAdmin;
	}

	public boolean isEtatNotificationAdmin() {
		return etatNotificationAdmin;
	}

	public void setEtatNotificationDestinataire(
			boolean etatNotificationDestinataire) {
		this.etatNotificationDestinataire = etatNotificationDestinataire;
	}

	public boolean isEtatNotificationDestinataire() {
		return etatNotificationDestinataire;
	}

	public void setLdapOperation(LdapOperation ldapOperation) {
		this.ldapOperation = ldapOperation;
	}

	public LdapOperation getLdapOperation() {
		return ldapOperation;
	}

	public VariableGlobaleNotification getVb() {
		return vb;
	}

	public void setVb(VariableGlobaleNotification vb) {
		this.vb = vb;
	}

	public void setMessageExp(Message messageExp) {
		this.messageExp = messageExp;
	}

	public Message getMessageExp() {
		return messageExp;
	}

	public void setMessageDest(Message messageDest) {
		this.messageDest = messageDest;
	}

	public Message getMessageDest() {
		return messageDest;
	}

	public List<VariablesNotification> getVariablesNotificationList() {
		return variablesNotificationList;
	}

	public void setVariablesNotificationList(
			List<VariablesNotification> variablesNotificationList) {
		this.variablesNotificationList = variablesNotificationList;
	}

	public List<VariablesLog> getVariablesLogList() {
		return variablesLogList;
	}

	public void setVariablesLogList(List<VariablesLog> variablesLogList) {
		this.variablesLogList = variablesLogList;
	}

	public String getTypeEvenement() {
		return typeEvenement;
	}

	public void setTypeEvenement(String typeEvenement) {
		this.typeEvenement = typeEvenement;
	}

	public String getTemplateTexteFinal() {
		return templateTexteFinal;
	}

	public void setTemplateTexteFinal(String templateTexteFinal) {
		this.templateTexteFinal = templateTexteFinal;
	}

	public Date getDateNotification() {
		return dateNotification;
	}

	public void setDateNotification(Date dateNotification) {
		this.dateNotification = dateNotification;
	}

	public Date getDatelog() {
		return datelog;
	}

	public void setDatelog(Date datelog) {
		this.datelog = datelog;
	}

	public List<Informations> getListInformation() {
		return listInformation;
	}

	public void setListInformation(List<Informations> listInformation) {
		this.listInformation = listInformation;
	}

	public String getLogUser() {
		return logUser;
	}

	public void setLogUser(String logUser) {
		this.logUser = logUser;
	}

	public String getLogDate() {
		return logDate;
	}

	public void setLogDate(String logDate) {
		this.logDate = logDate;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public void setEmailUtil(EmailUtil emailUtil) {
		this.emailUtil = emailUtil;
	}

	public EmailUtil getEmailUtil() {
		return emailUtil;
	}

	public void setEvenementNomVariableNotif(String evenementNomVariableNotif) {
		this.evenementNomVariableNotif = evenementNomVariableNotif;
	}

	public String getEvenementNomVariableNotif() {
		return evenementNomVariableNotif;
	}

	public void setEvenementNomVariableLog(String evenementNomVariableLog) {
		this.evenementNomVariableLog = evenementNomVariableLog;
	}

	public String getEvenementNomVariableLog() {
		return evenementNomVariableLog;
	}

	public void setNotificationNomVariablAdmin(
			String notificationNomVariablAdmin) {
		this.notificationNomVariablAdmin = notificationNomVariablAdmin;
	}

	public String getNotificationNomVariablAdmin() {
		return notificationNomVariablAdmin;
	}

	public void setNotificationNomVariableDestinataire(
			String notificationNomVariableDestinataire) {
		this.notificationNomVariableDestinataire = notificationNomVariableDestinataire;
	}

	public String getNotificationNomVariableDestinataire() {
		return notificationNomVariableDestinataire;
	}

	public void setNomExpediteur(String nomExpediteur) {
		this.nomExpediteur = nomExpediteur;
	}

	public String getNomExpediteur() {
		return nomExpediteur;
	}

	public void setMailExpediteur(String mailExpediteur) {
		this.mailExpediteur = mailExpediteur;
	}

	public String getMailExpediteur() {
		return mailExpediteur;
	}

	public void setListMailNomDestinataire(
			List<NotificationListAddress> listMailNomDestinataire) {
		this.listMailNomDestinataire = listMailNomDestinataire;
	}

	public List<NotificationListAddress> getListMailNomDestinataire() {
		return listMailNomDestinataire;
	}

	public void setNotificationNomDestinataire(
			NotificationListAddress notificationNomDestinataire) {
		this.notificationNomDestinataire = notificationNomDestinataire;
	}

	public NotificationListAddress getNotificationNomDestinataire() {
		return notificationNomDestinataire;
	}

	public void setNotificationNomVariableAdminPrincipal(
			String notificationNomVariableAdminPrincipal) {
		this.notificationNomVariableAdminPrincipal = notificationNomVariableAdminPrincipal;
	}

	public String getNotificationNomVariableAdminPrincipal() {
		return notificationNomVariableAdminPrincipal;
	}

	public void setEtatNotificationAdminGeneral(
			boolean etatNotificationAdminGeneral) {
		this.etatNotificationAdminGeneral = etatNotificationAdminGeneral;
	}

	public boolean isEtatNotificationAdminGeneral() {
		return etatNotificationAdminGeneral;
	}
}