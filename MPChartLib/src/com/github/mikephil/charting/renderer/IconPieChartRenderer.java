package com.github.mikephil.charting.renderer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Pair;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.IconPieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.IconPieDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

public class IconPieChartRenderer extends PieChartRenderer {
    private final float mChartInset;
    private final float mRadiusIconDivider;
    protected IconPieChart mChart;

    private final Resources mResources;
    private final PointF mIconSize;
    private HashMap<Integer, WeakReference<Bitmap>> mIcons = new HashMap<>();
    private int mIconXOffset;
    private int mIconYOffset;

    public IconPieChartRenderer(IconPieChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler, Resources resources, PointF iconSize, float chartInset, float radiusIconDivider) {
        super(chart, animator, viewPortHandler);

        mChart = chart;
        mResources = resources;
        mIconSize = iconSize;
        mChartInset = chartInset;
        mRadiusIconDivider = radiusIconDivider;
    }

    @Override
    protected void drawDataSet(Canvas c, IPieDataSet dataSet) {
        drawDataSet(c, (IconPieDataSet) dataSet);
    }

    protected void drawDataSet(Canvas c, IconPieDataSet dataSet) {
        float angle = 0;
        float rotationAngle = mChart.getRotationAngle();

        float[] drawAngles = mChart.getDrawAngles();

        for (int j = 0; j < dataSet.getEntryCount(); j++) {

            float sliceAngle = drawAngles[j];
            float sliceSpace = dataSet.getSliceSpace();

            Entry e = dataSet.getEntryForIndex(j);

            // draw only if the value is greater than zero
            if ((Math.abs(e.getVal()) > 0.000001)) {

                if (mChart.valuesToHighlight() && !mChart.needsHighlight(e.getXIndex(), mChart.getData().getIndexOfDataSet(dataSet))) {
                    mRenderPaint.setColor(dataSet.getInactiveColor());
                }
                else {
                    mRenderPaint.setColor(dataSet.getColor(j));
                }

                RectF rectInner = new RectF(mChart.getCircleBox());
                rectInner.inset(Math.max(mIconSize.x, mIconSize.y)*mChartInset, Math.max(mIconSize.x, mIconSize.y)*mChartInset);

                mBitmapCanvas.drawArc(rectInner,
                        rotationAngle + (angle + sliceSpace / 2f) * mAnimator.getPhaseY(),
                        (sliceAngle - sliceSpace / 2f) * mAnimator.getPhaseY(),
                        true, mRenderPaint);
            }

            angle += sliceAngle * mAnimator.getPhaseX();
        }
    }

    @Override
    public void drawValues(Canvas c) {

        PointF center = mChart.getCenterCircleBox();

        // get whole the radius
        float r = mChart.getRadius();
        float rotationAngle = mChart.getRotationAngle();
        float[] drawAngles = mChart.getDrawAngles();
        float[] absoluteAngles = mChart.getAbsoluteAngles();

        r -= Math.max(mIconSize.x, mIconSize.y) / mRadiusIconDivider;

        PieData data = mChart.getData();
        List<IPieDataSet> dataSets = data.getDataSets();

        boolean drawXVals = mChart.isDrawSliceTextEnabled();

        int cnt = 0;

        for (int i = 0; i < dataSets.size(); i++) {
            IconPieDataSet dataSet = (IconPieDataSet) dataSets.get(i);

            if (!dataSet.isDrawValuesEnabled() && !drawXVals)
                continue;

            // apply the text-styling defined by the DataSet
            applyValueTextStyle(dataSet);

            int entryCount = dataSet.getEntryCount();
            List<Pair<Integer, Integer>> icons = dataSet.getIcons();

            for (int j = 0, maxEntry = Math.min(
                    (int) Math.ceil(entryCount * mAnimator.getPhaseX()), entryCount); j < maxEntry; j++) {

                Pair<Integer, Integer> iconPair = icons.get(j);
                Entry entry = dataSet.getEntryForIndex(j);

                // offset needed to center the drawn text in the slice
                float offset = drawAngles[cnt] / 2;

                float angle = (absoluteAngles[cnt] - offset) * mAnimator.getPhaseY();
                // calculate the text position
                float x = (float) (r
                        * Math.cos(Math.toRadians(rotationAngle + angle))
                        + center.x);
                float y = (float) (r
                        * Math.sin(Math.toRadians(rotationAngle + angle))
                        + center.y);


                Integer icon;
                if (mChart.valuesToHighlight() && !mChart.needsHighlight(entry.getXIndex(), mChart.getData().getIndexOfDataSet(dataSet))) {
                    icon = iconPair.second;
                }
                else {
                    icon = iconPair.first;
                }

                if (icon != 0) {
                    Bitmap bitmap;
                    WeakReference<Bitmap> wr = mIcons.get(icon);
                    if (wr != null) {
                        bitmap = wr.get();

                        if (bitmap == null) {
                            bitmap = BitmapFactory.decodeResource(mResources, icon);
                            mIcons.put(icon, new WeakReference<>(bitmap));

                            if (mIconSize.x > bitmap.getWidth()) {
                                mIconXOffset = (int) ((mIconSize.x - bitmap.getWidth()) / 2);
                            }
                            if (mIconSize.y > bitmap.getHeight()) {
                                mIconYOffset = (int) ((mIconSize.x - bitmap.getHeight()) / 2);
                            }
                        }
                    }
                    else {
                        bitmap = BitmapFactory.decodeResource(mResources, icon);
                        mIcons.put(icon, new WeakReference<>(bitmap));

                        if (mIconSize.x > bitmap.getWidth()) {
                            mIconXOffset = (int) ((mIconSize.x - bitmap.getWidth()) / 2);
                        }
                        if (mIconSize.y > bitmap.getHeight()) {
                            mIconYOffset = (int) ((mIconSize.x - bitmap.getHeight()) / 2);
                        }
                    }

                    c.drawBitmap(bitmap, x - mIconSize.x / 2 + mIconXOffset, y - mIconSize.y / 2 + mIconYOffset, mValuePaint);
                }
                cnt++;
            }
        }
    }

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {

    }
}
