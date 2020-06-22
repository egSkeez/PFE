package xtensus.beans.common.GBO;

import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import xtensus.aop.LogClass;
import xtensus.beans.common.VariableGlobale;
import xtensus.services.ApplicationManager;

@Component
@Scope("request")
public class DeconnexionBean {
	@Autowired
	private VariableGlobale vb;
	private ApplicationManager appMgr;

	public DeconnexionBean() {
	}

	@Autowired
	public DeconnexionBean(
			@Qualifier("applicationManager") ApplicationManager appMgr) {
		this.appMgr = appMgr;
	}

	public void deconnexion() {
		try {
			ExternalContext context = FacesContext.getCurrentInstance()
					.getExternalContext();
			RequestDispatcher dispatcher = ((ServletRequest) context
					.getRequest())
					.getRequestDispatcher("/j_spring_security_logout");
			dispatcher.forward((ServletRequest) context.getRequest(),
					(ServletResponse) context.getResponse());
			FacesContext.getCurrentInstance().getResponseComplete();
			LogClass logClass = new LogClass();
			logClass.addTrack("deconnexion", "Evénement de log de déconnexion",
					vb.getPerson(), "INFO", appMgr);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public VariableGlobale getVb() {
		return vb;
	}

	public void setVb(VariableGlobale vb) {
		this.vb = vb;
	}

}
