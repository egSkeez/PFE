package xtensus.beans.common.entityDataModel;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
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

import xtensus.entity.Transaction;
import xtensus.services.ApplicationManager;

public class OthersFieldsDataModel extends SerializableDataModel{

	@Autowired
	private ApplicationManager appMgr;
    private Integer currentPk;
    private Map<Integer,Transaction> wrappedData = new HashMap<Integer,Transaction>();
    private List<Integer> wrappedKeys = null;

    public OthersFieldsDataModel(){
    	
    }
    
    /**
     * 
     */
    private static final long serialVersionUID = -1956179896877538628L;

    /**
     * This method never called from framework.
     * (non-Javadoc)
     * @see org.ajax4jsf.model.ExtendedDataModel#getRowKey()
     */
    
    public void test(ActionEvent e){
    	System.out.println("bien tapé");
    }
    
    @Override
    public Object getRowKey() {
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
     * This is main part of Visitor pattern. Method called by framework many times during request processing. 
     */
    @Override
    public void walk(FacesContext context, DataVisitor visitor, Range range, Object argument) throws IOException {
    	System.out.println("Dans Walk : "+range);
        int firstRow = ((SequenceRange)range).getFirstRow();
        System.out.println("firstRow : "+firstRow);
        int numberOfRows = ((SequenceRange)range).getRows();
        System.out.println("numberOfRows : "+numberOfRows);
        wrappedKeys = new ArrayList<Integer>();
        for (Transaction item: appMgr.getListTransactionTest(firstRow, rowCount)) {
            wrappedKeys.add(item.getTransactionId());
            wrappedData.put(item.getTransactionId(), item);
            visitor.process(context, item.getTransactionId(), argument);
        }
    }
    /**
     * This method must return actual data rows count from the Data Provider. It is used by pagination control
     * to determine total number of data items.
     */
    private Integer rowCount; // better to buffer row count locally
    @Override
    public int getRowCount() {
    	System.out.println("appMgr : "+appMgr);
        if (rowCount==null) {
            rowCount = (int) appMgr.getRowCount();
            return rowCount.intValue();
        } else {
            return rowCount.intValue();
        }
    }
    /**
     * This is main way to obtain data row. It is intensively used by framework. 
     * We strongly recommend use of local cache in that method. 
     */
    @Override
    public Transaction getRowData() {
        if (currentPk==null) {
            return null;
        } else {
            Transaction ret = wrappedData.get(currentPk);
            if (ret==null) {
//                ret = getDataProvider().getAuctionItemByPk(currentPk);
//                wrappedData.put(currentPk, ret);
                return ret;
            } else {
                return ret;
            }
        }
    }

    /**
     * Unused rudiment from old JSF staff.
     */
    @Override
    public int getRowIndex() {
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
}
