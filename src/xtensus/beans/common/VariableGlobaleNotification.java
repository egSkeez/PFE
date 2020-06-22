package xtensus.beans.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import xtensus.beans.utils.Informations;
import xtensus.entity.Pm;
import xtensus.entity.Pp;
import xtensus.gnl.entity.Evenement;
import xtensus.gnl.entity.Notification;
import xtensus.gnl.entity.Trace;
import xtensus.gnl.entity.Xtelog;
import xtensus.ldap.model.Person;
import xtensus.ldap.model.Unit;

@Component
@Scope("session")
public class VariableGlobaleNotification {

	/************* Logs & Notifications **********/
	private String evenementNomVariableNotif;
	private String evenementNomVariableLog;
	private String typeLog;
	private String typeObject;
	private String info;
	private String nomClass;
	private List<String> listAddress;
	private List<Informations> listInformations;
	private String nomExpediteur;
	private String mailExpediteur;
	private Xtelog log;
	private Trace trace;
	private Evenement evenement;
	private Notification notification;
	private List<Notification> listNotification;
	private String notificationNomVariable;
	private String notificationNomVariablAdmin;
	private String notificationNomVariableDestinataire;
	private List<Person> copyListSelectedPersonNotif = new ArrayList<Person>();
	private List<Unit> copyListSelectedUnitNotif = new ArrayList<Unit>();
	private List<Pp> copyListPPNotif = new ArrayList<Pp>();
	private List<Pm> copyListPMNotif = new ArrayList<Pm>();

	private String logUser;
	private String logDate;
	private String logType;

	private Person person;

	/************* Getters && Setters ********************/
	public String getTypeLog() {
		return typeLog;
	}

	public void setTypeLog(String typeLog) {
		this.typeLog = typeLog;
	}

	public String getTypeObject() {
		return typeObject;
	}

	public void setTypeObject(String typeObject) {
		this.typeObject = typeObject;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getNomClass() {
		return nomClass;
	}

	public void setNomClass(String nomClass) {
		this.nomClass = nomClass;
	}

	public List<String> getListAddress() {
		return listAddress;
	}

	public void setListAddress(List<String> listAddress) {
		this.listAddress = listAddress;
	}

	public List<Informations> getListInformations() {
		return listInformations;
	}

	public void setListInformations(List<Informations> listInformations) {
		this.listInformations = listInformations;
	}

	public String getNomExpediteur() {
		return nomExpediteur;
	}

	public void setNomExpediteur(String nomExpediteur) {
		this.nomExpediteur = nomExpediteur;
	}

	public String getMailExpediteur() {
		return mailExpediteur;
	}

	public void setMailExpediteur(String mailExpediteur) {
		this.mailExpediteur = mailExpediteur;
	}

	public Xtelog getLog() {
		return log;
	}

	public void setLog(Xtelog log) {
		this.log = log;
	}

	public Trace getTrace() {
		return trace;
	}

	public void setTrace(Trace trace) {
		this.trace = trace;
	}

	public Evenement getEvenement() {
		return evenement;
	}

	public void setEvenement(Evenement evenement) {
		this.evenement = evenement;
	}

	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}

	public List<Notification> getListNotification() {
		return listNotification;
	}

	public void setListNotification(List<Notification> listNotification) {
		this.listNotification = listNotification;
	}

	public List<Person> getCopyListSelectedPersonNotif() {
		return copyListSelectedPersonNotif;
	}

	public void setCopyListSelectedPersonNotif(
			List<Person> copyListSelectedPersonNotif) {
		this.copyListSelectedPersonNotif = copyListSelectedPersonNotif;
	}

	public List<Unit> getCopyListSelectedUnitNotif() {
		return copyListSelectedUnitNotif;
	}

	public void setCopyListSelectedUnitNotif(
			List<Unit> copyListSelectedUnitNotif) {
		this.copyListSelectedUnitNotif = copyListSelectedUnitNotif;
	}

	public List<Pp> getCopyListPPNotif() {
		return copyListPPNotif;
	}

	public void setCopyListPPNotif(List<Pp> copyListPPNotif) {
		this.copyListPPNotif = copyListPPNotif;
	}

	public List<Pm> getCopyListPMNotif() {
		return copyListPMNotif;
	}

	public void setCopyListPMNotif(List<Pm> copyListPMNotif) {
		this.copyListPMNotif = copyListPMNotif;
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

	public void setPerson(Person person) {
		this.person = person;
	}

	public Person getPerson() {
		return person;
	}

	public void setEvenementNomVariableLog(String evenementNomVariableLog) {
		this.evenementNomVariableLog = evenementNomVariableLog;
	}

	public String getEvenementNomVariableLog() {
		return evenementNomVariableLog;
	}

	public void setEvenementNomVariableNotif(String evenementNomVariableNotif) {
		this.evenementNomVariableNotif = evenementNomVariableNotif;
	}

	public String getEvenementNomVariableNotif() {
		return evenementNomVariableNotif;
	}

	public void setNotificationNomVariable(String notificationNomVariable) {
		this.notificationNomVariable = notificationNomVariable;
	}

	public String getNotificationNomVariable() {
		return notificationNomVariable;
	}

	public void setNotificationNomVariableDestinataire(
			String notificationNomVariableDestinataire) {
		this.notificationNomVariableDestinataire = notificationNomVariableDestinataire;
	}

	public String getNotificationNomVariableDestinataire() {
		return notificationNomVariableDestinataire;
	}

	public void setNotificationNomVariablAdmin(
			String notificationNomVariablAdmin) {
		this.notificationNomVariablAdmin = notificationNomVariablAdmin;
	}

	public String getNotificationNomVariablAdmin() {
		return notificationNomVariablAdmin;
	}

}
