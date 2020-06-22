package xtensus.beans.common.GBO0;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
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
import xtensus.entity.Menu;
import xtensus.ldap.business.LdapOperation;
import xtensus.services.ApplicationManager;

@Component()
@Scope("request")
public class MenuGestionBean  {

	private long records = 0;
	private long records1 = 0;
	private LdapOperation ldapOperation;

	@Autowired
	private VariableGlobale vb;
	private ApplicationManager appMgr;
	@Autowired
	private LanguageManagerBean lm;
	@Autowired
	private MessageSource messageSource;
	private String message;
	private boolean status;
	private boolean status1;
	private DataModel menuDataModel;
	private DataModel subMenuDataModel;
	private List<Menu> listSubMenu;
	private List<ItemMenu> listItemMenu;
	
	private Menu menu;
	private Menu sousMenu;
	private String hideAccor;
	private String hideAccor1;
	private String hideAccor2;
	private String typeMenu;
	private boolean showMenuForm;
	private boolean showSousMenuForm;
	private boolean showOrdreForm;
	private boolean showAddButton;
	private boolean showUpdateButton;
	private boolean showConsultGraphicImage; 
	private boolean showAffectGraphicImage;
	private String menuParent;
	private int newOrder;
	private int newOrder1;
	private int currentOrder;
	private List<Menu> listMenuPrincipal;
	private List<Menu> listSousMenus;
	private String titlePanel;
	private ItemMenu itemMenu;
	private int rowIndex;
	
	public class SousMenus{
		private int idSousMenu;
		private int ordreSousMenu;
		private String nomSousMenu;
		private String nomSousMenuArabe;
		private String menuPrincipal;
		private String classeSousMenu;
		private String methodeSousMenu;
		private boolean checkedSousMenu;
		private boolean nonCheckedSousMenu;

		public SousMenus(){
			this.checkedSousMenu = true;
			this.nonCheckedSousMenu = false;
		}

		public void setIdSousMenu(int idSousMenu) {
			this.idSousMenu = idSousMenu;
		}

		public int getIdSousMenu() {
			return idSousMenu;
		}

		public void setNomSousMenu(String nomSousMenu) {
			this.nomSousMenu = nomSousMenu;
		}

		public String getNomSousMenu() {
			return nomSousMenu;
		}

		public void setMenuPrincipal(String menuPrincipal) {
			this.menuPrincipal = menuPrincipal;
		}

		public String getMenuPrincipal() {
			return menuPrincipal;
		}

		public void setNomSousMenuArabe(String nomSousMenuArabe) {
			this.nomSousMenuArabe = nomSousMenuArabe;
		}

		public String getNomSousMenuArabe() {
			return nomSousMenuArabe;
		}

		public void setCheckedSousMenu(boolean checkedSousMenu) {
			this.checkedSousMenu = checkedSousMenu;
		}

		public boolean isCheckedSousMenu() {
			return checkedSousMenu;
		}

		public void setOrdreSousMenu(int ordreSousMenu) {
			this.ordreSousMenu = ordreSousMenu;
		}

		public int getOrdreSousMenu() {
			return ordreSousMenu;
		}

		public void setNonCheckedSousMenu(boolean nonCheckedSousMenu) {
			this.nonCheckedSousMenu = nonCheckedSousMenu;
		}

		public boolean isNonCheckedSousMenu() {
			return nonCheckedSousMenu;
		}

		public void setClasseSousMenu(String classeSousMenu) {
			this.classeSousMenu = classeSousMenu;
		}

		public String getClasseSousMenu() {
			return classeSousMenu;
		}

		public void setMethodeSousMenu(String methodeSousMenu) {
			this.methodeSousMenu = methodeSousMenu;
		}

		public String getMethodeSousMenu() {
			return methodeSousMenu;
		}
		
		
	}
	
	public class ItemMenu{
		private int idMenuPrincipal;
		private String menuPrincipal;
		private boolean checkedMenu;
		private List<SousMenus> sousMenus;

		public ItemMenu(){
			this.sousMenus = new ArrayList<SousMenus>();
			this.checkedMenu = false;
		}

		public void setMenuPrincipal(String menuPrincipal) {
			this.menuPrincipal = menuPrincipal;
		}

		public String getMenuPrincipal() {
			return menuPrincipal;
		}

		public void setCheckedMenu(boolean checkedMenu) {
			this.checkedMenu = checkedMenu;
		}

		public boolean isCheckedMenu() {
			return checkedMenu;
		}

		public void setSousMenus(List<SousMenus> sousMenus) {
			this.sousMenus = sousMenus;
		}

		public List<SousMenus> getSousMenus() {
			return sousMenus;
		}

		public void setIdMenuPrincipal(int idMenuPrincipal) {
			this.idMenuPrincipal = idMenuPrincipal;
		}

		public int getIdMenuPrincipal() {
			return idMenuPrincipal;
		}
	}
	
	@Autowired
	public MenuGestionBean(
			@Qualifier("applicationManager") ApplicationManager appMgr){
		this.appMgr = appMgr;
		menu = new Menu();
		sousMenu = new Menu();
		hideAccor = "none";
		typeMenu = "menu";
		showMenuForm = true;
		showSousMenuForm = false;
		showOrdreForm = false;
		ldapOperation = new LdapOperation();
		menuDataModel = new ListDataModel();
		listItemMenu = new ArrayList<ItemMenu>();
		subMenuDataModel = new ListDataModel();
		listSubMenu = new ArrayList<Menu>();
		subMenuDataModel.setWrappedData(listSubMenu);
		menuDataModel.setWrappedData(listItemMenu);
		showConsultGraphicImage = true;
		showAffectGraphicImage = false;
		System.out.println("*************Bean RoleGestionBean Injected***************");
	}
	
	@PostConstruct
	public void initialize() {
		status = false;
		status1 = false;
		hideAccor = "none";
		hideAccor2 = "none";
		listMenuPrincipal = new ArrayList<Menu>();
		listMenuPrincipal = appMgr.getListMenuPrincipal();
		listItemMenu = getItemMenuFromListPrincipalMenu(listMenuPrincipal,-1);
		menuDataModel.setWrappedData(listItemMenu);
		listSubMenu = new ArrayList<Menu>();
		listSubMenu = appMgr.getListNonAffectedSubMenu();
		showConsultGraphicImage = true;
		showAffectGraphicImage = false;
		if (listSubMenu.isEmpty()) {
			hideAccor1 = "none";
		} else {
			hideAccor1 = "inline";
		}
		subMenuDataModel.setWrappedData(listSubMenu);
	}
	
	public List<SelectItem> getSelectItemsMenuOrdre() {
		List<SelectItem> selectItemsMenuOrdre = new ArrayList<SelectItem>();
		for (int j = 1; j <= newOrder; j++) {
			selectItemsMenuOrdre.add(new SelectItem(j));
		}
		return selectItemsMenuOrdre;
	}
	
	public List<SelectItem> getSelectItemsSousMenuOrdre() {
		List<SelectItem> selectItemsSousMenuOrdre = new ArrayList<SelectItem>();
		for (int j = 1; j <= newOrder1; j++) {
			selectItemsSousMenuOrdre.add(new SelectItem(j));
		}
		return selectItemsSousMenuOrdre;
	}
	
	public List<SelectItem> getSelectItemsMenus() {
		List<SelectItem> selectItemsMenus = new ArrayList<SelectItem>();
		selectItemsMenus.add(new SelectItem(""));
		for (int j = 0; j <= listMenuPrincipal.size() - 1; j++) {
			selectItemsMenus.add(new SelectItem(listMenuPrincipal.get(j).getMenuLibelle()));
		}
		return selectItemsMenus;
	}
	
	public void showAddForm(){
		status = false;
		status1 = false;
		menu = new Menu();
		newOrder = listMenuPrincipal.size() + 1;
		menu.setMenuOrdre(newOrder);
		sousMenu = new Menu();
		showMenuForm = true;
		showSousMenuForm = false;
		showUpdateButton = false;
		showAddButton = true;
		hideAccor = "inline";
		hideAccor2 = "none";
		listSubMenu = new ArrayList<Menu>();
		listSubMenu = appMgr.getListNonAffectedSubMenu();
		showConsultGraphicImage = true;
		showAffectGraphicImage = false;
		if (listSubMenu.isEmpty()) {
			hideAccor1 = "none";
		} else {
			hideAccor1 = "inline";
		}
		subMenuDataModel.setWrappedData(listSubMenu);
		typeMenu = "menu";
		titlePanel = messageSource.getMessage("ajoutMenuSousMenu", new Object[]{},lm.createLocal());
	}
	
	public void cleanAll(){
		status = false;
		status1 = false;
		menu = new Menu();
		sousMenu = new Menu();
		hideAccor = "none";
	}
	
	public void showOrdreInput(ActionEvent evt) {
		Menu menu;
		if (menuParent.equals("")) {
			showOrdreForm = false;
		} else {
			menu = new Menu();
			menu = appMgr.getListMenuPrincipalByLibelle(menuParent).get(0);
			if (showUpdateButton && currentOrder != 0) {
				newOrder1 = appMgr.getListSousMenuByIdMenuPrincipal(menu.getMenuId()).size();
			} else {
				newOrder1 = appMgr.getListSousMenuByIdMenuPrincipal(menu.getMenuId()).size() + 1;
				sousMenu.setMenuOrdre(newOrder1);
			}
			showOrdreForm = true;
		}
	}
	
	public void eventChooseTypeMenu(ActionEvent actionEvent){
		if (typeMenu.equals("menu")) {
			showMenuForm = true;
			showSousMenuForm = false;
			menu = new Menu();
			newOrder = listMenuPrincipal.size() + 1;
			menu.setMenuOrdre(newOrder);
		} else {
			showMenuForm = false;
			showSousMenuForm = true;
			showOrdreForm = false;
			menuParent = "";
			sousMenu = new Menu();
			sousMenu.setMenuClass("menuBean");
		}
	}
	
	public void save(){
		System.out.print("dans save");
	    status = false;
		status1 = false;
		Menu menuParentObject;
		List<Menu> listSousMenu;
		Menu copyMenu;
		try{
			if (typeMenu.equals("menu")) {
				System.out.println(" menu");
				int indexOrder1 = menu.getMenuOrdre();
				System.out.println(indexOrder1);
				System.out.println(menu.getMenuLibelle());
				System.out.println(menu.getMenuLibelleArabe());
				for (Menu menu1 : listMenuPrincipal) {
					System.out.println(menu1.getMenuId()+" , "+menu1.getMenuLibelle()+" , "+menu1.getMenuOrdre());
				}
				for (Menu menu1 : listMenuPrincipal) {
					System.out.println(menu1.getMenuLibelle());
					if (menu1.getMenuOrdre().equals(indexOrder1)) {
						System.out.println("  *"+menu1.getMenuLibelle());
						copyMenu = new Menu();
						copyMenu = appMgr.getMenuPrincipalByIdMenu(menu1.getMenuId()).get(0);
						copyMenu.setMenuOrdre(indexOrder1 + 1);
						appMgr.update(copyMenu);
						indexOrder1 ++;
					}
				}
				appMgr.insert(menu);
				//**
				LogClass logClass = new LogClass();
				logClass.addTrack("ajout",
						"Evénement de log d'ajout de menu " + menu.getMenuId() + "-" + menu.getMenuLibelle() ,
						vb.getPerson(), "INFO", appMgr);
			} else {
				System.out.println("sous menu");
				if (!menuParent.equals("")) {
					int indexOrder2 = sousMenu.getMenuOrdre();
					menuParentObject = new Menu();
					listSousMenu = new ArrayList<Menu>();
					menuParentObject = appMgr.getListMenuPrincipalByLibelle(menuParent).get(0);
					listSousMenu = appMgr.getListSousMenuByIdMenuPrincipal(menuParentObject.getMenuId());
					sousMenu.setMenu(menuParentObject);
					for (Menu menu1 : listSousMenu) {
						if (menu1.getMenuOrdre().equals(indexOrder2)) {
							copyMenu = new Menu();
							copyMenu = appMgr.getMenuPrincipalByIdMenu(menu1.getMenuId()).get(0);
							copyMenu.setMenuOrdre(indexOrder2 + 1);
							appMgr.update(copyMenu);
							indexOrder2 ++;
						}
					}
				}
				appMgr.insert(sousMenu);
				//**
				LogClass logClass = new LogClass();
				logClass.addTrack("ajout",
						"Evénement de log d'ajout de sous menu " + sousMenu.getMenuId() + "-" + sousMenu.getMenuLibelle() ,
						vb.getPerson(), "INFO", appMgr);
			}
			
			status=true;
		} catch (Exception e) {
			status1=true;
			e.printStackTrace();
		}

	}
	
	public void update(){
		System.out.print("dans update");
	    status = false;
		status1 = false;
		Menu menuParentObject;
		List<Menu> listSousMenu;
		Menu copyMenu;
		try{
			if (typeMenu.equals("menu")) {
				System.out.println(" menu");
				int indexOrder1 = menu.getMenuOrdre();
				System.out.print(indexOrder1);System.out.println(" , "+menu.getMenuLibelle());
				System.out.println("Ordre courant : "+currentOrder);
				System.out.println("Ordre nouveau : "+indexOrder1);
				if (currentOrder < indexOrder1) {
					System.out.println("Ordre nouveau > Ordre courant");
					for (int i = currentOrder; i < indexOrder1; i++) {
						copyMenu = new Menu();
						copyMenu = appMgr.getMenuPrincipalByIdMenu(listMenuPrincipal.get(i).getMenuId()).get(0);
						System.out.println("   -"+copyMenu.getMenuLibelle()+" , "+copyMenu.getMenuOrdre()+" :: replace with ordre : "+i);
						copyMenu.setMenuOrdre(i);
						appMgr.update(copyMenu);
					}
				} else if (currentOrder > indexOrder1) {
					System.out.println("Ordre nouveau < Ordre courant");
					for (int i = indexOrder1; i < currentOrder; i++) {
						copyMenu = new Menu();
						copyMenu = appMgr.getMenuPrincipalByIdMenu(listMenuPrincipal.get(i-1).getMenuId()).get(0);
						System.out.println("   -"+copyMenu.getMenuLibelle()+" , "+copyMenu.getMenuOrdre()+" :: replace with ordre : "+i+1);
						copyMenu.setMenuOrdre(i+1);
						appMgr.update(copyMenu);
					}
				}
				copyMenu = new Menu();
				copyMenu = appMgr.getMenuPrincipalByIdMenu(menu.getMenuId()).get(0);
				copyMenu.setMenu(menu.getMenu());
				copyMenu.setMenuLibelle(menu.getMenuLibelle());
				copyMenu.setMenuLibelleArabe(menu.getMenuLibelleArabe());
				copyMenu.setMenuMethode(menu.getMenuMethode());
				copyMenu.setMenuClass(menu.getMenuClass());
				copyMenu.setMenuOrdre(menu.getMenuOrdre());
				appMgr.update(copyMenu);
				//**
				LogClass logClass = new LogClass();
				logClass.addTrack("modification",
						"Evénement de log de modification de menu " + copyMenu.getMenuId() + "-" + copyMenu.getMenuLibelle() ,
						vb.getPerson(), "INFO", appMgr);
			} else {
				System.out.println(" sous menu");
				
				if (!menuParent.equals("")) {
					int indexOrder2 = sousMenu.getMenuOrdre();
					menuParentObject = new Menu();
					listSousMenu = new ArrayList<Menu>();
					menuParentObject = appMgr.getListMenuPrincipalByLibelle(menuParent).get(0);
					listSousMenu = appMgr.getListSousMenuByIdMenuPrincipal(menuParentObject.getMenuId());
					sousMenu.setMenu(menuParentObject);
					for (Menu menu1 : listSousMenu) {
						System.out.println(menu1.getMenuId()+" , "+menu1.getMenuLibelle()+" , "+menu1.getMenuOrdre());
					}
					System.out.println("Ordre courant : "+currentOrder);
					System.out.println("Ordre nouveau : "+indexOrder2);
					if (currentOrder == 0){
						for (Menu menu1 : listSousMenu) {
							System.out.println(menu1.getMenuLibelle());
							if (menu1.getMenuOrdre().equals(indexOrder2)) {
								System.out.println("  *"+menu1.getMenuLibelle());
								copyMenu = new Menu();
								copyMenu = appMgr.getMenuPrincipalByIdMenu(menu1.getMenuId()).get(0);
								copyMenu.setMenuOrdre(indexOrder2 + 1);
								appMgr.update(copyMenu);
								indexOrder2 ++;
							}
						}
					} else if (currentOrder < indexOrder2) {
						System.out.println("Ordre nouveau > Ordre courant");
						for (int i = currentOrder; i < indexOrder2; i++) {
							copyMenu = new Menu();
							copyMenu = appMgr.getMenuPrincipalByIdMenu(listSousMenu.get(i).getMenuId()).get(0);
							System.out.println("   -"+copyMenu.getMenuLibelle()+" , "+copyMenu.getMenuOrdre()+" :: replace with ordre : "+i);
							copyMenu.setMenuOrdre(i);
							appMgr.update(copyMenu);
						}
					} else if (currentOrder > indexOrder2) {
						System.out.println("Ordre nouveau < Ordre courant");
						for (int i = indexOrder2; i < currentOrder; i++) {
							copyMenu = new Menu();
							copyMenu = appMgr.getMenuPrincipalByIdMenu(listSousMenu.get(i-1).getMenuId()).get(0);
							System.out.println("   -"+copyMenu.getMenuLibelle()+" , "+copyMenu.getMenuOrdre()+" :: replace with ordre : "+i+1);
							copyMenu.setMenuOrdre(i+1);
							appMgr.update(copyMenu);
						}
					} 
				} else {
					sousMenu.setMenu(null);
					sousMenu.setMenuOrdre(null);
				}
				copyMenu = new Menu();
				copyMenu = appMgr.getMenuPrincipalByIdMenu(sousMenu.getMenuId()).get(0);
				copyMenu.setMenu(sousMenu.getMenu());
				copyMenu.setMenuLibelle(sousMenu.getMenuLibelle());
				copyMenu.setMenuLibelleArabe(sousMenu.getMenuLibelleArabe());
				copyMenu.setMenuMethode(sousMenu.getMenuMethode());
				copyMenu.setMenuClass(sousMenu.getMenuClass());
				copyMenu.setMenuOrdre(sousMenu.getMenuOrdre());
				appMgr.update(copyMenu);
				//**
				LogClass logClass = new LogClass();
				logClass.addTrack("modification",
						"Evénement de log de modification de sous menu " + copyMenu.getMenuId() + "-" + copyMenu.getMenuLibelle() ,
						vb.getPerson(), "INFO", appMgr);
			}
			status=true;
		} catch (Exception e) {
			status1=true;
			e.printStackTrace();
		}

	}
	
	public void getDelete(){
		if (typeMenu.equals("menu")) {
			List<Menu> listSousMenu = new ArrayList<Menu>();
			listSousMenu = appMgr.getListSousMenuByIdMenuPrincipal(menu.getMenuId());
			setMessage(messageSource.getMessage("comfirmDeleteMenu2",
					new Object[] {}, lm.createLocal()));
			if (listSousMenu.isEmpty()) {
				setMessage(messageSource.getMessage("comfirmDeleteMenu1",
						new Object[] {}, lm.createLocal()));
			} 
		} else {
			setMessage(messageSource.getMessage("comfirmDeleteSousMenu",
					new Object[] {}, lm.createLocal()));
		}
	}
	
	public void delete(){
		status = false;
		status1 = false;
		Menu copyMenu;
		System.out.println("dans delete");
		try {
			if (typeMenu.equals("menu")) {
				System.out.println(" menu");
				appMgr.deleteSubMenuByIdPrincipalMenu(menu.getMenuId());
				appMgr.deleteMenuSubMenuByIdPrincipalMenu(menu.getMenuId());
				appMgr.deleteMenuDroitByIdMenu(menu.getMenuId());
				for (Menu menu1 : listSousMenus) {
					appMgr.deleteMenuDroitByIdMenu(menu1.getMenuId());
				}
				int indexOrder1 = menu.getMenuOrdre();
				System.out.println(indexOrder1);System.out.print(" , "+menu.getMenuLibelle());
				for (Menu menu1 : listMenuPrincipal) {
					System.out.println(menu1.getMenuLibelle());
					if (menu1.getMenuOrdre().equals(indexOrder1+1)) {
						System.out.println("  *"+menu1.getMenuLibelle());
						copyMenu = new Menu();
						copyMenu = appMgr.getMenuPrincipalByIdMenu(menu1.getMenuId()).get(0);
						copyMenu.setMenuOrdre(indexOrder1);
						appMgr.update(copyMenu);
						indexOrder1 ++;
					}
				}
				//**
				LogClass logClass = new LogClass();
				logClass.addTrack("suppression",
						"Evénement de log de suppression du menu " + menu.getMenuId() + "-" + menu.getMenuLibelle() ,
						vb.getPerson(), "INFO", appMgr);
			} else {
				System.out.println(" sous menu");
				appMgr.deleteMenuSubMenuByIdPrincipalMenu(sousMenu.getMenuId());
				appMgr.deleteMenuDroitByIdMenu(sousMenu.getMenuId());
				try {
					int indexOrder2 = sousMenu.getMenuOrdre();
					System.out.println(indexOrder2);System.out.print(" , "+sousMenu.getMenuLibelle());
					for (Menu menu1 : listSousMenus) {
						System.out.println(menu1.getMenuLibelle());
						if (menu1.getMenuOrdre().equals(indexOrder2+1)) {
							System.out.println("  *"+menu1.getMenuLibelle());
							copyMenu = new Menu();
							copyMenu = appMgr.getMenuPrincipalByIdMenu(menu1.getMenuId()).get(0);
							copyMenu.setMenuOrdre(indexOrder2);
							appMgr.update(copyMenu);
							indexOrder2 ++;
						}
					}
				} catch (NullPointerException e) {
				}
				//**
				LogClass logClass = new LogClass();
				logClass.addTrack("suppression",
						"Evénement de log de suppression de sous menu " + sousMenu.getMenuId() + "-" + sousMenu.getMenuLibelle() ,
						vb.getPerson(), "INFO", appMgr);
			}
			status = true;
		} catch (Exception e) {
			status1 = true;
			e.printStackTrace();
		}
	}
	
	public void getSelectedMenuRow(){
		ItemMenu itemMenu = (ItemMenu) menuDataModel.getRowData();
		System.out.println("***Menu : "+itemMenu.getMenuPrincipal());
		menu = new Menu();
		menu = appMgr.getMenuPrincipalByIdMenu(itemMenu.getIdMenuPrincipal()).get(0);
		titlePanel = messageSource.getMessage("consultationMenuSousMenu", new Object[]{},lm.createLocal());
		listSousMenus = new ArrayList<Menu>();
		listSousMenus = appMgr.getListSousMenuByIdMenuPrincipal(menu.getMenuId());
		currentOrder = menu.getMenuOrdre();
		typeMenu = "menu";
		hideAccor = "inline";
		hideAccor1 = "none";
		hideAccor2 = "none";
		showMenuForm = true;
		showSousMenuForm = false;
		showUpdateButton = true;
		showAddButton = false;
		newOrder = listMenuPrincipal.size() ;
	}
	
	public void getSelectedMenuRow1(){
		System.out.println("debut operation");
		itemMenu = new ItemMenu();
		itemMenu = (ItemMenu) menuDataModel.getRowData();
		rowIndex = menuDataModel.getRowIndex();
		System.out.println("verif index : "+rowIndex);
		listItemMenu = getItemMenuFromListPrincipalMenu(listMenuPrincipal,itemMenu.getIdMenuPrincipal());
		menuDataModel.setWrappedData(listItemMenu);
		showAffectGraphicImage = true;
		showConsultGraphicImage = false;
		hideAccor1 = "inline";
		hideAccor2 = "inline";
		System.out.println("fin operation");
	}
	
	public void affectSubMenu(){
		System.out.println("dans affect");
		System.out.println("index : "+rowIndex);
		Menu menu = (Menu) subMenuDataModel.getRowData();
		SousMenus sousMenu = new SousMenus();
		sousMenu.setIdSousMenu(menu.getMenuId());
		sousMenu.setNomSousMenu(menu.getMenuLibelle());
		sousMenu.setNomSousMenuArabe(menu.getMenuLibelleArabe());
		sousMenu.setMenuPrincipal(itemMenu.getMenuPrincipal());
		sousMenu.setCheckedSousMenu(false);
		sousMenu.setNonCheckedSousMenu(true);
		listItemMenu.get(rowIndex).getSousMenus().add(sousMenu);
		menuDataModel.setWrappedData(listItemMenu);
		listSubMenu.remove(menu);
		subMenuDataModel.setWrappedData(listSubMenu);
		System.out.println("fin affect");
	}
	
	public void desaffectSubMenu(){
		System.out.println("dans desaffect");
		System.out.println("index : "+rowIndex);
		Menu menu;
		SousMenus sousMenus = (SousMenus) FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{sousMenu}", SousMenus.class);
		listItemMenu.get(rowIndex).getSousMenus().remove(sousMenus);
		menuDataModel.setWrappedData(listItemMenu);
		menu = new Menu();
		menu.setMenuId(sousMenus.getIdSousMenu());
		menu.setMenuLibelle(sousMenus.getNomSousMenu());
		menu.setMenuLibelleArabe(sousMenus.getNomSousMenuArabe());
		menu.setMenuClass(sousMenus.getClasseSousMenu());
		menu.setMenuMethode(sousMenus.getMethodeSousMenu());
		listSubMenu.add(menu);
		subMenuDataModel.setWrappedData(listSubMenu);
		System.out.println("fin desaffect");
	}
	
	public void cancelUpdate(){
		System.out.println("dans cancel update");
		hideAccor2 = "none";
		showConsultGraphicImage = true;
		showAffectGraphicImage = false;
		hideAccor1 = "none";
		hideAccor2 = "none";
		listSubMenu = appMgr.getListNonAffectedSubMenu();
		subMenuDataModel.setWrappedData(listSubMenu);
		listItemMenu = getItemMenuFromListPrincipalMenu(listMenuPrincipal,-1);
		menuDataModel.setWrappedData(listItemMenu);
		System.out.println("fin cancel update");
	}
	
	public void validateUpdate(){
		System.out.println("dans validate update");
		try {
			Menu copyMenu;
			Menu principalMenu = new Menu();
			principalMenu = appMgr.getMenuPrincipalByIdMenu(itemMenu.getIdMenuPrincipal()).get(0);
			hideAccor1 = "none";
			hideAccor2 = "none";
			List<SousMenus> listSousMenu = new ArrayList<MenuGestionBean.SousMenus>();
			listSousMenu = listItemMenu.get(rowIndex).getSousMenus();
			for (int i = 0; i < listSousMenu.size(); i++) {
				copyMenu = new Menu();
				copyMenu = appMgr.getMenuPrincipalByIdMenu(listSousMenu.get(i).getIdSousMenu()).get(0);
				copyMenu.setMenu(principalMenu);
				copyMenu.setMenuOrdre(i+1);
				appMgr.update(copyMenu);
			}
			for (int i = 0; i < listSubMenu.size(); i++) {
				copyMenu = new Menu();
				copyMenu = appMgr.getMenuPrincipalByIdMenu(listSubMenu.get(i).getMenuId()).get(0);
				copyMenu.setMenu(null);
				copyMenu.setMenuOrdre(null);
				appMgr.update(copyMenu);
			}
			listSubMenu = new ArrayList<Menu>();
			listSubMenu = appMgr.getListNonAffectedSubMenu();
			showConsultGraphicImage = true;
			showAffectGraphicImage = false;
			if (listSubMenu.isEmpty()) {
				hideAccor1 = "none";
			} else {
				hideAccor1 = "inline";
			}
			subMenuDataModel.setWrappedData(listSubMenu);
			listMenuPrincipal = appMgr.getListMenuPrincipal();
			listItemMenu = getItemMenuFromListPrincipalMenu(listMenuPrincipal,-1);
			menuDataModel.setWrappedData(listItemMenu);
			LogClass logClass = new LogClass();
			logClass.addTrack("affectation",
					"Evénement de log d'affectation de sous-menu au menu "
							+ principalMenu.getMenuId() + "-"
							+ principalMenu.getMenuLibelle() , vb.getPerson(), "INFO", appMgr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("fin validate update");
	}
	
	public void getSelectedSubMenuRow(){
		SousMenus sousMenus = (SousMenus) FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{sousMenu}", SousMenus.class);
		sousMenu = new Menu();
		sousMenu = appMgr.getMenuPrincipalByIdMenu(sousMenus.getIdSousMenu()).get(0);
		
		currentOrder = sousMenu.getMenuOrdre();
		System.out.println("***SubMenu : "+sousMenus.getNomSousMenu());
		titlePanel = messageSource.getMessage("consultationMenuSousMenu", new Object[]{},lm.createLocal());
		typeMenu = "sousMenu";
		menuParent = sousMenus.getMenuPrincipal();
		Menu menu = new Menu();
		menu = appMgr.getListMenuPrincipalByLibelle(menuParent).get(0);
		listSousMenus = new ArrayList<Menu>();
		listSousMenus = appMgr.getListSousMenuByIdMenuPrincipal(menu.getMenuId());
		newOrder1 = listSousMenus.size();
		hideAccor = "inline";
		hideAccor1 = "none";
		hideAccor2 = "none";
		showSousMenuForm = true;
		showMenuForm = false;
		showOrdreForm = true;
		showUpdateButton = true;
		showAddButton = false;
		//**
		LogClass logClass = new LogClass();
		logClass.addTrack("consultation",
				"Evénement de log de consultation de sous menu " + sousMenu.getMenuId() + "-" + sousMenu.getMenuLibelle() ,
				vb.getPerson(), "INFO", appMgr);
	}
	
	public void getSelectedSubMenuRow1(){
		sousMenu = new Menu();
		sousMenu = (Menu) subMenuDataModel.getRowData();
		currentOrder = 0;
		titlePanel = messageSource.getMessage("consultationMenuSousMenu", new Object[]{},lm.createLocal());
		typeMenu = "sousMenu";
		menuParent = "";
		hideAccor = "inline";
		showSousMenuForm = true;
		showMenuForm = false;
		showOrdreForm = false;
		showUpdateButton = true;
		showAddButton = false;
	}
	
	private List<ItemMenu> getItemMenuFromListPrincipalMenu(List<Menu> listMenuPrincipal,int idMenuPrincipal) {
		List<ItemMenu> result = new ArrayList<MenuGestionBean.ItemMenu>();
		List<Menu> listSousMenu;
		ItemMenu itemMenu;
		SousMenus sousMenus;
		List<SousMenus> listSousMenus;
		int i = 1;
		for (Menu menuPrincipal : listMenuPrincipal) {
			itemMenu = new ItemMenu();
			itemMenu.setMenuPrincipal(i+"."+menuPrincipal.getMenuLibelle());
			itemMenu.setIdMenuPrincipal(menuPrincipal.getMenuId());
			if (idMenuPrincipal == menuPrincipal.getMenuId()) {
				itemMenu.setCheckedMenu(true);
			}
			listSousMenu = new ArrayList<Menu>();
			listSousMenu = appMgr.getListSousMenuByIdMenuPrincipal(menuPrincipal.getMenuId());
			listSousMenus = new ArrayList<MenuGestionBean.SousMenus>();
			int j = 1;
			for (Menu sousMenu : listSousMenu) {
				sousMenus = new SousMenus();
				sousMenus.setIdSousMenu(sousMenu.getMenuId());
				sousMenus.setOrdreSousMenu(j);
				sousMenus.setMenuPrincipal(menuPrincipal.getMenuLibelle());
				sousMenus.setNomSousMenu(sousMenu.getMenuLibelle());
				sousMenus.setNomSousMenuArabe(sousMenu.getMenuLibelleArabe());
				sousMenus.setClasseSousMenu(sousMenu.getMenuClass());
				sousMenus.setMethodeSousMenu(sousMenu.getMenuMethode());
				if (idMenuPrincipal == menuPrincipal.getMenuId()) {
					sousMenus.setCheckedSousMenu(false);
					sousMenus.setNonCheckedSousMenu(true);
				}
				listSousMenus.add(sousMenus);
				j++;
			}
			itemMenu.setSousMenus(listSousMenus);
			result.add(itemMenu);
			i++;
		}
		return result ;
	}
	
	// **************************** Getter && Setter********************
	@SuppressWarnings("unchecked")
	public long getRecords() {
		if (menuDataModel == null && menuDataModel.getWrappedData() == null)
			records = 0;
		else
			records = ((List<ItemMenu>) menuDataModel.getWrappedData()).size();
		return records;
	}
	
	@SuppressWarnings("unchecked")
	public long getRecords1() {
		if (subMenuDataModel == null && subMenuDataModel.getWrappedData() == null)
			records1 = 0;
		else
			records1 = ((List<Menu>) subMenuDataModel.getWrappedData()).size();
		return records1;
	}
	
	public VariableGlobale getVb() {
		return vb;
	}
	
	public void setVb(VariableGlobale vb) {
		this.vb = vb;
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
	
	public void setLdapOperation(LdapOperation ldapOperation) {
		this.ldapOperation = ldapOperation;
	}

	public LdapOperation getLdapOperation() {
		return ldapOperation;
	}

	public void setMenuDataModel(DataModel menuDataModel) {
		this.menuDataModel = menuDataModel;
	}

	public DataModel getMenuDataModel() {
		return menuDataModel;
	}

	public void setListItemMenu(List<ItemMenu> listItemMenu) {
		this.listItemMenu = listItemMenu;
	}

	public List<ItemMenu> getListItemMenu() {
		return listItemMenu;
	}

	public void setHideAccor(String hideAccor) {
		this.hideAccor = hideAccor;
	}

	public String getHideAccor() {
		return hideAccor;
	}

	public void setTypeMenu(String typeMenu) {
		this.typeMenu = typeMenu;
	}

	public String getTypeMenu() {
		return typeMenu;
	}

	public void setShowMenuForm(boolean showMenuForm) {
		this.showMenuForm = showMenuForm;
	}

	public boolean isShowMenuForm() {
		return showMenuForm;
	}

	public void setShowSousMenuForm(boolean showSousMenuForm) {
		this.showSousMenuForm = showSousMenuForm;
	}

	public boolean isShowSousMenuForm() {
		return showSousMenuForm;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setSousMenu(Menu sousMenu) {
		this.sousMenu = sousMenu;
	}

	public Menu getSousMenu() {
		return sousMenu;
	}

	public void setMenuParent(String menuParent) {
		this.menuParent = menuParent;
	}

	public String getMenuParent() {
		return menuParent;
	}

	public void setShowOrdreForm(boolean showOrdreForm) {
		this.showOrdreForm = showOrdreForm;
	}

	public boolean isShowOrdreForm() {
		return showOrdreForm;
	}

	public void setNewOrder(int newOrder) {
		this.newOrder = newOrder;
	}

	public int getNewOrder() {
		return newOrder;
	}

	public List<Menu> getListMenuPrincipal() {
		return listMenuPrincipal;
	}

	public void setListMenuPrincipal(List<Menu> listMenuPrincipal) {
		this.listMenuPrincipal = listMenuPrincipal;
	}

	public void setNewOrder1(int newOrder1) {
		this.newOrder1 = newOrder1;
	}

	public int getNewOrder1() {
		return newOrder1;
	}

	public void setShowAddButton(boolean showAddButton) {
		this.showAddButton = showAddButton;
	}

	public boolean isShowAddButton() {
		return showAddButton;
	}

	public void setShowUpdateButton(boolean showUpdateButton) {
		this.showUpdateButton = showUpdateButton;
	}

	public boolean isShowUpdateButton() {
		return showUpdateButton;
	}

	public void setTitlePanel(String titlePanel) {
		this.titlePanel = titlePanel;
	}

	public String getTitlePanel() {
		return titlePanel;
	}

	public void setCurrentOrder(int currentOrder) {
		this.currentOrder = currentOrder;
	}

	public int getCurrentOrder() {
		return currentOrder;
	}

	public void setListSousMenus(List<Menu> listSousMenus) {
		this.listSousMenus = listSousMenus;
	}

	public List<Menu> getListSousMenus() {
		return listSousMenus;
	}

	public void setSubMenuDataModel(DataModel subMenuDataModel) {
		this.subMenuDataModel = subMenuDataModel;
	}

	public DataModel getSubMenuDataModel() {
		return subMenuDataModel;
	}

	public void setListSubMenu(List<Menu> listSubMenu) {
		this.listSubMenu = listSubMenu;
	}

	public List<Menu> getListSubMenu() {
		return listSubMenu;
	}

	public void setHideAccor1(String hideAccor1) {
		this.hideAccor1 = hideAccor1;
	}

	public String getHideAccor1() {
		return hideAccor1;
	}

	public void setShowConsultGraphicImage(boolean showConsultGraphicImage) {
		this.showConsultGraphicImage = showConsultGraphicImage;
	}

	public boolean isShowConsultGraphicImage() {
		return showConsultGraphicImage;
	}

	public void setShowAffectGraphicImage(boolean showAffectGraphicImage) {
		this.showAffectGraphicImage = showAffectGraphicImage;
	}

	public boolean isShowAffectGraphicImage() {
		return showAffectGraphicImage;
	}

	public void setHideAccor2(String hideAccor2) {
		this.hideAccor2 = hideAccor2;
	}

	public String getHideAccor2() {
		return hideAccor2;
	}

	public void setItemMenu(ItemMenu itemMenu) {
		this.itemMenu = itemMenu;
	}

	public ItemMenu getItemMenu() {
		return itemMenu;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public int getRowIndex() {
		return rowIndex;
	}
	
}
