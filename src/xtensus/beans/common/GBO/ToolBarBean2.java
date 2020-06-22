package xtensus.beans.common.GBO;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.el.MethodExpression;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.context.FacesContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

import org.richfaces.component.UIToolBar;
import org.richfaces.component.html.HtmlDropDownMenu;
import org.richfaces.component.html.HtmlMenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import xtensus.beans.common.VariableGlobale;
import xtensus.entity.Menu;
import xtensus.ldap.business.LdapOperation;
import xtensus.services.ApplicationManager;

@Component
@Scope("request")
public class ToolBarBean2 {
	private ApplicationManager appMgr;
	private UIToolBar toolBar = null;
	private UIToolBar toolBarArabe = null;
	private LdapOperation ldapOperation;
	@Autowired
	private VariableGlobale vb;
	private boolean status;

	public class MenuModel {
		private Menu menuPrincipal;
		private List<Menu> listSousMenu;

		public MenuModel() {
		}

		public MenuModel(Menu menuPrincipal, List<Menu> listSousMenu) {
			this.menuPrincipal = menuPrincipal;
			this.listSousMenu = listSousMenu;
		}

		public void setMenuPrincipal(Menu menuPrincipal) {
			this.menuPrincipal = menuPrincipal;
		}

		public Menu getMenuPrincipal() {
			return menuPrincipal;
		}

		public void setListSousMenu(List<Menu> listSousMenu) {
			this.listSousMenu = listSousMenu;
		}

		public List<Menu> getListSousMenu() {
			return listSousMenu;
		}
	}

	@Autowired
	public ToolBarBean2(@Qualifier("applicationManager") ApplicationManager appMgr) {
		this.appMgr = appMgr;
		ldapOperation = new LdapOperation();
	}

	@SuppressWarnings("rawtypes")
	@PostConstruct
	public void Initialize() {
		try {
			status = false;
			if (vb.getPerson() == null) {
				status = true;
			} else {
				String pathUser = "cn=" + vb.getPerson().getCn() + "," + ldapOperation.CONTEXT_USER;
				// IsResponsable ??
				BasicAttributes attributesResponsable = new BasicAttributes(true);
				Attribute attributeResponsable = new BasicAttribute("manager");
				attributeResponsable.add(pathUser);
				attributesResponsable.put(attributeResponsable);
				// IsSecretary ??
				BasicAttributes attributesSecretary = new BasicAttributes(true);
				Attribute attributeSecretary = new BasicAttribute("secretary");
				attributeSecretary.add(pathUser);
				attributesSecretary.put(attributeSecretary);
				// IsAgent ??
				BasicAttributes attributesAgent = new BasicAttributes(true);
				Attribute attributeAgent = new BasicAttribute("member");
				attributeAgent.add(pathUser);
				attributesAgent.put(attributeAgent);

				NamingEnumeration neManager = ldapOperation.getDirContext().search(
						ldapOperation.CONTEXT_UNIT, attributesResponsable);
				NamingEnumeration neSecretary = ldapOperation.getDirContext().search(
						ldapOperation.CONTEXT_UNIT, attributesSecretary);
				NamingEnumeration neAgent = ldapOperation.getDirContext().search(
						ldapOperation.CONTEXT_UNIT, attributesAgent);
				if (neManager.hasMore() || neSecretary.hasMore() || neAgent.hasMore()
						|| vb.getPerson().isBoc()) {
					vb.setUserAffectedToUnit(true);
					loadToolbar(vb.getRole());
				} else {
					vb.setUserAffectedToUnit(false);
					loadToolbar(new ArrayList<String>());
				}
			}
		} catch (NamingException e) {
		}
	}

	public void loadToolbar(List<String> role) {
		System.out.println("LoadToolBar*************");
		List<MenuModel> listAuthorities = new ArrayList<ToolBarBean2.MenuModel>();
		listAuthorities = getMapOfRoles(role);
		HtmlDropDownMenu dropDownMenu;
		HtmlDropDownMenu dropDownMenuArabe;
		HtmlMenuItem menuItem;
		HtmlMenuItem menuItemArabe;
		toolBar = (UIToolBar) FacesContext.getCurrentInstance().getApplication()
				.createComponent("org.richfaces.ToolBar");
		toolBarArabe = (UIToolBar) FacesContext.getCurrentInstance().getApplication()
				.createComponent("org.richfaces.ToolBar");
		HtmlGraphicImage htmlGraphicImage = new HtmlGraphicImage();
		HtmlGraphicImage htmlGraphicImageArabe = new HtmlGraphicImage();
		htmlGraphicImage.setValue("../framGraphique/images/accueil.png");
		htmlGraphicImageArabe.setValue("../framGraphique/images/accueil.png");
		htmlGraphicImage.setOnclick("document.location.href='../GBO/accueilWeb.jsf'");
		htmlGraphicImageArabe.setOnclick("document.location.href='../GBO/accueilWeb.jsf'");
		htmlGraphicImage.setOnmouseover("this.src='../framGraphique/images/accueilActif.png'");
		htmlGraphicImageArabe.setOnmouseover("this.src='../framGraphique/images/accueilActif.png'");
		htmlGraphicImage.setOnmouseout("this.src='../framGraphique/images/accueil.png'");
		htmlGraphicImageArabe.setOnmouseout("this.src='../framGraphique/images/accueil.png'");

		toolBar.getChildren().add(htmlGraphicImage);
		toolBarArabe.getChildren().add(htmlGraphicImageArabe);
		int k = 0;
		List<Menu> menuListeCourriers = appMgr.getMenuPrincipalByIdMenu(2);
		if(menuListeCourriers !=null && menuListeCourriers.size()>0)
		{  Menu menu=menuListeCourriers.get(0);
		System.out.println("Libele Menu : "+menu.getMenuLibelle());
			menuItem = new HtmlMenuItem();		
				menuItem = new HtmlMenuItem();
				menuItemArabe = new HtmlMenuItem();
				menuItem.setId("menuItem00"+menu.getId()
						);
				menuItemArabe.setId("menuItemArabe00"+menu.getId()
						);
				menuItem.setValue(menu.getMenuLibelle());
		
				try {
					MethodExpression methodExpression = FacesContext
					.getCurrentInstance()
					.getApplication()
					.getExpressionFactory()
					.createMethodExpression(
							FacesContext.getCurrentInstance().getELContext(),
							"#{menuBean.interfaceAccueil}",
							null, new Class<?>[0]);
					menuItem.setActionExpression(methodExpression);
					menuItemArabe.setActionExpression(methodExpression);
					menuItem.setSubmitMode("ajax");
					menuItemArabe.setSubmitMode("ajax");
					menuItemArabe.setValue(menu.getMenuLibelleArabe());
				} catch (NullPointerException e) {
				}
				
				toolBar.getChildren().add(menuItem);
				toolBarArabe.getChildren().add(menuItemArabe);
			} 
	
	}

	public List<MenuModel> getMapOfRoles(List<String> roles) {
		List<MenuModel> listAuthorities = new ArrayList<ToolBarBean2.MenuModel>();
		List<MenuModel> copyListAuthorities;
		List<Menu> listMenuPrincipal = new ArrayList<Menu>();
		List<Menu> listSousMenu;
		List<Menu> copyListSousMenu;
		MenuModel menuModel;
		boolean findMenu;
		int i;
		int idRole;
		String realRole;
		for (String role : roles) {
			realRole = role.replaceFirst("ROLE_", "").toLowerCase();
			idRole = ldapOperation.getRoleByName(realRole).getReferenceRole();
			listMenuPrincipal = appMgr.getListMenuByIdDroit(idRole);
			for (Menu menuPrincipal : listMenuPrincipal) {
				copyListAuthorities = new ArrayList<ToolBarBean2.MenuModel>();
				for (MenuModel listAuthoritie : listAuthorities) {
					copyListAuthorities.add(listAuthoritie);
				}
				listSousMenu = new ArrayList<Menu>();
				listSousMenu = appMgr.getListSousMenuByIdMenu((int) menuPrincipal.getMenuId(),
						idRole);
				menuModel = new MenuModel(menuPrincipal, listSousMenu);
				findMenu = false;
				i = 0;
				if (!copyListAuthorities.isEmpty()) {
					do {
						if (copyListAuthorities.get(i).getMenuPrincipal().equals(menuPrincipal)) {
							findMenu = true;
						} else {
							i++;
						}
					} while (!findMenu && i < copyListAuthorities.size());
				}
				if (findMenu) {
					copyListSousMenu = new ArrayList<Menu>();
					copyListSousMenu = copyListAuthorities.get(i).getListSousMenu();
					for (Menu menu : listSousMenu) {
						if (!copyListSousMenu.contains(menu)) {
							listAuthorities.get(i).getListSousMenu().add(menu);
						}
					}
				} else {
					listAuthorities.add(menuModel);
				}
			}
		}
		int indexMax;
		MenuModel firstMenuModelToBeReplaced;
		MenuModel secondMenuModelToBeReplaced;
		for (int j = 0; j < listAuthorities.size() - 1; j++) {
			indexMax = j;
			for (int l = j + 1; l < listAuthorities.size(); l++) {
				if (listAuthorities.get(indexMax).getMenuPrincipal().getMenuOrdre().intValue() > listAuthorities
						.get(l).getMenuPrincipal().getMenuOrdre().intValue()) {
					indexMax = l;
				}
			}
			if (indexMax != j) {
				firstMenuModelToBeReplaced = new MenuModel();
				secondMenuModelToBeReplaced = new MenuModel();
				firstMenuModelToBeReplaced = listAuthorities.get(j);
				secondMenuModelToBeReplaced = listAuthorities.get(indexMax);
				listAuthorities.set(j, secondMenuModelToBeReplaced);
				listAuthorities.set(indexMax, firstMenuModelToBeReplaced);
			}
		}
		return listAuthorities;
	}

	public void setToolBar(UIToolBar toolBar) {
		this.toolBar = toolBar;
	}

	public UIToolBar getToolBar() {
		return toolBar;
	}

	public void setAppMgr(ApplicationManager appMgr) {
		this.appMgr = appMgr;
	}

	public ApplicationManager getAppMgr() {
		return appMgr;
	}

	public void setVb(VariableGlobale vb) {
		this.vb = vb;
	}

	public VariableGlobale getVb() {
		return vb;
	}

	public void setToolBarArabe(UIToolBar toolBarArabe) {
		this.toolBarArabe = toolBarArabe;
	}

	public UIToolBar getToolBarArabe() {
		return toolBarArabe;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean isStatus() {
		return status;
	}
}