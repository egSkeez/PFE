package xtensus.beans.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import xtensus.beans.common.GBO.UserAgentProcessor;

@Component
@Scope("request")
public class MenuBean {
	@Autowired
	private VariableGlobale vb;
	@Autowired
	private UserAgentProcessor userAgent;
	
	public String interfaceParametrages() {
		System.out.println("is phone : "+userAgent.isPhone() );
		if(userAgent.isPhone() ==true)			
			return "passPhone";
		else
			return "pass";
		
	}

	public String interfaceAccueil() {
		if(userAgent.isPhone() == true)
		  return "passPhone";
		else
		return "pass";
	}

	public String interfaceAjouterCourrier() {
		vb.setRedirect("");
		vb.resetVG();
		vb.setExpNom(null);
		vb.setDestNom(null);
		vb.setListeDestinataire(null);
		vb.setFlagValise(false);
		vb.setSelectedItemNature(null);
		vb.setSelectedItemsTr(null);
		vb.setSelectedItemCategorie(null);
		vb.setListComposantDynamiqueNature(null);
		vb.setListComposantDynamiqueTransmission(null);
		vb.setListChequesSave(null);
		vb.setAoConsultation(null);
		return "pass";
	}

	public String interfaceRechercherCourrier() {
		vb.setRedirect("rechercheToCourrierDetails");
		

		System.out.println("******getDétails******* : " + vb.getRedirect());
		return "pass";
	}

	public String interfaceGestionDossier() {
		return "pass";
	}

	public String interfaceRechercherDossier() {
		return "pass";
	}

	public String interfaceAjouterContact() {
		return "pass";
	}

	public String interfaceRechercherContact() {
		return "pass";
	}

	public String interfaceRechercherCourrierAvance() {
		vb.setRechercheRowCount(null);
		vb.setRechercheCountVisited(false);
		return "pass";
	}

	public String interfaceRechercherDossierAvance() {
		return "pass";
	}

	public String interfaceGestionUtilisateur() {
		return "pass";
	}

	public String interfaceGestionGroupe() {
		return "pass";
	}

	public String interfaceGestionUnite() {
		return "pass";
	}

	public String interfacelistMailFax() {
		return "pass";
	}

	public String interfaceannuaireRechercheMulticritere() {
		return "pass";
	}

	public String interfaceGestionRole() {
		return "pass";
	}

	public String interfaceModifierMotDePasse() {
		return "pass";
	}

	public String interfaceGroupeMailing() {
		return "pass";
	}

	public String interfaceSujetMailing() {
		return "pass";
	}

	public String interfaceGestionArmoire() {
		return "pass";
	}

	public String interfaceBoiteArchive() {
		return "pass";
	}
	
	public String interfaceAOConsulatation() {
		vb.setAoConsultation(null);
		return "pass";
	}

	public String interfaceGestionValise() {
		return "pass";
	}


	public VariableGlobale getVb() {
		return vb;
	}

	public void setVb(VariableGlobale vb) {
		this.vb = vb;
	}

	// KHA : rapports :
	public String interfaceRapports() {
		return "pass";
	}
	
	public String interfaceMobile(){
		
		if(userAgent.isPhone() ==true)			
			return "passPhone";
		else
			return "passDesktop";
		
	}
	
	public String interfaceGestionMailStructure(){
		return "pass";
	}

}
