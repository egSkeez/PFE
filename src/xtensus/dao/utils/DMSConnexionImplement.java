package xtensus.dao.utils;

import java.util.HashMap;
import java.util.Map;

import com.xtensus.dal.connection.ConnectionParameters;
import com.xtensus.dal.core.DMSAccessLayer;

public class DMSConnexionImplement {

	private static DMSAccessLayer accessLayer;
	public static boolean connetionStatus;

	private DMSConnexionImplement() {
		// Optional Code
	}

	public static DMSAccessLayer getConnexionGed(String login, String mdp,
			String URL, String namingConfigFilePath) {
		if (accessLayer == null) {
			connetionStatus = false;
			try {
				// -------------- CMIS --------------
				// Parametres de connexion au ged et emplacement du fihier de
				// configuration pour le stockage des documents
				Map<String, String> parameters = new HashMap<String, String>();
				parameters.put(ConnectionParameters.LOGIN, login);
				parameters.put(ConnectionParameters.PASSWORD, mdp);
				// http://localhost:8081/alfresco/service/cmis";
				parameters.put(ConnectionParameters.URL, URL);
				parameters.put(ConnectionParameters.FILE_CONFIG_PATH,
						namingConfigFilePath);
				accessLayer = new DMSAccessLayer(parameters);
				// -------------- CMIS --------------
			} catch (Exception e) {
				connetionStatus = true;
				e.printStackTrace();
			}
		}

		return accessLayer;
	}

}
