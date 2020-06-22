/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xtensus.services;

import static xtensus.dao.utils.QueryParameterBuilder.with;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xtensus.beans.utils.CourrierInformations;
import xtensus.beans.utils.RecherchePmModel;
import xtensus.beans.utils.RecherchePpModel;
//import xtensus.beans.utils.RecherchePmModel;
//import xtensus.beans.utils.RecherchePpModel;
//import xtensus.beans.utils.StatistiqueCourrierStructureByNature;
//import xtensus.beans.utils.StatistiqueCourrierUtilisateur;
import xtensus.dao.IGenericDao;
import xtensus.entity.Aide;
import xtensus.entity.Annotation;
import xtensus.entity.AoConsultation;
import xtensus.entity.ArmoirBoiteArchiveUser;
import xtensus.entity.Classement_archivage_niveau_02;
import xtensus.entity.Cheque;
import xtensus.entity.Commentaire;
import xtensus.entity.Confidentialite;
import xtensus.entity.Courrier;
import xtensus.entity.CourrierDonneeSupplementaire;
import xtensus.entity.CourrierDossier;
import xtensus.entity.CourrierLiens;
import xtensus.entity.Direction;
import xtensus.entity.Document;
import xtensus.entity.DocumentCategorie;
import xtensus.entity.DonneeSupplementaire;
import xtensus.entity.DonneeSupplementaireNature;
import xtensus.entity.Dossier;
import xtensus.entity.Droit;
import xtensus.entity.Employe;
import xtensus.entity.Entite;
import xtensus.entity.Classement_archivage_niveau_01;
import xtensus.entity.Etat;
import xtensus.entity.Expdest;
import xtensus.entity.Expdestexterne;
import xtensus.entity.Gouvernerat;
import xtensus.entity.Groupecontactmailing;
import xtensus.entity.Groupecontactpppm;
import xtensus.entity.Lienscourriers;
import xtensus.entity.Mail;
import xtensus.entity.Menu;
import xtensus.entity.Nature;
import xtensus.entity.Pays;
import xtensus.entity.Pm;
import xtensus.entity.Pp;
import xtensus.entity.Raisonnontransmission;
import xtensus.entity.Sauvegardehistorique;
import xtensus.entity.Societe;
import xtensus.entity.Suivitransmissioncourrier;
import xtensus.entity.Sujetmailing;
import xtensus.entity.Transaction;
import xtensus.entity.TransactionAnnotation;
import xtensus.entity.TransactionDestination;
import xtensus.entity.TransactionDestinationReelle;
import xtensus.entity.TransactionDocument;
import xtensus.entity.Transmission;
import xtensus.entity.Typedossier;
import xtensus.entity.Typetransaction;
import xtensus.entity.Typeutilisateur;
import xtensus.entity.Unite;
import xtensus.entity.Urgence;
import xtensus.entity.Utilisateur;
import xtensus.entity.Variables;
import xtensus.entity.Ville;
import xtensus.entity.Workflow;
//import xtensus.faxmail.beans.AttachmentFileBean;
//import xtensus.faxmail.beans.AttachmentHeadBean;
import xtensus.gnl.entity.Evenement;
import xtensus.gnl.entity.Message;
import xtensus.gnl.entity.Notification;
import xtensus.gnl.entity.NotificationVariable;
import xtensus.gnl.entity.Templatelog;
import xtensus.gnl.entity.Templatenotification;
import xtensus.gnl.entity.TypeEvenement;
import xtensus.gnl.entity.UserNotification;
import xtensus.gnl.entity.VariablesNotification;
import xtensus.gnl.entity.Xtelog;
import xtensus.qualifiers.Dao;

/**
 * HB
 * 
 */
@Service
public class ApplicationManager extends AbstractGenericManager {

	// private static final String PROJECT_BY_STATE = "projectByState";
	private static final String LIST_NATURE_WITH_WORKFLOW = "listNatureWithWorkFlow";
	private static final String LIST_NATURE_BY_NOM = "listNature";
	private static final String LIST_TRANSMISSION_BY_NOM = "listTransmission";
	private static final String LIST_CONFIDENTIALITE_BY_NOM = "listConfidentialite";
	private static final String LIST_URGENCE_BY_NOM = "listUrgence";
	private static final String LIST_ANNOTATION_BY_LIBELLE = "listAnnotation";
	private static final String LIST_TYPEUTILISATEUR_BY_LIBELLE = "listTypeUtiisateurByLibelle";
	private static final String LIST_TYPEUTILISATEUR_BY_ID = "listTypeUtiisateurById";
	private static final String LIST_TypeDossier_BY_ID = "listTypeDossierById";
	private static final String LIST_TYPEDOSSIER_BY_LIBELLE = "listTypeDossierByLibelle";
	private static final String LIST_TypeTransaction_BY_ID = "listTypeTransactionById";
	private static final String LIST_TYPE_TRANSACTION_BY_LIBELLE = "listTypeTransactionByLibelle";
	private static final String LIST_DOSSIER_BY_ID = "listDossierById";
	private static final String LIST_EXPEDITEUR_BY_ID = "listExpediteurById";
	private static final String LIST_COURRIERDOSSIER_BY_IDCOURRIER = "listcourrierDossierByIdCourrier";
	private static final String LIST_COURRIERDOSSIER_BY_IDDOSSIER = "listcourrierDossierByIdDossier";
	private static final String LIST_TRANSACTION_BY_IDDOSSIER = "listTransactionByIdDossier";
	private static final String LIST_TRANSACTION_BY_IDDOSSIER_FORSUIVI = "listTransactionByIdDossierPourSuivi";
	private static final String LIST_TRANSACTION_BY_IDDOSSIER_Etat = "listTransactionByIdDossierByEtat";
	private static final String LIST_ANNOTATION_BY_ID = "listAnnotationId";
	private static final String LIST_NATURE_BY_ID = "listNatureId";
	private static final String LIST_TRANSMISSION_BY_ID = "listTransmissionId";
	private static final String LIST_CONFIDENTIALITE_BY_ID = "listConfidentialiteId";
	private static final String LIST_URGENCE_BY_ID = "listUrgenceId";
	private static final String LIST_DOCUMENT_BY_ID_COURRIER = "listDocumentByIdCourrier";
	private static final String LIST_ANNOTATATION_BY_IDTRANSACTION = "listAnnotationIdByIdTransaction";
	private static final String LIST_COURRIER_BY_ID = "listCourrierById";
	private static final String LIST_COURRIER_BY_REF = "listCourrierByRef";
	private static final String LIST_COURRIER_BY_I_CLOTURE = "listCourrierByIdCloture";
	private static final String LIST_COURRIER_INTERENE = "listCourrierInterene";
	private static final String LIST_COURRIER_BY_DATE_JOUR_ID = "listCrByDateJourId";
	private static final String LIST_COURRIER_BY_DATE = "listCrByDateId";
	private static final String LIST_COURRIER_BY_SEM_ID = "listCrBySemId";
	private static final String LIST_PpByIDExpediteur = "PpByidExpediteur";
	private static final String LIST_PmByIDExpediteur = "PmByidExpediteur";
	private static final String listExpediteurByStatus = "listexpediteurBystatus";
	private static final String LIST_PP_BY_ID_PM = "listPPByPm";
	private static final String LIST_PM_BY_ID_PM = "listPMByIdPm";

	private static final String LIST_PP_BY_TYPE_USER= "listPPByUser";
	private static final String LIST_PM_BY_TYPE_USER = "listPMByUser";
	private static final String RECHERCHE_COURRIER = "rechercheCourrier";
	private static final String LIST_COURRIERTYPEORDRE_BY_ID = "listCourrierTypeOrdreById";
	// Etat
	private static final String LIST_ETAT_BY_REFERENCE = "listEtatByRef";
	private static final String LIST_ETAT_BY_LIBELLE = "listEtatBylibelle";
	private static final String LIST_UTILISATEUR_BY_ID = "listUtiliById";
	private static final String LIST_DESTINATAIRE_BY_ID_TRANSACTION = "listDestByIdTransaction";
	// CourrierTransaction
	private static final String LIST_EXPDEST_BY_ID = "listExpDestById";
	private static final String LIST_EXPDEST_BY_ID_EXPDEST = "listExpDestByIdExpDest";
	private static final String listExpDestExterne = "listExpDestExterne";
	private static final String LIST_TRANSACTION_DESTINATION_BY_ID_EXPDEST = "listTransactionDestinationByIdExpDest";
	private static final String LIST_TRANSACTION_DESTINATION_BY_ID_TRANSACTION = "listTransactionDestinationByIdTransaction";
	private static final String LIST_TRANSACTION_BY_ID_TRANSACTION = "listTransactionByIdTransaction";
	private static final String LIST_TRANSACTION_BY_ID_TRANSACTION_JOUR = "listTransactionByIdTransactionJour";
	private static final String LIST_TRANSACTION_BY_ID_EXPDEST = "listTransactionByIdExpDest";
	private static final String LIST_TRANSACTION_BY_ID_EXPDEST_JOUR = "listTransactionByIdExpDestJour";
	private static final String LIST_TRANSACTION_BY_ID_TRANSACTION_AND_FIRST_ORDER = "listTransactionByIdAndFirstOrder";
	private static final String LIST_COMMENTAIRE_BY_ID_TRANSACTION = "listCommentaireByIdTransaction";
	private static final String LIST_COMMENTAIRE_BY_ID_COURRIER = "listCommentaireByIdCourrier";
	private static final String LIST_COMMENTAIRE_BY_ID_USER = "listCommentaireByIdUser";
	private static final String LIST_COMMENTAIRE_COURRIER_BY_ID_USER = "listCommentaireCourrierByIdUser";
	private static final String LIST_TRANSACTION_ENVOYES_FOR_RESPONSABLE_JOUR = "listTransactionEnvoyesParResponsableJour";
	private static final String LIST_TRANSACTION_RECUS_AU_RESPONSABLE_JOUR = "listTransactionRecusAuResponsableJour";
	private static final String LIST_TRANSACTION_ENVOYES_FOR_RESPONSABLE = "listTransactionEnvoyesParResponsable";
	private static final String LIST_TRANSACTION_RECUS_AU_RESPONSABLE = "listTransactionRecusAuResponsable";
	private static final String LIST_TRANSACTION_ENVOYES_FOR_AGENT_JOUR = "listTransactionEnvoyesParAgentJour";
	private static final String LIST_TRANSACTION_RECUS_AU_AGENT_JOUR = "listTransactionRecusAuAgentJour";
	private static final String LIST_TRANSACTION_ENVOYES_FOR_AGENT = "listTransactionEnvoyesParAgent";
	private static final String LIST_TRANSACTION_RECUS_AU_AGENT = "listTransactionRecusAuAgent";
	private static final String LIST_TRANSACTION_ENVOYES_FOR_SECRETARY_JOUR = "listTransactionEnvoyesParSecretaireJour";
	private static final String LIST_TRANSACTION_RECUS_AU_SECRETARY_JOUR = "listTransactionRecusAuSecretaireJour";
	private static final String LIST_TRANSACTION_ENVOYES_FOR_SECRETARY = "listTransactionEnvoyesParSecretaire";
	private static final String LIST_TRANSACTION_RECUS_AU_SECRETARY = "listTransactionRecusAuSecretaire";
	private static final String LIST_DROIT_BY_LIBELLE = "listDroitByLibelle";
	private static final String LIST_MENU_BY_ID_DROIT = "listMenuByIdDroit";
	private static final String LIST_SOUS_MENU_BY_ID_MENU = "listSousMenuByIdMenu";
	private static final String LIST_TRANSACTION_TEST = "listTransactionTest";
	private static final String LIST_TRANSACTION_TEST_RECORD = "listTransactionTestRecord";
	private static final String LIST_PARTAGE_BY_ID_TRANSACTION = "listPartageByIdTransaction";
	private static final String DOSSIERCOURRIER_BY_IDDOSSIER = "deleteDossierCourrierByIdDossier";
	// Rapport
	private static final String DirectionByRefrence = "directionByRefrence";
	private static final String AnnotationByOrdre = "annotationByOrdre";
	private static final String UrgenceByOrdre = "urgenceByOrdre";
	private static final String ConfidentialiteByOrdre = "confidentialiteByOrdre";
	private static final String NatureByOrdreRef = "natureByOrdreRef";
	private static final String COUNTLIST = "countListconfidentialite";
	private static final String COUNTLISTURGENCE = "countListurgence";
	private static final String COUNTLISTURGENCEDOSSIER = "countListurgenceDossier";
	private static final String COUNTLISTCONFEDENTIALITEDOSSIER = "countListconfidentialiteDossier";
	private static final String AIDE_BY_TITREINTERFACE = "aideByInterface";
	private static final String DOSSIER_BY_IDPROPRIETAIRE = "listDossierByIdProprietaire";
	private static final String LISTE_COURRIER_NECESSITANT_REPONSE = "listCourrierNecessitnatReponse";
	// Variables
	private static final String LIST_VARIABLES_BY_ID = "listVariablesById";
	private static final String LIST_VARIABLES_BY_Libelle = "listVariablesByLibelle";
	// Societe
	private static final String LIST_SOCIETE_BY_ID = "listSocieteById";
	private static final String LIST_SOCIETE_BY_Libelle = "listSocieteByLibelle";
	// Armoire
	private static final String LIST_Armoire_BY_ID = "listArmoireById";
	private static final String LIST_Armoire_BY_Libelle = "listArmoireByLibelle";
	private static final String LIST_Armoire_BY_ETAT = "listArmoireByEtat";
	// Etages
	private static final String LIST_Etages_BY_ID = "listEtagesById";
	private static final String LIST_Etages_BY_ID_Armoire = "listEtagesByIdArmoire";
	private static final String LIST_Etages_BY_Libelle = "listEtagesByLibelle";
	private static final String LIST_Etages_BY_ID_Armoire_AND_ETAT = "listEtagesByIdArmoireAndEtat";
	private static final String LIST_COURRIER_BY_IDETAGES = "listCourrierByIdEtages";
	private static final String LIST_ARMBT_BY_IDUSER = "listArmoireUserClassement";

	// ********Attribut :
	private static final String LIST_TRANSACTION_ENVOYES_FOR_AGENT_BOC_JOUR = "listTransactionEnvoyesParAgentBocJour";
	private static final String LIST_TRANSACTION_ENVOYES_FOR_AGENT_BOC = "listTransactionEnvoyesParAgentBoc";
	private static final String LIST_TRANSACTION_RECUS_AU_AGENT_BOC_JOUR = "listTransactionRecusAuAgentBocJour";
	private static final String LIST_TRANSACTION_RECUS_AU_AGENT_BOC = "listTransactionRecusAuAgentBoc";
	private static final String LIST_TRANSACTION_ENVOYES_PAR_SUBORDONNES_JOUR = "listTransactionEnvoyesParSubordonnesJour";
	private static final String LIST_TRANSACTION_RECUS_AU_SUBORDONNES_JOUR = "listTransactionRecusAuSubordonnesJour";
	private static final String LIST_TRANSACTION_ENVOYES_PAR_SUBORDONNES = "listTransactionEnvoyesParSubordonnes";
	private static final String LIST_TRANSACTION_RECUS_AU_SUBORDONNES = "listTransactionRecusAuSubordonnes";
	private static final String LIST_TRANSACTION_ENVOYES_PAR_SOUS_UNITE_JOUR = "listTransactionEnvoyesParSousUniteJour";
	private static final String LIST_TRANSACTION_RECUS_AU_SOUS_UNITE_JOUR = "listTransactionRecusAuSousUniteJour";
	private static final String LIST_TRANSACTION_ENVOYES_PAR_SOUS_UNITE = "listTransactionEnvoyesParSousUnite";
	private static final String LIST_TRANSACTION_RECUS_AU_SOUS_UNITE = "listTransactionRecusAuSousUnite";
	private static final String LIST_TRANSACTION_BOC_JOUR = "listTransactionBocJour";
	private static final String LIST_TRANSACTION_BOC = "listTransactionBoc";
	private static final String LIST_TRANSACTION_DESTINATION_BOC_JOUR = "listTransactionDestinationBocJour";
	private static final String LIST_TRANSACTION_DESTINATION_BOC = "listTransactionDestinationBoc";
	private static final String LIST_TRANSACTION_DESTINATION_BOC_SANS_ETAT = "listTransactionDestinationBocSansEtat";
	private static final String LIST_TRANSACTION_EXPEDITEUR_BY_ID_TRANSACTION_DESTINANTION_REELLE = "listTransactionExpediteurByIdTransactionDestinationReelle";
	private static final String LIST_TRANSACTION_BY_ID_TRANSACTION_DESTINANTION_REELLE = "listTransactionByIdTransactionDestinationReelle";
	private static final String LIST_TRANSACTION_BY_ID_TRANSACTION_DESTINANTION_REELLE_ETAT = "listTransactionByIdTransactionDestinationReelleByEtat";

	private static final String LIST_COURRIER_LIEN_BY_ID_COURRIER = "listCourrierLienByIdCourrier";
	private static final String LIST_LIENS_COURRIERS_BY_ID_COURRIER_LIEN = "listLiensCourriersByIdCourrierLien";
	private static final String LIST_LIENS_COURRIERS_BY_COURRIER_LIEN = "listCourrierLiensByCourrierId";
	private static final String  LIST_LIENS_COURRIERS="listCourrierByLien";
	private static final String COURRIER_LIEN_BY_ID_LIEN_COURRIER_AND_TYPE_LIEN = "CourrierLienByIdLienCourrierAndTypeLien";
	private static final String COURRIER_LIENS_BY_ID_COURRIER = "courrierLiensByIdCourrier";
	private static final String COURRIER_LIENS_BY_ID_LIEN_COURRIER = "courrierLiensByIdLienCourrier";
	private static final String LIST_TRANSACTION_DESTINATION_BY_TYPE = "listTransactionDestinationByType";
	private static final String ACCUSE_RECEPTION_BY_ID_COURRIER = "accuseReceptionByIdCourrier";
	private static final String LIST_ETAT_BY_CATEGORIE = "listEtatByCategorie";
	private static final String LIST_TRANSACTION_DESTINATION_FOR_BOOST_ALL_BY_TYPE = "listTransactionDestinationForBoostAllByType";
	private static final String LIST_TRANSACTION_DESTINATION_FOR_BOOST_BY_TYPE = "listTransactionDestinationForBoostByType";
	private static final String LIST_TRANSACTION_DESTINATION_FOR_BOOST_OUT_OF_DATE_BY_TYPE = "listTransactionDestinationForBoostOutOfDateByType";
	private static final String LIST_TRANSACTION_DESTINATION_FOR_BOOST_OUT_OF_DATE_BY_TYPE_TRAITE = "listTransactionDestinationForBoostOutOfDateByTypeTraite";

	// Workflow
	private static final String LIST_WORKFLOW_BY_NATURE = "listWorkflowByIdNature";
	private static final String LIST_TRANSACTION_BY_DOSSIER_ID_AND_TYPE = "listTransactionByDossierIdAndType";
	private static final String LIST_TRANSACTION_BY_DOSSIER_ID_AND_ID_UTILISATEUR = "listTransactionByDossierIdAndIdUtilisateur";
	// PP&PM
	private static final String LIST_ExpdestexterneByMAIL = "listExpdestexterneByMail";

	// Evenenment
	private static final String LIST_EVENEMENT_BY_REFERENCE_EVENEMENT = "listEvenementByRef";
	private static final String LIST_EVENEMENT_BY_LIBELLE_EVENEMENT = "listEvenementBylibelle";

	// Notification
	private static final String LIST_NOTIFICATION_BY_REFERENCE_NOTIFICATION = "listNotificationByRef";
	private static final String LIST_NOTIFICATION_BY_REFERENCE_EVENEMENT = "listNotificationByEvenement";
	private static final String LIST_NOTIFICATION_BY_REFERENCE_EVENEMENT_LIBELLE = "listNotificationByEvenementAndLibelle";
	private static final String LIST_NOTIFICATION_BY_EVENEMENT_ETAT = "listNotificationByEvenementAndEtat";

	// User notification
	private static final String LIST_NOTIFICATION_BY_USER = "listNotificationByUser";
	private static final String LIST_NOTIFICATION_BY_USER_NOTIFICATION = "listNotificationByUserAndNotification";

	// Log
	private static final String LIST_LOG_BY_REFERENCE_LOG = "listLogByRef";
	private static final String LIST_LOG_BY_REFERENCE_EVENEMENT = "listLogByEvenement";

	// Template Notification
	private static final String LIST_TEMPLATE_NOTIFICATION_BY_REFERENCE_NOTIFICATION = "listTemplateNotificationByNotification";

	// Template Log
	private static final String LIST_TEMPLATE_LOG_BY_REFERENCE_LOG = "listTemplateLogByLog";

	// Variables de notification listVariableByRef
	private static final String LIST_VARIABLE_BY_REFERENCE_VARIABLE = "listVariableByRef";

	// variables Notification (intermidiére)
	private static final String LIST_VARIABLE_NOTIFICATION_BY_NOTIFICATION = "listVariablesNotificationByNotification";

	private static final String TRANSACTION_ANNOTATION_BY_ID_TRANSACTION = "deleteTransactionAnnotationByIdTransaction";
	private static final String TRANSACTION_DESTINATION_BY_ID_TRANSACTION = "deleteTransactionDestinationByIdTransaction";
	// Raison
	private static final String LIST_RAISON_BY_ID = "listRaisonById";
	private static final String LIST_RAISON_BY_Libelle = "listRaisonByLibelle";
	private static final String LIST_RAISON_BY_Categorie = "listRaisonByCategorie";

	private static final String LIST_TRANSACTION_DESTINATION_FOR_BOOST_ARRIVAL_ALL_BY_TYPE = "listTransactionDestinationForBoostArrivalAllByType";
	private static final String LIST_TRANSACTION_DESTINATION_FOR_BOOST_ARRIVAL_BY_TYPE = "listTransactionDestinationForBoostArrivalByType";
	private static final String LIST_TRANSACTION_DESTINATION_FOR_BOOST_ARRIVAL_OUT_OF_DATE_BY_TYPE = "listTransactionDestinationForBoostArrivalOutOfDateByType";

	private static final String LIST_TRANSACTION_DESTINATION_FOR_BOOST_LIVING_ALL_BY_TYPE = "listTransactionDestinationForBoostLivingAllByType";
	private static final String LIST_TRANSACTION_DESTINATION_FOR_BOOST_LIVING_BY_TYPE = "listTransactionDestinationForBoostLivingByType";
	private static final String LIST_TRANSACTION_DESTINATION_FOR_BOOST_LIVING_OUT_OF_DATE_BY_TYPE = "listTransactionDestinationForBoostLivingOutOfDateByType";

	// Type Evenement
	private static final String LIST_TYPEEVENEMENT_BY_REFERENCE = "listTypeEvenementByRef";
	private static final String LIST_TYPEEVENEMENT_BY_LIBELLE = "listTypeEvenementBylibelle";

	// Message
	private static final String LIST_MESSAGE_BY_USER = "listMessageByUser";
	private static final String LIST_LOG_BY_LIBELLE = "listLogByLibelle";

	private static final String LIST_MENU_PRINCIPAL = "listMenuPrincipal";
	private static final String LIST_SOUS_MENU_BY_ID_MENU_PRINCIPAL = "listSousMenuByIdMenuPrincipal";
	private static final String LIST_MENUS_BY_ID_DROIT = "listMenusByIdDroit";
	private static final String MENU_DROIT_BY_ID_DROIT = "menuDroitByIdDroit";

	private static final String LIST_TRANSACTION_DESTINATION_BOCC = "listTransactionDestinationBoccJour";
	private static final String LIST_TRANSACTION_BOCC_JOUR = "listTransactionBoccJour";
	private static final String LIST_COURRIER_NECESSITANT_REPONSE = "listCourrierNecessitantReponse";

	// Menu
	private static final String LIST_MENU_PRINCIPAL_BY_LIBELLE = "listMenuPrincipalByLibelle";
	private static final String LIST_MENU_PRINCIPAL_BY_ID_MENU = "listMenuPrincipalByIdMenu";
	private static final String LIST_MENU_PRINCIPAL_BY_ID_ORDER = "listMenuPrincipalByIdOrder";
	private static final String MENU_SOUS_MENU_BY_ID_MENU_PRINCIPAL = "menuSousMenuByIdMenuPrincipal";
	private static final String MENU_DROIT_BY_ID_MENU = "menuDroitByIdMenu";
	private static final String SOUS_MENU_BY_ID_MENU_PRINCIPAL = "sousMenuByIdMenuPrincipal";
	private static final String LIST_NON_AFFECTED_SUB_MENU = "listNonAffectedSubMenu";

	// pays/gov
	private static final String LIST_GOUVERNERAT_BY_REFERENCEPAYS = "listGouverneratByRefPays";
	private static final String LIST_VILLE_BY_REFERENCEGOUVERNERAT = "listVilleByRefGouvernerat";
	private static final String LIST_PAYS_BY_LIBELLE = "listPaysByLibelle";
	private static final String LIST_GOUVERNORAT_BY_LIBELLE = "listGouvernoratByLibelle";
	private static final String LIST_VILLE_BY_LIBELLE = "listVilleByLibelle";
	private static final String LIST_PAYS_BY_REF = "listPaysByRef";
	private static final String LIST_GOV_BY_REF = "listGovByRef";
	private static final String LIST_VILLE_BY_REF = "listVilleByRef";

	// Mailing
	private static final String LIST_ContactGroupecontactByIdGroupecontact = "listGroupecontactpppmByIdGroupecontact";
	private static final String ContactGroupecontactByIdGroupecontact = "deleteContactGroupecontactByIdGroupecontact";
	private static final String LIST_ContactGroupeMailingByIdGroupecontact = "listGroupecontactmailingByIdGroupecontact";
	private static final String LIST_SUJETMAILING_BY_ID = "listSujetmailingById";
	private static final String LIST_CONTACT_MAILING_BY_ID_MAILING = "listContactMailingbyIdMailing";
	private static final String LIST_CONTACT_MAILING_BY_ID_GROUPE_AND_ID_MAILING = "listContactMailingbyIdGroupeAndIdMailing";

	// Sauvegardehistorique
	private static final String LIST_Sauvegardehistorique_BY_Date = "listSauvegardehistoriqueByDate";

	// Archive
	private static final String LIST_TRANSACTION_BY_DATE = "listTransactionByDate";

	// **
	// ** Relance
	private static final String LIST_TRANSACTION_DESTINATION_BOOST_BOC = "listTransactionDestinationForBoostBOC";
	private static final String LIST_TRANSACTION_DESTINATION_FOR_BOOST_OUT_OF_DATE_BOC = "listTransactionDestinationForBoostOutOfDateBOC";
	private static final String LIST_TRANSACTION_DESTINATION_FOR_BOOST_OUT_OF_DATE_BOC_TRAITE = "listTransactionDestinationForBoostOutOfDateBOCTraite";
	private static final String LIST_TRANSACTION_DESTINATION_FOR_BOOST_ALL_BOC = "listTransactionDestinationForBoostAllBOC";

	// ** Mail
	private static final String LIST_MAIL_BY_DELETE_STATE = "listMailByDeleteState";

	// ** expDest
	private static final String LIST_EXP_DEST_EXTERNE_BY_TYPE = "listExpDestExterneByType";
	private static final String COUNT_TRANSACTION_BY_IDEXPDESTEXTERNE = "countTransactionByIdExpdestExterne";
	private static final String LIST_EXPDEST_EXTERNE_JUST_PP_AND_PM = "listExpDestExternJustPpAndPm";

	private static final String LIST_EXPDEST_EXTERNE_USER_BY_LDAP_UID = "listExpDestExterneUserByLdapUID";
	private static final String DELETE_GROUP_CONTACT_PP_PM_BY_IDEXPDESTEXTERNE ="deleteGroupeContactPpPmByIdExpDestExterne";
	// ** Courrier
	private static final String LIST_COURRIER_BY_ID_TRANSATION = "listCourrierByIdTransaction";

	// ** Statistique
	private static final String COUNT_TRANSACTION_DESTINATION_BOOST_BOC = "countTransactionDestinationForBoostBOC";
	private static final String COUNT_TRANSACTION_DESTINATION_FOR_BOOST_BY_TYPE = "countTransactionDestinationForBoostByType";
	private static final String COUNT_TRANSACTION_DESTINATION_FOR_BOOST_OUT_OF_DATE_BOC = "countTransactionDestinationForBoostOutOfDateBOC";
	private static final String COUNT_TRANSACTION_DESTINATION_FOR_BOOST_OUT_OF_DATE_BY_TYPE = "countTransactionDestinationForBoostOutOfDateByType";
	private static final String COUNT_DOSSIER_BY_IDPROPRIETAIRE_AND_TYPE_DOSSIER = "countDossierByIdProprietaireAndTypeDossier";
	private static final String List_TRANSACTION_BY_DATE_TRANSACTION = "listTransactionByDateTansaction";
	
	// ** Test suppression lien entre courriers
	private static final String DELETE_LIENS_COURRIERS_BY_ID_COURRIERS = "deleteLiensCourriersByIdCourrier";
	private static final String DELETE_LIENs_COURRIERS_BY_LIENS_COURRIERS = "deleteLiensCourriersByLienCourrierId";
	
	private static final String TRANSACTION_DESTINATION_REELLE_BY_ID = "transactionDestinationReelleId";
	private static final String TRANSACTION_DESTINATION_REELLE_BY_DOSSIER_ID = "transactionDestinationReelleByDossierId";
	// documents - versionning
	private static final String LIST_DOCUMENT_BY_ID_COURRIER_AND_DELETE_FLAG = "listDocumentByIdCourrierAndDeleteFlag";
	private static final String DOCUMENT_CATEGORIE_BY_ID = "documentCategorieById";
	// Dossier
	private static final String DOSSIER_BY_ID_COURRIER_AND_ID_TYPE_DOSSIER = "dossierByIdCourrierAndIdTypeDossier";
	private static final String LIST_VARIABLE_BY_Libelle = "listVariableByLibelle";
	private static final String LIST_ANNOTATION_BY_DESTINATAIRE_TRANSACTION = "listeAnnotationsParDestinataireAndTransaction";
	private static final String LIST_NATURE_BY_CATEGORIE="listNaturesByCategorie";
	private static final String DONNESUPPLEMENTAIRE_BY_IDNATURE="deleteDonneeSupplementaireByIdNature";
	private static final String LISTE_DONNEE_SUPPLEMENTAIRE_NATURE_AFFECTES="listDonneeSupplementaireNatureAffectes";
	private static final String LISTE_DONNEE_SUPPLEMENTAIRE_TRANSMISSION_AFFECTES="listDonneeSupplementaireTransmissionAffectes";
	private static final String LISTE_DONNEE_SUPPLEMENTAIRE_NATURE_AAFFECTES="listDonneeSupplementaireNatureAAffectes";
	private static final String DONNEE_SUPPLEMENTAIRE_COURRIER="getDonneeSupplementaireCourrier";
	private static final String LIST_TRANSACTION_BY_IDANNOTATION= "listTransactionByIdAnnotation";
	private static final String REFERENCE_COURRIER_BY_DESTINATAIRE= "listRefCourrierByDestinataire";
	private static final String LIST_AO_CONSULTATION_BY_IDAOC = "listAoConsultationByIdAo";
	private static final String LIST_AO_CONSULTATION = "listAOConsultation";
	private static final String COURRIER_AVEC_RECEPTION_PHYSIQUE_BYETAT= "courrierAvecReceptionPhysiqueByByEtat";
	private static final String COURRIER_AVEC_RECEPTION_PHYSIQUE= "courrierAvecReceptionPhysique";
	private static final String LIST_CHEQUES_BY_ID= "listeChequeByCourrier";
	private static final String LIST_DOCUMENT_BY_ID_COURRIER_AND_TYPE = "listDocumentByIdCourrierAndType";
	
	// **
	/**
	 * @param dao
	 * 
	 *            the dao to set
	 */

	@Override
	@Autowired
	public void setDao(@Dao(type = Entite.class) IGenericDao dao) {
		this.dao = dao;
		System.out
				.println("****************Chargement Service GBO_V2013*******************");
	}

	@Override
	public void insert(Entite entity) throws Exception {
		dao.insert(entity);

	}

	@Override
	public void update(Entite entity) throws Exception {
		dao.update(entity);

	}

	@Override
	public void delete(Entite entity) throws Exception {
		dao.delete(entity);

	}

	@Override
	public <T extends Entite> List<T> getList(Class<T> t) throws Exception {
		return dao.getList(t);
	}

	@Override
	public <T extends Entite> List<T> getListUnique(Class<T> t)
			throws Exception {
		return dao.getListUnique(t);
	}

	@Override
	public List<Courrier> getListCourrier(String refrenceCourrier,
			String necessiteReponse, Date dateReception, String keywords,
			Date dateReponse) throws Exception {

		return dao.getListCourrier(refrenceCourrier, necessiteReponse,
				dateReception, keywords, dateReponse);
	}

	/*********************** Requettes ******************/
	@SuppressWarnings("unchecked")
	public List<Nature> getNatureByNom(String nom) {
		return (List<Nature>) dao.getListWithNamedQuery(LIST_NATURE_BY_NOM,
				with("nom", nom).parameters());

	}

	@SuppressWarnings("unchecked")
	public List<Transmission> getTransmissionByNom(String nom) {
		return (List<Transmission>) dao.getListWithNamedQuery(
				LIST_TRANSMISSION_BY_NOM, with("nom", nom).parameters());

	}

	@SuppressWarnings("unchecked")
	public List<Pays> listPaysByRef(int ref) {
		return (List<Pays>) dao.getListWithNamedQuery(LIST_PAYS_BY_REF,
				with("id", ref).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Gouvernerat> listGovByRef(int ref) {
		return (List<Gouvernerat>) dao.getListWithNamedQuery(LIST_GOV_BY_REF,
				with("id", ref).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Ville> listVilleByRef(int ref) {
		return (List<Ville>) dao.getListWithNamedQuery(LIST_VILLE_BY_REF,
				with("id", ref).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Confidentialite> getConfByNom(String nom) {
		return (List<Confidentialite>) dao.getListWithNamedQuery(
				LIST_CONFIDENTIALITE_BY_NOM, with("nom", nom).parameters());

	}

	@SuppressWarnings("unchecked")
	public List<Urgence> getUrgenceByNom(String nom) {
		return (List<Urgence>) dao.getListWithNamedQuery(LIST_URGENCE_BY_NOM,
				with("nom", nom).parameters());

	}

	@SuppressWarnings("unchecked")
	public List<Annotation> getAnnotationByLibelle(String nom) {
		return (List<Annotation>) dao.getListWithNamedQuery(
				LIST_ANNOTATION_BY_LIBELLE, with("nom", nom).parameters());

	}

	@SuppressWarnings("unchecked")
	public List<Typeutilisateur> getTypeUtilisateurByLibelle(String nom) {
		return (List<Typeutilisateur>) dao.getListWithNamedQuery(
				LIST_TYPEUTILISATEUR_BY_LIBELLE, with("id1", nom).parameters());

	}

	@SuppressWarnings("unchecked")
	public List<Typeutilisateur> getTypeUtilisateurById(int ref) {
		return (List<Typeutilisateur>) dao.getListWithNamedQuery(
				LIST_TYPEUTILISATEUR_BY_ID, with("id1", ref).parameters());

	}

	@SuppressWarnings("unchecked")
	public List<Typedossier> getTypeDossierById(int ref) {
		return (List<Typedossier>) dao.getListWithNamedQuery(
				LIST_TypeDossier_BY_ID, with("id", ref).parameters());

	}

	@SuppressWarnings("unchecked")
	public List<Typedossier> getTypeDossierByLibelle(String nom) {
		return (List<Typedossier>) dao.getListWithNamedQuery(
				LIST_TYPEDOSSIER_BY_LIBELLE, with("nom", nom).parameters());

	}

	@SuppressWarnings("unchecked")
	public List<Typetransaction> getTypeTransactionById(int ref) {
		return (List<Typetransaction>) dao.getListWithNamedQuery(
				LIST_TypeTransaction_BY_ID, with("id", ref).parameters());

	}

	@SuppressWarnings("unchecked")
	public List<Typetransaction> getTypeTransactionByLibelle(String nom) {
		return (List<Typetransaction>) dao
				.getListWithNamedQuery(LIST_TYPE_TRANSACTION_BY_LIBELLE,
						with("nom", nom).parameters());

	}

	@SuppressWarnings("unchecked")
	public List<CourrierDossier> getCourrierDossierByIdCourrier(int ref) {
		return (List<CourrierDossier>) dao.getListWithNamedQuery(
				LIST_COURRIERDOSSIER_BY_IDCOURRIER, with("id", ref)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<CourrierDossier> getCourrierDossierByIdDossier(int ref) {
		return (List<CourrierDossier>) dao
				.getListWithNamedQuery(LIST_COURRIERDOSSIER_BY_IDDOSSIER,
						with("id", ref).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getTransactionByIdDossier(int ref) {
		return (List<Transaction>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_BY_IDDOSSIER, with("id", ref).parameters());

	}

	//TTTT
	
	public List<Integer> getTransactionByIdDossierPourSuivi(int refDossier) {
		return (List<Integer>) dao.getTransactionByIdDossierPourSuivi(refDossier);
		}
	public List<Integer> getTransactionByIdDossierPourSuiviDestExterne(int refDossier) {
		return (List<Integer>) dao.getTransactionByIdDossierPourSuiviDestExterne(refDossier);
		}	
	
	@SuppressWarnings("unchecked")
	public List<Transaction> getTransactionByIdDossierByEtat(int ref) {
		return (List<Transaction>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_BY_IDDOSSIER_Etat, with("id", ref).parameters());

	}
	@SuppressWarnings("unchecked")
	public List<Dossier> getDossierByIdDossier(int ref) {
		return (List<Dossier>) dao.getListWithNamedQuery(LIST_DOSSIER_BY_ID,
				with("id", ref).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Expdestexterne> getExpediteurById(int ref) {
		return (List<Expdestexterne>) dao.getListWithNamedQuery(
				LIST_EXPEDITEUR_BY_ID, with("id", ref).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Annotation> getAnnotationByIdAnotation(int idA) {
		return (List<Annotation>) dao.getListWithNamedQuery(
				LIST_ANNOTATION_BY_ID, with("idA", idA).parameters());

	}

	@SuppressWarnings("unchecked")
	public List<Nature> getNatureById(int id) {
		return (List<Nature>) dao.getListWithNamedQuery(LIST_NATURE_BY_ID,
				with("id", id).parameters());

	}

	@SuppressWarnings("unchecked")
	public List<Transmission> getTransmissionById(int id) {
		return (List<Transmission>) dao.getListWithNamedQuery(
				LIST_TRANSMISSION_BY_ID, with("id", id).parameters());

	}

	@SuppressWarnings("unchecked")
	public List<Confidentialite> getConfidentialiteById(int id) {
		return (List<Confidentialite>) dao.getListWithNamedQuery(
				LIST_CONFIDENTIALITE_BY_ID, with("id", id).parameters());

	}

	@SuppressWarnings("unchecked")
	public List<Urgence> getUrgenceById(int id) {
		return (List<Urgence>) dao.getListWithNamedQuery(LIST_URGENCE_BY_ID,
				with("id", id).parameters());

	}

	@SuppressWarnings("unchecked")
	public List<Document> getDocumentByIdCourrier(int id2) {
		return (List<Document>) dao.getListWithNamedQuery(
				LIST_DOCUMENT_BY_ID_COURRIER, with("id2", id2).parameters());

	}

	@SuppressWarnings("unchecked")
	public List<TransactionAnnotation> getAnnotationByIdTransaction(int idA) {
		return (List<TransactionAnnotation>) dao.getListWithNamedQuery(
				LIST_ANNOTATATION_BY_IDTRANSACTION, with("idA", idA)
						.parameters());

	}

	@SuppressWarnings("unchecked")
	public List<Courrier> getCourrierByDateJourId(int id, Date date) {
		return (List<Courrier>) dao.getListWithNamedQuery(
				LIST_COURRIER_BY_DATE_JOUR_ID, with("id", id).and("date", date)
						.parameters());

	}

	@SuppressWarnings("unchecked")
	public List<Courrier> getCourrierByIdCourrier(int id) {
		return (List<Courrier>) dao.getListWithNamedQuery(LIST_COURRIER_BY_ID,
				with("id", id).parameters());

	}
	@SuppressWarnings("unchecked")
	public List<Courrier> getCourrierByRefCourrier(String ref) {
		return (List<Courrier>) dao.getListWithNamedQuery(LIST_COURRIER_BY_REF,
				with("ref", ref).parameters());
		
	}
	@SuppressWarnings("unchecked")
	public List<Courrier> getCourrierByIdCourrierCloturer() {
		return (List<Courrier>) dao.getListWithNamedQuery(LIST_COURRIER_BY_I_CLOTURE);
		
	}
	@SuppressWarnings("unchecked")
	public List<Courrier> getCourrierInterene(int id) {
		return (List<Courrier>) dao.getListWithNamedQuery(LIST_COURRIER_INTERENE,
				with("id", id).parameters());
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Courrier> getListCourrierTypeOrdreById(String type, int year) {
		return (List<Courrier>) dao.getListWithNamedQuery(LIST_COURRIERTYPEORDRE_BY_ID,
				with("type", type).and("year", year).parameters());

	}

	@SuppressWarnings("unchecked")
	public List<Courrier> rechercheCourrier(String refrenceCourrier,
			String necessiteReponse, Date dateReception, String keywords,
			Date dateReponse) {
		return (List<Courrier>) dao.getListWithNamedQuery(
				RECHERCHE_COURRIER,
				with("id", refrenceCourrier).and("daterrec", dateReception)
						.and("nrep", necessiteReponse)
						.and("daterrep", dateReponse).and("keyw", keywords)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Courrier> getCourrierByDateId(int id, Date dateD, Date dateF) {
		return (List<Courrier>) dao.getListWithNamedQuery(
				LIST_COURRIER_BY_DATE,
				with("id", id).and("dateD", dateD).and("dateF", dateF)
						.parameters());

	}

	@SuppressWarnings("unchecked")
	public List<Courrier> getCourrierBySemId(int id, Date debutS, Date finS) {
		return (List<Courrier>) dao.getListWithNamedQuery(
				LIST_COURRIER_BY_SEM_ID, with("id", id).and("débutS", debutS)
						.and("finS", finS).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Pp> getPPByReferenceExpediteur(int id) {
		return (List<Pp>) dao.getListWithNamedQuery(LIST_PpByIDExpediteur,
				with("id", id).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Pm> getPMByReferenceExpediteur(int id) {
		return (List<Pm>) dao.getListWithNamedQuery(LIST_PmByIDExpediteur,
				with("id", id).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Expdestexterne> listExpediteurByStatus(String s) {
		return (List<Expdestexterne>) dao.getListWithNamedQuery(
				listExpediteurByStatus, with("id1", s).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Pp> listPPByIDPM(int id) {
		return (List<Pp>) dao.getListWithNamedQuery(LIST_PP_BY_ID_PM,
				with("id", id).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Pm> getPMByIDPM(int id) {
		return (List<Pm>) dao.getListWithNamedQuery(LIST_PM_BY_ID_PM,
				with("id", id).parameters());
	}
	@SuppressWarnings("unchecked")
	public List<Pp> listPPByTypeUser() {
		return (List<Pp>) dao.getListWithNamedQuery(LIST_PP_BY_TYPE_USER);
	}
	
	@SuppressWarnings("unchecked")
	public List<Pm> getPMByTypeUser() {
		return (List<Pm>) dao.getListWithNamedQuery(LIST_PM_BY_TYPE_USER);
	}

	// Etat
	@SuppressWarnings("unchecked")
	public List<Etat> listEtatByRef(int t) {
		return (List<Etat>) dao.getListWithNamedQuery(LIST_ETAT_BY_REFERENCE,
				with("id1", t).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Etat> listEtatByLibelle(String t) {
		return (List<Etat>) dao.getListWithNamedQuery(LIST_ETAT_BY_LIBELLE,
				with("id1", t).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Utilisateur> getUtilisateurById(int id) {
		return (List<Utilisateur>) dao.getListWithNamedQuery(
				LIST_UTILISATEUR_BY_ID, with("id", id).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getDestinationByIdTransaction(int id) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_DESTINATAIRE_BY_ID_TRANSACTION, with("id", id)
						.parameters());
	}

	public void deleteDossierCourrierByIdDossier(int id) {
		dao.deleteWithNamedQuery(DOSSIERCOURRIER_BY_IDDOSSIER, with("id1", id)
				.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Expdest> getListExpDestById(int id, String type) {
		return (List<Expdest>) dao.getListWithNamedQuery(LIST_EXPDEST_BY_ID,
				with("id", id).and("type", type).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Expdest> getListExpDestByIdExpDest(int id) {
		return (List<Expdest>) dao.getListWithNamedQuery(
				LIST_EXPDEST_BY_ID_EXPDEST, with("id", id).parameters());
	}
	@SuppressWarnings("unchecked")
	public List<Expdestexterne> getListExpDestExterne(int id) {
		return (List<Expdestexterne>) dao.getListWithNamedQuery(
				listExpDestExterne, with("id", id).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionDestinationByIdExpDest(
			int id) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_DESTINATION_BY_ID_EXPDEST, with("id", id)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionDestinationByIdTransaction(
			int id) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_DESTINATION_BY_ID_TRANSACTION, with("id", id)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionByIdTransaction(int id) {
		return (List<Transaction>) dao
				.getListWithNamedQuery(LIST_TRANSACTION_BY_ID_TRANSACTION,
						with("id", id).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionByIdTransactionJour(int id,
			Date date) {
		return (List<Transaction>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_BY_ID_TRANSACTION_JOUR,
				with("id", id).and("date", date).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionByIdExpDest(int id) {
		return (List<Transaction>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_BY_ID_EXPDEST, with("id", id).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionByIdExpDestJour(int id, Date date) {
		return (List<Transaction>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_BY_ID_EXPDEST_JOUR,
				with("id", id).and("date", date).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getTransactionByIdTransactionAndFirstOrder(
			int idTr, int order) {
		return (List<Transaction>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_BY_ID_TRANSACTION_AND_FIRST_ORDER,
				with("id", idTr).and("id1", order).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Commentaire> getListCommentaireByIdTransaction(int id) {
		return (List<Commentaire>) dao
				.getListWithNamedQuery(LIST_COMMENTAIRE_BY_ID_TRANSACTION,
						with("id", id).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Commentaire> getListCommentaireByIdCourrier(int id, int id1) {
		return (List<Commentaire>) dao.getListWithNamedQuery(
				LIST_COMMENTAIRE_BY_ID_COURRIER, with("id", id).and("id1", id1)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Commentaire> getListCommentaireByIdUser(int id, int id1,
			String type) {
		return (List<Commentaire>) dao.getListWithNamedQuery(
				LIST_COMMENTAIRE_BY_ID_USER, with("id", id).and("id1", id1)
						.and("type", type).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Commentaire> getListCommentaireByIdUser(int id, int id1,
			int id2, String type) {
		return (List<Commentaire>) dao.getListWithNamedQuery(
				LIST_COMMENTAIRE_COURRIER_BY_ID_USER,
				with("id", id).and("id1", id1).and("id2", id2)
						.and("type", type).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionTest(int rowIndex,
			int numberOfRows) {
		return (List<Transaction>) dao.getListLimitedWithNamedQuery(
				LIST_TRANSACTION_TEST, rowIndex, numberOfRows);
	}

	public long getRowCount() {
		return dao.getRowCount(LIST_TRANSACTION_TEST_RECORD);
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionEnvoyesParResponsable(int id,
			String type, String type1) {
		return (List<Transaction>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_ENVOYES_FOR_RESPONSABLE,
				with("id", id).and("type1", type1).and("type", type)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionRecusAuResponsable(
			int id, String type, String type1) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_RECUS_AU_RESPONSABLE,
				with("id", id).and("type1", type1).and("type", type)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionEnvoyesParResponsableJour(
			int id, String type, String type1, Date date) {
		return (List<Transaction>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_ENVOYES_FOR_RESPONSABLE_JOUR, with("id", id)
						.and("type1", type1).and("type", type)
						.and("date", date).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionRecusAuResponsableJour(
			int id, String type, String type1, Date date) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_RECUS_AU_RESPONSABLE_JOUR,
				with("id", id).and("type1", type1).and("type", type)
						.and("date", date).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionEnvoyesParAgent(String type) {
		return (List<Transaction>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_ENVOYES_FOR_AGENT, with("type", type)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionRecusAuAgent(
			String type) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_RECUS_AU_AGENT, with("type", type)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionEnvoyesParAgentJour(String type,
			Date date) {
		return (List<Transaction>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_ENVOYES_FOR_AGENT_JOUR, with("type", type)
						.and("date", date).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionRecusAuAgentJour(
			String type, Date date) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_RECUS_AU_AGENT_JOUR,
				with("type", type).and("date", date).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionEnvoyesParSecretaire(
			String type, String type1) {
		return (List<Transaction>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_ENVOYES_FOR_SECRETARY, with("type1", type1)
						.and("type", type).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionRecusAuSecretaire(
			String type, String type1) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_RECUS_AU_SECRETARY,
				with("type1", type1).and("type", type).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionEnvoyesParSecretaireJour(
			String type, String type1, Date date) {
		return (List<Transaction>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_ENVOYES_FOR_SECRETARY_JOUR,
				with("type1", type1).and("type", type).and("date", date)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionRecusAuSecretaireJour(
			String type, String type1, Date date) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_RECUS_AU_SECRETARY_JOUR, with("type1", type1)
						.and("type", type).and("date", date).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Commentaire> getListPartageByIdTransaction(int id, int id1) {
		return (List<Commentaire>) dao.getListWithNamedQuery(
				LIST_PARTAGE_BY_ID_TRANSACTION, with("id", id).and("id1", id1)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Droit> getListDroitByLibelle(String libelle) {
		return (List<Droit>) dao.getListWithNamedQuery(LIST_DROIT_BY_LIBELLE,
				with("libelle", libelle).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Menu> getListMenuByIdDroit(int id) {
		return (List<Menu>) dao.getListWithNamedQuery(LIST_MENU_BY_ID_DROIT,
				with("id", id).parameters());
	}

	public List<Transaction> testRecheche(Integer id, String type) {
		return (List<Transaction>) dao.testRecherheMulticritere(id, type);
	}

	@SuppressWarnings("unchecked")
	public List<Annotation> listAnnotationByOrdre() {
		return (List<Annotation>) dao.getListWithNamedQuery(AnnotationByOrdre);
	}

	@SuppressWarnings("unchecked")
	public List<Urgence> listUrgenceByOrdre() {
		return (List<Urgence>) dao.getListWithNamedQuery(UrgenceByOrdre);
	}

	@SuppressWarnings("unchecked")
	public List<Confidentialite> ConfidentialiteByOrdre() {
		return (List<Confidentialite>) dao
				.getListWithNamedQuery(ConfidentialiteByOrdre);
	}

	@SuppressWarnings("unchecked")
	public List<Nature> NatureByOrdreRef() {
		return (List<Nature>) dao.getListWithNamedQuery(NatureByOrdreRef);
	}

	@SuppressWarnings("unchecked")
	public List<Direction> listDirectionByIdParent(int ref) {
		return (List<Direction>) dao.getListWithNamedQuery(DirectionByRefrence,
				with("id1", ref).parameters());
	}

	public long countListRowConfidentialite(int id1) {
		return dao.countList(COUNTLIST, with("id1", id1).parameters());
	}

	public long countListRowrgence(int id1) {
		return dao.countList(COUNTLISTURGENCE, with("id1", id1).parameters());
	}

	public long countListRowUrgenceDossier(int id1) {
		return dao.countList(COUNTLISTURGENCEDOSSIER, with("id1", id1)
				.parameters());
	}

	public long countListRowConfedentilaiteDossier(int id1) {
		return dao.countList(COUNTLISTCONFEDENTIALITEDOSSIER, with("id1", id1)
				.parameters());
	}

	public List<Transaction> recherheMulticritereCourrier(Integer id,
			String type, String type1, Date transactionDate,
			List<Integer> listExpediteur, List<Integer> listDossier) {
		return (List<Transaction>) dao.recherheMulticritereCourrier(id, type,
				type1, transactionDate, listExpediteur, listDossier);
	}

	public List<CourrierDossier> listDossier(String objet, String motCles,
			String necessiteReponse, Integer idTransmission, Integer idNature,
			Integer idConfidentialite, Integer idUrgence) {
		return (List<CourrierDossier>) dao.listDossier(objet, motCles,
				necessiteReponse, idTransmission, idNature, idConfidentialite,
				idUrgence);
	}

	@SuppressWarnings("unchecked")
	public List<Aide> aideByInterface(String titreInterface) {
		return (List<Aide>) dao.getListWithNamedQuery(AIDE_BY_TITREINTERFACE,
				with("titreInterface", titreInterface).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Dossier> listDossierByIdProprietaire(int ref) {
		return (List<Dossier>) dao.getListWithNamedQuery(
				DOSSIER_BY_IDPROPRIETAIRE, with("id", ref).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Courrier> listCourrierNecessitnatReponse(String cas) {
		return (List<Courrier>) dao.getListWithNamedQuery(
				LISTE_COURRIER_NECESSITANT_REPONSE, with("id", cas)
						.parameters());
	}

	// VARIABLES
	@SuppressWarnings("unchecked")
	public List<Variables> listVariablesById(int s) {
		return (List<Variables>) dao.getListWithNamedQuery(
				LIST_VARIABLES_BY_ID, with("id1", s).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Variables> listVariablesByLibelle(String s) {
		return (List<Variables>) dao.getListWithNamedQuery(
				LIST_VARIABLES_BY_Libelle, with("id1", s).parameters());
	}

	// SOCIETE
	@SuppressWarnings("unchecked")
	public List<Societe> listSocieteById(int s) {
		return (List<Societe>) dao.getListWithNamedQuery(LIST_SOCIETE_BY_ID,
				with("id1", s).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Societe> listSocieteByLibelle(String s) {
		return (List<Societe>) dao.getListWithNamedQuery(
				LIST_SOCIETE_BY_Libelle, with("id1", s).parameters());
	}

	// Armoire
	@SuppressWarnings("unchecked")
	public List<Classement_archivage_niveau_02> listArmoireById(int s) {
		return (List<Classement_archivage_niveau_02>) dao.getListWithNamedQuery(LIST_Armoire_BY_ID,
				with("id1", s).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Classement_archivage_niveau_02> listArmoireByLibelle(String s) {
		return (List<Classement_archivage_niveau_02>) dao.getListWithNamedQuery(
				LIST_Armoire_BY_Libelle, with("id1", s).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Classement_archivage_niveau_02> listArmoireByEtat(String s) {
		return (List<Classement_archivage_niveau_02>) dao.getListWithNamedQuery(LIST_Armoire_BY_ETAT,
				with("id1", s).parameters());
	}

	// Etages
	@SuppressWarnings("unchecked")
	public List<Classement_archivage_niveau_01> listEtagesById(int s) {
		return (List<Classement_archivage_niveau_01>) dao.getListWithNamedQuery(LIST_Etages_BY_ID,
				with("id1", s).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Classement_archivage_niveau_01> listEtagesByIdArmoire(int s) {
		return (List<Classement_archivage_niveau_01>) dao.getListWithNamedQuery(
				LIST_Etages_BY_ID_Armoire, with("id1", s).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Classement_archivage_niveau_01> listEtagesByLibelle(String s) {
		return (List<Classement_archivage_niveau_01>) dao.getListWithNamedQuery(LIST_Etages_BY_Libelle,
				with("id1", s).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Classement_archivage_niveau_01> listEtagesByIdArmoireAndEtat(int s, String etat) {
		return (List<Classement_archivage_niveau_01>) dao.getListWithNamedQuery(
				LIST_Etages_BY_ID_Armoire_AND_ETAT,
				with("id1", s).and("id2", etat).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Courrier> listCourrierByIdEtages(int s) {
		return (List<Courrier>) dao.getListWithNamedQuery(
				LIST_COURRIER_BY_IDETAGES, with("id", s).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<ArmoirBoiteArchiveUser> listArmoireBoiteByUser(int s) {
		return (List<ArmoirBoiteArchiveUser>) dao.getListWithNamedQuery(
				LIST_ARMBT_BY_IDUSER, with("id", s).parameters());
	}

	/****** Methodes integrer *************/
	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionEnvoyesParAgentBocJour(
			String type, String type1, Date date) {
		return (List<Transaction>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_ENVOYES_FOR_AGENT_BOC_JOUR, with("type", type)
						.and("type1", type1).and("date", date).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionEnvoyesParAgentBoc(String type,
			String type1) {
		return (List<Transaction>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_ENVOYES_FOR_AGENT_BOC,
				with("type", type).and("type1", type1).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionRecusAuAgentBocJour(
			String type, String type1, Date date) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_RECUS_AU_AGENT_BOC_JOUR, with("type", type)
						.and("type1", type1).and("date", date).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionRecusAuAgentBoc(
			String type, String type1) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_RECUS_AU_AGENT_BOC,
				with("type", type).and("type1", type1).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionEnvoyesUniqueParResponsableJour(
			String type, String type1, Date date) {
		return (List<Transaction>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_ENVOYES_FOR_SECRETARY_JOUR,
				with("type1", type1).and("type", type).and("date", date)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionRecusUniqueAuResponsableJour(
			String type, String type1, Date date) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_RECUS_AU_SECRETARY_JOUR, with("type1", type1)
						.and("type", type).and("date", date).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionEnvoyesUniqueParResponsable(
			String type, String type1) {
		return (List<Transaction>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_ENVOYES_FOR_SECRETARY, with("type1", type1)
						.and("type", type).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionRecusUniqueAuResponsable(
			String type, String type1) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_RECUS_AU_SECRETARY,
				with("type1", type1).and("type", type).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionEnvoyesUniqueParSecretaireJour(
			String typeSecretaire, Date date) {
		return (List<Transaction>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_ENVOYES_FOR_AGENT_JOUR,
				with("type", typeSecretaire).and("date", date).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionRecusUniqueAuSecretaireJour(
			String typeSecretaire, Date date) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_RECUS_AU_AGENT_JOUR,
				with("type", typeSecretaire).and("date", date).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionEnvoyesUniqueParSecretaire(
			String typeSecretaire) {
		return (List<Transaction>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_ENVOYES_FOR_AGENT,
				with("type", typeSecretaire).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionRecusUniqueAuSecretaire(
			String typeSecretaire) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_RECUS_AU_AGENT, with("type", typeSecretaire)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionEnvoyesParSubordonnesJour(
			int id, Date date) {
		return (List<Transaction>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_ENVOYES_PAR_SUBORDONNES_JOUR, with("id", id)
						.and("date", date).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionRecusAuSubordonnesJour(
			int id, Date date) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_RECUS_AU_SUBORDONNES_JOUR,
				with("id", id).and("date", date).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionEnvoyesParSubordonnes(int id) {
		return (List<Transaction>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_ENVOYES_PAR_SUBORDONNES, with("id", id)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionRecusAuSubordonnes(
			int id) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_RECUS_AU_SUBORDONNES, with("id", id)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionEnvoyesParSousUniteJour(int id,
			Date date) {
		return (List<Transaction>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_ENVOYES_PAR_SOUS_UNITE_JOUR, with("id", id)
						.and("date", date).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionRecusAuSousUniteJour(
			int id, Date date) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_RECUS_AU_SOUS_UNITE_JOUR,
				with("id", id).and("date", date).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionEnvoyesParSousUnite(int id) {
		return (List<Transaction>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_ENVOYES_PAR_SOUS_UNITE, with("id", id)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionRecusAuSousUnite(
			int id) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_RECUS_AU_SOUS_UNITE, with("id", id)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionBocJour(int id, Date date,
			String type) {
		return (List<Transaction>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_BOC_JOUR, with("id", id).and("date", date)
						.and("type", type).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionBoc(int id, String type) {
		return (List<Transaction>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_BOC, with("id", id).and("type", type)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionDestinationBocJour(
			String type, int id, Date date) {
		return (List<TransactionDestination>) dao
				.getListWithNamedQuery(LIST_TRANSACTION_DESTINATION_BOC_JOUR,
						with("type", type).and("date", date).and("id", id)
								.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionDestinationBoc(
			String type, int id) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_DESTINATION_BOC,
				with("type", type).and("id", id).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionDestinationBocSansEtat(
			String type) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_DESTINATION_BOC_SANS_ETAT, with("type", type)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getTransactionExpediteurByIdTransactionDestinationReelle(
			int id, int id1) {
		return (List<Transaction>) dao
				.getListWithNamedQuery(
						LIST_TRANSACTION_EXPEDITEUR_BY_ID_TRANSACTION_DESTINANTION_REELLE,
						with("id", id).and("id1", id1).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getTransactionByIdTransactionDestinationReelle(
			int id) {
		return (List<Transaction>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_BY_ID_TRANSACTION_DESTINANTION_REELLE,
				with("id", id).parameters());
	}
	
	@SuppressWarnings("unchecked")
	public List<Transaction> getTransactionByIdTransactionDestinationReelleByEtat(
			int id) {
		return (List<Transaction>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_BY_ID_TRANSACTION_DESTINANTION_REELLE_ETAT,
				with("id", id).parameters());
	}
	
	@SuppressWarnings("unchecked")
	public List<Lienscourriers> getListCourrierLiensByIdCourrier(Integer id) {
		return (List<Lienscourriers>) dao.getListWithNamedQuery(
				LIST_COURRIER_LIEN_BY_ID_COURRIER, with("id", id).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<CourrierLiens> getListLiensCourrierByIdCourrierLien(int id) {
		return (List<CourrierLiens>) dao.getListWithNamedQuery(
				LIST_LIENS_COURRIERS_BY_ID_COURRIER_LIEN, with("id", id)
						.parameters());
	}
	@SuppressWarnings("unchecked")
	public List<CourrierLiens> getCourrierLiensByCourrierId(int id) {
		return (List<CourrierLiens>) dao.getListWithNamedQuery(
				LIST_LIENS_COURRIERS_BY_COURRIER_LIEN, with("id", id)
						.parameters());
	}
	
	@SuppressWarnings("unchecked")
	public List<Lienscourriers> getCourrierL(int id) {
		return (List<Lienscourriers>) dao.getListWithNamedQuery(
				LIST_LIENS_COURRIERS, with("id", id)
						.parameters());
	}

	public void deleteCourrierLiensByIdLiensCourrier(int id) {
		dao.deleteWithNamedQuery(COURRIER_LIENS_BY_ID_LIEN_COURRIER,
				with("id", id).parameters());
	}

	public void deleteCourrierLiensByIdCourrier(int id) {
		dao.deleteWithNamedQuery(COURRIER_LIENS_BY_ID_COURRIER, with("id", id)
				.parameters());
	}

	public List<Courrier> listDossier(String objet, String motCles,
			String description, String necessiteReponse,
			Integer idTransmission, Integer idNature,
			Integer idConfidentialite, Integer idUrgence) {
		return (List<Courrier>) dao.listDossier(objet, motCles, description,
				necessiteReponse, idTransmission, idNature, idConfidentialite,
				idUrgence);
	}

	public List<CourrierDossier> listCourrierDossier(List<Integer> list) {
		return (List<CourrierDossier>) dao.listCourrierDossier(list);
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getlistTransactionDestinationByType(
			String type) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_DESTINATION_BY_TYPE, with("type", type)
						.parameters());
	}

	// public List<Transaction> recherheMulticritereCourrierEnvoye(Integer
	// idBoc,
	// Integer id, String type, String type1, Date transactionDateDebut,
	// Date transactionDateFin, Date dateReponse,
	// List<Integer> listExpediteur, List<Integer> listDossier,
	// List<Integer> listDestinataire) {
	// return (List<Transaction>) dao.recherheMulticritereCourrierEnvoye(
	// idBoc, id, type, type1, transactionDateDebut,
	// transactionDateFin, dateReponse, listExpediteur, listDossier,
	// listDestinataire);
	// }
	public List<CourrierInformations> recherheMulticritereCourrierEnvoye(boolean isResponsable,
			List<Integer> listIdsSousUnites, List<Integer> listIdSubordonnes, String secretaire,
			String sub, String unit, String type, String type1, String typeSecretaire,
			Integer idUser, String object, String motCle, Map<Integer, String> listExp,
			Map<Integer, String> listDes, Integer idTransmission, Integer idNature,Integer idCategorie,
			Date dateLimiteRep, List<Integer> listIdAnnotation, String description,
			Integer idConfidentialite, Integer idUrgence, Date transactionDateDebut,
			Date transactionDateFin,Date dateCourrierReel,Date dateCourrierReelFin ,boolean isBoc, String typeCourrierBoc, String recuOrEnvoyer,
			List<Integer> listDestinataire, int firstRow, int numberOfRows, String oldRef,Integer refGeneral,
			String necessiteReponse, String courrierReference, List<String> annees, String DBType ,int etatCloturer,String courrierCopyTransfere,String courrierCopy,String colonne1,String colonne2,
			String colonne3,String colonne4,String colonne5,String colonne6,String colonne7,String colonne8,String colonne9,String colonne10,String colonne11,String colonne12,
			String colonne13,Date colonne14,Date colonne15,Date colonne16,Date colonne17,Date colonne18,Date colonne19,Date colonne20,
			String colonne21,Boolean colonne22,String courrierFlagInterne,List<Integer> listIdBocMembers) {
		return (List<CourrierInformations>) dao.recherheMulticritereCourrierEnvoye(isResponsable,
				listIdsSousUnites, listIdSubordonnes, secretaire, sub, unit, type, type1,
				typeSecretaire, idUser, object, motCle, listExp, listDes, idTransmission, idNature,idCategorie,
				dateLimiteRep, listIdAnnotation, description, idConfidentialite, idUrgence,
				transactionDateDebut, transactionDateFin,dateCourrierReel,dateCourrierReelFin, isBoc, typeCourrierBoc, recuOrEnvoyer,
				listDestinataire, firstRow, numberOfRows, oldRef,refGeneral, necessiteReponse, courrierReference, annees, DBType,etatCloturer, courrierCopyTransfere,courrierCopy,colonne1,colonne2,colonne3,colonne4,
				colonne5,colonne6,colonne7,colonne8,colonne9,colonne10,colonne11,colonne12,
				colonne13,colonne14,colonne15,colonne16,colonne17,colonne18,colonne19,colonne20,
				colonne21,colonne22,courrierFlagInterne,listIdBocMembers);
	}

	public List<Transaction> listTransactions(Date transactionDateDebut,
			Date transactionDateFin, Date dateReponse,
			List<Integer> listExpediteur, List<Integer> listDossier) {
		return (List<Transaction>) dao.listTransactions(transactionDateDebut,
				transactionDateFin, dateReponse, listExpediteur, listDossier);
	}

	// public List<TransactionDestination> recherheMulticritereCourrierRecu(
	// Integer id, String type, String type1,
	// List<Integer> listTransactionId, List<Integer> listIdTransaction,
	// List<Integer> listIdExpDest) {
	// return (List<TransactionDestination>) dao
	// .recherheMulticritereCourrierRecu(id, type, type1,
	// listTransactionId, listIdTransaction, listIdExpDest);
	// return null;
	// }
	public List<TransactionDestination> recherheMulticritereCourrierRecu(
			boolean isResponsable, List<Integer> listIdsSousUnites,
			List<Integer> listIdSubordonnes, String secretaire, String sub,
			String unit, String type, String type1, String typeSecretaire,
			Integer idUser, String object, String motCle,
			List<Integer> listExpediteur, List<Integer> listIdExpDest,
			Integer idTransmission, Integer idNature, Date dateLimiteRep,
			List<Integer> listIdAnnotation, String description,
			Integer idConfidentialite, Integer idUrgence,
			Date transactionDateDebut, Date transactionDateFin) {

		return (List<TransactionDestination>) dao
				.recherheMulticritereCourrierRecu(isResponsable,
						listIdsSousUnites, listIdSubordonnes, secretaire, sub,
						unit, type, type1, typeSecretaire, idUser, object,
						motCle, listExpediteur, listIdExpDest, idTransmission,
						idNature, dateLimiteRep, listIdAnnotation, description,
						idConfidentialite, idUrgence, transactionDateDebut,
						transactionDateFin);

	}

	@SuppressWarnings("unchecked")
	public List<Suivitransmissioncourrier> accuseReceptionByIdCourrier(int refcr) {
		return (List<Suivitransmissioncourrier>) dao
				.getListWithNamedQuery(ACCUSE_RECEPTION_BY_ID_COURRIER,
						with("id", refcr).parameters());

	}

	@SuppressWarnings("unchecked")
	public List<Etat> listEtatByCategorie(int cat) {
		return (List<Etat>) dao.getListWithNamedQuery(LIST_ETAT_BY_CATEGORIE,
				with("id1", cat).parameters());

	}

	@SuppressWarnings("unchecked")
	public List<Menu> getListSousMenuByIdMenu(int id, int id1) {
		return (List<Menu>) dao.getListWithNamedQuery(
				LIST_SOUS_MENU_BY_ID_MENU, with("id", id).and("id1", id1)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Workflow> listWorkflowByIdNature(int ref) {
		return (List<Workflow>) dao.getListWithNamedQuery(
				LIST_WORKFLOW_BY_NATURE, with("id", ref).parameters());

	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getTransactionDestinationForBoostAllByType(
			String type) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_DESTINATION_FOR_BOOST_ALL_BY_TYPE,
				with("type", type).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getTransactionDestinationForBoostByType(
			String type, String dateJour, String date) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_DESTINATION_FOR_BOOST_BY_TYPE,
				with("type", type).and("dateDebut", dateJour)
						.and("dateFin", date).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getTransactionDestinationForBoostOutOfDateByType(
			String type, String date) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_DESTINATION_FOR_BOOST_OUT_OF_DATE_BY_TYPE,
				with("type", type).and("date", date).parameters());
	}
	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getTransactionDestinationForBoostOutOfDateByTypeTraite(
			String type, Date date) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_DESTINATION_FOR_BOOST_OUT_OF_DATE_BY_TYPE_TRAITE,
				with("type", type).and("date", date).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionByDossierIdAndTypeIntervenant(
			String type, Integer dossierId) {
		return (List<Transaction>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_BY_DOSSIER_ID_AND_TYPE, with("type", type)
						.and("id", dossierId).parameters());

	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionByDossierIdAndIdUtilisateur(
			int id, Integer dossierId) {
		return (List<Transaction>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_BY_DOSSIER_ID_AND_ID_UTILISATEUR,
				with("id1", id).and("id", dossierId).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Expdestexterne> listExpdestexterneByMail(String mail) {
		return (List<Expdestexterne>) dao.getListWithNamedQuery(
				LIST_ExpdestexterneByMAIL, with("mail", mail).parameters());

	}

	// Evenement
	@SuppressWarnings("unchecked")
	public List<Evenement> listEvenementByRef(int ref) {
		return (List<Evenement>) dao.getListWithNamedQuery(
				LIST_EVENEMENT_BY_REFERENCE_EVENEMENT, with("id1", ref)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Evenement> listEvenementBylibelle(String s) {
		return (List<Evenement>) dao.getListWithNamedQuery(
				LIST_EVENEMENT_BY_LIBELLE_EVENEMENT, with("id1", s)
						.parameters());
	}

	// Notification
	@SuppressWarnings("unchecked")
	public List<Notification> listNotificationByRef(int ref) {
		return (List<Notification>) dao.getListWithNamedQuery(
				LIST_NOTIFICATION_BY_REFERENCE_NOTIFICATION, with("id1", ref)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Notification> listNotificationByEvenement(int refEvenement) {
		return (List<Notification>) dao.getListWithNamedQuery(
				LIST_NOTIFICATION_BY_REFERENCE_EVENEMENT,
				with("id1", refEvenement).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Notification> listNotificationByEvenementAndLibelle(
			int refEvenement, String libelle) {
		return (List<Notification>) dao.getListWithNamedQuery(
				LIST_NOTIFICATION_BY_REFERENCE_EVENEMENT_LIBELLE,
				with("id1", refEvenement).and("id2", libelle).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Notification> listNotificationByEvenementAndEtat(boolean etat,
			int refEvenement) {
		return (List<Notification>) dao.getListWithNamedQuery(
				LIST_NOTIFICATION_BY_EVENEMENT_ETAT,
				with("id1", etat).and("id2", refEvenement).parameters());
	}

	// User notification
	@SuppressWarnings("unchecked")
	public List<UserNotification> listNotificationByUser(int refUser) {
		return (List<UserNotification>) dao.getListWithNamedQuery(
				LIST_NOTIFICATION_BY_USER, with("id1", refUser).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<UserNotification> listNotificationByUserAndNotification(
			int refUser, int idNotification) {
		return (List<UserNotification>) dao.getListWithNamedQuery(
				LIST_NOTIFICATION_BY_USER_NOTIFICATION, with("id1", refUser)
						.and("id2", idNotification).parameters());
	}

	// Log
	@SuppressWarnings("unchecked")
	public List<Xtelog> listLogByRef(int ref) {
		return (List<Xtelog>) dao.getListWithNamedQuery(
				LIST_LOG_BY_REFERENCE_LOG, with("id1", ref).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Xtelog> listLogByEvenement(int refEvenement) {
		return (List<Xtelog>) dao.getListWithNamedQuery(
				LIST_LOG_BY_REFERENCE_EVENEMENT, with("id1", refEvenement)
						.parameters());
	}

	// Template Notification listTemplateNotificationByNotification
	@SuppressWarnings("unchecked")
	public List<Templatenotification> listTemplateNotificationByNotification(
			int ref) {
		return (List<Templatenotification>) dao.getListWithNamedQuery(
				LIST_TEMPLATE_NOTIFICATION_BY_REFERENCE_NOTIFICATION,
				with("id1", ref).parameters());
	}

	// Template Log LIST_TEMPLATE_LOG_BY_REFERENCE_LOG listTemplateLogByLog
	@SuppressWarnings("unchecked")
	public List<Templatelog> listTemplateLogByLog(int ref) {
		return (List<Templatelog>) dao.getListWithNamedQuery(
				LIST_TEMPLATE_LOG_BY_REFERENCE_LOG, with("id1", ref)
						.parameters());
	}

	// Variables de notification
	@SuppressWarnings("unchecked")
	public List<VariablesNotification> listVariableByRef(int ref) {
		return (List<VariablesNotification>) dao.getListWithNamedQuery(
				LIST_VARIABLE_BY_REFERENCE_VARIABLE, with("id1", ref)
						.parameters());
	}

	// variables Notification (intermidiere)
	@SuppressWarnings("unchecked")
	public List<NotificationVariable> listVariablesNotificationByNotification(
			int ref) {
		return (List<NotificationVariable>) dao.getListWithNamedQuery(
				LIST_VARIABLE_NOTIFICATION_BY_NOTIFICATION, with("id1", ref)
						.parameters());
	}

	public void deleteAnnotationsByIdTransaction(int id) {
		dao.deleteWithNamedQuery(TRANSACTION_ANNOTATION_BY_ID_TRANSACTION,
				with("id", id).parameters());
	}

	public void deleteDestinatairesByIdTransaction(int id) {
		dao.deleteWithNamedQuery(TRANSACTION_DESTINATION_BY_ID_TRANSACTION,
				with("id", id).parameters());

	}

	@SuppressWarnings("unchecked")
	public List<Raisonnontransmission> listRaisonById(int id) {
		return (List<Raisonnontransmission>) dao.getListWithNamedQuery(
				LIST_RAISON_BY_ID, with("id", id).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Raisonnontransmission> listRaisonByLibelle(String libelle) {
		return (List<Raisonnontransmission>) dao.getListWithNamedQuery(
				LIST_RAISON_BY_Libelle, with("id", libelle).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Raisonnontransmission> listRaisonByCategorie(String cat) {
		return (List<Raisonnontransmission>) dao.getListWithNamedQuery(
				LIST_RAISON_BY_Categorie, with("id", cat).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getTransactionDestinationForBoostArrivalAllByType(
			String type) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_DESTINATION_FOR_BOOST_ARRIVAL_ALL_BY_TYPE,
				with("type", type).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getTransactionDestinationForBoostLivingAllByType(
			String type) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_DESTINATION_FOR_BOOST_LIVING_ALL_BY_TYPE,
				with("type", type).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getTransactionDestinationForBoostArrivalByType(
			String type, Date dateJour, Date date) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_DESTINATION_FOR_BOOST_ARRIVAL_BY_TYPE,
				with("type", type).and("dateDebut", dateJour)
						.and("dateFin", date).parameters());

	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getTransactionDestinationForBoostLivingByType(
			String type, Date dateJour, Date date) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_DESTINATION_FOR_BOOST_LIVING_BY_TYPE,
				with("type", type).and("dateDebut", dateJour)
						.and("dateFin", date).parameters());

	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getTransactionDestinationForBoostArrivalOutOfDateByType(
			String type, Date date) {
		return (List<TransactionDestination>) dao
				.getListWithNamedQuery(
						LIST_TRANSACTION_DESTINATION_FOR_BOOST_ARRIVAL_OUT_OF_DATE_BY_TYPE,
						with("type", type).and("date", date).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getTransactionDestinationForBoostLivingOutOfDateByType(
			String type, Date date) {
		return (List<TransactionDestination>) dao
				.getListWithNamedQuery(
						LIST_TRANSACTION_DESTINATION_FOR_BOOST_LIVING_OUT_OF_DATE_BY_TYPE,
						with("type", type).and("date", date).parameters());
	}

	// Type Evenement
	@SuppressWarnings("unchecked")
	public List<TypeEvenement> listTypeEvenementByRef(int ref) {
		return (List<TypeEvenement>) dao.getListWithNamedQuery(
				LIST_TYPEEVENEMENT_BY_REFERENCE, with("id1", ref).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TypeEvenement> listTypeEvenementBylibelle(String s) {
		return (List<TypeEvenement>) dao.getListWithNamedQuery(
				LIST_TYPEEVENEMENT_BY_LIBELLE, with("id1", s).parameters());
	}

	// Message
	@SuppressWarnings("unchecked")
	public List<Message> listMessageByUser(int ref) {
		return (List<Message>) dao.getListWithNamedQuery(LIST_MESSAGE_BY_USER,
				with("id1", ref).parameters());
	}

	public List<Expdestexterne> getListExpeDestExterneByCriteria(String nom,
			String prenom, String telephone, String mail, String adresse,
			String codePostal, String ville, String pays, String fax,
			String gouvernorat) {
		return dao.getListExpeDestExterneByCriteria(nom, prenom, telephone,
				mail, adresse, codePostal, ville, pays, fax, gouvernorat);
	}

	public List<Pp> getListPpByCriteria(RecherchePpModel recherchePpModel,
			List<Integer> listExpDestExterne) {
		return dao.getListPpByCriteria(recherchePpModel, listExpDestExterne);
	}

	public List<Pm> getListPmByCriteria(RecherchePmModel recherchePmModel,
			List<Integer> listExpDestExterne) {
		return dao.getListPmByCriteria(recherchePmModel, listExpDestExterne);
	}

	@SuppressWarnings("unchecked")
	public List<Xtelog> listLogByLibelle(String libelle) {
		return (List<Xtelog>) dao.getListWithNamedQuery(LIST_LOG_BY_LIBELLE,
				with("id1", libelle).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Menu> getListMenuPrincipal() {
		return (List<Menu>) dao.getListWithNamedQuery(LIST_MENU_PRINCIPAL);
	}

	@SuppressWarnings("unchecked")
	public List<Menu> getListSousMenuByIdMenuPrincipal(int id) {
		return (List<Menu>) dao.getListWithNamedQuery(
				LIST_SOUS_MENU_BY_ID_MENU_PRINCIPAL, with("id", id)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Menu> getListMenusByIdRole(int id) {
		return (List<Menu>) dao.getListWithNamedQuery(LIST_MENUS_BY_ID_DROIT,
				with("id", id).parameters());
	}

	public void deleteMenuDroitByIdDroit(int id) {
		dao.deleteWithNamedQuery(MENU_DROIT_BY_ID_DROIT, with("id", id)
				.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionBoccJour(int id, Date dateDebut,
			Date dateFin) {
		return (List<Transaction>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_BOCC_JOUR,
				with("id", id).and("dateDebut", dateDebut)
						.and("dateFin", dateFin).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionDestinationBocJour(
			String type, Date dateDebut, Date dateFin) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_DESTINATION_BOCC,
				with("type", type).and("dateDebut", dateDebut)
						.and("dateFin", dateFin).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Courrier> getListCourrierNecessitantReponse(String type,
			Date dateDebut, Date dateFin) {
		return (List<Courrier>) dao.getListWithNamedQuery(
				LIST_COURRIER_NECESSITANT_REPONSE,
				with("type", type).and("dateDebut", dateDebut)
						.and("dateFin", dateFin).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Menu> getListMenuPrincipalByLibelle(String menuParent) {
		return (List<Menu>) dao.getListWithNamedQuery(
				LIST_MENU_PRINCIPAL_BY_LIBELLE, with("id", menuParent)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Menu> getMenuPrincipalByIdMenu(int idMenuPrincipal) {
		return (List<Menu>) dao.getListWithNamedQuery(
				LIST_MENU_PRINCIPAL_BY_ID_MENU, with("id", idMenuPrincipal)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Menu> getMenuPrincipalByIdOrder(int indexOrder) {
		return (List<Menu>) dao.getListWithNamedQuery(
				LIST_MENU_PRINCIPAL_BY_ID_ORDER, with("id", indexOrder)
						.parameters());
	}

	public void deleteMenuSubMenuByIdPrincipalMenu(Integer menuId) {
		dao.deleteWithNamedQuery(MENU_SOUS_MENU_BY_ID_MENU_PRINCIPAL,
				with("id", menuId).parameters());
	}

	public void deleteSubMenuByIdPrincipalMenu(Integer menuId) {
		dao.deleteWithNamedQuery(SOUS_MENU_BY_ID_MENU_PRINCIPAL,
				with("id", menuId).parameters());
	}

	public void deleteMenuDroitByIdMenu(Integer menuId) {
		dao.deleteWithNamedQuery(MENU_DROIT_BY_ID_MENU, with("id", menuId)
				.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Menu> getListNonAffectedSubMenu() {
		return (List<Menu>) dao
				.getListWithNamedQuery(LIST_NON_AFFECTED_SUB_MENU);
	}

	@SuppressWarnings("unchecked")
	public List<Gouvernerat> listGouvernoratByRefPays(int refPays) {
		return (List<Gouvernerat>) dao.getListWithNamedQuery(
				LIST_GOUVERNERAT_BY_REFERENCEPAYS, with("id", refPays)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Ville> listVilleByRefGouvernorat(int refGouvernorat) {
		return (List<Ville>) dao.getListWithNamedQuery(
				LIST_VILLE_BY_REFERENCEGOUVERNERAT, with("id", refGouvernorat)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Pays> listPaysBylibelle(String libelle) {
		return (List<Pays>) dao.getListWithNamedQuery(LIST_PAYS_BY_LIBELLE,
				with("id", libelle).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Gouvernerat> listGouverneratBylibelle(String libelle) {
		return (List<Gouvernerat>) dao.getListWithNamedQuery(
				LIST_GOUVERNORAT_BY_LIBELLE, with("id", libelle).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Ville> listVilleBylibelle(String libelle) {
		return (List<Ville>) dao.getListWithNamedQuery(LIST_VILLE_BY_LIBELLE,
				with("id", libelle).parameters());
	}

	public int maxIdUserNotification() {
		return dao.getMaxIdUserNotification();
	}

	@SuppressWarnings("unchecked")
	public List<Groupecontactpppm> getContactGroupecontactByIdGroupecontact(
			int id) {
		return (List<Groupecontactpppm>) dao.getListWithNamedQuery(
				LIST_ContactGroupecontactByIdGroupecontact, with("id", id)
						.parameters());
	}

	public void deleteContactGroupecontactByIdGroupecontact(int id) {
		dao.deleteWithNamedQuery(ContactGroupecontactByIdGroupecontact,
				with("id1", id).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Groupecontactmailing> getGroupecontactmailingByIdGroupecontact(
			int id) {
		return (List<Groupecontactmailing>) dao.getListWithNamedQuery(
				LIST_ContactGroupeMailingByIdGroupecontact, with("id", id)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Sujetmailing> listSujetmailingById(int ref) {
		return (List<Sujetmailing>) dao.getListWithNamedQuery(
				LIST_SUJETMAILING_BY_ID, with("id", ref).parameters());

	}

	@SuppressWarnings("unchecked")
	public List<Groupecontactmailing> getContactSujetmailingByIdSujetmailing(
			int refSujetmailing) {
		return (List<Groupecontactmailing>) dao.getListWithNamedQuery(
				LIST_CONTACT_MAILING_BY_ID_MAILING, with("id", refSujetmailing)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Groupecontactmailing> getListGroupeContctMailingByIdGroupAndIdSujet(
			Integer idgroupecontact, Integer idsujetmailing) {
		return (List<Groupecontactmailing>) dao.getListWithNamedQuery(
				LIST_CONTACT_MAILING_BY_ID_GROUPE_AND_ID_MAILING,
				with("id", idgroupecontact).and("id1", idsujetmailing)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Sauvegardehistorique> listSauvegardehistoriqueByDate(Date date) {
		return (List<Sauvegardehistorique>) dao.getListWithNamedQuery(
				LIST_Sauvegardehistorique_BY_Date, with("id", date)
						.parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionByDate(Date dateArchivage) {
		return (List<Transaction>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_BY_DATE, with("date", dateArchivage)
						.parameters());
	}

	// **
	// ** Relance

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getTransactionDestinationForBoostBOC(
			String type, String dateJour, String date) {	
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_DESTINATION_BOOST_BOC,
				with("type", type).and("dateDebut", dateJour)
						.and("dateFin", date).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getTransactionDestinationForBoostOutOfDateBOC(
			String type, String date) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_DESTINATION_FOR_BOOST_OUT_OF_DATE_BOC,
				with("type", type).and("date", date).parameters());
	}
	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getTransactionDestinationForBoostOutOfDateBOCTraite(
			String type, Date date) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LIST_TRANSACTION_DESTINATION_FOR_BOOST_OUT_OF_DATE_BOC_TRAITE,
				with("type", type).and("date", date).parameters());
	}

	
//2020-06-17 /////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getTransactionDestinationForBoostAllBOC() {

		return (List<TransactionDestination>) dao
				.getListWithNamedQuery(LIST_TRANSACTION_DESTINATION_FOR_BOOST_ALL_BOC);
	}

	private static final String LIST_TRANSACTION_DESTINATION_FOR_BOOST_ALL_BOC_BY_TYPE = "listTransactionDestinationForBoostAllBOCByType";
	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getTransactionDestinationForBoostAllBOCByType(String type) {

		return (List<TransactionDestination>) dao
				.getListWithNamedQuery(LIST_TRANSACTION_DESTINATION_FOR_BOOST_ALL_BOC_BY_TYPE,
						with("type", type)
						.parameters());
	}
//2020-06-17 /////////////////////////////////////////////////////////////////////////////////
	
	
	@SuppressWarnings("unchecked")
	public List<Mail> listMailBymailState(int mailState) {
		return (List<Mail>) dao.getListWithNamedQuery(
				LIST_MAIL_BY_DELETE_STATE, with("mailState", mailState)
						.parameters());
	}

	// expdest
	@SuppressWarnings("unchecked")
	public List<Expdestexterne> listExpDestByType(int typeExpDestExterne) {
		return (List<Expdestexterne>) dao.getListWithNamedQuery(
				LIST_EXP_DEST_EXTERNE_BY_TYPE,
				with("typeExpDestExterne", typeExpDestExterne).parameters());
	}

	// DEBUT maintenance lenteur liste des courriers

	// process Director
	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionEnvoyesParResponsableJour(
			int id, String type, String type1, Date date, int firstIndex,
			int maxResult) {
		return (List<Transaction>) dao.getListLimitedWithNamedQuery(
				LIST_TRANSACTION_ENVOYES_FOR_RESPONSABLE_JOUR, with("id", id)
						.and("type1", type1).and("type", type)
						.and("date", date).parameters(), firstIndex, maxResult);
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionRecusAuResponsableJour(
			int id, String type, String type1, Date date, int firstIndex,
			int maxResult) {
		return (List<TransactionDestination>) dao.getListLimitedWithNamedQuery(
				LIST_TRANSACTION_RECUS_AU_RESPONSABLE_JOUR,
				with("id", id).and("type1", type1).and("type", type)
						.and("date", date).parameters(), firstIndex, maxResult);
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionEnvoyesUniqueParResponsableJour(
			String type, String type1, Date date, int firstIndex, int maxResult) {
		return (List<Transaction>) dao.getListLimitedWithNamedQuery(
				LIST_TRANSACTION_ENVOYES_FOR_SECRETARY_JOUR,
				with("type1", type1).and("type", type).and("date", date)
						.parameters(), firstIndex, maxResult);
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionRecusUniqueAuResponsableJour(
			String type, String type1, Date date, int firstIndex, int maxResult) {
		return (List<TransactionDestination>) dao.getListLimitedWithNamedQuery(
				LIST_TRANSACTION_RECUS_AU_SECRETARY_JOUR, with("type1", type1)
						.and("type", type).and("date", date).parameters(),
				firstIndex, maxResult);
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionEnvoyesUniqueParSecretaireJour(
			String typeSecretaire, Date date, int firstIndex, int maxResult) {
		return (List<Transaction>) dao.getListLimitedWithNamedQuery(
				LIST_TRANSACTION_ENVOYES_FOR_AGENT_JOUR,
				with("type", typeSecretaire).and("date", date).parameters(),
				firstIndex, maxResult);
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionRecusUniqueAuSecretaireJour(
			String typeSecretaire, Date date, int firstIndex, int maxResult) {
		return (List<TransactionDestination>) dao.getListLimitedWithNamedQuery(
				LIST_TRANSACTION_RECUS_AU_AGENT_JOUR,
				with("type", typeSecretaire).and("date", date).parameters(),
				firstIndex, maxResult);
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionEnvoyesParSubordonnesJour(
			int id, Date date, int firstIndex, int maxResult) {
		return (List<Transaction>) dao.getListLimitedWithNamedQuery(
				LIST_TRANSACTION_ENVOYES_PAR_SUBORDONNES_JOUR, with("id", id)
						.and("date", date).parameters(), firstIndex, maxResult);
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionRecusAuSubordonnesJour(
			int id, Date date, int firstIndex, int maxResult) {
		return (List<TransactionDestination>) dao.getListLimitedWithNamedQuery(
				LIST_TRANSACTION_RECUS_AU_SUBORDONNES_JOUR,
				with("id", id).and("date", date).parameters(), firstIndex,
				maxResult);
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionEnvoyesParSousUniteJour(int id,
			Date date, int firstIndex, int maxResult) {
		return (List<Transaction>) dao.getListLimitedWithNamedQuery(
				LIST_TRANSACTION_ENVOYES_PAR_SOUS_UNITE_JOUR, with("id", id)
						.and("date", date).parameters(), firstIndex, maxResult);
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionRecusAuSousUniteJour(
			int id, Date date, int firstIndex, int maxResult) {
		return (List<TransactionDestination>) dao.getListLimitedWithNamedQuery(
				LIST_TRANSACTION_RECUS_AU_SOUS_UNITE_JOUR,
				with("id", id).and("date", date).parameters(), firstIndex,
				maxResult);
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionEnvoyesParResponsable(int id,
			String type, String type1, int firstIndex, int maxResult) {
		return (List<Transaction>) dao.getListLimitedWithNamedQuery(
				LIST_TRANSACTION_ENVOYES_FOR_RESPONSABLE,
				with("id", id).and("type1", type1).and("type", type)
						.parameters(), firstIndex, maxResult);
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionRecusAuResponsable(
			int id, String type, String type1, int firstIndex, int maxResult) {
		return (List<TransactionDestination>) dao.getListLimitedWithNamedQuery(
				LIST_TRANSACTION_RECUS_AU_RESPONSABLE,
				with("id", id).and("type1", type1).and("type", type)
						.parameters(), firstIndex, maxResult);
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionEnvoyesUniqueParResponsable(
			String type, String type1, int firstIndex, int maxResult) {
		return (List<Transaction>) dao.getListLimitedWithNamedQuery(
				LIST_TRANSACTION_ENVOYES_FOR_SECRETARY, with("type1", type1)
						.and("type", type).parameters(), firstIndex, maxResult);
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionRecusUniqueAuResponsable(
			String type, String type1, int firstIndex, int maxResult) {
		return (List<TransactionDestination>) dao.getListLimitedWithNamedQuery(
				LIST_TRANSACTION_RECUS_AU_SECRETARY,
				with("type1", type1).and("type", type).parameters(),
				firstIndex, maxResult);
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionEnvoyesUniqueParSecretaire(
			String typeSecretaire, int firstIndex, int maxResult) {
		return (List<Transaction>) dao.getListLimitedWithNamedQuery(
				LIST_TRANSACTION_ENVOYES_FOR_AGENT,
				with("type", typeSecretaire).parameters(), firstIndex,
				maxResult);
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionRecusUniqueAuSecretaire(
			String typeSecretaire, int firstIndex, int maxResult) {
		return (List<TransactionDestination>) dao.getListLimitedWithNamedQuery(
				LIST_TRANSACTION_RECUS_AU_AGENT, with("type", typeSecretaire)
						.parameters(), firstIndex, maxResult);
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionEnvoyesParSubordonnes(int id,
			int firstIndex, int maxResult) {
		return (List<Transaction>) dao.getListLimitedWithNamedQuery(
				LIST_TRANSACTION_ENVOYES_PAR_SUBORDONNES, with("id", id)
						.parameters(), firstIndex, maxResult);
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionRecusAuSubordonnes(
			int id, int firstIndex, int maxResult) {
		return (List<TransactionDestination>) dao.getListLimitedWithNamedQuery(
				LIST_TRANSACTION_RECUS_AU_SUBORDONNES, with("id", id)
						.parameters(), firstIndex, maxResult);
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionEnvoyesParSousUnite(int id,
			int firstIndex, int maxResult) {
		return (List<Transaction>) dao.getListLimitedWithNamedQuery(
				LIST_TRANSACTION_ENVOYES_PAR_SOUS_UNITE, with("id", id)
						.parameters(), firstIndex, maxResult);
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionRecusAuSousUnite(
			int id, int firstIndex, int maxResult) {
		return (List<TransactionDestination>) dao.getListLimitedWithNamedQuery(
				LIST_TRANSACTION_RECUS_AU_SOUS_UNITE, with("id", id)
						.parameters(), firstIndex, maxResult);
	}

	// process BOC
	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionBocJour(int id, Date date,
			String type, int firstIndex, int maxResult) {
		return (List<Transaction>) dao.getListLimitedWithNamedQuery(
				LIST_TRANSACTION_BOC_JOUR, with("id", id).and("date", date)
						.and("type", type).parameters(), firstIndex, maxResult);
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionDestinationBocJour(
			String type, int id, Date date, int firstIndex, int maxResult) {
		return (List<TransactionDestination>) dao
				.getListLimitedWithNamedQuery(
						LIST_TRANSACTION_DESTINATION_BOC_JOUR,
						with("type", type).and("date", date).and("id", id)
								.parameters(), firstIndex, maxResult);
	}

	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionBoc(int id, String type,
			int firstIndex, int maxResult) {
		return (List<Transaction>) dao.getListLimitedWithNamedQuery(
				LIST_TRANSACTION_BOC, with("id", id).and("type", type)
						.parameters(), firstIndex, maxResult);
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> getListTransactionDestinationBoc(
			String type, int id, int firstIndex, int maxResult) {
		return (List<TransactionDestination>) dao.getListLimitedWithNamedQuery(
				LIST_TRANSACTION_DESTINATION_BOC,
				with("type", type).and("id", id).parameters(), firstIndex,
				maxResult);
	}

	// FIN maintenance lenteur liste des courriers ( des nouveaux requetes )
	public Long getCountTransactionByIdExp(Integer idExpdestExterne) {
		return dao.count(COUNT_TRANSACTION_BY_IDEXPDESTEXTERNE,
				with("idExpdestExterne", idExpdestExterne).parameters());
	}

	// Nouvelle Bean CourrierConsultationBean
	public List<CourrierInformations> findCourrierEnvoyerANDRecuByCriteria(
			boolean isResponsable, List<Integer> listIdsSousUnites,
			List<Integer> listIdSubordonnes, HashMap<String, Object> filterMap,
			String sortField, boolean descending, String secretaire,
			String sub, String unit, int jourOrAutre, Date dateDebut,
			Date dateFin, String type, String type1, String typeSecretaire,
			Integer idUser, Integer typeTransmission, String stateTraitement,
			int firstIndex, int maxResult, boolean forRapport,
			Integer courrierRubriqueId,String typeCourrier, String DBType, Integer idCourrierCourant,int flagueCloture,int flagInterne) {
		return dao.findCourrierEnvoyerANDRecuWithCriterion(
				isResponsable, listIdsSousUnites, listIdSubordonnes, filterMap,
				sortField, descending, secretaire, sub, unit, jourOrAutre,
				dateDebut, dateFin, type, type1, typeSecretaire, idUser,
				typeTransmission, stateTraitement, firstIndex, maxResult,
				forRapport, courrierRubriqueId, typeCourrier, DBType,idCourrierCourant, flagueCloture, flagInterne);
	}

	public List<TransactionDestination> findCourrierRecuByCriteria(
			boolean isResponsable, List<Integer> listIdsSousUnites,
			List<Integer> listIdSubordonnes, HashMap<String, Object> filterMap,
			String sortField, boolean descending, String secretaire,
			String sub, String unit, int jourOrAutre, Date dateDebut,
			Date dateFin, String type, String type1, String typeSecretaire,
			Integer idUser, Integer typeTransmission, String stateTraitement,
			int firstIndex, int maxResult, boolean forRapport,
			Integer courrierRubriqueId) {
		return dao.findCourrierRecuWithCriterion(isResponsable,
				listIdsSousUnites, listIdSubordonnes, filterMap, sortField,
				descending, secretaire, sub, unit, jourOrAutre, dateDebut,
				dateFin, type, type1, typeSecretaire, idUser, typeTransmission,
				stateTraitement, firstIndex, maxResult, forRapport,
				courrierRubriqueId);
	}

	public List<CourrierInformations> findCourrierEnvoyerBOCByCriteria(
			HashMap<String, Object> filterMap, String sortField,
			boolean descending, int jourOrAutre, Date dateDebut, Date dateFin,
			String type, String type1, List<Integer> listIdBocMembers, String typeTransmission,
			String stateTraitement, int firstIndex, int maxResult,
			String categorieCourrierJour, Boolean forRapport, String DBType,Integer idCourrierCourant,int flagueCloture,int flagInterne) {
		return dao.findCourrierEnvoyerBOCWithCriterion(filterMap, sortField,
				descending, jourOrAutre, dateDebut, dateFin, type, type1,
				listIdBocMembers, typeTransmission, stateTraitement, firstIndex,
				maxResult, categorieCourrierJour, forRapport, DBType,idCourrierCourant,flagueCloture,flagInterne);
	}
	
	
	public List<CourrierInformations> findCourrierEnvoyerBOCByCriteriaLies(
			HashMap<String, Object> filterMap, String sortField,
			boolean descending, int jourOrAutre, Date dateDebut, Date dateFin,
			String type, String type1, List<Integer> listIdBocMembers, String typeTransmission,
			String stateTraitement, int firstIndex, int maxResult,
			String categorieCourrierJour, Boolean forRapport, String DBType,Integer idCourrierCourant,int flagueCloture,int flagInterne) {
		return dao.findCourrierEnvoyerBOCWithCriterionLies(filterMap, sortField,
				descending, jourOrAutre, dateDebut, dateFin, type, type1,
				listIdBocMembers, typeTransmission, stateTraitement, firstIndex,
				maxResult, categorieCourrierJour, forRapport, DBType,idCourrierCourant,flagueCloture,flagInterne);
	}
	
	public List<Object[]> courrierStatistiquesBOC(
			Date dateDebut, Date dateFin, List<String> types, String type1,
			List<Integer> listIdBocMembers, String typeTransmission,
			String stateTraitement, String categorieCourrierJour, String categorie) {
		return dao.courrierStatistiquesBOC(dateDebut, dateFin, types, type1, listIdBocMembers, typeTransmission, stateTraitement, categorieCourrierJour, categorie);
	}

	public List<Object[]> courrierStatistiquesBOCStructure(Date dateDebut, Date dateFin,
			Integer categorieCourrierJour, List<String> types,
			List<Integer> listIdBocMembers) {
		return dao.courrierStatistiquesBOCStructure(dateDebut, dateFin, categorieCourrierJour,types,listIdBocMembers);
	}

	public List<Object[]> courrierStatistiquesBOCReponse(String type, Date dateDebut, Date dateFin) {
		return dao.courrierStatistiquesBOCReponse(type, dateDebut, dateFin);
	}

	public List<TransactionDestination> findCourrierRecuBOCByCriteria(
			int jourOrAutre, Date dateDebut, Date dateFin, String type,
			String type1, Integer idUser, Integer typeTransmission,
			Integer stateTraitement, int firstIndex, int maxResult,
			Integer etatId) {
		// return dao.findCourrierRecuBOCWithCriterion(jourOrAutre,
		// dateDebut,dateFin, type, type1, idUser, typeTransmission,
		// stateTraitement, firstIndex, maxResult, etatId);
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Courrier> listCourrierByIdTransaction(Integer idTransaction) {
		return (List<Courrier>) dao.getListWithNamedQuery(
				LIST_COURRIER_BY_ID_TRANSATION,
				with("idTransaction", idTransaction).parameters());
	}


	public Long CountAllCourrierEnvoyerANDRecuByCriteria(boolean isResponsable,
			List<Integer> listIdsSousUnites, List<Integer> listIdSubordonnes,
			HashMap<String, Object> filterMap, String secretaire, String sub,
			String unit, int jourOrAutre, Date dateDebut, Date dateFin,
			String type, String type1, String typeSecretaire, Integer idUser,
			Integer typeTransmission, String stateTraitement,
			Integer courrierRubriqueId, boolean forStat, String typeCourrier) {
		return dao.CountAllCourrierEnvoyerANDRecuWithCriterion(isResponsable,
				listIdsSousUnites, listIdSubordonnes, filterMap, secretaire,
				sub, unit, jourOrAutre, dateDebut, dateFin, type, type1,
				typeSecretaire, idUser, typeTransmission, stateTraitement,
				courrierRubriqueId, forStat, typeCourrier);
	}

	public Integer recherheMulticritereCount(boolean isResponsable, boolean isBoc,
			List<Integer> listIdsSousUnites, List<Integer> listIdSubordonnes, String secretaire,
			String sub, String unit, String type, String type1, String typeSecretaire,
			Integer idUser, String object, String motCle, Map<Integer, String> listExp,
			Map<Integer, String> listDes, Integer idTransmission, Integer idNature,Integer idCategorie,
			Date dateLimiteRep, List<Integer> listIdAnnotation, String description,
			Integer idConfidentialite, Integer idUrgence, Date transactionDateDebut,
			Date transactionDateFin,Date dateCourrierReel,Date dateCourrierReelFin , String typeCourrierBoc, String recuOrEnvoyer,
			List<Integer> listDestinataire, String oldRef,Integer refGeneral, String necessiteReponse,
			String courrierReference, List<String> annees, int etatCloturer, String courrierCopyTransfere, String courrierCopy,String colonne1,String colonne2,
			String colonne3,String colonne4,String colonne5,String colonne6,String colonne7,String colonne8,String colonne9,String colonne10,String colonne11,String colonne12,
			String colonne13,Date colonne14,Date colonne15,Date colonne16,Date colonne17,Date colonne18,Date colonne19,Date colonne20,
			String colonne21,Boolean colonne22, String courrierFlagInterne, List<Integer> listIdBocMembers) {
		return (Integer) dao.recherheMulticritereCount(isResponsable, isBoc, listIdsSousUnites,
				listIdSubordonnes, secretaire, sub, unit, type, type1, typeSecretaire, idUser,
				object, motCle, listExp, listDes, idTransmission, idNature,idCategorie, dateLimiteRep,
				listIdAnnotation, description, idConfidentialite, idUrgence, transactionDateDebut,
				transactionDateFin,dateCourrierReel,dateCourrierReelFin, typeCourrierBoc, recuOrEnvoyer, listDestinataire, oldRef,refGeneral, 
				necessiteReponse, courrierReference, annees,etatCloturer, courrierCopyTransfere,courrierCopy,colonne1,colonne2,colonne3,colonne4,
				colonne5,colonne6,colonne7,colonne8,colonne9,colonne10,colonne11,colonne12,
				colonne13,colonne14,colonne15,colonne16,colonne17,colonne18,colonne19,colonne20,
				colonne21,colonne22,courrierFlagInterne,listIdBocMembers).get(0);
	}

	public Long CountAllCourrierRecuByCriteria(boolean isResponsable,
			List<Integer> listIdsSousUnites, List<Integer> listIdSubordonnes,
			HashMap<String, Object> filterMap, String secretaire, String sub,
			String unit, int jourOrAutre, Date dateDebut, Date dateFin,
			String type, String type1, String typeSecretaire, Integer idUser,
			Integer typeTransmission, String stateTraitement,
			Integer courrierRubriqueId, boolean forStat) {
		return dao.countCourrierRecuWithCriterion(isResponsable,
				listIdsSousUnites, listIdSubordonnes, filterMap, secretaire,
				sub, unit, jourOrAutre, dateDebut, dateFin, type, type1,
				typeSecretaire, idUser, typeTransmission, stateTraitement,
				courrierRubriqueId, forStat);
	}

	public Long CountAllCourrierBOCByCriteria(
			HashMap<String, Object> filterMap, int jourOrAutre, Date dateDebut,
			Date dateFin,String type, String type1, List<Integer> listIdBocMembers,
			String typeTransmission, String stateTraitement,
			String categorieCourrierJour) {

		return dao.countCourrierBOCWithCriterion(filterMap, jourOrAutre,
				dateDebut, dateFin, type, listIdBocMembers, typeTransmission,
				stateTraitement, categorieCourrierJour);
	}
	public Long CountAllCourrierBOCByCriteriaStat(
			HashMap<String, Object> filterMap, int jourOrAutre, Date dateDebut,
			Date dateFin,String type, String type1, List<Integer> listIdBocMembers,
			String typeTransmission, String stateTraitement,
			String categorieCourrierJour) {

		return dao.countCourrierBOCWithCriterionStat(filterMap, jourOrAutre,
				dateDebut, dateFin, type, listIdBocMembers, typeTransmission,
				stateTraitement, categorieCourrierJour);
	}

	public List<CourrierInformations> findDossierEnvoyerByCriteria(
			boolean isResponsable, List<Integer> listIdsSousUnites,
			List<Integer> listIdSubordonnes, HashMap<String, Object> filterMap,
			String sortField, boolean descending, String secretaire,
			String sub, String unit, int jourOrAutre, Date dateDebut,
			Date dateFin, String type, String type1, String typeSecretaire,
			Integer idUser, String typeDossier, int firstIndex, int maxResult,
			Integer dossierRubriqueJourId) {
		return dao.findDossierEnvoyerWithCriterion(isResponsable,
				listIdsSousUnites, listIdSubordonnes, filterMap, sortField,
				descending, secretaire, sub, unit, jourOrAutre, dateDebut,
				dateFin, type, type1, typeSecretaire, idUser, typeDossier,
				firstIndex, maxResult, dossierRubriqueJourId);
	}

	public List<CourrierInformations> findDossierRecuByCriteria(
			boolean isResponsable, List<Integer> listIdsSousUnites,
			List<Integer> listIdSubordonnes, HashMap<String, Object> filterMap,
			String sortField, boolean descending, String secretaire,
			String sub, String unit, int jourOrAutre, Date dateDebut,
			Date dateFin, String type, String type1, String typeSecretaire,
			Integer idUser, String typeDossier, int firstIndex, int maxResult,
			Integer dossierRubriqueJourId) {
		return dao.findDossierRecuWithCriterion(isResponsable,
				listIdsSousUnites, listIdSubordonnes, filterMap, secretaire,
				sub, unit, sortField, descending, jourOrAutre, dateDebut,
				dateFin, type, type1, typeSecretaire, idUser, typeDossier,
				firstIndex, maxResult, dossierRubriqueJourId);
	}

	// it's for testing server side pagination with dataTable
	public List<Nature> findNatureByCriteria(Integer firstRow,
			Integer maxResult, HashMap<String, Object> filterMap,
			String sortField, boolean descending) {
		return dao.findNatureByCriterion(firstRow, maxResult, filterMap,
				sortField, descending);
	}

	// **

	public Long countAllDossierEnvoyer(boolean isResponsable,
			List<Integer> listIdsSousUnites, List<Integer> listIdSubordonnes,
			HashMap<String, Object> filterMap, String secretaire, String sub,
			String unit, int jourOrAutre, Date dateDebut, Date dateFin,
			String type, String type1, String typeSecretaire, Integer idUser,
			String typeDossier, Integer dossierRubriqueJourId, boolean forStat) {
		return dao.countDossierEnvoyerWithCriterion(isResponsable,
				listIdsSousUnites, listIdSubordonnes, filterMap, secretaire,
				sub, unit, jourOrAutre, dateDebut, dateFin, type, type1,
				typeSecretaire, idUser, typeDossier, dossierRubriqueJourId,
				forStat);
	}

	public Long countAllDossierRecu(boolean isResponsable,
			List<Integer> listIdsSousUnites, List<Integer> listIdSubordonnes,
			HashMap<String, Object> filterMap, String secretaire, String sub,
			String unit, int jourOrAutre, Date dateDebut, Date dateFin,
			String type, String type1, String typeSecretaire, Integer idUser,
			String typeDossier, Integer dossierRubriqueJourId, boolean forStat) {
		return dao.countDossierRecuWithCriterion(isResponsable,
				listIdsSousUnites, listIdSubordonnes, filterMap, secretaire,
				sub, unit, jourOrAutre, dateDebut, dateFin, type, type1,
				typeSecretaire, idUser, typeDossier, dossierRubriqueJourId,
				forStat);
	}

	public List<Object[]> countCourrierRecuByUrgence(boolean isResponsable,
			List<Integer> listIdsSousUnites, List<Integer> listIdSubordonnes,
			String secretaire, String sub, String unit, String type,
			String type1, String typeSecretaire, Integer idUser) {
		return dao.countCourrierRecuByConfOrUrgWithCriterion(isResponsable,
				listIdsSousUnites, listIdSubordonnes, secretaire, sub, unit,
				type, type1, typeSecretaire, idUser, "u");
	}

	public List<Object[]> countCourrierRecuByConfidentialite(boolean isResponsable,
			List<Integer> listIdsSousUnites, List<Integer> listIdSubordonnes,
			String secretaire, String sub, String unit, String type,
			String type1, String typeSecretaire, Integer idUser) {
		return dao.countCourrierRecuByConfOrUrgWithCriterion(
				isResponsable, listIdsSousUnites, listIdSubordonnes,
				secretaire, sub, unit, type, type1, typeSecretaire, idUser, "c");
	}

	public List<Object[]> countCourrierRecuBOCByUrgence(String type, Integer idUser,
			String categorieCourrierJour) {
		return dao.countCourrierBOCByConfOrUrgWithCriterion(type, idUser,
				categorieCourrierJour, "u");
	}

	public List<Object[]> countCourrierRecuBOCByConfidentialite(String type,
			Integer idUser, String categorieCourrierJour) {
		return dao.countCourrierBOCByConfOrUrgWithCriterion(type, idUser,
				categorieCourrierJour, "c");
	}

	public Long countCourrierForRelanceBOC(Date dateDebut, Date dateFin) {
		
		String patternString = "yyyy-MM-dd";
	    SimpleDateFormat format = new SimpleDateFormat(patternString);
	    String dateD = format.format(dateDebut);
	    String dateF = format.format(dateFin);
	    
		return dao.count(COUNT_TRANSACTION_DESTINATION_BOOST_BOC,
				with("dateDebut", dateD).and("dateFin", dateF)
						.parameters());
	}

	public Long countCourrierForRelanceByType(String type, Date dateDebut,
			Date dateFin) {
		
		String patternString = "yyyy-MM-dd";
	    SimpleDateFormat format = new SimpleDateFormat(patternString);
	    String dateD = format.format(dateDebut);
	    String dateF = format.format(dateFin);
	    
		return dao.count(
				COUNT_TRANSACTION_DESTINATION_FOR_BOOST_BY_TYPE,
				with("dateDebut", dateD).and("dateFin", dateF)
						.and("type", type).parameters());
	}

	public Long countCourrierForRelanceOutOfDateBOC(Date date) {
		
		String patternString = "yyyy-MM-dd";
	    SimpleDateFormat format = new SimpleDateFormat(patternString);
	    String dateD = format.format(date);
	    
		return dao.count(
				COUNT_TRANSACTION_DESTINATION_FOR_BOOST_OUT_OF_DATE_BOC,
				with("date", dateD).parameters());
	}

	public Long countCourrierForRelanceOutOfDateByType(String type, Date date) {
		
		String patternString = "yyyy-MM-dd";
	    SimpleDateFormat format = new SimpleDateFormat(patternString);
	    String dateD = format.format(date);
	    
		return dao.count(
				COUNT_TRANSACTION_DESTINATION_FOR_BOOST_OUT_OF_DATE_BY_TYPE,
				with("date", dateD).and("type", type).parameters());
	}

	public Long countDossierByIdProprietaire(int idProprietaire,
			int idTypeDossier) {
		return dao.count(COUNT_DOSSIER_BY_IDPROPRIETAIRE_AND_TYPE_DOSSIER,
				with("id", idProprietaire).and("idTypeDossier", idTypeDossier)
						.parameters());
	}

	public List<Courrier> findCourrierEnvoyerForLien(Integer idCourrierCourant,
			boolean isResponsable, List<Integer> listIdsSousUnites,
			List<Integer> listIdSubordonnes, String secretaire, String sub,
			String unit, String type, String type1, String typeSecretaire,
			Integer idUser, boolean isForLienCourrier) {
		return dao.findCourrierEnvoyerForLien(idCourrierCourant, isResponsable,
				listIdsSousUnites, listIdSubordonnes, secretaire, sub, unit,
				type, type1, typeSecretaire, idUser, isForLienCourrier);
	}

	public List<Courrier> findCourrierRecuForLien(Integer idCourrierCourant,
			boolean isResponsable, List<Integer> listIdsSousUnites,
			List<Integer> listIdSubordonnes, String secretaire, String sub,
			String unit, String type, String type1, String typeSecretaire,
			Integer idUser, boolean isForLienCourrier) {
		return dao.findCourrierRecuForLien(idCourrierCourant, isResponsable,
				listIdsSousUnites, listIdSubordonnes, secretaire, sub, unit,
				type, type1, typeSecretaire, idUser, isForLienCourrier);
	}

	public List<Courrier> findCourrierForLienBOC(Integer idUser,
			String categorieCourrierJour, Integer idCourrierCourant, boolean isForLienCourrier) {
		return dao.findCourrierForLienBOC(idUser, categorieCourrierJour,
				idCourrierCourant, isForLienCourrier);
	}
	public List<Courrier> findAllCourrierForBOC(Integer idUser,
			 Integer idCourrierCourant, boolean isForLienCourrier) {
		return dao.findAllCourrierForBOC(idUser, 
				idCourrierCourant, isForLienCourrier);
	}
	// ExpdestExterne after adding list User to List Contacts
	@SuppressWarnings("unchecked")
	public List<Expdestexterne> getListAllAxpDestExternJustPpAndPm(){
		return (List<Expdestexterne>) dao.getListWithNamedQuery(LIST_EXPDEST_EXTERNE_JUST_PP_AND_PM);
	}

	@SuppressWarnings("unchecked")
	public List<Expdestexterne> getListExpDestExternUserByLdapUID(Integer userUID){
		return (List<Expdestexterne>) dao.getListWithNamedQuery(LIST_EXPDEST_EXTERNE_USER_BY_LDAP_UID, with("userUID", userUID).parameters());
	}
	public void deleteGroupeContactPpPmByIdExpDestExterne(Integer idExpDestExterne){
		dao.deleteWithNamedQuery(DELETE_GROUP_CONTACT_PP_PM_BY_IDEXPDESTEXTERNE, with("idExpDestExterne",idExpDestExterne).parameters());
	}
	
	public void deleteLiensCourriersByIdCourrier(int id) {
		dao.deleteWithNamedQuery(DELETE_LIENS_COURRIERS_BY_ID_COURRIERS, with("id", id)
				.parameters());
	}
	
	public void deleteLiensCourriersByLienCourrierId(int id) {
		dao.deleteWithNamedQuery(DELETE_LIENs_COURRIERS_BY_LIENS_COURRIERS, with("id", id)
				.parameters());
	}
	@SuppressWarnings("unchecked")
	public List<Transaction> getListTransactionByDateTransaction() {
		return (List<Transaction>) dao.getListWithNamedQuery(List_TRANSACTION_BY_DATE_TRANSACTION);
	}
	@SuppressWarnings("unchecked")
	public List<Nature> getListNatureWithWorkFlow() {
		return (List<Nature>) dao
				.getListWithNamedQuery(LIST_NATURE_WITH_WORKFLOW);

	}

	public TransactionDestinationReelle getTransactionDestinationReelById(
			Integer idTransactionDestinationReel) {
		return (TransactionDestinationReelle) dao.getObjectWithNamedQuery(
				TRANSACTION_DESTINATION_REELLE_BY_ID,
				with("idTransactionDestinationReel",
						idTransactionDestinationReel).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<TransactionDestinationReelle> listTransactionDestinationReelByDossierID(
			Integer idDossier) {
		return (List<TransactionDestinationReelle>) dao.getListWithNamedQuery(
				TRANSACTION_DESTINATION_REELLE_BY_DOSSIER_ID,
				with("idDossier", idDossier).parameters());
	}

	@SuppressWarnings("unchecked")
	public List<Document> listDocumentByIdDocumentAndDeleteFlagAndCatgDoc(
			int courrierId, Boolean deleteFlag, Integer catgDoc) {
		return (List<Document>) dao.getListWithNamedQuery(
				LIST_DOCUMENT_BY_ID_COURRIER_AND_DELETE_FLAG,
				with("courrierId", courrierId).and("deleteFlag", deleteFlag)
						.and("catgDoc", catgDoc).parameters());
	}

	public DocumentCategorie getDocumentCategorieById(int id) {
		return (DocumentCategorie) dao.getObjectWithNamedQuery(
				DOCUMENT_CATEGORIE_BY_ID, with("id", id).parameters());

	}

	//[JS] : 2020-05-15
	@SuppressWarnings("unchecked")
	public List<CourrierLiens>  getCourrierLienByListIdLienAndTypeLien(
			List<Integer> listIdLiensCourrier, Integer typeLien) {
		return  (List<CourrierLiens>) dao.getListWithNamedQuery(
				COURRIER_LIEN_BY_ID_LIEN_COURRIER_AND_TYPE_LIEN,
				with("listIdLien", listIdLiensCourrier).and("typeLien",
						typeLien).parameters());

	}

	public Dossier getDossierByIdCourrierAndIdTypeDossier(Integer idCourrier,
			Integer idTypeDossier) {
		return (Dossier) dao.getObjectWithNamedQuery(
				DOSSIER_BY_ID_COURRIER_AND_ID_TYPE_DOSSIER,
				with("idCourrier", idCourrier).and("idTypeDossier",
						idTypeDossier).parameters());

	}

	// statistique new
	public List<CourrierInformations> getListCourrierExterneBOC(String type,
			List<Integer> listIdBocMembers, String categorieCourrierJour,
			Date dateDebut, Date dateFin) {
		return (List<CourrierInformations>) dao
				.findCourrierExterneBOCWithCriterion(type, listIdBocMembers,
						categorieCourrierJour, dateDebut, dateFin);
	}

	public List<CourrierInformations> getListCourrierArriveeDepartByStructure(
			List<String> listMembers, Integer courrierDepartArrivee,
			Date dateDebut, Date dateFin) {
		return (List<CourrierInformations>) dao
				.findCourrierArriveeDepartByStructure(listMembers,
						courrierDepartArrivee, dateDebut, dateFin);
	}

	public void executeSQLQuery(StringBuffer query) {
		dao.executeSQLQuery(query);
	}

	public Integer getCourrierLastIdByTypeOrdreAndAnnee(String type,
			Integer year) {
		return dao.getCourrierLastIdByTypeOrdreAndAnnee(type, year);
	}

	public List<Courrier> listCourrierNonAffecteDossier(
			Integer idCourrierCourant, boolean isResponsable,
			List<Integer> listIdsSousUnites, List<Integer> listIdSubordonnes,
			String secretaire, String sub, String unit, String type,
			String type1, String typeSecretaire, Integer idUser,
			boolean isForLienCourrier) {
		return dao.listCourrierNonAffecteDossier(idCourrierCourant,
				isResponsable, listIdsSousUnites, listIdSubordonnes,
				secretaire, sub, unit, type, type1, typeSecretaire, idUser,
				isForLienCourrier);
	}

	@Override
	public List<Object[]> executeQuery3(String myRequet)
			throws HibernateException {
		return dao.executeQuery3(myRequet);
	}

	@SuppressWarnings("unchecked")
	public List<Variables> getListVariableByLibelle() {
		return (List<Variables>) dao
				.getListWithNamedQuery(LIST_VARIABLE_BY_Libelle);
	}

	@SuppressWarnings("unchecked")
	public List<Annotation> listeAnnotationsParDestinataireAndTransaction(
			int idDest, int idDoss) {
		return (List<Annotation>) dao.getListWithNamedQuery(
				LIST_ANNOTATION_BY_DESTINATAIRE_TRANSACTION,
				with("idDest", idDest).and("idDoss", idDoss).parameters());

	}

	@SuppressWarnings("unchecked")
	public List<Nature> listNaturesByCategorie(int categorieId) {
		return (List<Nature>) dao.getListWithNamedQuery(
				LIST_NATURE_BY_CATEGORIE, with("categorieId", categorieId)
						.parameters());

	}

	// ***KHA*** Liste annotations des destinataires que ce soit interne ou
	// externe
	private final String LISTE_ANNOTATIONS_PaAR_DESTINATAIRE_ET_TRANSACTION = "listeAnnotationsParDestinataireEtTransaction";

	@SuppressWarnings("unchecked")
	public List<Annotation> listeAnnotationsParDestinataireEtTransaction(
			int idDest, int idDoss, String typeExp) {
		return (List<Annotation>) dao.getListWithNamedQuery(
				LISTE_ANNOTATIONS_PaAR_DESTINATAIRE_ET_TRANSACTION,
				with("idDest", idDest).and("idDoss", idDoss)
						.and("typeExp", typeExp).parameters());

	}

	private final String LISTE_ANNOTATIONS_PAR_DESTINATAIRE_ET_TRANSACTION = "listeAnnotationParDestinataireEtTransaction";

	@SuppressWarnings("unchecked")
	public List<Annotation> listeAnnotationParDestinataireEtTransaction(
			int idDest, int idDoss) {
		return (List<Annotation>) dao.getListWithNamedQuery(
				LISTE_ANNOTATIONS_PAR_DESTINATAIRE_ET_TRANSACTION,
				with("idDest", idDest).and("idDoss", idDoss).parameters());

	}

	private final String LISTE_DESTINATAIRES_BY_ID_DOSSIER = "listDestByIdDossier";

	@SuppressWarnings("unchecked")
	public List<Expdest> listDestByIdDossier(int idDossier) {
		return (List<Expdest>) dao.getListWithNamedQuery(
				LISTE_DESTINATAIRES_BY_ID_DOSSIER, with("idDossier", idDossier)
						.parameters());

	}

	private final String LISTE_TRANSACTION_DESTINATION_BY_ID__EXPDEST_EXPDESTLDAP = "listTransactionDestinationByIdExpDestIdExpDestLdap";

	@SuppressWarnings("unchecked")
	public List<TransactionDestination> listTransactionDestinationByIdExpDestIdExpDestLdap(
			int idExpDest, int idExpDestLdap) {
		return (List<TransactionDestination>) dao.getListWithNamedQuery(
				LISTE_TRANSACTION_DESTINATION_BY_ID__EXPDEST_EXPDESTLDAP,
				with("idExpDest", idExpDest)
						.and("idExpDestLdap", idExpDestLdap).parameters());

	}

	private final String LISTE_TRANSACTION_DOCUMENT_BY_ID_TRANSACTION = "listTransactionDocumentByIdTransaction";

	@SuppressWarnings("unchecked")
	public List<TransactionDocument> listTransactionDocumentByIdTransaction(
			int idTransaction, Boolean deleteFlag) {
		return (List<TransactionDocument>) dao.getListWithNamedQuery(
				LISTE_TRANSACTION_DOCUMENT_BY_ID_TRANSACTION,
				with("idTransaction", idTransaction).and("deleteFlag",
						deleteFlag).parameters());

	}

	private final String LISTE_DOCUMENT_BY_ID_DOCUMENT = "listDocumentByIdDocument";

	@SuppressWarnings("unchecked")
	public List<Document> listDocumentByIdDocument(int idDocument) {
		return (List<Document>) dao.getListWithNamedQuery(
				LISTE_DOCUMENT_BY_ID_DOCUMENT, with("idDocument", idDocument)
						.parameters());

	}

	public void deleteDonneeSupplementaireByIdNature(int id) {
		dao.deleteWithNamedQuery(DONNESUPPLEMENTAIRE_BY_IDNATURE,
				with("id", id).parameters());
	}
	
	@SuppressWarnings("unchecked")
	public List<DonneeSupplementaireNature> getListDonneeSupplementaireNatureAffectes(
			int idnature) {
		return (List<DonneeSupplementaireNature>) dao.getListWithNamedQuery(
				LISTE_DONNEE_SUPPLEMENTAIRE_NATURE_AFFECTES,
				with("idnature", idnature).parameters());

	}
	@SuppressWarnings("unchecked")
	public List<DonneeSupplementaireNature> getListDonneeSupplementaireTransmissionAffectes(
			int transmissionId) {
		return (List<DonneeSupplementaireNature>) dao.getListWithNamedQuery(
				LISTE_DONNEE_SUPPLEMENTAIRE_TRANSMISSION_AFFECTES,
				with("transmissionId", transmissionId).parameters());

	}
	@SuppressWarnings("unchecked")
	public List<DonneeSupplementaire> getListDonneeSupplementaireNatureAAffectes(
			int idnature) {
		return (List<DonneeSupplementaire>) dao.getListWithNamedQuery(
				LISTE_DONNEE_SUPPLEMENTAIRE_NATURE_AAFFECTES,
				with("idnature", idnature).parameters());
	}
	
	 @SuppressWarnings("unchecked")
		public CourrierDonneeSupplementaire getDonneeSupplementaireCourrier(int idCourrier) {
			  return (CourrierDonneeSupplementaire) dao.getObjectWithNamedQuery(DONNEE_SUPPLEMENTAIRE_COURRIER,with("idCourrier", idCourrier).parameters());
		 }
	 
//	 //[JS]=========================================
//		public List<StatistiqueCourrierUtilisateur> CountAllCourrierMembreBOCByCriteria(
//				HashMap<String, Object> filterMap, int jourOrAutre, Date dateDebut,
//				Date dateFin, List<Integer> listIdBocMembers,String categorieCourrierJour) {
//			return dao.countCourrierMembreBOCWithCriterion(filterMap, jourOrAutre,
//					dateDebut, dateFin,listIdBocMembers,categorieCourrierJour);
//		}


		public List<Object[]> courrierStatistiquesBOCStructureByNature(Date dateDebut, Date dateFin,
				Integer categorieCourrierJour,Integer natureId,String valueAttr,List<String> types) {
			return dao.courrierStatistiquesBOCStructureByNature(dateDebut, dateFin, categorieCourrierJour,natureId,valueAttr,types);
		}
//		public List<StatistiqueCourrierStructureByNature> courrierStatistiquesBOCStructuretest(Date dateDebut,
//				Date dateFin, Integer categorieCourrierJour, Integer categorieId)
//		{
//			return dao.courrierStatistiquesBOCStructuretest(dateDebut, dateFin, categorieCourrierJour,categorieId);
//	
//		}
		
		public List<Object[]> courrierStatistiquesBOCReponseParUniteNature(String type, Date dateDebut, Date dateFin,Integer idNature) {
			return dao.courrierStatistiquesBOCReponseParUniteNature(type, dateDebut, dateFin,idNature);
		}
		private final String LISTE_ANNOTATIONS_PAR_DESTINATAIRE_ET_TRANSACTION_REEL= "listeAnnotationParDestinataireEtTransactionReell";

		@SuppressWarnings("unchecked")
	public List<Annotation> listeAnnotationParDestinataireEtTransactionReell(
			int idDest, int idDoss) {
		return (List<Annotation>) dao.getListWithNamedQuery(
				LISTE_ANNOTATIONS_PAR_DESTINATAIRE_ET_TRANSACTION_REEL,
				with("idDest", idDest).and("idDoss", idDoss).parameters());

	}

		 
		// KHA 16-04-2019
		
	 private final String LISTE_ANNOTATIONS_PAR_DESTINATAIRE_ET_TRANSACTION_EXPDEST = "listeAnnotationParDestinataireEtTransactionExpDest";

		@SuppressWarnings("unchecked")
		public List<Annotation> listeAnnotationParDestinataireEtTransactionExpDest(
			int idDest, int idDoss) {
		return (List<Annotation>) dao.getListWithNamedQuery(
				LISTE_ANNOTATIONS_PAR_DESTINATAIRE_ET_TRANSACTION_EXPDEST,
				with("idDest", idDest).and("idDoss", idDoss).parameters());

	}
		// KHA 25-04-2019
		private final String LISTE_COURRIER_NATURE_CHEQUE= "listeCourrierNatureCheque";

		@SuppressWarnings("unchecked")
		// 35 : id nature cheque
	public List<Courrier> listeCourrierNatureCheque(int idn) {
			
		return (List<Courrier>) dao.getListWithNamedQuery(LISTE_COURRIER_NATURE_CHEQUE,with("idn",idn).parameters());
	 

	}
		//=============== rapport 
		 public List<CourrierInformations> recherheMulticritereCourrierEnvoyeR(boolean isResponsable, 
		    String secretaire,
			String type, String type1, String typeSecretaire,
			List<Integer> listAgent,Integer idNature, 
			Date transactionDateDebut, Date transactionDateFin,boolean isBoc,
			String recuOrEnvoyer,String typeCourrierBoc,
			int firstIndex, int maxResult,String DBType,int jourOrAutre, Integer idTransmission) {
			return (List<CourrierInformations>) dao.recherheMulticritereCourrierEnvoyeRapport(isResponsable, 
				     secretaire, 
					 type,  type1,  typeSecretaire,
					   listAgent, idNature, 
					 transactionDateDebut, transactionDateFin, isBoc,
					 recuOrEnvoyer, typeCourrierBoc,
					 firstIndex,  maxResult, DBType, jourOrAutre, idTransmission);
		}

		
		
		//[JS] Statistique
		
		
		public Long CountAllCourrierNotBOCByCriteria(
				HashMap<String, Object> filterMap, int jourOrAutre, Date dateDebut,
				Date dateFin,List<String> types, String type1, List<Integer> listIdBocMembers,
				String typeTransmission, String stateTraitement,
				String categorieCourrierJour) {

			return dao.countCourrierNotBOCWithCriterion(filterMap, jourOrAutre,
					dateDebut, dateFin, types, listIdBocMembers, typeTransmission,
					stateTraitement, categorieCourrierJour);
		}
		
		
		public List<Courrier> courrierStatistiquesNecessitReponses(String necessiteReponse,Date dateDebut, Date dateFin,List<String> types,List<Integer> listIdBocMembers){
			return dao.courrierStatistiquesNecessitReponses(necessiteReponse, dateDebut, dateFin, types, listIdBocMembers);
		}
		
	
		private static final String UNITE_CONNECTEE_BY_ID = "uniteConnecteeById";
		private static final String AO_BY_REF = "getAOByRef";
		private static final String UNITE_BY_CODE_SONEDE = "getUniteByCodeSONEDE";
		private static final String EMPLOYER_BY_ID = "listEmployerById";
		private static final String EMPLOYER_BY_CODE_SONEDE= "listEmployerByCodeSonede";
		private static final String LISTE_BO_BYIDBD= "listeBoParId";
		public Unite getUniteConnecteeById(String  id) {
			return (Unite) dao.getObjectWithNamedQuery(
					UNITE_CONNECTEE_BY_ID, with("id", id).parameters());

		}

		//[JS]:
		public Integer getTransactionLastIdByTypeOrdreAndAnnees(String type,Integer year) {
			return dao.getTransactionLastIdByTypeOrdreAndAnnees(type, year);
		}
		
		public Integer getCourrierLastIdByTypeOrdreAndAnnees(String type,Integer year) {
			return dao.getCourrierLastIdByTypeOrdreAndAnnees(type, year);
		}
		
		@SuppressWarnings("unchecked")
		public List<TransactionAnnotation> getTransactionByIdAnnotation(int idA) {
			return (List<TransactionAnnotation>) dao.getListWithNamedQuery(
					LIST_TRANSACTION_BY_IDANNOTATION, with("idA", idA)
							.parameters());

		}
		
		@SuppressWarnings("unchecked")
		public List<Transaction> getReferenceCourrierByDestinataire(int idLdap, int dossierId) {
			return (List<Transaction>) dao.getListWithNamedQuery(
					REFERENCE_COURRIER_BY_DESTINATAIRE, with("idLdap", idLdap).and("dossierId", dossierId)
							.parameters());

		}
		
		
		
		public Integer CountAllCourrierBOCByTransaction(int idDossier,String type,Integer annee,String boc,List<Integer> listIdBocMembers) {

			return dao.getLastIDTransactionByTypeOrdreAndAnneeAndBO(idDossier,type,annee,boc,listIdBocMembers);
		}
		
		//[JS] [2019-06-14] : Référence Courrier (I)

		public Integer CountAllCourrierRefIByTransaction(String type,Integer annee) {

			return dao.getLastIDTransactionByTypeOrdreAndAnnee(type,annee);
		}
		public Integer CountAllCourrierRefIByTransactionValise(String type,Integer annee, int mois) {

			return dao.getLastIDTransactionByTypeOrdreAndAnnee(type,annee,mois);
		}
		public List<CourrierInformations> findCourrierEnvoyerBOCWithCriterionAO(int idaoConsultation,
				boolean b, int i, int j, String string) {
			return dao.findCourrierEnvoyerBOCWithCriterionAO(  idaoConsultation,b,  i,  j,  string);
		}
		public List<CourrierInformations> findCourrierEnvoyerBOCWithCriterionValise(	boolean b, int i, int j, String string) {
			return dao.findCourrierEnvoyerBOCWithCriterionValise(  b,  i,  j,  string);
		}
		public List<CourrierInformations> findCourrierEnvoyerBOCWithCriterionValiseFormat( int courrierValiseID,	boolean b, int i, int j, String string) {
			return dao.findCourrierEnvoyerBOCWithCriterionValiseFormat( courrierValiseID, b,  i,  j,  string);
		}
		public List<CourrierInformations> findCourrierEnvoyerBOCWithCriterionValiseFormatPointer( int courrierValiseID,	boolean b, int i, int j, String string) {
			return dao.findCourrierEnvoyerBOCWithCriterionValiseFormatPointer( courrierValiseID, b,  i,  j,  string);
		}
		@SuppressWarnings("unchecked")
		public List<AoConsultation> getAppelOffreConsultationByID() {
			return (List<AoConsultation>) dao.getListWithNamedQuery(
					LIST_AO_CONSULTATION_BY_IDAOC);

		}
	
	
	@SuppressWarnings("unchecked")
		public List<AoConsultation> getListAoConsultation(int ref) {
			return (List<AoConsultation>) dao.getListWithNamedQuery(LIST_AO_CONSULTATION,
					with("ref", ref).parameters());
			
		}		
		
		public List<AoConsultation> findAoConsultationByNumero(String reference) {
			return dao.findAoConsultationByNumero(reference);
		}
		
		public List<CourrierInformations> findCourrierEnvoyerBOCWithCriterionValise(
				int idcourrierFK,boolean b, int i, int j, String string){
				return dao.findCourrierEnvoyerBOCWithCriterionValise(idcourrierFK,b,i,j,string);
		}
		
		public List<CourrierInformations> getListCourriersNonAffectesAvalise(boolean b, int i, int j, String string){
				return dao.getListCourriersNonAffectesAvalise(b,i,j,string);
		}
		
		@SuppressWarnings("unchecked")
		public List<TransactionDestination> getListCourrierAvecReceptionPhysique(int ref) {
			return (List<TransactionDestination>) dao.getListWithNamedQuery(COURRIER_AVEC_RECEPTION_PHYSIQUE,
					with("ref", ref).parameters());
			
		}
		
		@SuppressWarnings("unchecked")
		public List<TransactionDestination> getListCourrierAvecReceptionPhysiqueByEtat(int ref,int transactionEnCours) {
			return (List<TransactionDestination>) dao.getListWithNamedQuery(COURRIER_AVEC_RECEPTION_PHYSIQUE_BYETAT,
					with("ref", ref).and("transactionEnCours", transactionEnCours).parameters());
			
		}
		public List<CourrierInformations> findCourrierEnvoyerBOCRecuAvantLorsAO(int idaoConsultation, Date AoConsultationDateSeanceCommission, boolean b, int i, int j, String string) {
			return dao.findCourrierEnvoyerBOCRecuAvantLorsAO( idaoConsultation, AoConsultationDateSeanceCommission, b,  i,  j,  string);
		}
		public List<CourrierInformations> findCourrierEnvoyerBOCRecuAvantLorsAOPorteur(int idaoConsultation, Date aoConsultationDateSeanceCommission, boolean b, int i, int j, String string) {
			return dao.findCourrierEnvoyerBOCRecuAvantLorsAOPorteur( idaoConsultation, aoConsultationDateSeanceCommission, b,  i,  j,  string);
		}
		
		public List<CourrierInformations> findCourrierEnvoyerBOCRecuDansDelaisAO(int idaoConsultation,Date aoConsultationDateLimiteReception, Date aoConsultationDelaisProlongation, boolean b, int i, int j, String string) {
			return dao.findCourrierEnvoyerBOCRecuDansDelaisAO( idaoConsultation,aoConsultationDateLimiteReception, aoConsultationDelaisProlongation, b,  i,  j,  string);
		}
		public List<CourrierInformations> findCourrierEnvoyerBOCRecuDansDelaisAOPorteur(int idaoConsultation,Date aoConsultationDateLimiteReception, Date aoConsultationDelaisProlongation, boolean b, int i, int j, String string) {
			return dao.findCourrierEnvoyerBOCRecuDansDelaisAOPorteur( idaoConsultation,aoConsultationDateLimiteReception, aoConsultationDelaisProlongation, b,  i,  j,  string);
		}
		
		public List<CourrierInformations> findCourrierEnvoyerBOCRecuApresDelaisAO(int idaoConsultation, Date aoConsultationDateSeanceCommission, boolean b, int i, int j, String string) {
			return dao.findCourrierEnvoyerBOCRecuApresDelaisAO( idaoConsultation, aoConsultationDateSeanceCommission, b,  i,  j,  string);
		}
		public List<CourrierInformations> findCourrierEnvoyerBOCRecuApresDelaisAOPorteur(int idaoConsultation, Date aoConsultationDateSeanceCommission, boolean b, int i, int j, String string) {
			return dao.findCourrierEnvoyerBOCRecuApresDelaisAOPorteur( idaoConsultation, aoConsultationDateSeanceCommission, b,  i,  j,  string);
		}
		
		public List<CourrierInformations> findCourrierEnvoyerBOCRecuSansReferenceAO(Date dateDebut, Date dateFin,String annee, String moi, boolean b, int i, int j, String string) {
			return dao.findCourrierEnvoyerBOCRecuSansReferenceAO(dateDebut, dateFin,annee, moi, b, i,  j,  string);
		}
		public List<CourrierInformations> findCourrierEnvoyerBOCRecuSansReferenceAOPorteur(Date dateDebut, Date dateFin,String annee, String moi,boolean b, int i, int j, String string) {
			return dao.findCourrierEnvoyerBOCRecuSansReferenceAOPorteur(dateDebut, dateFin, annee, moi, b,  i,  j,  string);
		}

		@SuppressWarnings("unchecked")
		public List<AoConsultation> getAOByRef(String numeroAoConsultation) {
			return (List<AoConsultation>) dao.getListWithNamedQuery(AO_BY_REF,
					with("ref", numeroAoConsultation).parameters());
		}

		@SuppressWarnings("unchecked")
		public List<Unite> getUniteByCodeSONEDE(String codeUniteRattachement) {
			//Metre à jour le code de la structure pour la SONEDE
			return (List<Unite>) dao.getListWithNamedQuery(UNITE_BY_CODE_SONEDE,
					with("c", codeUniteRattachement).parameters());
		}

		@SuppressWarnings("unchecked")
		public List<Employe> getEmployerById(int id) {
			return (List<Employe>) dao.getListWithNamedQuery(EMPLOYER_BY_ID,
					with("c", id).parameters());
		}
		
		@SuppressWarnings("unchecked")
		public List<Employe> getEmployerByCodeSonede(String code) {
			return (List<Employe>) dao.getListWithNamedQuery(EMPLOYER_BY_CODE_SONEDE,
					with("c", code).parameters());
		}
		
		
	
		
		
		@SuppressWarnings("unchecked")
		public List<Cheque> getListeChequeByCourrier(int id) {
			return (List<Cheque>) dao.getListWithNamedQuery(
					LIST_CHEQUES_BY_ID, with("id1", id).parameters());

		}
		private static final String LIST_Etages_ORDER_BY_ID = "listEtagesOrderById";
		private static final String LIST_Classement_archivage_niveau_02 = "listClassementNiveau02ByTypeConnecte";
		private static final String LIST_Classement_archivage_niveau_01 = "listClassementNiveau01ByTypeConnecte";
		private static final String UNITE_BY_CODE_SONEDE_PARENT = "getUniteByCodeSONEDEParent";
		private static final String LIST_MAIL_BY_DESTINATAIRE_STATE = "listMailByDestinataireState";
		private static final String UNITE_BY_MAIL = "listeUniteParMail";
		private static final String LIST_AO_ORDER_BY_DATE = "listAoConsultationOrdonne";
		private static final String LIST_AO_BY_NUMERO = "listAoConsultationParNumero";
		private static final String LIST_TRANSACTION_BY_DOSSIER = "listTransactionByDossier";
		private static final String LIST_EMPLOYE_BY_MATRICULE = "listeEmployerByMatricule";
		
		@SuppressWarnings("unchecked")
		public List<Classement_archivage_niveau_01> listEtagesOrderById() {
			return (List<Classement_archivage_niveau_01>) dao.getListWithNamedQuery(LIST_Etages_ORDER_BY_ID);
		}
		@SuppressWarnings("unchecked")
		public List<Document> getDocumentByIdCourrierAndType(int id2,String type) {
			return (List<Document>) dao.getListWithNamedQuery(
					LIST_DOCUMENT_BY_ID_COURRIER_AND_TYPE, with("id2", id2).and("type", type).parameters());

		}

		@SuppressWarnings("unchecked")
		public List<Classement_archivage_niveau_02> getListClassementNiveau02ByTypeConnecte(
				int pocesseurId, int typePocesseur) {
			return (List<Classement_archivage_niveau_02>) dao.getListWithNamedQuery(LIST_Classement_archivage_niveau_02, with("idp", pocesseurId).and("idt",typePocesseur).parameters());
			
		}

		public List<Classement_archivage_niveau_01> getListClassementNiveau01ByTypeConnecte(
				int pocesseurId, int typePocesseur) {
			return (List<Classement_archivage_niveau_01>) dao.getListWithNamedQuery(LIST_Classement_archivage_niveau_01, with("idp", pocesseurId).and("idt",typePocesseur).parameters());
			
		}

		@SuppressWarnings("unchecked")
		public List<Unite> getUniteParCodeCentreCout(String idUniteparente) {
			return (List<Unite>) dao.getListWithNamedQuery(UNITE_BY_CODE_SONEDE_PARENT,
					with("c", idUniteparente).parameters());
			
		}	
		
		//Courrier Lies 
		public List<CourrierInformations> findCourrierEnvoyerANDRecuByCriteriaLies(
				boolean isResponsable, List<Integer> listIdsSousUnites,
				List<Integer> listIdSubordonnes, HashMap<String, Object> filterMap,
				String sortField, boolean descending, String secretaire,
				String sub, String unit, int jourOrAutre, Date dateDebut,
				Date dateFin, String type, String type1, String typeSecretaire,
				Integer idUser, Integer typeTransmission, String stateTraitement,
				int firstIndex, int maxResult, boolean forRapport,
				Integer courrierRubriqueId,String typeCourrier, String DBType, Integer idCourrierCourant,int flagueCloture,int flagInterne) {
			return dao.findCourrierEnvoyerANDRecuWithCriterionLies(
					isResponsable, listIdsSousUnites, listIdSubordonnes, filterMap,
					sortField, descending, secretaire, sub, unit, jourOrAutre,
					dateDebut, dateFin, type, type1, typeSecretaire, idUser,
					typeTransmission, stateTraitement, firstIndex, maxResult,
					forRapport, courrierRubriqueId, typeCourrier, DBType,idCourrierCourant, flagueCloture, flagInterne);
		}

		public List<Mail> listMailByDestinataireAdresse(String adresseMailConnecte,int mailState) {
			return (List<Mail>) dao.getListMailByDestinataireAdresse(adresseMailConnecte,mailState);
					
		}

		@SuppressWarnings("unchecked")
		public List<Unite> getUniteParMail(String mailExpediteur) {
			return (List<Unite>) dao.getListWithNamedQuery(UNITE_BY_MAIL,
					with("m", mailExpediteur).parameters());
		}
		@SuppressWarnings("unchecked")
		public List<Unite> getListeBoParId(String idBO){
			return (List<Unite>) dao.getListWithNamedQuery(LISTE_BO_BYIDBD,
					with("idB", idBO).parameters());
			
		}
//	public List<AttachmentFileBean>listeMailAttachementFilesByIdMessage(int messageId){
//		return dao.listeMailAttachementFilesByIdMessage(messageId);
//	}
//
//	public List<AttachmentHeadBean> getMailHederByIdMail(Integer mailId) {
//		// TODO Auto-generated method stub
//		return dao.listeMailHederByIdMail(mailId);
//	}

	@SuppressWarnings("unchecked")
	public List<AoConsultation> getListAoConsultationOrdonne() {
	return (List<AoConsultation>) dao.getListWithNamedQuery(LIST_AO_ORDER_BY_DATE);
	}
	
	@SuppressWarnings("unchecked")
	public List<AoConsultation> getListAoConsultationParNumero(String numAO) {
		return (List<AoConsultation>) dao.getListWithNamedQuery(LIST_AO_BY_NUMERO,
				with("num", numAO).parameters());
		}

	public int getTransactionReceptionPhysique(int idBOC, int refdossier) {
		return dao.getTransactionReceptionPhysique(idBOC, refdossier);
	}

	public List<String> listeMessageIDIdMaile(Integer mailId, String messageID) {
		return dao.listeMessageIDIdMaile(mailId, messageID);
	}
	
	@SuppressWarnings("unchecked")
		public List<Transaction> listTransactionByDossier(int dossierId){
			return (List<Transaction>) dao.getListWithNamedQuery(LIST_TRANSACTION_BY_DOSSIER, with("dossierId", dossierId)
						.parameters());
	}
	
	
	
	public int getCountTransactionByIdExpExterne(int idExpediteur) {
		return dao.getCountTransactionByIdExpExterne(idExpediteur);
	}

	@SuppressWarnings("unchecked")
	public List<Employe> getEmployerByMatricule(String shortName) {
		return (List<Employe>) dao.getListWithNamedQuery(LIST_EMPLOYE_BY_MATRICULE, with("c", shortName)
				.parameters());
	}
	
}
