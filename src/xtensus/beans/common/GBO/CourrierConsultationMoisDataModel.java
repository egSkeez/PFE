package xtensus.beans.common.GBO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.ajax4jsf.model.SerializableDataModel;
import org.richfaces.model.FilterField;
import org.richfaces.model.Modifiable;
import org.richfaces.model.SortField2;

import xtensus.beans.utils.CourrierInformations;

public class CourrierConsultationMoisDataModel extends SerializableDataModel implements Modifiable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1182883001364981495L;
	private Integer currentPk;
	private Map<Integer, CourrierInformations> wrappedData = new HashMap<Integer, CourrierInformations>();
	private List<Integer> wrappedKeys = null;
	private Integer rowCount; // better to buffer row count locally
	private Integer rowIndex;
	private Integer oldFirstRow = 3;
	private Integer totalCount;
	private SequenceRange firstRange;
	private Integer firstPage;
	private boolean scrollerWalkVisit;

	// Beans
	private CourrierConsultationRecentBean courrierConsultation;

	// Filtre recherche
	private String typeCourrierValidationMois;
	private Boolean checkedTypeCourrierValidationMois;
	private String transmissionCourrierMois;
	private String typeCourrierTraitementMois;
	private String categorieCourrierMois;
	private String typeCourrierMois;
	private String courrierRubrique;

	private boolean moreChoicesMois;
	private boolean showExecuteAllButtonMois;
	private boolean hideExecuteAllButtonMois;
	private boolean hideExecuteAllButton;

	// For filter and Sort
	private String sortField;
	private boolean descending;
	private HashMap<String, Object> filterMap = new HashMap<String, Object>();

	// for calling walk Restrictions
	private boolean walkVisited;
	private boolean countVisited;

	@PostConstruct
	public void init() {
		typeCourrierValidationMois = "";
		transmissionCourrierMois = "Tout les courriers";
		typeCourrierTraitementMois = "tous";
		categorieCourrierMois = "T";
		typeCourrierMois = "Tous";
		courrierRubrique = "2";
		moreChoicesMois = false;
		showExecuteAllButtonMois = false;
		hideExecuteAllButtonMois = true;
		hideExecuteAllButton = true;
		checkedTypeCourrierValidationMois = false;
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
	public void walk(FacesContext context, DataVisitor visitor, Range range, Object argument) throws IOException {
		System.out.println("IN CRmois WALK");
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
			if (courrierConsultation.getVb().isOldPage() && courrierConsultation.getVb().getFirstPageMois() != null) {
				firstPage = courrierConsultation.getVb().getFirstPageMois();
				numberOfRows = courrierConsultation.getVb().getNumberOfRows();
				firstRow = (firstPage - 1) * numberOfRows;
			} else {
				firstRow = ((SequenceRange) range).getFirstRow();
				numberOfRows = ((SequenceRange) range).getRows();
			}
		}
		// if (walkVisited == false || firstRow != oldFirstRow || (walkVisited == true && firstRow == 0)) {
		if (courrierConsultation.getVb().getSelectedListCourrier().equals("CRmois")) {
			if (walkVisited == false || firstRow != oldFirstRow) {
				System.out.println("IN MOIS WALK true");
				courrierConsultation.getVb().setFirstRow(firstRow);
				courrierConsultation.getVb().setNumberOfRows(numberOfRows);
				courrierConsultation.getVb().setFilterMap(filterMap);
				courrierConsultation.getVb().setSortField(sortField);
				courrierConsultation.getVb().setDescending(descending);
				courrierConsultation.getVb().setFirstPageMois(firstPage);
				walkVisited = true;
				try {
					oldFirstRow = firstRow;
					wrappedKeys = new ArrayList<Integer>();
					if (checkedTypeCourrierValidationMois) {
						typeCourrierValidationMois = "Avalider";
					} else {
						typeCourrierValidationMois = "";
					}
					
					System.out.println("AH MOIS =======   "+typeCourrierValidationMois);
					for (CourrierInformations item : courrierConsultation.searchListCourrier("mois", filterMap,
							sortField, descending, typeCourrierMois, categorieCourrierMois, transmissionCourrierMois,
							typeCourrierTraitementMois, typeCourrierValidationMois, firstRow, numberOfRows, false,
							courrierRubrique)) {
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

	/**
	 * This method must return actual data rows count from the Data Provider. It
	 * is used by pagination control to determine total number of data items.
	 */

	@Override
	public int getRowCount() {
		if (courrierConsultation.getVb().getSelectedListCourrier().equals("CRmois")) {
			if (rowCount == null || countVisited == false) {
				try {
					countVisited = true;
					// if (rowCount == null) {
					if (checkedTypeCourrierValidationMois) {
						typeCourrierValidationMois = "Avalider";
					} else {
						typeCourrierValidationMois = "";
					}
					rowCount = courrierConsultation.getCountCourrier("mois", filterMap, transmissionCourrierMois,
							typeCourrierTraitementMois, typeCourrierMois, typeCourrierValidationMois,
							categorieCourrierMois, courrierRubrique, false).intValue();
					// System.out.println("rowcount--JOUR--- : " + rowCount);
					// }
					return rowCount;
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			} else {
				return rowCount;
			}
		}
		return 0;
	}

	@Override
	public void modify(List<FilterField> filterFields, List<SortField2> sortFields) {
		filterMap.clear();
		walkVisited = false;
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
		if (rowIndex == null) {
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

	public void permitionWalkVisit(ActionEvent evt) {
		walkVisited = false;
		countVisited = false;
		scrollerWalkVisit = true;
		courrierConsultation.getVb().setOldPage(false);
	}

	public void permitionWalkScroller(ActionEvent evt) {
		scrollerWalkVisit = false;
		courrierConsultation.getVb().setOldPage(false);
	}

	public void permitionWalk() {
		walkVisited = false;
	}

	// Geeters & Setters ....
	public String getTypeCourrierValidationMois() {
		return typeCourrierValidationMois;
	}

	public void setTypeCourrierValidationMois(String typeCourrierValidationMois) {
		System.out.println("AH MOIS=====> typeCourrierValidationMois variable modifiée");
		this.typeCourrierValidationMois = typeCourrierValidationMois;
	}

	public String getTransmissionCourrierMois() {
		return transmissionCourrierMois;
	}

	public void setTransmissionCourrierMois(String transmissionCourrierMois) {
		this.transmissionCourrierMois = transmissionCourrierMois;
	}

	public String getTypeCourrierTraitementMois() {
		return typeCourrierTraitementMois;
	}

	public void setTypeCourrierTraitementMois(String typeCourrierTraitementMois) {
		this.typeCourrierTraitementMois = typeCourrierTraitementMois;
	}

	public String getTypeCourrierMois() {
		return typeCourrierMois;
	}

	public void setTypeCourrierMois(String typeCourrierMois) {
		this.typeCourrierMois = typeCourrierMois;
	}

	public boolean isMoreChoicesMois() {
		return moreChoicesMois;
	}

	public void setMoreChoicesMois(boolean moreChoicesMois) {
		this.moreChoicesMois = moreChoicesMois;
	}

	public boolean isShowExecuteAllButtonMois() {
		return showExecuteAllButtonMois;
	}

	public void setShowExecuteAllButtonMois(boolean showExecuteAllButtonMois) {
		this.showExecuteAllButtonMois = showExecuteAllButtonMois;
	}

	public boolean isHideExecuteAllButtonMois() {
		return hideExecuteAllButtonMois;
	}

	public void setHideExecuteAllButtonMois(boolean hideExecuteAllButtonMois) {
		this.hideExecuteAllButtonMois = hideExecuteAllButtonMois;
	}

	public boolean isHideExecuteAllButton() {
		return hideExecuteAllButton;
	}

	public void setHideExecuteAllButton(boolean hideExecuteAllButton) {
		this.hideExecuteAllButton = hideExecuteAllButton;
	}

	public String getCategorieCourrierMois() {
		return categorieCourrierMois;
	}

	public void setCategorieCourrierMois(String categorieCourrierMois) {
		this.categorieCourrierMois = categorieCourrierMois;
	}

	public CourrierConsultationRecentBean getCourrierConsultation() {
		return courrierConsultation;
	}

	public void setCourrierConsultation(CourrierConsultationRecentBean courrierConsultation) {
		this.courrierConsultation = courrierConsultation;
	}

	public Boolean getCheckedTypeCourrierValidationMois() {
		return checkedTypeCourrierValidationMois;
	}

	public void setCheckedTypeCourrierValidationMois(Boolean checkedTypeCourrierValidationMois) {
		this.checkedTypeCourrierValidationMois = checkedTypeCourrierValidationMois;
	}

	public String getCourrierRubrique() {
		return courrierRubrique;
	}

	public void setCourrierRubrique(String courrierRubrique) {
		this.courrierRubrique = courrierRubrique;
	}

	public Integer getTotalCount() {
		if (totalCount == null) {
			if (rowCount != null) {
				totalCount = rowCount;
			} else {
				totalCount = courrierConsultation.getCountCourrier("mois", filterMap, transmissionCourrierMois,
						typeCourrierTraitementMois, typeCourrierMois, typeCourrierValidationMois,
						categorieCourrierMois, courrierRubrique, false).intValue();
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
}