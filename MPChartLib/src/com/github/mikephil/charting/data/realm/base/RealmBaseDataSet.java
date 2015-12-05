package com.github.mikephil.charting.data.realm.base;

import com.github.mikephil.charting.data.BaseDataSet;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Philipp Jahoda on 06/11/15.
 */
public abstract class RealmBaseDataSet<T extends RealmObject, S extends Entry> extends BaseDataSet<S> {

    /**
     * a list of queried realm objects
     */
    protected RealmResults<T> results;

    /**
     * a cached list of all data read from the database
     */
    protected List<S> mValues;

    /**
     * fieldname of the column that contains the y-values of this dataset
     */
    protected String mValuesField;

    /**
     * fieldname of the column that contains the x-indices of this dataset
     */
    protected String mIndexField;

    /**
     * Constructor that takes the realm RealmResults, sorts & stores them.
     *
     * @param results
     * @param yValuesField
     * @param xIndexField
     */
    public RealmBaseDataSet(RealmResults<T> results, String yValuesField, String xIndexField) {
        this.results = results;
        this.mValuesField = yValuesField;
        this.mIndexField = xIndexField;
        this.mValues = new ArrayList<S>();
        this.results.sort(mIndexField, true);

        build(this.results);
        calcMinMax(0, results.size());
    }

    /**
     * Rebuilds the DataSet based on the given RealmResults.
     */
    public abstract void build(RealmResults<T> results);

    @Override
    public float getYMin() {
        //return results.min(mValuesField).floatValue();
        return -50;
    }

    @Override
    public float getYMax() {
        //return results.max(mValuesField).floatValue();
        return 200;
    }

    @Override
    public int getEntryCount() {
        return mValues.size();
    }

    @Override
    public void calcMinMax(int start, int end) {


    }

    @Override
    public S getEntryForXIndex(int xIndex) {
        //DynamicRealmObject o = new DynamicRealmObject(results.where().equalTo(mIndexField, xIndex).findFirst());
        //return new Entry(o.getFloat(mValuesField), o.getInt(mIndexField));
        return getEntryForXIndex(xIndex, DataSet.Rounding.CLOSEST);
    }

    @Override
    public S getEntryForXIndex(int xIndex, DataSet.Rounding rounding) {
        int index = getEntryIndex(xIndex, rounding);
        if (index > -1)
            return mValues.get(index);
        return null;
    }

    @Override
    public S getEntryForIndex(int index) {
        //DynamicRealmObject o = new DynamicRealmObject(results.get(index));
        //return new Entry(o.getFloat(mValuesField), o.getInt(mIndexField));
        return mValues.get(index);
    }

    @Override
    public int getEntryIndex(int x, DataSet.Rounding rounding) {

        int low = 0;
        int high = mValues.size() - 1;
        int closest = -1;

        while (low <= high) {
            int m = (high + low) / 2;

            if (x == mValues.get(m).getXIndex()) {
                while (m > 0 && mValues.get(m - 1).getXIndex() == x)
                    m--;

                return m;
            }

            if (x > mValues.get(m).getXIndex())
                low = m + 1;
            else
                high = m - 1;

            closest = m;
        }

        if (closest != -1) {
            int closestXIndex = mValues.get(closest).getXIndex();
            if (rounding == DataSet.Rounding.UP) {
                if (closestXIndex < x && closest < mValues.size() - 1) {
                    ++closest;
                }
            } else if (rounding == DataSet.Rounding.DOWN) {
                if (closestXIndex > x && closest > 0) {
                    --closest;
                }
            }
        }

        return closest;
    }

    @Override
    public int getEntryIndex(S e) {
        return mValues.indexOf(e);
    }

    @Override
    public float getYValForXIndex(int xIndex) {
        //return new DynamicRealmObject(results.where().greaterThanOrEqualTo(mIndexField, xIndex).findFirst()).getFloat(mValuesField);
        Entry e = getEntryForXIndex(xIndex);

        if (e != null && e.getXIndex() == xIndex)
            return e.getVal();
        else
            return Float.NaN;
    }

    @Override
    public boolean addEntry(S e) {
        return false;
    }

    @Override
    public boolean removeEntry(S e) {
        return false;
    }

    /**
     * Returns the List of values that has been extracted from the RealmResults
     * using the provided fieldnames.
     *
     * @return
     */
    public List<S> getValues() {
        return mValues;
    }

    /**
     * Clears all values from the DataSet.
     */
    public void clearValues() {
        mValues.clear();
        notifyDataSetChanged();
    }

    public RealmResults<T> getResults() {
        return results;
    }

    public String getValuesField() {
        return mValuesField;
    }

    /**
     * Sets the field name that is used for getting the y-values out of the RealmResultSet.
     *
     * @param yValuesField
     */
    public void setValuesField(String yValuesField) {
        this.mValuesField = yValuesField;
    }

    public String getIndexField() {
        return mIndexField;
    }

    /**
     * Sets the field name that is used for getting the x-indices out of the RealmResultSet.
     *
     * @param xIndexField
     */
    public void setIndexField(String xIndexField) {
        this.mIndexField = xIndexField;
    }
}