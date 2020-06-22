package xtensus.beans.common.GBO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import xtensus.aop.LogClass;
import xtensus.beans.common.LanguageManagerBean;
import xtensus.beans.common.VariableGlobale;
import xtensus.beans.common.VariableGlobaleNotification;
import xtensus.beans.utils.CourrierConsulterInformations;
import xtensus.beans.utils.Informations;
import xtensus.dao.utils.DMSConnexionImplement;
import xtensus.entity.Commentaire;
import xtensus.entity.Confidentialite;
import xtensus.entity.Courrier;
import xtensus.entity.CourrierDossier;
import xtensus.entity.Dossier;
import xtensus.entity.Etat;
import xtensus.entity.Expdest;
import xtensus.entity.Expdestexterne;
import xtensus.entity.Nature;
import xtensus.entity.Transaction;
import xtensus.entity.TransactionAnnotation;
import xtensus.entity.TransactionAnnotationId;
import xtensus.entity.TransactionDestination;
import xtensus.entity.TransactionDestinationId;
import xtensus.entity.TransactionDestinationReelle;
import xtensus.entity.Transmission;
import xtensus.entity.Typedossier;
import xtensus.entity.Typetransaction;
import xtensus.entity.Urgence;
import xtensus.entity.Utilisateur;
import xtensus.entity.Variables;
import xtensus.entity.Workflow;
import xtensus.ldap.business.LdapOperation;
import xtensus.ldap.model.Person;
import xtensus.ldap.model.Unit;
import xtensus.services.ApplicationManager;
import xtensus.services.Export;
import xtensus.workflow.beans.JBPMAccessProcessBean;
import xtensus.workflow.handlers.TraitementEtapeSuivant;

@Component
@Scope("request")
public class CourrierConsultationBeanAncienAncienAncien {

	private ApplicationManager appMgr;
	private Export export;
	private DataModel listCourrier;
	private DataModel listCourrierJour;
	private DataModel listDossier;
	private DataModel listDossierJour;
	private Courrier courrier;
	private Dossier dossier;
	private boolean status;
	private Date date;
	private Transaction transaction;
	private TransactionDestination transactionDestination;
	private TransactionAnnotation transactionAnnotation;
	private Nature nature;
	private Transmission transmission;
	private Confidentialite confidentialite;
	private Urgence urgence;
	private Expdestexterne expdestexterne;
	private List<Expdestexterne> listDestExpdestexternes;
	private Utilisateur utilisateur;
	private List<CourrierConsulterInformations> courrierConsulterInformations;
	private List<CourrierConsulterInformations> listcourrierConsulterInformationsJour;
	private List<CourrierConsulterInformations> dossierConsulterInformations;
	private List<CourrierConsulterInformations> listdossierConsulterInformationsJour;
	private List<CourrierConsulterInformations> listCourriersRecus;
	private List<CourrierConsulterInformations> listCourriersRecusAvalider;
	private List<CourrierConsulterInformations> listCourriersRecusJour;
	private List<CourrierConsulterInformations> listCourriersRecusJourAvalider;
	private List<CourrierConsulterInformations> listCourriersEnvoyes;
	private List<CourrierConsulterInformations> listCourriersEnvoyesJour;
	private List<CourrierConsulterInformations> listDossiersRecus;
	private List<CourrierConsulterInformations> listDossiersRecusJour;
	private List<CourrierConsulterInformations> listDossiersEnvoyes;
	private List<CourrierConsulterInformations> listDossiersEnvoyesJour;
	private List<CourrierConsulterInformations> listCourriersDeValidationJour;
	private List<CourrierConsulterInformations> listCourriersDeValidation;
	private List<CourrierConsulterInformations> listCourriersTraiteJour;
	private List<CourrierConsulterInformations> listCourriersTraite;
	private List<CourrierConsulterInformations> listCourriersAValiderJour;
	private List<CourrierConsulterInformations> listCourriersAValider;
	private List<CourrierConsulterInformations> listCourriersValideJour;
	private List<CourrierConsulterInformations> listCourriersValide;
	private List<CourrierConsulterInformations> listCourriersNonValideJour;
	private List<CourrierConsulterInformations> listCourriersNonValide;
	// Courrier Boc
	private List<CourrierConsulterInformations> listCourriersTousBoc;
	private List<CourrierConsulterInformations> listCourriersTousBocTraite;
	private List<CourrierConsulterInformations> listCourriersTousBocNonTraite;
	private List<CourrierConsulterInformations> listCourriersTousBocArrive;
	private List<CourrierConsulterInformations> listCourriersTousBocArriveTraite;
	private List<CourrierConsulterInformations> listCourriersTousBocArriveNonTraite;
	private List<CourrierConsulterInformations> listCourriersTousBocDepart;
	private List<CourrierConsulterInformations> listCourriersTousBocDepartTraite;
	private List<CourrierConsulterInformations> listCourriersTousBocDepartNonTraite;
	private List<CourrierConsulterInformations> listCourriersTousBocJour;
	private List<CourrierConsulterInformations> listCourriersTousBocTraiteJour;
	private List<CourrierConsulterInformations> listCourriersTousBocNonTraiteJour;
	private List<CourrierConsulterInformations> listCourriersTousBocArriveJour;
	private List<CourrierConsulterInformations> listCourriersTousBocArriveTraiteJour;
	private List<CourrierConsulterInformations> listCourriersTousBocArriveNonTraiteJour;
	private List<CourrierConsulterInformations> listCourriersTousBocDepartJour;
	private List<CourrierConsulterInformations> listCourriersTousBocDepartTraiteJour;
	private List<CourrierConsulterInformations> listCourriersTousBocDepartNonTraiteJour;

	private List<CourrierConsulterInformations> listCourriersFaxBoc;
	private List<CourrierConsulterInformations> listCourriersFaxBocTraite;
	private List<CourrierConsulterInformations> listCourriersFaxBocNonTraite;
	private List<CourrierConsulterInformations> listCourriersFaxBocArrive;
	private List<CourrierConsulterInformations> listCourriersFaxBocArriveTraite;
	private List<CourrierConsulterInformations> listCourriersFaxBocArriveNonTraite;
	private List<CourrierConsulterInformations> listCourriersFaxBocDepart;
	private List<CourrierConsulterInformations> listCourriersFaxBocDepartTraite;
	private List<CourrierConsulterInformations> listCourriersFaxBocDepartNonTraite;
	private List<CourrierConsulterInformations> listCourriersFaxBocJour;
	private List<CourrierConsulterInformations> listCourriersFaxBocTraiteJour;
	private List<CourrierConsulterInformations> listCourriersFaxBocNonTraiteJour;
	private List<CourrierConsulterInformations> listCourriersFaxBocArriveJour;
	private List<CourrierConsulterInformations> listCourriersFaxBocArriveTraiteJour;
	private List<CourrierConsulterInformations> listCourriersFaxBocArriveNonTraiteJour;
	private List<CourrierConsulterInformations> listCourriersFaxBocDepartJour;
	private List<CourrierConsulterInformations> listCourriersFaxBocDepartTraiteJour;
	private List<CourrierConsulterInformations> listCourriersFaxBocDepartNonTraiteJour;

	private List<CourrierConsulterInformations> listCourriersMailBoc;
	private List<CourrierConsulterInformations> listCourriersMailBocTraite;
	private List<CourrierConsulterInformations> listCourriersMailBocNonTraite;
	private List<CourrierConsulterInformations> listCourriersMailBocArrive;
	private List<CourrierConsulterInformations> listCourriersMailBocArriveTraite;
	private List<CourrierConsulterInformations> listCourriersMailBocArriveNonTraite;
	private List<CourrierConsulterInformations> listCourriersMailBocDepart;
	private List<CourrierConsulterInformations> listCourriersMailBocDepartTraite;
	private List<CourrierConsulterInformations> listCourriersMailBocDepartNonTraite;
	private List<CourrierConsulterInformations> listCourriersMailBocJour;
	private List<CourrierConsulterInformations> listCourriersMailBocTraiteJour;
	private List<CourrierConsulterInformations> listCourriersMailBocNonTraiteJour;
	private List<CourrierConsulterInformations> listCourriersMailBocArriveJour;
	private List<CourrierConsulterInformations> listCourriersMailBocArriveTraiteJour;
	private List<CourrierConsulterInformations> listCourriersMailBocArriveNonTraiteJour;
	private List<CourrierConsulterInformations> listCourriersMailBocDepartJour;
	private List<CourrierConsulterInformations> listCourriersMailBocDepartTraiteJour;
	private List<CourrierConsulterInformations> listCourriersMailBocDepartNonTraiteJour;

	private List<CourrierConsulterInformations> listCourriersAutreBoc;
	private List<CourrierConsulterInformations> listCourriersAutreBocTraite;
	private List<CourrierConsulterInformations> listCourriersAutreBocNonTraite;
	private List<CourrierConsulterInformations> listCourriersAutreBocArrive;
	private List<CourrierConsulterInformations> listCourriersAutreBocArriveTraite;
	private List<CourrierConsulterInformations> listCourriersAutreBocArriveNonTraite;
	private List<CourrierConsulterInformations> listCourriersAutreBocDepart;
	private List<CourrierConsulterInformations> listCourriersAutreBocDepartTraite;
	private List<CourrierConsulterInformations> listCourriersAutreBocDepartNonTraite;
	private List<CourrierConsulterInformations> listCourriersAutreBocJour;
	private List<CourrierConsulterInformations> listCourriersAutreBocTraiteJour;
	private List<CourrierConsulterInformations> listCourriersAutreBocNonTraiteJour;
	private List<CourrierConsulterInformations> listCourriersAutreBocArriveJour;
	private List<CourrierConsulterInformations> listCourriersAutreBocArriveTraiteJour;
	private List<CourrierConsulterInformations> listCourriersAutreBocArriveNonTraiteJour;
	private List<CourrierConsulterInformations> listCourriersAutreBocDepartJour;
	private List<CourrierConsulterInformations> listCourriersAutreBocDepartTraiteJour;
	private List<CourrierConsulterInformations> listCourriersAutreBocDepartNonTraiteJour;

	private long records = 0;
	private long recordsJour = 0;
	private long recordsDossier = 0;
	private long recordsDossierJour = 0;
	@Autowired
	private VariableGlobale vb;
	@Autowired
	private LanguageManagerBean lm;
	@Autowired
	private MessageSource messageSource;
	private String message;
	private LdapOperation ldapOperation;
	private Date dateReception;
	private Date dateReponse;
	private String destinataire;
	private String typeCourrier;
	private String typeCourrierJour;
	private String typeDossier;
	private String typeDossierJour;
	private String name;
	private String surname;
	private String typeCourrierValidationJour;
	private String typeCourrierValidation;
	private boolean moreChoices;
	private boolean allMailChecked;
	private boolean toValidateMailChecked;
	private boolean validatedMailChecked;
	private boolean notValidatedMailChecked;
	private boolean treatedMailChecked;
	private boolean moreChoicesJour;
	private boolean allMailCheckedJour;
	private boolean toValidateMailCheckedJour;
	private boolean validatedMailCheckedJour;
	private boolean notValidatedMailCheckedJour;
	private boolean treatedMailCheckedJour;
	private Variables variableConsultationCourrierSecretaire;
	private Variables variableConsultationCourrierSubordonne;
	private Variables variableConsultationCourrierSousUnite;
	private boolean showTab;
	private boolean bocOption;
	private boolean userOption;
	private List<Transmission> listTr;
	private String transmissionCourrierJour;
	private String transmissionCourrier;
	private String typeCourrierTraitementJour;
	private String typeCourrierTraitement;
	private String categorieCourrierJour;
	private String categorieCourrier;
	private boolean showExecuteAllButtonJour;
	private boolean showExecuteAllButton;
	private boolean hideExecuteAllButtonJour;
	private boolean hideExecuteAllButton;
	private Informations info1, info2, info3, info4;
	private List<Informations> listInfo;
	private String typeNotification;
	@Autowired
	private VariableGlobaleNotification vbn;
	private Transaction newTransaction;
	private boolean status1;
	private boolean status2;
	private int idBoc;
	private int nouveauCourrier = 0;
	private CourrierConsulterInformations consulterInformations;

	public CourrierConsultationBeanAncienAncienAncien() {
	}

	@Autowired
	public CourrierConsultationBeanAncienAncienAncien(
			@Qualifier("applicationManager") ApplicationManager appMgr,
			@Qualifier("export") Export export) {
		this.appMgr = appMgr;
		this.export = export;
		listCourrier = new ListDataModel();
		listCourrierJour = new ListDataModel();
		listDossier = new ListDataModel();
		listDossierJour = new ListDataModel();
		courrier = new Courrier();
		dossier = new Dossier();
		transaction = new Transaction();
		ldapOperation = new LdapOperation();
		transactionDestination = new TransactionDestination();
		transactionAnnotation = new TransactionAnnotation();
		variableConsultationCourrierSecretaire = new Variables();
		variableConsultationCourrierSubordonne = new Variables();
		variableConsultationCourrierSousUnite = new Variables();
		listTr = new ArrayList<Transmission>();
		nature = new Nature();
		transmission = new Transmission();
		confidentialite = new Confidentialite();
		urgence = new Urgence();
		expdestexterne = new Expdestexterne();
		listDestExpdestexternes = new ArrayList<Expdestexterne>();
		utilisateur = new Utilisateur();
		listCourriersRecus = new ArrayList<CourrierConsulterInformations>();
		listCourriersRecusAvalider = new ArrayList<CourrierConsulterInformations>();
		listCourriersEnvoyes = new ArrayList<CourrierConsulterInformations>();
		listCourriersRecusJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersRecusJourAvalider = new ArrayList<CourrierConsulterInformations>();
		listCourriersEnvoyesJour = new ArrayList<CourrierConsulterInformations>();
		listDossiersRecus = new ArrayList<CourrierConsulterInformations>();
		listDossiersEnvoyes = new ArrayList<CourrierConsulterInformations>();
		listDossiersRecusJour = new ArrayList<CourrierConsulterInformations>();
		listDossiersEnvoyesJour = new ArrayList<CourrierConsulterInformations>();
		courrierConsulterInformations = new ArrayList<CourrierConsulterInformations>();
		listcourrierConsulterInformationsJour = new ArrayList<CourrierConsulterInformations>();
		dossierConsulterInformations = new ArrayList<CourrierConsulterInformations>();
		listdossierConsulterInformationsJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersDeValidationJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersDeValidation = new ArrayList<CourrierConsulterInformations>();
		listCourriersTraiteJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersTraite = new ArrayList<CourrierConsulterInformations>();
		listCourriersAValiderJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersAValider = new ArrayList<CourrierConsulterInformations>();
		listCourriersValideJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersValide = new ArrayList<CourrierConsulterInformations>();
		listCourriersNonValideJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersNonValide = new ArrayList<CourrierConsulterInformations>();

		listCourriersTousBoc = new ArrayList<CourrierConsulterInformations>();
		listCourriersTousBocArrive = new ArrayList<CourrierConsulterInformations>();
		listCourriersTousBocArriveTraite = new ArrayList<CourrierConsulterInformations>();
		listCourriersTousBocArriveNonTraite = new ArrayList<CourrierConsulterInformations>();
		listCourriersTousBocDepart = new ArrayList<CourrierConsulterInformations>();
		listCourriersTousBocDepartTraite = new ArrayList<CourrierConsulterInformations>();
		listCourriersTousBocDepartNonTraite = new ArrayList<CourrierConsulterInformations>();
		listCourriersTousBocJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersTousBocArriveJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersTousBocArriveTraiteJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersTousBocArriveNonTraiteJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersTousBocDepartJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersTousBocDepartTraiteJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersTousBocDepartNonTraiteJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersTousBocTraite = new ArrayList<CourrierConsulterInformations>();
		listCourriersTousBocNonTraite = new ArrayList<CourrierConsulterInformations>();
		listCourriersTousBocTraiteJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersTousBocNonTraiteJour = new ArrayList<CourrierConsulterInformations>();

		listCourriersFaxBoc = new ArrayList<CourrierConsulterInformations>();
		listCourriersFaxBocArrive = new ArrayList<CourrierConsulterInformations>();
		listCourriersFaxBocArriveTraite = new ArrayList<CourrierConsulterInformations>();
		listCourriersFaxBocArriveNonTraite = new ArrayList<CourrierConsulterInformations>();
		listCourriersFaxBocDepart = new ArrayList<CourrierConsulterInformations>();
		listCourriersFaxBocDepartTraite = new ArrayList<CourrierConsulterInformations>();
		listCourriersFaxBocDepartNonTraite = new ArrayList<CourrierConsulterInformations>();
		listCourriersFaxBocJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersFaxBocArriveJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersFaxBocArriveTraiteJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersFaxBocArriveNonTraiteJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersFaxBocDepartJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersFaxBocDepartTraiteJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersFaxBocDepartNonTraiteJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersFaxBocTraite = new ArrayList<CourrierConsulterInformations>();
		listCourriersFaxBocNonTraite = new ArrayList<CourrierConsulterInformations>();
		listCourriersFaxBocTraiteJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersFaxBocNonTraiteJour = new ArrayList<CourrierConsulterInformations>();

		listCourriersMailBoc = new ArrayList<CourrierConsulterInformations>();
		listCourriersMailBocArrive = new ArrayList<CourrierConsulterInformations>();
		listCourriersMailBocArriveTraite = new ArrayList<CourrierConsulterInformations>();
		listCourriersMailBocArriveNonTraite = new ArrayList<CourrierConsulterInformations>();
		listCourriersMailBocDepart = new ArrayList<CourrierConsulterInformations>();
		listCourriersMailBocDepartTraite = new ArrayList<CourrierConsulterInformations>();
		listCourriersMailBocDepartNonTraite = new ArrayList<CourrierConsulterInformations>();
		listCourriersMailBocJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersMailBocArriveJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersMailBocArriveTraiteJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersMailBocArriveNonTraiteJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersMailBocDepartJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersMailBocDepartTraiteJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersMailBocDepartNonTraiteJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersMailBocTraite = new ArrayList<CourrierConsulterInformations>();
		listCourriersMailBocNonTraite = new ArrayList<CourrierConsulterInformations>();
		listCourriersMailBocTraiteJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersMailBocNonTraiteJour = new ArrayList<CourrierConsulterInformations>();

		listCourriersAutreBoc = new ArrayList<CourrierConsulterInformations>();
		listCourriersAutreBocArrive = new ArrayList<CourrierConsulterInformations>();
		listCourriersAutreBocArriveTraite = new ArrayList<CourrierConsulterInformations>();
		listCourriersAutreBocArriveNonTraite = new ArrayList<CourrierConsulterInformations>();
		listCourriersAutreBocDepart = new ArrayList<CourrierConsulterInformations>();
		listCourriersAutreBocDepartTraite = new ArrayList<CourrierConsulterInformations>();
		listCourriersAutreBocDepartNonTraite = new ArrayList<CourrierConsulterInformations>();
		listCourriersAutreBocJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersAutreBocArriveJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersAutreBocArriveTraiteJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersAutreBocArriveNonTraiteJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersAutreBocDepartJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersAutreBocDepartTraiteJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersAutreBocDepartNonTraiteJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersAutreBocTraite = new ArrayList<CourrierConsulterInformations>();
		listCourriersAutreBocNonTraite = new ArrayList<CourrierConsulterInformations>();
		listCourriersAutreBocTraiteJour = new ArrayList<CourrierConsulterInformations>();
		listCourriersAutreBocNonTraiteJour = new ArrayList<CourrierConsulterInformations>();

		listInfo = new ArrayList<Informations>();
		info1 = new Informations();
		info2 = new Informations();
		info3 = new Informations();

		System.out
				.println("**************BeanInjecte CourrierConsultationBean *********");
	}

	@PostConstruct
	public void Initialize() {
		showTab = true;
		bocOption = false;
		userOption = true;
		if (vb.getPerson() != null) {
			if (vb.getPerson().isBoc()) {
				showTab = false;
				bocOption = true;
				userOption = false;
			}
		}
		typeCourrierValidationJour = "";
		typeCourrierValidation = "";
		transmissionCourrierJour = "Tout les courriers";
		transmissionCourrier = "Tout les courriers";
		typeCourrierTraitementJour = "tous";
		typeCourrierTraitement = "tous";
		categorieCourrierJour = "tous";
		categorieCourrier = "tous";
		typeCourrier = "Tous";
		typeCourrierJour = "Tous";
		typeDossier = "Tous";
		typeDossierJour = "Tous";
		moreChoices = false;
		moreChoicesJour = false;
		showExecuteAllButtonJour = false;
		showExecuteAllButton = false;
		hideExecuteAllButtonJour = true;
		hideExecuteAllButton = true;
		try {
			variableConsultationCourrierSecretaire = appMgr
					.listVariablesByLibelle("consultation_courrier_secretaire")
					.get(0);
			variableConsultationCourrierSubordonne = appMgr
					.listVariablesByLibelle("consultation_courrier_subordonne")
					.get(0);
			variableConsultationCourrierSousUnite = appMgr
					.listVariablesByLibelle("consultation_courrier_sous_unite")
					.get(0);

			listTr = appMgr.getList(Transmission.class);

			if (vb.getPerson().isBoc()) {
				if (!vb.isConnected()) {
					process();
					// **
					vb.getListCourrierForLiens().addAll(listCourriersTousBoc);
					vb.getListCourrierForLiens().addAll(listCourriersTousBocJour);
					// **
					vb.setCopyListCourriersTousBoc(listCourriersTousBoc);
					vb.setCopyListCourriersTousBocTraite(listCourriersTousBocTraite);
					vb.setCopyListCourriersTousBocNonTraite(listCourriersTousBocNonTraite);
					vb.setCopyListCourriersTousBocArrive(listCourriersTousBocArrive);
					vb.setCopyListCourriersTousBocArriveTraite(listCourriersTousBocArriveTraite);
					vb.setCopyListCourriersTousBocArriveNonTraite(listCourriersTousBocArriveNonTraite);
					vb.setCopyListCourriersTousBocDepart(listCourriersTousBocDepart);
					vb.setCopyListCourriersTousBocDepartTraite(listCourriersTousBocDepartTraite);
					vb.setCopyListCourriersTousBocDepartNonTraite(listCourriersTousBocDepartNonTraite);
					vb.setCopyListCourriersMailBoc(listCourriersMailBoc);
					vb.setCopyListCourriersMailBocTraite(listCourriersMailBocTraite);
					vb.setCopyListCourriersMailBocNonTraite(listCourriersMailBocNonTraite);
					vb.setCopyListCourriersMailBocArrive(listCourriersMailBocArrive);
					vb.setCopyListCourriersMailBocArriveTraite(listCourriersMailBocArriveTraite);
					vb.setCopyListCourriersMailBocArriveNonTraite(listCourriersMailBocArriveNonTraite);
					vb.setCopyListCourriersMailBocDepart(listCourriersMailBocDepart);
					vb.setCopyListCourriersMailBocDepartTraite(listCourriersMailBocDepartTraite);
					vb.setCopyListCourriersMailBocDepartNonTraite(listCourriersMailBocDepartNonTraite);
					vb.setCopyListCourriersFaxBoc(listCourriersFaxBoc);
					vb.setCopyListCourriersFaxBocTraite(listCourriersFaxBocTraite);
					vb.setCopyListCourriersFaxBocNonTraite(listCourriersFaxBocNonTraite);
					vb.setCopyListCourriersFaxBocArrive(listCourriersFaxBocArrive);
					vb.setCopyListCourriersFaxBocArriveTraite(listCourriersFaxBocArriveTraite);
					vb.setCopyListCourriersFaxBocArriveNonTraite(listCourriersFaxBocArriveNonTraite);
					vb.setCopyListCourriersFaxBocDepart(listCourriersFaxBocDepart);
					vb.setCopyListCourriersFaxBocDepartTraite(listCourriersFaxBocDepartTraite);
					vb.setCopyListCourriersFaxBocDepartNonTraite(listCourriersFaxBocDepartNonTraite);
					vb.setCopyListCourriersAutreBoc(listCourriersAutreBoc);
					vb.setCopyListCourriersAutreBocTraite(listCourriersAutreBocTraite);
					vb.setCopyListCourriersAutreBocNonTraite(listCourriersAutreBocNonTraite);
					vb.setCopyListCourriersAutreBocArrive(listCourriersAutreBocArrive);
					vb.setCopyListCourriersAutreBocArriveTraite(listCourriersAutreBocArriveTraite);
					vb.setCopyListCourriersAutreBocArriveNonTraite(listCourriersAutreBocArriveNonTraite);
					vb.setCopyListCourriersAutreBocDepart(listCourriersAutreBocDepart);
					vb.setCopyListCourriersAutreBocDepartTraite(listCourriersAutreBocDepartTraite);
					vb.setCopyListCourriersAutreBocDepartNonTraite(listCourriersAutreBocDepartNonTraite);
					vb.setConnected(true);
				} else {
					process();
					listCourriersTousBoc = vb.getCopyListCourriersTousBoc();
					// **
					vb.setListCourrierForLiens(new ArrayList<CourrierConsulterInformations>());
					vb.getListCourrierForLiens().addAll(listCourriersTousBoc);
					vb.getListCourrierForLiens().addAll(listCourriersTousBocJour);
					// **
					listCourriersTousBocTraite = vb
							.getCopyListCourriersTousBocTraite();
					listCourriersTousBocNonTraite = vb
							.getCopyListCourriersTousBocNonTraite();
					listCourriersTousBocArrive = vb
							.getCopyListCourriersTousBocArrive();
					listCourriersTousBocArriveTraite = vb
							.getCopyListCourriersTousBocArriveTraite();
					listCourriersTousBocArriveNonTraite = vb
							.getCopyListCourriersTousBocArriveNonTraite();
					listCourriersTousBocDepart = vb
							.getCopyListCourriersTousBocDepart();
					listCourriersTousBocDepartTraite = vb
							.getCopyListCourriersTousBocDepartTraite();
					listCourriersTousBocDepartNonTraite = vb
							.getCopyListCourriersTousBocDepartNonTraite();
					listCourriersMailBoc = vb.getCopyListCourriersMailBoc();
					listCourriersMailBocTraite = vb
							.getCopyListCourriersMailBocTraite();
					listCourriersMailBocNonTraite = vb
							.getCopyListCourriersMailBocNonTraite();
					listCourriersMailBocArrive = vb
							.getCopyListCourriersMailBocArrive();
					listCourriersMailBocArriveTraite = vb
							.getCopyListCourriersMailBocArriveTraite();
					listCourriersMailBocArriveNonTraite = vb
							.getCopyListCourriersMailBocArriveNonTraite();
					listCourriersMailBocDepart = vb
							.getCopyListCourriersMailBocDepart();
					listCourriersMailBocDepartTraite = vb
							.getCopyListCourriersMailBocDepartTraite();
					listCourriersMailBocDepartNonTraite = vb
							.getCopyListCourriersMailBocDepartNonTraite();
					listCourriersFaxBoc = vb.getCopyListCourriersFaxBoc();
					listCourriersFaxBocTraite = vb
							.getCopyListCourriersFaxBocTraite();
					listCourriersFaxBocNonTraite = vb
							.getCopyListCourriersFaxBocNonTraite();
					listCourriersFaxBocArrive = vb
							.getCopyListCourriersFaxBocArrive();
					listCourriersFaxBocArriveTraite = vb
							.getCopyListCourriersFaxBocArriveTraite();
					listCourriersFaxBocArriveNonTraite = vb
							.getCopyListCourriersFaxBocArriveNonTraite();
					listCourriersFaxBocDepart = vb
							.getCopyListCourriersFaxBocDepart();
					listCourriersFaxBocDepartTraite = vb
							.getCopyListCourriersFaxBocDepartTraite();
					listCourriersFaxBocDepartNonTraite = vb
							.getCopyListCourriersFaxBocDepartNonTraite();
					listCourriersAutreBoc = vb.getCopyListCourriersAutreBoc();
					listCourriersAutreBocTraite = vb
							.getCopyListCourriersAutreBocTraite();
					listCourriersAutreBocNonTraite = vb
							.getCopyListCourriersAutreBocNonTraite();
					listCourriersAutreBocArrive = vb
							.getCopyListCourriersAutreBocArrive();
					listCourriersAutreBocArriveTraite = vb
							.getCopyListCourriersAutreBocArriveTraite();
					listCourriersAutreBocArriveNonTraite = vb
							.getCopyListCourriersAutreBocArriveNonTraite();
					listCourriersAutreBocDepart = vb
							.getCopyListCourriersAutreBocDepart();
					listCourriersAutreBocDepartTraite = vb
							.getCopyListCourriersAutreBocDepartTraite();
					listCourriersAutreBocDepartNonTraite = vb
							.getCopyListCourriersAutreBocDepartNonTraite();
				}
				listCourrier.setWrappedData(listCourriersTousBoc);
				listCourrierJour.setWrappedData(listCourriersTousBocJour);
			} else {
				if (!vb.isConnected()) {
					process();
					// **
					vb.getListCourrierForLiens().addAll(courrierConsulterInformations);
					vb.getListCourrierForLiens().addAll(listcourrierConsulterInformationsJour);
					// **
					vb.setCopyListCourriersTous(courrierConsulterInformations);
					vb.setCopyListCourriersEnvoyes(listCourriersEnvoyes);
					vb.setCopyListCourriersRecus(listCourriersRecus);
					vb.setCopyListCourriersRecusAvalider(listCourriersRecusAvalider);
					vb.setCopyListDossiersEnvoyes(listDossiersEnvoyes);
					vb.setCopyListDossiersRecus(listDossiersRecus);
					vb.setCopyListDossiersTous(dossierConsulterInformations);
					vb.setCopyListCourriersDeValidation(listCourriersDeValidation);
					vb.setCopyListCourriersTraite(listCourriersTraite);
					vb.setCopyListCourriersAValider(listCourriersAValider);
					vb.setCopyListCourriersValide(listCourriersValide);
					vb.setCopyListCourriersNonValide(listCourriersNonValide);
					vb.setConnected(true);
				} else {
					process();
					
					courrierConsulterInformations = vb
							.getCopyListCourriersTous();
					// **
					vb.setListCourrierForLiens(new ArrayList<CourrierConsulterInformations>());
					vb.getListCourrierForLiens().addAll(courrierConsulterInformations);
					vb.getListCourrierForLiens().addAll(listcourrierConsulterInformationsJour);
					// **
					listCourriersEnvoyes = vb.getCopyListCourriersEnvoyes();
					listCourriersRecus = vb.getCopyListCourriersRecus();
					listCourriersRecusAvalider = vb
							.getCopyListCourriersRecusAvalider();
					listDossiersEnvoyes = vb.getCopyListDossiersEnvoyes();
					listDossiersRecus = vb.getCopyListDossiersRecus();
					listCourriersDeValidation = vb
							.getCopyListCourriersDeValidation();
					listCourriersTraite = vb.getCopyListCourriersTraite();
					listCourriersAValider = vb.getCopyListCourriersAValider();
					listCourriersValide = vb.getCopyListCourriersValide();
					listCourriersNonValide = vb.getCopyListCourriersNonValide();
					dossierConsulterInformations = vb.getCopyListDossiersTous();
				}
				listCourrier.setWrappedData(courrierConsulterInformations);
				listCourrierJour
						.setWrappedData(listcourrierConsulterInformationsJour);
				listDossier.setWrappedData(dossierConsulterInformations);
				listDossierJour
						.setWrappedData(listdossierConsulterInformationsJour);
			}

			System.out
					.println("*******ChargementAvecSucces CourrierConsultationBean******");
		} catch (Exception e) {
			System.out
					.println("*******ErreurDeChargement CourrierConsultationBean*******");
			e.printStackTrace();
		}
	}

	private void process() {
		System.out.println("Dans process");
		if (vb.getPerson().isResponsable()) {
			processForDirector(vb.getPerson().getId());
		} else if (vb.getPerson().isSecretary()) {
			processForSecretary(vb.getPerson().getId());
		} else if (vb.getPerson().isAgent()) {
			processForAgent(vb.getPerson().getId());
		} else {
			processForBoc();
		}

	}

	private void processForDirector(int id) {
		System.out.println("Dans Director");
		List<Transaction> listTransaction = new ArrayList<Transaction>();
		List<TransactionDestination> listTransactionDestination = new ArrayList<TransactionDestination>();
		String type = "unit_"
				+ String.valueOf(vb.getPerson().getAssociatedDirection()
						.getIdUnit());
		String type1 = "sub_" + String.valueOf(id);
		nouveauCourrier = appMgr
				.getListTransactionRecusUniqueAuResponsableJour(type, type1,
						new Date()).size();
		if (vb.isConnected()) {
			if (variableConsultationCourrierSecretaire.getVaraiablesValeur()
					.equals("Oui")
					&& variableConsultationCourrierSubordonne
							.getVaraiablesValeur().equals("Oui")
					&& variableConsultationCourrierSousUnite
							.getVaraiablesValeur().equals("Oui")) {
				listTransaction = appMgr
						.getListTransactionEnvoyesParResponsableJour(id, type,
								type1, new Date());
				listTransactionDestination = appMgr
						.getListTransactionRecusAuResponsableJour(id, type,
								type1, new Date());
			} else {
				listTransaction = appMgr
						.getListTransactionEnvoyesUniqueParResponsableJour(
								type, type1, new Date());
				listTransactionDestination = appMgr
						.getListTransactionRecusUniqueAuResponsableJour(type,
								type1, new Date());
				if (variableConsultationCourrierSecretaire
						.getVaraiablesValeur().equals("Oui")) {
					try {
						String typeSecretaire = "secretary_"
								+ String.valueOf(vb.getPerson()
										.getAssociatedDirection()
										.getSecretaryUnit().getId());
						listTransaction
								.addAll(appMgr
										.getListTransactionEnvoyesUniqueParSecretaireJour(
												typeSecretaire, new Date()));
						listTransactionDestination.addAll(appMgr
								.getListTransactionRecusUniqueAuSecretaireJour(
										typeSecretaire, new Date()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (variableConsultationCourrierSubordonne
						.getVaraiablesValeur().equals("Oui")) {
					listTransaction.addAll(appMgr
							.getListTransactionEnvoyesParSubordonnesJour(id,
									new Date()));
					listTransactionDestination.addAll(appMgr
							.getListTransactionRecusAuSubordonnesJour(id,
									new Date()));
				}
				if (variableConsultationCourrierSousUnite.getVaraiablesValeur()
						.equals("Oui")) {
					listTransaction.addAll(appMgr
							.getListTransactionEnvoyesParSousUniteJour(id,
									new Date()));
					listTransactionDestination.addAll(appMgr
							.getListTransactionRecusAuSousUniteJour(id,
									new Date()));
				}
			}
			System.out.println("Size liste des transactions : "
					+ listTransaction.size());
			setListCourrierEnvoyes(listTransaction);
			setListCourriersRecus(listTransactionDestination);
		} else {
			if (variableConsultationCourrierSecretaire.getVaraiablesValeur()
					.equals("Oui")
					&& variableConsultationCourrierSubordonne
							.getVaraiablesValeur().equals("Oui")
					&& variableConsultationCourrierSousUnite
							.getVaraiablesValeur().equals("Oui")) {
				listTransaction = appMgr
						.getListTransactionEnvoyesParResponsable(id, type,
								type1);
				listTransactionDestination = appMgr
						.getListTransactionRecusAuResponsable(id, type, type1);
			} else {
				listTransaction = appMgr
						.getListTransactionEnvoyesUniqueParResponsable(type,
								type1);
				listTransactionDestination = appMgr
						.getListTransactionRecusUniqueAuResponsable(type, type1);
				if (variableConsultationCourrierSecretaire
						.getVaraiablesValeur().equals("Oui")) {
					try {
						String typeSecretaire = "secretary_"
								+ String.valueOf(vb.getPerson()
										.getAssociatedDirection()
										.getSecretaryUnit().getId());
						listTransaction
								.addAll(appMgr
										.getListTransactionEnvoyesUniqueParSecretaire(typeSecretaire));
						listTransactionDestination
								.addAll(appMgr
										.getListTransactionRecusUniqueAuSecretaire(typeSecretaire));
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
				}
				if (variableConsultationCourrierSubordonne
						.getVaraiablesValeur().equals("Oui")) {
					listTransaction.addAll(appMgr
							.getListTransactionEnvoyesParSubordonnes(id));
					listTransactionDestination.addAll(appMgr
							.getListTransactionRecusAuSubordonnes(id));
				}
				if (variableConsultationCourrierSousUnite.getVaraiablesValeur()
						.equals("Oui")) {
					listTransaction.addAll(appMgr
							.getListTransactionEnvoyesParSousUnite(id));
					listTransactionDestination.addAll(appMgr
							.getListTransactionRecusAuSousUnite(id));
				}
			}

			setListCourrierEnvoyes(listTransaction);
			setListCourriersRecus(listTransactionDestination);
		}
	}

	private void processForSecretary(int id) {
		List<Transaction> listTransaction = new ArrayList<Transaction>();
		List<TransactionDestination> listTransactionDestination = new ArrayList<TransactionDestination>();
		String type = "secretary_" + String.valueOf(id);
		String type1 = "unit_"
				+ String.valueOf(vb.getPerson().getAssociatedDirection()
						.getIdUnit());
		nouveauCourrier = appMgr.getListTransactionRecusAuSecretaireJour(type,
				type1, new Date()).size();
		if (vb.isConnected()) {
			listTransaction = appMgr
					.getListTransactionEnvoyesParSecretaireJour(type, type1,
							new Date());
			setListCourrierEnvoyes(listTransaction);
			listTransactionDestination = appMgr
					.getListTransactionRecusAuSecretaireJour(type, type1,
							new Date());
			setListCourriersRecus(listTransactionDestination);
		} else {
			listTransaction = appMgr.getListTransactionEnvoyesParSecretaire(
					type, type1);
			setListCourrierEnvoyes(listTransaction);
			listTransactionDestination = appMgr
					.getListTransactionRecusAuSecretaire(type, type1);
			setListCourriersRecus(listTransactionDestination);
		}

	}

	private void processForAgent(int id) {
		List<Transaction> listTransaction = new ArrayList<Transaction>();
		List<TransactionDestination> listTransactionDestination = new ArrayList<TransactionDestination>();
		String type = "agent_" + String.valueOf(id);
		String type1 = "";

		try {
			type1 = "boc_"
					+ String.valueOf(vb.getPerson().getAssociatedBOC()
							.getIdBOC());
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		if (vb.isConnected()) {
			if (vb.getPerson().getAssociatedBOC() != null) {
				listTransaction = appMgr
						.getListTransactionEnvoyesParAgentBocJour(type, type1,
								new Date());
				setListCourrierEnvoyes(listTransaction);
				listTransactionDestination = appMgr
						.getListTransactionRecusAuAgentBocJour(type, type1,
								new Date());
				setListCourriersRecus(listTransactionDestination);
			} else {
				listTransaction = appMgr.getListTransactionEnvoyesParAgentJour(
						type, new Date());
				setListCourrierEnvoyes(listTransaction);
				listTransactionDestination = appMgr
						.getListTransactionRecusAuAgentJour(type, new Date());
				setListCourriersRecus(listTransactionDestination);
			}
			nouveauCourrier = listTransactionDestination.size();
		} else {
			if (vb.getPerson().getAssociatedBOC() != null) {
				listTransaction = appMgr.getListTransactionEnvoyesParAgentBoc(
						type, type1);
				setListCourrierEnvoyes(listTransaction);
				listTransactionDestination = appMgr
						.getListTransactionRecusAuAgentBoc(type, type1);
				setListCourriersRecus(listTransactionDestination);
			} else {
				listTransaction = appMgr
						.getListTransactionEnvoyesParAgent(type);
				setListCourrierEnvoyes(listTransaction);
				listTransactionDestination = appMgr
						.getListTransactionRecusAuAgent(type);
				setListCourriersRecus(listTransactionDestination);
			}
		}
	}

	private void processForBoc() {
		List<Transaction> listTransaction = new ArrayList<Transaction>();
		List<TransactionDestination> listTransactionDestination = new ArrayList<TransactionDestination>();
		String type = "boc_"
				+ String.valueOf(vb.getPerson().getAssociatedBOC().getIdBOC());
		Etat etat = new Etat();
		etat = appMgr.listEtatByLibelle("Traité").get(0);
		if (vb.isConnected()) {
			listTransaction = appMgr.getListTransactionBocJour(vb.getPerson()
					.getId(), new Date(), type);
			setListCourrierEnvoyes(listTransaction);
			listTransactionDestination = appMgr
					.getListTransactionDestinationBocJour(type,
							etat.getEtatId(), new Date());
			setListCourriersRecus(listTransactionDestination);
		} else {
			listTransaction = appMgr.getListTransactionBoc(vb.getPerson()
					.getId(), type);
			setListCourrierEnvoyes(listTransaction);
			listTransactionDestination = appMgr
					.getListTransactionDestinationBoc(type, etat.getEtatId());
			setListCourriersRecus(listTransactionDestination);
		}
	}

	public List<SelectItem> getSelectItemsTr() {
		String libelle;
		List<SelectItem> selectItemsTr = new ArrayList<SelectItem>();

		selectItemsTr.add(new SelectItem(messageSource.getMessage(
				"toutCourrier", new Object[] {}, lm.createLocal())));
		// selectItemsTr.add(new SelectItem("Tout les courriers"));
		// for (Transmission transmission : listTr) {
		//
		// }
		for (int j = listTr.size() - 1; j > 1; j--) {
			if (vb.getLocale().equals("ar")) {
				libelle = listTr.get(j).getTransmissionLibelleAr();
			} else {
				libelle = listTr.get(j).getTransmissionLibelle();
			}
			selectItemsTr.add(new SelectItem(libelle));
		}
		selectItemsTr.add(new SelectItem(messageSource.getMessage("AutreLabel",
				new Object[] {}, lm.createLocal())));
		return selectItemsTr;
	}

	@SuppressWarnings("deprecation")
	public void setListCourrierEnvoyes(List<Transaction> listTransactions) {
		CourrierConsulterInformations consulterInformations;
		List<TransactionDestination> listTransactionDestination;
		int refDossier = 0;
		CourrierDossier courrierDossier;
		Courrier courrier;
		Expdest expDest;
		String result;
		Date dateJour = new Date();
		for (Transaction transaction : listTransactions) {
			if (!transaction.getTypetransaction().getTypeTransactionLibelle()
					.equals("Départ")) {
				consulterInformations = new CourrierConsulterInformations();
				listTransactionDestination = new ArrayList<TransactionDestination>();
				listTransactionDestination = appMgr
						.getListTransactionDestinationByIdTransaction(transaction
								.getTransactionId());
				result = "";
				if (transaction.getTransactionDestinationReelle() != null) {
//					consulterInformations.setForValidating(true);
//					Etat etat = new Etat();
//					etat = appMgr.listEtatByRef(
//							transaction.getEtat().getEtatId()).get(0);
//					if (transaction.getTransactionDestinationReelle()
//							.getTransactionDestinationReelleDateTraitement() != null) {
//						consulterInformations.setTreated(true);
//					} else {
//						if (etat.getEtatLibelle().equals("A valider")) {
//							consulterInformations.setInValidating(true);
//						} else if (etat.getEtatLibelle().equals("Validé")) {
//							consulterInformations.setValidated(true);
//						} else if (etat.getEtatLibelle().equals("Non validé")) {
//							consulterInformations.setUnValidated(true);
//						} else {
//							consulterInformations.setUntreated(true);
//						}
//					}
					TransactionDestinationReelle transactionDestinationReelle = new TransactionDestinationReelle();
					Expdestexterne expDestExterne;
					transactionDestinationReelle = transaction
							.getTransactionDestinationReelle();
					if (transactionDestinationReelle
							.getTransactionDestinationReelleTypeDestinataire()
							.equals("Externe")) {
						System.out.print("Dans courrier DestinationReelle : ");
						if (transaction.getTypetransaction()
								.getTypeTransactionLibelle().equals("Réponse")) {
							TransactionDestination transactionDestination = new TransactionDestination();
							transactionDestination = appMgr
									.getListTransactionDestinationByIdTransaction(
											transaction.getTransactionId())
									.get(0);
							expDest = new Expdest();
							expDest = appMgr.getListExpDestByIdExpDest(
									transactionDestination.getId()
											.getIdExpDest()).get(0);
							if (expDest.getTypeExpDest().equals(
									"Interne-Person")) {
								result = result
										+ ldapOperation.getCnById(
												ldapOperation.CONTEXT_USER,
												"uid",
												expDest.getIdExpDestLdap())
										+ " / ";
							}
						} else {
							expDestExterne = new Expdestexterne();
							expDestExterne = appMgr
									.getExpediteurById(
											transactionDestinationReelle
													.getTransactionDestinationReelleIdDestinataire())
									.get(0);
							if (expDestExterne.getTypeutilisateur()
									.getTypeUtilisateurLibelle().equals("PP")) {
								result = result
										+ expDestExterne.getExpDestExterneNom()
										+ " "
										+ expDestExterne
												.getExpDestExternePrenom()
										+ " (PP)" + " / ";
							} else {
								result = result
										+ expDestExterne.getExpDestExterneNom()
										+ " (PM)" + " / ";
							}
						}
					}
				} else {
					for (TransactionDestination transactionDestination : listTransactionDestination) {
						expDest = new Expdest();
						expDest = appMgr.getListExpDestByIdExpDest(
								transactionDestination.getId().getIdExpDest())
								.get(0);
						if (expDest.getTypeExpDest().equals("Interne-Person")) {
							result = result
									+ ldapOperation.getCnById(
											ldapOperation.CONTEXT_USER, "uid",
											expDest.getIdExpDestLdap()) + " / ";
						} else if (expDest.getTypeExpDest().equals(
								"Interne-Unité")) {
							result = result
									+ ldapOperation.getCnById(
											ldapOperation.CONTEXT_UNIT,
											"departmentNumber",
											expDest.getIdExpDestLdap()) + " / ";
						} else if (expDest.getTypeExpDest().equals(
								"Interne-Boc")) {
							result = result
									+ ldapOperation.getCnById(
											ldapOperation.CONTEXT_BOC,
											"departmentNumber",
											expDest.getIdExpDestLdap()) + " / ";
						} else if (expDest.getTypeExpDest().equals("Externe")) {
							if (expDest.getExpdestexterne()
									.getTypeutilisateur()
									.getTypeUtilisateurLibelle().equals("PP")) {
								result = result
										+ expDest.getExpdestexterne()
												.getExpDestExterneNom()
										+ " "
										+ expDest.getExpdestexterne()
												.getExpDestExternePrenom()
										+ " (PP)" + " / ";
							} else {
								result = result
										+ expDest.getExpdestexterne()
												.getExpDestExterneNom()
										+ " (PM)" + " / ";
							}
						}
					}
				}
				if (!result.equals("")) {
					int lastIndex = result.lastIndexOf("/");
					result = result.substring(0, lastIndex);
				}
				consulterInformations.setCourrierDestinataireReelle(result);
				expDest = new Expdest();
				expDest = appMgr.getListExpDestByIdExpDest(
						transaction.getExpdest().getIdExpDest()).get(0);
				if (expDest.getTypeExpDest().equals("Interne-Person")) {
					consulterInformations.setCourrierExpediteur(ldapOperation
							.getCnById(ldapOperation.CONTEXT_USER, "uid",
									expDest.getIdExpDestLdap()));
				} else if (expDest.getTypeExpDest().equals("Interne-Unité")) {
					consulterInformations.setCourrierExpediteur(ldapOperation
							.getCnById(ldapOperation.CONTEXT_UNIT,
									"departmentNumber",
									expDest.getIdExpDestLdap()));
				} else if (expDest.getTypeExpDest().equals("Interne-Boc")) {
					consulterInformations.setCourrierExpediteur(ldapOperation
							.getCnById(ldapOperation.CONTEXT_BOC,
									"departmentNumber",
									expDest.getIdExpDestLdap()));
				} else if (expDest.getTypeExpDest().equals("Externe")) {
					if (expDest.getExpdestexterne().getTypeutilisateur()
							.getTypeUtilisateurLibelle().equals("PP")) {
						consulterInformations.setCourrierExpediteur(expDest
								.getExpdestexterne().getExpDestExterneNom()
								+ " "
								+ expDest.getExpdestexterne()
										.getExpDestExternePrenom() + " (PP)");
					} else {
						consulterInformations.setCourrierExpediteur(expDest
								.getExpdestexterne().getExpDestExterneNom()
								+ " (PM)");
					}
				} else {
					consulterInformations
							.setCourrierExpediteur("Universite Centrale de Tunis");
				}

				consulterInformations.setCourrierAValider(0);
				consulterInformations.setCourrierRecu(0);
				
				
				// Dossier
				Dossier dossier = new Dossier();
				dossier = transaction.getDossier();
				Typedossier typeDossier = new Typedossier();
				typeDossier = appMgr.getTypeDossierById(
						dossier.getTypedossier().getTypeDossierId()).get(0);
				if (typeDossier.getTypeDossierLibelle().equals("Default")) {
					courrierDossier = new CourrierDossier();
					refDossier = dossier.getDossierId();
					courrier = new Courrier();
					courrierDossier = appMgr.getCourrierDossierByIdDossier(
							refDossier).get(0);
					courrier = appMgr.getCourrierByIdCourrier(
							courrierDossier.getId().getIdCourrier()).get(0);
					if (transaction.getTransactionDateConsultation() != null) {
						consulterInformations.setOpened(true);
					} else {
						consulterInformations.setNotOpened(true);
					}
					consulterInformations.setTransaction(transaction);
					consulterInformations.setCourrier(courrier);
					consulterInformations.setCourrierCommentaire(courrier
							.getCourrierCommentaire());
					consulterInformations.setCourrierObjet(courrier
							.getCourrierObjet());
					consulterInformations.setCourrierReference(courrier
							.getCourrierReferenceCorrespondant());
					consulterInformations.setCourrierNature(courrier
							.getNature().getNatureLibelle());
					consulterInformations
							.setCourrierDateReceptionEnvoi(transaction
									.getTransactionDateTransaction());
					consulterInformations.setTypeCourrier(getCategorieCourrier(
							transaction, true));

					if (vb.getPerson().isBoc()) {
						Transmission transmission = new Transmission();
						transmission = courrier.getTransmission();

						if (vb.isConnected()) {
							if (transmission.getTransmissionLibelle().equals(
									"e-Mail")) {
								if (transaction.getExpdest()
										.getExpdestexterne() == null) {
									if (transaction.getEtat().getEtatLibelle()
											.equals("Traité")) {
										consulterInformations
												.setCourrierAValider(0);
										listCourriersMailBocDepartTraiteJour
												.add(0, consulterInformations);
										listCourriersTousBocDepartTraiteJour
												.add(0, consulterInformations);
										listCourriersMailBocTraiteJour.add(0,
												consulterInformations);
										listCourriersTousBocTraiteJour.add(0,
												consulterInformations);
									} else {
										consulterInformations
												.setCourrierAValider(1);
										listCourriersMailBocDepartNonTraiteJour
												.add(0, consulterInformations);
										listCourriersTousBocDepartNonTraiteJour
												.add(0, consulterInformations);
										listCourriersMailBocNonTraiteJour.add(
												0, consulterInformations);
										listCourriersTousBocNonTraiteJour.add(
												0, consulterInformations);
									}
									listCourriersMailBocDepartJour.add(0,
											consulterInformations);
									listCourriersTousBocDepartJour.add(0,
											consulterInformations);
								} else {
									if (transaction.getEtat().getEtatLibelle()
											.equals("Traité")) {
										consulterInformations
												.setCourrierAValider(0);
										listCourriersMailBocArriveTraiteJour
												.add(0, consulterInformations);
										listCourriersTousBocArriveTraiteJour
												.add(0, consulterInformations);
										listCourriersMailBocTraiteJour.add(0,
												consulterInformations);
										listCourriersTousBocTraiteJour.add(0,
												consulterInformations);
									} else {
										consulterInformations
												.setCourrierAValider(0);
										listCourriersMailBocArriveNonTraiteJour
												.add(0, consulterInformations);
										listCourriersTousBocArriveNonTraiteJour
												.add(0, consulterInformations);
										listCourriersMailBocNonTraiteJour.add(
												0, consulterInformations);
										listCourriersTousBocNonTraiteJour.add(
												0, consulterInformations);
									}
									listCourriersMailBocArriveJour.add(0,
											consulterInformations);
									listCourriersTousBocArriveJour.add(0,
											consulterInformations);
								}
								listCourriersMailBocJour.add(0,
										consulterInformations);
							} else if (transmission.getTransmissionLibelle()
									.equals("Fax")) {
								if (transaction.getExpdest()
										.getExpdestexterne() == null) {
									if (transaction.getEtat().getEtatLibelle()
											.equals("Traité")) {
										consulterInformations
												.setCourrierAValider(0);
										listCourriersFaxBocDepartTraiteJour
												.add(0, consulterInformations);
										listCourriersTousBocDepartTraiteJour
												.add(0, consulterInformations);
										listCourriersFaxBocTraiteJour.add(0,
												consulterInformations);
										listCourriersTousBocTraiteJour.add(0,
												consulterInformations);
									} else {
										consulterInformations
												.setCourrierAValider(1);
										listCourriersFaxBocDepartNonTraiteJour
												.add(0, consulterInformations);
										listCourriersTousBocDepartNonTraiteJour
												.add(0, consulterInformations);
										listCourriersFaxBocNonTraiteJour.add(0,
												consulterInformations);
										listCourriersTousBocNonTraiteJour.add(
												0, consulterInformations);
									}
									listCourriersFaxBocDepartJour.add(0,
											consulterInformations);
									listCourriersTousBocDepartJour.add(0,
											consulterInformations);
								} else {
									if (transaction.getEtat().getEtatLibelle()
											.equals("Traité")) {
										consulterInformations
												.setCourrierAValider(0);
										listCourriersFaxBocArriveTraiteJour
												.add(0, consulterInformations);
										listCourriersTousBocArriveTraiteJour
												.add(0, consulterInformations);
										listCourriersFaxBocTraiteJour.add(0,
												consulterInformations);
										listCourriersTousBocTraiteJour.add(0,
												consulterInformations);
									} else {
										consulterInformations
												.setCourrierAValider(0);
										listCourriersFaxBocArriveNonTraiteJour
												.add(0, consulterInformations);
										listCourriersTousBocArriveNonTraiteJour
												.add(0, consulterInformations);
										listCourriersFaxBocNonTraiteJour.add(0,
												consulterInformations);
										listCourriersTousBocNonTraiteJour.add(
												0, consulterInformations);
									}
									listCourriersFaxBocArriveJour.add(0,
											consulterInformations);
									listCourriersTousBocArriveJour.add(0,
											consulterInformations);
								}
								listCourriersFaxBocJour.add(0,
										consulterInformations);
							} else {
								if (transaction.getExpdest()
										.getExpdestexterne() == null) {
									if (transaction.getEtat().getEtatLibelle()
											.equals("Traité")) {
										consulterInformations
												.setCourrierAValider(0);
										listCourriersAutreBocDepartTraiteJour
												.add(0, consulterInformations);
										listCourriersTousBocDepartTraiteJour
												.add(0, consulterInformations);
										listCourriersAutreBocTraiteJour.add(0,
												consulterInformations);
										listCourriersTousBocTraiteJour.add(0,
												consulterInformations);
									} else {
										consulterInformations
												.setCourrierAValider(1);
										listCourriersAutreBocDepartNonTraiteJour
												.add(0, consulterInformations);
										listCourriersTousBocDepartNonTraiteJour
												.add(0, consulterInformations);
										listCourriersAutreBocNonTraiteJour.add(
												0, consulterInformations);
										listCourriersTousBocNonTraiteJour.add(
												0, consulterInformations);
									}
									listCourriersAutreBocDepartJour.add(0,
											consulterInformations);
									listCourriersTousBocDepartJour.add(0,
											consulterInformations);
								} else {
									if (transaction.getEtat().getEtatLibelle()
											.equals("Traité")) {
										consulterInformations
												.setCourrierAValider(0);
										listCourriersAutreBocArriveTraiteJour
												.add(0, consulterInformations);
										listCourriersTousBocArriveTraiteJour
												.add(0, consulterInformations);
										listCourriersAutreBocTraiteJour.add(0,
												consulterInformations);
										listCourriersTousBocTraiteJour.add(0,
												consulterInformations);
									} else {
										consulterInformations
												.setCourrierAValider(0);
										listCourriersAutreBocArriveNonTraiteJour
												.add(0, consulterInformations);
										listCourriersTousBocArriveNonTraiteJour
												.add(0, consulterInformations);
										listCourriersAutreBocNonTraiteJour.add(
												0, consulterInformations);
										listCourriersTousBocNonTraiteJour.add(
												0, consulterInformations);
									}
									listCourriersAutreBocArriveJour.add(0,
											consulterInformations);
									listCourriersTousBocArriveJour.add(0,
											consulterInformations);
								}
								listCourriersAutreBocJour.add(0,
										consulterInformations);
							}
							listCourriersTousBocJour.add(0,
									consulterInformations);
						} else {
							if (consulterInformations
									.getCourrierDateReceptionEnvoi().getDate() == dateJour
									.getDate()
									&& consulterInformations
											.getCourrierDateReceptionEnvoi()
											.getMonth() == dateJour.getMonth()
									&& consulterInformations
											.getCourrierDateReceptionEnvoi()
											.getYear() == dateJour.getYear()) {
								if (transmission.getTransmissionLibelle()
										.equals("e-Mail")) {
									if (transaction.getExpdest()
											.getExpdestexterne() == null) {
										if (transaction.getEtat()
												.getEtatLibelle()
												.equals("Traité")) {
											consulterInformations
													.setCourrierAValider(0);
											listCourriersMailBocDepartTraiteJour
													.add(0,
															consulterInformations);
											listCourriersTousBocDepartTraiteJour
													.add(0,
															consulterInformations);
											listCourriersMailBocTraiteJour.add(
													0, consulterInformations);
											listCourriersTousBocTraiteJour.add(
													0, consulterInformations);
										} else {
											consulterInformations
													.setCourrierAValider(1);
											listCourriersMailBocDepartNonTraiteJour
													.add(0,
															consulterInformations);
											listCourriersTousBocDepartNonTraiteJour
													.add(0,
															consulterInformations);
											listCourriersMailBocNonTraiteJour
													.add(0,
															consulterInformations);
											listCourriersTousBocNonTraiteJour
													.add(0,
															consulterInformations);
										}
										listCourriersMailBocDepartJour.add(0,
												consulterInformations);
										listCourriersTousBocDepartJour.add(0,
												consulterInformations);
									} else {
										if (transaction.getEtat()
												.getEtatLibelle()
												.equals("Traité")) {
											consulterInformations
													.setCourrierAValider(0);
											listCourriersMailBocArriveTraiteJour
													.add(0,
															consulterInformations);
											listCourriersTousBocArriveTraiteJour
													.add(0,
															consulterInformations);
											listCourriersMailBocTraiteJour.add(
													0, consulterInformations);
											listCourriersTousBocTraiteJour.add(
													0, consulterInformations);
										} else {
											consulterInformations
													.setCourrierAValider(0);
											listCourriersMailBocArriveNonTraiteJour
													.add(0,
															consulterInformations);
											listCourriersTousBocArriveNonTraiteJour
													.add(0,
															consulterInformations);
											listCourriersMailBocNonTraiteJour
													.add(0,
															consulterInformations);
											listCourriersTousBocNonTraiteJour
													.add(0,
															consulterInformations);
										}
										listCourriersMailBocArriveJour.add(0,
												consulterInformations);
										listCourriersTousBocArriveJour.add(0,
												consulterInformations);
									}
									listCourriersMailBocJour.add(0,
											consulterInformations);
								} else if (transmission
										.getTransmissionLibelle().equals("Fax")) {
									if (transaction.getExpdest()
											.getExpdestexterne() == null) {
										consulterInformations
												.setCourrierAValider(0);
										if (transaction.getEtat()
												.getEtatLibelle()
												.equals("Traité")) {
											listCourriersFaxBocDepartTraiteJour
													.add(0,
															consulterInformations);
											listCourriersTousBocDepartTraiteJour
													.add(0,
															consulterInformations);
											listCourriersFaxBocTraiteJour.add(
													0, consulterInformations);
											listCourriersTousBocTraiteJour.add(
													0, consulterInformations);
										} else {
											consulterInformations
													.setCourrierAValider(1);
											listCourriersFaxBocDepartNonTraiteJour
													.add(0,
															consulterInformations);
											listCourriersTousBocDepartNonTraiteJour
													.add(0,
															consulterInformations);
											listCourriersFaxBocNonTraiteJour
													.add(0,
															consulterInformations);
											listCourriersTousBocNonTraiteJour
													.add(0,
															consulterInformations);
										}
										listCourriersFaxBocDepartJour.add(0,
												consulterInformations);
										listCourriersTousBocDepartJour.add(0,
												consulterInformations);
									} else {
										if (transaction.getEtat()
												.getEtatLibelle()
												.equals("Traité")) {
											consulterInformations
													.setCourrierAValider(0);
											listCourriersFaxBocArriveTraiteJour
													.add(0,
															consulterInformations);
											listCourriersTousBocArriveTraiteJour
													.add(0,
															consulterInformations);
											listCourriersFaxBocTraiteJour.add(
													0, consulterInformations);
											listCourriersTousBocTraiteJour.add(
													0, consulterInformations);
										} else {
											consulterInformations
													.setCourrierAValider(0);
											listCourriersFaxBocArriveNonTraiteJour
													.add(0,
															consulterInformations);
											listCourriersTousBocArriveNonTraiteJour
													.add(0,
															consulterInformations);
											listCourriersFaxBocNonTraiteJour
													.add(0,
															consulterInformations);
											listCourriersTousBocNonTraiteJour
													.add(0,
															consulterInformations);
										}
										listCourriersFaxBocArriveJour.add(0,
												consulterInformations);
										listCourriersTousBocArriveJour.add(0,
												consulterInformations);
									}
									listCourriersFaxBocJour.add(0,
											consulterInformations);
								} else {
									if (transaction.getExpdest()
											.getExpdestexterne() == null) {
										if (transaction.getEtat()
												.getEtatLibelle()
												.equals("Traité")) {
											consulterInformations
													.setCourrierAValider(0);
											listCourriersAutreBocDepartTraiteJour
													.add(0,
															consulterInformations);
											listCourriersTousBocDepartTraiteJour
													.add(0,
															consulterInformations);
											listCourriersAutreBocTraiteJour
													.add(0,
															consulterInformations);
											listCourriersTousBocTraiteJour.add(
													0, consulterInformations);
										} else {
											consulterInformations
													.setCourrierAValider(1);
											listCourriersAutreBocDepartNonTraiteJour
													.add(0,
															consulterInformations);
											listCourriersTousBocDepartNonTraiteJour
													.add(0,
															consulterInformations);
											listCourriersAutreBocNonTraiteJour
													.add(0,
															consulterInformations);
											listCourriersTousBocNonTraiteJour
													.add(0,
															consulterInformations);
										}
										listCourriersAutreBocDepartJour.add(0,
												consulterInformations);
										listCourriersTousBocDepartJour.add(0,
												consulterInformations);
									} else {
										if (transaction.getEtat()
												.getEtatLibelle()
												.equals("Traité")) {
											consulterInformations
													.setCourrierAValider(0);
											listCourriersAutreBocArriveTraiteJour
													.add(0,
															consulterInformations);
											listCourriersTousBocArriveTraiteJour
													.add(0,
															consulterInformations);
											listCourriersAutreBocTraiteJour
													.add(0,
															consulterInformations);
											listCourriersTousBocTraiteJour.add(
													0, consulterInformations);
										} else {
											consulterInformations
													.setCourrierAValider(0);
											listCourriersAutreBocArriveNonTraiteJour
													.add(0,
															consulterInformations);
											listCourriersTousBocArriveNonTraiteJour
													.add(0,
															consulterInformations);
											listCourriersAutreBocNonTraiteJour
													.add(0,
															consulterInformations);
											listCourriersTousBocNonTraiteJour
													.add(0,
															consulterInformations);
										}
										listCourriersAutreBocArriveJour.add(0,
												consulterInformations);
										listCourriersTousBocArriveJour.add(0,
												consulterInformations);
									}
									listCourriersAutreBocJour.add(0,
											consulterInformations);
								}
								listCourriersTousBocJour.add(0,
										consulterInformations);
							} else {
								if (transmission.getTransmissionLibelle()
										.equals("e-Mail")) {
									if (transaction.getExpdest()
											.getExpdestexterne() == null) {
										if (transaction.getEtat()
												.getEtatLibelle()
												.equals("Traité")) {
											consulterInformations
													.setCourrierAValider(0);
											listCourriersMailBocDepartTraite
													.add(0,
															consulterInformations);
											listCourriersTousBocDepartTraite
													.add(0,
															consulterInformations);
											listCourriersMailBocTraite.add(0,
													consulterInformations);
											listCourriersTousBocTraite.add(0,
													consulterInformations);
										} else {
											consulterInformations
													.setCourrierAValider(1);
											listCourriersMailBocDepartNonTraite
													.add(0,
															consulterInformations);
											listCourriersTousBocDepartNonTraite
													.add(0,
															consulterInformations);
											listCourriersMailBocNonTraite.add(
													0, consulterInformations);
											listCourriersTousBocNonTraite.add(
													0, consulterInformations);
										}
										listCourriersMailBocDepart.add(0,
												consulterInformations);
										listCourriersTousBocDepart.add(0,
												consulterInformations);
									} else {
										if (transaction.getEtat()
												.getEtatLibelle()
												.equals("Traité")) {
											consulterInformations
													.setCourrierAValider(0);
											listCourriersMailBocArriveTraite
													.add(0,
															consulterInformations);
											listCourriersTousBocArriveTraite
													.add(0,
															consulterInformations);
											listCourriersMailBocTraite.add(0,
													consulterInformations);
											listCourriersTousBocTraite.add(0,
													consulterInformations);
										} else {
											consulterInformations
													.setCourrierAValider(0);
											listCourriersMailBocArriveNonTraite
													.add(0,
															consulterInformations);
											listCourriersTousBocArriveNonTraite
													.add(0,
															consulterInformations);
											listCourriersMailBocNonTraite.add(
													0, consulterInformations);
											listCourriersTousBocNonTraite.add(
													0, consulterInformations);
										}
										listCourriersMailBocArrive.add(0,
												consulterInformations);
										listCourriersTousBocArrive.add(0,
												consulterInformations);
									}
									listCourriersMailBoc.add(0,
											consulterInformations);
								} else if (transmission
										.getTransmissionLibelle().equals("Fax")) {
									if (transaction.getExpdest()
											.getExpdestexterne() == null) {
										if (transaction.getEtat()
												.getEtatLibelle()
												.equals("Traité")) {
											consulterInformations
													.setCourrierAValider(0);
											listCourriersFaxBocDepartTraite
													.add(0,
															consulterInformations);
											listCourriersTousBocDepartTraite
													.add(0,
															consulterInformations);
											listCourriersFaxBocTraite.add(0,
													consulterInformations);
											listCourriersTousBocTraite.add(0,
													consulterInformations);
										} else {
											consulterInformations
													.setCourrierAValider(1);
											listCourriersFaxBocDepartNonTraite
													.add(0,
															consulterInformations);
											listCourriersTousBocDepartNonTraite
													.add(0,
															consulterInformations);
											listCourriersFaxBocNonTraite.add(0,
													consulterInformations);
											listCourriersTousBocNonTraite.add(
													0, consulterInformations);
										}
										listCourriersFaxBocDepart.add(0,
												consulterInformations);
										listCourriersTousBocDepart.add(0,
												consulterInformations);
									} else {
										if (transaction.getEtat()
												.getEtatLibelle()
												.equals("Traité")) {
											consulterInformations
													.setCourrierAValider(0);
											listCourriersFaxBocArriveTraite
													.add(0,
															consulterInformations);
											listCourriersTousBocArriveTraite
													.add(0,
															consulterInformations);
											listCourriersFaxBocTraite.add(0,
													consulterInformations);
											listCourriersTousBocTraite.add(0,
													consulterInformations);
										} else {
											consulterInformations
													.setCourrierAValider(0);
											listCourriersFaxBocArriveNonTraite
													.add(0,
															consulterInformations);
											listCourriersTousBocArriveNonTraite
													.add(0,
															consulterInformations);
											listCourriersFaxBocNonTraite.add(0,
													consulterInformations);
											listCourriersTousBocNonTraite.add(
													0, consulterInformations);
										}
										listCourriersFaxBocArrive.add(0,
												consulterInformations);
										listCourriersTousBocArrive.add(0,
												consulterInformations);
									}
									listCourriersFaxBoc.add(0,
											consulterInformations);
								} else {
									if (transaction.getExpdest()
											.getExpdestexterne() == null) {
										if (transaction.getEtat()
												.getEtatLibelle()
												.equals("Traité")) {
											consulterInformations
													.setCourrierAValider(0);
											listCourriersAutreBocDepartTraite
													.add(0,
															consulterInformations);
											listCourriersTousBocDepartTraite
													.add(0,
															consulterInformations);
											listCourriersAutreBocTraite.add(0,
													consulterInformations);
											listCourriersTousBocTraite.add(0,
													consulterInformations);
										} else {
											consulterInformations
													.setCourrierAValider(1);
											listCourriersAutreBocDepartNonTraite
													.add(0,
															consulterInformations);
											listCourriersTousBocDepartNonTraite
													.add(0,
															consulterInformations);
											listCourriersAutreBocNonTraite.add(
													0, consulterInformations);
											listCourriersTousBocNonTraite.add(
													0, consulterInformations);
										}
										listCourriersAutreBocDepart.add(0,
												consulterInformations);
										listCourriersTousBocDepart.add(0,
												consulterInformations);
									} else {
										if (transaction.getEtat()
												.getEtatLibelle()
												.equals("Traité")) {
											consulterInformations
													.setCourrierAValider(0);
											listCourriersAutreBocArriveTraite
													.add(0,
															consulterInformations);
											listCourriersTousBocArriveTraite
													.add(0,
															consulterInformations);
											listCourriersAutreBocTraite.add(0,
													consulterInformations);
											listCourriersTousBocTraite.add(0,
													consulterInformations);
										} else {
											consulterInformations
													.setCourrierAValider(0);
											listCourriersAutreBocArriveNonTraite
													.add(0,
															consulterInformations);
											listCourriersTousBocArriveNonTraite
													.add(0,
															consulterInformations);
											listCourriersAutreBocNonTraite.add(
													0, consulterInformations);
											listCourriersTousBocNonTraite.add(
													0, consulterInformations);
										}
										listCourriersAutreBocArrive.add(0,
												consulterInformations);
										listCourriersTousBocArrive.add(0,
												consulterInformations);
									}
									listCourriersAutreBoc.add(0,
											consulterInformations);
								}
								listCourriersTousBoc.add(0,
										consulterInformations);
							}
						}
					} else {
						if (vb.isConnected()) {
							if (consulterInformations.isForValidating()) {
								listCourriersDeValidationJour
										.add(consulterInformations);
								if (consulterInformations.isTreated()) {
									listCourriersTraiteJour
											.add(consulterInformations);
								} else if (consulterInformations.isValidated()) {
									listCourriersValideJour
											.add(consulterInformations);
								} else if (consulterInformations
										.isUnValidated()) {
									listCourriersNonValideJour
											.add(consulterInformations);
								} else if (consulterInformations
										.isInValidating()) {
									listCourriersAValiderJour
											.add(consulterInformations);
								}
							}
							listCourriersEnvoyesJour.add(consulterInformations);
							listcourrierConsulterInformationsJour
									.add(consulterInformations);
						} else {
							if (consulterInformations
									.getCourrierDateReceptionEnvoi().getDate() == dateJour
									.getDate()
									&& consulterInformations
											.getCourrierDateReceptionEnvoi()
											.getMonth() == dateJour.getMonth()
									&& consulterInformations
											.getCourrierDateReceptionEnvoi()
											.getYear() == dateJour.getYear()) {
								if (consulterInformations.isForValidating()) {
									listCourriersDeValidationJour.add(0,
											consulterInformations);
									if (consulterInformations.isTreated()) {
										listCourriersTraiteJour.add(0,
												consulterInformations);
									} else if (consulterInformations
											.isValidated()) {
										listCourriersValideJour.add(0,
												consulterInformations);
									} else if (consulterInformations
											.isUnValidated()) {
										listCourriersNonValideJour.add(0,
												consulterInformations);
									} else if (consulterInformations
											.isInValidating()) {
										listCourriersAValiderJour.add(0,
												consulterInformations);
									}
								}
								listCourriersEnvoyesJour
										.add(consulterInformations);
								listcourrierConsulterInformationsJour
										.add(consulterInformations);
							} else {
								if (consulterInformations.isForValidating()) {
									listCourriersDeValidation
											.add(consulterInformations);
									if (consulterInformations.isTreated()) {
										listCourriersTraite
												.add(consulterInformations);
									} else if (consulterInformations
											.isValidated()) {
										listCourriersValide
												.add(consulterInformations);
									} else if (consulterInformations
											.isUnValidated()) {
										listCourriersNonValide
												.add(consulterInformations);
									} else if (consulterInformations
											.isInValidating()) {
										listCourriersAValider
												.add(consulterInformations);
									}
								}
								listCourriersEnvoyes.add(consulterInformations);
								courrierConsulterInformations
										.add(consulterInformations);
							}
						}
					}
				} else if (typeDossier.getTypeDossierLibelle().equals(
						"Personnalise")) {
					consulterInformations.setTransaction(transaction);
					consulterInformations.setDossier(dossier);
					consulterInformations.setCourrierCommentaire(dossier
							.getDossierDescription());
					consulterInformations.setCourrierObjet(dossier
							.getDossierIntitule());
					consulterInformations.setCourrierReference(dossier
							.getDossierReference());
					consulterInformations
							.setCourrierDateReceptionEnvoi(transaction
									.getTransactionDateTransaction());
					consulterInformations.setTypeCourrier(getCategorieCourrier(
							transaction, false));
					if (vb.isConnected()) {
						listDossiersEnvoyesJour.add(consulterInformations);
						listdossierConsulterInformationsJour
								.add(consulterInformations);
					} else {
						if (consulterInformations
								.getCourrierDateReceptionEnvoi().getDate() == dateJour
								.getDate()
								&& consulterInformations
										.getCourrierDateReceptionEnvoi()
										.getMonth() == dateJour.getMonth()
								&& consulterInformations
										.getCourrierDateReceptionEnvoi()
										.getYear() == dateJour.getYear()) {
							listDossiersEnvoyesJour.add(consulterInformations);
							listdossierConsulterInformationsJour
									.add(consulterInformations);
						} else {
							listDossiersEnvoyes.add(consulterInformations);
							dossierConsulterInformations
									.add(consulterInformations);
						}
					}
				}

			}
		}

	}

	@SuppressWarnings("deprecation")
	public void setListCourriersRecus(
			List<TransactionDestination> listTransactions) {
		CourrierConsulterInformations consulterInformations;
		int refDossier = 0;
		CourrierDossier courrierDossier;
		Courrier courrier;
		Transaction transaction;
		Expdest expDest;
		List<Commentaire> listCommentaire = new ArrayList<Commentaire>();
		Date dateJour = new Date();
		for (TransactionDestination transactionDestination : listTransactions) {
			consulterInformations = new CourrierConsulterInformations();
			transaction = new Transaction();
			transaction = appMgr.getListTransactionByIdTransaction(
					transactionDestination.getId().getIdTransaction()).get(0);
			consulterInformations
					.setTransactionDestination(transactionDestination);
			expDest = new Expdest();
			expDest = appMgr.getListExpDestByIdExpDest(
					transaction.getExpdest().getIdExpDest()).get(0);
			if (expDest.getTypeExpDest().equals("Interne-Person")) {
				Person person = new Person();
				person = ldapOperation.getUserById(expDest.getIdExpDestLdap());
				consulterInformations.setCourrierExpediteur(person.getCn());
				consulterInformations.setCourrierExpediteurObjet(person);
			} else if (expDest.getTypeExpDest().equals("Interne-Unité")) {
				Unit unit = new Unit();
				unit = ldapOperation.getUnitById(expDest.getIdExpDestLdap());
				consulterInformations.setCourrierExpediteur(unit.getNameUnit());
				consulterInformations.setCourrierExpediteurObjet(unit);
			} else if (expDest.getTypeExpDest().equals("Interne-Boc")) {
				Unit unit = new Unit();
				unit = ldapOperation.getBocById(expDest.getIdExpDestLdap());
				consulterInformations.setCourrierExpediteur(unit.getNameUnit());
			} else if (expDest.getTypeExpDest().equals("Externe")) {
				if (expDest.getExpdestexterne().getTypeutilisateur()
						.getTypeUtilisateurLibelle().equals("PP")) {
					consulterInformations.setCourrierExpediteur(expDest
							.getExpdestexterne().getExpDestExterneNom()
							+ " "
							+ expDest.getExpdestexterne()
									.getExpDestExternePrenom() + " (PP)");
					consulterInformations.setCourrierExpediteurObjet(expDest
							.getExpdestexterne());
				} else {
					consulterInformations.setCourrierExpediteur(expDest
							.getExpdestexterne().getExpDestExterneNom()
							+ " (PM)");
					consulterInformations.setCourrierExpediteurObjet(expDest
							.getExpdestexterne());
				}
			}
			if (transaction.getTransactionDestinationReelle() != null) {
				consulterInformations.setForValidating(true);
				Etat etat = new Etat();
				etat = appMgr.listEtatByRef(transaction.getEtat().getEtatId())
						.get(0);
				if (transaction.getTransactionDestinationReelle()
						.getTransactionDestinationReelleDateTraitement() != null) {
					consulterInformations.setTreated(true);
				} else {
					if (etat.getEtatLibelle().equals("A valider")) {
						consulterInformations.setInValidating(true);
					} else if (etat.getEtatLibelle().equals("Validé")) {
						consulterInformations.setValidated(true);
					} else if (etat.getEtatLibelle().equals("Non validé")) {
						consulterInformations.setUnValidated(true);
					} else {
						consulterInformations.setUntreated(true);
					}
				}
				TransactionDestinationReelle transactionDestinationReelle = new TransactionDestinationReelle();
				Expdestexterne expDestExterne;
				transactionDestinationReelle = transaction
						.getTransactionDestinationReelle();
				if (transactionDestinationReelle
						.getTransactionDestinationReelleTypeDestinataire()
						.equals("Externe")) {
					etat = new Etat();
					etat = appMgr.listEtatByRef(
							transaction.getEtat().getEtatId()).get(0);
					if (etat.getEtatLibelle().equals("Simple")) {
						expDest = new Expdest();
						expDest = appMgr.getListExpDestByIdExpDest(
								transactionDestination.getId().getIdExpDest())
								.get(0);
						if (expDest.getTypeExpDest().equals("Interne-Person")) {
							consulterInformations
									.setCourrierDestinataireReelle(ldapOperation
											.getCnById(
													ldapOperation.CONTEXT_USER,
													"uid",
													expDest.getIdExpDestLdap()));
						} else if (expDest.getTypeExpDest().equals(
								"Interne-Unité")) {
							consulterInformations
									.setCourrierDestinataireReelle(ldapOperation
											.getCnById(
													ldapOperation.CONTEXT_UNIT,
													"departmentNumber",
													expDest.getIdExpDestLdap()));
						} else if (expDest.getTypeExpDest().equals(
								"Interne-Boc")) {
							consulterInformations
									.setCourrierDestinataireReelle(ldapOperation
											.getCnById(
													ldapOperation.CONTEXT_BOC,
													"departmentNumber",
													expDest.getIdExpDestLdap()));
						}
					} else {
						expDestExterne = new Expdestexterne();
						expDestExterne = appMgr
								.getExpediteurById(
										transactionDestinationReelle
												.getTransactionDestinationReelleIdDestinataire())
								.get(0);
						if (expDestExterne.getTypeutilisateur()
								.getTypeUtilisateurLibelle().equals("PP")) {
							consulterInformations
									.setCourrierDestinataireReelle(expDestExterne
											.getExpDestExterneNom()
											+ " "
											+ expDestExterne
													.getExpDestExternePrenom()
											+ " (PP)");
							consulterInformations
									.setCourrierDestinataireObject(expDestExterne);
						} else {
							consulterInformations
									.setCourrierDestinataireReelle(expDestExterne
											.getExpDestExterneNom() + " (PM)");
							consulterInformations
									.setCourrierDestinataireObject(expDestExterne);
						}
					}
				}
			} else {
				expDest = new Expdest();
				expDest = appMgr.getListExpDestByIdExpDest(
						transactionDestination.getId().getIdExpDest()).get(0);
				if (expDest.getTypeExpDest().equals("Interne-Person")) {
					consulterInformations
							.setCourrierDestinataireReelle(ldapOperation
									.getCnById(ldapOperation.CONTEXT_USER,
											"uid", expDest.getIdExpDestLdap()));
				} else if (expDest.getTypeExpDest().equals("Interne-Unité")) {
					consulterInformations
							.setCourrierDestinataireReelle(ldapOperation
									.getCnById(ldapOperation.CONTEXT_UNIT,
											"departmentNumber",
											expDest.getIdExpDestLdap()));
				} else if (expDest.getTypeExpDest().equals("Interne-Boc")) {
					consulterInformations
							.setCourrierDestinataireReelle(ldapOperation
									.getCnById(ldapOperation.CONTEXT_BOC,
											"departmentNumber",
											expDest.getIdExpDestLdap()));
				}
			}
			if ((transaction.getTransactionDestinationReelle() != null
					&& (transaction.getEtat().getEtatLibelle()
							.equals("A valider") || transaction.getEtat()
							.getEtatLibelle().equals("Non traité")) || transaction
					.getEtat().getEtatLibelle().equals("Faire suivre"))) {
				consulterInformations.setCourrierAValider(1);
			} else {
				consulterInformations.setCourrierAValider(0);
			}
			Dossier dossier = new Dossier();
			dossier = transaction.getDossier();
			Typedossier typeDossier = new Typedossier();
			typeDossier = appMgr.getTypeDossierById(
					dossier.getTypedossier().getTypeDossierId()).get(0);
			if (typeDossier.getTypeDossierLibelle().equals("Default")) {
				courrierDossier = new CourrierDossier();
				refDossier = dossier.getDossierId();
				courrier = new Courrier();
				courrierDossier = appMgr.getCourrierDossierByIdDossier(
						refDossier).get(0);
				courrier = appMgr.getCourrierByIdCourrier(
						courrierDossier.getId().getIdCourrier()).get(0);
				consulterInformations.setTransaction(transaction);
				consulterInformations.setCourrier(courrier);
				consulterInformations.setCourrierCommentaire(courrier
						.getCourrierCommentaire());
				consulterInformations.setCourrierObjet(courrier
						.getCourrierObjet());
				consulterInformations.setCourrierReference(courrier
						.getCourrierReferenceCorrespondant());
				consulterInformations.setCourrierNature(courrier.getNature()
						.getNatureLibelle());
				consulterInformations.setCourrierDateReceptionEnvoi(transaction
						.getTransactionDateTransaction());
				consulterInformations.setTypeCourrier(getCategorieCourrier(
						transactionDestination, true));
				String type = "";
				if (vb.getPerson().isResponsable()) {
					type = "sub_" + String.valueOf(vb.getPerson().getId());
				} else if (vb.getPerson().isSecretary()) {
					type = "secretary_"
							+ String.valueOf(vb.getPerson().getId());
				} else if (vb.getPerson().isAgent()) {
					type = "agent_" + String.valueOf(vb.getPerson().getId());
				}
				if (vb.getPerson().isResponsable()
						&& transaction.getTransactionDestinationReelle() == null
						&& !transactionDestination
								.getTransactionDestTypeIntervenant().equals(
										type)) {
					if (transaction.getTransactionDateConsultation() != null) {
						consulterInformations.setOpened(true);
					} else {
						consulterInformations.setNotOpened(true);
					}
				} else {
					if (transactionDestination
							.getTransactionDestDateConsultation() != null) {
						consulterInformations.setOpened(true);
					} else {
						consulterInformations.setNotOpened(true);
					}
				}
				if ((consulterInformations.getTypeCourrier().equals(
						"A. Mes Propres Courriers") || consulterInformations
						.getTypeCourrier().equals(
								"B. Les Courriers de Mon Unité"))
						&& transactionDestination
								.getTransactionDestDateTransfert() == null) {
					consulterInformations.setCourrierRecu(1);
				} else {
					consulterInformations.setCourrierRecu(0);
				}
				listCommentaire = appMgr.getListPartageByIdTransaction(
						transaction.getTransactionId(), dossier.getDossierId());
				if (!listCommentaire.isEmpty()) {
					consulterInformations.setNotShared(false);
					consulterInformations.setShared(true);
				}

				if (vb.getPerson().isBoc()) {
					Transmission transmission = new Transmission();
					transmission = courrier.getTransmission();
					consulterInformations.setCourrierAValider(1);

					if (vb.isConnected()) {
						if (transmission.getTransmissionLibelle().equals(
								"e-Mail")) {
							listCourriersMailBocDepartNonTraiteJour
									.add(consulterInformations);
							listCourriersMailBocNonTraiteJour
									.add(consulterInformations);
							listCourriersMailBocDepartJour
									.add(consulterInformations);
							listCourriersMailBocJour.add(consulterInformations);
						} else if (transmission.getTransmissionLibelle()
								.equals("Fax")) {
							listCourriersFaxBocDepartNonTraiteJour
									.add(consulterInformations);
							listCourriersFaxBocNonTraiteJour
									.add(consulterInformations);
							listCourriersFaxBocDepartJour
									.add(consulterInformations);
							listCourriersFaxBocJour.add(consulterInformations);
						} else {
							listCourriersAutreBocDepartNonTraiteJour
									.add(consulterInformations);
							listCourriersAutreBocNonTraiteJour
									.add(consulterInformations);
							listCourriersAutreBocDepartJour
									.add(consulterInformations);
							listCourriersAutreBocJour
									.add(consulterInformations);
						}
						listCourriersTousBocJour.add(consulterInformations);
						listCourriersTousBocNonTraiteJour
								.add(consulterInformations);
						listCourriersTousBocDepartJour
								.add(consulterInformations);
						listCourriersTousBocDepartNonTraiteJour
								.add(consulterInformations);
					} else {
						if (consulterInformations
								.getCourrierDateReceptionEnvoi().getDate() == dateJour
								.getDate()
								&& consulterInformations
										.getCourrierDateReceptionEnvoi()
										.getMonth() == dateJour.getMonth()
								&& consulterInformations
										.getCourrierDateReceptionEnvoi()
										.getYear() == dateJour.getYear()) {
							if (transmission.getTransmissionLibelle().equals(
									"e-Mail")) {
								listCourriersMailBocDepartNonTraiteJour
										.add(consulterInformations);
								listCourriersMailBocNonTraiteJour
										.add(consulterInformations);
								listCourriersMailBocDepartJour
										.add(consulterInformations);
								listCourriersMailBocJour
										.add(consulterInformations);
							} else if (transmission.getTransmissionLibelle()
									.equals("Fax")) {
								listCourriersFaxBocDepartNonTraiteJour
										.add(consulterInformations);
								listCourriersFaxBocNonTraiteJour
										.add(consulterInformations);
								listCourriersFaxBocDepartJour
										.add(consulterInformations);
								listCourriersFaxBocJour
										.add(consulterInformations);
							} else {
								listCourriersAutreBocDepartNonTraiteJour
										.add(consulterInformations);
								listCourriersAutreBocNonTraiteJour
										.add(consulterInformations);
								listCourriersAutreBocDepartJour
										.add(consulterInformations);
								listCourriersAutreBocJour
										.add(consulterInformations);
							}
							listCourriersTousBocJour.add(consulterInformations);
							listCourriersTousBocNonTraiteJour
									.add(consulterInformations);
							listCourriersTousBocDepartJour
									.add(consulterInformations);
							listCourriersTousBocDepartNonTraiteJour
									.add(consulterInformations);
						} else {
							if (transmission.getTransmissionLibelle().equals(
									"e-Mail")) {
								listCourriersMailBocDepartNonTraite
										.add(consulterInformations);
								listCourriersMailBocNonTraite
										.add(consulterInformations);
								listCourriersMailBocDepart
										.add(consulterInformations);
								listCourriersMailBoc.add(consulterInformations);
							} else if (transmission.getTransmissionLibelle()
									.equals("Fax")) {
								listCourriersFaxBocDepartNonTraite
										.add(consulterInformations);
								listCourriersFaxBocNonTraite
										.add(consulterInformations);
								listCourriersFaxBocDepart
										.add(consulterInformations);
								listCourriersFaxBoc.add(consulterInformations);
							} else {
								listCourriersAutreBocDepartNonTraite
										.add(consulterInformations);
								listCourriersAutreBocNonTraite
										.add(consulterInformations);
								listCourriersAutreBocDepart
										.add(consulterInformations);
								listCourriersAutreBoc
										.add(consulterInformations);
							}
							listCourriersTousBoc.add(consulterInformations);
							listCourriersTousBocNonTraite
									.add(consulterInformations);
							listCourriersTousBocDepart
									.add(consulterInformations);
							listCourriersTousBocDepartNonTraite
									.add(consulterInformations);
						}
					}
				} else {
					if (vb.isConnected()) {
						if (consulterInformations.getCourrierAValider() == 1) {
							consulterInformations.setCourrierRecu(0);
							listCourriersRecusJourAvalider
									.add(consulterInformations);
						}
						if (consulterInformations.isForValidating()) {
							listCourriersDeValidationJour.add(0,
									consulterInformations);
							if (consulterInformations.isTreated()) {
								listCourriersTraiteJour.add(0,
										consulterInformations);
							} else if (consulterInformations.isValidated()) {
								listCourriersValideJour.add(0,
										consulterInformations);
							} else if (consulterInformations.isUnValidated()) {
								listCourriersNonValideJour.add(0,
										consulterInformations);
							} else if (consulterInformations.isInValidating()) {
								listCourriersAValiderJour.add(0,
										consulterInformations);
							}
						}
						listCourriersRecusJour.add(consulterInformations);
						listcourrierConsulterInformationsJour
								.add(consulterInformations);
					} else {
						if (consulterInformations
								.getCourrierDateReceptionEnvoi().getDate() == dateJour
								.getDate()
								&& consulterInformations
										.getCourrierDateReceptionEnvoi()
										.getMonth() == dateJour.getMonth()
								&& consulterInformations
										.getCourrierDateReceptionEnvoi()
										.getYear() == dateJour.getYear()) {
							if (consulterInformations.getCourrierAValider() == 1) {
								consulterInformations.setCourrierRecu(0);
								listCourriersRecusJourAvalider
										.add(consulterInformations);
							}
							if (consulterInformations.isForValidating()) {
								listCourriersDeValidationJour.add(0,
										consulterInformations);
								if (consulterInformations.isTreated()) {
									listCourriersTraiteJour.add(0,
											consulterInformations);
								} else if (consulterInformations.isValidated()) {
									listCourriersValideJour.add(0,
											consulterInformations);
								} else if (consulterInformations
										.isUnValidated()) {
									listCourriersNonValideJour.add(0,
											consulterInformations);
								} else if (consulterInformations
										.isInValidating()) {
									listCourriersAValiderJour.add(0,
											consulterInformations);
								}
							}
							listCourriersRecusJour.add(consulterInformations);
							listcourrierConsulterInformationsJour
									.add(consulterInformations);
						} else {
							if (consulterInformations.getCourrierAValider() == 1) {
								listCourriersRecusAvalider
										.add(consulterInformations);
							}
							if (consulterInformations.isForValidating()) {
								listCourriersDeValidation.add(0,
										consulterInformations);
								if (consulterInformations.isTreated()) {
									listCourriersTraite.add(0,
											consulterInformations);
								} else if (consulterInformations.isValidated()) {
									listCourriersValide.add(0,
											consulterInformations);
								} else if (consulterInformations
										.isUnValidated()) {
									listCourriersNonValide.add(0,
											consulterInformations);
								} else if (consulterInformations
										.isInValidating()) {
									listCourriersAValider.add(0,
											consulterInformations);
								}
							}
							listCourriersRecus.add(consulterInformations);
							courrierConsulterInformations
									.add(consulterInformations);
						}
					}
				}

			} else if (typeDossier.getTypeDossierLibelle().equals(
					"Personnalise")) {
				consulterInformations.setTransaction(transaction);
				consulterInformations.setDossier(dossier);
				consulterInformations.setCourrierCommentaire(dossier
						.getDossierDescription());
				consulterInformations.setCourrierObjet(dossier
						.getDossierIntitule());
				consulterInformations.setCourrierReference(dossier
						.getDossierReference());
				consulterInformations.setCourrierDateReceptionEnvoi(transaction
						.getTransactionDateTransaction());
				consulterInformations.setTypeCourrier(getCategorieCourrier(
						transactionDestination, false));
				if (vb.isConnected()) {
					listDossiersRecusJour.add(consulterInformations);
					listdossierConsulterInformationsJour
							.add(consulterInformations);
				} else {
					if (consulterInformations.getCourrierDateReceptionEnvoi()
							.getDate() == dateJour.getDate()
							&& consulterInformations
									.getCourrierDateReceptionEnvoi().getMonth() == dateJour
									.getMonth()
							&& consulterInformations
									.getCourrierDateReceptionEnvoi().getYear() == dateJour
									.getYear()) {
						listDossiersRecusJour.add(consulterInformations);
						listdossierConsulterInformationsJour
								.add(consulterInformations);
					} else {
						listDossiersRecus.add(consulterInformations);
						dossierConsulterInformations.add(consulterInformations);
					}
				}
			}
		}
	}

	private String getCategorieCourrier(Transaction transaction, boolean isMail) {
		String result = "";
		String[] type = new String[2];
		if (vb.getPerson().isResponsable()) {
			if (transaction.getTransactionTypeIntervenant().contains("sub")) {
				type = transaction.getTransactionTypeIntervenant().split("_");
				if (Integer.parseInt(type[1]) == vb.getPerson().getId()) {
					if (isMail) {
						result = "A. Mes Propres Courriers";
					} else {
						result = "A. Mes Propres Dossiers";
					}
				} else {
					if (variableConsultationCourrierSecretaire
							.getVaraiablesValeur().equals("Non")
							&& variableConsultationCourrierSubordonne
									.getVaraiablesValeur().equals("Oui")
							&& variableConsultationCourrierSousUnite
									.getVaraiablesValeur().equals("Non")) {
						if (isMail) {
							result = "D. Les Courriers de Mes Subordonnées";
						} else {
							result = "D. Les Dossiers de Mes Subordonnées";
						}
					} else if (variableConsultationCourrierSecretaire
							.getVaraiablesValeur().equals("Non")
							&& variableConsultationCourrierSubordonne
									.getVaraiablesValeur().equals("Oui")
							&& variableConsultationCourrierSousUnite
									.getVaraiablesValeur().equals("Oui")
							|| variableConsultationCourrierSecretaire
									.getVaraiablesValeur().equals("Oui")
							&& variableConsultationCourrierSubordonne
									.getVaraiablesValeur().equals("Oui")
							&& variableConsultationCourrierSousUnite
									.getVaraiablesValeur().equals("Non")) {
						if (isMail) {
							result = "E. Les Courriers de Mes Subordonnées";
						} else {
							result = "E. Les Dossiers de Mes Subordonnées";
						}
					} else {
						if (isMail) {
							result = "F. Les Courriers de Mes Subordonnées";
						} else {
							result = "F. Les Dossiers de Mes Subordonnées";
						}
					}
				}
			} else if (transaction.getTransactionTypeIntervenant().contains(
					"unit")) {
				type = transaction.getTransactionTypeIntervenant().split("_");
				if (Integer.parseInt(type[1]) == vb.getPerson()
						.getAssociatedDirection().getIdUnit()) {
					if (isMail) {
						result = "B. Les Courriers de Mon Unité";
					} else {
						result = "B. Les Dossiers de Mon Unité";
					}
				} else {
					if (variableConsultationCourrierSecretaire
							.getVaraiablesValeur().equals("Non")
							&& variableConsultationCourrierSubordonne
									.getVaraiablesValeur().equals("Non")
							&& variableConsultationCourrierSousUnite
									.getVaraiablesValeur().equals("Oui")) {
						if (isMail) {
							result = "C. Les Courriers de Mes Sous-Unités";
						} else {
							result = "C. Les Dossiers de Mes Sous-Unités";
						}
					} else if (variableConsultationCourrierSecretaire
							.getVaraiablesValeur().equals("Non")
							&& variableConsultationCourrierSubordonne
									.getVaraiablesValeur().equals("Oui")
							&& variableConsultationCourrierSousUnite
									.getVaraiablesValeur().equals("Oui")
							|| variableConsultationCourrierSecretaire
									.getVaraiablesValeur().equals("Oui")
							&& variableConsultationCourrierSubordonne
									.getVaraiablesValeur().equals("Non")
							&& variableConsultationCourrierSousUnite
									.getVaraiablesValeur().equals("Oui")) {
						if (isMail) {
							result = "D. Les Courriers de Mes Sous-Unités";
						} else {
							result = "D. Les Dossiers de Mes Sous-Unités";
						}
					} else {
						if (isMail) {
							result = "E. Les Courriers de Mes Sous-Unités";
						} else {
							result = "E. Les Dossiers de Mes Sous-Unités";
						}
					}

				}
			} else if (transaction.getTransactionTypeIntervenant().contains(
					"secretary")) {
				if (isMail) {
					result = "C. Les Courriers de Ma Secrétaire";
				} else {
					result = "C. Les Dossiers de Ma Secrétaire";
				}
			} else if (transaction.getTransactionTypeIntervenant().contains(
					"agent")) {
				if (variableConsultationCourrierSecretaire
						.getVaraiablesValeur().equals("Non")
						&& variableConsultationCourrierSubordonne
								.getVaraiablesValeur().equals("Oui")
						&& variableConsultationCourrierSousUnite
								.getVaraiablesValeur().equals("Non")
						|| variableConsultationCourrierSecretaire
								.getVaraiablesValeur().equals("Non")
						&& variableConsultationCourrierSubordonne
								.getVaraiablesValeur().equals("Oui")
						&& variableConsultationCourrierSousUnite
								.getVaraiablesValeur().equals("Oui")) {
					if (isMail) {
						result = "C. Les Courriers de Mes Agents";
					} else {
						result = "C. Les Dossiers de Mes Agents";
					}
				} else {
					if (isMail) {
						result = "D. Les Courriers de Mes Agents";
					} else {
						result = "D. Les Dossiers de Mes Agents";
					}
				}
			}
		} else if (vb.getPerson().isSecretary()) {
			if (transaction.getTransactionTypeIntervenant().contains(
					"secretary")) {
				if (isMail) {
					result = "A. Mes Propres Courriers";
				} else {
					result = "A. Mes Propres Dossiers";
				}
			} else {
				if (isMail) {
					result = "B. Les Courriers de Mon Unité";
				} else {
					result = "B. Les Dossiers de Mon Unité";
				}
			}
		} else if (vb.getPerson().isAgent()) {
			if (transaction.getTransactionTypeIntervenant().contains("agent")) {
				if (isMail) {
					result = "A. Mes Propres Courriers";
				} else {
					result = "A. Mes Propres Dossiers";
				}
			} else {
				if (isMail) {
					result = "B. Les Courriers de Mon Bureau d'Ordre";
				} else {
					result = "B. Les Dossiers de Mon Bureau d'Ordre";
				}
			}
		} else {
			result = "A. Les Courriers du Bureau d'Ordre";
		}

		return result;
	}

	private String getCategorieCourrier(
			TransactionDestination transactionDestination, boolean isMail) {
		String result = "";
		String[] type = new String[2];
		if (vb.getPerson().isResponsable()) {
			if (transactionDestination.getTransactionDestTypeIntervenant()
					.contains("sub")) {
				type = transactionDestination
						.getTransactionDestTypeIntervenant().split("_");
				if (Integer.parseInt(type[1]) == vb.getPerson().getId()) {
					if (isMail) {
						result = "A. Mes Propres Courriers";
					} else {
						result = "A. Mes Propres Dossiers";
					}
				} else {
					if (variableConsultationCourrierSecretaire
							.getVaraiablesValeur().equals("Non")
							&& variableConsultationCourrierSubordonne
									.getVaraiablesValeur().equals("Oui")
							&& variableConsultationCourrierSousUnite
									.getVaraiablesValeur().equals("Non")) {
						if (isMail) {
							result = "D. Les Courriers de Mes Subordonnées";
						} else {
							result = "D. Les Dossiers de Mes Subordonnées";
						}
					} else if (variableConsultationCourrierSecretaire
							.getVaraiablesValeur().equals("Non")
							&& variableConsultationCourrierSubordonne
									.getVaraiablesValeur().equals("Oui")
							&& variableConsultationCourrierSousUnite
									.getVaraiablesValeur().equals("Oui")
							|| variableConsultationCourrierSecretaire
									.getVaraiablesValeur().equals("Oui")
							&& variableConsultationCourrierSubordonne
									.getVaraiablesValeur().equals("Oui")
							&& variableConsultationCourrierSousUnite
									.getVaraiablesValeur().equals("Non")) {
						if (isMail) {
							result = "E. Les Courriers de Mes Subordonnées";
						} else {
							result = "E. Les Dossiers de Mes Subordonnées";
						}
					} else {
						if (isMail) {
							result = "F. Les Courriers de Mes Subordonnées";
						} else {
							result = "F. Les Dossiers de Mes Subordonnées";
						}
					}
				}
			} else if (transactionDestination
					.getTransactionDestTypeIntervenant().contains("unit")) {
				type = transactionDestination
						.getTransactionDestTypeIntervenant().split("_");
				if (Integer.parseInt(type[1]) == vb.getPerson()
						.getAssociatedDirection().getIdUnit()) {
					if (isMail) {
						result = "B. Les Courriers de Mon Unité";
					} else {
						result = "B. Les Dossiers de Mon Unité";
					}
				} else {
					if (variableConsultationCourrierSecretaire
							.getVaraiablesValeur().equals("Non")
							&& variableConsultationCourrierSubordonne
									.getVaraiablesValeur().equals("Non")
							&& variableConsultationCourrierSousUnite
									.getVaraiablesValeur().equals("Oui")) {
						if (isMail) {
							result = "C. Les Courriers de Mes Sous-Unités";
						} else {
							result = "C. Les Dossiers de Mes Sous-Unités";
						}
					} else if (variableConsultationCourrierSecretaire
							.getVaraiablesValeur().equals("Non")
							&& variableConsultationCourrierSubordonne
									.getVaraiablesValeur().equals("Oui")
							&& variableConsultationCourrierSousUnite
									.getVaraiablesValeur().equals("Oui")
							|| variableConsultationCourrierSecretaire
									.getVaraiablesValeur().equals("Oui")
							&& variableConsultationCourrierSubordonne
									.getVaraiablesValeur().equals("Non")
							&& variableConsultationCourrierSousUnite
									.getVaraiablesValeur().equals("Oui")) {
						if (isMail) {
							result = "D. Les Courriers de Mes Sous-Unités";
						} else {
							result = "D. Les Dossiers de Mes Sous-Unités";
						}
					} else {
						if (isMail) {
							result = "E. Les Courriers de Mes Sous-Unités";
						} else {
							result = "E. Les Dossiers de Mes Sous-Unités";
						}
					}

				}
			} else if (transactionDestination
					.getTransactionDestTypeIntervenant().contains("secretary")) {
				if (isMail) {
					result = "C. Les Courriers de Ma Secrétaire";
				} else {
					result = "C. Les Dossiers de Ma Secrétaire";
				}
			} else if (transactionDestination
					.getTransactionDestTypeIntervenant().contains("agent")) {
				if (variableConsultationCourrierSecretaire
						.getVaraiablesValeur().equals("Non")
						&& variableConsultationCourrierSubordonne
								.getVaraiablesValeur().equals("Oui")
						&& variableConsultationCourrierSousUnite
								.getVaraiablesValeur().equals("Non")
						|| variableConsultationCourrierSecretaire
								.getVaraiablesValeur().equals("Non")
						&& variableConsultationCourrierSubordonne
								.getVaraiablesValeur().equals("Oui")
						&& variableConsultationCourrierSousUnite
								.getVaraiablesValeur().equals("Oui")) {
					if (isMail) {
						result = "C. Les Courriers de Mes Agents";
					} else {
						result = "C. Les Dossiers de Mes Agents";
					}
				} else {
					if (isMail) {
						result = "D. Les Courriers de Mes Agents";
					} else {
						result = "D. Les Dossiers de Mes Agents";
					}
				}
			}
		} else if (vb.getPerson().isSecretary()) {
			if (transactionDestination.getTransactionDestTypeIntervenant()
					.contains("secretary")) {
				if (isMail) {
					result = "A. Mes Propres Courriers";
				} else {
					result = "A. Mes Propres Dossiers";
				}
			} else {
				if (isMail) {
					result = "B. Les Courriers de Mon Unité";
				} else {
					result = "B. Les Dossiers de Mon Unité";
				}
			}
		} else if (vb.getPerson().isAgent()) {
			if (transactionDestination.getTransactionDestTypeIntervenant()
					.contains("agent")) {
				if (isMail) {
					result = "A. Mes Propres Courriers";
				} else {
					result = "A. Mes Propres Dossiers";
				}
			} else {
				if (isMail) {
					result = "B. Les Courriers de Mon Bureau d'Ordre";
				} else {
					result = "B. Les Dossiers de Mon Bureau d'Ordre";
				}
			}
		} else {
			result = "A. Les Courriers du Bureau d'Ordre";
		}

		return result;
	}

	public void eventChooseTransmissionCourrierJour(ActionEvent actionEvent) {
		System.out
				.println("dans la fonction eventChooseTransmissionCourrierJour");
		if (transmissionCourrierJour.equals("Tout les courriers")
				&& categorieCourrierJour.equals("tous")
				&& typeCourrierTraitementJour.equals("tous")) {
			showExecuteAllButtonJour = false;
			hideExecuteAllButtonJour = true;
			listCourrierJour.setWrappedData(listCourriersTousBocJour);
		} else if (transmissionCourrierJour.equals("Tout les courriers")
				&& categorieCourrierJour.equals("tous")
				&& typeCourrierTraitementJour.equals("traite")) {
			showExecuteAllButtonJour = false;
			hideExecuteAllButtonJour = true;
			listCourrierJour.setWrappedData(listCourriersTousBocTraiteJour);
		} else if (transmissionCourrierJour.equals("Tout les courriers")
				&& categorieCourrierJour.equals("tous")
				&& typeCourrierTraitementJour.equals("nonTraite")) {
			if (!listCourriersTousBocNonTraiteJour.isEmpty()) {
				showExecuteAllButtonJour = true;
				hideExecuteAllButtonJour = false;
			}
			listCourrierJour.setWrappedData(listCourriersTousBocNonTraiteJour);
		} else if (transmissionCourrierJour.equals("Tout les courriers")
				&& categorieCourrierJour.equals("arrive")
				&& typeCourrierTraitementJour.equals("tous")) {
			showExecuteAllButtonJour = false;
			hideExecuteAllButtonJour = true;
			listCourrierJour.setWrappedData(listCourriersTousBocArriveJour);
		} else if (transmissionCourrierJour.equals("Tout les courriers")
				&& categorieCourrierJour.equals("arrive")
				&& typeCourrierTraitementJour.equals("traite")) {
			showExecuteAllButtonJour = false;
			hideExecuteAllButtonJour = true;
			listCourrierJour
					.setWrappedData(listCourriersTousBocArriveTraiteJour);
		} else if (transmissionCourrierJour.equals("Tout les courriers")
				&& categorieCourrierJour.equals("arrive")
				&& typeCourrierTraitementJour.equals("nonTraite")) {
			if (!listCourriersTousBocArriveNonTraiteJour.isEmpty()) {
				showExecuteAllButtonJour = true;
				hideExecuteAllButtonJour = false;
			}
			listCourrierJour
					.setWrappedData(listCourriersTousBocArriveNonTraiteJour);
		} else if (transmissionCourrierJour.equals("Tout les courriers")
				&& categorieCourrierJour.equals("depart")
				&& typeCourrierTraitementJour.equals("tous")) {
			showExecuteAllButtonJour = false;
			hideExecuteAllButtonJour = true;
			listCourrierJour.setWrappedData(listCourriersTousBocDepartJour);
		} else if (transmissionCourrierJour.equals("Tout les courriers")
				&& categorieCourrierJour.equals("depart")
				&& typeCourrierTraitementJour.equals("traite")) {
			showExecuteAllButtonJour = false;
			hideExecuteAllButtonJour = true;
			listCourrierJour
					.setWrappedData(listCourriersTousBocDepartTraiteJour);
		} else if (transmissionCourrierJour.equals("Tout les courriers")
				&& categorieCourrierJour.equals("depart")
				&& typeCourrierTraitementJour.equals("nonTraite")) {
			if (!listCourriersTousBocDepartNonTraiteJour.isEmpty()) {
				showExecuteAllButtonJour = true;
				hideExecuteAllButtonJour = false;
			}
			listCourrierJour
					.setWrappedData(listCourriersTousBocDepartNonTraiteJour);
		} else

		if (transmissionCourrierJour.equals("e-Mail")
				&& categorieCourrierJour.equals("tous")
				&& typeCourrierTraitementJour.equals("tous")) {
			showExecuteAllButtonJour = false;
			hideExecuteAllButtonJour = true;
			listCourrierJour.setWrappedData(listCourriersMailBocJour);
		} else if (transmissionCourrierJour.equals("e-Mail")
				&& categorieCourrierJour.equals("tous")
				&& typeCourrierTraitementJour.equals("traite")) {
			showExecuteAllButtonJour = false;
			hideExecuteAllButtonJour = true;
			listCourrierJour.setWrappedData(listCourriersMailBocTraiteJour);
		} else if (transmissionCourrierJour.equals("e-Mail")
				&& categorieCourrierJour.equals("tous")
				&& typeCourrierTraitementJour.equals("nonTraite")) {
			if (!listCourriersMailBocNonTraiteJour.isEmpty()) {
				showExecuteAllButtonJour = true;
				hideExecuteAllButtonJour = false;
			}
			listCourrierJour.setWrappedData(listCourriersMailBocNonTraiteJour);
		} else if (transmissionCourrierJour.equals("e-Mail")
				&& categorieCourrierJour.equals("arrive")
				&& typeCourrierTraitementJour.equals("tous")) {
			showExecuteAllButtonJour = false;
			hideExecuteAllButtonJour = true;
			listCourrierJour.setWrappedData(listCourriersMailBocArriveJour);
		} else if (transmissionCourrierJour.equals("e-Mail")
				&& categorieCourrierJour.equals("arrive")
				&& typeCourrierTraitementJour.equals("traite")) {
			showExecuteAllButtonJour = false;
			hideExecuteAllButtonJour = true;
			listCourrierJour
					.setWrappedData(listCourriersMailBocArriveTraiteJour);
		} else if (transmissionCourrierJour.equals("e-Mail")
				&& categorieCourrierJour.equals("arrive")
				&& typeCourrierTraitementJour.equals("nonTraite")) {
			if (!listCourriersMailBocArriveNonTraiteJour.isEmpty()) {
				showExecuteAllButtonJour = true;
				hideExecuteAllButtonJour = false;
			}
			listCourrierJour
					.setWrappedData(listCourriersMailBocArriveNonTraiteJour);
		} else if (transmissionCourrierJour.equals("e-Mail")
				&& categorieCourrierJour.equals("depart")
				&& typeCourrierTraitementJour.equals("tous")) {
			showExecuteAllButtonJour = false;
			hideExecuteAllButtonJour = true;
			listCourrierJour.setWrappedData(listCourriersMailBocDepartJour);
		} else if (transmissionCourrierJour.equals("e-Mail")
				&& categorieCourrierJour.equals("depart")
				&& typeCourrierTraitementJour.equals("traite")) {
			showExecuteAllButtonJour = false;
			hideExecuteAllButtonJour = true;
			listCourrierJour
					.setWrappedData(listCourriersMailBocDepartTraiteJour);
		} else if (transmissionCourrierJour.equals("e-Mail")
				&& categorieCourrierJour.equals("depart")
				&& typeCourrierTraitementJour.equals("nonTraite")) {
			if (!listCourriersMailBocDepartNonTraiteJour.isEmpty()) {
				showExecuteAllButtonJour = true;
				hideExecuteAllButtonJour = false;
			}
			listCourrierJour
					.setWrappedData(listCourriersMailBocDepartNonTraiteJour);
		} else

		if (transmissionCourrierJour.equals("Fax")
				&& categorieCourrierJour.equals("tous")
				&& typeCourrierTraitementJour.equals("tous")) {
			showExecuteAllButtonJour = false;
			hideExecuteAllButtonJour = true;
			listCourrierJour.setWrappedData(listCourriersFaxBocJour);
		} else if (transmissionCourrierJour.equals("Fax")
				&& categorieCourrierJour.equals("tous")
				&& typeCourrierTraitementJour.equals("traite")) {
			showExecuteAllButtonJour = false;
			hideExecuteAllButtonJour = true;
			listCourrierJour.setWrappedData(listCourriersFaxBocTraiteJour);
		} else if (transmissionCourrierJour.equals("Fax")
				&& categorieCourrierJour.equals("tous")
				&& typeCourrierTraitementJour.equals("nonTraite")) {
			if (!listCourriersFaxBocNonTraiteJour.isEmpty()) {
				showExecuteAllButtonJour = true;
				hideExecuteAllButtonJour = false;
			}
			listCourrierJour.setWrappedData(listCourriersFaxBocNonTraiteJour);
		} else if (transmissionCourrierJour.equals("Fax")
				&& categorieCourrierJour.equals("arrive")
				&& typeCourrierTraitementJour.equals("tous")) {
			showExecuteAllButtonJour = false;
			hideExecuteAllButtonJour = true;
			listCourrierJour.setWrappedData(listCourriersFaxBocArriveJour);
		} else if (transmissionCourrierJour.equals("Fax")
				&& categorieCourrierJour.equals("arrive")
				&& typeCourrierTraitementJour.equals("traite")) {
			showExecuteAllButtonJour = false;
			hideExecuteAllButtonJour = true;
			listCourrierJour
					.setWrappedData(listCourriersFaxBocArriveTraiteJour);
		} else if (transmissionCourrierJour.equals("Fax")
				&& categorieCourrierJour.equals("arrive")
				&& typeCourrierTraitementJour.equals("nonTraite")) {
			if (!listCourriersFaxBocArriveNonTraiteJour.isEmpty()) {
				showExecuteAllButtonJour = true;
				hideExecuteAllButtonJour = false;
			}
			listCourrierJour
					.setWrappedData(listCourriersFaxBocArriveNonTraiteJour);
		} else if (transmissionCourrierJour.equals("Fax")
				&& categorieCourrierJour.equals("depart")
				&& typeCourrierTraitementJour.equals("tous")) {
			showExecuteAllButtonJour = false;
			hideExecuteAllButtonJour = true;
			listCourrierJour.setWrappedData(listCourriersFaxBocDepartJour);
		} else if (transmissionCourrierJour.equals("Fax")
				&& categorieCourrierJour.equals("depart")
				&& typeCourrierTraitementJour.equals("traite")) {
			showExecuteAllButtonJour = false;
			hideExecuteAllButtonJour = true;
			listCourrierJour
					.setWrappedData(listCourriersFaxBocDepartTraiteJour);
		} else if (transmissionCourrierJour.equals("Fax")
				&& categorieCourrierJour.equals("depart")
				&& typeCourrierTraitementJour.equals("nonTraite")) {
			if (!listCourriersFaxBocDepartNonTraiteJour.isEmpty()) {
				showExecuteAllButtonJour = true;
				hideExecuteAllButtonJour = false;
			}
			listCourrierJour
					.setWrappedData(listCourriersFaxBocDepartNonTraiteJour);
		} else

		if (transmissionCourrierJour.equals("Autre")
				&& categorieCourrierJour.equals("tous")
				&& typeCourrierTraitementJour.equals("tous")) {
			showExecuteAllButtonJour = false;
			hideExecuteAllButtonJour = true;
			listCourrierJour.setWrappedData(listCourriersAutreBocJour);
		} else if (transmissionCourrierJour.equals("Autre")
				&& categorieCourrierJour.equals("tous")
				&& typeCourrierTraitementJour.equals("traite")) {
			showExecuteAllButtonJour = false;
			hideExecuteAllButtonJour = true;
			listCourrierJour.setWrappedData(listCourriersAutreBocTraiteJour);
		} else if (transmissionCourrierJour.equals("Autre")
				&& categorieCourrierJour.equals("tous")
				&& typeCourrierTraitementJour.equals("nonTraite")) {
			if (!listCourriersAutreBocNonTraiteJour.isEmpty()) {
				showExecuteAllButtonJour = true;
				hideExecuteAllButtonJour = false;
			}
			listCourrierJour.setWrappedData(listCourriersAutreBocNonTraiteJour);
		} else if (transmissionCourrierJour.equals("Autre")
				&& categorieCourrierJour.equals("arrive")
				&& typeCourrierTraitementJour.equals("tous")) {
			showExecuteAllButtonJour = false;
			hideExecuteAllButtonJour = true;
			listCourrierJour.setWrappedData(listCourriersAutreBocArriveJour);
		} else if (transmissionCourrierJour.equals("Autre")
				&& categorieCourrierJour.equals("arrive")
				&& typeCourrierTraitementJour.equals("traite")) {
			showExecuteAllButtonJour = false;
			hideExecuteAllButtonJour = true;
			listCourrierJour
					.setWrappedData(listCourriersAutreBocArriveTraiteJour);
		} else if (transmissionCourrierJour.equals("Autre")
				&& categorieCourrierJour.equals("arrive")
				&& typeCourrierTraitementJour.equals("nonTraite")) {
			if (!listCourriersAutreBocArriveNonTraiteJour.isEmpty()) {
				showExecuteAllButtonJour = true;
				hideExecuteAllButtonJour = false;
			}
			listCourrierJour
					.setWrappedData(listCourriersAutreBocArriveNonTraiteJour);
		} else if (transmissionCourrierJour.equals("Autre")
				&& categorieCourrierJour.equals("depart")
				&& typeCourrierTraitementJour.equals("tous")) {
			showExecuteAllButtonJour = false;
			hideExecuteAllButtonJour = true;
			listCourrierJour.setWrappedData(listCourriersAutreBocDepartJour);
		} else if (transmissionCourrierJour.equals("Autre")
				&& categorieCourrierJour.equals("depart")
				&& typeCourrierTraitementJour.equals("traite")) {
			showExecuteAllButtonJour = false;
			hideExecuteAllButtonJour = true;
			listCourrierJour
					.setWrappedData(listCourriersAutreBocDepartTraiteJour);
		} else if (transmissionCourrierJour.equals("Autre")
				&& categorieCourrierJour.equals("depart")
				&& typeCourrierTraitementJour.equals("nonTraite")) {

			if (!listCourriersAutreBocDepartNonTraiteJour.isEmpty()) {
				showExecuteAllButtonJour = true;
				hideExecuteAllButtonJour = false;
			}
			listCourrierJour
					.setWrappedData(listCourriersAutreBocDepartNonTraiteJour);
		}

	}

	public void eventChooseTransmissionCourrier(ActionEvent actionEvent) {
		if (transmissionCourrier.equals("Tout les courriers")
				&& categorieCourrier.equals("tous")
				&& typeCourrierTraitement.equals("tous")) {
			showExecuteAllButton = false;
			hideExecuteAllButton = true;
			listCourrier.setWrappedData(listCourriersTousBoc);
		} else if (transmissionCourrier.equals("Tout les courriers")
				&& categorieCourrier.equals("tous")
				&& typeCourrierTraitement.equals("traite")) {
			showExecuteAllButton = false;
			hideExecuteAllButton = true;
			listCourrier.setWrappedData(listCourriersTousBocTraite);
		} else if (transmissionCourrier.equals("Tout les courriers")
				&& categorieCourrier.equals("tous")
				&& typeCourrierTraitement.equals("nonTraite")) {
			if (!listCourriersTousBocNonTraite.isEmpty()) {
				showExecuteAllButton = true;
				hideExecuteAllButton = false;
			}
			listCourrier.setWrappedData(listCourriersTousBocNonTraite);
		} else if (transmissionCourrier.equals("Tout les courriers")
				&& categorieCourrier.equals("arrive")
				&& typeCourrierTraitement.equals("tous")) {
			showExecuteAllButton = false;
			hideExecuteAllButton = true;
			listCourrier.setWrappedData(listCourriersTousBocArrive);
		} else if (transmissionCourrier.equals("Tout les courriers")
				&& categorieCourrier.equals("arrive")
				&& typeCourrierTraitement.equals("traite")) {

			showExecuteAllButton = false;
			hideExecuteAllButton = true;
			listCourrier.setWrappedData(listCourriersTousBocArriveTraite);
		} else if (transmissionCourrier.equals("Tout les courriers")
				&& categorieCourrier.equals("arrive")
				&& typeCourrierTraitement.equals("nonTraite")) {

			if (!listCourriersTousBocArriveNonTraite.isEmpty()) {
				showExecuteAllButton = true;
				hideExecuteAllButton = false;
			}
			listCourrier.setWrappedData(listCourriersTousBocArriveNonTraite);
		} else if (transmissionCourrier.equals("Tout les courriers")
				&& categorieCourrier.equals("depart")
				&& typeCourrierTraitement.equals("tous")) {

			showExecuteAllButton = false;
			hideExecuteAllButton = true;
			listCourrier.setWrappedData(listCourriersTousBocDepart);
		} else if (transmissionCourrier.equals("Tout les courriers")
				&& categorieCourrier.equals("depart")
				&& typeCourrierTraitement.equals("traite")) {

			showExecuteAllButton = false;
			hideExecuteAllButton = true;
			listCourrier.setWrappedData(listCourriersTousBocDepartTraite);
		} else if (transmissionCourrier.equals("Tout les courriers")
				&& categorieCourrier.equals("depart")
				&& typeCourrierTraitement.equals("nonTraite")) {
			if (!listCourriersTousBocDepartNonTraite.isEmpty()) {
				showExecuteAllButton = true;
				hideExecuteAllButton = false;
			}
			listCourrier.setWrappedData(listCourriersTousBocDepartNonTraite);
		} else

		if (transmissionCourrier.equals("e-Mail")
				&& categorieCourrier.equals("tous")
				&& typeCourrierTraitement.equals("tous")) {

			showExecuteAllButton = false;
			hideExecuteAllButton = true;
			listCourrier.setWrappedData(listCourriersMailBoc);
		} else if (transmissionCourrier.equals("e-Mail")
				&& categorieCourrier.equals("tous")
				&& typeCourrierTraitement.equals("traite")) {

			showExecuteAllButton = false;
			hideExecuteAllButton = true;
			listCourrier.setWrappedData(listCourriersMailBocTraite);
		} else if (transmissionCourrier.equals("e-Mail")
				&& categorieCourrier.equals("tous")
				&& typeCourrierTraitement.equals("nonTraite")) {

			if (!listCourriersMailBocNonTraite.isEmpty()) {
				showExecuteAllButton = true;
				hideExecuteAllButton = false;
			}
			listCourrier.setWrappedData(listCourriersMailBocNonTraite);
		} else if (transmissionCourrier.equals("e-Mail")
				&& categorieCourrier.equals("arrive")
				&& typeCourrierTraitement.equals("tous")) {

			showExecuteAllButton = false;
			hideExecuteAllButton = true;
			listCourrier.setWrappedData(listCourriersMailBocArrive);
		} else if (transmissionCourrier.equals("e-Mail")
				&& categorieCourrier.equals("arrive")
				&& typeCourrierTraitement.equals("traite")) {

			showExecuteAllButton = false;
			hideExecuteAllButton = true;
			listCourrier.setWrappedData(listCourriersMailBocArriveTraite);
		} else if (transmissionCourrier.equals("e-Mail")
				&& categorieCourrier.equals("arrive")
				&& typeCourrierTraitement.equals("nonTraite")) {

			if (!listCourriersMailBocArriveNonTraite.isEmpty()) {
				showExecuteAllButton = true;
				hideExecuteAllButton = false;
			}
			listCourrier.setWrappedData(listCourriersMailBocArriveNonTraite);
		} else if (transmissionCourrier.equals("e-Mail")
				&& categorieCourrier.equals("depart")
				&& typeCourrierTraitement.equals("tous")) {

			showExecuteAllButton = false;
			hideExecuteAllButton = true;
			listCourrier.setWrappedData(listCourriersMailBocDepart);
		} else if (transmissionCourrier.equals("e-Mail")
				&& categorieCourrier.equals("depart")
				&& typeCourrierTraitement.equals("traite")) {

			showExecuteAllButton = false;
			hideExecuteAllButton = true;
			listCourrier.setWrappedData(listCourriersMailBocDepartTraite);
		} else if (transmissionCourrier.equals("e-Mail")
				&& categorieCourrier.equals("depart")
				&& typeCourrierTraitement.equals("nonTraite")) {
			if (!listCourriersMailBocDepartNonTraite.isEmpty()) {
				showExecuteAllButton = true;
				hideExecuteAllButton = false;
			}
			listCourrier.setWrappedData(listCourriersMailBocDepartNonTraite);
		} else

		if (transmissionCourrier.equals("Fax")
				&& categorieCourrier.equals("tous")
				&& typeCourrierTraitement.equals("tous")) {

			showExecuteAllButton = false;
			hideExecuteAllButton = true;
			listCourrier.setWrappedData(listCourriersFaxBoc);
		} else if (transmissionCourrier.equals("Fax")
				&& categorieCourrier.equals("tous")
				&& typeCourrierTraitement.equals("traite")) {

			showExecuteAllButton = false;
			hideExecuteAllButton = true;
			listCourrier.setWrappedData(listCourriersFaxBocTraite);
		} else if (transmissionCourrier.equals("Fax")
				&& categorieCourrier.equals("tous")
				&& typeCourrierTraitement.equals("nonTraite")) {
			if (!listCourriersFaxBocNonTraite.isEmpty()) {
				showExecuteAllButton = true;
				hideExecuteAllButton = false;
			}
			listCourrier.setWrappedData(listCourriersFaxBocNonTraite);
		} else if (transmissionCourrier.equals("Fax")
				&& categorieCourrier.equals("arrive")
				&& typeCourrierTraitement.equals("tous")) {

			showExecuteAllButton = false;
			hideExecuteAllButton = true;
			listCourrier.setWrappedData(listCourriersFaxBocArrive);
		} else if (transmissionCourrier.equals("Fax")
				&& categorieCourrier.equals("arrive")
				&& typeCourrierTraitement.equals("traite")) {

			showExecuteAllButton = false;
			hideExecuteAllButton = true;
			listCourrier.setWrappedData(listCourriersFaxBocArriveTraite);
		} else if (transmissionCourrier.equals("Fax")
				&& categorieCourrier.equals("arrive")
				&& typeCourrierTraitement.equals("nonTraite")) {

			if (!listCourriersFaxBocArriveNonTraite.isEmpty()) {
				showExecuteAllButton = true;
				hideExecuteAllButton = false;
			}
			listCourrier.setWrappedData(listCourriersFaxBocArriveNonTraite);
		} else if (transmissionCourrier.equals("Fax")
				&& categorieCourrier.equals("depart")
				&& typeCourrierTraitement.equals("tous")) {
			showExecuteAllButton = false;
			hideExecuteAllButton = true;
			listCourrier.setWrappedData(listCourriersFaxBocDepart);
		} else if (transmissionCourrier.equals("Fax")
				&& categorieCourrier.equals("depart")
				&& typeCourrierTraitement.equals("traite")) {

			showExecuteAllButton = false;
			hideExecuteAllButton = true;
			listCourrier.setWrappedData(listCourriersFaxBocDepartTraite);
		} else if (transmissionCourrier.equals("Fax")
				&& categorieCourrier.equals("depart")
				&& typeCourrierTraitement.equals("nonTraite")) {

			if (!listCourriersFaxBocDepartNonTraite.isEmpty()) {
				showExecuteAllButton = true;
				hideExecuteAllButton = false;
			}
			listCourrier.setWrappedData(listCourriersFaxBocDepartNonTraite);
		} else

		if (transmissionCourrier.equals("Autre")
				&& categorieCourrier.equals("tous")
				&& typeCourrierTraitement.equals("tous")) {

			showExecuteAllButton = false;
			hideExecuteAllButton = true;
			listCourrier.setWrappedData(listCourriersAutreBoc);
		} else if (transmissionCourrier.equals("Autre")
				&& categorieCourrier.equals("tous")
				&& typeCourrierTraitement.equals("traite")) {

			showExecuteAllButton = false;
			hideExecuteAllButton = true;
			listCourrier.setWrappedData(listCourriersAutreBocTraite);
		} else if (transmissionCourrier.equals("Autre")
				&& categorieCourrier.equals("tous")
				&& typeCourrierTraitement.equals("nonTraite")) {

			if (!listCourriersAutreBocNonTraite.isEmpty()) {
				showExecuteAllButton = true;
				hideExecuteAllButton = false;
			}
			listCourrier.setWrappedData(listCourriersAutreBocNonTraite);
		} else if (transmissionCourrier.equals("Autre")
				&& categorieCourrier.equals("arrive")
				&& typeCourrierTraitement.equals("tous")) {

			showExecuteAllButton = false;
			hideExecuteAllButton = true;
			listCourrier.setWrappedData(listCourriersAutreBocArrive);
		} else if (transmissionCourrier.equals("Autre")
				&& categorieCourrier.equals("arrive")
				&& typeCourrierTraitement.equals("traite")) {

			showExecuteAllButton = false;
			hideExecuteAllButton = true;
			listCourrier.setWrappedData(listCourriersAutreBocArriveTraite);
		} else if (transmissionCourrier.equals("Autre")
				&& categorieCourrier.equals("arrive")
				&& typeCourrierTraitement.equals("nonTraite")) {

			if (!listCourriersAutreBocArriveNonTraite.isEmpty()) {
				showExecuteAllButton = true;
				hideExecuteAllButton = false;
			}
			listCourrier.setWrappedData(listCourriersAutreBocArriveNonTraite);
		} else if (transmissionCourrier.equals("Autre")
				&& categorieCourrier.equals("depart")
				&& typeCourrierTraitement.equals("tous")) {

			showExecuteAllButton = false;
			hideExecuteAllButton = true;
			listCourrier.setWrappedData(listCourriersAutreBocDepart);
		} else if (transmissionCourrier.equals("Autre")
				&& categorieCourrier.equals("depart")
				&& typeCourrierTraitement.equals("traite")) {

			showExecuteAllButton = false;
			hideExecuteAllButton = true;
			listCourrier.setWrappedData(listCourriersAutreBocDepartTraite);
		} else if (transmissionCourrier.equals("Autre")
				&& categorieCourrier.equals("depart")
				&& typeCourrierTraitement.equals("nonTraite")) {

			if (!listCourriersAutreBocDepartNonTraite.isEmpty()) {
				showExecuteAllButton = true;
				hideExecuteAllButton = false;
			}
			listCourrier.setWrappedData(listCourriersAutreBocDepartNonTraite);
		}
	}

	public void reorderList(List<CourrierConsulterInformations> list,
			String type) {
		String result;
		CourrierConsulterInformations courrierConsInformations;
		List<CourrierConsulterInformations> listCourriersSubordonnees = new ArrayList<CourrierConsulterInformations>();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getTypeCourrier().equals(type)) {
				listCourriersSubordonnees.add(list.get(i));
			}
		}
		List<CourrierConsulterInformations> copyListCourriersSubordonnees = new ArrayList<CourrierConsulterInformations>();
		for (int i = 0; i < listCourriersSubordonnees.size(); i++) {
			result = "";
			courrierConsInformations = new CourrierConsulterInformations();
			courrierConsInformations = listCourriersSubordonnees.get(i);
			result = result
					+ courrierConsInformations.getCourrierDestinataireReelle()
					+ " / ";
			if (!copyListCourriersSubordonnees
					.contains(courrierConsInformations)) {

				for (int j = 0; j < listCourriersSubordonnees.size(); j++) {
					try {
						if (listCourriersSubordonnees
								.get(j)
								.getCourrierReference()
								.equals(courrierConsInformations
										.getCourrierReference())
								&& listCourriersSubordonnees
										.get(j)
										.getCourrierExpediteur()
										.equals(courrierConsInformations
												.getCourrierExpediteur())
								&& listCourriersSubordonnees
										.get(j)
										.getCourrierDateReceptionEnvoi()
										.toString()
										.equals(courrierConsInformations
												.getCourrierDateReceptionEnvoi()
												.toString())
								&& listCourriersSubordonnees.get(j) != courrierConsInformations) {
							result = result
									+ listCourriersSubordonnees.get(j)
											.getCourrierDestinataireReelle()
									+ " / ";
							copyListCourriersSubordonnees
									.add(listCourriersSubordonnees.get(j));
						}
					} catch (NullPointerException e) {
						e.printStackTrace();
					}

				}
				int lastIndex;
				int indexOfObject;
				if (!result.equals(courrierConsInformations
						.getCourrierDestinataireReelle() + " / ")) {
					lastIndex = result.lastIndexOf("/");
					result = result.substring(0, lastIndex);
					indexOfObject = list.indexOf(courrierConsInformations);
					courrierConsInformations
							.setCourrierDestinataireReelle(result);
					list.set(indexOfObject, courrierConsInformations);
				}
			}
		}
		if (!copyListCourriersSubordonnees.isEmpty()) {
			list.removeAll(copyListCourriersSubordonnees);
		}

	}

	public void execute() {
		Courrier courrier = new Courrier();
		consulterInformations = new CourrierConsulterInformations();
		consulterInformations = (CourrierConsulterInformations) listCourrier
				.getRowData();
		courrier = consulterInformations.getCourrier();
		Etat etat = new Etat();
		etat = appMgr.listEtatByRef(
				consulterInformations.getTransaction().getEtat().getEtatId())
				.get(0);
		if (etat.getEtatLibelle().equals("Faire suivre")) {
			String circuitCourrier = courrier.getCourrierCircuit();
			if (circuitCourrier.equals("workflow")) {
				int refNature = courrier.getNature().getNatureId();
				int etatActuelle = courrier.getCourrierEtatActuelleWorkflow();
				Workflow workflow = new Workflow();
				int nb = appMgr.listWorkflowByIdNature(refNature).size();
				if (nb != 0) {
					/********* WorkFlow Request ********/
					workflow = appMgr.listWorkflowByIdNature(refNature).get(0);
					String processId = workflow.getWorkflowTitre();
					int idLastNode = workflow.getWorkflowIdLastNode();
					if (etatActuelle == idLastNode) {
						validerFinProcessus(consulterInformations
								.getTransaction());
					} else {
						TraitementEtapeSuivant etapeSuivant = new TraitementEtapeSuivant();
						JBPMAccessProcessBean jbpmAccessProcessBean = new JBPMAccessProcessBean();
						etapeSuivant = jbpmAccessProcessBean
								.startProcessTraitementCourrier(processId,
										etatActuelle);
						validateWorkflow(etapeSuivant,
								consulterInformations.getTransaction(),
								consulterInformations
										.getTransactionDestination(), courrier);
					}

				}
			}
		} else {
			executeOneTransaction(consulterInformations);
			if (courrier.getTransmission().getTransmissionLibelle()
					.equals("e-Mail")) {
				listCourriersMailBocDepartNonTraite
						.remove(consulterInformations);
				listCourriersMailBocNonTraite.remove(consulterInformations);
				listCourriersMailBocDepart.remove(consulterInformations);
				listCourriersMailBoc.remove(consulterInformations);
			} else if (courrier.getTransmission().getTransmissionLibelle()
					.equals("Fax")) {
				listCourriersFaxBocDepartNonTraite
						.remove(consulterInformations);
				listCourriersFaxBocNonTraite.remove(consulterInformations);
				listCourriersFaxBocDepart.remove(consulterInformations);
				listCourriersFaxBoc.remove(consulterInformations);
			} else {
				listCourriersAutreBocDepartNonTraite
						.remove(consulterInformations);
				listCourriersAutreBocNonTraite.remove(consulterInformations);
				listCourriersAutreBocDepart.remove(consulterInformations);
				listCourriersAutreBoc.remove(consulterInformations);
			}
			listCourriersTousBoc.remove(consulterInformations);
			listCourriersTousBocNonTraite.remove(consulterInformations);
			listCourriersTousBocDepart.remove(consulterInformations);
			listCourriersTousBocDepartNonTraite.remove(consulterInformations);
		}

	}

	public void executeJour() {
		Courrier courrier = new Courrier();
		consulterInformations = new CourrierConsulterInformations();
		consulterInformations = (CourrierConsulterInformations) listCourrierJour
				.getRowData();
		courrier = consulterInformations.getCourrier();
		Etat etat = new Etat();
		etat = appMgr.listEtatByRef(
				consulterInformations.getTransaction().getEtat().getEtatId())
				.get(0);
		if (etat.getEtatLibelle().equals("Faire suivre")) {
			String circuitCourrier = courrier.getCourrierCircuit();
			if (circuitCourrier.equals("workflow")) {
				int refNature = courrier.getNature().getNatureId();
				int etatActuelle = courrier.getCourrierEtatActuelleWorkflow();
				Workflow workflow = new Workflow();
				int nb = appMgr.listWorkflowByIdNature(refNature).size();
				if (nb != 0) {
					/********* WorkFlow Request ********/
					workflow = appMgr.listWorkflowByIdNature(refNature).get(0);
					String processId = workflow.getWorkflowTitre();
					int idLastNode = workflow.getWorkflowIdLastNode();
					if (etatActuelle == idLastNode) {
						validerFinProcessus(consulterInformations
								.getTransaction());
					} else {
						TraitementEtapeSuivant etapeSuivant = new TraitementEtapeSuivant();
						JBPMAccessProcessBean jbpmAccessProcessBean = new JBPMAccessProcessBean();
						etapeSuivant = jbpmAccessProcessBean
								.startProcessTraitementCourrier(processId,
										etatActuelle);
						validateWorkflow(etapeSuivant,
								consulterInformations.getTransaction(),
								consulterInformations
										.getTransactionDestination(), courrier);
					}

				}
			}
		} else {
			executeOneTransaction(consulterInformations);
			if (courrier.getTransmission().getTransmissionLibelle()
					.equals("e-Mail")) {
				listCourriersMailBocDepartNonTraiteJour
						.remove(consulterInformations);
				listCourriersMailBocNonTraiteJour.remove(consulterInformations);
				listCourriersMailBocDepartJour.remove(consulterInformations);
				listCourriersMailBocJour.remove(consulterInformations);
			} else if (courrier.getTransmission().getTransmissionLibelle()
					.equals("Fax")) {
				listCourriersFaxBocDepartNonTraiteJour
						.remove(consulterInformations);
				listCourriersFaxBocNonTraiteJour.remove(consulterInformations);
				listCourriersFaxBocDepartJour.remove(consulterInformations);
				listCourriersFaxBocJour.remove(consulterInformations);
			} else {
				listCourriersAutreBocDepartNonTraiteJour
						.remove(consulterInformations);
				listCourriersAutreBocNonTraiteJour
						.remove(consulterInformations);
				listCourriersAutreBocDepartJour.remove(consulterInformations);
				listCourriersAutreBocJour.remove(consulterInformations);
			}
			listCourriersTousBocJour.remove(consulterInformations);
			listCourriersTousBocNonTraiteJour.remove(consulterInformations);
			listCourriersTousBocDepartJour.remove(consulterInformations);
			listCourriersTousBocDepartNonTraiteJour
					.remove(consulterInformations);
		}

	}

	public void validateWorkflow(TraitementEtapeSuivant etapeSuivant,
			Transaction transaction,
			TransactionDestination transactionDestination, Courrier courrier) {
		status1 = false;
		status2 = false;
		try {
			transactionDestination.setTransactionDestDateTransfert(new Date());
			appMgr.update(transactionDestination);
			newTransaction = new Transaction();
			Etat etat = new Etat();
			Expdest expdest = new Expdest();
			Typetransaction typetransaction = new Typetransaction();
			TransactionDestinationId id = new TransactionDestinationId();
			TransactionDestination trDest = new TransactionDestination();
			etat = appMgr.listEtatByLibelle("Validé").get(0);
			transaction.setEtat(etat);
			transaction.setTransactionDateReponse(new Date());
			appMgr.update(transaction);
			/**************************************************************/
			expdest.setTypeExpDest("Interne-Person");
			expdest.setIdExpDestLdap(vb.getPerson().getId());
			appMgr.insert(expdest);
			if (vb.getPerson().isResponsable()) {
				newTransaction.setTransactionTypeIntervenant("sub_"
						+ String.valueOf(vb.getPerson().getId()));
			} else if (vb.getPerson().isSecretary()) {
				newTransaction.setTransactionTypeIntervenant("secretary_"
						+ String.valueOf(vb.getPerson().getId()));
			}
			newTransaction.setExpdest(expdest);
			newTransaction.setIdUtilisateur(vb.getPerson().getId());
			newTransaction.setTransactionDateTransaction(new Date());
			typetransaction = appMgr.getTypeTransactionByLibelle("Envoi")
					.get(0);
			newTransaction.setTypetransaction(typetransaction);
			// Faire suivre
			etat = appMgr.listEtatByLibelle("Faire suivre").get(0);
			newTransaction.setEtat(etat);
			newTransaction.setTransactionSupprimer(true);
			int newOrderNumber = transaction.getTransactionOrdre();
			newOrderNumber++;
			newTransaction.setTransactionOrdre(newOrderNumber);
			newTransaction.setDossier(transaction.getDossier());
			appMgr.insert(newTransaction);

			/********** Workflow **********/

			long resultat = etapeSuivant.getIdNodeSuivante();
			int etatActuelle = (int) resultat;
			courrier.setCourrierEtatActuelleWorkflow(etatActuelle);
			appMgr.update(courrier);

			String result = "";
			Unit u = new Unit();
			String unite = etapeSuivant.getEtatSuivant();
			// Unite
			u = ldapOperation.getUnitByShortName(unite);

			/*
			 * Person pr = new Person(); // Person pr = u.getResponsibleUnit();
			 * // Envoi Responsable result = result + pr.getCn() + " / ";
			 * expdest = new Expdest(); id = new TransactionDestinationId();
			 * trDest = new TransactionDestination();
			 * expdest.setTypeExpDest("Interne-Person");
			 * expdest.setIdExpDestLdap(pr.getId()); appMgr.insert(expdest);
			 * id.setIdTransaction(newTransaction.getTransactionId());
			 * id.setIdExpDest(expdest.getIdExpDest()); trDest.setId(id);
			 * trDest.setTransactionDestTypeIntervenant("sub_" +
			 * String.valueOf(pr.getId()));
			 * trDest.setTransactionDestDateReponse(
			 * transactionDestination.getTransactionDestDateReponse());
			 * appMgr.insert(trDest);
			 */
			// Envoi Unité
			result = result + u.getNameUnit() + " / ";
			expdest = new Expdest();
			id = new TransactionDestinationId();
			trDest = new TransactionDestination();
			if (unite.equals("BOC")) {
				if (vb.getPerson().getAssociatedDirection() != null) {
					getIdBocByUnit(vb.getPerson().getAssociatedDirection());
				} else {
					idBoc = vb.getPerson().getAssociatedBOC().getIdBOC();
				}
				expdest.setTypeExpDest("Interne-Boc");
				expdest.setIdExpDestLdap(idBoc);
				appMgr.insert(expdest);
				id.setIdTransaction(newTransaction.getTransactionId());
				id.setIdExpDest(expdest.getIdExpDest());
				trDest.setId(id);
				trDest.setTransactionDestTypeIntervenant("boc_" + idBoc);
			} else {
				expdest.setTypeExpDest("Interne-Unité");
				expdest.setIdExpDestLdap(u.getIdUnit());
				appMgr.insert(expdest);
				id.setIdTransaction(newTransaction.getTransactionId());
				id.setIdExpDest(expdest.getIdExpDest());
				trDest.setId(id);
				trDest.setTransactionDestTypeIntervenant("unit_"
						+ String.valueOf(u.getIdUnit()));
			}
			appMgr.insert(trDest);

			TransactionAnnotationId cI = new TransactionAnnotationId();
			TransactionAnnotation cA = new TransactionAnnotation();
			List<TransactionAnnotation> transactionAnnotation = new ArrayList<TransactionAnnotation>();
			transactionAnnotation = appMgr
					.getAnnotationByIdTransaction(transaction
							.getTransactionId());
			for (TransactionAnnotation tr : transactionAnnotation) {
				cI.setIdAnnotation(tr.getId().getIdAnnotation());
				cI.setIdTransaction(newTransaction.getTransactionId());
				cA.setId(cI);
				appMgr.insert(cA);
				cA = new TransactionAnnotation();
				cI = new TransactionAnnotationId();
			}
			status1 = true;

		} catch (Exception e) {
			status2 = true;
			e.printStackTrace();
		}
	}

	private void getIdBocByUnit(Unit unit) {
		if (unit.getAssociatedUnit() != null) {
			getIdBocByUnit(unit.getAssociatedUnit());
		} else {
			idBoc = unit.getAssociatedBOC().getIdBOC();
		}

	}

	public void validerFinProcessus(Transaction transaction) {
		status1 = false;
		status2 = false;
		try {
			newTransaction = new Transaction();
			Etat etat = new Etat();
			etat = appMgr.listEtatByLibelle("Validé").get(0);
			transaction.setEtat(etat);
			transaction.setTransactionDateReponse(new Date());
			appMgr.update(transaction);
			status1 = true;
		} catch (Exception e) {
			status2 = true;
			e.printStackTrace();
		}

	}

	// public void refresh1(){
	// int index =
	// listcourrierConsulterInformationsJour.indexOf(consulterInformations);
	// consulterInformations.setCourrierAValider(0);
	// listcourrierConsulterInformationsJour.set(index, consulterInformations);
	// listCourrierJour.setWrappedData(listcourrierConsulterInformationsJour);
	// }
	//
	// public void refresh11(){
	// int index = courrierConsulterInformations.indexOf(consulterInformations);
	// consulterInformations.setCourrierAValider(0);
	// courrierConsulterInformations.set(index, consulterInformations);
	// listCourrier.setWrappedData(courrierConsulterInformations);
	// }

	private void executeOneTransaction(
			CourrierConsulterInformations consulterInformations) {
		Transaction transaction = new Transaction();
		transaction = consulterInformations.getTransaction();
		Transaction newTransaction;
		Transaction transactionExpediteur;
		List<Transaction> listTransaction = new ArrayList<Transaction>();
		try {
			if (transaction.getTransactionDestinationReelle() != null) {
				newTransaction = new Transaction();
				transactionExpediteur = new Transaction();

				transactionExpediteur = appMgr
						.getTransactionExpediteurByIdTransactionDestinationReelle(
								transaction.getTransactionDestinationReelle()
										.getTransactionDestinationReelleId(), 1)
						.get(0);

				listTransaction = appMgr
						.getTransactionByIdTransactionDestinationReelle(transaction
								.getTransactionDestinationReelle()
								.getTransactionDestinationReelleId());
				Etat etat = new Etat();
				Expdest expdest = new Expdest();
				Typetransaction typetransaction = new Typetransaction();
				TransactionDestinationId id = new TransactionDestinationId();
				TransactionDestination trDest = new TransactionDestination();
				etat = appMgr.listEtatByLibelle("Traité").get(0);
				newTransaction.setExpdest(transactionExpediteur.getExpdest());
				newTransaction.setIdUtilisateur(vb.getPerson().getId());
				newTransaction.setTransactionDateTransaction(new Date());
				typetransaction = appMgr.getTypeTransactionByLibelle("Envoi")
						.get(0);
				newTransaction.setTypetransaction(typetransaction);
				newTransaction.setEtat(etat);
				newTransaction.setTransactionSupprimer(true);
				int newOrderNumber = transaction.getTransactionOrdre();
				newOrderNumber++;
				newTransaction.setTransactionOrdre(newOrderNumber);
				newTransaction.setDossier(transaction.getDossier());
				newTransaction.setTransactionDestinationReelle(transaction
						.getTransactionDestinationReelle());
				appMgr.insert(newTransaction);

				expdest = new Expdest();
				Expdestexterne expDestExterne = new Expdestexterne();
				TransactionDestinationReelle transactionDestinationReelle = new TransactionDestinationReelle();
				transactionDestinationReelle = transaction
						.getTransactionDestinationReelle();
				expdest.setTypeExpDest("Externe");
				expDestExterne = appMgr
						.getExpediteurById(
								transactionDestinationReelle
										.getTransactionDestinationReelleIdDestinataire())
						.get(0);
				expdest.setExpdestexterne(expDestExterne);
				transactionDestinationReelle
						.setTransactionDestinationReelleDateTraitement(new Date());
				appMgr.update(transactionDestinationReelle);
				appMgr.insert(expdest);

				id = new TransactionDestinationId();
				trDest = new TransactionDestination();
				id.setIdTransaction(newTransaction.getTransactionId());
				id.setIdExpDest(expdest.getIdExpDest());
				trDest.setId(id);
				appMgr.insert(trDest);

				List<TransactionAnnotation> transactionAnnotation = new ArrayList<TransactionAnnotation>();
				transactionAnnotation = appMgr
						.getAnnotationByIdTransaction(transaction
								.getTransactionId());
				TransactionAnnotationId cI = new TransactionAnnotationId();
				TransactionAnnotation cA = new TransactionAnnotation();
				for (TransactionAnnotation tr : transactionAnnotation) {
					cI.setIdAnnotation(tr.getId().getIdAnnotation());
					cI.setIdTransaction(newTransaction.getTransactionId());
					cA.setId(cI);
					appMgr.insert(cA);
					cA = new TransactionAnnotation();
					cI = new TransactionAnnotationId();
				}

				for (Transaction transaction1 : listTransaction) {
					transaction1.setEtat(etat);
					appMgr.update(transaction1);
				}

				if (consulterInformations.getCourrier().getTransmission()
						.getTransmissionLibelle().equals("e-Mail")) {
					sendMail(consulterInformations);
				} else if (consulterInformations.getCourrier()
						.getTransmission().getTransmissionLibelle()
						.equals("Fax")) {
					sendFax(consulterInformations);
				}

				List<Transaction> list = new ArrayList<Transaction>();
				list.add(newTransaction);
				setListCourrierEnvoyes(list);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendMail(CourrierConsulterInformations consulterInformations) {
		System.out.println("Mail envoyé");
	}

	private void sendFax(CourrierConsulterInformations consulterInformations) {
		System.out.println("Fax envoyé");
	}

	public void executeAllTransaction() {

	}

	public void executeAllTransactionJour() {

	}

	public void loadCheckedChoicesJour() {
		List<CourrierConsulterInformations> result = new ArrayList<CourrierConsulterInformations>();
		if (allMailCheckedJour) {
			result = listCourriersDeValidationJour;
		} else {
			if (toValidateMailCheckedJour) {
				result.addAll(listCourriersAValiderJour);
			}
			if (validatedMailCheckedJour) {
				result.addAll(listCourriersValideJour);
			}
			if (notValidatedMailCheckedJour) {
				result.addAll(listCourriersNonValideJour);
			}
			if (treatedMailCheckedJour) {
				result.addAll(listCourriersTraiteJour);
			}
		}
		listCourrierJour.setWrappedData(result);
	}

	public void loadCheckedChoices() {
		List<CourrierConsulterInformations> result = new ArrayList<CourrierConsulterInformations>();
		if (allMailChecked) {
			result = listCourriersDeValidation;
		} else {
			if (toValidateMailChecked) {
				result.addAll(listCourriersAValider);
			}
			if (validatedMailChecked) {
				result.addAll(listCourriersValide);
			}
			if (notValidatedMailChecked) {
				result.addAll(listCourriersNonValide);
			}
			if (treatedMailChecked) {
				result.addAll(listCourriersTraite);
			}
		}
		listCourrier.setWrappedData(result);
	}

	public void unCheckAllMailChekbox1(ActionEvent evt) {
		allMailChecked = false;
	}

	public void unCheckAllMailChekbox2(ActionEvent evt) {
		allMailChecked = false;
	}

	public void unCheckAllMailChekbox3(ActionEvent evt) {
		allMailChecked = false;
	}

	public void unCheckAllMailChekbox4(ActionEvent evt) {
		allMailChecked = false;
	}

	public void unCheckAllMailChekbox1Jour(ActionEvent evt) {
		allMailCheckedJour = false;
	}

	public void unCheckAllMailChekbox2Jour(ActionEvent evt) {
		allMailCheckedJour = false;
	}

	public void unCheckAllMailChekbox3Jour(ActionEvent evt) {
		allMailCheckedJour = false;
	}

	public void unCheckAllMailChekbox4Jour(ActionEvent evt) {
		allMailCheckedJour = false;
	}

	public void checkUncheckOtherChekbox(ActionEvent evt) {
		if (allMailChecked) {
			toValidateMailChecked = true;
			validatedMailChecked = true;
			notValidatedMailChecked = true;
			treatedMailChecked = true;
		} else {
			toValidateMailChecked = false;
			validatedMailChecked = false;
			notValidatedMailChecked = false;
			treatedMailChecked = false;
		}
	}

	public void checkUncheckOtherChekboxJour(ActionEvent evt) {
		if (allMailCheckedJour) {
			toValidateMailCheckedJour = true;
			validatedMailCheckedJour = true;
			notValidatedMailCheckedJour = true;
			treatedMailCheckedJour = true;
		} else {
			toValidateMailCheckedJour = false;
			validatedMailCheckedJour = false;
			notValidatedMailCheckedJour = false;
			treatedMailCheckedJour = false;
		}
	}

	public void eventChooseTypeCourrierJour(ActionEvent evt) {
		typeCourrierValidationJour = "";
		moreChoicesJour = false;
		if (typeCourrierJour.equals("Tous")) {
			listCourrierJour
					.setWrappedData(listcourrierConsulterInformationsJour);
		} else if (typeCourrierJour.equals("Envoyes")) {
			listCourrierJour.setWrappedData(listCourriersEnvoyesJour);
		} else {
			listCourrierJour.setWrappedData(listCourriersRecusJour);
		}
	}

	public void eventChooseTypeCourrierValidationJour(ActionEvent evt) {
		typeCourrierJour = "";
		moreChoicesJour = true;
		allMailCheckedJour = true;
		toValidateMailCheckedJour = true;
		validatedMailCheckedJour = true;
		notValidatedMailCheckedJour = true;
		treatedMailCheckedJour = true;
		listCourrierJour.setWrappedData(listCourriersDeValidationJour);
	}

	public void eventChooseTypeCourrier(ActionEvent evt) {
		typeCourrierValidation = "";
		moreChoices = false;
		if (typeCourrier.equals("Tous")) {
			listCourrier.setWrappedData(courrierConsulterInformations);
		} else if (typeCourrier.equals("Envoyes")) {
			listCourrier.setWrappedData(listCourriersEnvoyes);
		} else {
			listCourrier.setWrappedData(listCourriersRecus);
		}
	}

	public void eventChooseTypeCourrierValidation(ActionEvent evt) {
		typeCourrier = "";
		moreChoices = true;
		allMailChecked = true;
		toValidateMailChecked = true;
		validatedMailChecked = true;
		notValidatedMailChecked = true;
		treatedMailChecked = true;
		listCourrier.setWrappedData(listCourriersDeValidation);
	}

	public void eventChooseTypeDossierJour(ActionEvent evt) {
		if (typeDossierJour.equals("Tous")) {
			listDossierJour
					.setWrappedData(listdossierConsulterInformationsJour);
		} else if (typeDossierJour.equals("Envoyes")) {
			listDossierJour.setWrappedData(listDossiersEnvoyesJour);
		} else {
			listDossierJour.setWrappedData(listDossiersRecusJour);
		}
	}

	public void eventChooseTypeDossier(ActionEvent evt) {
		if (typeDossier.equals("Tous")) {
			listDossier.setWrappedData(dossierConsulterInformations);
		} else if (typeDossier.equals("Envoyes")) {
			listDossier.setWrappedData(listDossiersEnvoyes);
		} else {
			listDossier.setWrappedData(listDossiersRecus);
		}
	}

	public void refresh() {
		if (vb.getPerson().isResponsable()) {
			getListTransactionForDirector(vb.getPerson().getId(), new Date());
			vb.setItemDisabledForAssociatedUnit(false);
		} else if (vb.getPerson().isSecretary()) {
			getListTransactionForSecretary(vb.getPerson().getId(), new Date());
			vb.setItemDisabledForAssociatedUnit(false);
		} else {
			getListTransactionForAgent(vb.getPerson().getId(), new Date());
			vb.setItemDisabledForAssociatedUnit(true);
		}
		vb.setCopyListCourriersTousJour(listcourrierConsulterInformationsJour);
		vb.setCopyListCourriersEnvoyesJour(listCourriersEnvoyesJour);
		vb.setCopyListCourriersRecusJour(listCourriersRecusJour);
		vb.setCopyListCourriersRecusAvaliderJour(listCourriersRecusJourAvalider);
		vb.setCopyListDossiersEnvoyesJour(listDossiersEnvoyesJour);
		vb.setCopyListDossiersRecusJour(listDossiersRecusJour);
		vb.setCopyListDossiersTousJour(listdossierConsulterInformationsJour);

		courrierConsulterInformations = vb.getCopyListCourriersTous();
		listCourriersEnvoyes = vb.getCopyListCourriersEnvoyes();
		listCourriersRecus = vb.getCopyListCourriersRecus();
		listCourriersRecusAvalider = vb.getCopyListCourriersRecusAvalider();
		listDossiersEnvoyes = vb.getCopyListDossiersEnvoyes();
		listDossiersRecus = vb.getCopyListDossiersRecus();
		dossierConsulterInformations = vb.getCopyListDossiersTous();
	}

	@SuppressWarnings("deprecation")
	public void getListTransactionForDirector(int idDirector, Date date) {

		List<Transaction> listTransactions = new ArrayList<Transaction>();
		listTransactions = getListTransactionsRecus(idDirector,
				"Interne-Person", date);

		setListCourriersRecus(listTransactions, "A. Mes Propres Courriers",
				"A. Mes Propres Dossiers", 1);
		listTransactions = new ArrayList<Transaction>();
		listTransactions = getListTransactionsRecus(vb.getPerson()
				.getAssociatedDirection().getIdUnit(), "Interne-Unité", date);

		setListCourriersRecus(listTransactions,
				"B. Les Courriers de Mon Unité",
				"B. Les Dossiers de Mon Unité", 1);
		listTransactions = new ArrayList<Transaction>();
		listTransactions = getListTransactionsRecus(vb.getPerson()
				.getAssociatedDirection().getSecretaryUnit().getId(),
				"Interne-Person", date);
		setListCourriersRecus(listTransactions,
				"C. Les Courriers de Ma Secrétaire",
				"C. Les Dossiers de Ma Secrétaire", 0);
		for (int i = 0; i < vb.getPerson().getAssociatedDirection()
				.getMembersUnit().size(); i++) {
			listTransactions = new ArrayList<Transaction>();
			listTransactions = getListTransactionsRecus(vb.getPerson()
					.getAssociatedDirection().getMembersUnit().get(i).getId(),
					"Interne-Person", date);
			setListCourriersRecus(listTransactions,
					"D. Les Courriers de Mes Agents",
					"D. Les Dossiers de Mes Agents", 0);
		}
		if (!vb.getPerson().getAssociatedDirection().getListUnitsChildUnit()
				.isEmpty()) {
			for (int i = 0; i < vb.getPerson().getAssociatedDirection()
					.getListUnitsChildUnit().size(); i++) {
				listTransactions = new ArrayList<Transaction>();
				listTransactions = getListTransactionsRecus(vb.getPerson()
						.getAssociatedDirection().getListUnitsChildUnit()
						.get(i).getIdUnit(), "Interne-Unité", date);

				setListCourriersRecus(listTransactions,
						"E. Les Courriers de Mes Sous-Unités",
						"E. Les Dossiers de Mes Sous-Unités", 0);

				listTransactions = new ArrayList<Transaction>();
				listTransactions = getListTransactionsRecus(vb.getPerson()
						.getAssociatedDirection().getListUnitsChildUnit()
						.get(i).getResponsibleUnit().getId(), "Interne-Person",
						date);

				setListCourriersRecus(listTransactions,
						"F. Les Courriers de Mes Subordonnées",
						"F. Les Dossiers de Mes Subordonnées", 0);
			}
		}

		List<CourrierConsulterInformations> listCourriersSubordonnees = new ArrayList<CourrierConsulterInformations>();
		for (int i = 0; i < listCourriersRecus.size(); i++) {
			if (listCourriersRecus.get(i).getTypeCourrier()
					.equals("F. Les Courriers de Mes Subordonnées")) {
				listCourriersSubordonnees.add(listCourriersRecus.get(i));
			}
		}
		List<CourrierConsulterInformations> copyListCourriersSubordonnees = new ArrayList<CourrierConsulterInformations>();
		CourrierConsulterInformations courrierConsInformations;
		String result;
		for (int i = 0; i < listCourriersSubordonnees.size(); i++) {
			result = "";
			courrierConsInformations = new CourrierConsulterInformations();
			courrierConsInformations = listCourriersSubordonnees.get(i);
			result = result
					+ courrierConsInformations.getCourrierDestinataireReelle()
					+ " / ";
			if (!copyListCourriersSubordonnees
					.contains(courrierConsInformations)) {

				for (int j = 0; j < listCourriersSubordonnees.size(); j++) {
					try {
						if (listCourriersSubordonnees
								.get(j)
								.getCourrierReference()
								.equals(courrierConsInformations
										.getCourrierReference())
								&& listCourriersSubordonnees
										.get(j)
										.getCourrierExpediteur()
										.equals(courrierConsInformations
												.getCourrierExpediteur())
								&& listCourriersSubordonnees
										.get(j)
										.getCourrierDateReceptionEnvoi()
										.toString()
										.equals(courrierConsInformations
												.getCourrierDateReceptionEnvoi()
												.toString())
								&& listCourriersSubordonnees.get(j) != courrierConsInformations) {
							result = result
									+ listCourriersSubordonnees.get(j)
											.getCourrierDestinataireReelle()
									+ " / ";
							copyListCourriersSubordonnees
									.add(listCourriersSubordonnees.get(j));
						}
					} catch (NullPointerException e) {
						e.printStackTrace();
					}

				}
				int lastIndex;
				int indexOfObject;
				if (!result.equals(courrierConsInformations
						.getCourrierDestinataireReelle() + " / ")) {
					lastIndex = result.lastIndexOf("/");
					result = result.substring(0, lastIndex);
					indexOfObject = listCourriersRecus
							.indexOf(courrierConsInformations);
					courrierConsInformations
							.setCourrierDestinataireReelle(result);
					listCourriersRecus.set(indexOfObject,
							courrierConsInformations);
				}
			}
		}
		if (!copyListCourriersSubordonnees.isEmpty()) {
			listCourriersRecus.removeAll(copyListCourriersSubordonnees);
		}

		listCourriersSubordonnees = new ArrayList<CourrierConsulterInformations>();
		for (int i = 0; i < listCourriersRecus.size(); i++) {
			if (listCourriersRecus.get(i).getTypeCourrier()
					.equals("E. Les Courriers de Mes Sous-Unités")) {
				listCourriersSubordonnees.add(listCourriersRecus.get(i));
			}
		}
		copyListCourriersSubordonnees = new ArrayList<CourrierConsulterInformations>();
		for (int i = 0; i < listCourriersSubordonnees.size(); i++) {
			result = "";
			courrierConsInformations = new CourrierConsulterInformations();
			courrierConsInformations = listCourriersSubordonnees.get(i);
			result = result
					+ courrierConsInformations.getCourrierDestinataireReelle()
					+ " / ";
			if (!copyListCourriersSubordonnees
					.contains(courrierConsInformations)) {
				for (int j = 0; j < listCourriersSubordonnees.size(); j++) {
					try {
						if (listCourriersSubordonnees
								.get(j)
								.getCourrierReference()
								.equals(courrierConsInformations
										.getCourrierReference())
								&& listCourriersSubordonnees
										.get(j)
										.getCourrierExpediteur()
										.equals(courrierConsInformations
												.getCourrierExpediteur())
								&& listCourriersSubordonnees
										.get(j)
										.getCourrierDateReceptionEnvoi()
										.toString()
										.equals(courrierConsInformations
												.getCourrierDateReceptionEnvoi()
												.toString())
								&& listCourriersSubordonnees.get(j) != courrierConsInformations) {
							result = result
									+ listCourriersSubordonnees.get(j)
											.getCourrierDestinataireReelle()
									+ " / ";
							copyListCourriersSubordonnees
									.add(listCourriersSubordonnees.get(j));
						}
					} catch (NullPointerException e) {
						e.printStackTrace();
					}

				}
				int lastIndex;
				int indexOfObject;
				if (!result.equals(courrierConsInformations
						.getCourrierDestinataireReelle() + " / ")) {
					lastIndex = result.lastIndexOf("/");
					result = result.substring(0, lastIndex);
					indexOfObject = listCourriersRecus
							.indexOf(courrierConsInformations);
					courrierConsInformations
							.setCourrierDestinataireReelle(result);
					listCourriersRecus.set(indexOfObject,
							courrierConsInformations);
				}
			}

		}
		if (!copyListCourriersSubordonnees.isEmpty()) {
			listCourriersRecus.removeAll(copyListCourriersSubordonnees);
		}

		listCourriersSubordonnees = new ArrayList<CourrierConsulterInformations>();
		for (int i = 0; i < listCourriersRecus.size(); i++) {
			if (listCourriersRecus.get(i).getTypeCourrier()
					.equals("D. Les Courriers de Mes Agents")) {
				listCourriersSubordonnees.add(listCourriersRecus.get(i));
			}
		}
		copyListCourriersSubordonnees = new ArrayList<CourrierConsulterInformations>();
		for (int i = 0; i < listCourriersSubordonnees.size(); i++) {
			result = "";
			courrierConsInformations = new CourrierConsulterInformations();
			courrierConsInformations = listCourriersSubordonnees.get(i);
			result = result
					+ courrierConsInformations.getCourrierDestinataireReelle()
					+ " / ";
			if (!copyListCourriersSubordonnees
					.contains(courrierConsInformations)) {
				for (int j = 0; j < listCourriersSubordonnees.size(); j++) {
					try {
						if (listCourriersSubordonnees
								.get(j)
								.getCourrierReference()
								.equals(courrierConsInformations
										.getCourrierReference())
								&& listCourriersSubordonnees
										.get(j)
										.getCourrierExpediteur()
										.equals(courrierConsInformations
												.getCourrierExpediteur())
								&& listCourriersSubordonnees
										.get(j)
										.getCourrierDateReceptionEnvoi()
										.toString()
										.equals(courrierConsInformations
												.getCourrierDateReceptionEnvoi()
												.toString())
								&& listCourriersSubordonnees.get(j) != courrierConsInformations) {
							result = result
									+ listCourriersSubordonnees.get(j)
											.getCourrierDestinataireReelle()
									+ " / ";
							copyListCourriersSubordonnees
									.add(listCourriersSubordonnees.get(j));
						}
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
				}
				int lastIndex;
				int indexOfObject;
				if (!result.equals(courrierConsInformations
						.getCourrierDestinataireReelle() + " / ")) {
					lastIndex = result.lastIndexOf("/");
					result = result.substring(0, lastIndex);
					indexOfObject = listCourriersRecus
							.indexOf(courrierConsInformations);
					courrierConsInformations
							.setCourrierDestinataireReelle(result);
					listCourriersRecus.set(indexOfObject,
							courrierConsInformations);
				}
			}

		}
		if (!copyListCourriersSubordonnees.isEmpty()) {
			listCourriersRecus.removeAll(copyListCourriersSubordonnees);
		}

		List<CourrierConsulterInformations> copyListCourriersRecus = new ArrayList<CourrierConsulterInformations>();
		for (int i = 0; i < listCourriersRecus.size(); i++) {
			copyListCourriersRecus.add(listCourriersRecus.get(i));
		}
		List<Integer> listAllIndexs = new ArrayList<Integer>();
		List<Integer> listIndexs;
		for (int i = 0; i < copyListCourriersRecus.size(); i++) {
			if (!listAllIndexs.contains(i) || listAllIndexs.isEmpty()) {
				listIndexs = new ArrayList<Integer>();
				result = "";
				courrierConsInformations = new CourrierConsulterInformations();
				courrierConsInformations = copyListCourriersRecus.get(i);
				result = result
						+ courrierConsInformations
								.getCourrierDestinataireReelle() + " / ";
				for (int j = i + 1; j < copyListCourriersRecus.size(); j++) {
					try {
						if (copyListCourriersRecus
								.get(j)
								.getCourrierReference()
								.equals(courrierConsInformations
										.getCourrierReference())
								&& copyListCourriersRecus
										.get(j)
										.getCourrierExpediteur()
										.equals(courrierConsInformations
												.getCourrierExpediteur())
								&& copyListCourriersRecus
										.get(j)
										.getCourrierDateReceptionEnvoi()
										.toString()
										.equals(courrierConsInformations
												.getCourrierDateReceptionEnvoi()
												.toString())) {
							result = result
									+ copyListCourriersRecus.get(j)
											.getCourrierDestinataireReelle()
									+ " / ";
							listIndexs.add(j);
						}
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
				}

				int lastIndex;
				String copyResult;
				if (!listIndexs.isEmpty()) {
					CourrierConsulterInformations copyCourrierConsulterInformations;
					listAllIndexs.addAll(listIndexs);
					String stringToReplace = courrierConsInformations
							.getCourrierDestinataireReelle() + " / ";

					copyResult = result.replace(stringToReplace, "");
					lastIndex = copyResult.lastIndexOf("/");
					copyResult = copyResult.substring(0, lastIndex);
					courrierConsInformations
							.setCourrierAutreDestinataires(copyResult);
					listCourriersRecus.set(i, courrierConsInformations);
					for (int j = 0; j < listIndexs.size(); j++) {
						copyCourrierConsulterInformations = new CourrierConsulterInformations();
						copyCourrierConsulterInformations = copyListCourriersRecus
								.get(listIndexs.get(j));
						copyResult = result.replace(
								copyCourrierConsulterInformations
										.getCourrierDestinataireReelle()
										+ " / ", "");
						lastIndex = copyResult.lastIndexOf("/");
						copyResult = copyResult.substring(0, lastIndex);
						copyCourrierConsulterInformations
								.setCourrierAutreDestinataires(copyResult);
						listCourriersRecus.set(listIndexs.get(j),
								copyCourrierConsulterInformations);
					}
				}
			}
		}

		listCourriersSubordonnees = new ArrayList<CourrierConsulterInformations>();
		for (int i = 0; i < listDossiersRecus.size(); i++) {
			if (listDossiersRecus.get(i).getTypeCourrier()
					.equals("F. Les Dossiers de Mes Subordonnées")) {
				listCourriersSubordonnees.add(listDossiersRecus.get(i));
			}
		}
		copyListCourriersSubordonnees = new ArrayList<CourrierConsulterInformations>();
		for (int i = 0; i < listCourriersSubordonnees.size(); i++) {
			result = "";
			courrierConsInformations = new CourrierConsulterInformations();
			courrierConsInformations = listCourriersSubordonnees.get(i);
			result = result
					+ courrierConsInformations.getCourrierDestinataireReelle()
					+ " / ";
			if (!copyListCourriersSubordonnees
					.contains(courrierConsInformations)) {

				for (int j = 0; j < listCourriersSubordonnees.size(); j++) {
					try {
						if (listCourriersSubordonnees
								.get(j)
								.getCourrierReference()
								.equals(courrierConsInformations
										.getCourrierReference())
								&& listCourriersSubordonnees
										.get(j)
										.getCourrierExpediteur()
										.equals(courrierConsInformations
												.getCourrierExpediteur())
								&& listCourriersSubordonnees
										.get(j)
										.getCourrierDateReceptionEnvoi()
										.toString()
										.equals(courrierConsInformations
												.getCourrierDateReceptionEnvoi()
												.toString())
								&& listCourriersSubordonnees.get(j) != courrierConsInformations) {
							result = result
									+ listCourriersSubordonnees.get(j)
											.getCourrierDestinataireReelle()
									+ " / ";
							copyListCourriersSubordonnees
									.add(listCourriersSubordonnees.get(j));
						}
					} catch (NullPointerException e) {
						e.printStackTrace();
					}

				}
				int lastIndex;
				int indexOfObject;
				if (!result.equals(courrierConsInformations
						.getCourrierDestinataireReelle() + " / ")) {
					lastIndex = result.lastIndexOf("/");
					result = result.substring(0, lastIndex);
					indexOfObject = listDossiersRecus
							.indexOf(courrierConsInformations);
					courrierConsInformations
							.setCourrierDestinataireReelle(result);
					listDossiersRecus.set(indexOfObject,
							courrierConsInformations);
				}
			}
		}
		if (!copyListCourriersSubordonnees.isEmpty()) {
			listDossiersRecus.removeAll(copyListCourriersSubordonnees);
		}

		listCourriersSubordonnees = new ArrayList<CourrierConsulterInformations>();
		for (int i = 0; i < listDossiersRecus.size(); i++) {
			if (listDossiersRecus.get(i).getTypeCourrier()
					.equals("E. Les Dossiers de Mes Sous-Unités")) {
				listCourriersSubordonnees.add(listDossiersRecus.get(i));
			}
		}
		copyListCourriersSubordonnees = new ArrayList<CourrierConsulterInformations>();
		for (int i = 0; i < listCourriersSubordonnees.size(); i++) {
			result = "";
			courrierConsInformations = new CourrierConsulterInformations();
			courrierConsInformations = listCourriersSubordonnees.get(i);
			result = result
					+ courrierConsInformations.getCourrierDestinataireReelle()
					+ " / ";
			if (!copyListCourriersSubordonnees
					.contains(courrierConsInformations)) {
				for (int j = 0; j < listCourriersSubordonnees.size(); j++) {
					try {
						if (listCourriersSubordonnees
								.get(j)
								.getCourrierReference()
								.equals(courrierConsInformations
										.getCourrierReference())
								&& listCourriersSubordonnees
										.get(j)
										.getCourrierExpediteur()
										.equals(courrierConsInformations
												.getCourrierExpediteur())
								&& listCourriersSubordonnees
										.get(j)
										.getCourrierDateReceptionEnvoi()
										.toString()
										.equals(courrierConsInformations
												.getCourrierDateReceptionEnvoi()
												.toString())
								&& listCourriersSubordonnees.get(j) != courrierConsInformations) {
							result = result
									+ listCourriersSubordonnees.get(j)
											.getCourrierDestinataireReelle()
									+ " / ";
							copyListCourriersSubordonnees
									.add(listCourriersSubordonnees.get(j));
						}
					} catch (NullPointerException e) {
						e.printStackTrace();
					}

				}
				int lastIndex;
				int indexOfObject;
				if (!result.equals(courrierConsInformations
						.getCourrierDestinataireReelle() + " / ")) {
					lastIndex = result.lastIndexOf("/");
					result = result.substring(0, lastIndex);
					indexOfObject = listDossiersRecus
							.indexOf(courrierConsInformations);
					courrierConsInformations
							.setCourrierDestinataireReelle(result);
					listDossiersRecus.set(indexOfObject,
							courrierConsInformations);
				}
			}

		}
		if (!copyListCourriersSubordonnees.isEmpty()) {
			listDossiersRecus.removeAll(copyListCourriersSubordonnees);
		}

		listCourriersSubordonnees = new ArrayList<CourrierConsulterInformations>();
		for (int i = 0; i < listDossiersRecus.size(); i++) {
			if (listDossiersRecus.get(i).getTypeCourrier()
					.equals("D. Les Dossiers de Mes Agents")) {
				listCourriersSubordonnees.add(listDossiersRecus.get(i));
			}
		}
		copyListCourriersSubordonnees = new ArrayList<CourrierConsulterInformations>();
		for (int i = 0; i < listCourriersSubordonnees.size(); i++) {
			result = "";
			courrierConsInformations = new CourrierConsulterInformations();
			courrierConsInformations = listCourriersSubordonnees.get(i);
			result = result
					+ courrierConsInformations.getCourrierDestinataireReelle()
					+ " / ";
			if (!copyListCourriersSubordonnees
					.contains(courrierConsInformations)) {
				for (int j = 0; j < listCourriersSubordonnees.size(); j++) {
					try {
						if (listCourriersSubordonnees
								.get(j)
								.getCourrierReference()
								.equals(courrierConsInformations
										.getCourrierReference())
								&& listCourriersSubordonnees
										.get(j)
										.getCourrierExpediteur()
										.equals(courrierConsInformations
												.getCourrierExpediteur())
								&& listCourriersSubordonnees
										.get(j)
										.getCourrierDateReceptionEnvoi()
										.toString()
										.equals(courrierConsInformations
												.getCourrierDateReceptionEnvoi()
												.toString())
								&& listCourriersSubordonnees.get(j) != courrierConsInformations) {
							result = result
									+ listCourriersSubordonnees.get(j)
											.getCourrierDestinataireReelle()
									+ " / ";
							copyListCourriersSubordonnees
									.add(listCourriersSubordonnees.get(j));
						}
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
				}
				int lastIndex;
				int indexOfObject;
				if (!result.equals(courrierConsInformations
						.getCourrierDestinataireReelle() + " / ")) {
					lastIndex = result.lastIndexOf("/");
					result = result.substring(0, lastIndex);
					indexOfObject = listDossiersRecus
							.indexOf(courrierConsInformations);
					courrierConsInformations
							.setCourrierDestinataireReelle(result);
					listDossiersRecus.set(indexOfObject,
							courrierConsInformations);
				}
			}

		}
		if (!copyListCourriersSubordonnees.isEmpty()) {
			listDossiersRecus.removeAll(copyListCourriersSubordonnees);
		}

		copyListCourriersRecus = new ArrayList<CourrierConsulterInformations>();
		for (int i = 0; i < listDossiersRecus.size(); i++) {
			copyListCourriersRecus.add(listDossiersRecus.get(i));
		}
		listAllIndexs = new ArrayList<Integer>();
		for (int i = 0; i < copyListCourriersRecus.size(); i++) {
			if (!listAllIndexs.contains(i) || listAllIndexs.isEmpty()) {
				listIndexs = new ArrayList<Integer>();
				result = "";
				courrierConsInformations = new CourrierConsulterInformations();
				courrierConsInformations = copyListCourriersRecus.get(i);
				result = result
						+ courrierConsInformations
								.getCourrierDestinataireReelle() + " / ";
				for (int j = i + 1; j < copyListCourriersRecus.size(); j++) {
					try {
						if (copyListCourriersRecus
								.get(j)
								.getCourrierReference()
								.equals(courrierConsInformations
										.getCourrierReference())
								&& copyListCourriersRecus
										.get(j)
										.getCourrierExpediteur()
										.equals(courrierConsInformations
												.getCourrierExpediteur())
								&& copyListCourriersRecus
										.get(j)
										.getCourrierDateReceptionEnvoi()
										.toString()
										.equals(courrierConsInformations
												.getCourrierDateReceptionEnvoi()
												.toString())) {
							result = result
									+ copyListCourriersRecus.get(j)
											.getCourrierDestinataireReelle()
									+ " / ";
							listIndexs.add(j);
						}
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
				}

				int lastIndex;
				String copyResult;
				if (!listIndexs.isEmpty()) {
					CourrierConsulterInformations copyCourrierConsulterInformations;
					listAllIndexs.addAll(listIndexs);
					String stringToReplace = courrierConsInformations
							.getCourrierDestinataireReelle() + " / ";
					copyResult = result.replace(stringToReplace, "");
					lastIndex = copyResult.lastIndexOf("/");
					copyResult = copyResult.substring(0, lastIndex);
					courrierConsInformations
							.setCourrierAutreDestinataires(copyResult);
					listDossiersRecus.set(i, courrierConsInformations);
					for (int j = 0; j < listIndexs.size(); j++) {
						copyCourrierConsulterInformations = new CourrierConsulterInformations();
						copyCourrierConsulterInformations = copyListCourriersRecus
								.get(listIndexs.get(j));
						copyResult = result.replace(
								copyCourrierConsulterInformations
										.getCourrierDestinataireReelle()
										+ " / ", "");
						lastIndex = copyResult.lastIndexOf("/");
						copyResult = copyResult.substring(0, lastIndex);
						copyCourrierConsulterInformations
								.setCourrierAutreDestinataires(copyResult);
						listDossiersRecus.set(listIndexs.get(j),
								copyCourrierConsulterInformations);
					}
				}
			}
		}
		// Courriers envoyés
		listTransactions = new ArrayList<Transaction>();
		listTransactions = getListTransactionsEnvoyes(idDirector,
				"Interne-Person", date);

		setListCourriersEnvoyes(listTransactions, "A. Mes Propres Courriers",
				"A. Mes Propres Dossiers", 0);
		listTransactions = new ArrayList<Transaction>();
		listTransactions = getListTransactionsEnvoyes(vb.getPerson()
				.getAssociatedDirection().getIdUnit(), "Interne-Unité", date);

		setListCourriersEnvoyes(listTransactions,
				"B. Les Courriers de Mon Unité",
				"B. Les Dossiers de Mon Unité", 0);
		listTransactions = new ArrayList<Transaction>();
		listTransactions = getListTransactionsEnvoyes(vb.getPerson()
				.getAssociatedDirection().getSecretaryUnit().getId(),
				"Interne-Person", date);

		setListCourriersEnvoyes(listTransactions,
				"C. Les Courriers de Ma Secrétaire",
				"C. Les Dossiers de Ma Secrétaire", 0);
		for (int i = 0; i < vb.getPerson().getAssociatedDirection()
				.getMembersUnit().size(); i++) {
			listTransactions = new ArrayList<Transaction>();
			listTransactions = getListTransactionsEnvoyes(vb.getPerson()
					.getAssociatedDirection().getMembersUnit().get(i).getId(),
					"Interne-Person", date);

			setListCourriersEnvoyes(listTransactions,
					"D. Les Courriers de Mes Agents",
					"D. Les Dossiers de Mes Agents", 0);
		}
		if (!vb.getPerson().getAssociatedDirection().getListUnitsChildUnit()
				.isEmpty()) {
			for (int i = 0; i < vb.getPerson().getAssociatedDirection()
					.getListUnitsChildUnit().size(); i++) {
				listTransactions = new ArrayList<Transaction>();
				listTransactions = getListTransactionsEnvoyes(vb.getPerson()
						.getAssociatedDirection().getListUnitsChildUnit()
						.get(i).getIdUnit(), "Interne-Unité", date);

				setListCourriersEnvoyes(listTransactions,
						"E. Les Courriers de Mes Sous-Unités",
						"E. Les Dossiers de Mes Sous-Unités", 0);

				listTransactions = new ArrayList<Transaction>();
				listTransactions = getListTransactionsEnvoyes(vb.getPerson()
						.getAssociatedDirection().getListUnitsChildUnit()
						.get(i).getResponsibleUnit().getId(), "Interne-Person",
						date);

				setListCourriersEnvoyes(listTransactions,
						"F. Les Courriers de Mes Subordonnées",
						"F. Les Dossiers de Mes Subordonnées", 0);
			}
		}
		// Courriers totales
		Date dateJour = new Date();
		List<CourrierConsulterInformations> copyListCourriersRecusEnvoyés = new ArrayList<CourrierConsulterInformations>();
		for (CourrierConsulterInformations courrierConsulterInformations : listCourriersRecus) {
			copyListCourriersRecusEnvoyés.add(courrierConsulterInformations);
		}

		for (int i = 0; i < copyListCourriersRecusEnvoyés.size(); i++) {

			if (copyListCourriersRecusEnvoyés.get(i)
					.getCourrierDateReceptionEnvoi().getDate() == dateJour
					.getDate()
					&& copyListCourriersRecusEnvoyés.get(i)
							.getCourrierDateReceptionEnvoi().getMonth() == dateJour
							.getMonth()
					&& copyListCourriersRecusEnvoyés.get(i)
							.getCourrierDateReceptionEnvoi().getYear() == dateJour
							.getYear()) {

				listCourriersRecusJour
						.add(copyListCourriersRecusEnvoyés.get(i));
				listCourriersRecus.remove(copyListCourriersRecusEnvoyés.get(i));
			}
		}

		copyListCourriersRecusEnvoyés = new ArrayList<CourrierConsulterInformations>();
		for (CourrierConsulterInformations courrierConsulterInformations : listCourriersRecusAvalider) {
			copyListCourriersRecusEnvoyés.add(courrierConsulterInformations);
		}
		for (int i = 0; i < copyListCourriersRecusEnvoyés.size(); i++) {

			if (copyListCourriersRecusEnvoyés.get(i)
					.getCourrierDateReceptionEnvoi().getDate() == dateJour
					.getDate()
					&& copyListCourriersRecusEnvoyés.get(i)
							.getCourrierDateReceptionEnvoi().getMonth() == dateJour
							.getMonth()
					&& copyListCourriersRecusEnvoyés.get(i)
							.getCourrierDateReceptionEnvoi().getYear() == dateJour
							.getYear()) {

				listCourriersRecusJourAvalider
						.add(copyListCourriersRecusEnvoyés.get(i));
				listCourriersRecusAvalider.remove(copyListCourriersRecusEnvoyés
						.get(i));
			}
		}
		copyListCourriersRecusEnvoyés = new ArrayList<CourrierConsulterInformations>();
		for (CourrierConsulterInformations courrierConsulterInformations : listCourriersEnvoyes) {
			copyListCourriersRecusEnvoyés.add(courrierConsulterInformations);
		}
		for (int i = 0; i < copyListCourriersRecusEnvoyés.size(); i++) {

			if (copyListCourriersRecusEnvoyés.get(i)
					.getCourrierDateReceptionEnvoi().getDate() == dateJour
					.getDate()
					&& copyListCourriersRecusEnvoyés.get(i)
							.getCourrierDateReceptionEnvoi().getMonth() == dateJour
							.getMonth()
					&& copyListCourriersRecusEnvoyés.get(i)
							.getCourrierDateReceptionEnvoi().getYear() == dateJour
							.getYear()) {

				listCourriersEnvoyesJour.add(copyListCourriersRecusEnvoyés
						.get(i));
				listCourriersEnvoyes.remove(copyListCourriersRecusEnvoyés
						.get(i));
			}
		}
		listcourrierConsulterInformationsJour.addAll(listCourriersRecusJour);
		listcourrierConsulterInformationsJour.addAll(listCourriersEnvoyesJour);
		courrierConsulterInformations.addAll(listCourriersRecus);
		courrierConsulterInformations.addAll(listCourriersEnvoyes);

		copyListCourriersRecusEnvoyés = new ArrayList<CourrierConsulterInformations>();
		for (CourrierConsulterInformations courrierConsulterInformations : listDossiersRecus) {
			copyListCourriersRecusEnvoyés.add(courrierConsulterInformations);
		}
		for (int i = 0; i < copyListCourriersRecusEnvoyés.size(); i++) {

			if (copyListCourriersRecusEnvoyés.get(i)
					.getCourrierDateReceptionEnvoi().getDate() == dateJour
					.getDate()
					&& copyListCourriersRecusEnvoyés.get(i)
							.getCourrierDateReceptionEnvoi().getMonth() == dateJour
							.getMonth()
					&& copyListCourriersRecusEnvoyés.get(i)
							.getCourrierDateReceptionEnvoi().getYear() == dateJour
							.getYear()) {

				if (copyListCourriersRecusEnvoyés.get(i).isShared()) {

					listDossiersRecusJour.add(copyListCourriersRecusEnvoyés
							.get(i));
					listDossiersRecus.remove(copyListCourriersRecusEnvoyés
							.get(i));
				} else {
					listDossiersRecusJour.add(copyListCourriersRecusEnvoyés
							.get(i));
					listDossiersRecus.remove(copyListCourriersRecusEnvoyés
							.get(i));
				}
			}
		}

		copyListCourriersRecusEnvoyés = new ArrayList<CourrierConsulterInformations>();
		for (CourrierConsulterInformations courrierConsulterInformations : listDossiersEnvoyes) {
			copyListCourriersRecusEnvoyés.add(courrierConsulterInformations);
		}
		for (int i = 0; i < copyListCourriersRecusEnvoyés.size(); i++) {

			if (copyListCourriersRecusEnvoyés.get(i)
					.getCourrierDateReceptionEnvoi().getDate() == dateJour
					.getDate()
					&& copyListCourriersRecusEnvoyés.get(i)
							.getCourrierDateReceptionEnvoi().getMonth() == dateJour
							.getMonth()
					&& copyListCourriersRecusEnvoyés.get(i)
							.getCourrierDateReceptionEnvoi().getYear() == dateJour
							.getYear()) {

				if (copyListCourriersRecusEnvoyés.get(i).isShared()) {
					listDossiersEnvoyesJour.add(copyListCourriersRecusEnvoyés
							.get(i));
					listDossiersEnvoyes.remove(copyListCourriersRecusEnvoyés
							.get(i));
				} else {
					listDossiersEnvoyesJour.add(copyListCourriersRecusEnvoyés
							.get(i));
					listDossiersEnvoyes.remove(copyListCourriersRecusEnvoyés
							.get(i));
				}
			}
		}
		listdossierConsulterInformationsJour.addAll(listDossiersRecusJour);
		listdossierConsulterInformationsJour.addAll(listDossiersEnvoyesJour);
		dossierConsulterInformations.addAll(listDossiersRecus);
		dossierConsulterInformations.addAll(listDossiersEnvoyes);
	}

	@SuppressWarnings("deprecation")
	public void getListTransactionForSecretary(int idSecretary, Date date) {
		// Courriers recus
		List<Transaction> listTransactions = new ArrayList<Transaction>();
		listTransactions = getListTransactionsRecus(idSecretary,
				"Interne-Person", date);
		setListCourriersRecus(listTransactions, "A. Mes Propres Courriers",
				"A. Mes Propres Dossiers", 1);
		listTransactions = new ArrayList<Transaction>();
		listTransactions = getListTransactionsRecus(vb.getPerson()
				.getAssociatedDirection().getIdUnit(), "Interne-Unité", date);
		setListCourriersRecus(listTransactions,
				"B. Les Courriers de Mon Unité",
				"B. Les Dossiers de Mon Unité", 1);
		// Courriers envoyés
		listTransactions = new ArrayList<Transaction>();
		listTransactions = getListTransactionsEnvoyes(idSecretary,
				"Interne-Person", date);
		setListCourriersEnvoyes(listTransactions, "A. Mes Propres Courriers",
				"A. Mes Propres Dossiers", 0);
		listTransactions = new ArrayList<Transaction>();
		listTransactions = getListTransactionsEnvoyes(vb.getPerson()
				.getAssociatedDirection().getIdUnit(), "Interne-Unité", date);
		setListCourriersEnvoyes(listTransactions,
				"B. Les Courriers de Mon Unité",
				"B. Les Dossiers de Mon Unité", 0);
		// Courriers totales
		Date dateJour = new Date();
		List<CourrierConsulterInformations> copyListCourriersRecusEnvoyés = new ArrayList<CourrierConsulterInformations>();
		for (CourrierConsulterInformations courrierConsulterInformations : listCourriersRecus) {
			copyListCourriersRecusEnvoyés.add(courrierConsulterInformations);
		}

		for (int i = 0; i < copyListCourriersRecusEnvoyés.size(); i++) {

			if (copyListCourriersRecusEnvoyés.get(i)
					.getCourrierDateReceptionEnvoi().getDate() == dateJour
					.getDate()
					&& copyListCourriersRecusEnvoyés.get(i)
							.getCourrierDateReceptionEnvoi().getMonth() == dateJour
							.getMonth()
					&& copyListCourriersRecusEnvoyés.get(i)
							.getCourrierDateReceptionEnvoi().getYear() == dateJour
							.getYear()) {

				listCourriersRecusJour
						.add(copyListCourriersRecusEnvoyés.get(i));
				listCourriersRecus.remove(copyListCourriersRecusEnvoyés.get(i));
			}
		}

		copyListCourriersRecusEnvoyés = new ArrayList<CourrierConsulterInformations>();
		for (CourrierConsulterInformations courrierConsulterInformations : listCourriersEnvoyes) {
			copyListCourriersRecusEnvoyés.add(courrierConsulterInformations);
		}
		for (int i = 0; i < copyListCourriersRecusEnvoyés.size(); i++) {

			if (copyListCourriersRecusEnvoyés.get(i)
					.getCourrierDateReceptionEnvoi().getDate() == dateJour
					.getDate()
					&& copyListCourriersRecusEnvoyés.get(i)
							.getCourrierDateReceptionEnvoi().getMonth() == dateJour
							.getMonth()
					&& copyListCourriersRecusEnvoyés.get(i)
							.getCourrierDateReceptionEnvoi().getYear() == dateJour
							.getYear()) {
				listCourriersEnvoyesJour.add(copyListCourriersRecusEnvoyés
						.get(i));
				listCourriersEnvoyes.remove(copyListCourriersRecusEnvoyés
						.get(i));
			}
		}
		listcourrierConsulterInformationsJour.addAll(listCourriersRecusJour);
		listcourrierConsulterInformationsJour.addAll(listCourriersEnvoyesJour);
		courrierConsulterInformations.addAll(listCourriersRecus);
		courrierConsulterInformations.addAll(listCourriersEnvoyes);

		copyListCourriersRecusEnvoyés = new ArrayList<CourrierConsulterInformations>();
		for (CourrierConsulterInformations courrierConsulterInformations : listDossiersRecus) {
			copyListCourriersRecusEnvoyés.add(courrierConsulterInformations);
		}
		for (int i = 0; i < copyListCourriersRecusEnvoyés.size(); i++) {

			if (copyListCourriersRecusEnvoyés.get(i)
					.getCourrierDateReceptionEnvoi().getDate() == dateJour
					.getDate()
					&& copyListCourriersRecusEnvoyés.get(i)
							.getCourrierDateReceptionEnvoi().getMonth() == dateJour
							.getMonth()
					&& copyListCourriersRecusEnvoyés.get(i)
							.getCourrierDateReceptionEnvoi().getYear() == dateJour
							.getYear()) {
				listDossiersRecusJour.add(copyListCourriersRecusEnvoyés.get(i));
				listDossiersRecus.remove(copyListCourriersRecusEnvoyés.get(i));
			}
		}

		copyListCourriersRecusEnvoyés = new ArrayList<CourrierConsulterInformations>();
		for (CourrierConsulterInformations courrierConsulterInformations : listDossiersEnvoyes) {
			copyListCourriersRecusEnvoyés.add(courrierConsulterInformations);
		}
		for (int i = 0; i < copyListCourriersRecusEnvoyés.size(); i++) {

			if (copyListCourriersRecusEnvoyés.get(i)
					.getCourrierDateReceptionEnvoi().getDate() == dateJour
					.getDate()
					&& copyListCourriersRecusEnvoyés.get(i)
							.getCourrierDateReceptionEnvoi().getMonth() == dateJour
							.getMonth()
					&& copyListCourriersRecusEnvoyés.get(i)
							.getCourrierDateReceptionEnvoi().getYear() == dateJour
							.getYear()) {
				listDossiersEnvoyesJour.add(copyListCourriersRecusEnvoyés
						.get(i));
				listDossiersEnvoyes
						.remove(copyListCourriersRecusEnvoyés.get(i));
			}
		}
		listdossierConsulterInformationsJour.addAll(listDossiersRecusJour);
		listdossierConsulterInformationsJour.addAll(listDossiersEnvoyesJour);
		dossierConsulterInformations.addAll(listDossiersRecus);
		dossierConsulterInformations.addAll(listDossiersEnvoyes);
	}

	@SuppressWarnings("deprecation")
	public void getListTransactionForAgent(int idAgent, Date date) {
		if (vb.getPerson().getAssociatedDirection() != null) {
			// Courriers recus
			List<Transaction> listTransactions = new ArrayList<Transaction>();
			listTransactions = getListTransactionsRecus(idAgent,
					"Interne-Person", date);
			setListCourriersRecus(listTransactions, "A. Mes Propres Courriers",
					"A. Mes Propres Dossiers", 1);
			// Courriers envoyés
			listTransactions = new ArrayList<Transaction>();
			listTransactions = getListTransactionsEnvoyes(idAgent,
					"Interne-Person", date);
			setListCourriersEnvoyes(listTransactions,
					"A. Mes Propres Courriers", "A. Mes Propres Dossiers", 0);
		} else {
			// Courriers recus
			List<Transaction> listTransactions = new ArrayList<Transaction>();
			listTransactions = getListTransactionsRecus(idAgent,
					"Interne-Person", date);
			setListCourriersRecus(listTransactions, "A. Mes Propres Courriers",
					"A. Mes Propres Dossiers", 1);
			listTransactions = new ArrayList<Transaction>();
			listTransactions = getListTransactionsRecus(vb.getPerson()
					.getAssociatedBOC().getIdBOC(), "Interne-Boc", date);
			setListCourriersRecus(listTransactions,
					"B. Les Courriers de Mon Bureau d'Ordre",
					"B. Les Dossiers de Mon Bureau d'Ordre", 1);
			// Courriers envoyés
			listTransactions = new ArrayList<Transaction>();
			listTransactions = getListTransactionsEnvoyes(idAgent,
					"Interne-Person", date);
			setListCourriersEnvoyes(listTransactions,
					"A. Mes Propres Courriers", "A. Mes Propres Dossiers", 0);
			listTransactions = new ArrayList<Transaction>();
			listTransactions = getListTransactionsEnvoyes(vb.getPerson()
					.getAssociatedBOC().getIdBOC(), "Interne-Boc", date);
			setListCourriersEnvoyes(listTransactions,
					"B. Les Courriers de Mon Bureau d'Ordre",
					"B. Les Dossiers de Mon Bureau d'Ordre", 0);
		}

		// Courriers totales
		Date dateJour = new Date();
		List<CourrierConsulterInformations> copyListCourriersRecusEnvoyés = new ArrayList<CourrierConsulterInformations>();
		for (CourrierConsulterInformations courrierConsulterInformations : listCourriersRecus) {
			copyListCourriersRecusEnvoyés.add(courrierConsulterInformations);
		}

		for (int i = 0; i < copyListCourriersRecusEnvoyés.size(); i++) {

			if (copyListCourriersRecusEnvoyés.get(i)
					.getCourrierDateReceptionEnvoi().getDate() == dateJour
					.getDate()
					&& copyListCourriersRecusEnvoyés.get(i)
							.getCourrierDateReceptionEnvoi().getMonth() == dateJour
							.getMonth()
					&& copyListCourriersRecusEnvoyés.get(i)
							.getCourrierDateReceptionEnvoi().getYear() == dateJour
							.getYear()) {

				listCourriersRecusJour
						.add(copyListCourriersRecusEnvoyés.get(i));
				listCourriersRecus.remove(copyListCourriersRecusEnvoyés.get(i));
			}
		}

		copyListCourriersRecusEnvoyés = new ArrayList<CourrierConsulterInformations>();
		for (CourrierConsulterInformations courrierConsulterInformations : listCourriersEnvoyes) {
			copyListCourriersRecusEnvoyés.add(courrierConsulterInformations);
		}
		for (int i = 0; i < copyListCourriersRecusEnvoyés.size(); i++) {

			if (copyListCourriersRecusEnvoyés.get(i)
					.getCourrierDateReceptionEnvoi().getDate() == dateJour
					.getDate()
					&& copyListCourriersRecusEnvoyés.get(i)
							.getCourrierDateReceptionEnvoi().getMonth() == dateJour
							.getMonth()
					&& copyListCourriersRecusEnvoyés.get(i)
							.getCourrierDateReceptionEnvoi().getYear() == dateJour
							.getYear()) {

				listCourriersEnvoyesJour.add(copyListCourriersRecusEnvoyés
						.get(i));
				listCourriersEnvoyes.remove(copyListCourriersRecusEnvoyés
						.get(i));
			}
		}
		listcourrierConsulterInformationsJour.addAll(listCourriersRecusJour);
		listcourrierConsulterInformationsJour.addAll(listCourriersEnvoyesJour);
		courrierConsulterInformations.addAll(listCourriersRecus);
		courrierConsulterInformations.addAll(listCourriersEnvoyes);

		copyListCourriersRecusEnvoyés = new ArrayList<CourrierConsulterInformations>();
		for (CourrierConsulterInformations courrierConsulterInformations : listDossiersRecus) {
			copyListCourriersRecusEnvoyés.add(courrierConsulterInformations);
		}
		for (int i = 0; i < copyListCourriersRecusEnvoyés.size(); i++) {

			if (copyListCourriersRecusEnvoyés.get(i)
					.getCourrierDateReceptionEnvoi().getDate() == dateJour
					.getDate()
					&& copyListCourriersRecusEnvoyés.get(i)
							.getCourrierDateReceptionEnvoi().getMonth() == dateJour
							.getMonth()
					&& copyListCourriersRecusEnvoyés.get(i)
							.getCourrierDateReceptionEnvoi().getYear() == dateJour
							.getYear()) {
				System.out
						.println("Dossier recu : " + i + " est d'aujourd'hui");
				listDossiersRecusJour.add(copyListCourriersRecusEnvoyés.get(i));
				listDossiersRecus.remove(copyListCourriersRecusEnvoyés.get(i));
			}
		}

		copyListCourriersRecusEnvoyés = new ArrayList<CourrierConsulterInformations>();
		for (CourrierConsulterInformations courrierConsulterInformations : listDossiersEnvoyes) {
			copyListCourriersRecusEnvoyés.add(courrierConsulterInformations);
		}
		for (int i = 0; i < copyListCourriersRecusEnvoyés.size(); i++) {

			if (copyListCourriersRecusEnvoyés.get(i)
					.getCourrierDateReceptionEnvoi().getDate() == dateJour
					.getDate()
					&& copyListCourriersRecusEnvoyés.get(i)
							.getCourrierDateReceptionEnvoi().getMonth() == dateJour
							.getMonth()
					&& copyListCourriersRecusEnvoyés.get(i)
							.getCourrierDateReceptionEnvoi().getYear() == dateJour
							.getYear()) {

				listDossiersEnvoyesJour.add(copyListCourriersRecusEnvoyés
						.get(i));
				listDossiersEnvoyes
						.remove(copyListCourriersRecusEnvoyés.get(i));
			}
		}
		listdossierConsulterInformationsJour.addAll(listDossiersRecusJour);
		listdossierConsulterInformationsJour.addAll(listDossiersEnvoyesJour);
		dossierConsulterInformations.addAll(listDossiersRecus);
		dossierConsulterInformations.addAll(listDossiersEnvoyes);
	}

	public List<Transaction> getListTransactionsRecus(int id, String type,
			Date date) {
		List<Expdest> listExpDest = new ArrayList<Expdest>();
		List<TransactionDestination> listTransactionDestination = new ArrayList<TransactionDestination>();
		List<TransactionDestination> copyListTransactionDestination = new ArrayList<TransactionDestination>();
		List<Transaction> listTransaction = new ArrayList<Transaction>();
		List<Transaction> copyListTransaction = new ArrayList<Transaction>();
		listExpDest = appMgr.getListExpDestById(id, type);
		for (int i = 0; i < listExpDest.size(); i++) {
			copyListTransactionDestination = appMgr
					.getListTransactionDestinationByIdExpDest(listExpDest
							.get(i).getIdExpDest());
			for (int j = 0; j < copyListTransactionDestination.size(); j++) {
				listTransactionDestination.add(copyListTransactionDestination
						.get(j));
			}
			copyListTransactionDestination = new ArrayList<TransactionDestination>();
		}
		for (int i = 0; i < listTransactionDestination.size(); i++) {
			if (date == null) {
				copyListTransaction = appMgr
						.getListTransactionByIdTransaction(listTransactionDestination
								.get(i).getId().getIdTransaction());
			} else {
				copyListTransaction = appMgr
						.getListTransactionByIdTransactionJour(
								listTransactionDestination.get(i).getId()
										.getIdTransaction(), date);
			}
			for (int j = 0; j < copyListTransaction.size(); j++) {
				listTransaction.add(copyListTransaction.get(j));
			}
			copyListTransaction = new ArrayList<Transaction>();
		}
		int i = 0;
		boolean findPerson = false;
		if (type.equals("Interne-Person")) {
			do {
				if (vb.getCopyLdapListUser().get(i).getId() == id) {
					destinataire = vb.getCopyLdapListUser().get(i).getCn();
					findPerson = true;
				} else {
					i++;
				}
			} while (!findPerson && i < vb.getCopyLdapListUser().size());
		} else if (type.equals("Interne-Unité")) {
			do {
				if (vb.getCopyLdapListUnit().get(i).getIdUnit() == id) {
					destinataire = vb.getCopyLdapListUnit().get(i)
							.getNameUnit();
					findPerson = true;
				} else {
					i++;
				}
			} while (!findPerson && i < vb.getCopyLdapListUnit().size());
		}
		return listTransaction;
	}

	public List<Transaction> getListTransactionsEnvoyes(int id, String type,
			Date date) {

		List<Expdest> listExpDest = new ArrayList<Expdest>();
		List<Transaction> listTransaction = new ArrayList<Transaction>();
		Transaction copyListTransaction = new Transaction();
		listExpDest = appMgr.getListExpDestById(id, type);

		for (int i = 0; i < listExpDest.size(); i++) {
			try {
				if (date == null) {
					copyListTransaction = appMgr.getListTransactionByIdExpDest(
							listExpDest.get(i).getIdExpDest()).get(0);
				} else {
					copyListTransaction = appMgr
							.getListTransactionByIdExpDestJour(
									listExpDest.get(i).getIdExpDest(), date)
							.get(0);
				}
				listTransaction.add(copyListTransaction);
				copyListTransaction = new Transaction();
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
				System.out.println(listExpDest.get(i).getIdExpDest()
						+ " est iDExpDest non envoyé");
			}
		}
		return listTransaction;
	}

	public void setListCourriersRecus(List<Transaction> listTransactions,
			String typeCourrier, String categorieDossier, int courrierRecu) {
		CourrierConsulterInformations consulterInformations;
		int refDossier = 0;
		CourrierDossier courrierDossier;
		Courrier courrier;
		Expdest expDest;
		int i;
		boolean findPerson;
		for (Transaction transaction : listTransactions) {
			if (!transaction.getTypetransaction().getTypeTransactionLibelle()
					.equals("Départ")) {
				consulterInformations = new CourrierConsulterInformations();
				expDest = new Expdest();
				expDest = appMgr.getListExpDestByIdExpDest(
						transaction.getExpdest().getIdExpDest()).get(0);
				if (expDest.getTypeExpDest().equals("Interne-Person")) {
					i = 0;
					findPerson = false;
					do {
						if (vb.getCopyLdapListUser().get(i).getId() == expDest
								.getIdExpDestLdap()) {
							consulterInformations.setCourrierExpediteur(vb
									.getCopyLdapListUser().get(i).getCn());
							consulterInformations.setCourrierExpediteurObjet(vb
									.getCopyLdapListUser().get(i));
							findPerson = true;
						} else {
							i++;
						}
					} while (!findPerson && i < vb.getCopyLdapListUser().size());
				} else if (expDest.getTypeExpDest().equals("Interne-Unité")) {
					i = 0;
					findPerson = false;
					do {
						if (vb.getCopyLdapListUnit().get(i).getIdUnit() == expDest
								.getIdExpDestLdap()) {
							consulterInformations
									.setCourrierExpediteur(vb
											.getCopyLdapListUnit().get(i)
											.getNameUnit());
							consulterInformations.setCourrierExpediteurObjet(vb
									.getCopyLdapListUnit().get(i));
							findPerson = true;
						} else {
							i++;
						}
					} while (!findPerson && i < vb.getCopyLdapListUnit().size());
				} else if (expDest.getTypeExpDest().equals("Externe")) {
					if (expDest.getExpdestexterne().getTypeutilisateur()
							.getTypeUtilisateurLibelle().equals("PP")) {
						consulterInformations.setCourrierExpediteur(expDest
								.getExpdestexterne().getExpDestExterneNom()
								+ " (PP)");
						consulterInformations
								.setCourrierExpediteurObjet(expDest
										.getExpdestexterne());
					} else {
						consulterInformations.setCourrierExpediteur(expDest
								.getExpdestexterne().getExpDestExterneNom()
								+ " (PM)");
						consulterInformations
								.setCourrierExpediteurObjet(expDest
										.getExpdestexterne());
					}
				}
				if (transaction.getTransactionDestinationReelle() != null) {
					TransactionDestinationReelle transactionDestinationReelle = new TransactionDestinationReelle();
					Expdestexterne expDestExterne;
					transactionDestinationReelle = transaction
							.getTransactionDestinationReelle();
					if (transactionDestinationReelle
							.getTransactionDestinationReelleTypeDestinataire()
							.equals("Externe")) {
						expDestExterne = new Expdestexterne();
						expDestExterne = appMgr
								.getExpediteurById(
										transactionDestinationReelle
												.getTransactionDestinationReelleIdDestinataire())
								.get(0);
						if (expDestExterne.getTypeutilisateur()
								.getTypeUtilisateurLibelle().equals("PP")) {
							consulterInformations
									.setCourrierDestinataireReelle(expDestExterne
											.getExpDestExterneNom() + " (PP)");
							consulterInformations
									.setCourrierDestinataireObject(expDestExterne);
						} else {
							consulterInformations
									.setCourrierDestinataireReelle(expDestExterne
											.getExpDestExterneNom() + " (PM)");
							consulterInformations
									.setCourrierDestinataireObject(expDestExterne);
						}
					}
				} else {
					consulterInformations
							.setCourrierDestinataireReelle(destinataire);
				}
				consulterInformations.setCourrierRecu(courrierRecu);
				if (transaction.getTransactionDestinationReelle() != null
						&& transaction.getEtat().getEtatLibelle()
								.equals("A valider")) {
					consulterInformations.setCourrierAValider(1);
				} else {
					consulterInformations.setCourrierAValider(0);
				}

				Dossier dossier = new Dossier();
				dossier = transaction.getDossier();
				Typedossier typeDossier = new Typedossier();
				typeDossier = appMgr.getTypeDossierById(
						dossier.getTypedossier().getTypeDossierId()).get(0);
				if (typeDossier.getTypeDossierLibelle().equals("Default")) {
					courrierDossier = new CourrierDossier();
					refDossier = dossier.getDossierId();
					courrier = new Courrier();
					courrierDossier = appMgr.getCourrierDossierByIdDossier(
							refDossier).get(0);
					courrier = appMgr.getCourrierByIdCourrier(
							courrierDossier.getId().getIdCourrier()).get(0);
					consulterInformations.setTransaction(transaction);
					consulterInformations.setCourrier(courrier);
					consulterInformations.setCourrierCommentaire(courrier
							.getCourrierCommentaire());
					consulterInformations.setCourrierObjet(courrier
							.getCourrierObjet());
					consulterInformations.setCourrierReference(courrier
							.getCourrierReferenceCorrespondant());
					consulterInformations.setCourrierNature(courrier
							.getNature().getNatureLibelle());
					consulterInformations
							.setCourrierDateReceptionEnvoi(transaction
									.getTransactionDateTransaction());
					consulterInformations.setTypeCourrier(typeCourrier);
					listCourriersRecus.add(consulterInformations);
				} else if (typeDossier.getTypeDossierLibelle().equals(
						"Personnalise")) {
					consulterInformations.setTransaction(transaction);
					consulterInformations.setDossier(dossier);
					consulterInformations.setCourrierCommentaire(dossier
							.getDossierDescription());
					consulterInformations.setCourrierObjet(dossier
							.getDossierIntitule());
					consulterInformations.setCourrierReference(dossier
							.getDossierReference());
					consulterInformations
							.setCourrierDateReceptionEnvoi(transaction
									.getTransactionDateTransaction());
					consulterInformations.setTypeCourrier(categorieDossier);
					listDossiersRecus.add(consulterInformations);
				} else if (typeDossier.getTypeDossierLibelle()
						.equals("Partage")) {
					consulterInformations.setTransaction(transaction);
					consulterInformations.setDossier(dossier);
					consulterInformations.setCourrierCommentaire(dossier
							.getDossierDescription());
					consulterInformations.setCourrierObjet(dossier
							.getDossierIntitule());
					consulterInformations.setCourrierReference(dossier
							.getDossierReference());
					consulterInformations
							.setCourrierDateReceptionEnvoi(transaction
									.getTransactionDateTransaction());
					consulterInformations.setTypeCourrier(categorieDossier);
					consulterInformations.setShared(true);
					consulterInformations.setNotShared(false);
					listDossiersRecus.add(consulterInformations);

				}
				if (transaction.getTransactionDestinationReelle() != null
						&& transaction.getEtat().getEtatLibelle()
								.equals("A valider")
						&& (typeCourrier.equals("A. Mes Propres Courriers") || typeCourrier
								.equals("B. Les Courriers de Mon Unité"))) {
					listCourriersRecusAvalider.add(consulterInformations);
				}
			}
		}
	}

	public void setListCourriersEnvoyes(List<Transaction> listTransactions,
			String typeCourrier, String categorieDossier, int courrierRecu) {
		CourrierConsulterInformations consulterInformations;
		List<TransactionDestination> listTransactionDestination;
		int refDossier = 0;
		CourrierDossier courrierDossier;
		Courrier courrier;
		Expdest expDest;
		int i = 0;
		String result;
		boolean findPerson = false;
		for (Transaction transaction : listTransactions) {
			if (!transaction.getTypetransaction().getTypeTransactionLibelle()
					.equals("Départ")) {
				consulterInformations = new CourrierConsulterInformations();
				listTransactionDestination = new ArrayList<TransactionDestination>();
				listTransactionDestination = appMgr
						.getListTransactionDestinationByIdTransaction(transaction
								.getTransactionId());
				result = "";
				if (transaction.getTransactionDestinationReelle() != null) {
					TransactionDestinationReelle transactionDestinationReelle = new TransactionDestinationReelle();
					Expdestexterne expDestExterne;
					transactionDestinationReelle = transaction
							.getTransactionDestinationReelle();
					if (transactionDestinationReelle
							.getTransactionDestinationReelleTypeDestinataire()
							.equals("Externe")) {
						System.out.print("Dans courrier DestinationReelle : ");
						if (transaction.getTypetransaction()
								.getTypeTransactionLibelle().equals("Réponse")) {
							TransactionDestination transactionDestination = new TransactionDestination();
							transactionDestination = appMgr
									.getListTransactionDestinationByIdTransaction(
											transaction.getTransactionId())
									.get(0);
							expDest = new Expdest();
							expDest = appMgr.getListExpDestByIdExpDest(
									transactionDestination.getId()
											.getIdExpDest()).get(0);
							if (expDest.getTypeExpDest().equals(
									"Interne-Person")) {
								i = 0;
								findPerson = false;
								do {
									if (vb.getCopyLdapListUser().get(i).getId() == expDest
											.getIdExpDestLdap()) {
										result = result
												+ vb.getCopyLdapListUser()
														.get(i).getCn() + " / ";
										findPerson = true;
									} else {
										i++;
									}
								} while (!findPerson
										&& i < vb.getCopyLdapListUser().size());
							}
						} else {
							expDestExterne = new Expdestexterne();
							expDestExterne = appMgr
									.getExpediteurById(
											transactionDestinationReelle
													.getTransactionDestinationReelleIdDestinataire())
									.get(0);
							if (expDestExterne.getTypeutilisateur()
									.getTypeUtilisateurLibelle().equals("PP")) {
								result = result
										+ expDestExterne.getExpDestExterneNom()
										+ " (PP)" + " / ";
							} else {
								// consulterInformations.setCourrierDestinataireReelle(expDest.getExpdestexterne().getExpDestExterneNom()+" (PM)");
								result = result
										+ expDestExterne.getExpDestExterneNom()
										+ " (PM)" + " / ";
							}
						}
					}
				} else {
					for (TransactionDestination transactionDestination : listTransactionDestination) {
						expDest = new Expdest();
						expDest = appMgr.getListExpDestByIdExpDest(
								transactionDestination.getId().getIdExpDest())
								.get(0);
						if (expDest.getTypeExpDest().equals("Interne-Person")) {
							i = 0;
							findPerson = false;
							do {
								if (vb.getCopyLdapListUser().get(i).getId() == expDest
										.getIdExpDestLdap()) {
									// consulterInformations.setCourrierDestinataireReelle(vb.getCopyLdapListUser().get(i).getCn());
									result = result
											+ vb.getCopyLdapListUser().get(i)
													.getCn() + " / ";
									findPerson = true;
								} else {
									i++;
								}
							} while (!findPerson
									&& i < vb.getCopyLdapListUser().size());
						} else if (expDest.getTypeExpDest().equals(
								"Interne-Unité")) {
							i = 0;
							findPerson = false;
							do {
								if (vb.getCopyLdapListUnit().get(i).getIdUnit() == expDest
										.getIdExpDestLdap()) {
									// consulterInformations.setCourrierDestinataireReelle(vb.getCopyLdapListUnit().get(i).getNameUnit());
									result = result
											+ vb.getCopyLdapListUnit().get(i)
													.getNameUnit() + " / ";
									findPerson = true;
								} else {
									i++;
								}
							} while (!findPerson
									&& i < vb.getCopyLdapListUnit().size());
						} else if (expDest.getTypeExpDest().equals("Externe")) {
							if (expDest.getExpdestexterne()
									.getTypeutilisateur()
									.getTypeUtilisateurLibelle().equals("PP")) {
								// consulterInformations.setCourrierDestinataireReelle(expDest.getExpdestexterne().getExpDestExterneNom()+" (PP)");
								result = result
										+ expDest.getExpdestexterne()
												.getExpDestExterneNom()
										+ " (PP)" + " / ";
							} else {
								// consulterInformations.setCourrierDestinataireReelle(expDest.getExpdestexterne().getExpDestExterneNom()+" (PM)");
								result = result
										+ expDest.getExpdestexterne()
												.getExpDestExterneNom()
										+ " (PM)" + " / ";
							}
						}
					}
				}
				if (!result.equals("")) {
					int lastIndex = result.lastIndexOf("/");
					result = result.substring(0, lastIndex);
				}
				consulterInformations.setCourrierDestinataireReelle(result);
				expDest = new Expdest();
				expDest = appMgr.getListExpDestByIdExpDest(
						transaction.getExpdest().getIdExpDest()).get(0);
				if (expDest.getTypeExpDest().equals("Interne-Person")) {
					i = 0;
					findPerson = false;
					do {
						if (vb.getCopyLdapListUser().get(i).getId() == expDest
								.getIdExpDestLdap()) {
							consulterInformations.setCourrierExpediteur(vb
									.getCopyLdapListUser().get(i).getCn());
							findPerson = true;
						} else {
							i++;
						}
					} while (!findPerson && i < vb.getCopyLdapListUser().size());
				} else if (expDest.getTypeExpDest().equals("Interne-Unité")) {
					i = 0;
					findPerson = false;
					do {
						if (vb.getCopyLdapListUnit().get(i).getIdUnit() == expDest
								.getIdExpDestLdap()) {
							consulterInformations
									.setCourrierExpediteur(vb
											.getCopyLdapListUnit().get(i)
											.getNameUnit());
							findPerson = true;
						} else {
							i++;
						}
					} while (!findPerson && i < vb.getCopyLdapListUnit().size());
				} else if (expDest.getTypeExpDest().equals("Externe")) {
					if (expDest.getExpdestexterne().getTypeutilisateur()
							.getTypeUtilisateurLibelle().equals("PP")) {
						consulterInformations.setCourrierExpediteur(expDest
								.getExpdestexterne().getExpDestExterneNom()
								+ " (PP)");
					} else {
						consulterInformations.setCourrierExpediteur(expDest
								.getExpdestexterne().getExpDestExterneNom()
								+ " (PM)");
					}
				}

				consulterInformations.setCourrierAValider(0);
				consulterInformations.setCourrierRecu(courrierRecu);
				Dossier dossier = new Dossier();
				dossier = transaction.getDossier();
				Typedossier typeDossier = new Typedossier();
				typeDossier = appMgr.getTypeDossierById(
						dossier.getTypedossier().getTypeDossierId()).get(0);
				if (typeDossier.getTypeDossierLibelle().equals("Default")) {
					courrierDossier = new CourrierDossier();
					refDossier = dossier.getDossierId();
					courrier = new Courrier();
					courrierDossier = appMgr.getCourrierDossierByIdDossier(
							refDossier).get(0);
					courrier = appMgr.getCourrierByIdCourrier(
							courrierDossier.getId().getIdCourrier()).get(0);
					consulterInformations.setTransaction(transaction);
					consulterInformations.setCourrier(courrier);
					consulterInformations.setCourrierCommentaire(courrier
							.getCourrierCommentaire());
					consulterInformations.setCourrierObjet(courrier
							.getCourrierObjet());
					consulterInformations.setCourrierReference(courrier
							.getCourrierReferenceCorrespondant());
					consulterInformations.setCourrierNature(courrier
							.getNature().getNatureLibelle());
					consulterInformations
							.setCourrierDateReceptionEnvoi(transaction
									.getTransactionDateTransaction());
					consulterInformations.setTypeCourrier(typeCourrier);
					listCourriersEnvoyes.add(consulterInformations);
				} else if (typeDossier.getTypeDossierLibelle().equals(
						"Personnalise")) {
					consulterInformations.setTransaction(transaction);
					consulterInformations.setDossier(dossier);
					consulterInformations.setCourrierCommentaire(dossier
							.getDossierDescription());
					consulterInformations.setCourrierObjet(dossier
							.getDossierIntitule());
					consulterInformations.setCourrierReference(dossier
							.getDossierReference());
					consulterInformations
							.setCourrierDateReceptionEnvoi(transaction
									.getTransactionDateTransaction());
					consulterInformations.setTypeCourrier(categorieDossier);
					listDossiersEnvoyes.add(consulterInformations);
				} else if (typeDossier.getTypeDossierLibelle()
						.equals("Partage")) {
					consulterInformations.setTransaction(transaction);
					consulterInformations.setDossier(dossier);
					consulterInformations.setCourrierCommentaire(dossier
							.getDossierDescription());
					consulterInformations.setCourrierObjet(dossier
							.getDossierIntitule());
					consulterInformations.setCourrierReference(dossier
							.getDossierReference());
					consulterInformations
							.setCourrierDateReceptionEnvoi(transaction
									.getTransactionDateTransaction());
					consulterInformations.setTypeCourrier(categorieDossier);
					consulterInformations.setShared(true);
					consulterInformations.setNotShared(false);
					listDossiersEnvoyes.add(consulterInformations);
				}

			}
		}

	}

	// *** Log && Notification ***//
	public void chargementNotification(
			CourrierConsulterInformations consulterInformations) {

		// Transaction transaction = new Transaction();
		// transaction = consulterInformations.getTransaction();
		// *** Log && Notification ***//
		vbn.setEvenementNomVariableNotif("event_consult_courrier_notif");
		vbn.setNotificationNomVariablAdmin("consult_courrier_admin");
		vbn.setNotificationNomVariableDestinataire("consult_courrier_dest");
		vbn.setEvenementNomVariableLog("event_consult_courrier_log");
		vbn.setNomExpediteur("Administrator");
		vbn.setMailExpediteur("xtexte2@gmail.com");
		// ***Fin Log & notification***//
		List<Person> listPersonDest = new ArrayList<Person>();

		String expdest = consulterInformations.getCourrierExpediteur();
		Courrier courrierConsult = new Courrier();
		courrierConsult = consulterInformations.getCourrier();

		Date dateSystem = new Date();
		SimpleDateFormat formaterDate = null;
		formaterDate = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

		String infoCourrier = "La réference de ce courrier est : "

		+ courrierConsult.getCourrierReferenceCorrespondant();

		vbn.setInfo(infoCourrier);
		vbn.setNomClass(CourrierAjoutBean.class.getName());
		vbn.setTypeLog("INFO");
		Person p = new Person();
		p = ldapOperation.getUserByName(expdest);
		listPersonDest.add(p);

		// Chargement Hard Codé de les elements à remplir dans la classe
		// informations
		info1.setVar("#p");
		info1.setContenu(vb.getPerson().getNom());
		info2.setVar("#I");
		info2.setContenu(infoCourrier);
		info3.setVar("#d");
		info3.setContenu(formaterDate.format(dateSystem));
		listInfo.add(info1);
		listInfo.add(info2);
		listInfo.add(info3);
		vbn.setListInformations(listInfo);
		vbn.setTypeObject("TEST");
		vbn.setCopyListSelectedPersonNotif(listPersonDest);
		vbn.setPerson(vb.getPerson());
	}

	public void getSelectionRow() {
		System.out.println("Dans selected row");
		try {
			Transaction transaction = new Transaction();
			CourrierConsulterInformations consulterInformations = new CourrierConsulterInformations();
			consulterInformations = (CourrierConsulterInformations) listCourrier
					.getRowData();
			courrier = consulterInformations.getCourrier();
			vb.setCourrier(courrier);
	//		vb.setCourDossConsulterInformations(consulterInformations); //
			transaction = consulterInformations.getTransaction();
			if (vb.getPerson().isBoc()) {
				if (consulterInformations.getCourrierDestinataireReelle()
						.contains("(PP)")
						|| consulterInformations
								.getCourrierDestinataireReelle().contains(
										"(PM)")) {
					vb.setShowMonitoringButtonForDest(false);
				} else {
					vb.setShowMonitoringButtonForDest(true);
				}
				try {
					int index;
					if (transaction.getIdUtilisateur() != vb.getPerson()
							.getId()
							|| vb.getCopyListCourriersTousBocDepart().contains(
									consulterInformations)) {
						if (transaction.getIdUtilisateur() != vb.getPerson()
								.getId()) {
							TransactionDestination transactionDestination = new TransactionDestination();
							transactionDestination = consulterInformations
									.getTransactionDestination();
							if (transactionDestination
									.getTransactionDestDateConsultation() == null) {
								transactionDestination
										.setTransactionDestDateConsultation(new Date());
								appMgr.update(transactionDestination);
							}
							vb.setTransactionDestination(transactionDestination);
						} else {
							if (transaction.getTransactionDateConsultation() == null) {
								transaction
										.setTransactionDateConsultation(new Date());
								appMgr.update(transaction);
							}
						}
						Transmission transmission = new Transmission();
						transmission = consulterInformations.getCourrier()
								.getTransmission();
						if (transmission.getTransmissionLibelle().equals(
								"e-Mail")) {
							if (transaction.getExpdest().getExpdestexterne() == null) {
								if (transaction.getEtat().getEtatLibelle()
										.equals("Traité")) {
									index = vb
											.getCopyListCourriersMailBocDepartTraite()
											.indexOf(consulterInformations);
									vb.getCopyListCourriersMailBocDepartTraite()
											.get(index).setOpened(true);
									vb.getCopyListCourriersMailBocDepartTraite()
											.get(index).setNotOpened(false);
									index = vb
											.getCopyListCourriersTousBocDepartTraite()
											.indexOf(consulterInformations);
									vb.getCopyListCourriersTousBocDepartTraite()
											.get(index).setOpened(true);
									vb.getCopyListCourriersTousBocDepartTraite()
											.get(index).setNotOpened(false);
									index = vb
											.getCopyListCourriersMailBocTraite()
											.indexOf(consulterInformations);
									vb.getCopyListCourriersMailBocTraite()
											.get(index).setOpened(true);
									vb.getCopyListCourriersMailBocTraite()
											.get(index).setNotOpened(false);
									index = vb
											.getCopyListCourriersTousBocTraite()
											.indexOf(consulterInformations);
									vb.getCopyListCourriersTousBocTraite()
											.get(index).setOpened(true);
									vb.getCopyListCourriersTousBocTraite()
											.get(index).setNotOpened(false);
								} else {
									index = vb
											.getCopyListCourriersMailBocDepartNonTraite()
											.indexOf(consulterInformations);
									vb.getCopyListCourriersMailBocDepartNonTraite()
											.get(index).setOpened(true);
									vb.getCopyListCourriersMailBocDepartNonTraite()
											.get(index).setNotOpened(false);
									index = vb
											.getCopyListCourriersTousBocDepartNonTraite()
											.indexOf(consulterInformations);
									vb.getCopyListCourriersTousBocDepartNonTraite()
											.get(index).setOpened(true);
									vb.getCopyListCourriersTousBocDepartNonTraite()
											.get(index).setNotOpened(false);
									index = vb
											.getCopyListCourriersMailBocNonTraite()
											.indexOf(consulterInformations);
									vb.getCopyListCourriersMailBocNonTraite()
											.get(index).setOpened(true);
									vb.getCopyListCourriersMailBocNonTraite()
											.get(index).setNotOpened(false);
									index = vb
											.getCopyListCourriersTousBocNonTraite()
											.indexOf(consulterInformations);
									vb.getCopyListCourriersTousBocNonTraite()
											.get(index).setOpened(true);
									vb.getCopyListCourriersTousBocNonTraite()
											.get(index).setNotOpened(false);
								}
								index = vb.getCopyListCourriersMailBocDepart()
										.indexOf(consulterInformations);
								vb.getCopyListCourriersMailBocDepart()
										.get(index).setOpened(true);
								vb.getCopyListCourriersMailBocDepart()
										.get(index).setNotOpened(false);
								index = vb.getCopyListCourriersTousBocDepart()
										.indexOf(consulterInformations);
								vb.getCopyListCourriersTousBocDepart()
										.get(index).setOpened(true);
								vb.getCopyListCourriersTousBocDepart()
										.get(index).setNotOpened(false);
							}
							index = vb.getCopyListCourriersMailBoc().indexOf(
									consulterInformations);
							vb.getCopyListCourriersMailBoc().get(index)
									.setOpened(true);
							vb.getCopyListCourriersMailBoc().get(index)
									.setNotOpened(false);
						} else if (transmission.getTransmissionLibelle()
								.equals("Fax")) {
							if (transaction.getExpdest().getExpdestexterne() == null) {
								if (transaction.getEtat().getEtatLibelle()
										.equals("Traité")) {
									index = vb
											.getCopyListCourriersFaxBocDepartTraite()
											.indexOf(consulterInformations);
									vb.getCopyListCourriersFaxBocDepartTraite()
											.get(index).setOpened(true);
									vb.getCopyListCourriersFaxBocDepartTraite()
											.get(index).setNotOpened(false);
									index = vb
											.getCopyListCourriersTousBocDepartTraite()
											.indexOf(consulterInformations);
									vb.getCopyListCourriersTousBocDepartTraite()
											.get(index).setOpened(true);
									vb.getCopyListCourriersTousBocDepartTraite()
											.get(index).setNotOpened(false);
									index = vb
											.getCopyListCourriersFaxBocTraite()
											.indexOf(consulterInformations);
									vb.getCopyListCourriersFaxBocTraite()
											.get(index).setOpened(true);
									vb.getCopyListCourriersFaxBocTraite()
											.get(index).setNotOpened(false);
									index = vb
											.getCopyListCourriersTousBocTraite()
											.indexOf(consulterInformations);
									vb.getCopyListCourriersTousBocTraite()
											.get(index).setOpened(true);
									vb.getCopyListCourriersTousBocTraite()
											.get(index).setNotOpened(false);
								} else {
									index = vb
											.getCopyListCourriersFaxBocDepartNonTraite()
											.indexOf(consulterInformations);
									vb.getCopyListCourriersFaxBocDepartNonTraite()
											.get(index).setOpened(true);
									vb.getCopyListCourriersFaxBocDepartNonTraite()
											.get(index).setNotOpened(false);
									index = vb
											.getCopyListCourriersTousBocDepartNonTraite()
											.indexOf(consulterInformations);
									vb.getCopyListCourriersTousBocDepartNonTraite()
											.get(index).setOpened(true);
									vb.getCopyListCourriersTousBocDepartNonTraite()
											.get(index).setNotOpened(false);
									index = vb
											.getCopyListCourriersFaxBocNonTraite()
											.indexOf(consulterInformations);
									vb.getCopyListCourriersFaxBocNonTraite()
											.get(index).setOpened(true);
									vb.getCopyListCourriersFaxBocNonTraite()
											.get(index).setNotOpened(false);
									index = vb
											.getCopyListCourriersTousBocNonTraite()
											.indexOf(consulterInformations);
									vb.getCopyListCourriersTousBocNonTraite()
											.get(index).setOpened(true);
									vb.getCopyListCourriersTousBocNonTraite()
											.get(index).setNotOpened(false);
								}
								index = vb.getCopyListCourriersFaxBocDepart()
										.indexOf(consulterInformations);
								vb.getCopyListCourriersFaxBocDepart()
										.get(index).setOpened(true);
								vb.getCopyListCourriersFaxBocDepart()
										.get(index).setNotOpened(false);
								index = vb.getCopyListCourriersTousBocDepart()
										.indexOf(consulterInformations);
								vb.getCopyListCourriersTousBocDepart()
										.get(index).setOpened(true);
								vb.getCopyListCourriersTousBocDepart()
										.get(index).setNotOpened(false);
							}
							index = vb.getCopyListCourriersFaxBoc().indexOf(
									consulterInformations);
							vb.getCopyListCourriersFaxBoc().get(index)
									.setOpened(true);
							vb.getCopyListCourriersFaxBoc().get(index)
									.setNotOpened(false);
						} else {
							if (transaction.getExpdest().getExpdestexterne() == null) {
								if (transaction.getEtat().getEtatLibelle()
										.equals("Traité")) {
									index = vb
											.getCopyListCourriersAutreBocDepartTraite()
											.indexOf(consulterInformations);
									vb.getCopyListCourriersAutreBocDepartTraite()
											.get(index).setOpened(true);
									vb.getCopyListCourriersAutreBocDepartTraite()
											.get(index).setNotOpened(false);
									index = vb
											.getCopyListCourriersTousBocDepartTraite()
											.indexOf(consulterInformations);
									vb.getCopyListCourriersTousBocDepartTraite()
											.get(index).setOpened(true);
									vb.getCopyListCourriersTousBocDepartTraite()
											.get(index).setNotOpened(false);
									index = vb
											.getCopyListCourriersAutreBocTraite()
											.indexOf(consulterInformations);
									vb.getCopyListCourriersAutreBocTraite()
											.get(index).setOpened(true);
									vb.getCopyListCourriersAutreBocTraite()
											.get(index).setNotOpened(false);
									index = vb
											.getCopyListCourriersTousBocTraite()
											.indexOf(consulterInformations);
									vb.getCopyListCourriersTousBocTraite()
											.get(index).setOpened(true);
									vb.getCopyListCourriersTousBocTraite()
											.get(index).setNotOpened(false);
								} else {
									index = vb
											.getCopyListCourriersAutreBocDepartNonTraite()
											.indexOf(consulterInformations);
									vb.getCopyListCourriersAutreBocDepartNonTraite()
											.get(index).setOpened(true);
									vb.getCopyListCourriersAutreBocDepartNonTraite()
											.get(index).setNotOpened(false);
									index = vb
											.getCopyListCourriersTousBocDepartNonTraite()
											.indexOf(consulterInformations);
									vb.getCopyListCourriersTousBocDepartNonTraite()
											.get(index).setOpened(true);
									vb.getCopyListCourriersTousBocDepartNonTraite()
											.get(index).setNotOpened(false);
									index = vb
											.getCopyListCourriersAutreBocNonTraite()
											.indexOf(consulterInformations);
									vb.getCopyListCourriersAutreBocNonTraite()
											.get(index).setOpened(true);
									vb.getCopyListCourriersAutreBocNonTraite()
											.get(index).setNotOpened(false);
									index = vb
											.getCopyListCourriersTousBocNonTraite()
											.indexOf(consulterInformations);
									vb.getCopyListCourriersTousBocNonTraite()
											.get(index).setOpened(true);
									vb.getCopyListCourriersTousBocNonTraite()
											.get(index).setNotOpened(false);
								}
								index = vb.getCopyListCourriersAutreBocDepart()
										.indexOf(consulterInformations);
								vb.getCopyListCourriersAutreBocDepart()
										.get(index).setOpened(true);
								vb.getCopyListCourriersAutreBocDepart()
										.get(index).setNotOpened(false);
								index = vb.getCopyListCourriersTousBocDepart()
										.indexOf(consulterInformations);
								vb.getCopyListCourriersTousBocDepart()
										.get(index).setOpened(true);
								vb.getCopyListCourriersTousBocDepart()
										.get(index).setNotOpened(false);
							}
							index = vb.getCopyListCourriersAutreBoc().indexOf(
									consulterInformations);
							vb.getCopyListCourriersAutreBoc().get(index)
									.setOpened(true);
							vb.getCopyListCourriersAutreBoc().get(index)
									.setNotOpened(false);
						}
						index = vb.getCopyListCourriersTousBoc().indexOf(
								consulterInformations);
						vb.getCopyListCourriersTousBoc().get(index)
								.setOpened(true);
						vb.getCopyListCourriersTousBoc().get(index)
								.setNotOpened(false);
					} else {
						if (transaction.getTransactionDateConsultation() == null) {
							transaction
									.setTransactionDateConsultation(new Date());
							appMgr.update(transaction);
							Transmission transmission = new Transmission();
							transmission = consulterInformations.getCourrier()
									.getTransmission();
							if (transmission.getTransmissionLibelle().equals(
									"e-Mail")) {
								if (transaction.getExpdest()
										.getExpdestexterne() == null) {
									if (transaction.getEtat().getEtatLibelle()
											.equals("Traité")) {
										index = vb
												.getCopyListCourriersMailBocArriveTraite()
												.indexOf(consulterInformations);
										vb.getCopyListCourriersMailBocArriveTraite()
												.get(index).setOpened(true);
										vb.getCopyListCourriersMailBocArriveTraite()
												.get(index).setNotOpened(false);
										index = vb
												.getCopyListCourriersTousBocArriveTraite()
												.indexOf(consulterInformations);
										vb.getCopyListCourriersTousBocArriveTraite()
												.get(index).setOpened(true);
										vb.getCopyListCourriersTousBocArriveTraite()
												.get(index).setNotOpened(false);
										index = vb
												.getCopyListCourriersMailBocTraite()
												.indexOf(consulterInformations);
										vb.getCopyListCourriersMailBocTraite()
												.get(index).setOpened(true);
										vb.getCopyListCourriersMailBocTraite()
												.get(index).setNotOpened(false);
										index = vb
												.getCopyListCourriersTousBocTraite()
												.indexOf(consulterInformations);
										vb.getCopyListCourriersTousBocTraite()
												.get(index).setOpened(true);
										vb.getCopyListCourriersTousBocTraite()
												.get(index).setNotOpened(false);
									} else {
										index = vb
												.getCopyListCourriersMailBocArriveNonTraite()
												.indexOf(consulterInformations);
										vb.getCopyListCourriersMailBocArriveNonTraite()
												.get(index).setOpened(true);
										vb.getCopyListCourriersMailBocArriveNonTraite()
												.get(index).setNotOpened(false);
										index = vb
												.getCopyListCourriersTousBocArriveNonTraite()
												.indexOf(consulterInformations);
										vb.getCopyListCourriersTousBocArriveNonTraite()
												.get(index).setOpened(true);
										vb.getCopyListCourriersTousBocArriveNonTraite()
												.get(index).setNotOpened(false);
										index = vb
												.getCopyListCourriersMailBocNonTraite()
												.indexOf(consulterInformations);
										vb.getCopyListCourriersMailBocNonTraite()
												.get(index).setOpened(true);
										vb.getCopyListCourriersMailBocNonTraite()
												.get(index).setNotOpened(false);
										index = vb
												.getCopyListCourriersTousBocNonTraite()
												.indexOf(consulterInformations);
										vb.getCopyListCourriersTousBocNonTraite()
												.get(index).setOpened(true);
										vb.getCopyListCourriersTousBocNonTraite()
												.get(index).setNotOpened(false);
									}
									index = vb
											.getCopyListCourriersMailBocArrive()
											.indexOf(consulterInformations);
									vb.getCopyListCourriersMailBocArrive()
											.get(index).setOpened(true);
									vb.getCopyListCourriersMailBocArrive()
											.get(index).setNotOpened(false);
									index = vb
											.getCopyListCourriersTousBocArrive()
											.indexOf(consulterInformations);
									vb.getCopyListCourriersTousBocArrive()
											.get(index).setOpened(true);
									vb.getCopyListCourriersTousBocArrive()
											.get(index).setNotOpened(false);
								}
								index = vb.getCopyListCourriersMailBoc()
										.indexOf(consulterInformations);
								vb.getCopyListCourriersMailBoc().get(index)
										.setOpened(true);
								vb.getCopyListCourriersMailBoc().get(index)
										.setNotOpened(false);
							} else if (transmission.getTransmissionLibelle()
									.equals("Fax")) {
								if (transaction.getExpdest()
										.getExpdestexterne() == null) {
									if (transaction.getEtat().getEtatLibelle()
											.equals("Traité")) {
										index = vb
												.getCopyListCourriersFaxBocArriveTraite()
												.indexOf(consulterInformations);
										vb.getCopyListCourriersFaxBocArriveTraite()
												.get(index).setOpened(true);
										vb.getCopyListCourriersFaxBocArriveTraite()
												.get(index).setNotOpened(false);
										index = vb
												.getCopyListCourriersTousBocArriveTraite()
												.indexOf(consulterInformations);
										vb.getCopyListCourriersTousBocArriveTraite()
												.get(index).setOpened(true);
										vb.getCopyListCourriersTousBocArriveTraite()
												.get(index).setNotOpened(false);
										index = vb
												.getCopyListCourriersFaxBocTraite()
												.indexOf(consulterInformations);
										vb.getCopyListCourriersFaxBocTraite()
												.get(index).setOpened(true);
										vb.getCopyListCourriersFaxBocTraite()
												.get(index).setNotOpened(false);
										index = vb
												.getCopyListCourriersTousBocTraite()
												.indexOf(consulterInformations);
										vb.getCopyListCourriersTousBocTraite()
												.get(index).setOpened(true);
										vb.getCopyListCourriersTousBocTraite()
												.get(index).setNotOpened(false);
									} else {
										index = vb
												.getCopyListCourriersFaxBocArriveNonTraite()
												.indexOf(consulterInformations);
										vb.getCopyListCourriersFaxBocArriveNonTraite()
												.get(index).setOpened(true);
										vb.getCopyListCourriersFaxBocArriveNonTraite()
												.get(index).setNotOpened(false);
										index = vb
												.getCopyListCourriersTousBocArriveNonTraite()
												.indexOf(consulterInformations);
										vb.getCopyListCourriersTousBocArriveNonTraite()
												.get(index).setOpened(true);
										vb.getCopyListCourriersTousBocArriveNonTraite()
												.get(index).setNotOpened(false);
										index = vb
												.getCopyListCourriersFaxBocNonTraite()
												.indexOf(consulterInformations);
										vb.getCopyListCourriersFaxBocNonTraite()
												.get(index).setOpened(true);
										vb.getCopyListCourriersFaxBocNonTraite()
												.get(index).setNotOpened(false);
										index = vb
												.getCopyListCourriersTousBocNonTraite()
												.indexOf(consulterInformations);
										vb.getCopyListCourriersTousBocNonTraite()
												.get(index).setOpened(true);
										vb.getCopyListCourriersTousBocNonTraite()
												.get(index).setNotOpened(false);
									}
									index = vb
											.getCopyListCourriersFaxBocArrive()
											.indexOf(consulterInformations);
									vb.getCopyListCourriersFaxBocArrive()
											.get(index).setOpened(true);
									vb.getCopyListCourriersFaxBocArrive()
											.get(index).setNotOpened(false);
									index = vb
											.getCopyListCourriersTousBocArrive()
											.indexOf(consulterInformations);
									vb.getCopyListCourriersTousBocArrive()
											.get(index).setOpened(true);
									vb.getCopyListCourriersTousBocArrive()
											.get(index).setNotOpened(false);
								}
								index = vb.getCopyListCourriersFaxBoc()
										.indexOf(consulterInformations);
								vb.getCopyListCourriersFaxBoc().get(index)
										.setOpened(true);
								vb.getCopyListCourriersFaxBoc().get(index)
										.setNotOpened(false);
							} else {
								if (transaction.getExpdest()
										.getExpdestexterne() == null) {
									if (transaction.getEtat().getEtatLibelle()
											.equals("Traité")) {
										index = vb
												.getCopyListCourriersAutreBocArriveTraite()
												.indexOf(consulterInformations);
										vb.getCopyListCourriersAutreBocArriveTraite()
												.get(index).setOpened(true);
										vb.getCopyListCourriersAutreBocArriveTraite()
												.get(index).setNotOpened(false);
										index = vb
												.getCopyListCourriersTousBocArriveTraite()
												.indexOf(consulterInformations);
										vb.getCopyListCourriersTousBocArriveTraite()
												.get(index).setOpened(true);
										vb.getCopyListCourriersTousBocArriveTraite()
												.get(index).setNotOpened(false);
										index = vb
												.getCopyListCourriersAutreBocTraite()
												.indexOf(consulterInformations);
										vb.getCopyListCourriersAutreBocTraite()
												.get(index).setOpened(true);
										vb.getCopyListCourriersAutreBocTraite()
												.get(index).setNotOpened(false);
										index = vb
												.getCopyListCourriersTousBocTraite()
												.indexOf(consulterInformations);
										vb.getCopyListCourriersTousBocTraite()
												.get(index).setOpened(true);
										vb.getCopyListCourriersTousBocTraite()
												.get(index).setNotOpened(false);
									} else {
										index = vb
												.getCopyListCourriersAutreBocArriveNonTraite()
												.indexOf(consulterInformations);
										vb.getCopyListCourriersAutreBocArriveNonTraite()
												.get(index).setOpened(true);
										vb.getCopyListCourriersAutreBocArriveNonTraite()
												.get(index).setNotOpened(false);
										index = vb
												.getCopyListCourriersTousBocArriveNonTraite()
												.indexOf(consulterInformations);
										vb.getCopyListCourriersTousBocArriveNonTraite()
												.get(index).setOpened(true);
										vb.getCopyListCourriersTousBocArriveNonTraite()
												.get(index).setNotOpened(false);
										index = vb
												.getCopyListCourriersAutreBocNonTraite()
												.indexOf(consulterInformations);
										vb.getCopyListCourriersAutreBocNonTraite()
												.get(index).setOpened(true);
										vb.getCopyListCourriersAutreBocNonTraite()
												.get(index).setNotOpened(false);
										index = vb
												.getCopyListCourriersTousBocNonTraite()
												.indexOf(consulterInformations);
										vb.getCopyListCourriersTousBocNonTraite()
												.get(index).setOpened(true);
										vb.getCopyListCourriersTousBocNonTraite()
												.get(index).setNotOpened(false);
									}
									index = vb
											.getCopyListCourriersAutreBocArrive()
											.indexOf(consulterInformations);
									vb.getCopyListCourriersAutreBocArrive()
											.get(index).setOpened(true);
									vb.getCopyListCourriersAutreBocArrive()
											.get(index).setNotOpened(false);
									index = vb
											.getCopyListCourriersTousBocArrive()
											.indexOf(consulterInformations);
									vb.getCopyListCourriersTousBocArrive()
											.get(index).setOpened(true);
									vb.getCopyListCourriersTousBocArrive()
											.get(index).setNotOpened(false);
								}
								index = vb.getCopyListCourriersAutreBoc()
										.indexOf(consulterInformations);
								vb.getCopyListCourriersAutreBoc().get(index)
										.setOpened(true);
								vb.getCopyListCourriersAutreBoc().get(index)
										.setNotOpened(false);
							}
							index = vb.getCopyListCourriersTousBoc().indexOf(
									consulterInformations);
							vb.getCopyListCourriersTousBoc().get(index)
									.setOpened(true);
							vb.getCopyListCourriersTousBoc().get(index)
									.setNotOpened(false);
						}
					}
				} catch (NullPointerException e) {
				}
			} else {
				if (vb.getCopyListCourriersEnvoyes().contains(
						consulterInformations)
						|| transaction.getTransactionDestinationReelle() != null) {
					vb.setShowMonitoringButtonForDest(true);
				} else {
					vb.setShowMonitoringButtonForDest(false);
				}
				try {
					int index;
					if (vb.getCopyListCourriersRecus().contains(
							consulterInformations)
							&& consulterInformations
									.getTransactionDestination()
									.getTransactionDestDateConsultation() == null
							&& (consulterInformations.getTypeCourrier()
									.contains("Mes Propres Courriers") || consulterInformations
									.getTypeCourrier().contains(
											"Les Courriers de Mon Unité"))) {
						TransactionDestination transactionDestination = new TransactionDestination();
						transactionDestination = consulterInformations
								.getTransactionDestination();
						transactionDestination
								.setTransactionDestDateConsultation(new Date());
						appMgr.update(transactionDestination);
						vb.setTransactionDestination(transactionDestination);
						index = vb.getCopyListCourriersRecus().indexOf(
								consulterInformations);
						vb.getCopyListCourriersRecus().get(index)
								.setOpened(true);
						vb.getCopyListCourriersRecus().get(index)
								.setNotOpened(false);
						index = vb.getCopyListCourriersTous().indexOf(
								consulterInformations);
						vb.getCopyListCourriersTous().get(index)
								.setOpened(true);
						vb.getCopyListCourriersTous().get(index)
								.setNotOpened(false);
						// chargement variable log & notification
						chargementNotification(consulterInformations);

					} else if (vb.getCopyListCourriersEnvoyes().contains(
							consulterInformations)
							&& transaction.getTransactionDateConsultation() == null) {
						transaction.setTransactionDateConsultation(new Date());
						appMgr.update(transaction);
						index = vb.getCopyListCourriersEnvoyes().indexOf(
								consulterInformations);
						vb.getCopyListCourriersEnvoyes().get(index)
								.setOpened(true);
						vb.getCopyListCourriersEnvoyes().get(index)
								.setNotOpened(false);
						index = vb.getCopyListCourriersTous().indexOf(
								consulterInformations);
						vb.getCopyListCourriersTous().get(index)
								.setOpened(true);
						vb.getCopyListCourriersTous().get(index)
								.setNotOpened(false);
						// chargement variable log & notification
						// chargementNotification(consulterInformations);
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
			vb.setCopyDestNom(consulterInformations
					.getCourrierDestinataireReelle());
			vb.setCopyExpNom(consulterInformations.getCourrierExpediteur());
			vb.setCopyCourrierCommentaire(consulterInformations
					.getCourrierCommentaire());
			vb.setCopyOtherDest(consulterInformations
					.getCourrierAutreDestinataires());
			vb.setTransaction(transaction);
			// **
			int refDossier = transaction.getDossier().getDossierId();
			List<Transaction> listTransaction = appMgr
					.getTransactionByIdDossier(refDossier);
			Transaction firstTransaction = listTransaction.get(listTransaction
					.size() - 1);
			Expdest expDest;
			expDest = firstTransaction.getExpdest();
			if (expDest.getTypeExpDest().equals("Interne-Person")) {
				vb.setCopyExpReelNom(ldapOperation.getCnById(
						ldapOperation.CONTEXT_USER, "uid",
						expDest.getIdExpDestLdap()));
			} else if (expDest.getTypeExpDest().equals("Interne-Unité")) {
				// vb.setCopyExpReelNom(ldapOperation.getCnById(
				// ldapOperation.CONTEXT_UNIT, "departmentNumber",
				// expDest.getIdExpDestLdap()));
				vb.setCopyExpReelNom(ldapOperation.getUnitById(
						expDest.getIdExpDestLdap()).getShortNameUnit());

			} else if (expDest.getTypeExpDest().equals("Interne-Boc")) {
				vb.setCopyExpReelNom(ldapOperation.getBocById(
						expDest.getIdExpDestLdap()).getNameUnit());
			} else if (expDest.getTypeExpDest().equals("Externe")) {
				if (expDest.getExpdestexterne().getTypeutilisateur()
						.getTypeUtilisateurLibelle().equals("PP")) {
					vb.setCopyExpReelNom(expDest.getExpdestexterne()
							.getExpDestExterneNom()
							+ " "
							+ expDest.getExpdestexterne()
									.getExpDestExternePrenom() + " (PP)");
				} else {
					vb.setCopyExpReelNom(expDest.getExpdestexterne()
							.getExpDestExterneNom() + " (PM)");
				}
			}
			// **
//			if (consulterInformations.getTransaction()
//					.getTransactionDestinationReelle() != null) {
//				TransactionDestinationReelle transactionDestinationReelle = new TransactionDestinationReelle();
//				transactionDestinationReelle = consulterInformations
//						.getTransaction().getTransactionDestinationReelle();
//				Expdest expDest = new Expdest();
//				Transaction transaction1 = new Transaction();
//				transaction1 = appMgr
//						.getTransactionByIdTransactionAndFirstOrder(
//								transactionDestinationReelle
//										.getTransactionDestinationReelleId(),
//								1).get(0);
//				expDest = transaction1.getExpdest();
//				if (expDest.getTypeExpDest().equals("Interne-Person")) {
//					vb.setCopyExpReelNom(ldapOperation.getCnById(
//							ldapOperation.CONTEXT_USER, "uid",
//							expDest.getIdExpDestLdap()));
//				} else if (expDest.getTypeExpDest().equals("Interne-Unité")) {
//					vb.setCopyExpReelNom(ldapOperation.getCnById(
//							ldapOperation.CONTEXT_UNIT, "departmentNumber",
//							expDest.getIdExpDestLdap()));
//				}
//			} else {
//				vb.setCopyExpReelNom(vb.getCopyExpNom());
//			}
//			System.out.println("verif reel exp : " + vb.getCopyExpReelNom());
			List<TransactionAnnotation> annotations = new ArrayList<TransactionAnnotation>();
			annotations = appMgr
					.getAnnotationByIdTransaction(consulterInformations
							.getTransaction().getTransactionId());
			String annotationLibelle;
			int lastIndex;
			int refAnnotation;
			String result = "";
			for (TransactionAnnotation ta : annotations) {
				refAnnotation = ta.getId().getIdAnnotation();
				annotationLibelle = appMgr
						.getAnnotationByIdAnotation(refAnnotation).get(0)
						.getAnnotationLibelle();
				result = result + annotationLibelle + " / ";
			}
			if (!result.equals("")) {
				lastIndex = result.lastIndexOf("/");
				result = result.substring(0, lastIndex);
			}
			vb.setCopyAnnotationResult(result);

			LogClass logClass = new LogClass();
			logClass.addTrack(
					"consultation",
					"Evénement de log de consultation du courrier "
							+ courrier.getIdCourrier() + "-"
							+ courrier.getCourrierReferenceCorrespondant(),
					vb.getPerson(), "INFO", appMgr);
			System.out
					.println("*******SelectionSucces courrierConslterBean*********");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getSelectionRowDossier() {
		try {
			CourrierConsulterInformations consulterInformations = new CourrierConsulterInformations();
			consulterInformations = (CourrierConsulterInformations) listDossier
					.getRowData();
			// vb.setCourDossConsulterInformations(consulterInformations);   //
			dossier = consulterInformations.getDossier();
			vb.setDossier(dossier);
			vb.setCopyDestNom(consulterInformations
					.getCourrierDestinataireReelle());
			vb.setCopyExpNom(consulterInformations.getCourrierExpediteur());
			vb.setCopyOtherDest(consulterInformations
					.getCourrierAutreDestinataires());
			vb.setTransaction(consulterInformations.getTransaction());
			if (consulterInformations.getTransaction()
					.getTransactionDestinationReelle() != null) {
				TransactionDestinationReelle transactionDestinationReelle = new TransactionDestinationReelle();
				transactionDestinationReelle = consulterInformations
						.getTransaction().getTransactionDestinationReelle();
				Expdest expDest = new Expdest();
				Transaction transaction = new Transaction();
				transaction = appMgr
						.getTransactionByIdTransactionAndFirstOrder(
								transactionDestinationReelle
										.getTransactionDestinationReelleId(),
								1).get(0);
				expDest = transaction.getExpdest();
				if (expDest.getTypeExpDest().equals("Interne-Person")) {
					vb.setCopyExpReelNom(ldapOperation.getCnById(
							ldapOperation.CONTEXT_USER, "uid",
							expDest.getIdExpDestLdap()));
				} else if (expDest.getTypeExpDest().equals("Interne-Unité")) {
					vb.setCopyExpReelNom(ldapOperation.getCnById(
							ldapOperation.CONTEXT_UNIT, "departmentNumber",
							expDest.getIdExpDestLdap()));
				}
			} else {
				vb.setCopyExpReelNom(vb.getCopyExpNom());
			}
			List<TransactionAnnotation> annotations = new ArrayList<TransactionAnnotation>();
			annotations = appMgr
					.getAnnotationByIdTransaction(consulterInformations
							.getTransaction().getTransactionId());
			String annotationLibelle;
			int lastIndex;
			int refAnnotation;
			String result = "";
			for (TransactionAnnotation ta : annotations) {
				refAnnotation = ta.getId().getIdAnnotation();
				annotationLibelle = appMgr
						.getAnnotationByIdAnotation(refAnnotation).get(0)
						.getAnnotationLibelle();
				result = result + annotationLibelle + " / ";
			}
			if (!result.equals("")) {
				lastIndex = result.lastIndexOf("/");
				result = result.substring(0, lastIndex);
			}
			vb.setCopyAnnotationResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Get Selection Rows
	public void getSelectionRowJour() {
		try {
			Transaction transaction = new Transaction();
			CourrierConsulterInformations consulterInformations = new CourrierConsulterInformations();
			consulterInformations = (CourrierConsulterInformations) listCourrierJour
					.getRowData();
			// vb.setCourDossConsulterInformations(consulterInformations);   //
			courrier = consulterInformations.getCourrier();
			vb.setCourrier(courrier);
			transaction = consulterInformations.getTransaction();

			if (vb.getPerson().isBoc()) {
				if (consulterInformations.getCourrierDestinataireReelle()
						.contains("(PP)")
						|| consulterInformations
								.getCourrierDestinataireReelle().contains(
										"(PM)")) {
					vb.setShowMonitoringButtonForDest(false);
				} else {
					vb.setShowMonitoringButtonForDest(true);
				}
				try {
					if (transaction.getIdUtilisateur() != vb.getPerson()
							.getId()
							|| listCourriersTousBocDepartJour
									.contains(consulterInformations)) {
						if (transaction.getIdUtilisateur() != vb.getPerson()
								.getId()) {
							TransactionDestination transactionDestination = new TransactionDestination();
							transactionDestination = consulterInformations
									.getTransactionDestination();
							if (transactionDestination
									.getTransactionDestDateConsultation() == null) {
								transactionDestination
										.setTransactionDestDateConsultation(new Date());
								appMgr.update(transactionDestination);
							}
							vb.setTransactionDestination(transactionDestination);
						} else {
							if (transaction.getTransactionDateConsultation() == null) {
								transaction
										.setTransactionDateConsultation(new Date());
								appMgr.update(transaction);
							}
						}
					} else {
						if (transaction.getTransactionDateConsultation() == null) {
							transaction
									.setTransactionDateConsultation(new Date());
							appMgr.update(transaction);
						}
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
				}

			} else {
				if (listCourriersEnvoyesJour.contains(consulterInformations)
						|| transaction.getTransactionDestinationReelle() != null) {
					vb.setShowMonitoringButtonForDest(true);
				} else {
					vb.setShowMonitoringButtonForDest(false);
				}
				try {
					if (listCourriersRecusJour.contains(consulterInformations)
							&& consulterInformations
									.getTransactionDestination()
									.getTransactionDestDateConsultation() == null
							&& (consulterInformations.getTypeCourrier()
									.contains("Mes Propres Courriers") || consulterInformations
									.getTypeCourrier().contains(
											"Les Courriers de Mon Unité"))) {
						TransactionDestination transactionDestination = new TransactionDestination();
						transactionDestination = consulterInformations
								.getTransactionDestination();
						transactionDestination
								.setTransactionDestDateConsultation(new Date());
						appMgr.update(transactionDestination);
						vb.setTransactionDestination(transactionDestination);
						// chargement variable log & notification
						chargementNotification(consulterInformations);

					} else if (listCourriersEnvoyesJour
							.contains(consulterInformations)
							&& transaction.getTransactionDateConsultation() == null) {
						transaction.setTransactionDateConsultation(new Date());
						appMgr.update(transaction);
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
			vb.setCopyDestNom(consulterInformations
					.getCourrierDestinataireReelle());
			vb.setCopyExpNom(consulterInformations.getCourrierExpediteur());
			// vb.setCopyExpNom(consulterInformations.getcourrier)
			// vb.setCopyCourrierCommentaire(consulterInformations.getCourrierCommentaire());
			vb.setCopyCourrierCommentaire(consulterInformations.getCourrier()
					.getCourrierCommentaire());
			vb.setCopyOtherDest(consulterInformations
					.getCourrierAutreDestinataires());
			vb.setTransaction(transaction);
			// if (consulterInformations.getTransaction()
			// .getTransactionDestinationReelle() != null) {
			// TransactionDestinationReelle transactionDestinationReelle = new
			// TransactionDestinationReelle();
			// transactionDestinationReelle = consulterInformations
			// .getTransaction().getTransactionDestinationReelle();
			// **
			int refDossier = transaction.getDossier().getDossierId();
			List<Transaction> listTransaction = appMgr
					.getTransactionByIdDossier(refDossier);
			Transaction firstTransaction = listTransaction.get(listTransaction
					.size() - 1);
			Expdest expDest;
			// Transaction transaction1 = new Transaction();
			// transaction1 = appMgr
			// .getTransactionByIdTransactionAndFirstOrder(
			// transactionDestinationReelle
			// .getTransactionDestinationReelleId(),
			// 1).get(0);
			// expDest = transaction1.getExpdest();
			expDest = firstTransaction.getExpdest();
			if (expDest.getTypeExpDest().equals("Interne-Person")) {
				vb.setCopyExpReelNom(ldapOperation.getCnById(
						ldapOperation.CONTEXT_USER, "uid",
						expDest.getIdExpDestLdap()));
			} else if (expDest.getTypeExpDest().equals("Interne-Unité")) {
				// vb.setCopyExpReelNom(ldapOperation.getCnById(
				// ldapOperation.CONTEXT_UNIT, "departmentNumber",
				// expDest.getIdExpDestLdap()));
				vb.setCopyExpReelNom(ldapOperation.getUnitById(
						expDest.getIdExpDestLdap()).getShortNameUnit());

			} else if (expDest.getTypeExpDest().equals("Interne-Boc")) {
				vb.setCopyExpReelNom(ldapOperation.getBocById(
						expDest.getIdExpDestLdap()).getNameUnit());
			} else if (expDest.getTypeExpDest().equals("Externe")) {
				if (expDest.getExpdestexterne().getTypeutilisateur()
						.getTypeUtilisateurLibelle().equals("PP")) {
					vb.setCopyExpReelNom(expDest.getExpdestexterne()
							.getExpDestExterneNom()
							+ " "
							+ expDest.getExpdestexterne()
									.getExpDestExternePrenom() + " (PP)");
				} else {
					vb.setCopyExpReelNom(expDest.getExpdestexterne()
							.getExpDestExterneNom() + " (PM)");
				}
			}
			// **
			// } else {
			// vb.setCopyExpReelNom(vb.getCopyExpNom());
			// }
			// System.out.println("verif reel exp : " + vb.getCopyExpReelNom());
			List<TransactionAnnotation> annotations = new ArrayList<TransactionAnnotation>();
			annotations = appMgr
					.getAnnotationByIdTransaction(consulterInformations
							.getTransaction().getTransactionId());
			String annotationLibelle;
			int lastIndex;
			int refAnnotation;
			String result = "";
			for (TransactionAnnotation ta : annotations) {
				refAnnotation = ta.getId().getIdAnnotation();
				annotationLibelle = appMgr
						.getAnnotationByIdAnotation(refAnnotation).get(0)
						.getAnnotationLibelle();
				result = result + annotationLibelle + " / ";
			}
			if (!result.equals("")) {
				lastIndex = result.lastIndexOf("/");
				result = result.substring(0, lastIndex);
			}
			vb.setCopyAnnotationResult(result);
			LogClass logClass = new LogClass();
			logClass.addTrack(
					"consultation",
					"Evénement de log de consultation du courrier "
							+ courrier.getIdCourrier() + "-"
							+ courrier.getCourrierReferenceCorrespondant(),
					vb.getPerson(), "INFO", appMgr);
			System.out
					.println("*******SelectionSucces courrierConslterBeanJour*********");
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("*******ErreurDeSelection courrierConslterBeanJour*******");
		}
	}

	public void getSelectionRowDossierJour() {
		try {
			CourrierConsulterInformations consulterInformations = new CourrierConsulterInformations();
			consulterInformations = (CourrierConsulterInformations) listDossierJour
					.getRowData();
		//	vb.setCourDossConsulterInformations(consulterInformations);   //
			dossier = consulterInformations.getDossier();
			vb.setDossier(dossier);
			vb.setCopyDestNom(consulterInformations
					.getCourrierDestinataireReelle());
			vb.setCopyExpNom(consulterInformations.getCourrierExpediteur());
			vb.setCopyCourrierCommentaire(consulterInformations
					.getCourrierCommentaire());
			vb.setCopyOtherDest(consulterInformations
					.getCourrierAutreDestinataires());
			vb.setTransaction(consulterInformations.getTransaction());
			if (consulterInformations.getTransaction()
					.getTransactionDestinationReelle() != null) {
				TransactionDestinationReelle transactionDestinationReelle = new TransactionDestinationReelle();
				transactionDestinationReelle = consulterInformations
						.getTransaction().getTransactionDestinationReelle();
				Expdest expDest = new Expdest();
				Transaction transaction = new Transaction();
				transaction = appMgr
						.getTransactionByIdTransactionAndFirstOrder(
								transactionDestinationReelle
										.getTransactionDestinationReelleId(),
								1).get(0);
				expDest = transaction.getExpdest();
				if (expDest.getTypeExpDest().equals("Interne-Person")) {
					vb.setCopyExpReelNom(ldapOperation.getCnById(
							ldapOperation.CONTEXT_USER, "uid",
							expDest.getIdExpDestLdap()));
				} else if (expDest.getTypeExpDest().equals("Interne-Unité")) {
					vb.setCopyExpReelNom(ldapOperation.getCnById(
							ldapOperation.CONTEXT_UNIT, "departmentNumber",
							expDest.getIdExpDestLdap()));
				}
			} else {
				vb.setCopyExpReelNom(vb.getCopyExpNom());
			}

			List<TransactionAnnotation> annotations = new ArrayList<TransactionAnnotation>();
			annotations = appMgr
					.getAnnotationByIdTransaction(consulterInformations
							.getTransaction().getTransactionId());
			String annotationLibelle;
			int lastIndex;
			int refAnnotation;
			String result = "";
			for (TransactionAnnotation ta : annotations) {
				refAnnotation = ta.getId().getIdAnnotation();
				annotationLibelle = appMgr
						.getAnnotationByIdAnotation(refAnnotation).get(0)
						.getAnnotationLibelle();
				result = result + annotationLibelle + " / ";
			}
			if (!result.equals("")) {
				lastIndex = result.lastIndexOf("/");
				result = result.substring(0, lastIndex);
			}
			vb.setCopyAnnotationResult(result);
			LogClass logClass = new LogClass();
			logClass.addTrack(
					"consultation",
					"Evénement de log de consultation du dossier "
							+ dossier.getDossierId() + "-"
							+ dossier.getDossierReference(), vb.getPerson(),
					"INFO", appMgr);
			System.out
					.println("*******SelectionSucces courrierConslterBeanJour*********");
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("*******ErreurDeSelection courrierConslterBeanJour*******");
		}
	}

	public void getSelectionRowJourForResponse() {
		try {
			Transaction transaction = new Transaction();
			CourrierConsulterInformations consulterInformations = new CourrierConsulterInformations();
			consulterInformations = (CourrierConsulterInformations) listCourrierJour
					.getRowData();
			courrier = consulterInformations.getCourrier();
//			vb.setCourDossConsulterInformations(consulterInformations);   //
			vb.setCourrier(courrier);
			transaction = consulterInformations.getTransaction();
			if (listCourriersRecusJour.contains(consulterInformations)
					&& consulterInformations.getTransactionDestination()
							.getTransactionDestDateConsultation() == null) {
				TransactionDestination transactionDestination = new TransactionDestination();
				transactionDestination = consulterInformations
						.getTransactionDestination();
				transactionDestination
						.setTransactionDestDateConsultation(new Date());
				appMgr.update(transactionDestination);
				vb.setTransactionDestination(transactionDestination);
			} else if (listCourriersEnvoyesJour.contains(consulterInformations)
					&& transaction.getTransactionDateConsultation() == null) {
				transaction.setTransactionDateConsultation(new Date());
				appMgr.update(transaction);
			}
			if (consulterInformations.getCourrierExpediteurObjet() instanceof Person) {
				Person person = new Person();
				person = (Person) consulterInformations
						.getCourrierExpediteurObjet();
				vb.setCopyListSelectedPerson(new ArrayList<Person>());
				vb.getCopyListSelectedPerson().add(
						ldapOperation.getPersonalisedUserById(person.getId()));
				vb.setDestNom(person.getCn());
			} else if (consulterInformations.getCourrierExpediteurObjet() instanceof Unit) {
				Unit unit = new Unit();
				unit = (Unit) consulterInformations
						.getCourrierExpediteurObjet();
				vb.setCopyListSelectedUnit(new ArrayList<Unit>());
				vb.getCopyListSelectedUnit().add(unit);
				vb.setDestNom(unit.getNameUnit());
			}
			vb.setCopyDestNom(consulterInformations
					.getCourrierDestinataireReelle());
			vb.setCopyExpNom(consulterInformations.getCourrierExpediteur());
			vb.setCopyCourrierCommentaire(consulterInformations
					.getCourrierCommentaire());
			vb.setToReplay(true);
			System.out
					.println("*******SelectionSucces courrierConslterBeanJour*********");
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("*******ErreurDeSelection courrierConslterBeanJour*******");
		}
	}

	public void getSelectionRowJourDossierForResponse() {
		try {
			CourrierConsulterInformations consulterInformations = new CourrierConsulterInformations();
			consulterInformations = (CourrierConsulterInformations) listDossierJour
					.getRowData();
			// vb.setCourDossConsulterInformations(consulterInformations);  //
			dossier = consulterInformations.getDossier();
			vb.setDossier(dossier);
			if (consulterInformations.getCourrierExpediteurObjet() instanceof Person) {
				Person person = new Person();
				person = (Person) consulterInformations
						.getCourrierExpediteurObjet();
				vb.setCopyListSelectedPerson(new ArrayList<Person>());
				vb.getCopyListSelectedPerson().add(person);
				vb.setDestNom(person.getCn());
			} else if (consulterInformations.getCourrierExpediteurObjet() instanceof Unit) {
				Unit unit = new Unit();
				unit = (Unit) consulterInformations
						.getCourrierExpediteurObjet();
				vb.setCopyListSelectedUnit(new ArrayList<Unit>());
				vb.getCopyListSelectedUnit().add(unit);
				vb.setDestNom(unit.getNameUnit());
			}
			vb.setCopyDestNom(consulterInformations
					.getCourrierDestinataireReelle());
			vb.setCopyExpNom(consulterInformations.getCourrierExpediteur());
			vb.setCopyCourrierCommentaire(consulterInformations
					.getCourrierCommentaire());
			vb.setToReplay(true);
			System.out
					.println("*******SelectionSucces courrierConslterBeanJour*********");
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("*******ErreurDeSelection courrierConslterBeanJour*******");
		}
	}

	public void getSelectionRowForResponse() {
		try {
			CourrierConsulterInformations consulterInformations = new CourrierConsulterInformations();
			consulterInformations = (CourrierConsulterInformations) listCourrier
					.getRowData();
		//	vb.setCourDossConsulterInformations(consulterInformations);  //
			courrier = consulterInformations.getCourrier();
			vb.setCourrier(courrier);
			Transaction transaction = new Transaction();
			transaction = consulterInformations.getTransaction();
			int index;
			if (vb.getCopyListCourriersRecus().contains(consulterInformations)
					&& consulterInformations.getTransactionDestination()
							.getTransactionDestDateConsultation() == null) {
				TransactionDestination transactionDestination = new TransactionDestination();
				transactionDestination = consulterInformations
						.getTransactionDestination();
				transactionDestination
						.setTransactionDestDateConsultation(new Date());
				appMgr.update(transactionDestination);
				vb.setTransactionDestination(transactionDestination);
				index = vb.getCopyListCourriersRecus().indexOf(
						consulterInformations);
				vb.getCopyListCourriersRecus().get(index).setOpened(true);
				vb.getCopyListCourriersRecus().get(index).setNotOpened(false);
				index = vb.getCopyListCourriersTous().indexOf(
						consulterInformations);
				vb.getCopyListCourriersTous().get(index).setOpened(true);
				vb.getCopyListCourriersTous().get(index).setNotOpened(false);
			} else if (vb.getCopyListCourriersEnvoyes().contains(
					consulterInformations)
					&& transaction.getTransactionDateConsultation() == null) {
				transaction.setTransactionDateConsultation(new Date());
				appMgr.update(transaction);
				index = vb.getCopyListCourriersEnvoyes().indexOf(
						consulterInformations);
				vb.getCopyListCourriersEnvoyes().get(index).setOpened(true);
				vb.getCopyListCourriersEnvoyes().get(index).setNotOpened(false);
				index = vb.getCopyListCourriersTous().indexOf(
						consulterInformations);
				vb.getCopyListCourriersTous().get(index).setOpened(true);
				vb.getCopyListCourriersTous().get(index).setNotOpened(false);
			}
			if (consulterInformations.getCourrierExpediteurObjet() instanceof Person) {
				Person person = new Person();
				person = (Person) consulterInformations
						.getCourrierExpediteurObjet();
				vb.setCopyListSelectedPerson(new ArrayList<Person>());
				vb.getCopyListSelectedPerson().add(
						ldapOperation.getPersonalisedUserById(person.getId()));
				vb.setDestNom(person.getCn());
			} else if (consulterInformations.getCourrierExpediteurObjet() instanceof Unit) {
				Unit unit = new Unit();
				unit = (Unit) consulterInformations
						.getCourrierExpediteurObjet();
				vb.setCopyListSelectedUnit(new ArrayList<Unit>());
				vb.getCopyListSelectedUnit().add(unit);
				vb.setDestNom(unit.getNameUnit());
			}
			vb.setCopyDestNom(consulterInformations
					.getCourrierDestinataireReelle());
			vb.setCopyExpNom(consulterInformations.getCourrierExpediteur());
			vb.setCopyCourrierCommentaire(consulterInformations
					.getCourrierCommentaire());
			vb.setToReplay(true);
			LogClass logClass = new LogClass();
			logClass.addTrack(
					"consultation",
					"Evénement de log de consultation du courrier "
							+ courrier.getIdCourrier() + "-"
							+ courrier.getCourrierReferenceCorrespondant(),
					vb.getPerson(), "INFO", appMgr);

			System.out
					.println("*******SelectionSucces courrierConslterBeanJour*********");
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("*******ErreurDeSelection courrierConslterBeanJour*******");
		}
	}

	public void getSelectionRowDossierForResponse() {
		try {
			CourrierConsulterInformations consulterInformations = new CourrierConsulterInformations();
			consulterInformations = (CourrierConsulterInformations) listDossier
					.getRowData();
		//	vb.setCourDossConsulterInformations(consulterInformations);   //
			dossier = consulterInformations.getDossier();
			vb.setDossier(dossier);
			if (consulterInformations.getCourrierExpediteurObjet() instanceof Person) {
				Person person = new Person();
				person = (Person) consulterInformations
						.getCourrierExpediteurObjet();
				vb.setCopyListSelectedPerson(new ArrayList<Person>());
				vb.getCopyListSelectedPerson().add(person);
				vb.setDestNom(person.getCn());
			} else if (consulterInformations.getCourrierExpediteurObjet() instanceof Unit) {
				Unit unit = new Unit();
				unit = (Unit) consulterInformations
						.getCourrierExpediteurObjet();
				vb.setCopyListSelectedUnit(new ArrayList<Unit>());
				vb.getCopyListSelectedUnit().add(unit);
				vb.setDestNom(unit.getNameUnit());
			}
			vb.setCopyDestNom(consulterInformations
					.getCourrierDestinataireReelle());
			vb.setCopyExpNom(consulterInformations.getCourrierExpediteur());
			vb.setCopyCourrierCommentaire(consulterInformations
					.getCourrierCommentaire());
			vb.setToReplay(true);
			LogClass logClass = new LogClass();
			logClass.addTrack(
					"consultation",
					"Evénement de log de consultation du dossier "
							+ dossier.getDossierId() + "-"
							+ dossier.getDossierReference(), vb.getPerson(),
					"INFO", appMgr);

			System.out
					.println("*******SelectionSucces courrierConslterBeanJour*********");
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("*******ErreurDeSelection courrierConslterBeanJour*******");
		}
	}

	public void getSelectionRowJourForValidate() {
		try {
			Transaction transaction = new Transaction();
			CourrierConsulterInformations consulterInformations = new CourrierConsulterInformations();
			consulterInformations = (CourrierConsulterInformations) listCourrierJour
					.getRowData();
			vb.setConsulterInformations(consulterInformations);
			vb.setCourrier(consulterInformations.getCourrier());
			transaction = consulterInformations.getTransaction();
			if (listCourriersRecusJour.contains(consulterInformations)
					&& consulterInformations.getTransactionDestination()
							.getTransactionDestDateConsultation() == null) {
				TransactionDestination transactionDestination = new TransactionDestination();
				transactionDestination = consulterInformations
						.getTransactionDestination();
				transactionDestination
						.setTransactionDestDateConsultation(new Date());
				appMgr.update(transactionDestination);
				vb.setTransactionDestination(transactionDestination);
			} else if (listCourriersEnvoyesJour.contains(consulterInformations)
					&& transaction.getTransactionDateConsultation() == null) {
				transaction.setTransactionDateConsultation(new Date());
				appMgr.update(transaction);
			}
			// **
			int refDossier = transaction.getDossier().getDossierId();
			List<Transaction> listTransaction = appMgr
					.getTransactionByIdDossier(refDossier);
			Transaction firstTransaction = listTransaction.get(listTransaction
					.size() - 1);
			Expdest expDest;
			expDest = firstTransaction.getExpdest();
			if (expDest.getTypeExpDest().equals("Interne-Person")) {
				vb.setCopyExpReelNom(ldapOperation.getCnById(
						ldapOperation.CONTEXT_USER, "uid",
						expDest.getIdExpDestLdap()));
			} else if (expDest.getTypeExpDest().equals("Interne-Unité")) {
				
				vb.setCopyExpReelNom(ldapOperation.getUnitById(
						expDest.getIdExpDestLdap()).getShortNameUnit());

			} else if (expDest.getTypeExpDest().equals("Interne-Boc")) {
				vb.setCopyExpReelNom(ldapOperation.getBocById(
						expDest.getIdExpDestLdap()).getNameUnit());
			} else if (expDest.getTypeExpDest().equals("Externe")) {
				if (expDest.getExpdestexterne().getTypeutilisateur()
						.getTypeUtilisateurLibelle().equals("PP")) {
					vb.setCopyExpReelNom(expDest.getExpdestexterne()
							.getExpDestExterneNom()
							+ " "
							+ expDest.getExpdestexterne()
									.getExpDestExternePrenom() + " (PP)");
				} else {
					vb.setCopyExpReelNom(expDest.getExpdestexterne()
							.getExpDestExterneNom() + " (PM)");
				}
			}
//			if (consulterInformations.getTransaction()
//					.getTransactionDestinationReelle() != null) {
//				TransactionDestinationReelle transactionDestinationReelle = new TransactionDestinationReelle();
//				transactionDestinationReelle = consulterInformations
//						.getTransaction().getTransactionDestinationReelle();
//				Expdest expDest = new Expdest();
//				Transaction transaction1 = new Transaction();
//				transaction1 = appMgr
//						.getTransactionByIdTransactionAndFirstOrder(
//								transactionDestinationReelle
//										.getTransactionDestinationReelleId(),
//								1).get(0);
//				expDest = transaction1.getExpdest();
//				if (expDest.getTypeExpDest().equals("Interne-Person")) {
//					vb.setCopyExpReelNom(ldapOperation.getCnById(
//							ldapOperation.CONTEXT_USER, "uid",
//							expDest.getIdExpDestLdap()));
//				} else if (expDest.getTypeExpDest().equals("Interne-Unité")) {
//					vb.setCopyExpReelNom(ldapOperation.getCnById(
//							ldapOperation.CONTEXT_UNIT, "departmentNumber",
//							expDest.getIdExpDestLdap()));
//				}
//			} else {
//				vb.setCopyExpReelNom(vb.getCopyExpNom());
//			}
			vb.setCopyDestNom(consulterInformations
					.getCourrierDestinataireReelle());
			vb.setCopyExpNom(consulterInformations.getCourrierExpediteur());
			// vb.setCopyCourrierCommentaire(consulterInformations.getCourrierCommentaire());
			vb.setCopyCourrierCommentaire(consulterInformations.getCourrier()
					.getCourrierCommentaire());
			vb.setCopyOtherDest(consulterInformations
					.getCourrierAutreDestinataires());
			vb.setTransaction(transaction);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void getSelectionRowForValidate() {
		try {
			CourrierConsulterInformations consulterInformations = new CourrierConsulterInformations();
			consulterInformations = (CourrierConsulterInformations) listCourrier
					.getRowData();
			vb.setConsulterInformations(consulterInformations);
			vb.setCourrier(consulterInformations.getCourrier());
			Transaction transaction = new Transaction();
			transaction = consulterInformations.getTransaction();
			int index;
			if (vb.getCopyListCourriersRecus().contains(consulterInformations)
					&& consulterInformations.getTransactionDestination()
							.getTransactionDestDateConsultation() == null) {
				TransactionDestination transactionDestination = new TransactionDestination();
				transactionDestination = consulterInformations
						.getTransactionDestination();
				transactionDestination
						.setTransactionDestDateConsultation(new Date());
				appMgr.update(transactionDestination);
				vb.setTransactionDestination(transactionDestination);
				index = vb.getCopyListCourriersRecus().indexOf(
						consulterInformations);
				vb.getCopyListCourriersRecus().get(index).setOpened(true);
				vb.getCopyListCourriersRecus().get(index).setNotOpened(false);
				index = vb.getCopyListCourriersTous().indexOf(
						consulterInformations);
				vb.getCopyListCourriersTous().get(index).setOpened(true);
				vb.getCopyListCourriersTous().get(index).setNotOpened(false);
			} else if (vb.getCopyListCourriersEnvoyes().contains(
					consulterInformations)
					&& transaction.getTransactionDateConsultation() == null) {
				transaction.setTransactionDateConsultation(new Date());
				appMgr.update(transaction);
				index = vb.getCopyListCourriersEnvoyes().indexOf(
						consulterInformations);
				vb.getCopyListCourriersEnvoyes().get(index).setOpened(true);
				vb.getCopyListCourriersEnvoyes().get(index).setNotOpened(false);
				index = vb.getCopyListCourriersTous().indexOf(
						consulterInformations);
				vb.getCopyListCourriersTous().get(index).setOpened(true);
				vb.getCopyListCourriersTous().get(index).setNotOpened(false);
			}
			// **
			int refDossier = transaction.getDossier().getDossierId();
			List<Transaction> listTransaction = appMgr
					.getTransactionByIdDossier(refDossier);
			Transaction firstTransaction = listTransaction.get(listTransaction
					.size() - 1);
			Expdest expDest;
			// if (consulterInformations.getTransaction()
			// .getTransactionDestinationReelle() != null) {
			// TransactionDestinationReelle transactionDestinationReelle = new
			// TransactionDestinationReelle();
			// transactionDestinationReelle = consulterInformations
			// .getTransaction().getTransactionDestinationReelle();
			// Expdest expDest = new Expdest();
			// Transaction transaction1 = new Transaction();
			// transaction1 = appMgr
			// .getTransactionByIdTransactionAndFirstOrder(
			// transactionDestinationReelle
			// .getTransactionDestinationReelleId(),
			// 1).get(0);
			// expDest = transaction1.getExpdest();
			expDest = firstTransaction.getExpdest();
			if (expDest.getTypeExpDest().equals("Interne-Person")) {
				vb.setCopyExpReelNom(ldapOperation.getCnById(
						ldapOperation.CONTEXT_USER, "uid",
						expDest.getIdExpDestLdap()));
			} else if (expDest.getTypeExpDest().equals("Interne-Unité")) {
				// vb.setCopyExpReelNom(ldapOperation.getCnById(
				// ldapOperation.CONTEXT_UNIT, "departmentNumber",
				// expDest.getIdExpDestLdap()));
				vb.setCopyExpReelNom(ldapOperation.getUnitById(
						expDest.getIdExpDestLdap()).getShortNameUnit());

			} else if (expDest.getTypeExpDest().equals("Interne-Boc")) {
				vb.setCopyExpReelNom(ldapOperation.getBocById(
						expDest.getIdExpDestLdap()).getNameUnit());
			} else if (expDest.getTypeExpDest().equals("Externe")) {
				if (expDest.getExpdestexterne().getTypeutilisateur()
						.getTypeUtilisateurLibelle().equals("PP")) {
					vb.setCopyExpReelNom(expDest.getExpdestexterne()
							.getExpDestExterneNom()
							+ " "
							+ expDest.getExpdestexterne()
									.getExpDestExternePrenom() + " (PP)");
				} else {
					vb.setCopyExpReelNom(expDest.getExpdestexterne()
							.getExpDestExterneNom() + " (PM)");
				}
			}
			// **
			// if (expDest.getTypeExpDest().equals("Interne-Person")) {
			// vb.setCopyExpReelNom(ldapOperation.getCnById(
			// ldapOperation.CONTEXT_USER, "uid",
			// expDest.getIdExpDestLdap()));
			// } else if (expDest.getTypeExpDest().equals("Interne-Unité")) {
			// vb.setCopyExpReelNom(ldapOperation.getCnById(
			// ldapOperation.CONTEXT_UNIT, "departmentNumber",
			// expDest.getIdExpDestLdap()));
			// }
			// } else {
			// vb.setCopyExpReelNom(vb.getCopyExpNom());
			// }

			vb.setCopyDestNom(consulterInformations
					.getCourrierDestinataireReelle());
			vb.setCopyExpNom(consulterInformations.getCourrierExpediteur());
			// vb.setCopyCourrierCommentaire(consulterInformations.getCourrierCommentaire());
			vb.setCopyCourrierCommentaire(consulterInformations.getCourrier()
					.getCourrierCommentaire());
			vb.setCopyOtherDest(consulterInformations
					.getCourrierAutreDestinataires());
			vb.setTransaction(transaction);
			System.out
					.println("*******SelectionSucces courrierConslterBeanJour*********");
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("*******ErreurDeSelection courrierConslterBeanJour*******");
		}
	}

	public void getSelectionRowDossierForSharedJour() {
		CourrierConsulterInformations consulterInformations = new CourrierConsulterInformations();
		consulterInformations = (CourrierConsulterInformations) listDossierJour
				.getRowData();
	   //	vb.setCourDossConsulterInformations(consulterInformations);   //
		dossier = consulterInformations.getDossier();
		vb.setDossier(dossier);
	}

	public void getSelectionRowDossierForShared() {
		CourrierConsulterInformations consulterInformations = new CourrierConsulterInformations();
		consulterInformations = (CourrierConsulterInformations) listDossier
				.getRowData();
	//	vb.setCourDossConsulterInformations(consulterInformations); //
		dossier = consulterInformations.getDossier();
		vb.setDossier(dossier);
	}

	public void getSelectionRowDossierForDetailsSharedJour() {
		CourrierConsulterInformations consulterInformations = new CourrierConsulterInformations();
		consulterInformations = (CourrierConsulterInformations) listDossierJour
				.getRowData();
	//	vb.setCourDossConsulterInformations(consulterInformations); //
		dossier = consulterInformations.getDossier();
		vb.setDossier(dossier);
		vb.setTransaction(consulterInformations.getTransaction());
	}

	public void getSelectionRowDossierForDetailsShared() {
		CourrierConsulterInformations consulterInformations = new CourrierConsulterInformations();
		consulterInformations = (CourrierConsulterInformations) listDossier
				.getRowData();
	//	vb.setCourDossConsulterInformations(consulterInformations);  //
		dossier = consulterInformations.getDossier();
		vb.setDossier(dossier);
		vb.setTransaction(consulterInformations.getTransaction());
	}

	// fonction de filtrage des date dans le tableau
	public boolean filterDateRecep(Object current) {
		if (dateReception == null) {
			return true;
		}
		Courrier courrier1 = (Courrier) current;
		return dateReception.equals(courrier1.getCourrierDateReception());
	}

	public boolean filterDateRep(Object current) {

		if (dateReponse == null) {
			return true;
		}
		Courrier courrier1 = (Courrier) current;
		return dateReponse.equals(courrier1.getCourrierDateReponse());
	}

	// ************Getter & Setter********************
	@SuppressWarnings("unchecked")
	public long getRecords() {
		if (listCourrier == null && listCourrier.getWrappedData() == null)
			records = 0;
		else
			records = ((List<CourrierConsulterInformations>) listCourrier
					.getWrappedData()).size();
		return records;
	}

	@SuppressWarnings("unchecked")
	public long getRecordsJour() {
		if (listCourrierJour == null
				&& listCourrierJour.getWrappedData() == null)
			recordsJour = 0;
		else
			recordsJour = ((List<CourrierConsulterInformations>) listCourrierJour
					.getWrappedData()).size();
		return recordsJour;
	}

	@SuppressWarnings("unchecked")
	public long getRecordsDossier() {
		if (listDossier == null && listDossier.getWrappedData() == null)
			recordsDossier = 0;
		else
			recordsDossier = ((List<CourrierConsulterInformations>) listDossier
					.getWrappedData()).size();
		return recordsDossier;
	}

	@SuppressWarnings("unchecked")
	public long getRecordsDossierJour() {
		if (listDossierJour == null && listDossierJour.getWrappedData() == null)
			recordsDossierJour = 0;
		else
			recordsDossierJour = ((List<CourrierConsulterInformations>) listDossierJour
					.getWrappedData()).size();
		return recordsDossierJour;
	}

	public VariableGlobale getVb() {
		return vb;
	}

	public void setVb(VariableGlobale vb) {
		this.vb = vb;
	}

	public Export getExport() {
		return export;
	}

	public void setExport(Export export) {
		this.export = export;
	}

	public DataModel getListCourrier() {
		return listCourrier;
	}

	public void setListCourrier(DataModel listCourrier) {
		this.listCourrier = listCourrier;
	}

	public Courrier getCourrier() {
		return courrier;
	}

	public void setCourrier(Courrier courrier) {
		this.courrier = courrier;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public TransactionDestination getTransactionDestination() {
		return transactionDestination;
	}

	public void setTransactionDestination(
			TransactionDestination transactionDestination) {
		this.transactionDestination = transactionDestination;
	}

	public TransactionAnnotation getTransactionAnnotation() {
		return transactionAnnotation;
	}

	public void setTransactionAnnotation(
			TransactionAnnotation transactionAnnotation) {
		this.transactionAnnotation = transactionAnnotation;
	}

	public Nature getNature() {
		return nature;
	}

	public void setNature(Nature nature) {
		this.nature = nature;
	}

	public Transmission getTransmission() {
		return transmission;
	}

	public void setTransmission(Transmission transmission) {
		this.transmission = transmission;
	}

	public Confidentialite getConfidentialite() {
		return confidentialite;
	}

	public void setConfidentialite(Confidentialite confidentialite) {
		this.confidentialite = confidentialite;
	}

	public Expdestexterne getExpdestexterne() {
		return expdestexterne;
	}

	public void setExpdestexterne(Expdestexterne expdestexterne) {
		this.expdestexterne = expdestexterne;
	}

	public List<Expdestexterne> getListDestExpdestexternes() {
		return listDestExpdestexternes;
	}

	public void setListDestExpdestexternes(
			List<Expdestexterne> listDestExpdestexternes) {
		this.listDestExpdestexternes = listDestExpdestexternes;
	}

	public LanguageManagerBean getLm() {
		return lm;
	}

	public void setLm(LanguageManagerBean lm) {
		this.lm = lm;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Urgence getUrgence() {
		return urgence;
	}

	public void setUrgence(Urgence urgence) {
		this.urgence = urgence;
	}

	public Date getDateReception() {
		return dateReception;
	}

	public void setDateReception(Date dateReception) {
		this.dateReception = dateReception;
	}

	public Date getDateReponse() {
		return dateReponse;
	}

	public void setDateReponse(Date dateReponse) {
		this.dateReponse = dateReponse;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setDossier(Dossier dossier) {
		this.dossier = dossier;
	}

	public Dossier getDossier() {
		return dossier;
	}

	public void setCourrierConsulterInformations(
			List<CourrierConsulterInformations> courrierConsulterInformations) {
		this.courrierConsulterInformations = courrierConsulterInformations;
	}

	public List<CourrierConsulterInformations> getCourrierConsulterInformations() {
		return courrierConsulterInformations;
	}

	public void setListCourrierJour(DataModel listCourrierJour) {
		this.listCourrierJour = listCourrierJour;
	}

	public DataModel getListCourrierJour() {
		return listCourrierJour;
	}

	public void setListcourrierConsulterInformationsJour(
			List<CourrierConsulterInformations> listcourrierConsulterInformationsJour) {
		this.listcourrierConsulterInformationsJour = listcourrierConsulterInformationsJour;
	}

	public List<CourrierConsulterInformations> getListcourrierConsulterInformationsJour() {
		return listcourrierConsulterInformationsJour;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getSurname() {
		return surname;
	}

	public void setListCourriersRecus1(
			List<CourrierConsulterInformations> listCourriersRecus) {
		this.listCourriersRecus = listCourriersRecus;
	}

	public List<CourrierConsulterInformations> getListCourriersRecus() {
		return listCourriersRecus;
	}

	public void setListCourriersEnvoyes(
			List<CourrierConsulterInformations> listCourriersEnvoyes) {
		this.listCourriersEnvoyes = listCourriersEnvoyes;
	}

	public List<CourrierConsulterInformations> getListCourriersEnvoyes() {
		return listCourriersEnvoyes;
	}

	public void setListCourriersRecusJour(
			List<CourrierConsulterInformations> listCourriersRecusJour) {
		this.listCourriersRecusJour = listCourriersRecusJour;
	}

	public List<CourrierConsulterInformations> getListCourriersRecusJour() {
		return listCourriersRecusJour;
	}

	public void setListCourriersEnvoyesJour(
			List<CourrierConsulterInformations> listCourriersEnvoyesJour) {
		this.listCourriersEnvoyesJour = listCourriersEnvoyesJour;
	}

	public List<CourrierConsulterInformations> getListCourriersEnvoyesJour() {
		return listCourriersEnvoyesJour;
	}

	public void setTypeCourrier(String typeCourrier) {
		this.typeCourrier = typeCourrier;
	}

	public String getTypeCourrier() {
		return typeCourrier;
	}

	public String getTypeCourrierJour() {
		return typeCourrierJour;
	}

	public void setTypeCourrierJour(String typeCourrierJour) {
		this.typeCourrierJour = typeCourrierJour;
	}

	public void setDestinataire(String destinataire) {
		this.destinataire = destinataire;
	}

	public String getDestinataire() {
		return destinataire;
	}

	public void setTypeDossier(String typeDossier) {
		this.typeDossier = typeDossier;
	}

	public String getTypeDossier() {
		return typeDossier;
	}

	public void setTypeDossierJour(String typeDossierJour) {
		this.typeDossierJour = typeDossierJour;
	}

	public String getTypeDossierJour() {
		return typeDossierJour;
	}

	public void setListDossiersRecus(
			List<CourrierConsulterInformations> listDossiersRecus) {
		this.listDossiersRecus = listDossiersRecus;
	}

	public List<CourrierConsulterInformations> getListDossiersRecus() {
		return listDossiersRecus;
	}

	public void setListDossiersRecusJour(
			List<CourrierConsulterInformations> listDossiersRecusJour) {
		this.listDossiersRecusJour = listDossiersRecusJour;
	}

	public List<CourrierConsulterInformations> getListDossiersRecusJour() {
		return listDossiersRecusJour;
	}

	public void setListDossiersEnvoyes(
			List<CourrierConsulterInformations> listDossiersEnvoyes) {
		this.listDossiersEnvoyes = listDossiersEnvoyes;
	}

	public List<CourrierConsulterInformations> getListDossiersEnvoyes() {
		return listDossiersEnvoyes;
	}

	public void setListDossiersEnvoyesJour(
			List<CourrierConsulterInformations> listDossiersEnvoyesJour) {
		this.listDossiersEnvoyesJour = listDossiersEnvoyesJour;
	}

	public List<CourrierConsulterInformations> getListDossiersEnvoyesJour() {
		return listDossiersEnvoyesJour;
	}

	public void setListDossier(DataModel listDossier) {
		this.listDossier = listDossier;
	}

	public DataModel getListDossier() {
		return listDossier;
	}

	public void setListDossierJour(DataModel listDossierJour) {
		this.listDossierJour = listDossierJour;
	}

	public DataModel getListDossierJour() {
		return listDossierJour;
	}

	public void setDossierConsulterInformations(
			List<CourrierConsulterInformations> dossierConsulterInformations) {
		this.dossierConsulterInformations = dossierConsulterInformations;
	}

	public List<CourrierConsulterInformations> getDossierConsulterInformations() {
		return dossierConsulterInformations;
	}

	public void setListdossierConsulterInformationsJour(
			List<CourrierConsulterInformations> listdossierConsulterInformationsJour) {
		this.listdossierConsulterInformationsJour = listdossierConsulterInformationsJour;
	}

	public List<CourrierConsulterInformations> getListdossierConsulterInformationsJour() {
		return listdossierConsulterInformationsJour;
	}

	public void setListCourriersRecusAvalider(
			List<CourrierConsulterInformations> listCourriersRecusAvalider) {
		this.listCourriersRecusAvalider = listCourriersRecusAvalider;
	}

	public List<CourrierConsulterInformations> getListCourriersRecusAvalider() {
		return listCourriersRecusAvalider;
	}

	public void setListCourriersRecusJourAvalider(
			List<CourrierConsulterInformations> listCourriersRecusJourAvalider) {
		this.listCourriersRecusJourAvalider = listCourriersRecusJourAvalider;
	}

	public List<CourrierConsulterInformations> getListCourriersRecusJourAvalider() {
		return listCourriersRecusJourAvalider;
	}

	public void setLdapOperation(LdapOperation ldapOperation) {
		this.ldapOperation = ldapOperation;
	}

	public LdapOperation getLdapOperation() {
		return ldapOperation;
	}

	public void setTypeCourrierValidationJour(String typeCourrierValidationJour) {
		this.typeCourrierValidationJour = typeCourrierValidationJour;
	}

	public String getTypeCourrierValidationJour() {
		return typeCourrierValidationJour;
	}

	public void setMoreChoices(boolean moreChoices) {
		this.moreChoices = moreChoices;
	}

	public boolean isMoreChoices() {
		return moreChoices;
	}

	public void setListCourriersDeValidationJour(
			List<CourrierConsulterInformations> listCourriersDeValidationJour) {
		this.listCourriersDeValidationJour = listCourriersDeValidationJour;
	}

	public List<CourrierConsulterInformations> getListCourriersDeValidationJour() {
		return listCourriersDeValidationJour;
	}

	public void setListCourriersDeValidation(
			List<CourrierConsulterInformations> listCourriersDeValidation) {
		this.listCourriersDeValidation = listCourriersDeValidation;
	}

	public List<CourrierConsulterInformations> getListCourriersDeValidation() {
		return listCourriersDeValidation;
	}

	public void setListCourriersTraiteJour(
			List<CourrierConsulterInformations> listCourriersTraiteJour) {
		this.listCourriersTraiteJour = listCourriersTraiteJour;
	}

	public List<CourrierConsulterInformations> getListCourriersTraiteJour() {
		return listCourriersTraiteJour;
	}

	public void setListCourriersTraite(
			List<CourrierConsulterInformations> listCourriersTraite) {
		this.listCourriersTraite = listCourriersTraite;
	}

	public List<CourrierConsulterInformations> getListCourriersTraite() {
		return listCourriersTraite;
	}

	public void setListCourriersValideJour(
			List<CourrierConsulterInformations> listCourriersValideJour) {
		this.listCourriersValideJour = listCourriersValideJour;
	}

	public List<CourrierConsulterInformations> getListCourriersValideJour() {
		return listCourriersValideJour;
	}

	public void setListCourriersValide(
			List<CourrierConsulterInformations> listCourriersValide) {
		this.listCourriersValide = listCourriersValide;
	}

	public List<CourrierConsulterInformations> getListCourriersValide() {
		return listCourriersValide;
	}

	public void setListCourriersNonValideJour(
			List<CourrierConsulterInformations> listCourriersNonValideJour) {
		this.listCourriersNonValideJour = listCourriersNonValideJour;
	}

	public List<CourrierConsulterInformations> getListCourriersNonValideJour() {
		return listCourriersNonValideJour;
	}

	public void setListCourriersNonValide(
			List<CourrierConsulterInformations> listCourriersNonValide) {
		this.listCourriersNonValide = listCourriersNonValide;
	}

	public List<CourrierConsulterInformations> getListCourriersNonValide() {
		return listCourriersNonValide;
	}

	public void setListCourriersAValiderJour(
			List<CourrierConsulterInformations> listCourriersAValiderJour) {
		this.listCourriersAValiderJour = listCourriersAValiderJour;
	}

	public List<CourrierConsulterInformations> getListCourriersAValiderJour() {
		return listCourriersAValiderJour;
	}

	public void setListCourriersAValider(
			List<CourrierConsulterInformations> listCourriersAValider) {
		this.listCourriersAValider = listCourriersAValider;
	}

	public List<CourrierConsulterInformations> getListCourriersAValider() {
		return listCourriersAValider;
	}

	public void setAllMailChecked(boolean allMailChecked) {
		this.allMailChecked = allMailChecked;
	}

	public boolean isAllMailChecked() {
		return allMailChecked;
	}

	public void setToValidateMailChecked(boolean toValidateMailChecked) {
		this.toValidateMailChecked = toValidateMailChecked;
	}

	public boolean isToValidateMailChecked() {
		return toValidateMailChecked;
	}

	public void setValidatedMailChecked(boolean validatedMailChecked) {
		this.validatedMailChecked = validatedMailChecked;
	}

	public boolean isValidatedMailChecked() {
		return validatedMailChecked;
	}

	public void setNotValidatedMailChecked(boolean notValidatedMailChecked) {
		this.notValidatedMailChecked = notValidatedMailChecked;
	}

	public boolean isNotValidatedMailChecked() {
		return notValidatedMailChecked;
	}

	public void setTreatedMailChecked(boolean treatedMailChecked) {
		this.treatedMailChecked = treatedMailChecked;
	}

	public boolean isTreatedMailChecked() {
		return treatedMailChecked;
	}

	public void setTypeCourrierValidation(String typeCourrierValidation) {
		this.typeCourrierValidation = typeCourrierValidation;
	}

	public String getTypeCourrierValidation() {
		return typeCourrierValidation;
	}

	public void setMoreChoicesJour(boolean moreChoicesJour) {
		this.moreChoicesJour = moreChoicesJour;
	}

	public boolean isMoreChoicesJour() {
		return moreChoicesJour;
	}

	public void setAllMailCheckedJour(boolean allMailCheckedJour) {
		this.allMailCheckedJour = allMailCheckedJour;
	}

	public boolean isAllMailCheckedJour() {
		return allMailCheckedJour;
	}

	public void setTreatedMailCheckedJour(boolean treatedMailCheckedJour) {
		this.treatedMailCheckedJour = treatedMailCheckedJour;
	}

	public boolean isTreatedMailCheckedJour() {
		return treatedMailCheckedJour;
	}

	public void setNotValidatedMailCheckedJour(
			boolean notValidatedMailCheckedJour) {
		this.notValidatedMailCheckedJour = notValidatedMailCheckedJour;
	}

	public boolean isNotValidatedMailCheckedJour() {
		return notValidatedMailCheckedJour;
	}

	public void setValidatedMailCheckedJour(boolean validatedMailCheckedJour) {
		this.validatedMailCheckedJour = validatedMailCheckedJour;
	}

	public boolean isValidatedMailCheckedJour() {
		return validatedMailCheckedJour;
	}

	public void setToValidateMailCheckedJour(boolean toValidateMailCheckedJour) {
		this.toValidateMailCheckedJour = toValidateMailCheckedJour;
	}

	public boolean isToValidateMailCheckedJour() {
		return toValidateMailCheckedJour;
	}

	public void setVariableConsultationCourrierSecretaire(
			Variables variableConsultationCourrierSecretaire) {
		this.variableConsultationCourrierSecretaire = variableConsultationCourrierSecretaire;
	}

	public Variables getVariableConsultationCourrierSecretaire() {
		return variableConsultationCourrierSecretaire;
	}

	public void setVariableConsultationCourrierSubordonne(
			Variables variableConsultationCourrierSubordonne) {
		this.variableConsultationCourrierSubordonne = variableConsultationCourrierSubordonne;
	}

	public Variables getVariableConsultationCourrierSubordonne() {
		return variableConsultationCourrierSubordonne;
	}

	public void setVariableConsultationCourrierSousUnite(
			Variables variableConsultationCourrierSousUnite) {
		this.variableConsultationCourrierSousUnite = variableConsultationCourrierSousUnite;
	}

	public Variables getVariableConsultationCourrierSousUnite() {
		return variableConsultationCourrierSousUnite;
	}

	public void setShowTab(boolean showTab) {
		this.showTab = showTab;
	}

	public boolean isShowTab() {
		return showTab;
	}

	public void setBocOption(boolean bocOption) {
		this.bocOption = bocOption;
	}

	public boolean isBocOption() {
		return bocOption;
	}

	public void setUserOption(boolean userOption) {
		this.userOption = userOption;
	}

	public boolean isUserOption() {
		return userOption;
	}

	public void setListTr(List<Transmission> listTr) {
		this.listTr = listTr;
	}

	public List<Transmission> getListTr() {
		return listTr;
	}

	public List<CourrierConsulterInformations> getListCourriersTousBoc() {
		return listCourriersTousBoc;
	}

	public void setListCourriersTousBoc(
			List<CourrierConsulterInformations> listCourriersTousBoc) {
		this.listCourriersTousBoc = listCourriersTousBoc;
	}

	public List<CourrierConsulterInformations> getListCourriersTousBocArrive() {
		return listCourriersTousBocArrive;
	}

	public void setListCourriersTousBocArrive(
			List<CourrierConsulterInformations> listCourriersTousBocArrive) {
		this.listCourriersTousBocArrive = listCourriersTousBocArrive;
	}

	public List<CourrierConsulterInformations> getListCourriersTousBocArriveTraite() {
		return listCourriersTousBocArriveTraite;
	}

	public void setListCourriersTousBocArriveTraite(
			List<CourrierConsulterInformations> listCourriersTousBocArriveTraite) {
		this.listCourriersTousBocArriveTraite = listCourriersTousBocArriveTraite;
	}

	public List<CourrierConsulterInformations> getListCourriersTousBocArriveNonTraite() {
		return listCourriersTousBocArriveNonTraite;
	}

	public void setListCourriersTousBocArriveNonTraite(
			List<CourrierConsulterInformations> listCourriersTousBocArriveNonTraite) {
		this.listCourriersTousBocArriveNonTraite = listCourriersTousBocArriveNonTraite;
	}

	public List<CourrierConsulterInformations> getListCourriersTousBocDepart() {
		return listCourriersTousBocDepart;
	}

	public void setListCourriersTousBocDepart(
			List<CourrierConsulterInformations> listCourriersTousBocDepart) {
		this.listCourriersTousBocDepart = listCourriersTousBocDepart;
	}

	public List<CourrierConsulterInformations> getListCourriersTousBocDepartTraite() {
		return listCourriersTousBocDepartTraite;
	}

	public void setListCourriersTousBocDepartTraite(
			List<CourrierConsulterInformations> listCourriersTousBocDepartTraite) {
		this.listCourriersTousBocDepartTraite = listCourriersTousBocDepartTraite;
	}

	public List<CourrierConsulterInformations> getListCourriersTousBocDepartNonTraite() {
		return listCourriersTousBocDepartNonTraite;
	}

	public void setListCourriersTousBocDepartNonTraite(
			List<CourrierConsulterInformations> listCourriersTousBocDepartNonTraite) {
		this.listCourriersTousBocDepartNonTraite = listCourriersTousBocDepartNonTraite;
	}

	public List<CourrierConsulterInformations> getListCourriersTousBocJour() {
		return listCourriersTousBocJour;
	}

	public void setListCourriersTousBocJour(
			List<CourrierConsulterInformations> listCourriersTousBocJour) {
		this.listCourriersTousBocJour = listCourriersTousBocJour;
	}

	public List<CourrierConsulterInformations> getListCourriersTousBocArriveJour() {
		return listCourriersTousBocArriveJour;
	}

	public void setListCourriersTousBocArriveJour(
			List<CourrierConsulterInformations> listCourriersTousBocArriveJour) {
		this.listCourriersTousBocArriveJour = listCourriersTousBocArriveJour;
	}

	public List<CourrierConsulterInformations> getListCourriersTousBocArriveTraiteJour() {
		return listCourriersTousBocArriveTraiteJour;
	}

	public void setListCourriersTousBocArriveTraiteJour(
			List<CourrierConsulterInformations> listCourriersTousBocArriveTraiteJour) {
		this.listCourriersTousBocArriveTraiteJour = listCourriersTousBocArriveTraiteJour;
	}

	public List<CourrierConsulterInformations> getListCourriersTousBocArriveNonTraiteJour() {
		return listCourriersTousBocArriveNonTraiteJour;
	}

	public void setListCourriersTousBocArriveNonTraiteJour(
			List<CourrierConsulterInformations> listCourriersTousBocArriveNonTraiteJour) {
		this.listCourriersTousBocArriveNonTraiteJour = listCourriersTousBocArriveNonTraiteJour;
	}

	public List<CourrierConsulterInformations> getListCourriersTousBocDepartJour() {
		return listCourriersTousBocDepartJour;
	}

	public void setListCourriersTousBocDepartJour(
			List<CourrierConsulterInformations> listCourriersTousBocDepartJour) {
		this.listCourriersTousBocDepartJour = listCourriersTousBocDepartJour;
	}

	public List<CourrierConsulterInformations> getListCourriersTousBocDepartTraiteJour() {
		return listCourriersTousBocDepartTraiteJour;
	}

	public void setListCourriersTousBocDepartTraiteJour(
			List<CourrierConsulterInformations> listCourriersTousBocDepartTraiteJour) {
		this.listCourriersTousBocDepartTraiteJour = listCourriersTousBocDepartTraiteJour;
	}

	public List<CourrierConsulterInformations> getListCourriersTousBocDepartNonTraiteJour() {
		return listCourriersTousBocDepartNonTraiteJour;
	}

	public void setListCourriersTousBocDepartNonTraiteJour(
			List<CourrierConsulterInformations> listCourriersTousBocDepartNonTraiteJour) {
		this.listCourriersTousBocDepartNonTraiteJour = listCourriersTousBocDepartNonTraiteJour;
	}

	public List<CourrierConsulterInformations> getListCourriersFaxBoc() {
		return listCourriersFaxBoc;
	}

	public void setListCourriersFaxBoc(
			List<CourrierConsulterInformations> listCourriersFaxBoc) {
		this.listCourriersFaxBoc = listCourriersFaxBoc;
	}

	public List<CourrierConsulterInformations> getListCourriersFaxBocArrive() {
		return listCourriersFaxBocArrive;
	}

	public void setListCourriersFaxBocArrive(
			List<CourrierConsulterInformations> listCourriersFaxBocArrive) {
		this.listCourriersFaxBocArrive = listCourriersFaxBocArrive;
	}

	public List<CourrierConsulterInformations> getListCourriersFaxBocArriveTraite() {
		return listCourriersFaxBocArriveTraite;
	}

	public void setListCourriersFaxBocArriveTraite(
			List<CourrierConsulterInformations> listCourriersFaxBocArriveTraite) {
		this.listCourriersFaxBocArriveTraite = listCourriersFaxBocArriveTraite;
	}

	public List<CourrierConsulterInformations> getListCourriersFaxBocArriveNonTraite() {
		return listCourriersFaxBocArriveNonTraite;
	}

	public void setListCourriersFaxBocArriveNonTraite(
			List<CourrierConsulterInformations> listCourriersFaxBocArriveNonTraite) {
		this.listCourriersFaxBocArriveNonTraite = listCourriersFaxBocArriveNonTraite;
	}

	public List<CourrierConsulterInformations> getListCourriersFaxBocDepart() {
		return listCourriersFaxBocDepart;
	}

	public void setListCourriersFaxBocDepart(
			List<CourrierConsulterInformations> listCourriersFaxBocDepart) {
		this.listCourriersFaxBocDepart = listCourriersFaxBocDepart;
	}

	public List<CourrierConsulterInformations> getListCourriersFaxBocDepartTraite() {
		return listCourriersFaxBocDepartTraite;
	}

	public void setListCourriersFaxBocDepartTraite(
			List<CourrierConsulterInformations> listCourriersFaxBocDepartTraite) {
		this.listCourriersFaxBocDepartTraite = listCourriersFaxBocDepartTraite;
	}

	public List<CourrierConsulterInformations> getListCourriersFaxBocDepartNonTraite() {
		return listCourriersFaxBocDepartNonTraite;
	}

	public void setListCourriersFaxBocDepartNonTraite(
			List<CourrierConsulterInformations> listCourriersFaxBocDepartNonTraite) {
		this.listCourriersFaxBocDepartNonTraite = listCourriersFaxBocDepartNonTraite;
	}

	public List<CourrierConsulterInformations> getListCourriersFaxBocJour() {
		return listCourriersFaxBocJour;
	}

	public void setListCourriersFaxBocJour(
			List<CourrierConsulterInformations> listCourriersFaxBocJour) {
		this.listCourriersFaxBocJour = listCourriersFaxBocJour;
	}

	public List<CourrierConsulterInformations> getListCourriersFaxBocArriveJour() {
		return listCourriersFaxBocArriveJour;
	}

	public void setListCourriersFaxBocArriveJour(
			List<CourrierConsulterInformations> listCourriersFaxBocArriveJour) {
		this.listCourriersFaxBocArriveJour = listCourriersFaxBocArriveJour;
	}

	public List<CourrierConsulterInformations> getListCourriersFaxBocArriveTraiteJour() {
		return listCourriersFaxBocArriveTraiteJour;
	}

	public void setListCourriersFaxBocArriveTraiteJour(
			List<CourrierConsulterInformations> listCourriersFaxBocArriveTraiteJour) {
		this.listCourriersFaxBocArriveTraiteJour = listCourriersFaxBocArriveTraiteJour;
	}

	public List<CourrierConsulterInformations> getListCourriersFaxBocArriveNonTraiteJour() {
		return listCourriersFaxBocArriveNonTraiteJour;
	}

	public void setListCourriersFaxBocArriveNonTraiteJour(
			List<CourrierConsulterInformations> listCourriersFaxBocArriveNonTraiteJour) {
		this.listCourriersFaxBocArriveNonTraiteJour = listCourriersFaxBocArriveNonTraiteJour;
	}

	public List<CourrierConsulterInformations> getListCourriersFaxBocDepartJour() {
		return listCourriersFaxBocDepartJour;
	}

	public void setListCourriersFaxBocDepartJour(
			List<CourrierConsulterInformations> listCourriersFaxBocDepartJour) {
		this.listCourriersFaxBocDepartJour = listCourriersFaxBocDepartJour;
	}

	public List<CourrierConsulterInformations> getListCourriersFaxBocDepartTraiteJour() {
		return listCourriersFaxBocDepartTraiteJour;
	}

	public void setListCourriersFaxBocDepartTraiteJour(
			List<CourrierConsulterInformations> listCourriersFaxBocDepartTraiteJour) {
		this.listCourriersFaxBocDepartTraiteJour = listCourriersFaxBocDepartTraiteJour;
	}

	public List<CourrierConsulterInformations> getListCourriersFaxBocDepartNonTraiteJour() {
		return listCourriersFaxBocDepartNonTraiteJour;
	}

	public void setListCourriersFaxBocDepartNonTraiteJour(
			List<CourrierConsulterInformations> listCourriersFaxBocDepartNonTraiteJour) {
		this.listCourriersFaxBocDepartNonTraiteJour = listCourriersFaxBocDepartNonTraiteJour;
	}

	public List<CourrierConsulterInformations> getListCourriersMailBoc() {
		return listCourriersMailBoc;
	}

	public void setListCourriersMailBoc(
			List<CourrierConsulterInformations> listCourriersMailBoc) {
		this.listCourriersMailBoc = listCourriersMailBoc;
	}

	public List<CourrierConsulterInformations> getListCourriersMailBocArrive() {
		return listCourriersMailBocArrive;
	}

	public void setListCourriersMailBocArrive(
			List<CourrierConsulterInformations> listCourriersMailBocArrive) {
		this.listCourriersMailBocArrive = listCourriersMailBocArrive;
	}

	public List<CourrierConsulterInformations> getListCourriersMailBocArriveTraite() {
		return listCourriersMailBocArriveTraite;
	}

	public void setListCourriersMailBocArriveTraite(
			List<CourrierConsulterInformations> listCourriersMailBocArriveTraite) {
		this.listCourriersMailBocArriveTraite = listCourriersMailBocArriveTraite;
	}

	public List<CourrierConsulterInformations> getListCourriersMailBocArriveNonTraite() {
		return listCourriersMailBocArriveNonTraite;
	}

	public void setListCourriersMailBocArriveNonTraite(
			List<CourrierConsulterInformations> listCourriersMailBocArriveNonTraite) {
		this.listCourriersMailBocArriveNonTraite = listCourriersMailBocArriveNonTraite;
	}

	public List<CourrierConsulterInformations> getListCourriersMailBocDepart() {
		return listCourriersMailBocDepart;
	}

	public void setListCourriersMailBocDepart(
			List<CourrierConsulterInformations> listCourriersMailBocDepart) {
		this.listCourriersMailBocDepart = listCourriersMailBocDepart;
	}

	public List<CourrierConsulterInformations> getListCourriersMailBocDepartTraite() {
		return listCourriersMailBocDepartTraite;
	}

	public void setListCourriersMailBocDepartTraite(
			List<CourrierConsulterInformations> listCourriersMailBocDepartTraite) {
		this.listCourriersMailBocDepartTraite = listCourriersMailBocDepartTraite;
	}

	public List<CourrierConsulterInformations> getListCourriersMailBocDepartNonTraite() {
		return listCourriersMailBocDepartNonTraite;
	}

	public void setListCourriersMailBocDepartNonTraite(
			List<CourrierConsulterInformations> listCourriersMailBocDepartNonTraite) {
		this.listCourriersMailBocDepartNonTraite = listCourriersMailBocDepartNonTraite;
	}

	public List<CourrierConsulterInformations> getListCourriersMailBocJour() {
		return listCourriersMailBocJour;
	}

	public void setListCourriersMailBocJour(
			List<CourrierConsulterInformations> listCourriersMailBocJour) {
		this.listCourriersMailBocJour = listCourriersMailBocJour;
	}

	public List<CourrierConsulterInformations> getListCourriersMailBocArriveJour() {
		return listCourriersMailBocArriveJour;
	}

	public void setListCourriersMailBocArriveJour(
			List<CourrierConsulterInformations> listCourriersMailBocArriveJour) {
		this.listCourriersMailBocArriveJour = listCourriersMailBocArriveJour;
	}

	public List<CourrierConsulterInformations> getListCourriersMailBocArriveTraiteJour() {
		return listCourriersMailBocArriveTraiteJour;
	}

	public void setListCourriersMailBocArriveTraiteJour(
			List<CourrierConsulterInformations> listCourriersMailBocArriveTraiteJour) {
		this.listCourriersMailBocArriveTraiteJour = listCourriersMailBocArriveTraiteJour;
	}

	public List<CourrierConsulterInformations> getListCourriersMailBocArriveNonTraiteJour() {
		return listCourriersMailBocArriveNonTraiteJour;
	}

	public void setListCourriersMailBocArriveNonTraiteJour(
			List<CourrierConsulterInformations> listCourriersMailBocArriveNonTraiteJour) {
		this.listCourriersMailBocArriveNonTraiteJour = listCourriersMailBocArriveNonTraiteJour;
	}

	public List<CourrierConsulterInformations> getListCourriersMailBocDepartJour() {
		return listCourriersMailBocDepartJour;
	}

	public void setListCourriersMailBocDepartJour(
			List<CourrierConsulterInformations> listCourriersMailBocDepartJour) {
		this.listCourriersMailBocDepartJour = listCourriersMailBocDepartJour;
	}

	public List<CourrierConsulterInformations> getListCourriersMailBocDepartTraiteJour() {
		return listCourriersMailBocDepartTraiteJour;
	}

	public void setListCourriersMailBocDepartTraiteJour(
			List<CourrierConsulterInformations> listCourriersMailBocDepartTraiteJour) {
		this.listCourriersMailBocDepartTraiteJour = listCourriersMailBocDepartTraiteJour;
	}

	public List<CourrierConsulterInformations> getListCourriersMailBocDepartNonTraiteJour() {
		return listCourriersMailBocDepartNonTraiteJour;
	}

	public void setListCourriersMailBocDepartNonTraiteJour(
			List<CourrierConsulterInformations> listCourriersMailBocDepartNonTraiteJour) {
		this.listCourriersMailBocDepartNonTraiteJour = listCourriersMailBocDepartNonTraiteJour;
	}

	public List<CourrierConsulterInformations> getListCourriersAutreBoc() {
		return listCourriersAutreBoc;
	}

	public void setListCourriersAutreBoc(
			List<CourrierConsulterInformations> listCourriersAutreBoc) {
		this.listCourriersAutreBoc = listCourriersAutreBoc;
	}

	public List<CourrierConsulterInformations> getListCourriersAutreBocArrive() {
		return listCourriersAutreBocArrive;
	}

	public void setListCourriersAutreBocArrive(
			List<CourrierConsulterInformations> listCourriersAutreBocArrive) {
		this.listCourriersAutreBocArrive = listCourriersAutreBocArrive;
	}

	public List<CourrierConsulterInformations> getListCourriersAutreBocArriveTraite() {
		return listCourriersAutreBocArriveTraite;
	}

	public void setListCourriersAutreBocArriveTraite(
			List<CourrierConsulterInformations> listCourriersAutreBocArriveTraite) {
		this.listCourriersAutreBocArriveTraite = listCourriersAutreBocArriveTraite;
	}

	public List<CourrierConsulterInformations> getListCourriersAutreBocArriveNonTraite() {
		return listCourriersAutreBocArriveNonTraite;
	}

	public void setListCourriersAutreBocArriveNonTraite(
			List<CourrierConsulterInformations> listCourriersAutreBocArriveNonTraite) {
		this.listCourriersAutreBocArriveNonTraite = listCourriersAutreBocArriveNonTraite;
	}

	public List<CourrierConsulterInformations> getListCourriersAutreBocDepart() {
		return listCourriersAutreBocDepart;
	}

	public void setListCourriersAutreBocDepart(
			List<CourrierConsulterInformations> listCourriersAutreBocDepart) {
		this.listCourriersAutreBocDepart = listCourriersAutreBocDepart;
	}

	public List<CourrierConsulterInformations> getListCourriersAutreBocDepartTraite() {
		return listCourriersAutreBocDepartTraite;
	}

	public void setListCourriersAutreBocDepartTraite(
			List<CourrierConsulterInformations> listCourriersAutreBocDepartTraite) {
		this.listCourriersAutreBocDepartTraite = listCourriersAutreBocDepartTraite;
	}

	public List<CourrierConsulterInformations> getListCourriersAutreBocDepartNonTraite() {
		return listCourriersAutreBocDepartNonTraite;
	}

	public void setListCourriersAutreBocDepartNonTraite(
			List<CourrierConsulterInformations> listCourriersAutreBocDepartNonTraite) {
		this.listCourriersAutreBocDepartNonTraite = listCourriersAutreBocDepartNonTraite;
	}

	public List<CourrierConsulterInformations> getListCourriersAutreBocJour() {
		return listCourriersAutreBocJour;
	}

	public void setListCourriersAutreBocJour(
			List<CourrierConsulterInformations> listCourriersAutreBocJour) {
		this.listCourriersAutreBocJour = listCourriersAutreBocJour;
	}

	public List<CourrierConsulterInformations> getListCourriersAutreBocArriveJour() {
		return listCourriersAutreBocArriveJour;
	}

	public void setListCourriersAutreBocArriveJour(
			List<CourrierConsulterInformations> listCourriersAutreBocArriveJour) {
		this.listCourriersAutreBocArriveJour = listCourriersAutreBocArriveJour;
	}

	public List<CourrierConsulterInformations> getListCourriersAutreBocArriveTraiteJour() {
		return listCourriersAutreBocArriveTraiteJour;
	}

	public void setListCourriersAutreBocArriveTraiteJour(
			List<CourrierConsulterInformations> listCourriersAutreBocArriveTraiteJour) {
		this.listCourriersAutreBocArriveTraiteJour = listCourriersAutreBocArriveTraiteJour;
	}

	public List<CourrierConsulterInformations> getListCourriersAutreBocArriveNonTraiteJour() {
		return listCourriersAutreBocArriveNonTraiteJour;
	}

	public void setListCourriersAutreBocArriveNonTraiteJour(
			List<CourrierConsulterInformations> listCourriersAutreBocArriveNonTraiteJour) {
		this.listCourriersAutreBocArriveNonTraiteJour = listCourriersAutreBocArriveNonTraiteJour;
	}

	public List<CourrierConsulterInformations> getListCourriersAutreBocDepartJour() {
		return listCourriersAutreBocDepartJour;
	}

	public void setListCourriersAutreBocDepartJour(
			List<CourrierConsulterInformations> listCourriersAutreBocDepartJour) {
		this.listCourriersAutreBocDepartJour = listCourriersAutreBocDepartJour;
	}

	public List<CourrierConsulterInformations> getListCourriersAutreBocDepartTraiteJour() {
		return listCourriersAutreBocDepartTraiteJour;
	}

	public void setListCourriersAutreBocDepartTraiteJour(
			List<CourrierConsulterInformations> listCourriersAutreBocDepartTraiteJour) {
		this.listCourriersAutreBocDepartTraiteJour = listCourriersAutreBocDepartTraiteJour;
	}

	public List<CourrierConsulterInformations> getListCourriersAutreBocDepartNonTraiteJour() {
		return listCourriersAutreBocDepartNonTraiteJour;
	}

	public void setListCourriersAutreBocDepartNonTraiteJour(
			List<CourrierConsulterInformations> listCourriersAutreBocDepartNonTraiteJour) {
		this.listCourriersAutreBocDepartNonTraiteJour = listCourriersAutreBocDepartNonTraiteJour;
	}

	public void setListCourriersTousBocTraite(
			List<CourrierConsulterInformations> listCourriersTousBocTraite) {
		this.listCourriersTousBocTraite = listCourriersTousBocTraite;
	}

	public List<CourrierConsulterInformations> getListCourriersTousBocTraite() {
		return listCourriersTousBocTraite;
	}

	public void setListCourriersTousBocNonTraite(
			List<CourrierConsulterInformations> listCourriersTousBocNonTraite) {
		this.listCourriersTousBocNonTraite = listCourriersTousBocNonTraite;
	}

	public List<CourrierConsulterInformations> getListCourriersTousBocNonTraite() {
		return listCourriersTousBocNonTraite;
	}

	public void setListCourriersTousBocTraiteJour(
			List<CourrierConsulterInformations> listCourriersTousBocTraiteJour) {
		this.listCourriersTousBocTraiteJour = listCourriersTousBocTraiteJour;
	}

	public List<CourrierConsulterInformations> getListCourriersTousBocTraiteJour() {
		return listCourriersTousBocTraiteJour;
	}

	public void setListCourriersTousBocNonTraiteJour(
			List<CourrierConsulterInformations> listCourriersTousBocNonTraiteJour) {
		this.listCourriersTousBocNonTraiteJour = listCourriersTousBocNonTraiteJour;
	}

	public List<CourrierConsulterInformations> getListCourriersTousBocNonTraiteJour() {
		return listCourriersTousBocNonTraiteJour;
	}

	public void setTypeCourrierTraitementJour(String typeCourrierTraitementJour) {
		this.typeCourrierTraitementJour = typeCourrierTraitementJour;
	}

	public String getTypeCourrierTraitementJour() {
		return typeCourrierTraitementJour;
	}

	public void setCategorieCourrierJour(String categorieCourrierJour) {
		this.categorieCourrierJour = categorieCourrierJour;
	}

	public String getCategorieCourrierJour() {
		return categorieCourrierJour;
	}

	public void setTypeCourrierTraitement(String typeCourrierTraitement) {
		this.typeCourrierTraitement = typeCourrierTraitement;
	}

	public String getTypeCourrierTraitement() {
		return typeCourrierTraitement;
	}

	public void setCategorieCourrier(String categorieCourrier) {
		this.categorieCourrier = categorieCourrier;
	}

	public String getCategorieCourrier() {
		return categorieCourrier;
	}

	public void setTransmissionCourrier(String transmissionCourrier) {
		this.transmissionCourrier = transmissionCourrier;
	}

	public String getTransmissionCourrier() {
		return transmissionCourrier;
	}

	public void setTransmissionCourrierJour(String transmissionCourrierJour) {
		this.transmissionCourrierJour = transmissionCourrierJour;
	}

	public String getTransmissionCourrierJour() {
		return transmissionCourrierJour;
	}

	public void setListCourriersFaxBocTraite(
			List<CourrierConsulterInformations> listCourriersFaxBocTraite) {
		this.listCourriersFaxBocTraite = listCourriersFaxBocTraite;
	}

	public List<CourrierConsulterInformations> getListCourriersFaxBocTraite() {
		return listCourriersFaxBocTraite;
	}

	public void setListCourriersFaxBocNonTraite(
			List<CourrierConsulterInformations> listCourriersFaxBocNonTraite) {
		this.listCourriersFaxBocNonTraite = listCourriersFaxBocNonTraite;
	}

	public List<CourrierConsulterInformations> getListCourriersFaxBocNonTraite() {
		return listCourriersFaxBocNonTraite;
	}

	public void setListCourriersMailBocTraite(
			List<CourrierConsulterInformations> listCourriersMailBocTraite) {
		this.listCourriersMailBocTraite = listCourriersMailBocTraite;
	}

	public List<CourrierConsulterInformations> getListCourriersMailBocTraite() {
		return listCourriersMailBocTraite;
	}

	public void setListCourriersMailBocNonTraite(
			List<CourrierConsulterInformations> listCourriersMailBocNonTraite) {
		this.listCourriersMailBocNonTraite = listCourriersMailBocNonTraite;
	}

	public List<CourrierConsulterInformations> getListCourriersMailBocNonTraite() {
		return listCourriersMailBocNonTraite;
	}

	public void setListCourriersAutreBocTraite(
			List<CourrierConsulterInformations> listCourriersAutreBocTraite) {
		this.listCourriersAutreBocTraite = listCourriersAutreBocTraite;
	}

	public List<CourrierConsulterInformations> getListCourriersAutreBocTraite() {
		return listCourriersAutreBocTraite;
	}

	public void setListCourriersAutreBocNonTraite(
			List<CourrierConsulterInformations> listCourriersAutreBocNonTraite) {
		this.listCourriersAutreBocNonTraite = listCourriersAutreBocNonTraite;
	}

	public List<CourrierConsulterInformations> getListCourriersAutreBocNonTraite() {
		return listCourriersAutreBocNonTraite;
	}

	public List<CourrierConsulterInformations> getListCourriersFaxBocTraiteJour() {
		return listCourriersFaxBocTraiteJour;
	}

	public void setListCourriersFaxBocTraiteJour(
			List<CourrierConsulterInformations> listCourriersFaxBocTraiteJour) {
		this.listCourriersFaxBocTraiteJour = listCourriersFaxBocTraiteJour;
	}

	public List<CourrierConsulterInformations> getListCourriersFaxBocNonTraiteJour() {
		return listCourriersFaxBocNonTraiteJour;
	}

	public void setListCourriersFaxBocNonTraiteJour(
			List<CourrierConsulterInformations> listCourriersFaxBocNonTraiteJour) {
		this.listCourriersFaxBocNonTraiteJour = listCourriersFaxBocNonTraiteJour;
	}

	public List<CourrierConsulterInformations> getListCourriersMailBocTraiteJour() {
		return listCourriersMailBocTraiteJour;
	}

	public void setListCourriersMailBocTraiteJour(
			List<CourrierConsulterInformations> listCourriersMailBocTraiteJour) {
		this.listCourriersMailBocTraiteJour = listCourriersMailBocTraiteJour;
	}

	public List<CourrierConsulterInformations> getListCourriersMailBocNonTraiteJour() {
		return listCourriersMailBocNonTraiteJour;
	}

	public void setListCourriersMailBocNonTraiteJour(
			List<CourrierConsulterInformations> listCourriersMailBocNonTraiteJour) {
		this.listCourriersMailBocNonTraiteJour = listCourriersMailBocNonTraiteJour;
	}

	public List<CourrierConsulterInformations> getListCourriersAutreBocTraiteJour() {
		return listCourriersAutreBocTraiteJour;
	}

	public void setListCourriersAutreBocTraiteJour(
			List<CourrierConsulterInformations> listCourriersAutreBocTraiteJour) {
		this.listCourriersAutreBocTraiteJour = listCourriersAutreBocTraiteJour;
	}

	public List<CourrierConsulterInformations> getListCourriersAutreBocNonTraiteJour() {
		return listCourriersAutreBocNonTraiteJour;
	}

	public void setListCourriersAutreBocNonTraiteJour(
			List<CourrierConsulterInformations> listCourriersAutreBocNonTraiteJour) {
		this.listCourriersAutreBocNonTraiteJour = listCourriersAutreBocNonTraiteJour;
	}

	public void setShowExecuteAllButtonJour(boolean showExecuteAllButtonJour) {
		this.showExecuteAllButtonJour = showExecuteAllButtonJour;
	}

	public boolean isShowExecuteAllButtonJour() {
		return showExecuteAllButtonJour;
	}

	public void setShowExecuteAllButton(boolean showExecuteAllButton) {
		this.showExecuteAllButton = showExecuteAllButton;
	}

	public boolean isShowExecuteAllButton() {
		return showExecuteAllButton;
	}

	public void setHideExecuteAllButtonJour(boolean hideExecuteAllButtonJour) {
		this.hideExecuteAllButtonJour = hideExecuteAllButtonJour;
	}

	public boolean isHideExecuteAllButtonJour() {
		return hideExecuteAllButtonJour;
	}

	public void setHideExecuteAllButton(boolean hideExecuteAllButton) {
		this.hideExecuteAllButton = hideExecuteAllButton;
	}

	public boolean isHideExecuteAllButton() {
		return hideExecuteAllButton;
	}

	public Informations getInfo1() {
		return info1;
	}

	public void setInfo1(Informations info1) {
		this.info1 = info1;
	}

	public Informations getInfo2() {
		return info2;
	}

	public void setInfo2(Informations info2) {
		this.info2 = info2;
	}

	public Informations getInfo3() {
		return info3;
	}

	public void setInfo3(Informations info3) {
		this.info3 = info3;
	}

	public Informations getInfo4() {
		return info4;
	}

	public void setInfo4(Informations info4) {
		this.info4 = info4;
	}

	public List<Informations> getListInfo() {
		return listInfo;
	}

	public void setListInfo(List<Informations> listInfo) {
		this.listInfo = listInfo;
	}

	public String getTypeNotification() {
		return typeNotification;
	}

	public void setTypeNotification(String typeNotification) {
		this.typeNotification = typeNotification;
	}

	public VariableGlobaleNotification getVbn() {
		return vbn;
	}

	public void setVbn(VariableGlobaleNotification vbn) {
		this.vbn = vbn;
	}

	public Transaction getNewTransaction() {
		return newTransaction;
	}

	public void setNewTransaction(Transaction newTransaction) {
		this.newTransaction = newTransaction;
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

	public int getIdBoc() {
		return idBoc;
	}

	public void setIdBoc(int idBoc) {
		this.idBoc = idBoc;
	}

	public CourrierConsulterInformations getConsulterInformations() {
		return consulterInformations;
	}

	public void setConsulterInformations(
			CourrierConsulterInformations consulterInformations) {
		this.consulterInformations = consulterInformations;
	}

	public int getNouveauCourrier() {
		return nouveauCourrier;
	}

	public void setNouveauCourrier(int nouveauCourrier) {
		this.nouveauCourrier = nouveauCourrier;
	}

}