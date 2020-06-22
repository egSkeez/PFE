package xtensus.beans.common.GBO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.ajax4jsf.model.SerializableDataModel;
import org.richfaces.model.FilterField;
import org.richfaces.model.Modifiable;
import org.richfaces.model.SortField2;

import xtensus.beans.common.VariableGlobale;
import xtensus.beans.utils.CourrierInformations;
public class CourrierJourDataModel extends SerializableDataModel implements
		Modifiable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4245586903778075112L;
	private Integer currentPk;
	private Map<Integer, CourrierInformations> wrappedData = new HashMap<Integer, CourrierInformations>();
	private List<Integer> wrappedKeys = null;
	private Integer rowCount; // better to buffer row count locally
	private Integer rowIndex;
	private Integer oldFirstRow=3;
	private Integer totalCount;
	private SequenceRange firstRange;
	private Integer firstPage;
	private boolean scrollerWalkVisit;

	// Beans
	private CourrierConsultationJourBean courrierConsultation;

	// Filtre recherche
	private String typeCourrierValidationJour;
	private Boolean checkedTypeCourrierValidationJour;
	private String transmissionCourrierJour;
	private String typeCourrierTraitementJour;
	private String categorieCourrierJour;
	private String typeCourrierJour;
	private String courrierRubriqueJour;
	private VariableGlobale vb = new VariableGlobale();

	private boolean moreChoicesJour;
	private boolean showExecuteAllButtonJour;
	private boolean hideExecuteAllButtonJour;
	private boolean hideExecuteAllButton;
   private String stlecss;
	// For filter and Sort
	private String sortField;
	private boolean descending;
	private HashMap<String, Object> filterMap = new HashMap<String, Object>();

	// for calling walk Restrictions
//	private boolean modifyVisited;
	private boolean walkVisited;
	private boolean countVisited;

	@PostConstruct
	public void init() {
		typeCourrierValidationJour = "";
		transmissionCourrierJour = "Tout les courriers";
		typeCourrierTraitementJour = "tous";
		categorieCourrierJour = "T";
		typeCourrierJour = "Tous";
		courrierRubriqueJour = "2";
		moreChoicesJour = false;
		showExecuteAllButtonJour = false;
		hideExecuteAllButtonJour = true;
		hideExecuteAllButton = true;
		checkedTypeCourrierValidationJour = false;
		firstPage = 1;

	}

	/**
	 * This method never called from framework. (non-Javadoc)
	 * 
	 * @see org.ajax4jsf.model.ExtendedDataModel#getRowKey()
	 */
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
	 * This is main part of Visitor pattern. Method called by framework many
	 * times during request processing.
	 */
	@Override
	public void walk(FacesContext context, DataVisitor visitor, Range range,
			Object argument) throws IOException {
		System.out.println("IN CRjour WALK");
		int firstRow = 0;
		int numberOfRows = 0;
		if (firstRange == null) {
			firstRange = (SequenceRange) range;
		}
		if (scrollerWalkVisit) {
			firstRow = firstRange.getFirstRow();
			numberOfRows = firstRange.getRows();
			range = firstRange;
			firstPage = 1;
		} else {
			if (courrierConsultation.getVb().isOldPage() && courrierConsultation.getVb().getFirstPageJour() != null) {
				firstPage = courrierConsultation.getVb().getFirstPageJour();
				numberOfRows = courrierConsultation.getVb().getNumberOfRows();
				firstRow = (firstPage - 1) * numberOfRows;
			} else {
				firstRow = ((SequenceRange) range).getFirstRow();
				numberOfRows = ((SequenceRange) range).getRows();
			}
		}
//		if (walkVisited == false || firstRow != oldFirstRow || (walkVisited == true && firstRow == 0)) {
		if(courrierConsultation.getVb().getSelectedListCourrier().equals("CRjour")){
			if (walkVisited == false || firstRow != oldFirstRow || (walkVisited == true && firstRow == 0)) {
				System.out.println("IN JOUR WALK true");
				courrierConsultation.getVb().setFirstRow(firstRow);
				courrierConsultation.getVb().setNumberOfRows(numberOfRows);
				courrierConsultation.getVb().setFilterMap(filterMap);
				courrierConsultation.getVb().setSortField(sortField);
				courrierConsultation.getVb().setDescending(descending);
				courrierConsultation.getVb().setFirstPageJour(firstPage);
				walkVisited = true;
				try {
					oldFirstRow = firstRow;
					wrappedKeys = new ArrayList<Integer>();
					if (checkedTypeCourrierValidationJour) {
						typeCourrierValidationJour = "Avalider";
					} else {
						typeCourrierValidationJour = "";
					}
					
					System.out.println("AH =======   "+typeCourrierValidationJour);
					for (CourrierInformations item : courrierConsultation
							.searchListCourrier(filterMap, sortField, descending,typeCourrierJour,
									categorieCourrierJour,
									transmissionCourrierJour,
									typeCourrierTraitementJour,
									typeCourrierValidationJour, firstRow,
									numberOfRows,false,courrierRubriqueJour)) {
						wrappedKeys.add(item.getTransactionID());
						wrappedData.put(item.getTransactionID(), item);
						visitor.process(context, item.getTransactionID(), argument);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void modify(List<FilterField> filterFields, List<SortField2> sortFields) {
		filterMap.clear();
		walkVisited = false;
	}
//		SortField2 sortField2 = null;
//		ExtendedFilterField extendedFilterField = null;
//		String value = null;
//		Expression expression = null;
//		String expressionStr = null;
//		if (sortFields != null && !sortFields.isEmpty()) {
//			sortField2 = sortFields.get(0);
//			int index = sortField2.getExpression().getExpressionString()
//					.lastIndexOf(".");
//			this.sortField = sortField2.getExpression().getExpressionString()
//					.substring(index).replace("}", "").replace(".", "");
//			if (sortField2.getOrdering() == Ordering.DESCENDING) {
//				this.descending = true;
//			} else {
//				this.descending = false;
//			}
//
//		}

//		for (FilterField filterField : filterFields) {
//			extendedFilterField = (ExtendedFilterField) filterField;
//			System.out.println("VALUE : "
//					+ extendedFilterField.getFilterValue());
//			System.out
//					.println("EXPRESSION : "
//							+ extendedFilterField.getExpression()
//									.getExpressionString());
//			if (filterField instanceof ExtendedFilterField) {
//				extendedFilterField = (ExtendedFilterField) filterField;
//				value = extendedFilterField.getFilterValue();
//				if (value != null) {
//					expression = extendedFilterField.getExpression();
//					expressionStr = expression.getExpressionString();
//					int index = expressionStr.lastIndexOf(".");
//					if (!expression.isLiteralText()) {
//						expressionStr = expressionStr.substring(index)
//								.replace("}", "").replace(".", "");
//					}
//					filterMap.put(expressionStr, value);
//				}
//			}
//
//		}

//	}
	/**
	 * This method must return actual data rows count from the Data Provider. It
	 * is used by pagination control to determine total number of data items.
	 */

	@Override
	public int getRowCount() {
		System.out.println("2020-05-28 - DANS GETROWCOUNT============================== ");
		if(courrierConsultation.getVb().getSelectedListCourrier().equals("CRjour")){
			if (rowCount == null || countVisited == false) {
				try {
					countVisited = true;
		//			if (rowCount == null) {
						if (checkedTypeCourrierValidationJour) {
							typeCourrierValidationJour = "Avalider";
						} else {
							typeCourrierValidationJour = "";
						}
						
						rowCount = courrierConsultation.getCountCourrier(filterMap,
								transmissionCourrierJour, typeCourrierTraitementJour, typeCourrierJour,
								typeCourrierValidationJour, categorieCourrierJour, courrierRubriqueJour,
								false).intValue();
						System.out.println("2020-05-28 - DANS getRowCount "+rowCount);
		//				System.out.println("rowcount--JOUR--- : " + rowCount);
		//			}
						
						vb.setNumRowCourrier(rowCount.intValue());
						System.out.println("rowcount--JOUR--- if: " + rowCount);
					return rowCount;
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			} else {
			
				vb.setNumRowCourrier(rowCount.intValue());
				System.out.println("rowcount--JOUR--- else: " + rowCount);
				return rowCount;
			}
		}
		return 0;
	}

	@Override
	public CourrierInformations getRowData() {
		if (currentPk == null) {
			return null;
		} else {
			CourrierInformations ret = wrappedData.get(currentPk);
			if (ret == null) {
				wrappedData.put(currentPk, ret);
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
		if(rowIndex == null){
			return 0;
		}
		return rowIndex;
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
		if (currentPk == null) {
			return false;
		} else {
			// return getDataProvider().hasAuctionItemByPk(currentPk);
			return true;
		}
	}

	/**
	 * Unused rudiment from old JSF staff.
	 */
	@Override
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
		// throw new UnsupportedOperationException();
	}

	/**
	 * Unused rudiment from old JSF staff.
	 */
	@Override
	public void setWrappedData(Object data) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This method suppose to produce SerializableDataModel that will be
	 * serialized into View State and used on a post-back. In current
	 * implementation we just mark current model as serialized. In more
	 * complicated cases we may need to transform data to actually serialized
	 * form.
	 */
	public SerializableDataModel getSerializableModel(Range range) {
		if (wrappedKeys != null) {
			return this;
		} else {
			return null;
		}
	}

	/**
	 * This is helper method that is called by framework after model update. In
	 * must delegate actual database update to Data Provider.
	 */
	@Override
	public void update() {
		System.out.println("UPDATE CALL , SO change my Implementation");
	}
	
	public void permitionWalkVisit(ActionEvent evt){
		walkVisited = false;
		countVisited = false;
		scrollerWalkVisit = true;
		courrierConsultation.getVb().setOldPage(false);
	}
	public void permitionWalkScroller(ActionEvent evt) {
		scrollerWalkVisit = false;
		courrierConsultation.getVb().setOldPage(false);
	}
	public void permitionWalk(){
		walkVisited = false;
	}

	// Geeters & Setters ....
	public String getTypeCourrierValidationJour() {
		return typeCourrierValidationJour;
	}

	public void setTypeCourrierValidationJour(String typeCourrierValidationJour) {
		this.typeCourrierValidationJour = typeCourrierValidationJour;
	}

	public String getTransmissionCourrierJour() {
		return transmissionCourrierJour;
	}

	public void setTransmissionCourrierJour(String transmissionCourrierJour) {
		this.transmissionCourrierJour = transmissionCourrierJour;
	}

	public String getTypeCourrierTraitementJour() {
		return typeCourrierTraitementJour;
	}

	public void setTypeCourrierTraitementJour(String typeCourrierTraitementJour) {
		this.typeCourrierTraitementJour = typeCourrierTraitementJour;
	}

	public String getTypeCourrierJour() {
		return typeCourrierJour;
	}

	public void setTypeCourrierJour(String typeCourrierJour) {
		this.typeCourrierJour = typeCourrierJour;
	}

	public boolean isMoreChoicesJour() {
		return moreChoicesJour;
	}

	public void setMoreChoicesJour(boolean moreChoicesJour) {
		this.moreChoicesJour = moreChoicesJour;
	}

	public boolean isShowExecuteAllButtonJour() {
		return showExecuteAllButtonJour;
	}

	public void setShowExecuteAllButtonJour(boolean showExecuteAllButtonJour) {
		this.showExecuteAllButtonJour = showExecuteAllButtonJour;
	}

	public boolean isHideExecuteAllButtonJour() {
		return hideExecuteAllButtonJour;
	}

	public void setHideExecuteAllButtonJour(boolean hideExecuteAllButtonJour) {
		this.hideExecuteAllButtonJour = hideExecuteAllButtonJour;
	}

	public boolean isHideExecuteAllButton() {
		return hideExecuteAllButton;
	}

	public void setHideExecuteAllButton(boolean hideExecuteAllButton) {
		this.hideExecuteAllButton = hideExecuteAllButton;
	}

	public String getCategorieCourrierJour() {
		return categorieCourrierJour;
	}

	public void setCategorieCourrierJour(String categorieCourrierJour) {
		this.categorieCourrierJour = categorieCourrierJour;
	}

	public CourrierConsultationJourBean getCourrierConsultation() {
		return courrierConsultation;
	}

	public void setCourrierConsultation(
			CourrierConsultationJourBean courrierConsultation) {
		this.courrierConsultation = courrierConsultation;
	}

	public Boolean getCheckedTypeCourrierValidationJour() {
	
		return checkedTypeCourrierValidationJour;
	}

	public void setCheckedTypeCourrierValidationJour(
			Boolean checkedTypeCourrierValidationJour) {
		System.out.println("AH =====> checkedTypeCourrierValidationJour variable modifiée");
		this.checkedTypeCourrierValidationJour = checkedTypeCourrierValidationJour;
	}
	public String getCourrierRubriqueJour() {
		return courrierRubriqueJour;
	}

	public void setCourrierRubriqueJour(String courrierRubriqueJour) {
		this.courrierRubriqueJour = courrierRubriqueJour;
	}

	public Integer getTotalCount() {
System.out.println("2020-05-28 : DANS getTotalCount ");
		if (totalCount == null) {
			if (rowCount != null) {
				totalCount = rowCount;
			} else {
				
				totalCount = courrierConsultation.getCountCourrier(filterMap, transmissionCourrierJour,
						typeCourrierTraitementJour, typeCourrierJour, typeCourrierValidationJour,
						categorieCourrierJour, courrierRubriqueJour, false).intValue();
				
			}
		}
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getFirstPage() {
		return firstPage;
	}

	public void setFirstPage(Integer firstPage) {
		this.firstPage = firstPage;
	}

	public void setStlecss(String stlecss) {
		this.stlecss = stlecss;
	}

	public String getStlecss() {
		return stlecss;
	}
}