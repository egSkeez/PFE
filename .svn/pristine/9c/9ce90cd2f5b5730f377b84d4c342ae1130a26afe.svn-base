package xtensus.beans.common.GBO;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Binding;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.richfaces.component.UIToolBar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.CannotCreateTransactionException;

import xtensus.aop.LogClass;
import xtensus.aop.TracingAfterReturningAdvice;
import xtensus.beans.common.ConnexionNotificationUtil;
import xtensus.beans.common.GMAILAuthenticator;
import xtensus.beans.common.LanguageManagerBean;
import xtensus.beans.common.VariableGlobale;
import xtensus.beans.common.VariableGlobaleNotification;
import xtensus.entity.Courrier;
import xtensus.entity.Variables;
import xtensus.gnl.entity.Evenement;
import xtensus.gnl.entity.Templatelog;
import xtensus.gnl.entity.VariablesNotification;
import xtensus.gnl.entity.Xtelog;
import xtensus.ldap.business.LdapFunction;
import xtensus.ldap.business.LdapOperation;
import xtensus.ldap.model.Person;
import xtensus.ldap.model.Unit;
import xtensus.ldap.utils.CustomUserDetails;
import xtensus.services.ApplicationManager;

@Component
@Scope("session")
public class LoginBean {

	private String passwordConfirm;
	private String passwordNew;
	private String myErrorMessageModification = "";
	private String myErrorMessage = "";
	private List<Person> listUsers;
	private boolean roleUser;
	@Autowired
	private VariableGlobale vb;
	@Autowired
	private VariableGlobaleNotification vbn;
	private ApplicationManager appMgr;
	private UIToolBar toolBar = null;
	@Autowired
	private LanguageManagerBean lm;
	@Autowired
	private MessageSource messageSource;
	private String message;
	@Autowired
	private ApplicationContext appCtx;
	@Autowired
    private UserAgentProcessor userAgentProcessor;
	private boolean variableUserAgent;
	private LdapOperation ldapOperation;
	private boolean status1 = false;
	private boolean status2 = false;
	private boolean status = false;
	private String username;
	private String nouveauMotDePasse;

	private boolean refuse;
	private boolean combInc;

	@Autowired
	public LoginBean(@Qualifier("applicationManager") ApplicationManager appMgr) {
		this.appMgr = appMgr;

	}

	public LoginBean() {

	}

	@PostConstruct
	public void initialize() {
		try {
			
			// C'est pour test de la connexion à la BD 
			//Test si nous avons la gestion des Natures par catégorie
			appMgr.getList(Variables.class);
			
			
			if(userAgentProcessor.isPhone())
			{
				variableUserAgent=true;
			}else{
				variableUserAgent=false;
			}
			combInc = true;

			myErrorMessage = "";
			myErrorMessageModification = "";
			System.out.println("BEGIN");
			long deb = System.currentTimeMillis();
			final LdapFunction ldapFunction = new LdapFunction();
			ldapFunction.getDataFromDirectory();
			vb.setLocale("fr_FR");
			vb.setCopyLdapListAllUser(ldapFunction.ldapListAllUser);
			vb.setCopyLdapListUser(ldapFunction.ldapListUser);

			vb.setNewUserId(ldapFunction.newUserId);
			vb.setNewGroupId(ldapFunction.newGroupId);
			vb.setCentralBoc(ldapFunction.centralBoc);
			vb.setListTousBos(ldapFunction.listTousBos);
			vb.setListTousUnit(ldapFunction.listTousUnit);
			vb.getCentralBoc().getListChildBOCsBOC();

			Person person = new Person();
			List<Person> listotherusers = new ArrayList<Person>();
			for (int i = 0; i < vb.getCopyLdapListAllUser().size(); i++) {
				person = vb.getCopyLdapListAllUser().get(i);
				int j = 0;
				boolean findUser = false;
				do {
					if (vb.getCopyLdapListUser().get(j).getId() == person
							.getId()) {
						findUser = true;
					} else {
						j++;
					}
				} while (!findUser && j < vb.getCopyLdapListUser().size());
				if (!findUser) {
					listotherusers.add(person);
				}
			}
			//Test si nous avons la gestion des Natures par catégorie
			List<Variables> l = appMgr
					.listVariablesByLibelle("gestion_nature_par_categorie");
			if (l != null && l.size() > 0) {
				if (((l.get(0)).getVaraiablesValeur()).toUpperCase().equals("OUI"))
					vb.setCategorieParNature(true);
				else
					vb.setCategorieParNature(false);
			}

			vb.setCopyLdapListOtherUser(listotherusers);
			vb.setCopyLdapListGroup(ldapFunction.ldapListGroup);
			vb.setCopyLdapListUnit(ldapFunction.ldapListUnit);
			vb.setHashMapAllUser(ldapFunction.hashMapAllUser);
			vb.setHashMapUnit(ldapFunction.hashMapUnit);
			HttpServletRequest request = (HttpServletRequest) FacesContext
					.getCurrentInstance().getExternalContext().getRequest();
			String p = request.getParameter("user");
			System.out.println("+++++++++++++++++");
			System.out.println(p);
			// System.out.println(decrypt(p));
			System.out.println("+++++++++++++++++");
			com.mchange.v2.c3p0.ComboPooledDataSource cpds = appCtx
					.getBean(com.mchange.v2.c3p0.ComboPooledDataSource.class);
			vb.setDbType(cpds.getDriverClass().toLowerCase());
			long fin = System.currentTimeMillis();
			System.out.println("Duration : " + (fin - deb));
			// for (Entry entryUser : vb.getHashMapAllUser().entrySet()) {
			// System.out.println("ID : " + entryUser.getKey() + " Name : " +
			// ((Person) entryUser.getValue()).getCn() );
			// }
			// System.out.println("########################################");
			// for (Entry entryUnit : vb.getHashMapUnit().entrySet()) {
			// System.out.println("ID : " + entryUnit.getKey() + " NameUNIT : "
			// + ((Unit) entryUnit.getValue()).getNameUnit() );
			// }
		} 
		
		catch(CannotCreateTransactionException e1){
			System.out.println("Erreur de connexion au serveur BD");
			
			FacesContext.getCurrentInstance().addMessage(
					"messages",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, messageSource
							.getMessage("errorCodeBDConnexion",
									new Object[] {}, lm.createLocal()), ""));
		}
		catch (Exception e) {
			System.out.println("Erreur de connexion au serveur LDAP");
			FacesContext.getCurrentInstance().addMessage(
					"messages",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, messageSource
							.getMessage("errorCodeLdapConnexion",
									new Object[] {}, lm.createLocal()), ""));
			e.printStackTrace();
		}

		System.out.println();
	}

	// **********************cryptage et decryptage**********

	public String crypt(String ch) throws UnsupportedEncodingException {

		byte[] bytes = ch.getBytes("US-ASCII");
		return bytes.toString();
	}

	public String decrypt(String ch) throws UnsupportedEncodingException {

		byte[] bytes = ch.getBytes("US-ASCII");
		String s = new String(bytes);
		return s;
	}

	// ****************fin ****************************

	public void update() {
		boolean status = false;

		if (passwordConfirm == passwordNew) {
			myErrorMessageModification = "1";
			status1 = true;

		} else {
			myErrorMessageModification = "Succés de modification de mot de passe";
			status1 = false;

		}

	}

	

	public void recupPassword() {

		initialize();
		String email = null;
		String password = null;
		boolean rslt = false;
		String personne;
		int personneId;
		try {

			for (int i = 0; i <= vb.getCopyLdapListAllUser().size(); i++) {
				System.out.println("entrer");
				if (vb.getCopyLdapListAllUser().get(i).getLogin()
						.equals(getUsername())) {
					System.out.println("email************** :"
							+ vb.getCopyLdapListAllUser().get(i).getEmail());
					email = vb.getCopyLdapListAllUser().get(i).getEmail();
					password = vb.getCopyLdapListAllUser().get(i).getPwd();
					personne = vb.getCopyLdapListAllUser().get(i).getLogin();
					personneId = vb.getCopyLdapListAllUser().get(i).getId();
					System.out.println(personneId + "---" + personne + "----"
							+ password);
					rslt = true;
					status = true;
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			if (rslt) {

				status1 = true;
				myErrorMessage = "1";

				// --------------------------------------------------------------------------------------------------------------------------------------------------
				// Recupération de l'Id de l'utilisateur
				// --------------------------------------------------------------------------------------------------------------------------------------------------
				System.out.println("l'Id de l'utilisateur est:"
						+ vb.getPerson().getId());
				// --------------------------------------------------------------------------------------------------------------------------------------------------
				// --------------------------------------------------------------------------------------------------------------------------------------------------

				System.out.println("email du client est :" + email);
				System.out.println("password du client est :" + password);
				System.out.println("resultat :" + rslt);
				System.out.println("*****lien avant cryptage*********");
				System.out
						.println("http://localhost:8082/GBO_2013/Ilogin/ModificationMotDePasse.xhtml?user="
								+ getUsername());
				System.out.println("*****lien apres cryptage*********");
				System.out
						.println("++++http://localhost:8082/GBO_2013/Ilogin/ModificationMotDePasse.xhtml?user="
								+ crypt(getUsername()));
				System.out.println("*****lien apres decryptage*********");
				System.out
						.println("----http://localhost:8082/GBO_2013/Ilogin/ModificationMotDePasse.xhtml?user="
								+ decrypt(getUsername()));
				String l = "http://localhost:8082/GBO_2013/Ilogin/ModificationMotDePasse.xhtml?user="
						+ crypt(getUsername());
				sendEmailRecuperation(email, l);

			} else {
				myErrorMessage = "2";
				status1 = false;
				System.out.println("not exist ");

			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		setUsername("");

	}

	public boolean checkCombinaison(int idUser) {
		int id;
		boolean findPerson = false;
		int j = 0;
		do {
			id = vb.getCopyLdapListUser().get(j).getId();
			if (id == idUser) {
				findPerson = true;
				vb.setPerson(vb.getCopyLdapListUser().get(j));
				vbn.setPerson(vb.getCopyLdapListUser().get(j));
			} else {
				j++;
			}
		} while (!findPerson && j < vb.getCopyLdapListUser().size());
		System.out.println("#Connected Person : " + vb.getPerson());
//		if(vb.getPerson().getAssociatedBOC()!= null ){
//			vb.getPerson().setBoc(true);
			
	//	}
		return findPerson;
	}

	// *************************
	// ************EMAIL RECUPERATION PASSWORD***********
	public void sendEmailRecuperation(String email, String lien) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "mail.xtensus.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "25");

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(
								"gbo.mail@xtensus.net", "ADFHF632SM");
					}
				});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("gbo.mail@xtensus.net"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(email));
			message.setSubject("Recuperation password");
			message.setText("Bonjour,"
					+ "\n\n Pour recuperer votre mot de passe vous devez suivre le lien ci dessous :\n\n"
					+ lien);

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	// ****************************

	public String check() {
		int exp = 0;
		try {
			System.out
					.println("---------------------check--------------------");
			exp = (int) appMgr.getRowCount();
			// exp=appMgr.getList(Courrier.class).size();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(exp);
		int seuil = 0;
		List<VariablesNotification> listVariableByRef = appMgr
				.listVariableByRef(10);
		if (listVariableByRef != null && listVariableByRef.size() > 0) {
			seuil = Integer.parseInt(listVariableByRef.get(0)
					.getVariableValeur());
		}
		// if (exp <= seuil) {
		doLogin();
		System.out.println("accept");
		System.out.println("19856372");
		return "accepted";
		// } else {
		// System.out.println("198563726532");
		// checkIndexDataBase();
		// System.out.println("refuse");
		//
		// // refuse=true;
		// combInc = false;
		// return null;
		// }

	}

	public void checkIndexDataBase() {

	}

	public void doLogin() {

		try {
			boolean findPerson = false;
			ExternalContext context = FacesContext.getCurrentInstance()
					.getExternalContext();

			RequestDispatcher dispatcher = ((ServletRequest) context.getRequest())
					.getRequestDispatcher("/j_spring_security_check");

			dispatcher.forward((ServletRequest) context.getRequest(),
					(ServletResponse) context.getResponse());
			Object principal = SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			// String login = "";
			// String pwd = "";
			int id;
			if (principal instanceof CustomUserDetails) {
				// login = ((CustomUserDetails) principal).getUsername();
				// pwd = ((CustomUserDetails) principal).getPassword();
				// pwd = ((CustomUserDetails) principal).getEmail();
				// test pour les utilisateurs qui n'ont pas des roles sur
				// l'application
				if (((CustomUserDetails) principal).getAuthorities().isEmpty()) {

					ExternalContext externalContext = FacesContext
							.getCurrentInstance().getExternalContext();
					externalContext.redirect(externalContext
							.getRequestContextPath()
							+ "/Ilogin/notAutorizedToConnect.xhtml");
				} else {

					id = ((CustomUserDetails) principal).getId();
					findPerson = checkCombinaison(id);
					Runnable notif = new Runnable() {
						@Override
						public void run() {
							try {
								ConnexionNotificationUtil cf = new ConnexionNotificationUtil();
								cf.sendEmailAndLog(vb.getPerson(), appMgr);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					};
					new Thread(notif).start();
					// try {
					// ConnexionNotificationUtil cf = new
					// ConnexionNotificationUtil();
					// cf.sendEmailAndLog(vb.getPerson(), appMgr);
					// } catch (Exception e) {
					// e.printStackTrace();
					// }

					Collection<GrantedAuthority> authorities = ((CustomUserDetails) principal)
							.getAuthorities();
					for (GrantedAuthority grantedAuthority : authorities) {
						vb.getRole().add(grantedAuthority.getAuthority());
					}

					for (String role : vb.getRole()) {
						System.out.println("*** : " + role);
					}
					// ----------------------- KHA : ROLE_BORDEREAU_ENVOI :
					// ajouté le 15-03-2019
					if (vb.getRole().contains("ROLE_ROLE_BORDEREAU_ENVOI")) {
						System.out.println("il a le rôle de B. env");
						vb.setRoleBordereau(true);
					}
					if (vb.getRole().contains("ROLE_ROLE_NOTE_TRANSMISSION")) {
						System.out.println("il a le rôle de B. env");
						vb.setRoleNoteTransmission(true);
					}
					// -----------------------Fin KHA
					
				
					
				}

				if (FacesContext.getCurrentInstance().getExternalContext()
						.isUserInRole("ROLE_USER")) {
					vb.setRoleUser(true);
					roleUser = true;
				}
				System.out
						.println("----------------------- KHA a l'acces pour editer le Bordereau:"
								+ vb.isRoleBordereau());
			}
			FacesContext.getCurrentInstance().getResponseComplete();

		} catch (ServletException e) {
			System.out.println("Erreur de servlet !!!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Erreur de In/Out !!!");
			e.printStackTrace();
		} catch (NullPointerException e) {
			ConnexionNotificationUtil cf = new ConnexionNotificationUtil();
			// try {
			// cf.sendLogConnexion(appMgr);
			// } catch (AddressException e1) {
			// e1.printStackTrace();
			// } catch (MessagingException e1) {
			// e1.printStackTrace();
			// }
		}
	}

	/****** Getters && Setters ************/
	public void setVb(VariableGlobale vb) {
		this.vb = vb;
	}

	public VariableGlobale getVb() {
		return vb;
	}

	public List<Person> getlistUsers() {
		return listUsers;
	}

	public void setListUsers(List<Person> listUsers) {
		this.listUsers = listUsers;
	}

	public void setRoleUser(boolean roleUser) {
		this.roleUser = roleUser;
	}

	public boolean isRoleUser() {
		return roleUser;
	}

	public void setToolBar(UIToolBar toolBar) {
		this.toolBar = toolBar;
	}

	public UIToolBar getToolBar() {
		return toolBar;
	}

	public void setAppMgr(ApplicationManager appMgr) {
		this.appMgr = appMgr;
	}

	public ApplicationManager getAppMgr() {
		return appMgr;
	}

	public void setVbn(VariableGlobaleNotification vbn) {
		this.vbn = vbn;
	}

	public VariableGlobaleNotification getVbn() {
		return vbn;
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

	public ApplicationContext getAppCtx() {
		return appCtx;
	}

	public void setAppCtx(ApplicationContext appCtx) {
		this.appCtx = appCtx;
	}

	public LdapOperation getLdapOperation() {
		return ldapOperation;
	}

	public void setLdapOperation(LdapOperation ldapOperation) {
		this.ldapOperation = ldapOperation;
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

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Person> getListUsers() {
		return listUsers;
	}

	public String getNouveauMotDePasse() {
		return nouveauMotDePasse;
	}

	public void setNouveauMotDePasse(String nouveauMotDePasse) {
		this.nouveauMotDePasse = nouveauMotDePasse;
	}

	public String getMyErrorMessage() {
		return myErrorMessage;
	}

	public void setMyErrorMessage(String myErrorMessage) {
		this.myErrorMessage = myErrorMessage;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public String getPasswordNew() {
		return passwordNew;
	}

	public void setPasswordNew(String passwordNew) {
		this.passwordNew = passwordNew;
	}

	public String getMyErrorMessageModification() {
		return myErrorMessageModification;
	}

	public void setMyErrorMessageModification(String myErrorMessageModification) {
		this.myErrorMessageModification = myErrorMessageModification;
	}

	public void setRefuse(boolean refuse) {
		this.refuse = refuse;
	}

	public boolean getRefuse() {
		return refuse;
	}

	public void setCombInc(boolean combInc) {
		this.combInc = combInc;
	}

	public boolean isCombInc() {
		return combInc;
	}

	public boolean isVariableUserAgent() {
		return variableUserAgent;
	}

	public void setVariableUserAgent(boolean variableUserAgent) {
		this.variableUserAgent = variableUserAgent;
	}

	
}
