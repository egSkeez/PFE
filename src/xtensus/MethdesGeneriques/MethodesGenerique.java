package xtensus.MethdesGeneriques;
import java.util.List;
import xtensus.entity.Transaction;
import xtensus.entity.TransactionDestination;
import xtensus.entity.Variables;
import xtensus.ldap.business.LdapOperation;
import xtensus.ldap.model.BOC;
import xtensus.ldap.model.Person;
import xtensus.ldap.model.Unit;
import xtensus.services.ApplicationManager;

public class MethodesGenerique {
	
	
	public static String generationCodeUniqueCourrier(Person personneConnecte, ApplicationManager appMgr,Transaction transaction,String anneeCourrier,String moisCourrier,String serviceExpediteurAcronyme,boolean connecteIsBoc,Integer courrierEtat, List<BOC> listeBOS,List<Person> listTousUtilisateur,int idUtilisateurTransaction ){
		System.out.println("DANS generationCdeUniqueCourrier ");
		
		String templateCodeUnique="";
		
		String sens="";
		String acronymeBOC="";
		// Récupérer le template du code Unique du Courrier
		List<Variables> templateCodeUniqueList = appMgr.listVariablesByLibelle("code_courrier_unique_personnalisable");
		if(templateCodeUniqueList!=null && templateCodeUniqueList.size()>0){
			 templateCodeUnique = templateCodeUniqueList.get(0).getVaraiablesValeur();
			
			String courrierTypeOrdre=Integer.toString(transaction.getCourrierTypeOrdre()) ;

			// XTE : Si le courrier est ajouté par un non BOC, il aura le type à NULL
			if (transaction.getCourrierType() != null) {
				sens=transaction.getCourrierType();
				
			} else {
				sens="I";
				
			}
			
			System.out.println("courrierEtat :"+courrierEtat);
			System.out.println("connecteIsBoc "+connecteIsBoc);
			//Récupération de l'acronyme de BOC
		
			
			if ((connecteIsBoc &&  (courrierEtat.intValue() == 5 || courrierEtat.intValue() == 6))) {
				
				// si l'état 6 ==> c'est exécuté donc c'est l'abréviation de BO connecté à afficher
				if(courrierEtat.intValue() == 6){
					int idBoc=personneConnecte.getAssociatedBOC().getIdBOC();
					LdapOperation ldapOperation =new LdapOperation();
					BOC boc=ldapOperation.getBocByID(idBoc);
					acronymeBOC=boc.getShortNameBOC();
							
				}
				
				if(courrierEtat.intValue() == 5){
				//Le Connecté est un BO et le courrier à Exécuter
				// ON vérifie si c'est lui qui a ajouté le courrier ==> à afficher son abréviation
				int idBocPersonneConnecte = personneConnecte.getAssociatedBOC().getIdBOC();
				String codeIntervenantBO="boc_"+idBocPersonneConnecte;
					
				// Récupérer liste des transaction par Dossier
				List<Transaction> listTransaction = appMgr.getTransactionByIdDossier(transaction.getDossier().getDossierId());
				LdapOperation ldapOperation = new LdapOperation();
				if(listTransaction!=null && listTransaction.size()>0){
					Transaction tr = listTransaction.get(listTransaction.size()-1);
					// Chercher la trDest de la transaction
					List<TransactionDestination> trDests = appMgr.getDestinationByIdTransaction(tr.getTransactionId());
					TransactionDestination trDest= new TransactionDestination();
					if(trDests!=null && trDests.size()>0)
						trDest=trDests.get(0);
					
					
					String codeIntervenantBOActuel = trDest.getTransactionDestTypeIntervenant();
					
					
					
					//SI ajouté par BO connecté
					if(codeIntervenantBOActuel.equals(codeIntervenantBO)){
						int idBoc=personneConnecte.getAssociatedBOC().getIdBOC();
						BOC boc=ldapOperation.getBocByID(idBoc);
						acronymeBOC=boc.getShortNameBOC();
					}
					else {
						//SI ajouté par un autre BO
						if(codeIntervenantBOActuel.startsWith("boc_")){
							// récupérer l'ID de BO qui a ajouté
							String transactionTypeIntervenant = codeIntervenantBOActuel;
							int indexIdBoc=transactionTypeIntervenant.indexOf("_");
							if( indexIdBoc>0){
								int idBoc=Integer.parseInt(transactionTypeIntervenant.substring(indexIdBoc+1, transactionTypeIntervenant.length()));
								BOC boc=ldapOperation.getBocByID(idBoc);
								
								acronymeBOC=boc.getShortNameBOC();
							}
							
						}
						else{
							// C'est un Courrier ajouté par une structure
							int idExp=tr.getExpdest().getIdExpDestLdap();
							Unit unit ;
							if(tr.getExpdest().getTypeExpDest().equals("Interne-Unité")){
								
								// C'est une Unité 
								 unit = ldapOperation.getUnitById(idExp);
							}
							else{
								//C'est une personne de l'une Unité
								Person person = ldapOperation.getPersonalisedUserById(idExp);
								 unit=person.getAssociatedDirection();
								 unit = ldapOperation.getUnitById(unit.getIdUnit());
							}
							
							// Chercher le BO de Unité trouvée
							if(unit.getAssociatedBOC()!=null){
								
								BOC boc=ldapOperation.getBocByID(unit.getAssociatedBOC().getIdBOC());
								acronymeBOC=boc.getShortNameBOC();
								
							}
								
							else{
								BOC bocAssocie=getIdBocByUnit(unit);
								BOC boc=ldapOperation.getBocByID(bocAssocie.getIdBOC());
								acronymeBOC=boc.getShortNameBOC();
								//acronymeBOC=bocAssocie.getShortNameBOC();
							}
						}
					}
					
				}
				}
			
			
			}
			
			
			if (!connecteIsBoc &&  ((courrierEtat.intValue() == 5) || (courrierEtat.intValue() == 6))) {
				if(courrierEtat.intValue() == 5){
					//si le courrier n'est pas encore exécuté ne pas afficher l'abreviation de BO
					acronymeBOC="";
				}
								
				else{
				boolean personeTrouve = false;

				if(listeBOS != null && listeBOS.size()>0)
				for (BOC bureau : listeBOS) {
					
					List<Person> listeMembres = bureau.getMembersBOC();
					for (Person personne : listeMembres) {
						
						if (personne.getId() == idUtilisateurTransaction) {
							
							acronymeBOC= bureau.getShortNameBOC();
							personeTrouve = true;
							break;
						}
					}
				}
				if (!personeTrouve) {
					// récupérer le BO de la direction de cette utilisateur
					Person person = new Person();
					boolean findUser = false;
					if(listTousUtilisateur!=null &&listTousUtilisateur.size()>0){
					for (int i = 0; i < listTousUtilisateur.size(); i++) {
						int j = 0;

						do {
							if (listTousUtilisateur.get(j).getId() == idUtilisateurTransaction) {
								person = listTousUtilisateur.get(j);
								findUser = true;
							} else {
								j++;
							}
						} while (!findUser
								&& j < listTousUtilisateur.size());

					}
					}
					if (findUser) {
						// chercher le BO de l'unité du personne trouvé
						Unit unite = person.getAssociatedDirection();
					
						if (unite.getAssociatedBOC() != null) {
							
							acronymeBOC= unite.getAssociatedBOC().getShortNameBOC();
						}

					}

				}
				}
			}
			
			templateCodeUnique=constructionCode(templateCodeUnique,courrierTypeOrdre,serviceExpediteurAcronyme,anneeCourrier,moisCourrier,sens,acronymeBOC);
		}
		else {
			System.out.println("Le template n'existe pas dans la Base de données");
		}
		System.out.println("templateCodeUnique = "+templateCodeUnique);
		
		return templateCodeUnique;
	}
	
	
	private static BOC getIdBocByUnit(Unit unit) {

		if (unit.getAssociatedUnit() != null && unit.getIdUnit() != null) {
			
			LdapOperation ldapOperation = new LdapOperation();
			Unit u = ldapOperation .getUnitById(unit.getAssociatedUnit()
					.getIdUnit());
			return getIdBocByUnit(u);

		} else if (unit.getAssociatedBOC() != null) {
			return unit.getAssociatedBOC();
		
		} else {
			
			System.out
					.println("DANS else unit.getAssociatedUnit() != null && unit.getAssociatedBOC()!=null ");
			return null;

		}
	}
	
	
	public static String constructionCode(String template, String idCourrier,String srv, String annee, String mois, String sens, String bo){
		
		if (bo.equals("")) {
			int positionBOC = template.indexOf("[BOC]");
			if (positionBOC > 0) {
				// si cette chaine existe et n'est pas au début
				if (template.charAt(positionBOC - 1) != ']') {
					String premiereChaine = template.substring(0,
							positionBOC - 1);
					String deuxiemeChaine = template.substring(positionBOC,
							template.length());
//					System.out.println("premiereChaine  " + premiereChaine);
//					System.out.println("deuxiemeChaine " + deuxiemeChaine);
					template = premiereChaine + deuxiemeChaine;
					
				}
				
				// vérification si nous avons un caractère autre que "[" après [BOC]
				positionBOC = template.indexOf("[BOC]");
				// 5 :c'est la longueue de [BOC] à remplacer
				int positionFinBoc=positionBOC+5;
//				System.out.println("positionFinBoc "+positionFinBoc); 
				if(positionFinBoc<template.length()){
					String premiereChaine = template.substring(0,
							positionFinBoc);
					String deuxiemeChaine = template.substring(positionFinBoc+1,
							template.length());
//					System.out.println("premiereChaine  " + premiereChaine);
//					System.out.println("deuxiemeChaine " + deuxiemeChaine);
					template = premiereChaine + deuxiemeChaine;
//					System.out.println("Nouveau Template " + template);
					
				}
			}
			else if (positionBOC == 0) {
				positionBOC = template.indexOf("[BOC]");
				// vérification si nous avons un caractère autre que "[" après [BOC]
				int positionFinBoc=positionBOC+5;
//				System.out.println("positionFinBoc "+positionFinBoc); 
				if(positionFinBoc<template.length()){
					String premiereChaine = template.substring(0,
							positionFinBoc);
					String deuxiemeChaine = template.substring(positionFinBoc+1,
							template.length());
//					System.out.println("premiereChaine  " + premiereChaine);
//					System.out.println("deuxiemeChaine " + deuxiemeChaine);
					template = premiereChaine + deuxiemeChaine;
//					System.out.println("Nouveau Template " + template);
					
				}
			}
			else{
				System.out.println( "[BOC] n'existe pas dans le Template");
			}
			// 
		}
		else{
			System.out.println("BO " +bo);
		}
		
		template = template.replace("[ID]",idCourrier);
		template = template.replace("[Annee]",annee);
		template = template.replace("[Mois]",mois);
		template = template.replace("[Sens]",sens);
		template = template.replace("[BOC]",bo);
		template = template.replace("[SRV]",srv);
		
		return template;
	}

	
	public static String modificationStringPourLDAP(String chaine){
		chaine =chaine.replaceAll("/"," ");
		//chaine =chaine.replaceAll("\\"," ");
		return chaine;
	}
	
	
}
