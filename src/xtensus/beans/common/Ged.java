package xtensus.beans.common;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.chemistry.opencmis.commons.exceptions.CmisInvalidArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import xtensus.services.ScannerService;

import com.xtensus.dal.core.DMSAccessLayer;
import com.xtensus.dal.core.DMSDocument;
import com.xtensus.dal.core.Document;
import com.xtensus.dal.core.DocumentContext;
import com.xtensus.dal.exceptions.DeleteException;
import com.xtensus.dal.exceptions.DownloadException;
import com.xtensus.dal.exceptions.UpdateException;

@Component
@Scope("request")
public class Ged {
	private static HashMap<String, String> mimeTypeMapping;

	static {
		mimeTypeMapping = new HashMap<String, String>();

			
		mimeTypeMapping.put("gif", "image/gif");
		mimeTypeMapping.put("jpeg", "image/jpeg");
		mimeTypeMapping.put("jpg", "image/jpeg");
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

	DMSAccessLayer dmsAccessLayer;

	@Autowired
	private VariableGlobale vbg;

	@Autowired
	private ScannerService scannerService;

	public void uploadDocument(Object document) {
		try{
		System.out.println("Dans uploadDocument ");
		System.out.println("vbg.getUploadType() ::" +vbg.getUploadType());
		if (!vbg.getUploadType().equalsIgnoreCase("non")) {
			dmsAccessLayer = vbg.getDmsAccessLayer();
				System.out.println("aprs dmsAccessLayer");
			if (document instanceof xtensus.entity.Document) {
				xtensus.entity.Document doc = new xtensus.entity.Document();
				doc = (xtensus.entity.Document) document;
				DocumentContext docContext = dmsAccessLayer
						.getDocumentContext("doc");
			
				Document docDocument = docContext.getDocument(vbg.getCourrier()
						.getIdCourrier(), doc.getIdDocument());
				String mime = "";
				
				if (vbg.getDocumentType() != null
						&& vbg.getDocumentType().length() > 0) {
					String extension = vbg.getDocumentType().substring(1);
					mime = getMimeType(extension);
				}
				if (mime != "") {
					docDocument.setMimetype(mime);
				}
				// ** propre Khaled
				// System.out.println("____Name Before : " +
				// docDocument.getName());
				// String newName = docDocument.getName() + ".jpg";
				// docDocument.setName(newName);
				// System.out.println("____Name After : " +
				// docDocument.getName());
				// ** propre khaled
				//[AH]
				dmsAccessLayer.create(docDocument, getData(), getSizeData());
				System.out.println("°°* Path==============>:  "+ docContext.getParameters().get("rootpath"));

			}/*
			 * else if (document instanceof Cautiondefinitive) {
			 * Cautiondefinitive caution = new Cautiondefinitive(); caution =
			 * (Cautiondefinitive) document;
			 * 
			 * DocumentContext cautionContext = dmsAccessLayer
			 * .getDocumentContext("caution"); Document cautionDocument =
			 * cautionContext.getDocument(caution
			 * .getMarche().getMarcheReference(), caution
			 * .getCautionDefinitveRefrence()); dmsAccessLayer
			 * .create(cautionDocument, getData(), getSizeData());
			 * 
			 * }
			 */
		} 
		}catch (CmisInvalidArgumentException cmi) {
			vbg.setTypeUploadFichier(true);
			cmi.printStackTrace();
		}

		vbg.setUploadedData(null);
		vbg.setUploadType("");
		scannerService.setData(null);
	}

	public void updateDocument(Object document) throws UpdateException {
		if (!vbg.getUploadType().equalsIgnoreCase("non")) {
			dmsAccessLayer = vbg.getDmsAccessLayer();

			if (document instanceof xtensus.entity.Document) {
				xtensus.entity.Document doc = new xtensus.entity.Document();
				doc = (xtensus.entity.Document) document;
				DocumentContext docContext = dmsAccessLayer
						.getDocumentContext("doc");
				Document docDocument = docContext.getDocument(doc.getCourrier()
						.getIdCourrier(), doc.getIdDocument());
				String mime = "";
				if (vbg.getDocumentType().length() > 0) {
					String extension = vbg.getDocumentType().substring(1);
					mime = getMimeType(extension);
				}
				if (mime != "") {
					docDocument.setMimetype(mime);
				}
				System.out.println(getData() + " " + getSizeData());
				dmsAccessLayer.update(docDocument, getData(), getSizeData());

			}/*
			 * else if (document instanceof Cautiondefinitive) {
			 * Cautiondefinitive caution = new Cautiondefinitive(); caution =
			 * (Cautiondefinitive) document;
			 * 
			 * DocumentContext cautionContext = dmsAccessLayer
			 * .getDocumentContext("caution"); Document cautionDocument =
			 * cautionContext.getDocument(caution
			 * .getMarche().getMarcheReference(), caution
			 * .getCautionDefinitveRefrence()); dmsAccessLayer
			 * .create(cautionDocument, getData(), getSizeData());
			 * 
			 * }
			 */
		}
		vbg.setUploadedData(null);
		vbg.setUploadType("");
		scannerService.setData(null);
	}

	public void deleteDocument(Object document) {
		try {
			dmsAccessLayer = vbg.getDmsAccessLayer();
			if (document instanceof xtensus.entity.Document) {
				xtensus.entity.Document doc = new xtensus.entity.Document();
				doc = (xtensus.entity.Document) document;
				DocumentContext docContext = dmsAccessLayer
						.getDocumentContext("doc");
				Document docDocument = docContext.getDocument(doc.getCourrier()
						.getIdCourrier(), doc.getIdDocument());
				dmsAccessLayer.delete(docDocument);

			}/*
			 * else if (document instanceof Cautiondefinitive) {
			 * Cautiondefinitive caution = new Cautiondefinitive(); caution =
			 * (Cautiondefinitive) document;
			 * 
			 * DocumentContext cautionContext = dmsAccessLayer
			 * .getDocumentContext("caution"); Document cautionDocument =
			 * cautionContext.getDocument(caution
			 * .getMarche().getMarcheReference(), caution
			 * .getCautionDefinitveRefrence()); dmsAccessLayer
			 * .create(cautionDocument, getData(), getSizeData());
			 * 
			 * }
			 */

		} catch (DeleteException e) {
			e.printStackTrace();
		}

	}

	public DMSDocument readDocument(Object document)
			throws xtensus.beans.common.DownloadException {
		try {
			dmsAccessLayer = vbg.getDmsAccessLayer();
			if (document instanceof xtensus.entity.Document) {
				xtensus.entity.Document doc = (xtensus.entity.Document) document;
				DocumentContext docContext = dmsAccessLayer
						.getDocumentContext("doc");

				System.out.println("°°*  :  "
						+ docContext.getParameters().get("rootpath"));
				Document docDocument;
				if (doc.getDocumentIdDocumentOriginal() == null) {
					docDocument = docContext.getDocument(doc.getCourrier()
							.getIdCourrier(), doc.getIdDocument());
				} else {
					docDocument = docContext.getDocument(
							doc.getDocumentIdCourrierOriginal(),
							doc.getDocumentIdDocumentOriginal());
				}
				return dmsAccessLayer.read(docDocument);

			} /*
			 * else if (document instanceof xtensus.entity.Cautiondefinitive) {
			 * xtensus.entity.Cautiondefinitive caution =
			 * (xtensus.entity.Cautiondefinitive) document;
			 * 
			 * DocumentContext cautionContext = dmsAccessLayer
			 * .getDocumentContext("caution"); Document cautionDocument =
			 * cautionContext.getDocument(caution
			 * .getMarche().getMarcheReference(), caution
			 * .getCautionDefinitveRefrence()); return
			 * dmsAccessLayer.read(cautionDocument);
			 * 
			 * }
			 */
		} catch (DownloadException e) {
			throw new xtensus.beans.common.DownloadException();
		}

		return null;

	}

	public void uploadDocumentPhysique(Object document) {
		if (!vbg.getUploadType().equalsIgnoreCase("non")) {
			dmsAccessLayer = vbg.getDmsAccessLayer();

			if (document instanceof xtensus.entity.Document) {
				xtensus.entity.Document docPhysique = new xtensus.entity.Document();
				docPhysique = (xtensus.entity.Document) document;
				DocumentContext docContext = dmsAccessLayer
						.getDocumentContext("docPhysique");
				Document docDocument = docContext.getDocument(
						vbg.getTransactionDestination().getId()
								.getIdTransaction(),
						docPhysique.getIdDocument());

				String mime = "";
				if (vbg.getDocumentType().length() > 0) {
					String extension = vbg.getDocumentType().substring(1);
					mime = getMimeType(extension);
				}
				if (mime != "") {
					docDocument.setMimetype(mime);
				}
				//
				dmsAccessLayer.create(docDocument, getData(), getSizeData());

			}

		}

		vbg.setUploadedData(null);
		vbg.setUploadType("");
		scannerService.setData(null);
	}

	public void deleteDocumentPhysique(Object document) {
		try {
			dmsAccessLayer = vbg.getDmsAccessLayer();
			if (document instanceof xtensus.entity.Document) {
				xtensus.entity.Document docPhysique = new xtensus.entity.Document();
				docPhysique = (xtensus.entity.Document) document;
				DocumentContext docContext = dmsAccessLayer
						.getDocumentContext("docPhysique");
				Document docDocument = docContext.getDocument(
						vbg.getTransactionDestination().getId()
								.getIdTransaction(),
						docPhysique.getIdDocument());
				dmsAccessLayer.delete(docDocument);

			}

		} catch (DeleteException e) {
			e.printStackTrace();
		}

	}

	public DMSDocument readDocumentPhysique(Object document)
			throws xtensus.beans.common.DownloadException {
		try {
			dmsAccessLayer = vbg.getDmsAccessLayer();
			if (document instanceof xtensus.entity.Document) {
				xtensus.entity.Document docPhysique = (xtensus.entity.Document) document;
				DocumentContext docContext = dmsAccessLayer
						.getDocumentContext("docPhysique");

				Document docDocument;
				if (docPhysique.getDocumentIdDocumentOriginal() == null) {
					docDocument = docContext.getDocument(vbg.getTransaction()
							.getTransactionId(), docPhysique.getIdDocument());
				} else {
					docDocument = docContext.getDocument(vbg.getTransaction()
							.getTransactionId(), docPhysique
							.getDocumentIdDocumentOriginal());
				}

				return dmsAccessLayer.read(docDocument);

			}
		} catch (DownloadException e) {
			throw new xtensus.beans.common.DownloadException();
		}

		return null;

	}

	private InputStream getData() {
		if (vbg.getUploadType().equalsIgnoreCase("local")) {
			return new ByteArrayInputStream(vbg.getUploadedData());
		}

		// System.out.println("Scanned data to UPLOAD in Alfresco : " +
		// scannerService.getData().length);
		//[AH] pour le cas du Scanner récupérer le fichier enregistré à partir du Scan
		
		//return new ByteArrayInputStream(scannerService.getData());
		return new ByteArrayInputStream(vbg.getScannedData());
	}

	private long getSizeData() {
		if (vbg.getUploadType().equalsIgnoreCase("local")){
			return vbg.getUploadedData().length;
		}
		//[AH]
		return vbg.getScannedData().length;
		//return scannerService.getData().length;
	}

	public void setVg(VariableGlobale vbg) {
		this.vbg = vbg;
	}

	public VariableGlobale getVbg() {
		return vbg;
	}

}
