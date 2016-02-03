package com.github.mikephil.charting.data;

import android.graphics.Color;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class IconPieDataSet extends PieDataSet {
    private List<Pair<Integer, Integer>> mIcons = new ArrayList<>();
    private int mInactiveColor = Color.GRAY;

    public IconPieDataSet(List<Entry> yVals, String label, List<Pair<Integer, Integer>> icons) {
        super(yVals, label);

        mIcons = icons;
    }

    @Override
    public DataSet<Entry> copy() {
        List<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < mYVals.size(); i++) {
            yVals.add(mYVals.get(i).copy());
        }

        IconPieDataSet copied = new IconPieDataSet(yVals, getLabel(), mIcons);
        copied.mColors = mColors;
        copied.setSliceSpace(getSliceSpace());
        copied.setSelectionShift(getSelectionShift());
        copied.mIcons = new ArrayList<>(mIcons);

        return copied;
    }

    public void setInactiveColor(int inactiveColor) {
        mInactiveColor = inactiveColor;
    }

    public int getInactiveColor() {
        return mInactiveColor;
    }

    public List<Pair<Integer, Integer>> getIcons() {
        return mIcons;
    }
}
