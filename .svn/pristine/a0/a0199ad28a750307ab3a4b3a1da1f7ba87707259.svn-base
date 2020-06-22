package xtensus.dao.utils;

import java.util.Hashtable;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import xtensus.beans.utils.ConnexionLdap;

public class LdapConnectionSingleton {

	private static DirContext dirContext;

	public static DirContext getInstance() {
		if (dirContext == null) {
			Connection();
		}

		return dirContext;
	}

	public static void close() {
		try {
			dirContext.close();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	private static void Connection() {
		Hashtable<String, String> hashtable = new Hashtable<String, String>();

		try {
			hashtable = ConnexionLdap.getConnection();
			dirContext = new InitialDirContext(hashtable);
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
