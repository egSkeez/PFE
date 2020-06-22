package xtensus.faxmail.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import xtensus.beans.common.VariableGlobale;
import xtensus.dao.utils.DMSConnexionImplement;
import xtensus.entity.Fax;
import xtensus.entity.Mail;
import xtensus.faxmail.beans.AttachmentFileBean;

import com.xtensus.dal.connection.ConnectionParameters;
import com.xtensus.dal.core.DMSAccessLayer;
import com.xtensus.dal.core.DMSDocument;
import com.xtensus.dal.core.Document;
import com.xtensus.dal.core.DocumentContext;
import com.xtensus.dal.exceptions.DownloadException;

/**
 * 
 * @author HB , Xtensus Tunisie
 * 
 */

public class GedUtils {
	private static HashMap<String, String> mimeTypeMapping;

	static {
		mimeTypeMapping = new HashMap<String, String>();

		mimeTypeMapping.put("zip", "application/zip");
		mimeTypeMapping.put("rar", "application/x-rar-compressed");
		mimeTypeMapping.put("gif", "image/gif");
		mimeTypeMapping.put("vsd", "application/x-visio");
		mimeTypeMapping.put("jpeg", "image/jpeg");
		mimeTypeMapping.put("jpg", "image/jpeg");
		mimeTypeMapping.put("jpe", "image/jpeg");
		mimeTypeMapping.put("png", "image/png");
		mimeTypeMapping.put("pdf", "application/pdf");
		mimeTypeMapping.put("docx", "application/docx");
		mimeTypeMapping.put("doc", "application/doc");
		mimeTypeMapping.put("xlsx", "application/xlsx");
		mimeTypeMapping.put("xls", "application/xls");
	}

	public String getMimeType(String ext) {
		return mimeTypeMapping.get(ext.toLowerCase());
	}

	private static DMSAccessLayer accessLayer;
	private static DMSAccessLayer dmsAccessLayer;

	@Autowired
	private VariableGlobale vb;

	public GedUtils() {

	}

	public GedUtils(String url, String login, String password) {
		try {
			// **
			Resource rsrc = new ClassPathResource("/paramAlfresco.properties");
			String pathConfigFile = rsrc.getFile().getAbsolutePath();
			Properties props = new Properties();
			props.load(new FileInputStream(pathConfigFile));
			// **
			// get context path
			ExternalContext jsfContext = FacesContext.getCurrentInstance()
					.getExternalContext();
			ServletContext servletContext = (ServletContext) jsfContext
					.getContext();
			String webContentRoot = servletContext.getRealPath("/");
			// String res = webContentRoot.substring(0, 16) + "GBO_v1.k";
			// System.out.println(res);
			// webContentRoot = res;
			// fichier xml
			String pathConfigFileconfig = webContentRoot
					+ "\\WEB-INF\\GEDConfig\\config.xml";
			String pathConfigFileconfigFaxMail = webContentRoot
					+ "\\WEB-INF\\GEDConfig\\configFaxMail.xml";
			// Parametres de connexion au ged et emplacement du fihier de
			// configuration de la couche dacces au ged
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put(ConnectionParameters.LOGIN, props.getProperty("alfresco.login"));
			parameters.put(ConnectionParameters.PASSWORD, props.getProperty("alfresco.password"));
			parameters.put(ConnectionParameters.URL, url);
			parameters.put(ConnectionParameters.FILE_CONFIG_PATH,
					pathConfigFileconfigFaxMail);
			// Instantiation de la couche dacces au ged
			accessLayer = new DMSAccessLayer(parameters);

			// Parametres de connexion au ged et emplacement du fihier de
			// configuration de la couche dacces au ged
			String URL = props.getProperty("alfresco.Url");
			String namingConfigFilePath = pathConfigFileconfig;
			String userName = props.getProperty("alfresco.login");
			String mdp = props.getProperty("alfresco.password");
			dmsAccessLayer = DMSConnexionImplement.getConnexionGed(userName,
					mdp, URL, namingConfigFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void saveMail(Mail mail) {

		System.out.println("recuperation du contexte du document mail ...");
		DocumentContext mailContext = accessLayer.getDocumentContext("mail");

		Pdf pdf = new Pdf();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		pdf.createMailDocument(mail, outputStream);
		byte[] bs = outputStream.toByteArray();
		InputStream inputStream = new ByteArrayInputStream(bs);
		long size = bs.length;

		// Creation des documents a partir de leurs parametres
		System.out.println("creation du document mail...");
		Document mailDocument = mailContext.getDocument(
				Utils.format(mail.getMailFromAddress()),
				Utils.format(mail.getMailSubject()),
				Utils.format(mail.getMailReceivedDate().toString()));

		// La couche d'acces cree un document dans le ged
		System.out.println("upload du document mail ...");
		accessLayer.create(mailDocument, inputStream, size);

	}

	public void saveMailDocument(Mail mail, int refCourrier, int refDocument) {

		System.out.println("recuperation du contexte du document mail ...");
		DocumentContext mailContext = dmsAccessLayer.getDocumentContext("doc");

		Pdf pdf = new Pdf();

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		pdf.createMailDocument(mail, outputStream);
		byte[] bs = outputStream.toByteArray();
		InputStream inputStream = new ByteArrayInputStream(bs);
		long size = bs.length;

		// Creation des documents a partir de leurs parametres
		System.out.println("creation du document mail...");
		Document mailDocument = mailContext.getDocument(refCourrier,
				refDocument);

		// La couche d'acces cree un document dans le ged
		System.out.println("upload du document mail ...");
		dmsAccessLayer.create(mailDocument, inputStream, size);

	}

	public void saveFax(Fax fax) {

		System.out.println("recuperation du contexte du document fax ...");
		DocumentContext mailContext = accessLayer.getDocumentContext("fax");

		Pdf pdf = new Pdf();
		File document = pdf.createFaxDocument(fax);
		try {
			InputStream inputStream = new FileInputStream(document);
			long size = document.length();

			// Creation des documents a partir de leurs parametres
			System.out.println("creation du document fax...");
			Document faxDocument = mailContext.getDocument(
					Utils.format(fax.getFaxSenderNumber()),
					Utils.format(fax.getFaxReceivedDate().toString()));

			// La couche d'acces cree un document dans le ged
			System.out.println("upload du document fax ...");
			accessLayer.create(faxDocument, inputStream, size);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void saveAttachFile(AttachmentFileBean attachmentFile) {
		InputStream inputStream = null;
		long size = 0;

		String parentFolderName = "";
		String date = new SimpleDateFormat("yyyyMMdd").format(attachmentFile
				.getDate());
		parentFolderName = "Mail_Attachment_" + date + "_"
				+ attachmentFile.getExpediteur();

		DocumentContext attachmentFileContext = accessLayer
				.getDocumentContext("email_attachment");

		Document attachmentFileDocument = attachmentFileContext.getDocument(
				Utils.format(attachmentFile.getName()),
				Utils.format(parentFolderName));

		byte[] byteArray = attachmentFile.getByteArray();
		size = byteArray.length;
		inputStream = new ByteArrayInputStream(byteArray);
		accessLayer.create(attachmentFileDocument, inputStream, size);
	}

	public void saveAttachFileMail(AttachmentFileBean attachmentFile,
			int refCourrier, int refDocument) {
		InputStream inputStream = null;
		long size = 0;
		DocumentContext docContext = dmsAccessLayer.getDocumentContext("doc");
		Document attachmentFileDocument = docContext.getDocument(refCourrier,
				refDocument);
		String mime = "";
        if(vb.getDocumentType().length() > 0){
        	String extension = vb.getDocumentType().substring(1);
        	mime = getMimeType(extension);
        }
		if (mime != "") {
			attachmentFileDocument.setMimetype(mime);
		}
		vb.setDocumentType("");
		byte[] byteArray = attachmentFile.getByteArray();
		size = byteArray.length;
		inputStream = new ByteArrayInputStream(byteArray);
		dmsAccessLayer.create(attachmentFileDocument, inputStream, size);
	}

	public InputStream downloadAttachFile(String filename,
			String parentfoldername) {

		InputStream inputStream = null;

		try {

			DocumentContext attachmentFileContext = accessLayer
					.getDocumentContext("email_attachment");

			System.out.println("filename : " + filename);
			System.out.println("parentfoldername : " + parentfoldername);

			Document attachmentFileDocument = attachmentFileContext
					.getDocument(Utils.format(filename),
							Utils.format(parentfoldername));

			DMSDocument dmsDocument = accessLayer.read(attachmentFileDocument);
			inputStream = dmsDocument.getInputStream();

		} catch (DownloadException e) {
			e.printStackTrace();
		}

		return inputStream;

	}

	public void saveFaxRecu(Fax fax) {

		System.out
				.println("recuperation du contexte du document faxesrecus ...");
		DocumentContext faxContext = accessLayer.getDocumentContext("fax");

		Pdf pdf = new Pdf();
		File document = pdf.createFaxDocument(fax);
		try {
			InputStream inputStream = new FileInputStream(document);
			long size = document.length();

			// Creation des documents a partir de leurs parametres
			System.out.println("creation du document faxesrecus...");
			Document faxDocument = faxContext.getDocument(
					Utils.format(fax.getFaxSenderNumber()),
					Utils.format(fax.getFaxReceivedDate().toString()));

			// La couche d'acces cree un document dans le ged
			System.out.println("upload du document faxesrecus ...");
			accessLayer.create(faxDocument, inputStream, size);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public InputStream downloadFax(Fax selectedFax) {

		InputStream inputStream = null;

		try {

			DocumentContext FaxContext = accessLayer
					.getDocumentContext("faxesrecus");

			Document FaxDocument = FaxContext.getDocument(
					selectedFax.getFaxId(), selectedFax.getFaxReceivedDate(),
					selectedFax.getFaxReceivedDate());

			DMSDocument dmsDocument = accessLayer.read(FaxDocument);
			inputStream = dmsDocument.getInputStream();

		} catch (DownloadException e) {
			e.printStackTrace();
		}

		return inputStream;
	}

	public DMSAccessLayer getDmsAccessLayer() {
		return dmsAccessLayer;
	}

	public void setDmsAccessLayer(DMSAccessLayer dmsAccessLayer) {
		this.dmsAccessLayer = dmsAccessLayer;
	}

	public VariableGlobale getVb() {
		return vb;
	}

	public void setVb(VariableGlobale vb) {
		this.vb = vb;
	}

}
