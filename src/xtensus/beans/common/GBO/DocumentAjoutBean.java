package xtensus.beans.common.GBO;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.servlet.http.HttpServletRequest;

import org.apache.chemistry.opencmis.commons.exceptions.CmisInvalidArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import xtensus.aop.LogClass;
import xtensus.beans.common.DownloadException;
import xtensus.beans.common.Ged;
import xtensus.beans.common.LanguageManagerBean;
import xtensus.beans.common.VariableGlobale;
import xtensus.beans.utils.ListeDocument;
import xtensus.dao.utils.DMSConnexionImplement;
import xtensus.entity.Courrier;
import xtensus.entity.Document;
import xtensus.entity.DocumentCategorie;
import xtensus.services.ApplicationManager;

@Component
@Scope("request")
public class DocumentAjoutBean {
	private boolean showGedErrorMessage;
	private Courrier courrier;
	private Document document;
	private ApplicationManager appMgr;
	private DataModel listDDM;
	private List<Document> documents;
	public boolean status;
	public boolean status1;
	@Autowired
	private VariableGlobale vb;
	@Autowired
	private Ged ged;
	private long records = 0;
	@Autowired
	private LanguageManagerBean lm;
	@Autowired
	private MessageSource messageSource;
	private String message;
	private String result;
	//[JS] :2020-05-15
	   private String nombrePages;
	   private boolean testOK;
	   private Integer nombrePage;

	@Autowired
	public DocumentAjoutBean(
			@Qualifier("applicationManager") ApplicationManager appMgr) {
		this.appMgr = appMgr;
		document = new Document();
		courrier = new Courrier();
		listDDM = new ListDataModel();
		setDocuments(new ArrayList<Document>());
	}

	public DocumentAjoutBean() {

	}

	@PostConstruct
	public void Initialize() {
		try {
			testOK=true;
			vb.setMasquerPanelDetailCourrier(false);	
			Resource rsrc = new ClassPathResource(
						"/paramAlfresco.properties");
				String pathConfigFile = rsrc.getFile()
						.getAbsolutePath();
				Properties props = new Properties();
				props.load(new FileInputStream(pathConfigFile));
				String URL = props.getProperty("alfresco.Url");
				String namingConfigFilePath = vb
						.getNamingConfigFilePath();
				String login = props.getProperty("alfresco.login");
				String mdp = props.getProperty("alfresco.password");
				vb.setDmsAccessLayer(DMSConnexionImplement
						.getConnexionGed(login, mdp, URL,
								namingConfigFilePath));
				showGedErrorMessage = DMSConnexionImplement.connetionStatus;
			System.out.println("connetionStatus "+showGedErrorMessage);
			courrier = vb.getCourrier();
			
			List<Document> listDocument = appMgr.listDocumentByIdDocumentAndDeleteFlagAndCatgDoc(courrier.getIdCourrier(), true, 1);
			//listDDM.setWrappedData(listDocument);
			documents= appMgr.listDocumentByIdDocumentAndDeleteFlagAndCatgDoc(courrier.getIdCourrier(), true, 1);
			if (listDocument.size() <= 9) {
				document.setDocumentReference(courrier.getCourrierReferenceCorrespondant()
						+ "." + "DOC.00" + (listDocument.size() + 1));
			} else {
				document.setDocumentReference(courrier.getCourrierReferenceCorrespondant()
						+ "." + "DOC.0" + (listDocument.size() + 1));
			}
			
			List<ListeDocument> listeDoc = new ArrayList<ListeDocument>();
			// Création d'une nouvelle liste en important le nom de document
			// dans Alfresco.
			int ord=1;
			for (Document doc : documents) {
				ListeDocument d = new ListeDocument();
				d.setDocument(doc);
				String docNom = "";
				d.setOrdre(ord);
				ord++;
				try {
					System.out.println("Document " + doc);
					// InputStream inputStream =
					// ged.readDocument(document).getInputStream();
					if(vb.getDmsAccessLayer()!= null&&!doc.getDocumentTypeUpload().equals("Physique")  ){
					docNom = ged.readDocument(doc).getName();
					}
				} catch (DownloadException e) {
					docNom = "non disponible";
					System.out.println("docNom dans catch :" + docNom);
					e.printStackTrace();
				}
				
				d.setNomDocument(docNom);
				listeDoc.add(d);
			}
			listDDM.setWrappedData(listeDoc);
			nombrePages="1";
			
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	 public void execute() {
	        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
//	        String response = request.getParameter("myForm:amani");
//	        String responseParam = request.getParameter("amani");
	        //note the difference when getting the parameter
	       
	         result = request.getParameter("server_response");
	         System.out.println("RESULT :: "+result);
	        System.out.println("INDEX:: "+result.indexOf("\\"));
	         result=  result.replace("\\", java.io.File.separator);
	         System.out.println("File Séparator"+java.io.File.separator);
	        //use the value in txtProperty as you want...
	        //Note: don't use System.out.println in production, use a logger instead
	     
	       
	        System.out.println("result  "+result);
	        vb.setDocScanne(result);
	    }
	 
	//[JS] :2020-05-15
	 public void validationNum() {
		testOK=true;
		   try{
			   System.out.println("nombrePage =============> "+nombrePages);
			    nombrePage=Integer.parseInt(nombrePages);
		   }catch (Exception e) {
//			   e.printStackTrace();
				testOK=false;
			
		}
			
		}
	 
	// fonction d'ajout des documents
		public void save()  throws Exception {
			try {
			
				//[JS] : 2020-03-27===========================			 
				 if(vb.getUploadType().equals("local") && vb.getUploadedData()==null){
						FacesContext.getCurrentInstance().addMessage(
								"messages",
								new FacesMessage(FacesMessage.SEVERITY_ERROR, "*  Vous n'avez pas choisir un document à joindre ou le fichier choisi est volumineux ", 
										""));
				//[JS] :2020-05-15
				 }else if(!testOK){
					 
						FacesContext.getCurrentInstance().addMessage(
								"messages",
								new FacesMessage(FacesMessage.SEVERITY_ERROR, "* Nombre de copie est un champ entier",
										""));
				 }else{

				System.out.println("dans save");
				// FacesContext.getCurrentInstance().get
				 System.out.println("############# Document Scann:"+vb.getDocScanne());
			
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
				System.out.println("nombrePage "+nombrePage);
				document.setDocumentNombreCopie(nombrePage);
				////////////////////////////////////////
				DocumentCategorie docCategorie = appMgr.getDocumentCategorieById(1);
				System.out.println("######## vb.getUploadType() == " + vb.getUploadType());
				if (vb.getUploadType().equals("scanner")) {
					
					String MyResultGBO;
					MyResultGBO=vb.getDocScanne();

					System.out.println("MyResultGBO "+"/var/www/html/documentscan/tmp/"+MyResultGBO.substring(36));
		            File myDocumentScanGBO= new File("/var/www/html/documentscan/tmp"+MyResultGBO.substring(36));
		          
		
					byte[] tab = new byte[((int) myDocumentScanGBO.length())];
					BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(myDocumentScanGBO));
					
				
	
					inputStream.read(tab, 0, tab.length);
		            System.out.println("\n Size "+tab.length);
		           
		            vb.setScannedData(tab);
		            
					System.out.println("###### Dans vb.getUploadType().equals(scanner) ");
					document.setDocumentType(".pdf");
				} else {
					System.out.println("###### Dans ");
					System.out.println("vb.getDocumentType() ===================<> "+vb.getDocumentType());
					document.setDocumentType(vb.getDocumentType());
				}
				System.out.println("######## docCategorie " + docCategorie.getDocumentCategorieId());
				System.out.println("vb.getUploadType()  "+vb.getUploadType());
				//document.getDocumentCategorie();
				document.setCourrier(courrier);
				document.setDocumentCategorie(docCategorie);
				System.out.println("######## getDocumentCategor " + document.getDocumentCategorie().getDocumentCategorieId());
				document.setDocumentDateInsertion(new Date());
				System.out.println(document.getDocumentDateInsertion());
				System.out.println("New Date      "+new Date());
				document.setDocumentProprietaire(vb.getPerson().getId());
				document.setDocumentSupprime(true);
				document.setDocumentParent(document);
				
				//document.setDocumentNombreCopie(documentNombreCopie);
				document.setOrdre(appMgr.listDocumentByIdDocumentAndDeleteFlagAndCatgDoc(courrier.getIdCourrier(), true, 1).size()+1);
				appMgr.insert(document);
				System.out.println("Objet Document "+document.getDocumentObjet());
				int refdoc = document.getIdDocument();
				// ----------------COUCHE ACCES GED ----------------
//				try{
				ged.uploadDocument(document);
				
				System.out.println("vb.upload data ================> "+vb.getUploadedData());
				
				// ----------------COUCHE ACCES GED ----------------
				documents.add(document);
				
				List<ListeDocument> listeDoc = new ArrayList<ListeDocument>();
				// Création d'une nouvelle liste en important le nom de document
				// dans Alfresco.
				int ord=1;
				for (Document doc : documents) {
					ListeDocument d = new ListeDocument();
					d.setDocument(doc);
					String docNom = "";
					d.setOrdre(ord);
					ord++;
					try {
						System.out.println("Document " + doc);
						// InputStream inputStream =
						// ged.readDocument(document).getInputStream();
						if(vb.getDmsAccessLayer()!= null&&!doc.getDocumentTypeUpload().equals("Physique"))
						docNom = ged.readDocument(doc).getName();
					} catch (DownloadException e) {
						docNom = "non disponible";
						System.out.println("docNom dans catch :" + docNom);
						e.printStackTrace();
					}

					
					d.setNomDocument(docNom);
					listeDoc.add(d);
				}
				listDDM.setWrappedData(listeDoc);
				
			//	listDDM.setWrappedData(documents);

				LogClass logClass = new LogClass();
//				logClass.addTrack(
//						"ajout",
//						"Evénement de log d'ajout du document "
//								+ document.getIdDocument() + "-"
//								+ document.getDocumentNom(), vb.getPerson(),
//						"INFO", appMgr);
//				logClass.addTrack(
//						"affectation",
//						"Evénement de log d'affectation du document "
//								+ document.getIdDocument() + "-"
//								+ document.getDocumentNom() + " au courrier "
//								+ courrier.getIdCourrier() + "-"
//								+ courrier.getCourrierReferenceCorrespondant(),
//						vb.getPerson(), "INFO", appMgr);

				document = nouveauDoc();
				status = true;
			}}catch (CmisInvalidArgumentException cmis) {
				
				
					System.out.println("Dans catch");
				cmis.printStackTrace();
				vb.setTypeUploadFichier(true);
				
					System.out.println("=================> "+vb.isTypeUploadFichier());
					try {
						appMgr.delete(document);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
			
			} catch (Exception e) {
				e.printStackTrace();
				status = false;
				System.out.println(e.getMessage());
				System.out.println(e.getCause());

			}
		}


	public Document nouveauDoc() {
		document = new Document();
		vb.setUploadType("");
		vb.setTypeUploadFichier(false);
		return document;
	}

	// Get Selection Rows
	public void getSelectionRow() {
		try {
			
			//document = (Document) listDDM.getRowData();
			ListeDocument doc = (ListeDocument) listDDM.getRowData();
			document = doc.getDocument();

		} catch (Exception e) {
		}
	}

	public void deleteSelectedRow() {
		try {
			documents.remove(document);
			appMgr.delete(document);
			
			List<ListeDocument> listeDoc = new ArrayList<ListeDocument>();
			// Création d'une nouvelle liste en important le nom de document
			// dans Alfresco.
			int ord=1;
			
			for (Document doc : documents) {
				ListeDocument d = new ListeDocument();
				d.setDocument(doc);
				String docNom = "";
d.setOrdre(ord);
ord++;
				try {
					System.out.println("Document " + doc);
					// InputStream inputStream =
					// ged.readDocument(document).getInputStream();
					docNom = ged.readDocument(doc).getName();
				} catch (DownloadException e) {
					docNom = "non disponible";
					System.out.println("docNom dans catch :" + docNom);
					e.printStackTrace();
				}
				d.setNomDocument(docNom);
				listeDoc.add(d);
			}
			listDDM.setWrappedData(listeDoc);
			
			
			LogClass logClass = new LogClass();
			logClass.addTrack(
					"suppression",
					"Evénement de log de suppression du document "
							+ document.getIdDocument() + "-"
							+ document.getDocumentReference(), vb.getPerson(),
					"INFO", appMgr);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String remplire() {
		System.out.println("***************Remplire***************");
		return ("Remplire");
	}

	public String valide() {
		return ("OK");
	}

	// **************************** Getter && Setter********************
	@SuppressWarnings("unchecked")
	public long getRecords() {
		if (listDDM != null && listDDM.getWrappedData() != null)
			records = ((List<ListeDocument>) listDDM.getWrappedData()).size();
		else
			records = 0;
		return records;
	}

	public void setCourrier(Courrier courrier) {
		this.courrier = courrier;
	}

	public Courrier getCourrier() {
		return courrier;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public VariableGlobale getVb() {
		return vb;
	}

	public void setVb(VariableGlobale vb) {
		this.vb = vb;
	}

	public void setListDDM(DataModel listDDM) {
		this.listDDM = listDDM;
	}

	public DataModel getListDDM() {
		return listDDM;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean isStatus1() {
		return status1;
	}

	public void setStatus1(boolean status1) {
		this.status1 = status1;
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

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public Ged getGed() {
		return ged;
	}

	public void setGed(Ged ged) {
		this.ged = ged;
	}

	public boolean isShowGedErrorMessage() {
		return showGedErrorMessage;
	}

	public void setShowGedErrorMessage(boolean showGedErrorMessage) {
		this.showGedErrorMessage = showGedErrorMessage;
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
