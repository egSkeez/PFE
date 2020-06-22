package xtensus.beans.common.GBO;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.*;
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
import xtensus.entity.Expdest;
import xtensus.entity.Liensdossier;
import xtensus.entity.Transaction;
import xtensus.entity.TransactionAnnotation;
import xtensus.entity.TransactionDestination;
import xtensus.ldap.model.Person;
import xtensus.services.ApplicationManager;

@Component
@Scope("request")
public class DocumentConsultationBean {

	private DataModel listDDM;
	private Courrier courrier;
	private int idDoc;
	private Document document;
	private List<Document> list;
	private int id2;
	private int n;
	public boolean status;
	public boolean status1;
	private int nbrDoc;
	@Autowired
	private LanguageManagerBean lm;
	@Autowired
	private MessageSource messageSource;
	private String message;
	private String messageDel;
	@Autowired
	private VariableGlobale vb;
	private ApplicationManager appMgr;
	private long records = 0;
	private Transaction tr;
	private TransactionDestination trDest;
	private List<TransactionDestination> listTrDest;
	private List<Transaction> listTransaction;
	private List<TransactionAnnotation> listCrAnnot;
	private TransactionAnnotation crAnnot;
	private List<Liensdossier> listLiens;
	private List<Liensdossier> listLiensCr;
	private Liensdossier lien;
	private String nomCourrier;
	private Person person;
	private boolean status2;
	// **
	List<Document>  documents;
	// **

	public DocumentConsultationBean() {
	}

	@Autowired
	private Ged ged;
	private String docName;

	@Autowired
	public DocumentConsultationBean(
			@Qualifier("applicationManager") ApplicationManager appMgr) {
		this.appMgr = appMgr;
		courrier = new Courrier();
		document = new Document();
		listDDM = new ListDataModel();
		list = new ArrayList<Document>();
		tr = new Transaction();
		trDest = new TransactionDestination();
		listTrDest = new ArrayList<TransactionDestination>();
		listCrAnnot = new ArrayList<TransactionAnnotation>();
		crAnnot = new TransactionAnnotation();
		listLiens = new ArrayList<Liensdossier>();
		listLiensCr = new ArrayList<Liensdossier>();
		listTransaction = new ArrayList<Transaction>();
		lien = new Liensdossier();
		person=new Person();
		 documents = new ArrayList<Document>();
	}

	@PostConstruct
	public void Initialize() {
		try {
			//vb.setMasquerPanelDetailCourrier(false);
			person=vb.getPerson();
			courrier = vb.getCourrier();
//			 documents = appMgr.getDocumentByIdCourrier(courrier
//					.getIdCourrier());
			courrier = vb.getCourrier();
			System.out.println("courrier.getIdCourrier()   "+courrier.getIdCourrier());
			documents = appMgr.listDocumentByIdDocumentAndDeleteFlagAndCatgDoc(courrier.getIdCourrier(), true, 1);
			
			//listDDM.setWrappedData(documents);
			for (Document d : documents) {
				if (!d.getDocumentProprietaire().equals(person.getId())
						&& d.getDocumentDateConsultation() == null) {
				d.setDocumentDateConsultation(new Date());
				appMgr.update(d);
				}
				
			}
			List<ListeDocument> listeDoc = new ArrayList<ListeDocument>();
			// Création d'une nouvelle liste en important le nom de document
			// dans Alfresco.
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
			// dmsAccessLayer = vb.getDmsAccessLayer();
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
					if(vb.getDmsAccessLayer()!= null&&doc.getDocumentTypeUpload()!=null &&!doc.getDocumentTypeUpload().equals("Physique"))
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
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void goUp(){
		System.out.println("je suis dans goUp");
		ListeDocument doc = (ListeDocument) listDDM.getRowData();
		document = doc.getDocument();
		vb.setDocument(document);
		System.out
				.println("*******le nom est :*********"+document.getDocumentNom());
		
		int ordreCourant = listDDM.getRowIndex();
		int ordrePrecedent = listDDM.getRowIndex()-1;
		int ordreSuivant= listDDM.getRowIndex()+1;
		
		if (ordreCourant ==0)
			System.out.println("Rien");
		else 
		{
			
			System.out.println("ordrePrecedent:"+ordrePrecedent);
			listDDM.setRowIndex(ordrePrecedent);
			//listDDM.setRowIndex(arg0);
			//listDDM.setRowIndex(ordrePrecedent);
		}
	//	listDDM.
		
		
		
	//Document d1 = new Document();
		
		//d1=
			
		
		
		Initialize();
		
		
		
	}
	@SuppressWarnings("unchecked")
	public void goToReceptionPhysique(){
		List<ListeDocument> listeDoc = new ArrayList<ListeDocument>();
		if(listDDM.getWrappedData()!=null)
		listeDoc = (List<ListeDocument>) listDDM.getWrappedData();
		vb.setListeDocument(listeDoc);
		vb.setMasquerPanelDetailCourrier(false);
	}

	public DataModel chercher() {
		try {
			// list.add(appMgr.find(doc.getCourrier().getIdCourrier()));
			listDDM.setWrappedData(list);
			System.out
					.println("*******ChargementAvecSucces DocumentConsultationBean******");
			Initialize();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listDDM;
	}

	// Get Selection Rows
	
	public void getSelectionRow() {
		System.out.println("gestSelectionRow!!!!!");
		try {
			
			ListeDocument doc = (ListeDocument) listDDM.getRowData();
			document = doc.getDocument();
			vb.setDocument(document);
			// supprimer le doc s'il n'est pas consulter
			if (document.getDocumentProprietaire().equals(person.getId())
					&& document.getDocumentDateConsultation() == null) {
				status2 = false;
				// afficher message de l'impossibilité de supprimer un doc qui
				// est deja consulter.
			} else {
				status2 = true;
				setMessageDel(messageSource.getMessage("cantDelete",
						new Object[] {}, lm.createLocal()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void getSelectionRowConsult() {
		
		try {
			ListeDocument doc = (ListeDocument) listDDM.getRowData();
			document = doc.getDocument();
			vb.setDocument(document);
			// remplir la colone dateconsult apres la premiere consultation par
			// un autre personne que le proprietaire du document
//			if (!document.getDocumentProprietaire().equals(person.getId())
//					&& document.getDocumentDateConsultation() == null) {
//				document.setDocumentDateConsultation(new Date());
//				appMgr.update(document);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void getSelectionRowForUpdate() {
		try {
			ListeDocument doc = (ListeDocument) listDDM.getRowData();
			document = doc.getDocument();
			vb.setDocument(document);
			String typeDoc=document.getDocumentTypeUpload();
			if(typeDoc.equals("Physique"))
			vb.setUploadType("non");
			else{
				
					vb.setUploadType("local");
			}
			// passe vers la page de modification
			if (document.getDocumentProprietaire().equals(person.getId())
					&& document.getDocumentDateConsultation() == null) {
				status2 = true;
				vb.setAffichePanelModifUpload(true);
				vb.setAffichePanelAjoutUpload(false);
			}
			// afficher un message que le destinataire a lu le document
			else {
				status2 = false;
				setMessage(messageSource.getMessage("confirmModifyMail",
						new Object[] {}, lm.createLocal()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void downloadDocument() {
		System.out.println("<================== Dans downloadDocument ====================>");
		InputStream inputStream = null;
		try {
			ListeDocument doc = (ListeDocument) listDDM.getRowData();
			document = doc.getDocument();
			System.out.println("document ===============<> "+document);
			// ------------ couche acces GED -------------
			inputStream = ged.readDocument(document).getInputStream();
			docName = ged.readDocument(document).getName();
			// ------------ couche acces GED -------------
			
			System.out.println("Type Document =======> "+document.getDocumentType());
					

			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) context
					.getExternalContext().getResponse();
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
//			response.setContentType("application/pdf");
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
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteSelectedRow() {

//		try {
//			documents.remove(document);
//			appMgr.delete(document);
//			listDDM.setWrappedData(documents);
//			LogClass logClass = new LogClass();
//			logClass.addTrack(
//					"suppression",
//					"Evénement de log de suppression du document "
//							+ document.getIdDocument() + "-"
//							+ document.getDocumentReference(), vb.getPerson(),
//					"INFO", appMgr);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
		
		int i = -1;
		try {
			ListeDocument doc = (ListeDocument) listDDM.getRowData();
			document = doc.getDocument();
			vb.setDocument(document);
			System.out
					.println("*******SelectionSucces ConsulterDocBean*********");
			i = listDDM.getRowCount();
			System.out.println(i);
			if (i <= 1) {
				appMgr.delete(document);
				setN(1);

			} else {
				appMgr.delete(document);
				setN(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// fonction de suppression d'un courrier suite é la suppression de ses
	// documents
	public void SuppCourrier() {

		try {
			courrier = vb.getCourrier();
			// listTransaction =
			// appMgr.getTransactionByIdCourrier(courrier.getIdCourrier());
			for (int j = 0; j < listTransaction.size(); j++) {
				tr = listTransaction.get(j);

				try {
					tr.setTransactionSupprimer(false);
					appMgr.update(tr);

					System.out.println("Delete tr Terminée");
				} catch (Exception e) {
					System.out.println("Delete tr erreur");
				}
			}
		} catch (Exception e) {
			System.out.println("Delete tr erreur");
		}
		try {
			courrier.setCourrierSupprime(false);
			appMgr.update(courrier);
			System.out.println("DeleteTerminée");
		} catch (Exception e) {
			System.out.println("Delete cr erreur");
		}

	}

	public void modifier() {
		try {
			appMgr.update(document);
			vb.setDocument(document);
			status1 = true;
			System.out.println("ModifTerminee");
		} catch (Exception e) {
			status1 = false;
			System.out
					.println("*******ModifErreur DocumentModificationBean*******");
		}
	}
	public void modifierListeDocument() throws Exception{
		ListeDocument doc = (ListeDocument) listDDM.getRowData();
		document = doc.getDocument();
		appMgr.update(document);
		vb.setDocument(document);
		Initialize();
		System.out.println("document modifieée");
		
	}
	public void deleteSelectedDocument() {
		try {
			//System.out.println(documents.size());
			documents.remove(document);
			//System.out.println(documents.size());
			document.setDocumentSupprime(false);
			document.setDocumentSupprimeDate(new Date());
			document.setDocumentSupprimeDelegueId(vb.getPerson().getId());
			appMgr.update(document);
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

	public int navigation() {
		setN(1);
		return n;
	}

	public String remplire1() {
		System.out.println("***************Remplire1***************");
		return ("Remplire1");
	}

	public String remplire2() {
		System.out.println("***************Remplire2***************");
		return ("Remplire2");
	}

	// ************Getter & Setter********************

	public void setListDDM(DataModel listDDM) {
		this.listDDM = listDDM;
	}

	public DataModel getListDDM() {
		return listDDM;
	}

	/*@SuppressWarnings("unchecked")
	public long getRecords() {
		if (listDDM == null && listDDM.getWrappedData() == null)
			records = 0;
		else
			records = ((List<Document>) listDDM.getWrappedData()).size();
		return records;
	}*/

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

	public int getId2() {
		return id2;
	}

	public void setId2(int id2) {
		this.id2 = id2;
	}

	public List<Document> getList() {
		return list;
	}

	public void setList(List<Document> list) {
		this.list = list;
	}

	public void setRecords(long records) {
		this.records = records;
	}

	public void setIdDoc(int idDoc) {
		this.idDoc = idDoc;
	}

	public int getIdDoc() {
		return idDoc;
	}

	public void setN(int n) {
		this.n = n;
	}

	public int getN() {
		return n;
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

	public void setNbrDoc(int nbrDoc) {
		this.nbrDoc = nbrDoc;
	}

	public int getNbrDoc() {
		return nbrDoc;
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

	public Transaction getTr() {
		return tr;
	}

	public void setTr(Transaction tr) {
		this.tr = tr;
	}

	public boolean isStatus1() {
		return status1;
	}

	public void setStatus1(boolean status1) {
		this.status1 = status1;
	}

	public void setListTransaction(List<Transaction> listTransaction) {
		this.listTransaction = listTransaction;
	}

	public List<Transaction> getListTransaction() {
		return listTransaction;
	}

	public void setNomCourrier(String nomCourrier) {
		this.nomCourrier = nomCourrier;
	}

	public String getNomCourrier() {
		return nomCourrier;
	}

	public TransactionDestination getTrDest() {
		return trDest;
	}

	public void setTrDest(TransactionDestination trDest) {
		this.trDest = trDest;
	}

	public List<TransactionDestination> getListTrDest() {
		return listTrDest;
	}

	public void setListTrDest(List<TransactionDestination> listTrDest) {
		this.listTrDest = listTrDest;
	}

	public List<TransactionAnnotation> getListCrAnnot() {
		return listCrAnnot;
	}

	public void setListCrAnnot(List<TransactionAnnotation> listCrAnnot) {
		this.listCrAnnot = listCrAnnot;
	}

	public TransactionAnnotation getCrAnnot() {
		return crAnnot;
	}

	public void setCrAnnot(TransactionAnnotation crAnnot) {
		this.crAnnot = crAnnot;
	}

	public List<Liensdossier> getListLiens() {
		return listLiens;
	}

	public void setListLiens(List<Liensdossier> listLiens) {
		this.listLiens = listLiens;
	}

	public List<Liensdossier> getListLiensCr() {
		return listLiensCr;
	}

	public void setListLiensCr(List<Liensdossier> listLiensCr) {
		this.listLiensCr = listLiensCr;
	}

	public Liensdossier getLien() {
		return lien;
	}

	public void setLien(Liensdossier lien) {
		this.lien = lien;
	}

	public Ged getGed() {
		return ged;
	}

	public void setGed(Ged ged) {
		this.ged = ged;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Person getPerson() {
		return person;
	}

	public void setStatus2(boolean status2) {
		this.status2 = status2;
	}

	public boolean isStatus2() {
		return status2;
	}

	public String getMessageDel() {
		return messageDel;
	}

	public void setMessageDel(String messageDel) {
		this.messageDel = messageDel;
	}

	@SuppressWarnings("unchecked")
	public long getRecords() {
		
		
		if (listDDM != null
				&& listDDM.getWrappedData() != null)
			records = ((List<ListeDocument> ) listDDM.getWrappedData())
					.size();
		else
			records = 0;
		return records;
	}

}
