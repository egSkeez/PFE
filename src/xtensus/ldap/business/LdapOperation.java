package xtensus.ldap.business;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Binding;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import xtensus.beans.utils.RechercheUnitModel;
import xtensus.beans.utils.RechercheUserModel;
import xtensus.beans.utils.RoleModel;
import xtensus.dao.utils.LdapConnectionSingleton;
import xtensus.ldap.model.BOC;
import xtensus.ldap.model.Group;
import xtensus.ldap.model.Person;
import xtensus.ldap.model.Unit;

public class LdapOperation {

	private DirContext dirContext;
	public final String CONTEXT_USER = "o=XteUsers,dc=xtensus,dc=com";
	public final String CONTEXT_GROUP = "o=XteGroups,dc=xtensus,dc=com";
	public final String CONTEXT_ROLE = "o=XteDroits,dc=xtensus,dc=com";
	public final String CONTEXT_BOC = "ou=OrderOffices,o=XteUnits,dc=xtensus,dc=com";
	public final String CONTEXT_UNIT = "ou=SubUnits,o=XteUnits,dc=xtensus,dc=com";
	public final String CONTEXT_GROUPGAS = "o=XteGroupsGAS,dc=xtensus,dc=com";
	public final String CONTEXT_GROUPXGED = "o=XteGroupsXGED,dc=xtensus,dc=com";

	public LdapOperation() {
		dirContext = LdapConnectionSingleton.getInstance();
		//System.out.println("Début *** LdapOperation");
	}

	@SuppressWarnings("rawtypes")
	public String getCnById(String path, String attribut, int attributValue) {
		String result = "";
		//System.out.println("2019-06-12 Path : " + path);
		//System.out.println("2019-06-12 attribut : " + attribut);
		//System.out.println("2019-06-12 attributValue : " + attributValue);

		try {
			BasicAttributes attributes = new BasicAttributes(true);
			Attribute attribute = new BasicAttribute(attribut);
			attribute.add(attributValue);
			attributes.put(attribute);
			NamingEnumeration e = dirContext.search(path, attributes);
			//System.out.println("========<e> : " + e);

			//System.out.println("========<> : " + e.hasMore());
			while (e.hasMore()) {
				Binding b = (Binding) e.next();
				//System.out.println("2019-06-12 " + b.getNameInNamespace());
				result = dirContext.getAttributes(b.getNameInNamespace())
						.get("cn").get(0).toString();
				//System.out.println("2019-06-12 Result : " + result);
			}

		} catch (NamingException e) {
			e.printStackTrace();
		}

		return result;

	}

	@SuppressWarnings("rawtypes")
	public Person getUserByCN(String attributValue) {
		Person person = new Person();
		try {
			BasicAttributes attributes = new BasicAttributes(true);
			Attribute attribute = new BasicAttribute("cn");
			attribute.add(attributValue);
			attributes.put(attribute);
			String name;
			NamingEnumeration e = dirContext.search(CONTEXT_USER, attributes);
			while (e.hasMore()) {
				Binding b = (Binding) e.next();
				name = b.getNameInNamespace();
				person.setCn(dirContext.getAttributes(name).get("cn").get(0)
						.toString());
				person.setId(Integer.parseInt(dirContext.getAttributes(name)
						.get("uid").get(0).toString()));
				person.setNom(dirContext.getAttributes(name).get("sn").get(0)
						.toString());
				if(dirContext.getAttributes(name)
						.get("givenName")!=null && dirContext.getAttributes(name)
						.get("givenName").size()>0)
				person.setPrenom(dirContext.getAttributes(name)
						.get("givenName").get(0).toString());
				else 
					person.setPrenom(" ");
				person.setTelephoneNumber(dirContext.getAttributes(name)
						.get("telephoneNumber").get(0).toString());
				person.setFax(dirContext.getAttributes(name)
						.get("facsimileTelephoneNumber").get(0).toString());
				person.setEmail(dirContext.getAttributes(name).get("mail")
						.get(0).toString());
				if(dirContext.getAttributes(name).get("street")!=null && dirContext.getAttributes(name).get("street").size()>0)
				person.setAdresse(dirContext.getAttributes(name).get("street")
						.get(0).toString());
				person.setCodePostal(Integer.parseInt(dirContext
						.getAttributes(name).get("postalCode").get(0)
						.toString()));
				person.setLogin(dirContext.getAttributes(name)
						.get("displayName").get(0).toString());
				person.setPwd(dirContext.getAttributes(name)
						.get("userPassword").get(0).toString());
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return person;
	}

	public String getCnByNameInNamespace(String nameInNamespace) {
		String result = "";
		int firstIndex = nameInNamespace.indexOf("=");
		int secondIndex = nameInNamespace.indexOf(",");
		try {
			result = nameInNamespace.substring(firstIndex + 1, secondIndex);
		} catch (StringIndexOutOfBoundsException e) {

		}
		return result;
	}

	public String getOuByNameInNamespace(String nameInNamespace) {
		//System.out.println("*KHA********getOuByNameInNamespace********");
		String result = "";
		int firstIndex = nameInNamespace.indexOf(",");
		//System.out.println("firstIndex" + firstIndex);
		int secondIndex = firstIndex + 3;
		// System.out.println("secondIndex"+secondIndex);
		try {
			result = nameInNamespace.substring(firstIndex + 1, secondIndex);
			//System.out.println("result= " + result);
		} catch (StringIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public Unit getUnitByIdUnit(int attributValue) {
		Unit unit = new Unit();
		try {
			BasicAttributes attributes = new BasicAttributes(true);
			Attribute attribute = new BasicAttribute("departmentNumber");
			attribute.add(attributValue);
			attributes.put(attribute);
			NamingEnumeration e = dirContext.search(CONTEXT_UNIT, attributes);
			while (e.hasMore()) {
				Binding b = (Binding) e.next();
				unit.setNameUnit(dirContext
						.getAttributes(b.getNameInNamespace()).get("cn").get(0)
						.toString());
				if (dirContext.getAttributes(b.getNameInNamespace()).get(
						"description") != null
						&& dirContext.getAttributes(b.getNameInNamespace())
								.get("description").size() > 0) {
					unit.setDescriptionUnit(dirContext
							.getAttributes(b.getNameInNamespace())
							.get("description").get(0).toString());
				} else {
					unit.setDescriptionUnit("");
				}

				unit.setIdUnit(attributValue);
				String cnResponsable = "";

				Attribute cnResp = dirContext.getAttributes(
						b.getNameInNamespace()).get("manager");
				if (cnResp != null && cnResp.size() > 0)
					cnResponsable = cnResp.get(0).toString();
				String resultatCN = getLdapEntryAttribute("cn", cnResponsable);
				Person p = new Person();
				p = getUserByCN(resultatCN);
				unit.setResponsibleUnit(p);
			}

		} catch (NamingException e) {
			e.printStackTrace();
		}

		return unit;

	}

	@SuppressWarnings("rawtypes")
	public Unit getUnitByCN(String attributValue) {
		Unit unit = new Unit();
		try {
			BasicAttributes attributes = new BasicAttributes(true);
			Attribute attribute = new BasicAttribute("cn");
			attribute.add(attributValue);
			attributes.put(attribute);
			NamingEnumeration e = dirContext.search(CONTEXT_UNIT, attributes);
			while (e.hasMore()) {
				Binding b = (Binding) e.next();
				unit.setNameUnit(dirContext
						.getAttributes(b.getNameInNamespace()).get("cn").get(0)
						.toString());

				// unit.setDescriptionUnit(dirContext
				// .getAttributes(b.getNameInNamespace())
				// .get("description").get(0).toString());
				// unit.setIdUnit(attributValue);

				unit.setIdUnit(Integer.parseInt(dirContext
						.getAttributes(b.getNameInNamespace())
						.get("departmentNumber").get(0).toString()));
				// unit.setIdUnit(attributValue);
				// String cnResponsable = "";
				//
				// Attribute cnResp = dirContext.getAttributes(
				// b.getNameInNamespace()).get("manager");
				// if (cnResp != null && cnResp.size() > 0)
				// cnResponsable = cnResp.get(0).toString();
				// String resultatCN = getLdapEntryAttribute("cn",
				// cnResponsable);
				// Person p = new Person();
				// p = getUserByCN(resultatCN);
				// unit.setResponsibleUnit(p);
			}

		} catch (NamingException e) {
			e.printStackTrace();
		}

		return unit;

	}

	@SuppressWarnings("rawtypes")
	public Unit getUnitByCn(String attributValue) {
		Unit unit = new Unit();
		try {
			BasicAttributes attributes = new BasicAttributes(true);
			Attribute attribute = new BasicAttribute("cn");
			attribute.add(attributValue);
			attributes.put(attribute);
			NamingEnumeration e = dirContext.search(CONTEXT_UNIT, attributes);
			while (e.hasMore()) {
				Binding b = (Binding) e.next();
				unit.setIdUnit(Integer.parseInt(dirContext
						.getAttributes(b.getNameInNamespace())
						.get("departmentNumber").get(0).toString()));
				unit.setDescriptionUnit(dirContext
						.getAttributes(b.getNameInNamespace())
						.get("description").get(0).toString());
				unit.setNameUnit(attributValue);

				String cnResponsable = "";
				cnResponsable = dirContext
						.getAttributes(b.getNameInNamespace()).get("manager")
						.get(0).toString();
				String resultatCN = getLdapEntryAttribute("cn", cnResponsable);
				Person p = new Person();
				p = getUserByCN(resultatCN);
				unit.setResponsibleUnit(p);
			}

		} catch (NamingException e) {
			e.printStackTrace();
		}

		return unit;

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

	@SuppressWarnings("rawtypes")
	public Person getUserById(int attributValue) {
		Person person = new Person();
		try {
			BasicAttributes attributes = new BasicAttributes(true);
			Attribute attribute = new BasicAttribute("uid");
			attribute.add(attributValue);
			attributes.put(attribute);
			String name;
			NamingEnumeration e = dirContext.search(CONTEXT_USER, attributes);
			while (e.hasMore()) {
				Binding b = (Binding) e.next();
				name = b.getNameInNamespace();
				person.setCn(getLdapEntryAttribute("cn", name));
				person.setId(Integer
						.parseInt(getLdapEntryAttribute("uid", name)));
				person.setNom(getLdapEntryAttribute("sn", name));
				person.setPrenom(getLdapEntryAttribute("givenName", name));
				person.setShortName(getLdapEntryAttribute("employeeNumber",
						name));
				person.setTelephoneNumber(getLdapEntryAttribute(
						"telephoneNumber", name));
				person.setFax(getLdapEntryAttribute("facsimileTelephoneNumber",
						name));
				person.setEmail(getLdapEntryAttribute("mail", name));
				person.setAdresse(getLdapEntryAttribute("street", name));
				person.setCodePostal(Integer.parseInt(getLdapEntryAttribute(
						"postalCode", name)));
				person.setLogin(getLdapEntryAttribute("displayName", name));
				person.setPwd(getLdapEntryAttribute("userPassword", name));
			}

		} catch (NamingException e) {
			e.printStackTrace();
		}

		return person;

	}

	@SuppressWarnings("rawtypes")
	public Person getUserByName(String attributValue) {
		Person person = new Person();
		try {
			BasicAttributes attributes = new BasicAttributes(true);
			Attribute attribute = new BasicAttribute("cn");
			attribute.add(attributValue);
			attributes.put(attribute);
			String name;
			NamingEnumeration e = dirContext.search(CONTEXT_USER, attributes);
			while (e.hasMore()) {
				Binding b = (Binding) e.next();
				name = b.getNameInNamespace();
				person.setCn(getLdapEntryAttribute("cn", name));
				person.setId(Integer
						.parseInt(getLdapEntryAttribute("uid", name)));
				person.setNom(getLdapEntryAttribute("sn", name));
				person.setPrenom(getLdapEntryAttribute("givenName", name));
				person.setShortName(getLdapEntryAttribute("employeeNumber",
						name));
				person.setTelephoneNumber(getLdapEntryAttribute(
						"telephoneNumber", name));
				person.setFax(getLdapEntryAttribute("facsimileTelephoneNumber",
						name));
				person.setEmail(getLdapEntryAttribute("mail", name));
				person.setAdresse(getLdapEntryAttribute("street", name));
				person.setCodePostal(Integer.parseInt(getLdapEntryAttribute(
						"postalCode", name)));
				person.setLogin(getLdapEntryAttribute("displayName", name));
				person.setPwd(getLdapEntryAttribute("userPassword", name));
			}

		} catch (NamingException e) {
			e.printStackTrace();
		}

		return person;

	}

	/**
	 * Méthode de recherche d'un Person par mail
	 * 
	 * @param attributValue
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Person> getUserByMail(String attributValue) {
		Person person = new Person();
		List<Person> listPerson = new ArrayList<Person>();
		try {
			BasicAttributes attributes = new BasicAttributes(true);
			Attribute attribute = new BasicAttribute("mail");
			attribute.add(attributValue);
			attributes.put(attribute);
			String name;
			NamingEnumeration e = dirContext.search(CONTEXT_USER, attributes);
			while (e.hasMore()) {
				Binding b = (Binding) e.next();
				name = b.getNameInNamespace();
				person.setCn(getLdapEntryAttribute("cn", name));
				person.setId(Integer
						.parseInt(getLdapEntryAttribute("uid", name)));
				person.setNom(getLdapEntryAttribute("sn", name));
				person.setPrenom(getLdapEntryAttribute("givenName", name));
				person.setShortName(getLdapEntryAttribute("employeeNumber",
						name));
				person.setTelephoneNumber(getLdapEntryAttribute(
						"telephoneNumber", name));
				person.setFax(getLdapEntryAttribute("facsimileTelephoneNumber",
						name));
				person.setEmail(getLdapEntryAttribute("mail", name));
				person.setAdresse(getLdapEntryAttribute("street", name));
				person.setCodePostal(Integer.parseInt(getLdapEntryAttribute(
						"postalCode", name)));
				person.setLogin(getLdapEntryAttribute("displayName", name));
				person.setPwd(getLdapEntryAttribute("userPassword", name));
				listPerson.add(person);
			}

		} catch (NamingException e) {
			e.printStackTrace();
		}

		return listPerson;

	}

	@SuppressWarnings("rawtypes")
	public Unit getUnitById(int attributValue) {
		//System.out.println("AH : DANS getUnitById ======================>");
		Unit unit = new Unit();
		try {
			BasicAttributes attributes = new BasicAttributes(true);
			String nameInSpace = "";
			Attribute attribute = new BasicAttribute("departmentNumber");
			attribute.add(attributValue);
			attributes.put(attribute);
			NamingEnumeration e = dirContext.search(CONTEXT_UNIT, attributes);
			while (e.hasMore()) {
				Binding b = (Binding) e.next();
				unit.setNameUnit(getLdapEntryAttribute("cn",
						b.getNameInNamespace()));
				unit.setDescriptionUnit(getLdapEntryAttribute("description",
						b.getNameInNamespace()));
				unit.setShortNameUnit(getLdapEntryAttribute("l",
						b.getNameInNamespace()));
				unit.setIdUnit(attributValue);

				nameInSpace = getLdapEntryAttribute("associatedName",
						b.getNameInNamespace());
				//System.out.println("AssociatedUnit  =" + nameInSpace);
				// Extraire le nom de la direction

				String result = "";
				int firstIndex = nameInSpace.indexOf(",");

				result = nameInSpace.substring(3, firstIndex);
				//System.out.println("result=" + result);
				Unit uniteAssocie = getUnitByName(result);
				//System.out.println("uniteAssocie  =  " + uniteAssocie);
				if (uniteAssocie.getIdUnit() != null) {
					unit.setAssociatedUnit(uniteAssocie);
				} else {
					//System.out.println("Ah :: Unité n'a pas d'Unité assosiée");
					BOC boc = getBocByCn(result);
					if (boc != null && boc.getIdBOC() != 0)
						unit.setAssociatedBOC(boc);
				}

				Person responsable = new Person();
				nameInSpace = getLdapEntryAttribute("manager",
						b.getNameInNamespace());
				responsable = getUserByName(getCnByNameInNamespace(nameInSpace));
				responsable.setResponsable(true);
				responsable.setTitle("1.Responsable");
				responsable.setAssociatedDirection(unit);

				unit.setResponsibleUnit(responsable);
				Person secretary = new Person();
				nameInSpace = getLdapEntryAttribute("secretary",
						b.getNameInNamespace());
				secretary = getUserByName(getCnByNameInNamespace(nameInSpace));
				secretary.setSecretary(true);
				secretary.setTitle("2.Secrétaire");
				secretary.setAssociatedDirection(unit);
				unit.setSecretaryUnit(secretary);
				List<Person> listMembers = new ArrayList<Person>();
				listMembers.add(responsable);
				listMembers.add(secretary);
				listMembers.addAll(getLdapListMembers("member",
						b.getNameInNamespace()));
				unit.setMembersUnit(listMembers);
			}

		} catch (NamingException e) {
			e.printStackTrace();
		}

		return unit;

	}

	
	
	
	//@SuppressWarnings("rawtypes")
//	public Unit getUnitById(String attributValue) {
//		System.out.println("AH : DANS getUnitById String ======================>");
//		Unit unit = new Unit();
//		try {
//			BasicAttributes attributes = new BasicAttributes(true);
//			String nameInSpace = "";
//			Attribute attribute = new BasicAttribute("departmentNumber");
//			attribute.add(attributValue);
//			attributes.put(attribute);
//			NamingEnumeration e = dirContext.search(CONTEXT_UNIT, attributes);
//			while (e.hasMore()) {
//				Binding b = (Binding) e.next();
//				unit.setNameUnit(getLdapEntryAttribute("cn",
//						b.getNameInNamespace()));
//				unit.setDescriptionUnit(getLdapEntryAttribute("description",
//						b.getNameInNamespace()));
//				unit.setShortNameUnit(getLdapEntryAttribute("l",
//						b.getNameInNamespace()));
//				
//				unit.setIdUnit(Integer.parseInt(attributValue));
//
//				nameInSpace = getLdapEntryAttribute("associatedName",
//						b.getNameInNamespace());
//				System.out.println("AssociatedUnit  =" + nameInSpace);
//				// Extraire le nom de la direction
//
//				String result = "";
//				int firstIndex = nameInSpace.indexOf(",");
//
//				result = nameInSpace.substring(3, firstIndex);
//				System.out.println("result=" + result);
//				Unit uniteAssocie = getUnitByName(result);
//				System.out.println("uniteAssocie  =  " + uniteAssocie);
//				if (uniteAssocie.getIdUnit() != null) {
//					unit.setAssociatedUnit(uniteAssocie);
//				} else {
//					System.out.println("Ah :: Unité n'a pas d'Unité assosiée");
//					BOC boc = getBocByCn(result);
//					if (boc != null && boc.getIdBOC() != 0)
//						unit.setAssociatedBOC(boc);
//				}
//
//				Person responsable = new Person();
//				nameInSpace = getLdapEntryAttribute("manager",
//						b.getNameInNamespace());
//				responsable = getUserByName(getCnByNameInNamespace(nameInSpace));
//				responsable.setResponsable(true);
//				responsable.setTitle("1.Responsable");
//				responsable.setAssociatedDirection(unit);
//
//				unit.setResponsibleUnit(responsable);
//				Person secretary = new Person();
//				nameInSpace = getLdapEntryAttribute("secretary",
//						b.getNameInNamespace());
//				secretary = getUserByName(getCnByNameInNamespace(nameInSpace));
//				secretary.setSecretary(true);
//				secretary.setTitle("2.Secrétaire");
//				secretary.setAssociatedDirection(unit);
//				unit.setSecretaryUnit(secretary);
//				List<Person> listMembers = new ArrayList<Person>();
//				listMembers.add(responsable);
//				listMembers.add(secretary);
//				listMembers.addAll(getLdapListMembers("member",
//						b.getNameInNamespace()));
//				unit.setMembersUnit(listMembers);
//			}
//
//		} catch (NamingException e) {
//			e.printStackTrace();
//		}
//
//		return unit;
//
//	}
//	
	
	
	@SuppressWarnings("rawtypes")
	public Unit getUnitByName(String attributValue) {
		Unit unit = new Unit();
		try {
			BasicAttributes attributes = new BasicAttributes(true);
			String nameInSpace = "";
			Attribute attribute = new BasicAttribute("cn");
			attribute.add(attributValue);
			attributes.put(attribute);
			NamingEnumeration e = dirContext.search(CONTEXT_UNIT, attributes);
			while (e.hasMore()) {
				Binding b = (Binding) e.next();
				//System.out.println(b.getNameInNamespace());
				unit.setNameUnit(getLdapEntryAttribute("cn",
						b.getNameInNamespace()));
				unit.setDescriptionUnit(getLdapEntryAttribute("description",
						b.getNameInNamespace()));
				unit.setShortNameUnit(getLdapEntryAttribute("l",
						b.getNameInNamespace()));
				unit.setIdUnit(Integer.parseInt(getLdapEntryAttribute(
						"departmentNumber", b.getNameInNamespace())));
				Person responsable = new Person();
				Attribute nameInSpaces = dirContext.getAttributes(
						b.getNameInNamespace()).get("manager");
				if (nameInSpaces != null && nameInSpaces.size() > 0) {
					nameInSpace = nameInSpaces.get(0).toString();

					responsable = getUserByName(getCnByNameInNamespace(nameInSpace));
					responsable.setResponsable(true);
					responsable.setAssociatedDirection(unit);
					unit.setResponsibleUnit(responsable);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return unit;

	}

	@SuppressWarnings("rawtypes")
	public Unit getBocById(int attributValue) {
		Unit unit = new Unit();
		try {
			BasicAttributes attributes = new BasicAttributes(true);
			Attribute attribute = new BasicAttribute("departmentNumber");
			attribute.add(attributValue);
			attributes.put(attribute);
			NamingEnumeration e = dirContext.search(CONTEXT_BOC, attributes);
			while (e.hasMore()) {
				Binding b = (Binding) e.next();
				unit.setNameUnit(getLdapEntryAttribute("cn",
						b.getNameInNamespace()));
				unit.setDescriptionUnit(getLdapEntryAttribute("description",
						b.getNameInNamespace()));
				unit.setShortNameUnit(getLdapEntryAttribute("l",
						b.getNameInNamespace()));
				unit.setIdUnit(attributValue);
				List<Person> listMembers = new ArrayList<Person>();
				listMembers.addAll(getLdapListMembers("member",
						b.getNameInNamespace()));
				unit.setMembersUnit(listMembers);
			}

		} catch (NamingException e) {
			e.printStackTrace();
		}

		return unit;

	}

	@SuppressWarnings("rawtypes")
	public Unit getUnitByShortName(String attributValue) {
		Unit unit = new Unit();
		try {
			BasicAttributes attributes = new BasicAttributes(true);
			String nameInSpace = "";
			Attribute attribute = new BasicAttribute("l");
			attribute.add(attributValue);
			attributes.put(attribute);
			NamingEnumeration e;
			if (attributValue.equals("BOC")) {
				e = dirContext.search(CONTEXT_BOC, attributes);
				while (e.hasMore()) {
					Binding b = (Binding) e.next();
					//System.out.println(b.getNameInNamespace());
					unit.setNameUnit(getLdapEntryAttribute("cn",
							b.getNameInNamespace()));
					unit.setDescriptionUnit(getLdapEntryAttribute(
							"description", b.getNameInNamespace()));
					unit.setShortNameUnit(getLdapEntryAttribute("l",
							b.getNameInNamespace()));
					unit.setIdUnit(Integer.parseInt(getLdapEntryAttribute(
							"departmentNumber", b.getNameInNamespace())));
				}
			} else {
				e = dirContext.search(CONTEXT_UNIT, attributes);
				while (e.hasMore()) {
					Binding b = (Binding) e.next();
					//System.out.println(b.getNameInNamespace());
					unit.setNameUnit(getLdapEntryAttribute("cn",
							b.getNameInNamespace()));
					unit.setDescriptionUnit(getLdapEntryAttribute(
							"description", b.getNameInNamespace()));
					unit.setShortNameUnit(getLdapEntryAttribute("l",
							b.getNameInNamespace()));
					unit.setIdUnit(Integer.parseInt(getLdapEntryAttribute(
							"departmentNumber", b.getNameInNamespace())));
					Person responsable = new Person();
					try {
						nameInSpace = dirContext
								.getAttributes(b.getNameInNamespace())
								.get("manager").get(0).toString();
						responsable = getUserByName(getCnByNameInNamespace(nameInSpace));
						responsable.setResponsable(true);
						responsable.setAssociatedDirection(unit);
						unit.setResponsibleUnit(responsable);
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}

		} catch (NamingException e) {
			e.printStackTrace();
		}

		return unit;

	}

	// Ah : 2019-06-26

	@SuppressWarnings("rawtypes")
	public BOC getBocByCn(String attributValue) {
	//	System.out.println("[attributValue] " + attributValue);
		BOC boc = new BOC();
		try {

			BasicAttributes attributes = new BasicAttributes(true);

			Attribute attribute = new BasicAttribute("cn");
			attribute.add(attributValue);
			attributes.put(attribute);
			NamingEnumeration e;
			String chaine = "cn=Bureau d'Ordre Central,";
			if (!attributValue.equals("Bureau d'Ordre Central"))
				e = dirContext.search(chaine + CONTEXT_BOC, attributes);
			else
				e = dirContext.search(CONTEXT_BOC, attributes);
			while (e.hasMore()) {
				Binding b = (Binding) e.next();
				boc.setNameBOC(getLdapEntryAttribute("cn",
						b.getNameInNamespace()));
				boc.setIdBOC(Integer.parseInt(getLdapEntryAttribute(
						"departmentNumber", b.getNameInNamespace())));
			}

		} catch (NamingException e) {
			e.printStackTrace();
		}
		//System.out.println("[BOC] :" + boc);

		return boc;

	}
	
	@SuppressWarnings("rawtypes")
	public BOC getBocByAbreviation(String attributValue) {
	//	System.out.println("[attributValue] " + attributValue);
		BOC boc = new BOC();
		try {

			BasicAttributes attributes = new BasicAttributes(true);

			Attribute attribute = new BasicAttribute("l");
			attribute.add(attributValue);
			attributes.put(attribute);
			NamingEnumeration e;
			String chaine = "cn=Bureau d'Ordre Central,";
			if (!attributValue.equals("Bureau d'Ordre Central"))
				e = dirContext.search(chaine + CONTEXT_BOC, attributes);
			else
				e = dirContext.search(CONTEXT_BOC, attributes);
			while (e.hasMore()) {
				Binding b = (Binding) e.next();
				boc.setNameBOC(getLdapEntryAttribute("cn",
						b.getNameInNamespace()));
				boc.setIdBOC(Integer.parseInt(getLdapEntryAttribute(
						"departmentNumber", b.getNameInNamespace())));
			}

		} catch (NamingException e) {
			e.printStackTrace();
		}
		//System.out.println("[BOC] :" + boc);

		return boc;

	}


	@SuppressWarnings("rawtypes")
	public BOC getBocByID(int attributValue) {
		//System.out.println("[attributValue] " + attributValue);
		BOC boc = new BOC();
		try {

			BasicAttributes attributes = new BasicAttributes(true);

			Attribute attribute = new BasicAttribute("departmentNumber");
			attribute.add(attributValue);
			attributes.put(attribute);
			NamingEnumeration e;
			// [2019-06-27]
			String chaine = "cn=Bureau d'Ordre Central,";
			if (attributValue != 1)
				e = dirContext.search(chaine + CONTEXT_BOC, attributes);
			else
				e = dirContext.search(CONTEXT_BOC, attributes);
			LdapFunction lf = new LdapFunction();
			while (e.hasMore()) {
				Binding b = (Binding) e.next();
				//System.out.println("=>" + b.getNameInNamespace());
				boc.setNameBOC(getLdapEntryAttribute("cn",
						b.getNameInNamespace()));
				boc.setShortNameBOC(getLdapEntryAttribute("l",
						b.getNameInNamespace()));
				boc.setIdBOC(Integer.parseInt(getLdapEntryAttribute(
						"departmentNumber", b.getNameInNamespace())));
				
			
				List<Person> pr = lf.getLdapListMembers1("member",
						b.getNameInNamespace(), boc);
				List<Person> ps = lf.getLdapListMembers1("manager",
						b.getNameInNamespace(), boc);
				if (boc.getMembersBOC() != null) {
					if (pr != null)
						boc.getMembersBOC().addAll(pr);
					if (ps != null)
						boc.getMembersBOC().addAll(ps);
				} else {
					boc.setMembersBOC(new ArrayList<Person>());
					if (pr != null)
						boc.getMembersBOC().addAll(pr);
					if (ps != null)
						boc.getMembersBOC().addAll(ps);

				}
				// Person responsable = new Person();
				// try {
				// nameInSpace = dirContext
				// .getAttributes(b.getNameInNamespace())
				// .get("manager").get(0).toString();
				// responsable =
				// getUserByName(getCnByNameInNamespace(nameInSpace));
				// responsable.setResponsable(true);
				// responsable.setAssociatedDirection(unit);
				// boc.set(responsable);
				// } catch (Exception e2) {
				// }

			}

		} catch (NamingException e) {
			e.printStackTrace();
		}
		//System.out.println("[BOC] :" + boc);

		return boc;

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
						person.setAgent(true);
						person.setTitle("3.Agent");
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

	public void close() {
		try {
			dirContext.close();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	public List<RechercheUnitModel> getListUnitByCriteria(RechercheUnitModel rum) {
		RechercheUnitModel unit;
		List<RechercheUnitModel> result = new ArrayList<RechercheUnitModel>();
		List<RechercheUnitModel> result1;
		try {
			String name;
			if (!rum.getIdUnit().equals("") || !rum.getNameUnit().equals("")
					|| !rum.getShortnameUnit().equals("")) {
				//System.out.println("Champs non vides");
				NamingEnumeration e = dirContext.listBindings(CONTEXT_UNIT);
				while (e.hasMore()) {
					Binding b = (Binding) e.next();
					name = b.getNameInNamespace();
					unit = new RechercheUnitModel();
					unit.setIdUnit(getLdapEntryAttribute("departmentNumber",
							name));
					unit.setNameUnit(getLdapEntryAttribute("cn", name));
					unit.setShortnameUnit(getLdapEntryAttribute("l", name));
					unit.setDescriptionUnit(getLdapEntryAttribute(
							"description", name));
					try {
						unit.setNameResponsable(getCnByNameInNamespace(dirContext
								.getAttributes(name).get("manager").get(0)
								.toString()));
					} catch (NullPointerException e2) {
					}
					try {
						unit.setNameSecretary(getCnByNameInNamespace(dirContext
								.getAttributes(name).get("secretary").get(0)
								.toString()));
					} catch (NullPointerException e2) {
					}
					result.add(unit);
				}
				if (!rum.getIdUnit().equals("")) {
					result1 = new ArrayList<RechercheUnitModel>();
					for (RechercheUnitModel rechercheUserModel : result) {
						if (rechercheUserModel.getIdUnit().equals(
								rum.getIdUnit())) {
							result1.add(rechercheUserModel);
						}
					}
					result = new ArrayList<RechercheUnitModel>();
					for (RechercheUnitModel rechercheUserModel : result1) {
						result.add(rechercheUserModel);
					}
				}
				if (!rum.getNameUnit().equals("")) {
					result1 = new ArrayList<RechercheUnitModel>();
					for (RechercheUnitModel rechercheUserModel : result) {
						if (rechercheUserModel.getNameUnit().toLowerCase()
								.contains(rum.getNameUnit().toLowerCase())) {
							result1.add(rechercheUserModel);
						}
					}
					result = new ArrayList<RechercheUnitModel>();
					for (RechercheUnitModel rechercheUserModel : result1) {
						result.add(rechercheUserModel);
					}
				}
				if (!rum.getShortnameUnit().equals("")) {
					result1 = new ArrayList<RechercheUnitModel>();
					for (RechercheUnitModel rechercheUserModel : result) {
						if (rechercheUserModel.getShortnameUnit().toLowerCase()
								.contains(rum.getShortnameUnit().toLowerCase())) {
							result1.add(rechercheUserModel);
						}
					}
					result = new ArrayList<RechercheUnitModel>();
					for (RechercheUnitModel rechercheUserModel : result1) {
						result.add(rechercheUserModel);
					}
				}
			}

		} catch (NamingException e) {
			e.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public List<RechercheUserModel> getListUserByCriteria(RechercheUserModel rum) {
		RechercheUserModel user;
		List<RechercheUserModel> result = new ArrayList<RechercheUserModel>();
		List<RechercheUserModel> result1;
		try {
			String name;
			if (!rum.getIdUser().equals("") || !rum.getNameUser().equals("")
					|| !rum.getSurnameUser().equals("")
					|| !rum.getShortnameUser().equals("")
					|| !rum.getAdressUser().equals("")
					|| !rum.getMailUser().equals("")
					|| !rum.getTelephoneNumberUser().equals("")
					|| !rum.getFacsimileTelephoneNumberUser().equals("")
					|| !rum.getCodePostalUser().equals("")) {
				//System.out.println("Champs non vides");
				NamingEnumeration e = dirContext.listBindings(CONTEXT_USER);
				while (e.hasMore()) {
					Binding b = (Binding) e.next();
					name = b.getNameInNamespace();
					user = new RechercheUserModel();
					user.setIdUser(getLdapEntryAttribute("uid", name));
					user.setNameUser(getLdapEntryAttribute("sn", name));
					user.setSurnameUser(getLdapEntryAttribute("givenName", name));
					user.setShortnameUser(getLdapEntryAttribute(
							"employeeNumber", name));
					user.setTelephoneNumberUser(getLdapEntryAttribute(
							"telephoneNumber", name));
					user.setMailUser(getLdapEntryAttribute("mail", name));
					user.setFacsimileTelephoneNumberUser(getLdapEntryAttribute(
							"facsimileTelephoneNumber", name));
					user.setAdressUser(getLdapEntryAttribute("street", name));
					user.setCodePostalUser(getLdapEntryAttribute("postalCode",
							name));
					result.add(user);
				}
				if (!rum.getIdUser().equals("")) {
					result1 = new ArrayList<RechercheUserModel>();
					for (RechercheUserModel rechercheUserModel : result) {
						if (rechercheUserModel.getIdUser().equals(
								rum.getIdUser())) {
							result1.add(rechercheUserModel);
						}
					}
					result = new ArrayList<RechercheUserModel>();
					for (RechercheUserModel rechercheUserModel : result1) {
						result.add(rechercheUserModel);
					}
				}
				if (!rum.getNameUser().equals("")) {
					result1 = new ArrayList<RechercheUserModel>();
					for (RechercheUserModel rechercheUserModel : result) {
						if (rechercheUserModel.getNameUser().toLowerCase()
								.contains(rum.getNameUser().toLowerCase())) {
							result1.add(rechercheUserModel);
						}
					}
					result = new ArrayList<RechercheUserModel>();
					for (RechercheUserModel rechercheUserModel : result1) {
						result.add(rechercheUserModel);
					}
				}
				if (!rum.getSurnameUser().equals("")) {
					result1 = new ArrayList<RechercheUserModel>();
					for (RechercheUserModel rechercheUserModel : result) {
						if (rechercheUserModel.getSurnameUser().toLowerCase()
								.contains(rum.getSurnameUser().toLowerCase())) {
							result1.add(rechercheUserModel);
						}
					}
					result = new ArrayList<RechercheUserModel>();
					for (RechercheUserModel rechercheUserModel : result1) {
						result.add(rechercheUserModel);
					}
				}
				if (!rum.getShortnameUser().equals("")) {
					result1 = new ArrayList<RechercheUserModel>();
					for (RechercheUserModel rechercheUserModel : result) {
						if (rechercheUserModel.getShortnameUser().toLowerCase()
								.contains(rum.getShortnameUser().toLowerCase())) {
							result1.add(rechercheUserModel);
						}
					}
					result = new ArrayList<RechercheUserModel>();
					for (RechercheUserModel rechercheUserModel : result1) {
						result.add(rechercheUserModel);
					}
				}
				if (!rum.getAdressUser().equals("")) {
					result1 = new ArrayList<RechercheUserModel>();
					for (RechercheUserModel rechercheUserModel : result) {
						if (rechercheUserModel.getAdressUser().toLowerCase()
								.contains(rum.getAdressUser().toLowerCase())) {
							result1.add(rechercheUserModel);
						}
					}
					result = new ArrayList<RechercheUserModel>();
					for (RechercheUserModel rechercheUserModel : result1) {
						result.add(rechercheUserModel);
					}
				}
				if (!rum.getMailUser().equals("")) {
					result1 = new ArrayList<RechercheUserModel>();
					for (RechercheUserModel rechercheUserModel : result) {
						if (rechercheUserModel.getMailUser().equals(
								rum.getMailUser())) {
							result1.add(rechercheUserModel);
						}
					}
					result = new ArrayList<RechercheUserModel>();
					for (RechercheUserModel rechercheUserModel : result1) {
						result.add(rechercheUserModel);
					}
				}
				if (!rum.getTelephoneNumberUser().equals("")) {
					result1 = new ArrayList<RechercheUserModel>();
					for (RechercheUserModel rechercheUserModel : result) {
						if (rechercheUserModel.getTelephoneNumberUser().equals(
								rum.getTelephoneNumberUser())) {
							result1.add(rechercheUserModel);
						}
					}
					result = new ArrayList<RechercheUserModel>();
					for (RechercheUserModel rechercheUserModel : result1) {
						result.add(rechercheUserModel);
					}
				}
				if (!rum.getFacsimileTelephoneNumberUser().equals("")) {
					result1 = new ArrayList<RechercheUserModel>();
					for (RechercheUserModel rechercheUserModel : result) {
						if (rechercheUserModel
								.getFacsimileTelephoneNumberUser().equals(
										rum.getFacsimileTelephoneNumberUser())) {
							result1.add(rechercheUserModel);
						}
					}
					result = new ArrayList<RechercheUserModel>();
					for (RechercheUserModel rechercheUserModel : result1) {
						result.add(rechercheUserModel);
					}
				}
				if (!rum.getCodePostalUser().equals("")) {
					result1 = new ArrayList<RechercheUserModel>();
					for (RechercheUserModel rechercheUserModel : result) {
						if (rechercheUserModel.getCodePostalUser().equals(
								rum.getCodePostalUser())) {
							result1.add(rechercheUserModel);
						}
					}
					result = new ArrayList<RechercheUserModel>();
					for (RechercheUserModel rechercheUserModel : result1) {
						result.add(rechercheUserModel);
					}
				}
			}

		} catch (NamingException e) {
			e.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public int getListUser(List<Person> result) {
		int reference = 0;
		Person person;
		try {
			String name;
			NamingEnumeration e = dirContext.listBindings(CONTEXT_USER);
			while (e.hasMore()) {
				Binding b = (Binding) e.next();
				name = b.getNameInNamespace();
				person = new Person();
				person.setCn(getLdapEntryAttribute("cn", name));
				person.setId(Integer
						.parseInt(getLdapEntryAttribute("uid", name)));
				person.setNom(getLdapEntryAttribute("sn", name));
				person.setPrenom(getLdapEntryAttribute("givenName", name));
				person.setShortName(getLdapEntryAttribute("employeeNumber",
						name));
				person.setTelephoneNumber(getLdapEntryAttribute(
						"telephoneNumber", name));
				person.setFax(getLdapEntryAttribute("facsimileTelephoneNumber",
						name));
				person.setEmail(getLdapEntryAttribute("mail", name));
				person.setAdresse(getLdapEntryAttribute("street", name));
				person.setCodePostal(Integer.parseInt(getLdapEntryAttribute(
						"postalCode", name)));
				person.setLogin(getLdapEntryAttribute("displayName", name));
				person.setPwd(getLdapEntryAttribute("userPassword", name));
				person.setNomUserAr(getLdapEntryAttribute("carLicense", name));
				person.setPrenomUserAr(getLdapEntryAttribute(
						"businessCategory", name));

				if (person.getId() != 0) {
					if (reference < person.getId()) {
						reference = person.getId();
					}
					result.add(person);
				}
			}

		} catch (NamingException e) {
			e.printStackTrace();
		}

		return reference;
	}

	public List<Person> getGroupMembers(String attribute, String path) {
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

	@SuppressWarnings("rawtypes")
	public Person getPersonalisedUserById(int attributValue) {
		Person person = new Person();
		try {
			Unit unit = new Unit();
			Unit associatedUnit = new Unit();
			BasicAttributes attributes = new BasicAttributes(true);
			Attribute attribute = new BasicAttribute("uid");
			attribute.add(attributValue);
			attributes.put(attribute);
			String nameInSpaceAssociatedUnit = "";
			String nameInSpaceUnitAssociatedUnit = "";
			String name;
			NamingEnumeration ne;
			Binding be;
			// kha
			String typeUnite = "";
			NamingEnumeration e = dirContext.search(CONTEXT_USER, attributes);
			while (e.hasMore()) {
				Binding b = (Binding) e.next();
				name = b.getNameInNamespace();
				person.setCn(getLdapEntryAttribute("cn", name));
				person.setId(Integer
						.parseInt(getLdapEntryAttribute("uid", name)));
				person.setNom(getLdapEntryAttribute("sn", name));
				person.setPrenom(getLdapEntryAttribute("givenName", name));

				person.setNomUserAr(getLdapEntryAttribute("carLicense", name));
				person.setPrenomUserAr(getLdapEntryAttribute(
						"businessCategory", name));

				person.setShortName(getLdapEntryAttribute("employeeNumber",
						name));
				person.setTelephoneNumber(getLdapEntryAttribute(
						"telephoneNumber", name));
				person.setFax(getLdapEntryAttribute("facsimileTelephoneNumber",
						name));
				person.setEmail(getLdapEntryAttribute("mail", name));
				person.setAdresse(getLdapEntryAttribute("street", name));
				person.setCodePostal(Integer.parseInt(getLdapEntryAttribute(
						"postalCode", name)));
				person.setLogin(getLdapEntryAttribute("displayName", name));
				person.setPwd(getLdapEntryAttribute("userPassword", name));

				// IsResponsable ??
				BasicAttributes attributesResponsable = new BasicAttributes(
						true);
				Attribute attributeResponsable = new BasicAttribute("manager");
				attributeResponsable.add(name);
				attributesResponsable.put(attributeResponsable);
				// IsSecretary ??
				BasicAttributes attributesSecretary = new BasicAttributes(true);
				Attribute attributeSecretary = new BasicAttribute("secretary");
				attributeSecretary.add(name);
				attributesSecretary.put(attributeSecretary);
				// IsAgent ??
				BasicAttributes attributesAgent = new BasicAttributes(true);
				Attribute attributeAgent = new BasicAttribute("member");
				attributeAgent.add(name);
				attributesAgent.put(attributeAgent);

				ne = dirContext.search(CONTEXT_UNIT, attributesResponsable);
				if (ne.hasMore()) {
					be = (Binding) ne.next();
					nameInSpaceAssociatedUnit = be.getNameInNamespace();
					nameInSpaceUnitAssociatedUnit = getLdapEntryAttribute(
							"associatedName", nameInSpaceAssociatedUnit);
//					System.out.println("nameInSpaceUnitAssociatedUnit="
//							+ nameInSpaceUnitAssociatedUnit);
					typeUnite = getOuByNameInNamespace(nameInSpaceUnitAssociatedUnit);
					// typeUnite =
					// dirContext.getAttributes(nameInSpaceUnitAssociatedUnit).get("ou").get(0).toString()
					// ;
					//System.out.println("typeUnite= " + typeUnite);

					//

					person.setResponsable(true);
				} else {
					ne = dirContext.search(CONTEXT_UNIT, attributesSecretary);
					if (ne.hasMore()) {
						be = (Binding) ne.next();
						nameInSpaceAssociatedUnit = be.getNameInNamespace();
						nameInSpaceUnitAssociatedUnit = getLdapEntryAttribute(
								"associatedName", nameInSpaceAssociatedUnit);

						person.setSecretary(true);
					} else {
						try {
							ne = dirContext.search(CONTEXT_UNIT,
									attributesAgent);
							be = (Binding) ne.next();
							nameInSpaceAssociatedUnit = be.getNameInNamespace();
							nameInSpaceUnitAssociatedUnit = getLdapEntryAttribute(
									"associatedName", nameInSpaceAssociatedUnit);
							person.setAgent(true);
						} catch (NullPointerException e2) {
						}
					}
				}
				try {
					unit = getUnitByName(getCnByNameInNamespace(nameInSpaceAssociatedUnit));
					// boc =
					// getBocByName(getCnByNameInNamespace(nameInSpaceUnitAssociatedUnit));
					if (!nameInSpaceUnitAssociatedUnit.equals("")
							&& !typeUnite.equals("cn")) {
						associatedUnit = getUnitByName(getCnByNameInNamespace(nameInSpaceUnitAssociatedUnit));
						unit.setAssociatedUnit(associatedUnit);
						person.setBoc(false);

					} else {
						
						ne = dirContext.search(CONTEXT_BOC, attributesAgent);
						if (ne.hasMore()) {
							
								be = (Binding) ne.next();
								nameInSpaceAssociatedUnit = be.getNameInNamespace();
								nameInSpaceUnitAssociatedUnit = getLdapEntryAttribute(
										"associatedName", nameInSpaceAssociatedUnit);
								System.out.println("nameInSpaceAssociatedUnit Agt "+nameInSpaceAssociatedUnit);
								BOC associatedBOC = getBocByCn(getCnByNameInNamespace(nameInSpaceAssociatedUnit));
								
								person.setAssociatedBOC(associatedBOC);
						}
						ne = dirContext.search(CONTEXT_BOC, attributesResponsable);
						
						if (ne.hasMore()) {
							
								be = (Binding) ne.next();
								nameInSpaceAssociatedUnit = be.getNameInNamespace();
								nameInSpaceUnitAssociatedUnit = getLdapEntryAttribute(
										"associatedName", nameInSpaceAssociatedUnit);
								System.out.println("nameInSpaceAssociatedUnit Resp "+nameInSpaceAssociatedUnit);
								BOC associatedBOC = getBocByCn(getCnByNameInNamespace(nameInSpaceAssociatedUnit));
								
								person.setAssociatedBOC(associatedBOC);
						}
						// associatedBOC= boc;
						// unit.setAssociatedBOC(associatedBOC);
						person.setBoc(true);

					}
					person.setAssociatedDirection(unit);
				} catch (Exception exc) {
					exc.printStackTrace();
				}

			}

		} catch (NamingException e) {
			e.printStackTrace();
		}

		return person;

	}

	@SuppressWarnings("rawtypes")
	public int getListRole(List<RoleModel> result) {
		int reference = 0;
		RoleModel roleModel;
		String name;
		try {
			NamingEnumeration e = dirContext.listBindings(CONTEXT_ROLE);
			while (e.hasMore()) {
				Binding b = (Binding) e.next();
				name = b.getNameInNamespace();
				roleModel = new RoleModel();
				roleModel.setReferenceRole(Integer
						.parseInt(getLdapEntryAttribute("gidNumber", name)));
				roleModel.setNomRole(getLdapEntryAttribute("cn", name));
				roleModel.setDescriptionRole(getLdapEntryAttribute(
						"description", name));
				if (reference < roleModel.getReferenceRole()) {
					reference = roleModel.getReferenceRole();
				}
				result.add(roleModel);
			}
		} catch (NamingException ex) {
			ex.printStackTrace();
		}
		return reference;
	}

	@SuppressWarnings("rawtypes")
	public int getListGroup(List<Group> result) {
		int reference = 0;
		Group group;
		try {
			NamingEnumeration e = dirContext.listBindings(CONTEXT_GROUP);
			while (e.hasMore()) {
				Binding b = (Binding) e.next();
				group = new Group();
				group.setId(Integer.parseInt(getLdapEntryAttribute("gidNumber",
						b.getNameInNamespace())));
				group.setCn(getLdapEntryAttribute("cn", b.getNameInNamespace()));
				group.setDescription(getLdapEntryAttribute("description",
						b.getNameInNamespace()));
				if (reference < group.getId()) {
					reference = group.getId();
				}
				result.add(group);
			}
		} catch (NamingException ex) {
			ex.printStackTrace();
		}
		return reference;
	}

	@SuppressWarnings("rawtypes")
	public List<RoleModel> getListRoleByPathGroup(String pathGroup) {
		String name;
		List<RoleModel> resultList = new ArrayList<RoleModel>();
		RoleModel roleModel;
		BasicAttributes attributes;
		Attribute attribute;
		int i = 0;
		boolean exception = false;

		try {
			name = dirContext.getAttributes(pathGroup).get("sn").get(i)
					.toString();
			if (!name.equals("")) {
				do {
					try {
						name = dirContext.getAttributes(pathGroup).get("sn")
								.get(i).toString();
						attributes = new BasicAttributes(true);
						attribute = new BasicAttribute("cn");
						attribute.add(name);
						attributes.put(attribute);
						NamingEnumeration e = dirContext.search(CONTEXT_ROLE,
								attributes);
						while (e.hasMore()) {
							Binding b = (Binding) e.next();
							roleModel = new RoleModel();
							roleModel
									.setReferenceRole(Integer
											.parseInt(getLdapEntryAttribute(
													"gidNumber",
													b.getNameInNamespace())));
							roleModel.setNomRole(getLdapEntryAttribute("cn",
									b.getNameInNamespace()));
							roleModel.setDescriptionRole(getLdapEntryAttribute(
									"description", b.getNameInNamespace()));
							resultList.add(roleModel);
						}
						i++;
					} catch (ArrayIndexOutOfBoundsException ex) {
						exception = true;
					}

				} while (!exception);
			}

		} catch (NamingException e) {
			e.printStackTrace();
		} catch (NullPointerException ex) {

		}
		return resultList;
	}

	@SuppressWarnings("rawtypes")
	public RoleModel getRoleByName(String attributValue) {
		RoleModel roleModel = new RoleModel();
		try {
			BasicAttributes attributes = new BasicAttributes(true);
			Attribute attribute = new BasicAttribute("cn");
			attribute.add(attributValue);
			attributes.put(attribute);
			NamingEnumeration e = dirContext.search(CONTEXT_ROLE, attributes);
			while (e.hasMore()) {
				Binding b = (Binding) e.next();
				roleModel.setReferenceRole(Integer
						.parseInt(getLdapEntryAttribute("gidNumber",
								b.getNameInNamespace())));
				roleModel.setNomRole(getLdapEntryAttribute("cn",
						b.getNameInNamespace()));
				roleModel.setDescriptionRole(getLdapEntryAttribute(
						"description", b.getNameInNamespace()));
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}

		return roleModel;

	}

	@SuppressWarnings("rawtypes")
	public List<Group> getCurrentsGroupsByPathUser(String attributValue) {
		List<Group> result = new ArrayList<Group>();
		Group group;
		try {
			BasicAttributes attributes = new BasicAttributes(true);
			Attribute attribute = new BasicAttribute("member");
			attribute.add(attributValue);
			attributes.put(attribute);
			NamingEnumeration e = dirContext.search(CONTEXT_GROUP, attributes);
			while (e.hasMore()) {
				Binding b = (Binding) e.next();
				group = new Group();
				group.setId(Integer.parseInt(getLdapEntryAttribute("gidNumber",
						b.getNameInNamespace())));
				group.setCn(getLdapEntryAttribute("cn", b.getNameInNamespace()));
				group.setDescription(getLdapEntryAttribute("description",
						b.getNameInNamespace()));
				result.add(group);
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}

		return result;
	}

	@SuppressWarnings("rawtypes")
	public List<Unit> getListUnit() {
		Unit unit;
		List<Unit> result = new ArrayList<Unit>();
		try {
			NamingEnumeration e = dirContext.listBindings(CONTEXT_UNIT);
			while (e.hasMore()) {
				Binding b = (Binding) e.next();
				//System.out.println(b.getNameInNamespace());
				unit = new Unit();
				unit.setNameUnit(getLdapEntryAttribute("cn",
						b.getNameInNamespace()));
				unit.setDescriptionUnit(getLdapEntryAttribute("description",
						b.getNameInNamespace()));
				unit.setShortNameUnit(getLdapEntryAttribute("l",
						b.getNameInNamespace()));
				unit.setIdUnit(Integer.parseInt(getLdapEntryAttribute(
						"departmentNumber", b.getNameInNamespace())));
				result.add(unit);
			}
		} catch (NamingException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public Person getUserByLoginMp(String path, String login, String MP) {
		Person person = new Person();
		try {
			BasicAttributes attributes = new BasicAttributes(true);

			Attribute attribute = new BasicAttribute("displayName");
			attribute.add(login);
			attributes.put(attribute);

			attribute = new BasicAttribute("userPassword");
			attribute.add(MP);
			attributes.put(attribute);
			NamingEnumeration e = dirContext.search(path, attributes);
			String name;
			while (e.hasMore()) {
				Binding b = (Binding) e.next();
				name = b.getNameInNamespace();
				person.setCn(getLdapEntryAttribute("cn", name));
				person.setId(Integer
						.parseInt(getLdapEntryAttribute("uid", name)));
				person.setNom(getLdapEntryAttribute("sn", name));
				person.setPrenom(getLdapEntryAttribute("givenName", name));
				person.setShortName(getLdapEntryAttribute("employeeNumber",
						name));
				person.setTelephoneNumber(getLdapEntryAttribute(
						"telephoneNumber", name));
				person.setFax(getLdapEntryAttribute("facsimileTelephoneNumber",
						name));
				person.setEmail(getLdapEntryAttribute("mail", name));
				person.setAdresse(getLdapEntryAttribute("street", name));
				person.setCodePostal(Integer.parseInt(getLdapEntryAttribute(
						"postalCode", name)));
				person.setLogin(getLdapEntryAttribute("displayName", name));
				person.setPwd(getLdapEntryAttribute("userPassword", name));
			}

		} catch (NamingException e) {
			e.printStackTrace();
		}

		return person;

	}

	// Getters and Setters
	public void setDirContext(DirContext dirContext) {
		this.dirContext = dirContext;
	}

	public DirContext getDirContext() {
		return dirContext;
	}

	// PUBLIC STATIC VOID MAIN( STRING[] ARGS){
	// LDAPOPERATION L = NEW LDAPOPERATION();
	// L.GETBOCBYCN("BUREAU D'ORDRE 2");
	//
	// }
}
