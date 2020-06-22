package xtensus.workflow.beans;

import xtensus.workflow.dao.JBPMDao;
import xtensus.workflow.handlers.TraitementEtapeSuivant;
import xtensus.workflow.handlers.TraitementStartProcessus;

public class JBPMAccessProcessBean {

	private String msg;
	private long idActuel;
	private long idDemande;
	private boolean initial = true;
	String processId = "casD_A_TF";

	public JBPMAccessProcessBean() {

	}

	public TraitementEtapeSuivant startProcessTraitementCourrier(String cas,
			long id1) {
		initial = false;
		JBPMDao dao = new JBPMDao();
		TraitementEtapeSuivant resultatSuivant = new TraitementEtapeSuivant();
		resultatSuivant = dao.requestProcessNextStepTraitement(cas, id1);
		return resultatSuivant;
	}
	// C*
	public TraitementEtapeSuivant refuseProcessTraitementCourrier(String cas,
			long id1) {
		initial = false;
		JBPMDao dao = new JBPMDao();
		TraitementEtapeSuivant resultatSuivant = new TraitementEtapeSuivant();
		resultatSuivant = dao.requestProcessBeforeStepTraitement(cas, id1);
		return resultatSuivant;
	}
	// C*
	public TraitementStartProcessus startProcess(String cas) {
		initial = false;
		JBPMDao dao = new JBPMDao();
		TraitementStartProcessus resultatSuivant = new TraitementStartProcessus();
		resultatSuivant = dao.startNewProcess(cas);
		return resultatSuivant;
	}

	public TraitementEtapeSuivant actuelNode(String cas, long id) {
		initial = false;
		JBPMDao dao = new JBPMDao();
		TraitementEtapeSuivant resultat = new TraitementEtapeSuivant();
		resultat = dao.requestProcessNodeTraitement(cas, id);
		return resultat;
	}

	/******* Methode test HB ******************/
	public String start() {
		initial = false;
		JBPMDao dao = new JBPMDao();
		msg = dao.requestProcess(processId, idActuel, idDemande);
		return "calculated";
	}

	public String getProcessLastNode(String cas){
		JBPMDao dao = new JBPMDao();
		return dao.requestLastNodeInProcess(cas);
	}
	
	
	
	
	
	/************** Getters && Setters ******************/
	public long getIdActuel() {
		return idActuel;
	}

	public void setIdActuel(long idActuel) {
		this.idActuel = idActuel;
	}

	public long getIdDemande() {
		return idDemande;
	}

	public void setIdDemande(long idDemande) {
		this.idDemande = idDemande;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean getInitial() {
		return initial;
	}

	public String reset() {
		initial = true;
		msg = null;
		idActuel = 0;
		idDemande = 0;
		return "reset";
	}

}
