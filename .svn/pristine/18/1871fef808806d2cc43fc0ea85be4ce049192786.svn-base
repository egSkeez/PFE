package xtensus.beans.common.GBO;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import xtensus.beans.common.VariableGlobale;
import xtensus.services.ApplicationManager;

@Component()
@Scope("request")
public class AideEnLigneBean {

	private ApplicationManager appMgr;

	public boolean statusAide;
	@Autowired
	private VariableGlobale vb;

	private String message;
	private String titreInterface;

	@Autowired
	public AideEnLigneBean(
			@Qualifier("applicationManager") ApplicationManager appMgr) {
		this.setAppMgr(appMgr);

		System.out.println("*************Bean Injecte***************");

	}

	public AideEnLigneBean() {

	}

	public void resultat() {
		statusAide = false;
		message = "";
		titreInterface = "";
		try {
			// get context path
			ExternalContext jsfContext = FacesContext.getCurrentInstance()
					.getExternalContext();
			ServletContext servletContext = (ServletContext) jsfContext
					.getContext();
			String webContentRoot = jsfContext.getRequestServletPath();
			titreInterface = webContentRoot.substring(5);
			int nb = appMgr.aideByInterface(titreInterface).size();
			if (nb != 0) {
				String res = " ";
				try {
					res = appMgr.aideByInterface(titreInterface).get(0)
					.getAideContenuFr();
				} catch (Exception e) {
				}
				setMessage(res);
				statusAide = true;
			} else {
				message = "Aide non disponible, veuillez consulter l'administrateur du systéme.";
			}
			System.out
					.println("*******Chargement Avec Succes   AideEnLigneBean******");
		} catch (Exception e) {
			System.out
					.println("*******Erreur De Chargement AideEnLigneBean*******");
		}
	}

	public boolean isStatusAide() {
		return statusAide;
	}

	public void setStatusAide(boolean statusAide) {
		this.statusAide = statusAide;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setVb(VariableGlobale vb) {
		this.vb = vb;
	}

	public VariableGlobale getVb() {
		return vb;
	}

	public void setAppMgr(ApplicationManager appMgr) {
		this.appMgr = appMgr;
	}

	public ApplicationManager getAppMgr() {
		return appMgr;
	}

	public void setTitreInterface(String titreInterface) {
		this.titreInterface = titreInterface;
	}

	public String getTitreInterface() {
		return titreInterface;
	}

}
