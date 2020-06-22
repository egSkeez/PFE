package xtensus.beans.common.GBO;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import xtensus.aop.LogClass;
import xtensus.beans.common.VariableGlobale;
import xtensus.entity.Document;
import xtensus.services.ApplicationManager;

@Component
@Scope("request")
public class DocumentModificationBean {
	private ApplicationManager appMgr;
	private Document doc;

	@Autowired
	private VariableGlobale vb;

	@Autowired
	public DocumentModificationBean(
			@Qualifier("applicationManager") ApplicationManager appMgr) {
		this.appMgr = appMgr;
		doc = new Document();
		System.out
				.println("**************BeanInjecte DocumentModificationBean *********");
	}

	@PostConstruct
	public void Initialize() {

		try {
			vb.setMasquerPanelDetailCourrier(false);
			setDoc(vb.getDocument());
			System.out.println("****ChargementDocumentModificationBean****");
		} catch (Exception e) {
			System.out
					.println("***ErreurDeChargementDocumentModificationBean***");
		}

	}

	public void modifier() {
		try {
			appMgr.update(doc);
			vb.setDocument(doc);
			System.out
					.println("*******ModificationSucces DocumentModificationBean*********");
			vb.setDocument(doc);
			LogClass logClass = new LogClass();
			logClass.addTrack(
					"modification",
					"Evénement de log de modification du document "
							+ doc.getIdDocument() + "-" + doc.getDocumentNom(),
					vb.getPerson(), "INFO", appMgr);

		} catch (Exception e) {
			System.out
					.println("*******ModifErreur DocumentModificationBean*******");
		}
	}

	// //////////////////////////
	public ApplicationManager getAppMgr() {
		return appMgr;
	}

	public void setAppMgr(ApplicationManager appMgr) {
		this.appMgr = appMgr;
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public VariableGlobale getVb() {
		return vb;
	}

	public void setVb(VariableGlobale vb) {
		this.vb = vb;
	}

}
