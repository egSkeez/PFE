package xtensus.beans.common.GBO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.ajax4jsf.model.SerializableDataModel;

/**
 * @param <T>
 * @param <U>
 */
public abstract class PaginatingDataModel<T, U> extends SerializableDataModel {
    /** */
    private static final long serialVersionUID = 2954923950179861809L;
    /** */
    protected Integer currentPk;
    /** */
    protected boolean descending = true;
    /** */
    protected String sortField = getDefaultSortField();
    /** */
    protected boolean detached = false;
    /** */
    protected List<Integer> wrappedKeys = new ArrayList<Integer>();
    /** */
    protected Integer rowCount;
    /** */
    protected Map<Integer, T> wrappedData = new HashMap<Integer, T>();

    /**
     * @see org.ajax4jsf.model.ExtendedDataModel#getRowKey()
     */
    @Override
    public Object getRowKey()
    {
        return currentPk;
    }

    /**
     * @see org.ajax4jsf.model.ExtendedDataModel#setRowKey(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setRowKey(final Object key)
    {
        this.currentPk = (Integer) key;
    }

    /**
     * @see org.ajax4jsf.model.SerializableDataModel#update()
     */
    @Override
    public void update()
    {
        if (getSortFieldObject() != null)
        {
            final String newSortField = getSortFieldObject().toString();
            if (newSortField.equals(sortField))
            {
                descending = !descending;
            }
            sortField = newSortField;
        }
        detached = false;
    }

    /**
     * @return Object
     */
    protected Object getSortFieldObject()
    {
        final FacesContext context = FacesContext.getCurrentInstance();
        final Object sortFieldObject = context.getExternalContext().getRequestParameterMap().get("sortField");
        return sortFieldObject;
    }

    /**
     * @param sortField
     */
    public void setSortField(final String sortField)
    {
        if (this.sortField.equals(sortField))
        {
            descending = !descending;
        } else
        {
            this.sortField = sortField;
        }
    }

    /**
     * @return String
     */
    public String getSortField()
    {
        return sortField;
    }

    /**
     * @see org.ajax4jsf.model.ExtendedDataModel#getSerializableModel(org.ajax4jsf.model.Range)
     */
    @Override
    public SerializableDataModel getSerializableModel(final Range range)
    {
        if (wrappedKeys != null)
        {
            detached = true;
            return this;
        }
        return null;
    }

    /**
     * @see javax.faces.model.DataModel#setRowIndex(int)
     */
    @Override
    public void setRowIndex(final int rowIndex)
    {
        throw new UnsupportedOperationException();

    }

    /**
     * @see javax.faces.model.DataModel#setWrappedData(java.lang.Object)
     */
    @Override
    public void setWrappedData(final Object data)
    {
        throw new UnsupportedOperationException();

    }

    /**
     * @see javax.faces.model.DataModel#getRowIndex()
     */
    @Override
    public int getRowIndex()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * @see javax.faces.model.DataModel#getWrappedData()
     */
    @Override
    public Object getWrappedData()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * @see org.ajax4jsf.model.ExtendedDataModel#walk(javax.faces.context.FacesContext,
     *      org.ajax4jsf.model.DataVisitor, org.ajax4jsf.model.Range,
     *      java.lang.Object)
     */
    @Override
    public void walk(final FacesContext context, final DataVisitor visitor, final Range range, final Object argument)
            throws IOException
    {
        final int firstRow = ((SequenceRange) range).getFirstRow();
        System.out.println("    -firstRow     : "+firstRow);
        final int numberOfRows = ((SequenceRange) range).getRows();
        System.out.println("    -numberOfRows : "+numberOfRows);
        if (detached && getSortFieldObject() != null)
        {
            for (final Integer key : wrappedKeys)
            {
                setRowKey(key);
                visitor.process(context, key, argument);
            }
        } else { // if not serialized, than we request data from data
            // provider
            wrappedKeys = new ArrayList<Integer>();
            for (final T object : findObjects(firstRow, numberOfRows, sortField, descending))
            {
                wrappedKeys.add(getId(object));
                wrappedData.put(getId(object), object);
                visitor.process(context, getId(object), argument);
            }
        }
    }

    /**
     * @see javax.faces.model.DataModel#isRowAvailable()
     */
    @Override
    public boolean isRowAvailable()
    {
        if (currentPk == null)
        {
            return false;
        }
        if (wrappedKeys.contains(currentPk))
        {
            return true;
        }
        if (wrappedData.entrySet().contains(currentPk))
        {
            return true;
        }
        try
        {
            if (getObjectById(currentPk) != null)
            {
                return true;
            }
        } catch (final Exception e)
        {

        }
        return false;
    }

    /**
     * @see javax.faces.model.DataModel#getRowData()
     */
    @Override
    public Object getRowData()
    {
    	System.out.println("Dans getRowData");
        if (currentPk == null)
        {
            return null;
        }

        T object = wrappedData.get(currentPk);
        if (object == null)
        {
            object = getObjectById(currentPk);
            wrappedData.put(currentPk, object);
        }
        return object;
    }

    /**
     * @see javax.faces.model.DataModel#getRowCount()
     */
    @Override
    public int getRowCount()
    {
        if (rowCount == null)
        {
            rowCount = getNumRecords();
        }
        System.out.println("Dans getRowCount : "+rowCount);
        return rowCount;
    }

    /**
     *
     * @param object
     * @return U
     */
    public abstract Integer getId(T object);

    /**
     * @param firstRow
     * @param numberOfRows
     * @param sortField
     * @param descending
     * @return List<T>
     */
    public abstract List<T> findObjects(int firstRow, int numberOfRows, String sortField, boolean descending);

    /**
     * @param id
     * @return T
     */
    public abstract T getObjectById(Integer id);

    /**
     * @return String
     */
    public abstract String getDefaultSortField();

    /**
     * @return int
     */
    public abstract int getNumRecords();
}