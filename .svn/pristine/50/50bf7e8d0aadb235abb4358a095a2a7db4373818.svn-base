package xtensus.beans.common.entityDataModel;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.ajax4jsf.model.SerializableDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import xtensus.beans.common.VariableGlobale;
import xtensus.beans.utils.CourrierConsulterInformations;
import xtensus.entity.Courrier;
import xtensus.entity.CourrierDossier;
import xtensus.entity.Dossier;
import xtensus.entity.Expdest;
import xtensus.entity.Expdestexterne;
import xtensus.entity.Transaction;
import xtensus.entity.TransactionDestination;
import xtensus.entity.TransactionDestinationReelle;
import xtensus.entity.Typedossier;
import xtensus.ldap.business.LdapOperation;
import xtensus.services.ApplicationManager;

@Component
@Scope("request")
public class TodayMailsDataModel extends SerializableDataModel{

	@Autowired
	private ApplicationManager appMgr;
	private static final long serialVersionUID = -1956179896877538628L;
    private Integer currentPk;
    private Map<Integer,CourrierConsulterInformations> wrappedData = new HashMap<Integer,CourrierConsulterInformations>();
    private List<Integer> wrappedKeys = null;
    private Integer rowCount; 
    private LdapOperation ldapOperation;
	@Autowired
	private VariableGlobale vb;
	private List<CourrierConsulterInformations> listCourriers;
	private String typeCourrier;
	private boolean rowIndex;
//	private List<CourrierConsulterInformations> listCourriersRecus;
//	private List<CourrierConsulterInformations> listCourriersEnvoyes;

    public TodayMailsDataModel(){
    	System.out.println("ds constructeur");
    	ldapOperation = new LdapOperation();	
    	listCourriers = new ArrayList<CourrierConsulterInformations>();
    	typeCourrier = "Tous";
    	rowIndex = false;
//    	listCourriersEnvoyes = new ArrayList<CourrierConsulterInformations>();
//    	listCourriersRecus = new ArrayList<CourrierConsulterInformations>();
    }
    

    /**
     * This is main part of Visitor pattern. Method called by framework many times during request processing. 
     */
    @Override
    public void walk(FacesContext context, DataVisitor visitor, Range range, Object argument) throws IOException {
    	System.out.println("ds walk");
        int firstRow = ((SequenceRange)range).getFirstRow();
        int numberOfRows = ((SequenceRange)range).getRows();
        if (!rowIndex) {
        	 wrappedKeys = new ArrayList<Integer>();
             process();
             Integer i = 0;
             for (CourrierConsulterInformations item: listCourriers) {
                 wrappedKeys.add(i);
                 wrappedData.put(i, item);
                 visitor.process(context, i, argument);
                 i++;
             }
             rowIndex = false;
		}
       
    }
    
    private void process() {
    	if(vb.getPerson().isResponsable()){
    		processForDirector(vb.getPerson().getId());
		}else if(vb.getPerson().isSecretary()){
			processForSecretary(vb.getPerson().getId());
		}else{
			processForAgent(vb.getPerson().getId());
		}
	}
    
    private void processForDirector (int id) {
    	List<Transaction> listTransaction = new ArrayList<Transaction>();
    	List<TransactionDestination> listTransactionDestination = new ArrayList<TransactionDestination>();
    	String type = "unit_"+String.valueOf(id);
    	String type1 = "sub_"+String.valueOf(id);
    	Typedossier typeDossier = new Typedossier();
		typeDossier = appMgr.getTypeDossierByLibelle("Default").get(0);
		if (typeCourrier.equals("Envoyes")) {
//			listTransaction = appMgr.getListTransactionEnvoyesParResponsable(id,typeDossier.getTypeDossierId(), type, type1, new Date());
	    	setListCourrierEnvoyes(listTransaction);
		} else if (typeCourrier.equals("Recus")) {
//			listTransactionDestination = appMgr.getListTransactionRecusAuResponsable(id, typeDossier.getTypeDossierId(), type, type1, new Date());
	    	System.out.println("liste courrier recus : "+listTransactionDestination.size());
	    	setListCourriersRecus(listTransactionDestination);
		} else if (typeCourrier.equals("Tous")) {
//			listTransaction = appMgr.getListTransactionEnvoyesParResponsable(id,typeDossier.getTypeDossierId(), type, type1, new Date());
	    	setListCourrierEnvoyes(listTransaction);
//	    	listTransactionDestination = appMgr.getListTransactionRecusAuResponsable(id, typeDossier.getTypeDossierId(), type, type1, new Date());
	    	setListCourriersRecus(listTransactionDestination);
		} else {
			
		}
    	
	}

    private List<CourrierConsulterInformations> processForSecretary(int id) {
    	List<CourrierConsulterInformations> result = new ArrayList<CourrierConsulterInformations>();
    	
    	
    	return result;
	}
    
    private List<CourrierConsulterInformations> processForAgent(int id) {
    	List<CourrierConsulterInformations> result = new ArrayList<CourrierConsulterInformations>();
    	
    	
    	return result;
	}

    public void setListCourrierEnvoyes(List<Transaction> listTransactions ){
		CourrierConsulterInformations consulterInformations;
		List<TransactionDestination> listTransactionDestination;
		int refDossier = 0;
		CourrierDossier  courrierDossier;
		Courrier courrier;
		Expdest expDest;
		String result;
		for (Transaction transaction : listTransactions) {
			if(!transaction.getTypetransaction().getTypeTransactionLibelle().equals("Début")){
				consulterInformations = new CourrierConsulterInformations();
				listTransactionDestination = new ArrayList<TransactionDestination>();
				listTransactionDestination = appMgr.getListTransactionDestinationByIdTransaction(transaction.getTransactionId());
				result = "";
				if (transaction.getTransactionDestinationReelle() != null) {
					TransactionDestinationReelle transactionDestinationReelle = new TransactionDestinationReelle();
					Expdestexterne expDestExterne ;
					transactionDestinationReelle = transaction.getTransactionDestinationReelle();
					if(transactionDestinationReelle.getTransactionDestinationReelleTypeDestinataire().equals("Externe")){
						System.out.print("Dans courrier DestinationReelle : ");
						if (transaction.getTypetransaction().getTypeTransactionLibelle().equals("Réponse")) {
							TransactionDestination transactionDestination = new TransactionDestination();
							transactionDestination = appMgr.getListTransactionDestinationByIdTransaction(transaction.getTransactionId()).get(0);
							expDest = new Expdest();
							expDest = appMgr.getListExpDestByIdExpDest(transactionDestination.getId().getIdExpDest()).get(0);
							if (expDest.getTypeExpDest().equals("Interne-Person")) {
								result = result + ldapOperation.getCnById(ldapOperation.CONTEXT_USER, "uid", expDest.getIdExpDestLdap()) + " / ";
							}
						} else {
							expDestExterne = new Expdestexterne();
							expDestExterne = appMgr.getExpediteurById(transactionDestinationReelle.getTransactionDestinationReelleIdDestinataire()).get(0);
							if (expDestExterne.getTypeutilisateur().getTypeUtilisateurLibelle().equals("PP")) {
								result = result + expDestExterne.getExpDestExterneNom()+" (PP)" + " / ";
							} else {
								result = result + expDestExterne.getExpDestExterneNom()+" (PM)" + " / ";
							}
						}
					}
				}else{
					for (TransactionDestination transactionDestination : listTransactionDestination) {
						expDest = new Expdest();
						expDest = appMgr.getListExpDestByIdExpDest(transactionDestination.getId().getIdExpDest()).get(0);
						if (expDest.getTypeExpDest().equals("Interne-Person")) {
							result = result + ldapOperation.getCnById(ldapOperation.CONTEXT_USER, "uid", expDest.getIdExpDestLdap()) + " / ";
						}else if (expDest.getTypeExpDest().equals("Interne-Unité")) {
							result = result + ldapOperation.getCnById(ldapOperation.CONTEXT_UNIT, "departmentNumber", expDest.getIdExpDestLdap()) + " / ";
						}else if (expDest.getTypeExpDest().equals("Externe")) {
							if (expDest.getExpdestexterne().getTypeutilisateur().getTypeUtilisateurLibelle().equals("PP")) {
								result = result + expDest.getExpdestexterne().getExpDestExterneNom()+" (PP)" + " / ";
							} else {
								result = result + expDest.getExpdestexterne().getExpDestExterneNom()+" (PM)" + " / ";
							}
						}
					}
				}
				if (!result.equals("")) {
					int lastIndex = result.lastIndexOf("/");
					result = result.substring(0, lastIndex);
				}
				consulterInformations.setCourrierDestinataireReelle(result);
					expDest = new Expdest();
					expDest = appMgr.getListExpDestByIdExpDest(transaction.getExpdest().getIdExpDest()).get(0);
					if (expDest.getTypeExpDest().equals("Interne-Person")) {
						consulterInformations.setCourrierExpediteur(ldapOperation.getCnById(ldapOperation.CONTEXT_USER, "uid", expDest.getIdExpDestLdap()));
					}else if (expDest.getTypeExpDest().equals("Interne-Unité")) {
						consulterInformations.setCourrierExpediteur(ldapOperation.getCnById(ldapOperation.CONTEXT_UNIT, "departmentNumber", expDest.getIdExpDestLdap()));
					}else if (expDest.getTypeExpDest().equals("Externe")) {
						if (expDest.getExpdestexterne().getTypeutilisateur().getTypeUtilisateurLibelle().equals("PP")) {
							consulterInformations.setCourrierExpediteur(expDest.getExpdestexterne().getExpDestExterneNom()+" (PP)");
						} else {
							consulterInformations.setCourrierExpediteur(expDest.getExpdestexterne().getExpDestExterneNom()+" (PM)");
						}
					}
					
					consulterInformations.setCourrierAValider(0);
					consulterInformations.setCourrierRecu(0);
					Dossier dossier = new Dossier();
					dossier = transaction.getDossier();
					
						courrierDossier = new CourrierDossier();
						refDossier = dossier.getDossierId();
						courrier = new Courrier();
						courrierDossier = appMgr.getCourrierDossierByIdDossier(refDossier).get(0);
						courrier = appMgr.getCourrierByIdCourrier(courrierDossier.getId().getIdCourrier()).get(0);
						consulterInformations.setTransaction(transaction);
						consulterInformations.setCourrier(courrier);
						consulterInformations.setCourrierCommentaire(courrier.getCourrierCommentaire());
						consulterInformations.setCourrierObjet(courrier.getCourrierObjet());
						consulterInformations.setCourrierReference(courrier.getCourrierReferenceCorrespondant());
						consulterInformations.setCourrierNature(courrier.getNature().getNatureLibelle());
						consulterInformations.setCourrierDateReceptionEnvoi(transaction.getTransactionDateTransaction());
						consulterInformations.setTypeCourrier(getCategorieCourrier(transaction));
						listCourriers.add(consulterInformations);
				
			}
			}
			
	}
    
	public void setListCourriersRecus(List<TransactionDestination> listTransactions ){
		CourrierConsulterInformations consulterInformations;
		int refDossier = 0;
		CourrierDossier  courrierDossier;
		Courrier courrier;
		Transaction transaction;
		Expdest expDest;
		int i ;
		boolean findPerson ;
		for (TransactionDestination transactionDestination : listTransactions) {
				consulterInformations = new CourrierConsulterInformations();
				transaction = new Transaction();
				transaction = appMgr.getListTransactionByIdTransaction(transactionDestination.getId().getIdTransaction()).get(0);
				expDest = new Expdest();
				expDest = appMgr.getListExpDestByIdExpDest(transaction.getExpdest().getIdExpDest()).get(0);
				if (expDest.getTypeExpDest().equals("Interne-Person")) {
					consulterInformations.setCourrierExpediteur(ldapOperation.getCnById(ldapOperation.CONTEXT_USER, "uid", expDest.getIdExpDestLdap()));
				}else if (expDest.getTypeExpDest().equals("Interne-Unité")) {
					consulterInformations.setCourrierExpediteur(ldapOperation.getCnById(ldapOperation.CONTEXT_UNIT, "departmentNumber", expDest.getIdExpDestLdap()));
				}else if (expDest.getTypeExpDest().equals("Externe")) {
					if (expDest.getExpdestexterne().getTypeutilisateur().getTypeUtilisateurLibelle().equals("PP")) {
						consulterInformations.setCourrierExpediteur(expDest.getExpdestexterne().getExpDestExterneNom()+" (PP)");
						consulterInformations.setCourrierExpediteurObjet(expDest.getExpdestexterne());
					} else {
						consulterInformations.setCourrierExpediteur(expDest.getExpdestexterne().getExpDestExterneNom()+" (PM)");
						consulterInformations.setCourrierExpediteurObjet(expDest.getExpdestexterne());
					}
				}
				if (transaction.getTransactionDestinationReelle() != null) {
					TransactionDestinationReelle transactionDestinationReelle = new TransactionDestinationReelle();
					Expdestexterne expDestExterne ;
					transactionDestinationReelle = transaction.getTransactionDestinationReelle();
					if(transactionDestinationReelle.getTransactionDestinationReelleTypeDestinataire().equals("Externe")){
						expDestExterne = new Expdestexterne();
						expDestExterne = appMgr.getExpediteurById(transactionDestinationReelle.getTransactionDestinationReelleIdDestinataire()).get(0);
						if (expDestExterne.getTypeutilisateur().getTypeUtilisateurLibelle().equals("PP")) {
							consulterInformations.setCourrierDestinataireReelle(expDestExterne.getExpDestExterneNom()+" (PP)");
							consulterInformations.setCourrierDestinataireObject(expDestExterne);
						} else {
							consulterInformations.setCourrierDestinataireReelle(expDestExterne.getExpDestExterneNom()+" (PM)");
							consulterInformations.setCourrierDestinataireObject(expDestExterne);
						}
					}
				}else{
					expDest = new Expdest();
					expDest = appMgr.getListExpDestByIdExpDest(transactionDestination.getId().getIdExpDest()).get(0);
					if (expDest.getTypeExpDest().equals("Interne-Person")) {
						consulterInformations.setCourrierDestinataireReelle(ldapOperation.getCnById(ldapOperation.CONTEXT_USER, "uid", expDest.getIdExpDestLdap()));
					}else if (expDest.getTypeExpDest().equals("Interne-Unité")) {
						consulterInformations.setCourrierDestinataireReelle(ldapOperation.getCnById(ldapOperation.CONTEXT_UNIT, "departmentNumber", expDest.getIdExpDestLdap()));
					}
				}
				consulterInformations.setCourrierRecu(1);
				if (transaction.getTransactionDestinationReelle() != null && transaction.getEtat().getEtatLibelle().equals("A valider")) {
					consulterInformations.setCourrierAValider(1);
				} else {
					consulterInformations.setCourrierAValider(0);
				}
				
				Dossier dossier = new Dossier();
				dossier = transaction.getDossier();
					courrierDossier = new CourrierDossier();
					refDossier = dossier.getDossierId();
					courrier = new Courrier();
					courrierDossier = appMgr.getCourrierDossierByIdDossier(refDossier).get(0);
					courrier = appMgr.getCourrierByIdCourrier(courrierDossier.getId().getIdCourrier()).get(0);
					consulterInformations.setTransaction(transaction);
					consulterInformations.setCourrier(courrier);
					consulterInformations.setCourrierCommentaire(courrier.getCourrierCommentaire());
					consulterInformations.setCourrierObjet(courrier.getCourrierObjet());
					consulterInformations.setCourrierReference(courrier.getCourrierReferenceCorrespondant());
					consulterInformations.setCourrierNature(courrier.getNature().getNatureLibelle());
					consulterInformations.setCourrierDateReceptionEnvoi(transaction.getTransactionDateTransaction());
					consulterInformations.setTypeCourrier(getCategorieCourrier(transactionDestination));
					listCourriers.add(consulterInformations);
//				if (transaction.getTransactionDestinationReelle() != null && transaction.getEtat().getEtatLibelle().equals("A valider") && (typeCourrier.equals("A. Mes Propres Courriers") || typeCourrier.equals("B. Les Courriers de Mon Unité"))) {
//					listCourriersRecusAvalider.add(consulterInformations);
//				}
		}
	}
    
    private String getCategorieCourrier(Transaction transaction){
    	String result = "";
    	String [] type = new String [2];
    	if(vb.getPerson().isResponsable()){
			if (transaction.getTransactionTypeIntervenant().contains("sub")) {
				type = transaction.getTransactionTypeIntervenant().split("_");
					if (Integer.parseInt(type[1]) == vb.getPerson().getId()) {
						result = "A. Mes Propres Courriers";
					} else {
						result = "F. Les Courriers de Mes Subordonnées";						
					}
			} else if (transaction.getTransactionTypeIntervenant().contains("unit")) {
				type = transaction.getTransactionTypeIntervenant().split("_");
					if (Integer.parseInt(type[1]) == vb.getPerson().getAssociatedDirection().getIdUnit()) {
						result = "B. Les Courriers de Mon Unité";
					} else {
						result = "E. Les Courriers de Mes Sous-Unités";
					}
			} else if (transaction.getTransactionTypeIntervenant().contains("secretary")) {
				result = "C. Les Courriers de Ma Secrétaire";
			} else if (transaction.getTransactionTypeIntervenant().contains("agent")) {
				result = "D. Les Courriers de Mes Agents";
			} 
		}
    	
    	return result;
    }
    
    private String getCategorieCourrier(TransactionDestination transactionDestination){
    	String result = "";
    	String [] type = new String [2];
    	if(vb.getPerson().isResponsable()){
			if (transactionDestination.getTransactionDestTypeIntervenant().contains("sub")) {
				type = transactionDestination.getTransactionDestTypeIntervenant().split("_");
					if (Integer.parseInt(type[1]) == vb.getPerson().getId()) {
						result = "A. Mes Propres Courriers";
					} else {
						result = "F. Les Courriers de Mes Subordonnées";						
					}
			} else if (transactionDestination.getTransactionDestTypeIntervenant().contains("unit")) {
				type = transactionDestination.getTransactionDestTypeIntervenant().split("_");
				if (Integer.parseInt(type[1]) == vb.getPerson().getAssociatedDirection().getIdUnit()) {
					result = "B. Les Courriers de Mon Unité";
				} else {
					result = "E. Les Courriers de Mes Sous-Unités";
				}
			} else if (transactionDestination.getTransactionDestTypeIntervenant().contains("secretary")) {
				result = "C. Les Courriers de Ma Secrétaire";
			} else if (transactionDestination.getTransactionDestTypeIntervenant().contains("agent")) {
				result = "D. Les Courriers de Mes Agents";
			} 
		}
    	
    	return result;
    }
    
	public void test(ActionEvent e){
    	System.out.println("bien tapé");
    	listCourriers = new ArrayList<CourrierConsulterInformations>();
    }

	/**
     * This method must return actual data rows count from the Data Provider. It is used by pagination control
     * to determine total number of data items.
     */
    @Override
    public int getRowCount() {
    	System.out.println("appMgr : "+appMgr);
    	rowCount = listCourriers.size();
    	return rowCount.intValue();
//        if (rowCount==null) {
//            rowCount = (int) listCourriers.size();
//            return rowCount.intValue();
//        } else {
//            return rowCount.intValue();
//        }
    }
    /**
     * This is main way to obtain data row. It is intensively used by framework. 
     * We strongly recommend use of local cache in that method. 
     */
    @Override
    public CourrierConsulterInformations getRowData() {
        if (currentPk==null) {
            return null;
        } else {
        	CourrierConsulterInformations ret = wrappedData.get(currentPk);
            if (ret==null) {
//                ret = getDataProvider().getAuctionItemByPk(currentPk);
//                wrappedData.put(currentPk, ret);
                return ret;
            } else {
                return ret;
            }
        }
    }
    
    @Override
    public Object getRowKey() {
    	System.out.println("ds getRowKey");
        return currentPk;
    }
    /**
     * This method normally called by Visitor before request Data Row.
     */
    @Override
    public void setRowKey(Object key) {
        this.currentPk = (Integer) key;
    }

    /**
     * Unused rudiment from old JSF staff.
     */
    @Override
    public int getRowIndex() {
    	System.out.println("ds getRowIndex");
    	rowIndex = true;
    	return 1;
    }

    /**
     * Unused rudiment from old JSF staff.
     */
    @Override
    public Object getWrappedData() {
        throw new UnsupportedOperationException();
    }

    /**
     * Never called by framework.
     */
    @Override
    public boolean isRowAvailable() {
//        if (currentPk==null) {
//            return false;
//        }
//        else {
//            return getDataProvider().hasAuctionItemByPk(currentPk);
//        }
    	return true;
    }

    /**
     * Unused rudiment from old JSF staff.
     */
    @Override
    public void setRowIndex(int rowIndex) {
        throw new UnsupportedOperationException();
    }

    /**
     * Unused rudiment from old JSF staff.
     */
    @Override
    public void setWrappedData(Object data) {
        throw new UnsupportedOperationException();
    }

    /**
     * This method suppose to produce SerializableDataModel that will be serialized into View State and used on a post-back.
     * In current implementation we just mark current model as serialized. In more complicated cases we may need to 
     * transform data to actually serialized form.
     */
    public  SerializableDataModel getSerializableModel(Range range) {
        if (wrappedKeys!=null) {
            return this; 
        } else {
            return null;
        }
    }
    
    private <V> V lookupInContext(String expression, Class<? extends V> c) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application application = facesContext.getApplication();
        return c.cast(application.evaluateExpressionGet(facesContext, MessageFormat.format("#'{'{0}'}'", expression), c));
    }
    
    private String auctionDataModelExpressionString;

    private String auctionDataProviderExpressionString;

    
    /**
     * This is helper method that is called by framework after model update. In must delegate actual database update to 
     * Data Provider.
     */
    @Override
    public void update() {
//        AuctionDataModel auctionDataModel = lookupInContext(auctionDataModelExpressionString, AuctionDataModel.class);
//        Object savedKey = getRowKey();
//        for (Integer key : wrappedKeys) {
//            auctionDataModel.setRowKey(key);
//            auctionDataModel.getRowData().setBid(wrappedData.get(key).getBid());
//        }
//        setRowKey(savedKey);
//        //getDataProvider().update();
//        
//        this.wrappedData.clear();
//        this.wrappedKeys.clear();
//        resetDataProvider();
    }
    
//    protected void resetDataProvider() {
//        this.dataProvider = null;
//    }
//
//    public AuctionDataProvider getDataProvider() {
//        if (dataProvider == null) {
//            dataProvider = lookupInContext(auctionDataProviderExpressionString, AuctionDataProvider.class);
//        }
//        return dataProvider;
//    }
    public String getAuctionDataModelExpressionString() {
        return auctionDataModelExpressionString;
    }
    public void setAuctionDataModelExpressionString(
            String auctionDataModelExpressionString) {
        this.auctionDataModelExpressionString = auctionDataModelExpressionString;
    }
    public String getAuctionDataProviderExpressionString() {
        return auctionDataProviderExpressionString;
    }
    public void setAuctionDataProviderExpressionString(
            String auctionDataProviderExpressionString) {
        this.auctionDataProviderExpressionString = auctionDataProviderExpressionString;
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


	public void setLdapOperation(LdapOperation ldapOperation) {
		this.ldapOperation = ldapOperation;
	}


	public LdapOperation getLdapOperation() {
		return ldapOperation;
	}


	public void setListCourriers(List<CourrierConsulterInformations> listCourriers) {
		this.listCourriers = listCourriers;
	}


	public List<CourrierConsulterInformations> getListCourriers() {
		return listCourriers;
	}


	public void setTypeCourrier(String typeCourrier) {
		this.typeCourrier = typeCourrier;
	}


	public String getTypeCourrier() {
		return typeCourrier;
	}


	public void setRowIndex(boolean rowIndex) {
		this.rowIndex = rowIndex;
	}


	public boolean isRowIndex() {
		return rowIndex;
	}


//	public void setListCourriersRecus(List<CourrierConsulterInformations> listCourriersRecus) {
//		this.listCourriersRecus = listCourriersRecus;
//	}
//
//
//	public List<CourrierConsulterInformations> getListCourriersRecus() {
//		return listCourriersRecus;
//	}
//
//
//	public void setListCourriersEnvoyes(List<CourrierConsulterInformations> listCourriersEnvoyes) {
//		this.listCourriersEnvoyes = listCourriersEnvoyes;
//	}
//
//
//	public List<CourrierConsulterInformations> getListCourriersEnvoyes() {
//		return listCourriersEnvoyes;
//	}
	
}
