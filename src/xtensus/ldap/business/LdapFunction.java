package xtensus.ldap.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.Binding;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import org.springframework.beans.factory.annotation.Autowired;

import xtensus.beans.common.VariableGlobale;
import xtensus.beans.utils.ConnexionLdap;
import xtensus.dao.utils.LdapConnectionSingleton;
import xtensus.ldap.model.BOC;
import xtensus.ldap.model.Group;
import xtensus.ldap.model.Person;
import xtensus.ldap.model.Service;
import xtensus.ldap.model.Unit;

public class LdapFunction {

	public final String CONTEXT_USER = "o=XteUsers,dc=xtensus,dc=com";
	public final String CONTEXT_GROUP = "o=XteGroups,dc=xtensus,dc=com";
	public final String CONTEXT_BOC = "ou=OrderOffices,o=XteUnits,dc=xtensus,dc=com";
	public final String CONTEXT_UNIT = "ou=SubUnits,o=XteUnits,dc=xtensus,dc=com";
	public ConnexionLdap connexionLdap;
	public List<Object> ldapData;
	public BOC centralBoc;
	public List<Person> ldapListUser;
	public List<Person> ldapListAllUser;
	public List<Group> ldapListGroup;
	public List<Unit> ldapListUnit;
	public int rowKeyIndex = 0;
	public int newUserId = 0;
	public int newGroupId = 0;
	private DirContext dirContext;
	@Autowired
	private VariableGlobale vb;
	public String rowKey = new String(Integer.toString(rowKeyIndex));
	// **
	public HashMap<Integer, Person> hashMapAllUser;
	public HashMap<Integer, Unit> hashMapUnit;
	public List<BOC> listTousBos = new ArrayList<BOC>();
	public List<Unit>listTousUnit = new ArrayList<Unit>();
	public LdapFunction() {
		dirContext = LdapConnectionSingleton.getInstance();
		hashMapAllUser = new HashMap<Integer, Person>();
		hashMapUnit = new HashMap<Integer, Unit>();
	}

	public void getDataFromDirectory() {
		long start = System.currentTimeMillis();
		System.out.println("Début");
		ldapData = new ArrayList<Object>();
		ldapListUser = new ArrayList<Person>();
		ldapListAllUser = new ArrayList<Person>();
		ldapListGroup = new ArrayList<Group>();
		ldapListUnit = new ArrayList<Unit>();
		listTousBos= new ArrayList<BOC>();
		listTousUnit= new ArrayList<Unit>();
		Runnable runListUnitGroup = new Runnable() {
			@Override
			public void run() {
				System.out.println("DEBUT UNIT");
				getLdapListUnit();
				System.out.println("FIN UNIT");
				System.out.println("DEBUT GROUP");
				getLdapListGroup();
				System.out.println("FIN GROUP");
				// vb.setCopyLdapListGroup(ldapListGroup);
				// vb.setCopyLdapListUnit(ldapListUnit);
			}
		};
		new Thread(runListUnitGroup).start();
		System.out.println("DEBUT ALL USER");
		getLdapListAllUser();
		System.out.println("FIN ALL USER");
		// centralBoc = new BOC();
		System.out.println("DEBUT BOC");
		centralBoc = getLdapBOC();
//		vb.setListTousBos(new ArrayList<BOC>());
//		for(BOC b: listTousBos){
//			System.out.println("BBB "+b);
//		}
		//vb.setListTousBos(listTousBos);
		//System.out.println("FIN BOC");
		System.out.println("FIN : Response time"
				+ (System.currentTimeMillis() - start));
		
		
	}

	public void fillData(Object object, String rowKey) {
		if (object instanceof BOC) {

			//System.out.println("fillData : object instanceof BOC");
			String rowKeyLocal = rowKey + ":";
			BOC boc = (BOC) object;
			//System.out.println("fillData : BOC =  " + boc);

			boc.setRowKeyBOC(rowKey);
			ldapData.add(boc);

			List<Unit> listDirections = new ArrayList<Unit>();
			List<BOC> listBOSecondaires = new ArrayList<BOC>();
//			if (boc.getListChildBOCsBOC() != null)
//			{System.out.println(boc.getListChildBOCsBOC().size());
//				
//			}
//			else{
//				System.out.println("boc.getListChildBOCsBOC() est null");
//			}
			if (boc.getListChildBOCsBOC() != null
					&& boc.getListChildBOCsBOC().size() > 0) {
				// Récupérer la liste des Boc child
				//AAAA
//				boc.setListChildBOCsBOC(getLdapListBOCs(
//					"associatedName", "cn="+boc.getNameBOC()+",ou=OrderOffices,o=XteUnits,dc=xtensus,dc=com", CONTEXT_BOC));
				listBOSecondaires = boc.getListChildBOCsBOC();

				if (listBOSecondaires != null) {
//					System.out.println("fillData : BOC listBOSs  "
//							+ listBOSecondaires.size());
					for (int i = 0; i < listBOSecondaires.size(); i++) {
						listBOSecondaires.get(i).setRowKeyBOC(
								rowKeyLocal + new String(Integer.toString(i)));
						fillData(listBOSecondaires.get(i), rowKeyLocal
								+ new String(Integer.toString(i)));
					}
				} else {
					System.out
							.println("fillData : BOC listBOSecondaires est null pour "+boc.getNameBOC());
				}

			} 
			//else {
				listDirections = boc.getListDirectionsChildBOC();
				if (listDirections != null) {
//					System.out.println("fillData : BOC listDirections  "
//							+ listDirections.size());
					for (int i = 0; i < listDirections.size(); i++) {
						listDirections.get(i).setRowKeyDirection(
								rowKeyLocal + new String(Integer.toString(i)));
						fillData(listDirections.get(i), rowKeyLocal
								+ new String(Integer.toString(i)));
					}
				} else {
					System.out
							.println("fillData : BOC listDirections est null ");
				}
			//}

		} else if (object instanceof Unit) {
			//System.out.println("           fillData : object instanceof Unit");
			//System.out.println();
			String rowKeyLocal = rowKey + ":";
			Unit direction = (Unit) object;
			//listTousUnit.add(direction);
			// direction.get
			direction.setRowKeyDirection(rowKey);
			ldapData.add(direction);
			List<Unit> listDirectons = new ArrayList<Unit>();
			List<BOC> listBODirections = new ArrayList<BOC>();
			listDirectons = direction.getListUnitsChildUnit();
			
			if (listDirectons != null)
				System.out
						.println("           fillData : Unite listDirections  "
								+ listDirectons.size());
			for (int i = 0; i < listDirectons.size(); i++) {
				listDirectons.get(i).setRowKeyDirection(
						rowKeyLocal + new String(Integer.toString(i)));
				fillData(listDirectons.get(i),
						rowKeyLocal + new String(Integer.toString(i)));
			}
			//Liste des BOS
			listBODirections=direction.getListBOChildUnit();
			if (listBODirections != null)
				System.out
						.println("           fillData : Unite listBOS  "
								+ listBODirections.size());
			for (int i = 0; i < listBODirections.size(); i++) {
				listBODirections.get(i).setRowKeyBOC(
						rowKeyLocal + new String(Integer.toString(i)));
				fillData(listBODirections.get(i),
						rowKeyLocal + new String(Integer.toString(i)));
			}
			
		}
	}

	public List<BOC> getLdapListBOCs(String attribute, String attributeValue,
			String path,Object o) {

		List<BOC> resultList = new ArrayList<BOC>();
		try {
			BasicAttributes attributes = new BasicAttributes(true);
			Attribute attribut = new BasicAttribute(attribute);
			attribut.add(attributeValue);
			attributes.put(attribut);
			@SuppressWarnings("rawtypes")
			NamingEnumeration e = dirContext.search(path, attributes);
			BOC boc;
			int i=0;
			while (e.hasMore()) {
				//System.out.println( " i :  "+i);
				i++;
				boc = new BOC();
				Binding b = (Binding) e.next();
				boc.setNameBOC(getLdapEntryAttribute("cn",
						b.getNameInNamespace()));
				boc.setIdBOC(Integer.parseInt(getLdapEntryAttribute(
						"departmentNumber", b.getNameInNamespace())));
				boc.setDescriptionBOC(getLdapEntryAttribute("description",
						b.getNameInNamespace()));
				boc.setShortNameBOC(getLdapEntryAttribute("l",
						b.getNameInNamespace()));
//				jh
//				boc.setMembersBOC(getLdapListMembers1("member",
//						b.getNameInNamespace(), boc));
				List<Person> tousmembre= new ArrayList<Person>();
				List<Person> pr = getLdapListMembers1("member",
						b.getNameInNamespace(), boc);
				if (pr != null && pr.size() > 0) {
					for (Person pme : pr) {
						pme.setResponsableBO(false);
						pme.setAgentBO(true);
						pme.setResponsable(false);
						pme.setSecretary(false);
						pme.setBoc(true);
					}
					tousmembre.addAll(pr);
					//centralBoc.getMembersBOC().addAll(pr);
				}

				List<Person> ps = getLdapListMembers1("manager",b.getNameInNamespace(), boc);
				
				if (ps != null && ps.size() > 0) {
					for (Person pm : ps) {
						pm.setResponsableBO(true);
						pm.setAgentBO(false);
						pm.setResponsable(true);
						pm.setSecretary(false);
						pm.setBoc(true);
					}
					tousmembre.addAll(ps);
					//centralBoc.getMembersBOC().addAll(ps);

				} else {
					//System.out.println("la liste des manager BOC est vide");
				}
				boc.setMembersBOC(tousmembre);
				
				boc.setTypeBOC("Secondaire");
				
				boc.setListDirectionsChildBOC(getLdapListDirectionsGeneral(
						"associatedName", b.getNameInNamespace().trim(), CONTEXT_UNIT,
						boc));
				if(o!= null && o instanceof Unit)
					
				boc.setAssociatedDirection((Unit)o);
				
				if(o!=null && o instanceof BOC)
					boc.setAssociatedBOC((BOC)o);
				resultList.add(boc);
				//Ajouter l'associated BOC ou direction
				if(!listTousBos.contains(boc))
				listTousBos.add(boc);
			}
		} catch (NamingException e) {
			//System.out.println("Erreur lors de l'acces au serveur LDAP:" + e);
			e.printStackTrace();
		}
//		for (int i = 0; i < resultList.size(); i++) {
//			//System.out.println("BOC "+resultList.get(i));
//		}

		return resultList;

	}

	public List<Person> getLdapListMembers1(String attribute, String path,
			Object associatedObject) {//XXX
		// AAAA
		//"manager",b.getNameInNamespace(), boc
//		//System.out.println("###############################################################");
//		//System.out.println("attribute  " +attribute);
//		//System.out.println("path  :"+path);
		
		
		String name;
		Person person;
		List<Person> resultList = new ArrayList<Person>();
		int i = 0;
		boolean exception = false;
		try {
			do {
				try {
					person = new Person();
					Attribute att = dirContext.getAttributes(path).get(attribute);
					if(att!=null && dirContext.getAttributes(path).get(attribute).size()>0)
					{name = dirContext.getAttributes(path).get(attribute).get(i)
							.toString();
					//System.out.println("namse :: "+name );
					}
					else{
						break;
					}
					
				//	//System.out.println(" name =" + name);
					person.setCn(getLdapEntryAttribute("cn", name));
					person.setId(Integer.parseInt(getLdapEntryAttribute("uid",
							name)));
					person.setNom(getLdapEntryAttribute("sn", name));
					 if(getLdapEntryAttribute("givenName", name)!=null && !getLdapEntryAttribute("givenName", name).equals(""))
					person.setPrenom(getLdapEntryAttribute("givenName", name));
					 else
						 person.setPrenom(getLdapEntryAttribute("givenName", ""));
					person.setShortName(getLdapEntryAttribute("employeeNumber",
							name));
					person.setTelephoneNumber(getLdapEntryAttribute(
							"telephoneNumber", name));
					
					person.setFax(getLdapEntryAttribute(
							"facsimileTelephoneNumber", name));
					person.setEmail(getLdapEntryAttribute("mail", name));
					person.setAdresse(getLdapEntryAttribute("street", name));
					person.setCodePostal(Integer
							.parseInt(getLdapEntryAttribute("postalCode", name)));
					person.setLogin(getLdapEntryAttribute("displayName", name));
					person.setPwd(getLdapEntryAttribute("userPassword", name));

//					//System.out.println(" person id = " + person.getId());
					if (person.getId() == 0) {
						person.setBoc(true);
					} else {
						person.setTitle("3.Agent");
						person.setAgent(true);
					}
					
					if(attribute.equals("membre")){
						person.setTitle("3.Agent");
						person.setAgent(true);
					}
					if(attribute.equals("manager")){
						person.setTitle("1.Responsable");
						person.setResponsable(true);
					}
					if(attribute.equals("secretary")){
						person.setTitle("2.Secrétaire");
						person.setSecretary(true);
					}
					
					if (associatedObject instanceof BOC) {
						BOC boc = (BOC) associatedObject;
						person.setAssociatedBOC(boc);
						

					} else if (associatedObject instanceof Unit) {
						Unit direction = (Unit) associatedObject;
						person.setAssociatedDirection(direction);
						ldapListUser.add(person);
					} else {
						Service service = (Service) associatedObject;
						person.setAssociatedService(service);
						ldapListUser.add(person);
					}
					resultList.add(person);
//					//System.out.println(person);
					i++;
				} catch (ArrayIndexOutOfBoundsException ex) {
					exception = true;
				}
			} while (!exception);

		} catch (NamingException e) {
			//System.out.println("Erreur lors de l'acces au serveur LDAP:" + e);
			e.printStackTrace();
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
		//System.out.println(resultList.size());
		return resultList;

	}

	public List<Unit> getLdapListDirectionsGeneral(String attribute,
			String attributeValue, String path, BOC boc) {
		List<Unit> resultList = new ArrayList<Unit>();
//		System.out.println("");
//		System.out.println("=================Direction BOCT===============================");
//		System.out.println("Dans getLdapListDirectionsGeneral");
//		System.out.println("attribute  " + attribute);
//		System.out.println("attributeValue  " + attributeValue);
//		System.out.println("path  " + path);

//		System.out.println("================================================");
//		System.out.println("");
		try {
			BasicAttributes attributes = new BasicAttributes(true);
			Attribute attribut = new BasicAttribute(attribute);
			attribut.add(attributeValue);
			attributes.put(attribut);
			@SuppressWarnings("rawtypes")
			NamingEnumeration e = dirContext.search(path, attributes);
			Unit direction;
			while (e.hasMore()) {
				direction = new Unit();
				Binding b = (Binding) e.next();
				direction.setNameUnit(getLdapEntryAttribute("cn",
						b.getNameInNamespace()));
				direction.setIdUnit(Integer.parseInt(getLdapEntryAttribute(
						"departmentNumber", b.getNameInNamespace())));
				direction.setDescriptionUnit(getLdapEntryAttribute(
						"description", b.getNameInNamespace()));
				direction.setShortNameUnit(getLdapEntryAttribute("l",
						b.getNameInNamespace()));
				direction.setAssociatedBOC(boc);
				String directeur = getLdapEntryAttribute("manager",
						b.getNameInNamespace());
				if (!directeur.equals("")) {
					direction
							.setResponsibleUnit(new Person(Integer
									.parseInt(getLdapEntryAttribute("uid",
											directeur)), getLdapEntryAttribute(
									"cn", directeur), getLdapEntryAttribute(
									"sn", directeur), getLdapEntryAttribute(
									"givenName", directeur),
									getLdapEntryAttribute("employeeNumber",
											directeur), getLdapEntryAttribute(
											"telephoneNumber", directeur),
									getLdapEntryAttribute("mail", directeur),
									getLdapEntryAttribute("street", directeur),
									(getLdapEntryAttribute(
											"facsimileTelephoneNumber",
											directeur)), Integer
											.parseInt(getLdapEntryAttribute(
													"postalCode", directeur)),
									getLdapEntryAttribute("displayName",
											directeur), getLdapEntryAttribute(
											"userPassword", directeur)));
				}
				String secretaire = getLdapEntryAttribute("secretary",
						b.getNameInNamespace());
				if (!secretaire.equals("")) {
					direction
							.setSecretaryUnit(new Person(
									Integer.parseInt(getLdapEntryAttribute(
											"uid", secretaire)),
									getLdapEntryAttribute("cn", secretaire),
									getLdapEntryAttribute("sn", secretaire),
									getLdapEntryAttribute("givenName",
											secretaire),
									getLdapEntryAttribute("employeeNumber",
											secretaire),
									getLdapEntryAttribute("telephoneNumber",
											secretaire),
									getLdapEntryAttribute("mail", secretaire),
									getLdapEntryAttribute("street", secretaire),
									(getLdapEntryAttribute(
											"facsimileTelephoneNumber",
											secretaire)), Integer
											.parseInt(getLdapEntryAttribute(
													"postalCode", secretaire)),
									getLdapEntryAttribute("displayName",
											secretaire), getLdapEntryAttribute(
											"userPassword", secretaire)));
				}
//				System.out.println("direction encours "+direction.getNameUnit());
				direction.setListUnitsChildUnit(getLdapListDirections(
						"associatedName", b.getNameInNamespace(), path,
						direction));
				
				String s= "cn="+direction.getNameUnit().trim()+","+CONTEXT_BOC;
//				System.out.println("Le contexte de recherche : "+s);
				List<BOC> lb = getLdapListBOCs("associatedName", s, "cn=Bureau d'Ordre Central,"+CONTEXT_BOC,direction);
				direction.setListBOChildUnit(lb);
				//AAAA
			//	System.out.println("Récupérer les sous BOS de la direction "+ direction.getNameUnit()+" "+lb.size());
//				if(lb!=null&& lb.size()>0){
//					System.out.println("nombre des sous BO de "+direction.getNameUnit().trim() +" : "+lb.size());
//				}
//				else{
//					System.out.println("nombre des sous BO est "+direction.getNameUnit().trim() +" : "+0);
//				}
				
				List<Unit> listAdjoiningsDirections = new ArrayList<Unit>();
				listAdjoiningsDirections = getLdapListAdjoiningDirections(
						"associatedName", "cn="
								+ direction.getAssociatedBOC().getNameBOC()
								+ "," + CONTEXT_BOC, path, null);
				for (int i = 0; i < listAdjoiningsDirections.size(); i++) {
					if (direction.getIdUnit() == listAdjoiningsDirections
							.get(i).getIdUnit()) {
						listAdjoiningsDirections.remove(i);
					}
				}
				direction.setListAdjoiningUnitsUnit(listAdjoiningsDirections);

				direction.setMembersUnit(getLdapListMembers1("member",
						b.getNameInNamespace(), direction));
				if (direction.getResponsibleUnit() != null) {
					direction.getResponsibleUnit().setResponsable(true);
					direction.getResponsibleUnit().setTitle("1.Responsable");
					direction.getResponsibleUnit().setAssociatedDirection(
							direction);
					ldapListUser.add(direction.getResponsibleUnit());
				}
				if (direction.getSecretaryUnit() != null) {
					direction.getSecretaryUnit().setSecretary(true);
					direction.getSecretaryUnit().setTitle("2.Secrétaire");
					direction.getSecretaryUnit().setAssociatedDirection(
							direction);
					ldapListUser.add(direction.getSecretaryUnit());
				}
				listTousUnit.add(direction);
				resultList.add(direction);

			}
		} catch (NamingException e) {
			System.out.println("Erreur lors de l'acces au serveur LDAP:" + e);
			e.printStackTrace();
		}
		return resultList;

	}

	public List<Unit> getLdapListDirections(String attribute,
			String attributeValue, String path, Unit associatedDirection) {
//		System.out.println("");
//		System.out.println("================================================");
		List<Unit> resultList = new ArrayList<Unit>();
//		System.out.println("2222   Dans getLdapListDirections");
//		System.out.println("attribute  " + attribute);
//		System.out.println("attributeValue  " + attributeValue);
//		System.out.println("path  " + path);
//
//		System.out.println("================================================");
//		System.out.println("");

		try {
			BasicAttributes attributes = new BasicAttributes(true);
			Attribute attribut = new BasicAttribute(attribute);
			attribut.add(attributeValue);
			attributes.put(attribut);
			@SuppressWarnings("rawtypes")
			NamingEnumeration e = dirContext.search(path, attributes);
			Unit direction;
			while (e.hasMore()) {
				direction = new Unit();
				Binding b = (Binding) e.next();
				direction.setNameUnit(getLdapEntryAttribute("cn",
						b.getNameInNamespace()));
				direction.setIdUnit(Integer.parseInt(getLdapEntryAttribute(
						"departmentNumber", b.getNameInNamespace())));
				direction.setDescriptionUnit(getLdapEntryAttribute(
						"description", b.getNameInNamespace()));
				direction.setShortNameUnit(getLdapEntryAttribute("l",
						b.getNameInNamespace()));
				direction.setAssociatedUnit(associatedDirection);
				String directeur = getLdapEntryAttribute("manager",
						b.getNameInNamespace());
				if (!directeur.equals("")) {
					direction
							.setResponsibleUnit(new Person(Integer
									.parseInt(getLdapEntryAttribute("uid",
											directeur)), getLdapEntryAttribute(
									"cn", directeur), getLdapEntryAttribute(
									"sn", directeur), getLdapEntryAttribute(
									"givenName", directeur),
									getLdapEntryAttribute("employeeNumber",
											directeur), getLdapEntryAttribute(
											"telephoneNumber", directeur),
									getLdapEntryAttribute("mail", directeur),
									getLdapEntryAttribute("street", directeur),
									getLdapEntryAttribute(
											"facsimileTelephoneNumber",
											directeur), Integer
											.parseInt(getLdapEntryAttribute(
													"postalCode", directeur)),
									getLdapEntryAttribute("displayName",
											directeur), getLdapEntryAttribute(
											"userPassword", directeur)));
				}
				String secretaire = getLdapEntryAttribute("secretary",
						b.getNameInNamespace());
				if (!secretaire.equals("")) {
					direction
							.setSecretaryUnit(new Person(
									Integer.parseInt(getLdapEntryAttribute(
											"uid", secretaire)),
									getLdapEntryAttribute("cn", secretaire),
									getLdapEntryAttribute("sn", secretaire),
									getLdapEntryAttribute("givenName",
											secretaire),
									getLdapEntryAttribute("employeeNumber",
											secretaire),
									getLdapEntryAttribute("telephoneNumber",
											secretaire),
									getLdapEntryAttribute("mail", secretaire),
									getLdapEntryAttribute("street", secretaire),
									getLdapEntryAttribute(
											"facsimileTelephoneNumber",
											secretaire), Integer
											.parseInt(getLdapEntryAttribute(
													"postalCode", secretaire)),
									getLdapEntryAttribute("displayName",
											secretaire), getLdapEntryAttribute(
											"userPassword", secretaire)));
				}
				String s= "cn="+ direction.getNameUnit().trim()+","+CONTEXT_BOC;
				List<BOC> lb = getLdapListBOCs("associatedName", s,  "cn=Bureau d'Ordre Central,"+CONTEXT_BOC,direction);
				direction.setListBOChildUnit(lb);
				
//				System.out.println("Récupérer les sous BOS de la direction "+ direction.getNameUnit()+" "+lb.size());
				List<Unit> listAdjoiningsDirections = new ArrayList<Unit>();
				listAdjoiningsDirections = getLdapListAdjoiningDirections(
						"associatedName", "cn="
								+ direction.getAssociatedUnit().getNameUnit()
								+ "," + CONTEXT_UNIT, path,
						direction.getAssociatedUnit());
				for (int i = 0; i < listAdjoiningsDirections.size(); i++) {
					if (direction.getIdUnit() == listAdjoiningsDirections
							.get(i).getIdUnit()) {
						listAdjoiningsDirections.remove(i);
					}
				}
				direction.setListAdjoiningUnitsUnit(listAdjoiningsDirections);
				direction.setListUnitsChildUnit(getLdapListDirections(
						"associatedName", b.getNameInNamespace(), path,
						direction));
				direction.setMembersUnit(getLdapListMembers1("member",
						b.getNameInNamespace(), direction));
				if (direction.getResponsibleUnit() != null) {
					direction.getResponsibleUnit().setResponsable(true);
					direction.getResponsibleUnit().setTitle("1.Responsable");
					direction.getResponsibleUnit().setAssociatedDirection(
							direction);
					ldapListUser.add(direction.getResponsibleUnit());
				}
				if (direction.getSecretaryUnit() != null) {
					direction.getSecretaryUnit().setSecretary(true);
					direction.getSecretaryUnit().setTitle("2.Secrétaire");
					direction.getSecretaryUnit().setAssociatedDirection(
							direction);
					ldapListUser.add(direction.getSecretaryUnit());
				}
				
								
				listTousUnit.add(direction);
				resultList.add(direction);

			}
		} catch (NamingException e) {
			System.out.println("Erreur lors de l'acces au serveur LDAP:" + e);
			e.printStackTrace();
		}
		return resultList;

	}

	public List<Unit> getLdapListAdjoiningDirections(String attribute,
			String attributeValue, String path, Unit associatedDirection) {
		List<Unit> resultList = new ArrayList<Unit>();
//		System.out
//				.println("DANS getLdapListAdjoiningDirections ================================");
		try {
			BasicAttributes attributes = new BasicAttributes(true);
			Attribute attribut = new BasicAttribute(attribute);
			attribut.add(attributeValue);
			attributes.put(attribut);
			NamingEnumeration e = dirContext.search(path, attributes);
			Unit direction;
			while (e.hasMore()) {
				direction = new Unit();
				Binding b = (Binding) e.next();

				direction.setNameUnit(getLdapEntryAttribute("cn",
						b.getNameInNamespace()));
				direction.setIdUnit(Integer.parseInt(getLdapEntryAttribute(
						"departmentNumber", b.getNameInNamespace())));
				direction.setDescriptionUnit(getLdapEntryAttribute(
						"description", b.getNameInNamespace()));
				direction.setShortNameUnit(getLdapEntryAttribute("l",
						b.getNameInNamespace()));
				direction.setAssociatedUnit(associatedDirection);
				String directeur = getLdapEntryAttribute("manager",
						b.getNameInNamespace());
				if (!directeur.equals("")) {
					direction
							.setResponsibleUnit(new Person(Integer
									.parseInt(getLdapEntryAttribute("uid",
											directeur)), getLdapEntryAttribute(
									"cn", directeur), getLdapEntryAttribute(
									"sn", directeur), getLdapEntryAttribute(
									"givenName", directeur),
									getLdapEntryAttribute("employeeNumber",
											directeur), getLdapEntryAttribute(
											"telephoneNumber", directeur),
									getLdapEntryAttribute("mail", directeur),
									getLdapEntryAttribute("street", directeur),
									getLdapEntryAttribute(
											"facsimileTelephoneNumber",
											directeur), Integer
											.parseInt(getLdapEntryAttribute(
													"postalCode", directeur)),
									getLdapEntryAttribute("displayName",
											directeur), getLdapEntryAttribute(
											"userPassword", directeur), true,
									false));
				}
				String secretaire = getLdapEntryAttribute("secretary",
						b.getNameInNamespace());
				if (!secretaire.equals("")) {
					direction
							.setSecretaryUnit(new Person(
									Integer.parseInt(getLdapEntryAttribute(
											"uid", secretaire)),
									getLdapEntryAttribute("cn", secretaire),
									getLdapEntryAttribute("sn", secretaire),
									getLdapEntryAttribute("givenName",
											secretaire),
									getLdapEntryAttribute("employeeNumber",
											secretaire),
									getLdapEntryAttribute("telephoneNumber",
											secretaire),
									getLdapEntryAttribute("mail", secretaire),
									getLdapEntryAttribute("street", secretaire),
									getLdapEntryAttribute(
											"facsimileTelephoneNumber",
											secretaire), Integer
											.parseInt(getLdapEntryAttribute(
													"postalCode", secretaire)),
									getLdapEntryAttribute("displayName",
											secretaire), getLdapEntryAttribute(
											"userPassword", secretaire), false,
									true));
				}
				try {
					if (direction.getResponsibleUnit() != null)
						direction.getResponsibleUnit().setAssociatedDirection(
								direction);
					if (direction.getSecretaryUnit() != null)
						direction.getSecretaryUnit().setAssociatedDirection(
								direction);
				} catch (Exception e2) {
					e2.printStackTrace();
					System.out.println("responsable non affecté" + e2);

				}
				// try {
				//
				// direction.getSecretaryUnit().setAssociatedDirection(
				// direction);
				// } catch (Exception e2) {
				// //System.out.println("secretaire non affecté" + e2);
				//
				// }
				resultList.add(direction);

			}
		} catch (NamingException e) {
			System.out.println("Erreur lors de l'acces au serveur LDAP:" + e);
			e.printStackTrace();
		}
		return resultList;
	}

	public List<Group> getLdapListGroups(String attribute,
			String attributeValue, String path) {
		List<Group> resultList = new ArrayList<Group>();

		try {
			BasicAttributes attributes = new BasicAttributes(true);
			Attribute attribut = new BasicAttribute(attribute);
			attribut.add(attributeValue);
			attributes.put(attribut);
			NamingEnumeration e = dirContext.search(path, attributes);
			Group group;
			while (e.hasMore()) {
				group = new Group();
				Binding b = (Binding) e.next();
				group.setId(Integer.parseInt(getLdapEntryAttribute("gidNumber",
						b.getNameInNamespace())));
				group.setCn(getLdapEntryAttribute("cn", b.getNameInNamespace()));
				group.setDescription(getLdapEntryAttribute("description",
						b.getNameInNamespace()));

				resultList.add(group);
			}
		} catch (NamingException e) {
			System.out.println("Erreur lors de l'acces au serveur LDAP:" + e);
			e.printStackTrace();
		}
		return resultList;

	}

	public List<Person> getLdapListMembers(String attribute, String path) {
		String name;
		Person person;
		List<Person> resultList = new ArrayList<Person>();
		int i = 0;
		boolean exception = false;

		try {
			name = dirContext.getAttributes(path).get(attribute).get(i)
					.toString();
			if (!name.equals("")) {
				do {
					try {
						person = new Person();
						name = dirContext.getAttributes(path).get(attribute)
								.get(i).toString();
						person.setCn(getLdapEntryAttribute("cn", name));
						person.setId(Integer.parseInt(getLdapEntryAttribute(
								"uid", name)));
						person.setNom(getLdapEntryAttribute("sn", name));
						person.setPrenom(getLdapEntryAttribute("givenName",
								name));
						person.setShortName(getLdapEntryAttribute(
								"employeeNumber", name));
						person.setTelephoneNumber(getLdapEntryAttribute(
								"telephoneNumber", name));
						person.setFax(getLdapEntryAttribute(
								"facsimileTelephoneNumber", name));
						person.setEmail(getLdapEntryAttribute("mail", name));
						person.setAdresse(getLdapEntryAttribute("street", name));
						person.setCodePostal(Integer
								.parseInt(getLdapEntryAttribute("postalCode",
										name)));
						person.setLogin(getLdapEntryAttribute("displayName",
								name));
						person.setPwd(getLdapEntryAttribute("userPassword",
								name));
						resultList.add(person);
						i++;
					} catch (ArrayIndexOutOfBoundsException ex) {
						exception = true;
					}

				} while (!exception);
			}

		} catch (NamingException e) {
			System.out.println("Erreur lors de l'acces au serveur LDAP:" + e);
			e.printStackTrace();
		} catch (NullPointerException ex) {

		}
		return resultList;

	}

	public String getLdapEntryAttribute(String attribute, String path) {
		String result = "";
		try {
			
			String res = dirContext.getAttributes(path).get(attribute).get(0)
					.toString();
			if (!res.equals(" ")) {
				result = res;
			}
			
		} catch (NamingException e) {
			System.out.println("Erreur lors de l'acces au serveur LDAP:" + e);
			e.printStackTrace();
		} catch (NullPointerException ex) {

		} catch (NumberFormatException ex) {
			result = null;

		}
		return result;
	}

	public void getLdapListAllUser() {
		Person person;
		try {
			NamingEnumeration e = dirContext.listBindings(CONTEXT_USER);
			while (e.hasMore()) {
				Binding b = (Binding) e.next();
				if (Integer.parseInt(getLdapEntryAttribute("uid",
						b.getNameInNamespace())) != 0) {
					person = new Person();
					person.setCn(getLdapEntryAttribute("cn",
							b.getNameInNamespace()));
					person.setId(Integer.parseInt(getLdapEntryAttribute("uid",
							b.getNameInNamespace())));
					person.setNom(getLdapEntryAttribute("sn",
							b.getNameInNamespace()));
					person.setPrenom(getLdapEntryAttribute("givenName",
							b.getNameInNamespace()));
					person.setShortName(getLdapEntryAttribute("employeeNumber",
							b.getNameInNamespace()));
//					person.setTelephoneNumber(getLdapEntryAttribute(
//							"telephoneNumber", b.getNameInNamespace()));
					String tel=getLdapEntryAttribute(
							"telephoneNumber", b.getNameInNamespace());
					if (tel.equals("")) {
						person.setTelephoneNumber(null);
					} else {
						person.setTelephoneNumber(tel);
					}
					person.setEmail(getLdapEntryAttribute("mail",
							b.getNameInNamespace()));
					person.setAdresse(getLdapEntryAttribute("street",
							b.getNameInNamespace()));
					String fax = getLdapEntryAttribute(
							"facsimileTelephoneNumber", b.getNameInNamespace());
					if (fax.equals(" ")) {
						person.setFax(null);
					} else {
						person.setFax(fax);
					}
					String codPostal = getLdapEntryAttribute("postalCode",
							b.getNameInNamespace());
					if (codPostal.equals("")) {
						person.setCodePostal(null);
					} else {
						person.setCodePostal(Integer.parseInt(codPostal));
					}
					person.setLogin(getLdapEntryAttribute("displayName",
							b.getNameInNamespace()));
					person.setPwd(getLdapEntryAttribute("userPassword",
							b.getNameInNamespace()));
					person.setListAffectedGroups(getLdapListGroups("member",
							b.getNameInNamespace(), CONTEXT_GROUP));
					if (person.getId() > newUserId) {
						newUserId = person.getId();
					}
					ldapListAllUser.add(person);
					hashMapAllUser.put(person.getId(), person);
				}
			}
		} catch (NamingException e) {
			System.out.println("Erreur lors de l'acces au serveur LDAP:" + e);
			e.printStackTrace();
		}
	}

	public void getLdapListGroup() {
		long start = System.currentTimeMillis();
		Group group;
		try {
			NamingEnumeration e = dirContext.listBindings(CONTEXT_GROUP);

			while (e.hasMore()) {
				Binding b = (Binding) e.next();
				group = new Group();
				group.setCn(getLdapEntryAttribute("cn", b.getNameInNamespace()));
				group.setId(Integer.parseInt(getLdapEntryAttribute("gidNumber",
						b.getNameInNamespace())));
				group.setDescription(getLdapEntryAttribute("description",
						b.getNameInNamespace()));
				group.setListUser(getLdapListMembers("member",
						b.getNameInNamespace()));
				if (group.getId() > newGroupId) {
					newGroupId = group.getId();
				}
				ldapListGroup.add(group);
			}

		} catch (NamingException e) {
			System.out.println("Erreur lors de l'acces au serveur LDAP:" + e);
			e.printStackTrace();
		}
	}

	public void getLdapListUnit() {
		Unit unit;
		try {
			NamingEnumeration e = dirContext.listBindings(CONTEXT_UNIT);
			while (e.hasMore()) {
				Binding b = (Binding) e.next();
				unit = new Unit();
				unit.setIdUnit(Integer.parseInt(getLdapEntryAttribute(
						"departmentNumber", b.getNameInNamespace())));
				unit.setNameUnit(getLdapEntryAttribute("cn",
						b.getNameInNamespace()));
				unit.setDescriptionUnit(getLdapEntryAttribute("description",
						b.getNameInNamespace()));
				unit.setShortNameUnit(getLdapEntryAttribute("l",
						b.getNameInNamespace()));
				ldapListUnit.add(unit);
				hashMapUnit.put(unit.getIdUnit(), unit);
			}
		} catch (NamingException e) {
			System.out.println("Erreur lors de l'acces au serveur LDAP:" + e);
			e.printStackTrace();
		}
	}

	public BOC getLdapBOC() {
		BOC boc;
		BOC centralBoc = new BOC();
		try {
			NamingEnumeration e = dirContext.listBindings(CONTEXT_BOC);
			while (e.hasMore()) {
				Binding b = (Binding) e.next();
				
				centralBoc.setNameBOC(getLdapEntryAttribute("cn",b.getNameInNamespace()));
				centralBoc.setIdBOC(Integer.parseInt(getLdapEntryAttribute("departmentNumber", b.getNameInNamespace())));
				centralBoc.setDescriptionBOC(getLdapEntryAttribute("description", b.getNameInNamespace()));
				centralBoc.setListDirectionsChildBOC(getLdapListDirectionsGeneral("associatedName", b.getNameInNamespace().toString(),CONTEXT_UNIT, centralBoc));
				centralBoc.setTypeBOC("Central");
				centralBoc.setAssociatedBOC(null);
				centralBoc.setShortNameBOC(getLdapEntryAttribute("l",b.getNameInNamespace()));
				centralBoc.setListChildBOCsBOC(getLdapListBOCs("associatedName", b.getNameInNamespace().trim(), CONTEXT_BOC,centralBoc));
								
				listTousBos.add(centralBoc);
				List<BOC> listeFilsBO = getLdapListBOCs("associatedName",b.getNameInNamespace().trim(), "cn=Bureau d'Ordre Central,"+CONTEXT_BOC,centralBoc);
//				if (listeFilsBO != null) {
//					System.out.println("AH -->  listeFilsBO "+ b.getNameInNamespace().trim()+"  "+ listeFilsBO.size());
//					for (BOC n : listeFilsBO) {
//						System.out.println(n.getNameBOC());
//						//listTousBos.add(n);
//					}
//				}

				centralBoc.setListChildBOCsBOC(listeFilsBO);
				// AH : récupératioon le la liste des "membre" sous Boc
				// centralBoc.setMembersBOC(getLdapListMembers1("member",b.getNameInNamespace(),
				// centralBoc));
				List<Person> tousmembre= new ArrayList<Person>();
				List<Person> pr = getLdapListMembers1("member",
						b.getNameInNamespace(), centralBoc);
				if (pr != null && pr.size() > 0) {
					for (Person pme : pr) {
						pme.setResponsableBO(false);
						pme.setAgentBO(true);
						pme.setResponsable(false);
						pme.setSecretary(false);
						pme.setBoc(true);
					}
					tousmembre.addAll(pr);
					//centralBoc.getMembersBOC().addAll(pr);
				}

				List<Person> ps = getLdapListMembers1("manager",b.getNameInNamespace(), centralBoc);
				
				if (ps != null && ps.size() > 0) {
					for (Person pm : ps) {
						pm.setResponsableBO(true);
						pm.setAgentBO(false);
						pm.setResponsable(true);
						pm.setSecretary(false);
						pm.setBoc(true);
					}
					tousmembre.addAll(ps);
					//centralBoc.getMembersBOC().addAll(ps);

				} else {
					System.out.println("la liste des manager BOC est vide");
				}
				centralBoc.setMembersBOC(tousmembre);
				for (int i = 0; i < centralBoc.getMembersBOC().size(); i++) {
					centralBoc.getMembersBOC().get(i)
							.setAssociatedBOC(centralBoc);
//					centralBoc.getMembersBOC().get(i).setAgent(false);
//					centralBoc.getMembersBOC().get(i).setBoc(true);
					ldapListUser.add(centralBoc.getMembersBOC().get(i));
				}
			}
			e = dirContext.listBindings("cn=" + centralBoc.getNameBOC() + ","+ CONTEXT_BOC);
			System.out.println("binding :"+"cn=" + centralBoc.getNameBOC() + ","+ CONTEXT_BOC);
			List<BOC> listChildBOC = new ArrayList<BOC>();
			while (e.hasMore()) {
				
				Binding b = (Binding) e.next();
				boc = new BOC();
				
				boc.setNameBOC(getLdapEntryAttribute("cn",b.getNameInNamespace()));
				boc.setIdBOC(Integer.parseInt(getLdapEntryAttribute("departmentNumber", b.getNameInNamespace())));
				boc.setDescriptionBOC(getLdapEntryAttribute("description",b.getNameInNamespace()));
				boc.setListDirectionsChildBOC(getLdapListDirectionsGeneral("associatedName", b.getNameInNamespace().trim(), CONTEXT_UNIT,boc));
//				System.out.println("Liste Unité de BO " +boc.getNameBOC() +" = "+boc.getListDirectionsChildBOC().size());
//				System.out.println(" KHA ===> recuperer liste des sous BO "+getLdapEntryAttribute("cn",b.getNameInNamespace()));
//				System.out.println("associatedName   :"+getLdapEntryAttribute("associatedName",b.getNameInNamespace()));
				List<BOC> listeFilsBOs = getLdapListBOCs("associatedName",b.getNameInNamespace().trim(), "cn=Bureau d'Ordre Central,"+CONTEXT_BOC,boc);
				boc.setListChildBOCsBOC(listeFilsBOs);
				
//				System.out.println("Liste BOS de BO " +boc.getNameBOC() +" = "+boc.getListChildBOCsBOC().size());
				boc.setTypeBOC("Secondaire");
				int indexVirgule=getLdapEntryAttribute("associatedName",b.getNameInNamespace()).indexOf(",");
//				System.out.println("AH>>>>>>>>> "+getLdapEntryAttribute("associatedName",b.getNameInNamespace()).substring(3, indexVirgule));
				String nomStructure=getLdapEntryAttribute("associatedName",b.getNameInNamespace()).substring(3, indexVirgule);
				
				
				
				if(!nomStructure.equals(centralBoc.getNameBOC())){
//					System.out.println("<<<<<<<<<<<<<<<<dans  :"+boc.getNameBOC());
//					System.out.println("C'est un BO avec parent n'est pas le BOCT plus tot "+nomStructure);
//					
//					
//					System.out.println("nomStructure  :"+nomStructure);
//					System.out.println("centralBoc.getNameBOC()  :"+centralBoc.getNameBOC());
//					
//					System.out.println(getLdapEntryAttribute("associatedName",b.getNameInNamespace()).substring(3, indexVirgule));
					LdapOperation ldapOperation =new LdapOperation();

//					Unit uniteAssocie=ldapOperation.getUnitByCN(nomStructure);
//					if(uniteAssocie!=null){
//						System.out.println("uniteAssocie!=null"+uniteAssocie);
//						boc.setAssociatedDirection(uniteAssocie);
//						boc.setListChildBOCsBOC(getLdapListBOCs("associatedName",b.getNameInNamespace(),"cn=Bureau d'Ordre Central,"+ CONTEXT_BOC,boc));	
//					}
//					BOC bo=ldapOperation.getBocByCn(nomStructure);
//					if(bo!=null && bo.getIdBOC()!=0 )
//					{System.out.println("bo!=null");
//						boc.setAssociatedBOC(bo);//à ajouter
//					 boc.setListChildBOCsBOC(getLdapListBOCs("associatedName",b.getNameInNamespace(),"cn=Bureau d'Ordre Central,"+ CONTEXT_BOC,boc));
//					}
//					System.out.println("Liste BOS de BO 2 " +boc.getNameBOC() +" = "+boc.getListChildBOCsBOC().size());
					
				}
				else{
//					System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>dans else :"+boc.getNameBOC());
//				System.out.println("nomStructure  :"+nomStructure);
//				System.out.println("centralBoc.getNameBOC()  :"+centralBoc.getNameBOC());
					boc.setAssociatedBOC(centralBoc);
					boc.setListChildBOCsBOC(getLdapListBOCs("associatedName",b.getNameInNamespace(),"cn="+centralBoc.getNameBOC()+","+ CONTEXT_BOC,boc));
				}
				

//				System.out.println("Liste BO de "+boc.getNameBOC()+" = "+boc.getListChildBOCsBOC().size()  );
				
				// ================================================
				// chercher memebre et chercher responsable
				List<Person> membresBoc = new ArrayList<Person>();

				// KHA ===> recupérer responsable BO

				List<Person> personResponsable = getLdapListMembers1("manager",b.getNameInNamespace(), boc);
				if (personResponsable != null && personResponsable.size() > 0) {
					for (Person pr : personResponsable) {
						pr.setBoc(true);
						pr.setResponsableBO(true);
						pr.setAgentBO(false);
						pr.setAgent(false);
						pr.setResponsable(true);
						pr.setSecretary(false);
						
					}
					membresBoc.addAll(personResponsable);
				}

				// KHA ===> recupérer agent BO
				List<Person> personAgent = getLdapListMembers1("member",
						b.getNameInNamespace(), boc);
				if (personAgent != null && personAgent.size() > 0) {
					for (Person pa : personAgent) {
						pa.setAgent(false);
						pa.setBoc(true);
						pa.setAgentBO(true);
						pa.setResponsableBO(false);
						
						pa.setResponsable(false);
						pa.setSecretary(false);
					}
					membresBoc.addAll(personAgent);

				}
				boc.setMembersBOC(membresBoc);
				for (int i = 0; i < boc.getMembersBOC().size(); i++) {
					boc.getMembersBOC().get(i).setAssociatedBOC(boc);
					ldapListUser.add(boc.getMembersBOC().get(i));
				}

				if(getLdapEntryAttribute("associatedName",b.getNameInNamespace()).startsWith("cn="+centralBoc.getNameBOC())){
				listChildBOC.add(boc);
				
				}
			
			}
			centralBoc.setListChildBOCsBOC(listChildBOC);
		} catch (NamingException e) {
			System.out
					.println("getLdapBOC Erreur lors de l'acces au serveur LDAP:"
							+ e);
			e.printStackTrace();
		}
		return centralBoc;
	}

	public List<Object> getLdapData() {
		return ldapData;
	}

	public void setLdapData(List<Object> ldapData) {
		this.ldapData = ldapData;
	}

	public List<Person> getLdapListUser() {
		return ldapListUser;
	}

	public void setLdapListUser(List<Person> ldapListUser) {
		this.ldapListUser = ldapListUser;
	}

	public int getRowKeyIndex() {
		return rowKeyIndex;
	}

	public void setRowKeyIndex(int rowKeyIndex) {
		this.rowKeyIndex = rowKeyIndex;
	}

	public String getRowKey() {
		return rowKey;
	}

	public void setRowKey(String rowKey) {
		this.rowKey = rowKey;
	}

	public void setLdapListGroup(List<Group> ldapListGroup) {
		this.ldapListGroup = ldapListGroup;
	}

	public void setLdapListUnit(List<Unit> ldapListUnit) {
		this.ldapListUnit = ldapListUnit;
	}

	public void setDirContext(DirContext dirContext) {
		this.dirContext = dirContext;
	}

	public DirContext getDirContext() {
		return dirContext;
	}

	public void setVb(VariableGlobale vb) {
		this.vb = vb;
	}

	public VariableGlobale getVb() {
		return vb;
	}

	public List<BOC> getListTousBos() {
		return listTousBos;
	}

	public void setListTousBos(List<BOC> listTousBos) {
		this.listTousBos = listTousBos;
	}

	public List<Unit> getListTousUnit() {
		return listTousUnit;
	}

	public void setListTousUnit(List<Unit> listTousUnit) {
		this.listTousUnit = listTousUnit;
	}
	
}
