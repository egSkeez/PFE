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
public class ToolBarBean {
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
	public ToolBarBean(@Qualifier("applicationManager") ApplicationManager appMgr) {
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
			e.printStackTrace();
		}
	}

	public void loadToolbar(List<String> role) {
		System.out.println("LoadToolBar*************");
		List<MenuModel> listAuthorities = new ArrayList<ToolBarBean.MenuModel>();
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
		htmlGraphicImage.setOnclick("document.location.href='../GBO/acceuilGBO.jsf'");
		htmlGraphicImageArabe.setOnclick("document.location.href='../GBO/acceuilGBO.jsf'");
		htmlGraphicImage.setOnmouseover("this.src='../framGraphique/images/accueilActif.png'");
		htmlGraphicImageArabe.setOnmouseover("this.src='../framGraphique/images/accueilActif.png'");
		htmlGraphicImage.setOnmouseout("this.src='../framGraphique/images/accueil.png'");
		htmlGraphicImageArabe.setOnmouseout("this.src='../framGraphique/images/accueil.png'");

		toolBar.getChildren().add(htmlGraphicImage);
		toolBarArabe.getChildren().add(htmlGraphicImageArabe);
		int k = 0;
		for (MenuModel menuModel : listAuthorities) {
			if (menuModel.getListSousMenu().isEmpty()
					&& menuModel.getMenuPrincipal().getMenuClass() != null) {
				menuItem = new HtmlMenuItem();
				menuItemArabe = new HtmlMenuItem();
				menuItem.setId("menuItem00"+k
						);
				menuItemArabe.setId("menuItemArabe00"+k
						);
				menuItem.setValue(menuModel.getMenuPrincipal().getMenuLibelle());
				menuItemArabe.setValue(menuModel.getMenuPrincipal().getMenuLibelleArabe());
				try {
					MethodExpression methodExpression = FacesContext
							.getCurrentInstance()
							.getApplication()
							.getExpressionFactory()
							.createMethodExpression(
									FacesContext.getCurrentInstance().getELContext(),
									"#{" + menuModel.getMenuPrincipal().getMenuClass() + "."
											+ menuModel.getMenuPrincipal().getMenuMethode() + "}",
									null, new Class<?>[0]);
					menuItem.setActionExpression(methodExpression);
					menuItemArabe.setActionExpression(methodExpression);
					menuItem.setSubmitMode("ajax");
					menuItemArabe.setSubmitMode("ajax");
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
				toolBar.getChildren().add(menuItem);
				toolBarArabe.getChildren().add(menuItemArabe);
			} else {
				dropDownMenu = new HtmlDropDownMenu();
				dropDownMenuArabe = new HtmlDropDownMenu();
				dropDownMenu.setId("dropDownMenu" + String.valueOf(k));
				dropDownMenuArabe.setId("dropDownMenuArabe" + String.valueOf(k));
				dropDownMenu.setValue(menuModel.getMenuPrincipal().getMenuLibelle());
				dropDownMenuArabe.setValue(menuModel.getMenuPrincipal().getMenuLibelleArabe());
				dropDownMenuArabe.setDirection("bottom-left");
				int l = 0;
				for (int i = 0; i < menuModel.getListSousMenu().size(); i++) {
					menuItem = new HtmlMenuItem();
					menuItemArabe = new HtmlMenuItem();
					menuItem.setId("menuItem" + String.valueOf(k) + String.valueOf(l));
					menuItemArabe.setId("menuItemArabe" + String.valueOf(k) + String.valueOf(l));
					menuItem.setValue(menuModel.getListSousMenu().get(i).getMenuLibelle());
					menuItemArabe
							.setValue(menuModel.getListSousMenu().get(i).getMenuLibelleArabe());
					MethodExpression methodExpression = FacesContext
							.getCurrentInstance()
							.getApplication()
							.getExpressionFactory()
							.createMethodExpression(
									FacesContext.getCurrentInstance().getELContext(),
									"#{" + menuModel.getListSousMenu().get(i).getMenuClass() + "."
											+ menuModel.getListSousMenu().get(i).getMenuMethode()
											+ "}", null, new Class<?>[0]);
					menuItem.setActionExpression(methodExpression);
					menuItemArabe.setActionExpression(methodExpression);
					menuItem.setSubmitMode("ajax");
					menuItemArabe.setSubmitMode("ajax");
					dropDownMenu.getChildren().add(menuItem);
					dropDownMenuArabe.getChildren().add(menuItemArabe);
					l++;
				}
				toolBar.getChildren().add(dropDownMenu);
				toolBarArabe.getChildren().add(dropDownMenuArabe);
			}
			k++;
		}
	}

	public List<MenuModel> getMapOfRoles(List<String> roles) {
		List<MenuModel> listAuthorities = new ArrayList<ToolBarBean.MenuModel>();
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
				copyListAuthorities = new ArrayList<ToolBarBean.MenuModel>();
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