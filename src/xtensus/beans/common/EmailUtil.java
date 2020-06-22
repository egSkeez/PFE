package xtensus.beans.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import xtensus.beans.utils.NotificationListAddress;
import xtensus.dao.utils.DMSConnexionImplement;
import xtensus.entity.Document;
import xtensus.entity.Expdest;
//import xtensus.faxmail.utils.InputStreamDataSource;
import xtensus.ldap.model.Person;
import xtensus.ldap.model.Unit;

public class EmailUtil {
	private static Properties prop = new Properties();

	/**
	 * Envoi d'un email utilisant une socket SSL (SSLSocketFactory).
	 * 
	 * @param to
	 *            celui ou ceux qui doivent recevoir l'email (séparation des
	 *            adresses par des virgules)
	 * @param subject
	 *            sujet de l'email
	 * @param content
	 *            contenu de l'email
	 * @throws AddressException
	 *             les adresses de destinations sont incorrectes
	 * @throws MessagingException
	 *             une erreur est survenue é l'envoi de l'email
	 */
	public EmailUtil() {

	}

	public static boolean checkConnection() {
		
		try {
			Resource rsrc = new ClassPathResource("../MailFax/mail.properties");
			String pathConfigFile = rsrc.getFile().getAbsolutePath();
			try {
				getProp().load(new FileInputStream(pathConfigFile));
			} catch (FileNotFoundException e) {
				System.err.println("Pas de fichier");
				e.printStackTrace();
			} catch (IOException e) {
				System.err.println("Erreur");
				e.printStackTrace();
			}
			// smtp properties
			Properties props = new Properties();
			// SUBSTITUTE YOUR ISP'S MAIL SERVER HERE
			String host = getProp().getProperty("mail.smtp.host");
			String user = getProp().getProperty("mail.session.user");
			String pwd = getProp().getProperty("mail.session.pass");

//			props.put("mail.smtp.host", host);
//			props.put("mail.session.user", user);
//			props.put("mail.session.pass", pwd);
//			props.put("mail.smtp.socketFactory.port",
//					getProp().getProperty("mail.smtp.socketFactory.port"));
//			props.put("mail.smtp.socketFactory.class",
//					getProp().getProperty("mail.smtp.socketFactory.class"));
//			props.put("mail.smtp.auth", getProp().getProperty("mail.smtp.auth"));
//			props.put("mail.smtp.port", getProp().getProperty("mail.smtp.port"));
           
			// Get a session
			 props.load(new FileInputStream(pathConfigFile));
			Session session = Session.getInstance(props,
					new GMAILAuthenticator(user, pwd));
			Transport bus = session.getTransport("smtp");
			bus.connect(host, user, pwd);
			return false;
		} catch (Exception mex) {
			// Prints all nested (chained) exceptions as well
			mex.printStackTrace();
			return true;
		}
	}

	public void sendEmailSSL(String subject, String content,
			List<NotificationListAddress> listDestinataire,
			String nomExpediteur, String mailExpediteur)
			throws AddressException, MessagingException {

		/**
		 * Ressource contenant les éléments statiques liés é la création et
		 * l'envoi d'un email.
		 */
	
		try {
			Resource rsrc = new ClassPathResource("../MailFax/mail.properties");
			String pathConfigFile = rsrc.getFile().getAbsolutePath();
			// **
			long startTime = System.currentTimeMillis();
			try {
				getProp().load(new FileInputStream(pathConfigFile));
			} catch (FileNotFoundException e) {
				System.err.println("Pas de fichier");
			} catch (IOException e) {
				System.err.println("Erreur");
			}
			// smtp properties
			Properties props = new Properties();
			// SUBSTITUTE YOUR ISP'S MAIL SERVER HERE
			String host = getProp().getProperty("mail.smtp.host");
			String user = getProp().getProperty("mail.session.user");
			String pwd = getProp().getProperty("mail.session.pass");
			
			// props.put("mail.smtp.host", host);

			// Get a session

			// Session session = Session.getInstance(props);

			props.load(new FileInputStream(pathConfigFile));
			mailExpediteur = props.getProperty("mailFrom");
			Session session = Session.getInstance(props,
					new GMAILAuthenticator(user, pwd));
			// Get a Transport object to send e-mail
			// Transport bus = session.getTransport("smtp");
			// bus.connect(host, user, pwd);
			// bus.connect(host, user, pwd);
			// Instantiate a message
			MimeMessage msg = new MimeMessage(session);
			// Set message attributes
			// msg.setFrom(new InternetAddress(user));

			for (NotificationListAddress mailto : listDestinataire) {
				// nader kassas
				// InternetAddress[] address = { new InternetAddress(
				// mailto.getMailDestinataire()) };
				// msg.setRecipients(Message.RecipientType.TO, address);
				// nader kassas

				// ** khaled saoudi
				System.out.println("@@ Dest: " + mailto.getMailDestinataire());
				msg.setRecipient(Message.RecipientType.TO, new InternetAddress(
						mailto.getMailDestinataire()));
				msg.setFrom(new InternetAddress(mailExpediteur, nomExpediteur));
				msg.setSubject(subject,"UTF-8");
				msg.setSentDate(new Date());
//				msg.setContent(content, "text/html");
				msg.setContent(content, "text/html; charset=utf-8");
				msg.saveChanges();
				// bus.sendMessage(msg, address);
				Transport.send(msg);
			}
			// bus.close();

			long duree = System.currentTimeMillis() - startTime;
			System.out.println("Methode " + " a pris " + duree + " (ms)");
		} catch (MessagingException mex) {
			// Prints all nested (chained) exceptions as well
			mex.printStackTrace();
			// How to access nested exceptions
			while (mex.getNextException() != null) {
				// Get next exception in chain
				Exception ex = mex.getNextException();
				ex.printStackTrace();
				if (!(ex instanceof MessagingException))
					break;
				else
					mex = (MessagingException) ex;
			}
			System.out.println("ERROR MessagingException");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ERROR UnsupportedEncodingException");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR Exception");
		}
	}

	public void sendEmailSSL(String subject, String content, String pathFile,
			List<NotificationListAddress> listDestinataire,
			String nomExpediteur, String mailExpediteur)
			throws AddressException, MessagingException {
		long startTime = System.currentTimeMillis();
		/**
		 * Ressource contenant les éléments statiques liés é la création et
		 * l'envoi d'un email.
		 */
		
		try {
			Resource rsrc = new ClassPathResource("../MailFax/mail.properties");
			String pathConfigFile = rsrc.getFile().getAbsolutePath();
			// **
			try {
				getProp().load(new FileInputStream(pathConfigFile));
			} catch (FileNotFoundException e) {
				System.err.println("Pas de fichier");
			} catch (IOException e) {
				System.err.println("Erreur");
			}
			// smtp properties
			Properties props = new Properties();
			// SUBSTITUTE YOUR ISP'S MAIL SERVER HERE
			String host = getProp().getProperty("mail.smtp.host");
			String user = getProp().getProperty("mail.session.user");
			String pwd = getProp().getProperty("mail.session.pass");

			// props.put("mail.smtp.host", host);

			// Get a session
			Session session = Session.getInstance(props,
					new GMAILAuthenticator(user, pwd));
			// Session session = Session.getInstance(props);

			props.load(new FileInputStream(pathConfigFile));
			mailExpediteur = props.getProperty("mailFrom");
			// Get a Transport object to send e-mail
			// Transport bus = session.getTransport("smtp");
			// bus.connect(host, user, pwd);
			// Instantiate a message
			Message msg = new MimeMessage(session);
			// Set message attributes
			///msg.setFrom(new InternetAddress(user));

			for (NotificationListAddress mailto : listDestinataire) {
				// InternetAddress[] address = { new InternetAddress(
				// mailto.getMailDestinataire()) };
				// msg.setRecipients(Message.RecipientType.TO, address);
				msg.setRecipient(Message.RecipientType.TO, new InternetAddress(
						mailto.getMailDestinataire()));
				msg.setFrom(new InternetAddress(mailExpediteur, nomExpediteur));
				msg.setSubject(subject);
				msg.setSentDate(new Date());
				// Create a multipart message
				Multipart multipart = new MimeMultipart();
				// Create the message part
				BodyPart messageBodyPart = new MimeBodyPart();
				// Fill the message
				messageBodyPart.setContent(content, "text/html");

				// Set text message part
				multipart.addBodyPart(messageBodyPart);
				// Part two is attachment
				messageBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(pathFile);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(pathFile);
				multipart.addBodyPart(messageBodyPart);
				// Send the complete message parts
				msg.setContent(multipart);
				msg.saveChanges();
				// bus.sendMessage(msg, address);
				Transport.send(msg);
			}
			// bus.close();

			long duree = System.currentTimeMillis() - startTime;
			System.out.println("Methode " + " a pris " + duree + " (ms)");
		} catch (MessagingException mex) {
			// Prints all nested (chained) exceptions as well
			mex.printStackTrace();
			// How to access nested exceptions
			while (mex.getNextException() != null) {
				// Get next exception in chain
				Exception ex = mex.getNextException();
				ex.printStackTrace();
				if (!(ex instanceof MessagingException))
					break;
				else
					mex = (MessagingException) ex;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	public void sendEmailSSLWithAttachemnt(String subject, String content,
			List<String> listDestinataire, List<InputStream> inputStreams, List<String> listNomDoc, List<String> listeNomExtension) throws AddressException,
			MessagingException {
		System.out.println("DANS sendEmailSSLWithAttachemnt");

		try {
			
		
			Resource rsrc = new ClassPathResource("../MailFax/mail.properties");
			String pathConfigFile = rsrc.getFile().getAbsolutePath();
		// **

		try {
			getProp().load(new FileInputStream(pathConfigFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("Pas de fichier");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Erreur");
		}
		// smtp properties
		Properties props = new Properties();
		// SUBSTITUTE YOUR ISP'S MAIL SERVER HERE
		String host = getProp().getProperty("mail.smtp.host");
		String user = getProp().getProperty("mail.session.user");
		String pwd = getProp().getProperty("mail.session.pass");
		
		// props.put("mail.smtp.host", host);

		// Get a session

		// Session session = Session.getInstance(props);

		props.load(new FileInputStream(pathConfigFile));

		String mailExpediteur="";
		mailExpediteur = props.getProperty("mailFrom");
		Session session = Session.getInstance(props,
				new GMAILAuthenticator(user, pwd));
		

		// construct message
		for (String to : listDestinataire) {
			Message message = new MimeMessage(session);
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));
			message.setFrom(new InternetAddress(mailExpediteur, "SONEDE"));
			message.setSubject(subject);
			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();
			// Fill the message
			messageBodyPart.setContent(content, "text/html");
			// Create a multipar message
			Multipart multipart = new MimeMultipart();
			// Set text message part
			multipart.addBodyPart(messageBodyPart);
		
			
			// Part two is attachment
			messageBodyPart = new MimeBodyPart();
             
			System.out.println("Done!");

    		if(inputStreams!=null && inputStreams.size()>0){
    			for(int j=0;j<inputStreams.size();j++){
    			InputStream inputStream =inputStreams.get(0);
    			
//     		DataHandler dataHandler = new DataHandler(new InputStreamDataSource(inputStream));
//			messageBodyPart.setDataHandler(dataHandler);
			System.out.println("Le fichier :: "+ listNomDoc.get(j));
			messageBodyPart.setFileName(listNomDoc.get(j)+listeNomExtension.get(j));
			multipart.addBodyPart(messageBodyPart);
    			}
		}
		 System.out.println(" Fin attachement");
			
			
			// Send the complete message parts
			message.setContent(multipart);
			// send email
			Transport.send(message);
			
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void sendEmailSSLMailing(String subject, String content,
			List<NotificationListAddress> listDestinataire,
			String nomExpediteur, String mailExpediteur)
			throws AddressException, MessagingException {

		/**
		 * Ressource contenant les éléments statiques liés é la création et
		 * l'envoi d'un email.
		 */
	
		try {
			Resource rsrc = new ClassPathResource("../MailFax/mailMailing.properties");
			String pathConfigFile = rsrc.getFile().getAbsolutePath();
			// **
			long startTime = System.currentTimeMillis();
			try {
				getProp().load(new FileInputStream(pathConfigFile));
			} catch (FileNotFoundException e) {
				System.err.println("Pas de fichier");
			} catch (IOException e) {
				System.err.println("Erreur");
			}
			// smtp properties
			Properties props = new Properties();
			// SUBSTITUTE YOUR ISP'S MAIL SERVER HERE
			String host = getProp().getProperty("mail.smtp.host");
			String user = getProp().getProperty("mail.session.user");
			String pwd = getProp().getProperty("mail.session.pass");
			
			// props.put("mail.smtp.host", host);

			// Get a session

			// Session session = Session.getInstance(props);

			props.load(new FileInputStream(pathConfigFile));
			mailExpediteur = props.getProperty("mailFrom");
			Session session = Session.getInstance(props,
					new GMAILAuthenticator(user, pwd));
			// Get a Transport object to send e-mail
			// Transport bus = session.getTransport("smtp");
			// bus.connect(host, user, pwd);
			// bus.connect(host, user, pwd);
			// Instantiate a message
			Message msg = new MimeMessage(session);
			// Set message attributes
			// msg.setFrom(new InternetAddress(user));

			for (NotificationListAddress mailto : listDestinataire) {
				// nader kassas
				// InternetAddress[] address = { new InternetAddress(
				// mailto.getMailDestinataire()) };
				// msg.setRecipients(Message.RecipientType.TO, address);
				// nader kassas

				// ** khaled saoudi
				System.out.println("@@ Dest: " + mailto.getMailDestinataire());
				msg.setRecipient(Message.RecipientType.TO, new InternetAddress(
						mailto.getMailDestinataire()));
				msg.setFrom(new InternetAddress(mailExpediteur, nomExpediteur));
				msg.setSubject(subject);
				msg.setSentDate(new Date());
//				msg.setContent(content, "text/html");
				msg.setContent(content, "text/html; charset=utf-8");
				msg.saveChanges();
				// bus.sendMessage(msg, address);
				Transport.send(msg);
			}
			// bus.close();

			long duree = System.currentTimeMillis() - startTime;
			System.out.println("Methode " + " a pris " + duree + " (ms)");
		} catch (MessagingException mex) {
			// Prints all nested (chained) exceptions as well
			mex.printStackTrace();
			// How to access nested exceptions
			while (mex.getNextException() != null) {
				// Get next exception in chain
				Exception ex = mex.getNextException();
				ex.printStackTrace();
				if (!(ex instanceof MessagingException))
					break;
				else
					mex = (MessagingException) ex;
			}
			System.out.println("ERROR MessagingException");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ERROR UnsupportedEncodingException");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR Exception");
		}
	}
	
	
	
	public static void setProp(Properties prop) {
		EmailUtil.prop = prop;
	}

	public static Properties getProp() {
		return prop;
	}
	// public void sendEmailNOTIF(String subject, String content,
	// List<NotificationListAddress> listDestinataire,
	// Object expediteur)
	// throws AddressException, MessagingException {
	//
	// long startTime = System.currentTimeMillis();
	// String mailExpediteur = null;
	// String nomExpediteur = null;
	//
	// Person person = (Person) expediteur;
	// mailExpediteur = person.getEmail();
	// nomExpediteur = person.getPrenom() + " " + person.getNom();
	//
	// person.get
	//
	//
	//
	// String pathConfigFile = "C:\\GBO\\GedConfig\\smtp.properties";
	// System.out.println("  *" + pathConfigFile);
	// try {
	// getProp().load(new FileInputStream(pathConfigFile));
	// } catch (FileNotFoundException e) {
	// System.err.println("Pas de fichier");
	// } catch (IOException e) {
	// System.err.println("Erreur");
	// }
	// // smtp properties
	// Properties props = new Properties();
	// // SUBSTITUTE YOUR ISP'S MAIL SERVER HERE
	// String host = getProp().getProperty("mail.smtp.host");
	// String user = mailExpediteur;
	// String pwd = getProp().getProperty("mail.session.pass");
	//
	//
	//
	//
	// try {
	// //props.load(new FileInputStream(pathConfigFile));
	// Session session = Session.getInstance(props, new GMAILAuthenticator(user,
	// pwd));
	// // Get a Transport object to send e-mail
	// //Transport bus = session.getTransport("smtp");
	// // bus.connect(host, user, pwd);
	// //bus.connect(host, user, pwd);
	// // Instantiate a message
	// Message msg = new MimeMessage(session);
	// // Set message attributes
	// // msg.setFrom(new InternetAddress(user));
	//
	// for (NotificationListAddress mailto : listDestinataire) {
	// // nader kassas
	// // InternetAddress[] address = { new InternetAddress(
	// // mailto.getMailDestinataire()) };
	// //msg.setRecipients(Message.RecipientType.TO, address);
	// // nader kassas
	//
	// //** khaled saoudi
	// msg.setRecipient(Message.RecipientType.TO, new InternetAddress(
	// mailto.getMailDestinataire()));
	// msg.setFrom(new InternetAddress(mailExpediteur, nomExpediteur));
	// msg.setSubject(subject);
	// msg.setSentDate(new Date());
	// msg.setContent(content, "text/html");
	// msg.saveChanges();
	// //bus.sendMessage(msg, address);
	// Transport.send(msg);
	// }
	// //bus.close();
	//
	// long duree = System.currentTimeMillis() - startTime;
	// System.out.println("Methode " + " a pris " + duree + " (ms)");
	// } catch (MessagingException mex) {
	// // Prints all nested (chained) exceptions as well
	// mex.printStackTrace();
	// // How to access nested exceptions
	// while (mex.getNextException() != null) {
	// // Get next exception in chain
	// Exception ex = mex.getNextException();
	// ex.printStackTrace();
	// if (!(ex instanceof MessagingException))
	// break;
	// else
	// mex = (MessagingException) ex;
	// }
	// } catch (UnsupportedEncodingException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (Exception e){
	// e.printStackTrace();
	// }
	// }

	/*
	 * public static void main(String[] args) { try { List<String> ld = new
	 * ArrayList<String>(); ld.add("ahlem.draouil@elmedina.tn"); String
	 * htmlContent = "<html><h3>Bonjour</h3><p>Document en PJ !!!</p></html>";
	 * String object = "Mail de Test Avec Attachement"; EmailUtil emailUtil =
	 * new EmailUtil(); emailUtil.sendEmailSSLWithAttachemnt(object,
	 * htmlContent, ld); System.out.println("Envoie effectué avec succes"); }
	 * catch (NoSuchProviderException e) {
	 * System.err.println("Pas de transport disponible pour ce protocole");
	 * System.err.println(e); } catch (AddressException e) {
	 * System.err.println("Adresse invalide"); System.err.println(e); } catch
	 * (MessagingException e) { System.err.println("Erreur dans le message");
	 * System.err.println(e); } }
	 */
}