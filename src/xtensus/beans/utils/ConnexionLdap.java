package xtensus.beans.utils;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.naming.Context;
import org.jdom.JDOMException;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ConnexionLdap {

	//public Hashtable<String, String> hashtable;

	/*public Hashtable<String, String> getConnexion() {
		org.jdom.Document document;
		Element racine;
		SAXBuilder sxb = new SAXBuilder();
		hashtable = new Hashtable<String, String>();
		try {
			//
			// C:/WorkspaceSIGA/GBO_v1.k/WebContent/WEB-INF/LdapConfig/LdapConfiguration.xml
			document = sxb.build(new File(
					"C:\\GBO\\LdapConfig\\LdapConfiguration.xml"));
			racine = document.getRootElement();
			//elementValue = racine.getChildText(elementName);
			hashtable.put(Context.INITIAL_CONTEXT_FACTORY,
					racine.getChildText("INITIAL_CONTEXT_FACTORY"));
			hashtable.put(Context.PROVIDER_URL, racine.getChildText("PROVIDER_URL"));
			hashtable.put(Context.SECURITY_AUTHENTICATION,
					racine.getChildText("SECURITY_AUTHENTICATION"));
			hashtable.put(Context.SECURITY_PRINCIPAL,
					racine.getChildText("SECURITY_PRINCIPAL"));
			hashtable.put(Context.SECURITY_CREDENTIALS,
					racine.getChildText("SECURITY_CREDENTIALS"));
		} catch (JDOMException e) {
			System.out.println("error");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("error");
			e.printStackTrace();
		}

		return hashtable;
	}
*/
	private static Hashtable<String, String> hashtable;

	public static Hashtable<String, String> getConnection() throws Exception {
		System.out.println("getConnection()))) From LDAP");
		if (hashtable == null) {
			org.jdom.Document document;
			Element racine;
			SAXBuilder sxb = new SAXBuilder();
			hashtable = new Hashtable<String, String>();
			try {
				//
				// C:/WorkspaceSIGA/GBO_v1.k/WebContent/WEB-INF/LdapConfig/LdapConfiguration.xml
				
				
//				document = sxb.build(new File(
//				"C:\\GBO\\LdapConfig\\LdapConfiguration.xml"));
//				Resource rsrc = new ClassPathResource(
//				"/WEB-INF/LdapConfig/LdapConfiguration.xml");
//		         String pathConfigFile = rsrc.getFile().getAbsolutePath();
				Resource rsrc = new ClassPathResource(
				"../LdapConfig/LdapConfiguration.xml");
		        String pathConfigFile = rsrc.getFile().getAbsolutePath();
				
				document = sxb.build(new File(pathConfigFile));
				racine = document.getRootElement();
				//elementValue = racine.getChildText(elementName);
				hashtable.put(Context.INITIAL_CONTEXT_FACTORY,
						racine.getChildText("INITIAL_CONTEXT_FACTORY"));
				hashtable.put(Context.PROVIDER_URL, racine.getChildText("PROVIDER_URL"));
				hashtable.put(Context.SECURITY_AUTHENTICATION,
						racine.getChildText("SECURITY_AUTHENTICATION"));
				hashtable.put(Context.SECURITY_PRINCIPAL,
						racine.getChildText("SECURITY_PRINCIPAL"));
				hashtable.put(Context.SECURITY_CREDENTIALS,
						racine.getChildText("SECURITY_CREDENTIALS"));
			} catch (JDOMException e) {
				System.out.println("##Error LDAPConfiguration JDOM");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("##Error LDAPConfiguration File");
				e.printStackTrace();
			}
		}
		return hashtable;
	}
	
	/*
	public String getElement(String elementName) {
		org.jdom.Document document;
		Element racine;
		SAXBuilder sxb = new SAXBuilder();
		String elementValue = "";

		try {
			//
			// C:/WorkspaceSIGA/GBO_v1.k/WebContent/WEB-INF/LdapConfig/LdapConfiguration.xml
			document = sxb.build(new File(
					"C:\\GBO\\LdapConfig\\LdapConfiguration.xml"));
			racine = document.getRootElement();
			elementValue = racine.getChildText(elementName);
		} catch (JDOMException e) {
			System.out.println("error");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("error");
			e.printStackTrace();
		}

		return elementValue;

	}
*/
}
