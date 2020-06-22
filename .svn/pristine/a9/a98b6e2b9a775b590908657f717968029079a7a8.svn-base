package xtensus.aop;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import xtensus.gnl.entity.Templatelog;
import xtensus.gnl.entity.Trace;
import xtensus.gnl.entity.VariablesLog;
import xtensus.gnl.entity.Xtelog;
import xtensus.ldap.model.Person;
import xtensus.services.ApplicationManager;

public class LogClass {

	private Logger logger;

	public void addTrack(String variableNameOfEventLog, String eventLog,
			Person person, String typeLog, ApplicationManager appMgr) {
		String templateLogTexte;
		Xtelog log = new Xtelog();
		Templatelog templateLog = new Templatelog();
		SimpleDateFormat formaterDateHeure = null;
		formaterDateHeure = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String dateLog = formaterDateHeure.format(new Date());
		URL u = getClass().getClassLoader()
				.getResource("xtensus/aop/log4j.xml");
		DOMConfigurator.configure(u);
		logger = Logger.getLogger(TracingAfterReturningAdvice.class);

		try {
			log = appMgr.listLogByLibelle(variableNameOfEventLog).get(0);
			templateLog = appMgr.listTemplateLogByLog(log.getLogId()).get(0);
			templateLogTexte = changeVariableLog(
					templateLog.getTemplateLogVariable(), dateLog, typeLog,
					person.getCn(), eventLog, appMgr);
			logger.info(templateLogTexte);
			Trace trace = new Trace();
			trace.setXtelog(log);
			trace.setTypelog(typeLog);
			trace.setUserTexte(person.getCn());
			trace.setDateTexte(dateLog);
			trace.setTraceTexte(templateLogTexte);
			appMgr.insert(trace);
		} catch (Exception e) {
			System.err.println("Erreur dans le Trace");
			e.printStackTrace();
		}
	}

	private String changeVariableLog(String templateLogVariable,
			String dateLog, String typeLog, String cn, String eventLog,
			ApplicationManager appMgr) {
		List<VariablesLog> listVariablesLog = new ArrayList<VariablesLog>();
		try {
			listVariablesLog = appMgr.getList(VariablesLog.class);
			for (VariablesLog variablesLog : listVariablesLog) {
				if (templateLogVariable.contains(variablesLog
						.getVariableValeur())) {
					int firstIndex = templateLogVariable.indexOf(variablesLog
							.getVariableValeur());
					int lastIndex = firstIndex
							+ variablesLog.getVariableValeur().length();
					if (variablesLog.getVariableValeur().equals("#Date/Heure")) {
						templateLogVariable = templateLogVariable.substring(0,
								firstIndex)
								+ dateLog
								+ templateLogVariable.substring(lastIndex);
					} else if (variablesLog.getVariableValeur().equals(
							"#Type_Log")) {
						templateLogVariable = templateLogVariable.substring(0,
								firstIndex)
								+ typeLog
								+ templateLogVariable.substring(lastIndex);
					} else if (variablesLog.getVariableValeur().equals("#Nom")) {
						templateLogVariable = templateLogVariable.substring(0,
								firstIndex)
								+ cn
								+ templateLogVariable.substring(lastIndex);
					} else if (variablesLog.getVariableValeur().equals(
							"#Evenement")) {
						templateLogVariable = templateLogVariable.substring(0,
								firstIndex)
								+ eventLog
								+ templateLogVariable.substring(lastIndex);
					}
				}
			}
		} catch (Exception e1) {
			System.err.println("Erreur chargement");
			e1.printStackTrace();
		}
		return templateLogVariable;
	}

}
