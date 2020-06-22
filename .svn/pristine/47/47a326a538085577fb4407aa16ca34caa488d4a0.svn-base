package xtensus.beans.common.GBO0;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import xtensus.aop.LogClass;
import xtensus.beans.common.LanguageManagerBean;
import xtensus.beans.common.VariableGlobale;
import xtensus.entity.Variables;
import xtensus.services.ApplicationManager;

@Component()
@Scope("request")
public class VariablesGestionBean {

	private Variables variable;
	private String divajouter;
	private String divmodifier;
	private String titre;
	private String infoChampObligatoir;
	private DataModel listVariables;
	private List<Variables> listVariable;
	private List<Variables> copylistVariable;
	private long records = 0;
	private boolean openaccor;
	private boolean openaccor1;
	private String hidebuttonValidateUser;
	private String hidebuttonDelUpUser;
	private String hideAccor;
	private boolean msgerror;
	private String titleaccor;
	private String selectedValue;
	Integer[] ouiNonValues  = {2,3,4,5,11,12,13,14,15,16,17,19,20,21,22,23,24,25,26};

	@Autowired
	private VariableGlobale vb;
	// fixe

	private ApplicationManager appMgr;
	@Autowired
	private LanguageManagerBean lm;
	@Autowired
	private MessageSource messageSource;
	private String message;
	private boolean status;
	private boolean status1;
	private boolean status2;
	private boolean status3;
	private Variables variable2;
	private boolean showSelectList;

	@Autowired
	public VariablesGestionBean(
			@Qualifier("applicationManager") ApplicationManager appMgr) {

		this.appMgr = appMgr;
		variable = new Variables();
		variable2 = new Variables();
		listVariables = new ListDataModel();
		listVariable = new ArrayList<Variables>();
		copylistVariable = new ArrayList<Variables>();
		divajouter = "inline";
		divmodifier = "none";
		infoChampObligatoir = "inline";
		openaccor = false;
		openaccor1 = false;
		msgerror = true;
		hidebuttonValidateUser = "inline";
		hidebuttonDelUpUser = "none";
		hideAccor = "none";
		System.out
				.println("*************Bean VariableGestionBean Injecte***************");
	}

	@PostConstruct
	public void Initialize() {
		titre = "Ajouter variable";
		listVariable = new ArrayList<Variables>();
		copylistVariable = new ArrayList<Variables>();
		try {
			listVariable = appMgr.getList(Variables.class);
			if (vb.isInRestoration()) {
				listVariable.remove(listVariable.size()-1);
				listVariable.remove(listVariable.size()-1);
			} else {
				System.out.println("Non");
			}
			for (int i = listVariable.size(); i > 0; i--) {
				variable2 = listVariable.get(i - 1);
				copylistVariable.add(variable2);
				variable2 = new Variables();
			}
		} catch (Exception e) {
			System.out.println("erreur");
			e.printStackTrace();
		}
		listVariables.setWrappedData(copylistVariable);
	}

	public void save() {
		status = false;
		status3 = false;
		try {
			
			appMgr.insert(variable);
			setMessage(messageSource.getMessage("confirmInsert",
					new Object[] {}, lm.createLocal()));
			status = true;
			// -méthode save()
			LogClass logClass = new LogClass();
			logClass.addTrack(
					"ajout",
					"Evénement de log d'ajout de la variable "
							+ variable.getVariablesId() + "-"
							+ variable.getVariablesLibelle(), vb.getPerson(),
					"INFO", appMgr);

			Initialize();
			System.out
					.println("***************Succes VariablesGestionBean***************");
		} catch (Exception e) {
			status3 = true;
			System.out
					.println("***************Erreur VariablesGestionBean***************");
		}

	}

	// Get Selection Rows
	public void getSelectionRow() {
		
		System.out.println(divajouter);
		System.out.println(divmodifier);
		try {
			variable = (Variables) listVariables.getRowData();
			
			if(Arrays.asList(ouiNonValues).contains(variable.getVariablesId())){
				showSelectList = true;
			}else{
				showSelectList = false;
			}
			vb.setVariables(variable);
			// -méthode getSelectionRow()
			LogClass logClass = new LogClass();
			logClass.addTrack(
					"consultation",
					"Evénement de log de consultation de la variable "
							+ variable.getVariablesId() + "-"
							+ variable.getVariablesLibelle(), vb.getPerson(),
					"INFO", appMgr);

			System.out
					.println("*******Selection VariablesGestionBean*********");
			Initialize();

		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("*******ErreurDeSelection VariablesGestionBean*******");
		}
		divajouter = "none";
		divmodifier = "inline";
		infoChampObligatoir = "inline";
		titre = "Modifier variable";
		titleaccor =messageSource.getMessage("Consultationvariable",new Object[] {}, lm.createLocal());
		openaccor = true;
		openaccor1 = true;
		msgerror = false;
		hideAccor = "inline";
		hidebuttonValidateUser = "none";
		hidebuttonDelUpUser = "inline";

	}

	public void updateSelectedRow() {
		status1 = false;
		status2 = false;

		try {
			variable = vb.getVariables();
			variable.setVaraiablesValeur(selectedValue);
			appMgr.update(variable);
			status1 = true;
			setMessage(messageSource.getMessage("confirmInsert",
					new Object[] {}, lm.createLocal()));
			System.out.println("UpdateTerminee");
			// -méthode updateSelectedRow()
			LogClass logClass = new LogClass();
			logClass.addTrack("modification",
					"Evénement de log de modification de la variable "
							+ vb.getVariables().getVariablesId() + "-"
							+ vb.getVariables().getVariablesLibelle(),
					vb.getPerson(), "INFO", appMgr);

			listVariable = new ArrayList<Variables>();
			Initialize();
			variable = new Variables();
		} catch (Exception e) {
			status2 = true;
			setMessage(messageSource.getMessage("erreurUpdate",
					new Object[] {}, lm.createLocal()));
		}
		divajouter = "inline";
		divmodifier = "none";
		infoChampObligatoir = "inline";
		titre = "Ajouter variable";
		titleaccor = messageSource.getMessage("Ajoutvariable",new Object[] {}, lm.createLocal());

	}

	public void deleteSelectedRow() {

		try {
			variable = vb.getVariables();
			appMgr.delete(variable);
			System.out.println("DeleteTerminee");
			// -méthode deleteSelectedRow
			LogClass logClass = new LogClass();
			logClass.addTrack(
					"suppression",
					"Evénement de log de suppression de la variable "
							+ variable.getVariablesId() + "-"
							+ variable.getVariablesLibelle(), vb.getPerson(),
					"INFO", appMgr);

		} catch (Exception e) {

		}
		listVariables = new ListDataModel();
		listVariable = new ArrayList<Variables>();
		copylistVariable = new ArrayList<Variables>();
		Initialize();
		// annotation = new annotation();
	}

	public void annulModif() {
		variable = new Variables();
		divajouter = "inline";
		divmodifier = "none";
		infoChampObligatoir = "inline";
		titre = "Ajouter variable";
	}

	public void viderchamp() {
		status = false;
		status3 = false;
		status1 = false;
		status2 = false;
		variable = new Variables();
		msgerror = true;
		titleaccor = messageSource.getMessage("Ajoutvariable",new Object[] {}, lm.createLocal());
		openaccor1 = true;
		openaccor = false;
		hidebuttonDelUpUser = "none";
		hidebuttonValidateUser = "inline";
		hideAccor = "inline";
		divajouter = "inline";
		divmodifier = "none";
		infoChampObligatoir = "inline";
	}

	public String valide() {
		return ("OK");
	}
	public List<SelectItem> getSelectItemsValue() {
		List<SelectItem> selectItemsValue = new ArrayList<SelectItem>();
		selectItemsValue.add(new SelectItem(""));
		System.out.println("DANS getSelectItemsValue");
//		if (variable.getVariablesId().equals(11) ) {
//			selectItemsValue.add(new SelectItem("0"));
//			selectItemsValue.add(new SelectItem("1"));
//		}else {
//			selectItemsValue.add(new SelectItem("Oui"));
//			selectItemsValue.add(new SelectItem("Non"));
		//}
			if (variable!=null && variable.getVariablesId()!=null && variable.getVariablesId().equals(26) ) {
				selectItemsValue.add(new SelectItem("1"));
				selectItemsValue.add(new SelectItem("2"));
			}else {
				selectItemsValue.add(new SelectItem("Oui"));
				selectItemsValue.add(new SelectItem("Non"));
			}
			
		selectedValue = variable.getVaraiablesValeur();
		return selectItemsValue;
	}
	@SuppressWarnings("unchecked")
	public long getRecords() {
		if (listVariables == null && listVariables.getWrappedData() == null)
			records = 0;
		else
			records = ((List<Variables>) listVariables.getWrappedData()).size();
		return records;
	}

	public String getDivajouter() {
		return divajouter;
	}

	public void setDivajouter(String divajouter) {
		this.divajouter = divajouter;
	}

	public String getDivmodifier() {
		return divmodifier;
	}

	public void setDivmodifier(String divmodifier) {
		this.divmodifier = divmodifier;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getInfoChampObligatoir() {
		return infoChampObligatoir;
	}

	public void setInfoChampObligatoir(String infoChampObligatoir) {
		this.infoChampObligatoir = infoChampObligatoir;
	}

	public ApplicationManager getAppMgr() {
		return appMgr;
	}

	public void setAppMgr(ApplicationManager appMgr) {
		this.appMgr = appMgr;
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

	public void setRecords(long records) {
		this.records = records;
	}

	public boolean isOpenaccor() {
		return openaccor;
	}

	public void setOpenaccor(boolean openaccor) {
		this.openaccor = openaccor;
	}

	public boolean isOpenaccor1() {
		return openaccor1;
	}

	public void setOpenaccor1(boolean openaccor1) {
		this.openaccor1 = openaccor1;
	}

	public String getHidebuttonValidateUser() {
		return hidebuttonValidateUser;
	}

	public void setHidebuttonValidateUser(String hidebuttonValidateUser) {
		this.hidebuttonValidateUser = hidebuttonValidateUser;
	}

	public String getHidebuttonDelUpUser() {
		return hidebuttonDelUpUser;
	}

	public void setHidebuttonDelUpUser(String hidebuttonDelUpUser) {
		this.hidebuttonDelUpUser = hidebuttonDelUpUser;
	}

	public String getHideAccor() {
		return hideAccor;
	}

	public void setHideAccor(String hideAccor) {
		this.hideAccor = hideAccor;
	}

	public boolean isMsgerror() {
		return msgerror;
	}

	public void setMsgerror(boolean msgerror) {
		this.msgerror = msgerror;
	}

	public String getTitleaccor() {
		return titleaccor;
	}

	public void setTitleaccor(String titleaccor) {
		this.titleaccor = titleaccor;
	}

	public boolean isStatus2() {
		return status2;
	}

	public void setStatus2(boolean status2) {
		this.status2 = status2;
	}

	public boolean isStatus3() {
		return status3;
	}

	public void setStatus3(boolean status3) {
		this.status3 = status3;
	}

	public VariableGlobale getVb() {
		return vb;
	}

	public void setVb(VariableGlobale vb) {
		this.vb = vb;
	}

	public Variables getVariable() {
		return variable;
	}

	public void setVariable(Variables variable) {
		this.variable = variable;
	}

	public DataModel getListVariables() {
		return listVariables;
	}

	public void setListVariables(DataModel listVariables) {
		this.listVariables = listVariables;
	}

	public List<Variables> getListVariable() {
		return listVariable;
	}

	public void setListVariable(List<Variables> listVariable) {
		this.listVariable = listVariable;
	}

	public List<Variables> getCopylistVariable() {
		return copylistVariable;
	}

	public void setCopylistVariable(List<Variables> copylistVariable) {
		this.copylistVariable = copylistVariable;
	}

	public Variables getVariable2() {
		return variable2;
	}

	public void setVariable2(Variables variable2) {
		this.variable2 = variable2;
	}


	public String getSelectedValue() {
		return selectedValue;
	}

	public void setSelectedValue(String selectedValue) {
		this.selectedValue = selectedValue;
	}

	public boolean isShowSelectList() {
		return showSelectList;
	}

	public void setShowSelectList(boolean showSelectList) {
		this.showSelectList = showSelectList;
	}

}
