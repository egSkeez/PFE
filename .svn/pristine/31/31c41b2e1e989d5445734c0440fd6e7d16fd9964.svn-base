package xtensus.beans.common.GBO;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import org.springframework.stereotype.Component;

import com.xtensus.dal.core.DMSAccessLayer;
import com.xtensus.dal.core.DMSDocument;
import com.xtensus.dal.core.DocumentContext;

import xtensus.aop.LogClass;
import xtensus.beans.common.DownloadException;
import xtensus.beans.common.Ged;
import xtensus.beans.common.LanguageManagerBean;
import xtensus.beans.common.VariableGlobale;
import xtensus.dao.utils.DMSConnexionImplement;
import xtensus.entity.Courrier;
import xtensus.entity.Document;
import xtensus.entity.DocumentCategorie;
import xtensus.ldap.business.LdapOperation;
import xtensus.services.ApplicationManager;

@Component
@Scope("request")
public class DocumentDetailsBean {
	private boolean showGedErrorMessage;
	private Courrier courrier;
	private Document document;
	public boolean status;
	public boolean status1;
	public boolean showModif;
	private String changement="nonChanger";
	private String disvModif;
	@Autowired
	private LanguageManagerBean lm;
	@Autowired
	private MessageSource messageSource;
	private String message;
	@Autowired
	private VariableGlobale vb;
	private ApplicationManager appMgr;

	@Autowired
	private Ged ged;
	private String docName;
	DMSAccessLayer dmsAccessLayer;
	private String versionDoc;
	private String nomProprietaireDoc;
	private String loginPropretaireDoc;
	private LdapOperation ldapOperation;
	private VariableGlobale vbg;
	private String changementDoc;
	private boolean afficheUploadDoc = false;
	private String nombrePages;
	private boolean testOK;
	private Integer nombrePage;
	@Autowired
	public DocumentDetailsBean(
			@Qualifier("applicationManager") ApplicationManager appMgr) {
		this.appMgr = appMgr;
		courrier = new Courrier();
		document = new Document();
		ldapOperation=new LdapOperation();
	}

	@PostConstruct
	public void Initialize() {
		try {
			disvModif="none";
			changementDoc="Non";
			Resource rsrc = new ClassPathResource("/paramAlfresco.properties");
			String pathConfigFile = rsrc.getFile().getAbsolutePath();
			Properties props = new Properties();
			props.load(new FileInputStream(pathConfigFile));
			String URL = props.getProperty("alfresco.Url");
			String namingConfigFilePath = vb.getNamingConfigFilePath();
			String login = props.getProperty("alfresco.login");
			String mdp = props.getProperty("alfresco.password");
			vb.setDmsAccessLayer(DMSConnexionImplement.getConnexionGed(login,
					mdp, URL, namingConfigFilePath));
			showGedErrorMessage = DMSConnexionImplement.connetionStatus;
			testOK=true;
			courrier = vb.getCourrier();
			document = vb.getDocument();
			//[JS] : 2020-0515
			if(document!=null &&document.getDocumentNombreCopie()==0){
				nombrePages="";
			}else if(document!=null){
				nombrePages=String.valueOf(document.getDocumentNombreCopie());
			}else{
				System.out.println("cas ne sepose pas");
			}
			System.out.println("nombrePages "+nombrePages);
			
			boolean findPerson = false;
			if (!showGedErrorMessage) {
				int refCourrier;
				int refDocument;
				if (document!=null && document.getDocumentIdDocumentOriginal() != null) {
					 refCourrier = document.getDocumentIdCourrierOriginal();
					 refDocument = document.getDocumentIdDocumentOriginal(); 
					
				} else if(document!=null){
					refCourrier = courrier.getIdCourrier();
					 refDocument = document.getIdDocument();
				}
				else{
					refCourrier = courrier.getIdCourrier();
					 refDocument = 0;
				}
				
				dmsAccessLayer = vb.getDmsAccessLayer();
				try {
					if(document!=null){
					System.out.println("document===============> "+document);
					this.docName = ged.readDocument(document).getName();
					// ------------ couche acces GED -------------

					DocumentContext docContext = dmsAccessLayer
							.getDocumentContext("doc");
					com.xtensus.dal.core.Document docDocument = docContext
							.getDocument(refCourrier, refDocument);
					// RECUPERATION VERSIONS ANTERIEURES
					List<DMSDocument> documentVersionsList = dmsAccessLayer
							.getAllVersions(docDocument);

					int nbVersonDoc = documentVersionsList.size();

					versionDoc = "Nombres de versions du document "
							+ docDocument.getName() + " : " + nbVersonDoc
							+ " versions";
					loginPropretaireDoc=ldapOperation.getUserById(document.getDocumentProprietaire()).getLogin();
					}
					else 
					{
						System.out.println("Contacter Admin problème de document");
					}
					
				} catch (DownloadException e) {
					this.docName = "non disponible";
					e.printStackTrace();
				}
			} else {
				this.docName = "non disponible";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	 public void validationNum() {
			setTestOK(true);
			   try{
				   System.out.println("nombrePage =============> "+nombrePages);
				    nombrePage=Integer.parseInt(nombrePages);
			   }catch (Exception e) {
//				   e.printStackTrace();
					testOK=false;
				
			}
				
			}
	public void downloadDocument() {

		InputStream inputStream = null;
		try {

			// ------------ couche acces GED -------------
			inputStream = ged.readDocument(document).getInputStream();
			docName = ged.readDocument(document).getName();
			// ------------ couche acces GED -------------

			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) context
					.getExternalContext().getResponse();
//			if (document.getDocumentType().equals(".pdf")) {
//				response.setContentType("application/pdf");
//			} else if (document.getDocumentType().equals(".jpg")) {
//				response.setContentType("image/jpeg");
//			} else if (document.getDocumentType().equals(".gif")) {
//				response.setContentType("image/gif");
//			} else if (document.getDocumentType().equals(".png")) {
//				response.setContentType("image/png");
//			}
			document.setDocumentType(document.getDocumentType().toLowerCase());
			
			if (document.getDocumentType().equals(".pdf")) {
				response.setContentType("application/pdf");
			}
			if (document.getDocumentType().equals(".gif")) {
				response.setContentType("image/gif");
			}
			if (document.getDocumentType().equals(".jpg")) {
				response.setContentType("image/jpeg");
			}
			if (document.getDocumentType().equals(".png")) {
				response.setContentType("image/png");
			}
			if (document.getDocumentType().equals(".vsd")) {
				response.setContentType("application/x-visio");
			}
			if (document.getDocumentType().equals(".zip")) {
				response.setContentType("application/zip");
			}
			if (document.getDocumentType().equals(".rar")) {
				response.setContentType("application/x-rar-compressed");
			}
			if (document.getDocumentType().equals(".doc")) {
				response.setContentType("application/msword");
			}
			if (document.getDocumentType().equals(".docx")) {
				response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			}
			if (document.getDocumentType().equals(".xls")) {
				response.setContentType("application/vnd.ms-excel");
			}
			if (document.getDocumentType().equals(".xlsx")) {
				response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			}
			
			response.setHeader("Content-Disposition", "inline; filename="
					+ docName + document.getDocumentType());

			OutputStream outputStream = response.getOutputStream();

			int b;
			while ((b = inputStream.read()) != -1) {
				outputStream.write(b);
			}

			outputStream.flush();
			outputStream.close();

			context.responseComplete();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (DownloadException e) {
			e.printStackTrace();
		}
	}

	public void modifier() {
		try {
			// 
			System.out.println("testOK "+testOK);
			if(testOK){
			vb.setAffichePanelModifUpload(false);
			vb.setAffichePanelAjoutUpload(true);
			//[JS] : 2020-03-27================
			System.out.println("vb.getUploadType()===> "+vb.getUploadType());
			System.out.println("vb.getUploadedData(()===> "+vb.getUploadedData());
			 if(!changementDoc.equals("Non") && vb.getUploadType().equals("local") && vb.getUploadedData()==null){
				 
				 //uploaddocument
				 FacesContext.getCurrentInstance().addMessage(
							"messages",
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "*  Vous n'avez pas choisir un document à joindre ou le fichier choisi est volumineux ",
									""));
			 }else{
			
			
			
			DocumentCategorie docCategorie = appMgr.getDocumentCategorieById(document.getDocumentCategorie().getDocumentCategorieId());
			//////////////KBS 2019-11-26/////////////////////
			String typeDocument="";
			if (vb.getUploadType().equals("non")){
				System.out.println("######## Dans If1");
				typeDocument = "Physique";
			}else{
				System.out.println("######## Dans else");
				typeDocument = "Numérique";
			}
			document.setDocumentTypeUpload(typeDocument);
			System.out.println("Ordre ==========================================> "+document.getOrdre());
			////////////////////////////////////////
			Document doc = new Document();
			doc.setDocumentCategorie(docCategorie);
			doc.setDocumentCommentaire(document.getDocumentCommentaire());
			doc.setDocumentNom(document.getDocumentNom());
			doc.setDocumentObjet(document.getDocumentObjet());
			doc.setDocumentReference(document.getDocumentReference());
			doc.setDocumentNombreCopie(nombrePage);
			doc.setDocumentDateInsertion(new Date());
			doc.setDocumentParent(document.getDocumentParent());
			doc.setDocumentSupprime(true);
			doc.setDocumentTypeUpload(document.getDocumentTypeUpload());
			doc.setOrdre(appMgr.listDocumentByIdDocumentAndDeleteFlagAndCatgDoc(courrier.getIdCourrier(), true, 1).size()+1);

			doc.setCourrier(vb.getCourrier());
			doc.setDocumentProprietaire(vb.getPerson().getId());
			if(vb.getUploadType().equals("scanner")){
				doc.setDocumentType(".pdf");
			}else{
				
				System.out.println("vb.getDocumentType()>>>>>>>>>>>>>>>>>>>>>>>> "+vb.getDocumentType());
				doc.setDocumentType(vb.getDocumentType());

			}
			if(vb.getUploadType().equalsIgnoreCase("non")){
				doc.setDocumentIdDocumentOriginal(document.getDocumentParent().getIdDocument());
				doc.setDocumentIdCourrierOriginal(vb.getCourrier().getIdCourrier());
			}
			System.out.println("afficheUploadDoc"+afficheUploadDoc);
			if(afficheUploadDoc==false){
				document=vb.getDocument();
				System.out.println("REf doc===> "+document);
				List<Document> lisd = appMgr.listDocumentByIdDocument(document.getIdDocument());
			
				Document docc=lisd.get(0);
				System.out.println("Ref Doc =>"+document.getIdDocument());
				System.out.println("Commentarire Doc ====> "+document.getDocumentCommentaire());
				docc.setDocumentCommentaire(document.getDocumentCommentaire());
				docc.setDocumentCommentaire(document.getDocumentCommentaire());
				docc.setDocumentNom(document.getDocumentNom());
				docc.setDocumentObjet(document.getDocumentObjet());
				docc.setDocumentReference(document.getDocumentReference());
				docc.setDocumentNombreCopie(nombrePage);
				docc.setDocumentDateInsertion(new Date());
				docc.setDocumentParent(document.getDocumentParent());
				docc.setDocumentSupprime(true);
				docc.setDocumentTypeUpload(document.getDocumentTypeUpload());
//				document.setDocumentCommentaire(document.getDocumentCommentaire());

			 appMgr.update(docc);
			}else{
				appMgr.insert(doc);
				 ged.uploadDocument(doc);
			}
			// *
//			appMgr.update(document);
			// ----------------COUCHE ACCES GED ----------------
//			ged.updateDocument(document);
			 System.out.println("doc=======> "+doc.getDocumentTypeUpload());
			 System.out.println(document instanceof xtensus.entity.Document);
				System.out.println("afficheUploadDoc====> "+afficheUploadDoc);
			
			
			
			
			 
			// ----------------COUCHE ACCES GED ----------------
			vb.setDocument(doc);
			status1 = true;
			LogClass logClass = new LogClass();
			logClass.addTrack(
					"modification",
					"Evénement de log de modification du document "
							+ document.getIdDocument() + "-"
							+ document.getDocumentNom(), vb.getPerson(),
					"INFO", appMgr);

		} 
			}else{
				FacesContext.getCurrentInstance().addMessage(
						"messages",
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "* Nombre de copie est un champ entier",
								""));
			}
		}catch (Exception e) {
			status1 = false;
			e.printStackTrace();
		}
	}
	public void evenementChoix(ActionEvent evt) {
		System.out.println("la valeur de changement est :"+changement);
		if (changement.equals("changer")) {
			showModif = true;
			disvModif="inline";
			
		} else {
			disvModif="none";
			showModif = false;
		}
		System.out.println("la valeur de booleane est :"+showModif);
	}
	public void saveTempValue() {
		vb.setChangerDoc(changement);

	}
	//*Fonction de retour vers la page précédantes
	public String retourPage(){
		return vb.getTypeSender();
	}
	public void evenementChangementDoc(ActionEvent evt) {
		System.out.println("changement doc ===========> "+changementDoc);
		if (changementDoc.equals("Non")) {
			afficheUploadDoc = false;
		} else {
			afficheUploadDoc = true;
		}
	}
	
	
	
	// ************Getter & Setter********************

	public void setCourrier(Courrier courrier) {
		this.courrier = courrier;
	}

	public Courrier getCourrier() {
		return courrier;
	}

	public void setVb(VariableGlobale vb) {
		this.vb = vb;
	}

	public VariableGlobale getVb() {
		return vb;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
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

	public ApplicationManager getAppMgr() {
		return appMgr;
	}

	public void setAppMgr(ApplicationManager appMgr) {
		this.appMgr = appMgr;
	}

	public boolean isStatus1() {
		return status1;
	}

	public void setStatus1(boolean status1) {
		this.status1 = status1;
	}

	public Ged getGed() {
		return ged;
	}

	public void setGed(Ged ged) {
		this.ged = ged;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getDocName() {
		return docName;
	}

	public void setVersionDoc(String versionDoc) {
		this.versionDoc = versionDoc;
	}

	public String getVersionDoc() {
		return versionDoc;
	}

	public boolean isShowGedErrorMessage() {
		return showGedErrorMessage;
	}

	public void setShowGedErrorMessage(boolean showGedErrorMessage) {
		this.showGedErrorMessage = showGedErrorMessage;
	}

	public void setLoginPropretaireDoc(String loginPropretaireDoc) {
		this.loginPropretaireDoc = loginPropretaireDoc;
	}

	public String getLoginPropretaireDoc() {
		return loginPropretaireDoc;
	}
	public String getChangement() {
		return changement;
	}

	public void setChangement(String changement) {
		this.changement = changement;
	}

	public boolean isShowModif() {
		return showModif;
	}

	public void setShowModif(boolean showModif) {
		this.showModif = showModif;
	}

	public String getDisvModif() {
		return disvModif;
	}

	public void setDisvModif(String disvModif) {
		this.disvModif = disvModif;
	}

	public String getChangementDoc() {
		return changementDoc;
	}

	public void setChangementDoc(String changementDoc) {
		this.changementDoc = changementDoc;
	}

	public boolean isAfficheUploadDoc() {
		return afficheUploadDoc;
	}

	public void setAfficheUploadDoc(boolean afficheUploadDoc) {
		this.afficheUploadDoc = afficheUploadDoc;
	}

	public String getNombrePages() {
		return nombrePages;
	}

	public void setNombrePages(String nombrePages) {
		this.nombrePages = nombrePages;
	}

	public boolean isTestOK() {
		return testOK;
	}

	public void setTestOK(boolean testOK) {
		this.testOK = testOK;
	}

	public Integer getNombrePage() {
		return nombrePage;
	}

	public void setNombrePage(Integer nombrePage) {
		this.nombrePage = nombrePage;
	}
	
	
}
